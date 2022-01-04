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
import prompt from '@system.prompt'

function getTabSrc(tabIndex: number, index: number) {
  let imgSrc = $r('app.media.tab_gray')
  if (tabIndex === index) {
    imgSrc = $r('app.media.tab_blue')
  }
  return imgSrc
}

function getTabTextColor(tabIndex: number, index: number) {
  let color = '#000000'
  if (tabIndex === index) {
    color = '#0A59F7'
  }
  return color
}

@Component
export struct BottomTabs {
  private tabSrc: number[] = [0, 1, 2, 3]
  private backgroundColor: string = '#F1F3F5'
  private controller: TabsController = new TabsController()
  private tips: string = '这是测试功能，暂时未实现'
  @Link bottomTabIndex: number

  build() {
    Flex({ direction: FlexDirection.Row, alignItems: ItemAlign.Center, justifyContent: FlexAlign.SpaceEvenly }) {
      ForEach(this.tabSrc, item => {
        Column() {
          Image(getTabSrc(this.bottomTabIndex, item))
            .objectFit(ImageFit.Contain)
            .width('60%').height('60%')
          Text($r('app.string.tab_strings'))
            .fontSize(14)
            .fontColor(getTabTextColor(this.bottomTabIndex, item))
        }
        .onClick(() => {
          if (item === this.bottomTabIndex) {
            this.controller.changeIndex(this.bottomTabIndex)
          } else {
            prompt.showToast({
              message: this.tips
            })
          }
        })
      }, item => item.toString())
    }
    .width('100%').height('8%')
    .backgroundColor(this.backgroundColor)
  }
}