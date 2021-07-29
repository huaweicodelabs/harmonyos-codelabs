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

package com.huawei.codelab.utils;

import ohos.aafwk.ability.Ability;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.gif.GifImage;

/**
 * Dialog util
 *
 * @since 2021-04-14
 */
public class DialogUtil {
    /**
     * list dialog width
     */
    public static final int LIST_DIALOG_WIDTH = 600;
    /**
     * list dialog height
     */
    public static final int LIST_DIALOG_HEIGHT = 800;
    /**
     * button dialog width
     */
    public static final int BUTTON_DIALOG_HEIGHT = 400;
    /**
     * delay time
     */
    public static final int DELAY_TIME = 3000;
    /**
     * delay time
     */
    public static final int DELAY_TIME_O = 1000;
    /**
     * delay time
     */
    public static final int DELAY = 3;

    /**
     * show toast
     *
     * @param context context
     * @param message message
     */
    public static void showToast(Context context, String message) {
        ToastDialog toastDialog = new ToastDialog(context);
        Component layout = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_toast_layout, null, false);
        if (layout.findComponentById(ResourceTable.Id_text) instanceof Text) {
            Text text = (Text) layout.findComponentById(ResourceTable.Id_text);
            text.setText(message);
        }
        toastDialog.setComponent(layout);
        toastDialog.setAlignment(LayoutAlignment.BOTTOM);
        // Set the background transparency of the dialog box.
        toastDialog.setTransparent(true);
        toastDialog.setDuration(3 * 1000);
        toastDialog.show();
    }

    /**
     * show open close dialog
     *
     * @param content content
     * @param context context
     * @param comfirmListener comfirm
     * @param cancelListener  cancel
     */
    public static void showComfirmDialog(String content, Context context,
            CommonDialog.DestroyedListener comfirmListener,
            CommonDialog.DestroyedListener cancelListener) {
        CommonDialog commonDialog = new CommonDialog(context);
        Component layout = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_dialog_comfirm, null, false);
        commonDialog.setAutoClosable(true);
        commonDialog.setContentCustomComponent(layout);
        commonDialog.setSize(LIST_DIALOG_HEIGHT, BUTTON_DIALOG_HEIGHT);
        if (layout.findComponentById(ResourceTable.Id_text) instanceof Text) {
            Text text = (Text) layout.findComponentById(ResourceTable.Id_text);
            text.setText(content);
        }
        layout.findComponentById(ResourceTable.Id_comfirm).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                commonDialog.destroy();
                comfirmListener.onDestroy();
            }
        });
        layout.findComponentById(ResourceTable.Id_cancel).setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                commonDialog.destroy();
                cancelListener.onDestroy();
            }
        });
        commonDialog.show();
    }

    /**
     * show gif dialog
     *
     * @param context  context
     * @param destroyedListener destroy
     */
    public static void showGifDialog(Context context, CommonDialog.DestroyedListener destroyedListener) {
        CommonDialog toastDialog = new CommonDialog(context);
        Component layout = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_dialog_gif, null, false);
        if (layout.findComponentById(ResourceTable.Id_gifImage) instanceof GifImage) {
            GifImage gifImage = (GifImage) layout.findComponentById(ResourceTable.Id_gifImage);
            gifImage.play();
            toastDialog.setContentCustomComponent(layout);
            toastDialog.setSize(LIST_DIALOG_HEIGHT, LIST_DIALOG_HEIGHT);
            toastDialog.setAlignment(LayoutAlignment.CENTER);
            toastDialog.setAutoClosable(true);
            toastDialog.setDestroyedListener(new CommonDialog.DestroyedListener() {
                @Override
                public void onDestroy() {
                    gifImage.pause();
                    destroyedListener.onDestroy();
                }
            });
            toastDialog.show();
            context.getMainTaskDispatcher().delayDispatch(new Runnable() {
                @Override
                public void run() {
                    gifImage.pause();
                    toastDialog.destroy();
                    destroyedListener.onDestroy();
                }
            }, DELAY_TIME);
        }
    }

    /**
     * exit dialog
     *
     * @param context Ability
     */
    public static void exitDialog(Ability context) {
        CommonDialog toastDialog = new CommonDialog(context);
        Component layout = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_exit_dialog, null, false);
        if (layout.findComponentById(ResourceTable.Id_delay) instanceof Text) {
            Text delay = (Text) layout.findComponentById(ResourceTable.Id_delay);
            toastDialog.setContentCustomComponent(layout);
            toastDialog.setSize(LIST_DIALOG_WIDTH, BUTTON_DIALOG_HEIGHT);
            toastDialog.setAlignment(LayoutAlignment.CENTER);
            toastDialog.setAutoClosable(false);
            toastDialog.show();
            EventHandler eventHandler = new EventHandler(EventRunner.getMainEventRunner());
            final int[] count = {DELAY};
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (count[0] == 0) {
                        toastDialog.destroy();
                        context.terminateAbility();
                    } else {
                        count[0]--;
                        delay.setText(count[0] + "");
                        eventHandler.postTask(this, DELAY_TIME_O);
                    }
                }
            };
            eventHandler.postTask(runnable, DELAY_TIME_O);
        }
    }
}
