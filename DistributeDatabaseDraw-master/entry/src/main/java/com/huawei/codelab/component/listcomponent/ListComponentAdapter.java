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
 * ListComponentAdapter<T>
 *
 * @since 2021-04-06
 */
public abstract class ListComponentAdapter<T> extends BaseItemProvider {
    private Context mContext;
    private List<T> mListBean;
    private int mXmlId;

    protected ListComponentAdapter(Context context, List<T> list, int xmlId) {
        this.mContext = context;
        this.mListBean = list;
        this.mXmlId = xmlId;
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
        return mListBean.size();
    }

    @Override
    public T getItem(int var1) {
        return mListBean.get(var1);
    }

    @Override
    public long getItemId(int var2) {
        return var2;
    }

    @Override
    public Component getComponent(int var1, Component component, ComponentContainer componentContainer) {
        CommentViewHolder commentViewHolder = CommentViewHolder.getCommentViewHolder(mContext, component, mXmlId);
        T var2 = mListBean.get(var1);
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
