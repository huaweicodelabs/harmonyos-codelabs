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
import com.huawei.codecdemo.media.constant.MediaStatu;
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
 * VideoRecorder 视频收录器
 *
 * @since 2021-04-09
 */
public class VideoRecorder {
    private Recorder recorder;
    private EventHandler videoRecorderHandler;
    private MediaStatu recordStatu = MediaStatu.IDEL;
    private VideoProperty.Builder videoPropertyBuilder;
    private AudioProperty.Builder audioPropertyBuilder;
    private StorageProperty.Builder storagePropertyBuilder;

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
        storagePropertyBuilder.setRecorderPath(builder.savefilePath);
    }

    /**
     * 初始化录像器
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
     * 录像器准备
     */
    public void prepare() {
        if (recordStatu == MediaStatu.IDEL) {
            if (recorder.prepare()) {
                recordStatu = MediaStatu.PREPARED;
            }
        }
    }

    /**
     * 录像器启动
     */
    public void start() {
        if (recordStatu != MediaStatu.IDEL && recordStatu != MediaStatu.START) {
            videoRecorderHandler.postTask(() -> {
                if (recorder.start()) {
                    recordStatu = MediaStatu.START;
                }
            });
        }
    }

    /**
     * pause
     */
    public void pause() {
        if (recordStatu == MediaStatu.START) {
            if (recorder.pause()) {
                recordStatu = MediaStatu.PAUSE;
            }
        }
    }

    /**
     * resume
     */
    public void resume() {
        if (recordStatu == MediaStatu.PAUSE) {
            if (recorder.resume()) {
                recordStatu = MediaStatu.START;
            }
        }
    }

    /**
     * stop
     */
    public void stop() {
        if (recordStatu != MediaStatu.IDEL) {
            videoRecorderHandler.postTask(() -> {
                if (recorder.stop()) {
                    recordStatu = MediaStatu.STOP;
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
            recordStatu = MediaStatu.IDEL;
        }
        init(builder);
        prepare();
    }

    /**
     * release
     */
    public void release() {
        if (recorder.release()) {
            recordStatu = MediaStatu.IDEL;
        }
    }

    /**
     * 获取视频收录器状态
     *
     * @return MediaStatu MediaStatu
     */
    public MediaStatu getStatu() {
        return recordStatu;
    }

    /**
     * 获取视频收录器surface
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
     * 获取视频收录器监听回调
     *
     * @param listener IRecorderListener
     */
    public void setVideoRecorderListener(Recorder.IRecorderListener listener) {
        if (recorder != null) {
            recorder.registerRecorderListener(listener);
        }
    }

    /**
     * Builder
     *
     * @since 2020-12-04
     */
    public static class Builder {
        private Context context;
        private String savefilePath;
        private Size resolution;
        private int degrees;
        private int fps = MediaConst.RECORDER_FPS;
        private int frameRate = MediaConst.RECORDER_FRAME_RATE;
        private int bitRate = MediaConst.RECORDER_BIT_RATE;

        /**
         * constructor of Builder
         *
         * @param context context
         */
        public Builder(Context context) {
            this.context = context;
            resolution = new Size(ScreenUtils.getScreenWidth(context), ScreenUtils.getScreenHeight(context));
        }

        /**
         * setSaveFilePath of Builder
         *
         * @param filePath filePath
         * @return builder
         */
        public Builder setSaveFilePath(String filePath) {
            this.savefilePath = filePath;
            return this;
        }

        /**
         * setStartMillisecond of Builder
         *
         * @param resolution resolution
         * @return builder
         */
        public Builder setResolution(Size resolution) {
            this.resolution = resolution;
            return this;
        }

        /**
         * setDegree of Builder
         *
         * @param degrees degrees
         * @return Builder
         */
        public Builder setDegrees(int degrees) {
            this.degrees = degrees;
            return this;
        }

        /**
         * setFps of Builder
         *
         * @param fps fps
         * @return Builder
         */
        public Builder setFps(int fps) {
            this.fps = fps;
            return this;
        }

        /**
         * setFrameRate of Builder
         *
         * @param frameRate frameRate
         * @return Builder
         */
        public Builder setFrameRate(int frameRate) {
            this.frameRate = frameRate;
            return this;
        }

        /**
         * setBitRate of Builder
         *
         * @param bitRate bitRate
         * @return Builder
         */
        public Builder setBitRate(int bitRate) {
            this.bitRate = bitRate;
            return this;
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
