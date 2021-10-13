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

package com.huawei.codecdemo;

import static ohos.bundle.IBundleManager.PERMISSION_GRANTED;

import com.huawei.codecdemo.slice.MainAbilitySlice;
import com.huawei.codecdemo.utils.LogUtil;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.security.SystemPermission;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * MainAbility
 *
 * @since 2021-04-09
 */
public class MainAbility extends Ability {
    private static final String TAG = MainAbilitySlice.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        requestCameraPermission();
    }

    private void requestCameraPermission() {
        List<String> permissions =
                new LinkedList<>(Arrays.asList(SystemPermission.CAMERA));
        permissions.removeIf(permission ->
                verifySelfPermission(permission) == PERMISSION_GRANTED || !canRequestPermission(permission));
        if (!permissions.isEmpty()) {
            requestPermissionsFromUser(permissions.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return;
        }
        for (int grantResult : grantResults) {
            if (grantResult != PERMISSION_GRANTED) {
                LogUtil.info(TAG, grantResult + " is denied , Some functions may be affected.");
            }
        }
    }
}
