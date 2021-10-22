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

package com.huawei.cookbooks.utils;

import com.huawei.cookbooks.database.Form;
import com.huawei.cookbooks.database.FormDatabase;

import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;

import java.util.List;

/**
 * Card Database Operations
 */
public class DatabaseUtils {
    /**
     * Hidden Construction Method
     */
    private DatabaseUtils() {
    }
    /**
     * delete data
     *
     * @param formId form id
     * @param context context
     */

    public static void deleteFormData(long formId, Context context) {
        OrmContext connect = getConnect(context);
        OrmPredicates where = connect.where(Form.class);
        where.equalTo("formId", formId);
        List<Form> forms = connect.query(where);
        if (!forms.isEmpty()) {
            connect.delete(forms.get(0));
            connect.flush();
        }
    }

    /**
     * add card info
     *
     * @param form card object
     * @param context context
     */
    public static void insertForm(Form form,Context context) {
        OrmContext connect = getConnect(context);
        connect.insert(form);
        connect.flush();
    }

    private static OrmContext getConnect(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        return helper.getOrmContext("FormDatabase", "FormDatabase.db", FormDatabase.class);
    }
}
