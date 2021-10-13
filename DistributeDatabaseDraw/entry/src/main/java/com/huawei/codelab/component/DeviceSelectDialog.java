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

package com.huawei.codelab.component;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.bean.DeviceData;
import com.huawei.codelab.component.listcomponent.CommentViewHolder;
import com.huawei.codelab.component.listcomponent.ListComponentAdapter;

import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DeviceSelectDialog
 *
 * @since 2021-04-06
 */
public class DeviceSelectDialog extends CommonDialog {
    private static final int CORNER_RADIUS = 10;
    private ListContainer listContainer;
    private final Context context;
    private OnclickListener onclickListener;
    private final List<DeviceData> deviceList = new ArrayList<>(0);
    private final List<String> checkedDeviceIds = new ArrayList<>(0);

    private ListComponentAdapter listComponentAdapter;
    private Text operateConfirm;
    private Text operateCancel;

    /**
     * Interfaces for setting the OK button and canceling clicks
     *
     * @since 2021-04-06
     */
    public interface OnclickListener {
        /**
         * Used for interface callback.
         *
         * @param deviceIds deviceIds
         */
        void onConfirmClick(List<String> deviceIds);
    }

    /**
     * setListener
     *
     * @param context context
     */
    public DeviceSelectDialog(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * setListener
     *
     * @param listener listener
     */
    public void setListener(OnclickListener listener) {
        onclickListener = listener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        setAdapter();
    }

    private void initView() {
        Component rootView = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_dialog_layout_device, null, false);
        if (rootView.findComponentById(ResourceTable.Id_list_container_device) instanceof ListContainer) {
            listContainer = (ListContainer) rootView.findComponentById(ResourceTable.Id_list_container_device);
        }
        if (rootView.findComponentById(ResourceTable.Id_operate_yes) instanceof Text) {
            operateConfirm = (Text) rootView.findComponentById(ResourceTable.Id_operate_yes);
        }
        if (rootView.findComponentById(ResourceTable.Id_operate_no) instanceof Text) {
            operateCancel = (Text) rootView.findComponentById(ResourceTable.Id_operate_no);
        }
        setSize(MATCH_PARENT, MATCH_CONTENT);
        setAlignment(LayoutAlignment.BOTTOM);
        setCornerRadius(CORNER_RADIUS);
        setAutoClosable(true);
        setContentCustomComponent(rootView);
        setTransparent(true);
        componentBonding();
    }

    private void componentBonding() {
        operateConfirm.setClickedListener(component -> {
            if (onclickListener != null) {
                checkedDeviceIds.clear();
                cirDevice();
            }
        });
        operateCancel.setClickedListener(component -> hide());
    }

    private void cirDevice() {
        for (DeviceData deviceData : deviceList) {
            if (deviceData.isChecked()) {
                checkedDeviceIds.add(deviceData.getDeviceInfo().getDeviceId());
            }
        }
        onclickListener.onConfirmClick(checkedDeviceIds);
    }

    private void setAdapter() {
        List<DeviceInfo> deviceInfoList = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        deviceList.clear();
        for (DeviceInfo deviceInfo : deviceInfoList) {
            deviceList.add(new DeviceData(false, deviceInfo));
        }
        listComponentAdapter = new ListComponentAdapter<DeviceData>(context,
                deviceList, ResourceTable.Layout_dialog_device_item) {
            @Override
            public void onBindViewHolder(CommentViewHolder commonViewHolder, DeviceData item, int position) {
                commonViewHolder.getTextView(ResourceTable.Id_item_desc)
                        .setText(item.getDeviceInfo().getDeviceName());
                switch (item.getDeviceInfo().getDeviceType()) {
                    case SMART_PHONE:
                        commonViewHolder.getImageView(ResourceTable.Id_item_type)
                                .setPixelMap(ResourceTable.Media_dv_phone);
                        break;
                    case SMART_PAD:
                        commonViewHolder.getImageView(ResourceTable.Id_item_type)
                                .setPixelMap(ResourceTable.Media_dv_pad);
                        break;
                    case SMART_WATCH:
                        commonViewHolder.getImageView(ResourceTable.Id_item_type)
                                .setPixelMap(ResourceTable.Media_dv_watch);
                        break;
                    default:
                        break;
                }
                commonViewHolder.getImageView(ResourceTable.Id_item_check).setPixelMap(item.isChecked()
                        ? ResourceTable.Media_checked_point : ResourceTable.Media_uncheck_point);
            }

            @Override
            public void onItemClick(Component component, DeviceData item, int position) {
                super.onItemClick(component, item, position);
                deviceList.get(position).setChecked(!item.isChecked());
                listComponentAdapter.notifyDataChanged();
            }
        };
        listContainer.setItemProvider(listComponentAdapter);
    }
}
