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

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ListContainer;
import ohos.app.Context;

import java.util.List;

/**
 * Note list item provider.
 * You can create different styles of items easily by implement the NoteRow interface. AlarmListProvider
 * will calculate the category index of each position and add the item created by NoteRow interface into list
 * container. NoteRowItemBase interface use to declare different styles of items in NoteRow.
 *
 * @since 2021-05-15
 */
public class NoteListProvider extends BaseItemProvider implements ListContainer.ItemClickedListener {
    private static final int ERROR_CATEGORY_INDEX = -1;

    private final Context mContext;
    private List<NoteRow> notesList;

    /**
     * NoteRow list item provider constructor
     *
     * @param context Context
     */
    public NoteListProvider(Context context) {
        mContext = context;
    }

    /**
     * Initialization of this list provider's data.
     *
     * @param notesList initializing data
     */
    public void initData(List<NoteRow> notesList) {
        this.notesList = notesList;
    }

    /**
     * add category list and notify
     *
     * @param notesList category list
     */
    public void addItems(List<NoteRow> notesList) {
        this.notesList.addAll(notesList);
        mContext.getUITaskDispatcher().asyncDispatch(this::notifyDataChanged);
    }

    @Override
    public int getCount() {
        int count = 0;
        for (NoteRow note : notesList) {
            count++;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int getNoteRowIndex(int position) {
        if (position < 0 || position >= getCount()) {
            return ERROR_CATEGORY_INDEX;
        }

        int idx = 0;
        int startPosition = 0;
        for (NoteRow note : notesList) {
            if (position - startPosition < 1) {
                return idx;
            }
            startPosition++;
            idx++;
        }

        return ERROR_CATEGORY_INDEX;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        int idx = getNoteRowIndex(position);

        if (idx == ERROR_CATEGORY_INDEX) {
            return null;
        }

        NoteRow note = notesList.get(idx);
        if (component == null) {
            Component newComponent = note.createComponent();
            note.bindComponent(newComponent);
            return newComponent;
        } else {
            note.bindComponent(component);
            return component;
        }
    }

    private void onClickNoteRowItem(int position) {
        int categoryIdx = getNoteRowIndex(position);
        NoteRow noteRow = notesList.get(categoryIdx);
        noteRow.onClick();
    }

    @Override
    public void onItemClicked(ListContainer listContainer, Component component, int position, long id) {
        onClickNoteRowItem(position);
    }
}