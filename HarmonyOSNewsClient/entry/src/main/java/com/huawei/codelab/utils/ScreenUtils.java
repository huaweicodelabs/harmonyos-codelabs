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

import ohos.agp.utils.Point;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

import java.util.Optional;

/**
 * Screen Util
 *
 * @since 2020-12-04
 */
class ScreenUtils {
    private ScreenUtils() { }

    /**
     * get Screen Width Method
     *
     * @param context context
     * @return screen width
     */
    static int getScreenWidth(Context context) {
        DisplayManager displayManager = DisplayManager.getInstance();
        Optional<Display> optDisplay = displayManager.getDefaultDisplay(context);
        Point point = new Point(0, 0);
        if (!optDisplay.isPresent()) {
            return (int) point.position[0];
        } else {
            Display display = optDisplay.get();
            display.getSize(point);
            return (int) point.position[0];
        }
    }
}
