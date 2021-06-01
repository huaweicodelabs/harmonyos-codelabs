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

package com.huawei.codelab;

import com.huawei.codelab.manager.VideoMigrationStub;
import com.huawei.codelab.player.api.ImplPlayer;
import com.huawei.codelab.player.constant.Constants;
import com.huawei.codelab.player.constant.ControlCode;
import com.huawei.codelab.player.constant.PlayerStatus;
import com.huawei.codelab.slice.SimplePlayerAbilitySlice;
import com.huawei.codelab.util.AbilitySliceRouteUtil;
import com.huawei.codelab.util.LogUtil;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.media.audio.AudioManager;
import ohos.media.audio.AudioRemoteException;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

/**
 * VideoMigrateService
 *
 * @since 2020-12-04
 */
public class VideoMigrateService extends Ability {
    private static final String DESCRIPTOR = "com.huawei.codelab.ImplVideoMigration";
    private static final String TAG = VideoMigrateService.class.getName();
    private int maxVolume;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        AudioManager mAudio = new AudioManager(getBundleName());
        try {
            maxVolume = mAudio.getMaxVolume(AudioManager.AudioVolumeType.STREAM_MUSIC);
        } catch (AudioRemoteException e) {
            LogUtil.error(TAG, "AudioRemoteException occurs ");
        }
    }

    @Override
    protected IRemoteObject onConnect(Intent intent) {
        return new MyRemote(DESCRIPTOR);
    }

    /**
     * MyRemote
     *
     * @since 2020-12-04
     */
    private class MyRemote extends VideoMigrationStub {
        /**
         * constructor of MyRemote
         *
         * @param descriptor descriptor
         */
        MyRemote(String descriptor) {
            super(descriptor);
        }

        @Override
        public void flyIn(int startTimemiles) throws RemoteException {
            Intent intent = new Intent();
            Operation operation =
                    new Intent.OperationBuilder()
                            .withBundleName(getBundleName())
                            .withAbilityName(MainAbility.class.getName())
                            .withAction("action.video.play")
                            .build();
            intent.setOperation(operation);
            intent.setParam(Constants.INTENT_STARTTIME_PARAM, startTimemiles);
            startAbility(intent);
        }

        @Override
        public void playControl(int controlCode, int extras) throws RemoteException {
            ImplPlayer player = SimplePlayerAbilitySlice.getImplPlayer();
            if (player != null) {
                if (controlCode == ControlCode.RESUME.getCode()) {
                    if (player.getPlayerStatus() == PlayerStatus.STOP
                            || player.getPlayerStatus() == PlayerStatus.COMPLETE) {
                        player.replay();
                    } else {
                        player.resume();
                    }
                } else if (controlCode == ControlCode.PAUSE.getCode()) {
                    player.pause();
                } else if (controlCode == ControlCode.STOP.getCode()) {
                    player.stop();
                } else if (controlCode == ControlCode.SEEK.getCode()) {
                    player.rewindTo(player.getAudioDuration() * extras / Constants.ONE_HUNDRED_PERCENT);
                } else if (controlCode == ControlCode.FORWARD.getCode()) {
                    player.rewindTo(player.getAudioCurrentPosition() + Constants.REWIND_STEP);
                } else if (controlCode == ControlCode.REWARD.getCode()) {
                    player.rewindTo(player.getAudioCurrentPosition() - Constants.REWIND_STEP);
                } else if (controlCode == ControlCode.VOLUME_SET.getCode()) {
                    player.setVolume(maxVolume * extras / Constants.ONE_HUNDRED_PERCENT);
                } else if (controlCode == ControlCode.VOLUME_ADD.getCode()) {
                    player.setVolume(player.getVolume() + Constants.VOLUME_STEP);
                } else if (controlCode == ControlCode.VOLUME_REDUCED.getCode()) {
                    player.setVolume(player.getVolume() - Constants.VOLUME_STEP);
                } else {
                    LogUtil.info(TAG, "playControl else message");
                }
            }
        }

        @Override
        public int flyOut() throws RemoteException {
            AbilitySliceRouteUtil.getInstance().terminateAbilitySlice();
            return SimplePlayerAbilitySlice.getImplPlayer().getAudioCurrentPosition();
        }
    }
}
