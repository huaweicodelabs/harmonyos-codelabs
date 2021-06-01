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

package com.huawei.codelab.utils;

import com.huawei.codelab.MainAbility;
import com.huawei.codelab.MathGameServiceAbility;
import com.huawei.codelab.PictureGameServiceAbility;

/**
 * CommonData Util
 *
 * @since 2021-01-11
 */
public class CommonData {
    /**
     * MainAbility name
     */
    public static final String ABILITY_MAIN = MainAbility.class.getName();

    /**
     * MathGameService name
     */
    public static final String MATH_GAME_SERVICE_NAME = MathGameServiceAbility.class.getName();

    /**
     * PictureGameServiceAbility name
     */
    public static final String PICTURE_GAME_SERVICE_NAME = PictureGameServiceAbility.class.getName();

    /**
     * LOG TAG
     */
    public static final String TAG = "[Education System] ";

    /**
     * picture page action flag
     */
    public static final String PICTURE_PAGE = "action.system.picture";

    /**
     * math page action flag
     */
    public static final String MATH_PAGE = "action.system.math";

    /**
     * draw page action flag
     */
    public static final String DRAW_PAGE = "action.system.draw.rem";

    /**
     * math draw event action flag
     */
    public static final String MATH_DRAW_EVENT = "com.huawei.math.draw";

    /**
     * picture game event
     */
    public static final String PICTURE_GAME_EVENT = "com.huawei.picturegame";

    /**
     * key remote device id
     */
    public static final String KEY_REMOTE_DEVICEID = "remoteDeviceId";

    /**
     * isLocal
     */
    public static final String KEY_IS_LOCAL = "isLocal";

    /**
     * pointXs
     */
    public static final String KEY_POINT_X = "pointXs";

    /**
     * pointYs
     */
    public static final String KEY_POINT_Y = "pointYs";

    /**
     * isLastPoint
     */
    public static final String KEY_IS_LAST_POINT = "isLastPoint";

    /**
     * imageIndex
     */
    public static final String KEY_IMAGE_INDEX = "imageIndex";

    /**
     * moveImageId
     */
    public static final String KEY_MOVE_IMAGE_ID = "moveImageId";

    /**
     * movePosition
     */
    public static final String KEY_MOVE_POSITION = "movePosition";

    private CommonData() {
    }
}
