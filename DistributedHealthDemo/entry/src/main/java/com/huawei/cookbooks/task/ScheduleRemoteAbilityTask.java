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

package com.huawei.cookbooks.task;

import com.huawei.cookbooks.remoteAbility.ISelectResult;
import com.huawei.cookbooks.remoteAbility.impl.ISelectResultImplStartPa;

import ohos.app.Context;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 心率异常拉起其他设备PA的任务
 *
 * @since 2021-05-29
 */
public class ScheduleRemoteAbilityTask implements Runnable {
    private final ISelectResult iSelectResult = new ISelectResultImplStartPa();

    private final Context context;

    public ScheduleRemoteAbilityTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        List<DeviceInfo> onlineDevices = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        // 判断组网设备是否为空
        if (onlineDevices.isEmpty()) {
            iSelectResult.onSelectResult(null, context);
            return;
        }
        ArrayList<String> deviceIds = new ArrayList<>();
        onlineDevices.forEach(device -> {
            if ("LAN".equals(device.getDeviceName())) {
                deviceIds.add(device.getDeviceId());
            }
        });
        if (deviceIds.size() == 0) {
            return;
        }
        String selectDeviceId = deviceIds.get(0);
        iSelectResult.onSelectResult(selectDeviceId, context);
    }
}
