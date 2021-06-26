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

import com.huawei.metadatabindingdemo.custom_ui.component.MySquare;

import ohos.agp.components.Component;
import ohos.mp.metadata.annotation.BindingComponent;
import ohos.mp.metadata.annotation.BindingTag;
import ohos.mp.metadata.binding.uioperate.Operator;

/**
 * This class creates two {@link Operator}s for {@link MySquare} component.
 * Operator is an abstract conception of 'operation' that do some operation with the binding value,
 * normally setting component's attributes.
 *
 * @since 2021-05-15
 */
@BindingComponent(component =
        "com.huawei.metadatabindingdemo.custom_ui.component.MySquare")
public class Custom1Operator {
    /**
     * When the binding MetaData changing, we hope this operator is used to do setColor operation of
     * the MySquare.
     */
    @BindingTag(attr = "enabled2", type = "java.lang.Boolean")
    public static Operator<Boolean> SetColorOperator = new Operator<Boolean>() {
        @Override
        public void operate(Component component, Boolean value) {
            ((MySquare) component).setColor(value);
        }
    };
}