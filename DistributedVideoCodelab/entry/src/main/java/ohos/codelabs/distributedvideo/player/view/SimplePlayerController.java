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

package ohos.codelabs.distributedvideo.player.view;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Slider;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;
import ohos.codelabs.distributedvideo.ResourceTable;
import ohos.codelabs.distributedvideo.player.api.ImplPlayModule;
import ohos.codelabs.distributedvideo.player.api.ImplPlayer;
import ohos.codelabs.distributedvideo.player.api.StatuChangeListener;
import ohos.codelabs.distributedvideo.player.constant.Constants;
import ohos.codelabs.distributedvideo.player.constant.PlayerStatu;
import ohos.codelabs.distributedvideo.player.util.DateUtils;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

/**
 * PlayerController
 *
 * @since 2021-04-04
 */
public class SimplePlayerController extends ComponentContainer implements ImplPlayModule {
    private static final int THUMB_RED = 255;
    private static final int THUMB_GREEN = 255;
    private static final int THUMB_BLUE = 240;
    private static final int THUMB_WIDTH = 40;
    private static final int THUMB_HEIGHT = 40;
    private static final int THUMB_RADIUS = 20;
    private static final int CONTROLLER_HIDE_DLEY_TIME = 5000;
    private static final int PROGRESS_RUNNING_TIME = 1000;
    private boolean mIsDragMode = false;
    private final Context mContext;
    private ImplPlayer mPlayer;
    private DirectionalLayout bottomLayout;
    private Image mPlayToogle;
    private Image mForward;
    private Image mBackward;
    private Slider mProgressBar;
    private Text mCurrentTime;
    private Text mTotleTime;
    private ControllerHandler mHandler;
    private final StatuChangeListener mStatuChangeListener = new StatuChangeListener() {
        @Override
        public void statuCallback(PlayerStatu statu) {
            mContext.getUITaskDispatcher().asyncDispatch(() -> {
                switch (statu) {
                    case PREPARING:
                        mPlayToogle.setClickable(false);
                        mProgressBar.setEnabled(false);
                        mProgressBar.setProgressValue(0);
                        break;
                    case PREPARED:
                        mProgressBar.setMaxValue(mPlayer.getDuration());
                        mTotleTime.setText(DateUtils.msToString(mPlayer.getDuration()));
                        break;
                    case PLAY:
                        showController(false);
                        mPlayToogle.setPixelMap(ResourceTable.Media_ic_music_stop);
                        mPlayToogle.setClickable(true);
                        mProgressBar.setEnabled(true);
                        break;
                    case PAUSE:
                        mPlayToogle.setPixelMap(ResourceTable.Media_ic_music_play);
                        break;
                    case STOP:
                    case COMPLETE:
                        mPlayToogle.setPixelMap(ResourceTable.Media_ic_update);
                        mProgressBar.setEnabled(false);
                        break;
                    default:
                        break;
                }
            });
        }
    };

    /**
     * constructor of SimplePlayerController
     *
     * @param context context
     */
    public SimplePlayerController(Context context) {
        this(context, null);
    }

    /**
     * constructor of SimplePlayerController
     *
     * @param context context
     * @param attrSet attSet
     */
    public SimplePlayerController(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    /**
     * constructor of SimplePlayerController
     *
     * @param context context
     * @param attrSet attSet
     * @param styleName styleName
     */
    public SimplePlayerController(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        mContext = context;
        createHandler();
        initView();
        initListener();
    }

    private void createHandler() {
        EventRunner runner = EventRunner.create(true);
        if (runner == null) {
            return;
        }
        mHandler = new ControllerHandler(runner);
    }

    private void initView() {
        Component playerController =
                LayoutScatter.getInstance(mContext)
                        .parse(ResourceTable.Layout_simple_player_controller_layout, null, false);
        addComponent(playerController);
        if (playerController.findComponentById(ResourceTable.Id_controller_bottom_layout)
                instanceof DirectionalLayout) {
            bottomLayout = (DirectionalLayout) playerController
                    .findComponentById(ResourceTable.Id_controller_bottom_layout);
        }
        if (playerController.findComponentById(ResourceTable.Id_play_controller) instanceof Image) {
            mPlayToogle = (Image) playerController.findComponentById(ResourceTable.Id_play_controller);
        }
        if (playerController.findComponentById(ResourceTable.Id_play_forward) instanceof Image) {
            mForward = (Image) playerController.findComponentById(ResourceTable.Id_play_forward);
        }
        if (playerController.findComponentById(ResourceTable.Id_play_backward) instanceof Image) {
            mBackward = (Image) playerController.findComponentById(ResourceTable.Id_play_backward);
        }
        if (playerController.findComponentById(ResourceTable.Id_progress) instanceof Slider) {
            mProgressBar = (Slider) playerController.findComponentById(ResourceTable.Id_progress);
        }
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(new RgbColor(THUMB_RED, THUMB_GREEN, THUMB_BLUE));
        shapeElement.setBounds(0, 0, THUMB_WIDTH, THUMB_HEIGHT);
        shapeElement.setCornerRadius(THUMB_RADIUS);
        mProgressBar.setThumbElement(shapeElement);
        if (playerController.findComponentById(ResourceTable.Id_current_time) instanceof Text) {
            mCurrentTime = (Text) playerController.findComponentById(ResourceTable.Id_current_time);
        }
        if (playerController.findComponentById(ResourceTable.Id_end_time) instanceof Text) {
            mTotleTime = (Text) playerController.findComponentById(ResourceTable.Id_end_time);
        }
    }

    private void initListener() {
        bottomLayout.setTouchEventListener((component, touchEvent) -> true);
    }

    private void initPlayListener() {
        mPlayer.addPlayerStatuCallback(mStatuChangeListener);
        mPlayToogle.setClickedListener(component -> {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            } else {
                if (mPlayer.getPlayerStatu() == PlayerStatu.STOP) {
                    mPlayer.replay();
                } else {
                    mPlayer.resume();
                }
            }
        });
        mForward.setClickedListener(component ->
                mPlayer.rewindTo(getBasicTransTime(mPlayer.getCurrentPosition()) + Constants.REWIND_STEP));
        mBackward.setClickedListener(component ->
                mPlayer.rewindTo(getBasicTransTime(mPlayer.getCurrentPosition()) - Constants.REWIND_STEP));
        mProgressBar.setValueChangedListener(
                new Slider.ValueChangedListener() {
                    @Override
                    public void onProgressUpdated(Slider slider, int value, boolean isB) {
                        mContext.getUITaskDispatcher().asyncDispatch(() ->
                                mCurrentTime.setText(DateUtils.msToString(value)));
                    }

                    @Override
                    public void onTouchStart(Slider slider) {
                        mIsDragMode = true;
                        mHandler.removeEvent(Constants.PLAYER_PROGRESS_RUNNING, EventHandler.Priority.IMMEDIATE);
                    }

                    @Override
                    public void onTouchEnd(Slider slider) {
                        mIsDragMode = false;
                        if (slider.getProgress() == mPlayer.getDuration()) {
                            mPlayer.stop();
                        } else {
                            mPlayer.rewindTo(getBasicTransTime(slider.getProgress()));
                        }
                    }
                });
    }

    private int getBasicTransTime(int currentTime) {
        return currentTime / PROGRESS_RUNNING_TIME * PROGRESS_RUNNING_TIME;
    }

    /**
     * showController of PlayerController
     *
     * @param isAutoHide isAutoHide
     */
    public void showController(boolean isAutoHide) {
        mHandler.sendEvent(Constants.PLAYER_CONTROLLER_SHOW, EventHandler.Priority.HIGH);
        if (isAutoHide) {
            hideController(CONTROLLER_HIDE_DLEY_TIME);
        } else {
            mHandler.removeEvent(Constants.PLAYER_CONTROLLER_HIDE);
        }
    }

    /**
     * hideController of PlayerController
     *
     * @param delay delay
     */
    public void hideController(int delay) {
        mHandler.removeEvent(Constants.PLAYER_CONTROLLER_HIDE);
        mHandler.sendEvent(Constants.PLAYER_CONTROLLER_HIDE, delay, EventHandler.Priority.HIGH);
    }

    @Override
    public void bind(ImplPlayer player) {
        mPlayer = player;
        initPlayListener();
    }

    @Override
    public void unbind() {
        mHandler.removeAllEvent();
        mHandler = null;
    }

    /**
     * ControllerHandler
     *
     * @since 2021-09-07
     */
    private class ControllerHandler extends EventHandler {
        private int currentPosition;

        private ControllerHandler(EventRunner runner) {
            super(runner);
        }

        @Override
        public void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event == null) {
                return;
            }
            switch (event.eventId) {
                case Constants.PLAYER_PROGRESS_RUNNING:
                    if (mPlayer != null && mPlayer.isPlaying() && !mIsDragMode) {
                        currentPosition = mPlayer.getCurrentPosition();
                        while (currentPosition < PROGRESS_RUNNING_TIME) {
                            currentPosition = mPlayer.getCurrentPosition();
                        }
                        mContext.getUITaskDispatcher().asyncDispatch(() -> {
                            mProgressBar.setProgressValue(currentPosition);
                            mCurrentTime.setText(DateUtils.msToString(currentPosition));
                        });
                        mHandler.sendEvent(
                                Constants.PLAYER_PROGRESS_RUNNING, PROGRESS_RUNNING_TIME, Priority.HIGH);
                    }
                    break;
                case Constants.PLAYER_CONTROLLER_HIDE:
                    mContext.getUITaskDispatcher().asyncDispatch(() -> setVisibility(INVISIBLE));
                    mHandler.removeEvent(Constants.PLAYER_PROGRESS_RUNNING);
                    break;
                case Constants.PLAYER_CONTROLLER_SHOW:
                    mHandler.removeEvent(Constants.PLAYER_PROGRESS_RUNNING);
                    mHandler.sendEvent(Constants.PLAYER_PROGRESS_RUNNING, Priority.IMMEDIATE);
                    mContext.getUITaskDispatcher().asyncDispatch(() -> {
                        if (getVisibility() != VISIBLE) {
                            setVisibility(VISIBLE);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }
}
