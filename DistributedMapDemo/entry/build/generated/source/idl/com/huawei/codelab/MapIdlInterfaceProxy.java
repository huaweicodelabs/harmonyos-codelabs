/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
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

package com.huawei.codelab;

import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.rpc.MessageParcel;
import ohos.rpc.MessageOption;

public class MapIdlInterfaceProxy implements IMapIdlInterface {
    private static final String DESCRIPTOR = "com.huawei.codelab.IMapIdlInterface";

    private static final int COMMAND_ACTION = IRemoteObject.MIN_TRANSACTION_ID + 0;

    private final IRemoteObject remote;
    private static final int ERR_OK = 0;

    public MapIdlInterfaceProxy(
        /* [in] */ IRemoteObject remote) {
        this.remote = remote;
    }

    @Override
    public IRemoteObject asObject() {
        return remote;
    }

    @Override
    public void action(
        /* [in] */ String actionType,
        /* [in] */ String actionContent) throws RemoteException {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(DESCRIPTOR);
        data.writeString(actionType);
        data.writeString(actionContent);

        try {
            remote.sendRequest(COMMAND_ACTION, data, reply, option);
            reply.readException();
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }
};

