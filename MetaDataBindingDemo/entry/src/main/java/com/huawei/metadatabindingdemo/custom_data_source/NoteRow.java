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

package com.huawei.metadatabindingdemo.custom_data_source;

import com.huawei.metadatabindingdemo.custom_data_source.metadata.NoteMetaData;
import com.huawei.metadatabindingdemo.metadatabinding.NoterowMetaDataBinding;

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.mp.metadata.binding.metadata.MetaData;

/**
 * A row item of Alarm ListContainer. For the item layout, see: src/main/resources/base/layout/note_row.xml.
 *
 * @since 2021-05-15
 */
public class NoteRow {
    private final AbilitySlice context;
    private final NoteMetaData noteMetaData;

    /**
     * NoteRow constructor
     *
     * @param context Context
     * @param noteMetaData MetaData
     */
    public NoteRow(AbilitySlice context, MetaData noteMetaData) {
        this.context = context;
        this.noteMetaData = (NoteMetaData) noteMetaData;
    }

    /**
     * create NoteRow component.
     *
     * @return a NoteRow component
     */
    public Component createComponent() {
        NoterowMetaDataBinding metaBinding = NoterowMetaDataBinding.createBinding(context, noteMetaData);
        Component comp = metaBinding.getLayoutComponent();
        comp.setTag(metaBinding);
        return comp;
    }

    /**
     * bind noteMetaData with component.
     *
     * @param component a NoteRow Component
     */
    public void bindComponent(Component component) {
        NoterowMetaDataBinding metaBinding = (NoterowMetaDataBinding) component.getTag();
        metaBinding.reBinding(component, noteMetaData);
    }

    /**
     * do nothing.
     */
    public void onClick() {
    }
}
