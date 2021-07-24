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

package com.huawei.cookbook.qrlibrary.core;

import ohos.agp.utils.Rect;
import ohos.media.image.PixelMap;

import java.nio.charset.StandardCharsets;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;

import com.huawei.cookbook.qrlibrary.util.QRCodeUtil;

/**
 * Scanner Core.
 *
 * @since 2021-05-25
 */
public class ScannerCore {
    /**
     * ImageScanner config size.
     */
    private static final int CONFIG_SIZE = 3;
    /**
     * 二维码识别核心类.
     */
    private ImageScanner mScanner;

    /**
     * 构造函数.
     */
    public ScannerCore() {
        initImageScanner();
    }

    /**
     * 初始化识别核心类.
     */
    private void initImageScanner() {
        mScanner = new ImageScanner();
        mScanner.setConfig(0, Config.X_DENSITY, CONFIG_SIZE);
        mScanner.setConfig(0, Config.Y_DENSITY, CONFIG_SIZE);
        mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);

        mScanner.setConfig(Symbol.PARTIAL, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.EAN8, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.UPCE, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.ISBN10, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.UPCA, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.EAN13, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.ISBN13, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.I25, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.DATABAR, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.DATABAR_EXP, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.CODABAR, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.CODE39, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.PDF417, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.CODE93, Config.ENABLE, 1);
        mScanner.setConfig(Symbol.CODE128, Config.ENABLE, 1);
    }

    /**
     * 处理图像识别结果.
     *
     * @param pixelMap        识别的位图
     * @param scanBoxAreaRect 识别的矩阵区域
     * @return 识别后的结果
     */
    public String processPixelMapData(PixelMap pixelMap, Rect scanBoxAreaRect) {
        Image image = processImage(pixelMap, scanBoxAreaRect);
        String result = processData(image);
        return result;
    }

    /**
     * 处理图像识别结果.
     *
     * @param pixelMap        识别的位图
     * @param scanBoxAreaRect 识别的矩阵区域
     * @return 识别的Image对象
     */
    private Image processImage(PixelMap pixelMap, Rect scanBoxAreaRect) {
        int pWidth = pixelMap.getImageInfo().size.width;
        int pHeight = pixelMap.getImageInfo().size.height;

        Image barcode;
        int[] pix;

        if (scanBoxAreaRect == null) {
            barcode = new Image(pWidth, pHeight, "RGB4");
            pix = new int[pWidth * pHeight];
            pixelMap.readPixels(pix, 0, pWidth,
                    new ohos.media.image.common.Rect(0, 0, pWidth, pHeight));
        } else {
            int scanWidth = scanBoxAreaRect.getWidth();
            int scanHeight = scanBoxAreaRect.getHeight();
            barcode = new Image(scanWidth, scanHeight, "RGB4");
            pix = new int[scanWidth * scanHeight];
            pixelMap.readPixels(pix, 0, scanWidth,
                    new ohos.media.image.common.Rect(scanBoxAreaRect.left,
                            scanBoxAreaRect.top, scanWidth, scanHeight));
        }
        barcode.setData(pix);

        return barcode.convert("Y800");
    }

    /**
     * 核心处理.
     *
     * @param barcode 包装处理数据的
     * @return 识别结果
     */
    private String processData(Image barcode) {
        String symData = null;
        if (mScanner.scanImage(barcode) == 0) {
            return symData;
        }

        for (Symbol symbol : mScanner.getResults()) {
            // 未能识别的格式继续遍历
            if (symbol.getType() == Symbol.NONE) {
                continue;
            }

            symData = new String(symbol.getDataBytes(), StandardCharsets.UTF_8);
            // 空数据继续遍历
            if (QRCodeUtil.isEmpty(symData)) {
                continue;
            }

            return symData;
        }
        return symData;
    }
}
