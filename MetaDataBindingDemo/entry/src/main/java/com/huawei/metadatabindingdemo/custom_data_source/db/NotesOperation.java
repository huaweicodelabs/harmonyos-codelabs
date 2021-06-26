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

import com.huawei.metadatabindingdemo.MyApplication;
import com.huawei.metadatabindingdemo.MyLog;

import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.utils.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Note data Operation, wrapping datasource CURD operations for 'note-list' example..
 *
 * @since 2021-05-15
 */
public class NotesOperation {
    private static final int ERR_CODE = -1;
    private final DataAbilityHelper helper;
    private final Uri USER_URI;

    private NotesOperation() {
        helper = DataAbilityHelper.creator(MyApplication.getApplication());
        USER_URI = NotesDataAbility.NOTE_URI;
    }

    private static class SingleNotesOperation {
        private static final NotesOperation INSTANCE = new NotesOperation();
    }

    /**
     * Obtaining a Singleton Instance
     *
     * @return NotesOperation object
     */
    public static NotesOperation getInstance() {
        return SingleNotesOperation.INSTANCE;
    }

    /**
     * Querying All Data
     *
     * @return A List of {@link Note}s
     */
    public List<Note> queryAll() {
        return query(null);
    }

    /**
     * Insert Note Data
     *
     * @param content Indicates a Note contents.
     * @param time Indicates a Note created time in seconds.
     * @return Note index
     */
    public int insert(String content, int time) {
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString("content", content);
        valuesBucket.putInteger("time", time);

        int result;
        try {
            result = helper.insert(USER_URI, valuesBucket);
        } catch (DataAbilityRemoteException e) {
            result = ERR_CODE;
        }
        return result;
    }

    private List<Note> query(DataAbilityPredicates predicates) {
        ResultSet resultSet = null;
        try {
            resultSet = helper.query(USER_URI, new String[]{"content", "time"}, predicates);
        } catch (DataAbilityRemoteException e) {
            MyLog.error("NotesOperation data query exception");
        }
        List<Note> notes = new ArrayList<>();
        if (resultSet != null) {
            // Processing result
            boolean hasData = resultSet.goToFirstRow();
            if (!hasData) {
                return notes;
            }
            do {
                // Process the records in the ResultSet.
                notes.add(getQueryResults(resultSet));
            }
            while (resultSet.goToNextRow());
        }
        return notes;
    }

    private Note getQueryResults(ResultSet resultSet) {
        Note note = new Note();
        for (String column : resultSet.getAllColumnNames()) {
            int index = resultSet.getColumnIndexForName(column);
            note.setAttribute(column, getFromColumn(resultSet, index));
        }
        return note;
    }

    private Object getFromColumn(ResultSet resultSet, int index) {
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
