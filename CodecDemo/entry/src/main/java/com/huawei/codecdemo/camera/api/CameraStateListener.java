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

import com.huawei.codecdemo.camera.CameraController;

/**
 * CameraStateListener
 *
 * @since 2021-04-09
 *
 */
public interface CameraStateListener {
    /**
     * onCameraConfigured
     *
     * @param cameraController cameraController
     */
    void onCameraConfigured(CameraController cameraController);

    /**
     * onCameraReleased
     *
     */
    void onCameraReleased();

    /**
     * onGetFrameResult
     *
     * @param frame frame
     */
    void onGetFrameResult(byte[] frame);
}
