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

import { NotificationSettings } from '../common/notificationSettings'
import { HomeTabComponent } from '../common/homeTabContent'
import { BottomTabs } from '../common/bottomTabs'

@Entry
@Component
struct HomeComponent {
  private controller: TabsController = new TabsController()
  private backgroundColor: string = '#F1F3F5'
  @State bottomTabIndex: number = 1
  @State showSettings: boolean = false

  build() {
    Stack({ alignContent: Alignment.BottomStart }) {
      Flex({ direction: FlexDirection.Column, alignItems: ItemAlign.End, justifyContent: FlexAlign.End }) {
        Tabs({ barPosition: BarPosition.End, index: 1, controller: this.controller }) {
          TabContent() {
          }
          TabContent() {
            HomeTabComponent({ showSettings: $showSettings })
          }.padding({ left: 15, right: 15 })
          TabContent() {
          }
          TabContent() {
          }
        }
        .onChange((index: number) => {
          this.bottomTabIndex = index
        })
        .vertical(false)
        .barHeight(0)
        .width('100%')
        .scrollable(false)
        BottomTabs({ controller: this.controller, bottomTabIndex: $bottomTabIndex })
      }
      .width('100%')
      .layoutWeight(1)
      .backgroundColor(this.backgroundColor)

      if (this.showSettings) {
        NotificationSettings({ showSettings: $showSettings })
      }
    }
    .width('100%').height('100%')
  }
}
