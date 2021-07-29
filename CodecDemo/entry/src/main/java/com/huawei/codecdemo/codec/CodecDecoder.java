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

package com.huawei.codecdemo.codec;

import com.huawei.codecdemo.codec.api.CodecListener;
import com.huawei.codecdemo.utils.LogUtil;

import ohos.agp.graphics.Surface;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.codec.Codec;
import ohos.media.common.BufferInfo;
import ohos.media.common.Format;
import ohos.media.muxer.Muxer;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * CodecDecoder
 *
 * @since 2021-04-09
 */
public class CodecDecoder {
    private static final String TAG = CodecDecoder.class.getSimpleName();
    private static final long NUMBER_LONG_100 = 100L;
    private static final int WRITE_BUFFER = 1;
    private static final int STOP_CODEC = 2;
    private Codec decoder;
    private EventHandler decoderHandler;
    private CodecListener callback;
    private Muxer muxer;
    private int trackIndex;
    private boolean isOpen;
    private boolean isSaveFile;
    private File decodeFile;

    private CodecDecoder(Builder builder) {
        initDecoder(builder.format, builder.surface);
        if (builder.filePath != null) {
            isSaveFile = true;
            initMuxer(builder.format, builder.filePath);
        }
        decoderHandler = new MyEventHandler(EventRunner.create(CodecDecoder.class.getSimpleName()));
    }

    /**
     * MyEventHandler
     *
     * @since 2021-04-09
     */
    private class MyEventHandler extends EventHandler {
        MyEventHandler(EventRunner eventRunner) {
            super(eventRunner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            switch (event.eventId) {
                case WRITE_BUFFER:
                    LogUtil.info(TAG, "begin decode");
                    if (event.object instanceof byte[]) {
                        byte[] bytes = (byte[]) (event.object);
                        int size = (int) event.param;
                        ByteBuffer byteBuffer = decoder.getAvailableBuffer(NUMBER_LONG_100);
                        if (byteBuffer != null) {
                            byteBuffer.clear();
                            byteBuffer.put(bytes);
                            BufferInfo bufferInfo = new BufferInfo();
                            bufferInfo.offset = 0;
                            bufferInfo.size = size;
                            boolean isSuccess = decoder.writeBuffer(byteBuffer, bufferInfo);
                            LogUtil.info(TAG, "end decode,result is :" + isSuccess);
                        }
                    }
                    break;
                case STOP_CODEC:
                    LogUtil.info(TAG, "stop decode");
                    if (muxer != null) {
                        muxer.stop();
                        muxer.release();
                        muxer = null;
                    }
                    if (decoder.stop() && decoder.release()) {
                        isOpen = false;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void initDecoder(Format fmt, Surface surface) {
        LogUtil.info(TAG, " init decoder");
        decoder = Codec.createDecoder();
        decoder.setVideoSurface(surface);
        decoder.setCodecFormat(fmt);
        decoder.registerCodecListener(
                new Codec.ICodecListener() {
                    @Override
                    public void onReadBuffer(ByteBuffer byteBuffer, BufferInfo bufferInfo, int tag) {
                        LogUtil.info(TAG, "decode onReadBuffer is called ");
                        if (callback != null) {
                            callback.onGetcodecBuffer(byteBuffer, bufferInfo);
                        }
                        if (isSaveFile) {
                            boolean isSuccess = muxer.writeBuffer(trackIndex, byteBuffer, bufferInfo);
                            LogUtil.info(TAG, " 文件decodec写入,result is :" + isSuccess);
                        }
                    }

                    @Override
                    public void onError(int tag, int tag1, int tag2) {
                        LogUtil.info(TAG, "decode onError is called");
                    }
                });
    }

    private void initMuxer(Format fmt, String filePath) {
        decodeFile = new File(filePath);
        muxer = new Muxer(decodeFile.getPath(), Muxer.MediaFileFormat.FORMAT_MPEG4);
        trackIndex = muxer.appendTrack(fmt);
        muxer.start();
    }

    /**
     * 设置解码监听回调
     *
     * @param listener listener
     */
    public void setDecodeListener(CodecListener listener) {
        callback = listener;
    }

    /**
     * 启动解码器
     */
    public void openDecoder() {
        if (!isOpen) {
            if (decoder.start()) {
                isOpen = true;
            }
        }
    }

    /**
     * 开始解码
     *
     * @param buffer buffer
     */
    public void startDecode(byte[] buffer) {
        if (isOpen) {
            InnerEvent innerEvent = InnerEvent.get(WRITE_BUFFER, buffer.length, buffer);
            decoderHandler.sendEvent(innerEvent, 0, EventHandler.Priority.IMMEDIATE);
        }
    }

    /**
     * 停止解码
     */
    public void stopDecode() {
        if (isOpen) {
            decoderHandler.sendEvent(STOP_CODEC);
        }
    }

    /**
     * 解码器是否启动
     *
     * @return isOpen
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Builder
     *
     * @since 2020-03-16
     */
    public static class Builder {
        private Format format;
        private String filePath;
        private Surface surface;

        /**
         * constructor of Builder
         */
        public Builder() {
        }

        /**
         * setSurface of Builder
         *
         * @param surface surface
         * @return builder
         */
        public Builder setSurface(Surface surface) {
            this.surface = surface;
            return this;
        }

        /**
         * setFormat of Builder
         *
         * @param format format
         * @return builder
         */
        public Builder setFormat(Format format) {
            this.format = format;
            return this;
        }

        /**
         * setSaveFilePath of Builder
         *
         * @param path path
         * @return builder
         */
        public Builder setSaveFilePath(String path) {
            this.filePath = path;
            return this;
        }

        /**
         * create of create
         *
         * @return CodecDecoder
         */
        public CodecDecoder create() {
            return new CodecDecoder(this);
        }
    }
}
