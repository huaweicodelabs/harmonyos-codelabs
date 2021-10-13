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

package com.huawei.cookbook.database;

import ohos.data.orm.OrmObject;
import ohos.data.orm.annotation.Entity;
import ohos.data.orm.annotation.PrimaryKey;

/**
 * Sensor Data Storage Table
 */
@Entity(tableName = "sensorData")
public class SensorData extends OrmObject {
    @PrimaryKey()
    // date yyyyMMdd
    private String date;

    // stepsValue
    private int stepsValue;

    // 实际步数
    private int realValue;

    /**
     * parametric construction
     *
     * @param date       date
     * @param stepsValue stepsValue
     * @param realValue  real stepsValue
     */
    public SensorData(String date, int stepsValue, int realValue) {
        this.date = date;
        this.stepsValue = stepsValue;
        this.realValue = realValue;
    }

    public SensorData() {
        super();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStepsValue() {
        return stepsValue;
    }

    public void setStepsValue(int stepsValue) {
        this.stepsValue = stepsValue;
    }

    public int getRealValue() {
        return realValue;
    }

    public void setRealValue(int realValue) {
        this.realValue = realValue;
    }
}
