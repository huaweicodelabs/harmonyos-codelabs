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

package ohos.codelabs.distributedvideo.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.bundle.ElementName;
import ohos.codelabs.distributedvideo.ResourceTable;
import ohos.codelabs.distributedvideo.VideoMigrateService;
import ohos.codelabs.distributedvideo.component.RemoteController;
import ohos.codelabs.distributedvideo.component.SlidePopupWindow;
import ohos.codelabs.distributedvideo.component.Toast;
import ohos.codelabs.distributedvideo.manager.idl.ImplVideoMigration;
import ohos.codelabs.distributedvideo.manager.idl.VideoMigrationStub;
import ohos.codelabs.distributedvideo.player.HmPlayer;
import ohos.codelabs.distributedvideo.player.api.ImplPlayer;
import ohos.codelabs.distributedvideo.player.constant.Constants;
import ohos.codelabs.distributedvideo.player.constant.PlayerStatu;
import ohos.codelabs.distributedvideo.player.view.PlayerLoading;
import ohos.codelabs.distributedvideo.player.view.PlayerView;
import ohos.codelabs.distributedvideo.player.view.SimplePlayerController;
import ohos.codelabs.distributedvideo.provider.CommonProvider;
import ohos.codelabs.distributedvideo.provider.ViewProvider;
import ohos.codelabs.distributedvideo.util.AbilitySliceRouteUtil;
import ohos.codelabs.distributedvideo.util.LogUtil;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * PlayerAbilitySlice
 *
 * @since 2021-09-29
 */
public class SimplePlayerAbilitySlice extends AbilitySlice {
    private static final String TAG = SimplePlayerAbilitySlice.class.getSimpleName();
    private static final int TOAST_DURATION = 3000;
    private static ImplPlayer player;
    private RemoteController remoteController;
    private ImplVideoMigration implVideoMigration;
    private ListContainer deviceListContainer;
    private Image tv;
    private SlidePopupWindow transWindow;
    private final List<DeviceInfo> devices = new ArrayList<>(0);
    private int startMillisecond;
    private final String url = "entry/resources/base/media/gubeishuizhen.mp4";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_simple_video_play_layout);
        AbilitySliceRouteUtil.getInstance().addRoute(this);
        startMillisecond = intent.getIntParam(Constants.INTENT_STARTTIME_PARAM, 0);
        player = new HmPlayer.Builder(this).setFilePath(url).setStartMillisecond(startMillisecond).create();
        player.getLifecycle().onStart();
        initComponent();
        initListener();
    }

    /**
     * getImplPlayer
     *
     * @return ImplPlayer
     */
    public static ImplPlayer getImplPlayer() {
        return player;
    }

    private void initComponent() {
        if (findComponentById(ResourceTable.Id_parent) instanceof DependentLayout) {
            transWindow = new SlidePopupWindow.Builder(this).create(ResourceTable.Layout_trans_slide);
            if (transWindow.findComponentById(ResourceTable.Id_device_list_container) instanceof ListContainer) {
                deviceListContainer =
                        (ListContainer) transWindow.findComponentById(ResourceTable.Id_device_list_container);
            }
            remoteController = new RemoteController(this);
            DependentLayout parentLayout = (DependentLayout) findComponentById(ResourceTable.Id_parent);
            parentLayout.addComponent(transWindow);
            parentLayout.addComponent(remoteController);
        }
        if (findComponentById(ResourceTable.Id_player_view) instanceof PlayerView) {
            PlayerView playerView = (PlayerView) findComponentById(ResourceTable.Id_player_view);
            playerView.bind(player);
        }
        if (findComponentById(ResourceTable.Id_loading_view) instanceof PlayerLoading) {
            PlayerLoading playerLoading = (PlayerLoading) findComponentById(ResourceTable.Id_loading_view);
            playerLoading.bind(player);
        }
        if (findComponentById(ResourceTable.Id_controller_view) instanceof SimplePlayerController) {
            SimplePlayerController simplePlayerController =
                    (SimplePlayerController) findComponentById(ResourceTable.Id_controller_view);
            if (simplePlayerController.findComponentById(ResourceTable.Id_tv) instanceof Image) {
                tv = (Image) simplePlayerController.findComponentById(ResourceTable.Id_tv);
            }
            simplePlayerController.bind(player);
        }
    }

    private void initListener() {
        tv.setClickedListener(component -> {
            initDevices();
            showDeviceList();
        });
        transWindow.setPopupWindowListener(new SlidePopupWindow.PopupWindowListener() {
            @Override
            public void windowShow() {
                player.pause();
            }

            @Override
            public void windowDismiss() {
                if (player.getPlayerStatu() == PlayerStatu.PAUSE) {
                    player.resume();
                }
            }
        });
        setRemoteControllerCallback();
    }

    private void setRemoteControllerCallback() {
        remoteController.setRemoteControllerCallback(new MyRemoteControllerListener());
    }

    /**
     * MyRemoteControllerListener
     *
     * @since 2021-09-07
     */
    private class MyRemoteControllerListener implements RemoteController.RemoteControllerListener {
        @Override
        public void controllerShow() {

        }

        @Override
        public void controllerDismiss() {
            int progress = 0;
            try {
                if (implVideoMigration != null) {
                    progress = implVideoMigration.flyOut();
                }
            } catch (RemoteException e) {
                LogUtil.error(TAG, "RemoteException occurs");
            }
            player.reload(url, progress);
        }

        @Override
        public void sendControl(int code, int extra) {
            try {
                if (implVideoMigration != null) {
                    // 通过IDL通信，发送控制视频播放的指令
                    implVideoMigration.playControl(code, extra);
                }
            } catch (RemoteException e) {
                LogUtil.error(TAG, "RemoteException occurs ");
            }
        }
    }

    private void initDevices() {
        if (devices.size() > 0) {
            devices.clear();
        }
        List<DeviceInfo> deviceInfos = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        devices.addAll(deviceInfos);
    }

    private void showDeviceList() {
        CommonProvider<DeviceInfo> commonProvider = new CommonProvider<DeviceInfo>(
                devices,
                getContext(),
                ResourceTable.Layout_device_list_item) {
            @Override
            protected void convert(ViewProvider holder, DeviceInfo item, int position) {
                holder.setText(ResourceTable.Id_device_text, item.getDeviceName());
                Button clickButton = holder.getView(ResourceTable.Id_device_text);
                clickButton.setText(item.getDeviceName());
                clickButton.setClickedListener(component -> {
                    transWindow.hide();
                    startAbilityFa(item.getDeviceId());
                });
            }
        };
        deviceListContainer.setItemProvider(commonProvider);
        commonProvider.notifyDataChanged();
        transWindow.show();
    }

    private void startAbilityFa(String devicesId) {
        Intent intent = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withDeviceId(devicesId)
                        .withBundleName(getBundleName())
                        .withAbilityName(VideoMigrateService.class.getName())
                        .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                        .build();
        intent.setOperation(operation);
        boolean connectFlag = connectAbility(intent, new IAbilityConnection() {
            @Override
            public void onAbilityConnectDone(ElementName elementName, IRemoteObject remoteObject, int extra) {
                flyIn(remoteObject);
            }

            @Override
            public void onAbilityDisconnectDone(ElementName elementName, int extra) {
                disconnectAbility(this);
            }
        });
        if (connectFlag) {
            Toast.toast(this, "transmit successful!", TOAST_DURATION);
            remoteController.show();
            startMillisecond = player.getCurrentPosition();
            player.release();
        } else {
            Toast.toast(this, "transmit failed!Please try again later.", TOAST_DURATION);
        }
    }

    private void flyIn(IRemoteObject remoteObject) {
        implVideoMigration = VideoMigrationStub.asInterface(remoteObject);
        try {
            if (implVideoMigration != null) {
                implVideoMigration.flyIn(startMillisecond);
            }
        } catch (RemoteException e) {
            LogUtil.error(TAG, "connect successful,but have remote exception");
        }
    }

    @Override
    public void onActive() {
        super.onActive();
        getGlobalTaskDispatcher(TaskPriority.DEFAULT).delayDispatch(() -> player.play(), Constants.NUMBER_1000);
    }

    @Override
    protected void onInactive() {
        LogUtil.info(TAG, "onInactive is called");
        super.onInactive();
    }

    @Override
    public void onForeground(Intent intent) {
        if (remoteController == null || !remoteController.isShown()) {
            player.getLifecycle().onForeground();
        }
        super.onForeground(intent);
    }

    @Override
    protected void onBackground() {
        LogUtil.info(TAG, "onBackground is called");
        if (remoteController == null || !remoteController.isShown()) {
            player.getLifecycle().onBackground();
        }
        super.onBackground();
    }

    @Override
    protected void onBackPressed() {
        if (remoteController != null && remoteController.isShown()) {
            remoteController.hide();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        LogUtil.info(TAG, "onStop is called");
        AbilitySliceRouteUtil.getInstance().removeRoute(this);
        player.getLifecycle().onStop();
        super.onStop();
    }
}
