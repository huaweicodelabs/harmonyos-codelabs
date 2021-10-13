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

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.FormBindingData;
import ohos.aafwk.ability.FormException;
import ohos.aafwk.content.Intent;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import ohos.event.notification.NotificationRequest;
import ohos.utils.zson.ZSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * service ability
 */
public class ServiceAbility extends Ability {
    private static final List<MovieInfo> MOVIE_INFO_LIST = new ArrayList<>();
    private static final long PERIOD = 50000L;
    private static final String SHARED_SP_NAME = "form_info_sp.xml";
    private static final int NOTICE_ID = 1005;
    private static final String NOTICE = "JS Movie Service Widget";
    /**
     * invalid number
     */
    private static final int INVALID_NUMBER = -1;

    @Override
    public void onStart(Intent intent) {
        if (MOVIE_INFO_LIST.size() == 0) {
            MOVIE_INFO_LIST.addAll(CommonUtils.getMoviesData(this));
        }
        LogUtils.info("ServiceAbility", "onStart");
        super.onStart(intent);
        startTimer();
        notice();
    }

    // foreground service
    private void notice() {
        // 创建通知
        NotificationRequest request = new NotificationRequest(NOTICE_ID);
        request.setAlertOneTime(true);
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        content.setText(NOTICE);
        NotificationRequest.NotificationContent notificationContent
                = new NotificationRequest.NotificationContent(content);
        request.setContent(notificationContent);
        // 绑定通知
        keepBackgroundRunning(NOTICE_ID, request);
    }

    /**
     * update forms
     */
    private void refreshData() {
        // 获取卡片集合
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getApplicationContext());
        Preferences preferences = databaseHelper.getPreferences(SHARED_SP_NAME);
        Map<String, ?> forms = preferences.getAll();
        // 电影信息集合
        int top = CommonUtils.getRandomInt(MOVIE_INFO_LIST.size(), INVALID_NUMBER);
        int bottom = CommonUtils.getRandomInt(MOVIE_INFO_LIST.size(), top);
        MovieInfo topMovie = MOVIE_INFO_LIST.get(top);
        MovieInfo bottomMovie = MOVIE_INFO_LIST.get(bottom);
        for (String formId : forms.keySet()) {
            try {
                ZSONObject zsonObject = CommonUtils.getJsBindData(top, bottom, topMovie, bottomMovie);
                updateForm(Long.parseLong(formId), new FormBindingData(zsonObject));
            } catch (FormException e) {
                LogUtils.error(MainAbility.class.getName(), "formId:" + formId + " invalid");
            }
        }
    }

    @Override
    public void onBackground() {
        super.onBackground();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // start timer to update forms
    private void startTimer() {
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    /**
                     * 定时更新卡片
                     */
                    public void run() {
                        LogUtils.info("startTimer", "run");
                        getUITaskDispatcher().syncDispatch(() -> refreshData());
                    }
                },
                0, PERIOD
        );
    }
}