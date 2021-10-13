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

package com.huawei.codecdemo.media;

import com.huawei.codecdemo.media.constant.MediaConst;
import com.huawei.codecdemo.media.constant.MediaStatus;
import com.huawei.codecdemo.utils.ScreenUtils;

import ohos.agp.graphics.Surface;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.common.AudioProperty;
import ohos.media.common.Source;
import ohos.media.common.StorageProperty;
import ohos.media.common.VideoProperty;
import ohos.media.image.common.Size;
import ohos.media.recorder.Recorder;

import java.util.Optional;

/**
 * VideoRecorder
 *
 * @since 2021-04-09
 */
public class VideoRecorder {
    private final Recorder recorder;
    private final EventHandler videoRecorderHandler;
    private MediaStatus recordStatus = MediaStatus.IDEL;
    private final VideoProperty.Builder videoPropertyBuilder;
    private final AudioProperty.Builder audioPropertyBuilder;
    private final StorageProperty.Builder storagePropertyBuilder;

    private VideoRecorder(Builder builder) {
        videoRecorderHandler = new EventHandler(EventRunner.create(VideoRecorder.class.getSimpleName()));
        videoPropertyBuilder = new VideoProperty.Builder();
        audioPropertyBuilder = new AudioProperty.Builder();
        storagePropertyBuilder = new StorageProperty.Builder();
        recorder = new Recorder();
        init(builder);
    }

    private void init(Builder builder) {
        initVideoProperty(builder);
        initAudioProperty();
        initStorageProperty(builder);
        initRecorder();
    }

    private void initVideoProperty(Builder builder) {
        videoPropertyBuilder.setRecorderDegrees(builder.degrees);
        videoPropertyBuilder.setRecorderFps(builder.fps);
        videoPropertyBuilder.setRecorderHeight(Math.min(builder.resolution.width, builder.resolution.height));
        videoPropertyBuilder.setRecorderWidth(Math.max(builder.resolution.width, builder.resolution.height));
        videoPropertyBuilder.setRecorderVideoEncoder(Recorder.VideoEncoder.H264);
        videoPropertyBuilder.setRecorderRate(builder.frameRate);
        videoPropertyBuilder.setRecorderBitRate(builder.bitRate);
    }

    private void initAudioProperty() {
        audioPropertyBuilder.setRecorderAudioEncoder(Recorder.AudioEncoder.AAC);
    }

    private void initStorageProperty(Builder builder) {
        storagePropertyBuilder.setRecorderPath(builder.saveFilePath);
    }

    /**
     * initRecorder
     */
    private void initRecorder() {
        Source source = new Source();
        source.setRecorderAudioSource(Recorder.AudioSource.MIC);
        source.setRecorderVideoSource(Recorder.VideoSource.SURFACE);
        recorder.setSource(source);
        recorder.setOutputFormat(Recorder.OutputFormat.MPEG_4);
        recorder.setStorageProperty(storagePropertyBuilder.build());
        recorder.setAudioProperty(audioPropertyBuilder.build());
        recorder.setVideoProperty(videoPropertyBuilder.build());
    }

    /**
     * prepare
     */
    public void prepare() {
        if (recordStatus == MediaStatus.IDEL) {
            if (recorder.prepare()) {
                recordStatus = MediaStatus.PREPARED;
            }
        }
    }

    /**
     * start
     */
    public void start() {
        if (recordStatus != MediaStatus.IDEL && recordStatus != MediaStatus.START) {
            videoRecorderHandler.postTask(() -> {
                if (recorder.start()) {
                    recordStatus = MediaStatus.START;
                }
            });
        }
    }

    /**
     * stop
     */
    public void stop() {
        if (recordStatus != MediaStatus.IDEL) {
            videoRecorderHandler.postTask(() -> {
                if (recorder.stop()) {
                    recordStatus = MediaStatus.STOP;
                }
            });
        }
    }

    /**
     * reset
     *
     * @param builder builder
     */
    public void reset(Builder builder) {
        if (recorder.reset()) {
            recordStatus = MediaStatus.IDEL;
        }
        init(builder);
        prepare();
    }

    /**
     * release
     */
    public void release() {
        if (recorder.release()) {
            recordStatus = MediaStatus.IDEL;
        }
    }

    /**
     * getStatus
     *
     * @return MediaStatus
     */
    public MediaStatus getStatus() {
        return recordStatus;
    }

    /**
     * getRecordSurface
     *
     * @return Surface
     */
    public Optional<Surface> getRecordSurface() {
        if (recorder != null) {
            return Optional.of(recorder.getVideoSurface());
        }
        return Optional.empty();
    }

    /**
     * Builder
     *
     * @since 2020-12-04
     */
    public static class Builder {
        private String saveFilePath;
        private Size resolution;
        private int degrees;
        private final int fps = MediaConst.RECORDER_FPS;
        private final int frameRate = MediaConst.RECORDER_FRAME_RATE;
        private final int bitRate = MediaConst.RECORDER_BIT_RATE;

        /**
         * constructor of Builder
         *
         * @param context context
         */
        public Builder(Context context) {
            resolution = new Size(ScreenUtils.getScreenWidth(context), ScreenUtils.getScreenHeight(context));
        }

        /**
         * setSaveFilePath of Builder
         *
         * @param filePath filePath
         */
        public void setSaveFilePath(String filePath) {
            this.saveFilePath = filePath;
        }

        /**
         * setStartMillisecond of Builder
         *
         * @param resolution resolution
         */
        public void setResolution(Size resolution) {
            this.resolution = resolution;
        }

        /**
         * setDegree of Builder
         *
         * @param degrees degrees
         */
        public void setDegrees(int degrees) {
            this.degrees = degrees;
        }

        /**
         * create of Builder
         *
         * @return VideoRecorder
         */
        public VideoRecorder create() {
            return new VideoRecorder(this);
        }
    }
}
