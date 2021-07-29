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

package com.huawei.cookbook;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;

import com.huawei.cookbook.qrlibrary.QRCodeView;
import com.huawei.cookbook.qrlibrary.util.QRCodeUtil;

/**
 * Code ScanningA.
 *
 * @since 2021-05-25
 */
public class ScanAbility extends Ability implements QRCodeView.ScanResultListener {
    /**
     * 显示二维码扫描视图.
     */
    private QRCodeView qrCodeView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        addComponent();
    }

    /**
     * 添加扫码布局
     */
    private void addComponent() {
        qrCodeView = new QRCodeView(this);
        DirectionalLayout.LayoutConfig params =
                new DirectionalLayout.LayoutConfig(
                        ComponentContainer.LayoutConfig.MATCH_PARENT,
                        ComponentContainer.LayoutConfig.MATCH_PARENT);
        qrCodeView.setLayoutConfig(params);
        qrCodeView.setScanResultListener(this);

        setUIContent(qrCodeView);
    }

    /**
     * 展示二维码扫码的结果.
     *
     * @param result result
     */
    @Override
    public void scanResult(String result) {
        QRCodeUtil.showToast(getContext(), result);
    }

    /**
     * 生命周期.
     */
    @Override
    protected void onStop() {
        super.onStop();
        qrCodeView.onDestroy(); // 销毁二维码扫描控件
    }
}
