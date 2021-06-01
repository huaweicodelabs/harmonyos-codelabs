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

package com.huawei.camerademo.slice;

import static com.huawei.camerademo.utils.CameraUtil.CAMERA_IMAGE_FILE_PATH;
import static ohos.media.camera.device.Camera.FrameConfigType.FRAME_CONFIG_PICTURE;
import static ohos.media.camera.device.Camera.FrameConfigType.FRAME_CONFIG_PREVIEW;

import com.huawei.camerademo.ImageAbility;
import com.huawei.camerademo.ResourceTable;
import com.huawei.camerademo.listener.MyAsrListener;
import com.huawei.camerademo.utils.CameraUtil;
import com.huawei.camerademo.utils.DistributeFileUtil;
import com.huawei.camerademo.utils.MyDrawTask;
import com.huawei.camerademo.utils.PermissionBridge;

import com.alibaba.fastjson.JSONObject;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.ai.asr.AsrClient;
import ohos.ai.asr.AsrIntent;
import ohos.ai.asr.AsrListener;
import ohos.ai.asr.util.AsrResultKey;
import ohos.app.Environment;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.audio.AudioCapturer;
import ohos.media.audio.AudioCapturerInfo;
import ohos.media.audio.AudioStreamInfo;
import ohos.media.camera.CameraKit;
import ohos.media.camera.device.Camera;
import ohos.media.camera.device.CameraConfig;
import ohos.media.camera.device.CameraStateCallback;
import ohos.media.camera.device.FrameConfig;
import ohos.media.image.ImageReceiver;
import ohos.media.image.common.ImageFormat;
import ohos.utils.PacMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * MainAbilitySlice
 *
 * @since 2021-03-08
 */
public class MainAbilitySlice extends AbilitySlice implements PermissionBridge.OnPermissionStateListener {
    private static final HiLogLabel TAG = new HiLogLabel(3, 0xD001100, "MainAbilitySlice");

    private static final int EVENT_IMAGESAVING_PROMTING = 0x0000024;

    private static final int POOL_SIZE = 3;
    private static final int ALIVE_TIME = 3;
    private static final int CAPACITY = 6;
    private static final int SLEEP_TIME = 200;
    private static final int BYTES_LENGTH = 1280;
    private static final int VAD_END_WAIT_MS = 2000;
    private static final int VAD_FRONT_WAIT_MS = 4800;
    private static final int SAMPLE_RATE = 16000;
    private static final int TIMEOUT_DURATION = 20000;

    private static final int SCREEN_WIDTH = 1080;

    private static final int SCREEN_HEIGHT = 2340;

    private static final int IMAGE_RCV_CAPACITY = 9;

    private static final String IMG_FILE_PREFIX = "IMG_";

    private static final String IMG_FILE_TYPE = ".png";

    private static final Map<String, Boolean> COMMAND_MAP = new HashMap<>();

    private static AsrClient asrClient;

    static {
        COMMAND_MAP.put("拍照", true);
        COMMAND_MAP.put("茄子", true);
    }

    private EventHandler handler = new EventHandler(EventRunner.current()) {
        @Override
        protected void processEvent(InnerEvent event) {
            switch (event.eventId) {
                case EVENT_IMAGESAVING_PROMTING:
                    HiLog.info(TAG, "EVENT_IMAGESAVING_PROMTING");
                    MyDrawTask drawTask = new MyDrawTask(smallImage);
                    smallImage.addDrawTask(drawTask);
                    drawTask.putPixelMap(CameraUtil.getPixelMap(bytes, "", 1));
                    smallImage.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    private Surface previewSurface;

    private SurfaceProvider surfaceProvider;

    private boolean isCameraRear;

    private Camera cameraDevice;

    private EventHandler creamEventHandler;

    private ImageReceiver imageReceiver;

    private Image takePictureImage;

    private Image switchCameraImage;

    private File targetFile;

    private Image smallImage;

    private byte[] bytes;

    private boolean isRecord = false;

    private ThreadPoolExecutor poolExecutor;

    private AudioCapturer audioCapturer;

    private boolean recognizeOver;

    private String fileName;

    @Override
    public void onStart(Intent intent) {
        HiLog.info(TAG, "onStart");
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        new PermissionBridge().setOnPermissionStateListener(this);
    }

    private void initSurface() {
        surfaceProvider = new SurfaceProvider(this);
        DirectionalLayout.LayoutConfig params =
                new DirectionalLayout.LayoutConfig(
                        ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);

        surfaceProvider.setLayoutConfig(params);
        surfaceProvider.pinToZTop(true);

        surfaceProvider.getSurfaceOps().get().addCallback(new SurfaceCallBack());
        Component surfaceContainer = findComponentById(ResourceTable.Id_surface_container);
        if (surfaceContainer instanceof ComponentContainer) {
            ((ComponentContainer) surfaceContainer).addComponent(surfaceProvider);
        }
    }

    private void initControlComponents() {
        if (findComponentById(ResourceTable.Id_tack_picture_btn) instanceof Image) {
            takePictureImage = (Image) findComponentById(ResourceTable.Id_tack_picture_btn);
        }
        takePictureImage.setClickedListener(this::takePicture);

        if (findComponentById(ResourceTable.Id_switch_camera_btn) instanceof Image) {
            switchCameraImage = (Image) findComponentById(ResourceTable.Id_switch_camera_btn);
            MyDrawTask drawTask = new MyDrawTask(switchCameraImage);
            switchCameraImage.addDrawTask(drawTask);
        }
        switchCameraImage.setClickedListener(component -> switchClicked());

        if (findComponentById(ResourceTable.Id_small_pic) instanceof Image) {
            smallImage = (Image) findComponentById(ResourceTable.Id_small_pic);
        }
        smallImage.setClickedListener(component -> showBigPic());
    }

    private void openAudio() {
        HiLog.info(TAG, "openAudio");

        if (!isRecord) {
            asrClient.startListening(setStartIntent());
            isRecord = true;
            poolExecutor.submit(new AudioCaptureRunnable());
        }
    }

    private void takePicture(Component component) {
        HiLog.info(TAG, "takePicture");
        if (!takePictureImage.isEnabled()) {
            HiLog.info(TAG, "takePicture return");
            return;
        }
        if (cameraDevice == null || imageReceiver == null) {
            return;
        }
        FrameConfig.Builder framePictureConfigBuilder = cameraDevice.getFrameConfigBuilder(FRAME_CONFIG_PICTURE);
        framePictureConfigBuilder.addSurface(imageReceiver.getRecevingSurface());
        FrameConfig pictureFrameConfig = framePictureConfigBuilder.build();
        cameraDevice.triggerSingleCapture(pictureFrameConfig);
    }

    private void switchClicked() {
        if (!takePictureImage.isEnabled()) {
            return;
        }
        takePictureImage.setEnabled(false);
        isCameraRear = !isCameraRear;
        CameraUtil.setIsCameraRear(isCameraRear);
        openCamera();
    }

    private void openCamera() {
        imageReceiver = ImageReceiver.create(SCREEN_WIDTH, SCREEN_HEIGHT, ImageFormat.JPEG, IMAGE_RCV_CAPACITY);
        imageReceiver.setImageArrivalListener(this::saveImage);

        CameraKit cameraKit = CameraKit.getInstance(getApplicationContext());
        String[] cameraList = cameraKit.getCameraIds();
        String cameraId = cameraList.length > 1 && isCameraRear ? cameraList[1] : cameraList[0];
        CameraStateCallbackImpl cameraStateCallback = new CameraStateCallbackImpl();
        cameraKit.createCamera(cameraId, cameraStateCallback, creamEventHandler);
    }

    private void saveImage(ImageReceiver receiver) {
        fileName = IMG_FILE_PREFIX + System.currentTimeMillis() + IMG_FILE_TYPE;
        targetFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
        try {
            HiLog.info(TAG, "filePath is " + targetFile.getCanonicalPath());
        } catch (IOException e) {
            HiLog.error(TAG, "filePath is error");
        }

        ohos.media.image.Image image = receiver.readNextImage();
        if (image == null) {
            return;
        }
        ohos.media.image.Image.Component component = image.getComponent(ImageFormat.ComponentType.JPEG);
        bytes = new byte[component.remaining()];
        component.read(bytes);
        try (FileOutputStream output = new FileOutputStream(targetFile)) {
            output.write(bytes);
            output.flush();
            handler.sendEvent(EVENT_IMAGESAVING_PROMTING);
            DistributeFileUtil.copyPicToDistributedDir(MainAbilitySlice.this, targetFile, fileName);
        } catch (IOException e) {
            HiLog.info(TAG, "IOException, Save image failed");
        }
    }

    private void releaseCamera() {
        if (cameraDevice != null) {
            cameraDevice.release();
            cameraDevice = null;
        }

        if (imageReceiver != null) {
            imageReceiver.release();
            imageReceiver = null;
        }

        if (creamEventHandler != null) {
            creamEventHandler.removeAllEvent();
            creamEventHandler = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    @Override
    public void onPermissionGranted() {
        getWindow().setTransparent(true);
        initSurface();
        initAudioCapturer();
        initControlComponents();
        initAsrClient();
        creamEventHandler = new EventHandler(EventRunner.create("CameraBackground"));
    }

    private void initAudioCapturer() {
        poolExecutor =
                new ThreadPoolExecutor(
                        POOL_SIZE,
                        POOL_SIZE,
                        ALIVE_TIME,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(CAPACITY),
                        new ThreadPoolExecutor.DiscardOldestPolicy());

        AudioStreamInfo audioStreamInfo =
                new AudioStreamInfo.Builder()
                        .encodingFormat(AudioStreamInfo.EncodingFormat.ENCODING_PCM_16BIT)
                        .channelMask(AudioStreamInfo.ChannelMask.CHANNEL_IN_MONO)
                        .sampleRate(SAMPLE_RATE)
                        .build();

        AudioCapturerInfo audioCapturerInfo = new AudioCapturerInfo.Builder().audioStreamInfo(audioStreamInfo).build();

        audioCapturer = new AudioCapturer(audioCapturerInfo);
    }

    private boolean recognizeWords(String result) {
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject resultObject = new JSONObject();
        if (jsonObject.getJSONArray("result").get(0) instanceof JSONObject) {
            resultObject = (JSONObject) jsonObject.getJSONArray("result").get(0);
        }
        String resultWord = resultObject.getString("ori_word").replace(" ", "");
        boolean command = COMMAND_MAP.getOrDefault(resultWord, false);
        HiLog.info(TAG, "======" + resultWord + "===" + command);
        return command;
    }

    private void initListener() {
        AsrListener asrListener = new MyAsrListener() {
            @Override
            public void onInit(PacMap params) {
                super.onInit(params);
                openAudio();
                HiLog.info(TAG, "======onInit======");
            }

            @Override
            public void onError(int error) {
                super.onError(error);
                HiLog.info(TAG, "======error:" + error);
            }

            @Override
            public void onIntermediateResults(PacMap pacMap) {
                super.onIntermediateResults(pacMap);
                HiLog.info(TAG, "======onIntermediateResults:");
                String result = pacMap.getString(AsrResultKey.RESULTS_INTERMEDIATE);
                boolean recognizeResult = recognizeWords(result);

                if (recognizeResult && !recognizeOver) {
                    recognizeOver = true;
                    takePicture(new Component(getContext()));
                    asrClient.stopListening();
                }
            }

            @Override
            public void onResults(PacMap results) {
                super.onResults(results);
                HiLog.info(TAG, "======onResults:");

                recognizeOver = false;
                asrClient.startListening(setStartIntent());
            }

            @Override
            public void onEnd() {
                super.onEnd();
                HiLog.info(TAG, "======onEnd:");
                recognizeOver = false;
                asrClient.stopListening();
                asrClient.startListening(setStartIntent());
            }
        };

        // Initializes asr.
        if (asrClient != null) {
            asrClient.init(setInitIntent(), asrListener);
        }
    }

    private void initAsrClient() {
        asrClient = AsrClient.createAsrClient(this).orElse(null);
        TaskDispatcher taskDispatcher = getAbility().getMainTaskDispatcher();
        taskDispatcher.asyncDispatch(
                new Runnable() {
                    @Override
                    public void run() {
                        initListener();
                    }
                });
    }

    @Override
    public void onPermissionDenied() {
    }

    /**
     * CameraStateCallbackImpl
     *
     * @since 2021-03-08
     */
    class CameraStateCallbackImpl extends CameraStateCallback {
        CameraStateCallbackImpl() {
        }

        @Override
        public void onCreated(Camera camera) {
            previewSurface = surfaceProvider.getSurfaceOps().get().getSurface();
            if (previewSurface == null) {
                HiLog.info(TAG, "create camera filed, preview surface is null");
                return;
            }

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException exception) {
                HiLog.info(TAG, "Waiting to be interrupted");
            }

            CameraConfig.Builder cameraConfigBuilder = camera.getCameraConfigBuilder();
            cameraConfigBuilder.addSurface(previewSurface);
            cameraConfigBuilder.addSurface(imageReceiver.getRecevingSurface());
            camera.configure(cameraConfigBuilder.build());
            cameraDevice = camera;

            enableImageGroup();
        }

        @Override
        public void onConfigured(Camera camera) {
            FrameConfig.Builder framePreviewConfigBuilder = camera.getFrameConfigBuilder(FRAME_CONFIG_PREVIEW);
            framePreviewConfigBuilder.addSurface(previewSurface);
            try {
                // 启动循环帧捕获
                camera.triggerLoopingCapture(framePreviewConfigBuilder.build());
            } catch (IllegalArgumentException e) {
                HiLog.error(TAG, "Argument Exception");
            } catch (IllegalStateException e) {
                HiLog.error(TAG, "State Exception");
            }
        }

        private void enableImageGroup() {
            takePictureImage.setEnabled(true);
            switchCameraImage.setEnabled(true);
        }
    }

    /**
     * SurfaceCallBack
     *
     * @since 2021-03-08
     */
    class SurfaceCallBack implements SurfaceOps.Callback {
        @Override
        public void surfaceCreated(SurfaceOps callbackSurfaceOps) {
            if (callbackSurfaceOps != null) {
                callbackSurfaceOps.setFixedSize(surfaceProvider.getHeight(), surfaceProvider.getWidth());
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

    /**
     * showBigPic
     */
    private void showBigPic() {
        Intent intent = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withBundleName(getBundleName())
                        .withAbilityName(ImageAbility.class.getName())
                        .build();
        intent.setOperation(operation);
        if (targetFile != null) {
            try {
                intent.setParam(CAMERA_IMAGE_FILE_PATH, targetFile.getCanonicalPath());
            } catch (IOException e) {
                HiLog.error(TAG, "filePath is error");
            }
        }
        startAbility(intent);
    }

    private AsrIntent setInitIntent() {
        AsrIntent initIntent = new AsrIntent();
        initIntent.setAudioSourceType(AsrIntent.AsrAudioSrcType.ASR_SRC_TYPE_PCM);
        initIntent.setEngineType(AsrIntent.AsrEngineType.ASR_ENGINE_TYPE_LOCAL);
        return initIntent;
    }

    private AsrIntent setStartIntent() {
        AsrIntent asrIntent = new AsrIntent();
        asrIntent.setVadEndWaitMs(VAD_END_WAIT_MS);
        asrIntent.setVadFrontWaitMs(VAD_FRONT_WAIT_MS);
        asrIntent.setTimeoutThresholdMs(TIMEOUT_DURATION);
        return asrIntent;
    }

    /**
     * Obtains PCM audio streams and sends them to the ASR engine during recording.
     *
     * @since 2021-03-08
     */
    private class AudioCaptureRunnable implements Runnable {
        @Override
        public void run() {
            byte[] buffers = new byte[BYTES_LENGTH];
            audioCapturer.start();
            while (isRecord) {
                int ret = audioCapturer.read(buffers, 0, BYTES_LENGTH);
                if (ret <= 0) {
                    HiLog.error(TAG, "======Error read data");
                } else {
                    asrClient.writePcm(buffers, BYTES_LENGTH);
                }
            }
        }
    }
}
