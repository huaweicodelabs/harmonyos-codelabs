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
struct CustomTransition {
  build() {
    Stack() {
      Image($rawfile('image2.jpg'))
        .objectFit(ImageFit.Cover)
        .width('100%')
        .height('100%')
    }
    .width('100%')
    .height('100%')
  }

  pageTransition() {

    // 页面入场组件
    PageTransitionEnter({ duration: 600, curve: Curve.Smooth })
      .opacity(0.2) //入场时候透明度从0.2到1
    .scale({ x: 0, y: 0 }) //入场时x、y轴缩放从0变化到1

    // 页面退场组件
    PageTransitionExit({ duration: 600, curve: Curve.Smooth })
      .translate({ x: 500, y: 500 }) //退场时x、y轴的偏移量为500
  }
}