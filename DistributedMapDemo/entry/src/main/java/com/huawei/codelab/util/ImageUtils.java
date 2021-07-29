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

package com.huawei.codelab.util;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.map.Const;

/**
 * MapHelper
 *
 * @since 2021-02-01
 */
public class ImageUtils {
    private ImageUtils() {
    }

    /**
     * 获取图像id
     *
     * @param actionType actionType
     * @return imageId
     */
    public static int getImageId(String actionType) {
        int imageId = 0;
        switch (actionType) {
            case Const.ROUTE_STRAIGHT:
                imageId = ResourceTable.Media_nav_straight;
                break;
            case Const.ROUTE_END:
                imageId = ResourceTable.Media_route_end;
                break;
            case Const.ROUTE_START:
                imageId = ResourceTable.Media_route_start;
                break;
            case Const.ROUTE_TURN_LEFT:
                imageId = ResourceTable.Media_nav_turn_left;
                break;
            case Const.ROUTE_TURN_RIGHT:
                imageId = ResourceTable.Media_nav_turn_right;
                break;
            case Const.ROUTE_LEFT_STRAIGHT:
                imageId = ResourceTable.Media_nav_turn_little_left;
                break;
            case Const.ROUTE_RIGHT_STRAIGHT:
                imageId = ResourceTable.Media_nav_turn_little_right;
                break;
            case Const.ROUTE_PEOPLE:
                imageId = ResourceTable.Media_loc;
                break;
            default:
                break;
        }
        return imageId;
    }
}
