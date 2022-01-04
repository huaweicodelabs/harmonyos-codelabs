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

import deviceManager from '@ohos.distributedHardware.deviceManager';
import {NewsData} from '../model/NewsData'
import {DeviceListDialog} from '../component/DeviceListDialog'

/**
 * 新闻详情组件
 */
@Component
export struct DetailComponent {
  build() {
    Column() {
      Scroll() {
        NewsInfo()
      }
      .flexGrow(1).width("100%")

      NewsBottom()
    }.height("100%").width("100%")
  }
}

/**
 * 新闻详情(上部)
 */
@Component
struct NewsInfo {
// 新闻详情数据
  @Consume newsData: NewsData;

  build() {
    Column() {
      Column() {
        Text(this.newsData.title)
          .fontSize(25)
          .margin({ right: 20, left: 20, top: 10, bottom: 10 })
        Text("reads:" + this.newsData.reads + "  likes:" + this.newsData.likes)
          .fontSize(16)
          .margin({ right: 20, left: 20, bottom: 10 })
        Image(this.newsData.imgUrl).width("100%").height(180)
          .margin({ left: 20, right: 20, bottom: 15 })
        Text(this.newsData.content).fontSize(18).margin({ left: 20, right: 20 })
      }.alignItems(HorizontalAlign.Start)
    }
  }
}

/**
 * 新闻详情（底部）
 */
@Component
struct NewsBottom {
// 可信设备列表
  @Provide deviceList: any[]= [];
  @Consume newsData: NewsData
// 设备管理对象
  private deviceMag: any= null

  build() {
    Column() {
      Divider()
        .vertical(false)
        .color("#c1bdc1")
        .strokeWidth(1)
        .height(1)
        .width('100%')
        .margin({ bottom: 5 })

      Row() {
        Button("Enter a comment.       ").fontSize(15)
          .margin({ left: 20, right: 35 }).backgroundColor("#dbd8db").fontColor("#4b494b")
        Image($rawfile('icon_message.png')).width(25).height(21).margin({ right: 10 })
        Image($rawfile('icon_star.png')).width(25).height(21).margin({ right: 10 })
        Image($rawfile('icon_good.png')).width(25).height(21).margin({ right: 10 })
        Image($rawfile('icon_share.png')).width(25).height(21).margin({ right: 10 })
          .onClick(() => {
            this.getDeviceList()
          })
      }
      .margin({ bottom: 5 }).height(40).width("100%")
    }
  }

// 创建CustomDialogController对象，控制设备列表弹窗显示和隐藏
  dialogController: CustomDialogController = new CustomDialogController({
    builder: DeviceListDialog(),
    autoCancel: true,
    alignment: DialogAlignment.Bottom
  });

/**
    * 获取设备列表并且打开设备列表弹窗
    */
  getDeviceList() {
    if (this.deviceMag != null) {
      // 获取所有可信设备列表
      this.deviceList = this.deviceMag.getTrustedDeviceListSync();
    } else {
      // 创建设备管理实例
      deviceManager.createDeviceManager("com.huawei.codelab", (err, data) => {
        if (err) {
          console.error("createDeviceManager failed err: " + JSON.stringify(err));
          return;
        }
        console.info('createDeviceManager successful. Data: ' + JSON.stringify(data))
        this.deviceMag = data;
        this.deviceList = this.deviceMag.getTrustedDeviceListSync();
      });
    }
    // 显显示设备列表弹窗
    this.dialogController.open()
  }

/**
   * 释放DeviceManager实例
   */
  aboutToDisappear() {
    this.deviceMag.release();
  }
}


