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

import {titles, newsData} from '../../../default/common/data/data.js';
import router from '@system.router';
import mediaquery from '@system.mediaquery';
import deviceManager from '@ohos.distributedHardware.deviceManager';
import featureAbility from '@ohos.ability.featureAbility';
import wantConstant from '@ohos.ability.wantConstant';

export default {
  data: {
    titleList: titles,
    newsList: newsData,
    title: newsData[0].title,
    type: newsData[0].type,
    imgUrl: newsData[0].imgUrl,
    reads: newsData[0].reads,
    likes: newsData[0].likes,
    content: newsData[0].content,
    isMatchMedia: true,
    deviceList: [],
    deviceMag: {}
  },
  // 选择新闻类型
  changeNewsType: function(e) {
    const type = titles[e.index].name;
    this.newsList = [];
    if (type === 'All') {
      // 展示全部新闻
      this.newsList = newsData;
    } else {
      // 分类展示新闻
      const newsArray = [];
      for (const news of newsData) {
        if (news.type === type) {
          newsArray.push(news);
        }
      }
      this.newsList = newsArray;
    }
  },
  itemClick(news) {
    // 竖屏展示新闻列表，点击单个新闻跳转到详情页
    if (this.isMatchMedia) {
      router.push({
        uri: 'pages/detail/detail',
        params: {
          'title': news.title,
          'type': news.type,
          'imgUrl': news.imgUrl,
          'reads': news.reads,
          'likes': news.likes,
          'content': news.content
        }
      });
    } else {
      // 横屏分左侧新闻列表、右侧新闻详情，点击单个新闻右侧显示新闻详情
      this.title = news.title;
      this.type = news.type;
      this.imgUrl = news.imgUrl;
      this.reads = news.reads;
      this.likes = news.likes;
      this.content = news.content;
    }
  },
  onReady() {
    // 根据媒体查询条件(竖屏)，创建MediaQueryList对象.
    const mMediaQueryList = mediaquery.matchMedia('(orientation: portrait)');

    const that = this;
    function maxWidthMatch(e) {
      // 查询条件匹配成功返回true，否则返回值为false
      that.isMatchMedia = e.matches;
    }

    mMediaQueryList.addListener(maxWidthMatch);
  },

  toShare() {
    // 创建设备管理实例
    deviceManager.createDeviceManager('com.huawei.codelab', (err, data) => {
      if (err) {
        return;
      }
      this.deviceMag = data;
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
            'deviceId': this.deviceList[i].deviceId,
            'bundleName': 'com.huawei.codelab',
            'abilityName': 'com.huawei.codelab.MainAbility',
            'flags': wantConstant.Flags.FLAG_ABILITYSLICE_MULTI_DEVICE,
            'parameters': {
              'url': 'pages/detail/detail',
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
