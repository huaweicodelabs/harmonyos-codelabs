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

package com.huawei.codelab.utils;

import ohos.agp.render.PixelMapHolder;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.IOException;
import java.io.InputStream;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.model.Product;

/**
 * Generating an Image Model
 *
 * @since 2021-04-14
 */
public class ImageUtil {
    /**
     * Image Resources
     */
    private static int[] imageLists = {ResourceTable.Media_product0, ResourceTable.Media_product1,
        ResourceTable.Media_product2, ResourceTable.Media_product3, ResourceTable.Media_product4,
        ResourceTable.Media_product5, ResourceTable.Media_product6};
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "ImageUtil");

    private ImageUtil() {
    }

    /**
     * Number of resources
     *
     * @return Number of resources
     */
    public static int getLength() {
        return imageLists.length;
    }

    /**
     * generate product
     *
     * @param context context
     * @param level level
     * @return Product
     */
    public static Product generateProduct(Context context, int level) {
        PixelMap pixelMap = getPixelMap(context, level);
        PixelMapHolder pmh = new PixelMapHolder(pixelMap);

        int radius = pixelMap.getImageInfo().size.width / NumbersUtil.VAULE_TWO;
        Product product = new Product();
        product.setLevel(level);
        product.setRadius(radius);
        product.setPixelMapHolder(pmh);
        return product;
    }

    /**
     * getPixelMap
     *
     * @param context context
     * @param level level
     * @return PixelMap
     */
    private static PixelMap getPixelMap(Context context, int level) {
        InputStream drawableInputStream = null;
        PixelMap pixelMap = null;
        try {
            drawableInputStream = context.getResourceManager().getResource(imageLists[level]);
            ImageSource imageSource = ImageSource.create(drawableInputStream, new ImageSource.SourceOptions());
            ImageSource.DecodingOptions options = new ImageSource.DecodingOptions();
            pixelMap = imageSource.createPixelmap(options);
            return pixelMap;
        } catch (IOException | NotExistException exception) {
            HiLog.info(LABEL_LOG, "error");
        } finally {
            if (drawableInputStream != null) {
                try {
                    drawableInputStream.close();
                } catch (IOException e) {
                    HiLog.info(LABEL_LOG, "error");
                }
            }
        }
        return pixelMap;
    }
}
