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

package com.huawei.harmonyaudiodemo.player;

import com.huawei.harmonyaudiodemo.constant.Const;
import com.huawei.harmonyaudiodemo.player.api.ImplHmPlayer;
import com.huawei.harmonyaudiodemo.player.api.ImplLifecycle;
import com.huawei.harmonyaudiodemo.player.api.ScreenChangeListener;
import com.huawei.harmonyaudiodemo.player.api.StatuChangeListener;
import com.huawei.harmonyaudiodemo.player.constant.PlayerStatu;
import com.huawei.harmonyaudiodemo.player.factory.SourceFactory;
import com.huawei.harmonyaudiodemo.player.manager.HmPlayerLifecycle;
import com.huawei.harmonyaudiodemo.util.LogUtil;

import ohos.agp.graphics.Surface;
import ohos.app.Context;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.media.common.Source;
import ohos.media.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Hm player
 *
 * @since 2021-04-04
 */
public class HmPlayer implements ImplHmPlayer {
    private static final String TAG = HmPlayer.class.getSimpleName();
    private static final int MICRO_MILLI_RATE = 1000;
    private Player mPlayer;
    private Surface surface;
    private final HmPlayerLifecycle mLifecycle;
    private final Builder mBuilder;
    private PlayerStatu mStatu = PlayerStatu.IDEL;
    private float currentVolume = 1;
    private double videoScale = Const.NUMBER_NEGATIVE_1;
    private boolean isGestureOpen = true;

    private final List<StatuChangeListener> statuChangeCallbacks = new ArrayList<>(0);
    private final List<ScreenChangeListener> screenChangeCallbacks = new ArrayList<>(0);

    /**
     * constructor of HmPlayer
     *
     * @param builder builder
     */
    private HmPlayer(Builder builder) {
        mBuilder = builder;
        mLifecycle = new HmPlayerLifecycle(this);
    }

    private void initBasePlayer() {
        mPlayer = new Player(mBuilder.mContext);
        Source source = new SourceFactory(mBuilder.mContext, mBuilder.filePath).getSource();
        mPlayer.setSource(source);
        mPlayer.setPlayerCallback(new HmPlayerCallback());
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
            for (StatuChangeListener callback : statuChangeCallbacks) {
                mStatu = PlayerStatu.PREPARED;
                callback.statuCallback(PlayerStatu.PREPARED);
            }
        }

        @Override
        public void onMessage(int info, int i1) {
            LogUtil.info(TAG, "onMessage info is " + info + ",i1 is" + i1);
            if (i1 == 0) {
                switch (info) {
                    case Player.PLAYER_INFO_VIDEO_RENDERING_START:
                        prepareEnd();
                        break;
                    case Player.PLAYER_INFO_BUFFERING_START:
                        for (StatuChangeListener callback : statuChangeCallbacks) {
                            mStatu = PlayerStatu.BUFFERING;
                            callback.statuCallback(PlayerStatu.BUFFERING);
                        }
                        break;
                    case Player.PLAYER_INFO_BUFFERING_END:
                        for (StatuChangeListener callback : statuChangeCallbacks) {
                            mStatu = PlayerStatu.PLAY;
                            callback.statuCallback(PlayerStatu.PLAY);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onError(int type, int extra) {
            LogUtil.info(TAG, "onError is called ,i is " + type + ",i1 is " + extra);
            for (StatuChangeListener callback : statuChangeCallbacks) {
                mStatu = PlayerStatu.ERROR;
                callback.statuCallback(PlayerStatu.ERROR);
            }
            release();
        }

        @Override
        public void onResolutionChanged(int videoX, int videoY) {
            LogUtil.info(TAG, "onResolutionChanged videoX is " + videoX + ",videoY is " + videoY);
            if (!mBuilder.isStretch && videoX != 0 && videoY != 0) {
                videoScale = (double) videoX / videoY;
                for (ScreenChangeListener screenChangeCallback : screenChangeCallbacks) {
                    screenChangeCallback.screenCallback(0, 0);
                }
            }
        }

        @Override
        public void onPlayBackComplete() {
            for (StatuChangeListener callback : statuChangeCallbacks) {
                mStatu = PlayerStatu.COMPLETE;
                callback.statuCallback(PlayerStatu.COMPLETE);
            }
        }

        @Override
        public void onRewindToComplete() {
            resume();
        }

        @Override
        public void onBufferingChange(int value) {
        }

        @Override
        public void onNewTimedMetaData(Player.MediaTimedMetaData mediaTimedMetaData) {
        }

        @Override
        public void onMediaTimeIncontinuity(Player.MediaTimeInfo mediaTimeInfo) {
            LogUtil.info(TAG, "onMediaTimeIncontinuity is called");
            Player.StreamInfo[] streamInfos = mPlayer.getStreamInfo();
            if (streamInfos != null) {
                for (Player.StreamInfo streanInfo : streamInfos) {
                    int streamType = streanInfo.getStreamType();
                    if (streamType == Player.StreamInfo.MEDIA_STREAM_TYPE_AUDIO && mStatu == PlayerStatu.PREPARED) {
                        prepareEnd();
                    }
                }
            }
        }
    }

    private void prepareEnd() {
        for (StatuChangeListener callback : statuChangeCallbacks) {
            mStatu = PlayerStatu.PLAY;
            callback.statuCallback(PlayerStatu.PLAY);
        }
        if (mBuilder.isPause) {
            pause();
        }
    }

    /**
     * start time consuming operation
     */
    private void start() {
        if (mPlayer != null) {
            mBuilder.mContext.getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(() -> {
                if (surface != null) {
                    mPlayer.setVideoSurface(surface);
                } else {
                    LogUtil.error(TAG, "The surface has not been initialized.");
                }
                mPlayer.prepare();
                if (mBuilder.startMillisecond > 0) {
                    int microsecond = mBuilder.startMillisecond * MICRO_MILLI_RATE;
                    mPlayer.rewindTo(microsecond);
                }
                mPlayer.play();
            });
        }
    }

    @Override
    public ImplLifecycle getLifecycle() {
        return mLifecycle;
    }

    @Override
    public void addSurface(Surface videoSurface) {
        this.surface = videoSurface;
    }

    @Override
    public void addPlayerStatuCallback(StatuChangeListener callback) {
        if (callback != null) {
            statuChangeCallbacks.add(callback);
        }
    }

    @Override
    public void removePlayerStatuCallback(StatuChangeListener callback) {
        statuChangeCallbacks.remove(callback);
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
        return mBuilder;
    }

    @Override
    public PlayerStatu getPlayerStatu() {
        return mStatu;
    }

    @Override
    public void resizeScreen(int width, int height) {
        for (ScreenChangeListener screenChangeCallback : screenChangeCallbacks) {
            screenChangeCallback.screenCallback(width, height);
        }
    }

    @Override
    public void gestureSwitch(boolean isOpen) {
        isGestureOpen = isOpen;
    }

    @Override
    public boolean isGestureOpen() {
        return isGestureOpen;
    }

    @Override
    public void play() {
        if (mPlayer != null) {
            mPlayer.reset();
        }
        for (StatuChangeListener callback : statuChangeCallbacks) {
            mStatu = PlayerStatu.PREPARING;
            callback.statuCallback(PlayerStatu.PREPARING);
        }
        initBasePlayer();
        start();
    }

    @Override
    public void replay() {
        if (isPlaying()) {
            rewindTo(0);
        } else {
            reload(mBuilder.filePath, 0);
        }
    }

    @Override
    public void reload(String filepath, int startMillisecond) {
        mBuilder.filePath = filepath;
        mBuilder.startMillisecond = startMillisecond;
        play();
    }

    @Override
    public void stop() {
        if (mPlayer == null) {
            return;
        }
        mPlayer.stop();
        for (StatuChangeListener callback : statuChangeCallbacks) {
            mStatu = PlayerStatu.STOP;
            callback.statuCallback(PlayerStatu.STOP);
        }
    }

    @Override
    public void release() {
        if (mPlayer == null) {
            return;
        }
        if (mStatu != PlayerStatu.IDEL) {
            videoScale = Const.NUMBER_NEGATIVE_1;
            mPlayer.release();
            for (StatuChangeListener callback : statuChangeCallbacks) {
                mStatu = PlayerStatu.IDEL;
                callback.statuCallback(PlayerStatu.IDEL);
            }
        }
    }

    @Override
    public void resume() {
        if (mPlayer == null) {
            return;
        }
        if (mStatu != PlayerStatu.IDEL) {
            if (!isPlaying()) {
                mPlayer.play();
            }
            for (StatuChangeListener callback : statuChangeCallbacks) {
                mStatu = PlayerStatu.PLAY;
                callback.statuCallback(PlayerStatu.PLAY);
            }
        }
    }

    @Override
    public void pause() {
        if (mPlayer == null) {
            return;
        }
        if (isPlaying()) {
            mPlayer.pause();
            for (StatuChangeListener callback : statuChangeCallbacks) {
                mStatu = PlayerStatu.PAUSE;
                callback.statuCallback(PlayerStatu.PAUSE);
            }
        }
    }

    @Override
    public int getCurrentPosition() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getCurrentTime();
    }

    @Override
    public int getDuration() {
        if (mPlayer == null) {
            return 0;
        }
        return mPlayer.getDuration();
    }

    @Override
    public float getVolume() {
        return currentVolume;
    }

    @Override
    public void setVolume(float volume) {
        if (mPlayer != null && mPlayer.setVolume(volume)) {
            currentVolume = volume;
        }
    }

    @Override
    public void setPlaySpeed(float speed) {
        if (mPlayer == null) {
            return;
        }
        if (mStatu != PlayerStatu.IDEL) {
            mPlayer.setPlaybackSpeed(speed);
        }
    }

    @Override
    public double getVideoScale() {
        return videoScale;
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isNowPlaying();
    }

    @Override
    public void rewindTo(int startMicrosecond) {
        if (mPlayer == null) {
            return;
        }
        if (mStatu != PlayerStatu.IDEL) {
            for (StatuChangeListener callback : statuChangeCallbacks) {
                mStatu = PlayerStatu.BUFFERING;
                callback.statuCallback(PlayerStatu.BUFFERING);
            }
            mPlayer.rewindTo(startMicrosecond * MICRO_MILLI_RATE);
        }
    }

    /**
     * Builder
     *
     * @since 2020-12-04
     */
    public static class Builder {
        private final Context mContext;
        private String filePath;
        private int startMillisecond;
        private boolean isStretch;
        private boolean isPause;

        /**
         * constructor of Builder
         *
         * @param context context
         */
        public Builder(Context context) {
            mContext = context;
            filePath = "";
            startMillisecond = 0;
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
         * setStretch of Builder
         *
         * @param isS isStretch
         * @return Builder
         */
        public Builder setStretch(boolean isS) {
            this.isStretch = isS;
            return this;
        }

        /**
         * setPause of Builder
         *
         * @param isP isPause
         * @return Builder
         */
        public Builder setPause(boolean isP) {
            this.isPause = isP;
            return this;
        }

        /**
         * create of Builder
         *
         * @return IPlayer
         */
        public ImplHmPlayer create() {
            return new HmPlayer(this);
        }
    }
}
