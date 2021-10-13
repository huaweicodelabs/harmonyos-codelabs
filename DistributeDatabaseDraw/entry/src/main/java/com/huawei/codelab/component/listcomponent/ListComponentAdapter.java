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

package com.huawei.codelab.component.listcomponent;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.app.Context;

import java.util.List;

/**
 * ListComponentAdapter
 *
 * @param <T> type
 * @since 2021-04-06
 */
public abstract class ListComponentAdapter<T> extends BaseItemProvider {
    private final Context context;
    private final List<T> lists;
    private final int xmlId;

    /**
     * constructor of ListComponentAdapter
     *
     * @param context context
     * @param lists lists
     * @param xmlId xmlId
     */
    protected ListComponentAdapter(Context context, List<T> lists, int xmlId) {
        this.context = context;
        this.lists = lists;
        this.xmlId = xmlId;
    }

    /**
     * onBindViewHolder
     *
     * @param commonViewHolder CommentViewHolder
     * @param item T
     * @param position int
     */
    public abstract void onBindViewHolder(CommentViewHolder commonViewHolder, T item, int position);

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public T getItem(int var1) {
        return lists.get(var1);
    }

    @Override
    public long getItemId(int var2) {
        return var2;
    }

    @Override
    public Component getComponent(int var1, Component component, ComponentContainer componentContainer) {
        CommentViewHolder commentViewHolder = CommentViewHolder.getCommentViewHolder(context, component, xmlId);
        T var2 = lists.get(var1);
        onBindViewHolder(commentViewHolder, var2, var1);
        commentViewHolder.convertView.setClickedListener(component1 -> onItemClick(component, var2, var1));
        return commentViewHolder.convertView;
    }

    /**
     * onItemClick
     *
     * @param component Component
     * @param item item
     * @param position int
     */
    public void onItemClick(Component component, T item, int position) {
    }
}
