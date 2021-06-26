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

package com.huawei.metadatabindingdemo.custom_data_source.db;

import ohos.data.orm.OrmObject;
import ohos.data.orm.annotation.Entity;
import ohos.data.orm.annotation.PrimaryKey;

/**
 * This class indicates a Note table structure of alarm database.
 *
 * @since 2021-05-15
 */
@Entity(tableName = "notes")
public class Note extends OrmObject {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String content;
    private int time;

    /**
     * Constructor
     */
    public Note() {
    }

    /**
     * Set Note 'content' and 'time' attributes' value.
     *
     * @param attribute attribute's name
     * @param value attribute's value
     */
    public void setAttribute(String attribute, Object value) {
        switch (attribute) {
            case "content":
                setContent((String) value);
                break;
            case "time":
                setTime((Integer) value);
                break;
            default:
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
