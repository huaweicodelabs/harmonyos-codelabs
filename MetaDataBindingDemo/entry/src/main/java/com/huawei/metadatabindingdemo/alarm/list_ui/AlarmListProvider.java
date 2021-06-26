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

package com.huawei.metadatabindingdemo.alarm.list_ui;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ListContainer;
import ohos.app.Context;

import java.util.List;

/**
 * Alarm list item provider.
 * You can create different styles of items easily by implement the AlarmRow interface. AlarmListProvider
 * will calculate the alarm index of each position and add the item created by AlarmRow interface into list
 * container. AlarmRowItemBase interface use to declare different styles of items in AlarmRow.
 *
 * @since 2021-05-15
 */
public class AlarmListProvider extends BaseItemProvider implements ListContainer.ItemClickedListener {
    private static final int ERROR_CATEGORY_INDEX = -1;

    private final Context mContext;
    private List<AlarmRow> alarmList;

    /**
     * AlarmRow list item provider constructor
     *
     * @param context Context
     */
    public AlarmListProvider(Context context) {
        mContext = context;
    }

    /**
     * Initialization of this list provider's data.
     *
     * @param alarmList initializing data
     */
    public void initData(List<AlarmRow> alarmList) {
        this.alarmList = alarmList;
    }

    /**
     * add alarm list and notify
     *
     * @param alarmList alarm list
     */
    public void addItems(List<AlarmRow> alarmList) {
        this.alarmList.addAll(alarmList);
        mContext.getUITaskDispatcher().asyncDispatch(this::notifyDataChanged);
    }

    @Override
    public int getCount() {
        int count = 0;
        for (AlarmRow alarm : alarmList) {
            count++;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        return alarmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int getAlarmRowIndex(int position) {
        if (position < 0 || position >= getCount()) {
            return ERROR_CATEGORY_INDEX;
        }

        int alarmIndex = 0;
        int alarmStartPosition = 0;
        for (AlarmRow alarm : alarmList) {
            if (position - alarmStartPosition < 1) {
                return alarmIndex;
            }
            alarmStartPosition++;
            alarmIndex++;
        }

        return ERROR_CATEGORY_INDEX;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        int alarmIndex = getAlarmRowIndex(position);

        if (alarmIndex == ERROR_CATEGORY_INDEX) {
            return null;
        }

        AlarmRow alarm = alarmList.get(alarmIndex);
        if (component == null) {
            Component newComponent = alarm.createComponent();
            alarm.bindComponent(newComponent);
            return newComponent;
        } else {
            alarm.bindComponent(component);
            return component;
        }
    }

    private void onClickAlarmRowItem(int position) {
        int alarmIdx = getAlarmRowIndex(position);
        AlarmRow alarm = alarmList.get(alarmIdx);
        alarm.onClick();
    }

    @Override
    public void onItemClicked(ListContainer listContainer, Component component, int position, long id) {
        onClickAlarmRowItem(position);
    }
}