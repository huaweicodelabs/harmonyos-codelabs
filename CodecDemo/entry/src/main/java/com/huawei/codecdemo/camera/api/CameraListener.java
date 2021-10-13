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

package com.huawei.codecdemo.camera.api;

import com.huawei.codecdemo.camera.constant.CameraType;
import com.huawei.codecdemo.camera.constant.CaptureMode;

import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.media.image.common.Size;

import java.util.List;

/**
 * CameraListener
 *
 * @since 2021-04-09
 *
 */
public interface CameraListener {
    /**
     * bind
     *
     * @param surfaceProvider surfaceProvider
     */
    void bind(SurfaceProvider surfaceProvider);

    /**
     * unBind
     */
    void unBind();

    /**
     * setCameraListener
     *
     * @param cameraStateListener cameraStateListener
     */
    void setCameraListener(CameraStateListener cameraStateListener);

    /**
     * setMode
     *
     * @param mode mode
     */
    void setMode(CaptureMode mode);

    /**
     * getResolution
     *
     * @return Size
     */
    Size getResolution();

    /**
     * getResolutions
     *
     * @return Sizes
     */
    List<Size> getResolutions();

    /**
     * isRecording
     *
     * @return isRecording
     */
    boolean isCapturing();

    /**
     * isFrontCamera
     *
     * @return isFrontCamera
     */
    boolean isFrontCamera();

    /**
     * stopCapture
     */
    void stopCapture();

    /**
     * capture
     */
    void capture();

    /**
     * switchCamera
     *
     * @param type type
     */
    void switchCamera(CameraType type);

    /**
     * setMirrorEffect
     *
     * @param isMirror isMirror
     */
    void setMirrorEffect(boolean isMirror);
}
