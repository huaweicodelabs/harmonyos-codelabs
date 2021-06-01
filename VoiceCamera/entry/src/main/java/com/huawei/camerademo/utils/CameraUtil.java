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

package com.huawei.camerademo.utils;

import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;

/**
 * CameraUtil
 *
 * @since 2021-03-08
 */
public class CameraUtil {
    /**
     * camera image file path
     */
    public static final String CAMERA_IMAGE_FILE_PATH = "camera_image_file_path";

    /**
     * is camera rear
     */
    private static boolean isCameraRear = false;

    private static final int DEGREES_90 = 90;
    private static final int DEGREES_270 = 270;

    private CameraUtil() {
    }

    /**
     * Obtains the PixelMap of an image.
     *
     * @param imageBytes  Image byte stream
     * @param imagePath   Image path
     * @param rotateCount angle of rotation
     * @return PixelMap
     */
    public static PixelMap getPixelMap(byte[] imageBytes, String imagePath, int rotateCount) {
        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
        sourceOptions.formatHint = "image/png";
        ImageSource imageSource = null;
        if (imagePath != null && !imagePath.isEmpty()) {
            imageSource = ImageSource.create(imagePath, sourceOptions);
        } else if (imageBytes != null) {
            imageSource = ImageSource.create(imageBytes, sourceOptions);
        } else {
            return null;
        }
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.desiredSize = new Size(0, 0);
        decodingOpts.rotateDegrees = (isCameraRear ? DEGREES_270 : DEGREES_90) * rotateCount;
        decodingOpts.desiredPixelFormat = PixelFormat.ARGB_8888;
        return imageSource.createPixelmap(decodingOpts);
    }

    public static void setIsCameraRear(boolean isCameraRear) {
        CameraUtil.isCameraRear = isCameraRear;
    }
}
