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

import com.huawei.codelab.slice.HandleAbilitySlice;
import com.huawei.codelab.utils.LogUtil;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteObject;

/**
 * 远程通信service
 *
 * @since 2021-03-15
 */
public class GameServiceAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");

    private final GameRemoteObject remoteObject = new GameRemoteObject();

    @Override
    public void onStart(Intent intent) {
        HiLog.error(LABEL_LOG, "GameServiceAbility::onStart");
        super.onStart(intent);
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
     * 功能描述
     *
     * @since 2021-03-16
     */
    private static class GameRemoteObject extends RemoteObject implements IRemoteBroker {
        private static final int ERR_OK = 0;

        private static final String TAG = "GameService";

        GameRemoteObject() {
            super("descriptor");
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            LogUtil.info(TAG, "onRemoteRequest......");
            int score = data.readInt();
            reply.writeInt(ERR_OK);
            HandleAbilitySlice.setScore(score);
            return true;
        }
    }
}