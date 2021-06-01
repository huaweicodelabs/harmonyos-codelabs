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

package com.huawei.codelab.proxy;

import ohos.app.Context;

import java.util.Map;

/**
 * 远程连接管理类接口
 *
 * @since 2021-02-25
 */
public interface ConnectManager {
    /**
     * 连接远程PA
     *
     * @param context 上下文
     * @param deviceId 设备id
     */
    void connectPa(Context context, String deviceId);

    /**
     * 发送指令
     *
     * @param requestType 指令类型
     * @param params 指令参数
     */
    void sendRequest(int requestType, Map<String, String> params);
}
