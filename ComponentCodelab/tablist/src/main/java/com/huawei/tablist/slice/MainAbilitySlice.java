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

package com.huawei.tablist.slice;

import com.huawei.tablist.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.TabList;
import ohos.agp.components.Text;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final int TAB_WIDTH = 64;
    private static final int TAB_PADDING_LEFT = 12;
    private static final int TAB_PADDING_RIGHT = 12;
    private static final int TAB_PADDING_BOTTOM = 12;
    private static final int TAB_PADDING_TOP = 12;
    private TabList tabList;
    private Text tabContent;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_tab_list);
        initComponent();
        addTabSelectedListener();
    }

    private void initComponent() {
        tabContent = (Text) findComponentById(ResourceTable.Id_tab_content);
        tabList = (TabList) findComponentById(ResourceTable.Id_tab_list);
        initTab();
    }

    private void initTab() {
        if (tabList.getTabCount() == 0) {
            tabList.addTab(createTab("Image"));
            tabList.addTab(createTab("Video"));
            tabList.addTab(createTab("Audio"));
            tabList.setFixedMode(true);
            tabList.getTabAt(0).select();
            tabContent.setText("Select the " + tabList.getTabAt(0).getText());
        }
    }

    private void addTabSelectedListener() {
        tabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            @Override
            public void onSelected(TabList.Tab tab) {
                tabContent.setText("Select the " + tab.getText());
            }

            @Override
            public void onUnselected(TabList.Tab tab) {
            }

            @Override
            public void onReselected(TabList.Tab tab) {
            }
        });
    }

    private TabList.Tab createTab(String text) {
        TabList.Tab tab = tabList.new Tab(this);
        tab.setText(text);
        tab.setMinWidth(TAB_WIDTH);
        tab.setPadding(TAB_PADDING_LEFT, TAB_PADDING_TOP, TAB_PADDING_RIGHT, TAB_PADDING_BOTTOM);
        return tab;
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
