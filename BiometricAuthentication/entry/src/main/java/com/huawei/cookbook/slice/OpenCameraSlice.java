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

package com.huawei.cookbook.slice;

import com.huawei.cookbook.ResourceTable;
import com.huawei.cookbook.util.LogUtils;
import com.huawei.cookbook.util.PermissionBridge;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.app.Environment;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.camera.CameraKit;
import ohos.media.camera.device.Camera;
import ohos.media.camera.device.CameraConfig;
import ohos.media.camera.device.CameraStateCallback;
import ohos.media.camera.device.FrameConfig;
import ohos.media.image.ImageReceiver;
import ohos.media.image.common.ImageFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Open Camera Slice
 *
 * @since 2021-04-12
 */
public class OpenCameraSlice extends AbilitySlice implements PermissionBridge.OnPermissionStateListener {
    private static final String TAG = OpenCameraSlice.class.getName();

    private static final int SCREEN_WIDTH = 1080;

    private static final int SCREEN_HEIGHT = 1920;

    private static final int IMAGE_RCV_CAPACITY = 9;

    private static final String IMG_FILE_PREFIX = "IMG_";

    private static final String IMG_FILE_TYPE = ".jpg";

    /**
     * Photographing Restrictions
     */
    private static final int EVENT_LIMIT_PROMTING = 0x0000023;

    /**
     * Photographing Restrictions
     */
    private static final int EVENT_IMAGESAVING_PROMTING = 0x0000024;
    private static final int SLEEP_TIME = 200;

    private EventHandler creamEventHandler;

    private Image takePictureImage;

    private Image exitImage;

    private SurfaceProvider surfaceProvider;

    private Image switchCameraImage;

    private boolean isCameraRear;

    private Camera cameraDevice;

    private ImageReceiver imageReceiver;

    private File targetFile;

    private Surface previewSurface;

    /**
     * EventHandler is used for communication between threads.
     */
    private final EventHandler handler = new EventHandler(EventRunner.current()) {
        @Override
        protected void processEvent(InnerEvent event) {
            switch (event.eventId) {
                case EVENT_LIMIT_PROMTING:
                    showTips(OpenCameraSlice.this, "The maximum number of consecutive clicks is 9.");
                    return;
                case EVENT_IMAGESAVING_PROMTING:
                    showTips(OpenCameraSlice.this, "Take picture success,save path:" + targetFile.getPath());
                    return;
                default:
            }
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_open_camera);
        new PermissionBridge().setOnPermissionStateListener(this);
    }

    private void initSurface() {
        surfaceProvider = new SurfaceProvider(this);
        DirectionalLayout.LayoutConfig params = new DirectionalLayout.LayoutConfig(
                ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);
        surfaceProvider.setLayoutConfig(params);
        surfaceProvider.pinToZTop(false);
        // Add SurfaceCallBack callback
        boolean isPresent = surfaceProvider.getSurfaceOps().isPresent();
        if (isPresent) {
            surfaceProvider.getSurfaceOps().get().addCallback(new SurfaceCallBack());
        }
        // Add SurfaceProvider to Layout
        Component component = findComponentById(ResourceTable.Id_surface_container);
        if (component instanceof ComponentContainer) {
            ((ComponentContainer) component).addComponent(surfaceProvider);
        }
    }

    private void initControlComponents() {
        // Start photographing icon
        Component takePictureCom = findComponentById(ResourceTable.Id_tack_picture_btn);
        if (takePictureCom instanceof Image) {
            takePictureImage = (Image) takePictureCom;
            takePictureImage.setClickedListener(this::takePicture);
        }
        // Icon for exiting the photographing page
        Component exitImageCom = findComponentById(ResourceTable.Id_exit);
        if (exitImageCom instanceof Image) {
            exitImage = (Image) exitImageCom;
            exitImage.setClickedListener(component -> terminate());
        }
        // Switch between front and rear cameras
        Component switchCameraImageCom = findComponentById(ResourceTable.Id_switch_camera_btn);
        if (switchCameraImageCom instanceof Image) {
            switchCameraImage = (Image) switchCameraImageCom;
            switchCameraImage.setClickedListener(component -> switchClicked());
        }
    }

    private void switchClicked() {
        if (!takePictureImage.isEnabled()) {
            return;
        }
        takePictureImage.setEnabled(false);
        isCameraRear = !isCameraRear;
        openCamera();
    }

    private void takePicture(Component con) {
        if (!takePictureImage.isEnabled()) {
            return;
        }
        if (cameraDevice == null || imageReceiver == null) {
            return;
        }
        // Camera Configuration
        FrameConfig.Builder framePictureConfigBuilder
                = cameraDevice.getFrameConfigBuilder(Camera.FrameConfigType.FRAME_CONFIG_PICTURE);
        framePictureConfigBuilder.addSurface(imageReceiver.getRecevingSurface());
        FrameConfig pictureFrameConfig = framePictureConfigBuilder.build();
        // Turn on image capture
        cameraDevice.triggerSingleCapture(pictureFrameConfig);
    }

    private void openCamera() {
        // Photo Acceptance
        imageReceiver = ImageReceiver.create(SCREEN_WIDTH, SCREEN_HEIGHT, ImageFormat.JPEG, IMAGE_RCV_CAPACITY);
        // Setting Graph Arrival Listening
        imageReceiver.setImageArrivalListener(this::saveImage);
        CameraKit cameraKit = CameraKit.getInstance(getApplicationContext());
        String[] cameraLists = cameraKit.getCameraIds();
        String cameraId = cameraLists.length > 1 && isCameraRear ? cameraLists[1] : cameraLists[0];
        CameraStateCallbackImpl cameraStateCallback = new CameraStateCallbackImpl();
        cameraKit.createCamera(cameraId, cameraStateCallback, creamEventHandler);
    }

    private void saveImage(ImageReceiver receiver) {
        // Name of the photo to be taken
        String fileName = IMG_FILE_PREFIX + UUID.randomUUID() + IMG_FILE_TYPE;
        // Read file
        ohos.media.image.Image image = receiver.readNextImage();
        if (image == null) {
            handler.sendEvent(EVENT_LIMIT_PROMTING);
            return;
        }
        ohos.media.image.Image.Component component = image.getComponent(ImageFormat.ComponentType.JPEG);
        byte[] bytes = new byte[component.remaining()];
        component.read(bytes);
        FileOutputStream output = null;
        try {
            // Generate a photographing file.
            targetFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getCanonicalPath(), fileName);
            output = new FileOutputStream(targetFile);
            // Storing photo files
            output.write(bytes);
            output.flush();
            handler.sendEvent(EVENT_IMAGESAVING_PROMTING);
        } catch (IOException e) {
            LogUtils.error(TAG, "=====================Save image failed");
        } finally {
            try {
                assert output != null;
                output.close();
            } catch (IOException e) {
                LogUtils.error("saveImage-close output", "close output error");
            }
        }
    }

    private void showTips(Context context, String message) {
        getUITaskDispatcher().asyncDispatch(() -> {
            ToastDialog toastDialog = new ToastDialog(context);
            toastDialog.setAutoClosable(false);
            toastDialog.setContentText(message);
            toastDialog.show();
        });
    }

    @Override
    public void onPermissionGranted() {
        getWindow().setTransparent(true);
        initSurface();
        initControlComponents();
        creamEventHandler = new EventHandler(EventRunner.create("======CameraBackground"));
    }

    @Override
    public void onPermissionDenied() {
        showTips(OpenCameraSlice.this, "=======No permission");
    }

    /**
     * CameraStateCallbackImpl
     *
     * @since 2020-09-03
     */
    class CameraStateCallbackImpl extends CameraStateCallback {
        CameraStateCallbackImpl() {
        }

        @Override
        public void onCreated(Camera camera) {
            // Get Preview
            boolean isPresent = surfaceProvider.getSurfaceOps().isPresent();
            if (isPresent) {
                previewSurface = surfaceProvider.getSurfaceOps().get().getSurface();
            }
            if (previewSurface == null) {
                LogUtils.error(TAG, "create camera filed, preview surface is null");
                return;
            }
            // Wait until the preview surface is created.
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException exception) {
                LogUtils.warn(TAG, "Waiting to be interrupted");
            }
            CameraConfig.Builder cameraConfigBuilder = camera.getCameraConfigBuilder();
            // Configuration Preview
            cameraConfigBuilder.addSurface(previewSurface);
            cameraConfigBuilder.addSurface(imageReceiver.getRecevingSurface());
            camera.configure(cameraConfigBuilder.build());
            cameraDevice = camera;
            enableImageGroup();
            takePictureImage.setEnabled(true);
        }

        @Override
        public void onConfigured(Camera camera) {
            FrameConfig.Builder framePreviewConfigBuilder
                    = camera.getFrameConfigBuilder(Camera.FrameConfigType.FRAME_CONFIG_PREVIEW);
            framePreviewConfigBuilder.addSurface(previewSurface);
            // Turn on loop capture
            camera.triggerLoopingCapture(framePreviewConfigBuilder.build());
        }

        private void enableImageGroup() {
            if (!exitImage.isEnabled()) {
                exitImage.setEnabled(true);
                takePictureImage.setEnabled(true);
                switchCameraImage.setEnabled(true);
            }
        }
    }

    /**
     * SurfaceCallBack
     *
     * @since 2020-09-03
     */
    class SurfaceCallBack implements SurfaceOps.Callback {
        @Override
        public void surfaceCreated(SurfaceOps callbackSurfaceOps) {
            if (callbackSurfaceOps != null) {
                callbackSurfaceOps.setFixedSize(SCREEN_HEIGHT, SCREEN_WIDTH);
            }
            openCamera();
        }

        @Override
        public void surfaceChanged(SurfaceOps callbackSurfaceOps, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceOps callbackSurfaceOps) {
        }
    }

    @Override
    public void onStop() {
        cameraDevice.release();
    }
}
