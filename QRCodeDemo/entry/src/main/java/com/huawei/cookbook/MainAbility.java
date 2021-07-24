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

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySliceAnimator;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import ohos.security.SystemPermission;

import com.huawei.cookbook.slice.MainAbilitySlice;

/**
 * AI QR CODE.
 *
 * @since 2021-01-18
 */
public class MainAbility extends Ability {
    /**
     * request Code.
     */
    private static final int REQUEST_CODE = 20210601;

    /**
     * 生命周期.
     * @param intent intent
     */
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        // 取消页面切换动画
        setAbilitySliceAnimator(new AbilitySliceAnimator().setDuration(0));
        requestPermission();
    }

    /**
     * 申请权限.
     */
    private void requestPermission() {
        // 必须手动权限
        if (verifySelfPermission(SystemPermission.CAMERA)
                != IBundleManager.PERMISSION_GRANTED) {
            // has no permission
            if (canRequestPermission(SystemPermission.CAMERA)) {
                // toast
                requestPermissionsFromUser(
                        new String[]{SystemPermission.CAMERA}, REQUEST_CODE);
            }
        }
    }
}
