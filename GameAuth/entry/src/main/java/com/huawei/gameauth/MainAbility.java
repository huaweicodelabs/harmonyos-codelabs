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

package com.huawei.gameauth;

import ohos.ace.ability.AceAbility;
import ohos.aafwk.content.Intent;
import ohos.ace.ability.LocalParticleAbility;
import ohos.distributedhardware.devicemanager.DeviceManager;

import com.huawei.gameauth.manager.HPermission;
import com.huawei.gameauth.manager.JSInterface;

/**
 * Distributed game authentication.
 *
 * @since 2021-09-01
 */
public class MainAbility extends AceAbility {
    /**
     * Permission.
     */
    private HPermission permission;

    /**
     * JS java.
     */
    private JSInterface jsInterface;

    /**
     * which needs to be invoked in threads.
     */
    private LocalParticleAbility.Callback callback;

    /**
     * onStart.
     *
     * @param intent intent
     */
    @Override
    public void onStart(final Intent intent) {
        super.onStart(intent);
        jsInterface = new JSInterface(this);
        jsInterface.register(this);
    }

    /**
     * onStop.
     */
    @Override
    public void onStop() {
        super.onStop();
        jsInterface.deregister(this);
    }

    /**
     * Request permission and obtain the local NetWorkId.
     */
    public void requestPermissionAndGetNetWorkId() {
        permission = new HPermission();
        permission.requestPermissions(this, this::getNetWorkId);
    }

    /**
     * Obtain the local NetWorkId and return it to the JS.
     * The NetWorkId needs to be obtained through .
     * the DeviceManager.DeviceManagerCallback callback.
     */
    private void getNetWorkId() {
        DeviceManager.createInstance(new DeviceManager.DeviceManagerCallback() {
            @Override
            public void onGet(final DeviceManager deviceManager) {
                String networkId = deviceManager.getLocalDeviceInfo()
                        .getNetworkId();
                jsCallBack(networkId);
            }

            @Override
            public void onDied() {
            }
        });
    }

    /**
     * Asynchronous callback JS.
     *
     * @param networkId device networkId
     */
    private void jsCallBack(final String networkId) {
        if (callback != null) {
            new Thread(() -> callback.reply(networkId)).start();
        }
    }


    /**
     * Permissions.
     *
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
    @Override
    public void onRequestPermissionsFromUserResult(
            final int requestCode, final String[] permissions,
            final int[] grantResults) {
        if (permission != null) {
            permission.onRequestPermissionsFromUserResult(
                    requestCode, grantResults);
        }
    }


    /**
     * start Remote Ability.
     *
     * @param type type
     */
    public void isAllowGameAsync(final int type) {
        permission = new HPermission();
        permission.requestPermissions(this, () -> {
            if (callback != null) {
                new Thread(() -> callback.reply(type)).start();
            }
        });
    }


    /**
     * Setting the Asynchronous JS Callback Interface.
     *
     * @param back Callback interface.
     */
    public void setCallback(final LocalParticleAbility.Callback back) {
        this.callback = back;
    }
}
