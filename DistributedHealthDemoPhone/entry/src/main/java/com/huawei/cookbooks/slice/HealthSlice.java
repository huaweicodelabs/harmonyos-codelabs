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

package com.huawei.cookbooks.slice;

import com.huawei.cookbooks.Entity.WatchEntity;
import com.huawei.cookbooks.ResourceTable;
import com.huawei.cookbooks.annotation.Bind;
import com.huawei.cookbooks.enums.KeyEnum;
import com.huawei.cookbooks.factory.MyKvManagerFactory;
import com.huawei.cookbooks.task.PushDataTask;
import com.huawei.cookbooks.util.CommonUtils;
import com.huawei.cookbooks.util.LogUtils;
import com.huawei.cookbooks.view.HomeLayoutManager;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.Button;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ComponentState;
import ohos.agp.components.RoundProgressBar;
import ohos.agp.components.Switch;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.data.distributed.common.Entry;
import ohos.data.distributed.common.KvManager;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvStoreException;
import ohos.data.distributed.common.KvStoreType;
import ohos.data.distributed.common.Options;
import ohos.data.distributed.common.SyncMode;
import ohos.data.distributed.device.DeviceFilterStrategy;
import ohos.data.distributed.device.DeviceInfo;
import ohos.data.distributed.user.SingleKvStore;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * health slice
 *
 * @since 2021-06-18
 */
public class HealthSlice extends AbilitySlice {
    private static final int EVENT_MESSAGE_NORMAL = 1;

    private static final String STORE_ID = "watch_demo_mine";

    private static final String STORE_ID2 = "copyToPhone_mine";

    private static final String TAG = "HealthSlice";

    private static final int SHOW_TIME = 1500;

    private static final int DIALOG_SIZE_WIDTH = 1000;

    private static final int DIALOG_SIZE_HEIGHT = 100;

    private static final int MAX_HEART_RATE = 120;

    private static final int MIN_HEART_RATE = 60;

    private static final int ERROR_CODE = -1;

    private static final long INTERVAL = 5000L;

    private static final int CORNER_RADIUS = 50;

    private static final int ON_COLOR_SLIDER = 0xFF1E90FF;

    private static final int OFF_COLOR_SLIDER = 0xFFFFFFFF;

    private static final int ON_COLOR = 0xFF87CEFA;

    private static final int OFF_COLOR = 0xFF808080;

    private static final String DOT = ".";

    private KvManager kvManager;

    private SingleKvStore singleKvStore;

    private SingleKvStore singleKvStore2;

    /**
     * Health Data Slice
     */
    @Bind(ResourceTable.Id_stepData)
    private RoundProgressBar stepData;

    @Bind(ResourceTable.Id_stepTimeText)
    private Text stepTime;

    @Bind(ResourceTable.Id_rateData)
    private Text rateData;

    @Bind(ResourceTable.Id_rateTimeText)
    private Text rateTime;

    @Bind(ResourceTable.Id_rateRange)
    private Text rateRange;

    @Bind(ResourceTable.Id_stepDistances)
    private Text stepDistances; // 距离

    @Bind(ResourceTable.Id_stepHeat)
    private Text stepHeat; // 热量

    @Bind(ResourceTable.Id_stepFloor)
    private Text stepFloor; // 爬楼层

    @Bind(ResourceTable.Id_stepData1)
    private RoundProgressBar stepData1;

    /**
     * Others Data Slice Component
     */
    @Bind(ResourceTable.Id_otherStepData)
    private RoundProgressBar otherStepData;

    @Bind(ResourceTable.Id_otherStepTimeText)
    private Text otherStepTime;

    @Bind(ResourceTable.Id_otherRateData)
    private Text otherRateData;

    @Bind(ResourceTable.Id_otherRateTimeText)
    private Text otherRateTime;

    @Bind(ResourceTable.Id_otherRateRangeData)
    private Text otherRateRangeData;

    @Bind(ResourceTable.Id_otherStepDistances)
    private Text otherStepDistances; // 距离

    @Bind(ResourceTable.Id_otherStepHeat)
    private Text otherStepHeat; // 热量

    @Bind(ResourceTable.Id_otherStepFloor)
    private Text otherStepFloor; // 爬楼层

    @Bind(ResourceTable.Id_otherStepData1)
    private RoundProgressBar otherStepData1;

    /**
     * Synchronize Data Button
     */
    @Bind(ResourceTable.Id_button_syncData)
    private Button syncData;

    @Bind(ResourceTable.Id_button_syncPhoneData)
    private Button syncPhoneData;

    @Bind(ResourceTable.Id_btn_switch)
    private Switch sw;

    private final List<String> deviceIdList = new ArrayList<>(0);

    private final List<Integer> rateList = new ArrayList<>(0);

    private static final String regex = "-->";

    private MyEventHandler myHandler;

    private Timer timer;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        HomeLayoutManager manager = new HomeLayoutManager(this);
        ComponentContainer rootLayout = manager.initSliceLayout();
        super.setUIContent(rootLayout);
        initViewAnnotation();
        initListener();
        initSwitch();
        initDb();
    }

    private void initMyHandler() {
        myHandler = new MyEventHandler(EventRunner.current());
        long param = 0L;
        Object object = null;
        InnerEvent normalInnerEvent = InnerEvent.get(EVENT_MESSAGE_NORMAL, param, object);
        myHandler.sendEvent(normalInnerEvent, 0, EventHandler.Priority.IMMEDIATE);
    }

    private void initViewAnnotation() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            Bind bind = field.getAnnotation(Bind.class);
            if (bind != null) {
                if (bind.value() == ERROR_CODE) {
                    LogUtils.error(TAG, "bind.value is must set!");
                    return;
                }
                try {
                    field.setAccessible(true);
                    field.set(this, findComponentById(bind.value()));
                } catch (IllegalAccessException e) {
                    LogUtils.error(TAG, "IllegalAccessException :" + e.getMessage());
                }
            }
        }
    }

    private void initListener() {
        syncData.setClickedListener(
            va -> syncData(singleKvStore));
        syncPhoneData.setClickedListener(
            va -> syncData(singleKvStore2));
        // 回调处理Switch状态改变事件
        sw.setCheckedStateChangedListener((button, isChecked) -> {
            if (isChecked) {
                timer = new Timer();
                initMyHandler();
            } else {
                timer.cancel();
            }
        });
    }

    private void initDb() {
        KvManagerConfig config = new KvManagerConfig(this);
        kvManager = MyKvManagerFactory.getInstance(config);
        Options options = new Options();
        options.setCreateIfMissing(true).setEncrypt(false).setKvStoreType(KvStoreType.SINGLE_VERSION);
        try {
            singleKvStore = kvManager.getKvStore(options, STORE_ID);
            singleKvStore2 = kvManager.getKvStore(options, STORE_ID2);
        } catch (KvStoreException e) {
            LogUtils.info(TAG, "KvStore Exception Occur");
        }
    }

    private void syncData(SingleKvStore kvStore) {
        List<DeviceInfo> deviceInfoList = kvManager.getConnectedDevicesInfo(DeviceFilterStrategy.NO_FILTER);
        if (deviceInfoList.size() == 0) {
            String message = "未发现手表/手机设备";
            showTip(message);
            renderingData(kvStore);
            return;
        }
        deviceIdList.clear();
        for (DeviceInfo deviceInfo : deviceInfoList) {
            deviceIdList.add(deviceInfo.getId());
        }
        inferredSyncDevice(kvStore);
    }

    private void inferredSyncDevice(SingleKvStore kvStore) {
        if (kvStore.equals(singleKvStore)) {
            String message = "同步手表数据成功";
            doSync(message, kvStore);
            renderingData(kvStore);
        } else if (kvStore.equals(singleKvStore2)) {
            String message = "同步手机数据成功";
            doSync(message, kvStore);
            renderingData(kvStore);
        } else {
            LogUtils.info("syncData()==>", "DATA BASE NOT EXIST");
        }
    }

    private void doSync(String message, SingleKvStore kvStore) {
        kvStore.registerSyncCallback(
                map -> {
                    getUITaskDispatcher()
                            .asyncDispatch(
                                    () -> showTip(message));
                    kvStore.unRegisterSyncCallback();
                });
        try {
            LogUtils.info(TAG, "Start to get data");
            kvStore.sync(deviceIdList, SyncMode.PULL_ONLY);
        } catch (KvStoreException e) {
            LogUtils.info(TAG, "doSync KvStoreException");
        }
    }

    private void renderingData(SingleKvStore kvStore) {
        if (kvStore != null) {
            List<Entry> entries = kvStore.getEntries("");
            if (entries.size() != 0) {
                dealEntries(entries, kvStore);
            } else {
                String message = "未找到数据";
                showTip(message);
            }
        }
    }

    private void dealEntries(List<Entry> entries, SingleKvStore kvStore) {
        for (Entry entry : entries) {
            finishPageInitialization(entry.getKey(), kvStore, createWatchDataEntity(entry));
        }
        populateRateRangePage(entries, kvStore);
    }

    private void finishPageInitialization(String key, SingleKvStore kvStore, WatchEntity watchEntity) {
        if (KeyEnum.LATEST_STEP.getValue().equals(key)) {
            populateStepPage(kvStore, watchEntity);
        } else if (KeyEnum.LATEST_RATE.getValue().equals(key)) {
            if (kvStore.equals(singleKvStore)) {
                populateRatePage(rateData, rateTime, watchEntity);
            } else {
                populateRatePage(otherRateData, otherRateTime, watchEntity);
            }
        } else if (key.contains(KeyEnum.RATE.getValue())) {
            rateList.add(Integer.valueOf(watchEntity.getData()));
        }
    }

    private void populateRateRangePage(List<Entry> entries, SingleKvStore kvStore) {
        if (rateList.size() == 0) {
            return;
        }
        doPopulateRateRangePage(entries, kvStore);
    }

    private void doPopulateRateRangePage(List<Entry> entries, SingleKvStore kvStore) {
        Integer min = Collections.min(rateList);
        Integer max = Collections.max(rateList);
        if (kvStore.equals(singleKvStore)) {
            rateRange.setText(min + "~" + max);
            List<Entry> entries2s = singleKvStore2.getEntries("");
            getUITaskDispatcher().asyncDispatch(new PushDataTask(entries, entries2s, singleKvStore2));
        } else {
            otherRateRangeData.setText(min + "~" + max);
            if (min < MIN_HEART_RATE || max > MAX_HEART_RATE) {
                String message = "他人心率异常";
                showTip(message);
            }
        }
    }

    private WatchEntity createWatchDataEntity(Entry entry) {
        String value = entry.getValue().getString();
        String[] splits = value.split(regex);
        WatchEntity watchEntity = new WatchEntity();
        watchEntity.setTime(splits[0]);
        watchEntity.setData(splits[1].substring(0, splits[1].indexOf(DOT)));
        return watchEntity;
    }

    private void populateStepPage(SingleKvStore kvStore, WatchEntity watchDataEntity) {
        if (kvStore.equals(singleKvStore)) { // 我的数据
            stepData.setProgressHintText("步数 " + watchDataEntity.getData() + " 步");
            stepData.setProgressValue(CommonUtils.getProgressWithSteps(Integer.parseInt(watchDataEntity.getData())));
            stepTime.setText(watchDataEntity.getTime());
            // 距离
            stepDistances.setText("距离" + CommonUtils.getDistanceWithSteps(
                    Integer.parseInt(watchDataEntity.getData())) + "公里 |");
            // 热量
            stepHeat.setText("热量" + CommonUtils.getCalorieWithSteps(
                    Integer.parseInt(watchDataEntity.getData())) + "千卡 |");
            // 爬楼层
            stepFloor.setText("爬楼" + CommonUtils.getFloorWithSteps(
                    Integer.parseInt(watchDataEntity.getData())) + "层");
            // 强度
            Map<String, String> intensityMap = CommonUtils.getIntensityWithSteps(
                    Integer.parseInt(watchDataEntity.getData()));
            stepData1.setProgressHintText(intensityMap.get("text"));
            stepData1.setProgressValue(Integer.parseInt(intensityMap.get("progress")));
        } else { // 他人数据
            otherStepData.setProgressHintText("步数 " + watchDataEntity.getData() + " 步");
            otherStepData.setProgressValue(CommonUtils.getProgressWithSteps(
                    Integer.parseInt(watchDataEntity.getData())));
            otherStepTime.setText(watchDataEntity.getTime());
            // 距离
            otherStepDistances.setText("距离" + CommonUtils.getDistanceWithSteps(
                    Integer.parseInt(watchDataEntity.getData())) + "公里 |");
            // 热量
            otherStepHeat.setText("热量" + CommonUtils.getCalorieWithSteps(
                    Integer.parseInt(watchDataEntity.getData())) + "千卡 |");
            // 爬楼层
            otherStepFloor.setText("爬楼" + CommonUtils.getFloorWithSteps(
                    Integer.parseInt(watchDataEntity.getData())) + "层");
            // 强度
            Map<String, String> intensityMap = CommonUtils.getIntensityWithSteps(
                    Integer.parseInt(watchDataEntity.getData()));
            otherStepData1.setProgressHintText(intensityMap.get("text"));
            otherStepData1.setProgressValue(Integer.parseInt(intensityMap.get("progress")));
        }
    }

    private void populateRatePage(Text rateDataText, Text rateTimeText, WatchEntity watchDataEntity) {
        rateDataText.setText(watchDataEntity.getData());
        rateTimeText.setText(watchDataEntity.getTime());
    }

    private void showTip(String message) {
        myHandler.postTask(() -> new ToastDialog(getContext())
                .setAlignment(LayoutAlignment.TOP)
                .setText(message)
                .setDuration(SHOW_TIME)
                .setSize(DIALOG_SIZE_WIDTH, DIALOG_SIZE_HEIGHT)
                .show());
    }

    /**
     * my event handler
     *
     * @since 2021-06-18
     */
    class MyEventHandler extends EventHandler {
        MyEventHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            if (event == null) {
                return;
            }
            int eventId = event.eventId;
            if (eventId == EVENT_MESSAGE_NORMAL) {
                timer.schedule(new TimerTask() {
                    public void run() {
                        getUITaskDispatcher().syncDispatch(() -> syncData(singleKvStore));
                    }
                }, 0, INTERVAL);
            }
        }
    }

    private StateElement trackElementInit(ShapeElement on, ShapeElement off) {
        StateElement trackElement = new StateElement();
        trackElement.addState(new int[]{ComponentState.COMPONENT_STATE_CHECKED}, on);
        trackElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, off);
        return trackElement;
    }

    private StateElement thumbElementInit(ShapeElement on, ShapeElement off) {
        StateElement thumbElement = new StateElement();
        thumbElement.addState(new int[]{ComponentState.COMPONENT_STATE_CHECKED}, on);
        thumbElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, off);
        return thumbElement;
    }

    private void initSwitch() {
        // 开启状态下滑块的样式
        ShapeElement elementThumbOn = new ShapeElement();
        elementThumbOn.setShape(ShapeElement.OVAL);
        elementThumbOn.setRgbColor(RgbColor.fromArgbInt(ON_COLOR_SLIDER));
        elementThumbOn.setCornerRadius(CORNER_RADIUS);

        // 关闭状态下滑块的样式
        ShapeElement elementThumbOff = new ShapeElement();
        elementThumbOff.setShape(ShapeElement.OVAL);
        elementThumbOff.setRgbColor(RgbColor.fromArgbInt(OFF_COLOR_SLIDER));
        elementThumbOff.setCornerRadius(CORNER_RADIUS);

        // 开启状态下轨迹样式
        ShapeElement elementTrackOn = new ShapeElement();
        elementTrackOn.setShape(ShapeElement.RECTANGLE);
        elementTrackOn.setRgbColor(RgbColor.fromArgbInt(ON_COLOR));
        elementTrackOn.setCornerRadius(CORNER_RADIUS);

        // 关闭状态下轨迹样式
        ShapeElement elementTrackOff = new ShapeElement();
        elementTrackOff.setShape(ShapeElement.RECTANGLE);
        elementTrackOff.setRgbColor(RgbColor.fromArgbInt(OFF_COLOR));
        elementTrackOff.setCornerRadius(CORNER_RADIUS);

        sw.setTrackElement(trackElementInit(elementTrackOn, elementTrackOff));
        sw.setThumbElement(thumbElementInit(elementThumbOn, elementThumbOff));
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        kvManager.closeKvStore(singleKvStore);
        kvManager.deleteKvStore(STORE_ID);
        kvManager.closeKvStore(singleKvStore2);
        kvManager.deleteKvStore(STORE_ID2);
    }
}
