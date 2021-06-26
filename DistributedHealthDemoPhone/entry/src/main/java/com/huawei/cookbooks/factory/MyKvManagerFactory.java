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

package com.huawei.cookbooks.factory;

import com.huawei.cookbooks.util.LogUtils;

import ohos.data.distributed.common.KvManager;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.data.distributed.common.KvStoreException;

public class MyKvManagerFactory {
    private static KvManager instance = null;

    private MyKvManagerFactory() { }

    public static KvManager getInstance(KvManagerConfig config) {
        if (instance == null) {
            synchronized (MyKvManagerFactory.class) {
                if (instance == null) {
                    try {
                        instance = KvManagerFactory.getInstance().createKvManager(config);
                    } catch (KvStoreException e) {
                        LogUtils.info("KvStoreException", e.getMessage());
                    }
                }
            }
        }
        return instance;
    }
}
