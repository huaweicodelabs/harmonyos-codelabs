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
import com.huawei.codelab.devices.SelectDeviceDialog;
import com.huawei.codelab.utils.CommonData;
import com.huawei.codelab.utils.CommonUtil;
import com.huawei.codelab.utils.LogUtil;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * MathGameAbilitySlice
 *
 * @since 2021-01-11
 */
public class MathGameAbilitySlice extends AbilitySlice {
    private static final String TAG = CommonData.TAG + MathGameAbilitySlice.class.getSimpleName();

    private static final int MAX_NUM = 100;

    private static final int EXCEPTION_NUM = -1;

    private Text numberText1;

    private Text numberText2;

    private TextField answerText;

    private int number1;

    private int number2;

    private int answer;

    private Button answerBtn;

    private Button nextBtn;

    private Button helpBtn;

    private List<DeviceInfo> devices = new ArrayList<>();

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_math_game);
        initView();
        setQuestion();
    }

    private void initView() {
        if (findComponentById(ResourceTable.Id_number1) instanceof Text) {
            numberText1 = (Text) findComponentById(ResourceTable.Id_number1);
        }
        if (findComponentById(ResourceTable.Id_number2) instanceof Text) {
            numberText2 = (Text) findComponentById(ResourceTable.Id_number2);
        }
        if (findComponentById(ResourceTable.Id_answer) instanceof TextField) {
            answerText = (TextField) findComponentById(ResourceTable.Id_answer);
        }
        if (findComponentById(ResourceTable.Id_answer_btn) instanceof Button) {
            answerBtn = (Button) findComponentById(ResourceTable.Id_answer_btn);
        }
        if (findComponentById(ResourceTable.Id_next_btn) instanceof Button) {
            nextBtn = (Button) findComponentById(ResourceTable.Id_next_btn);
        }
        if (findComponentById(ResourceTable.Id_help_btn) instanceof Button) {
            helpBtn = (Button) findComponentById(ResourceTable.Id_help_btn);
        }

        answerBtn.setClickedListener(new ButtonClick());
        nextBtn.setClickedListener(new ButtonClick());
        helpBtn.setClickedListener(new ButtonClick());
    }

    private void setQuestion() {
        number1 = CommonUtil.getRandomInt(MAX_NUM);
        number2 = CommonUtil.getRandomInt(MAX_NUM);
        numberText1.setText(number1 + "");
        numberText2.setText(number2 + "");
        answerText.setText("");
    }

    private void checkAnswer() {
        try {
            answer = Integer.parseInt(answerText.getText());
        } catch (NumberFormatException e) {
            answer = EXCEPTION_NUM;
        }
        if (answer == number1 + number2) {
            new ToastDialog(getContext()).setText("回答正确").setAlignment(LayoutAlignment.CENTER).show();
        } else {
            new ToastDialog(getContext()).setText("回答错误").setAlignment(LayoutAlignment.CENTER).show();
        }
    }

    private void getDevices() {
        if (devices.size() > 0) {
            devices.clear();
        }
        List<DeviceInfo> deviceInfos =
            DeviceManager.getDeviceList(ohos.distributedschedule.interwork.DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        LogUtil.info(TAG, "MathGameAbilitySlice deviceInfos size is :" + deviceInfos.size());
        devices.addAll(deviceInfos);
        showDevicesDialog();
    }

    private void showDevicesDialog() {
        new SelectDeviceDialog(this, devices, deviceInfo -> {
            startLocalFa(deviceInfo.getDeviceId());
            startRemoteFa(deviceInfo.getDeviceId());
        }).show();
    }

    private void startLocalFa(String deviceId) {
        LogUtil.info(TAG, "startLocalFa......");
        Intent intent = new Intent();
        intent.setParam(CommonData.KEY_REMOTE_DEVICEID, deviceId);
        intent.setParam(CommonData.KEY_IS_LOCAL, true);
        Operation operation = new Intent.OperationBuilder().withBundleName(getBundleName())
            .withAbilityName(CommonData.ABILITY_MAIN)
            .withAction(CommonData.DRAW_PAGE)
            .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    private void startRemoteFa(String deviceId) {
        LogUtil.info(TAG, "startRemoteFa......");
        String localDeviceId =
            KvManagerFactory.getInstance().createKvManager(new KvManagerConfig(this)).getLocalDeviceInfo().getId();
        Intent intent = new Intent();
        intent.setParam(CommonData.KEY_REMOTE_DEVICEID, localDeviceId);
        intent.setParam(CommonData.KEY_IS_LOCAL, false);
        Operation operation = new Intent.OperationBuilder().withDeviceId(deviceId)
            .withBundleName(getBundleName())
            .withAbilityName(CommonData.ABILITY_MAIN)
            .withAction(CommonData.DRAW_PAGE)
            .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
            .build();
        intent.setOperation(operation);
        startAbility(intent);
    }

    /**
     * ButtonClick
     *
     * @since 2021-01-11
     */
    private class ButtonClick implements Component.ClickedListener {
        @Override
        public void onClick(Component component) {
            int btnId = component.getId();
            switch (btnId) {
                case ResourceTable.Id_answer_btn:
                    checkAnswer();
                    break;
                case ResourceTable.Id_next_btn:
                    setQuestion();
                    break;
                case ResourceTable.Id_help_btn:
                    getDevices();
                    break;
                default:
                    break;
            }
        }
    }
}
