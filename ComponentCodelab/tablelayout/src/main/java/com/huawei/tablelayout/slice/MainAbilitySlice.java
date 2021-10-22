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

package com.huawei.tablelayout.slice;

import com.huawei.tablelayout.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.TableLayout;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final int TIALOG_OFFSET_X = 0;
    private static final int TIALOG_OFFSET_Y = 180;
    private Text info;
    private Button call;
    private Button clear;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_table_layout);
        initComponent();
        setClickedListener();
    }

    /**
     * 初始化组件
     */
    private void initComponent() {
        info = (Text) findComponentById(ResourceTable.Id_info);
        call = (Button) findComponentById(ResourceTable.Id_call);
        clear = (Button) findComponentById(ResourceTable.Id_clear);
    }

    /**
     * 设置点击事件
     */
    private void setClickedListener() {
        call.setClickedListener(component -> showTips());

        clear.setClickedListener(component -> info.setText(""));

        TableLayout table = (TableLayout) findComponentById(ResourceTable.Id_table);
        int childNum = table.getChildCount();
        for (int index = 0; index < childNum; index++) {
            setClickListener(table, index);
        }
    }

    private void setClickListener(TableLayout table, int index) {
        Button child = (Button)(table.getComponentAt(index));
        child.setClickedListener(component -> {
            if (component instanceof Button) {
                Button button = (Button)component;
                info.setText(info.getText() + button.getText());
            }
        });
    }

    private void showTips() {
        String toastInfo = info.getText();
        if (toastInfo == null || toastInfo.isEmpty()) {
            toastInfo = "Please enter the number!";
        } else {
            toastInfo = "Call " + info.getText();
        }
        new ToastDialog(getContext())
                .setText(toastInfo)
                .setAlignment(LayoutAlignment.CENTER)
                .setOffset(TIALOG_OFFSET_X, TIALOG_OFFSET_Y)
                .show();
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
