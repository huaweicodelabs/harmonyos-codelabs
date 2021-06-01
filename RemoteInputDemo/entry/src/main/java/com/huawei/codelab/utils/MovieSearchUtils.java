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

package com.huawei.codelab.utils;

/**
 * 影片搜索工具类
 *
 * @since 2021-02-26
 */
public class MovieSearchUtils {
    /**
     * 构造函数
     *
     * @since 2020-12-03
     */
    private MovieSearchUtils() {
    }

    /**
     * 是否包含当前影片
     *
     * @param movieName 影片名称
     * @param searchName 搜索内容
     * @return 布尔值
     * @since 2020-12-03
     */
    public static boolean isContainMovie(String movieName, String searchName) {
        if (movieName == null || "".equals(movieName) || searchName == null || "".equals(searchName)) {
            return false;
        }
        char[] searchNameChars = searchName.toCharArray();
        for (char ch : searchNameChars) {
            if (!movieName.contains(ch + "")) {
                return false;
            }
        }
        return true;
    }
}
