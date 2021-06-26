/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.metadatabindingdemo.alarm.db;

import com.huawei.metadatabindingdemo.MyLog;
import com.huawei.metadatabindingdemo.metadatabinding.AlarmlistsliceMetaDataBinding;

import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.app.Context;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.mp.metadata.binding.databinding.MetaDataBinding;
import ohos.mp.metadata.binding.databinding.MetaDataRequestInfo;
import ohos.mp.metadata.binding.metadata.MetaData;

/**
 * this class wraps alarm database CURD operations.
 *
 * @since 2021-05-15
 */
public class AlarmsOperation {
    private static int idx = 0;
    private static int count = 0;
    private static final int DEFAULT_HOUR = 12;
    private static final int DEFAULT_MINUTES = 12;
    private static final int DEFAULT_DAYS_OF_WEEK = 12;
    private static final int DEFAULT_ENABLE = 1;
    private static final int HOURS = 24;
    private static final int MINUTES = 60;
    private static final int ENABLES = 2;
    private static final int DAYS_OF_WEEK = 128;

    private static final String COL_HOUR = "hour";
    private static final String COL_MINUTE = "minutes";
    private static final String COL_ENABLE = "enabled";
    private static final String COL_DAYS = "daysofweek";
    private static final String COL_MSG = "message";

    private AlarmsOperation() { }

    /**
     * insert alarm data
     *
     * @param context context
     */
    public static void insert(Context context) {
        try {
            int time = Math.abs((int) System.currentTimeMillis());
            ValuesBucket bucket = new ValuesBucket();
            bucket.putInteger(COL_HOUR, time % HOURS);
            bucket.putInteger(COL_MINUTE, time % MINUTES);
            bucket.putInteger(COL_ENABLE, time % ENABLES);
            bucket.putInteger(COL_DAYS, time % DAYS_OF_WEEK);
            bucket.putString(COL_MSG, "Clock" + idx++);
            DataAbilityHelper.creator(context).insert(AlarmsDataAbility.CLOCK_URI, bucket);
        } catch (DataAbilityRemoteException ex) {
            MyLog.error("Insert Clock Data Failed: DataAbility Remote Exception");
        }
    }

    /**
     * Create a metadata by binding request information and add into datasource.
     *
     * @param binding meta data binding
     */
    public static void insertAnAlarm(MetaDataBinding binding) {
        MetaDataRequestInfo.RequestItem requestItem = binding.getRequestInfo().getRequestItem("ClockMetaData");

        MetaData metaData = AlarmlistsliceMetaDataBinding.createMetaData(requestItem);
        metaData.put(COL_HOUR, DEFAULT_HOUR);
        metaData.put(COL_MINUTE, DEFAULT_MINUTES);
        metaData.put(COL_DAYS, DEFAULT_DAYS_OF_WEEK);
        metaData.put(COL_ENABLE, DEFAULT_ENABLE);
        metaData.put(COL_MSG, "count" + count);

        binding.addMetaData(metaData, requestItem);
        count++;
    }

    /**
     * Getting the first alarm record of the database.
     *
     * @param context app context, an ability or slice will be fine.
     * @return An alarm instance of the first alarm record in database, or null is the database has no records.
     */
    public static Alarm queryFirst(Context context) {
        DataAbilityHelper helper = DataAbilityHelper.creator(context);
        ResultSet resultSet = null;
        try {
            resultSet = helper.query(
                    AlarmsDataAbility.CLOCK_URI,
                    new String[]{COL_HOUR, COL_MINUTE, COL_DAYS, COL_ENABLE, COL_MSG},
                    null);
        } catch (DataAbilityRemoteException e) {
            MyLog.error("AlarmsOperation query Exception");
        }
        Alarm alarm = null;
        if (resultSet != null) {
            boolean hasData = resultSet.goToFirstRow();
            if (!hasData) {
                return null;
            }
            // get the first alarm record, resultSet now pointing to the first row.
            alarm = getQueryResults(resultSet);
        }
        return alarm;
    }

    private static Alarm getQueryResults(ResultSet resultSet) {
        Alarm alarm = new Alarm();
        for (String column : resultSet.getAllColumnNames()) {
            int index = resultSet.getColumnIndexForName(column);
            alarm.setAttribute(column, getFromColumn(resultSet, index));
        }
        return alarm;
    }

    private static Object getFromColumn(ResultSet resultSet, int index) {
        ResultSet.ColumnType type = resultSet.getColumnTypeForIndex(index);
        switch (type) {
            case TYPE_INTEGER:
                return resultSet.getInt(index);
            case TYPE_FLOAT:
                return resultSet.getDouble(index);
            case TYPE_STRING:
                return resultSet.getString(index);
            case TYPE_BLOB:
            case TYPE_NULL:
            default:
                return null;
        }
    }
}
