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

package com.huawei.codelab.service;

import com.huawei.codelab.constants.EventConstants;
import com.huawei.codelab.proxy.ConnectManagerIml;
import com.huawei.codelab.utils.LogUtils;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

/**
 * RemoteService
 *
 * @since 2021-02-25
 */
public class RemoteService extends Ability {
    private static final String TAG = RemoteService.class.getSimpleName();
    private final MyRemote remote = new MyRemote();

    @Override
    public void onStart(Intent intent) {
        LogUtils.info(TAG, "RemoteService::onStart");
        super.onStart(intent);
    }

    @Override
    public void onBackground() {
        super.onBackground();
        LogUtils.info(TAG, "RemoteService::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.info(TAG, "RemoteService::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean isRestart, int startId) {
    }

    @Override
    protected IRemoteObject onConnect(Intent intent) {
        super.onConnect(intent);
        return remote.asObject();
    }

    @Override
    public void onDisconnect(Intent intent) {
        LogUtils.info(TAG, "RemoteService::onDisconnect");
    }

    /**
     * RemoteObject
     *
     * @since 2021-02-25
     */
    public class MyRemote extends RemoteObject implements IRemoteBroker {
        private MyRemote() {
            super("===MyService_Remote");
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            LogUtils.info(TAG, "===onRemoteRequest......");
            int requestType = data.readInt();
            String inputString = data.readString();
            sendEvent(requestType, inputString);
            return true;
        }
    }

    private void sendEvent(int requestType, String string) {
        LogUtils.info(TAG, "sendEvent......");
        try {
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withAction(EventConstants.SCREEN_REMOTE_CONTROLL_EVENT)
                    .build();
            intent.setOperation(operation);
            if (requestType == ConnectManagerIml.REQUEST_SEND_MOVE) {
                intent.setParam("move", string);
            } else {
                intent.setParam("inputString", string);
            }
            intent.setParam("requestType", requestType);
            CommonEventData eventData = new CommonEventData(intent);
            CommonEventManager.publishCommonEvent(eventData);
        } catch (RemoteException e) {
            LogUtils.error(TAG, "publishCommonEvent occur exception.");
        }
    }
}