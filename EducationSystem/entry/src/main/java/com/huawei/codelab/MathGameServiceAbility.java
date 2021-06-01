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

import com.huawei.codelab.utils.CommonData;
import com.huawei.codelab.utils.LogUtil;

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
 * MathGameServiceAbility
 *
 * @since 2021-01-11
 */
public class MathGameServiceAbility extends Ability {
    private static final String TAG = CommonData.TAG + MathGameServiceAbility.class.getSimpleName();

    private MathRemote remote = new MathRemote();

    @Override
    public void onStart(Intent intent) {
        LogUtil.info(TAG, "RemoteServiceAbility::onStart");
        super.onStart(intent);
    }

    @Override
    public void onBackground() {
        super.onBackground();
        LogUtil.info(TAG, "RemoteServiceAbility::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.info(TAG, "RemoteServiceAbility::onStop");
    }

    @Override
    protected IRemoteObject onConnect(Intent intent) {
        super.onConnect(intent);
        return remote.asObject();
    }

    private void sendEvent(boolean[] isLastPoint, float[] pointsX, float[] pointsY) {
        LogUtil.info(TAG, "sendEvent......");
        try {
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder().withAction(CommonData.MATH_DRAW_EVENT).build();
            intent.setOperation(operation);
            intent.setParam(CommonData.KEY_POINT_X, pointsX);
            intent.setParam(CommonData.KEY_POINT_Y, pointsY);
            intent.setParam(CommonData.KEY_IS_LAST_POINT, isLastPoint);
            CommonEventData eventData = new CommonEventData(intent);
            CommonEventManager.publishCommonEvent(eventData);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "publishCommonEvent occur exception.");
        }
    }

    /**
     * MathRemote Establish a remote connection
     */
    public class MathRemote extends RemoteObject implements IRemoteBroker {
        private static final int ERR_OK = 0;

        private MathRemote() {
            super("MathRemote");
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            LogUtil.info(TAG, "onRemoteRequest......");
            float[] pointsX = data.readFloatArray();
            float[] pointsY = data.readFloatArray();
            boolean[] isLastPoint = data.readBooleanArray();
            reply.writeInt(ERR_OK);
            sendEvent(isLastPoint, pointsX, pointsY);
            return true;
        }
    }
}