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

import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.codec.Codec;
import ohos.media.common.BufferInfo;
import ohos.media.common.Format;
import ohos.media.muxer.Muxer;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * CodecEncoder
 *
 * @since 2021-04-09
 */
public class CodecEncoder {
    private static final String TAG = CodecEncoder.class.getSimpleName();
    private static final long NUMBER_LONG_100 = 100L;
    private static final int NUMBER_INT_1000 = 1000;
    private static final int WRITE_BUFFER = 1;
    private static final int STOP_CODEC = 2;
    private boolean isOpen = false;
    private CodecListener codecListener;
    private Codec encoder;
    private final EventHandler encoderHandler;
    private Muxer muxer;
    private final boolean isSaveFile = false;

    private CodecEncoder(Builder builder) {
        initEncoder(builder.format);
        encoderHandler = new MyEventHandler(EventRunner.create(CodecEncoder.class.getSimpleName()));
    }

    /**
     * MyEventHandler
     *
     * @since 2021-04-12
     */
    private class MyEventHandler extends EventHandler {
        MyEventHandler(EventRunner eventRunner) {
            super(eventRunner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            switch (event.eventId) {
                case WRITE_BUFFER:
                    byte[] bytes = (byte[]) (event.object);
                    LogUtil.info(TAG, "start encode");
                    if (isOpen) {
                        ByteBuffer byteBuffer = encoder.getAvailableBuffer(NUMBER_LONG_100);
                        if (byteBuffer != null) {
                            byteBuffer.clear();
                            byteBuffer.put(bytes);
                            BufferInfo info = new BufferInfo();
                            info.size = bytes.length;
                            info.offset = 0;
                            info.timeStamp = System.currentTimeMillis();
                            boolean isSuccess = encoder.writeBuffer(byteBuffer, info);
                            LogUtil.info(TAG, "encode result is " + isSuccess);
                        }
                    }
                    break;
                case STOP_CODEC:
                    LogUtil.info(TAG, "stop encode");
                    if (muxer != null) {
                        muxer.stop();
                        muxer.release();
                        muxer = null;
                    }
                    if (encoder.stop() && encoder.release()) {
                        encoder = null;
                        isOpen = false;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void initEncoder(Format fmt) {
        LogUtil.info(TAG, "init encoder");
        encoder = Codec.createEncoder();
        encoder.setCodecFormat(fmt);
        encoder.registerCodecListener(
                new Codec.ICodecListener() {
                    @Override
                    public void onReadBuffer(ByteBuffer byteBuffer, BufferInfo bufferInfo, int tag) {
                        LogUtil.info(TAG, "encode onReadBuffer is called");
                        if (codecListener != null) {
                            codecListener.onGetcodecBuffer(byteBuffer, bufferInfo);
                        }
                        if (isSaveFile) {
                            bufferInfo.timeStamp = new Date().getTime() * NUMBER_INT_1000;
                            boolean isSuccess = muxer.writeBuffer(0, byteBuffer, bufferInfo);
                            LogUtil.info(TAG, "muxer.writeBuffer " + bufferInfo.size + ", result: " + isSuccess);
                        }
                    }

                    @Override
                    public void onError(int tag, int tag1, int tag2) {
                        LogUtil.info(TAG, "encode onError is called");
                    }
                });
    }

    /**
     * setEncodeListener
     *
     * @param listener listener
     */
    public void setEncodeListener(CodecListener listener) {
        codecListener = listener;
    }

    /**
     * openEncoder
     */
    public void openEncoder() {
        if (!isOpen) {
            if (encoder.start()) {
                isOpen = true;
            }
        }
    }

    /**
     * startEncode
     *
     * @param bytes bytes
     */
    public void startEncode(byte[] bytes) {
        if (isOpen) {
            InnerEvent innerEvent = InnerEvent.get(WRITE_BUFFER, 0, bytes);
            encoderHandler.sendEvent(innerEvent, 0, EventHandler.Priority.IMMEDIATE);
        }
    }

    /**
     * stopEncode
     */
    public void stopEncode() {
        if (isOpen) {
            encoderHandler.sendEvent(STOP_CODEC);
        }
    }

    /**
     * isOpen
     *
     * @return boolean isOpen
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

        /**
         * constructor of Builder
         */
        public Builder() {
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
         * create of Builder
         *
         * @return CodecEncoder
         */
        public CodecEncoder create() {
            return new CodecEncoder(this);
        }
    }
}
