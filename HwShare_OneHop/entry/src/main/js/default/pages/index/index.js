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
import state from '../../../share/common/util/state.js';
import Log from '../../../share/common/util/log.js';
import bleOperator from '../../../share/common/ble/bleOperator.js';
import config from '../../../share/common/util/config.js';
import app from '@system.app';
import commonOperator from '../../../share/common/util/commonOperator.js';

export default {
  data: {
    title: '',
    state: state.pair.PRIVACY,
    connectTimeout: 0,
    isSHow: false
  },
  onInit() {
    this.initListener();
  },
  initListener() {
    bleOperator.setStateListener(this.bleConnectState);
    bleOperator.onBluetoothAdapterStateChange(this.onBluetoothAdapterStateChange);
  },
  async bleConnectState(callback) {
    Log.info('bleConnectState:' + JSON.stringify(callback));
    if (callback.data.state === 1) {
      this.changeState(state.pair.PAIR_FAILED, state.pair.PAIR);
    } else if (callback.data.state === 2) {
      if(this.isShare){
        commonOperator.startAbility(config.bundleName, config.ControlAbility, callbackJson => {
          Log.info('app terminate');
          app.terminate();
        }, {
          macAddress: this.macAddress,
          productId: this.productId
        }, {
          flags: 276826112
        });
      }else {
        const data = await commonOperator.checkRegister(this.checkRegisterProxy);
        this.checkRegisterProxy(data);
      }
    }
  },
  async checkRegisterProxy(callbackJson) {
    Log.info('checkRegisterProxy:' + JSON.stringify(callbackJson));
    if (callbackJson.data.flag === 1) { // 已经注册,启动到控制
      commonOperator.startAbility(config.bundleName, config.ControlAbility, callbackJson => {
        Log.info('app terminate');
        app.terminate();
      }, {
        macAddress: this.macAddress,
        productId: this.productId
      }, {
        flags: 276826112
      });
    } else if (callbackJson.data.flag === 0) { // 获取固件版本号
      await bleOperator.setDeviceInfoListener(this.deviceStateCallback);
      setTimeout(() => {
        bleOperator.getDeviceVersion();
      }, 1000);
    }
  },
  deviceStateCallback(callbackData) {
    Log.info('deviceStateCallback:' + JSON.stringify(callbackData));
    const deviceInfo = JSON.parse(callbackData.data.deviceInfo);
    Log.info('fwv = ' + deviceInfo.fwv);
    Log.info('hwv = ' + deviceInfo.hwv);
    commonOperator.goHiLinkRegister(deviceInfo);
    app.terminate();
  },
  onBluetoothAdapterStateChange(callbackJson) {
    Log.info('Adapter state changed:' + JSON.stringify(callbackJson));
    if (callbackJson.data.isAvailable === false) {
      if (this.state === state.pair.PAIR) {
        this.changeState(state.pair.PAIR_FAILED, state.pair.PAIR);
        clearTimeout(this.connectTimeout);
        bleOperator.stopBluetoothDevicesDiscovery();
      }
    } else {
      Log.info('Adapter state ' + this.state);
      if (this.state === state.pair.REPAIR_REQUESTED) {
        this.changeState(state.pair.PAIR, state.pair.REPAIR_REQUESTED);
        this.connectToDevice();
      }
    }
  },
  connectToDevice: function() {
    // connect to device directly
    bleOperator.createBleConnection(this.macAddress);
  },
  reConnect() {
    Log.info('Reconnect started, device mac address: ' + this.macAddress);
    if (config.scanRequired) {
      bleOperator.getBluetoothAdapterState(this.onCheckAdapterState);
    } else {
      this.changeState(state.pair.PAIR, state.pair.PAIR_FAILED);
      this.connectToDevice();
    }
  },
  onLoginSuccess(resultObj) {
    Log.info('Login success:' + JSON.stringify(resultObj));
    if (resultObj.detail.allScopeGranted) {
      Log.info('All scope granted.');
      this.changeState(state.pair.PAIR, state.pair.PRIVACY);
      bleOperator.getBluetoothAdapterState(this.onCheckAdapterState);
    } else {
      Log.info('Not all scope granted.');
      this.changeState(state.pair.PRIVACY);
    }
  },
  onLoginError(resultObj) {
    this.changeState(state.pair.PAIR, state.pair.PRIVACY);
  },
  onCheckAdapterState(callbackJson) {
    Log.info('Bluetooth adapter state is:' + JSON.stringify(callbackJson));
    if (callbackJson.data.isAvailable === true) {
      this.changeState(state.pair.PAIR, state.pair.PAIR_FAILED);
      this.onAdapeterAvailable();
    } else {
      this.changeState(state.pair.REPAIR_REQUESTED, state.pair.PAIR_FAILED);
    }
  },
  async onAdapeterAvailable() {
    Log.info('onAdapeterAvailable scanRequired=' + config.scanRequired);
      if (await this.isBleConnect()) {
        Log.info('start......');
        commonOperator.startAbility(config.bundleName, config.ControlAbility, callbackJson => {
          Log.info('app terminate');
          app.terminate();
        }, {
          macAddress: this.macAddress,
          productId: this.productId
        }, {
          flags: 286435456
        });
      } else {
        this.scanDevice();
      }
  },
  async isBleConnect() {
    const result = await bleOperator.isBleConnected();
    Log.info('code = ' + result.code);
    Log.info('isConnected = ' + result.data.isConnected);
    if (result.code === 0) {
      const isConnected = result.data.isConnected;
      return isConnected;
    } else {
      return false;
    }
  },
  scanDevice() {
    // scan device for connection
    const interval = 10;
    bleOperator.startBluetoothDevicesDiscovery(config.bleScanFilterServices, this.macAddress, interval);
  },
  onPrivacyAgreed() {
    this.changeState(state.pair.PAIR, state.pair.PRIVACY);
  },
  changeState(newState, ...oldStates) {
    Log.info('Trying changing state from: ' + oldStates + ' to: ' + newState);
    if (oldStates === undefined || oldStates.length === 0 || oldStates.indexOf(this.state) > -1) {
      Log.info('State changed from: ' + this.state + ' to: ' + newState);
      this.state = newState;
    }
  }
};
