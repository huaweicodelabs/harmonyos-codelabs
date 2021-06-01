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

package com.huawei.harmonyaudiodemo.player.view;

import com.huawei.harmonyaudiodemo.ResourceTable;
import com.huawei.harmonyaudiodemo.player.api.HmPlayModuler;
import com.huawei.harmonyaudiodemo.player.api.ImplHmPlayer;

import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Component.TouchEventListener;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

/**
 * PlayerLoading
 *
 * @since 2021-04-04
 */
public class PlayerLoading extends ComponentContainer implements HmPlayModuler, TouchEventListener {
    private static final int HALF_NUMBER = 2;
    private static final int ANIM_ROTATE = 360;
    private static final int ANIM_DURATION = 2000;
    private static final int ANIM_LOOPED_COUNT = -1;
    private ImplHmPlayer mPlayer;
    private Image mLoading;
    private AnimatorProperty mLoadingAnim;

    /**
     * constructor of PlayerLoading
     *
     * @param context context
     */
    public PlayerLoading(Context context) {
        this(context, null);
    }

    /**
     * constructor of PlayerLoading
     *
     * @param context context
     * @param attrSet attSet
     */
    public PlayerLoading(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    /**
     * constructor of PlayerLoading
     *
     * @param context context
     * @param attrSet attSet
     * @param styleName styleName
     */
    public PlayerLoading(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView(context);
    }

    private void initView(Context context) {
        Component loadingContainer =
                LayoutScatter.getInstance(context).parse(ResourceTable.Layout_player_loading_layout, null, false);
        if (loadingContainer.findComponentById(ResourceTable.Id_image_loading) instanceof Image) {
            mLoading = (Image) loadingContainer.findComponentById(ResourceTable.Id_image_loading);
            initAnim();
        }
        addComponent(loadingContainer);
        hide();
        setTouchEventListener(this);
    }

    private void initAnim() {
        int with = mLoading.getWidth() / HALF_NUMBER;
        int height = mLoading.getHeight() / HALF_NUMBER;
        mLoading.setPivotX(with);
        mLoading.setPivotY(height);
        mLoadingAnim = mLoading.createAnimatorProperty();
        mLoadingAnim.rotate(ANIM_ROTATE).setDuration(ANIM_DURATION).setLoopedCount(ANIM_LOOPED_COUNT);
    }

    private void initListener() {
        mPlayer.addPlayerStatuCallback(statu -> mContext.getUITaskDispatcher().asyncDispatch(() -> {
            switch (statu) {
                case PREPARING:
                case BUFFERING:
                    show();
                    break;
                case PLAY:
                    hide();
                    break;
                default:
                    break;
            }
        }));
    }

    /**
     * show of PlayerLoading
     */
    public void show() {
        if (mLoadingAnim.isPaused()) {
            mLoadingAnim.resume();
        } else {
            mLoadingAnim.start();
        }
        setVisibility(VISIBLE);
    }

    /**
     * hide of PlayerLoading
     */
    public void hide() {
        setVisibility(INVISIBLE);
        mLoadingAnim.pause();
    }

    @Override
    public void bind(ImplHmPlayer player) {
        mPlayer = player;
        initListener();
    }

    @Override
    public void unbind() {
        mLoadingAnim.release();
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        return true;
    }
}
