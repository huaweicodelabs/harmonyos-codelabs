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

package com.huawei.codecdemo.camera.utils;

/**
 * Nv21Handler NV21，（YUV420）
 *
 * @since 2021-04-09
 */
public class Nv21Handler {
    private static final int NUMBER_2 = 2;
    private static final int NUMBER_3 = 3;

    private Nv21Handler() {
    }

    /**
     * 420p Rotation 90 Degrees
     *
     * @param data data
     * @param imageWidth imageWidth
     * @param imageHeight imageHeight
     * @return byte[]
     */
    public static byte[] rotateYuv420Degree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuvs = new byte[imageWidth * imageHeight * NUMBER_3 / NUMBER_2];
        int temp = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuvs[temp] = data[y * imageWidth + x];
                temp++;
            }
        }
        temp = imageWidth * imageHeight * NUMBER_3 / NUMBER_2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - NUMBER_2) {
            for (int y = 0; y < imageHeight / NUMBER_2; y++) {
                yuvs[temp] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                temp--;
                yuvs[temp] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                temp--;
            }
        }
        return yuvs;
    }

    /**
     * 420p Rotation 270 Degrees
     *
     * @param data data
     * @param imageWidth imageWidth
     * @param imageHeight imageHeight
     * @return byte[]
     */
    public static byte[] rotateYuv420Degree270(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuvs = new byte[imageWidth * imageHeight * NUMBER_3 / NUMBER_2];
        int temp = 0;
        for (int x = imageWidth - 1; x >= 0; x--) {
            for (int y = 0; y < imageHeight; y++) {
                yuvs[temp] = data[y * imageWidth + x];
                temp++;
            }
        }
        for (int x = imageWidth - 1; x > 0; x = x - NUMBER_2) {
            for (int y = 0; y < imageHeight / NUMBER_2; y++) {
                yuvs[temp] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                temp++;
                yuvs[temp] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                temp++;
            }
        }
        return yuvs;
    }

    /**
     * yuv420 Rotation 90 Degrees and scale x
     *
     * @param data data
     * @param imageW imageWidth
     * @param imageH imageHeight
     * @return byte[]
     */
    public static byte[] rotateYuvDegree270AndMirror(byte[] data, int imageW, int imageH) {
        byte[] yuvs = new byte[imageW * imageH * NUMBER_3 / NUMBER_2];
        int temp = 0;
        int maxY;
        for (int x = imageW - 1; x >= 0; x--) {
            maxY = imageW * (imageH - 1) + x * NUMBER_2;
            for (int y = 0; y < imageH; y++) {
                yuvs[temp] = data[maxY - (y * imageW + x)];
                temp++;
            }
        }
        // Rotate and mirror the U and V color components
        int uvSize = imageW * imageH;
        temp = uvSize;
        int maxUv;
        for (int x = imageW - 1; x > 0; x = x - NUMBER_2) {
            maxUv = imageW * (imageH / NUMBER_2 - 1) + x * NUMBER_2 + uvSize;
            for (int y = 0; y < imageH / NUMBER_2; y++) {
                yuvs[temp] = data[maxUv - NUMBER_2 - (y * imageW + x - 1)];
                temp++;
                yuvs[temp] = data[maxUv - (y * imageW + x)];
                temp++;
            }
        }
        return yuvs;
    }
}
