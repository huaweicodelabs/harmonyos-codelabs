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

package com.huawei.codelab.slice;

import com.huawei.codelab.VideoMigrateService;
import com.huawei.codelab.manager.ImplVideoMigration;
import com.huawei.codelab.manager.VideoMigrationStub;
import com.huawei.codelab.provider.CommonProvider;
import com.huawei.codelab.provider.ViewProvider;
import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.component.RemoteController;
import com.huawei.codelab.component.SlidePopupWindow;
import com.huawei.codelab.component.Toast;
import com.huawei.codelab.player.HmPlayer;
import com.huawei.codelab.player.api.ImplPlayer;
import com.huawei.codelab.player.constant.Constants;
import com.huawei.codelab.player.constant.PlayerStatus;
import com.huawei.codelab.player.component.PlayerLoading;
import com.huawei.codelab.player.component.SimplePlayerController;
import com.huawei.codelab.util.AbilitySliceRouteUtil;
import com.huawei.codelab.util.LogUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.bundle.ElementName;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * PlayerAbilitySlice
 *
 * @since 2020-12-04
 */
public class SimplePlayerAbilitySlice extends AbilitySlice {
    private static final String TAG = SimplePlayerAbilitySlice.class.getSimpleName();
    private static final int TOAST_DURATION = 3000;
    private static ImplPlayer implPlayer;
    private RemoteController remoteController;
    private ImplVideoMigration implVideoMigration;
    private ListContainer deviceListContainer;
    private Image tv;
    private SlidePopupWindow transWindow;
    private List<DeviceInfo> devices = new ArrayList<>(0);
    private int startMillisecond;
    //private String url = "entry/resources/base/media/202011060201.mp4";
    private String url = "entry/resources/base/media/gubeishuizhen.mp4";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_simple_video_play_layout);
        startMillisecond = intent.getIntParam(Constants.INTENT_STARTTIME_PARAM, 0);
        initComponent();
        initListener();
        implPlayer.getLifecycle().onStart();
    }

    public static ImplPlayer getImplPlayer() {
        return implPlayer;
    }

    private void initComponent() {
        DependentLayout layout = null;
        if (findComponentById(ResourceTable.Id_parent) instanceof DependentLayout) {
            layout = (DependentLayout) findComponentById(ResourceTable.Id_parent);
        }
        transWindow = new SlidePopupWindow(this, ResourceTable.Layout_trans_slide);
        remoteController = new RemoteController(this);
        layout.addComponent(transWindow);
        layout.addComponent(remoteController);
        if (transWindow.findComponentById(ResourceTable.Id_device_list_container) instanceof ListContainer) {
            deviceListContainer = (ListContainer) transWindow.findComponentById(ResourceTable.Id_device_list_container);
        }
        DependentLayout playerLayout = null;
        if (findComponentById(ResourceTable.Id_parent_layout) instanceof DependentLayout) {
            playerLayout = (DependentLayout) findComponentById(ResourceTable.Id_parent_layout);
        }
        implPlayer = new HmPlayer.Builder(this).setStartMillisecond(startMillisecond).setFilePath(url).create();
        SimplePlayerController simplePlayerController = new SimplePlayerController(this, implPlayer);
        if (simplePlayerController.findComponentById(ResourceTable.Id_tv) instanceof Image) {
            tv = (Image) simplePlayerController.findComponentById(ResourceTable.Id_tv);
        }
        PlayerLoading playerLoading = new PlayerLoading(this, implPlayer);
        playerLayout.addComponent(implPlayer.getPlayerView());
        playerLayout.addComponent(playerLoading);
        playerLayout.addComponent(simplePlayerController);
        implPlayer.play();
    }

    private void initListener() {
        tv.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                initDevices();
                showDeviceList();
            }
        });
        deviceListContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int num, long l) {
                transWindow.hide();
                startAbilityFa(devices.get(num).getDeviceId());
            }
        });
        transWindow.setPopupWindowListener(new SlidePopupWindow.PopupWindowListener() {
            @Override
            public void windowShow() {
                implPlayer.pause();
            }

            @Override
            public void windowDismiss() {
                if (implPlayer.getPlayerStatus() == PlayerStatus.PAUSE) {
                    implPlayer.resume();
                }
            }
        });
        setRemoteControllerCallback();
    }

    private void setRemoteControllerCallback() {
        remoteController.setRemoteControllerCallback(new RemoteController.RemoteControllerListener() {
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
                implPlayer.reload(url, progress);
            }

            @Override
            public void sendControl(int code, int extra) {
                try {
                    if (implVideoMigration != null) {
                        implVideoMigration.playControl(code, extra);
                    }
                } catch (RemoteException e) {
                    LogUtil.error(TAG, "RemoteException occurs ");
                }
            }
        });
    }

    private void initDevices() {
        if (devices.size() > 0) {
            devices.clear();
        }
        List<DeviceInfo> deviceInfos = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        devices.addAll(deviceInfos);
    }

    private void showDeviceList() {
        CommonProvider commonProvider = new CommonProvider<DeviceInfo>(devices, getContext(),
                ResourceTable.Layout_device_list_item) {
            @Override
            protected void convert(ViewProvider viewProvider, DeviceInfo item, int position) {
                viewProvider.setText(ResourceTable.Id_device_text, item.getDeviceName());
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
            public void onAbilityConnectDone(ElementName elementName, IRemoteObject remoteObject, int i) {
                implVideoMigration = VideoMigrationStub.asInterface(remoteObject);
                try {
                    implVideoMigration.flyIn(startMillisecond);
                } catch (RemoteException e) {
                    LogUtil.error(TAG, "connect successful,but have remote exception");
                }
            }

            @Override
            public void onAbilityDisconnectDone(ElementName elementName, int i) {
                disconnectAbility(this);
            }
        });
        if (connectFlag) {
            Toast.toast(this, "transmit successful！", TOAST_DURATION);
            remoteController.show();
            startMillisecond = implPlayer.getAudioCurrentPosition();
            implPlayer.release();
        } else {
            Toast.toast(this, "transmit failed!Please try again later.", TOAST_DURATION);
        }
    }

    @Override
    public void onActive() {
        super.onActive();
        AbilitySliceRouteUtil.getInstance().addRoute(this);
    }

    @Override
    protected void onInactive() {
        LogUtil.info(TAG, "onInactive is called");
        super.onInactive();
    }

    @Override
    public void onForeground(Intent intent) {
        if (remoteController == null || !remoteController.isShown()) {
            implPlayer.getLifecycle().onForeground();
        }
        super.onForeground(intent);
    }

    @Override
    protected void onBackground() {
        LogUtil.info(TAG, "onBackground is called");
        if (remoteController == null || !remoteController.isShown()) {
            implPlayer.getLifecycle().onBackground();
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
        implPlayer.getLifecycle().onStop();
        super.onStop();
    }
}
