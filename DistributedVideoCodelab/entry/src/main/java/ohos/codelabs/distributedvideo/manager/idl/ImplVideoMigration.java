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
import ohos.rpc.RemoteException;

/**
 * the main page
 *
 * @since 2021-09-07
 *
 */

public interface ImplVideoMigration extends IRemoteBroker {
    /**
     * flyIn
     *
     * @param startTimemiles startTimemiles
     * @throws RemoteException RemoteException
     */
    void flyIn(
            /* [in] */ int startTimemiles) throws RemoteException;

    /**
     * playControl
     *
     * @param controlCode controlCode
     * @param extras extras
     * @throws RemoteException RemoteException
     */
    void playControl(
            /* [in] */ int controlCode,
            /* [in] */ int extras) throws RemoteException;

    /**
     * flyOut
     *
     * @return return the status code
     * @throws RemoteException RemoteException
     */
    int flyOut() throws RemoteException;
}

