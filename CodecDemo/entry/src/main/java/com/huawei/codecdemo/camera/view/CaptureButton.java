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

import com.huawei.codecdemo.utils.LogUtil;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Component.TouchEventListener;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.multimodalinput.event.TouchEvent;

/**
 * CaptureButton 采集按钮
 *
 * @since 2021-04-09
 */
public class CaptureButton extends Button implements TouchEventListener {
    private static final String TAG = CaptureButton.class.getSimpleName();

    private static final float NUMBER_FLOAT_POINT_8 = 0.8f;
    private static final float NUMBER_FLOAT_POINT_4 = 0.4f;
    private static final int NUMBER_INT_2 = 2;
    private static final int NUMBER_INT_255 = 255;

    private Context context;
    private float currentCorner;

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
        this.context = context;
        initView();
        initListener();
    }

    private void initView() {
        currentCorner = (float) getHeight() / NUMBER_INT_2;
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

    /**
     * captrue样式切换，圆形转矩形
     */
    public void capture2Rect() {
        context.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            int temp = getHeight() / NUMBER_INT_2;
            ShapeElement rectElement = new ShapeElement();
            rectElement.setRgbColor(new RgbColor(NUMBER_INT_255, 0, 0));
            while (currentCorner >= 0) {
                context.getUITaskDispatcher().syncDispatch(() -> {
                    rectElement.setCornerRadius(currentCorner);
                    setBackground(rectElement);
                    setScaleX(1 - (NUMBER_FLOAT_POINT_4 - NUMBER_FLOAT_POINT_4 / temp * currentCorner));
                    setScaleY(1 - (NUMBER_FLOAT_POINT_4 - NUMBER_FLOAT_POINT_4 / temp * currentCorner));
                });
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    LogUtil.error(TAG, "capture2Rect failed,exception is " + e.getMessage());
                }
                currentCorner--;
            }
        });
    }

    /**
     * captrue样式切换，矩形转圆形
     */
    public void capture2round() {
        context.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
            int temp = getHeight() / NUMBER_INT_2;
            ShapeElement roundElement = new ShapeElement();
            roundElement.setRgbColor(new RgbColor(NUMBER_INT_255, NUMBER_INT_255, NUMBER_INT_255));
            while (currentCorner <= (float) temp) {
                context.getUITaskDispatcher().syncDispatch(() -> {
                    roundElement.setCornerRadius(currentCorner);
                    setBackground(roundElement);
                    setScaleX(1 - (NUMBER_FLOAT_POINT_4 - NUMBER_FLOAT_POINT_4 / temp * currentCorner));
                    setScaleY(1 - (NUMBER_FLOAT_POINT_4 - NUMBER_FLOAT_POINT_4 / temp * currentCorner));
                });
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    LogUtil.error(TAG, "InterruptedException is " + e.getMessage());
                }
                currentCorner++;
            }
        });
    }
}
