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

import com.huawei.codelab.slice.RightAbilitySlice;

import ohos.aafwk.ability.continuation.DeviceConnectState;
import ohos.aafwk.ability.continuation.ExtraParams;
import ohos.aafwk.ability.continuation.IContinuationDeviceCallback;
import ohos.aafwk.ability.continuation.IContinuationRegisterManager;
import ohos.aafwk.ability.continuation.RequestCallback;
import ohos.app.Context;

/**
 * Device Selection List
 *
 * @since 2021-09-10
 */
public class DeviceDialog {
    // context Object
    private final Context context;

    // 获取流转任务管理服务管理类
    private final IContinuationRegisterManager continuationRegisterManager;

    // Ability token returned after the transfer task management service is registered
    private int abilityToken;

    // Device ID returned after a user selects a device in the device list
    private String selectDeviceId;

    // 设置流转任务管理服务设备状态变更的回调
    private final IContinuationDeviceCallback callback = new IContinuationDeviceCallback() {
        @Override
        public void onDeviceConnectDone(String str, String str1) {
            // Set the device ID after the user selects the device
            selectDeviceId = str;
            continuationRegisterManager.updateConnectStatus(abilityToken, selectDeviceId,
                    DeviceConnectState.CONNECTED.getState(), null);
            returnDeviceId();
        }

        @Override
        public void onDeviceDisconnectDone(String str) {
        }
    };

    // 设置注册流转任务管理服务回调
    private final RequestCallback requestCallback = new RequestCallback() {
        @Override
        public void onResult(int result) {
            abilityToken = result;
        }
    };

    /**
     * Initialize the DeviceDialog, set the transfer task management service management class,
     * and register the transfer task management service management class.
     *
     * @param continuationRegisterManager continuationRegisterManager
     * @param slice slice
     * @since 2021-09-10
     */
    public DeviceDialog(IContinuationRegisterManager continuationRegisterManager, Context slice) {
        this.continuationRegisterManager = continuationRegisterManager;
        this.context = slice;
        // 注册
        registerManager();
    }

    // 注册流转任务管理服务管理类
    private void registerManager() {
        // 增加过滤条件
        ExtraParams params = new ExtraParams();
        String[] devTypes = new String[]{ExtraParams.DEVICETYPE_SMART_PAD, ExtraParams.DEVICETYPE_SMART_PHONE};
        params.setDevType(devTypes);
        continuationRegisterManager.register(context.getBundleName(), params, callback, requestCallback);
    }

    /**
     * Open the device selection box
     *
     * @since 2021-09-10
     */
    public void showDeviceList() {
        // 设置过滤设备类型
        ExtraParams params = new ExtraParams();
        String[] devTypes = new String[]{ExtraParams.DEVICETYPE_SMART_PAD, ExtraParams.DEVICETYPE_SMART_PHONE};
        params.setDevType(devTypes);
        // 注册
        continuationRegisterManager.register(context.getBundleName(), params, callback, requestCallback);
        // 显示选择设备列表
        continuationRegisterManager.showDeviceList(abilityToken, params, null);
    }

    // Return the device ID
    private void returnDeviceId() {
        RightAbilitySlice.setDeviceId(selectDeviceId);
    }

    /**
     * Disconnecting the Transfer Task Management Service
     *
     * @since 2021-09-10
     */
    public void clearRegisterManager() {
        // 解注册流转任务管理服务
        continuationRegisterManager.unregister(abilityToken, null);
        // 断开流转任务管理服务连接
        continuationRegisterManager.disconnect();
    }
}
