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

import ohos.aafwk.ability.AbilitySlice;

import java.util.ArrayList;
import java.util.List;

/**
 * AbilitySliceRoute util
 *
 * @since 2021-05-04
 */
public class AbilitySliceRouteUtil {
    private static AbilitySliceRouteUtil instance;

    private final List<AbilitySlice> routes;

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
        routes.add(slice);
    }

    /**
     * remove Route
     *
     * @param slice slice
     */
    public void removeRoute(AbilitySlice slice) {
        for (int i = routes.size() - 1; i >= 0; i--) {
            if (routes.get(i).equals(slice)) {
                routes.remove(routes.get(i));
                break;
            }
        }
    }

    /**
     * terminate AbilitySlices from slices
     *
     */
    public void terminateSlices() {
        terminateSlices(routes);
    }

    /**
     * terminate AbilitySlices from slices
     *
     * @param slices slices
     */
    private void terminateSlices(List<AbilitySlice> slices) {
        for (int i = slices.size() - 1; i >= 0; i--) {
            slices.get(i).terminate();
        }
    }
}
