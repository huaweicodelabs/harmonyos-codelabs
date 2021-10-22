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

import reminderAgent from '@ohos.reminderAgent'
import { SubscribeData, SubscribeTime, getSubscribeData, initializeSubscribeTimes } from '../model/subscribeDataModels'

let calendar = null
let subscribeItems: SubscribeData[] = getSubscribeData()
let subscribeTimes: SubscribeTime[] = initializeSubscribeTimes()
let num = 0

@CustomDialog
struct SubscribeDialog {
  controller: CustomDialogController
  action: () => void

  build() {
    Column() {
      Text($r('app.string.subscribe_succeed'))
        .fontSize(17)
        .fontColor(Color.Black)
        .fontWeight(FontWeight.Bold)
      Text($r('app.string.add_reminder'))
        .fontSize(17)
        .fontColor(Color.Black)
        .fontWeight(FontWeight.Medium)
        .margin({ top: '3%' })
      Row() {
        Button($r('app.string.yes'))
          .layoutWeight(7)
          .fontColor(Color.Blue)
          .backgroundColor(Color.White)
          .margin(5)
          .onClick(() => {
            this.controller.close()
            this.action()
          })
        Text()
          .width(1).height(35)
          .backgroundColor('#8F8F8F')
        Button($r('app.string.no'))
          .layoutWeight(7)
          .fontColor(Color.Red)
          .backgroundColor(Color.White)
          .margin(5)
          .onClick(() => {
            this.controller.close()
            this.action()
          })
      }
      .width('100%')
      .margin({ top: '3%' })
    }
    .padding('3%')
  }
}

@Component
struct SwiperItem {
  private index: number
  private isSubscribe: boolean
  dialogController: CustomDialogController = new CustomDialogController({
    builder: SubscribeDialog({ action: this.onAccept }),
    cancel: this.cancelDialog,
    autoCancel: true
  })

  onAccept() {
    console.log("onAccept")
    if (calendar == null) {
      console.log("the calendar reminder is null")
      return
    }
    reminderAgent.publishReminder(calendar, (err, reminderId) => {
      console.info('num=' + num + ', reminderId:' + reminderId)
      subscribeItems[num].isSubscribe = true
      subscribeItems[num].reminderId = reminderId
    })
  }

  cancelDialog() {
    console.log("Cancel dialog")
  }

  build() {
    Stack({ alignContent: Alignment.BottomStart }) {
      Image(subscribeItems[this.index].image)
        .objectFit(ImageFit.Cover)
        .height('100%')
        .width('100%')
        .borderRadius(10)
      Text(subscribeItems[this.index].title)
        .fontSize(15)
        .fontColor(Color.Black)
        .fontWeight(FontWeight.Bold)
        .margin({ left: '5%', bottom: '10%' })
    }
    .height('100%')
    .width(330)
    .onClick(() => {
      num = this.index
      if (this.index >= subscribeItems.length || subscribeItems[this.index].isSubscribe) {
        calendar = null
        return
      } else {
        calendar = {
          reminderType: 1,
          dateTime: {
            year: subscribeTimes[this.index].year,
            month: subscribeTimes[this.index].month,
            day: subscribeTimes[this.index].day,
            hour: subscribeTimes[this.index].hour,
            minute: subscribeTimes[this.index].minute,
            second: subscribeTimes[this.index].second
          },
          repeatMonths: [0],
          repeatDays: [0],
          actionButton: [
            {
              title: 'close',
              type: 0
            },
            {
              title: 'snooze',
              type: 1
            },
          ],
          wantAgent: {
            pkgName: 'ohos.samples.simplegallery',
            abilityName: 'ohos.samples.simplegallery.MainAbility'
          },
          maxScreenWantAgent: {
            pkgName: 'ohos.samples.simplegallery',
            abilityName: 'ohos.samples.simplegallery.MainAbility'
          },
          ringDuration: 5,
          snoozeTimes: 2,
          timeInterval: 5,
          title: 'SimpleGallery',
          content: subscribeItems[this.index].title,
          expiredContent: '',
          snoozeContent: "remind later",
          notificationId: 100,
          slotType: 3
        }
      }
      this.dialogController.open()
    })
  }
}

@Component
export struct SubscribeSwiper {
  private index: number = 0

  build() {
    Swiper() {
      ForEach(subscribeItems, item => {
        SwiperItem({ index: item.id })
      }, item => item.title)
    }
    .width('100%')
    .height('30%')
    .indicator(false)
    .index(this.index)
    .autoPlay(true)
    .itemSpace(15)
    .displayMode(SwiperDisplayMode.AutoLinear)
    .margin({ bottom: '4%' })
  }
}