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

package com.huawei.harmonyaudiodemo.media.constant;

/**
 * Camera ConstUtil
 *
 * @since 2021-01-17
 */
public class MediaConst {
    /**
     * read render interval
     */
    public static final int READ_RENDER_INTERVAL = 1024;
    /**
     * VOICE FILE PREFIX
     */
    public static final String VOICE_FILE_MUSIC = "MUSIC_";

    /**
     * VOICE FILE TYPE
     */
    public static final String VOICE_FILE_TYPE_MP3 = ".mp3";

    /**
     * recorder fps
     */
    public static final int RECORDER_FPS = 30;

    /**
     * recorder bit rate
     */
    public static final int RECORDER_BIT_RATE = 10000000;

    /**
     * recorder frame rate
     */
    public static final int RECORDER_FRAME_RATE = 25;

    /**
     * BUFFERSIZE
     */
    public static final int BUFFERSIZE = 1024;

    /**
     * SAMPLE RATE
     */
    public static final int SAMPLE_RATE = 44100;

    private MediaConst() {
    }
}
