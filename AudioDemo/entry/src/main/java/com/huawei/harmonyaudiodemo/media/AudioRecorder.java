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

import com.huawei.harmonyaudiodemo.constant.Const;
import com.huawei.harmonyaudiodemo.media.api.AudioRecordListener;
import com.huawei.harmonyaudiodemo.media.constant.MediaConst;
import com.huawei.harmonyaudiodemo.util.LogUtil;

import ohos.agp.utils.TextTool;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.audio.AudioCapturer;
import ohos.media.audio.AudioCapturerInfo;
import ohos.media.audio.AudioStreamInfo;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Hm AudioRecorder
 *
 * @since 2021-04-04
 */
public class AudioRecorder {
    private static final String TAG = AudioRecorder.class.getSimpleName();
    private static final int HALF_BUFFER = 2;
    private static final int OXFF = 0xff;
    private static final int NUMBER_8 = 8;
    private AudioCapturer audioCapturer;
    private AudioStreamInfo audioStreamInfo;
    private final EventHandler audioRecorderHandler;
    private final Builder builder;
    private final int bufferSize;
    private boolean isRecording;
    private final boolean isSaveFile;

    private AudioRecordListener audioRecordListener;

    private AudioRecorder(Builder builder) {
        this.builder = builder;
        isSaveFile = !TextTool.isNullOrEmpty(builder.savefilePath);
        bufferSize =
                AudioCapturer.getMinBufferSize(
                        builder.inputSampleRate,
                        AudioStreamInfo.getChannelCount(builder.channelMask),
                        builder.encodingFormat.getValue());
        audioRecorderHandler = new EventHandler(EventRunner.create(AudioRecorder.class.getSimpleName()));
        initRecord();
    }

    private void initRecord() {
        if (audioStreamInfo == null) {
            audioStreamInfo = new AudioStreamInfo.Builder()
                    .encodingFormat(builder.encodingFormat)
                    .channelMask(builder.channelMask)
                    .sampleRate(builder.inputSampleRate)
                    .build();
            AudioCapturerInfo audioCapturerInfo = new AudioCapturerInfo.Builder()
                    .audioStreamInfo(audioStreamInfo)
                    .audioInputSource(builder.inputSource)
                    .build();
            audioCapturer = new AudioCapturer(audioCapturerInfo);
        }
    }

    private void startRecord() {
        isRecording = true;
        BufferedOutputStream dos = null;
        try {
            if (isSaveFile) {
                dos = new BufferedOutputStream(new FileOutputStream(builder.savefilePath));
            }
            byte[] buffers = new byte[bufferSize];
            int length = audioCapturer.read(buffers, 0, bufferSize);
            while (length != Const.NUMBER_NEGATIVE_1) {
                if (audioRecordListener != null) {
                    int inputSize = getInputVolume(buffers, length);
                    audioRecordListener.onGetRecordBuffer(buffers, length, inputSize);
                }
                if (dos != null) {
                    dos.write(buffers);
                }
                length = audioCapturer.read(buffers, 0, bufferSize);
            }
        } catch (IOException e) {
            LogUtil.error(TAG, "start record failed!");
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    LogUtil.error(TAG, "start record failed!");
                }
            }
        }
    }

    private int getInputVolume(byte[] buffer, int bufferLength) {
        int mShortArrayLenght = bufferLength / HALF_BUFFER;
        short[] retVals = new short[mShortArrayLenght];
        for (int i = 0; i < mShortArrayLenght; i++) {
            retVals[i] = (short) ((buffer[i * HALF_BUFFER] & OXFF) | (buffer[i * HALF_BUFFER + 1] & OXFF) << NUMBER_8);
        }
        int max = 0;
        if (bufferLength > 0) {
            for (int i = 0; i < mShortArrayLenght; i++) {
                if (Math.abs(retVals[i]) > max) {
                    max = Math.abs(retVals[i]);
                }
            }
        }
        return max;
    }

    /**
     * record
     */
    public void record() {
        if (!isRecording) {
            initRecord();
            audioRecorderHandler.postTask(() -> {
                if (audioCapturer.start()) {
                    startRecord();
                }
            });
        }
    }

    /**
     * stopRecord
     */
    public void stopRecord() {
        if (isRecording && audioCapturer.stop()) {
            isRecording = false;
        }
    }

    /**
     * setAudioRecordListener
     *
     * @param listener listener
     */
    public void setAudioRecordListener(AudioRecordListener listener) {
        audioRecordListener = listener;
    }

    /**
     * release recorder
     */
    public void release() {
        stopRecord();
        if (audioCapturer != null) {
            audioCapturer.release();
        }
        audioStreamInfo = null;
    }

    /**
     * is recording in current time
     *
     * @return isRecording
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * Builder
     *
     * @since 2020-12-04
     */
    public static class Builder {
        private String savefilePath;
        private final AudioStreamInfo.EncodingFormat encodingFormat;
        private final AudioStreamInfo.ChannelMask channelMask;
        private final AudioCapturerInfo.AudioInputSource inputSource;
        private final int inputSampleRate;

        /**
         * constructor of Builder
         */
        public Builder() {
            encodingFormat = AudioStreamInfo.EncodingFormat.ENCODING_PCM_16BIT; // 16-bit PCM
            channelMask = AudioStreamInfo.ChannelMask.CHANNEL_IN_STEREO; // 双声道
            inputSource = AudioCapturerInfo.AudioInputSource.AUDIO_INPUT_SOURCE_VOICE_COMMUNICATION; // 消除回声，降噪
            inputSampleRate = MediaConst.SAMPLE_RATE;
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
         * create of Builder
         *
         * @return AudioRecorder
         */
        public AudioRecorder create() {
            return new AudioRecorder(this);
        }
    }
}
