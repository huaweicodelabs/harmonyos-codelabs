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

import com.huawei.cookbook.util.BasicConstants;
import com.huawei.cookbook.util.LogUtils;
import ohos.ace.ability.AceInternalAbility;
import ohos.app.Context;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * ble option ability
 *
 * @ClassName: BleOptionAbility
 * @Description: java description
 */
public class BleOptionAbility extends AceInternalAbility {
    private static final String BUNDLE_NAME = "com.huawei.cookbook";
    private static final String ABILITY_NAME = BleOptionAbility.class.getName();
    private Context mContext;
    private final Set<IRemoteObject> bleAdapterStateCallbackSet = new HashSet<>();
    private Optional<BleOptionHelper> bleOptionHelper;
    private final Set<IRemoteObject> bleStateCallbackSet = new HashSet<>();
    private final Set<IRemoteObject> bleDeviceInfoCallbackSet = new HashSet<>();
    private final Set<IRemoteObject> bleAdapterStateChangeCallbackSet = new HashSet<>();

    public BleOptionAbility() {
        super(BUNDLE_NAME, ABILITY_NAME);
    }

    private boolean onRemoteRequestCase(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
        switch (code) {
            case BleOperatorConstants.GET_BLUETOOTH_ADAPTER_STATE:
                return getBluetoothAdapterStateCase(data, reply, option);
            case BleOperatorConstants.START_BLUETOOTH_DEVICES_DISCOVERY:
                return startBluetoothDevicesDiscoveryCase(data, reply, option);
            case BleOperatorConstants.GET_BLE_CONNECTION_STATE:
                return getBleConnectState(data, reply, option);
            case BleOperatorConstants.GET_DEVICE_STATE:
                return setDeviceUpdateListener(data, reply, option);
            case BleOperatorConstants.GET_DEVICE_INFO:
                return getDeviceInfo(reply, option);
            case BleOperatorConstants.CONTROL_DEVICE:
                return controlDevice(data, reply, option);
            case BleOperatorConstants.ON_BLUETOOTH_ADAPTER_STATE_CHANGE:
                return onBluetoothAdapterStateChangeCase(data, reply, option);
            case BleOperatorConstants.IS_BLE_CONNECTED:
                return isBleConnected(reply, option);
            case BleOperatorConstants.CREATE_BLE_CONNECTION:
                return createBleConnected(data, reply, option);
            case BleOperatorConstants.CHECK_BLE_SWITCH:
                return checkBleSwitch(reply, option);
            case BleOperatorConstants.CLOSE_BLE_CONNECTION:
                return closeBleConnectionCase(data, reply, option);
            case BleOperatorConstants.GET_DEVICE_VERSION:
                return getDeviceVersion(reply, option);
        }
        return true;
    }

    private boolean getDeviceVersion(MessageParcel reply, MessageOption option) {
        return bleOptionHelper.map(optionHelper -> optionHelper.getDeviceVersion(reply, option)).orElse(false);
    }

    private boolean closeBleConnectionCase(MessageParcel data, MessageParcel reply, MessageOption option) {
        return bleOptionHelper.map(optionHelper -> optionHelper.closeBleConnection(data, reply, option)).orElse(false);
    }

    private boolean checkBleSwitch( MessageParcel reply, MessageOption option) {
        return bleOptionHelper.map(optionHelper -> optionHelper.checkBleSwitch(reply, option)).orElse(false);
    }

    private boolean createBleConnected(MessageParcel data, MessageParcel reply, MessageOption option) {
        return bleOptionHelper.map(optionHelper -> optionHelper.createBleConnected(data, reply, option)).orElse(false);
    }

    private boolean isBleConnected(MessageParcel reply, MessageOption option) {
        return bleOptionHelper.map(optionHelper -> optionHelper.isBleConnected(reply, option)).orElse(false);
    }

    private boolean onBluetoothAdapterStateChangeCase(MessageParcel data, MessageParcel reply, MessageOption option) {
        bleAdapterStateChangeCallbackSet.clear();
        bleAdapterStateChangeCallbackSet.add(data.readRemoteObject());
        if(bleOptionHelper.isPresent()){
            bleOptionHelper.get().onBluetoothAdapterStateChange(bleAdapterStateChangeCallbackSet);
            return bleOptionHelper.get().replyResult(reply, option, BasicConstants.SUCCESS, new HashMap<>());
        }
        return false;
    }

    private boolean controlDevice(MessageParcel data, MessageParcel reply, MessageOption option) {
        return bleOptionHelper.map(optionHelper -> optionHelper.controlDevice(data, reply, option)).orElse(false);
    }

    private boolean getDeviceInfo(MessageParcel reply, MessageOption option) {
        if(bleOptionHelper.isPresent()){
            bleOptionHelper.get().getDeviceInfo();
            return bleOptionHelper.get().replyResult(reply, option, BasicConstants.SUCCESS, new HashMap<>());
        }
        return false;
    }

    private boolean setDeviceUpdateListener(MessageParcel data, MessageParcel reply, MessageOption option) {
        bleDeviceInfoCallbackSet.clear();
        bleDeviceInfoCallbackSet.add(data.readRemoteObject());
        if(bleOptionHelper.isPresent()){
            bleOptionHelper.get().setDeviceUpdateLinstener(bleDeviceInfoCallbackSet);
            return bleOptionHelper.get().replyResult(reply, option, BasicConstants.SUCCESS, new HashMap<>());
        }
        return false;
    }

    private boolean getBleConnectState(MessageParcel data, MessageParcel reply, MessageOption option) {
        bleStateCallbackSet.clear();
        bleStateCallbackSet.add(data.readRemoteObject());
        if(bleOptionHelper.isPresent()){
            bleOptionHelper.get().setConnectStateListener(bleStateCallbackSet);
            return bleOptionHelper.get().replyResult(reply, option, BasicConstants.SUCCESS, new HashMap<>());
        }
        return false;
    }

    private boolean startBluetoothDevicesDiscoveryCase(MessageParcel data, MessageParcel reply, MessageOption option) {
        return bleOptionHelper.map(optionHelper -> optionHelper
                .startBluetoothDevicesDiscovery(data, reply, option)).orElse(false);
    }

    private boolean getBluetoothAdapterStateCase(MessageParcel data, MessageParcel reply, MessageOption option) {
        bleAdapterStateCallbackSet.clear();
        bleAdapterStateCallbackSet.add(data.readRemoteObject());
        if(bleOptionHelper.isPresent()){
            bleOptionHelper.get().getBluetoothAdapterState(bleAdapterStateCallbackSet);
            return bleOptionHelper.get().replyResult(reply, option, BasicConstants.SUCCESS, new HashMap<>());
        }
        return false;
    }

    /**
     * register
     *
     * @param context context
     */
    public void register(Context context) {
        LogUtils.erro("register");
        this.mContext = context;
        BleOptionHelper helper = new BleOptionHelper(mContext);
        if (bleOptionHelper == null) {
            LogUtils.erro("coming in");
            bleOptionHelper = Optional.of(helper);
        }
        setInternalAbilityHandler(
                this::onRemoteRequestCase);
    }

    /**
     * un register
     */
    public void unRegister() {
        LogUtils.erro("unRegister");
        this.mContext = null;
        setInternalAbilityHandler(null);
    }

    /**
     * get instance
     *
     * @return BleOptionAbility
     */
    public static BleOptionAbility getInstance() {
        return BleOptionAbilityHelper.INSTANCE;
    }

    /**
     * ble option ability helper
     */
    private static class BleOptionAbilityHelper {
        private static final BleOptionAbility INSTANCE = new BleOptionAbility();
    }
}
