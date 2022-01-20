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

import {NewsData} from '../model/NewsData'
import {startRemoteAbilities} from '../model/Utils'

/**
 * 设备列表弹窗组件
 */
@CustomDialog
@Component
export struct DeviceListDialog {
  // 获取新闻条目数据
  @Consume newsData: NewsData;
  // 获取组网设备列表数据
  @Consume deviceList: any[]
  // 被选择的设备id
  private selectedDevices: any[]= [];
  // 用于控制弹窗的显示和关闭
  controller: CustomDialogController;

  build() {
    Column() {
      Text("选择设备")
        .fontWeight(FontWeight.Bold)
        .fontSize(20)
        .margin({ top: 20, bottom: 10 })

      List() {
        ForEach(this.deviceList, item => {
          ListItem() {
            Row() {
              Text(item.deviceName)
                .fontSize(18)
                .layoutWeight(1)

              Toggle({ type: ToggleType.Checkbox })
                .onChange((isOn: boolean) => {
                  if (isOn) {
                    // 添加被选择的设备deviceId
                    this.selectedDevices.push(item.deviceId)
                  } else {
                    // 移除未被选择的设备deviceId
                    for (var i = 0; i < this.selectedDevices.length; i++) {
                      if (this.selectedDevices[i] === item.deviceId) {
                        this.selectedDevices.splice(i, 1);
                      }
                    }
                  }
                })
                .width(20)
                .height(20)
            }
            .padding({ left: 30, right: 30 })
          }
        }, item => item.deviceId.toString())
      }
      .height("30%")
      .align(Alignment.TopStart)

      Row() {
        Button("取消")
          .layoutWeight(1)
          .backgroundColor(Color.White)
          .fontColor(Color.Blue)
          .height(60)
          .onClick(() => {
            // 关闭弹窗
            this.controller.close();
          })

        Button("确定")
          .layoutWeight(1)
          .backgroundColor(Color.White)
          .fontColor(Color.Blue)
          .height(60)
          .onClick(() => {
            // 关闭弹窗
            this.controller.close();
            startRemoteAbilities(this.selectedDevices, this.newsData.newsId)
          })
      }
    }
  }
}
