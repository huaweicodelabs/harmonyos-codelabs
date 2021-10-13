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

import ohos.agp.components.AttrSet;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Component.TouchEventListener;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/**
 * CaptureButton
 *
 * @since 2021-04-09
 */
public class CaptureButton extends Button implements TouchEventListener {
    private static final float NUMBER_FLOAT_POINT_8 = 0.8f;

    /**
     * CaptureButton
     *
     * @param context context
     */
    public CaptureButton(Context context) {
        this(context, null);
    }

    /**
     * CaptureButton
     *
     * @param context context
     * @param attrSet attrSet
     */
    public CaptureButton(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    /**
     * CaptureButton
     *
     * @param context context
     * @param attrSet attrSet
     * @param styleName styleName
     */
    public CaptureButton(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initListener();
    }

    private void initListener() {
        setTouchEventListener(this);
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                setAlpha(NUMBER_FLOAT_POINT_8);
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                setAlpha(1.0f);
                break;
            default:
                break;
        }
        return true;
    }

}
