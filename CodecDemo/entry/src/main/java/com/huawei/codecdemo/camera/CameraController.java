/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.codecdemo.camera;

import com.huawei.codecdemo.camera.api.CameraListener;
import com.huawei.codecdemo.camera.api.CameraStateListener;
import com.huawei.codecdemo.camera.constant.CameraConst;
import com.huawei.codecdemo.camera.constant.CameraType;
import com.huawei.codecdemo.camera.constant.CaptureMode;
import com.huawei.codecdemo.camera.utils.Nv21Handler;
import com.huawei.codecdemo.media.VideoRecorder;
import com.huawei.codecdemo.media.constant.MediaStatus;
import com.huawei.codecdemo.utils.LogUtil;

import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.app.Context;
import ohos.app.Environment;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.camera.CameraKit;
import ohos.media.camera.device.Camera;
import ohos.media.camera.device.CameraAbility;
import ohos.media.camera.device.CameraConfig;
import ohos.media.camera.device.CameraStateCallback;
import ohos.media.camera.device.FrameConfig;
import ohos.media.camera.params.ParameterKey;
import ohos.media.image.Image;
import ohos.media.image.ImageReceiver;
import ohos.media.image.common.ImageFormat;
import ohos.media.image.common.Size;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CameraController
 *
 * @since 2021-04-09
 */
public class CameraController implements CameraListener {
    private static final String TAG = CameraController.class.getName();
    private static final int NUMBER_INT_2 = 2;
    private static final int NUMBER_INT_3 = 3;
    private static final int NUMBER_INT_5 = 5;
    private static final int NUMBER_INT_90 = 90;
    private static final int NUMBER_INT_270 = 270;
    private final Context context;
    private CameraAbility cameraSupporter;
    private Camera cameraDevice;
    private CameraConfig.Builder cameraConfigBuilder;
    private FrameConfig.Builder frameConfigBuilder;
    private ImageReceiver imageReceiver;
    private final EventHandler eventHandler;

    private boolean isCameraCreated;
    private boolean isPrepared;
    private boolean isCapturing;
    private boolean isMirror;
    private Surface recorderSurface;
    private Surface previewSurface;
    private int cameraType;
    private int previewWidth;
    private int previewHeight;
    private int resoluteX;
    private int resoluteY;

    private CaptureMode captureMode = CaptureMode.SINGLE_SHOT;
    private VideoRecorder videoRecorder;
    private CameraStateListener cameraCallback;

    /**
     * CameraListenerController
     *
     * @param context context
     * @param isFrontCamera isFrontCamera
     */
    public CameraController(Context context, boolean isFrontCamera) {
        this.context = context;
        this.cameraType = isFrontCamera ? CameraType.FRONT.getType() : CameraType.BACK.getType();
        eventHandler = new EventHandler(EventRunner.current());
    }

    @Override
    public void bind(SurfaceProvider surfaceProvider) {
        if (surfaceProvider.getSurfaceOps().isPresent()) {
            this.previewSurface = surfaceProvider.getSurfaceOps().get().getSurface();
            this.previewWidth = surfaceProvider.getWidth();
            this.previewHeight = surfaceProvider.getHeight();
            context.getUITaskDispatcher().asyncDispatch(this::cameraInit);
        }
    }

    private void cameraInit() {
        isPrepared = false;
        CameraKit camerakit = CameraKit.getInstance(context.getApplicationContext());
        if (camerakit != null && camerakit.getCameraIds().length > cameraType) {
            String cameraId = camerakit.getCameraIds()[cameraType];
            cameraSupporter = camerakit.getCameraAbility(cameraId);
            CameraStateCallback cameraStateCallback = new MyCameraStatusCallback();
            camerakit.createCamera(cameraId, cameraStateCallback, eventHandler);
            captureInit();
        }
    }

    private void captureInit() {
        setResolution();
        int imageFormat = ImageFormat.JPEG;
        switch (captureMode) {
            case PUSH_FLOW:
                imageFormat = ImageFormat.YUV420_888;
                break;
            case VIDEO_RECORD:
                recorderInit();
                break;
            default:
                break;
        }
        receiverInit(imageFormat);
    }

    private void receiverInit(int imageFormat) {
        imageReceiver =
                ImageReceiver.create(
                        Math.max(resoluteX, resoluteY),
                        Math.min(resoluteX, resoluteY),
                        imageFormat,
                        CameraConst.IMAGE_RCV_CAPACITY);
        imageReceiver.setImageArrivalListener(new MyImageArrivalListener());
    }

    private void recorderInit() {
        VideoRecorder.Builder builder = new VideoRecorder.Builder(context);
        builder.setResolution(new Size(resoluteX, resoluteY));
        builder.setDegrees(isFrontCamera() ? NUMBER_INT_270 : NUMBER_INT_90);
        String child = CameraConst.VIDEO_FILE_PREFIX + UUID.randomUUID() + CameraConst.VIDEO_FILE_TYPE;
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), child);
        builder.setSaveFilePath(file.getPath());
        if (videoRecorder == null) {
            videoRecorder = builder.create();
            videoRecorder.prepare();
        } else {
            videoRecorder.reset(builder);
        }
    }

    // 选择最合适分辨率
    private void setResolution() {
        List<Size> resolutions = getResolutions();
        if (resolutions != null) {
            LogUtil.info(TAG, "takePictureInit pictureSizes :" + Arrays.toString(resolutions.toArray()));
            double baseResolution =
                    (double) Math.max(previewHeight, previewWidth) / Math.min(previewHeight, previewWidth);
            double minDeference = baseResolution;
            for (Size resolution : resolutions) {
                double resolutionTemp = (double) resolution.width / resolution.height;
                double difference = Math.abs(baseResolution - resolutionTemp);
                if (difference < minDeference) {
                    minDeference = difference;
                    resoluteX = resolution.width;
                    resoluteY = resolution.height;
                }
            }
        }
    }

    /**
     * MyCameraStatusCallback
     *
     * @since 2020-04-09
     */
    private class MyCameraStatusCallback extends CameraStateCallback {
        @Override
        public void onCreated(Camera camera) {
            super.onCreated(camera);
            isCameraCreated = true;
            cameraDevice = camera;
            cameraConfigBuilder = camera.getCameraConfigBuilder();
            if (cameraConfigBuilder == null) {
                return;
            }
            CameraConfig cameraConfig =
                    cameraConfigBuilder
                            .addSurface(previewSurface) // 配置预览的Surface
                            .addSurface(imageReceiver.getRecevingSurface()) // 配置拍照的Surface
                            .build();
            camera.configure(cameraConfig);
        }

        @Override
        public void onCreateFailed(String cameraId, int errorCode) {
            super.onCreateFailed(cameraId, errorCode);
            isCameraCreated = false;
        }

        @Override
        public void onConfigured(Camera camera) {
            isPrepared = true;
            frameConfigBuilder = camera.getFrameConfigBuilder(Camera.FrameConfigType.FRAME_CONFIG_PREVIEW);
            frameConfigBuilder.addSurface(previewSurface);
            if (isRecording() && recorderSurface != null) {
                frameConfigBuilder.addSurface(recorderSurface);
                videoRecorder.start();
            }
            camera.triggerLoopingCapture(frameConfigBuilder.build());
            if (cameraCallback != null) {
                cameraCallback.onCameraConfigured(CameraController.this);
            }
        }

        @Override
        public void onConfigureFailed(Camera camera, int errorCode) {
            isCameraCreated = false;
        }

        @Override
        public void onReleased(Camera camera) {
            isCameraCreated = false;
            isPrepared = false;
            stopCapture();
            if (videoRecorder != null) {
                videoRecorder.release();
            }
            if (cameraCallback != null) {
                cameraCallback.onCameraReleased();
            }
        }

        @Override
        public void onFatalError(Camera camera, int errorCode) {
            super.onFatalError(camera, errorCode);
            isCameraCreated = false;
            isPrepared = false;
        }
    }

    /**
     * MyImageArrivalListener
     *
     * @since 2020-04-09
     */
    private class MyImageArrivalListener implements ImageReceiver.IImageArrivalListener {
        private static final int BUFFER_NUM = 5;

        private final List<byte[]> buffers = new ArrayList<>(NUMBER_INT_5);

        private int index;

        MyImageArrivalListener() {
            for (int i = 0; i < BUFFER_NUM; i++) {
                buffers.add(new byte[resoluteX * resoluteY * NUMBER_INT_3 / NUMBER_INT_2]);
            }
        }

        @Override
        public void onImageArrival(ImageReceiver receiver) {
            LogUtil.info(TAG, "onImageArrival is called");
            byte[] bytes;
            Image image = imageReceiver.readNextImage();
            if (captureMode == CaptureMode.PUSH_FLOW) {
                bytes = getBuffer();
                Image.Component component = image.getComponent(ImageFormat.ComponentType.YUV_Y);
                component.read(bytes, 0, component.remaining());
                component = image.getComponent(ImageFormat.ComponentType.YUV_U);
                ByteBuffer mBuffer = component.getBuffer();
                mBuffer.get(
                        bytes,
                        resoluteX * resoluteY,
                        Math.min(mBuffer.remaining(), resoluteX * resoluteY / NUMBER_INT_2));
                if (isFrontCamera()) {
                    bytes = isMirror
                            ? Nv21Handler.rotateYuvDegree270AndMirror(bytes, resoluteX, resoluteY)
                            : Nv21Handler.rotateYuv420Degree270(bytes, resoluteX, resoluteY);
                } else {
                    bytes = Nv21Handler.rotateYuv420Degree90(bytes, resoluteX, resoluteY);
                }
            } else {
                Image.Component component = image.getComponent(ImageFormat.ComponentType.JPEG);
                bytes = new byte[component.remaining()];
                component.read(bytes);
            }
            image.release();
            if (cameraCallback != null) {
                cameraCallback.onGetFrameResult(bytes);
            }
        }

        private byte[] getBuffer() {
            index++;
            if (index == BUFFER_NUM) {
                index = 0;
            }
            return buffers.get(index);
        }
    }

    private void takeSinglePhoto() {
        frameConfigBuilder = cameraDevice.getFrameConfigBuilder(Camera.FrameConfigType.FRAME_CONFIG_PICTURE);
        if (isMirror) {
            frameConfigBuilder.setParameter(ParameterKey.IMAGE_MIRROR, true);
        }
        frameConfigBuilder.addSurface(imageReceiver.getRecevingSurface());
        frameConfigBuilder.setImageRotation(isFrontCamera() ? NUMBER_INT_270 : NUMBER_INT_90);
        try {
            cameraDevice.triggerSingleCapture(frameConfigBuilder.build());
        } catch (IllegalArgumentException e) {
            LogUtil.info(TAG, "takeSinglePhoto is failed," + e.getMessage());
        }
    }

    private void takeMultiPhoto() {
        frameConfigBuilder = cameraDevice.getFrameConfigBuilder(Camera.FrameConfigType.FRAME_CONFIG_PICTURE);
        if (isMirror) {
            frameConfigBuilder.setParameter(ParameterKey.IMAGE_MIRROR, true);
        }
        frameConfigBuilder.addSurface(imageReceiver.getRecevingSurface());
        frameConfigBuilder.setImageRotation(isFrontCamera() ? NUMBER_INT_270 : NUMBER_INT_90);
        try {
            cameraDevice.triggerLoopingCapture(frameConfigBuilder.build());
        } catch (IllegalArgumentException e) {
            LogUtil.info(TAG, "takeMultiPhoto is failed," + e.getMessage());
        }
    }

    private void pushFlow() {
        frameConfigBuilder.addSurface(imageReceiver.getRecevingSurface());
        try {
            cameraDevice.triggerLoopingCapture(frameConfigBuilder.build());
        } catch (IllegalArgumentException e) {
            LogUtil.info(TAG, "pushFlow is failed," + e.getMessage());
        }
    }

    private void startRecord() {
        if (!isRecording() && videoRecorder.getStatus() != MediaStatus.IDEL) {
            if (videoRecorder.getStatus() == MediaStatus.STOP) {
                recorderInit();
            }
            Optional<Surface> optional = videoRecorder.getRecordSurface();
            optional.ifPresent(surface -> {
                recorderSurface = optional.get();
                cameraConfigBuilder.addSurface(previewSurface);
                cameraConfigBuilder.addSurface(recorderSurface);
                cameraDevice.configure(cameraConfigBuilder.build());
            });
        }
    }

    @Override
    public void setMode(CaptureMode mode) {
        captureMode = mode;
    }

    @Override
    public void switchCamera(CameraType type) {
        if (isPrepared) {
            cameraType = type.getType();
            cameraInit();
        }
    }

    @Override
    public void setMirrorEffect(boolean isMirrorEffect) {
        this.isMirror = isMirrorEffect;
    }

    @Override
    public void capture() {
        if (isCameraCreated) {
            isCapturing = true;
            switch (captureMode) {
                case SINGLE_SHOT:
                    takeSinglePhoto();
                    break;
                case MULTI_SHOT:
                    takeMultiPhoto();
                    break;
                case VIDEO_RECORD:
                    startRecord();
                    break;
                case PUSH_FLOW:
                    pushFlow();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void stopCapture() {
        if (isCameraCreated && isCapturing) {
            isCapturing = false;
            if (captureMode == CaptureMode.VIDEO_RECORD) {
                videoRecorder.stop();
                if (recorderSurface != null) {
                    cameraConfigBuilder.removeSurface(recorderSurface);
                    recorderSurface = null;
                }
            }
            cameraDevice.configure(cameraConfigBuilder.build());
        }
    }

    private boolean isRecording() {
        return isCapturing && captureMode == CaptureMode.VIDEO_RECORD;
    }

    @Override
    public boolean isCapturing() {
        return isCapturing;
    }

    @Override
    public boolean isFrontCamera() {
        return cameraType == CameraType.FRONT.getType();
    }

    @Override
    public Size getResolution() {
        return new Size(resoluteX, resoluteY);
    }

    @Override
    public List<Size> getResolutions() {
        List<Size> sizes = null;
        if (cameraSupporter != null) {
            sizes = cameraSupporter.getSupportedSizes(SurfaceOps.class);
        }
        return sizes;
    }

    @Override
    public void setCameraListener(CameraStateListener listener) {
        cameraCallback = listener;
    }

    @Override
    public void unBind() {
        if (cameraDevice != null) {
            cameraDevice.release();
        }
    }
}
