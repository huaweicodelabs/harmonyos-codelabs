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

package com.huawei.codelab.util;

import ohos.app.Context;
import ohos.bundle.IBundleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * PermissionsUtils
 *
 * @since 2021-03-12
 */
public class PermissionsUtils {
    private static final String TAG = PermissionsUtils.class.getName();

    private static final int PERMISSION_REQUEST_CODE = 1;

    private static PermissionsUtils permissionsUtils;

    private RequestListener requestListener;

    private PermissionsUtils() {
    }

    /**
     * 获取PermissionsUtils单例
     *
     * @return PermissionsUtils
     */
    public static synchronized PermissionsUtils getInstance() {
        if (permissionsUtils == null) {
            permissionsUtils = new PermissionsUtils();
        }
        return permissionsUtils;
    }

    /**
     * requestPermissions
     *
     * @param context context
     * @param permissions 需要申请的权限集合
     */
    public void requestPermissions(Context context, String[] permissions) {
        List<String> applyPermissions = new ArrayList<>(0);
        for (String element : permissions) {
            if (context.verifySelfPermission(element) != IBundleManager.PERMISSION_GRANTED) {
                if (context.canRequestPermission(element)) {
                    applyPermissions.add(element);
                }
            }
        }

        if (!applyPermissions.isEmpty()) {
            context.requestPermissionsFromUser(applyPermissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }

    public void setRequestListener(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    /**
     * 在MainAbility中的onRequestPermissionsFromUserResult方法会调用此方法
     *
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return;
        }
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == IBundleManager.PERMISSION_GRANTED) {
                if (requestListener != null) {
                    requestListener.permissionGranted(permissions[i]);
                }
            } else {
                LogUtils.info(TAG, "user deny permission:" + permissions[i]);
            }
        }
    }

    /**
     * 权限申请结果回调
     *
     * @since 2021-03-12
     */
    public interface RequestListener {
        /**
         * permissionGranted
         *
         * @param permission 类型为string
         */
        void permissionGranted(String permission);
    }
}
