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

import com.huawei.cookbook.CommonOperatorAbility;
import com.huawei.cookbook.CommonOperatorUtil;
import com.huawei.cookbook.ControlAbility;
import com.huawei.cookbook.ble.BleHelper;
import com.huawei.cookbook.util.CommonUtil;
import com.huawei.cookbook.util.LogUtils;
import com.huawei.cookbook.util.PermissionManager;
import com.huawei.cookbook.util.ToastUtil;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.ace.ability.AceAbility;
import ohos.agp.utils.TextTool;
import ohos.utils.zson.ZSONObject;

import java.util.Locale;

/**
 * main ability
 */
public class MainAbility extends AceAbility {
    private static final int WINDOW_MODAL_CARD = 3;
    private static final String INTENT_PARAM = "window_modal";
    private boolean isFirst = true;
    private boolean isShare = false;
    private String bleMac;
    private String prodId;

    @Override
    public void onStart(Intent intent) {
        LogUtils.info("mainAbility intent params = " + ZSONObject.toZSONString(intent.getParams()));
        try {
            String deviceId = intent.getStringParam("deviceId");
            String uuid = intent.getStringParam("uuid");
            int businessStage = intent.getIntParam("businessStage", 0);
            boolean init = intent.getBooleanParam("init", false);
            // 获取设备Mac地址和产品id
            getMacAndProdId(intent);
            CommonOperatorAbility.getInstance().setContext(this);
            if (TextTool.isNullOrEmpty(bleMac) || TextTool.isNullOrEmpty(prodId)) {
                ToastUtil.showToast(this, "未获取到Mac地址或pid");
                terminateAbility();
                return;
            }
            intent.setParam(INTENT_PARAM, WINDOW_MODAL_CARD);
            intent.setParam("macAddress", bleMac);
            intent.setParam("productId", prodId);
            intent.setParam("deviceId", deviceId);
            intent.setParam("isShare", isShare);
            setPageParams(null, intent.getParams());
            super.onStart(intent);
            if (checkBleAndNetWork(deviceId, uuid, businessStage, init)) {
                return;
            }
        } catch (Exception e) {
            LogUtils.error("MainAbility onStart get intent params error");
        }
        PermissionManager permissionManager = new PermissionManager(this);
        permissionManager.checkPermissions(new String[]{"ohos.permission.LOCATION"}, 0);
    }

    private void getMacAndProdId(Intent intent) {
        if (intent.hasParameter("sharing_fa_extra_info")) {
            // 华为分享拉起的
            String sharingFaExtraInfo = intent.getStringParam("sharing_fa_extra_info");
            ZSONObject object = ZSONObject.stringToZSON(sharingFaExtraInfo);
            isShare = object.getBoolean("isShare");
            if (isShare) {
                // 获取设备mac地址和产品id
                bleMac = object.getString("macAddress");
                prodId = object.getString("prodId");
            }
        } else {
            // 碰一碰拉起的
            bleMac = intent.getStringParam("bleMac");
            String productInfo = intent.getStringParam("productInfo");
            if (!TextTool.isNullOrEmpty(productInfo) && productInfo.length() >= 6) {
                prodId = productInfo.substring(0, 4);
            }
        }
    }

    // 检查蓝牙和网络状态
    private boolean checkBleAndNetWork(String deviceId, String uuid, int businessStage, boolean init) {
        CommonOperatorUtil.getInstance().initData(init, deviceId, uuid);
        if (!TextTool.isNullOrEmpty(deviceId)) {
            if (businessStage == 1) {
                // 代理注册之后的回拉
                goToControl(bleMac, prodId);
                terminateAbility();
                return true;
            }
        }
        if (!isBleOpen()) {
            ToastUtil.showToast(this, "未开启蓝牙");
            terminateAbility();
            return true;
        }
        if (!CommonUtil.checkNetwork(this)) {
            ToastUtil.showToast(this, "未开启网络");
            terminateAbility();
            return true;
        }
        return false;
    }

    private void goToControl(String mac, String pid) {
        Intent intent = new Intent();
        ZSONObject params = getParams(mac, pid);
        LogUtils.erro("params = " + params.toString());
        intent.setParam("params", params.toString());
        Operation operation =
                new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName(getBundleName())
                        .withAbilityName(ControlAbility.class.getName())
                        .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    private ZSONObject getParams(String mac, String pid) {
        ZSONObject params = new ZSONObject();
        params.put("macAddress", mac);
        params.put("productId", pid);
        return params;
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.erro("mainAbility on stop");
    }

    private boolean isBleOpen() {
        return BleHelper.getInstance().isBlueOpen();
    }

}
