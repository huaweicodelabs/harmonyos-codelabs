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

package com.huawei.searchimagebykeywords.provider;

import com.huawei.searchimagebykeywords.ResourceTable;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;

import java.util.Optional;

/**
 * 图片提供类
 *
 * @since 2021-01-15
 *
 */
public class PictureProvider extends BaseItemProvider {
    private final int[] pictureLists;
    private final Context context;

    /**
     * 构造方法
     *
     * @param pictureLists 图片资源集合
     * @param context 上下文对象
     * @since 2021-01-15
     */
    public PictureProvider(int[] pictureLists, Context context) {
        this.pictureLists = pictureLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pictureLists == null ? 0 : pictureLists.length;
    }

    @Override
    public Object getItem(int position) {
        return Optional.of(this.pictureLists[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int var1, Component var2, ComponentContainer var3) {
        ViewHolder viewHolder = null;
        Component component = var2;
        if (component == null) {
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_image_layout,
                    null, false);
            viewHolder = new ViewHolder();
            Component componentImage = component.findComponentById(ResourceTable.Id_select_picture_list);
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
            viewHolder.image.setPixelMap(pictureLists[var1]);
        }
        return component;
    }

    /**
     * 具有标题的ViewHolder
     *
     * @since 2021-01-15
     */
    private static class ViewHolder {
        Image image;
    }
}