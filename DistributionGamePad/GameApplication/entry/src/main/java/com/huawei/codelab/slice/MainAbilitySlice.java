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

import com.huawei.codelab.MainAbility;
import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.devices.SelectDeviceDialog;
import com.huawei.codelab.game.Handle;
import com.huawei.codelab.service.GameServiceAbility;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.bundle.ElementName;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 *  game over ability slice
 *
 * @since 2021-03-15
 *
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, 0xD001400, "mainAbilitySlice");
    private static final List<Handle> handles = new ArrayList<>(0); // 保存手柄连接信息

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        // 设备弹出框
        if (handles == null || handles.size() == 0) { // 清空连接信息
            SelectDeviceDialog dialog = showDialog();
            dialog.initDialog().show();
            startService();
        }
    }

    // 设备选择弹出框
    private SelectDeviceDialog showDialog() {
        return new SelectDeviceDialog(this, new SelectDeviceDialog.SelectResultListener() {
            /**
             *  call back
             *
             * @param deviceInfos deviceInfos
             *
             */
            public void callBack(List<DeviceInfo> deviceInfos) {
                for (DeviceInfo deviceInfo : deviceInfos) {
                    Handle handleInfo = new Handle(MainAbilitySlice.this); // 连接信息
                    Intent intent = new Intent();
                    Operation operation = new Intent.OperationBuilder()
                            .withDeviceId(deviceInfo.getDeviceId())
                            .withBundleName(getBundleName())
                            .withAbilityName(MainAbility.class.getName())
                            .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                            .build();
                    intent.setOperation(operation);
                    startAbility(intent);
                    // 保存手柄连接信息
                    boolean isConn = connectRemotePa(deviceInfo.getDeviceId(), handleInfo);
                    handleInfo.setDeviceId(deviceInfo.getDeviceId());
                    handleInfo.setConn(isConn);
                    handles.add(handleInfo);
                }
            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void startService() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(getBundleName())
                .withAbilityName(GameServiceAbility.class.getName())
                .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    private boolean connectRemotePa(String deviceId, Handle handleInfo) {
        Intent connectPaIntent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId(deviceId)
                .withBundleName(getBundleName())
                .withAbilityName(GameServiceAbility.class.getName())
                .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                .build();
        connectPaIntent.setOperation(operation);

        IAbilityConnection conn = new IAbilityConnection() {
            @Override
            public void onAbilityConnectDone(ElementName elementName, IRemoteObject remote, int resultCode) {
                GameRemoteProxy proxy = new GameRemoteProxy(remote);
                handleInfo.setProxy(proxy);
            }

            @Override
            public void onAbilityDisconnectDone(ElementName elementName, int index) {
                if (handleInfo.getProxy() != null) {
                    handleInfo.setProxy(null);
                }
            }
        };

        return connectAbility(connectPaIntent, conn);
    }

    /**
     *  return score to handle
     *
     * @param score score
     * @param deviceId deviceId
     *
     */
    public static void returnScore(int score, String deviceId) {
        for (Handle handleInfo: handles) {
            if (handleInfo.getDeviceId().equals(deviceId) && handleInfo.isConn() && handleInfo.getProxy() != null) {
                handleInfo.getProxy().senDataToRemote(score);
                break;
            }
        }
    }

    /**
     *  game remote proxy
     *
     * @since 2021-03-15
     *
     */
    public static class GameRemoteProxy implements IRemoteBroker {
        private final IRemoteObject remote;

        GameRemoteProxy(IRemoteObject remote) {
            this.remote = remote;
        }

        @Override
        public IRemoteObject asObject() {
            return remote;
        }

        private void senDataToRemote(int score) {
            MessageParcel data = MessageParcel.obtain();
            MessageParcel reply = MessageParcel.obtain();
            try {
                data.writeInt(score);
                MessageOption option = new MessageOption(MessageOption.TF_SYNC);
                remote.sendRequest(1, data, reply, option);
            } catch (RemoteException e) {
                HiLog.error(TAG, "GameRemoteProxy::senDataToRemote faild");
            } finally {
                data.reclaim();
                reply.reclaim();
            }
        }
    }

    /**
     *  Obtains the ID of the connected device.
     *
     * @return deviceIds
     *
     */
    public static List<String> getDeviceIds() {
        List<String> deviceIds = new ArrayList<>(handles.size());
        for (Handle handleInfo: handles) {
            if (handleInfo.isConn() && handleInfo.getProxy() != null) {
                deviceIds.add(handleInfo.getDeviceId());
            }
        }
        return deviceIds;
    }

    // 获取连接设备信息
    public static List<Handle> getHandles() {
        return handles;
    }
}
