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

import router from '@system.router'
import featureAbility from '@ohos.ability.featureAbility';
import {NewsData} from '../model/NewsData'
import {DetailComponent} from '../Component/DetailComponent'
import {getDefaultData} from '../model/NewsDataModel'
import {getNewsDatas} from '../model/NewsDataModel'

@Entry
@Component
struct NewsDetail {
  @Provide newsData: NewsData= getDefaultData()
  private newsItems: NewsData[] = getNewsDatas()

  build() {
    Column() {
      DetailComponent()
    }.height("100%").width("100%")
  }

  aboutToAppear() {
    if (router.getParams()) {
      // 从本机获取数据
      this.newsData = router.getParams().newsItem
    } else {
      // 从远端获取数据
      featureAbility.getWant()
        .then((want) => {
          console.info('getWant successful. Data: ' + JSON.stringify(want));
          // 获取从远端
          let newsId = want.parameters.newsId
          // 通过newsId获取新闻信息
          this.newsData = this.newsItems.filter(item => (item.newsId === newsId))[0]
        }).catch((error) => {
        console.error('getWant failed. Cause: ' + JSON.stringify(error));
      })
    }
  }
}

