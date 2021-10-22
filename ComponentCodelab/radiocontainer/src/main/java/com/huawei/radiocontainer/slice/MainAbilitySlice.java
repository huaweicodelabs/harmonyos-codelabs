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

package com.huawei.radiocontainer.slice;

import com.huawei.radiocontainer.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.ComponentState;
import ohos.agp.components.RadioButton;
import ohos.agp.components.RadioContainer;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;

import java.util.Locale;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_radio_container);
        initComponent();
    }

    /**
     * 定义RadioButton的背景
     * @return 背景样式
     */
    private StateElement getStateElement() {
        ShapeElement elementButtonOn = new ShapeElement();
        elementButtonOn.setRgbColor(RgbPalette.RED);
        elementButtonOn.setShape(ShapeElement.OVAL);

        ShapeElement elementButtonOff = new ShapeElement();
        elementButtonOff.setRgbColor(RgbPalette.DARK_GRAY);
        elementButtonOff.setShape(ShapeElement.OVAL);

        StateElement checkElement = new StateElement();
        checkElement.addState(new int[]{ComponentState.COMPONENT_STATE_CHECKED}, elementButtonOn);
        checkElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, elementButtonOff);
        return checkElement;
    }

    /**
     * 初始化组件并设置响应RadioContainer状态改变的事件，显示单选结果
     */
    private void initComponent() {
        Text answer = (Text) findComponentById(ResourceTable.Id_answer);
        RadioContainer container = (RadioContainer) findComponentById(ResourceTable.Id_radio_container);
        int count = container.getChildCount();
        for (int i = 0; i < count; i++) {
            ((RadioButton) container.getComponentAt(i)).setButtonElement(getStateElement());
        }
        container.setMarkChangedListener((radioContainer1, index) ->
                answer.setText(String.format(Locale.ROOT,"[%c]", (char) ('A' + index))));
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
