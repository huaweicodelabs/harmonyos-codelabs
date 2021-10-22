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
struct BottomTransition {
  build() {
    Stack() {
      Image($rawfile('image1.jpg'))
        .objectFit(ImageFit.Cover)
        .width('100%')
        .height('100%')
    }
    .width('100%')
    .height('100%')
  }

// 页面转场通过全局transition方法进行配置转场参数
  pageTransition() {

    // 页面入场组件：SlideEffect.Bottom 设置到入场时表示从下边滑入，出场时表示滑出到下边。
    PageTransitionEnter({ duration: 600, curve: Curve.Smooth })
      .slide(SlideEffect.Bottom)

    // 页面退场组件：SlideEffect.Bottom 设置到入场时表示从下边滑入，出场时表示滑出到下边。
    PageTransitionExit({ duration: 600, curve: Curve.Smooth })
      .slide(SlideEffect.Bottom)
  }
}