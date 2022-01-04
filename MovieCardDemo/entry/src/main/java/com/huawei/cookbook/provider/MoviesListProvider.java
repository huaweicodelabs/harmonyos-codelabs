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

package com.huawei.cookbook.provider;

import com.huawei.cookbook.ResourceTable;
import com.huawei.cookbook.database.MovieInfo;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.List;

/**
 * MoviesListProvider
 */
public class MoviesListProvider extends BaseItemProvider {
    private final List<MovieInfo> moviesInfos;

    private final Context context;

    /**
     * MoviesListProvider
     *
     * @param moviesInfo moviesInfo
     * @param context context
     */
    public MoviesListProvider(List<MovieInfo> moviesInfo, Context context) {
        this.moviesInfos = moviesInfo;
        this.context = context;
    }

    @Override
    public int getCount() {
        return moviesInfos == null ? 0 : moviesInfos.size();
    }

    @Override
    public Object getItem(int index) {
        return moviesInfos.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public Component getComponent(int index, Component component, ComponentContainer componentContainer) {
        ViewHolder viewHolder;
        Component temp = component;
        if (temp == null) {
            temp = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_movies_layout,
                    null, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (Text) temp.findComponentById(ResourceTable.Id_item_movies_title);
            viewHolder.image = (Image) temp.findComponentById(ResourceTable.Id_item_movies_image);
            viewHolder.rating = (Text) temp.findComponentById(ResourceTable.Id_item_movies_rating);
            viewHolder.type = (Text) temp.findComponentById(ResourceTable.Id_item_movies_type);
            viewHolder.sort = (Text) temp.findComponentById(ResourceTable.Id_item_movies_id);
            temp.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) temp.getTag();
        }
        viewHolder.sort.setText(moviesInfos.get(index).getSort());
        viewHolder.title.setText(moviesInfos.get(index).getTitle());
        viewHolder.image.setPixelMap(moviesInfos.get(index).getImgUrl().intValue());
        viewHolder.rating.setText(moviesInfos.get(index).getRating());
        viewHolder.type.setText(moviesInfos.get(index).getType());
        return temp;
    }

    /**
     * view
     */
    private static class ViewHolder {
        Text sort;
        Text title;
        Image image;
        Text rating;
        Text type;
    }
}
