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

import { TopTabs } from '../common/topTabs'
import { SubscribeSwiper } from '../common/subscribe'
import { HomeListItem } from '../common/homeListItem'

@Component
export struct HomeTabComponent {
  private recommends: Resource[] = [$r('app.string.today_recommend'), $r('app.string.featured_recommend'),
                                    $r('app.string.september_recommend'), $r('app.string.august_recommend')]
  @Link showSettings: boolean

  build() {
    Column() {
      TopTabs({ showSettings: $showSettings })
      List() {
        ListItem() {
          SubscribeSwiper()
        }

        ForEach(this.recommends, item => {
          ListItem() {
            HomeListItem({ titleIndex: this.recommends.indexOf(item) })
          }
        }, item => this.recommends.indexOf(item).toString())
      }
      .listDirection(Axis.Vertical)
      .width('100%')
      .layoutWeight(1)
    }
  }
}