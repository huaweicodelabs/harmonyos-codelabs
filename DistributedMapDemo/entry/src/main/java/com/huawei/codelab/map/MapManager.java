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

import com.dongyu.tinymap.Element;
import com.dongyu.tinymap.TinyMap;

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

import java.util.ArrayList;
import java.util.List;

/**
 * 地图控制类
 *
 * @since 2021-03-31
 */
public class MapManager {
    private static final String TAG = MapManager.class.getSimpleName();

    private static final int STEP_DELAY_TIME = 800;

    private final TinyMap tinyMap;

    private final AbilitySlice slice;

    private final MapEventHandler mapEventHandler;

    private IMapIdlInterface proxy;

    private int stepPoint = 0;

    private Element nextElement;

    private NavListener navListener;

    /**
     * IAbilityConnection
     *
     * @since 2021-03-12
     */
    private final IAbilityConnection conn = new IAbilityConnection() {
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
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (tinyMap.getMapElements() == null || tinyMap.getMapElements().size() <= 1) {
                return;
            }
            // 将定位图标的下一个坐标点的坐标，赋值给当前定位图标
            Element peopleElement = tinyMap.getMapElements().get(0);
            nextElement = tinyMap.getMapElements().get(stepPoint + 1);
            peopleElement.setMercatorPoint(nextElement.getMercatorPoint());
            peopleElement.setNowPoint(nextElement.getNowPoint());
            peopleElement.setOriginPoint(nextElement.getOriginPoint());

            // 调用sendEvent方法，发送更新UI事件消息
            mapEventHandler.sendEvent(1, EventHandler.Priority.IMMEDIATE);

            // 再次调用postTask发送延时任务，让任务持续进行，从而实现坐标的持续移动
            mapEventHandler.postTask(task, STEP_DELAY_TIME, EventHandler.Priority.IMMEDIATE);
            stepPoint++;

            // 将stepPoint传递给NavMap，绘制已经过的路径时也会用到stepPoint
            tinyMap.setStepPoint(stepPoint);
            LogUtils.info(TAG, "run......" + stepPoint);
            if (stepPoint >= tinyMap.getMapElements().size() - 1) {
                mapEventHandler.removeTask(task);
            }
        }
    };

    /**
     * 构造方法
     *
     * @param tinyMap navMap
     * @param slice   slice
     */
    public MapManager(TinyMap tinyMap, AbilitySlice slice) {
        this.tinyMap = tinyMap;
        this.slice = slice;
        mapEventHandler = new MapEventHandler(EventRunner.current());
    }

    /**
     * 移除tips中location为空的元素
     *
     * @param tips tips
     */
    public static void clearEmptyLocation(List<InputTipsResult.TipsEntity> tips) {

        List<InputTipsResult.TipsEntity> newList = new ArrayList<>();

        for (InputTipsResult.TipsEntity tipsEntity : tips) {
            if (tipsEntity.getLocation() != null && !(tipsEntity.getLocation()).isEmpty()) {
                newList.add(tipsEntity);
            }
        }

        tips.clear();
        tips.addAll(newList);
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
        tinyMap.setStepPoint(0);
        tinyMap.getMapElements().clear();
        tinyMap.invalidate();
    }

    /**
     * 迁移
     *
     * @param deviceId selected deviceId
     */
    public void translate(String deviceId) {
        try {
            slice.continueAbility(deviceId);
            // 关闭WatchAbility
            requestRemote(Const.STOP_WATCH_ABILITY, "");
        } catch (Exception exception) {
            LogUtils.info(TAG, "translate exception");
        }

    }

    /**
     * 迁移完成后的回调
     */
    public void translateComplete() {
        tinyMap.getMapElements().clear();
        tinyMap.invalidate();
        mapEventHandler.removeTask(task);
    }

    /**
     * 导航行动轨迹监听
     *
     * @since 2021-03-29
     */
    public interface NavListener {
        /**
         * 监听回调，将数据会调给MainAbilitySlice
         *
         * @param element MapElement
         */
        void onNavListener(Element element);
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
            if (nextElement.getActionType() != null && !nextElement.getActionType().isEmpty()
                    && !nextElement.getActionContent().equals("[]")) {
                // 将nextElement回调给MainAbilitySlice
                navListener.onNavListener(nextElement);
            }
            if (proxy != null) {
                // 将数据发送给WatchService
                requestRemote(nextElement.getActionType() == null ? "" : nextElement.getActionType(),
                        nextElement.getActionContent() == null ? "" : nextElement.getActionContent());
            }
            tinyMap.invalidate();
        }
    }
}
