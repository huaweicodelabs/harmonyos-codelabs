package com.huawei.cookbook;

import com.huawei.cookbook.ble.BleOptionAbility;
import com.huawei.cookbook.util.LogUtils;
import com.huawei.hms.jsb.adapter.har.bridge.HmsBridge;

import ohos.aafwk.ability.AbilityPackage;

/**
 * MyApplication
 */
public class MyApplication extends AbilityPackage {
    @Override
    public void onInitialize() {
        super.onInitialize();
        LogUtils.info("onInitialize onInitialize");
        // 华为账号登录
        HmsBridge.getInstance().initBridge(this);
        // 蓝牙控制Java FA注册
        BleOptionAbility.getInstance().register(this);
        // 公用Java FA注册
        CommonOperatorAbility.register(this);
    }

    @Override
    public void onEnd() {
        super.onEnd();
        LogUtils.info("onEnd onEnd");
        BleOptionAbility.getInstance().unRegister();
    }
}
