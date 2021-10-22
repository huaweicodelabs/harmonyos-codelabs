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

package com.huawei.cookbooks;

import com.huawei.cookbooks.database.Form;
import com.huawei.cookbooks.database.FormDatabase;
import com.huawei.cookbooks.slice.ClockCardSlice;
import com.huawei.cookbooks.utils.ComponentProviderUtils;
import com.huawei.cookbooks.utils.DatabaseUtils;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.ProviderFormInfo;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.ComponentProvider;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;

/**
 * Card Main Ability
 */
public class MainAbility extends Ability {
    private static final int DEFAULT_DIMENSION_2X2 = 2;
    private static final int DEFAULT_DIMENSION_2X4 = 3;
    private static final String EMPTY_STRING = "";
    private static final int INVALID_FORM_ID = -1;
    private final DatabaseHelper helper = new DatabaseHelper(this);
    private OrmContext connect;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        connect = helper.getOrmContext("FormDatabase", "FormDatabase.db", FormDatabase.class);
        // 启动TimerAbility
        Intent intentService = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName(getBundleName())
                        .withAbilityName(TimerAbility.class.getName())
                        .build();
        intentService.setOperation(operation);
        startAbility(intentService);
        super.setMainRoute(ClockCardSlice.class.getName());
    }

    @Override
    protected ProviderFormInfo onCreateForm(Intent intent) {
        if (intent == null) {
            return new ProviderFormInfo();
        }
        // 获取卡片id
        long formId;
        if (intent.hasParameter(AbilitySlice.PARAM_FORM_IDENTITY_KEY)) {
            formId = intent.getLongParam(AbilitySlice.PARAM_FORM_IDENTITY_KEY, INVALID_FORM_ID);
        } else {
            return new ProviderFormInfo();
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
        int layoutId = ResourceTable.Layout_form_image_with_info_date_card_2_2;
        if (dimension == DEFAULT_DIMENSION_2X4) {
            layoutId = ResourceTable.Layout_form_image_with_info_date_card_2_4;
        }
        ProviderFormInfo formInfo = new ProviderFormInfo(layoutId, this);
        // 存储卡片信息
        Form form = new Form(formId, formName, dimension);
        ComponentProvider componentProvider = ComponentProviderUtils.getComponentProvider(form, this);
        formInfo.mergeActions(componentProvider);
        if (connect == null) {
            connect =
                    helper.getOrmContext("FormDatabase", "FormDatabase.db", FormDatabase.class);
        }
        DatabaseUtils.insertForm(form, this);
        return formInfo;
    }

    @Override
    protected void onDeleteForm(long id) {
        super.onDeleteForm(id);
        // 删除数据库中的卡片信息
        DatabaseUtils.deleteFormData(id, this);
    }
}
