package com.huawei.codelab.distribute.factory;

import com.huawei.codelab.distribute.api.SelectDeviceResultListener;
import com.huawei.codelab.distribute.constant.DistributeConst;
import com.huawei.codelab.utils.LogUtils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.continuation.*;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.distributedschedule.interwork.IInitCallback;
import ohos.rpc.RemoteException;

import java.util.List;

// 设置初始化分布式环境的回调
// 设置流转任务管理服务设备状态变更的回调
// 设置注册流转任务管理服务回调
public class DeviceSelector implements IInitCallback, IContinuationDeviceCallback, RequestCallback {
    private int token;
    private boolean isSetup;
    private DeviceInfo deviceInfo;
    private SelectDeviceResultListener selectDeviceResultListener;

    // 获取流转任务管理服务管理类
    private IContinuationRegisterManager continuationRegisterManager;

    public DeviceSelector() {
        deviceInfo = new DeviceInfo();
    }

    public void setup(Ability ability) {
        if (!isSetup) {
            isSetup = true;
            continuationRegisterManager = ability.getContinuationRegisterManager();
             //注册流转任务管理服务
            continuationRegisterManager.register(ability.getBundleName(), null, this, this);
        }
    }

    public void showDistributeDevices(String[] devTypes, String ext) {
        if (isSetup) {
            LogUtils.error(DistributeConst.TAG, "showDistributeDevices is called");
            // 设置过滤设备类型
            ExtraParams params = new ExtraParams();
            params.setDevType(devTypes);
            params.setJsonParams(ext);
            // 显示选择设备列表
            continuationRegisterManager.showDeviceList(token, params, null);
        } else {
            LogUtils.error(DistributeConst.TAG, "Please use setup method first!");
        }
    }

    public void destroy() {
        continuationRegisterManager.disconnect();
        if (isSetup) {
            isSetup = false;
            // 解注册流转任务管理服务
            continuationRegisterManager.unregister(token, null);
            // 断开流转任务管理服务连接
            continuationRegisterManager.disconnect();
        }
    }

    public void setSelectDeviceResultListener(SelectDeviceResultListener listener) {
        selectDeviceResultListener = listener;
    }

    private void setRemoteDeviceInfo(String selectedId) {
        List<DeviceInfo> distributeDeviceDatas = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        for (DeviceInfo distributeDeviceData : distributeDeviceDatas) {
            if (distributeDeviceData.getDeviceId().equals(selectedId)) {
                deviceInfo = distributeDeviceData;
                return;
            }
        }
        deviceInfo.setDeviceInfo(selectedId, "");
    }

    @Override
    public void onResult(int result) {
        LogUtils.info(DistributeConst.TAG, "IContinuationRegisterManager register success,result is: " + result);
        token = result;
    }

    @Override
    public void onInitSuccess(String deviceId) {
        LogUtils.info(DistributeConst.TAG, "device id success: " + deviceId);
        if (selectDeviceResultListener != null) {
            selectDeviceResultListener.onSuccess(deviceInfo);
        }
    }

    @Override
    public void onInitFailure(String deviceId, int errorCode) {
        LogUtils.info(DistributeConst.TAG, "device id failed: " + deviceId + "errorCode: " + errorCode);
        if (selectDeviceResultListener != null) {
            selectDeviceResultListener.onFail(deviceInfo);
        }
    }

    @Override
    public void onDeviceConnectDone(String deviceId, String deviceType) {
        LogUtils.info(DistributeConst.TAG, "select done,onDeviceConnectDone is called: " + deviceId);
        setRemoteDeviceInfo(deviceId);
        try {
            // 初始化分布式环境
            DeviceManager.initDistributedEnvironment(deviceId, this);
        } catch (RemoteException e) {
            LogUtils.info(DistributeConst.TAG, "initDistributedEnvironment failed");
        }
        continuationRegisterManager.updateConnectStatus(token, deviceId, DeviceConnectState.CONNECTED.getState(), null);
    }

    @Override
    public void onDeviceDisconnectDone(String deviceId) {
        LogUtils.info(DistributeConst.TAG, "onDeviceDisconnectDone is called: " + deviceId);
        continuationRegisterManager.updateConnectStatus(token, deviceId, DeviceConnectState.IDLE.getState(), null);
    }
}
