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

import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Point;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 *  game util
 *
 * @since 2021-03-15
 *
 */
public class GameUtils {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, 0xD001400, "screenUtils");
    private static Context context;
    private static int screenHeight = 0;
    private static int screenWidth = 0;

    // 工具类隐藏构造函数
    private GameUtils() {
    }

    /**
     * Initialization
     *
     * @param slice slice
     */
    public static void setContext(Context slice) {
        GameUtils.context = slice;
    }

    /**
     * Obtains the screen height.
     *
     * @return screenHeight
     *
     */
    public static int getScreenHeight() {
        if (screenHeight == 0) {
            DisplayManager displayManager = DisplayManager.getInstance();
            Optional<Display> optDisplay = displayManager.getDefaultDisplay(context);
            Point point = new Point(0, 0);
            if (optDisplay.isPresent()) {
                Display display = optDisplay.get();
                display.getSize(point);
            }
            screenHeight = (int) point.position[1];
        }
        return screenHeight;
    }

    /**
     * Obtains the screen width.
     *
     * @return screenWidth
     *
     */
    public static int getScreenWidth() {
        if (screenWidth == 0) {
            DisplayManager displayManager = DisplayManager.getInstance();
            Optional<Display> optDisplay = displayManager.getDefaultDisplay(context);
            Point point = new Point(0, 0);
            if (optDisplay.isPresent()) {
                Display display = optDisplay.get();
                display.getSize(point);
            }
            screenWidth = (int) point.position[0];
        }
        return screenWidth;
    }

    /**
     * Convert the image ID to a PixelMap.
     *
     * @param resId resId
     * @param width width
     * @param height height
     * @return deviceIds
     *
     */
    public static PixelMapHolder transIdToPixelMapHolder(int resId, int width, int height) {
        InputStream source = null;
        ImageSource imageSource;
        try {
            source = context.getResourceManager().getResource(resId);
            imageSource = ImageSource.create(source, null);
            ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
            decodingOpts.desiredSize = new Size(width, height);
            return new PixelMapHolder(imageSource.createPixelmap(decodingOpts));
        } catch (IOException | NotExistException e) {
            HiLog.error(TAG, "getPixelMap error");
        } finally {
            try {
                assert source != null;
                source.close();
            } catch (IOException e) {
                HiLog.error(TAG, "getPixelMap source close error");
            }
        }
        return new PixelMapHolder(null);
    }
}
