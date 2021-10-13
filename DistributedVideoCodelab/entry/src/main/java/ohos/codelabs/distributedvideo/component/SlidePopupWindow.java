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

package ohos.codelabs.distributedvideo.component;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;
import ohos.codelabs.distributedvideo.player.constant.Constants;
import ohos.codelabs.distributedvideo.util.ScreenUtils;
import ohos.multimodalinput.event.TouchEvent;

/**
 * SlidePopupWindow
 *
 * @since 2021-09-07
 */
public class SlidePopupWindow extends DependentLayout implements Component.TouchEventListener {
    /**
     * SLIDE FROM TOP
     */
    public static final int SLIDE_FROM_TOP = 0;
    /**
     * SLIDE FROM BOTTOM
     */
    public static final int SLIDE_FROM_BOTTOM = 1;
    /**
     * SLIDE FROM LEFT
     */
    public static final int SLIDE_FROM_LEFT = 2;
    /**
     * SLIDE FROM RIGHT
     */
    public static final int SLIDE_FROM_RIGHT = 3;

    private static final int COLOR_FULL = 255;
    private final Builder builder;
    private final Component rootView;
    private final AnimatorProperty animatorProperty;
    private int startX = 0;
    private int endX = 0;
    private int startY = 0;
    private int endY = 0;
    private boolean isShow;
    private PopupWindowListener listener;

    private SlidePopupWindow(Builder builder, int res) {
        super(builder.context);
        this.builder = builder;
        rootView = LayoutScatter.getInstance(builder.context).parse(res, this, false);
        animatorProperty = rootView.createAnimatorProperty();
        initView();
        initListener();
    }

    private void initView() {
        setVisibility(HIDE);
        setWidth(ScreenUtils.getScreenWidth(mContext));
        setHeight(ScreenUtils.getScreenHeight(mContext) - Constants.NUMBER_168);
        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(0, 0, 0, (int) (COLOR_FULL * builder.alpha)));
        setBackground(element);
        LayoutConfig config = (LayoutConfig) rootView.getLayoutConfig();
        switch (builder.direction) {
            case SLIDE_FROM_TOP:
                config.addRule(LayoutConfig.ALIGN_PARENT_TOP);
                config.setMargins(0, -rootView.getHeight(), 0, 0);
                startY = -rootView.getHeight();
                break;
            case SLIDE_FROM_BOTTOM:
                config.addRule(LayoutConfig.ALIGN_PARENT_BOTTOM);
                config.setMargins(0, 0, 0, -rootView.getHeight());
                startY = getHeight();
                endY = getHeight() - rootView.getHeight();
                break;
            case SLIDE_FROM_LEFT:
                config.addRule(LayoutConfig.ALIGN_PARENT_LEFT);
                config.setMargins(-rootView.getWidth(), 0, 0, 0);
                startX = -rootView.getWidth();
                break;
            case SLIDE_FROM_RIGHT:
                config.addRule(LayoutConfig.ALIGN_PARENT_RIGHT);
                config.setMargins(0, 0, -rootView.getWidth(), 0);
                startX = getWidth();
                endX = getWidth() - rootView.getWidth();
                break;
            default:
                break;
        }
        rootView.setLayoutConfig(config);
        rootView.setTouchEventListener((component, touchEvent) -> true);
        addComponent(rootView);
    }

    private void initListener() {
        setTouchEventListener(this);
        animatorProperty.setStateChangedListener(new MyAnimatorStateChangeListener());
    }

    /**
     * MyAnimatorStateChangeListener
     *
     * @since 2021-09-07
     */
    private class MyAnimatorStateChangeListener implements Animator.StateChangedListener {
        @Override
        public void onStart(Animator animator) {
            if (isShow) {
                if (listener != null) {
                    listener.windowShow();
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
                if (listener != null) {
                    listener.windowDismiss();
                }
            }
        }

        @Override
        public void onPause(Animator animator) {
        }

        @Override
        public void onResume(Animator animator) {
        }
    }

    /**
     * PopupWindowListener
     *
     * @since 2021-09-07
     */
    public interface PopupWindowListener {
        /**
         * windowShow
         */
        void windowShow();

        /**
         * windowDismiss
         */
        void windowDismiss();
    }

    /**
     * setPopupWindowListener
     *
     * @param popupWindowListener popupWindowListener
     */
    public void setPopupWindowListener(PopupWindowListener popupWindowListener) {
        this.listener = popupWindowListener;
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        hide();
        return true;
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
                    .setDuration(builder.animDuration)
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
                    .setDuration(builder.animDuration)
                    .start();
        }
    }

    /**
     * Builder
     *
     * @author chenweiquan
     * @since 2021-09-07
     */
    public static class Builder {
        private static final float ALPHA_DEFAULT = 0.5f;
        private static final int ANIM_DURATION = 300;
        private final Context context;
        private final float alpha;
        private final int animDuration;
        private final int direction;

        /**
         * constructor of Builder
         *
         * @param context builder
         */
        public Builder(Context context) {
            this.context = context;
            alpha = ALPHA_DEFAULT;
            animDuration = ANIM_DURATION;
            direction = SLIDE_FROM_BOTTOM;
        }

        /**
         * getView of Builder
         *
         * @param res res
         * @return SlidePopupWindow
         */
        public SlidePopupWindow create(int res) {
            return new SlidePopupWindow(this, res);
        }
    }
}
