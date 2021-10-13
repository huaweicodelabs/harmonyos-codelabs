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

package com.huawei.codelab;

import static com.huawei.codelab.utils.PermissionBridge.EVENT_PERMISSION_DENIED;
import static com.huawei.codelab.utils.PermissionBridge.EVENT_PERMISSION_GRANTED;
import static ohos.bundle.IBundleManager.PERMISSION_GRANTED;
import static ohos.security.SystemPermission.DISTRIBUTED_DATASYNC;

import com.huawei.codelab.slice.MainAbilitySlice;
import com.huawei.codelab.utils.PermissionBridge;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.window.service.WindowManager;

/**
 * MainAbility
 *
 * @since 2021-02-26
 */
public class MainAbility extends Ability {
    private static final int PERMISSION_REQUEST_CODE = 0;

    @Override
    public void onStart(Intent intent) {
        // 禁止软件盘弹出
        getWindow().setLayoutFlags(WindowManager.LayoutConfig.MARK_ALT_FOCUSABLE_IM,
                WindowManager.LayoutConfig.MARK_ALT_FOCUSABLE_IM);
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());

        reqPermission();
    }

    private void reqPermission() {
        String permission = DISTRIBUTED_DATASYNC;

        if (verifySelfPermission(permission) == PERMISSION_GRANTED) {
            PermissionBridge.getHandler().sendEvent(EVENT_PERMISSION_GRANTED);
        } else {
            requestPermissionsFromUser(new String[]{permission}, PERMISSION_REQUEST_CODE);
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
