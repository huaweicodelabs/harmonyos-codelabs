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

package com.huawei.codelab.player.component;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.player.api.ImplPlayer;
import com.huawei.codelab.player.api.StatusChangeListener;
import com.huawei.codelab.player.constant.PlayerStatus;
import com.huawei.codelab.util.LogUtil;

import ohos.agp.animation.AnimatorProperty;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;

/**
 * PlayerLoading
 *
 * @since 2020-12-04
 */
public class PlayerLoading extends ComponentContainer {
    private static final int HALF_NUMBER = 2;
    private static final int ANIM_ROTATE = 360;
    private static final int ANIM_DURATION = 2000;
    private static final int ANIM_LOOPED_COUNT = -1;
    private ImplPlayer player;
    private Image loading;
    private AnimatorProperty loadingAnim;

    /**
     * constructor of PlayerLoading
     *
     * @param context builder
     * @param player player
     */
    public PlayerLoading(Context context, ImplPlayer player) {
        super(context);
        this.player = player;
        initComponent(context);
        initListener();
    }

    private void initComponent(Context context) {
        DependentLayout.LayoutConfig layoutConfig =
                new DependentLayout.LayoutConfig(DependentLayout.LayoutConfig.MATCH_PARENT, LayoutConfig.MATCH_PARENT);
        setLayoutConfig(layoutConfig);
        Component loadingContainer = LayoutScatter.getInstance(context).parse(
                ResourceTable.Layout_player_loading_layout, null, false);
        if (loadingContainer.findComponentById(ResourceTable.Id_image_loading) instanceof Image) {
            loading = (Image) loadingContainer.findComponentById(ResourceTable.Id_image_loading);
        }
        addComponent(loadingContainer);
        initAnim();
        hide();
    }

    private void initAnim() {
        int with = loading.getWidth() / HALF_NUMBER;
        int height = loading.getHeight() / HALF_NUMBER;
        loading.setPivotX(with);
        loading.setPivotY(height);
        loadingAnim = loading.createAnimatorProperty();
        loadingAnim.rotate(ANIM_ROTATE).setDuration(ANIM_DURATION).setLoopedCount(ANIM_LOOPED_COUNT);
    }

    private void initListener() {
        player.addPlayerStatusCallback(new StatusChangeListener() {
            @Override
            public void statusCallback(PlayerStatus status) {
                mContext.getUITaskDispatcher().delayDispatch(
                        new Runnable() {
                            @Override
                            public void run() {
                                if (status == PlayerStatus.PREPARING || status == PlayerStatus.BUFFERING) {
                                    show();
                                } else if (status == PlayerStatus.PLAY) {
                                    hide();
                                } else {
                                    LogUtil.info(PlayerLoading.class.getName(), "statuCallback else message");
                                }
                            }
                        }, 0);
            }
        });
    }

    /**
     * show of PlayerLoading
     */
    public void show() {
        if (loadingAnim.isPaused()) {
            loadingAnim.resume();
        } else {
            loadingAnim.start();
        }
        setVisibility(VISIBLE);
    }

    /**
     * hide of PlayerLoading
     */
    public void hide() {
        setVisibility(INVISIBLE);
        loadingAnim.pause();
    }
}
