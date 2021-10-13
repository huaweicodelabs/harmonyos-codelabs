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

package com.huawei.codelab.map;

/**
 * 地图导航常量封装
 *
 * @since 2021-03-12
 */
public class Const {
    /**
     * 高德地图接口的调用凭证，需要开发者自己申请
     */
    public static final String MAP_KEY = "";

    /**
     * 高德地图输入提示api接口
     */
    public static final String INPUT_TIPS_URL =
        "https://restapi.amap.com/v3/assistant/inputtips?keywords=%s&key=%s&location=%s&city=%s";

    /**
     * 高德地图路径导航api接口
     */
    public static final String ROUTE_URL =
        "https://restapi.amap.com/v3/direction/driving?extensions=all&strategy=5&origin=%s&destination=%s&key=%s";

    /**
     * 高德地图逆地理编码api接口
     */
    public static final String REGION_DETAIL_URL = "https://restapi.amap.com/v3/geocode/regeo?location=%s&key=%s";

    /**
     * 导航开始标识
     */
    public static final String ROUTE_START = "route_start";

    /**
     * 导航结束标识
     */
    public static final String ROUTE_END = "route_end";

    /**
     * 当前位置标识
     */
    public static final String ROUTE_PEOPLE = "route_people";

    /**
     * 关闭WatchAbility标识
     */
    public static final String STOP_WATCH_ABILITY = "stop_watch_ability";

    /**
     * 右转标识
     */
    public static final String ROUTE_TURN_RIGHT = "右转";

    /**
     * 左转标识
     */
    public static final String ROUTE_TURN_LEFT = "左转";

    /**
     * 直行标识
     */
    public static final String ROUTE_STRAIGHT = "直行";

    /**
     * 向右前方行驶标识
     */
    public static final String ROUTE_RIGHT_STRAIGHT = "向右前方行驶";

    /**
     * 向左前方行驶标识
     */
    public static final String ROUTE_LEFT_STRAIGHT = "向左前方行驶";

    private Const() {
    }
}