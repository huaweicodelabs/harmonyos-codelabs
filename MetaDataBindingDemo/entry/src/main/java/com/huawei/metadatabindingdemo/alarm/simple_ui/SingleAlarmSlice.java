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

package com.huawei.metadatabindingdemo.alarm.simple_ui;

import com.huawei.metadatabindingdemo.ResourceTable;
import com.huawei.metadatabindingdemo.alarm.db.Alarm;
import com.huawei.metadatabindingdemo.alarm.db.AlarmsOperation;
import com.huawei.metadatabindingdemo.alarm.metadata.ClockRowMetaData;
import com.huawei.metadatabindingdemo.metadatabinding.SinglealarmMetaDataBinding;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.TimePicker;
import ohos.mp.metadata.binding.databinding.DataSourceConnectionException;
import ohos.mp.metadata.binding.databinding.MetaDataBinding;
import ohos.mp.metadata.binding.databinding.MetaDataRequestInfo;

/**
 * This AbilitySlice displays an alarm setting page.
 *
 * @since 2021-05-15
 */
public class SingleAlarmSlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        createComponent();
    }

    private void createComponent() {
        // in this case, if no records exists in database, we create one.
        Alarm alarm = AlarmsOperation.queryFirst(this);
        if (alarm == null) {
            AlarmsOperation.insert(this);
        }
        // making binding request.
        MetaDataRequestInfo request = new MetaDataRequestInfo.Builder()
                .setMetaDataClass("ClockMetaData", ClockRowMetaData.class)
                .setSyncRequest("ClockMetaData", true)
                .build();
        MetaDataBinding binding;
        Component mainComponent;
        try {
            // request binding.
            binding = SinglealarmMetaDataBinding.requestBinding(this, request, null);
            // getting binding layout component.
            mainComponent = binding.getLayoutComponent();
        } catch (DataSourceConnectionException e) {
            mainComponent = LayoutScatter.getInstance(this)
                    .parse(ResourceTable.Layout_default_error, null, false);
        }
        setUIContent((ComponentContainer) mainComponent);

        TimePicker timePicker = (TimePicker) mainComponent.findComponentById(ResourceTable.Id_time_picker);
        if (timePicker == null) {
            return;
        }
        timePicker.set24Hour(false);
        timePicker.showSecond(false);
        DependentLayout dependentLayout = (DependentLayout) mainComponent.findComponentById(ResourceTable
                .Id_title_area_back_icon_hot_area);
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
}
