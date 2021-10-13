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
 *
 *
 *
 * Gson is released under the Apache 2.0 license.
 *
 * Copyright 2008 Google Inc.
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
 *
 */

package com.huawei.codelab.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GsonUtil
 *
 * @param <E> type
 * @since 2021-03-12
 */
public class GsonUtils<E> {
    private static final String TAG = GsonUtils.class.getSimpleName();

    private static final Gson gson;

    static {
        gson = new GsonBuilder().disableHtmlEscaping().registerTypeAdapterFactory(new MyTypeAdapterFactory()).create();
    }

    private GsonUtils() {
    }

    /**
     * 对象转json字符串
     *
     * @param data type e
     * @param <E> type
     * @return string
     */
    public static <E> String objectToString(E data) {
        String jsonString = null;
        if (gson != null) {
            jsonString = gson.toJson(data);
        }
        return jsonString;
    }

    /**
     * json字符串转对象
     *
     * @param jsonString string
     * @param cls cls
     * @param <T> T
     * @return bean
     */
    public static <T> T jsonToBean(String jsonString, Class<T> cls) {
        T bean = null;
        try {
            if (gson != null) {
                bean = gson.fromJson(jsonString, cls);
            }
            return bean;
        } catch (JsonSyntaxException exception) {
            LogUtils.info(TAG, "jsonToBean exception:" + exception.getMessage());
        }
        return bean;
    }

    /**
     * json字符串转对象集合
     *
     * @param json json string
     * @param cls cls
     * @param <T> t
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
     * Gson容错处理：定义的数据类型为string，但是有时候api返回数据为“[]”，导致解决异常
     *
     * @param <T> type
     * @since 2021-03-12
     */
    public static class MyTypeAdapterFactory<T> implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gs, TypeToken<T> type) {
            TypeAdapter<T> stringNullAdapter = null;
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType == String.class) {
                stringNullAdapter = (TypeAdapter<T>) new StringNullAdapter();
            }
            return stringNullAdapter;
        }
    }

    /**
     * StringNullAdapter
     *
     * @since 2021-03-12
     */
    public static class StringNullAdapter extends TypeAdapter<String> {
        @Override
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.BEGIN_ARRAY) {
                reader.skipValue();
                return "";
            }
            return reader.nextString();
        }

        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            writer.value(value);
        }
    }
}
