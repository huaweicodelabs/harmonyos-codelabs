/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
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

package com.huawei.codelab.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 影片静态类
 *
 * @since 2021-02-26
 */
public class ComponentPointDataMgr {
    private static final int LIST_INIT_SIZE = 16;
    private static final int POINT_ONE = 1;
    private static final int POINT_TWO = 2;
    private static final int POINT_THREE = 3;
    private static final int POINT_FOUR = 4;
    private static final int POINT_FIVE = 5;
    private static List<ComponentPointData> componentPointDataMgrs = new ArrayList<>(LIST_INIT_SIZE);

    static {
        componentPointDataMgrs.add(new ComponentPointData("无线耳机", "WXEJ", POINT_ONE, POINT_ONE));
        componentPointDataMgrs.add(new ComponentPointData("音响", "YX", POINT_ONE, POINT_TWO));
        componentPointDataMgrs.add(new ComponentPointData("胡杨树", "HYS", POINT_ONE, POINT_THREE));
        componentPointDataMgrs.add(new ComponentPointData("WATCHFIT", "WATCHFIT", POINT_ONE, POINT_FOUR));
        componentPointDataMgrs.add(new ComponentPointData("WATCHGT2PRO", "WATCHGT2PRO", POINT_ONE, POINT_FIVE));
        componentPointDataMgrs.add(new ComponentPointData("智能眼镜", "ZNYJ", POINT_TWO, POINT_ONE));
        componentPointDataMgrs.add(new ComponentPointData("旗舰店", "QJD", POINT_TWO, POINT_TWO));
        componentPointDataMgrs.add(new ComponentPointData("沙漠", "SM", POINT_TWO, POINT_THREE));
        componentPointDataMgrs.add(new ComponentPointData("WATCHGT2", "WATCHGT2", POINT_TWO, POINT_FOUR));
        componentPointDataMgrs.add(new ComponentPointData("星空蓝", "XKL", POINT_TWO, POINT_FIVE));
    }

    private ComponentPointDataMgr() {
    }

    public static List<ComponentPointData> getComponentPointDataMgrs() {
        return componentPointDataMgrs;
    }

    /**
     * 根据坐标获取当前焦点
     *
     * @param pointX X轴坐标
     * @param pointY Y轴坐标
     * @return 返回
     */
    public static Optional<ComponentPointData> getMoviePoint(int pointX, int pointY) {
        for (ComponentPointData componentPointData : componentPointDataMgrs) {
            if (componentPointData.getPointX() == pointX && componentPointData.getPointY() == pointY) {
                return Optional.of(componentPointData);
            }
        }
        return Optional.ofNullable(null);
    }

    /**
     * 根据影片名称获取焦点
     *
     * @param name 名称
     * @return 返回
     */
    public static Optional<ComponentPointData> getConstantMovie(String name) {
        for (ComponentPointData componentPointData : componentPointDataMgrs) {
            if (componentPointData.getMovieName().equals(name)) {
                return Optional.of(componentPointData);
            }
        }
        return Optional.ofNullable(null);
    }
}
