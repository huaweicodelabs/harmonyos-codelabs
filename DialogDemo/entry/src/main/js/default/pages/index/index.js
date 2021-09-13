/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
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

export default {
  data: {
    percent: 0,
    interval: ''
  },
  showAlert() {
    this.$element('alertDialog').show();
  },
  showConfirm() {
    this.$element('confirmDialog').show();
  },
  showLoading() {
    const options = {
      duration: 800,
      easing: 'linear',
      iterations: 'Infinity'
    };
    const frames = [
      {
        transform: {
          rotate: '0deg'
        }
      },
      {
        transform: {
          rotate: '360deg'
        }
      }
    ];
    this.animation = this.$element('loading-img').animate(frames, options);
    this.$element('loadingDialog').show();
    this.animation.play();
  },
  showPrompt() {
    this.$element('promptDialog').show();
  },
  showProgress() {
    const that = this;
    that.percent = 0;
    this.$element('progressDialog').show();
    this.interval = setInterval(function() {
      that.percent += 10;
      if (that.percent >= 100) {
        clearInterval(that.interval);
      }
    }, 500);
  },
  confirmClick(id) {
    this.$element(id).close();
    prompt.showToast({
      message: 'confirm clicked'
    });
  },
  cancelClick(id) {
    this.$element(id).close();
    prompt.showToast({
      message: 'cancel clicked'
    });
  },
  onCancel(){
    clearInterval(this.interval);
  }
};
