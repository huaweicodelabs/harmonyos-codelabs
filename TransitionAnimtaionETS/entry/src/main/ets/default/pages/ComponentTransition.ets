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

@Component
struct ComponentItem {
  build() {
    Stack({ alignContent: Alignment.Center }) {
      Image($rawfile('image3.png'))
        .objectFit(ImageFit.Cover)
        .width('100%')
        .height(120)
        .borderRadius(15)
    }
    .height(120)
    .borderRadius(15)
    .width('80%')
    .padding({ top: 20 })
    // 组件添加时x、y轴缩放从0.5变化到1，透明度从0到1
    .transition({ type: TransitionType.Insert, scale: { x: 0.5, y: 0.5 }, opacity: 0 })
    // 组件移除时沿y轴旋转360度，x、y轴缩放从1变化到0，
    .transition({ type: TransitionType.Delete, rotate: { x: 0, y: 1, z: 0, angle: 360 }, scale: { x: 0, y: 0 } })
  }
}

@Entry
@Component
struct ComponentTransition {
  @State private isShow: boolean= false

  build() {
    Column() {
      if (this.isShow) {
        ComponentItem()
      }

      ComponentItem()

      Button("Toggle")
        .onClick(() => {
        //执行动效，动效时长600ms
        animateTo({ duration: 600 }, () => {
          this.isShow = !this.isShow;
        })
      })
        .height(45)
        .width(200)
        .fontColor(Color.Black)
        .backgroundColor('rgb(181,222,224)')
        .margin({ top: 20 })
    }
    .padding({ left: 20, right: 20 })
    .backgroundColor('#FFECECEC')
    .height('100%')
    .width('100%')
  }
}

