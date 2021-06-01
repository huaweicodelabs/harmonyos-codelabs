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

package com.huawei.maildemo;

import com.huawei.maildemo.slice.MailEditSlice;
import com.huawei.maildemo.utils.LogUtil;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;

import java.util.ArrayList;
import java.util.List;

/**
 * MainAbility
 *
 * @since 2021-02-04
 */
public class MainAbility extends Ability implements IAbilityContinuation {
    private static final String TAG = MainAbility.class.getSimpleName();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MailEditSlice.class.getName());
        requestPermission();
    }

    private void requestPermission() {
        String[] permissions = {
            "ohos.permission.READ_USER_STORAGE",
            "ohos.permission.WRITE_USER_STORAGE",
            "ohos.permission.DISTRIBUTED_DATASYNC"
        };
        List<String> applyPermissions = new ArrayList<>();
        for (String element : permissions) {
            LogUtil.info(TAG, "check permission: " + element);
            checkPermission(applyPermissions, element);
        }
        requestPermissionsFromUser(applyPermissions.toArray(new String[0]), 0);
    }

    private void checkPermission(List<String> applyPermissions, String element) {
        if (verifySelfPermission(element) != 0) {
            if (canRequestPermission(element)) {
                applyPermissions.add(element);
            } else {
                LogUtil.info(TAG, "user deny permission");
            }
        } else {
            LogUtil.info(TAG, "user granted permission: " + element);
        }
    }

    @Override
    public void onInactive() {
        super.onInactive();
        LogUtil.info(TAG, "is onInactive");
    }

    @Override
    public void onActive() {
        super.onActive();
        LogUtil.info(TAG, "is onActive");
    }

    @Override
    public void onBackground() {
        super.onBackground();
        LogUtil.info(TAG, "is onBackground");
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
        LogUtil.info(TAG, "is onForeground");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.info(TAG, "is onStop");
    }

    @Override
    public void onCompleteContinuation(int code) { }

    @Override
    public boolean onRestoreData(IntentParams params) {
        return true;
    }

    @Override
    public boolean onSaveData(IntentParams params) {
        return true;
    }

    @Override
    public boolean onStartContinuation() {
        return true;
    }
}
