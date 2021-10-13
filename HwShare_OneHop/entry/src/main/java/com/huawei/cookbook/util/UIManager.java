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

import ohos.agp.utils.Point;
import ohos.agp.window.service.DisplayManager;
import ohos.app.AbilityContext;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * UI Manager
 *
 * @ClassName: UIManager
 * @Description: java description
 */
public class UIManager {
    private static UIManager iInstance = null;
    private EventHandler mHandler = null;

    private UIManager() { }

    /**
     * get
     *
     * @return UIManager
     */
    public static UIManager get() {
        if (UIManager.iInstance == null) {
            UIManager.iInstance = new UIManager();
            UIManager.iInstance.init();
        }
        return UIManager.iInstance;
    }

    private void init() {
        EventRunner runner = EventRunner.getMainEventRunner();
        mHandler =
            new EventHandler(runner) {
            @Override
            protected void processEvent(InnerEvent event) {
                super.processEvent(event);
                handleEvent(event);
            }
        };
    }

    private void handleEvent(InnerEvent event) {
        if (event == null) {
            return;
        }
        if (event.object instanceof Runnable) {
            Runnable runnable = (Runnable) event.object;
            runnable.run();
        }
    }

    /**
     * post event delay
     *
     * @param object object
     * @param delayTime delayTime
     */
    public void postEventDelay(Object object, long delayTime) {
        int eventId1 = 0;
        long param = 0L;
        InnerEvent event1 = InnerEvent.get(eventId1, param, object);
        mHandler.sendEvent(event1, delayTime);
    }

    /**
     * post task delay
     *
     * @param runnable r
     * @param delayTime delayTime
     */
    public void postTaskDelay(Runnable runnable, long delayTime) {
        mHandler.postTask(runnable, delayTime);
    }

    /**
     * test
     *
     * @param runnable r
     */
    public void test(Runnable runnable) {
        new Timer()
                .schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                postTaskDelay(runnable, 0);
                            }
                        },
                        1000,
                        1000);
    }

    /**
     * post safe
     *
     * @param runnable runnable
     * @param context context
     */
    public void postSafe(Runnable runnable, AbilityContext context) {
        context.getUITaskDispatcher().asyncDispatch(runnable);
    }

    /**
     * init device size
     *
     * @param context context
     */
    public void initDeviceSize(Context context) {
        DisplayManager dm = DisplayManager.getInstance();
        Point point = new Point();
        dm.getDefaultDisplay(context).get().getSize(point);
    }
}
