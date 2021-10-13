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

package com.huawei.cookbooks.provider;

import com.huawei.cookbooks.ResourceTable;
import com.huawei.cookbooks.bean.Contactor;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.List;

/**
 * Contact List Adapter
 *
 * @since 2021-01-06
 */
public class ContactProvider extends BaseItemProvider {
    /**
     * Contact list data
     */
    private final List<Contactor> contactArrays;

    /**
     * context
     */
    private final Context context;

    /**
     * Click Callback
     */
    private AdapterClickListener adapterClickListener;

    /**
     * constructor of ContactProvider
     *
     * @param context context
     * @param contactArrays contactArrays
     */
    public ContactProvider(Context context, List<Contactor> contactArrays) {
        this.context = context;
        this.contactArrays = contactArrays;
    }

    /**
     * Number of list.
     *
     * @return Number of list.
     */
    @Override
    public int getCount() {
        return contactArrays == null ? 0 : contactArrays.size();
    }

    /**
     * the data model of each item
     *
     * @param position Index
     * @return the data model of each item
     */
    @Override
    public Object getItem(int position) {
        if (position < contactArrays.size() && position >= 0) {
            return contactArrays.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Component of each item
     *
     * @param position Index
     * @param componentPara Reused Component
     * @param componentContainer Container
     * @return Component of each item
     */
    @Override
    public Component getComponent(int position, Component componentPara, ComponentContainer componentContainer) {
        ViewHolder viewHolder = null;
        Component component = componentPara;
        if (component == null) {
            component = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_contact,
                    null, false);
            viewHolder = new ViewHolder();
            Component componentText = component.findComponentById(ResourceTable.Id_name);
            if (componentText instanceof Text) {
                viewHolder.name = (Text) componentText;
            }
            componentText = component.findComponentById(ResourceTable.Id_phone);
            if (componentText instanceof Text) {
                viewHolder.phone = (Text) componentText;
            }
            viewHolder.delete = (Button) component.findComponentById(ResourceTable.Id_delete);
            viewHolder.edit = (Button) component.findComponentById(ResourceTable.Id_edit);
            component.setTag(viewHolder);
        } else {
            if (component.getTag() instanceof ViewHolder) {
                viewHolder = (ViewHolder) component.getTag();
            }
        }
        if (viewHolder != null) {
            viewHolder.name.setText(contactArrays.get(position).getName());
            viewHolder.phone.setText(contactArrays.get(position).getPhone());
            viewHolder.edit.setClickedListener(editComponent -> {
                if (adapterClickListener != null) {
                    adapterClickListener.edit(position);
                }
            });
            viewHolder.delete.setClickedListener(deleteComponent -> {
                if (adapterClickListener != null) {
                    adapterClickListener.delete(position);
                }
            });
        }
        return component;
    }

    /**
     * ViewHolder
     *
     * @since 2021-01-06
     */
    private static class ViewHolder {
        private Text name;
        private Text phone;
        private Button edit;
        private Button delete;
    }

    /**
     * Defines the callback event interface.
     *
     * @since 2021-01-06
     */
    public interface AdapterClickListener {
        /**
         * edit method
         *
         * @param position position
         */
        void edit(int position);

        /**
         * delete method
         *
         * @param position position
         */
        void delete(int position);
    }

    public void setAdapterClickListener(AdapterClickListener adapterClickListener) {
        this.adapterClickListener = adapterClickListener;
    }
}
