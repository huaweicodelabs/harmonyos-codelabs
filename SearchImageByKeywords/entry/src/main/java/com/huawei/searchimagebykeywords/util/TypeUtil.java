/*
 *
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

package com.huawei.searchimagebykeywords.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 类型转换工具类
 *
 * @since 2021-09-09
 */
public class TypeUtil {
    private TypeUtil() {
    }

    /***
     * 转化为List
     *
     * @param object 要转换的对象
     * @param clazz 目标类型class
     * @param <T> 泛型
     * @return 集合类对象Optional
     */
    public static <T> Optional<List<T>> castList(Object object, Class<T> clazz) {
        List<T> array = new ArrayList<>();
        Optional<List<T>> result = Optional.empty();
        if (object instanceof List<?>) {
            for (Object obj : (List<?>) object) {
                array.add(clazz.cast(obj));
            }
            result = Optional.of(array);
        }
        return result;
    }

    /***
     * 转化为Map
     *
     * @param object 要转换的对象
     * @param keyClass key值类型class
     * @param valueClass value值类型class
     * @param <K> key值泛型
     * @param <V> value值泛型
     * @return Map类对象Optional
     */
    public static <K, V> Optional<Map<K, V>> castMap(Object object, Class<K> keyClass, Class<V> valueClass) {
        Optional<Map<K, V>> result = Optional.empty();
        Map<K, V> map = new HashMap<>();
        if (object instanceof HashMap<?, ?>) {
            ((HashMap<?, ?>) object).forEach((key, value) -> map.put(keyClass.cast(key), valueClass.cast(value)));
            result = Optional.of(map);
        }
        return result;
    }
}
