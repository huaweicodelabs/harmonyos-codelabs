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

export class NewsData {
  newsId: string;
  title: string;
  newsType: string;
  imgUrl: Resource;
  reads: string;
  likes: string;
  content: string;

  constructor(newsId: string, title: string, newsType: string, imgUrl: Resource, reads: string, likes: string, content: string) {
    this.newsId = newsId;
    this.title = title;
    this.newsType = newsType;
    this.imgUrl = imgUrl;
    this.reads = reads;
    this.likes = likes;
    this.content = content;
  }
}