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

package com.huawei.listcontainer.provider;

import com.huawei.listcontainer.ResourceTable;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.List;
import java.util.Optional;

/**
 * NewsListProvider
 */
public class NewsListProvider extends BaseItemProvider {
    private final List<NewsInfo> newsInfoList;
    private final Context context;

    /**
     * 有参构造
     * @param listBasicInfo 新闻信息集合
     * @param context context
     */
    public NewsListProvider(List<NewsInfo> listBasicInfo, Context context) {
        this.newsInfoList = listBasicInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return newsInfoList == null ? 0 : newsInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return Optional.of(this.newsInfoList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component componentP, ComponentContainer componentContainer) {
        ViewHolder viewHolder = null;
        Component component = componentP;
        if (component == null) {
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_news_layout, null, false);
            viewHolder = new ViewHolder();
            Component componentTitle = component.findComponentById(ResourceTable.Id_item_news_title);
            Component componentImage = component.findComponentById(ResourceTable.Id_item_news_image);
            if (componentTitle instanceof Text) {
                viewHolder.title = (Text) componentTitle;
            }
            if (componentImage instanceof Image) {
                viewHolder.image = (Image) componentImage;
            }
            component.setTag(viewHolder);
        } else {
            if (component.getTag() instanceof ViewHolder) {
                viewHolder = (ViewHolder) component.getTag();
            }
        }
        if (viewHolder != null) {
            viewHolder.title.setText(newsInfoList.get(position).getTitle());
            viewHolder.image.setScaleMode(Image.ScaleMode.STRETCH);
        }
        return component;
    }

    /**
     * 显示内容
     */
    private static class ViewHolder {
        Text title;
        Image image;
    }
}
