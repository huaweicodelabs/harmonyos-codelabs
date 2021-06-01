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

package com.huawei.maildemo.utils;

import com.huawei.maildemo.ui.WidgetHelper;

import ohos.app.AbilityContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Device method utils
 *
 * @since 2021-02-04
 */
public class DeviceUtils {
    private DeviceUtils() {
        // do nothing
    }

    /**
     * Get device picture.
     *
     * @param context context
     * @return local device picture
     */
    public static List<String> getFile(AbilityContext context) {
        if (context.getDistributedDir() == null) {
            WidgetHelper.showTips(context, "分布式异常！");
            return new ArrayList<>();
        }
        File file = new File(context.getDistributedDir().getPath());
        File[] files = file.listFiles();

        if (files == null) {
            // 空目录
            return new ArrayList<>();
        }
        List<String> lists = new ArrayList<>();

        for (File eachFile : files) {
            lists.add(eachFile.getPath());
        }
        return lists;
    }
}
