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

package ohos.codelabs.distributedvideo.manager.idl;

import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

/**
 * the main page
 *
 * @since 2020-12-04
 *
 */
public class VideoMigrationProxy implements ImplVideoMigration {
    private static final String DESCRIPTOR = "com.huawei.codelab.ImplVideoMigration";
    private static final int COMMAND_FLY_IN = IRemoteObject.MIN_TRANSACTION_ID;
    private static final int COMMAND_PLAY_CONTROL = IRemoteObject.MIN_TRANSACTION_ID + 1;
    private static final int COMMAND_FLY_OUT = IRemoteObject.MIN_TRANSACTION_ID + 2;
    private static final int ERR_OK = 0;
    private static final String TAG = "VideoMigrationProxy";

    private final IRemoteObject remote;

    /**
     * constructor of VideoMigrationProxy
     *
     * @param remote remote
     */
    public VideoMigrationProxy(
        /* [in] */ IRemoteObject remote) {
        this.remote = remote;
    }

    @Override
    public IRemoteObject asObject() {
        return remote;
    }

    @Override
    public void flyIn(

        /* [in] */ int startTimemiles) throws RemoteException {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(DESCRIPTOR);
        data.writeInt(startTimemiles);

        try {
            remote.sendRequest(COMMAND_FLY_IN, data, reply, option);
            reply.readException();
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public void playControl(
        /* [in] */ int controlCode,
        /* [in] */ int extras) throws RemoteException {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(DESCRIPTOR);
        data.writeInt(controlCode);
        data.writeInt(extras);

        try {
            remote.sendRequest(COMMAND_PLAY_CONTROL, data, reply, option);
            reply.readException();
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }

    @Override
    public int flyOut() throws RemoteException {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption(MessageOption.TF_SYNC);

        data.writeInterfaceToken(DESCRIPTOR);
        try {
            remote.sendRequest(COMMAND_FLY_OUT, data, reply, option);
            reply.readException();
            return reply.readInt();
        } finally {
            data.reclaim();
            reply.reclaim();
        }
    }
}

