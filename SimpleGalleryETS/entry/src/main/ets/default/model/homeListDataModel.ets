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

export class ImageData {
  id: string
  smallImg: Resource
  bigImg: Resource
  name: string

  constructor(id: string, smallImg: Resource, bigImg: Resource, name: string) {
    this.id = id
    this.smallImg = smallImg
    this.bigImg = bigImg
    this.name = name
  }
}

export function initializeImageData(): Array<ImageData> {
  let imageDataArray: Array<ImageData> = [
    { "id": "0", "smallImg": $r('app.media.recommend1'), "bigImg": $r('app.media.recommend1'), "name": '推荐1' },
    { "id": "1", "smallImg": $r('app.media.recommend2'), "bigImg": $r('app.media.recommend2'), "name": '推荐2' },
    { "id": "2", "smallImg": $r('app.media.recommend3'), "bigImg": $r('app.media.recommend3'), "name": '推荐3' },
    { "id": "3", "smallImg": $r('app.media.recommend4'), "bigImg": $r('app.media.recommend4'), "name": '推荐4' },
    { "id": "4", "smallImg": $r('app.media.recommend5'), "bigImg": $r('app.media.recommend5'), "name": '推荐5' },
    { "id": "5", "smallImg": $r('app.media.recommend6'), "bigImg": $r('app.media.recommend6'), "name": '推荐6' },
    { "id": "6", "smallImg": $r('app.media.recommend7'), "bigImg": $r('app.media.recommend7'), "name": '推荐7' },
  ]
  return imageDataArray
}

export function initializeTodayData(): Array<ImageData> {
  let todyDataArray: Array<ImageData> = [
    { "id": "0", "smallImg": $r('app.media.today1'), "bigImg": $r('app.media.today1'), "name": '今日推荐1' },
    { "id": "1", "smallImg": $r('app.media.today2'), "bigImg": $r('app.media.today2'), "name": '今日推荐2' },
    { "id": "2", "smallImg": $r('app.media.today3'), "bigImg": $r('app.media.today3'), "name": '今日推荐3' },
    { "id": "3", "smallImg": $r('app.media.today4'), "bigImg": $r('app.media.today4'), "name": '今日推荐4' },
    { "id": "4", "smallImg": $r('app.media.today5'), "bigImg": $r('app.media.today5'), "name": '今日推荐5' },
    { "id": "5", "smallImg": $r('app.media.today6'), "bigImg": $r('app.media.today6'), "name": '今日推荐6' },
    { "id": "6", "smallImg": $r('app.media.today7'), "bigImg": $r('app.media.today7'), "name": '今日推荐7' },
    { "id": "7", "smallImg": $r('app.media.today8'), "bigImg": $r('app.media.today8'), "name": '今日推荐8' },
    { "id": "8", "smallImg": $r('app.media.today9'), "bigImg": $r('app.media.today9'), "name": '今日推荐9' },
    { "id": "9", "smallImg": $r('app.media.today10'), "bigImg": $r('app.media.today10'), "name": '今日推荐10' },
    { "id": "10", "smallImg": $r('app.media.today11'), "bigImg": $r('app.media.today11'), "name": '今日推荐11' }
  ]
  return todyDataArray
}