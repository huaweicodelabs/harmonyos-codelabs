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
import com.huawei.cookbook.ble.subscriber.BluetoothAdapterStateSubscriber;
import com.huawei.cookbook.util.GattCharacteristicConstants;
import com.huawei.cookbook.util.GattDescriptorConstants;
import com.huawei.cookbook.util.LogUtils;
import com.huawei.cookbook.util.UIManager;

import ohos.app.Context;
import ohos.bluetooth.BluetoothHost;
import ohos.bluetooth.ProfileBase;
import ohos.bluetooth.ble.BleCentralManager;
import ohos.bluetooth.ble.BleCentralManagerCallback;
import ohos.bluetooth.ble.BlePeripheralCallback;
import ohos.bluetooth.ble.BlePeripheralDevice;
import ohos.bluetooth.ble.BleScanFilter;
import ohos.bluetooth.ble.BleScanResult;
import ohos.bluetooth.ble.GattCharacteristic;
import ohos.bluetooth.ble.GattService;
import ohos.event.commonevent.CommonEventManager;
import ohos.event.commonevent.CommonEventSubscribeInfo;
import ohos.event.commonevent.MatchingSkills;
import ohos.rpc.RemoteException;
import ohos.utils.zson.ZSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * ble manager
 *
 * @ClassName: BleCenterManager
 * @Description: java description
 */
public class BleManager {
    private static final UUID CHARACTERISTIC_READ_UUID = UUID.fromString("15f1e001-a277-43fc-a484-dd39ef8a9100");

    private static final UUID CHARACTERISTIC_WRITE_UUID = UUID.fromString("15f1f001-a277-43fc-a484-dd39ef8a9100");

    private static final int DELAY_TIME = 1000;

    private static final int STATE_NUMBER = 2;
    /**
     * 连接特征描述确认UUID
     */
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR =
            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    // 特征标识（notify数据）
    private static final UUID CHARACTERISTIC_NOTIFY_UUID = UUID.fromString("0000fda5-0000-1000-8000-00805f9b12ea");

    private static final UUID CHARACTERISTIC_DEV_INFO_UUID = UUID.fromString("15f1e401-a277-43fc-a484-dd39ef8a9100");

    private BluetoothHost bluetoothHost;
    private final BleCentralManager centralManager;
    private final List<UUID> uuidList;
    private boolean isScanning = false;
    private String macAddress;
    private Optional<BlePeripheralDevice> peripheralDevice;
    private GattCharacteristic mWriteChar;
    private GattCharacteristic mReadChar;
    private GattCharacteristic mReadVersionChar;
    private GattCharacteristic mNotifyChar;

    private ConnectStateCallback mConnectStateCallback;
    private DeviceInfo mDeviceInfoCallback;
    private final BluetoothAdapterStateSubscriber bluetoothAdapterStateSubscriber;
    private boolean isConnect = false;
    private boolean isRegister = false;

    /**
     * ble manager
     *
     * @param context context
     */
    public BleManager(Context context) {
        uuidList = new ArrayList<>();
        bluetoothHost = BluetoothHost.getDefaultHost(context);
        centralManager = new BleCentralManager(context, getCentralManagerCallback());
        bluetoothAdapterStateSubscriber = new BluetoothAdapterStateSubscriber(getStageChangeSubscribeInfo());
        try {
            CommonEventManager.subscribeCommonEvent(bluetoothAdapterStateSubscriber);
        } catch (RemoteException e) {
            LogUtils.erro("BleCentralOperator construct exception: " + e.getCause().getLocalizedMessage());
        }
    }

    private CommonEventSubscribeInfo getStageChangeSubscribeInfo() {
        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(BluetoothHost.EVENT_HOST_STATE_UPDATE);
        matchingSkills.addEvent(BluetoothHost.EVENT_HOST_DISCOVERY_STARTED);
        matchingSkills.addEvent(BluetoothHost.EVENT_HOST_DISCOVERY_FINISHED);
        return new CommonEventSubscribeInfo(matchingSkills);
    }

    /**
     * start bluetooth devices discovery
     *
     * @param services services
     * @param mac mac
     * @param interval interval
     * @return int
     */
    public int startBluetoothDevicesDiscovery(UUID[] services, String mac, int interval) {
        LogUtils.info("startBluetoothDevicesDiscovery mac = " + mac);
        this.macAddress = mac;
        uuidList.clear();
        uuidList.addAll(Arrays.asList(services));
        LogUtils.info("interval = " + interval);
        LogUtils.info("interval = " + interval);
        boolean hasConnect = hasConnected(macAddress);
        LogUtils.error("hasConnected is = " + hasConnect);
        if (hasConnect) {
            connectToDevice(macAddress);
        } else {
            unConnectBle(interval);
        }
        return BleOperatorConstants.ERROR_CODE_OK;
    }

    private void unConnectBle(int interval) {
        UIManager.get()
            .postEventDelay(
                (Runnable)
                () -> {
                    LogUtils.info("isScanning = " + isScanning);
                    if (isScanning) {
                        stopScan();
                        mConnectStateCallback.stateChange(1, "have no discovey");
                    }
                }, interval * DELAY_TIME);
        isScanning = true;
        List<BleScanFilter> emptyList = new ArrayList<>();
        centralManager.startScan(emptyList);
    }

    private boolean hasConnected(String mac) {
        List<BlePeripheralDevice> devicesByStates =
                centralManager.getDevicesByStates(new int[] {ProfileBase.STATE_CONNECTED});
        LogUtils.error("devicesByStates = " + ZSONObject.toZSONString(devicesByStates));
        for (int i = 0; devicesByStates != null && i < devicesByStates.size(); i++) {
            BlePeripheralDevice device = devicesByStates.get(i);
            if (mac.equals(device.getDeviceAddr())) {
                return true;
            }
        }
        return false;
    }

    /**
     * set register
     *
     * @param flag flag
     */
    public void setRegister(boolean flag) {
        this.isRegister = flag;
    }

    /**
     * set connect state callback
     *
     * @param callback callback
     */
    public void setConnectStateCallback(ConnectStateCallback callback) {
        this.mConnectStateCallback = callback;
    }

    /**
     * set device info callback
     *
     * @param callback callback
     */
    public void setDeviceInfoCallback(DeviceInfo callback) {
        LogUtils.erro("setDeviceInfoCallback");
        this.mDeviceInfoCallback = callback;
    }

    /**
     * set bluetooth adapter state change callback
     *
     * @param callback callback
     */
    public void setBluetoothAdapterStateChangeCallback(BluetoothAdapterStateChangeCallback callback) {
        bluetoothAdapterStateSubscriber.setBluetoothAdapterStateChangeCallback(callback);
    }

    /**
     * get bluetooth adapter state
     *
     * @param stateCallback stateCallback
     */
    public void getBluetoothAdapterState(BluetoothAdapterStateCallback stateCallback) {
        if (stateCallback != null) {
            boolean isDiscovering = bluetoothHost.isBtDiscovering();
            boolean isOpen = bluetoothHost.getBtState() == BluetoothHost.STATE_ON;
            stateCallback.getBluetoothAdapterState(isDiscovering, isOpen, BleOperatorConstants.ERROR_CODE_OK);
        }
    }

    /**
     * is blue open
     *
     * @return boolean
     */
    public boolean isBlueOpen() {
        if (bluetoothHost != null) {
            return bluetoothHost.getBtState() == BluetoothHost.STATE_ON
                    || bluetoothHost.getBtState() == BluetoothHost.STATE_BLE_ON;
        }
        return false;
    }

    private void stopScan() {
        isScanning = false;
        centralManager.stopScan();
    }

    /**
     * dis connect device
     *
     * @return int
     */
    public int disConnectDevice() {
        if (isScanning) {
            stopScan();
        }
        int code =
            peripheralDevice
                .map(
                    device ->
                        device.disconnect() && device.close()
                                ? BleOperatorConstants.ERROR_CODE_OK
                                : BleOperatorConstants.ERROR_CODE_DISCONNECT_ERR)
            .orElse(BleOperatorConstants.ERROR_CODE_COMMON_ERR);
        LogUtils.info("disConnectDevice code = " + code);
        if (code == 0) {
            this.isConnect = false;
        }
        return code;
    }

    private BleCentralManagerCallback getCentralManagerCallback() {
        return new BleCentralManagerCallback() {
            @Override
            public void scanResultEvent(BleScanResult bleScanResult) {
                handlerScanResultEvent(bleScanResult);
            }

            @Override
            public void scanFailedEvent(int index) { }

            @Override
            public void groupScanResultsEvent(List<BleScanResult> list) { }
        };
    }

    // 处理扫码结果
    private void handlerScanResultEvent(BleScanResult bleScanResult) {
        if (bleScanResult == null) {
            return;
        }
        BlePeripheralDevice device = bleScanResult.getPeripheralDevice();
        if (device == null) {
            return;
        }

        LogUtils.info("scan name = " + device.getDeviceName().get());
        List<UUID> uuids = bleScanResult.getServiceUuids();
        if (uuids != null) {
            LogUtils.info("UUIDs:" + uuids.toString());
        }
        stopScanAndConnectDevice(device);
    }

    private void stopScanAndConnectDevice(BlePeripheralDevice device) {
        String mac = device.getDeviceAddr();
        if (mac.equals(macAddress)) {
            if (isScanning) {
                stopScan();
            }
            connectToDevice(mac);
        }
    }

    /**
     * connect to device
     *
     * @param deviceId deviceId
     */
    public void connectToDevice(String deviceId) {
        LogUtils.info("deviceId = " + deviceId);
        BlePeripheralDevice device = BlePeripheralDevice.createInstance(deviceId);
        peripheralDevice = Optional.ofNullable(device);
        peripheralDevice.map(peripheralDevice -> peripheralDevice.connect(false, getBlePeripheralCallback(device)));
    }

    private BlePeripheralCallback getBlePeripheralCallback(BlePeripheralDevice device) {
        return new BlePeripheralCallback() {
            @Override
            public void servicesDiscoveredEvent(int status) {
                super.servicesDiscoveredEvent(status);
                // 发现服务
                LogUtils.info("servicesDiscoveredEvent");
                onServiceDiscoveredEvent(status, device);
            }

            @Override
            public void connectionStateChangeEvent(int connectionState) {
                super.connectionStateChangeEvent(connectionState);
                // 连接的回调
                LogUtils.info("connectionStateChangeEvent");
                onConnectionStateChangeEvent(connectionState);
            }

            @Override
            public void characteristicReadEvent(GattCharacteristic characteristic, int ret) {
                super.characteristicReadEvent(characteristic, ret);
                updateDeviceReadEvent(characteristic, ret);
            }

            @Override
            public void characteristicWriteEvent(GattCharacteristic characteristic, int ret) {
                super.characteristicWriteEvent(characteristic, ret);
                updateDeviceWriteEvent(characteristic, ret);
            }

            @Override
            public void characteristicChangedEvent(GattCharacteristic characteristic) {
                super.characteristicChangedEvent(characteristic);
                updateDevicechangeEvent(characteristic);
            }
        };
    }

    private void updateDevicechangeEvent(GattCharacteristic characteristic) {
        LogUtils.info("characteristicChangedEvent");
        byte[] byteData = characteristic.getValue();
        String data;
        data = new String(byteData, StandardCharsets.UTF_8);
        LogUtils.info("characteristicChangedEvent = " + data);
        if (mDeviceInfoCallback != null) {
            mDeviceInfoCallback.updateDeviceInfo(data);
        }
    }

    private void updateDeviceWriteEvent(GattCharacteristic characteristic, int ret) {
        LogUtils.info("characteristicWriteEvent ret = " + ret);
        if (ret == BlePeripheralDevice.OPERATION_SUCC) {
            byte[] byteData = characteristic.getValue();
            String data;
            data = new String(byteData, StandardCharsets.UTF_8);
            LogUtils.info("characteristicWriteEvent = " + data);
            if (mDeviceInfoCallback != null) {
                mDeviceInfoCallback.updateDeviceInfo(data);
                getDeviceInfo();
            }
        }
    }

    private void updateDeviceReadEvent(GattCharacteristic characteristic, int ret) {
        LogUtils.info("characteristicReadEventret = " + ret);
        if (ret == BlePeripheralDevice.OPERATION_SUCC) {
            byte[] byteData = characteristic.getValue();
            String data;
            data = new String(byteData, StandardCharsets.UTF_8);
            LogUtils.info("characteristicReadEvent = " + data);
            if (mDeviceInfoCallback != null) {
                mDeviceInfoCallback.updateDeviceInfo(data);
            }
        }
    }

    private void onConnectionStateChangeEvent(int connectionState) {
        LogUtils.info("connectionState = " + connectionState);
        if (connectionState == ProfileBase.STATE_CONNECTED) {
            isConnect = true;
            discoveryServices();
        } else if (connectionState == ProfileBase.STATE_DISCONNECTED) {
            LogUtils.erro("断开连接");
            isConnect = false;
            disConnectDevice();
        }
    }

    /**
     * is connect
     *
     * @return boolean
     */
    public boolean isConnect() {
        return isConnect;
    }

    private void onServiceDiscoveredEvent(int status, BlePeripheralDevice device) {
        LogUtils.info("servicesDiscoveredEvent status = " + status);
        if (status == BlePeripheralDevice.OPERATION_SUCC) {
            List<GattService> services = device.getServices();
            LogUtils.info("services size = " + services.size());
            LogUtils.info("services content = " + services.toString());
            for (GattService gattService : services) {
                checkGattCharacteristic(gattService, device);
            }
            mConnectStateCallback.stateChange(STATE_NUMBER, "connected");
        }
    }

    private void discoveryServices() {
        peripheralDevice.map(BlePeripheralDevice::discoverServices);
    }

    private void checkGattCharacteristic(GattService service, BlePeripheralDevice device) {
        for (GattCharacteristic gattCharacteristic : service.getCharacteristics()) {
            LogUtils.info("uuid = " + gattCharacteristic.getUuid());
            handleGattCharacteristic(device, gattCharacteristic);
        }
    }

    private void handleGattCharacteristic(BlePeripheralDevice device, GattCharacteristic gattCharacteristic) {
        if (gattCharacteristic.getUuid().equals(CHARACTERISTIC_NOTIFY_UUID)) {
            LogUtils.info("notify");
            mNotifyChar = gattCharacteristic;
            testSetNotify(mNotifyChar, device);
        }

        if (gattCharacteristic.getUuid().equals(CHARACTERISTIC_WRITE_UUID)) {
            LogUtils.info("write");
            mWriteChar = gattCharacteristic;
        }
        if (gattCharacteristic.getUuid().equals(CHARACTERISTIC_READ_UUID)) {
            LogUtils.info("read");
            mReadChar = gattCharacteristic;
        }

        if (gattCharacteristic.getUuid().equals(CHARACTERISTIC_DEV_INFO_UUID)) {
            LogUtils.erro("service = " + gattCharacteristic.getService().getUuid());
            mReadVersionChar = gattCharacteristic;
        }
    }

    private void testSetNotify(
            GattCharacteristic gattCharacteristic, BlePeripheralDevice blePeripheralDevice) {
        byte[] descriptorValue = GattDescriptorConstants.getEnableNotificationValue();
        LogUtils.info("enableCharacteristicNotifyOrIndicate: isEnable = " + true);
        int propertyFlag = GattCharacteristicConstants.PROPERTY_NOTIFY;
        if ((gattCharacteristic != null)
                && ((gattCharacteristic.getProperties() & propertyFlag) != 0)
                && (blePeripheralDevice != null)) {
            LogUtils.info("gattCharacteristic getProperties = " + gattCharacteristic.getProperties());
            blePeripheralDevice.setNotifyCharacteristic(gattCharacteristic, true);
            gattCharacteristic
                .getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR)
                .ifPresent(
                    descriptor -> {
                        LogUtils.erro("finalDescriptorValue = " + Arrays.toString(descriptorValue));
                        Optional.of(descriptor).get().setValue(descriptorValue);
                        boolean flag =
                                blePeripheralDevice.writeDescriptor(Optional.of(descriptor).get());
                        LogUtils.erro("test notify flag = " + flag);
                    });
        }
    }

    /**
     * get device info
     */
    public void getDeviceInfo() {
        if (mReadChar != null && peripheralDevice.isPresent()) {
            peripheralDevice.get().readCharacteristic(mReadChar);
        }
    }

    /**
     * control device
     *
     * @param value value
     */
    public void controlDevice(String value) {
        LogUtils.erro("controlDevice value = " + value);
        if (mWriteChar != null) {
            mWriteChar.setValue(value.getBytes(StandardCharsets.UTF_8));
            if (peripheralDevice.isPresent()) {
                boolean flag = peripheralDevice.get().writeCharacteristic(mWriteChar);
                LogUtils.erro("WRITE FLAG IS : " + flag);
            }
        }
    }

    /**
     * get version
     */
    public void getVersion() {
        LogUtils.error("getVersion getVersion mReadVersionChar = " + mReadVersionChar);
        if (mReadVersionChar != null && peripheralDevice.isPresent()) {
            peripheralDevice.get().readCharacteristic(mReadVersionChar);
        }
    }
}
