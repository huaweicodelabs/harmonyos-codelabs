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

/**
 * Ble Operator Constants
 */
public class BleOperatorConstants {
    /**
     * normal state
     */
    public static final int ERROR_CODE_OK = 0;

    /**
     * General error code
     */
    public static final int ERROR_CODE_COMMON_ERR = 11_001;

    /**
     * Bluetooth connection failed error code
     */
    public static final int ERROR_CODE_CONNECT_ERR = 11_002;

    /**
     * Bluetooth disconnection failure error code
     */
    public static final int ERROR_CODE_DISCONNECT_ERR = 11_003;

    /**
     * Open the communication code of the Bluetooth adapter
     */
    public static final int OPEN_BLUETOOTH_ADAPTER = 1001;

    /**
     * Turn off the communication code of the Bluetooth adapter
     */
    public static final int CLOSE_BLUETOOTH_ADAPTER = 1002;

    /**
     * Get the communication code of the Bluetooth adapter status
     */
    public static final int GET_BLUETOOTH_ADAPTER_STATE = 1003;

    /**
     * The communication code to monitor the status change of the Bluetooth adapter
     */
    public static final int ON_BLUETOOTH_ADAPTER_STATE_CHANGE = 1004;

    /**
     * Start the communication code for Bluetooth device discovery
     */
    public static final int START_BLUETOOTH_DEVICES_DISCOVERY = 1005;

    /**
     * Monitor the communication code found by the Bluetooth device
     */
    public static final int ON_BLUETOOTH_DEVICE_FOUND = 1007;

    /**
     * The communication code to open the Bluetooth low energy connection
     */
    public static final int CREATE_BLE_CONNECTION = 1008;

    /**
     * Turn off the communication code of the Bluetooth low energy connection
     */
    public static final int CLOSE_BLE_CONNECTION = 1009;
    /**
     * Get the connection state by device id
     */
    public static final int GET_BLE_CONNECTION_STATE = 1025;
    /**
     * get device state
     */
    public static final int GET_DEVICE_STATE = 1026;
    /**
     * get device info
     */
    public static final int GET_DEVICE_INFO = 1027;
    /**
     * control device
     */
    public static final int CONTROL_DEVICE = 1028;
    /**
     * is ble connected
     */
    public static final int IS_BLE_CONNECTED = 1029;
    /**
     * check ble switch
     */
    public static final int CHECK_BLE_SWITCH = 1030;
    /**
     * get device version
     */
    public static final int GET_DEVICE_VERSION = 1031;

    private BleOperatorConstants() { }
}
