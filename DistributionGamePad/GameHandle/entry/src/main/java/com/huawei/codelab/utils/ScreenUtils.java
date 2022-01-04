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

import ohos.agp.components.Component;
import ohos.agp.utils.Point;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;

import java.util.Optional;

/**
 * ScreenUtils
 *
 * @since 2021-03-12
 */
public class ScreenUtils {
    private ScreenUtils() {
    }

    /**
     * get screen height
     *
     * @param context context
     * @return screen height
     */
    public static int getScreenHeight(Context context) {
        DisplayManager displayManager = DisplayManager.getInstance();
        Optional<Display> optDisplay = displayManager.getDefaultDisplay(context);
        Point point = new Point(0, 0);
        if (optDisplay.isPresent()) {
            Display display = optDisplay.get();
            display.getSize(point);
        }
        return (int) point.position[1];
    }

    /**
     * setWindows
     */
    public static void setWindows() {
        WindowManager.getInstance().getTopWindow().get().setStatusBarVisibility(Component.INVISIBLE);
        WindowManager.getInstance().getTopWindow().get().addFlags(WindowManager.LayoutConfig.MARK_FULL_SCREEN);
        WindowManager.getInstance().getTopWindow().get().addFlags(WindowManager.LayoutConfig.MARK_ALLOW_EXTEND_LAYOUT);
    }
}
