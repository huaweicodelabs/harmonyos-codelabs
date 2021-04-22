/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

import com.huawei.codelab.utils.LogUtils;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

import java.util.Map;

/**
 * 功能描述
 *
 * @since 2021-02-25
 */
public class MyRemoteProxy implements IRemoteBroker {
    /**
     * 远端响应成功的标识
     */
    public static final int ERR_OK = 0;
    private static final String TAG = MyRemoteProxy.class.getSimpleName();
    // request successful flag
    private final IRemoteObject remote;

    /**
     * MyRemoteProxy
     *
     * @param remote remote
     */
    public MyRemoteProxy(IRemoteObject remote) {
        this.remote = remote;
    }

    @Override
    public IRemoteObject asObject() {
        return remote;
    }

    /**
     * 发送数据到远端
     *
     * @param requestType 请求类型
     * @param paramMap 参数
     * @return 返回
     */
    public int senDataToRemote(int requestType, Map paramMap) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);
        int ec = 1;

        try {
            if (paramMap.get("inputString") instanceof String) {
                String inputString = (String) paramMap.get("inputString");
                data.writeInt(requestType);
                data.writeString(inputString);
                remote.sendRequest(requestType, data, reply, option);
            }
            if (paramMap.get("move") instanceof String) {
                String moveString = (String) paramMap.get("move");
                data.writeInt(requestType);
                data.writeString(moveString);
                remote.sendRequest(requestType, data, reply, option);
            }

            ec = reply.readInt();
            if (ec != ERR_OK) {
                LogUtils.error(TAG, "RemoteException:");
            }
        } catch (RemoteException e) {
            LogUtils.error(TAG, "RemoteException:");
        } finally {
            ec = ERR_OK;
            data.reclaim();
            reply.reclaim();
        }
        return ec;
    }
}
