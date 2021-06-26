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

package com.huawei.metadatabindingdemo;

import com.huawei.metadatabindingdemo.alarm.list_ui.AlarmListSlice;
import com.huawei.metadatabindingdemo.alarm.simple_ui.SingleAlarmSlice;
import com.huawei.metadatabindingdemo.custom_data_source.NotesSlice;
import com.huawei.metadatabindingdemo.custom_ui.CustomOperatorSlice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;

/**
 * Samples list ability slice, displays 4 buttons as entrance for 4 samples.
 *
 * @since 2021-05-15
 */
public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_main_ability_slice);

        // click and go to the Alarm List sample
        Button listAlarmButton = (Button) findComponentById(ResourceTable.Id_list_alarm_button);
        listAlarmButton.setClickedListener(component -> present(new AlarmListSlice(), new Intent()));

        // click and go to the Single Alarm Setting sample
        Button singleAlarmButton = (Button) findComponentById(ResourceTable.Id_single_alarm_button);
        singleAlarmButton.setClickedListener(component -> present(new SingleAlarmSlice(), new Intent()));

        // click and go to the Customized components and ui-attributes binding sample
        Button customOperatorButton = (Button) findComponentById(ResourceTable.Id_custom_operator_button);
        customOperatorButton.setClickedListener(component -> present(new CustomOperatorSlice(), new Intent()));

        // click and go to the Customized datasource binding sample
        Button notesButton = (Button) findComponentById(ResourceTable.Id_notes_button);
        notesButton.setClickedListener(component -> present(new NotesSlice(), new Intent()));
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
