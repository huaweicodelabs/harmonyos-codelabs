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

package com.huawei.cookbook.slice;

import com.huawei.cookbook.ResourceTable;
import com.huawei.cookbook.StepSensorService;
import com.huawei.cookbook.database.FormDatabase;
import com.huawei.cookbook.database.SensorData;
import com.huawei.cookbook.utils.DatabaseUtils;
import com.huawei.cookbook.utils.DateUtils;
import com.huawei.cookbook.utils.PermissionBridge;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.ProgressBar;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice implements PermissionBridge.OnPermissionStateListener {
    private static ChangeUi cs;
    private Text step;
    private ProgressBar progressBar;
    private int value;
    private final DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_form_router_page);
        new PermissionBridge().setOnPermissionStateListener(this);
        intComponet();
        cs = new ChangeUi();
    }

    private void intComponet() {
        step = (Text) findComponentById(ResourceTable.Id_step);
        OrmContext connect = helper.getOrmContext("FormDatabase", "FormDatabase.db", FormDatabase.class);
        SensorData sensorData = DatabaseUtils.getSensorData(connect, DateUtils.getDate(0));
        if (sensorData != null) {
            value = sensorData.getStepsValue();
        }
        step.setText(value + "");
        progressBar = (ProgressBar) findComponentById(ResourceTable.Id_progressBar);
        progressBar.setProgressValue(value);
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
    public void onPermissionGranted() {
        Intent intentService = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(getBundleName())
                .withAbilityName(StepSensorService.class.getName())
                .build();
        intentService.setOperation(operation);
        startAbility(intentService);
    }

    private void showTips(Context context) {
        getUITaskDispatcher().asyncDispatch(() -> {
            ToastDialog toastDialog = new ToastDialog(context);
            toastDialog.setAutoClosable(false);
            toastDialog.setContentText("=======No permission");
            toastDialog.show();
        });
    }

    @Override
    public void onPermissionDenied() {
        showTips(MainAbilitySlice.this);
    }

    /**
     * 修改页面
     *
     * @param value 传感器传过来的数据
     */
    public static void updatePage(String value) {
        cs.updatePage(value);
    }

    /**
     * 修改页面
     */
    private class ChangeUi {
        /**
         * 修改页面数据
         *
         * @param sensorValue 传感器传过来的数据
         */
        public void updatePage(String sensorValue) {
            step.setText(sensorValue);
            progressBar.setProgressValue(Integer.parseInt(sensorValue));
        }
    }
}
