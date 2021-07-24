/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.huawei.cookbook.qrlibrary;

import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.camera.CameraKit;
import ohos.media.camera.device.Camera;
import ohos.media.camera.device.CameraConfig;
import ohos.media.camera.device.CameraStateCallback;
import ohos.media.camera.device.FrameConfig;
import ohos.media.image.Image;
import ohos.media.image.ImageReceiver;
import ohos.media.image.common.ImageFormat;

import static ohos.media.camera.device.Camera.FrameConfigType.FRAME_CONFIG_PREVIEW;
import static ohos.media.camera.params.Metadata.FlashMode.FLASH_ALWAYS_OPEN;
import static ohos.media.camera.params.Metadata.FlashMode.FLASH_CLOSE;

import com.huawei.cookbook.qrlibrary.util.QRCodeUtil;

/**
 * Preview Camera.
 *
 * @since 2021-05-21
 */
public class CameraPreview extends SurfaceProvider
        implements SurfaceOps.Callback {
    /**
     * 屏幕宽度.
     */
    private static final int SCREEN_WIDTH = 1080;
    /**
     * 屏幕高度.
     */
    private static final int SCREEN_HEIGHT = 1920;
    /**
     * ImageReceiver捕获帧数.
     */
    private static final int IMAGE_RCV_CAPACITY = 9;
    /**
     * 配置预览的Surface.
     */
    private Surface previewSurface;
    /**
     * 配置读取数据的Surface.
     */
    private Surface imageSurface;
    /**
     * 相机.
     */
    private Camera qrCamera;
    /**
     * 图像帧数据接收.
     */
    private ImageReceiver imageReceiver;
    /**
     * 可以显示闪光灯.
     */
    private FrameConfig.Builder framePreviewConfigBuilder;
    /**
     * 捕获每一帧相机数据结果处理.
     */
    private CameraResultListener cameraResultListener;
    /**
     * 单帧捕获生成图像回调Listener.
     */
    private final ImageReceiver.IImageArrivalListener imageArrivalListener
            = new ImageReceiver.IImageArrivalListener() {
        @Override
        public void onImageArrival(ImageReceiver imageReceiver) {
            Image mImage = imageReceiver.readNextImage();
            if (mImage != null) {
                byte[] data = QRCodeUtil.image2Byte(mImage);
                mImage.release();

                if (cameraResultListener != null && data != null) {
                    cameraResultListener.cameraResult(data);
                }
            }
        }
    };

    /**
     * constructor.
     *
     * @param context context
     */
    public CameraPreview(Context context) {
        super(context);
        initView();
    }

    // init Surface
    private void initView() {
        WindowManager.getInstance().getTopWindow().get().setTransparent(true);
        pinToZTop(false);
        getSurfaceOps().get().addCallback(this);
    }

    /**
     * surface Created.
     *
     * @param surfaceOps surfaceOps
     */
    @Override
    public void surfaceCreated(SurfaceOps surfaceOps) {
        if (surfaceOps != null) {
            surfaceOps.setFixedSize(SCREEN_HEIGHT, SCREEN_WIDTH);
        }
        previewSurface = surfaceOps.getSurface();
        imageReceiver = ImageReceiver.create(SCREEN_HEIGHT, SCREEN_WIDTH,
                ImageFormat.JPEG, IMAGE_RCV_CAPACITY);
        imageReceiver.setImageArrivalListener(imageArrivalListener);

        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceOps surfaceOps, int format,
        int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceOps surfaceOps) {}

    // openCamera
    private void openCamera() {
        CameraKit cameraKit = CameraKit.getInstance(getContext());
        if (cameraKit == null) {
            return;
        }
        String[] cameraIds = cameraKit.getCameraIds();
        if (cameraIds.length <= 0) {
            return;
        }
        CameraStateCallbackImpl callback = new CameraStateCallbackImpl();
        cameraKit.createCamera(cameraIds[0], callback,
                new EventHandler(EventRunner.create("qr")));
    }

    private class CameraStateCallbackImpl extends CameraStateCallback {
        @Override
        public void onCreated(Camera camera) {
            super.onCreated(camera);
            qrCamera = camera;
            // 创建相机设备
            CameraConfig.Builder cameraBuilder =
                    qrCamera.getCameraConfigBuilder();

            // 配置预览的Surface
            cameraBuilder.addSurface(previewSurface);
            // 配置拍照的Surface
            imageSurface = imageReceiver.getRecevingSurface();
            cameraBuilder.addSurface(imageSurface);

            qrCamera.configure(cameraBuilder.build());
        }

        @Override
        public void onConfigured(Camera camera) {
            // 配置相机设备
            framePreviewConfigBuilder =
                    qrCamera.getFrameConfigBuilder(FRAME_CONFIG_PREVIEW);
            framePreviewConfigBuilder.addSurface(previewSurface);
            framePreviewConfigBuilder.addSurface(imageSurface);
            FrameConfig frameConfig = framePreviewConfigBuilder.build();

            qrCamera.triggerLoopingCapture(frameConfig);
        }
    }

    /**
     * Stop Preview.
     */
    public void stopCameraPreview() {
        // 释放相机设备
        if (qrCamera != null) {
            qrCamera.stopLoopingCapture();
            qrCamera.release();
            qrCamera = null;
            cameraResultListener = null;
            imageReceiver.release();
            imageReceiver = null;
        }
    }

    /**
     * open Flashlight.
     */
    public void openFlashlight() {
        framePreviewConfigBuilder.setFlashMode(FLASH_ALWAYS_OPEN);
        qrCamera.triggerLoopingCapture(framePreviewConfigBuilder.build());
    }

    /**
     * close Flashlight.
     */
    public void closeFlashlight() {
        framePreviewConfigBuilder.setFlashMode(FLASH_CLOSE);
        qrCamera.triggerLoopingCapture(framePreviewConfigBuilder.build());
    }

    /**
     * Scan callback class.
     */
    public interface CameraResultListener {
        /**
         * 相机每帧数据
         *
         * @param data 相机每帧数据
         */
        void cameraResult(byte[] data);
    }

    /**
     * Set the scanning result.
     *
     * @param cameraResultListener Callback Event
     */
    public void setCameraResultListener(CameraResultListener cameraResultListener) {
        this.cameraResultListener = cameraResultListener;
    }
}
