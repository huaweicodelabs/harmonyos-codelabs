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

package com.huawei.codelab.slice;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.component.SimplePlayerController;
import com.huawei.codelab.utils.LogUtils;
import com.huawei.codelab.utils.WindowManagerUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.agp.window.service.WindowManager;
import ohos.global.resource.RawFileDescriptor;
import ohos.media.common.Source;
import ohos.media.player.Player;

import java.io.IOException;

/**
 * MoviePlayAbilitySlice
 *
 * @since 2021-03-05
 */
public class MoviePlayAbilitySlice extends AbilitySlice {
    private static final String TAG = "MoviePlayAbilitySlice";
    private static final String MOVIE_URL = "entry/resources/base/media/gubeishuizhen.mp4";

    private Player player;

    private final SurfaceOps.Callback mSurfaceCallback = new SurfaceOps.Callback() {
        @Override
        public void surfaceCreated(SurfaceOps surfaceOps) {
            Surface surface = surfaceOps.getSurface();
            player.setVideoSurface(surface);
            player.prepare();
            player.play();
        }

        @Override
        public void surfaceChanged(SurfaceOps surfaceOps, int intI, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceOps surfaceOps) {
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_movie_play);

        // 全屏设置
        WindowManagerUtils.setWindows();
        WindowManager.getInstance().getTopWindow().get().setTransparent(true); // 不设置窗体透明会挡住播放内容

        SurfaceProvider surfaceView = new SurfaceProvider(this);
        DependentLayout.LayoutConfig layoutConfig = new DependentLayout.LayoutConfig();
        layoutConfig.addRule(DependentLayout.LayoutConfig.CENTER_IN_PARENT);
        surfaceView.setLayoutConfig(layoutConfig);
        surfaceView.setVisibility(Component.VISIBLE);
        surfaceView.setFocusable(Component.FOCUS_ENABLE);
        surfaceView.setTouchFocusable(true);
        surfaceView.requestFocus();
        surfaceView.pinToZTop(false);
        surfaceView.getSurfaceOps().get().addCallback(mSurfaceCallback);

        player = new Player(this);
        if (findComponentById(ResourceTable.Id_parent_layout) instanceof DependentLayout) {
            DependentLayout dependentLayout = (DependentLayout)
                    findComponentById(ResourceTable.Id_parent_layout);
            SimplePlayerController simplePlayerController = new SimplePlayerController(this, player);
            dependentLayout.addComponent(surfaceView);
            dependentLayout.addComponent(simplePlayerController);
        }

        try {
            RawFileDescriptor filDescriptor = getResourceManager().getRawFileEntry(MOVIE_URL).openRawFileDescriptor();
            Source source = new Source(filDescriptor.getFileDescriptor(),
                    filDescriptor.getStartPosition(), filDescriptor.getFileSize());
            player.setSource(source);
        } catch (IOException e) {
            LogUtils.info(TAG, "=== play movie fail");
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onInactive() {
        player.release();
        terminateAbility();
    }
}
