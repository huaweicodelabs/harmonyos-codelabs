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

package com.huawei.harmonyaudiodemo.player.constant;

/**
 * Constants
 *
 * @since 2021-04-04
 */
public class Constants {
    /**
     * running
     */
    public static final int PLAYER_PROGRESS_RUNNING = 0;
    /**
     * hide
     */
    public static final int PLAYER_CONTROLLER_HIDE = 1;
    /**
     * hide
     */
    public static final int PLAYER_CONTROLLER_SHOW = 2;
    /**
     * 100
     */
    public static final int ONE_HUNDRED_PERCENT = 100;
    /**
     * rewind step
     */
    public static final int REWIND_STEP = 5000;
    /**
     * volume step
     */
    public static final int VOLUME_STEP = 5;
    /**
     * INTENT STARTTIME PARAM
     */
    public static final String INTENT_STARTTIME_PARAM = "intetn_starttime_param";
    /**
     * INTENT PLAYURL PARAM
     */
    public static final String INTENT_PLAYURL_PARAM = "intetn_playurl_param";
    /**
     * INTENT PLAYSTATU PARAM
     */
    public static final String INTENT_PLAYSTATU_PARAM = "intetn_playstatu_param";
    /**
     * INTENT LANDSCREEN REQUEST CODE
     */
    public static final int INTENT_LANDSCREEN_REQUEST_CODE = 1001;

    private Constants() {
    }
}
