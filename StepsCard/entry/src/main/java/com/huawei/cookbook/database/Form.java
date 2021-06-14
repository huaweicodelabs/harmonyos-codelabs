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
 * 卡片表
 */
@Entity(tableName = "form")
public class Form extends OrmObject {
    @PrimaryKey()
    // 卡片id
    private Long formId;

    // 卡片名称
    private String formName;

    // 卡片规格
    private int dimension;

    /**
     * 有参构造
     *
     * @param formId 卡片id
     * @param formName 卡片名
     * @param dimension 卡片规格
     */
    public Form(Long formId, String formName, int dimension) {
        this.formId = formId;
        this.formName = formName;
        this.dimension = dimension;
    }

    /**
     * 无参构造
     */
    public Form() {
        super();
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
