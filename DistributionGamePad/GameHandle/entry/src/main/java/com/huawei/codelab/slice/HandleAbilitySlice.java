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

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.handle.Handle;
import com.huawei.codelab.proxy.GameRemoteProxy;
import com.huawei.codelab.service.GameServiceAbility;
import com.huawei.codelab.utils.CalculAngle;
import com.huawei.codelab.utils.Constants;
import com.huawei.codelab.utils.LogUtil;
import com.huawei.codelab.utils.ScreenUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.bundle.ElementName;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.rpc.IRemoteObject;
import ohos.vibrator.agent.VibratorAgent;

import java.util.List;

/**
 * 手柄操作
 *
 * @since 2021-03-15
 */
public class HandleAbilitySlice extends AbilitySlice {
    private static final String TAG = "HandleAbilitySlice";

    private static ScoreEntity scoreEntity;

    private static boolean isConn;

    private static GameRemoteProxy proxy;

    private CalculAngle calculAngle;

    private String localDeviceId; // 本机设备Id

    private Handle handle;

    private Text txtScore;

    private Button bigCircle; // 摇杆 大圆

    private Button smallCircle; // 摇杆 小圆

    private Image imgA; // A技能图片

    private Image imgB; // B技能图片

    private Button btnPause; // 暂停

    private Button btnStart; // 开始

    private VibratorAgent vibratorAgent;

    /**
     * A技能
     */
    private final Component.ClickedListener listenerA = new Component.ClickedListener() {
        @Override
        public void onClick(Component component) {
            vibrator(Constants.VIBRATION_30); // 震动
            handle.setIsAbtnClick(1);
            postData();
            handle.setIsAbtnClick(0);
        }
    };

    /**
     * B技能
     */
    private final Component.ClickedListener listenerB = new Component.ClickedListener() {
        @Override
        public void onClick(Component component) {
            vibrator(Constants.VIBRATION_80); // 震动
            handle.setIsBbtnClick(1);
            postData();
            handle.setIsBbtnClick(0);
        }
    };

    /**
     * 暂停
     */
    private final Component.ClickedListener btnPauseListener = new Component.ClickedListener() {
        @Override
        public void onClick(Component component) {
            vibrator(Constants.VIBRATION_100); // 震动
            handle.setIsPause(1);
            postData();
            handle.setIsPause(0);
        }
    };

    /**
     * 开始游戏
     */
    private final Component.ClickedListener btnRestartListener = new Component.ClickedListener() {
        @Override
        public void onClick(Component component) {
            vibrator(Constants.VIBRATION_100); // 震动
            handle.setIsStart(1);
            postData();
            handle.setIsStart(0);
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_handle);
        ScreenUtils.setWindows();
        Object obj = intent.getParams().getParam("deviceId");
        if (obj instanceof String) {
            String deviceId = (String) obj;
            isConn = connectRemotePa(deviceId);
        }
        initComponent(); // 初始化
        setListener(); // 控件绑定事件
        startService(); // start service
    }

    private void startService() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder().withDeviceId("")
            .withBundleName(getBundleName())
            .withAbilityName(GameServiceAbility.class.getName())
            .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
            .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    // 绑定事件
    private void setListener() {
        bigCircle.setTouchEventListener(calculAngle.getOnTouchEvent());
        imgA.setClickedListener(listenerA);
        imgB.setClickedListener(listenerB);
        btnPause.setClickedListener(btnPauseListener);
        btnStart.setClickedListener(btnRestartListener);
    }

    // 初始化控件
    private void initComponent() {
        Component layout = findComponentById(ResourceTable.Id_layout);
        int screenHeight = ScreenUtils.getScreenHeight(this);
        Component comScore = findComponentById(ResourceTable.Id_score);
        if (comScore instanceof Text) {
            txtScore = (Text) comScore; // 得分
        }
        Component comImgA = findComponentById(ResourceTable.Id_btn_A);
        if (comImgA instanceof Image) {
            imgA = (Image) comImgA;
        }
        Component comImgB = findComponentById(ResourceTable.Id_btn_B);
        if (comImgB instanceof Image) {
            imgB = (Image) comImgB;
        }
        Component comBtnPause = findComponentById(ResourceTable.Id_pause);
        if (comBtnPause instanceof Button) {
            btnPause = (Button) comBtnPause;
        }
        Component comBtnStart = findComponentById(ResourceTable.Id_start);
        if (comBtnStart instanceof Button) {
            btnStart = (Button) comBtnStart;
        }
        ComponentContainer componentContainer = findComponentById(ResourceTable.Id_layout);
        componentContainer.setTouchEventSplitable(true); // 多点触控
        Component comSlide = findComponentById(ResourceTable.Id_btn);
        if (comSlide instanceof Button) {
            bigCircle = (Button) comSlide;
        }
        Component comSmallcircle = findComponentById(ResourceTable.Id_small_circle);
        if (comSmallcircle instanceof Button) {
            smallCircle = (Button) comSmallcircle;
        }
        scoreEntity = new ScoreEntity(); // 实时显示得分
        vibratorAgent = new VibratorAgent();
        handle = new Handle();
        localDeviceId =
            KvManagerFactory.getInstance().createKvManager(new KvManagerConfig(this)).getLocalDeviceInfo().getId();
        calculAngle = new CalculAngle(smallCircle, bigCircle, layout, screenHeight); // 摇杆事件
    }

    private boolean connectRemotePa(String deviceId) {
        Intent connectPaIntent = new Intent();
        Operation operation = new Intent.OperationBuilder().withDeviceId(deviceId)
                .withBundleName(getBundleName())
                .withAbilityName(GameServiceAbility.class.getName())
                .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                .build();
        connectPaIntent.setOperation(operation);

        IAbilityConnection conn = new IAbilityConnection() {
            @Override
            public void onAbilityConnectDone(ElementName elementName, IRemoteObject remote, int resultCode) {
                LogUtil.info(TAG, "onAbilityConnectDone......");
                proxy = new GameRemoteProxy(remote, localDeviceId, calculAngle, handle);
                LogUtil.error(TAG, "connectRemoteAbility done");
                send();
            }

            @Override
            public void onAbilityDisconnectDone(ElementName elementName, int index) {
                if (proxy != null) {
                    proxy = null;
                }
                LogUtil.info(TAG, "onAbilityDisconnectDone......");
            }
        };
        return connectAbility(connectPaIntent, conn);
    }

    private void send() {
        if (proxy != null) {
            proxy.senDataToRemote(1);
        }
    }

    /**
     * 远程发数据
     */
    public static void postData() {
        if (isConn && proxy != null) {
            proxy.senDataToRemote(1);
        }
    }

    /**
     * 震动
     *
     * @param duration 震动时长ms
     */
    private void vibrator(int duration) {
        // 查询硬件设备上的振动器列表
        List<Integer> vibratorList = vibratorAgent.getVibratorIdList();
        if (vibratorList.isEmpty()) {
            return;
        }
        int vibratorId = vibratorList.get(0);
        // 创建指定振动时长的一次性振动
        vibratorAgent.startOnce(vibratorId, duration);
    }

    // 判断某个点在不在View范围之内，代码如下：
    private boolean inComponentArea(Component component, float pointX, float pointY) {
        int top = component.getTop();
        int bottom = component.getBottom();
        int left = component.getLeft();
        int right = component.getRight();
        return pointX > left && pointX < right && pointY > top && pointY < bottom;
    }

    /**
     * 设置分数
     *
     * @param score 分数
     */
    public static void setScore(int score) {
        scoreEntity.setScore(score);
    }

    /**
     * 实时显示得分
     *
     * @since 2021-03-15
     */
    class ScoreEntity {
        /**
         * 实时显示得分
         *
         * @param score 得分
         */
        public void setScore(int score) {
            getContext().getUITaskDispatcher().asyncDispatch(new Runnable() {
                @Override
                public void run() {
                    txtScore.setText(String.valueOf(score));
                }
            });
        }
    }
}
