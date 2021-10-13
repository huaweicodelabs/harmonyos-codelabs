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

import ohos.app.Context;
import ohos.global.resource.Resource;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

/**
 * CommonUtils
 */
public class CommonUtils {
    private static final String TAG = "Common Utils";
    private static final int JSON_READ_ERROR = -1;

    private CommonUtils() {
    }

    /**
     * get json String from file
     * @param context context
     * @param jsonPath file path
     * @return json String
     */
    public static String getStringFromJsonPath(Context context, String jsonPath) {
        Resource dataResource;
        try {
            dataResource = context.getResourceManager().getRawFileEntry(jsonPath).openRawFile();
            byte[] buffers = new byte[dataResource.available()];
            if (dataResource.read(buffers) != JSON_READ_ERROR) {
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
        return ZSONArray.stringToClassList(getStringFromJsonPath(context,
                "entry/resources/rawfile/movies_data.json"), MovieInfo.class);
    }

    /**
     * get random number
     *
     * @param max max number
     * @param last last number
     * @return number
     */
    public static int getRandomInt(int max, int last) {
        SecureRandom random = new SecureRandom();
        int ran = random.nextInt(max);
        if (ran == last) {
            ran = getRandomInt(max, last);
        }
        return ran;
    }

    /**
     * get js page data
     *
     * @param top top index
     * @param bottom bottom index
     * @param topMovie top movie
     * @param bottomMovie bottom movie
     * @return js page data
     */
    public static ZSONObject getJsBindData(int top, int bottom, MovieInfo topMovie, MovieInfo bottomMovie) {
        ZSONObject zsonObject = new ZSONObject();
        zsonObject.put("topIndex", top);
        zsonObject.put("initShow", false);
        zsonObject.put("isShow", true);
        zsonObject.put("topMovieName", topMovie.getTitle());
        zsonObject.put("topMovieDesc", topMovie.getIntroduction());
        zsonObject.put("topMovieImgUrl", topMovie.getImgUrl());
        zsonObject.put("bottomIndex", bottom);
        zsonObject.put("bottomMovieName", bottomMovie.getTitle());
        zsonObject.put("bottomMovieDesc", bottomMovie.getIntroduction());
        zsonObject.put("bottomMovieImgUrl", bottomMovie.getImgUrl());
        return zsonObject;
    }
}
