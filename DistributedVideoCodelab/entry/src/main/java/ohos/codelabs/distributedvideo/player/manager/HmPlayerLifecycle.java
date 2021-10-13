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

package ohos.codelabs.distributedvideo.player.manager;

import ohos.codelabs.distributedvideo.player.api.ImplLifecycle;
import ohos.codelabs.distributedvideo.player.api.ImplPlayer;
import ohos.codelabs.distributedvideo.player.constant.PlayerStatu;

/**
 * HmPlayerLifecycle
 *
 * @since 2021-04-09
 *
 */
public class HmPlayerLifecycle implements ImplLifecycle {
    private final ImplPlayer mPlayer;

    /**
     * HmPlayerLifecycle
     *
     * @param player player
     */
    public HmPlayerLifecycle(ImplPlayer player) {
        mPlayer = player;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onForeground() {
        String url = mPlayer.getBuilder().getFilePath();
        int startMillisecond = mPlayer.getBuilder().getStartMillisecond();
        mPlayer.reload(url, startMillisecond);
    }

    @Override
    public void onBackground() {
        mPlayer.getBuilder().setPause(mPlayer.getPlayerStatu() == PlayerStatu.PAUSE);
        mPlayer.getBuilder().setStartMillisecond(mPlayer.getCurrentPosition());
        mPlayer.release();
    }

    @Override
    public void onStop() {
        mPlayer.release();
    }
}
