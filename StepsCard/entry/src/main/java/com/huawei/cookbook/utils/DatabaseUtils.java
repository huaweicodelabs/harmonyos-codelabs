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
import com.huawei.cookbook.database.Form;
import com.huawei.cookbook.database.SensorData;

import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡片数据库操作
 */
public class DatabaseUtils {
    private static final int SHOW_DAYS = 3;
    private static final String DATA_FILED_DATE = "date";

    private DatabaseUtils() {
    }

    /**
     * 删除数据
     *
     * @param formId 卡片id
     * @param connect 数据连接
     */
    public static void deleteFormData(long formId, OrmContext connect) {
        OrmPredicates where = connect.where(Form.class);
        where.equalTo("formId", formId);
        List<Form> forms = connect.query(where);
        if (!forms.isEmpty()) {
            connect.delete(forms.get(0));
            connect.flush();
        }
    }

    /**
     * 新增卡片信息
     *
     * @param form 卡片对象
     * @param connect 数据连接
     */
    public static void insertForm(Form form, OrmContext connect) {
        connect.insert(form);
        connect.flush();
    }

    /**
     * 插入步数到数据库
     *
     * @param value 数据
     * @param connect 数据连接
     * @param realValue 真实传感器数据
     */
    public static void insertValues(float value, float realValue, OrmContext connect) {
        String now = DateUtils.getDate(0);
        SensorData sensorData = new SensorData(now, (int) value, (int) realValue);
        // 查询今天是否有数据
        SensorData todayData = getSensorData(connect, now);
        boolean isInsert;
        if (todayData == null) {
            isInsert = connect.insert(sensorData);
        } else {
            todayData.setStepsValue(sensorData.getStepsValue());
            isInsert = connect.update(todayData);
        }
        if (isInsert) {
            connect.flush();
        }
    }

    /**
     * 获取真实的当天步数
     *
     * @param value 步数
     * @param connect 数据库连接
     * @param now 今天日期
     * @param yestday 昨天日期
     * @return 数据
     */
    public static SensorData getRealSensorData(float value, OrmContext connect, String now, String yestday) {
        OrmPredicates ormPredicates = new OrmPredicates(SensorData.class);
        ormPredicates.equalTo(DATA_FILED_DATE, yestday);
        SensorData yesterdayData = getSensorData(connect, yestday);
        SensorData todayData = getSensorData(connect, now);
        SensorData sensorData;
        if (yesterdayData != null) {
            if (todayData != null) {
                sensorData = new SensorData(now, (int) (value - todayData.getRealValue()), (int) value);
            } else {
                sensorData = new SensorData(now, (int) (value - yesterdayData.getRealValue()), (int) value);
            }
        } else {
            if (todayData != null) {
                sensorData = new SensorData(now, (int) (value - todayData.getRealValue()), (int) value);
            } else {
                sensorData = new SensorData(now, 0, (int) value);
            }
        }
        return sensorData;
    }

    /**
     * 获取真实的当天步数
     *
     * @param connect 数据库连接
     * @param date 日期
     * @return 数据
     */
    public static SensorData getSensorData(OrmContext connect, String date) {
        OrmPredicates ormPredicates = new OrmPredicates(SensorData.class);
        ormPredicates.equalTo(DATA_FILED_DATE, date);
        List<SensorData> datas = connect.query(ormPredicates);
        SensorData sensorData = null;
        if (datas.size() > 0) {
            sensorData = datas.get(0);
        }
        return sensorData;
    }

    /**
     * 获取前三天的数据
     *
     * @param connect 数据连接
     * @return 数据
     */
    public static List<ChartPoint> getLastFourDaysValue(OrmContext connect) {
        List<ChartPoint> results = new ArrayList<>(SHOW_DAYS);
        OrmPredicates ormPredicates = connect.where(SensorData.class);
        for (int index = SHOW_DAYS; index > 0; index--) {
            ormPredicates.clear();
            ormPredicates.equalTo(DATA_FILED_DATE, DateUtils.getDate(index));
            List<SensorData> datas = connect.query(ormPredicates);
            if (datas.size() == 0) {
                results.add(ChartDataUtils.getChartPoint(0));
            } else {
                results.add(ChartDataUtils.getChartPoint(datas.get(0).getStepsValue()));
            }
        }
        return results;
    }
}
