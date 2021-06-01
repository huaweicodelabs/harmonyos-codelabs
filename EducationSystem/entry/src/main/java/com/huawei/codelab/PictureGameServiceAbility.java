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
 * PictureGameServiceAbility
 *
 * @since 2021-01-11
 */
public class PictureGameServiceAbility extends Ability {
    private static final String TAG = CommonData.TAG + PictureGameServiceAbility.class.getSimpleName();

    private PictureRemote remote = new PictureRemote();

    @Override
    public void onStart(Intent intent) {
        LogUtil.info(TAG, "PictureGameServiceAbility::onStart");
        super.onStart(intent);
    }

    @Override
    public void onBackground() {
        super.onBackground();
        LogUtil.info(TAG, "PictureGameServiceAbility::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.info(TAG, "PictureGameServiceAbility::onStop");
    }

    @Override
    protected IRemoteObject onConnect(Intent intent) {
        super.onConnect(intent);
        return remote.asObject();
    }

    @Override
    public void onDisconnect(Intent intent) {
        LogUtil.info(TAG, "PictureGameServiceAbility::onDisconnect");
    }

    private void sendEvent(int[] imageIndex, int moveImageId, int movePosition) {
        try {
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder().withAction(CommonData.PICTURE_GAME_EVENT).build();
            intent.setOperation(operation);
            intent.setParam(CommonData.KEY_IMAGE_INDEX, imageIndex);
            intent.setParam(CommonData.KEY_MOVE_IMAGE_ID, moveImageId);
            intent.setParam(CommonData.KEY_MOVE_POSITION, movePosition);
            CommonEventData eventData = new CommonEventData(intent);
            CommonEventManager.publishCommonEvent(eventData);
        } catch (RemoteException e) {
            LogUtil.error(TAG, "publishCommonEvent occur exception.");
        }
    }

    /**
     * PictureRemote Establish a remote connection
     *
     * @since 2021-01-12
     */
    public class PictureRemote extends RemoteObject implements IRemoteBroker {
        private static final int ERR_OK = 0;

        private static final int REQUEST_START_ABILITY = 1;

        private PictureRemote() {
            super("PictureRemote");
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            LogUtil.info(TAG, "onRemoteRequest......");
            int[] imageIndexs = data.readIntArray();
            String remoteDeviceId = data.readString();
            boolean isLocal = data.readBoolean();
            int moveImageId = data.readInt();
            int movePosition = data.readInt();
            LogUtil.info(TAG, "receive number:" + imageIndexs.length);
            reply.writeInt(ERR_OK);
            if (code == REQUEST_START_ABILITY) {
                LogUtil.error(TAG, "RemoteServiceAbility::isFirstStart:");
                Intent secondIntent = new Intent();
                Operation operation = new Intent.OperationBuilder().withDeviceId("")
                    .withBundleName(getBundleName())
                    .withAbilityName(CommonData.ABILITY_MAIN)
                    .withAction(CommonData.PICTURE_PAGE)
                    .build();
                secondIntent.setParam(CommonData.KEY_REMOTE_DEVICEID, remoteDeviceId);
                secondIntent.setParam(CommonData.KEY_IS_LOCAL, isLocal);
                secondIntent.setParam(CommonData.KEY_MOVE_IMAGE_ID, moveImageId);
                secondIntent.setParam(CommonData.KEY_MOVE_POSITION, movePosition);
                secondIntent.setParam(CommonData.KEY_IMAGE_INDEX, imageIndexs);
                secondIntent.setOperation(operation);
                startAbility(secondIntent);
            } else {
                sendEvent(imageIndexs, moveImageId, movePosition);
            }
            return true;
        }
    }
}