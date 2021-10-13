/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.huawei.cookbook.ble;

import com.huawei.cookbook.ble.listener.BluetoothAdapterStateCallback;
import com.huawei.cookbook.ble.listener.BluetoothAdapterStateChangeCallback;
import com.huawei.cookbook.ble.listener.ConnectStateCallback;
import com.huawei.cookbook.ble.listener.DeviceInfo;
import com.huawei.cookbook.util.LogUtils;

import ohos.app.Context;

import java.util.Optional;
import java.util.UUID;

/**
 * ble helper
 *
 * @ClassName: BleManager
 * @Description: java description
 */
public class BleHelper {
    private Optional<BleManager> bleCenterManager;

    public BleHelper() { }

    /**
     * is ble connected
     *
     * @return boolean
     */
    public boolean isBleConnected() {
        return bleCenterManager.map(BleManager::isConnect).orElse(false);
    }

    /**
     * start bluetooth devices discovery
     *
     * @param services services
     * @param mac mac
     * @param interval interval
     * @return String
     */
    public String startBluetoothDevicesDiscovery(UUID[] services, String mac, int interval) {
        int code = 0;
        if (bleCenterManager.isPresent()) {
            code = bleCenterManager.get().startBluetoothDevicesDiscovery(services, mac, interval);
        }
        return String.valueOf(code);
    }

    /**
     * get bluetooth adapter state
     *
     * @param stateCallback stateCallback
     */
    public void getBluetoothAdapterState(BluetoothAdapterStateCallback stateCallback) {
        if (stateCallback != null && bleCenterManager.isPresent()) {
            bleCenterManager.get().getBluetoothAdapterState(stateCallback);
        }
    }

    /**
     * get version
     */
    public void getVersion() {
        bleCenterManager.ifPresent(BleManager::getVersion);
    }

    /**
     * set register
     *
     * @param flag flag
     */
    public void setRegister(boolean flag) {
        bleCenterManager.ifPresent(bleManager -> bleManager.setRegister(flag));
    }

    /**
     * get state callback
     *
     * @param callback callback
     */
    public void getStateCallback(ConnectStateCallback callback) {
        bleCenterManager.ifPresent(bleManager -> bleManager.setConnectStateCallback(callback));
    }

    /**
     * on bluetooth adapter state change
     *
     * @param callback callback
     */
    public void onBluetoothAdapterStateChange(BluetoothAdapterStateChangeCallback callback) {
        bleCenterManager.ifPresent(bleManager -> bleManager.setBluetoothAdapterStateChangeCallback(callback));
    }

    /**
     * create ble connected
     *
     * @param deviceId deviceId
     */
    public void createBleConnected(String deviceId) {
        bleCenterManager.ifPresent(bleManager -> bleManager.connectToDevice(deviceId));
    }

    /**
     * set device info callback
     *
     * @param callback callback
     */
    public void setDeviceInfoCallback(DeviceInfo callback) {
        bleCenterManager.ifPresent(bleManager -> bleManager.setDeviceInfoCallback(callback));
    }

    /**
     * get device info
     */
    public void getDeviceInfo() {
        bleCenterManager.ifPresent(BleManager::getDeviceInfo);
    }

    /**
     * dis connect
     */
    public void disConnect() {
        bleCenterManager.ifPresent(BleManager::disConnectDevice);
    }

    /**
     * control device
     *
     * @param value value
     */
    public void controlDevice(String value) {
        bleCenterManager.ifPresent(bleManager -> bleManager.controlDevice(value));
    }

    /**
     * close ble connection
     *
     * @param deviceId deviceId
     * @return String
     */
    public String closeBleConnection(String deviceId) {
        LogUtils.erro("closeBleConnection code = " + deviceId);
        int code = 0;
        if(bleCenterManager.isPresent()){
            code = bleCenterManager.get().disConnectDevice();
            LogUtils.erro("closeBleConnection code = " + code);
        }
        return String.valueOf(code);
    }

    /**
     * 判断蓝牙开关是否打开
     *
     * @return boolean
     */
    public boolean isBlueOpen() {
        return bleCenterManager.map(BleManager::isBlueOpen).orElse(false);
    }

    /**
     * init
     *
     * @param context context
     */
    public void init(Context context) {
        BleManager manager = new BleManager(context);
        bleCenterManager = Optional.of(manager);
    }

    /**
     * get instance
     *
     * @return BleHelper
     */
    public static BleHelper getInstance() {
        return BluetoothHelper.INSTANCE;
    }

    /**
     * bluetooth helper
     *
     * @author x00424131
     * @since 2021/09/17
     */
    private static class BluetoothHelper {
        /**
         * instance
         */
        public static final BleHelper INSTANCE = new BleHelper();
    }
}
