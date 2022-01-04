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
import mediaquery from '@ohos.mediaquery'

import {NewsData} from '../model/NewsData'
import {getNewsDatas} from '../model/NewsDataModel'
import {DetailComponent} from '../Component/DetailComponent'

let portraitFunc = null

@Entry
@Component
struct NewsMain {
  @Provide isLandscape: boolean = false
  private newsItems: NewsData[] = getNewsDatas()
  // 横屏时，详情页默认展示第一条数据
  @Provide newsData: NewsData= this.newsItems[0]
  // 监听设备状态：横屏并且宽度大于1500
  orientationListener = mediaquery.matchMediaSync('screen and (1500 < width) and (orientation: landscape)')

  build() {
    Row() {
      Column() {
        NewsTypeComponent({ newsItems: this.newsItems })
      }
      .layoutWeight(2)

      if (this.isLandscape) {
        Column() {
          DetailComponent()
        }
        .layoutWeight(3)
      }
    }
  }

  onPortrait(mediaQueryResult) {
    console.log("isLandscape:" + mediaQueryResult.matches);
    this.isLandscape = mediaQueryResult.matches;
  }

  aboutToAppear() {
    portraitFunc = this.onPortrait.bind(this)
    this.orientationListener.on('change', portraitFunc)
  }
}

/**
 * 主页顶部类型新闻组件
 */
@Component
struct NewsTypeComponent {
  private newsItems: NewsData[]

  build() {
    Tabs() {
      TabContent() {
        NewsListPage({ newsItems: this.newsItems })
      }.tabBar('All')

      TabContent() {
        NewsListPage({ newsItems: this.newsItems.filter(item => (item.newsType == 'Health')) })
      }.tabBar('Health')

      TabContent() {
        NewsListPage({ newsItems: this.newsItems.filter(item => (item.newsType == 'Finance')) })
      }.tabBar('Finance')

      TabContent() {
        NewsListPage({ newsItems: this.newsItems.filter(item => (item.newsType == 'Technology')) })
      }.tabBar('Technology')

      TabContent() {
        NewsListPage({ newsItems: this.newsItems.filter(item => (item.newsType == 'Sport')) })
      }.tabBar('Sport')

      TabContent() {
        NewsListPage({ newsItems: this.newsItems.filter(item => (item.newsType == 'Internet')) })
      }.tabBar('Internet')

      TabContent() {
        NewsListPage({ newsItems: this.newsItems.filter(item => (item.newsType == 'Game')) })
      }.tabBar('Game')
    }
    .barWidth(310)
    .barHeight(50)
    .barMode(BarMode.Scrollable)
  }
}


/**
 * 主页新闻列表组件
 */
@Component
struct NewsListPage {
  private newsItems: NewsData[]

  build() {
    Column() {
      List() {
        ForEach(this.newsItems, item => {
          ListItem() {
            NewsListItem({ newsItem: item })
          }
        }, item => item.newsId.toString())
      }
    }
  }
}

/**
 * 新闻列表条目组件
 */
@Component
struct NewsListItem {
  private newsItem: NewsData
  @Consume isLandscape: boolean;
  @Consume newsData: NewsData;

  build() {
    Column() {
      Row() {
        Text(this.newsItem.title)
          .fontSize(17)
          .width("55%")
          .height(80)
          .maxLines(4)
          .margin(10)

        Image(this.newsItem.imgUrl).width("35%").height(80).margin(10)
      }

      Divider()
        .vertical(false)
        .color("#dbd8db")
        .strokeWidth(1)
    }
    .height(101)
    .width("100%")
    .onClick(() => {
      if (this.isLandscape) {
        // 如果为横屏则切换右侧详情页数据
        this.newsData = this.newsItem
      } else {
        // 如果为竖屏则跳转到详情页
        router.push(
          {
            // 跳转到指定页面
            uri: 'pages/NewsDetail',
            params: {
              // 跳转传递的参数
              newsItem: this.newsItem
            }
          })
      }
    })
  }
}


