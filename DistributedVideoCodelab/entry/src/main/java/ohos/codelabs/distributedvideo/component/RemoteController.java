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

package ohos.codelabs.distributedvideo.component;

import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Slider;
import ohos.app.Context;
import ohos.codelabs.distributedvideo.ResourceTable;
import ohos.codelabs.distributedvideo.player.constant.Constants;
import ohos.codelabs.distributedvideo.player.constant.ControlCode;
import ohos.codelabs.distributedvideo.util.ScreenUtils;
import ohos.multimodalinput.event.TouchEvent;

/**
 * RemoteController
 *
 * @since 2020-12-04
 */
public class RemoteController extends DependentLayout
        implements Component.ClickedListener, Component.TouchEventListener, Slider.ValueChangedListener {
    private static final int TOAST_DURATION = 3000;
    private static final float ALPHA = 0.7f;
    private final Context context;
    private RemoteControllerListener remoteControllerListener;
    private Component componentParent;
    private boolean isShown = false;
    private boolean isStop = false;
    private int controlCode = 0;
    private int controlNumber = 0;

    /**
     * constructor of RemoteController
     *
     * @param context context
     */
    public RemoteController(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    /**
     * init RemoteController view
     */
    public void initView() {
        setVisibility(INVISIBLE);
        componentParent = LayoutScatter.getInstance(context).parse(
                ResourceTable.Layout_remote_controller,
                this,
                false);
        initButton(ResourceTable.Id_remote_resume);
        initButton(ResourceTable.Id_remote_pause);
        initButton(ResourceTable.Id_remote_stop);
        initButton(ResourceTable.Id_remote_forward);
        initButton(ResourceTable.Id_remote_reward);
        initButton(ResourceTable.Id_remote_volume_up);
        initButton(ResourceTable.Id_remote_volume_down);
        if (componentParent.findComponentById(ResourceTable.Id_remote_progress_seek) instanceof Slider) {
            Slider progressSlider = (Slider) componentParent.findComponentById(ResourceTable.Id_remote_progress_seek);
            progressSlider.setValueChangedListener(this);
        }
        if (componentParent.findComponentById(ResourceTable.Id_remote_volume_seek) instanceof Slider) {
            Slider volumeSlider = (Slider) componentParent.findComponentById(ResourceTable.Id_remote_volume_seek);
            volumeSlider.setMinValue(0);
            volumeSlider.setMaxValue(Constants.NUMBER_100);
            volumeSlider.setValueChangedListener(this);
        }
        setWidth(ScreenUtils.getScreenWidth(context));
        setHeight(ScreenUtils.getScreenHeight(context));
        addComponent(componentParent);
    }

    private void initButton(int res) {
        Button button = null;
        if (componentParent.findComponentById(res) instanceof Button) {
            button = (Button) componentParent.findComponentById(res);
            button.setClickedListener(this);
            button.setTouchEventListener(this);
            button.setAlpha(ALPHA);
        }
    }

    /**
     * show method
     */
    public void show() {
        if (!isShown) {
            isShown = true;
            setVisibility(VISIBLE);
            if (remoteControllerListener != null) {
                remoteControllerListener.controllerShow();
            }
        }
    }

    /**
     * hide method
     */
    public void hide() {
        if (isShown) {
            isShown = false;
            setVisibility(INVISIBLE);
            if (remoteControllerListener != null) {
                remoteControllerListener.controllerDismiss();
            }
        }
    }

    /**
     * isShown
     *
     * @return boolean isShown
     */
    public boolean isShown() {
        return isShown;
    }

    /**
     * setRemoteControllerCallback
     *
     * @param listener listener
     */
    public void setRemoteControllerCallback(RemoteControllerListener listener) {
        remoteControllerListener = listener;
    }

    /**
     * RemoteControllerListener
     *
     * @since 2020-12-07
     */
    public interface RemoteControllerListener {
        /**
         * show the controller dialog
         *
         * @since 2020-12-07
         */
        void controllerShow();

        /**
         * dismiss the controller dialog
         *
         * @since 2020-12-07
         */
        void controllerDismiss();

        /**
         * sendControl
         *
         * @param code code
         * @param extra extra
         */
        void sendControl(int code, int extra);
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                component.setAlpha(1);
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                component.setAlpha(ALPHA);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(Component component) {
        if (component.getId() != ResourceTable.Id_remote_resume && isStop) {
            Toast.toast(context, "Please play video first.", TOAST_DURATION);
            return;
        }
        switch (component.getId()) {
            case ResourceTable.Id_remote_resume:
                isStop = false;
                controlCode = ControlCode.RESUME.getCode();
                break;
            case ResourceTable.Id_remote_stop:
                isStop = true;
                controlCode = ControlCode.STOP.getCode();
                break;
            case ResourceTable.Id_remote_forward:
                controlCode = ControlCode.FORWARD.getCode();
                break;
            case ResourceTable.Id_remote_pause:
                controlCode = ControlCode.PAUSE.getCode();
                break;
            case ResourceTable.Id_remote_reward:
                controlCode = ControlCode.REWARD.getCode();
                break;
            case ResourceTable.Id_remote_volume_up:
                controlCode = ControlCode.VOLUME_ADD.getCode();
                break;
            case ResourceTable.Id_remote_volume_down:
                controlCode = ControlCode.VOLUME_REDUCED.getCode();
                break;
            default:
                break;
        }
        if (remoteControllerListener != null) {
            remoteControllerListener.sendControl(controlCode, 0);
        }
    }

    @Override
    public void onProgressUpdated(Slider slider, int extra, boolean isUpdate) {
    }

    @Override
    public void onTouchStart(Slider slider) {
    }

    @Override
    public void onTouchEnd(Slider slider) {
        if (isStop) {
            Toast.toast(context, "Please play video first.", TOAST_DURATION);
            return;
        }
        int extras = slider.getProgress();
        switch (slider.getId()) {
            case ResourceTable.Id_remote_progress_seek:
                controlNumber = ControlCode.SEEK.getCode();
                break;
            case ResourceTable.Id_remote_volume_seek:
                controlNumber = ControlCode.VOLUME_SET.getCode();
                break;
            default:
                break;
        }
        if (remoteControllerListener != null) {
            remoteControllerListener.sendControl(controlNumber, extras);
        }
    }
}
