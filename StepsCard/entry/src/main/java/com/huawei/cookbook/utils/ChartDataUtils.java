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

package com.huawei.cookbook.utils;

import com.huawei.cookbook.cardentity.ChartPoint;
import com.huawei.cookbook.cardentity.ChartValues;
import com.huawei.cookbook.cardentity.PointStyle;

import ohos.data.orm.OrmContext;
import ohos.utils.zson.ZSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * chart date utls
 */
public class ChartDataUtils {
    private static final String GRAY_COLOR = "#CDCACA";
    private static final String ORANGE_COLOR = "#FF9C28";
    private static final int DIMENSION_2X4 = 3;
    private static final int TARGET_STEPS = 1000;
    private static final int PROGRESS_PERCENT = 100;
    private static final int POINT_SIZE = 5;
    private static final int PERCENT_100 = 100;
    private static final double METER_PER_STEP = 0.6;

    /**
     * hide constructor
     *
     */
    private ChartDataUtils() {
    }

    /**
     * get chart potin data list
     *
     * @param connect database connection
     * @return chart potin data list
     */
    public static List<ChartPoint> getChartPoints(OrmContext connect) {
        return DatabaseUtils.getLastFourDaysValue(connect);
    }

    /**
     * get chart datasets
     *
     * @param value value
     * @param connect database connection
     * @return chart value
     */
    public static ChartValues getChartValues(String value, OrmContext connect) {
        ChartValues chartValues = new ChartValues();
        // 设置填充色颜色
        chartValues.setFillColor(GRAY_COLOR);
        // 设置线的颜色
        chartValues.setStrokeColor(GRAY_COLOR);
        chartValues.setGradient(true);
        // 获取点的集合
        // 获取前三天的点的集合
        List<ChartPoint> chartPoints = ChartDataUtils.getChartPoints(connect);
        ChartPoint noewChartPoint = ChartDataUtils.getChartPoint(Integer.parseInt(value));
        // 加入今天的步数点
        chartPoints.add(noewChartPoint);
        chartValues.setData(chartPoints);
        return chartValues;
    }

    /**
     * get chart point
     *
     * @param value value
     * @return point data
     */
    public static ChartPoint getChartPoint(int value) {
        ChartPoint chartPoint = new ChartPoint();
        // 点的数值,超过1000只显示1000的高度
        chartPoint.setValue(Math.min(TARGET_STEPS, value));
        // 点的描述
        chartPoint.setDescription(value + "");
        // 点的描述显示位置，此处设置为点的上面
        chartPoint.setTextLocation(ChartPoint.TextLocation.top.toString());
        // 设置描述字体的颜色
        chartPoint.setTextColor(GRAY_COLOR);
        PointStyle pointStyle = new PointStyle();
        // 设置点的大小
        pointStyle.setSize(POINT_SIZE);
        // 设置点的颜色
        pointStyle.setFillColor(ORANGE_COLOR);
        // 设置点的外框颜色
        pointStyle.setStrokeColor(ORANGE_COLOR);
        // 设置点的样式，此处设置为圆形
        pointStyle.setShape(PointStyle.PointShape.CIRCLE.toString()
                .toLowerCase(Locale.ROOT));
        chartPoint.setPointStyle(pointStyle);
        return chartPoint;
    }

    /**
     * get data for js page
     *
     * @param value step count
     * @param dimension form dimension
     * @param connect database connection
     * @return data for js page
     */
    public static ZSONObject getZsonObject(String value, int dimension, OrmContext connect) {
        ZSONObject result = new ZSONObject();
        int round;
        if (Double.parseDouble(value) >= TARGET_STEPS) {
            round = PERCENT_100;
        } else {
            round = (int) Math.round(Double.parseDouble(value) / TARGET_STEPS * PROGRESS_PERCENT);
        }
        result.put("initShow", false);
        result.put("isShow", true);
        result.put("percent", round);
        result.put("steps", value);
        if (dimension == DIMENSION_2X4) {
            // 组装chartdatasets
            List<ChartValues> datasets = new ArrayList<>(1);
            // 获取点集数据
            ChartValues chartValues = ChartDataUtils.getChartValues(value, connect);
            datasets.add(chartValues);
            // chart图表数据
            result.put("datasets", datasets);
            // 里程
            result.put("mileage", Math.round(Integer.parseInt(value) * METER_PER_STEP));
        }
        return result;
    }
}
