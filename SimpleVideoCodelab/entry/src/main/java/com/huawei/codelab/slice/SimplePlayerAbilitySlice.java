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

package com.huawei.codelab.slice;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.player.constant.Constants;
import com.huawei.codelab.player.component.PlayerLoading;
import com.huawei.codelab.player.component.SimplePlayerController;
import com.huawei.codelab.util.AbilitySliceRouteUtil;
import com.huawei.codelab.util.LogUtil;
import com.huawei.codelab.player.HmPlayer;
import com.huawei.codelab.player.api.ImplPlayer;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.DependentLayout;

/**
 * PlayerAbilitySlice
 *
 * @since 2020-12-04
 */
public class SimplePlayerAbilitySlice extends AbilitySlice {
    private static final String TAG = SimplePlayerAbilitySlice.class.getSimpleName();
    private static ImplPlayer implPlayer;
    private int startMillisecond;
    private String url = "entry/resources/base/media/gubeishuizhen.mp4";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_simple_video_play_layout);
        startMillisecond = intent.getIntParam(Constants.INTENT_STARTTIME_PARAM, 0);
        initComponent();
        implPlayer.getLifecycle().onStart();
    }

    public static ImplPlayer getImplPlayer() {
        return implPlayer;
    }

    private void initComponent() {
        Fraction fraction = new Fraction();
        DependentLayout layout = null;
        if (findComponentById(ResourceTable.Id_parent) instanceof DependentLayout) {
            layout = (DependentLayout) findComponentById(ResourceTable.Id_parent);
        }

        DependentLayout playerLayout = null;
        if (findComponentById(ResourceTable.Id_parent_layout) instanceof DependentLayout) {
            playerLayout = (DependentLayout) findComponentById(ResourceTable.Id_parent_layout);
        }
        implPlayer = new HmPlayer.Builder(this).setStartMillisecond(startMillisecond).setFilePath(url).create();
        SimplePlayerController simplePlayerController = new SimplePlayerController(this, implPlayer);
        PlayerLoading playerLoading = new PlayerLoading(this, implPlayer);
        playerLayout.addComponent(implPlayer.getPlayerView());
        playerLayout.addComponent(playerLoading);
        playerLayout.addComponent(simplePlayerController);
        implPlayer.play();
    }

    @Override
    public void onActive() {
        super.onActive();
        AbilitySliceRouteUtil.getInstance().addRoute(this);
    }

    @Override
    protected void onInactive() {
        LogUtil.info(TAG, "onInactive is called");
        super.onInactive();
    }

    @Override
    public void onForeground(Intent intent) {
        implPlayer.getLifecycle().onForeground();
        super.onForeground(intent);
    }

    @Override
    protected void onBackground() {
        LogUtil.info(TAG, "onBackground is called");
        implPlayer.getLifecycle().onBackground();
        super.onBackground();
    }

    @Override
    protected void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        LogUtil.info(TAG, "onStop is called");
        AbilitySliceRouteUtil.getInstance().removeRoute(this);
        implPlayer.getLifecycle().onStop();
        super.onStop();
    }
}
