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
    require: '/common/images/require.png',
    username: '',
    password: '',
    password2: '',
    email: '',
    date: '',
    height: '',
    favorite: [],
    list: [{icon: '/common/images/require.png', content: '选项0'},
      {icon: '/common/images/require.png', content: '选项1'}]
  },
  // 文本框内容发生变化
  change(e) {
    const idName = e.target.id;
    if (idName === 'username') {
      this.username = e.value;
    } else if (idName === 'password') {
      this.password = e.value;
    } else if (idName === 'password2') {
      this.password2 = e.value;
    } else if (idName === 'email') {
      this.email = e.value;
    } else if (idName === 'date') {
      this.date = e.value;
    } else if (idName === 'height') {
      this.height = e.value;
    }
  },
  // 复选框修改
  checkboxOnChange(e) {
    const value = e.target.attr.value;
    if (e.checked) {
      this.favorite.push(value);
    } else {
      this.favorite.splice(this.favorite.findIndex(e => e === value), 1);
    }
  },
  // 提交
  buttonClick() {
    if (this.username === '') {
      this.showPrompt('用户名不能为空');
    } else if (this.password === '') {
      this.showPrompt('密码不能为空');
    } else if (this.password !== '' && this.password !== this.password2) {
      this.showPrompt('两次密码输入不一致');
    } else if (this.email === '') {
      this.showPrompt('邮箱不能为空');
    } else if (this.favorite.length === 0) {
      this.showPrompt('请至少选择一个爱好');
    } else {
      this.showPrompt('提交成功');
    }
  },
  // 弹框
  showPrompt(msg) {
    prompt.showToast({
      message: msg,
      duration: 3000
    });
  },
  // 进行文本选择操作后文本选择弹窗会出现翻译按钮
  translate(e) {
    this.showPrompt(e.value);
  },
  // 进行文本选择操作后文本选择弹窗会出现分享按钮
  share(e) {
    this.showPrompt(e.value);
  },
  // 进行文本选择操作后文本选择弹窗会出现查找按钮
  search(e) {
    this.showPrompt(e.value);
  },
  // 用户在文本选择操作后，点击菜单项后触发该回调
  optionselect(e) {
    this.showPrompt('选项' + e.index + ': ' + e.value);
  }
};
