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

import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Common utils
 *
 * @since 2020-12-04
 */
public class CommonUtils {
    private static final String TAG = "Common Utils";
    private static final String ERROR_MESSAGE = "some error happened";
    private static final int GET_COLOR_STATE_FAILED = -1;
    private static final int JSON_READ_ERROR = -1;

    private CommonUtils() {
    }

    /**
     * Get color method
     *
     * @param context context resourceID res id
     * @param resourceId res id
     * @return color
     * @since 2020-12-04
     */
    public static int getColor(Context context, int resourceId) {
        try {
            return context.getResourceManager().getElement(resourceId).getColor();
        } catch (IOException | NotExistException | WrongTypeException e) {
            LogUtils.info(TAG, "some exception happened");
        }
        return GET_COLOR_STATE_FAILED;
    }

    /**
     * Obtains a bitmap object based on the resource path.
     *
     * @param context context
     * @param path path
     * @return pixelMap
     */
    public static PixelMap getPixelMapFromPath(Context context, String path) {
        InputStream drawableInputStream = null;
        try {
            drawableInputStream = context.getResourceManager().getRawFileEntry(path).openRawFile();
            ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
            sourceOptions.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(drawableInputStream, sourceOptions);
            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            decodingOptions.desiredSize = new Size(0, 0);
            decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
            decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
            return imageSource.createPixelmap(decodingOptions);
        } catch (IOException e) {
            LogUtils.info(TAG, ERROR_MESSAGE);
        } finally {
            try {
                if (drawableInputStream != null) {
                    drawableInputStream.close();
                }
            } catch (IOException e) {
                LogUtils.error(TAG, ERROR_MESSAGE);
            }
        }
        return null;
    }

    /**
     * Obtains a List based on the resource path.
     *
     * @param context context
     * @param jsonPath path
     * @return jsonString
     */
    public static String getStringFromJsonPath(Context context, String jsonPath) {
        Resource datasResource;
        try {
            datasResource = context.getResourceManager().getRawFileEntry(jsonPath).openRawFile();
            byte[] buffers = new byte[datasResource.available()];
            if (datasResource.read(buffers) != JSON_READ_ERROR) {
                return new String(buffers, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            LogUtils.error(TAG, ERROR_MESSAGE);
        }
        return Optional.of(jsonPath).toString();
    }
}
