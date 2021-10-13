/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.huawei.cookbook.util;

import ohos.agp.render.render3d.BuildConfig;
import ohos.agp.utils.TextTool;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.Locale;

/**
 * log utils
 */
public class LogUtils {
    private static final String TAG = "net_work";
    private static final String TAG_LOG = "";

    private static final int DOMAIN_ID = 0xD000F00;

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, DOMAIN_ID, LogUtils.TAG_LOG);

    private static final String LOG_FORMAT = "%{public}s: %{public}s";

    private LogUtils() { }

    /**
     * Print debug log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void debug(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            HiLog.debug(LABEL_LOG, LOG_FORMAT, tag, msg);
        }
    }

    /**
     * Print info log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void info(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            HiLog.info(LABEL_LOG, LOG_FORMAT, tag, msg);
            HiLog.info(LABEL_LOG, msg);
        }
    }

    /**
     * i
     *
     * @param msg msg
     */
    public static void info(String msg) {
        if (BuildConfig.DEBUG) {
            if (!TextTool.isNullOrEmpty(msg)) {
                HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, msg);
            }
        }
    }

    /**
     * Print warn log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void warn(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            HiLog.warn(LABEL_LOG, LOG_FORMAT, tag, msg);
        }
    }

    /**
     * Print error log
     *
     * @param tag log tag
     * @param msg log message
     */
    public static void error(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            HiLog.error(LABEL_LOG, LOG_FORMAT, tag, msg);
        }
    }

    /**
     * e
     *
     * @param msg msg
     */
    public static void erro(String msg) {
        if (BuildConfig.DEBUG) {
            if (!TextTool.isNullOrEmpty(msg)) {
                HiLog.error(LABEL_LOG, LOG_FORMAT, TAG, msg);
            }
        }
    }

    /**
     * error
     *
     * @param msg msg
     */
    public static void error(String msg) {
        if (BuildConfig.DEBUG) {
            HiLog.error(LABEL_LOG, LOG_FORMAT, TAG, msg);
        }
    }

    /**
     * error
     *
     * @param tag tag
     * @param format format
     * @param args args
     */
    public static void error(String tag, final String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String buffMsg = String.format(Locale.ROOT, format, args);
            HiLog.error(LABEL_LOG, LOG_FORMAT, tag, buffMsg);
        }
    }

    /**
     * info
     *
     * @param tag tag
     * @param format format
     * @param args args
     */
    public static void info(String tag, final String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String buffMsg = String.format(Locale.ROOT, format, args);
            HiLog.info(LABEL_LOG, LOG_FORMAT, tag, buffMsg);
        }
    }
}
