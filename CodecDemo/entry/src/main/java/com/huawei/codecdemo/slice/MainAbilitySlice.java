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

package com.huawei.codecdemo.slice;

import com.huawei.codecdemo.CodecAbility;
import com.huawei.codecdemo.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;

/**
 * MainAbilitySlice
 *
 * @since 2021-04-09
 */
public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initView();
    }

    private void initView() {
        if (findComponentById(ResourceTable.Id_live_button) instanceof Button) {
            Button liveButton = (Button) findComponentById(ResourceTable.Id_live_button);
            liveButton.setClickedListener(component -> {
                Intent intent = new Intent();
                Operation operation =
                        new Intent.OperationBuilder()
                                .withBundleName(getBundleName())
                                .withAbilityName(CodecAbility.class.getName())
                                .build();
                intent.setOperation(operation);
                startAbility(intent);
            });
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
