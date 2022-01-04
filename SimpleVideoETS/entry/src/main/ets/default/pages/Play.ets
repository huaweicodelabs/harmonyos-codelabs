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
struct Play {
  // 取到Index页面跳转来时携带的source对应的数据。
  private source: string = router.getParams().source

  build() {
    Column() {
      Video({
        src: this.source,
      })
        .width('100%')
        .height('100%')
        .autoPlay(true)
        .controls(true)
    }
  }
}

