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
import com.huawei.codelab.slice.MathDrawRemSlice;
import com.huawei.codelab.slice.MathGameAbilitySlice;
import com.huawei.codelab.slice.PictureGameAbilitySlice;
import com.huawei.codelab.utils.CommonData;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * MainAbility
 *
 * @since 2021-01-11
 */
public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        addActionRoute(CommonData.PICTURE_PAGE, PictureGameAbilitySlice.class.getName());
        addActionRoute(CommonData.MATH_PAGE, MathGameAbilitySlice.class.getName());
        addActionRoute(CommonData.DRAW_PAGE, MathDrawRemSlice.class.getName());
    }
}
