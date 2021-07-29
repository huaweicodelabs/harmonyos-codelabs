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
import ohos.agp.components.Image;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.image.PixelMap;

import com.huawei.cookbook.ResourceTable;
import com.huawei.cookbook.qrlibrary.core.ScannerCore;
import com.huawei.cookbook.qrlibrary.util.PixelMapUtil;
import com.huawei.cookbook.qrlibrary.util.QRCodeUtil;

/**
 * 码识别.
 *
 * @since 2021-05-25
 */
public class CodeIdentificationAbilitySlice extends AbilitySlice {
    /**
     * 识别图片的文件名.
     */
    private String[] pathName = new String[]{"entry/resources/base/media/a.jpg",
            "entry/resources/base/media/b.jpg",
            "entry/resources/base/media/c.jpg",
            "entry/resources/base/media/d.jpg"};
    /**
     * 显示需要识别的图片.
     */
    private Image showIdentifyImage;
    /**
     * 数组索引.
     */
    private int index;
    /**
     * 识别二维码数据核心类.
     */
    private ScannerCore scannerCore;
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
        super.setUIContent(ResourceTable.Layout_ability_code_identification);
        // 初始化视图与事件
        initViewEvent();
        // 初始化数据
        initData();
        // 加载资源显示需要识别的图片
        showIdentifyImage();
    }

    /**
     * 初始化数据.
     */
    private void initData() {
        // 初始化扫码解析核心类
        scannerCore = new ScannerCore();
        handler = new EventHandler(EventRunner.create("qr"));
    }

    /**
     * 初始化视图与事件.
     */
    private void initViewEvent() {
        if (findComponentById(ResourceTable.Id_showIdentifyImage)
                instanceof Image) {
            showIdentifyImage = (Image) findComponentById(
                    ResourceTable.Id_showIdentifyImage);
            showIdentifyImage.setClickedListener(component -> showNextImage());
        }
        findComponentById(ResourceTable.Id_identifyQRCode).setClickedListener(
                component -> identifyImage());
    }

    /**
     * 显示下一个识别的图像.
     */
    private void showNextImage() {
        index++;
        if (index == pathName.length) {
            index = 0;
        }
        showIdentifyImage();
    }

    /**
     * 预先实现需要识别的图片.
     */
    private void showIdentifyImage() {
        handler.postTask(() -> {
            PixelMap pixelMap = PixelMapUtil.getPixelMapByResPath(
                    getContext(), pathName[index]);
            getMainTaskDispatcher().syncDispatch(() -> {
                showIdentifyImage.setPixelMap(pixelMap);
            });
        });
    }

    /**
     * 识别指定路径下图片中的二维码.
     */
    private void identifyImage() {
        handler.postTask(() -> {
            PixelMap pixelMap = PixelMapUtil.getPixelMapByResPath(
                    getContext(), pathName[index]);

            String result = scannerCore.processPixelMapData(pixelMap, null);
            getMainTaskDispatcher().syncDispatch(() -> {
                String msg = QRCodeUtil.isEmpty(result) ? "没有识别到二维码" : result;
                QRCodeUtil.showToast(getContext(), msg);
            });
        });
    }
}
