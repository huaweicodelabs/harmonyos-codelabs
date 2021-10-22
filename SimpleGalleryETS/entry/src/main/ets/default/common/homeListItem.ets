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

import router from '@system.router'
import { ImageData, initializeImageData, initializeTodayData } from '../model/homeListDataModel'

@Component
export struct HomeListItem {
  private recommends: Resource[] = [$r('app.string.today_recommend'), $r('app.string.featured_recommend'),
                                    $r('app.string.september_recommend'), $r('app.string.august_recommend')]
  private listItems: ImageData[] = initializeImageData()
  private isToday: boolean = true
  private titleIndex: number = 0
  private imageWidth: string = '120'
  private ratio: number = 1

  aboutToAppear() {
    if (this.titleIndex === 0) {
      this.listItems = initializeTodayData()
      this.isToday = true
      this.imageWidth = '92'
      this.ratio = 1
    } else {
      this.listItems = initializeImageData()
      this.isToday = false
      this.imageWidth = '145'
      this.ratio = 0.65
    }
  }

  build() {
    Column() {
      Row() {
        Text(this.recommends[this.titleIndex])
          .textAlign(TextAlign.Start)
          .fontSize(16)
          .fontWeight(FontWeight.Bold)
          .layoutWeight(1)
          .borderColor(Color.Blue)
        Text($r('app.string.more'))
          .fontSize(16)
          .fontColor(Color.Gray)
        Image($r('app.media.more'))
          .width('10').height('5%')
          .alignSelf(ItemAlign.End)
          .objectFit(ImageFit.Contain)
      }
      .width('100%')

      List({ initialIndex: 0 }) {
        ForEach(this.listItems, item => {
          ListItem() {
            Image(item.smallImg)
              .width(this.imageWidth)
              .aspectRatio(this.ratio)
              .borderRadius(10)
              .margin({ right: 15 })
              .sharedTransition(this.titleIndex + item.id, { duration: 500, curve: Curve.Linear })
              .onClick(() => {
                let shareIdStr = this.titleIndex + item.id
                console.info('Item onClick' + shareIdStr)
                router.push({
                  uri: 'pages/image',
                  params: { isToday: this.isToday, shareId: shareIdStr, index: item.id, ratio: this.ratio }
                })
            })
          }
        }, item => item.name)
      }
      .listDirection(Axis.Horizontal)
      .margin({ top: '2%', bottom: '2%' })
    }
  }
}