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

package com.huawei.datepicker.slice;

import com.huawei.datepicker.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.DatePicker;
import ohos.agp.components.Text;

import java.util.Locale;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice {
    private DatePicker datePicker;
    private Text textDate;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_data_picker);
        initComponent();
        initDate();
        setValueChangedListener();
    }

    /**
     * 初始化组件
     */
    private void initComponent() {
        datePicker = (DatePicker) findComponentById(ResourceTable.Id_date_pick);
        textDate = (Text) findComponentById(ResourceTable.Id_text_date);
    }

    /**
     * 显示初始日期
     */
    private void initDate() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        textDate.setText(String.format(Locale.ROOT,"%02d/%02d/%4d", day, month, year));
    }

    /**
     * 为DatePicker设置订阅事件
     */
    private void setValueChangedListener() {
        datePicker.setValueChangedListener(
                (picker, year, monthOfYear, dayOfMonth) ->
                        textDate.setText(String.format(Locale.ROOT,"%02d/%02d/%4d", dayOfMonth, monthOfYear, year))
        );
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
