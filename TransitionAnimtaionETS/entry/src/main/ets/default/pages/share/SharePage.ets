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
struct SharePage {
  build() {
    Stack() {
      Image($rawfile('image2.jpg'))
      // 设置共享元素转场属性
      .sharedTransition('imageId', { duration: 1000, curve: Curve.Smooth, delay: 100 })
        .objectFit(ImageFit.Cover)
        .width('100%')
        .height('100%')
    }
    .width('100%')
    .height('100%')
  }
}
