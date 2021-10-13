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

package com.huawei.cookbook.slice;

import com.huawei.cookbook.MainAbility;
import com.huawei.cookbook.OpenCamera;
import com.huawei.cookbook.ResourceTable;
import com.huawei.cookbook.util.FaceAuthResult;
import com.huawei.cookbook.util.LogUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.animation.AnimatorProperty;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.biometrics.authentication.BiometricAuthentication;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * MainAbilitySlice
 *
 * @since 2021-04-12
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final int POOL_CORE_SIZE = 2;

    private static final int POOL_MAX_SIZE = 5;

    private static final int NO_FACE_RET = -1;

    private static final int KEEP_ALIVE_TIME = 3;

    private static final int QUEUE_SIZE = 6;

    private static final int RET_NOT_SUPPORTED = 1;

    private static final int RET_SAFE_LEVEL_NOT_SUPPORTED = 2;

    private static final int RET_NOT_LOCAL = 3;

    private static final int MOVE = 200;

    private MyEventHandle myEventHandle;

    private BiometricAuthentication mBiometricAuthentication;

    /**
     * Create a thread for authentication to avoid blocking other tasks.
     */
    private final Runnable runnable = new Runnable() {
        private void initHandler() {
            EventRunner runner = EventRunner.getMainEventRunner();
            if (runner == null) {
                return;
            }
            myEventHandle = new MyEventHandle(runner);
        }

        @Override
        public void run() {
            // Initialization myEventHandle
            initHandler();
            // Start Authentication
            startAuth();
        }
    };

    private AnimatorProperty animatorProperty;

    private Image image;

    /**
     * onStart
     *
     * @param intent intent
     */
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        image = (Image) findComponentById(ResourceTable.Id_hx_image);

        animatorProperty = new AnimatorProperty();
        animatorProperty.setTarget(image);
        animatorProperty.moveFromY(MOVE)
            .moveToY(-MOVE)
            .moveFromY(-MOVE)
            .moveToY(MOVE)
            .setLoopedCount(AnimatorValue.INFINITE);
        // Create the Start Authentication button and add a click event.
        createStartBtn();
        // Create a Cancel Authentication button and add a click event.
        createCancelBtn();
    }

    /**
     * Create Cancel Button
     */
    private void createCancelBtn() {
        // Create Click Event
        Component component = findComponentById(ResourceTable.Id_button_cancel);
        // Create Button
        Button cancelBtn = null;
        if (component instanceof Button) {
            cancelBtn = (Button) component;
            cancelBtn.setClickedListener(view -> {
                if (mBiometricAuthentication != null) {
                    // Stop Animation
                    animatorProperty.stop();
                    image.setVisibility(Component.HIDE);
                    // Invoke the cancellation interface.
                    int result = mBiometricAuthentication.cancelAuthenticationAction();
                    LogUtils.info("createCancelBtn:", result + "");
                }
            });
        }
    }

    /**
     * Create a button click event to start recognition
     */
    private void createStartBtn() {
        // Create Click Event
        Component component = findComponentById(ResourceTable.Id_button_start);
        // Create Button
        Button featureBtn = null;
        if (component instanceof Button) {
            featureBtn = (Button) component;
            featureBtn.setClickedListener(view -> {
                createStartListener();
            });
        }
    }

    private void createStartListener() {
        image.setVisibility(Component.VISIBLE);
        // Create Animation
        animatorProperty.start();
        // The system prompts the user to point the face at the camera during facial recognition.
        getAndSetText(ResourceTable.Id_text_status, NO_FACE_RET);
        try {
            // Create Biometric Objects
            mBiometricAuthentication = BiometricAuthentication.getInstance(MainAbility.getMainAbility());
            if (mBiometricAuthentication == null) {
                LogUtils.error("mBiometricAuthentication", "mBiometricAuthentication is null");
            }
            // Check whether the device supports facial recognition.
            int hasAuth = mBiometricAuthentication.checkAuthenticationAvailability(
                BiometricAuthentication.AuthType.AUTH_TYPE_BIOMETRIC_FACE_ONLY,
                BiometricAuthentication.SecureLevel.SECURE_LEVEL_S2, true);
            if (hasAuth == 0) {
                ThreadPoolExecutor pool =
                    new ThreadPoolExecutor(POOL_CORE_SIZE, POOL_MAX_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(QUEUE_SIZE), new ThreadPoolExecutor.DiscardOldestPolicy());
                pool.submit(runnable);
            } else {
                // If facial recognition is not supported or has other problems, the page is displayed
                // The main thread does not need to send an echo task through the EventHandler.
                int retExcAuth = getRetExcAuth(hasAuth);
                getAndSetText(ResourceTable.Id_text_status, retExcAuth);
            }
        } catch (IllegalAccessException e) {
            LogUtils.error("createStartBtn", "IllegalAccessException when start auth");
        }
    }

    /**
     * Start Authentication
     */
    private void startAuth() {
        int retExcAuth = mBiometricAuthentication.execAuthenticationAction(
            BiometricAuthentication.AuthType.AUTH_TYPE_BIOMETRIC_FACE_ONLY,
            BiometricAuthentication.SecureLevel.SECURE_LEVEL_S2, true, false, null);
        // Sends the modified page to the main thread for execution.
        myEventHandle.sendEvent(retExcAuth);
    }

    /**
     * Obtain the prompt code based on the returned value of the verification whether authentication is supported.
     *
     * @param hasAuth Certification Capabilities
     * @return Return the authentication code.
     */
    private int getRetExcAuth(int hasAuth) {
        int retExcAuth;
        if (hasAuth == RET_NOT_SUPPORTED) {
            // 2D facial recognition is not supported.
            retExcAuth = FaceAuthResult.AUTH_2D_NOT_SUPPORTED;
        } else if (hasAuth == RET_SAFE_LEVEL_NOT_SUPPORTED) {
            // The security level is not supported.
            retExcAuth = FaceAuthResult.AUTH_SAFE_LEVEL_NOT_SUPPORTED;
        } else if (hasAuth == RET_NOT_LOCAL) {
            // Local authentication
            retExcAuth = FaceAuthResult.AUTH_NOT_LOCAL;
        } else {
            // No Face Recording
            retExcAuth = FaceAuthResult.AUTH_NO_FACE;
        }
        return retExcAuth;
    }

    /**
     * Get and set text
     *
     * @param textId Text box ID.
     * @param retExcAuth Authentication return code
     */
    private void getAndSetText(int textId, int retExcAuth) {
        // Obtaining the Status Text
        Component componentText = findComponentById(textId);
        if (componentText instanceof Text) {
            Text text = (Text) componentText;
            setTextValueAndColor(retExcAuth, text);
            text.setVisibility(Component.VISIBLE);
        }
    }

    /**
     * Setting Text Prompts
     *
     * @param text Text Object
     * @param textValue Text Value
     * @param color Text Color
     */
    private void setTextValueAndColor(Text text, String textValue, Color color) {
        text.setText(textValue);
        text.setTextColor(color);
    }

    /**
     * Set the text display value and text color.
     *
     * @param retExcAuth Authentication return value
     * @param text Text Object
     */
    private void setTextValueAndColor(int retExcAuth, Text text) {
        switch (retExcAuth) {
            case FaceAuthResult.AUTH_SUCCESS:
                setTextValueAndColor(text, FaceAuthResult.AUTH_SUCCESS_MSG, Color.GREEN);
                image.setVisibility(Component.HIDE);
                // Page redirection
                toAuthAfterPage();
                break;
            case FaceAuthResult.AUTH_FAIL:
                setTextValueAndColor(text, FaceAuthResult.AUTH_FAIL_MSG, Color.RED);
                break;
            case FaceAuthResult.AUTH_CANCLE:
                setTextValueAndColor(text, FaceAuthResult.AUTH_CANCLE_MSG, Color.RED);
                break;
            case FaceAuthResult.AUTH_TIME_OUT:
                setTextValueAndColor(text, FaceAuthResult.AUTH_TIME_OUT_MSG, Color.RED);
                break;
            case FaceAuthResult.AUTH_OPEN_CAMERA_FAIL:
                setTextValueAndColor(text, FaceAuthResult.AUTH_OPEN_CAMERA_FAIL_MSG, Color.RED);
                break;
            case FaceAuthResult.AUTH_BUSY:
                setTextValueAndColor(text, FaceAuthResult.AUTH_BUSY_MSG, Color.RED);
                break;
            case FaceAuthResult.AUTH_PARAM_ERROR:
                setTextValueAndColor(text, FaceAuthResult.AUTH_PARAM_ERROR_MSG, Color.RED);
                break;
            case FaceAuthResult.AUTH_FACE_LOCKED:
                setTextValueAndColor(text, FaceAuthResult.AUTH_FACE_LOCKED_MSG, Color.RED);
                break;
            case FaceAuthResult.AUTH_NO_FACE:
                setTextValueAndColor(text, FaceAuthResult.AUTH_NO_FACE_MSG, Color.WHITE);
                break;
            case FaceAuthResult.AUTH_OTHER_ERROR:
                setTextValueAndColor(text, FaceAuthResult.AUTH_OTHER_ERROR_MSG, Color.RED);
                break;
            case FaceAuthResult.AUTH_2D_NOT_SUPPORTED:
                setTextValueAndColor(text, FaceAuthResult.AUTH_2D_NOT_SUPPORTED_MSG, Color.WHITE);
                break;
            case FaceAuthResult.AUTH_SAFE_LEVEL_NOT_SUPPORTED:
                setTextValueAndColor(text, FaceAuthResult.AUTH_SAFE_LEVEL_NOT_SUPPORTED_MSG, Color.WHITE);
                break;
            case FaceAuthResult.AUTH_NOT_LOCAL:
                setTextValueAndColor(text, FaceAuthResult.AUTH_NOT_LOCAL_MSG, Color.WHITE);
                break;
            default:
                setTextValueAndColor(text, FaceAuthResult.BASE_MSG, Color.WHITE);
                break;
        }
    }

    private void toAuthAfterPage() {
        Intent secondIntent = new Intent();
        // Specify the bundleName and abilityName of the FA to be started.
        Operation operation = new Intent.OperationBuilder().withDeviceId("")
            .withBundleName(getBundleName())
            .withAbilityName(OpenCamera.class.getName())
            .build();
        secondIntent.setOperation(operation);
        // Use the startAbility interface of the AbilitySlice to start another page.
        startAbility(secondIntent);
    }

    @Override
    public void onStop() {
        mBiometricAuthentication.cancelAuthenticationAction();
    }

    /**
     * Event Distributor
     *
     * @since 2021-04-12
     */
    private class MyEventHandle extends EventHandler {
        MyEventHandle(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            int eventId = event.eventId;
            getAndSetText(ResourceTable.Id_text_status, eventId);
        }
    }
}
