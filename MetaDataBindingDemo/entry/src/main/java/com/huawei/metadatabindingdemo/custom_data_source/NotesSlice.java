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

package com.huawei.metadatabindingdemo.custom_data_source;

import com.huawei.metadatabindingdemo.ResourceTable;
import com.huawei.metadatabindingdemo.custom_data_source.db.Note;
import com.huawei.metadatabindingdemo.custom_data_source.db.NotesOperation;
import com.huawei.metadatabindingdemo.metadatabinding.NotessliceMetaDataBinding;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.mp.metadata.binding.databinding.DataSourceConnectionException;
import ohos.mp.metadata.binding.databinding.IMetaDataObserver;
import ohos.mp.metadata.binding.databinding.MetaDataBinding;
import ohos.mp.metadata.binding.databinding.MetaDataRequestInfo;
import ohos.mp.metadata.binding.metadata.MetaData;

import java.util.ArrayList;
import java.util.List;

/**
 * A Slice Ability displays a note list. It implements {@link IMetaDataObserver} that handles MetaData changes.
 *
 * @see IMetaDataObserver
 * @since 2021-05-15
 */
public class NotesSlice extends AbilitySlice implements IMetaDataObserver {
    private static final int DB_SIZE = 8;
    private static final int TIME_DEFAULT = 1621047451;
    private static final int UNIT_TIME_1000 = 1000;
    private ListContainer listContainer;
    private NoteListProvider notesProvider;
    private List<MetaData> metaDatas;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        List<Note> notes = NotesOperation.getInstance().queryAll();
        // init database, add some data first to show this foundation.
        if (notes.size() < DB_SIZE) {
            for (int i = 0; i < DB_SIZE; i++) {
                NotesOperation.getInstance().insert("Node" + i, (int) (System.currentTimeMillis() / UNIT_TIME_1000));
            }
            NotesOperation.getInstance().insert("old Node", TIME_DEFAULT);
        }
        createComponent();
    }

    private void createComponent() {
        MetaDataRequestInfo request = new MetaDataRequestInfo.Builder()
                .setRequestSource("NoteMetaData",
                        "datahandler:///com.huawei.metadatabindingdemo.custom_data_source.handler.MyDataHandler")
                .setSyncRequest("NoteMetaData", true)
                .build();
        MetaDataBinding binding;
        Component mainComponent;
        try {
            binding = NotessliceMetaDataBinding.requestBinding(this, request, this);
            mainComponent = binding.getLayoutComponent();
        } catch (DataSourceConnectionException e) {
            mainComponent = LayoutScatter.getInstance(this)
                    .parse(ResourceTable.Layout_default_error, null, false);
            setUIContent((ComponentContainer) mainComponent);
            return;
        }
        setUIContent((ComponentContainer) mainComponent);

        notesProvider = new NoteListProvider(this);
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_note_list);
        notesProvider.initData(createNotes(this, metaDatas));
        listContainer.setItemProvider(notesProvider);

        DependentLayout dependentLayout = (DependentLayout) findComponentById(
                ResourceTable.Id_title_area_back_icon_hot_area);
        dependentLayout.setClickedListener(component -> this.terminate());
    }

    @Override
    public void onDataLoad(List<MetaData> metaData, MetaDataRequestInfo.RequestItem requestItem) {
        if (metaData == null || requestItem == null) {
            return;
        }
        metaDatas = metaData;
    }

    @Override
    public void onDataChange(List<MetaData> addedMetaData, List<MetaData> updatedMetaData,
        List<MetaData> deletedMetaData, MetaDataRequestInfo.RequestItem requestItem) {
        if (addedMetaData == null) {
            return;
        }
        notesProvider.addItems(createNotes(this, addedMetaData));
    }

    /**
     * Init notes data
     *
     * @param context ability slice
     * @param dataList clock row mete data list
     * @return category list
     */
    private List<NoteRow> createNotes(AbilitySlice context, List<MetaData> dataList) {
        List<NoteRow> list = new ArrayList<>();
        if (dataList == null || dataList.isEmpty()) {
            return list;
        }
        for (MetaData metaData : dataList) {
            NoteRow item = new NoteRow(context, metaData);
            list.add(item);
        }
        return list;
    }
}
