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

import java.util.ArrayList;
import java.util.List;

/**
 * GifHeader
 *
 * @since 2021-05-26
 */
public class GifHeader {
    int[] gct = null;
    int status = GifDecoder.STATUS_OK;
    int frameCount = 0;

    GifFrame currentFrame;
    List<GifFrame> frames = new ArrayList<>();

    // Full image width.
    int width;

    // Full image height.
    int height;

    // 1 : global color table flag.
    boolean gctFlag;

    // 2-4 : color resolution.
    // 5 : gct sort flag.
    // 6-8 : gct size.
    int gctSize;

    // Background color index.
    int bgIndex;

    // Pixel aspect ratio.
    int pixelAspect;

    int bgColor;
    int loopCount = 0;
}