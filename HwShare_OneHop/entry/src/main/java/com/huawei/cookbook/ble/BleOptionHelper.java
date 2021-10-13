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
import com.huawei.cookbook.ble.model.BluetoothDiscoveryParam;
import com.huawei.cookbook.util.BasicConstants;
import com.huawei.cookbook.util.LogUtils;

import ohos.app.Context;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.zson.ZSONObject;
import org.apache.hc.core5.util.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * ble option helper
 *
 * @ClassName: BleOptionHelper
 * @Description: java class description
 */
public class BleOptionHelper {
    private static final String IS_DISCOVERING = "isDiscovering";

    private static final String IS_AVAILABLE = "isAvailable";

    private static final String CODE = "code";

    private static final String DATA = "data";

    private static final String DEVICE_ID = "deviceId";

    /**
     * ble option helper
     *
     * @param context context
     */
    public BleOptionHelper(Context context) {
        BleHelper.getInstance().init(context);
    }

    /**
     * set device update listener
     *
     * @param bleDeviceInfoCallbackSet bleDeviceInfoCallbackSet
     */
    public void setDeviceUpdateLinstener(Set<IRemoteObject> bleDeviceInfoCallbackSet) {
        DeviceInfo infoCallback =
                deviceInfo -> {
                    LogUtils.info("setConnectStateListener deviceInfo:" + deviceInfo);
                    MessageParcel data = MessageParcel.obtain();
                    MessageOption option = new MessageOption();
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("deviceInfo", deviceInfo);
                    data.writeString(ZSONObject.toZSONString(resultMap));
                    MessageParcel reply = MessageParcel.obtain();
                    for (IRemoteObject remoteObject : bleDeviceInfoCallbackSet) {
                        sendRequest(remoteObject, BasicConstants.SUCCESS, data, reply, option);
                    }
                    reply.reclaim();
                    data.reclaim();
                };
        BleHelper.getInstance().setDeviceInfoCallback(infoCallback);
    }

    /**
     * set connect state listener
     *
     * @param bleStateCallbackSet bleStateCallbackSet
     */
    public void setConnectStateListener(Set<IRemoteObject> bleStateCallbackSet) {
        ConnectStateCallback stateCallback =
                (state, desc) -> {
                    LogUtils.info("setConnectStateListener state:" + state);
                    LogUtils.info("setConnectStateListener desc:" + desc);
                    MessageParcel data = MessageParcel.obtain();
                    MessageOption option = new MessageOption();
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("state", state);
                    resultMap.put("desc", desc);
                    data.writeString(ZSONObject.toZSONString(resultMap));
                    MessageParcel reply = MessageParcel.obtain();
                    for (IRemoteObject remoteObject : bleStateCallbackSet) {
                        sendRequest(remoteObject, BasicConstants.SUCCESS, data, reply, option);
                    }
                    reply.reclaim();
                    data.reclaim();
                };
        BleHelper.getInstance().getStateCallback(stateCallback);
    }

    /**
     * get device info
     */
    public void getDeviceInfo() {
        BleHelper.getInstance().getDeviceInfo();
    }

    /**
     * on bluetooth adapter state change
     *
     * @param bleAdapterStateChangeCallbackSet bleAdapterStateChangeCallbackSet
     */
    public void onBluetoothAdapterStateChange(Set<IRemoteObject> bleAdapterStateChangeCallbackSet) {
        BluetoothAdapterStateChangeCallback callback =
            (isDiscovering, isAvailable) -> {
                LogUtils.info("onBluetoothAdapterStateChange isDiscovering:" + isDiscovering);
                LogUtils.info("onBluetoothAdapterStateChange isAvailable:" + isAvailable);
                MessageParcel data = MessageParcel.obtain();
                MessageParcel reply = MessageParcel.obtain();
                MessageOption option = new MessageOption();
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put(IS_DISCOVERING, isDiscovering);
                resultMap.put(IS_AVAILABLE, isAvailable);
                data.writeString(ZSONObject.toZSONString(resultMap));
                for (IRemoteObject remoteObject : bleAdapterStateChangeCallbackSet) {
                    sendRequest(remoteObject, 100, data, reply, option);
                }
                reply.reclaim();
                data.reclaim();
            };
        BleHelper.getInstance().onBluetoothAdapterStateChange(callback);
    }

    /**
     * close ble connection
     *
     * @param data data
     * @param reply reply
     * @param option option
     * @return boolean
     */
    public boolean closeBleConnection(MessageParcel data, MessageParcel reply, MessageOption option) {
        String dataString = data.readString();
        LogUtils.info("dataString:" + dataString);
        ZSONObject dataObject = ZSONObject.stringToZSON(dataString);
        String deviceId = String.valueOf(dataObject.get(DEVICE_ID));
        if (TextUtils.isEmpty(deviceId)) {
            LogUtils.info("deviceId can not be null");
            return true;
        }
        String errorCode = BleHelper.getInstance().closeBleConnection(deviceId);
        return replyResult(reply, option, parseErrorCode(errorCode), new HashMap<>());
    }

    /**
     * control device
     *
     * @param data data
     * @param reply reply
     * @param option option
     * @return boolean
     */
    public boolean controlDevice(MessageParcel data, MessageParcel reply, MessageOption option) {
        String dataString = data.readString();
        LogUtils.info("dataString:" + dataString);
        BleHelper.getInstance().controlDevice(dataString);
        return replyResult(reply, option, BasicConstants.SUCCESS, new HashMap<>());
    }

    /**
     * is ble connected
     *
     * @param reply reply
     * @param option option
     * @return boolean
     */
    public boolean isBleConnected(MessageParcel reply, MessageOption option) {
        boolean isConnected = BleHelper.getInstance().isBleConnected();
        HashMap<String, Object> map = new HashMap<>();
        map.put("isConnected", isConnected);
        return replyResult(reply, option, BasicConstants.SUCCESS, map);
    }

    /**
     * get device version
     *
     * @param reply reply
     * @param option option
     * @return boolean
     */
    public boolean getDeviceVersion(MessageParcel reply, MessageOption option) {
        BleHelper.getInstance().getVersion();
        return replyResult(reply, option, BasicConstants.SUCCESS, new HashMap<>());
    }

    /**
     * check ble switch
     *
     * @param reply reply
     * @param option option
     * @return boolean
     */
    public boolean checkBleSwitch( MessageParcel reply, MessageOption option) {
        boolean isOpen = BleHelper.getInstance().isBlueOpen();
        HashMap<String, Object> map = new HashMap<>();
        map.put("isOpen", isOpen);
        return replyResult(reply, option, BasicConstants.SUCCESS, map);
    }

    /**
     * create ble connected
     *
     * @param data data
     * @param reply reply
     * @param option option
     * @return boolean
     */
    public boolean createBleConnected(MessageParcel data, MessageParcel reply, MessageOption option) {
        String dataString = data.readString();
        LogUtils.info("dataString deviceid:" + dataString);
        BleHelper.getInstance().createBleConnected(dataString);
        return replyResult(reply, option, BasicConstants.SUCCESS, new HashMap<>());
    }

    /**
     * start bluetooth devices discovery
     *
     * @param data data
     * @param reply reply
     * @param option option
     * @return boolean
     */
    public boolean startBluetoothDevicesDiscovery(MessageParcel data, MessageParcel reply, MessageOption option) {
        String dataString = data.readString();
        LogUtils.info("dataString:" + dataString);
        BluetoothDiscoveryParam param = ZSONObject.stringToClass(dataString, BluetoothDiscoveryParam.class);
        String errorCode =
                BleHelper.getInstance()
                        .startBluetoothDevicesDiscovery(
                                getUuidArrayByStringArray(param.getServices()),
                                param.getMacAddress(),
                                param.getInterval());
        LogUtils.info("errorCode = " + errorCode);
        int code = 0;
        try {
            code = Integer.parseInt("0");
        } catch (NumberFormatException e) {
            LogUtils.erro("NumberFormatException");
        }
        return replyResult(reply, option, code, new HashMap<>());
    }

    /**
     * get bluetooth adapter state
     *
     * @param bleAdapterStateCallbackSet bleAdapterStateCallbackSet
     */
    public void getBluetoothAdapterState(Set<IRemoteObject> bleAdapterStateCallbackSet) {
        BluetoothAdapterStateCallback callback =
                (isDiscovering, isAvailable, errorCode) -> {
                    LogUtils.info("getBluetoothAdapterState isDiscovering:" + isDiscovering);
                    LogUtils.info("getBluetoothAdapterState isAvailable:" + isAvailable);
                    LogUtils.info("getBluetoothAdapterState errorCode:" + errorCode);
                    MessageParcel data = MessageParcel.obtain();
                    MessageOption option = new MessageOption();
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put(IS_DISCOVERING, isDiscovering);
                    resultMap.put(IS_AVAILABLE, isAvailable);
                    data.writeString(ZSONObject.toZSONString(resultMap));
                    MessageParcel reply = MessageParcel.obtain();
                    for (IRemoteObject remoteObject : bleAdapterStateCallbackSet) {
                        sendRequest(remoteObject, errorCode, data, reply, option);
                    }
                    reply.reclaim();
                    data.reclaim();
                };
        BleHelper.getInstance().getBluetoothAdapterState(callback);
    }

    private void sendRequest(
            IRemoteObject remoteObject, int errorCode, MessageParcel data, MessageParcel reply, MessageOption option) {
        if (remoteObject == null) {
            return;
        }
        try {
            remoteObject.sendRequest(errorCode, data, reply, option);
        } catch (RemoteException e) {
            LogUtils.erro("RemoteException:" + e);
        }
    }

    /**
     * reply result
     *
     * @param reply reply
     * @param option option
     * @param code code
     * @param resultMap resultMap
     * @return boolean
     */
    public boolean replyResult(MessageParcel reply, MessageOption option, int code, Map<String, Object> resultMap) {
        if (option.getFlags() == MessageOption.TF_SYNC) {
            // SYNC
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put(CODE, code);
            dataMap.put(DATA, resultMap);
            reply.writeString(ZSONObject.toZSONString(dataMap));
        } else {
            // ASYNC
            MessageParcel responseData = MessageParcel.obtain();
            responseData.writeString(ZSONObject.toZSONString(resultMap));
            IRemoteObject remoteReply = reply.readRemoteObject();
            try {
                remoteReply.sendRequest(code, responseData, MessageParcel.obtain(), new MessageOption());
                responseData.reclaim();
            } catch (RemoteException exception) {
                LogUtils.erro("RemoteException = " + exception);
                return false;
            }
        }
        return true;
    }

    private UUID[] getUuidArrayByStringArray(String[] strings) {
        if (strings == null || strings.length == 0) {
            return new UUID[0];
        }
        UUID[] uuids = new UUID[strings.length];
        for (int i = 0; i < strings.length; i++) {
            uuids[i] = UUID.fromString(strings[i]);
        }
        return uuids;
    }

    private int parseErrorCode(String errorCode) {
        int code = BleOperatorConstants.ERROR_CODE_COMMON_ERR;
        try {
            code = Integer.parseInt(errorCode);
        } catch (NumberFormatException e) {
            LogUtils.erro("NumberFormatException errorCode:" + errorCode);
        }
        return code;
    }
}
