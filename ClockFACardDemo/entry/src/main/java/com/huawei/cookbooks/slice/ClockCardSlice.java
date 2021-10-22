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
import com.huawei.cookbooks.utils.ComponentProviderUtils;
import com.huawei.cookbooks.utils.DateUtils;
import com.huawei.cookbooks.utils.LogUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Clock Card Slice
 */
public class ClockCardSlice extends AbilitySlice {
    private static final long SEND_PERIOD = 1000L;
    private static final int COLOR_RGB = 192;
    private static final int TIME_LENGTH = 2;
    private MyEventHandle myEventHandle;
    private Timer timer;
    private final Runnable runnable = new Runnable() {
        private void initHandler() {
            EventRunner runner = EventRunner.getMainEventRunner();
                if (runner == null) {
                    return;
                }
                myEventHandle = new MyEventHandle(runner);
            }

            @Override
            public void run() {
                // 初始化认证对象
                initHandler();
            }
        };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_form_image_with_info_date_card_2_2);
        initComponent();
        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        runnable.run();
                        myEventHandle.sendEvent(1);
                    }
                },
                0,
                SEND_PERIOD);
    }

    /**
     * Init Component
     */
    private void initComponent() {
        Calendar now = Calendar.getInstance();
        Component dateComponent = findComponentById(ResourceTable.Id_date);
        if (dateComponent instanceof Text) {
            Text dateText = (Text) dateComponent;
            dateText.setText(DateUtils.getCurrentDate("yyyy-MM-dd"));
        }
        Component hourComponent = findComponentById(ResourceTable.Id_hour);
        if (hourComponent instanceof Text) {
            Text hourText = (Text) hourComponent;
            int hour = now.get(Calendar.HOUR_OF_DAY);
            setTextValue(hour, hourText);
        }
        Component minComponent = findComponentById(ResourceTable.Id_min);
        if (minComponent instanceof Text) {
            Text minText = (Text) minComponent;
            int min = now.get(Calendar.MINUTE);
            setTextValue(min, minText);
        }
        Component secComponent = findComponentById(ResourceTable.Id_sec);
        if (secComponent instanceof Text) {
            Text secondText = (Text) secComponent;
            int second = now.get(Calendar.SECOND);
            setTextValue(second, secondText);
        }
        int weekId = ComponentProviderUtils.getWeekDayId();
        Component weekComponent = findComponentById(weekId);
        if (weekComponent instanceof Text) {
            Text week = (Text) weekComponent;
            week.setTextColor(new Color(Color.rgb(0, 0, 0)));
        }
        int lastWeekDayId = ComponentProviderUtils.getLastWeekDayId();
        Component lastWeekComponent = findComponentById(lastWeekDayId);
        if (weekComponent != null && lastWeekComponent instanceof Text) {
            Text lastWeek = (Text) lastWeekComponent;
            lastWeek.setTextColor(new Color(Color.rgb(COLOR_RGB, COLOR_RGB, COLOR_RGB)));
        }
    }

    private void setTextValue(int now, Text text) {
        if (String.valueOf(now).length() < TIME_LENGTH) {
            text.setText("0" + now);
        } else {
            text.setText(now + "");
        }
    }

    /**
     * MyEventHandle 用于更新页面
     */
    private class MyEventHandle extends EventHandler {
        MyEventHandle(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            int eventId = event.eventId;
            if (eventId == 1) {
                // 更新页面
                initComponent();
            }
        }
    }

    @Override
    protected void onStop() {
        LogUtils.info("onStop", " start to destroy slice");
        timer.cancel();
    }
}
