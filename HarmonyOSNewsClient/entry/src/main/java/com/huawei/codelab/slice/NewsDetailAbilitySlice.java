/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.All rights reserved.
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

package com.huawei.codelab.slice;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.SharedService;
import com.huawei.codelab.provider.DevicesListProvider;
import com.huawei.codelab.utils.CommonUtils;
import com.huawei.codelab.utils.DialogUtils;
import com.huawei.codelab.utils.LogUtils;

import manager.INewsDemoIDL;
import manager.NewsDemoIDLStub;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.CommonDialog;
import ohos.bundle.ElementName;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * News detail slice
 *
 * @since 2020-12-04
 */
public class NewsDetailAbilitySlice extends AbilitySlice {
    public static final String INTENT_TITLE = "intent_title";
    public static final String INTENT_READ = "intent_read";
    public static final String INTENT_LIKE = "intent_like";
    public static final String INTENT_CONTENT = "intent_content";
    public static final String INTENT_IMAGE = "intent_image";
    private static final String TAG = "NewsDetailAbilitySlice";
    private static final int WAIT_TIME = 30000;
    private static final int DIALOG_SIZE_WIDTH = 900;
    private static final int DIALOG_SIZE_HEIGHT = 800;
    private DependentLayout parentLayout;
    private TextField commentFocus;
    private Image iconShared;
    private CommonDialog dialog;
    private List<DeviceInfo> devices = new ArrayList<>();
    private String reads;
    private String likes;
    private String title;
    private String content;
    private String image;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_news_detail_layout);
        reads = intent.getStringParam(INTENT_READ);
        likes = intent.getStringParam(INTENT_LIKE);
        title = intent.getStringParam(INTENT_TITLE);
        content = intent.getStringParam(INTENT_CONTENT);
        image = intent.getStringParam(INTENT_IMAGE);
        initView();
        initListener();
    }

    private void initView() {
        parentLayout = (DependentLayout) findComponentById(ResourceTable.Id_parent_layout);
        commentFocus = (TextField) findComponentById(ResourceTable.Id_text_file);
        iconShared = (Image) findComponentById(ResourceTable.Id_button4);
        Text newsRead = (Text) findComponentById(ResourceTable.Id_read_num);
        Text newsLike = (Text) findComponentById(ResourceTable.Id_like_num);
        Text newsTitle = (Text) findComponentById(ResourceTable.Id_title_text);
        Text newsContent = (Text) findComponentById(ResourceTable.Id_title_content);
        Image newsImage = (Image) findComponentById(ResourceTable.Id_image_content);
        newsRead.setText("reads: " + reads);
        newsLike.setText("likes: " + likes);
        newsTitle.setText("Original title: " + title);
        newsContent.setText(content);
        newsImage.setPixelMap(CommonUtils.getPixelMapFromPath(this, image));
    }

    private void initListener() {
        parentLayout.setTouchEventListener(
            (component, touchEvent) -> {
                if (commentFocus.hasFocus()) {
                    commentFocus.clearFocus();
                }
                return true;
            });
        iconShared.setClickedListener(
            v -> {
                initDevices();
                showDeviceList();
            });
    }

    private void initDevices() {
        if (devices.size() > 0) {
            devices.clear();
        }
        List<DeviceInfo> deviceInfos =
                DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        devices.addAll(deviceInfos);
    }

    private void showDeviceList() {
        dialog = new CommonDialog(NewsDetailAbilitySlice.this);
        dialog.setAutoClosable(true);
        dialog.setTitleText("Harmony devices");
        dialog.setSize(DIALOG_SIZE_WIDTH, DIALOG_SIZE_HEIGHT);
        ListContainer devicesListContainer = new ListContainer(getContext());
        DevicesListProvider devicesListProvider = new DevicesListProvider(devices, this);
        devicesListContainer.setItemProvider(devicesListProvider);
        devicesListContainer.setItemClickedListener(
            (listContainer, component, position, id) -> {
                dialog.destroy();
                startAbilityFA(devices.get(position).getDeviceId());
            });
        devicesListProvider.notifyDataChanged();
        dialog.setContentCustomComponent(devicesListContainer);
        dialog.show();
    }

    private void startAbilityFA(String devicesId) {
        Intent intent = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withDeviceId(devicesId)
                        .withBundleName(getBundleName())
                        .withAbilityName(SharedService.class.getName())
                        .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                        .build();
        intent.setOperation(operation);
        boolean connectFlag =
                connectAbility(
                        intent,
                        new IAbilityConnection() {
                            @Override
                            public void onAbilityConnectDone(
                                    ElementName elementName, IRemoteObject remoteObject, int i) {
                                INewsDemoIDL sharedManager = NewsDemoIDLStub.asInterface(remoteObject);
                                try {
                                    sharedManager.tranShare(title, reads, likes, content, image);
                                } catch (RemoteException e) {
                                    LogUtils.i(TAG, "connect successful,but have remote exception");
                                }
                            }

                            @Override
                            public void onAbilityDisconnectDone(ElementName elementName, int i) {
                                disconnectAbility(this);
                            }
                        });
        DialogUtils.toast(
                this, connectFlag ? "Sharing succeeded!" : "Sharing failed. Please try again later.", WAIT_TIME);
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
        super.onStop();
    }
}
