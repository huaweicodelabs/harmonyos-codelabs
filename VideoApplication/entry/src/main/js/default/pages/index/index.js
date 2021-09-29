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
import prompt from '@system.prompt';

export default {
  data: {
    autoplay: false, // 是否自动播放
    videoId: 'video', // 播放器id
    url: '/common/video/1.mp4', // 视频地址
    posterUrl: '/common/images/bg-tv.jpg', // 视频预览的海报路径
    controlShow: true, // 是否显示控制栏
    loop: true, // 是否循环播放
    startTime: 10, // 播放开始时间
    speed: 0.2, // 播放速度
    isfullscreenchange: false // 是否全屏
  },
  // 视频准备完成时触发该事件
  prepared(e) {
    this.showPrompt('视频时长：' + e.duration + '秒');
  },
  // 视频开始播放
  start() {
    this.showPrompt('视频开始播放');
  },
  // 视频暂停播放
  pause() {
    this.showPrompt('视频暂停播放');
  },
  // 视频播放完成
  finish() {
    this.$element('confirmDialog').show();
  },
  // 拖动进度条调用
  seeked(e) {
    this.showPrompt('设置播放进度：' + e.currenttime + '秒');
  },
  // 播放进度变化调用
  timeupdate(e) {

  },
  // 自带组件进入全屏和退出全屏
  fullscreenchange(e) {
    if (e.fullscreen === 1) {
      this.isfullscreenchange = true;
    } else {
      this.isfullscreenchange = false;
    }
  },
  // 长按屏幕视频进入和退出全屏调用
  longPressFullscreenchange() {
    if (this.isfullscreenchange) { // 全屏
      this.$element('video').exitFullscreen();
      this.isfullscreenchange = false;
    } else { // 非全屏
      this.$element('video').requestFullscreen({ screenOrientation: 'default' });
      this.isfullscreenchange = true;
    }
  },

  // dialog确定
  confirm() {
    this.$element('video').start();
    this.$element('confirmDialog').close();
  },
  // dialog取消
  cancel() {
    this.$element('confirmDialog').close();
  },
  // 弹框
  showPrompt(msg) {
    prompt.showToast({
      message: msg,
      duration: 1000
    });
  },
  // 点击视频
  hideControls() {
    this.controlShow = !this.controlShow;
  }

};
