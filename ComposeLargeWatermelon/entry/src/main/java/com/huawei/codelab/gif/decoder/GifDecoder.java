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

package com.huawei.codelab.gif.decoder;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * GifDecoder
 *
 * @since 2021-05-26
 */
public class GifDecoder {
    /**
     * File read status: No errors.
     */
    public static final int STATUS_OK = 0;

    /**
     * File read status: Error decoding file (may be partially decoded).
     */
    public static final int STATUS_FORMAT_ERROR = 1;

    /**
     * File read status: Unable to open source.
     */
    public static final int STATUS_OPEN_ERROR = 2;

    /**
     * unkown
     */
    public static final int LOOP_FOREVER = -1;

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "QRCodeUtil");

    // Raw GIF data from input source.
    private ByteBuffer rawData;
    private GifHeaderParser parser;
    private GifHeader header;
    private int status;

    /**
     * constructor
     */
    public GifDecoder() {
        header = new GifHeader();
    }

    /**
     * Gets display duration for specified frame.
     *
     * @param n int index of frame.
     * @return delay in milliseconds.
     */
    public int getDelay(int n) {
        int delay = -1;
        if ((n >= 0) && (n < header.frameCount)) {
            delay = header.frames.get(n).delay;
        }
        return delay;
    }

    /**
     * Gets the number of frames read from file.
     *
     * @return frame count.
     */
    public int getFrameCount() {
        return header.frameCount;
    }

    /**
     * Reads GIF image from stream.
     *
     * @param is containing GIF file.
     * @param contentLength legnth
     * @return read status code (0 = no errors).
     */
    public int read(InputStream is, int contentLength) {
        ByteArrayOutputStream buffer = null;
        if (is != null) {
            try {
                int capacity = (contentLength > 0) ? (contentLength + 4096) : 16384;
                buffer = new ByteArrayOutputStream(capacity);
                int nRead;
                byte[] data = new byte[16384];//16384
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();

                read(buffer.toByteArray());
            } catch (IOException e) {
                HiLog.error(LABEL_LOG, "error");
            }finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    HiLog.error(LABEL_LOG, "error");
                }
                try {
                    if (buffer != null) {
                        buffer.close();
                    }
                } catch (IOException e) {
                    HiLog.error(LABEL_LOG, "error");
                }
            }
        } else {
            status = STATUS_OPEN_ERROR;
        }
        return status;
    }

    private synchronized void setData(GifHeader header, byte[] data) {
        setData(header, ByteBuffer.wrap(data));
    }

    private synchronized void setData(GifHeader header, ByteBuffer buffer) {
        setData(header, buffer, 1);
    }

    private synchronized void setData(GifHeader header, ByteBuffer buffer, int sampleSize) {
        if (sampleSize <= 0) {
            throw new IllegalArgumentException("Sample size must be >=0, not: " + sampleSize);
        }
        this.status = STATUS_OK;
        this.header = header;
        rawData = buffer.asReadOnlyBuffer();
        rawData.position(0);
        rawData.order(ByteOrder.LITTLE_ENDIAN);
    }

    private GifHeaderParser getHeaderParser() {
        if (parser == null) {
            parser = new GifHeaderParser();
        }
        return parser;
    }

    /**
     * Reads GIF image from byte array.
     *
     * @param data containing GIF file.
     * @return read status code (0 = no errors).
     */
    public synchronized int read(byte[] data) {
        this.header = getHeaderParser().setData(data).parseHeader();
        if (data != null) {
            setData(header, data);
        }

        return status;
    }
}