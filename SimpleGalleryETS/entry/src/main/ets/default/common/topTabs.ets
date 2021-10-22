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

import prompt from '@system.prompt'
import resourceManager from '@ohos.resourceManager'

@Component
export struct TopTabs {
  private tabs: Resource[] = [$r('app.string.featured'), $r('app.string.discover'), $r('app.string.wallpaper'),
                              $r('app.string.knowledge'), $r('app.string.photo')]
  private tips: string = '这是测试功能，暂时未实现'
  @Link showSettings: boolean

  build() {
    Flex({ direction: FlexDirection.Row, alignItems: ItemAlign.Center }) {
      Row() {
        ForEach(this.tabs, item => {
          if (this.tabs.indexOf(item) == 0) {
            Text(item)
              .fontSize(18)
              .fontWeight(FontWeight.Bold)
          } else {
            Text(item)
              .fontSize(17)
              .fontWeight(FontWeight.Medium)
              .margin({ left: '5%' })
          }
        }, item => this.tabs.indexOf(item).toString())
      }
      .layoutWeight(1)
      .height('100%')

      Image($r('app.media.search'))
        .width('7%').height('80%')
        .constraintSize({ maxWidth: 25 })
        .objectFit(ImageFit.Contain)
        .onClick(() => {
          prompt.showToast({
            message: this.tips
          })
        })

      Image($r('app.media.setting'))
        .width('7%').height('80%')
        .constraintSize({ maxWidth: 25 })
        .objectFit(ImageFit.Contain)
        .margin({ left: 5 })
        .onClick(() => {
          animateTo({ duration: 500, curve: Curve.Ease }, () => {
            this.showSettings = !this.showSettings
          })
        })
    }
    .height('8%').width('100%')
    .alignSelf(ItemAlign.Start)
  }
}