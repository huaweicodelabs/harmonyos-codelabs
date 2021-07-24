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

package com.huawei.codelab.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Component;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;

import com.huawei.codelab.view.ProductContainer;
import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.utils.DialogUtil;
import com.huawei.codelab.MainAbility;
import com.huawei.codelab.manager.RegisterManager;
import com.huawei.codelab.manager.HPermission;

/**
 * graphic composition
 *
 * @since 2021-04-13
 */
public class MainAbilitySlice extends AbilitySlice implements RegisterManager.CommonEvent {
    /**
     * start remote FA
     */
    public static final int START_ORDER = 3;
    /**
     * deviceId
     */
    public static final String DEVICE_ID = "deviceId";

    // Device Type
    private static final String DEVICE_TYPE = "14";

    private DirectionalLayout parent;

    private Button start;

    private RegisterManager registerManager;

    /**
     * onStart
     *
     * @param intent intent
     */
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initViewEvent();
        initData();
    }

    /**
     * init View Event
     */
    private void initViewEvent() {
        if (findComponentById(ResourceTable.Id_parent) instanceof DirectionalLayout) {
            parent = (DirectionalLayout) findComponentById(ResourceTable.Id_parent);
        }
        if (findComponentById(ResourceTable.Id_start) instanceof Button) {
            start = (Button) findComponentById(ResourceTable.Id_start);
        }
        start.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                requestPermissions(true);
            }
        });
    }

    /**
     * init Data
     */
    private void initData() {
        registerManager = new RegisterManager();
        // CommonEvent
        registerManager.setCommonEvent(this);

        String localDeviceType = KvManagerFactory.getInstance().createKvManager(
                new KvManagerConfig(this)).getLocalDeviceInfo().getType();
        // Determine whether the device is a watch device.
        if (!DEVICE_TYPE.equals(localDeviceType)) {
            findComponentById(ResourceTable.Id_lable).setVisibility(Component.VISIBLE);
        } else {
            start.setVisibility(Component.VISIBLE);
            requestPermissions(false);
        }
    }

    /**
     * request Permissions
     *
     * @param show show Continuation
     */
    private void requestPermissions(boolean show) {
        HPermission hPermission = ((MainAbility) getAbility()).getPermission();
        hPermission.requestPermissions(this, () -> registerContinuation(show));
    }

    /**
     * register Continuation
     *
     * @param show show Continuation
     */
    private void registerContinuation(boolean show) {
        registerManager.registerContinuation(this, deviceId -> startRemoteAbility(deviceId), show);
    }

    /**
     * open remote FA
     *
     * @param deviceId deviceId
     */
    private void startRemoteAbility(String deviceId) {
        DialogUtil.showToast(getContext(), "请求已经发送，等待对方确认。");
        String localDeviceId = KvManagerFactory.getInstance().createKvManager(
                new KvManagerConfig(this)).getLocalDeviceInfo().getId();
        Intent intent = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withDeviceId(deviceId)
                        .withBundleName(getBundleName())
                        .withAbilityName(MainAbility.class.getName())
                        .withAction(MainAbility.ACTION)
                        .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                        .build();
        intent.setOperation(operation);
        intent.setParam(MainAbilitySlice.DEVICE_ID, localDeviceId);
        intent.setParam(SmartWatchSlice.CODE, START_ORDER);
        startAbility(intent);
    }

    /**
     * excute Remote Permission
     *
     * @param code code
     */
    private void excuteRemotePermission(int code) {
        switch (code) {
            // Agree to allow games to be played
            case SmartWatchSlice.TYPE1:
                start.setVisibility(Component.HIDE);
                ProductContainer container = new ProductContainer(getContext());
                parent.addComponent(container);
                break;
            // Refuse to play games
            case SmartWatchSlice.TYPE2:
                DialogUtil.exitDialog(getAbility());
                break;
            default:
                break;
        }
    }

    @Override
    public void onReceiveEvent(int code, String deviceId) {
        if (code == START_ORDER) {
            Intent intent = new Intent();
            intent.setParam(MainAbilitySlice.DEVICE_ID, deviceId);
            present(new SmartWatchSlice(), intent);
        } else {
            excuteRemotePermission(code);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registerManager != null) {
            registerManager.realse();
        }
    }
}
