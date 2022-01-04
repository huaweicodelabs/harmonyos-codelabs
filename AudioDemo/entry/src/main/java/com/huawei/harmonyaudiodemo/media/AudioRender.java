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

package com.huawei.harmonyaudiodemo.media;

import com.huawei.harmonyaudiodemo.media.api.AudioPlayListener;
import com.huawei.harmonyaudiodemo.media.constant.MediaConst;
import com.huawei.harmonyaudiodemo.media.constant.MediaStatu;

import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.audio.AudioCapturer;
import ohos.media.audio.AudioRenderer;
import ohos.media.audio.AudioRendererInfo;
import ohos.media.audio.AudioStreamInfo;

/**
 * AudioRender
 *
 * @since 2021-04-09
 */
public class AudioRender {
    private static final String TAG = AudioRender.class.getSimpleName();
    private AudioRendererInfo audioRenderInfo;
    private AudioRenderer audioRender;
    private final EventHandler audioRenderHandler;
    private MediaStatu audioRenderStatu = MediaStatu.IDEL;
    private final Builder builder;
    private final int bufferSize;
    private AudioPlayListener audioPlayListener;

    private AudioRender(Builder builder) {
        this.builder = builder;
        bufferSize =
                AudioCapturer.getMinBufferSize(
                        builder.inputSampleRate,
                        AudioStreamInfo.getChannelCount(builder.channelMask),
                        builder.encodingFormat.getValue());
        audioRenderHandler = new EventHandler(EventRunner.create(AudioRender.class.getSimpleName()));
        initRender();
    }

    private void initRender() {
        AudioStreamInfo asi = new AudioStreamInfo.Builder()
                .encodingFormat(builder.encodingFormat)
                .channelMask(builder.channelMask)
                .sampleRate(builder.inputSampleRate)
                .audioStreamFlag(builder.streamFlag)
                .streamUsage(builder.streamUsage)
                .build();
        audioRenderInfo = new AudioRendererInfo.Builder()
                .audioStreamInfo(asi)
                .audioStreamOutputFlag(builder.streamOutputFlag)
                .bufferSizeInBytes(bufferSize)
                .isOffload(builder.isOneOffLoad) // false表示分段传输buffer并播放，true表示整个音频流一次性传输到HAL层播放
                .build();
        audioRender = new AudioRenderer(audioRenderInfo, AudioRenderer.PlayMode.MODE_STREAM);
        audioRender.setFrameIntervalObserver(() -> {
            if (audioRender.getAudioTime().getFramePosition() != 0) {
                if (audioPlayListener != null) {
                    audioPlayListener.onComplete();
                }
                release();
            }
        }, MediaConst.READ_RENDER_INTERVAL, audioRenderHandler);
    }

    /**
     * start
     */
    public void start() {
        if (audioRenderStatu == MediaStatu.STOP || audioRenderStatu == MediaStatu.IDEL) {
            if (audioRender != null) {
                if (audioRender.start()) {
                    audioRenderStatu = MediaStatu.START;
                }
            }
        }
    }

    /**
     * pause
     */
    public void pause() {
        if (audioRender != null) {
            if (audioRender.pause()) {
                audioRenderStatu = MediaStatu.PAUSE;
            }
        }
    }

    /**
     * setSpeed
     *
     * @param speed speed
     */
    public void setSpeed(float speed) {
        if (audioRender != null) {
            audioRender.setSpeed(speed);
        }
    }

    /**
     * setVolume
     *
     * @param volume volume
     */
    public void setVolume(float volume) {
        if (audioRender != null) {
            audioRender.setVolume(volume);
        }
    }

    /**
     * stop
     */
    public void stop() {
        if (audioRender != null) {
            if (audioRender.stop()) {
                audioRenderStatu = MediaStatu.STOP;
                if (audioPlayListener != null) {
                    audioPlayListener.onComplete();
                }
            }
        }
    }

    /**
     * playRecord
     *
     * @param bytes bytes
     * @param length length
     */
    public void play(byte[] bytes, int length) {
        if (audioRenderInfo == null) {
            initRender();
        }
        start();
        audioRenderHandler.postTask(() -> {
            byte[] datas = new byte[length];
            System.arraycopy(bytes, 0, datas, 0, datas.length);
            audioRender.write(datas, 0, datas.length);
        });
    }

    /**
     * release recorder
     */
    public void release() {
        if (audioRender != null) {
            if (audioRender.release()) {
                audioRenderStatu = MediaStatu.IDEL;
                audioRenderInfo = null;
            }
        }
    }

    /**
     * release recorder
     *
     * @param listener listener
     */
    public void setPlayListener(AudioPlayListener listener) {
        audioPlayListener = listener;
    }

    /**
     * Builder
     *
     * @since 2020-12-04
     */
    public static class Builder {
        private boolean isOneOffLoad;
        private final AudioStreamInfo.EncodingFormat encodingFormat;
        private final AudioStreamInfo.ChannelMask channelMask;
        private final int inputSampleRate;
        private final AudioStreamInfo.AudioStreamFlag streamFlag;
        private final AudioStreamInfo.StreamUsage streamUsage;
        private final AudioRendererInfo.AudioStreamOutputFlag streamOutputFlag;

        /**
         * constructor of Builder
         */
        public Builder() {
            isOneOffLoad = true;
            encodingFormat = AudioStreamInfo.EncodingFormat.ENCODING_PCM_16BIT; // 16-bit PCM
            channelMask = AudioStreamInfo.ChannelMask.CHANNEL_OUT_STEREO; // 双声道
            inputSampleRate = MediaConst.SAMPLE_RATE;
            streamFlag = AudioStreamInfo.AudioStreamFlag.AUDIO_STREAM_FLAG_MAY_DUCK; // 混音
            streamUsage = AudioStreamInfo.StreamUsage.STREAM_USAGE_MEDIA; // 媒体类音频
            streamOutputFlag = AudioRendererInfo.AudioStreamOutputFlag.AUDIO_STREAM_OUTPUT_FLAG_DIRECT_PCM;
        }

        /**
         * setOneOffLoad of Builder
         *
         * @param isOneOffLoadDatas isOneOffLoad
         * @return builder
         */
        public Builder setOneOffLoad(boolean isOneOffLoadDatas) {
            this.isOneOffLoad = isOneOffLoadDatas;
            return this;
        }

        /**
         * create of Builder
         *
         * @return AudioRender
         */
        public AudioRender create() {
            return new AudioRender(this);
        }
    }
}
