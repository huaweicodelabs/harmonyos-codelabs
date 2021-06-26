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

import ohos.data.orm.OrmObject;
import ohos.data.orm.annotation.Entity;
import ohos.data.orm.annotation.PrimaryKey;
import ohos.data.rdb.ValuesBucket;

/**
 * This class indicates an Alarm table structure of alarm database.
 *
 * @since 2021-05-15
 */
@Entity(tableName = "alarms")
public class Alarm extends OrmObject {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private int hour;
    private int minutes;
    private int daysofweek;
    private int enabled;
    private String message;

    /**
     * get alarm id
     *
     * @return alarm id
     */
    public Integer getId() {
        return id;
    }

    /**
     * set alarm id
     *
     * @param id alarm id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getDaysofweek() {
        return daysofweek;
    }

    public void setDaysofweek(int daysofweek) {
        this.daysofweek = daysofweek;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    void setAttribute(String attribute, Object value) {
        switch (attribute) {
            case "hour":
                setHour((Integer) value);
                break;
            case "minutes":
                setMinutes((Integer) value);
                break;
            case "daysofweek":
                setDaysofweek((Integer) value);
                break;
            case "enabled":
                setEnabled((Integer) value);
                break;
            case "message":
                setMessage((String) value);
                break;
            default:
        }
    }

    /**
     * convert values bucket to alarm
     *
     * @param valuesBucket values bucket
     * @return alarm
     */
    public Alarm fromValues(ValuesBucket valuesBucket) {
        hour = valuesBucket.getInteger("hour");
        minutes = valuesBucket.getInteger("minutes");
        daysofweek = valuesBucket.getInteger("daysofweek");
        enabled = valuesBucket.getInteger("enabled");
        message = valuesBucket.getString("message");
        return this;
    }
}
