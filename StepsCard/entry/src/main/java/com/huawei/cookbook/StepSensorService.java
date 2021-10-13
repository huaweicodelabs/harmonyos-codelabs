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

package com.huawei.cookbook;

import com.huawei.cookbook.database.Form;
import com.huawei.cookbook.database.FormDatabase;
import com.huawei.cookbook.database.SensorData;
import com.huawei.cookbook.slice.MainAbilitySlice;
import com.huawei.cookbook.utils.ChartDataUtils;
import com.huawei.cookbook.utils.DatabaseUtils;
import com.huawei.cookbook.utils.DateUtils;
import com.huawei.cookbook.utils.LogUtils;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.FormException;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;
import ohos.event.notification.NotificationRequest;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.sensor.agent.CategoryMotionAgent;
import ohos.sensor.bean.CategoryMotion;
import ohos.sensor.data.CategoryMotionData;
import ohos.sensor.listener.ICategoryMotionDataCallback;
import ohos.utils.zson.ZSONObject;

import java.util.List;

/**
 * StepSensorService 计步器传感器service
 */
public class StepSensorService extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");
    private static final long INTERVAL = 3000000000L;
    private static final int NOTICE_ID = 1005;
    private final CategoryMotionAgent categoryMotionAgent = new CategoryMotionAgent();
    private ICategoryMotionDataCallback categoryMotionDataCallback;
    private StepSensorService.MyEventHandler myHandler;
    private final DatabaseHelper helper = new DatabaseHelper(this);
    private OrmContext connect;

    @Override
    public void onStart(Intent intent) {
        HiLog.info(LABEL_LOG, "StepSensorService::onStart");
        super.onStart(intent);
        connect = helper.getOrmContext("FormDatabase", "FormDatabase.db", FormDatabase.class);
        myHandler = new MyEventHandler(EventRunner.getMainEventRunner());
        categoryMotionDataCallback = new ICategoryMotionDataCallback() {
            @Override
            public void onSensorDataModified(CategoryMotionData categoryMotionData) {
                float[] values = categoryMotionData.getValues();
                handleSensorData(values[0]);
                // 设置前台service通知
                notice();
            }

            @Override
            public void onAccuracyDataModified(CategoryMotion category, int accuracy) {
            }

            @Override
            public void onCommandCompleted(CategoryMotion category) {
            }
        };
        CategoryMotion categoryMotion = categoryMotionAgent.getSingleSensor(CategoryMotion.SENSOR_TYPE_PEDOMETER);
        if (categoryMotion != null) {
            categoryMotionAgent.setSensorDataCallback(categoryMotionDataCallback, categoryMotion, INTERVAL);
        }
    }

    // 前台service
    private void notice() {
        // 创建通知
        NotificationRequest request = new NotificationRequest(NOTICE_ID);
        request.setAlertOneTime(true);
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        SensorData sensorData = DatabaseUtils.getSensorData(connect, DateUtils.getDate(0));
        int stepValue = 0;
        if (sensorData != null) {
            stepValue = sensorData.getStepsValue();
        }
        content.setText("今天走了" + stepValue + "步");
        NotificationRequest.NotificationContent notificationContent
                = new NotificationRequest.NotificationContent(content);
        request.setContent(notificationContent);
        // 绑定通知
        keepBackgroundRunning(NOTICE_ID, request);
    }

    /**
     * handle sensor data
     *
     * @param value step count
     */
    private void handleSensorData(float value) {
        SensorData realSensorData =
                DatabaseUtils.getRealSensorData(value, connect,
                        DateUtils.getDate(0), DateUtils.getDate(1));
        float stepsValue = realSensorData.getStepsValue();
        String stringValue = String.valueOf((int) stepsValue);
        myHandler.postTask(() -> {
            // 存储数据
            DatabaseUtils.insertValues(stepsValue, value, connect);
            // 更新页面
            MainAbilitySlice.updatePage(stringValue);
            // 更新卡片
            updateForms(stringValue);
        });
    }

    /**
     * update form
     *
     * @param value 步数值
     */
    private void updateForms(String value) {
        OrmPredicates ormPredicates = new OrmPredicates(Form.class);
        List<Form> forms = connect.query(ormPredicates);
        if (forms.size() <= 0) {
            return;
        }
        for (Form form : forms) {
            ZSONObject result = ChartDataUtils.getZsonObject(value, form.getDimension(), connect);
            try {
                updateForm(form.getFormId(), new FormBindingData(result));
            } catch (FormException e) {
                connect.delete(form);
                LogUtils.error("updateForms", "formid not exit");
            }
        }
    }

    @Override
    public void onBackground() {
        HiLog.info(LABEL_LOG, "StepSensorService::onBackground");
        super.onBackground();
    }

    @Override
    public void onStop() {
        categoryMotionAgent.releaseSensorDataCallback(categoryMotionDataCallback);
        HiLog.info(LABEL_LOG, "StepSensorService::onStop");
        super.onStop();
    }

    @Override
    public void onCommand(Intent intent, boolean isRestart, int startId) {
    }

    @Override
    public void onDisconnect(Intent intent) {
    }

    /**
     * MyEventHandler 用于线程间的通信
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
}