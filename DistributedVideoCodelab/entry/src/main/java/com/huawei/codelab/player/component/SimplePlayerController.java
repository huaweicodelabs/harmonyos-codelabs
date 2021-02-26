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

package com.huawei.codelab.player.component;

import com.huawei.codelab.player.api.ImplPlayer;
import com.huawei.codelab.player.api.StatusChangeListener;
import com.huawei.codelab.player.constant.Constants;
import com.huawei.codelab.player.constant.PlayerStatus;
import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.util.DateUtils;
import com.huawei.codelab.util.LogUtil;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

/**
 * PlayerController
 *
 * @since 2020-12-04
 */
public class SimplePlayerController extends ComponentContainer {
    private static final int THUMB_RED = 255;
    private static final int THUMB_GREEN = 255;
    private static final int THUMB_BLUE = 240;
    private static final int THUMB_WIDTH = 40;
    private static final int THUMB_HEIGHT = 40;
    private static final int THUMB_RADIUS = 20;
    private static final int CONTROLLER_HIDE_DLEY_TIME = 5000;
    private static final int PROGRESS_RUNNING_TIME = 1000;
    private boolean isDragMode = false;
    private Context context;
    private ImplPlayer implPlayer;
    private Image playToogle;
    private Image imageForward;
    private Image imageBackward;
    private Slider progressBar;
    private Text currentTime;
    private Text totleTime;
    private ControllerHandler controllerHandler;
    private StatusChangeListener statusChangeListener = new StatusChangeListener() {
        @Override
        public void statusCallback(PlayerStatus status) {
            context.getUITaskDispatcher().delayDispatch(
                    new Runnable() {
                        @Override
                        public void run() {
                            controllerHandler.removeEvent(Constants.PLAYER_PROGRESS_RUNNING);
                            if (status == PlayerStatus.PREPARING) {
                                playToogle.setClickable(false);
                                progressBar.setEnabled(false);
                                progressBar.setProgressValue(0);
                            } else if (status == PlayerStatus.PREPARED) {
                                progressBar.setMaxValue(implPlayer.getAudioDuration());
                                totleTime.setText(implPlayer.getDurationText());
                            } else if (status == PlayerStatus.PLAY) {
                                controllerHandler.sendEvent(
                                        Constants.PLAYER_PROGRESS_RUNNING,
                                        EventHandler.Priority.IMMEDIATE);
                                playToogle.setPixelMap(ResourceTable.Media_ic_music_stop);
                                playToogle.setClickable(true);
                                progressBar.setEnabled(true);
                            } else if (status == PlayerStatus.PAUSE) {
                                controllerHandler.sendEvent(
                                        Constants.PLAYER_PROGRESS_RUNNING,
                                        EventHandler.Priority.IMMEDIATE);
                                playToogle.setPixelMap(ResourceTable.Media_ic_music_play);
                            } else if (status == PlayerStatus.STOP || status == PlayerStatus.COMPLETE) {
                                controllerHandler.sendEvent(
                                        Constants.PLAYER_PROGRESS_RUNNING,
                                        EventHandler.Priority.IMMEDIATE);
                                playToogle.setPixelMap(ResourceTable.Media_ic_update);
                                progressBar.setEnabled(false);
                            } else {
                                LogUtil.info(SimplePlayerController.class.getName(), "statuCallback else message");
                            }
                        }
                    }, 0);
        }
    };

    /**
     * constructor of PlayerController
     *
     * @param context builder
     * @param player player
     */
    public SimplePlayerController(Context context, ImplPlayer player) {
        super(context);
        this.context = context;
        implPlayer = player;
        createHandler();
        initView();
        initListener();
    }

    private void createHandler() {
        EventRunner runner = EventRunner.create(true);
        if (runner == null) {
            return;
        }
        controllerHandler = new ControllerHandler(runner);
    }

    private void initView() {
        Component playerController = LayoutScatter.getInstance(context).parse(
                ResourceTable.Layout_simple_player_controller_layout, null, false);
        addComponent(playerController);
        if (playerController.findComponentById(ResourceTable.Id_play_controller) instanceof Image) {
            playToogle = (Image) playerController.findComponentById(ResourceTable.Id_play_controller);
        }
        if (playerController.findComponentById(ResourceTable.Id_play_forward) instanceof Image) {
            imageForward = (Image) playerController.findComponentById(ResourceTable.Id_play_forward);
        }
        if (playerController.findComponentById(ResourceTable.Id_play_backward) instanceof Image) {
            imageBackward = (Image) playerController.findComponentById(ResourceTable.Id_play_backward);
        }
        if (playerController.findComponentById(ResourceTable.Id_progress) instanceof Slider) {
            progressBar = (Slider) playerController.findComponentById(ResourceTable.Id_progress);
        }
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(new RgbColor(THUMB_RED, THUMB_GREEN, THUMB_BLUE));
        shapeElement.setBounds(0, 0, THUMB_WIDTH, THUMB_HEIGHT);
        shapeElement.setCornerRadius(THUMB_RADIUS);
        progressBar.setThumbElement(shapeElement);
        if (playerController.findComponentById(ResourceTable.Id_current_time) instanceof Text) {
            currentTime = (Text) playerController.findComponentById(ResourceTable.Id_current_time);
        }
        if (playerController.findComponentById(ResourceTable.Id_end_time) instanceof Text) {
            totleTime = (Text) playerController.findComponentById(ResourceTable.Id_end_time);
        }
    }

    private void initListener() {
        implPlayer.addPlayerStatusCallback(statusChangeListener);
        playToogle.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (implPlayer.isPlaying()) {
                    implPlayer.pause();
                } else {
                    if (implPlayer.getPlayerStatus() == PlayerStatus.STOP) {
                        implPlayer.replay();
                    } else {
                        implPlayer.resume();
                    }
                }
            }
        });
        imageForward.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                implPlayer.rewindTo(implPlayer.getAudioCurrentPosition() + Constants.REWIND_STEP);
            }
        });
        imageBackward.setClickedListener(new ClickedListener() {
            @Override
            public void onClick(Component component) {
                implPlayer.rewindTo(implPlayer.getAudioCurrentPosition() - Constants.REWIND_STEP);
            }
        });
        setValueChangedListener();
    }

    private void setValueChangedListener() {
        progressBar.setValueChangedListener(new Slider.ValueChangedListener() {
            @Override
            public void onProgressUpdated(Slider slider, int value, boolean isB) {
                context.getUITaskDispatcher().delayDispatch(new Runnable() {
                    @Override
                    public void run() {
                        currentTime.setText(DateUtils.msToString(value));
                    }
                }, 0);
            }

            @Override
            public void onTouchStart(Slider slider) {
                isDragMode = true;
                showController(false);
                controllerHandler.removeEvent(Constants.PLAYER_PROGRESS_RUNNING, EventHandler.Priority.IMMEDIATE);
            }

            @Override
            public void onTouchEnd(Slider slider) {
                isDragMode = false;
                if (slider.getProgress() == implPlayer.getAudioDuration()) {
                    implPlayer.stop();
                } else {
                    implPlayer.rewindTo(slider.getProgress());
                }
            }
        });
    }

    /**
     * showController of PlayerController
     *
     * @param isAutoHide isAutoHide
     */
    public void showController(boolean isAutoHide) {
        controllerHandler.sendEvent(Constants.PLAYER_CONTROLLER_SHOW, EventHandler.Priority.HIGH);
        if (isAutoHide) {
            hideController(CONTROLLER_HIDE_DLEY_TIME);
        } else {
            controllerHandler.removeEvent(Constants.PLAYER_CONTROLLER_HIDE);
        }
    }

    /**
     * hideController of PlayerController
     *
     * @param delay delay
     */
    public void hideController(int delay) {
        controllerHandler.removeEvent(Constants.PLAYER_CONTROLLER_HIDE);
        controllerHandler.sendEvent(Constants.PLAYER_CONTROLLER_HIDE, delay, EventHandler.Priority.HIGH);
    }

    /**
     * ControllerHandler
     *
     * @since 2020-12-04
     */
    private class ControllerHandler extends EventHandler {
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
                    context.getUITaskDispatcher()
                            .delayDispatch(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setProgressValue(implPlayer.getAudioCurrentPosition());
                                            currentTime.setText(implPlayer.getCurrentText());
                                        }
                                    },
                                    0);
                    if (implPlayer.isPlaying() && !isDragMode) {
                        controllerHandler.sendEvent(
                                Constants.PLAYER_PROGRESS_RUNNING, PROGRESS_RUNNING_TIME, Priority.IMMEDIATE);
                    }
                    break;
                case Constants.PLAYER_CONTROLLER_HIDE:
                    context.getUITaskDispatcher()
                            .delayDispatch(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            setVisibility(INVISIBLE);
                                        }
                                    },
                                    0);
                    controllerHandler.removeEvent(Constants.PLAYER_PROGRESS_RUNNING);
                    break;
                case Constants.PLAYER_CONTROLLER_SHOW:
                    controllerHandler.sendEvent(Constants.PLAYER_PROGRESS_RUNNING, Priority.IMMEDIATE);
                    context.getUITaskDispatcher()
                            .delayDispatch(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            setVisibility(VISIBLE);
                                        }
                                    },
                                    0);
                    break;
                default:
                    break;
            }
        }
    }
}
