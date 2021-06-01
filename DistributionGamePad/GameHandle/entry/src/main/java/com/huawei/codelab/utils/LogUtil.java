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

package com.huawei.codelab.utils;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * Log Util
 *
 * @since 2021-01-11
 */
public class LogUtil {
    private static final String TAG_LOG = "LogUtil";

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(0, 0, LogUtil.TAG_LOG);

    private static final String LOG_FORMAT = "%{public}s: %{public}s";

    private LogUtil() {
    }

    /**
     * Print info log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void info(String tag, String msg) {
        HiLog.info(LABEL_LOG, LOG_FORMAT, tag, msg);
    }

    /**
     * Print info log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void error(String tag, String msg) {
        HiLog.info(LABEL_LOG, LOG_FORMAT, tag, msg);
    }
}
