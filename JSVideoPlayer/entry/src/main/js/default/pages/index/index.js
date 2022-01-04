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

import router from '@system.router';

const LOCAL_SOURCE = '/common/test_video.mp4';
const WEB_SOURCE = 'https://ss0.bdstatic.com/-0U0bnSm1A5BphGlnYG/cae-legoup-video-target/93be3d88-9fc2-4fbd-bd14-833bca731ca7.mp4';

export default {
  data: {
    swiperList: [{
      image: '/common/images/video_ad0.jpg',
      source: LOCAL_SOURCE
    }, {
      image: '/common/images/video_ad1.jpg',
      source: WEB_SOURCE
    }, {
      image: '/common/images/video_ad2.jpg',
      source: LOCAL_SOURCE
    }],
    videoList: [{
      image: '/common/images/video_list0.jpg',
      source: WEB_SOURCE
    }, {
      image: '/common/images/video_list1.jpg',
      source: LOCAL_SOURCE
    }, {
      image: '/common/images/video_list2.jpg',
      source: WEB_SOURCE
    }]
  },
  playVideo(e) {
    router.push({
      uri: 'pages/videopage/videopage',
      params: { source: e.target.dataSet.url }
    });
  }
};

