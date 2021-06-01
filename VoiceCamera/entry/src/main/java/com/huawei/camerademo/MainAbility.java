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

package com.huawei.camerademo;

import static com.huawei.camerademo.utils.PermissionBridge.EVENT_PERMISSION_DENIED;
import static com.huawei.camerademo.utils.PermissionBridge.EVENT_PERMISSION_GRANTED;
import static ohos.bundle.IBundleManager.PERMISSION_GRANTED;

import com.huawei.camerademo.slice.MainAbilitySlice;
import com.huawei.camerademo.utils.PermissionBridge;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.utils.Color;
import ohos.security.SystemPermission;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * MainAbility
 *
 * @since 2021-03-08
 */
public class MainAbility extends Ability {
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        requestCameraPermission();
        getWindow().setStatusBarColor(Color.BLACK.getValue());
    }

    private void requestCameraPermission() {
        List<String> permissions =
                new LinkedList<String>(
                        Arrays.asList(
                                SystemPermission.WRITE_USER_STORAGE,
                                SystemPermission.READ_USER_STORAGE,
                                SystemPermission.CAMERA,
                                SystemPermission.DISTRIBUTED_DATASYNC,
                                SystemPermission.MICROPHONE));
        permissions.removeIf(
            permission ->
                        verifySelfPermission(permission) == PERMISSION_GRANTED || !canRequestPermission(permission));

        if (!permissions.isEmpty()) {
            requestPermissionsFromUser(permissions.toArray(new String[permissions.size()]), PERMISSION_REQUEST_CODE);
        } else {
            PermissionBridge.getHandler().sendEvent(EVENT_PERMISSION_GRANTED);
        }
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return;
        }
        for (int grantResult : grantResults) {
            if (grantResult != PERMISSION_GRANTED) {
                PermissionBridge.getHandler().sendEvent(EVENT_PERMISSION_DENIED);
                terminateAbility();
                return;
            }
        }
        PermissionBridge.getHandler().sendEvent(EVENT_PERMISSION_GRANTED);
    }
}
