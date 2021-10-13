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
import Log from '../util/log.js';
import commonData from './commonData.js';
import config from '../util/config.js';
const ABILITY_TYPE_INTERNAL = 1;

// syncOption(Optional, pair sync): 0-Sync; 1-Async
const ACTION_SYNC = 0;

// Java side register info
const BUNDLE_NAME = config.bundleName;
const ABILITY_NAME = config.CommonOperatorAbility;

// Operation Code
const START_ABILITY = 2020;
const OPEN_URL = 2040;
const GET_UNIQUE_ID = 2050;
const TO_VMALL = 2060;
const CHECK_REGISTER = 2070;
const HILINK_REGISTER = 2080;
const HW_SHARE = 2090;
const TAG = 'JS/Operator/Common: ';


export default {
  data: {},
  startAbility: async function(bundleName, abilityName, callback, params, options) {
    const actionData = {};
    actionData.bundleName = bundleName;
    actionData.abilityName = abilityName;
    actionData.params = params;
    actionData.options = options;
    const action =  commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,START_ABILITY);
    action.data = actionData;
    action.flag = 286435456;
    const resultStr = await FeatureAbility.subscribeAbilityEvent(action, callbackData => {
      const callbackJson = JSON.parse(callbackData);
      callback(callbackJson);
    });
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  getUniqueId: function(callback) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,GET_UNIQUE_ID);
    FeatureAbility.subscribeAbilityEvent(action, callbackData => {
      try {
        const callbackJson = JSON.parse(callbackData);
        callback(callbackJson);
      } catch (e) {
        Log.error(TAG + 'callbackJson get unique e =' + e);
      }
    });
  },
  
  openUrl: async function(url, callback) {
    const actionData = {};
    actionData.url = url;

    if (actionData.url === null || actionData.url === '') {
      return;
    }
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,OPEN_URL);
    action.data = actionData;
    await FeatureAbility.subscribeAbilityEvent(action, callbackData => {
      const callbackJson = JSON.parse(callbackData);
      callback(callbackJson);
    });
  },

  toVMall: async function(callback) {
    const actionData = {};
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,TO_VMALL);
    action.data = actionData;
    await FeatureAbility.subscribeAbilityEvent(action, callbackData => {
      const callbackJson = JSON.parse(callbackData);
      callback(callbackJson);
    });
  },
  
  checkRegister: async function(callback) {
    const actionData = {};
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,CHECK_REGISTER);
    action.data = actionData;
    const resultStr = await FeatureAbility.callAbility(action);
    const resultObj = JSON.parse(resultStr);
    return resultObj;
  },

  goHiLinkRegister: async function(deviceInfo) {
    if (deviceInfo === null || deviceInfo === '') {
      Log.info(TAG + 'actionData.url is empty or null:' + JSON.stringify(deviceInfo));
      return;
    }
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,HILINK_REGISTER);
    action.data = deviceInfo;
    await FeatureAbility.callAbility(action);
  },

  hwShare: async function(prodId,macAddress) {
    const action = commonData.getRequestAction(BUNDLE_NAME,ABILITY_NAME,
      ABILITY_TYPE_INTERNAL,ACTION_SYNC,HW_SHARE);
    const actionData = {};
    actionData.prodId = prodId;
    actionData.macAddress = macAddress;
    action.data = actionData;
    await FeatureAbility.callAbility(action);
  }
};
