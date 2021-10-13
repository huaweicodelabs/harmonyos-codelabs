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

import ohos.app.Context;

/**
 * permission manager
 *
 * @ClassName: PermissionManager
 * @Description: java class description
 */
public class PermissionManager {
    private final Context mContext;

    /**
     * permission manager
     *
     * @param context context
     */
    public PermissionManager(Context context) {
        this.mContext = context;
    }

    /**
     * check permissions
     *
     * @param permissions permissions
     * @param requestCode requestCode
     */
    public void checkPermissions(String[] permissions, int requestCode) {
        if (permissions.length > 0) {
            mContext.requestPermissionsFromUser(permissions, requestCode);
        }
    }

}
