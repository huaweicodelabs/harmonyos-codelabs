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

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;

import java.util.List;

/**
 * ListComponentAdapter
 *
 * @param <T> generics
 * @since 2021-02-13
 */
public abstract class ListComponentAdapter<T> extends BaseItemProvider {
    LayoutScatter layoutScatter;
    private final Context mContext;
    private final List<T> mListBeans;
    private final int mXmlId;

    /**
     * ListComponentAdapter
     *
     * @param context context
     * @param list list
     * @param xmlId xmlId
     */
    public ListComponentAdapter(Context context, List<T> list, int xmlId) {
        this.mContext = context;
        this.mListBeans = list;
        this.mXmlId = xmlId;
        layoutScatter = LayoutScatter.getInstance(mContext);
    }

    /**
     * onBindViewHolder
     *
     * @param commonViewHolder commonViewHolder
     * @param item item
     * @param position position
     */
    public abstract void onBindViewHolder(CommentViewHolder commonViewHolder, T item, int position);

    @Override
    public int getCount() {
        return mListBeans.size();
    }

    @Override
    public T getItem(int i) {
        return mListBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        CommentViewHolder commentViewHolder = CommentViewHolder.getCommentViewHolder(mContext, component, mXmlId);
        T t = mListBeans.get(i);
        onBindViewHolder(commentViewHolder, t, i);
        commentViewHolder.getConvertView().setClickedListener(component1 -> onItemClick(component, t, i));
        return commentViewHolder.getConvertView();
    }

    /**
     * onItemClick
     *
     * @param component component
     * @param item item
     * @param position position
     */
    public void onItemClick(Component component, T item, int position) { }
}