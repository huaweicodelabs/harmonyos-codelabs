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

package com.huawei.codelab;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.rpc.RemoteException;

import com.huawei.codelab.slice.MainAbilitySlice;
import com.huawei.codelab.slice.SmartWatchSlice;
import com.huawei.codelab.utils.LogUtils;
import com.huawei.codelab.manager.HPermission;

/**
 * graphic composition
 *
 * @since 2021-04-13
 */
public class MainAbility extends Ability {
    /**
     * action
     */
    public static final String ACTION = "action.smart";
    // Permission
    private HPermission permission;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        addActionRoute(ACTION, SmartWatchSlice.class.getName());

        permission = new HPermission();
    }

    /**
     * get HPermission
     *
     * @return HPermission
     */
    public HPermission getPermission() {
        return permission;
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        permission.onRequestPermissionsFromUserResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int code = intent.getIntParam(SmartWatchSlice.CODE, 0);
        String deviceId = intent.getStringParam(MainAbilitySlice.DEVICE_ID);
        sendCommonEvent(code, deviceId);
    }

    /**
     * send CommonEvent
     *
     * @param code     code
     * @param deviceId deviceId
     */
    private void sendCommonEvent(int code, String deviceId) {
        try {
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withAction(SmartWatchSlice.CODE)
                    .build();
            intent.setOperation(operation);
            intent.setParam(SmartWatchSlice.CODE, code);
            intent.setParam(MainAbilitySlice.DEVICE_ID, deviceId);
            CommonEventData eventData = new CommonEventData(intent);
            CommonEventManager.publishCommonEvent(eventData);
        } catch (RemoteException e) {
            LogUtils.info("error");
        }
    }
}
