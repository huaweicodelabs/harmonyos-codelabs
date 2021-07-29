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

package com.huawei.codelab.slice;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.map.Const;
import com.huawei.codelab.util.ImageUtils;
import com.huawei.codelab.util.LogUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.event.commonevent.CommonEventSubscribeInfo;
import ohos.event.commonevent.CommonEventSubscriber;
import ohos.event.commonevent.CommonEventSupport;
import ohos.event.commonevent.MatchingSkills;
import ohos.rpc.RemoteException;

/**
 * WatchAbilitySlice
 *
 * @since 2021-03-12
 */
public class WatchAbilitySlice extends AbilitySlice {
    private static final String TAG = MainAbilitySlice.class.getSimpleName();

    private MyCommonEventSubscriber subscriber;

    private Text contentComponent;

    private Image actionImg;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_watch_main);
        subscribe();
        findComponentById();
    }

    private void findComponentById() {
        contentComponent = (Text) findComponentById(ResourceTable.Id_action_content);
        actionImg = (Image) findComponentById(ResourceTable.Id_action_img);
    }

    private void subscribe() {
        String event = "com.huawei.map";
        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(event);
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_SCREEN_ON);
        CommonEventSubscribeInfo subscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
        subscriber = new MyCommonEventSubscriber(subscribeInfo);
        try {
            CommonEventManager.subscribeCommonEvent(subscriber);
        } catch (RemoteException e) {
            LogUtils.error(TAG, "subscribeCommonEvent occur exception.");
        }
    }

    private void unSubscribe() {
        try {
            CommonEventManager.unsubscribeCommonEvent(subscriber);
        } catch (RemoteException e) {
            LogUtils.error(TAG, "unSubscribe Exception");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unSubscribe();
    }

    /**
     * MyCommonEventSubscriber
     * 接收从WatchService发送的数据
     *
     * @since 2020-12-03
     */
    private class MyCommonEventSubscriber extends CommonEventSubscriber {
        MyCommonEventSubscriber(CommonEventSubscribeInfo info) {
            super(info);
        }

        @Override
        public void onReceiveEvent(CommonEventData commonEventData) {
            Intent intent = commonEventData.getIntent();
            String actionType = intent.getStringParam("actionType");
            String actionContent = intent.getStringParam("actionContent");
            if (actionType != null) {
                if (actionType.equals(Const.STOP_WATCH_ABILITY)) {
                    terminateAbility();
                } else {
                    actionImg.setPixelMap(ImageUtils.getImageId(actionType));
                    contentComponent.setText(actionContent);
                }
            }
        }
    }
}
