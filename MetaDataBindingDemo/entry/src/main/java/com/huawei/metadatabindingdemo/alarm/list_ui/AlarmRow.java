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

import com.huawei.metadatabindingdemo.alarm.metadata.ClockRowMetaData;
import com.huawei.metadatabindingdemo.metadatabinding.AlarmrowMetaDataBinding;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.mp.metadata.binding.metadata.MetaData;

/**
 * A row item of Alarm ListContainer. For the item layout, see: src/main/resources/base/layout/alarm_row.xml.
 *
 * @since 2021-05-15
 */
public class AlarmRow {
    private final AbilitySlice context;
    private final ClockRowMetaData clockMeta;

    /**
     * AlarmRow constructor
     *
     * @param context Context
     * @param clockMeta MetaData
     */
    public AlarmRow(AbilitySlice context, MetaData clockMeta) {
        this.context = context;
        this.clockMeta = (ClockRowMetaData) clockMeta;
    }

    /**
     * create AlarmRow component.
     *
     * @return a AlarmRow component
     */
    public Component createComponent() {
        // bind the metadata with the Alarm row item component.
        AlarmrowMetaDataBinding metaBinding = AlarmrowMetaDataBinding.createBinding(context, clockMeta);
        Component comp = metaBinding.getLayoutComponent();
        comp.setTag(metaBinding);
        return comp;
    }

    /**
     * bind clockMetaData with component.
     *
     * @param component a AlarmRow Component
     */
    public void bindComponent(Component component) {
        AlarmrowMetaDataBinding metaBinding = (AlarmrowMetaDataBinding) component.getTag();
        metaBinding.reBinding(component, clockMeta);
    }

    /**
     * go to AlarmEditSlice slice.
     */
    public void onClick() {
        context.present(new AlarmEditSlice(clockMeta), new Intent());
    }
}
