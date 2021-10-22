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

export class SubscribeTime {
  year: number
  month: number
  day: number
  hour: number
  minute: number
  second: number

  constructor(year: number, month: number, day: number, hour: number, minute: number, second: number) {
    this.year = year
    this.month = month
    this.day = day
    this.hour = hour
    this.minute = minute
    this.second = second
  }
}

export function initializeSubscribeTimes(): Array<SubscribeTime> {
  let subscribeTimeArray: Array<SubscribeTime> =  [
    { "year": 2021, "month": 9, "day": 17, "hour": 20, "minute": 0, "second": 0 },
    { "year": 2021, "month": 9, "day": 18, "hour": 20, "minute": 30, "second": 0 },
    { "year": 2021, "month": 9, "day": 19, "hour": 20, "minute": 0, "second": 0 },
    { "year": 2021, "month": 9, "day": 19, "hour": 20, "minute": 0, "second": 0 },
  ]
  return subscribeTimeArray
}

export class SubscribeData {
  id: number
  title: string
  image: Resource
  isSubscribe: boolean
  reminderId: number

  constructor(id: number, title: string, image: Resource, isSubscribe: boolean, reminderId: number) {
    this.id = id
    this.image = image
    this.title = title
    this.isSubscribe = isSubscribe
    this.reminderId = reminderId
  }
}

let subscribeDataArray:Array<SubscribeData> = null

export function getSubscribeData(): Array<SubscribeData> {
  if(subscribeDataArray === null) {
    subscribeDataArray = [
      { "id": 0, "title": '精致女人穿搭技巧', "image": $r('app.media.jxdt1'), "isSubscribe": false, "reminderId": 0 },
      { "id": 1, "title": '猫咪的欢乐日常', "image": $r('app.media.jxdt2'), "isSubscribe": false, "reminderId": 0 },
      { "id": 2, "title": '开心养鱼', "image": $r('app.media.jxdt3'), "isSubscribe": false, "reminderId": 0 },
      { "id": 3, "title": '炎炎养猫记', "image": $r('app.media.jxdt4'), "isSubscribe": false, "reminderId": 0 }
    ]
  }
  return subscribeDataArray
}

let settingsSwitchState = false

export function getSwitchState(): boolean {
  settingsSwitchState = false
  subscribeDataArray.forEach(item => {
    if (item.isSubscribe) {
      settingsSwitchState = true
    }
  })
  return settingsSwitchState
}