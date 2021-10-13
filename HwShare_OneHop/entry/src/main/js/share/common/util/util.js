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
import prompt from '@system.prompt';
import config from '../util/config.js';
export const BUNDLE_NAME = config.bundleName;
export function goToAbility(abilityName, dataInfo, flag) {
  const action = {};
  action.bundleName = BUNDLE_NAME;
  action.flag = flag;
  action.abilityName = abilityName;
  action.data = dataInfo;
  // @ts-ignore
  FeatureAbility.startAbility(action);
}

// 字节数组转十六进制字符串，对负值填坑
export function Bytes2HexString(arrBytes) {
  let str = '';
  for (let i = 0; i < arrBytes.length; i++) {
    let tmp;
    const num = arrBytes[i];
    if (num < 0) {
      // 此处填坑，当byte因为符合位导致数值为负时候，需要对数据进行处理
      tmp = (255 + num + 1).toString(16);
    } else {
      tmp = num.toString(16);
    }
    if (tmp.length === 1) {
      tmp = '0' + tmp;
    }
    str += tmp;
  }
  return str;
}

export function showToast(msg) {
  prompt.showToast({
    message: msg,
    duration: 3500
  });
}
