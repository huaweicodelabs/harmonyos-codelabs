/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.metadatabindingdemo.custom_ui.operator;

import com.huawei.metadatabindingdemo.custom_ui.component.MyRadioButton;

import ohos.agp.components.Component;
import ohos.agp.utils.Color;
import ohos.mp.metadata.annotation.BindingComponent;
import ohos.mp.metadata.annotation.BindingTag;
import ohos.mp.metadata.binding.uioperate.Operator;

/**
 * This class creates two {@link Operator}s for {@link MyRadioButton} component.
 * Operator is an abstract conception of 'operation' that do some operation according to the binding value,
 * normally setting component's attributes.
 *
 * @since 2021-05-15
 */
@BindingComponent(component =
        "com.huawei.metadatabindingdemo.custom_ui.component.MyRadioButton")
public class Custom2Operator {
    /**
     * When the binding MetaData changing, we hope this operator is used to do setTextColorOn operation of
     * the MyRadioButton.
     */
    @BindingTag(attr = "text_color_on2", type = "ohos.agp.utils.Color")
    public static Operator<Color> SetOnColor = new Operator<Color>() {
        @Override
        public void operate(Component component, Color value) {
            ((MyRadioButton)component).setTextColorOn(value);
        }
    };

    /**
     * When the binding MetaData changing, f, we hope this operator is used to do setTextColorOff operation of
     * the MyRadioButton.
     */
    @BindingTag(attr = "text_color_off2", type = "ohos.agp.utils.Color")
    public static Operator<Color> SetOffColor = new Operator<Color>() {
        @Override
        public void operate(Component component, Color value) {
            ((MyRadioButton)component).setTextColorOff(value);
        }
    };
}