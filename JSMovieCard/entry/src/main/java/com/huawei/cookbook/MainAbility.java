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

import com.huawei.cookbook.util.CommonUtils;
import com.huawei.cookbook.util.LogUtils;
import com.huawei.cookbook.util.MovieInfo;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.ace.ability.AceAbility;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * MainAbility
 */
public class MainAbility extends AceAbility {
    /**
     * 2x2 form dimension
     */
    public static final int DEFAULT_DIMENSION_2X2 = 2;
    /**
     * invalid form id
     */
    private static final int INVALID_FORM_ID = -1;
    /**
     * invalid number
     */
    private static final int INVALID_NUMBER = -1;
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.DEBUG, 0x0, MainAbility.class.getName());
    /**
     * formName
     */
    private static final String FORM_NAME = "formName";
    /**
     * dimension
     */
    private static final String DIMENSION = "dimension";
    /**
     * database name
     */
    private static final String SHARED_SP_NAME = "form_info_sp.xml";
    /**
     * movie list
     */
    private static final List<MovieInfo> MOVIE_INFO_LIST = new ArrayList<>();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        if (MOVIE_INFO_LIST.size() == 0) {
            MOVIE_INFO_LIST.addAll(CommonUtils.getMoviesData(this));
        }
        LogUtils.info("MainAbility", "onStart");
        startServiceAbility();
    }

    private void startServiceAbility() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(getBundleName())
                .withAbilityName(ServiceAbility.class.getName())
                .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected ProviderFormInfo onCreateForm(Intent intent) {
        HiLog.info(TAG, "onCreateForm");
        long formId = INVALID_FORM_ID;
        if (intent.hasParameter(AbilitySlice.PARAM_FORM_IDENTITY_KEY)) {
            formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        }
        String formName = "";
        if (intent.hasParameter(AbilitySlice.PARAM_FORM_NAME_KEY)) {
            formName = intent.getStringParam(AbilitySlice.PARAM_FORM_NAME_KEY);
        }
        int dimension = DEFAULT_DIMENSION_2X2;
        if (intent.hasParameter(AbilitySlice.PARAM_FORM_DIMENSION_KEY)) {
            dimension = intent.getIntParam(AbilitySlice.PARAM_FORM_DIMENSION_KEY, DEFAULT_DIMENSION_2X2);
        }
        HiLog.info(TAG, "onCreateForm: formId=" + formId + ",formName=" + formName);
        insertForm(formId, formName, dimension);
        int top = CommonUtils.getRandomInt(MOVIE_INFO_LIST.size(), INVALID_NUMBER);
        int bottom = CommonUtils.getRandomInt(MOVIE_INFO_LIST.size(), top);
        MovieInfo topMovie = MOVIE_INFO_LIST.get(top);
        MovieInfo bottomMovie = MOVIE_INFO_LIST.get(bottom);
        ZSONObject zsonObject = CommonUtils.getJsBindData(top, bottom, topMovie, bottomMovie);
        ProviderFormInfo formInfo = new ProviderFormInfo();
        formInfo.setJsBindingData(new FormBindingData(zsonObject));
        return formInfo;
    }

    /**
     * insert form
     *
     * @param formId form id
     * @param formName form name
     * @param dimension dimension
     */
    private void insertForm(long formId, String formName, int dimension) {
        ZSONObject formObj = new ZSONObject();
        formObj.put(FORM_NAME, formName);
        formObj.put(DIMENSION, dimension);
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getApplicationContext());
        Preferences preferences = databaseHelper.getPreferences(SHARED_SP_NAME);
        preferences.putString(Long.toString(formId), ZSONObject.toZSONString(formObj));
        preferences.flush();
    }

    @Override
    protected void onUpdateForm(long formId) {
        HiLog.info(TAG, "onUpdateForm");
        super.onUpdateForm(formId);
    }

    @Override
    protected void onDeleteForm(long formId) {
        HiLog.info(TAG, "onDeleteForm: formId=" + formId);
        super.onDeleteForm(formId);
        deleteForm(formId);
    }

    private void deleteForm(long formId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Preferences preferences = databaseHelper.getPreferences(SHARED_SP_NAME);
        preferences.delete(Long.toString(formId));
    }
}
