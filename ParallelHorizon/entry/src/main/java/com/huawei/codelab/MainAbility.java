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

import com.huawei.codelab.slice.MainAbilitySlice;
import com.huawei.codelab.slice.RightAbilitySlice;
import com.huawei.codelab.util.Utils;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.global.configuration.Configuration;

/**
 * main ability
 *
 * @since 2021-09-10
 */
public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        // 声明跨端迁移访问的权限
        requestPermissionsFromUser(new String[]{"ohos.permission.DISTRIBUTED_DATASYNC"}, 0);
    }

    @Override
    public void onConfigurationUpdated(Configuration configuration) {
        super.onConfigurationUpdated(configuration);
        // Obtains the current slice width
        Utils.setLeftWidth(getContext());
        // Initializing the listContainer Data
        MainAbilitySlice.initData(Utils.transIdToPixelMap(getContext()));
        // 重置右侧图片大小
        RightAbilitySlice.setImage(RightAbilitySlice.getIdIndex());
    }
}
