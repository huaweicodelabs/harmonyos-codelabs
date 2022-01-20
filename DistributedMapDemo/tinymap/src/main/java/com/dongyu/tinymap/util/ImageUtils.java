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

package com.dongyu.tinymap.util;

import com.dongyu.tinymap.ResourceTable;

import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * MapHelper
 *
 * @since 2021-02-01
 */
public class ImageUtils {
    private static final String TAG = ImageUtils.class.getName();

    private static final int TILE_LENGTH = 513;

    private static final int TIME_OUT = 5 * 1000;

    private static final int IMAGE_WIDTH_HEIGHT = 80;

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

    /**
     * getPixelMap
     *
     * @param context context
     * @param actionType actionType
     * @return PixelMap
     */
    public static PixelMap getPixelMap(Context context, String actionType) {
        InputStream drawableInputStream = null;
        PixelMap pixelMap = null;
        try {
            drawableInputStream = context.getResourceManager().getResource(ImageUtils.getImageId(actionType));
            ImageSource imageSource = ImageSource.create(drawableInputStream, new ImageSource.SourceOptions());
            ImageSource.DecodingOptions options = new ImageSource.DecodingOptions();
            options.desiredSize = new Size(IMAGE_WIDTH_HEIGHT, IMAGE_WIDTH_HEIGHT);
            pixelMap = imageSource.createPixelmap(options);
            return pixelMap;
        } catch (IOException | NotExistException exception) {
            LogUtils.info(TAG, "getPixelMap:" + exception.getMessage());
        } finally {
            if (drawableInputStream != null) {
                try {
                    drawableInputStream.close();
                } catch (IOException e) {
                    LogUtils.info(TAG, "getPixelMap:" + e.getMessage());
                }
            }
        }
        return pixelMap;
    }

    /**
     * 从网络获取地图瓦片数据
     *
     * @param urlString urlString
     * @return PixelMap
     */
    public static PixelMap getMapPixelMap(String urlString) {
        InputStream is = null;
        PixelMap pixelMap = null;
        try {
            URL url = new URL(urlString);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(TIME_OUT);
            is = con.getInputStream();
            ImageSource source = ImageSource.create(is, new ImageSource.SourceOptions());
            ImageSource.DecodingOptions options = new ImageSource.DecodingOptions();
            options.desiredSize = new Size(TILE_LENGTH, TILE_LENGTH);
            pixelMap = source.createPixelmap(options);
            return pixelMap;
        } catch (IOException | NullPointerException exception) {
            LogUtils.info(TAG, "getImagePixelMap:" + exception.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtils.info(TAG, "getImagePixelMap:" + e.getMessage());
                }
            }
        }
        return pixelMap;
    }
}
