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

package com.huawei.cookbooks.remoteAbility.impl;

import com.huawei.cookbooks.remoteAbility.ISelectResult;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;

/**
 * 跨设备启动PA
 *
 * @since 2021-04-06
 */
public class ISelectResultImplStartPa implements ISelectResult {
    @Override
    public void onSelectResult(String deviceId, Context context) {
        if (deviceId != null) {
            Intent intentToStartPA = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId(deviceId)
                    .withBundleName("com.huawei.cookbooks")
                    .withAbilityName("com.huawei.cookbooks.ServiceAbility")
                    .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                    .build();
            intentToStartPA.setOperation(operation);
            context.startAbility(intentToStartPA, 1);
        }
    }
}
