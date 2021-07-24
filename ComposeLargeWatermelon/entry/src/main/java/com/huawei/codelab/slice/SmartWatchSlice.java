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

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;

import com.huawei.codelab.utils.DialogUtil;
import com.huawei.codelab.manager.HPermission;
import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.MainAbility;

/**
 * IDL client
 *
 * @since 2021-05-26
 */
public class SmartWatchSlice extends AbilitySlice {
    /**
     * param
     */
    public static final String CODE = "code";

    // agreeing type
    public static final int TYPE1 = 1;

    // rejects type
    public static final int TYPE2 = 2;

    // delay close ability
    private static final int DELAY = 2000;

    // Remote Device ID
    private String remoteDeviceId;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_smart_watch);
        remoteDeviceId = intent.getStringParam(MainAbilitySlice.DEVICE_ID);
        initViewData();
    }

    /**
     * Initialize View Data
     */
    private void initViewData() {
        findComponentById(ResourceTable.Id_yes_btn).setClickedListener(component -> {
            sendMessage(TYPE1);
        });
        findComponentById(ResourceTable.Id_no_btn).setClickedListener(component -> {
            sendMessage(TYPE2);
        });
    }

    /**
     * Sends data to the server.
     *
     * @param type type
     */
    private void sendMessage(int type) {
        HPermission hPermission = ((MainAbility) getAbility()).getPermission();
        hPermission.requestPermissions(this, () -> {
            // button Enabled
            findComponentById(ResourceTable.Id_yes_btn).setEnabled(false);
            findComponentById(ResourceTable.Id_no_btn).setEnabled(false);

            startRemoteAbility(type);
        });
    }

    /**
     * start Remote Ability.
     *
     * @param type type
     */
    private void startRemoteAbility(int type) {
        DialogUtil.showToast(getContext(), type==TYPE1?"允许玩游戏":"已拒绝玩游戏");
        Intent intent = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withDeviceId(remoteDeviceId == null ? "" : remoteDeviceId)
                        .withBundleName(getBundleName())
                        .withAbilityName(MainAbility.class.getName())
                        .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                        .build();
        intent.setOperation(operation);
        intent.setParam(CODE, type);
        startAbility(intent);
        getUITaskDispatcher().delayDispatch(() -> terminateAbility(), DELAY);
    }
}
