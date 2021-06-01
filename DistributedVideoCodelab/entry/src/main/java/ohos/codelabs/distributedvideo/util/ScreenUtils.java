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

package ohos.codelabs.distributedvideo.util;

import ohos.agp.utils.Point;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.global.configuration.DeviceCapability;

import java.util.Optional;

/**
 * ScreenUtils
 *
 * @since 2020-12-04
 */
public class ScreenUtils {
    private ScreenUtils() {
    }

    /**
     * getScreenHeight
     *
     * @param context context
     * @return int
     */
    public static int getScreenHeight(Context context) {
        DisplayManager displayManager = DisplayManager.getInstance();
        Optional<Display> optDisplay = displayManager.getDefaultDisplay(context);
        Point point = new Point(0, 0);
        if (!optDisplay.isPresent()) {
            return (int) point.position[1];
        } else {
            Display display = optDisplay.get();
            display.getSize(point);
            return (int) point.position[1];
        }
    }

    /**
     * getScreenWidth
     *
     * @param context context
     * @return int
     */
    public static int getScreenWidth(Context context) {
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

    /**
     * vp转像素
     *
     * @param context context
     * @param vp vp
     * @return px
     */
    public static int vp2px(Context context, float vp) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (vp * attributes.densityPixels);
    }

    /**
     * 像素转vp
     *
     * @param context context
     * @param px px
     * @return vp
     */
    public static int px2vp(Context context, float px) {
        DisplayAttributes attributes = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        return (int) (px / attributes.densityPixels);
    }

    /**
     * dp2px
     *
     * @param context context
     * @param size size
     * @return int
     */
    public static int dp2px(Context context, int size) {
        int density = context.getResourceManager().getDeviceCapability().screenDensity / DeviceCapability.SCREEN_MDPI;
        return size * density;
    }

    /**
     * px2dip
     *
     * @param context context
     * @param size size
     * @return int
     */
    public static int px2dip(Context context, int size) {
        int density = context.getResourceManager().getDeviceCapability().screenDensity / DeviceCapability.SCREEN_MDPI;
        return size / density;
    }
}
