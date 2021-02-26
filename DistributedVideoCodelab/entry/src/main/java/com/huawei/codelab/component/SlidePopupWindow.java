/*
 * Copyright (c) 2021 Huawei Device Co., Ltd. All rights reserved.
 *
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

package com.huawei.codelab.component;

import com.huawei.codelab.util.ScreenUtils;
import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/**
 * the window to show device list
 *
 * @since 2020-12-04
 */
public class SlidePopupWindow extends DependentLayout implements Component.TouchEventListener {
    /**
     * SLIDE FROM BOTTOM
     */
    public static final int SLIDE_FROM_BOTTOM = 1;
    private static final int COLOR_FULL = 255;
    private static final int ANIM_DURATION = 300;
    private AnimatorProperty animatorProperty;
    private int startX = 0;
    private int endX = 0;
    private int startY = 0;
    private int endY = 0;
    private boolean isShow;
    private SlidePopupWindow.PopupWindowListener windowListener;
    private Component rootView;
    private Context windowContext;

    /**
     * constructor of SlidePopupWindow
     *
     * @param context the context
     * @param res the resources id
     * @since 2020-12-07
     */
    public SlidePopupWindow(Context context, int res) {
        super(context);
        windowContext = context;
        rootView = LayoutScatter.getInstance(windowContext).parse(res, this, false);
        animatorProperty = rootView.createAnimatorProperty();
        initView(SLIDE_FROM_BOTTOM);
        initListener();
    }

    private void initView(int direction) {
        setVisibility(HIDE);
        setWidth(ScreenUtils.getScreenWidth(windowContext));
        setHeight(ScreenUtils.getScreenHeight(windowContext));
        ShapeElement element = new ShapeElement();
        setBackground(element);
        LayoutConfig config = new LayoutConfig();
        if (rootView.getLayoutConfig() instanceof LayoutConfig) {
            config = (LayoutConfig) rootView.getLayoutConfig();
        }
        switch (direction) {
            case SLIDE_FROM_BOTTOM:
                config.addRule(LayoutConfig.ALIGN_PARENT_BOTTOM);
                config.setMargins(0, 0, 0, -rootView.getHeight());
                startY = ScreenUtils.getScreenHeight(windowContext);
                endY = ScreenUtils.getScreenHeight(windowContext) - rootView.getHeight();
                break;
            default:
                break;
        }
        rootView.setLayoutConfig(config);
        rootView.setTouchEventListener(
                new TouchEventListener() {
                    @Override
                    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
                        return true;
                    }
                });
        addComponent(rootView);
    }

    private void initListener() {
        setTouchEventListener(this);
        animatorProperty.setStateChangedListener(
                new Animator.StateChangedListener() {
                    @Override
                    public void onStart(Animator animator) {
                        if (isShow) {
                            if (windowListener != null) {
                                windowListener.windowShow();
                            }
                            setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onStop(Animator animator) {
                    }

                    @Override
                    public void onCancel(Animator animator) {
                    }

                    @Override
                    public void onEnd(Animator animator) {
                        if (!isShow) {
                            setVisibility(INVISIBLE);
                            if (windowListener != null) {
                                windowListener.windowDismiss();
                            }
                        }
                    }

                    @Override
                    public void onPause(Animator animator) {
                    }

                    @Override
                    public void onResume(Animator animator) {
                    }
                });
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        hide();
        return true;
    }

    /**
     * PopupWindowListener
     *
     * @since 2020-12-07
     */
    public interface PopupWindowListener {
        /**
         * show the device list window
         *
         * @since 2020-12-07
         */
        void windowShow();

        /**
         * dismiss the device list window
         *
         * @since 2020-12-07
         */
        void windowDismiss();
    }

    /**
     * setPopupWindowListener
     *
     * @param listener listener
     */
    public void setPopupWindowListener(SlidePopupWindow.PopupWindowListener listener) {
        windowListener = listener;
    }

    /**
     * show of SlidePopupWindow
     */
    public void show() {
        if (!isShow) {
            isShow = true;
            animatorProperty
                    .moveFromX(startX)
                    .moveToX(endX)
                    .moveFromY(startY)
                    .moveToY(endY)
                    .setCurveType(Animator.CurveType.LINEAR)
                    .setDuration(ANIM_DURATION)
                    .start();
        }
    }

    /**
     * hide of SlidePopupWindow
     */
    public void hide() {
        if (isShow) {
            isShow = false;
            animatorProperty
                    .moveFromX(endX)
                    .moveToX(startX)
                    .moveFromY(endY)
                    .moveToY(startY)
                    .setCurveType(Animator.CurveType.LINEAR)
                    .setDuration(ANIM_DURATION)
                    .start();
        }
    }
}
