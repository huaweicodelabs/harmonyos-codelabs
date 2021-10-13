/*
 *
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

package com.huawei.searchimagebykeywords.util;

import ohos.agp.components.Component;

import java.util.Optional;

/**
 * 窗口工具
 *
 * @since 2021-09-08
 */
public class WindowUtil {
    // 上下最大查找步数
    private static final int STEP_MAX = 99;

    private WindowUtil() {
    }

    /***
     * 解除获焦
     *
     * @param component 组件
     */
    public static void clearFocus(Component component) {
        Optional<Component> focusedComponent = findFocus(component, Component.FOCUS_NEXT); // 尝试向后找焦点控件
        if (!focusedComponent.isPresent()) {
            focusedComponent = findFocus(component, Component.FOCUS_PREVIOUS); // 尝试向前找焦点控件
        }
        focusedComponent.ifPresent(Component::clearFocus);
    }

    /***
     * 找焦点控件
     *
     * @param component 组件
     * @param direction 查找方向
     * @return 组件
     */
    private static Optional<Component> findFocus(Component component, int direction) {
        Optional<Component> optional = Optional.empty();
        if (component.hasFocus()) {
            optional = Optional.of(component);
            return optional;
        }
        Component focusableComponent = component;
        int index = STEP_MAX;
        while (index-- > 0) {
            focusableComponent = focusableComponent.findNextFocusableComponent(direction);
            if (focusableComponent != null) {
                if (focusableComponent.hasFocus()) {
                    optional = Optional.of(focusableComponent);
                    return optional;
                }
            } else {
                break;
            }
        }
        return optional;
    }
}
