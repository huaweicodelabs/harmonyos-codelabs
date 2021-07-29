/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.huawei.cookbook.qrlibrary.util;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Texture;
import ohos.agp.render.ColorFilter;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.BlendMode;
import ohos.agp.utils.Matrix;
import ohos.app.Context;
import ohos.global.resource.RawFileEntry;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.io.InputStream;

/**
 * PixelMap Util.
 *
 * @since 2021-05-25
 */
public final class PixelMapUtil {
    /**
     * 90角度.
     */
    private static final int DEGREE_90 = 90;
    /**
     * 大小5.
     */
    private static final int FIVE = 5;

    private PixelMapUtil() {
    }

    /**
     * 创建图片.
     *
     * @param width  图片宽度
     * @param height 图片高度
     * @return pixelMap 返回图片
     */
    private static PixelMap createPixelMap(int width, int height) {
        PixelMap pixelMap;
        PixelMap.InitializationOptions options =
                new PixelMap.InitializationOptions();
        options.size = new Size(width, height);
        options.pixelFormat = PixelFormat.ARGB_8888;
        options.editable = true;
        pixelMap = PixelMap.create(options);
        return pixelMap;
    }

    /**
     * 调整照片旋转.
     *
     * @param map    当前位图
     * @param degree 旋转角度
     * @return 新生位图
     */
    public static PixelMap adjustRotation(PixelMap map, int degree) {
        Matrix matrix = new Matrix();
        matrix.setRotate(degree, (float) map.getImageInfo().size.width / 2,
                (float) map.getImageInfo().size.height / 2);
        float outputX;
        float outputY;
        if (degree == DEGREE_90) {
            outputX = map.getImageInfo().size.height;
            outputY = 0;
        } else {
            outputX = map.getImageInfo().size.height;
            outputY = map.getImageInfo().size.width;
        }
        float[] values = matrix.getData();
        float x1 = values[2];
        float y1 = values[FIVE];
        matrix.postTranslate(outputX - x1, outputY - y1);
        Size size = map.getImageInfo().size;
        PixelMap pixelMap = createPixelMap(size.height, size.width);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(new Texture(pixelMap));
        canvas.setMatrix(matrix);
        canvas.drawPixelMapHolder(new PixelMapHolder(map), 0, 0, paint);
        return pixelMap;
    }

    /**
     * 给位图上色.
     *
     * @param inputBitmap 当前位图
     * @param tintColor   颜色
     * @return 新生位图
     */
    public static PixelMap makeTintBitmap(PixelMap inputBitmap, int tintColor) {
        Size size = inputBitmap.getImageInfo().size;
        PixelMap pixelMap = createPixelMap(size.height, size.width);
        Canvas canvas = new Canvas(new Texture(pixelMap));
        Paint paint = new Paint();
        paint.setColorFilter(new ColorFilter(tintColor, BlendMode.SRC_IN));
        canvas.drawPixelMapHolder(new PixelMapHolder(inputBitmap), 0, 0, paint);
        return pixelMap;
    }

    /**
     * 根据输入路径获取图片文件.
     *
     * @param context 上下文
     * @param src     资源文件
     * @return pixelMap对象
     */
    public static PixelMap getPixelMapByResPath(Context context, String src) {
        PixelMap pixelMap = null;
        InputStream resource = null;
        try {
            RawFileEntry rawFileEntry =
                    context.getResourceManager().getRawFileEntry(src);
            resource = rawFileEntry.openRawFile();
            ImageSource.SourceOptions opts = new ImageSource.SourceOptions();
            ImageSource imageSource = ImageSource.create(resource, opts);
            ImageSource.DecodingOptions decodingOpts =
                    new ImageSource.DecodingOptions();
            pixelMap = imageSource.createPixelmap(decodingOpts);
            resource.close();
        } catch (IOException e) {
            QRCodeUtil.error("PixelMap error");
        } finally {
            try {
                if (resource != null) {
                    resource.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pixelMap;
    }
}
