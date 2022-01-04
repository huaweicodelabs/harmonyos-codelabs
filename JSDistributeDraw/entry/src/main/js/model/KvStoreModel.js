/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import distributedData from '@ohos.data.distributedData';

const STORE_ID = 'DrawBoard_kvstore';

export default class KvStoreModel {
  kvManager;
  kvStore;

  constructor() {
  }

  createKvStore(callback) {
    if (typeof (this.kvStore) === 'undefined') {
      var config = {
        bundleName: 'com.huawei.jsdistributedraw',
        userInfo: {
          userId: '0',
          userType: 0
        }
      };

      // 创建KVManager对象实例
      distributedData.createKVManager(config).then((manager) => {
        this.kvManager = manager;
        var options = {
          createIfMissing: true,
          encrypt: false,
          backup: false,
          autoSync: true,
          kvStoreType: 1,
          schema: '',
          securityLevel: 1,
        };

        // 创建并获取KVStore数据库实例
        this.kvManager.getKVStore(STORE_ID, options).then((store) => {
          this.kvStore = store;
          callback();
        });
      });
    } else {
      callback();
    }
  }

  // 添加指定类型键值对到数据库
  put(key, value) {
    if (typeof (this.kvStore) === 'undefined') {
      return;
    }

    this.kvStore.put(key, value);
  }

  // 订阅指定类型的数据变更通知
  setDataChangeListener(callback) {
    this.createKvStore(() => {
      this.kvStore.on('dataChange', 1, (data) => {
        if (data.updateEntries.length > 0) {
          callback(data);
        }
      });
    });
  }
}