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

import com.huawei.codelab.handle.Handle;
import com.huawei.codelab.utils.CalculAngle;
import com.huawei.codelab.utils.LogUtil;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

/**
 * 代理
 *
 * @since 2021-03-15
 */
public class GameRemoteProxy implements IRemoteBroker {
    private static final String TAG = "GameRemoteProxy";

    private static final int ERR_OK = 0;

    private final IRemoteObject remote;

    private final CalculAngle calculAngle;

    private final String localDeviceId; // 本机设备Id

    private final Handle handle;

    /**
     * 功能描述
     *
     * @param remote remote
     * @param localDeviceId localDeviceId
     * @param calculAngle calculAngle
     * @param handle handle
     * @since 2021-03-16
     */
    public GameRemoteProxy(IRemoteObject remote, String localDeviceId, CalculAngle calculAngle, Handle handle) {
        this.remote = remote;
        this.calculAngle = calculAngle;
        this.localDeviceId = localDeviceId;
        this.handle = handle;
    }

    @Override
    public IRemoteObject asObject() {
        return remote;
    }

    /**
     * 远程通信
     *
     * @param requestType 请求类型
     */
    public void senDataToRemote(int requestType) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        try {
            data.writeString(localDeviceId);
            data.writeInt(calculAngle.getAngle());
            data.writeInt(handle.getIsAbtnClick());
            data.writeInt(handle.getIsBbtnClick());
            data.writeInt(handle.getIsPause());
            data.writeInt(handle.getIsStart());
            MessageOption option = new MessageOption(MessageOption.TF_SYNC);
            remote.sendRequest(requestType, data, reply, option);
            int ec = reply.readInt();
            if (ec != ERR_OK) {
                LogUtil.error(TAG, "ec != ERR_OK RemoteException");
            }
        } catch (RemoteException e) {
            LogUtil.error(TAG, "RemoteException");
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }
}
