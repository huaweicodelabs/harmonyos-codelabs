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

package com.huawei.codelab.callback;

import com.huawei.codelab.constants.Constants;
import com.huawei.codelab.slice.MainAbilitySlice;

/**
 * MainCallBack
 *
 * @since 2021-02-26
 */
public class MainCallBack {
    private static final int POINT_ADD = 1;
    private static final int POINT_DECREASE = -1;

    private MainCallBack() {
    }

    /**
     * movePoint
     *
     * @param slice slice
     * @param moveDirection moveDirection
     */
    public static void movePoint(MainAbilitySlice slice, String moveDirection) {
        switch (moveDirection) {
            case Constants.MOVE_LEFT:
                slice.move(POINT_DECREASE, 0);
                break;
            case Constants.MOVE_RIGHT:
                slice.move(POINT_ADD, 0);
                break;
            case Constants.MOVE_DOWN:
                slice.move(0, POINT_ADD);
                break;
            case Constants.MOVE_UP:
                slice.move(0, POINT_DECREASE);
                break;
            case Constants.GO_BACK:
                // 清除背景，滑动至顶部
                slice.goBack();
                break;
            default:
                break;
        }
    }

}
