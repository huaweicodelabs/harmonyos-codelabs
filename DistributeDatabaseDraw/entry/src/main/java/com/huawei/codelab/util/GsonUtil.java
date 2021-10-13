/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.codelab.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GsonUtil
 *
 * @since 2021-04-06
 */
public class GsonUtil {
    private static Gson gson;

    static {
        gson = new Gson();
    }

    private GsonUtil() {
    }

    /**
     * Converts an object into a JSON character string
     *
     * @param object Object
     * @return jsonString
     */
    public static String objectToString(Object object) {
        String jsonString = null;
        if (gson != null) {
            jsonString = gson.toJson(object);
        }
        return jsonString;
    }

    /**
     * Converting gsonString to Generic Beans
     *
     * @param jsonString jsonString
     * @param cls Class
     * @param <T> type
     * @return t
     */
    public static <T> T jsonToBean(String jsonString, Class<T> cls) {
        T var1 = null;
        if (gson != null) {
            var1 = gson.fromJson(jsonString, cls);
        }
        return var1;
    }

    /**
     * Converting to List to Solve Generic Problems
     *
     * @param json json
     * @param cls cls
     * @param <T> generic
     * @return list
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        List<T> lists = new ArrayList<T>(0);
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            lists.add(gson.fromJson(elem, cls));
        }
        return lists;
    }

    /**
     * Convert to a list with a map
     *
     * @param gsonString String
     * @param <T> type
     * @return list List
     */
    public static <T> List<Map<String, T>> jsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * Converted to map
     *
     * @param gsonString String
     * @param <T> type
     * @return map Map
     */
    public static <T> Map<String, T> jsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}