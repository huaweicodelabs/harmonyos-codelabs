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

import featureAbility from '@ohos.ability.featureAbility';
import wantConstant from '@ohos.ability.wantConstant';

/**
 * 拉起远程新闻详情页
 */
export function startRemoteAbilities(deviceIds, newsId) {
  for (var i = 0; i < deviceIds.length; i++) {
    var want = {
      "want": {
        "deviceId": deviceIds[i],
        "bundleName": "com.huawei.codelab",
        "abilityName": "com.huawei.codelab.MainAbility",
        // 分布式任务flag
        "flags": wantConstant.Flags.FLAG_ABILITYSLICE_MULTI_DEVICE,
        "parameters": {
          // 指定跳转的页面
          "url": 'pages/NewsDetail',
          // 跳转携带的参数
          "newsId": newsId
        },
      }
    };
    featureAbility.startAbility(want, (err, data) => {
      if (err) {
        console.error('startRemoteAbility err:' + JSON.stringify(err));
        return;
      }
      console.info('startRemoteAbility successful. Data: ' + JSON.stringify(data))
    });
  }
}