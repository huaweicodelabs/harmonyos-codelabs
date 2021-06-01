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

/**
 * 地图工具类
 *
 * @since 2021-04-01
 */
public class MapUtils {
    private static final double OVER_LENGTH = 20037508.3427892;

    private static final int PARAM_NUM = 2;

    private static final int NUM_90 = 90;

    private static final int NUM_180 = 180;

    private static final int NUM_360 = 360;

    private MapUtils() {
    }

    /**
     * Latitude and longitude Mercator to Mercator
     *
     * @param lon longitude
     * @param lat Latitude
     * @return Mercator
     */
    public static double[] lonLat2Mercator(double lon, double lat) {
        double[] mercators = new double[PARAM_NUM];
        double mercatorX = lon * OVER_LENGTH / NUM_180;
        double mercatorY = Math.log(Math.tan((NUM_90 + lat) * Math.PI / NUM_360)) / (Math.PI / NUM_180);
        mercatorY = mercatorY * OVER_LENGTH / NUM_180;
        mercators[0] = mercatorX;
        mercators[1] = mercatorY;
        return mercators;
    }
}
