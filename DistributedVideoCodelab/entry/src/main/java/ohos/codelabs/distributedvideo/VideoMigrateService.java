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

package ohos.codelabs.distributedvideo;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.codelabs.distributedvideo.manager.idl.VideoMigrationStub;
import ohos.codelabs.distributedvideo.player.api.ImplPlayer;
import ohos.codelabs.distributedvideo.player.constant.Constants;
import ohos.codelabs.distributedvideo.player.constant.ControlCode;
import ohos.codelabs.distributedvideo.player.constant.PlayerStatu;
import ohos.codelabs.distributedvideo.slice.SimplePlayerAbilitySlice;
import ohos.codelabs.distributedvideo.util.AbilitySliceRouteUtil;
import ohos.codelabs.distributedvideo.util.LogUtil;
import ohos.rpc.IRemoteObject;

/**
 * VideoMigrateService
 *
 * @since 2021-09-07
 */
public class VideoMigrateService extends Ability {
    private static final String DESCRIPTOR = "com.huawei.codelab.ImplVideoMigration";
    private static final String TAG = VideoMigrateService.class.getName();

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    protected IRemoteObject onConnect(Intent intent) {
        return new MyBinder(DESCRIPTOR);
    }

    /**
     * MyBinder
     *
     * @since 2021-09-07
     */
    private class MyBinder extends VideoMigrationStub {
        /**
         * constructor of PlayerLoading
         *
         * @param descriptor descriptor
         */
        MyBinder(String descriptor) {
            super(descriptor);
        }

        @Override
        public void flyIn(int startTimemiles) {
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder().withBundleName(getBundleName())
                    .withAbilityName(PlayerAbility.class.getName()).build();
            intent.setOperation(operation);
            intent.setParam(Constants.INTENT_STARTTIME_PARAM, startTimemiles);
            startAbility(intent);
        }

        @Override
        public void playControl(int controlCode, int extras) {
            ImplPlayer player = SimplePlayerAbilitySlice.getImplPlayer();
            if (player != null) {
                if (controlCode == ControlCode.RESUME.getCode()) {
                    resumeVideo(player);
                } else if (controlCode == ControlCode.PAUSE.getCode()) {
                    player.pause();
                } else if (controlCode == ControlCode.STOP.getCode()) {
                    player.stop();
                } else if (controlCode == ControlCode.SEEK.getCode()) {
                    player.rewindTo(player.getDuration() * extras / Constants.ONE_HUNDRED_PERCENT);
                } else if (controlCode == ControlCode.FORWARD.getCode()) {
                    player.rewindTo(player.getCurrentPosition() + Constants.REWIND_STEP);
                } else if (controlCode == ControlCode.REWARD.getCode()) {
                    player.rewindTo(player.getCurrentPosition() - Constants.REWIND_STEP);
                } else if (controlCode == ControlCode.VOLUME_SET.getCode()) {
                    player.setVolume(extras / Constants.ONE_HUNDRED_PERCENT_FLOAT);
                } else if (controlCode == ControlCode.VOLUME_ADD.getCode()) {
                    player.setVolume(player.getVolume() + Constants.VOLUME_STEP);
                } else if (controlCode == ControlCode.VOLUME_REDUCED.getCode()) {
                    float volume = player.getVolume() - Constants.VOLUME_STEP;
                    player.setVolume(volume < 0 ? 0 : Math.min(1, volume));
                } else {
                    LogUtil.info(TAG, "playControl else message");
                }
            }
        }

        @Override
        public int flyOut() {
            AbilitySliceRouteUtil.getInstance().terminateSlices();
            return SimplePlayerAbilitySlice.getImplPlayer().getCurrentPosition();
        }
    }

    private void resumeVideo(ImplPlayer player) {
        if (player.getPlayerStatu() == PlayerStatu.STOP
                || player.getPlayerStatu() == PlayerStatu.COMPLETE) {
            player.replay();
        } else {
            player.resume();
        }
    }
}
