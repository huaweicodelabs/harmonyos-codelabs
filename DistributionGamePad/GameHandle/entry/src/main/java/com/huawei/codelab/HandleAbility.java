/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package com.huawei.codelab;

import com.huawei.codelab.slice.HandleAbilitySlice;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/**
 * HandleAbility
 *
 * @since 2021-03-15
 */
public class HandleAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(HandleAbilitySlice.class.getName());
    }
}
