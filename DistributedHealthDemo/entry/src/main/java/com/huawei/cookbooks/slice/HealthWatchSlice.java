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

package com.huawei.cookbooks.slice;

import com.huawei.cookbooks.ResourceTable;
import com.huawei.cookbooks.annotation.Bind;
import com.huawei.cookbooks.enums.KeyEnum;
import com.huawei.cookbooks.task.ScheduleRemoteAbilityTask;
import com.huawei.cookbooks.util.LogUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.data.distributed.common.KvManager;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.data.distributed.common.KvStoreException;
import ohos.data.distributed.common.KvStoreType;
import ohos.data.distributed.common.Options;
import ohos.data.distributed.user.SingleKvStore;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.sensor.agent.CategoryBodyAgent;
import ohos.sensor.agent.CategoryMotionAgent;
import ohos.sensor.bean.CategoryBody;
import ohos.sensor.bean.CategoryMotion;
import ohos.sensor.data.CategoryBodyData;
import ohos.sensor.data.CategoryMotionData;
import ohos.sensor.listener.ICategoryBodyDataCallback;
import ohos.sensor.listener.ICategoryMotionDataCallback;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 智能穿戴手表FA
 *
 * @since 2021-04-06
 */
public class HealthWatchSlice extends AbilitySlice {
    private static final String STORE_ID = "watch_demo_mine";

    private static final String PERMISSION_MOTION = "ohos.permission.ACTIVITY_MOTION";

    private static final long INTERVAL = 100000000;

    private static final int SHOW_TIME = 1500;

    private static final int DIALOG_SIZE_WIDTH = 1000;

    private static final int DIALOG_SIZE_HEIGHT = 100;

    private static final int MAX_HEART_RATE = 100;

    private static final int MIN_HEART_RATE = 90;

    private static final int TOP_HEART_RATE = 255;

    private static final int BIND_ERROR = -1;

    private final CategoryBodyAgent categoryBodyAgent = new CategoryBodyAgent();

    private CategoryBody bodySensor;

    private ICategoryBodyDataCallback bodyDataCallback;

    private final CategoryMotionAgent categoryMotionAgent = new CategoryMotionAgent();

    private CategoryMotion categoryMotion;

    private ICategoryMotionDataCallback motionDataCallback;

    private MyEventHandler myHandler;

    @Bind(ResourceTable.Id_button_btnSubscribe)
    private Button subscribe;

    @Bind(ResourceTable.Id_button_btnUnsubscribe)
    private Button unsubscribe;

    @Bind(ResourceTable.Id_text_rate)
    private Text rate;

    @Bind(ResourceTable.Id_button_btnSubscribeStep)
    private Button subscribeStep;

    @Bind(ResourceTable.Id_button_btnUnsubscribeStep)
    private Button unsubscribeStep;

    @Bind(ResourceTable.Id_text_step)
    private Text step;

    private KvManager kvManager;

    private SingleKvStore singleKvStore;

    private Context context;

    private final String TAG = "WatchSlice";

    private final String regex = "-->";

    private static final String invalidData = "255.0";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        context = this.getContext();
        initViewAnnotation();
        initCallback();
        initListener();
        myHandler = new MyEventHandler(EventRunner.current());
        initDbManager();
        LogUtils.info("获取数据库内容", kvManager.getAllKvStoreId().toString());
        LogUtils.info("获取数据库内容", String.valueOf(singleKvStore.getResultSet("").getRowCount()));
        if (verifySelfPermission(PERMISSION_MOTION) != 0) {
            if (canRequestPermission(PERMISSION_MOTION)) {
                requestPermissionsFromUser(new String[]{PERMISSION_MOTION,
                    "ohos.permission.READ_HEALTH_DATA", "ohos.permission.DISTRIBUTED_DATASYNC"}, 1);
            }
        }
    }

    private void initCallback() {
        bodyDataCallback = new ICategoryBodyDataCallback() {
            @Override
            public void onSensorDataModified(CategoryBodyData categoryBodyData) {
                float[] values = categoryBodyData.getValues();
                myHandler.postTask(() -> {
                    rate.setText(getNowTime() + regex + values[0]);
                    if (values[0] != TOP_HEART_RATE) {
                        writeData(KeyEnum.RATE.getValue() + (UUID.randomUUID()),
                                getNowTime() + regex + values[0]);

                        if (values[0] < MIN_HEART_RATE || values[0] > MAX_HEART_RATE) {
                            getUITaskDispatcher().asyncDispatch(new ScheduleRemoteAbilityTask(context));
                        }
                    }
                });
            }

            @Override
            public void onAccuracyDataModified(CategoryBody categoryBody, int i) {
            }

            @Override
            public void onCommandCompleted(CategoryBody categoryBody) {
            }
        };

        motionDataCallback = new ICategoryMotionDataCallback() {
            @Override
            public void onSensorDataModified(CategoryMotionData categoryMotionData) {
                float[] values = categoryMotionData.getValues();
                myHandler.postTask(() -> {
                    step.setText(KeyEnum.STEP.getDesc() + values[0]);
                    writeData(KeyEnum.STEP.getValue() + (UUID.randomUUID()),
                            getNowTime() + regex + values[0]);
                });
            }

            @Override
            public void onAccuracyDataModified(CategoryMotion cateMotion, int i) {
            }

            @Override
            public void onCommandCompleted(CategoryMotion cateMotion) {
            }
        };
    }

    private void initListener() {
        subscribe.setClickedListener(va -> {
            bodySensor = categoryBodyAgent.getSingleSensor(CategoryBody.SENSOR_TYPE_HEART_RATE);
            if (bodySensor != null) {
                categoryBodyAgent.setSensorDataCallback(bodyDataCallback, bodySensor, INTERVAL);
                showTip("订阅心率成功");
            }
        });

        unsubscribe.setClickedListener(va -> {
            if (bodySensor != null) {
                categoryBodyAgent.releaseSensorDataCallback(bodyDataCallback, bodySensor);
                showTip("取消订阅心率成功");
                rate.setText("实时心率");
            }
        });

        subscribeStep.setClickedListener(va -> {
            categoryMotion = categoryMotionAgent
                    .getSingleSensor(CategoryMotion.SENSOR_TYPE_PEDOMETER);
            if (categoryMotion != null) {
                categoryMotionAgent
                        .setSensorDataCallback(motionDataCallback, categoryMotion, INTERVAL);
                showTip("订阅步数成功");
            }
        });

        unsubscribeStep.setClickedListener(va -> {
            if (categoryMotion != null) {
                categoryMotionAgent.releaseSensorDataCallback(motionDataCallback, categoryMotion);
                showTip("取消订阅步数成功");
                step.setText("实时总步数");
            }
        });
    }

    private void initViewAnnotation() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            Bind bind = field.getAnnotation(Bind.class);
            if (bind != null) {
                if (bind.value() == BIND_ERROR) {
                    LogUtils.error("TAG", "bind.value is must set!");
                    return;
                }
                try {
                    field.setAccessible(true);
                    field.set(this, findComponentById(bind.value()));
                } catch (IllegalAccessException e) {
                    LogUtils.error("TAG", "IllegalAccessException happened");
                }
            }
        }
    }

    private void showTip(String message) {
        new ToastDialog(this).setAlignment(LayoutAlignment.CENTER)
                .setText(message).setDuration(SHOW_TIME)
                .setSize(DIALOG_SIZE_WIDTH, DIALOG_SIZE_HEIGHT).show();
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss");
        return sd.format(date);
    }

    private void initDbManager() {
        kvManager = createManager();
        singleKvStore = createDb(kvManager);
    }

    private KvManager createManager() {
        KvManagerConfig config = new KvManagerConfig(this);
        KvManager manager = null;
        try {
            manager = KvManagerFactory.getInstance().createKvManager(config);
        } catch (KvStoreException e) {
            LogUtils.info(TAG, "KvStoreException");
        }
        return manager;
    }

    private SingleKvStore createDb(KvManager kvManagerDb) {
        Options options = new Options();
        options.setCreateIfMissing(true)
                .setEncrypt(false).setKvStoreType(KvStoreType.SINGLE_VERSION);
        SingleKvStore kvStore = null;
        try {
            kvStore = kvManagerDb.getKvStore(options, STORE_ID);
        } catch (KvStoreException e) {
            LogUtils.info(TAG, "KvStoreException");
        }
        return kvStore;
    }

    private void writeData(String key, String value) {
        if (key.isEmpty() || value.isEmpty()) {
            return;
        }
        if (key.contains(KeyEnum.STEP.getValue())) {
            singleKvStore.putString(KeyEnum.LATEST_STEP.getValue(), value);
            singleKvStore.putString(key, value);
        } else if (key.contains(KeyEnum.RATE.getValue())) {
            if (!value.contains(invalidData)) {
                singleKvStore.putString(KeyEnum.LATEST_RATE.getValue(), value);
                singleKvStore.putString(key, value);
            } else {
                LogUtils.info(TAG, "Invalid heart rate data is not written" + "==>" + key);
            }
        } else {
            LogUtils.info(TAG, "Invalid key");
        }
    }

    /**
     * EventHandler线程间交互
     *
     * @since 2021-05-29
     */
    static class MyEventHandler extends EventHandler {
        MyEventHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
