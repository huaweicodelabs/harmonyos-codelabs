/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import router from '@system.router';
import deviceManager from '@ohos.distributedHardware.deviceManager';
import featureAbility from '@ohos.ability.featureAbility';
import wantConstant from '@ohos.ability.wantConstant';

export default {
  data: {
    title: '',
    type: '',
    imgUrl: '',
    reads: '',
    likes: '',
    content: '',
    deviceList: [],
    deviceMag: {}
  },

  onInit() {
    if (router.getParams()) {
      
    } else {
      // 从远端获取数据
      featureAbility.getWant()
        .then((want) => {
          this.title = want.parameters.title;
          this.imgUrl = want.parameters.imgUrl;
          this.reads = want.parameters.reads;
          this.likes = want.parameters.likes;
          this.content = want.parameters.content;
        }).catch((error) => {
        });
    }
  },

  toShare() {
    // 创建设备管理实例
    deviceManager.createDeviceManager('com.huawei.codelab', (err, data) => {
      if (err) {
        return;
      }
      this.deviceMag = data;
      // 获取所有可信设备的列表
      this.deviceList = this.deviceMag.getTrustedDeviceListSync();
    });
    // 循环遍历设备列表,获取设备名称和设备Id
    for (let i = 0; i < this.deviceList.length; i++) {
      this.deviceList[i] = {
        deviceName: this.deviceList[i].deviceName,
        deviceId: this.deviceList[i].deviceId,
        checked: false
      };
    }
    this.$element('showDialog').show();
  },

  chooseCancel() {
    this.$element('showDialog').close();
  },

  chooseComform() {
    this.$element('showDialog').close();
    for (let i = 0; i < this.deviceList.length; i++) {
      // 判断设备是否被选中
      if (this.deviceList[i].checked) {
        const str = {
          'want': {
            // 远程设备的deviceId
            'deviceId': this.deviceList[i].deviceId,
            'bundleName': 'com.huawei.codelab',
            'abilityName': 'com.huawei.codelab.MainAbility',
            // 分布式任务flag
            'flags': wantConstant.Flags.FLAG_ABILITYSLICE_MULTI_DEVICE,
            'parameters': {
              // 指定跳转的页面
              'url': 'pages/detail/detail',
              // 跳转携带的参数
              title: this.title,
              type: this.type,
              imgUrl: this.imgUrl,
              reads: this.reads,
              likes: this.likes,
              content: this.content
            }
          }
        };
        featureAbility.startAbility(str)
          .then((data) => {

          }).catch((error) => {

          });
      }
    }
  },

  selectDevice(index, e) {
    this.deviceList[index].checked = e.checked;
  },

  /**
   * 释放DeviceManager实例
   */
  onDestroy() {
    this.deviceMag.release();
  }
};
