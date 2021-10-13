/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.huawei.cookbook.util;

import ohos.app.Context;
import ohos.net.NetManager;

/**
 * common util
 *
 * @ClassName: CommonUtil
 * @Description: java class description
 */
public class CommonUtil {
    /**
     * check network
     *
     * @param context context
     * @return boolean
     */
    public static boolean checkNetwork(Context context) {
        return NetManager.getInstance(context).getAllNets().length > 0;
    }

    /**
     * check user permissions
     */
    public static void checkUserPermissions() {}

}
