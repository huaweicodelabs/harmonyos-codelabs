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

@Entry
@Component
struct Index {
  @State private speed: number = 5
  @State private imageSize: number = 1
  @State private angle: number = 0
  @State private interval: number = 0

  @Builder DescribeText(text:string, speed: number) {
    Stack() {
      Text(text + speed.toFixed(1))
        .margin({ top: 30 })
        .fontSize(20)
        .fontWeight(FontWeight.Bold)
    }
  }

  build() {
    Column() {
      Image($rawfile('windmill.png'))
        .objectFit(ImageFit.Contain)
        .height(150)
        .width(150)
        .margin({ top: 50, bottom: 150, right: 16 })
        .rotate({ x: 0, y: 0, z: 1, angle: this.angle })
        .scale({ x: this.imageSize, y: this.imageSize })

      this.DescribeText('速度：', this.speed)

      Slider({
        value: this.speed,
        min: 1,
        max: 10,
        step: 1,
        style: SliderStyle.OutSet
      })
        .showTips(true)
        .blockColor(Color.Blue)
        .onChange((value: number, mode: SliderChangeMode) => {
        this.speed = value
        console.log("value:" + value);
        clearInterval(this.interval)
        this.speedChange()
      })

      this.DescribeText('缩放比例：', this.imageSize)

      Slider({
        value: this.imageSize,
        min: 0.5,
        max: 2.5,
        step: 0.1,
        style: SliderStyle.OutSet
      })
        .showTips(true)
        .blockColor(Color.Blue)
        .onChange((value: number, mode: SliderChangeMode) => {
        this.imageSize = value
      })
    }
    .margin({ left: 30, right: 30 })
  }

  speedChange() {
    var that = this;
    that.angle = 0;
    this.interval = setInterval(function () {
      that.angle += that.speed
    }, 15)
  }

  onPageShow() {
    clearInterval(this.interval)
    this.speedChange()
  }
}
