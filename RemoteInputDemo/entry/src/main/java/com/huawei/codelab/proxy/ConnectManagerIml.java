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

package com.huawei.codelab.proxy;

import com.huawei.codelab.service.RemoteService;
import com.huawei.codelab.utils.LogUtils;

import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.bundle.ElementName;
import ohos.rpc.IRemoteObject;

import java.util.Map;

/**
 * ConnectManagerIml
 *
 * @since 2021-02-25
 */
public class ConnectManagerIml implements ConnectManager {
    /**
     * 发送数据
     */
    public static final int REQUEST_SEND_DATA = 1;

    /**
     * 请求查询
     */
    public static final int REQUEST_SEND_SEARCH = 2;

    /**
     * 方向移动通知
     */
    public static final int REQUEST_SEND_MOVE = 3;

    private static final String TAG = ConnectManagerIml.class.getName();

    private static ConnectManager instance;
    private MyRemoteProxy proxy;

    /**
     * ConnectManager
     *
     * @return getInstance
     */
    public static synchronized ConnectManager getInstance() {
        if (instance == null) {
            instance = new ConnectManagerIml();
        }
        return instance;
    }

    /**
     * connectPa
     *
     * @param context context
     * @param deviceId deviceId
     */
    @Override
    public void connectPa(Context context, String deviceId) {
        if (deviceId != null && !deviceId.trim().isEmpty()) {
            Intent connectPaIntent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId(deviceId)
                    .withBundleName(context.getBundleName())
                    .withAbilityName(RemoteService.class.getName())
                    .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                    .build();
            connectPaIntent.setOperation(operation);
            IAbilityConnection conn = new IAbilityConnection() {
                @Override
                public void onAbilityConnectDone(ElementName elementName, IRemoteObject remote, int resultCode) {
                    LogUtils.info(TAG, "===connectRemoteAbility done");
                    proxy = new MyRemoteProxy(remote);
                }

                @Override
                public void onAbilityDisconnectDone(ElementName elementName, int resultCode) {
                    LogUtils.info(TAG, "onAbilityDisconnectDone......");
                    proxy = null;
                }
            };
            context.connectAbility(connectPaIntent, conn);
        }
    }

    @Override
    public void sendRequest(int requestType, Map<String, String> params) {
        if (proxy != null) {
            proxy.senDataToRemote(requestType, params);
        }
    }
}
