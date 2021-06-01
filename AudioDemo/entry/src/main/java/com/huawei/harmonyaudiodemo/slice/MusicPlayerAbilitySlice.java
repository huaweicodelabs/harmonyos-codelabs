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

package com.huawei.harmonyaudiodemo.slice;

import com.huawei.harmonyaudiodemo.ResourceTable;
import com.huawei.harmonyaudiodemo.constant.Const;
import com.huawei.harmonyaudiodemo.player.HmPlayer;
import com.huawei.harmonyaudiodemo.player.api.ImplHmPlayer;
import com.huawei.harmonyaudiodemo.player.view.PlayerLoading;
import com.huawei.harmonyaudiodemo.player.view.SimplePlayerController;
import com.huawei.harmonyaudiodemo.util.LogUtil;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;

import java.io.IOException;

/**
 * SimplePlayerAbilitySlice
 *
 * @since 2021-04-04
 */
public class MusicPlayerAbilitySlice extends AbilitySlice {
    private static final String TAG = MusicPlayerAbilitySlice.class.getSimpleName();
    private ImplHmPlayer player;
    private PlayerLoading playerLoading;
    private SimplePlayerController controllerView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_music_player);
        try {
            String mUrl = getResourceManager().getMediaPath(ResourceTable.Media_test);
            player = new HmPlayer.Builder(this)
                    .setFilePath(mUrl)
                    .create();
            initView();
            getGlobalTaskDispatcher(TaskPriority.DEFAULT).delayDispatch(() -> player.play(), Const.NUMBER_100);
        } catch (IOException | NotExistException | WrongTypeException e) {
            LogUtil.error(TAG, "get media path failed!");
        }
    }

    private void initView() {
        if (findComponentById(ResourceTable.Id_loadint_view) instanceof PlayerLoading) {
            playerLoading = (PlayerLoading) findComponentById(ResourceTable.Id_loadint_view);
        }
        if (findComponentById(ResourceTable.Id_controller_view) instanceof SimplePlayerController) {
            controllerView = (SimplePlayerController) findComponentById(ResourceTable.Id_controller_view);
        }
        playerLoading.bind(player);
        controllerView.bind(player);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        LogUtil.info(TAG, "onInactive is called");
        super.onInactive();
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
        playerLoading.unbind();
        controllerView.unbind();
        super.onStop();
    }
}
