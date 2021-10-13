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

package com.huawei.gameauth.manager;

import ohos.ace.ability.LocalParticleAbility;

import com.huawei.gameauth.MainAbility;

/**
 * For the JA JAVA.
 *
 * @since 2021-06-23
 */
public class JSInterface implements LocalParticleAbility {
    /**
     * MainAbility Home Page Reference.
     */
    private final MainAbility mainAbility;

    /**
     * constructor.
     *
     * @param ability Ability.
     */
    public JSInterface(final MainAbility ability) {
        this.mainAbility = ability;
    }

    /**
     * JavaScript asynchronous execution invokes the Java method.
     *
     * @param callback Used for JS callback.
     */
    public void getNetWorkIdAsync(final Callback callback) {
        mainAbility.setCallback(callback);
        mainAbility.requestPermissionAndGetNetWorkId();
    }

    /**
     * JavaScript asynchronous execution invokes the Java method.
     *
     * @param code     Agree or not
     * @param callback Used for JS callback.
     */
    public void isAllowGameAsync(final int code, final Callback callback) {
        mainAbility.setCallback(callback);
        mainAbility.isAllowGameAsync(code);
    }

    /**
     * get BundleName
     *
     * @return bundleName
     */
    public String getBundleName(){
        String bundleName=mainAbility.getBundleName();
        return bundleName;
    }

    /**
     * get AbilityName
     *
     * @return name
     */
    public String getAbilityName(){
        String mainAbilityName=mainAbility.getClass().getName();
        return mainAbilityName;
    }
}