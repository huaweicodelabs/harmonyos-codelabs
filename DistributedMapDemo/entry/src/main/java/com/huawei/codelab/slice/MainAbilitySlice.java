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
import com.huawei.codelab.bean.InputTipsResult;
import com.huawei.codelab.map.MapDataHelper;
import com.huawei.codelab.map.MapManager;
import com.huawei.codelab.provider.InputTipsProvider;
import com.huawei.codelab.util.GsonUtils;
import com.huawei.codelab.util.LogUtils;
import com.huawei.codelab.util.MapUtils;
import com.huawei.codelab.util.PermissionsUtils;

import com.dongyu.tinymap.Element;
import com.dongyu.tinymap.TinyMap;
import com.dongyu.tinymap.util.ImageUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.ability.continuation.DeviceConnectState;
import ohos.aafwk.ability.continuation.ExtraParams;
import ohos.aafwk.ability.continuation.IContinuationDeviceCallback;
import ohos.aafwk.ability.continuation.IContinuationRegisterManager;
import ohos.aafwk.ability.continuation.RequestCallback;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.utils.Point;
import ohos.bundle.IBundleManager;
import ohos.security.SystemPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * MainAbilitySlice
 *
 * @since 2021-03-12
 */
public class MainAbilitySlice extends AbilitySlice
        implements Component.ClickedListener, IAbilityContinuation, MapDataHelper.DataCallBack {
    private static final String TAG = MainAbilitySlice.class.getSimpleName();

    private static final String ELEMENT_STRING = "elementsString";

    private static final int INPUT_START = 1;

    private static final int INPUT_END = 2;

    private final List<InputTipsResult.TipsEntity> tips = new ArrayList<>(0);

    private int inputType;

    private DependentLayout routeTipsLayout;

    private TinyMap tinyMap;

    private List<Element> elements;

    private TextField startTextField;

    private TextField endTextField;

    private ListContainer listContainer;

    private DependentLayout inputTipsLayout;

    private DirectionalLayout navBottom;

    private Button btnStartNav;

    private Button btnEndNav;

    private String endLocation;

    private Button navTranslate;

    private DirectionalLayout selectPointLayout;

    private boolean isSetInputText = false;

    private InputTipsProvider inputTipsProvider;

    private Text routeContent;

    private MapDataHelper mapDataHelper;

    private MapManager mapManager;

    private Image routeImage;

    private int stepPoint = 0;

    // 注册流转任务管理服务后返回的Ability token
    private int abilityToken;

    // 用户在设备列表中选择设备后返回的设备ID
    private String selectDeviceId;

    // 获取流转任务管理服务管理类
    private IContinuationRegisterManager continuationRegisterManager;

    // 设置流转任务管理服务设备状态变更的回调
    private final IContinuationDeviceCallback callback = new IContinuationDeviceCallback() {
        @Override
        public void onDeviceConnectDone(String deviceId, String deviceType) {
            // 在用户选择设备后设置设备ID
            selectDeviceId = deviceId;
            LogUtils.info(TAG, "onDeviceConnectDone:");
            translate();
        }

        @Override
        public void onDeviceDisconnectDone(String deviceId) {
            LogUtils.info(TAG, "onDeviceDisconnectDone:" + deviceId);
        }
    };

    // 设置注册流转任务管理服务回调
    private final RequestCallback requestCallback = new RequestCallback() {
        @Override
        public void onResult(int result) {
            abilityToken = result;
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        findComponentById();
        initView();
        initInputTipsProvider();
        setListener();
    }

    private void findComponentById() {
        tinyMap = (TinyMap) findComponentById(ResourceTable.Id_map);
        startTextField = (TextField) findComponentById(ResourceTable.Id_start_point_field);
        endTextField = (TextField) findComponentById(ResourceTable.Id_end_point_field);
        routeContent = (Text) findComponentById(ResourceTable.Id_route_content);
        routeTipsLayout = (DependentLayout) findComponentById(ResourceTable.Id_route_tips);
        routeImage = (Image) findComponentById(ResourceTable.Id_route_img);
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_list_input_tips);
        inputTipsLayout = (DependentLayout) findComponentById(ResourceTable.Id_layout_input_tips);
        selectPointLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_select_point);
        navBottom = (DirectionalLayout) findComponentById(ResourceTable.Id_nav_bottom);
        btnStartNav = (Button) findComponentById(ResourceTable.Id_start_nav);
        btnEndNav = (Button) findComponentById(ResourceTable.Id_end_nav);
        navTranslate = (Button) findComponentById(ResourceTable.Id_nav_translate);
    }

    private void setListener() {
        btnStartNav.setClickedListener(this);
        btnEndNav.setClickedListener(this);
        navTranslate.setClickedListener(this);

        endTextField.setClickedListener(component -> inputTipsLayout.setVisibility(Component.VISIBLE));
        startTextField.setClickedListener(component -> inputTipsLayout.setVisibility(Component.VISIBLE));

        mapDataHelper.setDataCallBack(this);

        mapManager.setNavListener(element -> {
            routeContent.setText(element.getActionContent());
            routeImage.setPixelMap(ImageUtils.getImageId(element.getActionType()));
        });

        PermissionsUtils.getInstance().setRequestListener(permission -> {
            if (permission.equals(SystemPermission.LOCATION)) {
                mapDataHelper.getMyLocation(startTextField);
                initContinuationRegisterManager();
            }
            if (permission.equals(SystemPermission.DISTRIBUTED_DATASYNC)) {
                initContinuationRegisterManager();
            }
        });

        startTextField.addTextObserver((text, start, before, count) -> {
            if (!text.trim().isEmpty() && !isSetInputText) {
                mapDataHelper.getInputTips(text);
            }
        });

        endTextField.addTextObserver((text, start, before, count) -> {
            if (!text.trim().isEmpty() && !isSetInputText) {
                mapDataHelper.getInputTips(text);
            }
        });

        startTextField.setFocusChangedListener((component, hasFocus) -> {
            if (hasFocus) {
                inputType = INPUT_START;
            }
        });

        endTextField.setFocusChangedListener((component, hasFocus) -> {
            if (hasFocus) {
                inputType = INPUT_END;
            }
        });
    }

    private void initView() {
        mapDataHelper = new MapDataHelper(tinyMap, this);
        mapManager = new MapManager(tinyMap, this);

        // 解决ListContainer和NavMap的Touch事件冲突
        listContainer.setTouchEventListener((component, touchEvent) -> true);

        if (verifySelfPermission(SystemPermission.LOCATION) == IBundleManager.PERMISSION_GRANTED) {
            mapDataHelper.getMyLocation(startTextField);
        }

        if (verifySelfPermission(SystemPermission.DISTRIBUTED_DATASYNC) == IBundleManager.PERMISSION_GRANTED) {
            initContinuationRegisterManager();
        }

        if (elements != null) {
            routeTipsLayout.setVisibility(Component.VISIBLE);
            navBottom.setVisibility(Component.VISIBLE);
            navTranslate.setVisibility(Component.VISIBLE);
            btnEndNav.setVisibility(Component.VISIBLE);
            btnStartNav.setVisibility(Component.HIDE);
            selectPointLayout.setVisibility(Component.HIDE);

            tinyMap.setStepPoint(stepPoint);
            mapManager.setStepPoint(stepPoint);
            Point mercatorPoint = elements.get(0).getMercatorPoint();
            tinyMap.setCenterPoint(new Point(mercatorPoint.getPointX(), mercatorPoint.getPointY()));

            // 将元素集合添加到navMap对象，并重新计算坐标
            for (Element element : elements) {
                tinyMap.addElement(element);
            }

            // 到达终点
            if (stepPoint < elements.size() - 1) {
                mapManager.startNav();
            }
        }
    }

    private void initInputTipsProvider() {
        if (inputTipsProvider == null) {
            inputTipsProvider = new InputTipsProvider(this, tips);
            listContainer.setItemProvider(inputTipsProvider);
            listContainer.setItemClickedListener((container, component, position, id) -> {
                inputTipsLayout.setVisibility(Component.HIDE);
                if (inputType == INPUT_START) {
                    isSetInputText = true;
                    startTextField.setText(tips.get(position).getName());
                    isSetInputText = false;
                    String location = tips.get(position).getLocation();
                    String[] coordinates = location.split(",");
                    mapDataHelper.setMapCenter(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
                    mapDataHelper.setLocation(location);
                } else {
                    endLocation = tips.get(position).getLocation();
                    isSetInputText = true;
                    endTextField.setText(tips.get(position).getName());
                    isSetInputText = false;
                }
                if (!startTextField.getText().trim().isEmpty() && !endTextField.getText().trim().isEmpty()) {
                    mapDataHelper.getRouteResult(endLocation);
                }
            });
        }
    }

    private void initContinuationRegisterManager() {
        continuationRegisterManager = getContinuationRegisterManager();
        // 增加过滤条件
        ExtraParams params = new ExtraParams();
        String[] devTypes = new String[]{ExtraParams.DEVICETYPE_SMART_PAD, ExtraParams.DEVICETYPE_SMART_PHONE,
                ExtraParams.DEVICETYPE_SMART_WATCH};
        params.setDevType(devTypes);
        // 注册流转任务管理服务
        continuationRegisterManager.register(getBundleName(), params, callback, requestCallback);
    }

    private void translate() {
        try {
            getUITaskDispatcher().asyncDispatch(() -> {
                if (continuationRegisterManager != null) {
                    continuationRegisterManager.updateConnectStatus(abilityToken, selectDeviceId,
                            DeviceConnectState.IDLE.getState(), null);
                    if (selectDeviceId != null) {
                        LogUtils.info(TAG, "translate:" + abilityToken);
                        // 用户点击后发起迁移流程
                        mapManager.translate(selectDeviceId);
                    }
                }
            });
        } catch (Exception exception) {
            LogUtils.info(TAG, "translate exception");
        }
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            // 迁移
            case ResourceTable.Id_nav_translate:
                if (tinyMap.getMapElements() != null && !tinyMap.getMapElements().isEmpty()) {
                    String elementStr = GsonUtils.objectToString(tinyMap.getMapElements());
                    // 目前跨端迁移的数据大小限制200KB以内，即onSaveData只能传递200KB以内的数据。
                    if (elementStr.length() >= 200 * 1024 / 2) {
                        MapUtils.showToast(MainAbilitySlice.this, "您输入的距离太远，请重新输入");
                        return;
                    }
                }

                // 设置过滤设备类型
                ExtraParams params = new ExtraParams();
                String[] devTypes = new String[]{ExtraParams.DEVICETYPE_SMART_PAD, ExtraParams.DEVICETYPE_SMART_PHONE,
                        ExtraParams.DEVICETYPE_SMART_WATCH};
                params.setDevType(devTypes);

                // 显示选择设备列表
                continuationRegisterManager.showDeviceList(abilityToken, params, null);
                break;

            // 开始导航
            case ResourceTable.Id_start_nav:
                mapManager.startNav();
                setStartNavView();
                break;

            // 结束导航
            case ResourceTable.Id_end_nav:
                mapManager.endNav();
                setEndNavView();
                break;
            default:
                break;
        }
    }

    private void setStartNavView() {
        btnStartNav.setVisibility(Component.HIDE);
        selectPointLayout.setVisibility(Component.HIDE);
        btnEndNav.setVisibility(Component.VISIBLE);
        navTranslate.setVisibility(Component.VISIBLE);
        routeTipsLayout.setVisibility(Component.VISIBLE);
    }

    private void setEndNavView() {
        selectPointLayout.setVisibility(Component.VISIBLE);
        routeTipsLayout.setVisibility(Component.HIDE);
        btnEndNav.setVisibility(Component.HIDE);
        btnStartNav.setVisibility(Component.VISIBLE);
        routeTipsLayout.setVisibility(Component.HIDE);
        navBottom.setVisibility(Component.HIDE);
    }

    @Override
    public void setInputTipsView(List<InputTipsResult.TipsEntity> inputTips) {
        tips.clear();
        tips.addAll(inputTips);
        MapManager.clearEmptyLocation(tips);
        inputTipsProvider.notifyDataChanged();
    }

    @Override
    public void setBottomView() {
        navBottom.setVisibility(Component.VISIBLE);
    }

    @Override
    public boolean onStartContinuation() {
        LogUtils.info(TAG, "onStartContinuation");
        return true;
    }

    @Override
    public void onRemoteTerminated() {
        LogUtils.info(TAG, "onRemoteTerminated");
    }

    @Override
    public boolean onSaveData(IntentParams saveData) {
        String elementsString = GsonUtils.objectToString(tinyMap.getMapElements());
        saveData.setParam(ELEMENT_STRING, elementsString);
        saveData.setParam("stepPoint", mapManager.getStepPoint());
        LogUtils.info(TAG, "onSaveData" + tinyMap.getMapElements().size());
        return true;
    }

    @Override
    public boolean onRestoreData(IntentParams restoreData) {
        if (restoreData.getParam(ELEMENT_STRING) instanceof String) {
            String elementsString = (String) restoreData.getParam(ELEMENT_STRING);
            elements = GsonUtils.jsonToList(elementsString, Element.class);
        }
        stepPoint = (int) restoreData.getParam("stepPoint");
        LogUtils.info(TAG, "onRestoreData::elements::" + elements.size());
        return true;
    }

    @Override
    public void onCompleteContinuation(int result) {
        if (result == 0) { // 迁移成功
            mapManager.translateComplete();
            terminateAbility();
        } else { //迁移失败
            MapUtils.showToast(this, "迁移失败，请稍后重试");
        }
        LogUtils.info(TAG, "onCompleteContinuation result:" + result);
    }
}
