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

package com.huawei.cookbook;

import com.huawei.cookbook.slice.MainAbilitySlice;
import com.huawei.cookbook.utils.PermissionBridge;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import ohos.security.SystemPermission;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MainAbility
 */
public class MainAbility extends Ability {
    /**
     * permission handler granted
     */
    private static final int EVENT_PERMISSION_GRANTED = 0x0000023;

    /**
     * permission handler denied
     */
    private static final int EVENT_PERMISSION_DENIED = 0x0000024;
    private static final int PERMISSION_REQUEST_CODE = 0;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        requestPermission();
    }

    // 向用户申请相关权限的授权
    private void requestPermission() {
        String[] permissions = {
            // 计步器权限
            SystemPermission.ACTIVITY_MOTION
        };
        List<String> permissionFiltereds = Arrays.stream(permissions)
                .filter(permission -> verifySelfPermission(permission) != IBundleManager.PERMISSION_GRANTED)
                .collect(Collectors.toList());
        if (permissionFiltereds.isEmpty()) {
            PermissionBridge.getHandler().sendEvent(EVENT_PERMISSION_GRANTED);
            return;
        }
        // 向用户申请相关权限的授权
        requestPermissionsFromUser(permissionFiltereds.toArray(new String[0]),
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions == null || permissions.length == 0 || grantResults == null || grantResults.length == 0) {
            return;
        }
        for (int grantResult : grantResults) {
            if (grantResult != IBundleManager.PERMISSION_GRANTED) {
                PermissionBridge.getHandler().sendEvent(EVENT_PERMISSION_DENIED);
                terminateAbility();
                return;
            }
        }
        PermissionBridge.getHandler().sendEvent(EVENT_PERMISSION_GRANTED);
    }
}