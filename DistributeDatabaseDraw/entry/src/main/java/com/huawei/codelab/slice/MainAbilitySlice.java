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

package com.huawei.codelab.slice;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;
import static ohos.security.SystemPermission.DISTRIBUTED_DATASYNC;

import com.huawei.codelab.MainAbility;
import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.bean.MyPoint;
import com.huawei.codelab.component.DeviceSelectDialog;
import com.huawei.codelab.component.DrawPoint;
import com.huawei.codelab.util.GsonUtil;
import com.huawei.codelab.util.LogUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.bundle.IBundleManager;
import ohos.data.distributed.common.ChangeNotification;
import ohos.data.distributed.common.Entry;
import ohos.data.distributed.common.KvManager;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.data.distributed.common.KvStoreObserver;
import ohos.data.distributed.common.KvStoreType;
import ohos.data.distributed.common.Options;
import ohos.data.distributed.common.SubscribeType;
import ohos.data.distributed.common.SyncMode;
import ohos.data.distributed.user.SingleKvStore;

import java.util.List;

/**
 * MainAbilitySlice
 *
 * @since 2021-04-06
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final String TAG = MainAbilitySlice.class.getName();
    private static final int PERMISSION_CODE = 20201203;
    private static final int DELAY_TIME = 10;
    private static final String STORE_ID_KEY = "storeId";
    private static final String POINTS_KEY = "points";
    private static final String COLOR_INDEX_KEY = "colorIndex";
    private static final String IS_FORM_LOCAL_KEY = "isFormLocal";
    private static String storeId;
    private DependentLayout canvas;
    private Image transform;
    private KvManager kvManager;
    private SingleKvStore singleKvStore;
    private Text title;
    private DrawPoint drawl;
    private Button back;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        storeId = STORE_ID_KEY + System.currentTimeMillis();
        findComponentById();
        requestPermission();
        initView(intent);
        initDatabase();
        initDraw(intent);
    }

    private void initView(Intent intent) {
        boolean isLocal = !intent.getBooleanParam(IS_FORM_LOCAL_KEY, false);
        if (!isLocal) {
            storeId = intent.getStringParam(STORE_ID_KEY);
        }

        title.setText(isLocal ? "本地端" : "远程端");
        transform.setVisibility(isLocal ? Component.VISIBLE : Component.INVISIBLE);
    }

    private void requestPermission() {
        if (verifySelfPermission(DISTRIBUTED_DATASYNC) != IBundleManager.PERMISSION_GRANTED) {
            if (canRequestPermission(DISTRIBUTED_DATASYNC)) {
                requestPermissionsFromUser(new String[]{DISTRIBUTED_DATASYNC}, PERMISSION_CODE);
            }
        }
    }

    private void findComponentById() {
        if (findComponentById(ResourceTable.Id_canvas) instanceof DependentLayout) {
            canvas = (DependentLayout) findComponentById(ResourceTable.Id_canvas);
        }
        if (findComponentById(ResourceTable.Id_transform) instanceof Image) {
            transform = (Image) findComponentById(ResourceTable.Id_transform);
        }
        if (findComponentById(ResourceTable.Id_title) instanceof Text) {
            title = (Text) findComponentById(ResourceTable.Id_title);
        }
        if (findComponentById(ResourceTable.Id_back) instanceof Button) {
            back = (Button) findComponentById(ResourceTable.Id_back);
        }
        transform.setClickedListener(component -> {
            DeviceSelectDialog dialog = new DeviceSelectDialog(MainAbilitySlice.this);
            dialog.setListener(deviceIds -> {
                if (deviceIds != null && !deviceIds.isEmpty()) {
                    // 启动远程页面
                    startRemoteFas(deviceIds);
                    // 同步远程数据库
                    singleKvStore.sync(deviceIds, SyncMode.PUSH_ONLY);
                }
                dialog.hide();
            });
            dialog.show();
        });
    }

    /**
     * Initialize art boards
     *
     * @param intent Intent
     */
    private void initDraw(Intent intent) {
        int colorIndex = intent.getIntParam(COLOR_INDEX_KEY, 0);
        drawl = new DrawPoint(this, colorIndex);
        drawl.setWidth(MATCH_PARENT);
        drawl.setWidth(MATCH_PARENT);
        canvas.addComponent(drawl);

        drawPoints();

        drawl.setOnDrawBack(points -> {
            if (points != null && points.size() > 1) {
                String pointsString = GsonUtil.objectToString(points);
                LogUtils.info(TAG, "pointsString::" + pointsString);
                if (singleKvStore != null) {
                    singleKvStore.putString(POINTS_KEY, pointsString);
                }
            }
        });
        back.setClickedListener(component -> {
            List<MyPoint> points = drawl.getPoints();
            if (points == null || points.size() <= 1) {
                return;
            }
            points.remove(points.size() - 1);
            for (int i = points.size() - 1; i >= 0; i--) {
                if (points.get(i).isLastPoint()) {
                    break;
                }
                points.remove(i);
            }
            drawl.setDrawParams(points);
            String pointsString = GsonUtil.objectToString(points);
            if (singleKvStore != null) {
                singleKvStore.putString(POINTS_KEY, pointsString);
            }
        });
    }

    // 获取数据库中的点数据，并在画布上画出来
    private void drawPoints() {
        List<Entry> points = singleKvStore.getEntries(POINTS_KEY);
        for (Entry entry : points) {
            if (entry.getKey().equals(POINTS_KEY)) {
                List<MyPoint> remotePoints = GsonUtil.jsonToList(singleKvStore.getString(POINTS_KEY), MyPoint.class);
                getUITaskDispatcher().delayDispatch(() -> drawl.setDrawParams(remotePoints), DELAY_TIME);
            }
        }
    }

    /**
     * Receive database messages
     *
     * @since 2021-04-06
     */
    private class KvStoreObserverClient implements KvStoreObserver {
        @Override
        public void onChange(ChangeNotification notification) {
            LogUtils.info(TAG, "data changed......");
            drawPoints();
        }
    }

    private void initDatabase() {
        // 创建分布式数据库管理对象
        KvManagerConfig config = new KvManagerConfig(this);
        kvManager = KvManagerFactory.getInstance().createKvManager(config);
        // 创建分布式数据库
        Options options = new Options();
        options.setCreateIfMissing(true).setEncrypt(false).setKvStoreType(KvStoreType.SINGLE_VERSION);
        singleKvStore = kvManager.getKvStore(options, storeId);
        // 订阅分布式数据变化
        KvStoreObserver kvStoreObserverClient = new KvStoreObserverClient();
        singleKvStore.subscribe(SubscribeType.SUBSCRIBE_TYPE_ALL, kvStoreObserverClient);
    }

    /**
     * Starting Multiple Remote Fas
     *
     * @param deviceIds deviceIds
     */
    private void startRemoteFas(List<String> deviceIds) {
        Intent[] intents = new Intent[deviceIds.size()];
        for (int i = 0; i < deviceIds.size(); i++) {
            Intent intent = new Intent();
            intent.setParam(IS_FORM_LOCAL_KEY, true);
            intent.setParam(COLOR_INDEX_KEY, i + 1);
            intent.setParam(STORE_ID_KEY, storeId);
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId(deviceIds.get(i))
                    .withBundleName(getBundleName())
                    .withAbilityName(MainAbility.class.getName())
                    .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                    .build();
            intent.setOperation(operation);
            intents[i] = intent;
        }
        startAbilities(intents);
    }

    @Override
    protected void onStop() {
        super.onStop();
        kvManager.closeKvStore(singleKvStore);
    }
}