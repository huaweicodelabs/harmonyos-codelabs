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

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.media.audio.SoundPlayer;
import ohos.media.audio.ToneDescriptor;

/**
 * SoundSystemSlice
 *
 * @since 2021-04-04
 */
public class SoundSystemSlice extends AbilitySlice implements Component.ClickedListener {
    private SoundPlayer soundPlayer;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_media_sound_tone);
        initView();
        initPlayer();
    }

    private void initView() {
        findComponentById(ResourceTable.Id_sound0).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound1).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound2).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound3).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound4).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound5).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound6).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound7).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound8).setClickedListener(this);
        findComponentById(ResourceTable.Id_sound9).setClickedListener(this);
    }

    private void initPlayer() {
        soundPlayer = new SoundPlayer();
    }

    @Override
    public void onClick(Component view) {
        switch (view.getId()) {
            case ResourceTable.Id_sound0:
                soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_0, Const.NUMBER_500);
                soundPlayer.play();
                break;
            case ResourceTable.Id_sound1:
                soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_1, Const.NUMBER_500);
                soundPlayer.play();
                break;
            case ResourceTable.Id_sound2:
                soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_2, Const.NUMBER_500);
                soundPlayer.play();
                break;
            case ResourceTable.Id_sound3:
                soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_3, Const.NUMBER_500);
                soundPlayer.play();
                break;
            case ResourceTable.Id_sound4:
                soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_4, Const.NUMBER_500);
                soundPlayer.play();
                break;
            case ResourceTable.Id_sound5:
                soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_5, Const.NUMBER_500);
                soundPlayer.play();
                break;
            case ResourceTable.Id_sound6:
                soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_6, Const.NUMBER_500);
                soundPlayer.play();
                break;
            case ResourceTable.Id_sound7:
                soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_7, Const.NUMBER_500);
                soundPlayer.play();
                break;
            case ResourceTable.Id_sound8:
                soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_8, Const.NUMBER_500);
                soundPlayer.play();
                break;
            case ResourceTable.Id_sound9:
                soundPlayer.createSound(ToneDescriptor.ToneType.DTMF_9, Const.NUMBER_500);
                soundPlayer.play();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        if (soundPlayer != null) {
            soundPlayer.release();
            soundPlayer = null;
        }
        super.onStop();
    }
}
