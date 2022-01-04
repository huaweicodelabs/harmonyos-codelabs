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

package com.huawei.checkbox.slice;

import com.huawei.checkbox.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.Checkbox;
import ohos.agp.components.ComponentState;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;

import java.util.HashSet;
import java.util.Set;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice {
    private Text answer;
    private final Set<String> selectedSet = new HashSet<>();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_checkbox);
        answer = (Text) findComponentById(ResourceTable.Id_text_answer);
        initCheckbox();
    }

    // 获取Checkbox的背景
    private StateElement getStateElement() {
        ShapeElement elementButtonOn = new ShapeElement();
        elementButtonOn.setRgbColor(RgbPalette.RED);
        elementButtonOn.setShape(ShapeElement.OVAL);

        ShapeElement elementButtonOff = new ShapeElement();
        elementButtonOff.setRgbColor(RgbPalette.WHITE);
        elementButtonOff.setShape(ShapeElement.OVAL);

        StateElement checkElement = new StateElement();
        checkElement.addState(new int[]{ComponentState.COMPONENT_STATE_CHECKED}, elementButtonOn);
        checkElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, elementButtonOff);
        return checkElement;
    }

    // 订阅Checkbox状态变化
    private void setCheckedStateChangedListener(Checkbox checkbox, String checkValue) {
        checkbox.setCheckedStateChangedListener((view, state) -> {
            if (state) {
                selectedSet.add(checkValue);
            } else {
                selectedSet.remove(checkValue);
            }
            showAnswer();
        });
    }

    // 初始化Checkbox并设置订阅事件
    private void initCheckbox() {
        Checkbox checkbox1 = (Checkbox) findComponentById(ResourceTable.Id_checkbox_1);
        checkbox1.setButtonElement(getStateElement());
        setCheckedStateChangedListener(checkbox1, "A");
        if (checkbox1.isChecked()) {
            selectedSet.add("A");
        }

        Checkbox checkbox2 = (Checkbox) findComponentById(ResourceTable.Id_checkbox_2);
        checkbox2.setButtonElement(getStateElement());
        setCheckedStateChangedListener(checkbox2, "B");
        if (checkbox2.isChecked()) {
            selectedSet.add("B");
        }

        Checkbox checkbox3 = (Checkbox) findComponentById(ResourceTable.Id_checkbox_3);
        checkbox3.setButtonElement(getStateElement());
        setCheckedStateChangedListener(checkbox3, "C");
        if (checkbox3.isChecked()) {
            selectedSet.add("C");
        }

        Checkbox checkbox4 = (Checkbox) findComponentById(ResourceTable.Id_checkbox_4);
        checkbox4.setButtonElement(getStateElement());
        setCheckedStateChangedListener(checkbox4, "D");
        if (checkbox4.isChecked()) {
            selectedSet.add("D");
        }
    }

    private void showAnswer() {
        String select = selectedSet.toString();
        answer.setText(select);
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
