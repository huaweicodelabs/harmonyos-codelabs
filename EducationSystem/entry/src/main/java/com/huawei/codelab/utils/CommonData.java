/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
public interface CommonData {
    String ABILITY_MAIN = MainAbility.class.getName();

    String MATH_GAME_SERVICE_NAME = MathGameServiceAbility.class.getName();

    String PICTURE_GAME_SERVICE_NAME = PictureGameServiceAbility.class.getName();

    String TAG = "[Education System] ";

    String PICTURE_PAGE = "action.system.picture";

    String MATH_PAGE = "action.system.math";

    String DRAW_PAGE = "action.system.draw.rem";

    String MATH_DRAW_EVENT = "com.huawei.math.draw";

    String PICTURE_GAME_EVENT = "com.huawei.picturegame";

    String KEY_REMOTE_DEVICEID = "remoteDeviceId";

    String KEY_IS_LOCAL = "isLocal";

    String KEY_POINT_X = "pointsX";

    String KEY_POINT_Y = "pointsY";

    String KEY_IS_LAST_POINT = "isLastPoint";

    String KEY_IMAGE_INDEX = "imageIndex";

    String KEY_MOVE_IMAGE_ID = "moveImageId";

    String KEY_MOVE_POSITION = "movePosition";

}
