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
import com.huawei.cookbook.slice.StepFormAbilitySlice;
import com.huawei.cookbook.utils.ChartDataUtils;
import com.huawei.cookbook.utils.DatabaseUtils;
import com.huawei.cookbook.utils.DateUtils;
import com.huawei.cookbook.utils.LogUtils;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.FormException;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;
import ohos.utils.zson.ZSONObject;

/**
 * StepFormAbility
 */
public class StepFormAbility extends Ability {
    private static final String TAG = StepFormAbility.class.getSimpleName();
    private static final String EMPTY_STRING = "";
    private static final int INVALID_FORM_ID = -1;
    private static final int DEFAULT_DIMENSION_2X2 = 2;
    private final DatabaseHelper helper = new DatabaseHelper(this);
    private OrmContext connect;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(StepFormAbilitySlice.class.getName());
        connect = helper.getOrmContext("FormDatabase", "FormDatabase.db", FormDatabase.class);
    }

    @Override
    protected ProviderFormInfo onCreateForm(Intent intent) {
        ProviderFormInfo providerFormInfo = new ProviderFormInfo();
        if (intent == null) {
            return providerFormInfo;
        }
        // 获取卡片id
        long formId = INVALID_FORM_ID;
        if (intent.hasParameter(AbilitySlice.PARAM_FORM_IDENTITY_KEY)) {
            formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        }
        // 获取卡片名称
        String formName = EMPTY_STRING;
        if (intent.hasParameter(AbilitySlice.PARAM_FORM_NAME_KEY)) {
            formName = intent.getStringParam(AbilitySlice.PARAM_FORM_NAME_KEY);
        }
        // 获取卡片规格
        int dimension = DEFAULT_DIMENSION_2X2;
        if (intent.hasParameter(AbilitySlice.PARAM_FORM_DIMENSION_KEY)) {
            dimension = intent.getIntParam(AbilitySlice.PARAM_FORM_DIMENSION_KEY, DEFAULT_DIMENSION_2X2);
        }
        connect = helper.getOrmContext("FormDatabase", "FormDatabase.db", FormDatabase.class);
        // 存储卡片信息
        Form form = new Form(formId, formName, dimension);
        DatabaseUtils.insertForm(form, connect);
        // 获取当天的步数
        SensorData sensorData = DatabaseUtils.getSensorData(connect, DateUtils.getDate(0));
        String stepValue;
        if (sensorData != null) {
            stepValue = sensorData.getStepsValue() + "";
        } else {
            stepValue = "0";
        }
        // 获取卡片页面需要的数据
        ZSONObject zsonObject = ChartDataUtils.getZsonObject(stepValue, dimension, connect);
        providerFormInfo.setJsBindingData(new FormBindingData(zsonObject));
        return providerFormInfo;
    }

    @Override
    protected void onUpdateForm(long formId) {
        LogUtils.info(TAG, "onUpdateForm()");
        super.onUpdateForm(formId);
        ZSONObject zsonObject = new ZSONObject();
        zsonObject.put(EMPTY_STRING, EMPTY_STRING);
        try {
            updateForm(formId, new FormBindingData(zsonObject));
        } catch (FormException e) {
            LogUtils.error(TAG, e.getMessage());
        }
    }

    @Override
    protected void onDeleteForm(long formId) {
        LogUtils.info(TAG, "onDeleteForm():formId=" + formId);
        super.onDeleteForm(formId);
        // 删除数据库中的卡片信息
        DatabaseUtils.deleteFormData(formId, connect);
    }
}
