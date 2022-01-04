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

package com.huawei.cookbooks.task;

import com.huawei.cookbooks.util.LogUtils;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentConstant;
import ohos.event.intentagent.IntentAgentHelper;
import ohos.event.intentagent.IntentAgentInfo;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.event.notification.NotificationSlot;
import ohos.rpc.RemoteException;
import ohos.vibrator.agent.VibratorAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Notification Task
 */
public class NotifyTask implements Runnable {
    private static final String TAG = "NotifyTask";

    private static final int TIME_DURATION = 1000;

    private static final int REQUEST_CODE = 200;

    private final Context context;

    /**
     * Control gadget - vibrator
     */
    private final VibratorAgent vibratorAgent = new VibratorAgent();

    /**
     * Notify abnormal data which use notification bar.
     */
    private final NotificationSlot slot = new NotificationSlot("slot_001", "slot_default", NotificationSlot.LEVEL_MIN);

    private final int notificationId = 1;

    private final NotificationRequest request = new NotificationRequest(notificationId);

    private final NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();

    public NotifyTask(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        List<Integer> vibratorList = vibratorAgent.getVibratorIdList();
        vibratorAgent.startOnce(vibratorList.get(0), TIME_DURATION);
        try {
            NotificationHelper.addNotificationSlot(slot);
            request.setSlotId(slot.getId());
            String title = "通知";
            String text = "心率异常";
            content.setTitle(title).setText(text);
            NotificationRequest.NotificationContent notificationContent =
                    new NotificationRequest.NotificationContent(content);
            request.setContent(notificationContent);
            request.setIntentAgent(initStartFa());
            NotificationHelper.publishNotification(request);
        } catch (RemoteException ex) {
            LogUtils.info(TAG, "Exception occurred during addNotificationSlot .invocation");
        }
    }

    private IntentAgent initStartFa() {
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName("com.huawei.cookbook")
                .withAbilityName("com.huawei.cookbooks.MainAbility")
                .build();
        intent.setOperation(operation);
        List<Intent> intentList = new ArrayList<>();
        intentList.add(intent);
        List<IntentAgentConstant.Flags> flags = new ArrayList<>();
        flags.add(IntentAgentConstant.Flags.UPDATE_PRESENT_FLAG);
        IntentAgentInfo paramsInfo = new IntentAgentInfo(REQUEST_CODE,
                IntentAgentConstant.OperationType.START_ABILITY, flags, intentList, null);
        return IntentAgentHelper.getIntentAgent(context, paramsInfo);
    }
}
