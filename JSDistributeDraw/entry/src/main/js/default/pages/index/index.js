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

import KvStoreModel from '../../../model/KvStoreModel.js';

const CHANGE_POSITION = 'change_position';
const LINE_WIDTH = 5;
const DRAW_DELAY = 10;
const DATA_PUT_DELAY = 100;
const CLEAR_WIDTH = 2000;
const CLEAR_HEIGHT = 2000;

export default {
  data: {
    ctx: null,
    startX: 0,
    startY: 0,
    moveX: 0,
    moveY: 0,
    intervalID: 0,
    initialData: [],
    positionList: [],
    deviceList: [],
    kvStoreModel: new KvStoreModel(),
    isDialogShowing: false,
    isNeedSync: false,
    title: ''
  },
  onInit() {
    if (this.title === '') {
      this.title = this.$t('strings.local');
    }

    if (this.positionList.length > 0) {
      this.positionList.forEach((num) => {
        this.initialData.push(num);
      });

      this.initDraw();
    }

    // 订阅分布式数据更新通知
    this.kvStoreModel.setDataChangeListener((data) => {
      data.updateEntries.forEach((num) => {
        this.positionList = [];
        const list = JSON.parse(num.value.value);
        list.forEach((num) => {
          this.positionList.push(num);
        })

        const self = this;
        setTimeout(function() {
          self.redraw();
        }, DRAW_DELAY);
      });
    });
  },

  // 初始化绘制
  initDraw() {
    // 使用定时器每10ms绘制一个坐标点
    const self = this;
    this.intervalID = setInterval(function() {
      if (self.initialData[0].isStartPosition) {
        self.ctx.beginPath();
        self.ctx.moveTo(self.initialData[0].positionX, self.initialData[0].positionY);
      } else {
        self.ctx.lineTo(self.initialData[0].positionX, self.initialData[0].positionY);
        self.ctx.stroke();
      }

      self.initialData.shift();

      if (self.initialData.length < 1) {
        // 绘制完成后清除定时器
        clearInterval(self.intervalID);
        self.intervalID = 0;
      }
    }, DRAW_DELAY);
  },

  // 初始化Path2D对象
  onShow() {
    const el = this.$refs.canvas;
    this.ctx = el.getContext('2d');
    this.ctx.lineWidth = LINE_WIDTH;
    this.ctx.lineCap = 'round';
    this.ctx.strokeStyle = '#090909';
  },

  // 手指触摸动作开始
  touchStart(msg) {
    this.startX = msg.touches[0].globalX;
    this.startY = msg.touches[0].globalY;
    this.ctx.beginPath();
    this.ctx.moveTo(this.startX, this.startY);
    const position = {};
    position.isStartPosition = true;
    position.positionX = this.startX;
    position.positionY = this.startY;
    this.pushData(position);
  },

  // 手指触摸后移动
  touchMove(msg) {
    this.moveX = msg.touches[0].globalX;
    this.moveY = msg.touches[0].globalY;
    this.ctx.lineTo(this.moveX, this.moveY);
    this.ctx.stroke();
    const position = {};
    position.isStartPosition = false;
    position.positionX = this.moveX;
    position.positionY = this.moveY;
    this.pushData(position);
  },

  // 将绘制笔迹写入分布式数据库
  pushData(position) {
    this.isNeedSync = true;
    this.positionList.push(position);
    const self = this;

    // 使用定时器每100ms写入一次
    if (this.intervalID === 0) {
      this.intervalID = setInterval(function() {
        if (self.isNeedSync) {
          self.kvStoreModel.put(CHANGE_POSITION, JSON.stringify(self.positionList));
          self.isNeedSync = false;
        }
      }, DATA_PUT_DELAY);
    }
  },

  // 撤回上一笔绘制
  goBack() {
    if (this.positionList.length > 0) {
      for (let i = this.positionList.length - 1; i > -1; i--) {
        if (this.positionList[i].isStartPosition) {
          this.positionList.pop();
          this.redraw();
          break;
        } else {
          this.positionList.pop();
        }
      }

      this.kvStoreModel.put(CHANGE_POSITION, JSON.stringify(this.positionList));
    }
  },

  // 重新绘制笔迹
  redraw() {
    // 清空画布
    this.ctx.clearRect(0, 0, CLEAR_WIDTH, CLEAR_HEIGHT);

    this.positionList.forEach((num) => {
      if (num.isStartPosition) {
        this.ctx.beginPath();
        this.ctx.moveTo(num.positionX, num.positionY);
      } else {
        this.ctx.lineTo(num.positionX, num.positionY);
        this.ctx.stroke();
      }
    });
  },

  // 获取并显示在线设备列表
  async showDeviceList() {
    let ret = await FeatureAbility.getDeviceList(0);
    this.deviceList = new Array();

    if (ret.code === 0) {
      for (let i = 0; i < ret.data.length; i++) {
        this.deviceList[i] = {
          deviceName: ret.data[i].deviceName,
          networkId: ret.data[i].networkId,
          checked: false
        }
      }
    }

    this.$element('showDialog').show();
  },

  // 选择设备
  selectDevice(index, e) {
    this.deviceList[index].checked = e.checked;
  },

  // 拉起在线设备并传递参数
  async chooseComform() {
    this.$element('showDialog').close();

    for (let i = 0; i < this.deviceList.length; i++) {
      if (this.deviceList[i].checked) {
        let actionData = {
          title: this.$t('strings.remote'),
          positionList: this.positionList
        };

        let target = {
          bundleName: 'com.huawei.jsdistributedraw',
          abilityName: 'com.huawei.jsdistributedraw.MainAbility',
          url: 'pages/index/index',
          networkId: this.deviceList[i].networkId,
          data: actionData
        };

        await FeatureAbility.startAbility(target);
      }
    }
  },

  // 取消弹框
  chooseCancel() {
    this.$element('showDialog').close();
  },
  onBackPress() {
    if (this.isDialogShowing === true) {
      this.dismissDialog();
      return true;
    }

    return false;
  }
};

