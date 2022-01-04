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

package com.huawei.cookbook.util;

import com.huawei.cookbook.database.MovieInfo;

import ohos.app.Context;
import ohos.global.configuration.DeviceCapability;
import ohos.global.resource.NotExistException;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.utils.zson.ZSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * CommonUtils
 */
public class CommonUtils {
    private static final String TAG = "Common Utils";
    private static final String LOCAL_LANGUAGES_ENGLISH = "en";
    private static final int JSON_READ_ERROR = -1;

    private CommonUtils() {
    }

    /**
     * get PixelMap
     * @param context context
     * @param path path
     * @param width width
     * @param height height
     * @return PixelMap
     */
    public static Optional<PixelMap> getPixelMapFromPath(Context context, String path, int width, int height) {
        InputStream drawableInputStream = null;
        try {
            LogUtils.info(CommonUtils.class.getName(), path);
            drawableInputStream = context.getResourceManager().getRawFileEntry(path).openRawFile();
            ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
            sourceOptions.formatHint = "image/jpg";
            ImageSource imageSource = ImageSource.create(path, sourceOptions);
            ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
            decodingOpts.desiredSize = new Size(vp2px(context, width), vp2px(context, height));
            return Optional.of(imageSource.createPixelmap(decodingOpts));
        } catch (IOException e) {
            LogUtils.error(TAG, "getPixelMapFromPath error");
        } finally {
            try {
                if (drawableInputStream != null) {
                    drawableInputStream.close();
                }
            } catch (IOException e) {
                LogUtils.error(TAG, "getPixelMapFromPath error");
            }
        }
        return Optional.empty();
    }

    /**
     * get json String from file
     * @param context context
     * @param jsonPath file path
     * @return json String
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
            LogUtils.error(TAG, "IOException");
        }
        return Optional.of(jsonPath).toString();
    }

    /**
     * json String to list
     * @param context context
     * @return movie list
     */
    public static List<MovieInfo> getMoviesData(Context context) {
        String filePath = "entry/resources/rawfile/movies_datas.json";
        String language = Locale.getDefault().getLanguage();
        if(LOCAL_LANGUAGES_ENGLISH.equals(language)){
            filePath = "entry/resources/rawfile/movies_datas_en.json";
        }
        return ZSONArray.stringToClassList(getStringFromJsonPath(context,
                filePath), MovieInfo.class);
    }

    /**
     * vp2px 将vp转换成px
     *
     * @param size size
     * @param context context
     * @return int
     */
    public static int vp2px(Context context, int size) {
        int density = context.getResourceManager().getDeviceCapability().screenDensity / DeviceCapability.SCREEN_MDPI;
        return size * density;
    }
}
