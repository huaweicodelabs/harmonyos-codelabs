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

import com.huawei.cookbooks.slice.HealthSlice;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;

public class MainAbility extends Ability {
    private static final String DISTRIBUTED_DATASYNC = "ohos.permission.DISTRIBUTED_DATASYNC";

    private static final int PERMISSION_CODE = 20201203;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(HealthSlice.class.getName());
        requestPermission();
    }

    private void requestPermission() {
        if (verifySelfPermission(DISTRIBUTED_DATASYNC) != IBundleManager.PERMISSION_GRANTED) {
            // has no permission
            if (canRequestPermission(DISTRIBUTED_DATASYNC)) {
                // toast
                requestPermissionsFromUser(new String[] {DISTRIBUTED_DATASYNC}, PERMISSION_CODE);
            }
        }
    }
}
