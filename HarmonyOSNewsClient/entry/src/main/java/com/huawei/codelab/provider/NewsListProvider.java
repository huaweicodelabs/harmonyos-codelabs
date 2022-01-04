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

package com.huawei.codelab.provider;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.bean.NewsInfo;
import com.huawei.codelab.utils.CommonUtils;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.List;

/**
 * News list adapter
 *
 * @since 2020-12-04
 */
public class NewsListProvider extends BaseItemProvider {
    private final List<NewsInfo> newsInfoList;
    private final Context context;

    /**
     * constructor function
     *
     * @param listBasicInfo list info
     * @param context context
     * @since 2020-12-04
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
        return newsInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        ViewHolder viewHolder;
        Component temp = component;
        if (temp == null) {
            temp = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_news_layout, null, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (Text) temp.findComponentById(ResourceTable.Id_item_news_title);
            viewHolder.image = (Image) temp.findComponentById(ResourceTable.Id_item_news_image);
            temp.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) temp.getTag();
        }
        viewHolder.title.setText(newsInfoList.get(position).getTitle());
        viewHolder.image.setPixelMap(CommonUtils.getPixelMapFromPath(context, newsInfoList.get(position).getImgUrl()));
        return temp;
    }

    /**
     * ViewHolder which has title and image
     *
     * @since 2020-12-04
     */
    private static class ViewHolder {
        Text title;
        Image image;
    }
}
