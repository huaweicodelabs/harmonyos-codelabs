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

package ohos.codelabs.distributedvideo.provider;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.app.Context;

import java.util.List;

/**
 * CommonAdapter
 *
 * @param <T> type
 * @since 2021-09-07
 */
public abstract class CommonProvider<T> extends BaseItemProvider {
    /**
     * source list
     */
    protected List<T> datas;
    /**
     * context
     */
    protected Context context;
    /**
     * the resource id
     */
    protected int layoutId;

    /**
     * constructor of CommonAdapter
     *
     * @param context context
     * @param layoutId id
     * @param datas listContainer data
     */
    public CommonProvider(List<T> datas, Context context, int layoutId) {
        this.datas = datas;
        this.context = context;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    /**
     * return data
     *
     * @param position position
     * @return data
     */
    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer parent) {
        ViewProvider holder = ViewProvider.get(context, component, layoutId, position);

        convert(holder, getItem(position), position);
        return holder.getComponentView();
    }

    /**
     * convert to a new Collection,contains clear it
     *
     * @param holder holder
     * @param item item
     * @param position position
     */
    protected abstract void convert(ViewProvider holder, T item, int position);
}
