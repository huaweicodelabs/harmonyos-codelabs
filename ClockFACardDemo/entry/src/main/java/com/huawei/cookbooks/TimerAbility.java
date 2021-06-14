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
import com.huawei.cookbooks.utils.ComponentProviderUtils;
import com.huawei.cookbooks.utils.DatabaseUtils;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.FormException;
import ohos.aafwk.content.Intent;
import ohos.agp.components.ComponentProvider;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Time PA
 */
public class TimerAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "Demo");
    private static final long SEND_PERIOD = 1000L;
    private DatabaseHelper helper = new DatabaseHelper(this);
    private OrmContext connect;

    @Override
    public void onStart(Intent intent) {
        HiLog.info(LABEL_LOG, "TimerAbility::onStart");
        connect = helper.getOrmContext("FormDatabase", "FormDatabase.db", FormDatabase.class);
        startTimer();
        super.onStart(intent);
    }

    // 卡片更新定时器，每秒更新一次
    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        updateForms();
                    }
                },
                0,
                SEND_PERIOD);
    }

    private void updateForms() {
        // 从数据库中获取卡片信息
        OrmPredicates ormPredicates = new OrmPredicates(Form.class);
        List<Form> formList = connect.query(ormPredicates);
        // 更新时分秒
        if (formList.size() <= 0) {
            return;
        }
        for (Form form : formList) {
            // 遍历卡片列表更新卡片
            ComponentProvider componentProvider = ComponentProviderUtils.getComponentProvider(form, this);
            try {
                Long updateFormId = form.getFormId();
                updateForm(updateFormId, componentProvider);
            } catch (FormException e) {
                // 删除不存在的卡片
                DatabaseUtils.deleteFormData(form.getFormId(), connect);
                HiLog.error(LABEL_LOG, "onUpdateForm updateForm error");
            }
        }
    }

    @Override
    public void onBackground() {
        super.onBackground();
        HiLog.info(LABEL_LOG, "TimerAbility::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.info(LABEL_LOG, "TimerAbility::onStop");
    }
}
