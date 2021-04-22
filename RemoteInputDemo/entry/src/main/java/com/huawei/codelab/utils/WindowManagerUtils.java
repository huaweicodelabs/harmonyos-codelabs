/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
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

package com.huawei.codelab.utils;

import ohos.agp.components.Component;
import ohos.agp.window.service.WindowManager;

/**
 * LogUtils
 *
 * @since 2021-02-25
 */
public class WindowManagerUtils {
    private WindowManagerUtils() {
    }

    /**
     * Window setting
     *
     */
    public static void setWindows() {
        WindowManager.getInstance().getTopWindow().get().setStatusBarVisibility(Component.INVISIBLE);
        WindowManager.getInstance().getTopWindow().get().addFlags(WindowManager.LayoutConfig.MARK_FULL_SCREEN);
        WindowManager.getInstance().getTopWindow().get().addFlags(WindowManager.LayoutConfig.MARK_ALLOW_EXTEND_LAYOUT);
    }
}
