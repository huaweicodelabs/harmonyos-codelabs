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
import {ImageData, initializeImageData, initializeTodayData} from '../model/homeListDataModel'

@Entry
@Component
struct ImageComponent {
  private imageSrc: ImageData[] = initializeImageData()
  private imageIndex: number = router.getParams().index
  private shareId: string = router.getParams().shareId
  private isToday: boolean = router.getParams().isToday
  @State ratio: number = router.getParams().ratio
  @State title: string = ''
  @State imageMargin: number = 56
  @State visibility: Visibility = Visibility.Visible
  @State scale: number = 1

  aboutToAppear() {
    if (this.isToday) {
      this.imageSrc = initializeTodayData()
    } else {
      this.imageSrc = initializeImageData()
    }
    if(this.imageIndex < this.imageSrc.length) {
      this.title = this.imageSrc[this.imageIndex].name
    }

    setTimeout(()=>{
       this.ratio = 0
    },500)
  }

  build() {
    Flex({ direction: FlexDirection.Column, alignItems: ItemAlign.End, justifyContent: FlexAlign.End }) {
      Row() {
        Image($r('app.media.arrow'))
          .width('8%').height('50%')
          .objectFit(ImageFit.Contain)
          .margin({ left: 10 })
          .onClick(() => {
            router.back()
          })
        Text(this.title)
          .fontSize(20)
          .fontColor(Color.Black)
          .margin({ left: 10 })
      }
      .width('100%').height(this.imageMargin)
      .backgroundColor('#F1F3F5')
      .visibility(this.visibility)

      Flex({ direction: FlexDirection.Column, alignItems: ItemAlign.Center, justifyContent: FlexAlign.Center }) {
        Swiper() {
          ForEach(this.imageSrc, item => {
            Image(item.bigImg)
              .height('100%').width('100%')
              .objectFit(ImageFit.Contain)
              .onClick(() => {
                console.info('Image Click')
                this.scale = 1
                if (this.visibility == Visibility.Hidden) {
                  this.imageMargin = 56
                  this.visibility = Visibility.Visible
                } else {
                  this.imageMargin = 0
                  this.visibility = Visibility.Hidden
                }
              })
              .gesture(
              GestureGroup(GestureMode.Parallel,
              PinchGesture()
                .onActionStart(() => {
                  console.log('pinch start')
                })
                .onActionUpdate((event: GestureEvent) => {
                  if (this.visibility == Visibility.Hidden && event.scale <= 2.0) {
                    this.scale = event.scale
                  }
                })
                .onActionEnd(() => {
                  if (this.scale < 1.0) {
                    animateTo({ duration: 500, curve: Curve.Ease }, () => {
                      this.scale = 1.0
                    })
                  }
                })
                , TapGesture({ count: 2, fingers: 1 })
                .onAction(() => {
                  if (this.visibility == Visibility.Hidden) {
                    animateTo({ duration: 500, curve: Curve.Ease }, () => {
                      this.scale = this.scale < 2.0 ? 2.0 : 1.0
                    })
                  }
                })
              )
              )
          }, item => item.name)
        }
        .width('100%').height('100%')
        .aspectRatio(this.ratio)
        .scale({ x: this.scale, y: this.scale })
        .index(this.imageIndex)
        .indicator(false)
        .loop(false)
        .sharedTransition(this.shareId, { duration: 500, curve: Curve.Linear })
        .onChange((index: number) => {
          this.scale = 1.0
          this.imageIndex = index
          if(this.imageIndex < this.imageSrc.length) {
            this.title = this.imageSrc[this.imageIndex].name
          }
        })
      }.width('100%').height('100%')

      Column() {
        Image($r('app.media.delete'))
          .objectFit(ImageFit.Contain)
          .width(25).height(25)
          .margin({ top: 5 })
        Text($r('app.string.delete'))
          .fontSize(14)
          .fontColor(Color.Black)
          .textAlign(TextAlign.Start)
      }
      .width('100%').height(this.imageMargin)
      .backgroundColor('#F1F3F5')
      .visibility(this.visibility)
    }
    .width('100%').height('100%')
  }
}