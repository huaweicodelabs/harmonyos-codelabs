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

package com.huawei.harmonyaudiodemo.slice;

import com.huawei.harmonyaudiodemo.MainAbility;
import com.huawei.harmonyaudiodemo.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;

/**
 * MainAbilitySlice
 *
 * @since 2021-04-04
 */
public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initView() {
        findComponentById(ResourceTable.Id_sound_music_play_btn).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound_system_play_btn).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound_record_btn).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound_volume_btn).setClickedListener(this);
    }

    @Override
    public void onClick(Component view) {
        Intent intent = new Intent();
        String actionName = "";
        switch (view.getId()) {
            case ResourceTable.Id_sound_music_play_btn:
                actionName = "action.sound.play.music";
                break;
            case ResourceTable.Id_sound_system_play_btn:
                actionName = "action.sound.play.system";
                break;
            case ResourceTable.Id_sound_record_btn:
                actionName = "action.sound.record";
                break;
            case ResourceTable.Id_sound_volume_btn:
                actionName = "action.sound.volume";
                break;
            default:
                break;
        }
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(getBundleName())
                .withAbilityName(MainAbility.class.getName())
                .withAction(actionName)
                .build();
        intent.setOperation(operation);
        startAbility(intent);
    }
}
