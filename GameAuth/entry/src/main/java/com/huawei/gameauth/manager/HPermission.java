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

package com.huawei.gameauth.manager;

import ohos.app.Context;
import ohos.bundle.IBundleManager;
import ohos.security.SystemPermission;

/**
 * Permission.
 *
 * @since 2021-06-23
 */
public class HPermission {
    /**
     * Permission Request No.
     */
    private static final int PERMISSION_CODE = 20210926;

    /**
     * Permission Request Result.
     */
    private OnPermissionStateListener onPermissionStateListener;

    /**
     * requestPermissions.
     *
     * @param context context
     * @param listener listener
     */
    public void requestPermissions(final Context context,
        final OnPermissionStateListener listener) {
        this.onPermissionStateListener = listener;
        if (context.verifySelfPermission(SystemPermission.DISTRIBUTED_DATASYNC)
                != IBundleManager.PERMISSION_GRANTED) {
            // 应用未被授予权限
            if (context.canRequestPermission(
                    SystemPermission.DISTRIBUTED_DATASYNC)) {
                // 是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
                context.requestPermissionsFromUser(
                        new String[]{SystemPermission.DISTRIBUTED_DATASYNC},
                        PERMISSION_CODE);
            }  // 显示应用需要权限的理由，提示用户进入设置授权
        } else {
            // 权限已被授予
            if (onPermissionStateListener != null) {
                onPermissionStateListener.onPermissionGranted();
            }
        }
    }

    /**
     * Permission callback function.
     *
     * @param requestCode  code
     * @param grantResults result
     */
    public void onRequestPermissionsFromUserResult(
            final int requestCode, final int[] grantResults) {
        // 匹配requestPermissions的requestCode
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == IBundleManager.PERMISSION_GRANTED) {
                if (onPermissionStateListener != null) {
                    onPermissionStateListener.onPermissionGranted();
                }
            }
        }
    }

    /**
     * OnPermissionStateListener.
     */
    public interface OnPermissionStateListener {
        /**
         * onPermissionGranted.
         */
        void onPermissionGranted();
    }
}
