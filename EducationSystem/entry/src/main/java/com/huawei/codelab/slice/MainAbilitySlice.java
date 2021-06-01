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

import static ohos.security.SystemPermission.DISTRIBUTED_DATASYNC;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.utils.CommonData;
import com.huawei.codelab.utils.LogUtil;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.bundle.IBundleManager;

/**
 * MainAbilitySlice
 *
 * @since 2021-01-11
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final String TAG = CommonData.TAG + MainAbilitySlice.class.getSimpleName();

    private static final int PERMISSION_CODE = 10000000;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        grantPermission();
        initView();
    }

    void grantPermission() {
        if (verifySelfPermission(DISTRIBUTED_DATASYNC) != IBundleManager.PERMISSION_GRANTED) {
            if (canRequestPermission(DISTRIBUTED_DATASYNC)) {
                requestPermissionsFromUser(new String[] {DISTRIBUTED_DATASYNC}, PERMISSION_CODE);
            }
        }
    }

    private void initView() {
        findComponentById(ResourceTable.Id_math_game).setClickedListener(new ButtonClick());
        findComponentById(ResourceTable.Id_picture_game).setClickedListener(new ButtonClick());
    }

    private void mathGame() {
        LogUtil.info(TAG, "Click ResourceTable Id_math_game");
        Intent mathGameIntent = new Intent();
        Operation operationMath = new Intent.OperationBuilder().withBundleName(getBundleName())
            .withAbilityName(CommonData.ABILITY_MAIN)
            .withAction(CommonData.MATH_PAGE)
            .build();
        mathGameIntent.setOperation(operationMath);
        startAbility(mathGameIntent);
    }

    private void pictureGame() {
        LogUtil.info(TAG, "Click ResourceTable Id_picture_game");
        Intent pictureGameIntent = new Intent();
        Operation operationPicture = new Intent.OperationBuilder().withBundleName(getBundleName())
            .withAbilityName(CommonData.ABILITY_MAIN)
            .withAction(CommonData.PICTURE_PAGE)
            .build();
        pictureGameIntent.setOperation(operationPicture);
        startAbility(pictureGameIntent);
    }

    /**
     * ButtonClick
     *
     * @since 2021-01-11
     */
    private class ButtonClick implements Component.ClickedListener {
        @Override
        public void onClick(Component component) {
            int btnId = component.getId();
            switch (btnId) {
                case ResourceTable.Id_math_game:
                    mathGame();
                    break;
                case ResourceTable.Id_picture_game:
                    pictureGame();
                    break;
                default:
                    LogUtil.info(TAG, "Click default");
                    break;
            }
        }
    }
}
