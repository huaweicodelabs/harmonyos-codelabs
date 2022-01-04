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
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.global.configuration.Configuration;

/**
 * right ability
 *
 * @since 2021-09-10
 */
public class RightAbility extends Ability implements IAbilityContinuation {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(RightAbilitySlice.class.getName());
    }

    @Override
    public void onConfigurationUpdated(Configuration configuration) {
        super.onConfigurationUpdated(configuration);
        // Obtains the current slice width
        Utils.setRightWidth(getContext());
        // Initializing the listContainer Data
        MainAbilitySlice.initData(Utils.transIdToPixelMap(getContext()));
        // 重置右侧图片大小
        RightAbilitySlice.setImage(RightAbilitySlice.getIdIndex());
    }

    // To facilitate demonstration, the transfer logic is not implemented in the Mobility. The specific logic is implemented in the MobilitySlice
    // However, IAbilityContinuation must be implemented in Ability
    @Override
    public boolean onStartContinuation() { // 确认当前是否可以开始迁移
        return true;
    }

    @Override
    public boolean onSaveData(IntentParams saveData) { // 保存迁移后恢复状态必须的数据
        return true;
    }

    @Override
    public boolean onRestoreData(IntentParams restoreData) { // 应用可在此方法恢复业务状态
        return true;
    }

    @Override
    public void onCompleteContinuation(int result) { // 知应用迁移成功
    }
}
