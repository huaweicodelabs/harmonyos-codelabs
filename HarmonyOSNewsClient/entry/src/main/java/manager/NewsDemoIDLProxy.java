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
 
 package manager;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

public class NewsDemoIDLProxy implements INewsDemoIDL {
    private static final String DESCRIPTOR = "com.huawei.codelab.INewsDemoIDL";

    private static final int COMMAND_TRAN_SHARE = IRemoteObject.MIN_TRANSACTION_ID;

    private final IRemoteObject remote;

    NewsDemoIDLProxy(
            /* [in] */ IRemoteObject remote) {
        this.remote = remote;
    }

    @Override
    public IRemoteObject asObject() {
        return remote;
    }

    @Override
    public void tranShare(
            /* [in] */ String title,
            /* [in] */ String reads,
            /* [in] */ String likes,
            /* [in] */ String content,
            /* [in] */ String image)
            throws RemoteException {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(DESCRIPTOR);
        data.writeString(title);
        data.writeString(reads);
        data.writeString(likes);
        data.writeString(content);
        data.writeString(image);

        try {
            remote.sendRequest(COMMAND_TRAN_SHARE, data, reply, option);
            reply.readException();
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }
}
