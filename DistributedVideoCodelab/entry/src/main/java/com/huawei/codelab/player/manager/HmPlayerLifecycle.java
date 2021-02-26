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

package com.huawei.codelab.player.manager;

import com.huawei.codelab.player.api.ImplLifecycle;
import com.huawei.codelab.player.api.ImplPlayer;
import com.huawei.codelab.player.constant.PlayerStatus;
import com.huawei.codelab.util.LogUtil;

/**
 * HmPlayerLifecycle
 *
 * @since 2020-12-04
 */
public class HmPlayerLifecycle implements ImplLifecycle {
    private static final String TAG = HmPlayerLifecycle.class.getSimpleName();
    private ImplPlayer player;
    private boolean isBackGround;

    /**
     * constructor of HmPlayerLifecycle
     *
     * @param player player
     */
    public HmPlayerLifecycle(ImplPlayer player) {
        this.player = player;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onForeground() {
        if (isBackGround) {
            String url = player.getBuilder().getFilePath();
            int startTime = player.getBuilder().getStartMillisecond();
            player.reload(url, startTime);
            isBackGround = false;
        }
    }

    @Override
    public void onBackground() {
        if (!isBackGround) {
            LogUtil.info(TAG, "onBackground is called ,palyer statu is " + player.getPlayerStatus().getStatus());
            player.getBuilder().setPause(player.getPlayerStatus() == PlayerStatus.PAUSE);
            player.getBuilder().setFilePath(player.getBuilder().getFilePath());
            player.getBuilder().setStartMillisecond(player.getAudioCurrentPosition());
            player.release();
            isBackGround = true;
        }
    }

    @Override
    public void onStop() {
    }
}
