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

package com.huawei.cookbook.util;

import com.huawei.cookbook.ResourceTable;

import ohos.agp.components.AttrHelper;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

/**
 * toast util
 *
 * @ClassName: ToastUtil
 * @Description: java description
 */
public class ToastUtil {
    /**
     * offset toast
     */
    public static final int OFFSET_TOAST = 64;
    /**
     * toast show duration
     */
    public static final int TOAST_SHOW_DURATION = 3500;

    /**
     * show toast
     *
     * @param context context
     * @param message message
     */
    public static void showToast(Context context, String message) {
        DirectionalLayout toast =
                (DirectionalLayout) LayoutScatter.getInstance(context).parse(ResourceTable.Layout_toast, null, false);
        Text toastText = (Text) toast.findComponentById(ResourceTable.Id_toastText);
        toastText.setText(message);
        new ToastDialog(context)
                .setTitleCustomComponent(toast)
                .setOffset(0, AttrHelper.vp2px(OFFSET_TOAST, context))
                .setTransparent(true)
                .setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT)
                .setDuration(TOAST_SHOW_DURATION)
                .show();
    }
}
