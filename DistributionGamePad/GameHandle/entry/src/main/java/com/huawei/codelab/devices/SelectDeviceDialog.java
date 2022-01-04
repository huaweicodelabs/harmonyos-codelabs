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

import static ohos.security.SystemPermission.DISTRIBUTED_DATASYNC;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.utils.Constants;
import com.huawei.codelab.utils.LogUtil;

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.bundle.IBundleManager;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择设备列表的弹窗
 *
 * @since 2021-01-11
 */
public class SelectDeviceDialog {
    private CommonDialog commonDialog;

    private DevicesListAdapter devicesListAdapter;

    private List<DeviceInfo> allDevices;

    private final AbilitySlice abilitySlice;

    private final SelectResultListener listener;

    /**
     * 选择连接设备的回调
     *
     * @since 2021-03-15
     */
    public interface SelectResultListener {
        /**
         * 功能描述
         *
         * @param selectDevices selectDevices
         * @since 2021-03-16
         */
        void callBack(List<DeviceInfo> selectDevices);
    }

    /**
     * 构造方法
     *
     * @param slice 当前Ability
     * @param listener 回调
     * @since 2021-03-15
     */
    public SelectDeviceDialog(AbilitySlice slice, SelectResultListener listener) {
        abilitySlice = slice;
        this.listener = listener;
        allDevices = new ArrayList<>(0);
    }

    private void getDevices() {
        if (allDevices.size() > 0) {
            allDevices.clear();
        }
        allDevices = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        LogUtil.info("TAG", "deviceInfos size is :" + allDevices.size());
    }

    private void grantPermission(AbilitySlice slice) {
        if (slice.verifySelfPermission(DISTRIBUTED_DATASYNC) != IBundleManager.PERMISSION_GRANTED) {
            if (slice.canRequestPermission(DISTRIBUTED_DATASYNC)) {
                slice.requestPermissionsFromUser(new String[] {DISTRIBUTED_DATASYNC}, 0);
            }
        }
    }

    /**
     * 初始化设备选择弹窗
     *
     * @return 设备选择弹窗
     */
    public SelectDeviceDialog initDialog() {
        grantPermission(abilitySlice);
        getDevices();
        Context context = abilitySlice.getContext();
        commonDialog = new CommonDialog(context);
        commonDialog.setAlignment(LayoutAlignment.CENTER);
        commonDialog.setSize(Constants.COMMON_DIALOG_WIDTH, Constants.COMMON_DIALOG_HIGHT);
        commonDialog.setAutoClosable(false);
        Component dialogLayout =
            LayoutScatter.getInstance(context).parse(ResourceTable.Layout_dialog_select_device, null, false);
        commonDialog.setContentCustomComponent(dialogLayout);
        if (dialogLayout.findComponentById(ResourceTable.Id_list_devices) instanceof ListContainer) {
            ListContainer devicesListContainer =
                (ListContainer) dialogLayout.findComponentById(ResourceTable.Id_list_devices);
            devicesListAdapter = new DevicesListAdapter(allDevices, context);
            devicesListContainer.setItemProvider(devicesListAdapter);
        }

        dialogLayout.findComponentById(ResourceTable.Id_confirm).setClickedListener(component -> {
            listener.callBack(getSelectDevices());
            commonDialog.hide();
        });
        return this;
    }

    /**
     * 弹窗显示
     */
    public void show() {
        commonDialog.show();
    }

    /**
     * 被选择的设备列表
     *
     * @return 被选择的设备列表
     */
    public List<DeviceInfo> getSelectDevices() {
        if (devicesListAdapter != null) {
            return devicesListAdapter.getSelectDevices();
        }
        return new ArrayList<>(0);
    }
}
