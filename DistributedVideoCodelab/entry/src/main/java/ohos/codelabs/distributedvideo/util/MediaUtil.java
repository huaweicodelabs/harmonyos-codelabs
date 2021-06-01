/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License,Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ohos.codelabs.distributedvideo.util;

import ohos.codelabs.distributedvideo.ResourceTable;
import ohos.codelabs.distributedvideo.data.AdvertisementMo;
import ohos.codelabs.distributedvideo.data.VideoListMo;

import java.util.ArrayList;
import java.util.List;

/**
 * Media util
 *
 * @since 2020-12-04
 */
public class MediaUtil {
    private static List<AdvertisementMo> videoAdvertisementMos = new ArrayList<>(0);
    private static List<VideoListMo> playListMos = new ArrayList<>(0);

    private MediaUtil() {
    }

    static {
        videoAdvertisementMos.add(new AdvertisementMo(ResourceTable.Media_video_ad0, "green"));
        videoAdvertisementMos.add(new AdvertisementMo(ResourceTable.Media_video_ad2, "yellow"));
        videoAdvertisementMos.add(new AdvertisementMo(ResourceTable.Media_video_ad3, "red"));
        playListMos.add(new VideoListMo(ResourceTable.Media_video_list0,
                "landscape", "waterfall"));
        playListMos.add(new VideoListMo(ResourceTable.Media_video_list2, "child", "cute"));
        playListMos.add(new VideoListMo(ResourceTable.Media_video_list1, "show", "drone"));
    }

    /**
     * get the advertisement constructor
     *
     * @return advertisementMos
     */
    public static List<AdvertisementMo> getVideoAdvertisementInfo() {
        return videoAdvertisementMos;
    }

    /**
     * get VideoListMos
     *
     * @return VideoListMo
     */
    public static List<VideoListMo> getPlayListMos() {
        return playListMos;
    }
}
