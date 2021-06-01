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
import com.huawei.codelab.constants.Constants;
import com.huawei.codelab.proxy.ConnectManager;
import com.huawei.codelab.proxy.ConnectManagerIml;
import com.huawei.codelab.utils.WindowManagerUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.media.audio.SoundPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * RemoteInputAbilitySlice
 *
 * @since 2021-02-26
 */
public class RemoteInputAbilitySlice extends AbilitySlice {
    private static final int SHOW_KEYBOARD_DELAY = 800;
    private static final int INIT_SIZE = 8;

    private ConnectManager connectManager;

    private TextField textField;

    private String deviceIdConn;

    private Component okButton;

    private Component leftButton;

    private Component rightButton;

    private Component upButton;

    private Component downButton;

    private Component goBackButton;

    private Component closeButton;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_remote_input);

        // 获取大屏ID用于配对
        deviceIdConn = intent.getStringParam("localDeviceId");

        // 全屏设置
        WindowManagerUtils.setWindows();
        initView();
        initListener();
        showKeyBoard();
        initConnManager();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void initView() {
        if (findComponentById(ResourceTable.Id_remote_input) instanceof TextField) {
            textField = (TextField) findComponentById(ResourceTable.Id_remote_input);
            textField.requestFocus();
        }
        okButton = findComponentById(ResourceTable.Id_ok_button);
        leftButton = findComponentById(ResourceTable.Id_left_button);
        rightButton = findComponentById(ResourceTable.Id_right_button);
        upButton = findComponentById(ResourceTable.Id_up_button);
        downButton = findComponentById(ResourceTable.Id_down_button);
        goBackButton = findComponentById(ResourceTable.Id_go_back);
        closeButton = findComponentById(ResourceTable.Id_close_fa);
    }

    private void initListener() {
        // 监听文本变化，远程同步
        textField.addTextObserver((ss, ii, i1, i2) -> {
            Map<String, String> map = new HashMap<>(INIT_SIZE);
            map.put("inputString", ss);
            connectManager.sendRequest(ConnectManagerIml.REQUEST_SEND_DATA, map);
        });
        okButton.setClickedListener(component -> {
            // 点击OK按钮
            buttonClickSound();
            String searchString = textField.getText();
            Map<String, String> map = new HashMap<>(INIT_SIZE);
            map.put("inputString", searchString);
            connectManager.sendRequest(ConnectManagerIml.REQUEST_SEND_SEARCH, map);
        });
        leftButton.setClickedListener(component -> {
            // 点击左键按钮
            sendMoveRequest(Constants.MOVE_LEFT);
        });
        rightButton.setClickedListener(component -> {
            // 点击右键按钮
            sendMoveRequest(Constants.MOVE_RIGHT);
        });
        upButton.setClickedListener(component -> {
            // 点击向上按钮
            sendMoveRequest(Constants.MOVE_UP);
        });
        downButton.setClickedListener(component -> {
            // 点击向下按钮
            sendMoveRequest(Constants.MOVE_DOWN);
        });
        goBackButton.setClickedListener(component -> {
            // 返回大屏主页
            sendMoveRequest(Constants.GO_BACK);
        });
        closeButton.setClickedListener(component -> {
            // 返回主页
            sendMoveRequest(Constants.GO_BACK);
            terminateAbility();
        });
    }

    private void sendMoveRequest(String direction) {
        buttonClickSound();
        Map<String, String> map = new HashMap<>(INIT_SIZE);
        map.put("move", direction);
        connectManager.sendRequest(ConnectManagerIml.REQUEST_SEND_MOVE, map);
    }

    private void showKeyBoard() {
        getUITaskDispatcher().delayDispatch(() -> textField.simulateClick(), SHOW_KEYBOARD_DELAY);
    }

    private void initConnManager() {
        connectManager = ConnectManagerIml.getInstance();
        connectManager.connectPa(this, deviceIdConn);
    }

    private void buttonClickSound() {
        // 步骤1：实例化对象
        SoundPlayer soundPlayer = new SoundPlayer("packageName");

        // 步骤2：播放键盘敲击音，音量为1.0
        soundPlayer.playSound(SoundPlayer.SoundType.KEY_CLICK, 1.0f);
    }
}
