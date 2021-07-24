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

import com.huawei.codelab.utils.LogUtils;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * GifHeaderParser
 *
 * @since 2021-05-26
 */
public class GifHeaderParser {
    // The minimum frame delay in hundredths of a second.
    static final int MIN_FRAME_DELAY = 2;
    // The default frame delay in hundredths of a second for GIFs with frame delays less than the
    // minimum.
    static final int DEFAULT_FRAME_DELAY = 10;

    private static final int MAX_BLOCK_SIZE = 256;
    // Raw data read working array.
    private final byte[] block = new byte[MAX_BLOCK_SIZE];

    private ByteBuffer rawData;
    private GifHeader header;
    private int blockSize = 0;

    /**
     * setdata
     *
     * @param data data
     * @return gif
     */
    public GifHeaderParser setData(ByteBuffer data) {
        reset();
        rawData = data.asReadOnlyBuffer();
        rawData.position(0);
        rawData.order(ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    /**
     * setdata
     *
     * @param data data
     * @return gif
     */
    public GifHeaderParser setData(byte[] data) {
        if (data != null) {
            setData(ByteBuffer.wrap(data));
        } else {
            rawData = null;
            header.status = GifDecoder.STATUS_OPEN_ERROR;
        }
        return this;
    }


    private void reset() {
        rawData = null;
        Arrays.fill(block, (byte) 0);
        header = new GifHeader();
        blockSize = 0;
    }

    /**
     * parse header
     *
     * @return gif header
     */
    public GifHeader parseHeader() {
        if (rawData == null) {
            throw new IllegalStateException("You must call setData() before parseHeader()");
        }
        if (err()) {
            return header;
        }

        readHeader();
        if (!err()) {
            readContents();
            if (header.frameCount < 0) {
                header.status = GifDecoder.STATUS_FORMAT_ERROR;
            }
        }
        return header;
    }


    /**
     * Main file parser. Reads GIF content blocks.
     */
    private void readContents() {
        readContents(Integer.MAX_VALUE /* maxFrames */);
    }

    /**
     * Main file parser. Reads GIF content blocks. Stops after reading maxFrames
     *
     * @param maxFrames maxFrames
     */
    private void readContents(int maxFrames) {
        boolean done = false;
        while (!(done || err() || header.frameCount > maxFrames)) {
            int code = read();
            switch (code) {
                case 0x2C:
                    if (header.currentFrame == null) {
                        header.currentFrame = new GifFrame();
                    }
                    readBitmap();
                    break;
                case 0x21:
                    code = read();
                    switch (code) {
                        case 0xf9:
                            header.currentFrame = new GifFrame();
                            readGraphicControlExt();
                            break;
                        case 0xff:
                            readBlock();
                            String app = "";
                            for (int i = 0; i < 11; i++) {
                                app += (char) block[i];
                            }
                            if ("NETSCAPE2.0".equals(app)) {
                                readNetscapeExt();
                            } else {
                                skip();
                            }
                            break;
                        case 0xfe:
                            skip();
                            break;
                        case 0x01:
                            skip();
                            break;
                        default:
                            skip();
                    }
                    break;
                case 0x3b:
                    done = true;
                    break;
                case 0x00:
                default:
                    header.status = GifDecoder.STATUS_FORMAT_ERROR;
            }
        }
    }

    /**
     * Reads Graphics Control Extension values.
     */
    private void readGraphicControlExt() {
        read();
        int packed = read();
        header.currentFrame.dispose = (packed & 0x1c) >> 2;
        if (header.currentFrame.dispose == 0) {
            header.currentFrame.dispose = 1;
        }
        header.currentFrame.transparency = (packed & 1) != 0;
        int delayInHundredthsOfASecond = readShort();

        if (delayInHundredthsOfASecond < MIN_FRAME_DELAY) {
            delayInHundredthsOfASecond = DEFAULT_FRAME_DELAY;
        }
        header.currentFrame.delay = delayInHundredthsOfASecond * 10;
        // Transparent color index
        header.currentFrame.transIndex = read();
        // Block terminator
        read();
    }

    /**
     * Reads next frame image.
     */
    private void readBitmap() {
        // (sub)image position & size.
        header.currentFrame.ix = readShort();
        header.currentFrame.iy = readShort();
        header.currentFrame.iw = readShort();
        header.currentFrame.ih = readShort();

        int packed = read();
        // 1 - local color table flag interlace
        boolean lctFlag = (packed & 0x80) != 0;
        int lctSize = (int) Math.pow(2, (packed & 0x07) + 1);
        // 3 - sort flag
        // 4-5 - reserved lctSize = 2 << (packed & 7);
        // 6-8 - local color
        // table size
        header.currentFrame.interlace = (packed & 0x40) != 0;
        if (lctFlag) {
            // Read table.
            header.currentFrame.lct = readColorTable(lctSize);
        } else {
            // No local color table.
            header.currentFrame.lct = null;
        }

        // Save this as the decoding position pointer.
        header.currentFrame.bufferFrameStart = rawData.position();

        // False decode pixel data to advance buffer.
        skipImageData();

        if (err()) {
            return;
        }

        header.frameCount++;
        // Add image to frame.
        header.frames.add(header.currentFrame);
    }

    /**
     * Reads Netscape extension to obtain iteration count.
     */
    private void readNetscapeExt() {
        do {
            readBlock();
            if (block[0] == 1) {
                // Loop count sub-block.
                int b1 = ((int) block[1]) & 0xff;
                int b2 = ((int) block[2]) & 0xff;
                header.loopCount = (b2 << 8) | b1;
                if (header.loopCount == 0) {
                    header.loopCount = GifDecoder.LOOP_FOREVER;
                }
            }
        } while ((blockSize > 0) && !err());
    }


    /**
     * Reads GIF file header information.
     */
    private void readHeader() {
        String id = "";
        for (int i = 0; i < 6; i++) {
            id += (char) read();
        }
        if (!id.startsWith("GIF")) {
            header.status = GifDecoder.STATUS_FORMAT_ERROR;
            return;
        }
        readLSD();
        if (header.gctFlag && !err()) {
            header.gct = readColorTable(header.gctSize);
            header.bgColor = header.gct[header.bgIndex];
        }
    }

    /**
     * Reads Logical Screen Descriptor.
     */
    private void readLSD() {
        // Logical screen size.
        header.width = readShort();
        header.height = readShort();
        // Packed fields
        int packed = read();
        // 1 : global color table flag.
        header.gctFlag = (packed & 0x80) != 0;
        // 2-4 : color resolution.
        // 5 : gct sort flag.
        // 6-8 : gct size.
        header.gctSize = 2 << (packed & 7);
        // Background color index.
        header.bgIndex = read();
        // Pixel aspect ratio
        header.pixelAspect = read();
    }

    /**
     * Reads color table as 256 RGB integer values.
     *
     * @param ncolors int number of colors to read.
     * @return int array containing 256 colors (packed ARGB with full alpha).
     */
    private int[] readColorTable(int ncolors) {
        int nbytes = 3 * ncolors;
        int[] tab = null;
        byte[] c = new byte[nbytes];

        try {
            rawData.get(c);
            // Max size to avoid bounds checks.
            tab = new int[MAX_BLOCK_SIZE];
            int i = 0;
            int j = 0;
            while (i < ncolors) {
                int r = ((int) c[j++]) & 0xff;
                int g = ((int) c[j++]) & 0xff;
                int b = ((int) c[j++]) & 0xff;
                tab[i++] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        } catch (BufferUnderflowException e) {
            header.status = GifDecoder.STATUS_FORMAT_ERROR;
            LogUtils.info("error");
        }

        return tab;
    }

    /**
     * Skips LZW image data for a single frame to advance buffer.
     */
    private void skipImageData() {
        // lzwMinCodeSize
        read();
        // data sub-blocks
        skip();
    }

    /**
     * Skips variable length blocks up to and including next zero length block.
     */
    private void skip() {
        try {
            int blockSize;
            do {
                blockSize = read();
                rawData.position(rawData.position() + blockSize);
            } while (blockSize > 0);
        } catch (IllegalArgumentException ex) {
            LogUtils.info( "error");
        }
    }

    /**
     * Reads next variable length block from input.
     *
     * @return number of bytes stored in "buffer"
     */
    private int readBlock() {
        blockSize = read();
        int n = 0;
        if (blockSize > 0) {
            int count;
            while (n < blockSize) {
                count = blockSize - n;
                rawData.get(block, n, count);

                n += count;
            }
        }
        return n;
    }

    /**
     * Reads a single byte from the input stream.
     *
     * @return count
     */
    private int read() {
        int curByte = rawData.get() & 0xFF;
        return curByte;
    }

    /**
     * Reads next 16-bit value, LSB first.
     *
     * @return value
     */
    private int readShort() {
        // Read 16-bit value.
        return rawData.getShort();
    }

    /**
     * err
     *
     * @return is err
     */
    private boolean err() {
        return header.status != GifDecoder.STATUS_OK;
    }
}