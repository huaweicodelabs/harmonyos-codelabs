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

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.dataability.DataAbilityUtils;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.PacMap;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;

/**
 * Clock data ability.
 * In this case, no self-defined permissions has set. Developers can connect this data ability directly.
 * See config.json.
 * For every data source changes operation, the operation should notify users the change, otherwise Metadata will not
 * know the change.
 *
 * @since 2021-05-15
 */
public class AlarmsDataAbility extends Ability {
    /**
     * data ability uri
     */
    public static final Uri CLOCK_URI = Uri.parse(
            "dataability:///com.huawei.metadatabindingdemo.db.AlarmsDataAbility");
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");
    private static final int ERR_CODE = -1;
    private static final String ROW_ID = "id";

    private OrmContext ormContext = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        HiLog.info(LABEL_LOG, "AlarmsDataAbility onStart");
        DatabaseHelper manager = new DatabaseHelper(this);
        ormContext = manager.getOrmContext(
                AlarmOrmDatabase.DATABASE_NAME_ALIAS,
                AlarmOrmDatabase.DATABASE_NAME,
                AlarmOrmDatabase.class);
    }

    @Override
    public ResultSet query(Uri uri, String[] columns, DataAbilityPredicates predicates) {
        if (uri.equals(CLOCK_URI)) {
            OrmPredicates ormPredicates = DataAbilityUtils.createOrmPredicates(predicates, Alarm.class);
            return ormContext.query(ormPredicates, columns);
        }
        return null;
    }

    @Override
    public int insert(Uri uri, ValuesBucket value) {
        Alarm alarm = new Alarm();
        if (ormContext.insert(alarm.fromValues(value))) {
            ormContext.flush();
            DataAbilityHelper.creator(this, uri).notifyChange(uri);
            return (int) alarm.getRowId();
        }
        return ERR_CODE;
    }

    @Override
    public int delete(Uri uri, DataAbilityPredicates predicates) {
        // delete is not used in this example.
        return 0;
    }

    @Override
    public int update(Uri uri, ValuesBucket value, DataAbilityPredicates predicates) {
        OrmPredicates ormPredicates;
        if (predicates == null) {
            Integer id = value.getInteger(ROW_ID);
            if (id == null) {
                return ERR_CODE;
            }
            value.delete(ROW_ID);
            ormPredicates = new OrmPredicates(Alarm.class).equalTo(ROW_ID, id);
        } else {
            ormPredicates = DataAbilityUtils.createOrmPredicates(predicates, Alarm.class);
        }
        int rst = ormContext.update(ormPredicates, value);
        DataAbilityHelper.creator(getContext(), uri).notifyChange(uri);
        return rst;
    }

    @Override
    public FileDescriptor openFile(Uri uri, String mode) {
        return null;
    }

    @Override
    public String[] getFileTypes(Uri uri, String mimeTypeFilter) {
        return new String[0];
    }

    @Override
    public PacMap call(String method, String arg, PacMap extras) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ormContext != null) {
            ormContext.close();
        }
    }
}