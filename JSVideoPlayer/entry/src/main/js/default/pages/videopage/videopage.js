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

export default {
  data: {
    url: '/common/test_video.mp4',
    isStart: true,
    isFullScreenChange: false
  },
  onInit() {
    this.url = router.getParams().source;
  },
  // 点击控制栏播放视频
  startCallback: function() {
    this.isStart = true;
  },
  // 点击控制栏暂停视频
  pauseCallback: function() {
    this.isStart = false;
  },
  // 点击屏幕播放和暂停视频
  changeStartPause() {
    if (this.isStart) {
      this.$element('videoId').pause();
      this.isStart = false;
    } else {
      this.$element('videoId').start();
      this.isStart = true;
    }
  },
  // 点击控制栏进入全屏和退出全屏
  fullScreenChange(e) {
    if (e.fullscreen === 1) {
      this.isFullScreenChange = true;
    } else {
      this.isFullScreenChange = false;
    }
  },
  // 长按屏幕视频进入和退出全屏
  longPressFullScreenChange() {
    if (!this.isFullScreenChange) {
      this.$element('videoId').requestFullscreen({
        screenOrientation: 'default'
      });
      this.isFullScreenChange = true;
    } else {
      this.$element('videoId').exitFullscreen();
      this.isFullScreenChange = false;
    }
  }
};
