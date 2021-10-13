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

import com.huawei.codecdemo.utils.LogUtil;

import ohos.agp.graphics.Surface;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.codec.Codec;
import ohos.media.common.BufferInfo;
import ohos.media.common.Format;
import ohos.media.muxer.Muxer;

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
    private final EventHandler decoderHandler;
    private Muxer muxer;
    private boolean isOpen;

    private CodecDecoder(Builder builder) {
        initDecoder(builder.format, builder.surface);
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
                    }

                    @Override
                    public void onError(int tag, int tag1, int tag2) {
                        LogUtil.info(TAG, "decode onError is called");
                    }
                });
    }

    /**
     * openDecoder
     */
    public void openDecoder() {
        if (!isOpen) {
            if (decoder.start()) {
                isOpen = true;
            }
        }
    }

    /**
     * startDecode
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
     * stopDecode
     */
    public void stopDecode() {
        if (isOpen) {
            decoderHandler.sendEvent(STOP_CODEC);
        }
    }

    /**
     * Builder
     *
     * @since 2020-03-16
     */
    public static class Builder {
        private Format format;
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
         * create of create
         *
         * @return CodecDecoder
         */
        public CodecDecoder create() {
            return new CodecDecoder(this);
        }
    }
}
