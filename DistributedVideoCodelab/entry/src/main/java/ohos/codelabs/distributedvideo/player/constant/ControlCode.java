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

package ohos.codelabs.distributedvideo.player.constant;

/**
 * ControlCode enum
 *
 * @since 2021-09-07
 *
 */
public enum ControlCode {
    /**
     * resume to play the video when the video is paused
     *
     */
    RESUME(1001),

    /**
     * pausethe video when the video is playing
     *
     */
    PAUSE(1002),

    /**
     * pause the video when the video is playing
     *
     */
    STOP(1003),

    /**
     * direct to the video
     *
     */
    SEEK(1004),

    /**
     * forward the video-playing per second.
     *
     */
    FORWARD(1005),

    /**
     * reduce the volume
     *
     */
    REWARD(1006),

    /**
     * add the volume
     *
     */
    VOLUME_ADD(1007),

    /**
     * reduce the volume
     *
     */
    VOLUME_REDUCED(1008),

    /**
     * set volume
     *
     */
    VOLUME_SET(1009);

    private final int code;

    ControlCode(int value) {
        this.code = value;
    }

    public int getCode() {
        return code;
    }
}
