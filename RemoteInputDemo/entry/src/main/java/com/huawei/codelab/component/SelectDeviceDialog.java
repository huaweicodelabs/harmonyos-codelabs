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

package com.huawei.codelab.component;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.adapter.DevicesListAdapter;

import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备选择弹框
 *
 * @since 2021-02-25
 */
public class SelectDeviceDialog {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int INIT_SIZE = 10;
    private CommonDialog commonDialog;
    private List<DeviceInfo> deviceIfs = new ArrayList<>(INIT_SIZE);

    /**
     * 设备选择监听
     *
     * @since 2021-02-25
     */
    public interface SelectResultListener {
        /**
         * 设备选择回调
         *
         * @param deviceInfo 设备信息
         */
        void callBack(DeviceInfo deviceInfo);
    }

    /**
     * 设备选择回调弹框
     *
     * @param context 上下文
     * @param callBack 回调
     * @since 2021-02-25
     */
    public SelectDeviceDialog(Context context, SelectResultListener callBack) {
        getDevices(context, callBack);
    }

    private void getDevices(Context context, SelectResultListener callBack) {
        if (deviceIfs.size() > 0) {
            deviceIfs.clear();
        }
        List<DeviceInfo> deviceInfos =
                DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        deviceIfs.addAll(deviceInfos);
        initView(context, deviceIfs, callBack);
    }

    private void initView(Context context, List<DeviceInfo> devices, SelectResultListener listener) {
        commonDialog = new CommonDialog(context);
        commonDialog.setAlignment(LayoutAlignment.CENTER);
        Component dialogLayout = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_dialog_select_device, null, false);
        commonDialog.setSize(WIDTH, HEIGHT);
        commonDialog.setAutoClosable(true);
        commonDialog.setContentCustomComponent(dialogLayout);
        if (dialogLayout.findComponentById(ResourceTable.Id_list_devices) instanceof ListContainer) {
            ListContainer devicesListContainer = (ListContainer) dialogLayout
                    .findComponentById(ResourceTable.Id_list_devices);
            DevicesListAdapter devicesListAdapter = new DevicesListAdapter(devices, context);
            devicesListContainer.setItemProvider(devicesListAdapter);
            devicesListContainer.setItemClickedListener((listContainer, component, position, ll) -> {
                listener.callBack(devices.get(position));
                commonDialog.hide();
            });
        }
        dialogLayout.findComponentById(ResourceTable.Id_cancel).setClickedListener(component -> {
            commonDialog.hide();
        });
    }

    /**
     * 显示弹框
     */
    public void show() {
        commonDialog.show();
    }
}
