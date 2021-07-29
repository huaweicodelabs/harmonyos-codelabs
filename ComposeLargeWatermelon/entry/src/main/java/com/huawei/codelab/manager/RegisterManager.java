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

package com.huawei.codelab.manager;

import com.huawei.codelab.slice.MainAbilitySlice;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.continuation.DeviceConnectState;
import ohos.aafwk.ability.continuation.ExtraParams;
import ohos.aafwk.ability.continuation.IContinuationDeviceCallback;
import ohos.aafwk.ability.continuation.RequestCallback;
import ohos.aafwk.ability.continuation.IContinuationRegisterManager;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.event.commonevent.MatchingSkills;
import ohos.event.commonevent.CommonEventSupport;
import ohos.event.commonevent.CommonEventSubscribeInfo;
import ohos.event.commonevent.CommonEventSubscriber;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.rpc.RemoteException;

import com.huawei.codelab.slice.SmartWatchSlice;
import com.huawei.codelab.utils.LogUtils;

/**
 * IContinuationRegisterManager and CommonEvent
 *
 * @since 2021-06-23
 */
public class RegisterManager {
    // abilityToken
    private int abilityToken;

    // IContinuationRegisterManager
    private IContinuationRegisterManager continuationRegisterManager;

    // Callback
    private DeviceCallback deviceCallback;

    // registerContinuation - showContinuation
    private boolean show;

    // Public events
    private MyCommonEventSubscriber subscriber;

    private CommonEvent commonEvent;

    public RegisterManager() {
        subscribeCommonEvent();
    }

    /**
     * register Continuation
     */
    public void registerContinuation(AbilitySlice context, DeviceCallback deviceCallback, boolean show) {
        if (continuationRegisterManager != null) {
            if (show) {
                showContinuation();
            }
        } else {
            this.deviceCallback = deviceCallback;
            this.show = show;
            continuationRegisterManager = context.getContinuationRegisterManager();

            ExtraParams params = new ExtraParams();
            String[] devTypes = new String[]{ExtraParams.DEVICETYPE_SMART_PAD,
                    ExtraParams.DEVICETYPE_SMART_WATCH,
                    ExtraParams.DEVICETYPE_SMART_PHONE};
            params.setDevType(devTypes);

            continuationRegisterManager.register(context.getBundleName(), params, callback, requestCallback);
        }
    }

    /**
     * show Continuation
     */
    private void showContinuation() {
        ExtraParams extraParams = new ExtraParams();
        extraParams.setDevType(new String[]{ExtraParams.DEVICETYPE_SMART_TV,
                ExtraParams.DEVICETYPE_SMART_PAD,
                ExtraParams.DEVICETYPE_SMART_WATCH,
                ExtraParams.DEVICETYPE_SMART_PHONE});
        extraParams.setDescription("小设备合成");
        continuationRegisterManager.showDeviceList(abilityToken, extraParams, null);
    }

    /**
     * unregister Continuation
     */
    private void unregisterContinuation() {
        if (continuationRegisterManager != null) {
            // Deregistering the Transfer Task Management Service
            continuationRegisterManager.unregister(abilityToken, null);
            // Disconnecting the Transfer Task Management Service
            continuationRegisterManager.disconnect();
        }
    }

    /**
     * IContinuationDeviceCallback
     */
    private IContinuationDeviceCallback callback = new IContinuationDeviceCallback() {
        @Override
        public void onDeviceConnectDone(String deviceId, String val) {
            EventHandler eventHandler = new EventHandler(EventRunner.getMainEventRunner());
            eventHandler.postTask(new Runnable() {
                @Override
                public void run() {
                    if (deviceCallback != null) {
                        deviceCallback.onItemClick(deviceId);
                    }
                    continuationRegisterManager.updateConnectStatus(abilityToken,
                            deviceId, DeviceConnectState.IDLE.getState(), null);
                }
            });
        }

        @Override
        public void onDeviceDisconnectDone(String deviceId) {
        }
    };


    /**
     * RequestCallback
     */
    private RequestCallback requestCallback = new RequestCallback() {
        @Override
        public void onResult(int result) {
            abilityToken = result;
            if (show) {
                showContinuation();
            }
        }
    };

    /**
     * register CommonEvent
     */
    private void subscribeCommonEvent() {
        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(SmartWatchSlice.CODE); // Custom Event
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_SCREEN_ON);
        CommonEventSubscribeInfo subscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
        subscriber = new MyCommonEventSubscriber(subscribeInfo);
        try {
            CommonEventManager.subscribeCommonEvent(subscriber);
        } catch (RemoteException e) {
            LogUtils.info("error");
        }
    }

    /**
     * unsubscribe CommonEvent
     */
    private void unsubscribeCommonEvent() {
        if (subscriber != null) {
            try {
                CommonEventManager.unsubscribeCommonEvent(subscriber);
            } catch (RemoteException e) {
                LogUtils.info("error");
            }
        }
    }

    /**
     * realse
     */
    public void realse() {
        unregisterContinuation();
        unsubscribeCommonEvent();
    }

    /**
     * CommonEvent Subscriber
     */
    private class MyCommonEventSubscriber extends CommonEventSubscriber {
        MyCommonEventSubscriber(CommonEventSubscribeInfo info) {
            super(info);
        }

        @Override
        public void onReceiveEvent(CommonEventData commonEventData) {
            int code = commonEventData.getIntent().getIntParam(SmartWatchSlice.CODE, 0);
            String remoteDeviceId = commonEventData.getIntent().getStringParam(MainAbilitySlice.DEVICE_ID);
            if (commonEvent != null) {
                commonEvent.onReceiveEvent(code, remoteDeviceId);
            }
        }
    }

    /**
     * CommonEvent callback
     *
     * @param commonEvent commonEvent
     */
    public void setCommonEvent(CommonEvent commonEvent) {
        this.commonEvent = commonEvent;
    }

    /**
     * Callback
     */
    public interface DeviceCallback {
        /**
         * onItemClick
         *
         * @param deviceId deviceId
         */
        void onItemClick(String deviceId);
    }

    /**
     * CommonEvent
     */
    public interface CommonEvent {
        /**
         * receive Event
         *
         * @param code     code
         * @param deviceId deviceId
         */
        void onReceiveEvent(int code, String deviceId);
    }

}
