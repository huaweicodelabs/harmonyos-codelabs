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

package ohos.codelabs.distributedvideo.player.util;

import java.util.Locale;

/**
 * Date util
 *
 * @since 2021-04-04
 */
public class DateUtils {
    private static final int ONE_SECONDS_MS = 1000;
    private static final int ONE_MINS_MINUTES = 60;
    private static final int NUMBER = 16;
    private static final String TIME_FORMAT = "%02d";
    private static final String SEMICOLON = ":";

    private DateUtils() {
    }

    /**
     * conversion of msToString
     *
     * @param ms ms
     * @return string
     */
    public static String msToString(int ms) {
        StringBuilder sb = new StringBuilder(NUMBER);
        int seconds = ms / ONE_SECONDS_MS;
        int minutes = seconds / ONE_MINS_MINUTES;
        if (minutes > ONE_MINS_MINUTES) {
            sb.append(String.format(Locale.ENGLISH, TIME_FORMAT, minutes / ONE_MINS_MINUTES));
            sb.append(SEMICOLON);
            sb.append(String.format(Locale.ENGLISH, TIME_FORMAT, minutes % ONE_MINS_MINUTES));
            sb.append(SEMICOLON);
        } else {
            sb.append("00:");
            sb.append(String.format(Locale.ENGLISH, TIME_FORMAT, minutes));
            sb.append(SEMICOLON);
        }

        if (seconds > minutes * ONE_MINS_MINUTES) {
            sb.append(String.format(Locale.ENGLISH, TIME_FORMAT, seconds - minutes * ONE_MINS_MINUTES));
        } else {
            sb.append("00");
        }
        return sb.toString();
    }
}
