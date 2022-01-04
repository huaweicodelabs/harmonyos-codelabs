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

package com.huawei.cookbooks.task;

import ohos.data.distributed.common.Entry;
import ohos.data.distributed.user.SingleKvStore;

import java.util.List;

public class PushDataTask implements Runnable {
    private final List<Entry> entries;

    private final List<Entry> entries2;

    private final SingleKvStore singleKvStore;

    public PushDataTask(List<Entry> entries, List<Entry> entries2, SingleKvStore singleKvStore) {
        this.entries = entries;
        this.entries2 = entries2;
        this.singleKvStore = singleKvStore;
    }

    @Override
    public void run() {
        for (Entry entry : entries) {
            if (!entries2.contains(entry)) {
                singleKvStore.putString(entry.getKey(), entry.getValue().getString());
            }
        }
    }
}
