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

import ohos.agp.colors.RgbColor;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

/**
 * Dialog util
 *
 * @since 2020-12-04
 */
public class DialogUtils {
    private static final int TEXT_SIZE = 40;
    private static final int TEXT_PADDING = 20;
    private static final int TEXT_HEIGHT = 100;
    private static final int TEXT_CORNER = 20;
    private static final int TEXT_OFFSETY = 200;
    private static final int TEXT_ALPHA = 120;

    private DialogUtils() {
    }

    /**
     * Toast Method
     *
     * @param context context
     * @param text Pop-up toast content
     * @param ms Toast display time, in ms.
     */
    public static void toast(Context context, String text, int ms) {
        DependentLayout layout = new DependentLayout(context);
        layout.setWidth(ScreenUtils.getScreenWidth(context));
        layout.setHeight(TEXT_HEIGHT);
        Text textView = new Text(context);
        ShapeElement background = new ShapeElement();
        background.setCornerRadius(TEXT_CORNER);
        background.setRgbColor(new RgbColor(0, 0, 0, TEXT_ALPHA));
        textView.setBackground(background);
        DependentLayout.LayoutConfig config =
                new DependentLayout.LayoutConfig(
                        DependentLayout.LayoutConfig.MATCH_CONTENT, DependentLayout.LayoutConfig.MATCH_CONTENT);
        config.addRule(DependentLayout.LayoutConfig.HORIZONTAL_CENTER);
        textView.setLayoutConfig(config);
        textView.setPadding(TEXT_PADDING, TEXT_PADDING, TEXT_PADDING, TEXT_PADDING);
        textView.setMaxTextLines(1);
        textView.setTextSize(TEXT_SIZE);
        textView.setMaxTextWidth(ScreenUtils.getScreenWidth(context));
        textView.setTextColor(Color.WHITE);
        textView.setText(text);
        layout.addComponent(textView);
        ToastDialog toastDialog = new ToastDialog(context);
        toastDialog.setContentCustomComponent(layout);
        toastDialog.setTransparent(true);
        toastDialog.setOffset(0, TEXT_OFFSETY);
        toastDialog.setSize(ScreenUtils.getScreenWidth(context), TEXT_HEIGHT);
        toastDialog.setDuration(ms);
        toastDialog.show();
    }
}
