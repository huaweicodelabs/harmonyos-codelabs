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

package com.huawei.codelab.utils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.bundle.ElementName;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;

/**
 * AbilityMgr
 *
 * @since 2021-02-26
 */
public class AbilityMgr {
    private final AbilitySlice abilitySlice;

    /**
     * Constructor
     *
     * @param abilitySlice abilitySlice
     * @since 2021-02-25
     */
    public AbilityMgr(AbilitySlice abilitySlice) {
        super();
        this.abilitySlice = abilitySlice;
    }

    /**
     * returnMainAbility
     *
     * @param bundleName bundleName
     * @param abilityName abilityName
     * @since 2021-02-25
     */
    public void returnMainAbility(String bundleName, String abilityName) {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(bundleName)
                .withAbilityName(abilityName)
                .build();
        intent.setOperation(operation);
        abilitySlice.startAbility(intent);
    }

    /**
     * openRemoteAbility
     *
     * @param deviceId deviceId
     * @param bundleName bundleName
     * @param abilityName abilityName
     * @since 2021-02-25
     */
    public void openRemoteAbility(String deviceId, String bundleName, String abilityName) {
        Intent intent = new Intent();
        String localDeviceId = KvManagerFactory.getInstance()
                .createKvManager(new KvManagerConfig(abilitySlice)).getLocalDeviceInfo().getId();
        intent.setParam("localDeviceId", localDeviceId);

        Operation operation = new Intent.OperationBuilder()
                .withDeviceId(deviceId)
                .withBundleName(bundleName)
                .withAbilityName(abilityName)
                .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                .build();
        intent.setOperation(operation);
        abilitySlice.startAbility(intent);
    }

    /**
     * playMovie
     *
     * @param bundleName bundleName
     * @param abilityName abilityName
     * @since 2021-02-25
     */
    public void playMovie(String bundleName, String abilityName) {
        Intent intent = new Intent();
        ElementName element = new ElementName("", bundleName, abilityName);
        intent.setElement(element);
        abilitySlice.startAbility(intent);
    }
}
