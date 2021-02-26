/*
 * Copyright (c) 2021 Huawei Device Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
import ohos.agp.components.Component;
import ohos.agp.window.dialog.ToastDialog;

/**
 * The main AbilitySlice.
 *
 * @since 2021-01-11
 */
public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
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

    private void setClickedListener(Component.ClickedListener clickListener, Component...components) {
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
            default:
                break;
        }
        abilitySliceJump(className);
    }

    private void abilitySliceJump(String name) {
        if (name == null || "".equals(name)) {
            return;
        }
        try {
            Class abilitySliceClass = Class.forName(name);
            Object object = abilitySliceClass.newInstance();
            if (object instanceof AbilitySlice) {
                present((AbilitySlice) object, new Intent());
            }
        } catch (Exception e) {
            new ToastDialog(getContext())
                    .setText("Error!")
                    .show();
        }
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