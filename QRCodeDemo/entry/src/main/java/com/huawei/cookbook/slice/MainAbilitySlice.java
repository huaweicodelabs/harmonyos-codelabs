/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
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

package com.huawei.cookbook.slice;

import com.huawei.cookbook.ScanAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import com.huawei.cookbook.ResourceTable;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;

/**
 * AI QR CODE.
 *
 * @since 2021-01-18
 */
public class MainAbilitySlice extends AbilitySlice {
    /**
     * 生命周期.
     *
     * @param intent intent
     */
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initViewEvent();
    }

    /**
     * 初始化事件.
     */
    private void initViewEvent() {
        presentSlice(ResourceTable.Id_codeGeneration,
                new CodeGenerationAbilitySlice());
        presentSlice(ResourceTable.Id_codeIdentification,
                new CodeIdentificationAbilitySlice());
        codeScanning();
    }

    /**
     * open code scan Ability
     */
    private void codeScanning() {
        findComponentById(ResourceTable.Id_codeScanning).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName(getBundleName())
                        .withAbilityName(ScanAbility.class)
                        .build();
                intent.setOperation(operation);
                startAbility(intent);
            }
        });
    }

    /**
     * 跳转页面.
     *
     * @param resId       按钮id
     * @param targetSlice 对应的AbilitySlice
     */
    private void presentSlice(int resId, AbilitySlice targetSlice) {
        findComponentById(resId).setClickedListener(
                component -> present(targetSlice, new Intent()));
    }
}
