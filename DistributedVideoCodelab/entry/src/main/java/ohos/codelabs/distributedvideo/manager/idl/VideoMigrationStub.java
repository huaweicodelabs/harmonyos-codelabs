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

import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

/**
 * the main page
 *
 * @since 2020-12-07
 *
 */
public abstract class VideoMigrationStub extends RemoteObject implements ImplVideoMigration {
    private static final String DESCRIPTOR = "com.huawei.codelab.ImplVideoMigration";
    private static final String TAG = "VideoMigrationStub";
    private static final int COMMAND_FLY_IN = IRemoteObject.MIN_TRANSACTION_ID;
    private static final int COMMAND_PLAY_CONTROL = IRemoteObject.MIN_TRANSACTION_ID + 1;
    private static final int COMMAND_FLY_OUT = IRemoteObject.MIN_TRANSACTION_ID + 2;
    private static final int ERR_OK = 0;
    private static final int ERR_RUNTIME_EXCEPTION = -1;

    /**
     * constructor of VideoMigrationStub
     *
     * @param descriptor descriptor
     */
    public VideoMigrationStub(
        /* [in] */ String descriptor) {
        super(descriptor);
    }

    @Override
    public IRemoteObject asObject() {
        return this;
    }

    /**
     * Video Migration
     *
     * @param object IRemoteObject
     * @return ImplVideoMigration
     */
    public static ImplVideoMigration asInterface(IRemoteObject object) {
        ImplVideoMigration result = null;
        if (object == null) {
            return result;
        }
        IRemoteBroker broker = object.queryLocalInterface(DESCRIPTOR);
        if (broker != null) {
            if (broker instanceof ImplVideoMigration) {
                result = (ImplVideoMigration)broker;
            }
        } else {
            result = new VideoMigrationProxy(object);
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
            case COMMAND_FLY_IN: {
                int startTimemiles = data.readInt();
                flyIn(startTimemiles);
                reply.writeNoException();
                return true;
            }
            case COMMAND_PLAY_CONTROL: {
                int controlCode = data.readInt();
                int extras = data.readInt();
                playControl(controlCode, extras);
                reply.writeNoException();
                return true;
            }
            case COMMAND_FLY_OUT: {
                int result;
                result = flyOut();
                reply.writeNoException();
                reply.writeInt(result);
                return true;
            }
            default:
                return super.onRemoteRequest(code, data, reply, option);
        }
    }
}

