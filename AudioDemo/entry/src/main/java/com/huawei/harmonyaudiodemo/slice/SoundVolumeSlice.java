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
import com.huawei.harmonyaudiodemo.util.LogUtil;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Slider;
import ohos.agp.components.Slider.ValueChangedListener;
import ohos.media.audio.AudioManager;
import ohos.media.audio.AudioRemoteException;

/**
 * SoundVolumeSlice
 *
 * @since 2021-04-04
 */
public class SoundVolumeSlice extends AbilitySlice implements ValueChangedListener {
    private static final String TAG = SoundVolumeSlice.class.getName();
    private AudioManager audioManager;
    private int dtmfVolume;
    private int musicVolume;
    private int callVolume;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_media_sound_volume);
        audioManager = new AudioManager();
        initView();
    }

    private void initView() {
        try {
            initSlider(ResourceTable.Id_sound_volume_bar);
            initSlider(ResourceTable.Id_sound_volume_bar2);
            initSlider(ResourceTable.Id_sound_volume_bar3);
        } catch (AudioRemoteException e) {
            LogUtil.error(TAG, "audio remote exception");
        }
    }

    private void initSlider(int id) throws AudioRemoteException {
        Slider slider = null;
        if (findComponentById(id) instanceof Slider) {
            slider = (Slider) findComponentById(id);
        }
        int maxValue = 0;
        int currentValue = 0;
        switch (id) {
            case ResourceTable.Id_sound_volume_bar:
                maxValue = audioManager.getMaxVolume(AudioManager.AudioVolumeType.STREAM_DTMF);
                currentValue = audioManager.getVolume(AudioManager.AudioVolumeType.STREAM_DTMF);
                dtmfVolume = currentValue;
                break;
            case ResourceTable.Id_sound_volume_bar2:
                maxValue = audioManager.getMaxVolume(AudioManager.AudioVolumeType.STREAM_MUSIC);
                currentValue = audioManager.getVolume(AudioManager.AudioVolumeType.STREAM_MUSIC);
                musicVolume = currentValue;
                break;
            case ResourceTable.Id_sound_volume_bar3:
                maxValue = audioManager.getMaxVolume(AudioManager.AudioVolumeType.STREAM_VOICE_CALL);
                currentValue = audioManager.getVolume(AudioManager.AudioVolumeType.STREAM_VOICE_CALL);
                callVolume = currentValue;
                break;
            default:
                break;
        }
        if (slider != null) {
            slider.setMaxValue(maxValue);
            slider.setProgressValue(currentValue);
            slider.setValueChangedListener(this);
        }
    }

    @Override
    public void onProgressUpdated(Slider slider, int info, boolean isBool) {
    }

    @Override
    public void onTouchStart(Slider slider) {
    }

    @Override
    public void onTouchEnd(Slider slider) {
        int progress = slider.getProgress();
        switch (slider.getId()) {
            case ResourceTable.Id_sound_volume_bar:
                try {
                    if (audioManager.setVolume(AudioManager.AudioVolumeType.STREAM_DTMF, progress)) {
                        dtmfVolume = progress;
                    } else {
                        audioManager.setVolume(AudioManager.AudioVolumeType.STREAM_DTMF, dtmfVolume);
                    }
                } catch (SecurityException e) {
                    LogUtil.error(TAG, "set dtmf volume error");
                }
                break;
            case ResourceTable.Id_sound_volume_bar2:
                try {
                    if (audioManager.setVolume(AudioManager.AudioVolumeType.STREAM_MUSIC, progress)) {
                        musicVolume = progress;
                    } else {
                        audioManager.setVolume(AudioManager.AudioVolumeType.STREAM_MUSIC, musicVolume);
                    }
                } catch (SecurityException e) {
                    LogUtil.error(TAG, "set music volume error");
                }
                break;
            case ResourceTable.Id_sound_volume_bar3:
                try {
                    if (audioManager.setVolume(AudioManager.AudioVolumeType.STREAM_VOICE_CALL, progress)) {
                        callVolume = progress;
                    } else {
                        audioManager.setVolume(AudioManager.AudioVolumeType.STREAM_VOICE_CALL, callVolume);
                    }
                } catch (SecurityException e) {
                    LogUtil.error(TAG, "set call volume error");
                }
                break;
            default:
                break;
        }
    }
}
