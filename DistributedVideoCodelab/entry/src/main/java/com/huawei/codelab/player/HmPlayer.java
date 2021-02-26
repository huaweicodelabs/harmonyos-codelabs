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

package com.huawei.codelab.player;

import com.huawei.codelab.player.api.ImplLifecycle;
import com.huawei.codelab.player.api.ImplPlayer;
import com.huawei.codelab.player.api.ScreenChangeListener;
import com.huawei.codelab.player.api.StatusChangeListener;
import com.huawei.codelab.player.constant.PlayerStatus;
import com.huawei.codelab.player.factory.SourceFactory;
import com.huawei.codelab.player.manager.HmPlayerLifecycle;
import com.huawei.codelab.util.DateUtils;
import com.huawei.codelab.util.LogUtil;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.media.audio.AudioManager;
import ohos.media.audio.AudioRemoteException;
import ohos.media.common.Source;
import ohos.media.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Hm player
 *
 * @since 2020-12-04
 */
public class HmPlayer implements ImplPlayer {
    private static final String TAG = "HmPlayer";
    private static final int MICRO_MILLI_RATE = 1000;
    private static final int MAX_MUSIC_VOLUME = 100;
    private static final int MIM_MUSIC_VOLUME = 0;
    private static final int CAPACITY = 6;
    private static final int CORE_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 20;
    private static final int KEEP_ALIVE_TIME = 3;
    private Player player;
    private SurfaceProvider surfaceView;
    private Surface surface;
    private AudioManager audio;
    private HmPlayerLifecycle lifecycle;
    private Builder playerBuilder;
    private PlayerStatus status = PlayerStatus.IDEL;

    private List<StatusChangeListener> statusChangeCallbacks = new ArrayList<>(0);
    private List<ScreenChangeListener> screenChangeCallbacks = new ArrayList<>(0);
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
            KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(CAPACITY),
            new ThreadPoolExecutor.DiscardOldestPolicy());
    private boolean isSurfaceViewCreated;

    private SurfaceOps.Callback surfaceCallback = new SurfaceOps.Callback() {
        @Override
        public void surfaceCreated(SurfaceOps surfaceOps) {
            LogUtil.info(TAG, "surfaceCreated");
            isSurfaceViewCreated = true;
            surface = surfaceOps.getSurface();
            start();
        }

        @Override
        public void surfaceChanged(SurfaceOps surfaceOps, int i, int width, int height) {
            LogUtil.info(TAG, "surfaceChanged i is " + i + ",width is " + width + ",height is " + height);
        }

        @Override
        public void surfaceDestroyed(SurfaceOps surfaceOps) {
            LogUtil.info(TAG, "surfaceDestroyed");
            isSurfaceViewCreated = false;
        }
    };

    /**
     * constructor of HmPlayer
     *
     * @param builder builder
     */
    private HmPlayer(Builder builder) {
        playerBuilder = builder;
        WindowManager.getInstance().getTopWindow().get().setTransparent(true); // 不设置窗体透明会挡住播放内容，除非设置pinToZTop为true
        audio = new AudioManager(playerBuilder.mContext.getBundleName());
        lifecycle = new HmPlayerLifecycle(this);
        surfaceView = new SurfaceProvider(playerBuilder.mContext);
        DependentLayout.LayoutConfig layoutConfig = new DependentLayout.LayoutConfig();
        layoutConfig.addRule(DependentLayout.LayoutConfig.CENTER_IN_PARENT);
        surfaceView.setLayoutConfig(layoutConfig);
        surfaceView.setVisibility(Component.VISIBLE);
        surfaceView.setFocusable(Component.FOCUS_ENABLE);
        surfaceView.setTouchFocusable(true);
        surfaceView.requestFocus();
        surfaceView.pinToZTop(playerBuilder.isTopPlay);
        surfaceView.getSurfaceOps().get().addCallback(surfaceCallback);
    }

    /**
     * HmPlayer Callback
     *
     * @since 2020-12-04
     */
    private class HmPlayerCallback implements Player.IPlayerCallback {
        @Override
        public void onPrepared() {
            LogUtil.info(TAG, "onPrepared is called ");
            for (StatusChangeListener callback : statusChangeCallbacks) {
                status = PlayerStatus.PREPARED;
                callback.statusCallback(PlayerStatus.PREPARED);
            }
        }

        @Override
        public void onMessage(int info, int i1) {
            LogUtil.info(TAG, "onMessage info is " + info + ",i1 is" + i1);
            if (info == Player.PLAYER_INFO_VIDEO_RENDERING_START && i1 == 0) {
                // start to rendering ，show controller
                for (StatusChangeListener callback : statusChangeCallbacks) {
                    status = PlayerStatus.PLAY;
                    callback.statusCallback(PlayerStatus.PLAY);
                }
            } else if (info == Player.PLAYER_INFO_BUFFERING_START && i1 == 0) {
                for (StatusChangeListener callback : statusChangeCallbacks) {
                    status = PlayerStatus.BUFFERING;
                    callback.statusCallback(PlayerStatus.BUFFERING);
                }
            } else if (info == Player.PLAYER_INFO_BUFFERING_END && i1 == 0) {
                for (StatusChangeListener callback : statusChangeCallbacks) {
                    status = PlayerStatus.PLAY;
                    callback.statusCallback(PlayerStatus.PLAY);
                }
            } else {
                LogUtil.info(TAG, "onMessage else message");
            }
        }

        @Override
        public void onError(int i, int i1) {
            LogUtil.info(TAG, "onError is called ,i is " + i + ",i1 is " + i1);
            for (StatusChangeListener callback : statusChangeCallbacks) {
                status = PlayerStatus.ERROR;
                callback.statusCallback(PlayerStatus.ERROR);
            }
            release();
        }

        @Override
        public void onResolutionChanged(int x, int y) {
            LogUtil.info(TAG, "onResolutionChanged i is " + x + ",i1 is " + y);
            if (!playerBuilder.isStretch && x != 0 && y != 0) {
                getBuilder().mContext.getUITaskDispatcher().delayDispatch(new Runnable() {
                    @Override
                    public void run() {
                        if (x > y) {
                            surfaceView.setWidth(surfaceView.getWidth());
                            surfaceView.setHeight(Math.min(y * surfaceView.getWidth() / x, surfaceView.getHeight()));
                        } else {
                            surfaceView.setHeight(surfaceView.getHeight());
                            surfaceView.setWidth(Math.min(x * surfaceView.getHeight() / y, surfaceView.getWidth()));
                        }
                    }
                }, 0);
            }
        }

        @Override
        public void onPlayBackComplete() {
            for (StatusChangeListener callback : statusChangeCallbacks) {
                status = PlayerStatus.COMPLETE;
                callback.statusCallback(PlayerStatus.COMPLETE);
            }
            stop();
        }

        @Override
        public void onRewindToComplete() {
            resume();
            if (playerBuilder.isPause) {
                playerBuilder.isPause = false;
                pause();
            }
        }

        @Override
        public void onBufferingChange(int i) {
        }

        @Override
        public void onNewTimedMetaData(Player.MediaTimedMetaData mediaTimedMetaData) {
            LogUtil.info(TAG, "onNewTimedMetaData is called");
        }

        @Override
        public void onMediaTimeIncontinuity(Player.MediaTimeInfo mediaTimeInfo) {
            LogUtil.info(TAG, "onMediaTimeIncontinuity is called");
        }
    }

    /**
     * start time consuming operation
     */
    private void start() {
        if (isSurfaceViewCreated) {
            threadPoolExecutor.execute(() -> {
                player.setVideoSurface(surface);
                player.prepare();
                if (playerBuilder.startMillisecond > 0) {
                    int microsecond = playerBuilder.startMillisecond * MICRO_MILLI_RATE;
                    player.rewindTo(microsecond);
                } else {
                    player.play();
                }
            });
        }
    }

    @Override
    public SurfaceProvider getPlayerView() {
        return surfaceView;
    }

    @Override
    public ImplLifecycle getLifecycle() {
        return lifecycle;
    }

    @Override
    public void addPlayerStatusCallback(StatusChangeListener callback) {
        if (callback != null) {
            statusChangeCallbacks.add(callback);
        }
    }

    @Override
    public void removePlayerStatuCallback(StatusChangeListener callback) {
        statusChangeCallbacks.remove(callback);
    }

    @Override
    public void addPlayerViewCallback(ScreenChangeListener callback) {
        if (callback != null) {
            screenChangeCallbacks.add(callback);
        }
    }

    @Override
    public void removePlayerViewCallback(ScreenChangeListener callback) {
        screenChangeCallbacks.remove(callback);
    }

    @Override
    public Builder getBuilder() {
        return playerBuilder;
    }

    @Override
    public PlayerStatus getPlayerStatus() {
        return status;
    }

    @Override
    public void play() {
        for (StatusChangeListener callback : statusChangeCallbacks) {
            status = PlayerStatus.PREPARING;
            callback.statusCallback(PlayerStatus.PREPARING);
        }
        release();
        player = new Player(playerBuilder.mContext);
        player.setPlayerCallback(new HmPlayerCallback());
        Source source = new SourceFactory(playerBuilder.mContext, playerBuilder.filePath).getSource();
        player.setSource(source);
        start();
    }

    @Override
    public void replay() {
        if (isPlaying()) {
            rewindTo(0);
        } else {
            reload(playerBuilder.filePath, 0);
        }
    }

    @Override
    public void reload(String filepath, int startMillisecond) {
        playerBuilder.filePath = filepath;
        playerBuilder.startMillisecond = startMillisecond;
        play();
    }

    @Override
    public void stop() {
        if (player == null) {
            return;
        }
        player.stop();
        for (StatusChangeListener callback : statusChangeCallbacks) {
            status = PlayerStatus.STOP;
            callback.statusCallback(PlayerStatus.STOP);
        }
    }

    @Override
    public void release() {
        if (player == null) {
            return;
        }
        if (status != PlayerStatus.IDEL) {
            player.release();
            for (StatusChangeListener callback : statusChangeCallbacks) {
                status = PlayerStatus.IDEL;
                callback.statusCallback(PlayerStatus.IDEL);
            }
        }
    }

    /**
     * resume playback
     */
    @Override
    public void resume() {
        if (player == null) {
            return;
        }
        if (status != PlayerStatus.IDEL) {
            if (!isPlaying()) {
                player.play();
            }
            for (StatusChangeListener callback : statusChangeCallbacks) {
                status = PlayerStatus.PLAY;
                callback.statusCallback(PlayerStatus.PLAY);
            }
        }
    }

    /**
     * pause playback
     */
    @Override
    public void pause() {
        if (player == null) {
            return;
        }
        if (isPlaying()) {
            player.pause();
            for (StatusChangeListener callback : statusChangeCallbacks) {
                status = PlayerStatus.PAUSE;
                callback.statusCallback(PlayerStatus.PAUSE);
            }
        }
    }

    /**
     * Get audio current play position
     *
     * @return play position
     */
    @Override
    public int getAudioCurrentPosition() {
        if (player == null) {
            return 0;
        }
        return player.getCurrentTime();
    }

    /**
     * Get audio duration
     *
     * @return audio duration
     */
    @Override
    public int getAudioDuration() {
        if (player == null) {
            return 0;
        }
        return player.getDuration();
    }

    @Override
    public int getVolume() {
        int curVolume = 0;
        if (audio == null) {
            return curVolume;
        }
        try {
            curVolume = audio.getVolume(AudioManager.AudioVolumeType.STREAM_MUSIC);
        } catch (AudioRemoteException e) {
            LogUtil.error(TAG, "get current volume failed");
        }
        return curVolume;
    }

    @Override
    public void setVolume(int volume) {
        if (audio == null) {
            return;
        }
        if (volume > MAX_MUSIC_VOLUME) {
            audio.setVolume(AudioManager.AudioVolumeType.STREAM_MUSIC, MAX_MUSIC_VOLUME);
        } else if (volume < MIM_MUSIC_VOLUME) {
            audio.setVolume(AudioManager.AudioVolumeType.STREAM_MUSIC, MIM_MUSIC_VOLUME);
        } else {
            audio.setVolume(AudioManager.AudioVolumeType.STREAM_MUSIC, volume);
        }
    }

    /**
     * set play speed
     *
     * @param speed 0~12
     */
    @Override
    public void setPlaySpeed(float speed) {
        if (player == null) {
            return;
        }
        if (status != PlayerStatus.IDEL) {
            player.setPlaybackSpeed(speed);
        }
    }

    @Override
    public void changeScreen() {
    }

    /**
     * whether player is running or not
     *
     * @return isPlaying boolean
     */
    @Override
    public boolean isPlaying() {
        return player.isNowPlaying();
    }

    /**
     * Rewind to specified time position
     *
     * @param startMicrosecond time
     */
    @Override
    public void rewindTo(int startMicrosecond) {
        if (player == null) {
            return;
        }
        if (status != PlayerStatus.IDEL) {
            for (StatusChangeListener callback : statusChangeCallbacks) {
                status = PlayerStatus.BUFFERING;
                callback.statusCallback(PlayerStatus.BUFFERING);
            }
            player.rewindTo(startMicrosecond * MICRO_MILLI_RATE);
        }
    }

    @Override
    public String getDurationText() {
        return DateUtils.msToString(getAudioDuration());
    }

    @Override
    public String getCurrentText() {
        return DateUtils.msToString(getAudioCurrentPosition());
    }

    /**
     * Builder
     *
     * @since 2020-12-04
     */
    public static class Builder {
        private Context mContext;
        private String filePath = "";
        private int startMillisecond = 0;
        private boolean isTopPlay;
        private boolean isStretch;
        private boolean isPause;

        /**
         * constructor of Builder
         *
         * @param context context
         */
        public Builder(Context context) {
            mContext = context;
        }

        /**
         * setFilePath of Builder
         *
         * @param filePath filePath
         * @return builder
         */
        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        /**
         * getFilePath of Builder
         *
         * @return filePath
         */
        public String getFilePath() {
            return filePath;
        }

        /**
         * setStartMillisecond of Builder
         *
         * @param startMillisecond startMillisecond
         * @return builder
         */
        public Builder setStartMillisecond(int startMillisecond) {
            this.startMillisecond = startMillisecond;
            return this;
        }

        /**
         * getStartMillisecond of Builder
         *
         * @return startMillisecond
         */
        public int getStartMillisecond() {
            return startMillisecond;
        }

        /**
         * setTopPlay of Builder
         *
         * @param topPlay topPlay
         * @return Builder
         */
        public Builder setTopPlay(boolean topPlay) {
            isTopPlay = topPlay;
            return this;
        }

        /**
         * setStretch of Builder
         *
         * @param stretch stretch
         * @return Builder
         */
        public Builder setStretch(boolean stretch) {
            isStretch = stretch;
            return this;
        }

        /**
         * setPause of Builder
         *
         * @param pause pause
         * @return Builder
         */
        public Builder setPause(boolean pause) {
            isPause = pause;
            return this;
        }

        /**
         * create of ImplPlayer
         *
         * @return ImplPlayer
         */
        public ImplPlayer create() {
            return new HmPlayer(this);
        }
    }
}
