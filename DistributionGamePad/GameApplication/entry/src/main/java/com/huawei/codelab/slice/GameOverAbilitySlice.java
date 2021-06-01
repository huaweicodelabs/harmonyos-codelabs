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

package com.huawei.codelab.slice;

import com.huawei.codelab.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;

/**
 *  game over ability slice
 *
 * @since 2021-03-15
 *
 */
public class GameOverAbilitySlice extends AbilitySlice {
    private static final String KEYONE = "playerOneScore";
    private static final String KEYTWO = "playerTwoScore";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_game_over);

        // 获得玩家分数
        Object scoreOne = intent.getParams().getParam(KEYONE);
        Object scoreTwo = intent.getParams().getParam(KEYTWO);

        // 设置分数
        Component componentOne = findComponentById(ResourceTable.Id_playerOne);
        Component componentTwo = findComponentById(ResourceTable.Id_playerTwo);
        if (componentOne instanceof Text) {
            Text textOne = (Text) componentOne;
            textOne.setText("玩家一分数：" + scoreOne);
        }
        if (componentTwo instanceof Text) {
            Text textTwo = (Text) componentTwo;
            textTwo.setText("玩家二分数：" + scoreTwo);
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
