/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.huawei.codelab.component.listcomponent;

import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * CommentViewHolder
 *
 * @since 2021-04-06
 */
public class CommentViewHolder {
    Component convertView;
    private final Map<Integer, Component> mViews = new HashMap<>(0);

    private CommentViewHolder(Component convertView) {
        this.convertView = convertView;
        convertView.setTag(this);
    }

    // Avoiding Multiple Static Classes Using Singleton Mode
    static CommentViewHolder getCommentViewHolder(Context context, Component convertView, int resource) {
        if (convertView == null) {
            Component createConvertView = LayoutScatter.getInstance(context).parse(resource, null, false);
            return new CommentViewHolder(createConvertView);
        } else {
            return (CommentViewHolder) convertView.getTag();
        }
    }

    /**
     * Specify the type of view based on the generic
     *
     * @param resId int
     * @param type Class
     * @param <T> generic
     * @return view
     */
    private <T extends Component> T getView(int resId, Class<T> type) {
        Component view = mViews.get(resId);
        if (view == null) {
            view = convertView.findComponentById(resId);
            mViews.put(resId, view);
        }
        return (T) view;
    }

    /**
     * Two common views, which are provided for external systems
     *
     * @param resId int
     * @return Text
     */
    public Text getTextView(int resId) {
        return getView(resId, Text.class);
    }

    /**
     * getImageView
     *
     * @param resId int
     * @return Image
     */
    public Image getImageView(int resId) {
        return getView(resId, Image.class);
    }
}
