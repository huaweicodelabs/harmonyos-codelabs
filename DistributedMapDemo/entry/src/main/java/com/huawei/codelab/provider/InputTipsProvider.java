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
import com.huawei.codelab.bean.InputTipsResult;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.List;
import java.util.Optional;

/**
 * InputTipsProvider
 *
 * @since 2021-03-12
 */
public class InputTipsProvider extends BaseItemProvider {
    private final List<InputTipsResult.TipsEntity> list;

    private final Context context;

    /**
     * InputTipsProvider
     *
     * @param context Context context
     * @param list list
     * @since 2021-03-12
     */
    public InputTipsProvider(Context context, List<InputTipsResult.TipsEntity> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return Optional.of(list.get(position));
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
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_input_tips, null, false);
            viewHolder = new ViewHolder();

            viewHolder.tipsName = (Text) component.findComponentById(ResourceTable.Id_tips_name);
            viewHolder.tipsDetail = (Text) component.findComponentById(ResourceTable.Id_tips_detail);

            component.setTag(viewHolder);
        } else {
            if (component.getTag() instanceof ViewHolder) {
                viewHolder = (ViewHolder) component.getTag();
            }
        }
        if (viewHolder != null) {
            viewHolder.tipsName.setText(list.get(position).getName());
            if (list.get(position).getAddress() instanceof String) {
                viewHolder.tipsDetail.setText((String) list.get(position).getAddress());
            } else {
                viewHolder.tipsDetail.setText(list.get(position).getName());
            }
        }
        return component;
    }

    /**
     * ViewHolder
     *
     * @since 2021-03-12
     */
    private static class ViewHolder {
        private Text tipsName;

        private Text tipsDetail;
    }
}
