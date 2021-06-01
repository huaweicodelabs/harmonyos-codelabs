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

package ohos.codelabs.video.player.factory;

import ohos.app.Context;
import ohos.codelabs.video.util.LogUtil;
import ohos.global.resource.RawFileDescriptor;
import ohos.media.common.Source;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * SourceFactory
 *
 * @since 2021-04-04
 *
 */
public class SourceFactory {
    private static final String TAG = "SourceFactory";
    private static final String NET_HTTP_MATCH = "http";
    private static final String NET_RTMP_MATCH = "rtmp";
    private static final String NET_RTSP_MATCH = "rtsp";
    private static final String STORAGE_MATCH = "/storage/";

    private Source mPlayerSource;

    /**
     * constructor of SourceFactory
     *
     * @param context context
     * @param path path
     */
    public SourceFactory(Context context, String path) {
        try {
            initSourceType(context, path);
        } catch (IOException e) {
            LogUtil.error(TAG, "Audio resource is unavailable: ");
        }
    }

    private void initSourceType(Context context, String path) throws IOException {
        if (context == null || path == null) {
            return;
        }
        if (path.substring(0, NET_HTTP_MATCH.length()).equalsIgnoreCase(NET_HTTP_MATCH)
                || path.substring(0, NET_RTMP_MATCH.length()).equalsIgnoreCase(NET_RTMP_MATCH)
                || path.substring(0, NET_RTSP_MATCH.length()).equalsIgnoreCase(NET_RTSP_MATCH)) {
            mPlayerSource = new Source(path);
        } else if (path.startsWith(STORAGE_MATCH)) {
            File file = new File(path);
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                FileDescriptor fileDescriptor = fileInputStream.getFD();
                mPlayerSource = new Source(fileDescriptor);
            }
        } else {
            RawFileDescriptor fd = context.getResourceManager().getRawFileEntry(path).openRawFileDescriptor();
            mPlayerSource = new Source(fd.getFileDescriptor(), fd.getStartPosition(), fd.getFileSize());
        }
    }

    /**
     * getSource
     *
     * @return Source Source
     */
    public Source getSource() {
        return mPlayerSource;
    }
}
