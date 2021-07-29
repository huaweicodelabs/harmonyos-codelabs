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

import com.huawei.cookbook.qrlibrary.util.QRCodeUtil;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.Texture;
import ohos.ai.cv.common.ConnectionCallback;
import ohos.ai.cv.common.VisionManager;
import ohos.ai.cv.qrcode.IBarcodeDetector;
import ohos.app.Context;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Position;
import ohos.media.image.common.Size;

import java.io.UnsupportedEncodingException;

/**
 * 码生成核心类.
 *
 * @since 2021-05-25
 */
public class GenerateCore {
    /**
     * 常量.
     */
    private static final int NUMBER2 = 2;
    /**
     * 字节个数.
     */
    private static final int BYTE_SIZE = 4;
    /**
     * logo位图缩放比例.
     */
    private static final float RATIO = 0.14f;
    /**
     * 默认二维码生成的颜色.
     */
    private static final int DEFAULT_CODE_COLOR = 0xFF000000;
    /**
     * 生成二维码字节流类.
     */
    private IBarcodeDetector barcodeDetector;

    /**
     * constructor.
     *
     * @param context context
     */
    public GenerateCore(Context context) {
        initManager(context);
    }

    private void initManager(Context context) {
        ConnectionCallback connectionCallback =
                new ConnectionCallback() {
                    @Override
                    public void onServiceConnect() {
                        // 实例化 IBarcodeDetector 接口
                        barcodeDetector =
                                VisionManager.getBarcodeDetector(context);
                    }

                    @Override
                    public void onServiceDisconnect() {
                    }
                };
        VisionManager.init(context, connectionCallback);
    }

    /**
     * release
     */
    public void release() {
        if (barcodeDetector != null) {
            barcodeDetector.release();
            VisionManager.destroy();
        }
    }

    /**
     * 生成二维码字节流.
     *
     * @param content 生成的内容
     * @param size    生成的大小
     * @return 二维码字节流
     */
    private byte[] generateQR(String content, int size) {
        // 1个int4个字节 BYTE_SIZE
        byte[] byteArray = new byte[size * size * BYTE_SIZE];
        String val = content;
        try {
            // 解决中文不能识别的问题
            val = new String(content.getBytes("UTF-8"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            QRCodeUtil.error("error");
        }
        while (barcodeDetector==null){
        }
        barcodeDetector.detect(val, byteArray, size, size);
        return byteArray;
    }

    /**
     * 生成普通二维码位图.
     *
     * @param content 生成的内容
     * @param size    生成的大小
     * @return 二维码图像
     */
    public PixelMap generateCommonQRCode(String content, int size) {
        byte[] data = generateQR(content, size);
        // 从字节数组创建图像源
        ImageSource source = ImageSource.create(data, null);
        // PixelMap位图解码配置
        ImageSource.DecodingOptions opts = new ImageSource.DecodingOptions();
        // 配置PixelMap位图为可编辑状态
        opts.editable = true;
        // 从图像数据源解码并创建PixelMap图像。
        PixelMap pixelMap = source.createPixelmap(opts);
        return pixelMap;
    }

    /**
     * 生成不同颜色二维码位图.
     *
     * @param content 生成的内容
     * @param size    生成的大小
     * @param color   生成的颜色
     * @return 二维码图像
     */
    public PixelMap generateColorQRCode(String content, int size, int color) {
        // 获取二维码位图
        PixelMap pixelMap = generateCommonQRCode(content, size);
        // 改变颜色
        modifyPixelMapColor(pixelMap, color);
        return pixelMap;
    }

    /**
     * 生成logo图标二维码位图.
     *
     * @param content      生成的内容
     * @param size         生成的大小
     * @param logoMap logo图标位图
     * @return 二维码图像
     */
    public PixelMap generateLogoQRCode(String content, int size, PixelMap logoMap) {
        // 获取二维码位图
        PixelMap pixelMap = generateCommonQRCode(content, size);
        updateLogoImage(pixelMap, logoMap);
        return pixelMap;
    }

    /**
     * 将logo位图贴在二维码位图中间.
     *
     * @param pixelMap     修改的位图
     * @param logoMap logo位图
     */
    private void updateLogoImage(PixelMap pixelMap, PixelMap logoMap) {
        // 获取logo图标位图大小和二维码位图大小
        Size size = pixelMap.getImageInfo().size;
        PixelMap.InitializationOptions opts =
                new PixelMap.InitializationOptions();
        // 缩放logo位图为二维码位图的0.18 RATIO
        opts.size = new Size((int) (size.width * RATIO),
                (int) (size.height * RATIO));
        PixelMap logoPixelMap = PixelMap.create(logoMap, opts);

        Size logoSize = logoPixelMap.getImageInfo().size;

        // 在二维码位图的中间生成logo图标的位图
        Canvas canvas = new Canvas(new Texture(pixelMap));
        Paint paint = new Paint();
        int centerX = size.width / NUMBER2 - logoSize.width / NUMBER2;
        int centerY = size.height / NUMBER2 - logoSize.height / NUMBER2;
        canvas.drawPixelMapHolder(new PixelMapHolder(logoPixelMap), centerX, centerY, paint);
    }

    /**
     * 修改对应位图像素点的值.
     *
     * @param pixelMap 修改的位图
     * @param color    修改的颜色
     */
    private void modifyPixelMapColor(PixelMap pixelMap, int color) {
        Size size = pixelMap.getImageInfo().size;

        for (int i = 0; i < size.width; i++) {
            for (int j = 0; j < size.height; j++) {
                // 读取指定位置像素
                int rawColor = pixelMap.readPixel(new Position(i, j));
                // DEFAULT_CODE_COLOR 默认生成颜色值为黑色
                if (rawColor == DEFAULT_CODE_COLOR) {
                    // 在指定位置写入像素
                    pixelMap.writePixel(new Position(i, j), color);
                }
            }
        }
    }
}
