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
import com.huawei.codelab.util.ImageUtils;
import com.huawei.codelab.util.LogUtils;
import com.huawei.codelab.util.PermissionsUtils;

import com.dongyu.tinymap.Element;
import com.dongyu.tinymap.TinyMap;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityContinuation;
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

    private List<InputTipsResult.TipsEntity> tips = new ArrayList<>(0);

    private InputTipsProvider inputTipsProvider;

    private Text routeContent;

    private MapDataHelper mapDataHelper;

    private MapManager mapManager;

    private Image routeImage;

    private int stepPoint = 0;

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
        if (findComponentById(ResourceTable.Id_map) instanceof TinyMap) {
            tinyMap = (TinyMap) findComponentById(ResourceTable.Id_map);
        }

        if (findComponentById(ResourceTable.Id_start_point_field) instanceof TextField) {
            startTextField = (TextField) findComponentById(ResourceTable.Id_start_point_field);
        }

        if (findComponentById(ResourceTable.Id_end_point_field) instanceof TextField) {
            endTextField = (TextField) findComponentById(ResourceTable.Id_end_point_field);
        }

        if (findComponentById(ResourceTable.Id_route_content) instanceof Text) {
            routeContent = (Text) findComponentById(ResourceTable.Id_route_content);
        }

        if (findComponentById(ResourceTable.Id_route_tips) instanceof DependentLayout) {
            routeTipsLayout = (DependentLayout) findComponentById(ResourceTable.Id_route_tips);
        }

        if (findComponentById(ResourceTable.Id_route_img) instanceof Image) {
            routeImage = (Image) findComponentById(ResourceTable.Id_route_img);
        }

        if (findComponentById(ResourceTable.Id_list_input_tips) instanceof ListContainer) {
            listContainer = (ListContainer) findComponentById(ResourceTable.Id_list_input_tips);
        }

        if (findComponentById(ResourceTable.Id_layout_input_tips) instanceof DependentLayout) {
            inputTipsLayout = (DependentLayout) findComponentById(ResourceTable.Id_layout_input_tips);
        }

        if (findComponentById(ResourceTable.Id_select_point) instanceof DirectionalLayout) {
            selectPointLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_select_point);
        }

        if (findComponentById(ResourceTable.Id_nav_bottom) instanceof DirectionalLayout) {
            navBottom = (DirectionalLayout) findComponentById(ResourceTable.Id_nav_bottom);
        }

        if (findComponentById(ResourceTable.Id_start_nav) instanceof Button) {
            btnStartNav = (Button) findComponentById(ResourceTable.Id_start_nav);
        }

        if (findComponentById(ResourceTable.Id_end_nav) instanceof Button) {
            btnEndNav = (Button) findComponentById(ResourceTable.Id_end_nav);
        }

        if (findComponentById(ResourceTable.Id_nav_translate) instanceof Button) {
            navTranslate = (Button) findComponentById(ResourceTable.Id_nav_translate);
        }
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
                mapDataHelper.getMyLocation();
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
            mapDataHelper.getMyLocation();
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

            mapManager.startNav();
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

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            // 迁移
            case ResourceTable.Id_nav_translate:
                mapManager.translate();
                setTranslateView();
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

    private void setTranslateView() {
        navTranslate.setVisibility(Component.HIDE);
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
    public void setRouteView(String route) {
        tinyMap.getMapElements().clear();
        navBottom.setVisibility(Component.VISIBLE);
        tinyMap.setStepPoint(0);
        mapDataHelper.parseRoute(route);
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
        mapManager.translateComplete();
        routeTipsLayout.setVisibility(Component.HIDE);
        LogUtils.info(TAG, "onCompleteContinuation");
    }
}
