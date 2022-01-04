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

package com.huawei.maildemo.ui.listcomponent;

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
 * @since 2021-02-04
 */
public class CommentViewHolder {
    /**
     * convertView
     */
    private final Component convertView;
    private final Map<Integer, Component> mViews = new HashMap<>();

    /**
     * 对成员变量赋值，并把当前CommentViewHolder用tag存起来以便复用
     *
     * @param convertView convertView
     */
    public CommentViewHolder(Component convertView) {
        this.convertView = convertView;
        convertView.setTag(this);
    }

    /**
     * getConvertView
     *
     * @return convertView
     */
    public Component getConvertView() {
        return convertView;
    }

    /**
     * 使用单例模式避免多个静态类
     *
     * @param context context
     * @param convertView convertView
     * @param resource resource
     * @return CommentViewHolder
     */
    public static CommentViewHolder getCommentViewHolder(Context context, Component convertView, int resource) {
        if (convertView == null) {
            return new CommentViewHolder(LayoutScatter.getInstance(context).parse(resource, null, false));
        } else {
            return (CommentViewHolder) convertView.getTag();
        }
    }

    /**
     * 根据泛型，指定view的类型
     *
     * @param resId resId
     * @param <T> generics
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getView(int resId) {
        Component view = mViews.get(resId);
        if (view == null) {
            view = convertView.findComponentById(resId);
            mViews.put(resId, view);
        }
        return (T) view;
    }

    /**
     * 2个常用的view，简单处理一下对外提供出来
     *
     * @param resId resId
     * @return Text
     */
    public Text getTextView(int resId) {
        return getView(resId);
    }

    /**
     * getImageView
     *
     * @param resId resId
     * @return Image
     */
    public Image getImageView(int resId) {
        return getView(resId);
    }
}
