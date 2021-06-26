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

package com.huawei.cookbooks.util;

import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Common utils
 *
 * @since 2021-06-18
 */
public class CommonUtils {
    private static final String TAG = "Common Utils";
    private static final int GET_COLOR_STATE_FAILED = -1;
    private static final float DISTANCE_UNIT = 0.6f / 1000; // 计算距离
    private static final float CALORIE_UNIT = 0.6f * 60 * 1.036f / 1000; // 计算热量
    private static final float FLOOR_UNIT = 0.6f / 28; // 计算楼层
    private static final float PROGRESS_UNIT = 0.1f; // 进度条百分比 1k步为100%
    private static final int INTENSITY_ONE = 400; // 低强度
    private static final int INTENSITY_TWO = 600; // 中等强度
    private static final int INTENSITY_THR = 800; // 中高强度
    private static final String PROGRESS_ONE = "25"; // 低强度
    private static final String PROGRESS_TWO = "50"; // 中等强度
    private static final String PROGRESS_THR = "75"; // 中高强度
    private static final String PROGRESS_FOU = "100"; // 高强度
    private static final String TEXT = "text";
    private static final String PROGRESS = "progress";

    private CommonUtils() {
    }

    /**
     * Get color method
     *
     * @param context context resourceID res id
     * @param resourceId res id
     * @return color
     */
    public static int getColor(Context context, int resourceId) {
        try {
            return context.getResourceManager().getElement(resourceId).getColor();
        } catch (IOException | NotExistException | WrongTypeException e) {
            LogUtils.info(TAG, "some exception happen");
        }
        return GET_COLOR_STATE_FAILED;
    }

    /**
     * 根据步数计算公里数
     *
     * @param steps steps
     * @return string
     */
    public static String getDistanceWithSteps(int steps) {
        return String.format("%.2f", steps * DISTANCE_UNIT);
    }

    /**
     * 根据步数计算热量-千卡
     *
     * @param steps steps
     * @return string
     */
    public static String getCalorieWithSteps(int steps) {
        return String.format("%.2f", steps * CALORIE_UNIT);
    }

    /**
     * 根据步数计算楼层
     *
     * @param steps steps
     * @return float
     */
    public static int getFloorWithSteps(int steps) {
        return (int) (steps * FLOOR_UNIT);
    }

    /**
     * 根据步数计算progress 1k步为100%
     *
     * @param steps steps
     * @return float
     */
    public static int getProgressWithSteps(int steps) {
        return (int) (steps * PROGRESS_UNIT);
    }

    /**
     * 根据步数获取强度
     * 0-400 低强度，401-600 中等强度，601-800 中高强度，大于800 高强度
     *
     * @param steps steps
     * @return map
     */
    public static Map<String, String> getIntensityWithSteps(int steps) {
        Map<String, String> intensityMap = new HashMap<>(0);
        if (steps <= INTENSITY_ONE) {
            intensityMap.put(TEXT, "低强度");
            intensityMap.put(PROGRESS, PROGRESS_ONE);
        }
        if (steps > INTENSITY_ONE && steps <= INTENSITY_TWO) {
            intensityMap.put(TEXT, "中等强度");
            intensityMap.put(PROGRESS, PROGRESS_TWO);
        }
        if (steps > INTENSITY_TWO && steps <= INTENSITY_THR) {
            intensityMap.put(TEXT, "中高强度");
            intensityMap.put(PROGRESS, PROGRESS_THR);
        }
        if (steps > INTENSITY_THR) {
            intensityMap.put(TEXT, "高强度");
            intensityMap.put(PROGRESS, PROGRESS_FOU);
        }
        return intensityMap;
    }
}
