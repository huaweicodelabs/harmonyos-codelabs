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

import com.huawei.cookbook.util.LogUtils;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.utils.TextTool;
import ohos.app.Context;
import ohos.bundle.IBundleManager;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * common operator util
 */
public class CommonOperatorUtil {
    private boolean isInit = false;
    private boolean hasDeviceId = false;
    private String deviceId;
    private String uuid;

    public String getDeviceId() {
        return deviceId;
    }

    public String getUuid() {
        return uuid;
    }

    /**
     * 判断是否需要代理注册
     *
     * @return boolean
     */
    public boolean isGoHilinkRegister() {
        return isInit && !hasDeviceId;
    }

    /**
     * hi link proxy resist
     *
     * @param context context
     * @param object object
     */
    public void goHiLinkRegister(Context context, ZSONObject object) {
        LogUtils.erro("object data = " + ZSONObject.toZSONString(object));
        Intent intent = new Intent();
        intent.setParam("uuid", getUuid());
        String bundleName = context.getBundleName();
        String uri =
                "hilink://hilinksvc.huawei.com"
                        + "/device?action=deviceAdd&prodId=25BN&fromApp="+bundleName;
        Operation operation =
                new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_NOT_OHOS_COMPONENT)
                        .withAction("android.intent.action.VIEW")
                        .withUri(Uri.parse(uri))
                        .build();
        intent.setOperation(operation);
        intent.setParam("deviceInfo", object.toString());
        context.startAbility(intent, 0);
    }

    /**
     * check app is install by packageName
     *
     * @param packageName package name
     * @param context context
     * @return whether install
     */
    public boolean checkAppIsInstall(String packageName, Context context) {
        IBundleManager manager = context.getBundleManager();
        try {
            if (checkApp(packageName, manager)) {
                return true;
            }
        } catch (IllegalArgumentException e) {
            LogUtils.info("Package " + packageName + " is not installed!");
        }
        return false;
    }

    private boolean checkApp(String packageName, IBundleManager manager) {
        if (manager != null) {
            boolean isApplicationEnabled = manager.isApplicationEnabled(packageName);
            if (isApplicationEnabled) {
                return true;
            }
        }
        return false;
    }

    /**
     * init data
     *
     * @param isInit isInit
     * @param deviceId deviceId
     * @param uuid uuid
     */
    public void initData(boolean isInit, String deviceId, String uuid) {
        this.isInit = isInit;
        this.deviceId = deviceId;
        this.uuid = uuid;
        if (TextTool.isNullOrEmpty(deviceId)) {
            LogUtils.erro("deviceId is null");
            hasDeviceId = false;
            return;
        }
        hasDeviceId = true;
    }

    /**
     * 通过deeplink拉起其他应用 Vmall
     *
     * @param url uri
     * @param context 上下文对象
     */
    public void openDeepLinkUrl(String url, Context context) {
        Intent intent = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withAction("android.intent.action.VIEW")
                        .withFlags(Intent.FLAG_ABILITY_NEW_MISSION)
                        .withUri(Uri.parse(url))
                        .build();
        intent.setOperation(operation);
        LogUtils.erro("zson data = " + ZSONObject.toZSONString(intent));
        context.startAbility(intent, 0);
    }

    /**
     * 拉起H5
     *
     * @param url h5 url
     * @param context 上下文对象
     */
    public void openH5Url(String url, Context context) {
        Intent intent = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withAction("android.intent.action.VIEW")
                        .withUri(Uri.parse(url))
                        .withFlags(Intent.FLAG_NOT_OHOS_COMPONENT)
                        .build();
        intent.setOperation(operation);
        context.startAbility(intent, 0);
    }

    /**
     * get instance
     *
     * @return CommonOperatorUtil
     */
    public static CommonOperatorUtil getInstance() {
        return CommonOperatorHelper.INSTANCE;
    }

    /**
     * common operator helper
     */
    private static class CommonOperatorHelper {
        private static final CommonOperatorUtil INSTANCE = new CommonOperatorUtil();
    }
}
