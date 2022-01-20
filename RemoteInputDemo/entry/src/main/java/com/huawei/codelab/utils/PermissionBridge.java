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

import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

/**
 * PermissionBridge
 *
 * @since 2021-03-08
 */
public class PermissionBridge {
    /**
     * permission handler granted
     */
    public static final int EVENT_PERMISSION_GRANTED = 0x0000023;

    /**
     * permission handler denied
     */
    public static final int EVENT_PERMISSION_DENIED = 0x0000024;

    private static final String TAG = PermissionBridge.class.getSimpleName();

    private static OnPermissionStateListener onPermissionStateListener;

    private static final EventHandler handler = new EventHandler(EventRunner.current()) {
        @Override
        protected void processEvent(InnerEvent event) {
            switch (event.eventId) {
                case EVENT_PERMISSION_GRANTED:
                    onPermissionStateListener.onPermissionGranted();
                    break;
                case EVENT_PERMISSION_DENIED:
                    onPermissionStateListener.onPermissionDenied();
                    break;
                default:
                    LogUtils.info(TAG, "EventHandler Undefined Event");
                    break;
            }
        }
    };

    /**
     * setOnPermissionStateListener
     *
     * @param permissionStateListener OnPermissionStateListener
     */
    public void setOnPermissionStateListener(OnPermissionStateListener permissionStateListener) {
        onPermissionStateListener = permissionStateListener;
    }

    /**
     * OnPermissionStateListener
     *
     * @since 2021-03-08
     */
    public interface OnPermissionStateListener {
        /**
         * onPermissionGranted
         */
        void onPermissionGranted();

        /**
         * onPermissionDenied
         */
        void onPermissionDenied();
    }

    /**
     * getHandler
     *
     * @return EventHandler
     */
    public static EventHandler getHandler() {
        return handler;
    }
}
