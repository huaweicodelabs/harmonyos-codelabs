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

package com.huawei.cookbook.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.PixelMap;

import com.huawei.cookbook.ResourceTable;
import com.huawei.cookbook.qrlibrary.util.PixelMapUtil;
import com.huawei.cookbook.qrlibrary.core.GenerateCore;
import com.huawei.cookbook.qrlibrary.util.QRCodeUtil;

/**
 * 码生成.
 *
 * @since 2021-05-25
 */
public class CodeGenerationAbilitySlice extends AbilitySlice {
    /**
     * 常量.
     */
    private static final int NUMBER2 = 2;
    /**
     * Image中间边距
     */
    private static final int NUMBER10 = 10;
    /**
     * Image与屏幕边距
     */
    private static final int NUMBER40 = 40;
    /**
     * 随机定义生成不同颜色的二维码.
     */
    private static final int QR_COLOR = 0xFF009900;
    /**
     * 定义二维码生成位图的宽高.
     */
    private int size = 0;
    /**
     * 显示英文二维码.
     */
    private Image englishQRCode;
    /**
     * 显示中文二维码.
     */
    private Image chineseQRCode;
    /**
     * 显示不同颜色二维码.
     */
    private Image colorQRCode;
    /**
     * 显示logo图标二维码.
     */
    private Image logoQRCode;
    /**
     * 生成英文二维码的内容.
     */
    private final String englishContent = "http://www.huawei.com";
    /**
     * 二维码字节流生成核心类.
     */
    private GenerateCore generateCore;
    /**
     * 线程类.
     */
    private EventHandler handler;

    /**
     * 生命周期.
     * @param intent intent
     */
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_code_generation);
        // 初始化视图
        initView();
        // 初始化数据
        initData();
        // 生成英文二维码
        generateEnglishQRCode();
        // 生成中文二维码
        generateChineseQRCode();
        // 生成不同颜色二维码
        generateColorQRCode();
        // 生成logo图标二维码
        generateLogoQRCode();
    }

    /**
     * 初始化事件.
     */
    private void initView() {
        size = (QRCodeUtil.getScreenResolution(getContext()).getPointXToInt()
                - (NUMBER10 + NUMBER40) * NUMBER2) / NUMBER2;
        DirectionalLayout.LayoutConfig config =
                new DirectionalLayout.LayoutConfig(size,size);

        if (findComponentById(ResourceTable.Id_englishQRCode)
                instanceof Image) {
            englishQRCode =  findComponentById(
                    ResourceTable.Id_englishQRCode);
            config.setMarginLeft(NUMBER40);
            config.setMarginRight(NUMBER10);
            englishQRCode.setLayoutConfig(config);
        }
        if (findComponentById(ResourceTable.Id_chineseQRCode)
                instanceof Image) {
            chineseQRCode =  findComponentById(
                    ResourceTable.Id_chineseQRCode);
            config.setMarginLeft(NUMBER10);
            config.setMarginRight(NUMBER40);
            chineseQRCode.setLayoutConfig(config);
        }
        if (findComponentById(ResourceTable.Id_colorQRCode)
                instanceof Image) {
            colorQRCode =  findComponentById(
                    ResourceTable.Id_colorQRCode);
            config.setMarginLeft(NUMBER40);
            config.setMarginRight(NUMBER10);
            colorQRCode.setLayoutConfig(config);
        }
        if (findComponentById(ResourceTable.Id_logoQRCode)
                instanceof Image) {
            logoQRCode =  findComponentById(
                    ResourceTable.Id_logoQRCode);
            config.setMarginLeft(NUMBER10);
            config.setMarginRight(NUMBER40);
            logoQRCode.setLayoutConfig(config);
        }
    }

    /**
     * 初始化数据.
     */
    private void initData() {
        generateCore = new GenerateCore(this);
        handler = new EventHandler(EventRunner.create("qr"));
    }

    /**
     * 生成英文二维码.
     */
    private void generateEnglishQRCode() {
        generateCommonQRCode(englishContent, englishQRCode);
    }

    /**
     * 生成中文二维码.
     */
    private void generateChineseQRCode() {
        // 生成中文二维码的内容
        String chineseContent = "华为官方网址";
        generateCommonQRCode(chineseContent, chineseQRCode);
    }

    /**
     * 生成不同颜色二维码.
     */
    private void generateColorQRCode() {
        handler.postTask(() -> {
            PixelMap pixelMap = generateCore.generateColorQRCode(
                    englishContent, size, QR_COLOR);

            getMainTaskDispatcher().syncDispatch(() -> {
                // 显示二维码图像
                colorQRCode.setPixelMap(pixelMap);
            });
        });
    }

    /**
     * 生成logo图标二维码.
     */
    private void generateLogoQRCode() {
        handler.postTask(() -> {
            PixelMap logoPixelMap = PixelMapUtil.getPixelMapByResPath(
                    getContext(), "entry/resources/base/media/icon.png");
            PixelMap pixelMap = generateCore.generateLogoQRCode(
                    englishContent, size, logoPixelMap);

            getMainTaskDispatcher().syncDispatch(() -> {
                // 显示二维码图像
                logoQRCode.setPixelMap(pixelMap);
            });
        });
    }

    /**
     * 生成普通二维码.
     *
     * @param content 需要生成的内容
     * @param image   在Image视图中显示
     */
    private void generateCommonQRCode(String content, Image image) {
        handler.postTask(() -> {
            PixelMap pixelMap = generateCore.generateCommonQRCode(
                    content, size);

            getMainTaskDispatcher().syncDispatch(() -> {
                // 显示二维码图像
                image.setPixelMap(pixelMap);
            });
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        generateCore.release();
    }
}
