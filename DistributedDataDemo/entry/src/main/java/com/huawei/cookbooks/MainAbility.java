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

package com.huawei.cookbooks;

import com.huawei.cookbooks.slice.ContactSlice;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;

/**
 * Distributed database Sample Code
 *
 * @since 2021-01-06
 */
public class MainAbility extends Ability {
    private static final int PERMISSION_CODE = 20201203;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(ContactSlice.class.getName());
        requestPermission();
    }

    // 申请权限
    private void requestPermission() {
        String permission = ohos.security.SystemPermission.DISTRIBUTED_DATASYNC;
        if (verifySelfPermission(permission) != IBundleManager.PERMISSION_GRANTED) {
            // has no permission
            if (canRequestPermission(permission)) {
                // toast
                requestPermissionsFromUser(new String[]{permission}, PERMISSION_CODE);
            }
        }
    }
}
