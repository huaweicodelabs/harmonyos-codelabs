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

@Entry
@Component
struct ShareItem {
  build() {
    Flex() {
      Flex({ justifyContent: FlexAlign.Start, alignItems: ItemAlign.Center }) {
        Stack() {
          Image($rawfile('image2.jpg'))
          // 设置共享元素转场属性
          .sharedTransition('imageId', { duration: 600, curve: Curve.Smooth, delay: 100 })
            .onClick(() => {
            router.push({ uri: 'pages/share/SharePage' })
          })
            .objectFit(ImageFit.Cover)
            .height('100%')
            .width('100%')
            .borderRadius(15)
        }
        .height('100%')
        .width('100%')

        Text('点击查看共享元素转场动效')
          .fontSize(20)
          .fontColor(Color.Black)
          .fontWeight(FontWeight.Regular)
          .margin({ left: 10, right: 10 })

      }
      .height(120)
      .backgroundColor('rgb(181,222,224)')
      .borderRadius(15)
      .margin({ top: 20 })
    }
    .width('100%')
    .padding({ left: 16, right: 16 })
    .backgroundColor('#FFECECEC')
  }
}
