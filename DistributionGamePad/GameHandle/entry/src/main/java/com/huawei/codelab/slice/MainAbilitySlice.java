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

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.devices.SelectDeviceDialog;
import com.huawei.codelab.utils.ScreenUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.aafwk.content.Operation;
import ohos.distributedschedule.interwork.DeviceInfo;

import java.util.List;

/**
 * 手柄slice
 *
 * @since 2021-03-15
 */
public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        ScreenUtils.setWindows();
        showDialog(); // 设备列表弹窗
    }

    // 设备列表弹窗
    private void showDialog() {
        SelectDeviceDialog dialogDeviceMsg =
            new SelectDeviceDialog(this, new SelectDeviceDialog.SelectResultListener() {
                /**
                 * 获取设备列表信息
                 *
                 * @param deviceInfos 设备信息
                 */
                @Override
                public void callBack(List<DeviceInfo> deviceInfos) {
                    for (DeviceInfo deviceInfo : deviceInfos) {
                        startHandleAbility(deviceInfo.getDeviceId());
                    }
                }
            });
        dialogDeviceMsg.initDialog().show();
    }

    private void startHandleAbility(String deviceId) {
        Intent intent = new Intent();
        IntentParams params = new IntentParams();
        params.setParam("deviceId", deviceId);
        intent.setParams(params);
        Operation operation = new Intent.OperationBuilder().withDeviceId("")
            .withBundleName("com.huawei.codelab")
            .withAbilityName("com.huawei.codelab.HandleAbility")
            .build();
        intent.setOperation(operation);
        startAbility(intent);
    }
}
