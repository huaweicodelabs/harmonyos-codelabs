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

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ComponentContainer.LayoutConfig;
import ohos.agp.components.DirectionalLayout;

/**
 * Tab view abstract class
 *
 * @since 2020-06-03
 */
public abstract class AbstractTabView {
    private final int id;

    private final String name;

    private final int normalIcon;

    private final int activeIcon;

    private ComponentContainer rootLayout;

    private final AbilitySlice slice;

    /**
     * Constructor
     *
     * @param id tab view id
     * @param name tab view name
     * @param normalIcon normalIcon
     * @param activeIcon activeIcon
     * @param slice tab view slice
     */
    public AbstractTabView(int id, String name, int normalIcon, int activeIcon, AbilitySlice slice) {
        super();
        this.id = id;
        this.name = name;
        this.normalIcon = normalIcon;
        this.activeIcon = activeIcon;
        this.slice = slice;

        initRootLayout();
        initTabView();
    }

    private void initRootLayout() {
        rootLayout = new DirectionalLayout(slice);
        rootLayout.setWidth(LayoutConfig.MATCH_PARENT);
        rootLayout.setHeight(LayoutConfig.MATCH_CONTENT);
    }

    /**
     * Init tab view components
     */
    public abstract void initTabView();

    /**
     * tab content on init
     */
    public void onSelected() {
    }

    /**
     * Get ability slice
     *
     * @return ability slice
     */
    protected final AbilitySlice getAbilitySlice() {
        return slice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ComponentContainer getRootLayout() {
        return rootLayout;
    }

    public void setRootLayout(ComponentContainer rootLayout) {
        this.rootLayout = rootLayout;
    }
}
