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

package com.huawei.metadatabindingdemo.alarm.list_ui;

import com.huawei.metadatabindingdemo.ResourceTable;
import com.huawei.metadatabindingdemo.alarm.db.AlarmsOperation;
import com.huawei.metadatabindingdemo.metadatabinding.AlarmlistsliceMetaDataBinding;

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
 * A Slice Ability displays a clock list. It implements {@link IMetaDataObserver} that handles MetaData changes.
 *
 * @see IMetaDataObserver
 * @since 2021-05-15
 */
public class AlarmListSlice extends AbilitySlice implements IMetaDataObserver {
    MetaDataBinding binding = null;
    ListContainer listContainer;
    AlarmListProvider itemProvider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        createComponent();
    }

    private void createComponent() {
        // making binding request
        MetaDataRequestInfo request = new MetaDataRequestInfo.Builder()
                .setRequestSource("ClockMetaData",
                        "dataability:///com.huawei.metadatabindingdemo.db.AlarmsDataAbility")
                .setSyncRequest("ClockMetaData", false)
                .build();
        Component mainComponent;
        try {
            // request binding
            binding = AlarmlistsliceMetaDataBinding.requestBinding(this, request, this);
            // get binding layout component
            mainComponent = binding.getLayoutComponent();
        } catch (DataSourceConnectionException e) {
            mainComponent = LayoutScatter.getInstance(this)
                    .parse(ResourceTable.Layout_default_error, null, false);
        }
        setUIContent((ComponentContainer) mainComponent);

        // initialize the list container and item provider
        listContainer = (ListContainer) mainComponent.findComponentById(ResourceTable.Id_list_view);
        itemProvider = new AlarmListProvider(this);
        listContainer.setItemClickedListener(itemProvider);

        // insert an alarm list item.
        mainComponent.findComponentById(ResourceTable.Id_title_area_add_icon)
                .setClickedListener(component -> {
                    AlarmsOperation.insertAnAlarm(binding);
                });

        DependentLayout dependentLayout = (DependentLayout) mainComponent.findComponentById(
                ResourceTable.Id_title_area_back_icon_hot_area);
        dependentLayout.setClickedListener(component -> this.terminate());
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onDataLoad(List<MetaData> metaDatas, MetaDataRequestInfo.RequestItem requestItem) {
        if (metaDatas == null || requestItem == null) {
            return;
        }
        // add loaded metadatas to ListContainer.
        if (listContainer != null) {
            itemProvider.initData(createAlarms(this, metaDatas));
            listContainer.setItemProvider(itemProvider);
        }
    }

    @Override
    public void onDataChange(List<MetaData> addedMetaData, List<MetaData> updatedMetaData,
        List<MetaData> deletedMetaData, MetaDataRequestInfo.RequestItem requestItem) {
        // in this case we only handle the added metadatas.
        if (addedMetaData == null) {
            return;
        }
        // add added metadatas to ListContainer.
        itemProvider.addItems(createAlarms(this, addedMetaData));
    }

    /**
     * Init alarms data
     *
     * @param context ability slice
     * @param dataList clock row mete data list
     * @return category list
     */
    private List<AlarmRow> createAlarms(AbilitySlice context, List<MetaData> dataList) {
        List<AlarmRow> list = new ArrayList<>();
        for (MetaData metaData : dataList) {
            AlarmRow item = new AlarmRow(context, metaData);
            list.add(item);
        }
        return list;
    }
}
