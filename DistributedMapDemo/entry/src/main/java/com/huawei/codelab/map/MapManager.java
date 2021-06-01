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

package com.huawei.codelab.map;

import com.huawei.codelab.IMapIdlInterface;
import com.huawei.codelab.MapIdlInterfaceStub;
import com.huawei.codelab.WatchAbility;
import com.huawei.codelab.WatchService;
import com.huawei.codelab.bean.InputTipsResult;
import com.huawei.codelab.util.LogUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.bundle.ElementName;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

import java.util.List;

/**
 * 地图控制类
 *
 * @since 2021-03-31
 */
public class MapManager {
    private static final String TAG = MapManager.class.getSimpleName();

    private static final int STEP_DELAY_TIME = 800;

    private IMapIdlInterface proxy;

    private MapEventHandler mapEventHandler;

    private int stepPoint = 0;

    private MapElement nextElement;

    private NavMap navMap;

    private AbilitySlice slice;

    private NavListener navListener;

    /**
     * IAbilityConnection
     *
     * @since 2021-03-12
     */
    private IAbilityConnection conn = new IAbilityConnection() {
        @Override
        public void onAbilityConnectDone(ElementName element, IRemoteObject remote, int resultCode) {
            LogUtils.info(TAG, "onAbilityConnectDone......");
            proxy = MapIdlInterfaceStub.asInterface(remote);
        }

        @Override
        public void onAbilityDisconnectDone(ElementName element, int resultCode) {
            LogUtils.info(TAG, "onAbilityDisconnectDone......");
            proxy = null;
        }
    };

    /**
     * 用于实现路径上点的移动
     *
     * @since 2021-03-12
     */
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            // 将定位图标的下一个坐标点的坐标，赋值给当前定位图标
            MapElement peopleElement = navMap.getMapElements().get(0);
            nextElement = navMap.getMapElements().get(stepPoint + 1);
            peopleElement.setMercatorPoint(nextElement.getMercatorPoint());
            peopleElement.setNowPoint(nextElement.getNowPoint());
            peopleElement.setOriginPoint(nextElement.getOriginPoint());

            // 调用sendEvent方法，发送更新UI事件消息
            mapEventHandler.sendEvent(1, EventHandler.Priority.IMMEDIATE);

            // 再次调用postTask发送延时任务，让任务持续进行，从而实现坐标的持续移动
            mapEventHandler.postTask(task, STEP_DELAY_TIME, EventHandler.Priority.IMMEDIATE);
            stepPoint++;

            // 将stepPoint传递给NavMap，绘制已经过的路径时也会用到stepPoint
            navMap.setStepPoint(stepPoint);
            LogUtils.info(TAG, "run......" + stepPoint);
            if (stepPoint >= navMap.getMapElements().size() - 1) {
                mapEventHandler.removeTask(task);
            }
        }
    };

    /**
     * 构造方法
     *
     * @param navMap navMap
     * @param slice slice
     */
    public MapManager(NavMap navMap, AbilitySlice slice) {
        this.navMap = navMap;
        this.slice = slice;
        mapEventHandler = new MapEventHandler(EventRunner.current());
    }

    /**
     * 移除tips中location为空的元素
     *
     * @param tips tips
     */
    public static void clearEmptyLocation(List<InputTipsResult.TipsEntity> tips) {
        for (int i = 0; i < tips.size(); i++) {
            InputTipsResult.TipsEntity tipsEntity = tips.get(i);
            if ((tipsEntity.getLocation()).isEmpty()) {
                tips.remove(tipsEntity);
            }
        }
    }

    public int getStepPoint() {
        return stepPoint;
    }

    public void setStepPoint(int stepPoint) {
        this.stepPoint = stepPoint;
    }

    public void setNavListener(NavListener navListener) {
        this.navListener = navListener;
    }

    /**
     * 连接手表
     */
    public void connectWatch() {
        List<DeviceInfo> devices = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        for (DeviceInfo deviceInfo : devices) {
            if (DeviceInfo.DeviceType.SMART_WATCH.equals(deviceInfo.getDeviceType())) {
                connectWatchService(deviceInfo.getDeviceId());
                startWatchAbility(deviceInfo.getDeviceId());
            }
        }
    }

    /**
     * 连接WatchService
     *
     * @param deviceId 智能表deviceId
     */
    private void connectWatchService(String deviceId) {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder().withDeviceId(deviceId)
            .withBundleName(slice.getBundleName())
            .withAbilityName(WatchService.class.getName())
            .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
            .build();
        intent.setOperation(operation);
        slice.connectAbility(intent, conn);
    }

    /**
     * 启动WatchAbility
     *
     * @param deviceId 智能表deviceId
     */
    private void startWatchAbility(String deviceId) {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder().withDeviceId(deviceId)
            .withBundleName(slice.getBundleName())
            .withAbilityName(WatchAbility.class.getName())
            .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
            .build();
        intent.setOperation(operation);
        slice.startAbility(intent);
    }

    /**
     * 向WatchService发送数据
     *
     * @param actionType actionType
     * @param actionContent actionContent
     */
    private void requestRemote(String actionType, String actionContent) {
        LogUtils.info(TAG, "requestRemote");
        try {
            if (proxy != null) {
                proxy.action(actionType, actionContent);
            }
        } catch (RemoteException e) {
            LogUtils.info(TAG, "RemoteException:RemoteException");
        }
    }

    /**
     * 开始导航
     */
    public void startNav() {
        if (!mapEventHandler.hasInnerEvent(task)) {
            mapEventHandler.postTask(task, STEP_DELAY_TIME, EventHandler.Priority.IMMEDIATE);
            connectWatch();
        }
    }

    /**
     * 结束导航
     */
    public void endNav() {
        requestRemote(Const.STOP_WATCH_ABILITY, "");
        mapEventHandler.removeTask(task);
        stepPoint = 0;
        navMap.setStepPoint(0);
        navMap.getMapElements().clear();
        navMap.invalidate();
    }

    /**
     * 迁移
     */
    public void translate() {
        slice.continueAbility();
        requestRemote(Const.STOP_WATCH_ABILITY, "");
    }

    /**
     * 迁移完成后的回调
     */
    public void translateComplete() {
        navMap.getMapElements().clear();
        navMap.invalidate();
        mapEventHandler.removeTask(task);
    }

    /**
     * 导航行动轨迹监听
     *
     * @since 2021-03-29
     */
    public interface NavListener {
        /**
         * 监听回调
         *
         * @param mapElement MapElement
         */
        void onNavListener(MapElement mapElement);
    }

    /**
     * MapEventHandler
     *
     * @since 2021-03-12
     */
    private class MapEventHandler extends EventHandler {
        private MapEventHandler(EventRunner runner) {
            super(runner);
        }

        @Override
        public void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event.eventId != 1) {
                return;
            }
            LogUtils.info(TAG, "processEvent invalidate");
            if (nextElement.getActionType() != null && !nextElement.getActionType().isEmpty()) {
                navListener.onNavListener(nextElement);
            }
            if (proxy != null) {
                requestRemote(nextElement.getActionType() == null ? "" : nextElement.getActionType(),
                    nextElement.getActionContent() == null ? "" : nextElement.getActionContent());
            }
            navMap.invalidate();
        }
    }
}
