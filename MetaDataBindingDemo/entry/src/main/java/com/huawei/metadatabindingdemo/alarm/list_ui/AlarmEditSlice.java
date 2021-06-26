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
import com.huawei.metadatabindingdemo.metadatabinding.AlarmdetailMetaDataBinding;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.TimePicker;
import ohos.agp.components.element.ShapeElement;
import ohos.mp.metadata.binding.metadata.MetaData;

/**
 * Alarm Edition slice.
 * This slice doesn't request a datasource to bind, it binds with an exist Metadata, the 'clockMeta'.
 * This metadata is passed in from AlarmListSlice, indicates a list item's content.
 *
 * @since 2021-05-15
 */
public class AlarmEditSlice extends AbilitySlice {
    private static final int CORNER_RADIUS = 12;
    MetaData clockMeta;
    AlarmdetailMetaDataBinding metaBinding;

    /**
     * Constructor
     *
     * @param clockMeta MetaData
     */
    public AlarmEditSlice(MetaData clockMeta) {
        this.clockMeta = clockMeta;
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        metaBinding = AlarmdetailMetaDataBinding.createBinding(this, clockMeta);
        Component comp = metaBinding.getLayoutComponent();
        DependentLayout dependentLayout = (DependentLayout) comp.findComponentById(ResourceTable
                .Id_title_area_back_icon_hot_area);
        dependentLayout.setClickedListener(component -> {
            this.terminate();
            clockMeta.rollback();
        });

        comp.findComponentById(ResourceTable.Id_save).setClickedListener(component -> {
            clockMeta.commit();
            clockMeta.stash();
            this.terminate();
        });

        TimePicker timePicker = (TimePicker) comp.findComponentById(ResourceTable.Id_time_picker);
        timePicker.set24Hour(false);
        timePicker.showSecond(false);

        Component nameComponent = comp.findComponentById(ResourceTable.Id_name_edit);
        ShapeElement shapeElement = (ShapeElement) nameComponent.getBackgroundElement();
        // only one item in category, set all corner radius and bottom margin 12vp
        shapeElement.setCornerRadiiArray(new float[]{CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS,
            CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS});
        nameComponent.setBackground(shapeElement);
        clockMeta.stash();
        setUIContent((ComponentContainer) comp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        clockMeta.rollback();
    }
}
