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
 * @since 2020-12-04
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
    private final Builder mBuilder;
    private final Component mRootView;
    private final AnimatorProperty animatorProperty;
    private int startX = 0;
    private int endX = 0;
    private int startY = 0;
    private int endY = 0;
    private boolean isShow;
    private PopupWindowListener mListener;

    private SlidePopupWindow(Builder builder, int res) {
        super(builder.mContext);
        mBuilder = builder;
        mRootView = LayoutScatter.getInstance(builder.mContext).parse(res, this, false);
        animatorProperty = mRootView.createAnimatorProperty();
        initView();
        initListener();
    }

    private SlidePopupWindow(Builder builder, Component rootView) {
        super(builder.mContext);
        mBuilder = builder;
        mRootView = rootView;
        animatorProperty = mRootView.createAnimatorProperty();
        initView();
        initListener();
    }

    private void initView() {
        setVisibility(HIDE);
        setWidth(ScreenUtils.getScreenWidth(mContext));
        setHeight(ScreenUtils.getScreenHeight(mContext) - Constants.NUMBER_168);
        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(0, 0, 0, (int) (COLOR_FULL * mBuilder.alpha)));
        setBackground(element);
        LayoutConfig config = (LayoutConfig) mRootView.getLayoutConfig();
        switch (mBuilder.direction) {
            case SLIDE_FROM_TOP:
                config.addRule(LayoutConfig.ALIGN_PARENT_TOP);
                config.setMargins(0, -mRootView.getHeight(), 0, 0);
                startY = -mRootView.getHeight();
                break;
            case SLIDE_FROM_BOTTOM:
                config.addRule(LayoutConfig.ALIGN_PARENT_BOTTOM);
                config.setMargins(0, 0, 0, -mRootView.getHeight());
                startY = getHeight();
                endY = getHeight() - mRootView.getHeight();
                break;
            case SLIDE_FROM_LEFT:
                config.addRule(LayoutConfig.ALIGN_PARENT_LEFT);
                config.setMargins(-mRootView.getWidth(), 0, 0, 0);
                startX = -mRootView.getWidth();
                break;
            case SLIDE_FROM_RIGHT:
                config.addRule(LayoutConfig.ALIGN_PARENT_RIGHT);
                config.setMargins(0, 0, -mRootView.getWidth(), 0);
                startX = getWidth();
                endX = getWidth() - mRootView.getWidth();
                break;
            default:
                break;
        }
        mRootView.setLayoutConfig(config);
        mRootView.setTouchEventListener((component, touchEvent) -> true);
        addComponent(mRootView);
    }

    private void initListener() {
        setTouchEventListener(this);
        animatorProperty.setStateChangedListener(new MyAnimatorStateChangeListener());
    }

    /**
     * MyAnimatorStateChangeListener
     *
     * @since 2020-12-04
     */
    private class MyAnimatorStateChangeListener implements Animator.StateChangedListener {
        @Override
        public void onStart(Animator animator) {
            if (isShow) {
                if (mListener != null) {
                    mListener.windowShow();
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
                if (mListener != null) {
                    mListener.windowDismiss();
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
     * @since 2020-12-04
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
     * @param listener listener
     */
    public void setPopupWindowListener(PopupWindowListener listener) {
        mListener = listener;
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
                    .setDuration(mBuilder.animDuration)
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
                    .setDuration(mBuilder.animDuration)
                    .start();
        }
    }

    /**
     * Builder
     *
     * @author chenweiquan
     * @since 2020-12-04
     */
    public static class Builder {
        private static final float ALPHA_DEFAULT = 0.5f;
        private static final int ANIM_DURATION = 300;
        private final Context mContext;
        private float alpha;
        private int animDuration;
        private int direction;

        /**
         * constructor of Builder
         *
         * @param context builder
         */
        public Builder(Context context) {
            this.mContext = context;
            alpha = ALPHA_DEFAULT;
            animDuration = ANIM_DURATION;
            direction = SLIDE_FROM_BOTTOM;
        }

        public float getAlpha() {
            return alpha;
        }

        /**
         * setAlpha of Builder
         *
         * @param alpha alpha
         * @return Builder
         */
        public Builder setAlpha(float alpha) {
            if (alpha > 1) {
                this.alpha = 1;
            } else {
                this.alpha = Math.max(0, alpha);
            }
            return this;
        }

        public int getAnimDuration() {
            return animDuration;
        }

        /**
         * setAnimDuration of Builder
         *
         * @param animDuration animDuration
         * @return Builder
         */
        public Builder setAnimDuration(int animDuration) {
            this.animDuration = animDuration;
            return this;
        }

        public int getDirection() {
            return direction;
        }

        /**
         * setDirection of Builder
         *
         * @param direction direction
         * @return Builder
         */
        public Builder setDirection(int direction) {
            this.direction = direction;
            return this;
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

        /**
         * getView of Builder
         *
         * @param component res
         * @return SlidePopupWindow
         */
        public SlidePopupWindow create(Component component) {
            return new SlidePopupWindow(this, component);
        }
    }
}
