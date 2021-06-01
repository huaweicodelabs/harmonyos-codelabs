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

import com.huawei.codelab.slice.NewsDetailAbilitySlice;
import com.huawei.codelab.slice.NewsListAbilitySlice;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;

/**
 * News ability
 *
 * @since 2020-12-04
 */
public class NewsAbility extends Ability {
    private static final String PERMISSION_DATASYNC = "ohos.permission.DISTRIBUTED_DATASYNC";
    private static final int MY_PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(NewsListAbilitySlice.class.getName());
        addActionRoute("action.detail", NewsDetailAbilitySlice.class.getName());

        if (verifySelfPermission(PERMISSION_DATASYNC) != IBundleManager.PERMISSION_GRANTED) {
            if (canRequestPermission(PERMISSION_DATASYNC)) {
                requestPermissionsFromUser(new String[] {PERMISSION_DATASYNC}, MY_PERMISSION_REQUEST_CODE);
            }
        }
    }
}
