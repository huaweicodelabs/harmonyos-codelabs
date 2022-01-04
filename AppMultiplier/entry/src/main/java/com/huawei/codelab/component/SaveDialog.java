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

package com.huawei.codelab.component;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.slice.RightAbilitySlice;

import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.RadioContainer;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;

/**
 * Save or Replace
 *
 * @since 2021-09-10
 */
public class SaveDialog {
    private static final int HEIGHT = 300;
    private static final int WIDTH = 500;
    private static int flag = 0; // 新增、替换标识
    private static final RadioContainer.CheckedStateChangedListener
            CHECKED_STATE_CHANGED_LISTENER = (radioContainer, index) -> flag = index;

    private SaveDialog() {
    }

    /**
     * Open the save mode selection box
     *
     * @param context context
     * @since 2021-09-10
     */
    public static void open(Context context) {
        CommonDialog commonDialog = new CommonDialog(context);
        commonDialog.setSize(WIDTH, HEIGHT);
        // Obtain the ability_calendar_dialog.xml file.
        Component componentXml = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_ability_selected_dialog,
                null, false);
        // 单选按钮
        Component componentRadio = componentXml.findComponentById(ResourceTable.Id_radio_container);
        if (componentRadio instanceof RadioContainer) {
            RadioContainer radioContainer = (RadioContainer) componentRadio;
            radioContainer.setMarkChangedListener(CHECKED_STATE_CHANGED_LISTENER);
        }
        // 获取确认按钮
        Component componentConfirm = componentXml.findComponentById(ResourceTable.Id_button_confirm);
        componentConfirm.setClickedListener(listener -> {
            RightAbilitySlice.saveOrReplaceImage(flag);
            commonDialog.destroy();
        });
        // 获取取消按钮
        Component componentCancel = componentXml.findComponentById(ResourceTable.Id_button_cancel);
        componentCancel.setClickedListener(listener -> commonDialog.destroy());
        commonDialog.setContentCustomComponent(componentXml);
        commonDialog.show();
    }
}
