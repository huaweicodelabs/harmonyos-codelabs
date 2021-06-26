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

import com.huawei.metadatabindingdemo.alarm.db.AlarmOrmDatabase;

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
import ohos.utils.PacMap;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;

/**
 * Note data ability.
 * This case reuse Alarm ORM Database, we can also change it to RDB Database, shared preference files as we want.
 * Just remember that whenever the data source is changed, notify users.
 *
 * @since 2021-05-15
 */
public class NotesDataAbility extends Ability {
    /**
     * clock uri
     */
    public static final Uri NOTE_URI = Uri.parse(
            "dataability:///com.huawei.metadatabindingdemo.db.NotesDataAbility/alarm");
    private static final int ERR_CODE = -1;
    private OrmContext ormContext = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        DatabaseHelper manager = new DatabaseHelper(this);
        // reuse Alarm Database. see Alarm Demo.
        ormContext = manager.getOrmContext(
                AlarmOrmDatabase.DATABASE_NAME_ALIAS,
                AlarmOrmDatabase.DATABASE_NAME,
                AlarmOrmDatabase.class);
    }

    @Override
    public ResultSet query(Uri uri, String[] columns, DataAbilityPredicates predicates) {
        if (ormContext == null) {
            return null;
        }
        OrmPredicates ormPredicates = DataAbilityUtils.createOrmPredicates(predicates, Note.class);
        return ormContext.query(ormPredicates, columns);
    }

    @Override
    public int insert(Uri uri, ValuesBucket value) {
        if (ormContext == null) {
            return ERR_CODE;
        }
        // create a note object
        Note note = new Note();
        note.setContent(value.getString("content"));
        note.setTime(value.getInteger("time"));
        // insert the note object to database
        boolean isSuccessful = ormContext.insert(note);
        if (!isSuccessful) {
            return ERR_CODE;
        }
        isSuccessful = ormContext.flush();
        if (!isSuccessful) {
            return ERR_CODE;
        }
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return Math.toIntExact(note.getRowId());
    }

    @Override
    public int delete(Uri uri, DataAbilityPredicates predicates) {
        if (ormContext == null) {
            return ERR_CODE;
        }
        OrmPredicates ormPredicates = DataAbilityUtils.createOrmPredicates(predicates, Note.class);
        int value = ormContext.delete(ormPredicates);
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return value;
    }

    @Override
    public int update(Uri uri, ValuesBucket value, DataAbilityPredicates predicates) {
        if (ormContext == null) {
            return ERR_CODE;
        }
        OrmPredicates ormPredicates = DataAbilityUtils.createOrmPredicates(predicates, Note.class);
        int index = ormContext.update(ormPredicates, value);
        DataAbilityHelper.creator(this, uri).notifyChange(uri);
        return index;
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