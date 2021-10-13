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

package com.huawei.codelab.component;

import com.huawei.codelab.ResourceTable;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;
import ohos.media.player.Player;

/**
 * SimplePlayerController
 *
 * @since 2021-03-08
 */
public class SimplePlayerController extends ComponentContainer {
    private final Player controllerPlayer;
    private final Context mainContext;
    private Image playToggle;

    /**
     * Constructor
     *
     * @param context context
     * @param player player
     */
    public SimplePlayerController(Context context, Player player) {
        super(context);
        mainContext = context;
        controllerPlayer = player;
        initView();
        initListener();
    }

    private void initView() {
        Component playerController =
                LayoutScatter.getInstance(mainContext)
                        .parse(ResourceTable.Layout_simple_player_controller_layout, null, false);
        addComponent(playerController);
        if (findComponentById(ResourceTable.Id_play_controller) instanceof Image) {
            playToggle = (Image) findComponentById(ResourceTable.Id_play_controller);
        }
    }

    private void initListener() {
        playToggle.setClickedListener(component -> {
            if (controllerPlayer.isNowPlaying()) {
                controllerPlayer.pause();
                playToggle.setPixelMap(ResourceTable.Media_video_play);
            } else {
                controllerPlayer.play();
                playToggle.setPixelMap(ResourceTable.Media_video_stop);
            }
        });
    }
}
