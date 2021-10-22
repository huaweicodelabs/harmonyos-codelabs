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

@Component
struct Item {

// 文本信息
  private text: string

// 页面跳转uri
  private uri: string

  build() {
    Stack({ alignContent: Alignment.Center }) {
      Image($rawfile('image3.png'))
        .objectFit(ImageFit.Cover)
        .width('100%')
        .height(100)
        .borderRadius(15)

      Text(this.text)
        .fontSize(20)
        .fontWeight(FontWeight.Bold)
        .fontColor(Color.Black)
    }
    .onClick(() => {
      router.push({ uri: this.uri })
    })
    .height(100)
    .borderRadius(15)
    .width('80%')
    .margin({ bottom: 20 })
  }
}

@Entry
@Component
struct Index {
  build() {
    Flex({ direction: FlexDirection.Column, alignItems: ItemAlign.Center, justifyContent: FlexAlign.Center }) {
      Item({ text: '页面间转场：底部滑入', uri: 'pages/page/BottomTransition' })
      Item({ text: '页面间转场：自定义1', uri: 'pages/page/CustomTransition' })
      Item({ text: '页面间转场：自定义2', uri: 'pages/page/FullCustomTransition' })
      Item({ text: '组件内转场', uri: 'pages/ComponentTransition' })
      Item({ text: '共享元素转场', uri: 'pages/share/ShareItem' })
    }
    .width('100%')
    .height('100%')
    .backgroundColor('#FFECECEC')
  }
}