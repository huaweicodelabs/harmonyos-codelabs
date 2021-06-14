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

import com.huawei.codelab.MapIdlInterfaceProxy;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;
import ohos.rpc.MessageParcel;
import ohos.rpc.MessageOption;

public abstract class MapIdlInterfaceStub extends RemoteObject implements IMapIdlInterface {
    private static final String DESCRIPTOR = "com.huawei.codelab.IMapIdlInterface";

    private static final int COMMAND_ACTION = IRemoteObject.MIN_TRANSACTION_ID + 0;

    private static final int ERR_OK = 0;
    private static final int ERR_RUNTIME_EXCEPTION = -1;

    public MapIdlInterfaceStub(
        /* [in] */ String descriptor) {
        super(descriptor);
    }

    @Override
    public IRemoteObject asObject() {
        return this;
    }

    public static IMapIdlInterface asInterface(IRemoteObject object) {
        if (object == null) {
            return null;
        }

        IMapIdlInterface result = null;
        IRemoteBroker broker = object.queryLocalInterface(DESCRIPTOR);
        if (broker != null) {
            if (broker instanceof IMapIdlInterface) {
                result = (IMapIdlInterface)broker;
            }
        } else {
            result = new MapIdlInterfaceProxy(object);
        }

        return result;
    }

    @Override
    public boolean onRemoteRequest(
        /* [in] */ int code,
        /* [in] */ MessageParcel data,
        /* [out] */ MessageParcel reply,
        /* [in] */ MessageOption option) throws RemoteException {
        String token = data.readInterfaceToken();
        if (!DESCRIPTOR.equals(token)) {
            return false;
        }
        switch (code) {
            case COMMAND_ACTION: {
                String actionType = data.readString();
                String actionContent = data.readString();
                action(actionType, actionContent);
                reply.writeNoException();
                return true;
            }
            default:
                return super.onRemoteRequest(code, data, reply, option);
        }
    }
};

