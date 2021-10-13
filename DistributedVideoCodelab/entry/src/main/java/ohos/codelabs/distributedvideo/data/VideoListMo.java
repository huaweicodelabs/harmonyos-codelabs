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

package ohos.codelabs.distributedvideo.data;

/**
 * VideoList object
 *
 * @since 2021-09-07
 *
 */
public class VideoListMo {
    private final int sourceId;

    private final String name;

    private final String description;

    /**
     * VideoListMo
     *
     * @param sourceId sourceId
     * @param name name
     * @param description description
     */
    public VideoListMo(int sourceId, String name, String description) {
        this.sourceId = sourceId;
        this.name = name;
        this.description = description;
    }

    public int getSourceId() {
        return sourceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
