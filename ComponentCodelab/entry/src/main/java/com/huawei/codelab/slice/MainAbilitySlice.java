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

package com.huawei.codelab.slice;

import com.huawei.codelab.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;

/**
 * The main AbilitySlice.
 *
 * @since 2021-01-11
 */
public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private static final String MainAbility_TABLIST = "com.huawei.tablist.MainAbility";
    private static final String MainAbility_CHECKBOX = "com.huawei.checkbox.MainAbility";
    private static final String MainAbility_DATEPICKER = "com.huawei.datepicker.MainAbility";
    private static final String MainAbility_DEPENDENTLAYOUT = "com.huawei.dependentlayout.MainAbility";
    private static final String MainAbility_DIRECTIONALLAYOUT = "com.huawei.directionallayout.MainAbility";
    private static final String MainAbility_LISTCONTAINER = "com.huawei.listcontainer.MainAbility";
    private static final String MainAbility_RADIOCONTINER = "com.huawei.radiocontainer.MainAbility";
    private static final String MainAbility_STACKLAYOUT = "com.huawei.stacklayout.MainAbility";
    private static final String MainAbility_TABLELAYOUT = "com.huawei.tablelayout.MainAbility";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        setClickedListener(this, findComponentById(ResourceTable.Id_tab_list),
                findComponentById(ResourceTable.Id_list_container),
                findComponentById(ResourceTable.Id_radio_container),
                findComponentById(ResourceTable.Id_checkbox),
                findComponentById(ResourceTable.Id_date_picker),
                findComponentById(ResourceTable.Id_directional_layout),
                findComponentById(ResourceTable.Id_dependent_layout),
                findComponentById(ResourceTable.Id_stack_layout),
                findComponentById(ResourceTable.Id_table_layout)
        );
    }

    private void setClickedListener(Component.ClickedListener clickListener, Component... components) {
        for (Component component : components) {
            if (component == null) {
                continue;
            }
            component.setClickedListener(clickListener);
        }
    }

    @Override
    public void onClick(Component component) {
        String className = "";
        switch (component.getId()) {
            case ResourceTable.Id_tab_list:
                className = MainAbility_TABLIST;
                break;
            case ResourceTable.Id_checkbox:
                className = MainAbility_CHECKBOX;
                break;
            case ResourceTable.Id_date_picker:
                className = MainAbility_DATEPICKER;
                break;
            case ResourceTable.Id_dependent_layout:
                className = MainAbility_DEPENDENTLAYOUT;
                break;
            case ResourceTable.Id_directional_layout:
                className = MainAbility_DIRECTIONALLAYOUT;
                break;
            case ResourceTable.Id_list_container:
                className = MainAbility_LISTCONTAINER;
                break;
            case ResourceTable.Id_radio_container:
                className = MainAbility_RADIOCONTINER;
                break;
            case ResourceTable.Id_stack_layout:
                className = MainAbility_STACKLAYOUT;
                break;
            case ResourceTable.Id_table_layout:
                className = MainAbility_TABLELAYOUT;
                break;
            default:
                break;
        }
        abilitySliceJump(className);
    }

private void abilitySliceJump(String name) {
    Intent intent = new Intent();
    Operation operation = new Intent.OperationBuilder()
            .withDeviceId("")
            .withBundleName(getBundleName())
            .withAbilityName(name)
            .build();
    intent.setOperation(operation);
    startAbility(intent);
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