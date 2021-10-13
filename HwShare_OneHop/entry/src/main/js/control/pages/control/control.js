import Log from '../../../share/common/util/log.js';
import app from '@system.app';
import bleOperator from '../../../share/common/ble/bleOperator.js';
import config from '../../../share/common/util/config.js';
import {showToast} from '../../../share/common/util/util.js';
import commonOperator from '../../../share/common/util/commonOperator.js';

export default {
  data: {
    title: '',
    isFullScreen: false,
    deviceInfo:
        {switchDesc: '未开启', battValue: '', isCharging: false, switchImg: '/common/img/base/ic_power_off.webp'},
    features: [
      {name: '模式', value: '', icon: '/common/img/base/ic_auto.webp', flag: false},
      {name: '浓度', value: '', icon: '/common/img/base/ic_speed_off.webp', flag: false},
      {name: '循环次数', value: '', icon: '/common/img/base/ic_speed_off.webp', flag: true},
      {name: '持续时长', value: '', icon: '/common/img/base/ic_auto.webp', flag: false}],
    modeNum: {'01': '5分钟', '02': '10分钟', '03': '20分钟', '04': '30分钟', '05': '40分钟',
      '06': '50分钟', '07': '1小时', '08': '1.5小时', '09': '2小时'},
    macAddress: undefined,
    productId: undefined,
    currentDeviceInfo: {'switch': '', 'gear': '', 'battValue': '', 'mode': '', 'modeNum': '', 'chargeStatus': ''},
    mode: {'01': '自控模式', '02': '循环模式', '03': '持续模式'},
    uiMode: ['自控模式', '循环模式', '持续模式'],
    uiGear: ['弱', '强'],
    uiTime: ['5分钟', '10分钟', '20分钟', '30分钟', '40分钟', '50分钟', '1小时', '1.5小时', '2小时'],
    modeNumTimes: {'01': '循环1次', '02': '循环2次', '03': '循环3次', '04': '循环4次', '05': '循环5次'},
    uiNumTimes: ['循环1次', '循环2次', '循环3次', '循环4次', '循环5次'],
    isPowerOn: false,
    isOnInit: false,
    dialogInfo: [
      {dialogTitile: '确认删除', dialogContentText: '确认删除油烟机XXXX', value: '', cacelValue: '取消', endValue: '确认'}
    ],
    dialogContent: {
      'disconnect': {dialogTitile: '蓝牙', dialogContentText: '蓝牙已断开，是否重新连接', value: '', cacelValue: '否', endValue: '是'},
      'reconnect': {dialogTitile: '蓝牙', dialogContentText: '未发现设备，是否重新扫描', value: '', cacelValue: '否', endValue: '是'},
      'close': {dialogTitile: '蓝牙', dialogContentText: '蓝牙未打开，请开启蓝牙', value: '', cacelValue: '知道了', endValue: ''}
    },
    currentDialogStatus: 'common',
    isDialogShow: false,
    isConnecting: false,
    isPageShow: false
  },
  onInit() {
    this.isOnInit = true;
    this.isPageShow = true;
    if (this.params === undefined) {
      Log.error('Params is undefined!');
    } else {
      const paramJson = JSON.parse(this.params);
      this.macAddress = paramJson.macAddress;
      this.productId = paramJson.productId;
    }
    this.initListener();
  },
  initListener() {
    bleOperator.setDeviceInfoListener(this.deviceStateCallback);
    bleOperator.onBluetoothAdapterStateChange(this.onBluetoothAdapterStateChange);
    bleOperator.setStateListener(this.bleConnectState);
  },
  async onShow() {
    this.isPageShow = true;
    if (!this.isOnInit) {
      if (!await this.isBleOpen()) {
        this.dialogShow(this.dialogContent['close'], 'close');
      } else {
        if (!await this.isBleConnect()) {
          bleOperator.setStateListener(this.bleConnectState);
          this.dialogShow(this.dialogContent['disconnect'], 'disconnect');
        }
      }
    } else {
      bleOperator.getDeviceInfo();
    }
    this.isOnInit = false;
  },
  onHide() {
    this.isPageShow = false;
  },
  bleConnectState(callback) {
    this.isConnecting = false;
    if (callback.data.state === 1) {
      this.dialogShow(this.dialogContent['reconnect'], 'reconnect');
    } else if (callback.data.state === 2) {
      bleOperator.getDeviceInfo();
    }
  },
  onBluetoothAdapterStateChange(callbackJson) {
    if (!this.isPageShow) {
      return;
    }
    if (callbackJson.data.isAvailable === false) {
      this.setUiUpdate(this.currentDeviceInfo);
      this.dialogShow(this.dialogContent['close'], 'close');
    } else {
      if (this.isDialogShow) {
        this.dialogShow();
      }
      this.scanDevice();
    }
  },
  connectToDevice: function() {
    bleOperator.createBleConnection(this.macAddress);
  },
  async isBleConnect() {
    const result = await bleOperator.isBleConnected();
    if (result.code === 0) {
      const isConnected = result.data.isConnected;
      return isConnected;
    } else {
      return false;
    }
  },
  deviceStateCallback(callbackData) {
    const devceInfo = JSON.parse(callbackData.data.deviceInfo);
    this.setCurrentData(devceInfo);
    this.setUiUpdate(this.currentDeviceInfo);
  },
  setCurrentData(devceInfo) {
    if (devceInfo.switch !== undefined) {
      this.currentDeviceInfo.switch = devceInfo.switch;
    }
    if (devceInfo.gear !== undefined) {
      this.currentDeviceInfo.gear = devceInfo.gear;
    }
    if (devceInfo.battValue !== undefined) {
      let battValue = devceInfo.battValue;
      if (battValue === '0100') {
        battValue = '100';
      }
      this.currentDeviceInfo.battValue = battValue;
    }
    if (devceInfo.mode !== undefined) {
      this.currentDeviceInfo.mode = devceInfo.mode;
    }
    if (devceInfo.modeNum !== undefined) {
      this.currentDeviceInfo.modeNum = devceInfo.modeNum;
    }
    if (devceInfo.chargeStatus !== undefined) {
      this.currentDeviceInfo.chargeStatus = devceInfo.chargeStatus;
    }
  },
  async setUiUpdate(deviceSate) {
    if (!await this.isBleOpen() || !await this.isBleConnect() || deviceSate.switch === '00') {
      this.isPowerOn = false;
      this.resetPower();
      this.resetUI();
      return;
    }
    this.setPowerState(deviceSate.chargeStatus, deviceSate.switch, deviceSate.battValue);
    this.setMode(deviceSate.mode, deviceSate.modeNum, deviceSate.gear);
  },
  setPowerState(chargStatus, swFlag, battValue) {
    if (swFlag === undefined) {
      return;
    } else {
      this.isPowerOn = true;
    }
    const switchImg = '/common/img/base/ic_power_on.webp';
    const switchDesc = '已连接';
    const isCharging = this.setCharing(chargStatus, battValue);
    const switchInfo = {switchDesc: switchDesc, battValue: battValue + '%',
      isCharging: isCharging, switchImg: switchImg};
    this.setSwitchState(switchInfo);
  },
  setCharing(chargStatus, battValue) {
    let isCharging = false;
    if (chargStatus === '00') {
      isCharging = false;
    } else if (chargStatus === '01') {
      if (battValue !== undefined) {
        isCharging = true;
      } else {
        isCharging = false;
      }
    }
    return isCharging;
  },
  setSwitchState(switchInfo) {
    this.deviceInfo = switchInfo;
  },
  setMode(mode, modeNum, gear) {
    if (mode === undefined) {
      mode = this.currentDeviceInfo.mode;
    } else {
      this.currentDeviceInfo.mode = mode;
    }
    if (modeNum === undefined) {
      modeNum = this.currentDeviceInfo.modeNum;
    } else {
      this.currentDeviceInfo.modeNum = modeNum;
    }
    const featureData01 = this.features[0];
    featureData01.value = this.mode[mode];
    const featureData03 = this.features[2];
    const featureData04 = this.features[3];
    featureData04.value = this.modeNum[modeNum];
    if (mode === '01') {
      // 自控模式
      featureData01.value = this.mode[mode];
      featureData03.value = '';
      featureData04.value = '';
    } else if (mode === '02') {
      // 循环模式
      featureData01.value = this.mode[mode];
      featureData03.value = this.modeNumTimes[modeNum];
      featureData03.flag = false;
      featureData04.flag = false;
      featureData04.value = '';
    } else if (mode === '03') {
      // 持续模式
      featureData01.value = this.mode[mode];
      featureData03.value = '';
      featureData04.value = this.modeNum[modeNum];
    }
    const featureData02 = this.setGrea(gear);
    this.features = [featureData01, featureData02, featureData03, featureData04];
  },
  setGrea(gear) {
    if (gear === undefined) {
      gear = this.currentDeviceInfo.gear;
    } else {
      this.currentDeviceInfo.gear = gear;
    }
    const featureGear = {name: '浓度', value: '', icon: '/common/img/base/ic_speed_off.webp', flag: false};
    if (gear === '01') {
      featureGear.value = '弱';
    } else if (gear === '02') {
      featureGear.value = '强';
    }
    this.features[1] = featureGear;
    return featureGear;
  },
  getDeviceState() {
    bleOperator.getDeviceInfo();
  },
  getMoreClick() {
    if (!this.isFullScreen) {
      this.isFullScreen = true;
      app.requestFullWindow();
    }
  },
  async powerOnClick() {
    if (!await this.isBleOpen()) {
      showToast('蓝牙未打开，请开启蓝牙');
      return;
    }
    if (!await this.isBleConnect()) {
      this.dialogShow(this.dialogContent['disconnect'], 'disconnect');
      return;
    }
    const data = {};
    if (!this.isPowerOn) {
      data.switch = '01';
    } else {
      data.switch = '00';
    }
    bleOperator.controlDevice(data);
  },
  async toDesc() {
    commonOperator.openUrl('https://m.vmall.com/product/10086555668295.html');
  },
  resetUI() {
    const featureData = this.features;
    this.features = featureData;
  },
  resetPower() {
    const isCharg = this.setCharing(this.currentDeviceInfo.chargeStatus, this.currentDeviceInfo.battValue);
    const power = {switchDesc: '未开启', battValue: this.currentDeviceInfo.battValue + '%', isCharging: isCharg,
      switchImg: '/common/img/base/ic_power_off.webp'};
    this.deviceInfo = power;
  },
  backClicked() {
    bleOperator.closeBleConnection(this.macAddress);
    app.terminate();
  },
  controClick(id) {
    if (!this.isPowerOn) {
      return;
    }
    switch (id) {
      case 0:
        this.$element('menu_opts').show({x: 8, y: 260});
        break;
      case 1:
        this.$element('menu_gear').show({x: 180, y: 310});
        break;
      case 2:
        if (this.currentDeviceInfo.mode !== '02') {
          return;
        }
        this.$element('menu_loop').show({x: 8, y: 240});
        break;
      case 3:
        this.$element('menu_time').show({x: 170, y: 40});
        break;
    }
  },
  modeMenuSelected(e) {
    const key = this.getKeyById(e.value);
    const data = {};
    if (key === '03') {
      data.mode = key;
      data.modeNum = this.currentDeviceInfo.modeNum;
    } else {
      let modeNum = this.currentDeviceInfo.modeNum;
      const modeNumId = this.getIdBykey(modeNum);
      if (modeNumId > 4) {
        modeNum = '05';
      }
      if (key === '02' && modeNum === '00') {
        modeNum = '03';
      }
      data.mode = key;
      data.modeNum = modeNum;
    }
    bleOperator.controlDevice(data);
  },
  gearMenuSelected(e) {
    const data = {};
    data.gear = this.getKeyById(e.value);;
    bleOperator.controlDevice(data);
  },
  timeMenuSelected(e) {
    const data = {};
    data.mode = '03';
    data.modeNum = this.getKeyById(e.value);
    bleOperator.controlDevice(data);
  },
  loopMenuSelected(e) {
    const data = {};
    data.mode = '02';
    data.modeNum = this.getKeyById(e.value);
    bleOperator.controlDevice(data);
  },
  getKeyById(id) {
    return '0' + parseInt(id) + 1;;
  },
  getIdBykey(key) {
    const currentKey = '' + key;
    const id = currentKey.substring(1, currentKey.length);
    return parseInt(id) - 1;
  },
  dialogShow: function(data, statue) {
    const self = this;
    if (!self.isDialogShow) {
      self.isDialogShow = !self.isDialogShow;
      self.dialogInfo = data;
      self.currentDialogStatus = statue;
      self.$element('deviceCommonDialog').show();
    } else {
      self.isDialogShow = !self.isDialogShow;
      self.$element('deviceCommonDialog').close();
    }
  },
  cancel: function() {
    this.isDialogShow = false;
  },
  endClick() {
    this.dialogShow();
    if (this.currentDialogStatus === 'disconnect') {
      this.scanDevice();
    } else if (this.currentDialogStatus === 'reconnect') {
      this.scanDevice();
    } else if (this.currentDialogStatus === 'close') {
      app.terminate();
    }
  },
  cancelClick: function() {
    this.dialogShow();
  },
  scanDevice() {
    this.isConnecting = true;
    bleOperator.startBluetoothDevicesDiscovery(config.bleScanFilterServices, this.macAddress, 10);
  },
  async isBleOpen() {
    const result =await bleOperator.checkBleState();
    if (result.code === 0) {
      const isOpen = result.data.isOpen;
      return isOpen;
    } else {
      return false;
    }
  },
  onDestroy() {
    bleOperator.closeBleConnection(this.macAddress);
  }
};
