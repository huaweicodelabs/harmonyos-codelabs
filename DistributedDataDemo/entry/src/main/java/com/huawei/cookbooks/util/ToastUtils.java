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

package com.huawei.cookbooks.util;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

/**
 * Widget helper
 *
 * @since 2021-09-04
 */
public final class ToastUtils {
    private static final int TEXT_SIZE = 48;
    private static final int LEFT_PADDING = 30;
    private static final int TOP_PADDING = 20;
    private static final int RIGHT_PADDING = 30;
    private static final int BOTTOM_PADDING = 20;
    private static final int RGB_COLOR = 0x666666FF;
    private static final int RGB_RED_COLOR = 0x8B3D48FF;
    private static final int CORNER_RADIUS = 15;
    private static final int DURATION = 2000;

    private ToastUtils() {
    }

    /**
     * Show tips
     *
     * @param context ability slice
     * @param msg show msg
     * @param flag error color flag
     */
    public static void showTips(Context context, String msg, int flag) {
        Text text = new Text(context);
        text.setWidth(MATCH_CONTENT);
        text.setHeight(MATCH_CONTENT);
        text.setTextSize(TEXT_SIZE);
        text.setText(msg);
        text.setPadding(LEFT_PADDING, TOP_PADDING, RIGHT_PADDING, BOTTOM_PADDING);
        text.setMultipleLine(true);
        text.setTextColor(Color.WHITE);
        text.setTextAlignment(TextAlignment.CENTER);
        DirectionalLayout.LayoutConfig config = new DirectionalLayout.LayoutConfig();
        config.alignment = LayoutAlignment.CENTER;
        text.setLayoutConfig(config);

        ShapeElement style = new ShapeElement();
        style.setShape(ShapeElement.RECTANGLE);
        style.setRgbColor(new RgbColor(flag == 0 ? RGB_COLOR : RGB_RED_COLOR));
        style.setCornerRadius(CORNER_RADIUS);
        text.setBackground(style);

        ToastDialog toastDialog = new ToastDialog(context);
        toastDialog.setSize(MATCH_PARENT, MATCH_CONTENT);
        toastDialog.setDuration(DURATION);
        toastDialog.setAutoClosable(true);
        toastDialog.setTransparent(true);

        toastDialog.setAlignment(LayoutAlignment.CENTER);
        toastDialog.setComponent(text);
        toastDialog.show();
    }
}
