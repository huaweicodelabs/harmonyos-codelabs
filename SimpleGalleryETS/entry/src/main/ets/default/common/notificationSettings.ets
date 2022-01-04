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

import { SubscribeData, getSubscribeData, getSwitchState } from '../model/subscribeDataModels'
import reminderAgent from '@ohos.reminderAgent'

let subscribeItems = getSubscribeData()
let reminderId = 0

@Component
export struct NotificationSettings {
  @State subscribes: SubscribeData[] = subscribeItems
  @State isToggleOn: boolean = getSwitchState()
  @Link showSettings: boolean

  build() {
    Scroll() {
      Column() {
        Text()
          .width('20%').height('1%')
          .backgroundColor(Color.Gray)
          .borderRadius(20)
          .margin({ top: 5 })
        Row() {
          Text($r('app.string.notification_settings'))
            .fontSize(18)
            .fontColor(Color.Black)
            .fontWeight(FontWeight.Bold)
            .layoutWeight(1)
            .textAlign(TextAlign.Start)
          Image($r('app.media.close'))
            .objectFit(ImageFit.Cover)
            .width(30).height(30)
            .onClick(() => {
              animateTo({ duration: 500, curve: Curve.Ease }, () => {
                this.showSettings = !this.showSettings
              })
            })
        }
        .width('100%')
        .padding({ top: '2%', left: '5%', right: '5%' })

        Text($r('app.string.harmony_notification'))
          .width('100%')
          .fontColor(Color.Grey)
          .fontSize(14)
          .textAlign(TextAlign.Start)
          .padding({ top: '3%', left: '5%' })

        Column() {
          Flex({ direction: FlexDirection.Row, alignItems: ItemAlign.Center, justifyContent: FlexAlign.End }) {
            Text($r('app.string.background_reminderAgent'))
              .fontSize(16)
              .fontColor(Color.Black)
              .fontWeight(FontWeight.Bold)
              .width('100%')
              .textAlign(TextAlign.Start)
            Toggle({ type: ToggleType.Switch, isOn: this.isToggleOn })
              .width(70)
              .height(30)
          }
          .width('100%')
          .margin({ top: '3%' })
          .padding({ left: '5%', right: '5%' })
          .backgroundColor(Color.White)

          Text($r('app.string.reminder_tips'))
            .fontColor(Color.Grey)
            .fontSize(12)
            .margin({ top: '3%' })

          List() {
            ForEach(this.subscribes, item => {
              if (item.isSubscribe) {
                ListItem() {
                  Flex({ direction: FlexDirection.Row, alignItems: ItemAlign.Center }) {
                    Text(item.title)
                      .fontSize(16)
                      .fontColor(Color.Black)
                      .fontWeight(FontWeight.Bold)
                      .width('100%')
                      .textAlign(TextAlign.Start)
                      .layoutWeight(1)
                    Image($r('app.media.close_white'))
                      .objectFit(ImageFit.Contain)
                      .width(25)
                      .onClick(() => {
                        reminderId = item.reminderId
                        subscribeItems[item.id].isSubscribe = false
                        subscribeItems[item.id].reminderId = 0
                        this.subscribes = subscribeItems
                        this.isToggleOn = getSwitchState()
                        reminderAgent.cancelReminder(reminderId)
                      })
                  }
                  .width('100%').height('10%')
                }
              }
            }, item => item.title.toString())
          }
          .backgroundColor(Color.White)
          .margin({ top: '5%' })
          .padding({ left: '5%', right: '5%' })
          .divider({ strokeWidth: 1, color: '#E5E5E6' })
        }
      }
      .backgroundColor('#E8E8E8')
      .constraintSize({ minHeight: '100%' })
      .borderRadius(30)
    }
    .height('70%').width('100%')
    .borderRadius(30)
    .backgroundColor('#E8E8E8')
    .transition({ type: TransitionType.Insert, translate: { y: '100%' } })
    .transition({ type: TransitionType.Delete, translate: { y: '100%' } })
  }
}