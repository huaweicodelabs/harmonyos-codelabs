package com.huawei.codelab.distribute.api;

import ohos.distributedschedule.interwork.DeviceInfo;

public interface SelectDeviceResultListener {
    void onSuccess(DeviceInfo info);
    void onFail(DeviceInfo info);
}
