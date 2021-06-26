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

package com.huawei.metadatabindingdemo.custom_data_source.handler;

import com.huawei.metadatabindingdemo.MyLog;
import com.huawei.metadatabindingdemo.custom_data_source.db.NotesDataAbility;

import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.app.Context;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.mp.metadata.binding.MetaDataFramework;
import ohos.mp.metadata.binding.dao.CustomDao;
import ohos.mp.metadata.binding.databinding.MetaDataRequestInfo;
import ohos.mp.metadata.binding.metadata.MetaData;
import ohos.utils.PacMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class supplies Customized Data Source binding. You can encapsulate any datasource types through this class.
 *
 * @since 2021-05-15
 */
public class MyDataHandler implements CustomDao.ICustomMetaDataHandler {
    private DataAbilityHelper mDbHelper = null;
    private MetaDataRequestInfo.RequestItem mReqItem = null;

    @Override
    public boolean onConnect(CustomDao dao) {
        Context mCtx = MetaDataFramework.appContext;
        this.mDbHelper = DataAbilityHelper.creator(mCtx);
        this.mReqItem = dao.getRequestItem();
        return true;
    }

    @Override
    public List<MetaData> onQuery(CustomDao dao, boolean isSync, String[] properties, PacMap predicates) {
        ArrayList<MetaData> list = new ArrayList<>();
        try {
            ResultSet resultSet = mDbHelper.query(NotesDataAbility.NOTE_URI, null, null);
            if (resultSet != null) {
                boolean hasData = resultSet.goToFirstRow();
                if (!hasData) {
                    return list;
                }
                do {
                    list.add(getQueryResults(resultSet));
                }
                while (resultSet.goToNextRow());
            }
        } catch (DataAbilityRemoteException e) {
            MyLog.error("ohosTest: SimpleHandler onQuery error");
        }
        return list;
    }

    private MetaData getQueryResults(ResultSet resultSet) {
        MetaData metaData = MetaDataFramework.createMetaData(mReqItem);
        for (String column : resultSet.getAllColumnNames()) {
            int index = resultSet.getColumnIndexForName(column);
            assert metaData != null;
            metaData.put(column, getFromColumn(resultSet, index));
        }
        return metaData;
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

    @Override
    public void onAdd(MetaData metaData) {
    }

    @Override
    public void onDelete(MetaData metaData) {
    }

    @Override
    public void onChange(MetaData metaData, String key, Object value) {
        MyLog.info("ohosTest: MyDataHandler----onchange" + key + "-" + value);
        ValuesBucket vb = new ValuesBucket();
        vb.putString(key, (String) value);
        try {
            mDbHelper.update(NotesDataAbility.NOTE_URI, vb, null);
        } catch (DataAbilityRemoteException e) {
            MyLog.error("ohosTest: SimpleHandler onChange error");
        }
    }

    @Override
    public void onFlush(MetaData metaData) {
    }

    @Override
    public void onDisconnect(CustomDao dao) {
    }
}
