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

package com.huawei.codelab.manager;

import ohos.app.Context;
import ohos.bundle.IBundleManager;
import ohos.security.SystemPermission;


/**
 * Permission
 *
 * @since 2021-06-23
 */
public class HPermission {
    private static final int PERMISSION_CODE = 20210326;

    private OnPermissionStateListener onPermissionStateListener;

    /**
     * requestPermissions
     *
     * @param context context
     */
    public void requestPermissions(Context context, OnPermissionStateListener onPermissionStateListener) {
        this.onPermissionStateListener = onPermissionStateListener;
        if (context.verifySelfPermission(SystemPermission.DISTRIBUTED_DATASYNC) != IBundleManager.PERMISSION_GRANTED) {
            // 应用未被授予权限
            if (context.canRequestPermission(SystemPermission.DISTRIBUTED_DATASYNC)) {
                // 是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
                context.requestPermissionsFromUser(
                        new String[]{SystemPermission.DISTRIBUTED_DATASYNC}, PERMISSION_CODE);
            } else {
                // 显示应用需要权限的理由，提示用户进入设置授权
            }
        } else {
            // 权限已被授予
            if (onPermissionStateListener != null) {
                onPermissionStateListener.onPermissionGranted();
            }
        }
    }

    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                // 匹配requestPermissions的requestCode
                if (grantResults.length > 0
                        && grantResults[0] == IBundleManager.PERMISSION_GRANTED) {
                    if (onPermissionStateListener != null) {
                        onPermissionStateListener.onPermissionGranted();
                    }
                } else {

                }
                return;
            }
        }
    }

    /**
     * OnPermissionStateListener
     */
    public interface OnPermissionStateListener {
        void onPermissionGranted();
    }
}
