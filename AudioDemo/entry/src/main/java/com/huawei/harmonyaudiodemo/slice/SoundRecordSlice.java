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
import com.huawei.harmonyaudiodemo.component.WaveView;
import com.huawei.harmonyaudiodemo.constant.Const;
import com.huawei.harmonyaudiodemo.media.AudioRecorder;
import com.huawei.harmonyaudiodemo.media.AudioRender;
import com.huawei.harmonyaudiodemo.util.LogUtil;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.RoundProgressBar;
import ohos.agp.components.SlideDrawer;
import ohos.agp.components.Switch;
import ohos.agp.components.element.FrameAnimationElement;
import ohos.app.Environment;
import ohos.multimodalinput.event.TouchEvent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * SoundRecordSlice
 *
 * @since 2021-04-04
 */
public class SoundRecordSlice extends AbilitySlice {
    private static final String TAG = SoundRecordSlice.class.getSimpleName();
    private FrameAnimationElement frameAnimationElement;
    private RoundProgressBar recordProgressBar;
    private Button recordButton;
    private SlideDrawer slideDrawer;
    private AudioRecorder audioRecorder;
    private AudioRender recordWithPlayRender;
    private AudioRender oneOffLoadRender;
    private WaveView waveView;
    private boolean isRealTimePlay;
    private String savefilePath;
    private int recordTag;
    private final Component.TouchEventListener recordBtnTouchListener = new Component.TouchEventListener() {
        private int progress = 0;
        private boolean canRecord;

        private void stopRecord() {
            if (audioRecorder.isRecording()) {
                canRecord = false;
                progress = 0;
                recordButton.setPressState(false);
                waveView.setVisibility(Component.HIDE);
                recordProgressBar.setProgressValue(progress);
                shutdownRecord();
            }
        }

        private void startRecord(){
            if(!audioRecorder.isRecording()){
                canRecord = true;
                recordButton.setPressState(true);
                waveView.setVisibility(Component.VISIBLE);
                beginRecord();
            }
        }

        @Override
        public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
            int action = touchEvent.getAction();
            switch (action) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    startRecord();
                    break;
                case TouchEvent.PRIMARY_POINT_UP:
                    stopRecord();
                    break;
                case TouchEvent.POINT_MOVE:
                    if (canRecord && recordButton.isPressed() && progress <= Const.NUMBER_100) {
                        recordProgressBar.setProgressValue(progress++);
                    } else {
                        stopRecord();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_media_sound_audio);
        initAnimation();
        initView();
        initListener();
        initRecorder();
        setupRecordWithPlayRender();
        setupOneOffLoadRender();
    }

    private void initAnimation() {
        frameAnimationElement = new FrameAnimationElement(getContext(), ResourceTable.Graphic_animation_record_play);
    }

    private void initView() {
        if (findComponentById(ResourceTable.Id_wave_view) instanceof WaveView) {
            waveView = (WaveView) findComponentById(ResourceTable.Id_wave_view);
        }
        if (findComponentById(ResourceTable.Id_real_time_switch) instanceof Switch) {
            Switch realTimeSwitch = (Switch) findComponentById(ResourceTable.Id_real_time_switch);
            realTimeSwitch.setCheckedStateChangedListener((absButton, bool) -> {
                isRealTimePlay = bool;
                if (isRealTimePlay) {
                    slideDrawer.closeSmoothly();
                    oneOffLoadRender.stop();
                }
            });
        }
        initRecordView();
        initPlayView();
    }

    private void initListener() {
        recordButton.setTouchEventListener(recordBtnTouchListener);
    }

    private void initRecordView() {
        if (findComponentById(ResourceTable.Id_progressbar_record) instanceof RoundProgressBar) {
            recordProgressBar = (RoundProgressBar) findComponentById(ResourceTable.Id_progressbar_record);
        }
        if (findComponentById(ResourceTable.Id_button_record) instanceof Button) {
            recordButton = (Button) findComponentById(ResourceTable.Id_button_record);
        }
        recordProgressBar.setMaxValue(Const.NUMBER_100);
    }

    private void initPlayView() {
        Component playButton = LayoutScatter
                .getInstance(this)
                .parse(ResourceTable.Layout_record_play_button, null, false);
        if (findComponentById(ResourceTable.Id_slide_drawer_record) instanceof SlideDrawer) {
            slideDrawer = (SlideDrawer) findComponentById(ResourceTable.Id_slide_drawer_record);
            slideDrawer.setDisplayMode(SlideDrawer.DisplayMode.WITH_ANIMATION);
            slideDrawer.setSlideEnabled(false);
            slideDrawer.setTouchForClose(false);
            Component playAnimView = playButton.findComponentById(ResourceTable.Id_animation_view);
            playAnimView.setBackground(frameAnimationElement);
            SlideDrawer.LayoutConfig layoutConfig = new SlideDrawer
                    .LayoutConfig(Const.NUMBER_300, Const.NUMBER_114, SlideDrawer.SlideDirection.TOP);
            layoutConfig.setMarginsTopAndBottom(0, Const.NUMBER_60);
            playButton.setLayoutConfig(layoutConfig);
            slideDrawer.addComponent(playButton);
        }
        playButton.setClickedListener(component -> {
            oneOffLoadRender.stop();
            frameAnimationElement.start();
            playLocalAudioFile();
        });
    }

    private void initRecorder() {
        savefilePath = getExternalFilesDir(Environment.DIRECTORY_MUSIC) + File.separator + "AudioTest.mp3";
        audioRecorder = new AudioRecorder.Builder().setSaveFilePath(savefilePath).create();
    }

    private void setupRecordWithPlayRender() {
        recordWithPlayRender = new AudioRender.Builder().setOneOffLoad(false).create();
        audioRecorder.setAudioRecordListener((buffer, length, inputSize) -> {
            waveView.start(inputSize / Const.NUMBER_300, Const.NUMBER_300);
            if (recordTag == 0) {
                recordWithPlayRender.play(buffer, length);
            }
        });
    }

    private void setupOneOffLoadRender() {
        oneOffLoadRender = new AudioRender.Builder().create();
        oneOffLoadRender.setPlayListener(() -> {
            frameAnimationElement.stop();
        });
    }

    private void shutdownRecord() {
        if (audioRecorder.isRecording()) {
            audioRecorder.stopRecord();
        }
        if (!isRealTimePlay) {
            slideDrawer.openSmoothly();
        }
    }

    private void beginRecord() {
        if (!audioRecorder.isRecording()) {
            recordTag = isRealTimePlay ? 0 : 1;
            audioRecorder.record();
        }
    }

    private void playLocalAudioFile() {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(savefilePath));
            byte[] buffers = new byte[bis.available()];
            int len;
            while ((len = bis.read(buffers)) != Const.NUMBER_NEGATIVE_1) {
                oneOffLoadRender.play(buffers, len);
            }
        } catch (IOException e) {
            LogUtil.error(TAG, "play local audio file failed");
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    LogUtil.error(TAG, "play local audio file failed");
                }
            }
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
    protected void onStop() {
        recordWithPlayRender.release();
        oneOffLoadRender.release();
        audioRecorder.stopRecord();
        super.onStop();
    }
}
