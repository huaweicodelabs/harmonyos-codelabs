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

import static ohos.security.SystemPermission.DISTRIBUTED_DATASYNC;

import com.huawei.codelab.slice.MainAbilitySlice;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.window.service.WindowManager;
import ohos.bundle.IBundleManager;

/**
 * ability主入口
 *
 * @since 2021-02-26
 */
public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        // 禁止软件盘弹出
        getWindow().setLayoutFlags(WindowManager.LayoutConfig.MARK_ALT_FOCUSABLE_IM,
                WindowManager.LayoutConfig.MARK_ALT_FOCUSABLE_IM);
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());

        if (verifySelfPermission(DISTRIBUTED_DATASYNC) != IBundleManager.PERMISSION_GRANTED) {
            // 没有权限
            if (canRequestPermission(DISTRIBUTED_DATASYNC)) {
                // 弹框
                requestPermissionsFromUser(
                        new String[]{DISTRIBUTED_DATASYNC}, 0);
            }
        }
    }
}
