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

package com.huawei.metadatabindingdemo.custom_ui;

import com.huawei.metadatabindingdemo.ResourceTable;
import com.huawei.metadatabindingdemo.custom_ui.metadata.CustomMetaData;
import com.huawei.metadatabindingdemo.metadatabinding.CustomoperatorMetaDataBinding;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.mp.metadata.binding.databinding.MetaDataBinding;

/**
 * Custom Operator Ability Slice
 *
 * @since 2021-05-15
 */
public class CustomOperatorSlice extends AbilitySlice {
    CustomMetaData metaData;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        createComponent();
    }

    private void createComponent() {
        metaData = new CustomMetaData();
        metaData.setPrimaryKey("id");
        metaData.setName("MetaData");
        metaData.put("bool_attr", false);
        MetaDataBinding binding;
        Component mainComponent;
        binding = CustomoperatorMetaDataBinding.createBinding(this, metaData);
        mainComponent = binding.getLayoutComponent();
        setUIContent((ComponentContainer) mainComponent);

        DependentLayout dependentLayout = (DependentLayout) mainComponent.findComponentById(ResourceTable
                .Id_title_area_back_icon_hot_area);
        dependentLayout.setClickedListener(component -> this.terminate());
    }

    @Override
    public void onActive() {
        super.onActive();
    }
}
