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

package com.huawei.metadatabindingdemo.alarm.metadata;

import ohos.mp.metadata.binding.metadata.DataAbilityMetaData;

/**
 * This MetaData indicates a row of alarm dataset, wraps the information of an alarm.
 * This class is used in Single Alarm demo and Alarm List demo.
 * For the MetaData content see: src/main/resources/rawfile/jsonschema/alarm_schema.json.
 *
 * @since 2021-05-15
 */
public class ClockRowMetaData extends DataAbilityMetaData {
    private static final int BINARY_CODE_2 = 2;
    private static final int HOUR_6 = 6;
    private static final int HOUR_11 = 11;
    private static final int HOUR_13 = 13;
    private static final int HOUR_14 = 14;
    private static final int HOUR_18 = 18;
    private static final int HOUR_23 = 23;
    String[] DATE = new String[]{"Mon.", "Tue.", "Wed.", "Thu.", " Fri.", "Sat.", "Sun."};

    /**
     * create string message
     *
     * @param message origin message
     * @param daysofweek days of week
     * @return message
     */
    public String toMessage(String message, int daysofweek) {
        StringBuilder res = new StringBuilder();
        int idx = 0;
        int dayOfWeek = daysofweek;
        while (dayOfWeek > 0) {
            if ((dayOfWeek % BINARY_CODE_2) != 0) {
                res.append(DATE[idx]).append(" ");
            }
            dayOfWeek /= BINARY_CODE_2;
            idx++;
        }
        return message + ",  " + res;
    }

    /**
     * convert hour to time zone
     *
     * @param hour hour
     * @return time zone
     */
    public String getTimeZone(int hour) {
        if (hour >= HOUR_6 && hour < HOUR_11) {
            return "Morning";
        }
        if (hour >= HOUR_11 && hour <= HOUR_13) {
            return "Midday";
        }
        if (hour >= HOUR_14 && hour < HOUR_18) {
            return "Afternoon";
        }
        if (hour >= HOUR_18 && hour < HOUR_23) {
            return "Evening";
        }
        return "Early Morning";
    }
}