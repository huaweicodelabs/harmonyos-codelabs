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
import com.huawei.cookbooks.util.CommonUtils;
import com.huawei.cookbooks.util.LogUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ScrollView;
import ohos.agp.components.TabList;
import ohos.agp.utils.TextAlignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Home Tab View
 *
 * @since 2020-12-07
 */
public class HomeLayoutManager {
    private static final String TAG = HomeLayoutManager.class.getSimpleName();
    private static final int HOME_TAB_ID = 1;
    private static final int OTHER_TAB_ID = 2;
    private static final int TABBAR_TEXT_SIZE = 30 * 3;
    private static final int TABBAR_TOP_PADDING = 8 * 3;
    private final AbilitySlice slice;
    private Map<Integer, AbstractTabView> tabViews;
    private List<AbstractTabView> tabViewList;
    private TabList.Tab defaultSelectedTab;

    /**
     * constructor of HomeLayoutManager
     *
     * @param slice AbilitySlice
     */
    public HomeLayoutManager(AbilitySlice slice) {
        this.slice = slice;
        initFrameLayoutView();
    }

    /**
     * Init framelayout view
     */
    private void initFrameLayoutView() {
        tabViews = new HashMap<>(0);
        tabViewList = new ArrayList<>(0);
        AbstractTabView othersTabView =
                new OtherTabView(
                        OTHER_TAB_ID, "他人数据", ResourceTable.Media_recommend, ResourceTable.Media_recommend, slice);
        tabViews.put(othersTabView.getId(), othersTabView);
        tabViewList.add(othersTabView);

        AbstractTabView selfTabView =
                new HomeTabView(HOME_TAB_ID, "健康数据", ResourceTable.Media_ranking, ResourceTable.Media_ranking, slice);
        tabViews.put(selfTabView.getId(), selfTabView);
        tabViewList.add(selfTabView);
    }

    /**
     * Init home layout
     *
     * @return layout
     */
    public ComponentContainer initSliceLayout() {
        Component tempView = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_root, null, false);
        if (!(tempView instanceof DependentLayout)) {
            LogUtils.info(TAG, "load root layout view failed");
            return new DependentLayout(slice);
        }
        LogUtils.info(getClass().getName(), "load layout xml success");
        DependentLayout rootLayout = (DependentLayout) tempView;
        addTabPage(rootLayout);
        addTabBar(rootLayout);
        return rootLayout;
    }

    /**
     * Add bottom navigation bar
     *
     * @param rootLayout DependentLayout
     */
    private void addTabBar(DependentLayout rootLayout) {
        Component tabView = rootLayout.findComponentById(ResourceTable.Id_footer_tab);
        if (tabView == null) {
            LogUtils.info(TAG, "cannot find footer tab view");
            return;
        }
        if (!(tabView instanceof TabList)) {
            LogUtils.info(TAG, "footer_tab is not TabList");
            return;
        }
        TabList tabBars = (TabList) tabView;
        tabBars.setFixedMode(true);
        tabBars.setTabTextSize(TABBAR_TEXT_SIZE);
        tabBars.setTabTextAlignment(TextAlignment.CENTER);
        tabBars.setSelectedTabIndicatorHeight(0);
        tabBars.setPadding(0, TABBAR_TOP_PADDING, 0, 0);
        tabBars.setTabTextColors(
                CommonUtils.getColor(slice.getContext(), ResourceTable.Color_text_off),
                CommonUtils.getColor(slice.getContext(), ResourceTable.Color_text_on));
        tabViewList.forEach(
            item -> {
                TabList.Tab tab = tabBars.new Tab(slice);
                tab.setId(item.getId());
                tab.setText(item.getName());
                tab.setTextAlignment(TextAlignment.CENTER);
                tabBars.addTab(tab);
                defaultSelectedTab = tab;
                LogUtils.info(TAG, "add tab view: " + item);
            });
        tabBars.addTabSelectedListener(new SelectedTabListener());
        tabBars.selectTab(defaultSelectedTab);
    }

    /**
     * Add page
     *
     * @param rootLayout DependentLayout
     */
    private void addTabPage(DependentLayout rootLayout) {
        Component tabPage = rootLayout.findComponentById(ResourceTable.Id_page);
        if (!(tabPage instanceof ScrollView)) {
            LogUtils.info(TAG, "cannot find page tab view");
            return;
        }
        tabViewList.forEach(
            item -> {
                item.getRootLayout().setVisibility(Component.INVISIBLE);
                ((ScrollView) tabPage).addComponent(item.getRootLayout());
            });
    }

    /**
     * Selected Tab Listener
     *
     * @since 2020-12-07
     */
    private class SelectedTabListener implements TabList.TabSelectedListener {
        @Override
        public void onSelected(TabList.Tab tab) {
            LogUtils.info(TAG, "Select tab " + tab.getId());
            int id = tab.getId();
            AbstractTabView selected = tabViews.get(id);
            selected.onSelected();
            selected.getRootLayout().setVisibility(Component.VISIBLE);
        }

        @Override
        public void onUnselected(TabList.Tab tab) {
            LogUtils.info(TAG, " onTabUnselected");
            int id = tab.getId();
            AbstractTabView unSelected = tabViews.get(id);
            unSelected.getRootLayout().setVisibility(Component.INVISIBLE);
        }

        @Override
        public void onReselected(TabList.Tab tab) {
            LogUtils.info(TAG, "onTabReselected: " + tab.getText());
        }
    }
}
