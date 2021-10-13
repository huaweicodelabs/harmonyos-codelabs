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
 * @since 2021-09-07
 */
public class MediaUtil {
    private static final List<AdvertisementMo> ADVERTISEMENT_MOS = new ArrayList<>(0);
    private static final List<VideoListMo> VIDEO_LIST_MO_LIST = new ArrayList<>(0);

    private MediaUtil() {
    }

    static {
        ADVERTISEMENT_MOS.add(new AdvertisementMo(ResourceTable.Media_video_ad0, "green"));
        ADVERTISEMENT_MOS.add(new AdvertisementMo(ResourceTable.Media_video_ad2, "yellow"));
        ADVERTISEMENT_MOS.add(new AdvertisementMo(ResourceTable.Media_video_ad3, "red"));
        VIDEO_LIST_MO_LIST.add(new VideoListMo(ResourceTable.Media_video_list0,
                "landscape", "waterfall"));
        VIDEO_LIST_MO_LIST.add(new VideoListMo(ResourceTable.Media_video_list2, "child", "cute"));
        VIDEO_LIST_MO_LIST.add(new VideoListMo(ResourceTable.Media_video_list1, "show", "drone"));
    }

    /**
     * get the advertisement constructor
     *
     * @return advertisementMos
     */
    public static List<AdvertisementMo> getVideoAdvertisementInfo() {
        return ADVERTISEMENT_MOS;
    }

    /**
     * get VideoListMos
     *
     * @return VideoListMo
     */
    public static List<VideoListMo> getPlayListMos() {
        return VIDEO_LIST_MO_LIST;
    }
}
