/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
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

package com.huawei.codelab.slice;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.player.HmPlayer;
import com.huawei.codelab.player.api.ImplPlayer;
import com.huawei.codelab.player.constant.Constants;
import com.huawei.codelab.player.view.PlayerLoading;
import com.huawei.codelab.player.view.PlayerView;
import com.huawei.codelab.player.view.SimplePlayerController;
import com.huawei.codelab.util.LogUtil;
import com.huawei.codelab.util.ScreenUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.DependentLayout;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.bundle.AbilityInfo;

/**
 * SimplePlayerAbilitySlice
 *
 * @since 2021-04-04
 */
public class SimplePlayerAbilitySlice extends AbilitySlice {
    private static final String TAG = SimplePlayerAbilitySlice.class.getSimpleName();
    private ImplPlayer player;
    private DependentLayout parentLayout;
    private PlayerView playerView;
    private PlayerLoading loadingView;
    private SimplePlayerController controllerView;
    private String url = "entry/resources/base/media/gubeishuizhen.mp4";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_simple_video_play_layout);
        player = new HmPlayer.Builder(this).setFilePath(url).create();
        player.getLifecycle().onStart();
        initComponent();
    }

    private void initComponent() {
        if (findComponentById(ResourceTable.Id_parent) instanceof DependentLayout) {
            parentLayout = (DependentLayout) findComponentById(ResourceTable.Id_parent);
        }
        if (findComponentById(ResourceTable.Id_player_view) instanceof PlayerView) {
            playerView = (PlayerView) findComponentById(ResourceTable.Id_player_view);
        }
        if (findComponentById(ResourceTable.Id_loading_view) instanceof PlayerLoading) {
            loadingView = (PlayerLoading) findComponentById(ResourceTable.Id_loading_view);
        }
        if (findComponentById(ResourceTable.Id_controller_view) instanceof SimplePlayerController) {
            controllerView = (SimplePlayerController) findComponentById(ResourceTable.Id_controller_view);
        }
        playerView.bind(player);
        loadingView.bind(player);
        controllerView.bind(player);
    }

    @Override
    protected void onOrientationChanged(AbilityInfo.DisplayOrientation displayOrientation) {
        super.onOrientationChanged(displayOrientation);
        int screenWidth = ScreenUtils.getScreenWidth(this);
        parentLayout.setWidth(screenWidth);
        player.openGesture(displayOrientation == AbilityInfo.DisplayOrientation.LANDSCAPE);
    }

    @Override
    public void onActive() {
        super.onActive();
        getGlobalTaskDispatcher(TaskPriority.DEFAULT).delayDispatch(() -> player.play(), Constants.NUMBER_1000);
    }

    @Override
    public void onForeground(Intent intent) {
        player.getLifecycle().onForeground();
        super.onForeground(intent);
    }

    @Override
    protected void onBackground() {
        LogUtil.info(TAG, "onBackground is called");
        player.getLifecycle().onBackground();
        super.onBackground();
    }

    @Override
    protected void onStop() {
        LogUtil.info(TAG, "onStop is called");
        loadingView.unbind();
        controllerView.unbind();
        playerView.unbind();
        player.getLifecycle().onStop();
        super.onStop();
    }
}
