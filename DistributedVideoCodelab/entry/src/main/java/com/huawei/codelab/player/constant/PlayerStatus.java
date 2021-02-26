/*
 * Copyright (c) 2021 Huawei Device Co., Ltd. All rights reserved.
 *
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

package com.huawei.codelab.player.constant;

/**
 * PlayerStatus enum
 *
 * @since 2020-12-04
 *
 */
public enum PlayerStatus {
    /**
     * the video is released
     *
     */
    IDEL("idel"),

    /**
     * video is preparing
     *
     */
    PREPARING("preparing"),

    /**
     * when the video become prepared will be ready to play
     *
     */
    PREPARED("prepared"),

    /**
     * start the video or resume to play
     *
     */
    PLAY("play"),

    /**
     * pause the playing
     *
     */
    PAUSE("pause"),

    /**
     * stop the playing
     *
     */
    STOP("stop"),

    /**
     * the video play completed
     *
     */
    COMPLETE("complete"),

    /**
     * the wrong status of video
     *
     */
    ERROR("error"),

    /**
     * before the status of play
     *
     */
    BUFFERING("buffering");

    private String status;

    PlayerStatus(String value) {
        this.status = value;
    }

    public String getStatus() {
        return status;
    }
}
