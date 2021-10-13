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
import com.huawei.codelab.callback.MainCallBack;
import com.huawei.codelab.constants.EventConstants;
import com.huawei.codelab.data.ComponentPointData;
import com.huawei.codelab.data.ComponentPointDataMgr;
import com.huawei.codelab.proxy.ConnectManagerIml;
import com.huawei.codelab.utils.AbilityMgr;
import com.huawei.codelab.utils.LogUtils;
import com.huawei.codelab.utils.MovieSearchUtils;
import com.huawei.codelab.utils.PermissionBridge;
import com.huawei.codelab.utils.WindowManagerUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.continuation.DeviceConnectState;
import ohos.aafwk.ability.continuation.ExtraParams;
import ohos.aafwk.ability.continuation.IContinuationDeviceCallback;
import ohos.aafwk.ability.continuation.IContinuationRegisterManager;
import ohos.aafwk.ability.continuation.RequestCallback;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.ScrollView;
import ohos.agp.components.TableLayout;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.event.commonevent.CommonEventData;
import ohos.event.commonevent.CommonEventManager;
import ohos.event.commonevent.CommonEventSubscribeInfo;
import ohos.event.commonevent.CommonEventSubscriber;
import ohos.event.commonevent.CommonEventSupport;
import ohos.event.commonevent.MatchingSkills;
import ohos.media.image.common.Size;
import ohos.rpc.RemoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MainAbilitySlice
 *
 * @since 2021-02-26
 */
public class MainAbilitySlice extends AbilitySlice implements PermissionBridge.OnPermissionStateListener {
    private static final String ABILITY_NAME = "com.huawei.codelab.RemoteInputAbility";
    private static final String MOVIE_PLAY_ABILITY = "com.huawei.codelab.MoviePlayAbility";
    private static final String MAIN_ABILITY = "com.huawei.codelab.MainAbility";
    private static final int FOCUS_PADDING = 8;
    private static final int SEARCH_PADDING = 3;
    private static final int LIST_INIT_SIZE = 16;
    private static final String TAG = MainAbilitySlice.class.getName();
    private static final Lock MOVE_LOCK = new ReentrantLock();
    private MainAbilitySlice.MyCommonEventSubscriber subscriber;
    private TextField tvTextInput;
    private ScrollView scrollView;
    private DirectionalLayout keyBoardLayout;
    private TableLayout movieTableLayout;
    private Size size;
    private final AbilityMgr abilityMgr = new AbilityMgr(this);

    // 搜索的到的影片
    private final List<ComponentPointData> movieSearchList = new ArrayList<>(LIST_INIT_SIZE);

    // 当前焦点所在位置
    private ComponentPointData componentPointDataNow;

    // 注册流转任务管理服务后返回的Ability token
    private int abilityToken;

    // 用户在设备列表中选择设备后返回的设备ID
    private String selectDeviceId;

    // 获取流转任务管理服务管理类
    private IContinuationRegisterManager continuationRegisterManager;

    // 设置监听FA流转管理服务设备状态变更的回调
    private final IContinuationDeviceCallback callback = new IContinuationDeviceCallback() {
        @Override
        public void onDeviceConnectDone(String deviceId, String s1) {
            selectDeviceId = deviceId;
            abilityMgr.openRemoteAbility(selectDeviceId, getBundleName(), ABILITY_NAME);
            getUITaskDispatcher().asyncDispatch(() -> continuationRegisterManager.updateConnectStatus(abilityToken, selectDeviceId,
                    DeviceConnectState.IDLE.getState(), null));
        }

        @Override
        public void onDeviceDisconnectDone(String deviceId) {
        }
    };

    // 设置注册FA流转管理服务服务回调
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

        new PermissionBridge().setOnPermissionStateListener(this);

        // 全屏设置
        WindowManagerUtils.setWindows();

        // 初始化布局
        initComponent();

        // 初始化按钮组件
        initKeyBoardComponent(keyBoardLayout);

        // 初始化影片图片组件
        initMovieTableComponent(movieTableLayout);

        // 事件订阅
        subscribe();
    }

    private void registerTransService() {
        continuationRegisterManager = getContinuationRegisterManager();

        // 增加过滤条件
        ExtraParams params = new ExtraParams();
        String[] devTypes = new String[]{ExtraParams.DEVICETYPE_SMART_PAD, ExtraParams.DEVICETYPE_SMART_PHONE};
        params.setDevType(devTypes);

        // 注册FA流转管理服务
        continuationRegisterManager.register(getBundleName(), params, callback, requestCallback);
    }

    private void initMovieTableComponent(TableLayout tableLayout) {
        int index = 0;
        while (index < tableLayout.getChildCount()) {
            DirectionalLayout childLayout = null;
            if (tableLayout.getComponentAt(index) instanceof DirectionalLayout) {
                childLayout = (DirectionalLayout) tableLayout.getComponentAt(index);
            }
            ComponentPointData componentPointData = new ComponentPointData();
            Component component = null;
            int indexMovie = 0;
            while (childLayout != null && indexMovie < childLayout.getChildCount()) {
                Component comChild = childLayout.getComponentAt(indexMovie);
                indexMovie++;
                if (comChild instanceof Text) {
                    componentPointData = ComponentPointDataMgr
                            .getConstantMovie(((Text) comChild).getText()).orElse(null);
                    continue;
                }
                component = findComponentById(comChild.getId());
            }

            if (componentPointData != null && component != null) {
                componentPointData.setComponentId(component.getId());
            }
            ComponentPointDataMgr.getComponentPointDataMgrs().add(componentPointData);
            index++;
        }
    }

    private void initKeyBoardComponent(DirectionalLayout directionalLayout) {
        int index = 0;
        while (index < directionalLayout.getChildCount()) {
            DirectionalLayout childLayout = null;
            if (directionalLayout.getComponentAt(index) instanceof DirectionalLayout) {
                childLayout = (DirectionalLayout) directionalLayout.getComponentAt(index);
            }
            int indexButton = 0;
            while (childLayout != null && indexButton < childLayout.getChildCount()) {
                if (childLayout.getComponentAt(indexButton) instanceof Button) {
                    Button button = (Button) childLayout.getComponentAt(indexButton);
                    buttonInit(button);
                    indexButton++;
                }
            }
            index++;
        }
    }

    private void initComponent() {
        if (findComponentById(ResourceTable.Id_scrollview) instanceof ScrollView) {
            scrollView = (ScrollView) findComponentById(ResourceTable.Id_scrollview);
        }

        if (findComponentById(ResourceTable.Id_TV_input) instanceof TextField) {
            tvTextInput = (TextField) findComponentById(ResourceTable.Id_TV_input);

            // 初始化默认选中效果
            ComponentPointData componentPointData = new ComponentPointData();
            componentPointData.setComponentId(tvTextInput.getId());
            componentPointData.setPointX(0);
            componentPointData.setPointY(1);
            tvTextInput.requestFocus();
            componentPointDataNow = componentPointData;
            ComponentPointDataMgr.getComponentPointDataMgrs().add(componentPointDataNow);

            // 点击事件触发设备选取弹框
            tvTextInput.setClickedListener(component -> showDevicesDialog());
        }

        if (findComponentById(ResourceTable.Id_keyBoardComponent) instanceof DirectionalLayout) {
            keyBoardLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_keyBoardComponent);
        }

        if (findComponentById(ResourceTable.Id_tableLayout) instanceof TableLayout) {
            movieTableLayout = (TableLayout) findComponentById(ResourceTable.Id_tableLayout);
        }

        if (findComponentById(ResourceTable.Id_image_ten) instanceof Image) {
            Image image = (Image) findComponentById(ResourceTable.Id_image_ten);
            size = image.getPixelMap().getImageInfo().size;
        }
    }

    private void showDevicesDialog() {
        ExtraParams extraParams = new ExtraParams();
        extraParams.setDevType(new String[]{ExtraParams.DEVICETYPE_SMART_TV,
            ExtraParams.DEVICETYPE_SMART_PHONE});
        extraParams.setDescription("远程遥控器");
        continuationRegisterManager.showDeviceList(abilityToken, extraParams, result -> LogUtils.info(TAG, "show devices success"));
    }

    private void buttonInit(Button button) {
        if (button.getId() == ResourceTable.Id_del) {
            findComponentById(button.getId()).setClickedListener(component -> {
                if (tvTextInput.getText().length() > 0) {
                    tvTextInput.setText(tvTextInput.getText().substring(0, tvTextInput.getText().length() - 1));
                }
            });
        } else if (button.getId() == ResourceTable.Id_clear) {
            findComponentById(button.getId()).setClickedListener(component -> tvTextInput.setText(""));
        } else {
            findComponentById(button.getId()).setClickedListener(component -> tvTextInput.append(button.getText()));
        }
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
    public void onPermissionGranted() {
        registerTransService();
    }

    @Override
    public void onPermissionDenied() {
        terminate();
    }

    /**
     * MyCommonEventSubscriber
     *
     * @since 2020-12-03
     */
    class MyCommonEventSubscriber extends CommonEventSubscriber {
        MyCommonEventSubscriber(CommonEventSubscribeInfo info) {
            super(info);
        }

        @Override
        public void onReceiveEvent(CommonEventData commonEventData) {
            Intent intent = commonEventData.getIntent();
            int requestType = intent.getIntParam("requestType", 0);
            String inputString = intent.getStringParam("inputString");
            if (requestType == ConnectManagerIml.REQUEST_SEND_DATA) {
                tvTextInput.setText(inputString);
            } else if (requestType == ConnectManagerIml.REQUEST_SEND_SEARCH) {
                // 如果当前选中的是文本框则搜索影片;如果当前选中的是影片则播放影片;X轴坐标为0当前选中文本框;X轴大于1当前选中影片
                if (componentPointDataNow.getPointX() == 0) {
                    // 调用大屏的搜索方法
                    searchMovies(tvTextInput.getText());
                    return;
                }

                // 播放影片
                abilityMgr.playMovie(getBundleName(), MOVIE_PLAY_ABILITY);
            } else {
                // 移动方向
                String moveString = intent.getStringParam("move");
                MainCallBack.movePoint(MainAbilitySlice.this, moveString);
            }
        }
    }

    /**
     * goBack
     */
    public void goBack() {
        clearLastBackg();
        scrollView.scrollTo(0, 0);
        componentPointDataNow = ComponentPointDataMgr.getMoviePoint(0, 1).orElse(null);
        findComponentById(ResourceTable.Id_TV_input).requestFocus();
        abilityMgr.returnMainAbility(getBundleName(), MAIN_ABILITY);
    }

    /**
     * move
     *
     * @param pointX pointX
     * @param pointY pointY
     */
    public void move(int pointX, int pointY) {
        MOVE_LOCK.lock();
        try {
            // 设置焦点滚动
            if (pointX == 0 && componentPointDataNow.getPointX() > 0) {
                scrollView.fluentScrollByY(pointY * size.height);
            }
            if (componentPointDataNow.getPointX() == 0 && pointX == 1) {
                scrollView.scrollTo(0, 0);
            }

            // 设置背景
            if (componentPointDataNow.getPointX() + pointX == 0) {
                setBackGround(componentPointDataNow.getPointX() + pointX, 1);
            } else {
                setBackGround(componentPointDataNow.getPointX() + pointX, componentPointDataNow.getPointY() + pointY);
            }
        } finally {
            MOVE_LOCK.unlock();
        }
    }

    private void setBackGround(int pointX, int pointY) {
        ComponentPointData componentPointDataNew = ComponentPointDataMgr.getMoviePoint(pointX, pointY).orElse(null);
        if (componentPointDataNew == null) {
            return;
        }

        // 清除上次选中的效果
        clearLastBackg();
        componentPointDataNow = componentPointDataNew;
        if (findComponentById(componentPointDataNow.getComponentId()) instanceof Image) {
            Image newImage = (Image) findComponentById(componentPointDataNow.getComponentId());
            newImage.setPadding(FOCUS_PADDING, FOCUS_PADDING, FOCUS_PADDING, FOCUS_PADDING);
        } else {
            Component component = findComponentById(componentPointDataNow.getComponentId());
            component.requestFocus();
        }
    }

    private void clearLastBackg() {
        Component component = null;
        if (findComponentById(componentPointDataNow.getComponentId()) instanceof TextField) {
            TextField textField = (TextField) findComponentById(componentPointDataNow.getComponentId());
            textField.clearFocus();
        } else {
            component = findComponentById(componentPointDataNow.getComponentId());
            component.setPadding(0, 0, 0, 0);
        }

        // 如果是搜索出来的还是保持搜索到的背景
        for (ComponentPointData componentPointData : movieSearchList) {
            if (component != null && componentPointData.getComponentId() == component.getId()) {
                component.setPadding(SEARCH_PADDING, SEARCH_PADDING, SEARCH_PADDING, SEARCH_PADDING);
            }
        }
    }

    private void searchMovies(String text) {
        if (text == null || "".equals(text)) {
            return;
        }

        // 清空上次搜索结果及背景效果
        clearHistroyBackGround();

        for (ComponentPointData componentPointData : ComponentPointDataMgr.getComponentPointDataMgrs()) {
            if (MovieSearchUtils.isContainMovie(componentPointData.getMovieName(), text)
                    || MovieSearchUtils.isContainMovie(componentPointData.getMovieFirstName(), text)) {
                movieSearchList.add(componentPointData);
                Component component = findComponentById(componentPointData.getComponentId());
                component.setPadding(SEARCH_PADDING, SEARCH_PADDING, SEARCH_PADDING, SEARCH_PADDING);
            }
        }

        if (movieSearchList.size() > 0) {
            componentPointDataNow = movieSearchList.get(0);
            Component component = findComponentById(componentPointDataNow.getComponentId());
            component.setPadding(FOCUS_PADDING, FOCUS_PADDING, FOCUS_PADDING, FOCUS_PADDING);
        } else {
            Component component = findComponentById(componentPointDataNow.getComponentId());
            component.requestFocus();
        }
    }

    private void clearHistroyBackGround() {
        // 清空上次搜索的结果
        for (ComponentPointData componentPointData : movieSearchList) {
            Component component = findComponentById(componentPointData.getComponentId());
            component.setPadding(0, 0, 0, 0);
        }
        movieSearchList.clear();

        // 去掉当前焦点背景
        clearLastBackg();
    }

    private void subscribe() {
        MatchingSkills matchingSkills = new MatchingSkills();
        matchingSkills.addEvent(EventConstants.SCREEN_REMOTE_CONTROLL_EVENT);
        matchingSkills.addEvent(CommonEventSupport.COMMON_EVENT_SCREEN_ON);
        CommonEventSubscribeInfo subscribeInfo = new CommonEventSubscribeInfo(matchingSkills);
        subscriber = new MainAbilitySlice.MyCommonEventSubscriber(subscribeInfo);
        try {
            CommonEventManager.subscribeCommonEvent(subscriber);
        } catch (RemoteException e) {
            LogUtils.error("", "subscribeCommonEvent occur exception.");
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
}
