/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package manager;

import ohos.rpc.IRemoteBroker;
import ohos.rpc.RemoteException;

/**
 * News Demo IDL
 */
public interface INewsDemoIDL extends IRemoteBroker {
    void tranShare(
            /* [in] */ String title,
            /* [in] */ String reads,
            /* [in] */ String likes,
            /* [in] */ String content,
            /* [in] */ String image)
            throws RemoteException;
}
;
