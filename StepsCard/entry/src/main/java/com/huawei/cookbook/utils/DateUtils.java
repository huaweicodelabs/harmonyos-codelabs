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

package com.huawei.cookbook.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日期计算
 */
public class DateUtils {
    private DateUtils() {
    }

    /**
     * 获取日期
     *
     * @param days 前几天
     * @return 返回年月日格式的日期
     */
    public static String getDate(int days) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        return simpleDateFormat.format(calendar.getTime());
    }
}
