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

package com.huawei.metadatabindingdemo;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * This is a util HiLog Wrapper class. Supplies info and error log print.
 *
 * @since 2021-05-15
 */
public class MyLog {
    private static final String TAG_LOG = "MyLog";

    private static final String MY_TAG = "MyTag";

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(0, 0, TAG_LOG);

    private static final String LOG_FORMAT = "%{public}s: %{public}s";

    /**
     * Print info log
     *
     * @param msg log message
     */
    public static void info(String msg) {
        HiLog.info(LABEL_LOG, LOG_FORMAT, MY_TAG, msg);
    }

    /**
     * Print error log
     *
     * @param msg log message
     */
    public static void error(String msg) {
        HiLog.error(LABEL_LOG, LOG_FORMAT, MY_TAG, msg);
    }
}
