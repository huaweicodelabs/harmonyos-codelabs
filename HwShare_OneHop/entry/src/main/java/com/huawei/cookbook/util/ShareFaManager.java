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

import com.huawei.cookbook.ble.BleHelper;
import com.huawei.hwshare.third.HwShareCallbackStub;
import com.huawei.hwshare.third.HwShareServiceProxy;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.utils.TextTool;
import ohos.app.Context;
import ohos.bundle.ElementName;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.interwork.utils.PacMapEx;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

/**
 * ShareFaManager
 */
public class ShareFaManager {
    /**
     * fa icon
     */
    public static final String HM_FA_ICON = "ohos_fa_icon";
    /**
     * fa name
     */
    public static final String HM_FA_NAME = "ohos_fa_name";
    /**
     * ability name
     */
    public static final String HM_ABILITY_NAME = "ohos_ability_name";
    /**
     * bundle name
     */
    public static final String HM_BUNDLE_NAME = "ohos_bundle_name";
    /**
     * share action
     */
    public static final String SHARE_ACTION = "com.huawei.instantshare.action.THIRD_SHARE";
    /**
     * sharing content info
     */
    public static final String SHARING_CONTENT_INFO = "sharing_fa_content_info";
    /**
     * sharing extra info
     */
    public static final String SHARING_EXTRA_INFO = "sharing_fa_extra_info";
    /**
     * fa type
     */
    public static final String SHARING_FA_TYPE = "sharing_fa_type";
    /**
     * share pkg name
     */
    public static final String SHARE_PKG_NAME = "com.huawei.android.instantshare";
    /**
     * sharing thumb data
     */
    public static final String SHARING_THUMB_DATA = "sharing_fa_thumb_data";
    /**
     * log format
     */
    private static final String LOG_FORMAT = "%{public}s: %{public}s";
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, "ShareFa");
    /**
     * log tag
     */
    private static final String TAG = "ShareHmFaManager";
    /**
     * unbind time
     */
    private static final long UNBIND_TIME = 20 * 1000L;
    private static ShareFaManager sSingleInstance;
    private final EventHandler mHandler = new EventHandler(EventRunner.getMainEventRunner());
    private final HwShareCallbackStub mFaCallback = new HwShareCallbackStub("HwShareCallbackStub") {
        @Override
        public void notifyState(int state) {
            mHandler.postTask(() -> {
                HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "notifyState: " + state);
                startHwShare(state);
            });
        }
    };

    private void startHwShare(int state) {
        if (state == 0) {
            isPermission = true;
            if (mSharePacMap != null) {
                shareFaInfo();
            }
        }
    }

    private final IAbilityConnection mConnection = new IAbilityConnection() {
        @Override
        public void onAbilityConnectDone(ElementName elementName, IRemoteObject remoteObject, int index) {
            HiLog.error(LABEL_LOG, LOG_FORMAT, TAG, "onAbilityConnectDone success.");
            mHandler.postTask(() -> {
                mShareService = new HwShareServiceProxy(remoteObject);
                try {
                    mShareService.startAuth(mAppId, mFaCallback);
                } catch (RemoteException e) {
                    HiLog.error(LABEL_LOG, LOG_FORMAT, TAG, "startAuth error.");
                }
            });
        }

        @Override
        public void onAbilityDisconnectDone(ElementName elementName, int index) {
            HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "onAbilityDisconnectDone.");
            mHandler.postTask(() -> {
                mShareService = null;
                isPermission = false;
            });
        }
    };
    private boolean isPermission = false;
    private Context mContext;

    private String mAppId;

    private PacMapEx mSharePacMap;

    private HwShareServiceProxy mShareService;
    private final Runnable mTask = () -> {
        if (mContext != null && mShareService != null) {
            mContext.disconnectAbility(mConnection);
            isPermission = false;
            mShareService = null;
        }
    };

    private ShareFaManager(Context context) {
        mContext = context;
    }

    /**
     * 单例模式获取ShareFaManager的实例对象
     *
     * @param context 程序Context
     * @return ShareFaManager实例对象
     */
    public static synchronized ShareFaManager getInstance(Context context) {
        if (sSingleInstance == null && context != null) {
            sSingleInstance = new ShareFaManager(context.getApplicationContext());
        }
        return sSingleInstance;
    }

    private void shareFaInfo() {
        if (mShareService == null) {
            return;
        }
        if (isPermission) {
            HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "start shareFaInfo.");
            startShare();
        }
        // 不使用时断开
        mHandler.postTask(mTask, UNBIND_TIME);
    }

    // 开始分享
    private void startShare() {
        try {
            mShareService.shareFaInfo(mSharePacMap);
            // 重置分享参数
            mSharePacMap = null;
        } catch (RemoteException e) {
            HiLog.error(LABEL_LOG, LOG_FORMAT, TAG, "shareFaInfo error.");
        }
    }

    /**
     * 用于分享服务
     *
     * @param appId app id
     * @param pacMap pac map
     * @param macAddress mac address
     */
    public void shareFaInfo(String appId, PacMapEx pacMap, String macAddress) {
        if (mContext == null) {
            return;
        }
        mAppId = appId;
        mSharePacMap = pacMap;
        mHandler.removeTask(mTask);
        shareFaInfo();
        boolean flag = bindShareService();
        LogUtils.info("flag = " + flag);
        // 由于蓝牙只能一一配对，因此拉起Fa后，关闭本机的蓝牙连接
        if (flag && !TextTool.isNullOrEmpty(macAddress)) {
            BleHelper.getInstance().closeBleConnection(macAddress);
        }
    }

    // 连接华为分享服务
    private boolean bindShareService() {
        if (mShareService != null) {
            return false;
        }
        HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "start bindShareService.");
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName(SHARE_PKG_NAME)
                .withAction(SHARE_ACTION)
                .withFlags(Intent.FLAG_NOT_OHOS_COMPONENT)
                .build();
        intent.setOperation(operation);
        return mContext.connectAbility(intent, mConnection);
    }
}
