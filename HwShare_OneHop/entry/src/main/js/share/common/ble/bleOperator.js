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
import commonData from '../util/commonData.js';
import config from '../util/config.js';
const ABILITY_TYPE_INTERNAL = 1;

// syncOption: 0-Sync; 1-Async
const ACTION_SYNC = 0;

// Java端入口
const BUNDLE_NAME = config.bundleName;
const ABILITY_NAME = config.BleOptionAbility;
const GET_BLUETOOTH_ADAPTER_STATE = 1003;
const ON_BLUETOOTH_ADAPTER_STATE_CHANGE = 1004;
const START_BLUETOOTH_DEVICES_DISCOVERY = 1005;
const STOP_BLUETOOTH_DEVICES_DISCOVERY = 1006;
const ON_BLUETOOTH_DEVICE_FOUND = 1007;
const CREATE_BLE_CONNECTION = 1008;
const CLOSE_BLE_CONNECTION = 1009;
const ON_BLE_CONNECTION_STATE_CHANGE = 1010;
const ON_BLE_SERVICES_DISCOVERED = 1012;
const ON_BLE_CHARACTERISTIC_VALUE_CHANGE = 1015;
const NOTIFY_BLE_CHARACTERISTIC_VALUE_CHANGE = 1016;
const SET_ENABLE_INDICATION = 1017;
const GET_BLE_CONNECTION_STATE = 1025;
const GET_DEVICE_STATE = 1026;
const GET_DEVICE_INFO = 1027;
const CONTROL_DEVICE = 1028;
const IS_BLE_CONNECTED = 1029;
const CHECK_BLE_SWITCH = 1030;
const GET_DEVICE_VERSION = 1031;

export default {
  getBluetoothAdapterState: async function(callback) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,GET_BLUETOOTH_ADAPTER_STATE);
    const resultStr = await FeatureAbility.subscribeAbilityEvent(action, callbackData => {
      const callbackJson = JSON.parse(callbackData);
      callback(callbackJson);
    });
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },
  
  onBluetoothAdapterStateChange: async function(callback) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,ON_BLUETOOTH_ADAPTER_STATE_CHANGE);
    const resultStr = await FeatureAbility.subscribeAbilityEvent(action, callbackData => {
      const callbackJson = JSON.parse(callbackData);
      callback(callbackJson);
    });
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },
  
  startBluetoothDevicesDiscovery: async function(services, macAddress, interval) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,START_BLUETOOTH_DEVICES_DISCOVERY);
    const actionData = {};
    actionData.services = services;
    actionData.macAddress = macAddress;
    actionData.interval = interval;
    action.data = actionData;
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },
  
  stopBluetoothDevicesDiscovery: async function() {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,STOP_BLUETOOTH_DEVICES_DISCOVERY);
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  onBluetoothDeviceFound: async function(callback) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,ON_BLUETOOTH_DEVICE_FOUND);
    const resultStr = await FeatureAbility.subscribeAbilityEvent(action, callbackData => {
      const callbackJson = JSON.parse(callbackData);
      callback(callbackJson);
    });
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  createBleConnection: async function(deviceId) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,CREATE_BLE_CONNECTION);
    const actionData = {};
    actionData.deviceId = deviceId.toUpperCase();
    action.data = actionData;
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);

    return resultObj;
  },

  closeBleConnection: async function(deviceId) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,CLOSE_BLE_CONNECTION);
    const actionData = {};
    actionData.deviceId = deviceId.toUpperCase();
    action.data = actionData;
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  onBleConnectionStateChange: async function(callback) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,ON_BLE_CONNECTION_STATE_CHANGE);
    const resultStr = await FeatureAbility.subscribeAbilityEvent(action, callbackData => {
      const callbackJson = JSON.parse(callbackData);
      callback(callbackJson);
    });
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  onBleCharacteristicValueChange: async function(callback) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,ON_BLE_CHARACTERISTIC_VALUE_CHANGE);
    const resultStr = await FeatureAbility.subscribeAbilityEvent(action, callbackData => {
      const callbackJson = JSON.parse(callbackData);
      callback(callbackJson);
    });
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  onBleServicesDiscovered: async function(callback) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,ON_BLE_SERVICES_DISCOVERED);
    const resultStr = await FeatureAbility.subscribeAbilityEvent(action, callbackData => {
      const callbackJson = JSON.parse(callbackData);
      callback(callbackJson);
    });
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  setEnableIndication: async function(isEnableIndication) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,SET_ENABLE_INDICATION);
    const actionData = {};
    actionData.isEnableIndication = isEnableIndication;
    action.data = actionData;
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  notifyBleCharacteristicValueChange: async function(deviceId, serviceId, characteristicId, state) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,NOTIFY_BLE_CHARACTERISTIC_VALUE_CHANGE);
    const actionData = {};
    actionData.deviceId = deviceId.toUpperCase();
    actionData.serviceId = serviceId;
    actionData.characteristicId = characteristicId;
    actionData.state = state;
    action.data = actionData;
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },
  
  setStateListener: async function(callback) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,GET_BLE_CONNECTION_STATE);
    const resultStr = await FeatureAbility.subscribeAbilityEvent(action, callbackdata => {
      const callbackJson = JSON.parse(callbackdata);
      callback(callbackJson);
    });
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  setDeviceInfoListener: async function(callback) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,GET_DEVICE_STATE);
    const resultStr = await FeatureAbility.subscribeAbilityEvent(action, callbackdata => {
      const callbackJson = JSON.parse(callbackdata);
      callback(callbackJson);
    });
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  getDeviceInfo: async function() {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,GET_DEVICE_INFO);
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  controlDevice: async function(data) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,CONTROL_DEVICE);
    action.data = data;
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  isBleConnected: async function() {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,IS_BLE_CONNECTED);
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  checkBleState: async function() {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,CHECK_BLE_SWITCH);
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  getDeviceVersion: async function() {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,GET_DEVICE_VERSION);
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  }
};
