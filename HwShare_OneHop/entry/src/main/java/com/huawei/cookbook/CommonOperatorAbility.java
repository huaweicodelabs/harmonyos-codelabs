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

import com.huawei.cookbook.util.CommonOperationConstants;
import com.huawei.cookbook.util.LogUtils;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.ace.ability.AceAbility;
import ohos.ace.ability.AceInternalAbility;
import ohos.agp.utils.TextTool;
import ohos.app.AbilityContext;
import ohos.bundle.IBundleManager;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The application context capability interface is implemented on the Java side and can be remotely
 * invoked by CommonOperator.js. This class is in singleton mode and can be registered for multiple times.
 */
public class CommonOperatorAbility extends AceInternalAbility {
    private static final String TAG = "CommonOperatorAbility";

    private static final String BUNDLE_NAME = "com.huawei.cookbook";

    private static final String ABILITY_NAME = CommonOperatorAbility.class.getName();

    private static final String KEY_UNIQUE_ID = "uniqueId";

    private static final String DPLINK_URL = "vmall://com.vmall.client/product/detail?prdId=10086555668295";
    private static final String H5_URL = "https://m.vmall.com/product/10086555668295.html";
    private static final String VMALL_PACKAGENAME = "com.vmall.client";

    private static volatile CommonOperatorAbility sInstance;

    private final Set<IRemoteObject> startAbilityCallbackSet = new HashSet<>();

    private final Set<IRemoteObject> checkPackageCallbackSet = new HashSet<>();

    private final Set<IRemoteObject> getUniqueIdCallbackSet = new HashSet<>();

    private final Set<IRemoteObject> goVmallCallbackSet = new HashSet<>();
    private final Set<IRemoteObject> checkRegisterCallbackSet = new HashSet<>();

    private AbilityContext context;

    private String uniqueId;

    private CommonOperatorAbility(AbilityContext abilityContext) {
        super(BUNDLE_NAME, ABILITY_NAME);
    }

    /**
     * get the instance of CommonOperatorAbility
     *
     * @return instance
     */
    public static CommonOperatorAbility getInstance() {
        return sInstance;
    }

    /**
     * register the context and init CommonOperatorAbility
     *
     * @param abilityContext ability context
     */
    public static void register(AbilityContext abilityContext) {
        if (sInstance == null) {
            sInstance = new CommonOperatorAbility(abilityContext);
        }
        sInstance.onRegister(abilityContext);
    }

    private void onRegister(AbilityContext abilityContext) {
        context = abilityContext;
        final String filename = "common_db";
        Preferences preferences;
        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);
        preferences = databaseHelper.getPreferences(filename);
        if (preferences != null) {
            uniqueId = preferences.getString(KEY_UNIQUE_ID, "");
            LogUtils.info("onRegister uniqueId from sp= " + this.uniqueId);
            if (uniqueId == null || uniqueId.isEmpty()) {
                uniqueId = UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.ROOT);
                preferences.putString(KEY_UNIQUE_ID, uniqueId);
                preferences.flush();
                LogUtils.info("onRegister uniqueId from uuid= " + this.uniqueId);
            }
            LogUtils.info("onRegister uniqueId = " + this.uniqueId);

            setInternalAbilityHandler(this::onRemoteRequest);
        }
    }

    private boolean onRemoteRequest(int code,
                                    MessageParcel data, MessageParcel reply, MessageOption option) {
        switch (code) {
            case CommonOperationConstants.START_ABILITY:
                if (startAbility(data, reply, option)) {
                    return false;
                }
                break;
            case CommonOperationConstants.CHECK_REGISTER:
                if (checkRegister(data, reply, option)) {
                    return false;
                }
                break;
            case CommonOperationConstants.CHECK_PACKAGE_INSTALLATION:
                checkPackageInstalled(data);
                if (replyResult(reply, option, "")) {
                    return false;
                }
                break;
            case CommonOperationConstants.OPEN_URL:
                openUrl(data);
                if (replyResult(reply, option, "")) {
                    return false;
                }
                break;
            case CommonOperationConstants.GET_UNIQUE_ID:
                getUniqueId(data);
                if (replyResult(reply, option, "")) {
                    return false;
                }
                break;
            case CommonOperationConstants.TO_VMALL:
                goVMall(data);
                if (replyResult(reply, option, "")) {
                    return false;
                }
                break;
            case CommonOperationConstants.HILINK_REGISTER:
                if (hilinkRegister(data, reply, option)) {
                    return false;
                }
                break;
            case CommonOperationConstants.HW_SHARE_REGISTER:
                hwShare(data);
                break;
            default:
                defaultHandle(reply);
                return false;
        }
        return true;
    }

    private boolean hilinkRegister(MessageParcel data, MessageParcel reply, MessageOption option) {
        ZSONObject object = getVersionInfo(data);
        CommonOperatorUtil.getInstance().goHiLinkRegister(context, object);
        return replyResult(reply, option, "");
    }

    private boolean checkRegister(MessageParcel data, MessageParcel reply, MessageOption option) {
        checkRegisterCallbackSet.clear();
        checkRegisterCallbackSet.add(data.readRemoteObject());
        boolean flag = CommonOperatorUtil.getInstance().isGoHilinkRegister();
        LogUtils.info("flag = " + flag);
        ZSONObject zsonObject = new ZSONObject();
        zsonObject.put("flag", flag ? 0 : 1);
        return replyResult(reply, option, zsonObject.toString());
    }

    private boolean startAbility(MessageParcel data, MessageParcel reply, MessageOption option) {
        LogUtils.info("start ability");
        if (data == null) {
            return true;
        }
        startAbility(data);
        return replyResult(reply, option, "");
    }

    private void hwShare(MessageParcel data) {
        ZSONObject params = ZSONObject.stringToZSON(data.readString());
        HwShareJSInterface hwShareJSInterface = new HwShareJSInterface(context);
        hwShareJSInterface.share(params.getString("prodId"),
                params.getString("macAddress"));
    }

    private ZSONObject getVersionInfo(MessageParcel data) {
        ZSONObject zsonObject = new ZSONObject();
        if (data == null) {
            return new ZSONObject();
        }
        String versionInfo = data.readString();
        LogUtils.info("startAbility dataString:" + versionInfo);
        if (!TextTool.isNullOrEmpty(versionInfo)) {
            ZSONObject version = ZSONObject.stringToZSON(versionInfo);
            String fwv = version.getString("fwv");
            String hwv = version.getString("hwv");
            LogUtils.info("fwv = " + fwv);
            LogUtils.info("hwv = " + hwv);
            zsonObject.put("fwv", fwv);
            zsonObject.put("hwv", hwv);
        }
        return zsonObject;
    }

    private void defaultHandle(MessageParcel reply) {
        Map<String, Object> resultMap = new HashMap<>();
        reply.writeString(ZSONObject.toZSONString(resultMap));
    }

    private boolean replyResult(MessageParcel reply, MessageOption option, String data) {
        if (option.getFlags() == MessageOption.TF_SYNC) {
            // SYNC
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("code", CommonOperationConstants.SUCCESS);
            if (!TextTool.isNullOrEmpty(data)) {
                dataMap.put("data", ZSONObject.stringToZSON(data));
            } else {
                dataMap.put("data", "");
            }
            reply.writeString(ZSONObject.toZSONString(dataMap));
        } else {
            // ASYNC
            MessageParcel responseData = MessageParcel.obtain();
            responseData.writeString(ZSONObject.toZSONString(data));
            IRemoteObject remoteReply = reply.readRemoteObject();
            try {
                remoteReply.sendRequest(CommonOperationConstants.SUCCESS, responseData, MessageParcel.obtain(), new MessageOption());
                responseData.reclaim();
            } catch (RemoteException exception) {
                LogUtils.error(TAG, "RemoteException", exception);
                return true;
            }
        }
        return false;
    }

    private void startAbility(MessageParcel data) {
        startAbilityCallbackSet.clear();
        startAbilityCallbackSet.add(data.readRemoteObject());
        String dataString = data.readString();
        LogUtils.info("startAbility dataString:" + dataString);
        if (TextTool.isNullOrEmpty(dataString)) {
            return;
        }
        ZSONObject dataObject = ZSONObject.stringToZSON(dataString);
        Intent intent = getIntent(dataObject);
        String abilityName = dataObject.getString("abilityName");
        int flags = getFlag(dataObject);
        LogUtils.info("intent = " + ZSONObject.toZSONString(intent));
        if ((flags & Intent.FLAG_NOT_OHOS_COMPONENT) == Intent.FLAG_NOT_OHOS_COMPONENT) {
            if (context instanceof AceAbility) {
                ((AceAbility) context).startAbility(intent);
            } else if (context instanceof Ability) {
                ((Ability) context).startAbility(intent);
            } else {
                context.startAbility(intent, 0);
            }
        } else {
            context.startAbility(intent, 0);
        }
        sendReturnRequest(abilityName);
    }

    private Intent getIntent(ZSONObject dataObject) {
        Intent intent = new Intent();
        String bundleName = dataObject.getString("bundleName");
        String abilityName = dataObject.getString("abilityName");
        ZSONObject params = dataObject.getZSONObject("params");
        ZSONObject options = dataObject.getZSONObject("options");
        if (params == null) {
            params = new ZSONObject();
        }
        if (options == null) {
            options = new ZSONObject();
        }
        setIntentOperation(bundleName, abilityName, params, options, intent);
        int flags = getFlag(options);
        if (flags != 0) {
            intent.addFlags(flags);
        }
        return intent;
    }

    private int getFlag(ZSONObject options) {
        int flags = 0;
        if (options.containsKey("flags")) {
            flags = options.getIntValue("flags");
            LogUtils.erro("flags = " + flags);
        }
        return flags;
    }

    private void setIntentOperation(
            String bundleName, String abilityName, ZSONObject params, ZSONObject options, Intent intent) {
        intent.setParam("params", params.toString());
        Operation operation;
        if (options != null ) {
            // Start ability by action
            Intent.OperationBuilder operationBuilder = new Intent.OperationBuilder();
            operationBuilder.withDeviceId("");
            if(options.containsKey("action")) {
                operationBuilder.withAction(options.getString("action"));
            }
            if (options.containsKey("uri")) {
                String uri = options.getString("uri");
                operationBuilder.withUri(Uri.parse(uri));
            }
            operation = operationBuilder.build();
        } else {
            operation =
                    new Intent.OperationBuilder()
                            .withDeviceId("")
                            .withBundleName(bundleName)
                            .withAbilityName(abilityName)
                            .build();
        }
        intent.setOperation(operation);
    }

    private void sendReturnRequest(String abilityName) {
        MessageParcel returnData = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("started", abilityName);
        returnData.writeString(ZSONObject.toZSONString(resultMap));
        sendRemoteRequest(startAbilityCallbackSet, returnData, reply, option);
        reply.reclaim();
        returnData.reclaim();
    }

    private void checkPackageInstalled(MessageParcel data) {
        if (data == null) {
            return;
        }
        checkPackageCallbackSet.clear();
        checkPackageCallbackSet.add(data.readRemoteObject());
        String dataString = data.readString();
        LogUtils.info("checkPackageInstalled dataString:" + dataString);
        if (TextTool.isNullOrEmpty(dataString)) {
            return;
        }
        ZSONObject dataObject = ZSONObject.stringToZSON(dataString);
        if (dataObject == null) {
            return;
        }
        String packageName = dataObject.getString("bundleName");
        boolean result = false;
        IBundleManager manager = context.getBundleManager();
        try {
            boolean isApplicationEnabled = manager.isApplicationEnabled(packageName);
            if (isApplicationEnabled) {
                result = true;
            }
        } catch (IllegalArgumentException e) {
            LogUtils.info("Package " + packageName + " is not installed!");
        }
        LogUtils.info("Check package " + packageName + " installation result is " + result);
        MessageParcel returnData = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("installed", result);
        returnData.writeString(ZSONObject.toZSONString(resultMap));
        sendRemoteRequest(checkPackageCallbackSet, returnData, reply, option);
        reply.reclaim();
        returnData.reclaim();
    }

    private void goVMall(MessageParcel data) {
        goVmallCallbackSet.clear();
        goVmallCallbackSet.add(data.readRemoteObject());
        if (CommonOperatorUtil.getInstance().checkAppIsInstall(CommonOperatorAbility.VMALL_PACKAGENAME, context)) {
            CommonOperatorUtil.getInstance().openDeepLinkUrl(DPLINK_URL, context);
        } else {
            CommonOperatorUtil.getInstance().openH5Url(H5_URL, context);
        }

        MessageParcel returnData = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("succeed", true);
        returnData.writeString(ZSONObject.toZSONString(resultMap));
        sendRemoteRequest(goVmallCallbackSet, returnData, reply, option);
        reply.reclaim();
        returnData.reclaim();
    }

    private void openUrl(MessageParcel data) {
        if (data == null) {
            return;
        }
        checkPackageCallbackSet.clear();
        checkPackageCallbackSet.add(data.readRemoteObject());
        String dataString = data.readString();
        LogUtils.info("openUrl dataString:" + dataString);
        if (TextTool.isNullOrEmpty(dataString)) {
            return;
        }
        ZSONObject dataObject = ZSONObject.stringToZSON(dataString);
        String url = dataObject.getString("url");
        LogUtils.info("url:" + url);
        Intent intent = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withAction("android.intent.action.VIEW")
                        .withUri(Uri.parse(url))
                        .withFlags(Intent.FLAG_ABILITY_NEW_MISSION | Intent.FLAG_NOT_OHOS_COMPONENT)
                        .build();
        intent.setOperation(operation);
        context.startAbility(intent, 0);
        LogUtils.info("Start url success, Url: " + url);
        MessageParcel returnData = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("succeed", true);
        returnData.writeString(ZSONObject.toZSONString(resultMap));
        sendRemoteRequest(checkPackageCallbackSet, returnData, reply, option);
        reply.reclaim();
        returnData.reclaim();
    }

    private void getUniqueId(MessageParcel data) {
        if (data == null) {
            return;
        }
        getUniqueIdCallbackSet.clear();
        getUniqueIdCallbackSet.add(data.readRemoteObject());
        LogUtils.info("get unique id = " + uniqueId);
        MessageParcel returnData = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(KEY_UNIQUE_ID, uniqueId);
        returnData.writeString(ZSONObject.toZSONString(resultMap));
        sendRemoteRequest(getUniqueIdCallbackSet, returnData, reply, option);
        reply.reclaim();
        returnData.reclaim();
    }

    private void sendRemoteRequest(
            Set<IRemoteObject> callbackSet, MessageParcel returnData, MessageParcel reply, MessageOption option) {
        for (IRemoteObject remoteObject : callbackSet) {
            try {
                remoteObject.sendRequest(0, returnData, reply, option);
            } catch (RemoteException e) {
                LogUtils.erro("RemoteException:" + e);
            }
        }
    }

    /**
     * unregister the CommonOperatorAbility
     */
    public static void unregister() {
        sInstance.onUnregister();
    }

    private void onUnregister() {
        context = null;
        setInternalAbilityHandler(null);
    }

    /**
     * set context
     *
     * @param context context
     */
    public void setContext(AbilityContext context) {
        this.context = context;
    }
}
