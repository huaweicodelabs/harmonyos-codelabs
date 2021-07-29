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

package com.huawei.cookbook.qrlibrary.util;

import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.common.ImageFormat;

import com.huawei.cookbook.ResourceTable;

/**
 * QR Util.
 *
 * @since 2021-05-25
 */
public final class QRCodeUtil {
    /**
     * 打印tag.
     */
    private static final HiLogLabel LABEL_LOG =
            new HiLogLabel(3, 0xD001100, "QRCodeUtil");

    private QRCodeUtil() {
    }

    /**
     * 获取屏幕分辨率.
     *
     * @param context 上下文
     * @return 获取屏幕分辨率
     */
    public static Point getScreenResolution(Context context) {
        DisplayAttributes displayAttributes = DisplayManager.getInstance()
                .getDefaultDisplay(context).get().getAttributes();
        Point size = new Point(displayAttributes.width,
                displayAttributes.height);
        return size;
    }

    /**
     * 像素转化.
     *
     * @param context 上下文
     * @param dpValue dp像素
     * @return px像素
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) dpValue * (int) DisplayManager.getInstance()
                .getDefaultDisplay(context).get().getAttributes().densityPixels;
    }

    /**
     * Image转byte[].
     *
     * @param image 相机每一帧的Image对象
     * @return byte[]数据
     */
    public static byte[] image2Byte(ohos.media.image.Image image) {
        ohos.media.image.Image.Component component =
                image.getComponent(ImageFormat.ComponentType.JPEG);
        byte[] bytes = new byte[component.remaining()];
        component.read(bytes);
        return bytes;
    }

    /**
     * 变换矩形框方法.
     *
     * @param rect 初始矩形框
     * @return ReactFloat 变换矩形框
     */
    public static RectFloat trasnsRectFfromRect(Rect rect) {
        RectFloat rectFloat = new RectFloat();
        if (rect == null) {
            return rectFloat;
        }
        rectFloat = new RectFloat(rect.left, rect.top, rect.right, rect.bottom);
        return rectFloat;
    }

    /**
     * show Toast.
     *
     * @param context context
     * @param message show msg
     */
    public static void showToast(Context context, String message) {
        ToastDialog toastDialog = new ToastDialog(context);
        Component layout = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_dialog_layout, null, false);
        if (layout.findComponentById(ResourceTable.Id_text) instanceof Text) {
            Text text = (Text) layout.findComponentById(ResourceTable.Id_text);
            text.setText(message);
        }
        toastDialog.setComponent(layout);
        toastDialog.setAlignment(LayoutAlignment.CENTER);
        // 设置弹框背景透明
        toastDialog.setTransparent(true);
        toastDialog.show();
    }

    /**
     * 字符串非空判断.
     *
     * @param content 传入的内容
     * @return boolean 是否为空
     */
    public static boolean isEmpty(String content) {
        return (content == null || content.length() == 0);
    }

    /**
     * 打印日志.
     *
     * @param msg 打印的信息
     */
    public static void error(String msg) {
        HiLog.error(LABEL_LOG, msg);
    }
}
