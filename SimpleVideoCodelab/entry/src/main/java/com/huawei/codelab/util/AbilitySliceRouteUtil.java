/*
 * Copyright (c) 2021 Huawei Device Co., Ltd. All rights reserved.
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

package com.huawei.codelab.util;

import ohos.aafwk.ability.AbilitySlice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbilitySliceRoute util
 *
 * @since 2020-12-04
 *
 */
public class AbilitySliceRouteUtil {
    private static AbilitySliceRouteUtil instance;

    private List<Map<String, AbilitySlice>> routes;

    private AbilitySliceRouteUtil() {
        routes = new ArrayList<>(0);
    }

    /**
     * initialization AbilitySliceRouteUtil
     *
     * @return AbilitySliceRouteUtil
     */
    public static synchronized AbilitySliceRouteUtil getInstance() {
        if (instance == null) {
            instance = new AbilitySliceRouteUtil();
        }
        return instance;
    }

    /**
     * add route
     *
     * @param slice slice
     */
    public void addRoute(AbilitySlice slice) {
        Map<String, AbilitySlice> map = new HashMap<>(0);
        map.put(slice.getClass().getName(), slice);
        routes.add(map);
    }

    /**
     * remove Route
     *
     * @param slice slice
     */
    public void removeRoute(AbilitySlice slice) {
        String tag = slice.getClass().getName();
        for (int i = routes.size() - 1; i >= 0; i--) {
            for (Map.Entry<String, AbilitySlice> stringAbilitySliceEntry : routes.get(i).entrySet()) {
                String key = null;
                if (stringAbilitySliceEntry instanceof Map.Entry) {
                    if (((Map.Entry) stringAbilitySliceEntry).getKey() instanceof String) {
                        key = (String) ((Map.Entry) stringAbilitySliceEntry).getKey();
                    }
                }
                if (tag.equals(key)) {
                    routes.remove(routes.get(i));
                    break;
                }
            }
        }
    }

    /**
     * terminateAbilitySlice method
     */
    public void terminateAbilitySlice() {
        for (int i = routes.size() - 1; i >= 0; i--) {
            for (Map.Entry<String, AbilitySlice> stringAbilitySliceEntry : routes.get(i).entrySet()) {
                if (stringAbilitySliceEntry instanceof Map.Entry) {
                    if (((Map.Entry) stringAbilitySliceEntry).getValue() instanceof AbilitySlice) {
                        AbilitySlice abilitySlice = (AbilitySlice) ((Map.Entry) stringAbilitySliceEntry).getValue();
                        abilitySlice.terminate();
                    }
                }
            }
        }
    }
}
