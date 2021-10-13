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

package com.huawei.cookbook;

import com.huawei.cookbook.ble.BleHelper;
import com.huawei.cookbook.util.LogUtils;

import ohos.aafwk.content.Intent;
import ohos.ace.ability.AceAbility;
import ohos.agp.window.service.WindowManager;
import ohos.utils.zson.ZSONObject;

/**
 * control ability
 */
public class ControlAbility extends AceAbility {
    @Override
    public void onStart(Intent intent) {
        WindowManager.getInstance().getTopWindow().get().addFlags(WindowManager.LayoutConfig.MARK_TRANSLUCENT_STATUS);
        try{
            LogUtils.info("intent is = " + ZSONObject.toZSONString(intent.getParams()));
            LogUtils.info("control ons tart");
            intent.setParam("window_modal", 1);
            setInstanceName("control");
            setPageParams(null, intent.getParams());
        }catch (Exception e){
            LogUtils.error("ControlAbility onStart get intent params error");
        }
        super.onStart(intent);
    }

    @Override
    public void onActive() {
        super.onActive();
        LogUtils.erro("control onActive");
    }

    @Override
    public void onInactive() {
        super.onInactive();
        LogUtils.erro("control onInactive");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.erro("control onStop");
        BleHelper.getInstance().disConnect();
    }
}
