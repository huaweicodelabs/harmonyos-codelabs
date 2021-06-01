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

package com.huawei.maildemo.utils;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.Locale;

/**
 * Log util function
 *
 * @since 2021-02-04
 */
public class LogUtil {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, "MailDemo");

    private static final String LOG_FORMAT = "%{public}s: %{public}s";

    private LogUtil() {
        /* Do nothing */
    }

    /**
     * Print debug log
     *
     * @param className class name
     * @param msg log message
     */
    public static void debug(String className, String msg) {
        HiLog.debug(LABEL_LOG, LOG_FORMAT, className, msg);
    }

    /**
     * Print info log
     *
     * @param className class name
     * @param msg log message
     */
    public static void info(String className, String msg) {
        HiLog.info(LABEL_LOG, LOG_FORMAT, className, msg);
    }

    /**
     * Print info log
     *
     * @param classType class name
     * @param format format
     * @param args args
     */
    public static void info(Class<?> classType, final String format, Object... args) {
        String buffMsg = String.format(Locale.ROOT, format, args);
        HiLog.info(LABEL_LOG, LOG_FORMAT, classType == null ? "null" : classType.getSimpleName(), buffMsg);
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
