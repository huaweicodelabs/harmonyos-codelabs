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

package com.huawei.cookbooks.utils;

import com.huawei.cookbooks.ResourceTable;
import com.huawei.cookbooks.database.Form;

import ohos.agp.components.ComponentProvider;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.app.Context;

import java.util.Calendar;
import java.util.Locale;

/**
 * Component ProviderUtils
 */
public class ComponentProviderUtils {
    private static final int COLOR_NUM_255 = 255;
    private static final int COLOR_NUM_245 = 245;
    private static final int COLOR_NUM_238 = 238;
    private static final int COLOR_NUM_192 = 192;
    // 当前星期颜色
    private static final Color NOW_WEEK_COLOR = new Color(Color.rgb(COLOR_NUM_255, COLOR_NUM_245, COLOR_NUM_238));

    // 原色星期
    private static final Color PRIMARY_WEEK_COLOR = new Color(Color.rgb(COLOR_NUM_192, COLOR_NUM_192, COLOR_NUM_192));

    private static final int FONT_SIZE = 100;
    private static final int WEEK_DAYS = 7;
    private static final int STRING_LENGTH = 2;
    private static final int DIM_VERSION = 3;
    private static final int DEFAULT_DIMENSION_2X2 = 2;
    private static final int MONDAY = 2;
    private static final int TUESDAY = 3;
    private static final int WEDNESDAY = 4;
    private static final int THURSDAY = 5;
    private static final int FRIDAY = 6;
    private static final int SATURDAY = 7;
    private static final String LANGUAGE_ENGLISH = "en";

    private ComponentProviderUtils() {
    }

    /**
     * Obtain the day of the week
     *
     * @return week
     */
    public static int getWeekDayId() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return getWeekIdResult(week);
    }

    /**
     * get week component id
     *
     * @param week week
     * @return component id
     */
    private static int getWeekIdResult(int week) {
        int result;
        switch (week) {
            case MONDAY:
                result = ResourceTable.Id_mon;
                break;
            case TUESDAY:
                result = ResourceTable.Id_tue;
                break;
            case WEDNESDAY:
                result = ResourceTable.Id_wed;
                break;
            case THURSDAY:
                result = ResourceTable.Id_thu;
                break;
            case FRIDAY:
                result = ResourceTable.Id_fri;
                break;
            case SATURDAY:
                result = ResourceTable.Id_sat;
                break;
            default:
                result = ResourceTable.Id_sun;
                break;
        }
        return result;
    }

    /**
     * Obtains the ComponentProvider object
     *
     * @param form form info
     * @param context context
     * @return component provider
     */
    public static ComponentProvider getComponentProvider(Form form, Context context) {
        int layoutId = ResourceTable.Layout_form_image_with_info_date_card_2_2;
        if (form.getDimension() == DIM_VERSION) {
            layoutId = ResourceTable.Layout_form_image_with_info_date_card_2_4;
        }
        ComponentProvider componentProvider = new ComponentProvider(layoutId, context);
        setComponentProviderValue(componentProvider);
        setWeekTextSize(form.getDimension(),componentProvider);
        return componentProvider;
    }

    /**
     * set week text size
     * @param dimension form dimension
     * @param componentProvider componentProvider
     */
    private static void setWeekTextSize(int dimension, ComponentProvider componentProvider) {
        String language = Locale.getDefault().getLanguage();
        if (dimension == DEFAULT_DIMENSION_2X2 && language.equals(LANGUAGE_ENGLISH)) {
            componentProvider.setTextSize(ResourceTable.Id_mon, FONT_SIZE, Text.TextSizeType.VP);
            componentProvider.setTextSize(ResourceTable.Id_tue, FONT_SIZE, Text.TextSizeType.VP);
            componentProvider.setTextSize(ResourceTable.Id_wed, FONT_SIZE, Text.TextSizeType.VP);
            componentProvider.setTextSize(ResourceTable.Id_thu, FONT_SIZE, Text.TextSizeType.VP);
            componentProvider.setTextSize(ResourceTable.Id_fri, FONT_SIZE, Text.TextSizeType.VP);
            componentProvider.setTextSize(ResourceTable.Id_sat, FONT_SIZE, Text.TextSizeType.VP);
            componentProvider.setTextSize(ResourceTable.Id_sun, FONT_SIZE, Text.TextSizeType.VP);
        }
    }

    /**
     * Time converted to string
     *
     * @param time time
     * @return time string
     */
    private static String int2String(int time) {
        String timeString;
        if (String.valueOf(time).length() < STRING_LENGTH) {
            timeString = "0" + time;
        } else {
            timeString = time + "";
        }
        return timeString;
    }

    /**
     * Set the value of componentProvider
     *
     * @param componentProvider component provider
     */
    private static void setComponentProviderValue(ComponentProvider componentProvider) {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        String hourString = int2String(hour);
        String minString = int2String(min);
        String secondString = int2String(second);
        componentProvider.setText(ResourceTable.Id_date, DateUtils.getCurrentDate("yyyy-MM-dd"));
        componentProvider.setText(ResourceTable.Id_hour, hourString);
        componentProvider.setText(ResourceTable.Id_min, minString);
        componentProvider.setText(ResourceTable.Id_sec, secondString);

        // 获取当前星期
        int weekDayId = getWeekDayId();
        componentProvider.setTextColor(weekDayId, NOW_WEEK_COLOR);

        // 将前一天的星期改回原色
        int lastWeekId = getLastWeekDayId();
        componentProvider.setTextColor(lastWeekId, PRIMARY_WEEK_COLOR);
    }

    /**
     * obtain previous day of the week
     *
     * @return previous day of the week
     */
    public static int getLastWeekDayId() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        int lastWeek;
        if (week == 1) {
            lastWeek = WEEK_DAYS;
        } else {
            lastWeek = week - 1;
        }
        return getWeekIdResult(lastWeek);
    }
}
