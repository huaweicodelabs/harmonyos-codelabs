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

import com.huawei.codelab.util.LogUtils;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

/**
 * WatchService
 *
 * @since 2021-03-12
 */
public class WatchService extends Ability {
    private static final String TAG = WatchService.class.getSimpleName();

    private final WatchRemote watchRemote = new WatchRemote("WatchRemote");

    @Override
    public void onStart(Intent intent) {
        LogUtils.info(TAG, "WatchService::onStart");
        super.onStart(intent);
    }

    @Override
    public void onBackground() {
        super.onBackground();
        LogUtils.info(TAG, "WatchService::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.info(TAG, "WatchService::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean isRestart, int startId) {
        LogUtils.info(TAG, "WatchService::onCommand");
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        return watchRemote.asObject();
    }

    @Override
    public void onDisconnect(Intent intent) {
        LogUtils.info(TAG, "WatchService::onDisconnect");
    }

    /**
     * WatchRemote
     *
     * @since 2021-03-12
     */
    public class WatchRemote extends MapIdlInterfaceStub {
        private WatchRemote(String descriptor) {
            super(descriptor);
        }

        @Override
        public void action(String actionType, String actionContent) {
            LogUtils.info(TAG, "WatchService::action");
            sendEvent(actionType, actionContent);
        }
    }

    private void sendEvent(String actionType, String actionContent) {
        LogUtils.info(TAG, "WatchService::sendEvent");
        try {
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder().withAction("com.huawei.map").build();
            intent.setOperation(operation);
            intent.setParam("actionType", actionType);
            intent.setParam("actionContent", actionContent);
            CommonEventData eventData = new CommonEventData(intent);
            CommonEventManager.publishCommonEvent(eventData);
        } catch (RemoteException e) {
            LogUtils.info(TAG, "publishCommonEvent occur exception.");
        }
    }
}