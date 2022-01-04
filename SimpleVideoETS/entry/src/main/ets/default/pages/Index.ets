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
import {swiperVideos, horizontalVideos} from '../common/data/VideoData.ets'

@Entry
@Component
struct Index {
  build() {
    Column() {
      // 视频轮播组件
      Swiper() {
        ForEach(swiperVideos, item => {
          SwiperItem({ imageSrc: item.image, source: item.source })
        }, item => item.image.toString())
      }
      .autoPlay(true)
      .height(180)
      .itemSpace(15)

      // 视频列表上方的文本信息
      Flex({ direction: FlexDirection.Row }) {
        Text('Coming soon')
          .fontSize(20).fontWeight(FontWeight.Bold).margin({ left: 10 })
        Image('/common/image/next.png').height(8).width(16)
      }
      .margin({ top: 20, bottom: 15 })

      // 视频列表组件
      List({ space: 15 }) {
        ForEach(horizontalVideos, item => {
          ListItem() {
            HorizontalItem({ imageSrc: item.image, source: item.source })
          }
        }, item => item.image.toString())
      }
      // 设置列表横向排列
      .listDirection(Axis.Horizontal)
    }
    .backgroundColor("#EEEEEE")
    .padding({ left: 15, top: 15, right: 15, bottom: 15 })
  }
}

@Component
struct SwiperItem {
  private imageSrc: string
  private source: string

  build() {
    // 跳转一：使用Navigator组件跳转到视频播放界面
    Navigator({ target: 'pages/Play', type: NavigationType.Push }) {
      Image(this.imageSrc).objectFit(ImageFit.Cover)
    }
    .params({ source: this.source })
  }
}

@Component
struct HorizontalItem {
  private imageSrc: string
  private source: string

  build() {
    // 跳转二：使用route跳转到视频播放界面
    Image(this.imageSrc)
      .width('80%')
      .height('25%')
      .onClick(() => {
        router.push({
          uri: 'pages/Play',
          params: { source: this.source }
        })
      })
  }
}