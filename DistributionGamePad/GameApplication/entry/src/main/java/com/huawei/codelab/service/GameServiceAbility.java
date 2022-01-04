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

import com.huawei.codelab.game.Handle;
import com.huawei.codelab.slice.MainAbilitySlice;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteObject;

/**
 *  game service ability
 *
 * @since 2021-03-15
 *
 */
public class GameServiceAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");

    private final GameRemoteObject remoteObject = new GameRemoteObject();
    private TaskDispatcher taskDispatcher;

    @Override
    public void onStart(Intent intent) {
        HiLog.error(LABEL_LOG, "GameServiceAbility::onStart");
        super.onStart(intent);
        taskDispatcher = this.getUITaskDispatcher();
    }

    @Override
    public void onBackground() {
        super.onBackground();
        HiLog.info(LABEL_LOG, "GameServiceAbility::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.info(LABEL_LOG, "GameServiceAbility::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean isRestart, int startId) {
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        super.onStart(intent);
        return remoteObject;
    }

    @Override
    public void onDisconnect(Intent intent) {
    }

    /**
     *  game remote object
     *
     * @since 2021-03-15
     *
     */
    private class GameRemoteObject extends RemoteObject implements IRemoteBroker {

        GameRemoteObject() {
            super("descriptor");
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            String deviceId = data.readString();
            taskDispatcher.syncDispatch(() -> syncDispatchRequest(deviceId, data));
            return true;
        }

        private void syncDispatchRequest(String deviceId, MessageParcel data) {
            int angle = data.readInt();
            int buttonA = data.readInt();
            int buttonB = data.readInt();
            int pause = data.readInt();
            int start = data.readInt();
            for (Handle handle: MainAbilitySlice.getHandles()) {
                if (deviceId.equals(handle.getDeviceId())) {
                    handle.operation(angle, buttonA, buttonB, pause, start);
                    break;
                }
            }
        }
    }
}