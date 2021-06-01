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

package com.huawei.codelab.devices;

import com.huawei.codelab.ResourceTable;

import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.distributedschedule.interwork.DeviceInfo;

import java.util.List;

/**
 * Select Device Dialog
 *
 * @since 2021-01-11
 */
public class SelectDeviceDialog {
    private static final int DIALOG_WIDTH = 840;

    private static final int DIALOG_HEIGHT = 900;

    private CommonDialog commonDialog;

    /**
     * SelectDeviceDialog
     *
     * @param context Context
     * @param devices List
     * @param listener SelectResultListener
     * @since 2021-03-12
     */
    public SelectDeviceDialog(Context context, List<DeviceInfo> devices, SelectResultListener listener) {
        initView(context, devices, listener);
    }

    private void initView(Context context, List<DeviceInfo> devices, SelectResultListener listener) {
        commonDialog = new CommonDialog(context);
        commonDialog.setAlignment(LayoutAlignment.CENTER);
        commonDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        commonDialog.setAutoClosable(true);
        Component dialogLayout =
            LayoutScatter.getInstance(context).parse(ResourceTable.Layout_dialog_select_device, null, false);
        commonDialog.setContentCustomComponent(dialogLayout);
        if (dialogLayout.findComponentById(ResourceTable.Id_list_devices) instanceof ListContainer) {
            ListContainer devicesListContainer =
                (ListContainer) dialogLayout.findComponentById(ResourceTable.Id_list_devices);
            DevicesListAdapter devicesListAdapter = new DevicesListAdapter(devices, context);
            devicesListContainer.setItemProvider(devicesListAdapter);
            devicesListContainer.setItemClickedListener((listContainer, component, position, id) -> {
                listener.callBack(devices.get(position));
                commonDialog.hide();
            });
        }
        dialogLayout.findComponentById(ResourceTable.Id_cancel).setClickedListener(component -> {
            commonDialog.hide();
        });
    }

    /**
     * Show Dialog
     */
    public void show() {
        commonDialog.show();
    }

    /**
     * select device result call back interface
     *
     * @since 2021-01-12
     */
    public interface SelectResultListener {
        /**
         * callBack
         *
         * @param deviceInfo DeviceInfo
         */
        void callBack(DeviceInfo deviceInfo);
    }
}
