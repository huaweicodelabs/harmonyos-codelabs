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

package com.huawei.cookbooks.view;

import com.huawei.cookbooks.ResourceTable;

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;

/**
 * UserTabView
 *
 * @since 2020-12-07
 *
 */
public class OtherTabView extends AbstractTabView {
    /**
     * Constructor
     *
     * @param id tab view id
     * @param name tab view name
     * @param normalIcon normalIcon
     * @param activeIcon activeIcon
     * @param slice tab view slice
     */
    public OtherTabView(int id, String name, int normalIcon, int activeIcon, AbilitySlice slice) {
        super(id, name, normalIcon, activeIcon, slice);
    }

    @Override
    public void initTabView() {
        Component rootView =
                LayoutScatter.getInstance(getAbilitySlice()).parse(ResourceTable.Layout_tablist_layout, null, false);
        if (!(rootView instanceof ComponentContainer)) {
            return;
        }
        setRootLayout((ComponentContainer) rootView);
    }
}
