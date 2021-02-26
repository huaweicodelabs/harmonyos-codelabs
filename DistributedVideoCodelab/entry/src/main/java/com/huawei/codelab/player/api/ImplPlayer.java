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

package com.huawei.codelab.player.api;

import com.huawei.codelab.player.HmPlayer;
import com.huawei.codelab.player.constant.PlayerStatus;
import ohos.agp.components.surfaceprovider.SurfaceProvider;

/**
 * IPlayer interface
 *
 * @since 2020-12-04
 *
 */
public interface ImplPlayer {
    /**
     * Player
     *
     * @param callback callback
     */
    void addPlayerStatusCallback(StatusChangeListener callback);

    /**
     * Player
     *
     * @param callback callback
     */
    void removePlayerStatuCallback(StatusChangeListener callback);

    /**
     * Player
     *
     * @param callback callback
     */
    void addPlayerViewCallback(ScreenChangeListener callback);

    /**
     * Player
     *
     * @param callback callback
     */
    void removePlayerViewCallback(ScreenChangeListener callback);

    /**
     * play the video
     *
     */
    void play();

    /**
     * replay
     *
     */
    void replay();

    /**
     * reload
     *
     * @param filepath filepath
     * @param startMillisecond startMillisecond
     */
    void reload(String filepath, int startMillisecond);

    /**
     * resume
     *
     */
    void resume();

    /**
     * pause
     *
     */
    void pause();

    /**
     * getAudioCurrentPosition
     *
     * @return int
     */
    int getAudioCurrentPosition();

    /**
     * getAudioDuration
     *
     * @return int
     */
    int getAudioDuration();

    /**
     * getDurationText
     *
     * @return string
     */
    String getDurationText();

    /**
     * getCurrentText
     *
     * @return string
     */
    String getCurrentText();

    /**
     * getVolume
     *
     * @return int
     */
    int getVolume();

    /**
     * setVolume
     *
     * @param volume volume
     */
    void setVolume(int volume);

    /**
     * setPlaySpeed
     *
     * @param speed speed
     */
    void setPlaySpeed(float speed);

    /**
     * change the screen direction
     *
     */
    void changeScreen();

    /**
     * rewindTo
     *
     * @param startMicrosecond startMicrosecond
     */
    void rewindTo(int startMicrosecond);

    /**
     * isPlaying
     *
     * @return true
     */
    boolean isPlaying();

    /**
     * stop the video
     *
     */
    void stop();

    /**
     * release the video
     *
     */
    void release();

    /**
     * getPlayerView
     *
     * @return SurfaceProvider
     */
    SurfaceProvider getPlayerView();

    /**
     * getLifecycle
     *
     * @return ImplLifecycle
     */
    ImplLifecycle getLifecycle();

    /**
     * getBuilder
     *
     * @return Builder
     */
    HmPlayer.Builder getBuilder();

    /**
     * getPlayerStatu
     *
     * @return PlayerStatus.java
     */
    PlayerStatus getPlayerStatus();
}
