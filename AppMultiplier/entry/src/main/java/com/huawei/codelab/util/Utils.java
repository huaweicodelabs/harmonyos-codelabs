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

package com.huawei.codelab.util;

import com.huawei.codelab.ResourceTable;

import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.Texture;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * tools
 *
 * @since 2021-09-10
 */
public class Utils {
    private static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, 0xD001400, "Utils");
    private static final List<Integer> PICTURE_IDS = Arrays.asList(ResourceTable.Media_p1,
            ResourceTable.Media_p2, ResourceTable.Media_p3,
            ResourceTable.Media_p4, ResourceTable.Media_p5,
            ResourceTable.Media_p6, ResourceTable.Media_p7,
            ResourceTable.Media_p8, ResourceTable.Media_p9,
            ResourceTable.Media_p10,
            ResourceTable.Media_p11, ResourceTable.Media_p12,
            ResourceTable.Media_p13); // Image ID set
    private static final int HALF = 2; // 取值一半，除以此数
    private static final int HEIGHT_RATIO = 9; // 高比例
    private static final int WIDTH_RATIO = 16; // 宽比例
    private static final int RES_HEIGHT = 500; // 默认高度500
    private static final int DIVIDE_WIDTH = 16; // Width of the split screen in the middle of two slices
    private static final int COLUMN_FOUR = 4; // 每行显示4张图片
    private static final int COLUMN_THREE = 3; // 每行显示3张图片
    private static final int COLUMN_TWO = 2; // 每行显示2张图片
    private static int leftWidth; // 左边屏幕宽度
    private static int rightWidth; // 右边屏幕宽度
    private static int sumWidth; // 左右屏幕之和

    // Original size of the pixelMap set when the image ID
    // in the Media is converted to the original size of the pixelMap set
    private static List<PixelMap> resourcePixelMaps;
    private static List<PixelMap> pixelMaps; // PixelMap collection of all images displayed in the listContainer

    private static List<PixelMap> addPixelmaps = new ArrayList<>(0); // Add a pixel map of an image

    // Update the pixel map of an image
    private static List<Map<String, Object>> updatePixelmaps = new ArrayList<>(0);
    private static int rows; // 行数
    private static int columns; // 列数
    private static int size; // 图片数量

    private static final List<Map<String, Object>> ADD_ORDER_MAPS = new ArrayList<>(0);
    private static final List<Map<String, Object>> UPDATE_ORDER_MAPS = new ArrayList<>(0);

    private Utils() {
    }

    /**
     * Obtains the screen width of the current slice
     *
     * @param context context
     * @return The type is int
     */
    public static int getScreenWidth(Context context) {
        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
        DisplayAttributes attributes = display.getAttributes();
        return attributes.width;
    }

    /**
     * Sets the width of the left slice screen
     *
     * @param context context
     */
    public static void setLeftWidth(Context context) {
        leftWidth = getScreenWidth(context);
    }

    /**
     * Sets the width of the right slice screen
     *
     * @param context context
     */
    public static void setRightWidth(Context context) {
        rightWidth = getScreenWidth(context);
    }

    /**
     * 计算左右屏幕宽度之和
     */
    public static void calSumWidth() {
        sumWidth = leftWidth + rightWidth;
    }

    /**
     * Converts an image to a PixelMap (default image size).
     * This operation is performed only once.
     *
     * @param context context
     */
    public static void transResourceIdsToListOnce(Context context) {
        resourcePixelMaps = new ArrayList<>(0);
        // Set the pixel map
        for (int index: PICTURE_IDS) {
            InputStream source = null;
            ImageSource imageSource;
            try {
                source = context.getResourceManager().getResource(index);
                imageSource = ImageSource.create(source, null);
                resourcePixelMaps.add(imageSource.createPixelmap(null));
            } catch (IOException | NotExistException e) {
                HiLog.error(TAG, "Get Resource PixelMap error");
            } finally {
                try {
                    assert source != null;
                    source.close();
                } catch (IOException e) {
                    HiLog.error(TAG, "getPixelMap source close error");
                }
            }
        }
    }

    /**
     * Assemble the PixelMap displayed in the ListContainer
     *
     * @param context context
     * @return PixelMap[] in List
     */
    public static List<PixelMap[]> transIdToPixelMap(Context context) {
        size = PICTURE_IDS.size() + addPixelmaps.size();
        // 设置行列
        setColunmRows();
        // Converts the pictures in the Media and the added and updated pictures
        // into a List<PixelMap> based on the width of the left screen
        transAllToResizeList(context);
        // 组装返回数据 并返回
        return assembleData();
    }

    // Set the number of rows and columns in the listContainer based on the width and number of images
    private static void setColunmRows() {
        // 根据宽度设置每行图片个数和图片行数
        if (leftWidth == sumWidth) { // Show MainAbility Only
            columns = COLUMN_TWO;
            rows = size / COLUMN_TWO;
        } else { // 分屏显示
            if (leftWidth == rightWidth) {
                if (leftWidth == sumWidth + DIVIDE_WIDTH) {
                    columns = COLUMN_FOUR;
                    rows = size / COLUMN_FOUR;
                } else {
                    // 每行显示两张图片
                    columns = COLUMN_TWO;
                    rows = size / COLUMN_TWO;
                }
            } else if (leftWidth < rightWidth) {
                // 每行显示一张图片
                columns = 1;
                rows = size;
            } else {
                // 每行显示三张图片
                columns = COLUMN_THREE;
                rows = size / COLUMN_THREE;
            }
        }
    }

    /**
     * Calculate the size of the left image based on the size of the left screen
     *
     * @param context context
     */
    public static void transAllToResizeList(Context context) {
        // Converts all IDs to pixelMaps
        pixelMaps = new ArrayList<>(0); // Convert the ID to a PixelMap set
        // Set the pixel map
        for (int index: PICTURE_IDS) {
            InputStream source = null;
            ImageSource imageSource;
            try {
                // Converts the image ID into an input object based on the subscript
                source = context.getResourceManager().getResource(index);
                imageSource = ImageSource.create(source, null); // 创建图像数据源
                ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions(); // 创建编码对象
                decodingOpts.desiredSize = new Size(leftWidth / columns, RES_HEIGHT); // 设置宽高
                // PixelMap objects converted from image IDs are stored in the pixelMaps collection
                pixelMaps.add(imageSource.createPixelmap(decodingOpts));
            } catch (IOException | NotExistException ex) {
                HiLog.error(TAG, "get Left PixelMap error");
            } finally {
                try {
                    assert source != null;
                    source.close();
                } catch (IOException e) {
                    HiLog.error(TAG, "getPixelMap source close error");
                }
            }
        }
        // Set the size of the new PixelMap and add it to pixelMaps
        addNewPixelMapToPixelMaps();
        // Modify the pixel map to be modified in pixelMaps based on the subscript
        updateNewPixelMapToPixelMaps();
    }

    // Add the new pixel map to the duplicate pixel map
    private static void addNewPixelMapToPixelMaps() {
        for (PixelMap pixelMap: addPixelmaps) {
            PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
            initializationOptions.size = new Size(leftWidth / columns, RES_HEIGHT); // 设置尺寸
            // Adding a PixelMap object to the end of the PixelMaps collection
            pixelMaps.add(PixelMap.create(pixelMap, initializationOptions));
        }
    }

    /**
     * Modify the pixel map to be modified based on the subscript
     */
    public static void updateNewPixelMapToPixelMaps() {
        for (Map<String, Object> map: updatePixelmaps) {
            int index = Integer.parseInt(map.get("index").toString()); // Obtain the PixelMap subscript to be updated
            PixelMap pixelMap = (PixelMap) map.get("pixelMap"); // Obtains the corresponding PixelMap object
            PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
            initializationOptions.size = new Size(leftWidth / columns, RES_HEIGHT); // 设置尺寸
            pixelMaps.remove(index); // Remove the original PixelMap object
            // Add a new PixelMap object to the corresponding subscript
            pixelMaps.add(index, PixelMap.create(pixelMap, initializationOptions));
        }
    }

    /**
     * Assemble data based on the number of rows and columns,
     * and convert the PixelMap set to the PixelMap[] set.
     *
     * @return PixelMap[] in List
     *
     */
    private static List<PixelMap[]> assembleData() {
        List<PixelMap[]> returnPixelMaps; // 返回的数据
        // 组装图片集合 返回的数据
        returnPixelMaps = new ArrayList<>(rows); // 初始化返回的数据，根据行数设置长度
        PixelMap[] pms = new PixelMap[columns]; // 每行显示的图片数（图片列数）
        // Set the position of the image in the listContainer based on the current image subscript
        for (int i = 1; i <= pixelMaps.size(); i++) {
            if (i % columns != 0) { // The picture is not at the end of the listContainer line
                pms[i % columns - 1] = pixelMaps.get(i - 1);
                if (i == pixelMaps.size()) {
                    returnPixelMaps.add(pms);
                }
            } else { // 图片在行的末尾位置
                pms[columns - 1] = pixelMaps.get(i - 1);
                returnPixelMaps.add(pms); // 将行数据存放到列表中
                pms = new PixelMap[columns];
            }
        }
        return returnPixelMaps;
    }

    /**
     * Obtains the PixelMap based on the subscript
     *
     * @param index index
     * @return PixelMap
     */
    public static PixelMap getPixelMapByIndex(int index) {
        return resizePixelMap(resourcePixelMaps.get(index));
    }

    /**
     * Set the image size based on the screen size
     *
     * @param pixelMap pixelMap
     * @return PixelMap
     */
    public static PixelMap resizePixelMap(PixelMap pixelMap) {
        PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
        initializationOptions.size = new Size(rightWidth, rightWidth * HEIGHT_RATIO / WIDTH_RATIO);
        return PixelMap.create(pixelMap, initializationOptions);
    }

    /**
     * resourcePixelMaps Adding Elements
     *
     * @param pixelMap pixelMap
     */
    public static void addPixelMap(PixelMap pixelMap) {
        addPixelmaps.add(pixelMap); // 添加到当前左边屏幕中图片集合
        resourcePixelMaps.add(pixelMap); // 添加到原始图片集合中
    }

    /**
     * resourcePixelMaps Modifying Elements
     *
     * @param pixelMap pixelMap
     * @param index index
     */
    public static void updatePixelMap(PixelMap pixelMap, int index) {
        Map<String, Object> map = new HashMap<>(0);
        map.put("pixelMap", pixelMap);
        map.put("index", index);
        updatePixelmaps.add(map);
        resourcePixelMaps.remove(index);
        resourcePixelMaps.add(index, pixelMap); // 添加到原始图片集合中
    }

    /**
     * Obtains the number of columns of an image in the listContainer
     *
     * @return int
     */
    public static int getColumns() {
        return columns;
    }

    /**
     * Creating a Toast
     *
     * @param context context
     * @param message message
     */
    public static void creatToastDialog(Context context, String message) {
        Component component = LayoutScatter.getInstance(context)
                .parse(ResourceTable.Layout_layout_toast, null, false);
        Component componentText = component.findComponentById(ResourceTable.Id_msg_toast);
        if (componentText instanceof Text) {
            ((Text) componentText).setText(message);
        }
        new ToastDialog(context)
                .setComponent(component)
                .setSize(DirectionalLayout.LayoutConfig.MATCH_CONTENT, DirectionalLayout.LayoutConfig.MATCH_CONTENT)
                .setAlignment(LayoutAlignment.CENTER).show();
    }

    /**
     * Creating a pixel map
     *
     * @param width width
     * @param height height
     * @return pixelMap
     */
    public static PixelMap createPixelMap(int width, int height) {
        PixelMap.InitializationOptions opts = new PixelMap.InitializationOptions();
        opts.size = new Size(width, height);
        opts.pixelFormat = PixelFormat.ARGB_8888;
        opts.editable = true;
        return PixelMap.create(opts);
    }

    /**
     * Mirroring PixelMap
     *
     * @param pixelMap pixelMap
     * @param scaleX scaleX
     * @return pixelMap
     */
    public static PixelMap mirrorPixelMap(PixelMap pixelMap, float scaleX) {
        PixelMap pixelMapReturns = Utils.createPixelMap(pixelMap.getImageInfo().size.width,
                pixelMap.getImageInfo().size.height); // Resize PixelMap objects
        Texture texture = new Texture(pixelMapReturns); // Convert PixelMap to Texture Objects
        Canvas canvas = new Canvas(); // Convert PixelMap to Texture Objects
        canvas.setTexture(texture); // Canvas object painting texture
        PixelMapHolder pixelMapHolder = new PixelMapHolder(pixelMap);
        // 进行镜像
        canvas.scale(
                scaleX,
                1.0f,
                (float) pixelMap.getImageInfo().size.width / HALF,
                (float) pixelMap.getImageInfo().size.height / HALF);
        canvas.drawPixelMapHolder(
                pixelMapHolder,
                0,
                0,
                new Paint());
        return pixelMapReturns;
    }

    /**
     * get new add orders
     *
     * @return pixelMaps
     */
    public static List<Map<String, Object>> getAddOrderMaps() {
        return ADD_ORDER_MAPS;
    }

    /**
     * get new update orders
     *
     * @return pixelMaps
     */
    public static List<Map<String, Object>> getUpdateOrderMaps() {
        return UPDATE_ORDER_MAPS;
    }

    /**
     * get all pixelMap
     *
     * @return pixelMaps
     */
    public static List<PixelMap> getResourcePixelMaps() {
        return resourcePixelMaps;
    }

    /**
     * 保存新增的操作指令
     *
     * @param opeMap opeMap
     */
    public static void setAddOrderMaps(Map<String, Object> opeMap) {
        ADD_ORDER_MAPS.add(opeMap);
    }

    /**
     * 保存修改的操作指令
     *
     * @param opeMap opeMap
     */
    public static void setUpdateOrderMaps(Map<String, Object> opeMap) {
        UPDATE_ORDER_MAPS.add(opeMap);
    }

    /**
     * 清空新增、修改的缓存
     */
    public static void clearCache() {
        addPixelmaps = new ArrayList<>(0);
        updatePixelmaps = new ArrayList<>(0);
    }
}
