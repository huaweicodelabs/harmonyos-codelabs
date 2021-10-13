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

package com.huawei.codecdemo.camera.view;

import com.huawei.codecdemo.camera.CameraController;
import com.huawei.codecdemo.camera.api.CameraListener;

import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.SurfaceOps;
import ohos.app.Context;

import java.util.Optional;

/**
 * CameraView NV21，（YUV420）
 *
 * @since 2021-04-09
 */
public class CameraView extends SurfaceProvider implements SurfaceOps.Callback {
    private final CameraListener cameraController;

    /**
     * CameraView
     *
     * @param context context
     */
    public CameraView(Context context) {
        this(context, null);
    }

    /**
     * CameraView
     *
     * @param context context
     * @param attrSet attrSet
     */
    public CameraView(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    /**
     * CameraView
     *
     * @param context context
     * @param attrSet attrSet
     * @param styleName styleName
     */
    public CameraView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        boolean isFront = false;
        Optional<Attr> optIsFront = attrSet.getAttr("front_camera");
        if (optIsFront.isPresent()) {
            isFront = "true".equals(optIsFront.get().getStringValue());
        }
        cameraController = new CameraController(mContext, isFront);
        initView();
    }

    private void initView() {
        setVisibility(Component.VISIBLE);
        setFocusable(Component.FOCUS_ENABLE); // 设置可聚焦
        setTouchFocusable(true); // 设置触摸模式
        requestFocus();
        pinToZTop(false);
        if (getSurfaceOps().isPresent()) {
            getSurfaceOps().get().addCallback(CameraView.this); // 添加回调事件
        }
    }

    @Override
    public void surfaceCreated(SurfaceOps surfaceOps) {
        surfaceOps.setFixedSize(getHeight(), getWidth());
        cameraController.bind(this);
    }

    @Override
    public void surfaceChanged(SurfaceOps surfaceOps, int tag, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceOps surfaceOps) {
        cameraController.unBind();
    }

    /**
     * get Controller
     *
     * @return ICamera
     */
    public CameraListener getController() {
        return cameraController;
    }
}
