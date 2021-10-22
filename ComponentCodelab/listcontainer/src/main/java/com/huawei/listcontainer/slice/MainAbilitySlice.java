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

package com.huawei.listcontainer.slice;

import com.huawei.listcontainer.ResourceTable;
import com.huawei.listcontainer.provider.NewsInfo;
import com.huawei.listcontainer.provider.NewsListProvider;
import com.huawei.listcontainer.provider.NewsTypeProvider;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final float FOCUS_TEXT_SIZE = 1.2f;
    private static final float UNFOCUS_TEXT_SIZE = 1.0f;
    private Text selectText;
    private ListContainer newsListContainer;
    private ListContainer selectorListContainer;
    private List<NewsInfo> totalNews;
    private List<NewsInfo> selectNews;
    private NewsTypeProvider newsTypeProvider;
    private NewsListProvider newsListProvider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_news_list_layout);
        initView();
        initProvider();
        setListContainer();
        initListener();
    }

    private void initView() {
        selectorListContainer = (ListContainer) findComponentById(ResourceTable.Id_selector_list);
        newsListContainer = (ListContainer) findComponentById(ResourceTable.Id_news_container);
    }

    /**
     * 初始化Provider
     */
    private void initProvider() {
        String[] listNames = new String[]{"All", "Health", "Finance", "Technology", "Sport", "Internet", "Game"};
        newsTypeProvider = new NewsTypeProvider(listNames, this);
        newsTypeProvider.notifyDataChanged();

        String[] newsTypes = new String[]{"Health", "Finance", "Finance", "Technology", "Sport",
                "Health", "Internet", "Game", "Game", "Internet"};
        String[] newsTitles = new String[]{
            "Best Enterprise Wi-Fi Network Award of the Wireless Broadband Alliance 2020",
            "Openness and Cooperation Facilitate Industry Upgrade",
            "High-voltage super-fast charging is an inevitable trend",
            "Ten Future Trends of Digital Energy",
            "Ascend Helps Industry, Learning, and Research Promote AI Industry "
                    + "Development in the National AI Contest",
            "Enterprise data centers are moving towards autonomous driving network",
            "One optical fiber lights up a green smart room",
            "Trust technology, embrace openness, and share the world prosperity brought by technology",
            "Intelligent Twins Won the Leading Technology Achievement Award at the 7th World Internet Conference",
            "Maximizing the Value of Wireless Networks and Ushering in the Golden Decade of 5G"
        };
        totalNews = new ArrayList<>();
        selectNews = new ArrayList<>();
        for (int i = 0; i < newsTypes.length; i++) {
            NewsInfo newsInfo = new NewsInfo();
            newsInfo.setTitle(newsTitles[i]);
            newsInfo.setType(newsTypes[i]);
            totalNews.add(newsInfo);
        }
        selectNews.addAll(totalNews);
        newsListProvider = new NewsListProvider(selectNews, this);
        newsListProvider.notifyDataChanged();
    }

    /**
     * 将Provider应用到ListContainer
     */
    private void setListContainer() {
        selectorListContainer.setItemProvider(newsTypeProvider);
        newsListContainer.setItemProvider(newsListProvider);
    }

    /**
     * 为ListContainer绑定点击事件
     */
    private void initListener() {
        selectorListContainer.setItemClickedListener((listContainer, component, position, listener) -> {
            setCategorizationFocus(false);
            Component newsTypeText = component.findComponentById(ResourceTable.Id_news_type_text);
            if (newsTypeText instanceof Text) {
                selectText = (Text) newsTypeText;
            }
            setCategorizationFocus(true);
            selectNews.clear();
            if (position == 0) {
                selectNews.addAll(totalNews);
            } else {
                setNewsData();
            }
            updateListView();
        });
        selectorListContainer.setSelected(true);
        selectorListContainer.setSelectedItemIndex(0);
    }

    private void setNewsData() {
        String newsType = selectText.getText();
        for (NewsInfo newsData : totalNews) {
            if (newsType.equals(newsData.getType())) {
                selectNews.add(newsData);
            }
        }
    }

    private void setCategorizationFocus(boolean isFocus) {
        if (selectText == null) {
            return;
        }
        if (isFocus) {
            selectText.setTextColor(new Color(Color.getIntColor("#afaafa")));
            selectText.setScaleX(FOCUS_TEXT_SIZE);
            selectText.setScaleY(FOCUS_TEXT_SIZE);
        } else {
            selectText.setTextColor(new Color(Color.getIntColor("#999999")));
            selectText.setScaleX(UNFOCUS_TEXT_SIZE);
            selectText.setScaleY(UNFOCUS_TEXT_SIZE);
        }
    }

    private void updateListView() {
        newsListProvider.notifyDataChanged();
        newsListContainer.invalidate();
        newsListContainer.scrollToCenter(0);
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
