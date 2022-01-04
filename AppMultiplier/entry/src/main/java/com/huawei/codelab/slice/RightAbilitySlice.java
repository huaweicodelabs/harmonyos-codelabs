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
import com.huawei.codelab.component.DeviceDialog;
import com.huawei.codelab.component.SaveDialog;
import com.huawei.codelab.util.Utils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.app.Context;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.distributedhardware.devicemanager.DeviceInfo;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * right abilitySlice
 *
 * @since 2021-09-10
 */
public class RightAbilitySlice extends AbilitySlice implements IAbilityContinuation {
    private static final int MARGIN = 2;
    private static final int SRCREGION = 100; // 裁剪的长宽
    private static final int HALF = 2; // 取值一半，除以此数
    private static final int HEIGHT_RATIO = 9; // 高比例
    private static final int WIDTH_RATIO = 16; // 宽比例
    private static final int CIR_SIZE = 50; // 裁剪截取宽高
    private static final String INDEX = "index";
    private static final String ISCROP = "isCrop";
    private static final String ISSCALE = "isScale";
    private static final String ISMIRRORFLAG = "isMirrorFlag";
    private static Context context;
    private static PixelMap pixelMap; // 被操作的原始图片
    private static PixelMap imagePixelMap; // 裁剪缩放镜像后的图片
    private static int idIndex; // Image ID index
    private static boolean isCrop = false; // 裁剪标识
    private static boolean isScale = false; // 缩放标识
    private static boolean isMirror = false; // 镜像标识
    private static boolean isMirrorFlag = false; // 镜像标识
    private static float scaleX = 1.0f;
    private static Image image; // 页面展示的图片组件
    private static String selectDeviceId; // 选择的设备id
    private Component cropComponent; // 裁剪组件
    private Component scaleComponent; // 缩放组件
    private Component mirrorComponent; // 镜像组件
    private Component cirComponent; // 流转组件
    private Component saveComponent; // 保存组件
    private Component deviceComponent; // 选择设备组件
    private DeviceDialog deviceDialog; // 设备选择
    private String remoteData; // 远程迁移标识
    private int remoteIndex; // 迁移的图片下标
    private boolean isOther; // 是否流转新增、修改的图片
    private boolean isCirculation = false; // 是否流转中

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_right);
        context = getContext();
        isOther = false;
        // 设置右屏幕宽度
        Utils.setRightWidth(getContext());
        // 计算屏幕总宽度
        Utils.calSumWidth();
        // 初始化组件
        initView();
        // 获取设备类型
        int localDeviceType = Integer.parseInt(KvManagerFactory.getInstance().createKvManager(
                new KvManagerConfig(this)).getLocalDeviceInfo().getType());
        // 根据设备类型显示隐藏保存组件
        if (localDeviceType == DeviceInfo.DeviceType.PHONE.value()) {
            saveComponent.setVisibility(Component.HIDE);
            // Refresh the listContainer data
            MainAbilitySlice.initData(Utils.transIdToPixelMap(context));
        } else {
            saveComponent.setVisibility(Component.VISIBLE);
        }
        if ("remote".equals(this.remoteData)) {
            // 设置远程图片
            setImageRemote();
        } else {
            // 获取图片下标 设置图片
            int index = Integer.parseInt(intent.getParams().getParam(INDEX).toString());
            setImage(index);
        }
        // 初始化设备选择
        deviceDialog = new DeviceDialog(getContinuationRegisterManager(),RightAbilitySlice.this);
    }

    // 初始化组件
    private void initView() {
        // 获取图片
        Component component = findComponentById(ResourceTable.Id_image);
        if (component instanceof Image) {
            image = (Image) component;
        }
        // 获取按钮
        if (findComponentById(ResourceTable.Id_crop_image) instanceof DirectionalLayout) {
            cropComponent = findComponentById(ResourceTable.Id_crop_image);
        }
        if (findComponentById(ResourceTable.Id_scale_image) instanceof DirectionalLayout) {
            scaleComponent = findComponentById(ResourceTable.Id_scale_image);
        }
        if (findComponentById(ResourceTable.Id_mirror_image) instanceof DirectionalLayout) {
            mirrorComponent = findComponentById(ResourceTable.Id_mirror_image);
        }
        if (findComponentById(ResourceTable.Id_cir_image) instanceof DirectionalLayout) {
            cirComponent = findComponentById(ResourceTable.Id_cir_image);
        }
        if (findComponentById(ResourceTable.Id_save_image) instanceof DirectionalLayout) {
            saveComponent = findComponentById(ResourceTable.Id_save_image);
        }
        if (findComponentById(ResourceTable.Id_device_image) instanceof DirectionalLayout) {
            deviceComponent = findComponentById(ResourceTable.Id_device_image);
        }
        if (findComponentById(ResourceTable.Id_image) instanceof Image) {
            image = (Image) findComponentById(ResourceTable.Id_image);
        }
        cropComponent.setClickedListener(new ComponentClick());
        scaleComponent.setClickedListener(new ComponentClick());
        mirrorComponent.setClickedListener(new ComponentClick());
        cirComponent.setClickedListener(new ComponentClick());
		cirComponent.setDoubleClickedListener(listener -> { // 防止多次点击
        });
        saveComponent.setClickedListener(new ComponentClick());
        deviceComponent.setClickedListener(new ComponentClick());
    }

    /**
     * Clipping, zooming, and mirroring binding click events
     *
     * @since 2021-09-10
     */
    private class ComponentClick implements Component.ClickedListener {
        @Override
        public void onClick(Component component) {
            int componentId = component.getId();
            switch (componentId) {
                case ResourceTable.Id_crop_image:
                    // 裁剪图片
                    isCrop = !isCrop;
                    isScale = false;
                    isMirror = false;
                    isMirrorFlag = false;
                    imagePixelMap = getPixelMapFromResource();
                    image.setPixelMap(imagePixelMap);
                    break;
                case ResourceTable.Id_scale_image:
                    // 缩放图片
                    isCrop = false;
                    isScale = !isScale;
                    isMirror = false;
                    isMirrorFlag = false;
                    imagePixelMap = getPixelMapFromResource();
                    image.setPixelMap(imagePixelMap);
                    break;
                case ResourceTable.Id_mirror_image:
                    // 镜像图片
                    isCrop = false;
                    isScale = false;
                    isMirror = true;
                    isMirrorFlag = !isMirrorFlag;
                    mirrorImage(pixelMap);
                    break;
                case ResourceTable.Id_save_image:
                    // 保存图片
                    saveImage();
                    break;
                case ResourceTable.Id_device_image:
                    // 打开设备选择框（连接）
                    deviceDialog.showDeviceList();
                    break;
                case ResourceTable.Id_cir_image:
                    // 流转
                    circulation();
                    break;
                default:
                    break;
            }
        }
    }

    // 对图片进行镜像
    private static void mirrorImage(PixelMap mirrorPixelMap) {
        scaleX = -scaleX; // 镜像参数设置

        image.addDrawTask(
            (component, canvas) -> {
                if (isMirror) {
                    isMirror = false; // Reset the image ID to false
                    PixelMapHolder pmh = new PixelMapHolder(mirrorPixelMap);
                    // 进行镜像操作
                    canvas.scale(
                            scaleX,
                            1.0f,
                            (float) mirrorPixelMap.getImageInfo().size.width / HALF,
                            (float) mirrorPixelMap.getImageInfo().size.height / HALF);
                    canvas.drawPixelMapHolder(
                            pmh,
                            0,
                            0,
                            new Paint());
                    // 获取镜像图片
                    imagePixelMap = Utils.mirrorPixelMap(mirrorPixelMap, scaleX);
                }
            }
        );
    }

    // 缩放裁剪图片
    private static PixelMap getPixelMapFromResource() {
        int width = image.getPixelMap().getImageInfo().size.width; // 获取当前图片宽度
        PixelMap.InitializationOptions initializationOptions = new PixelMap.InitializationOptions();
        if (isScale) {
            // 缩放 长宽缩小到一半
            initializationOptions.size = new Size(width / HALF, width * HEIGHT_RATIO / WIDTH_RATIO / HALF);
        }
        if (isCrop) {
            // 裁剪 图片中心位置 上下左右框50px
            Rect srcRegion = new Rect(width / HALF - CIR_SIZE,
                    width * HEIGHT_RATIO / WIDTH_RATIO / HALF - CIR_SIZE, SRCREGION, SRCREGION);
            return PixelMap.create(pixelMap, srcRegion, initializationOptions);
        }
        return PixelMap.create(pixelMap, initializationOptions);
    }

    /**
     * Setting Pictures in the RightAbilitySlice
     *
     * @param index Image Index
     */
    public static void setImage(int index) {
        if (image == null) {
            return;
        }
        // 重新定义右边图片大小
        idIndex = index; // 设置选择当前图片的索引
        pixelMap = Utils.getPixelMapByIndex(index); // 保存当前图片，操作时用到
        imagePixelMap = pixelMap;
        image.setPixelMap(pixelMap);
        image.setMarginLeft(MARGIN);
        image.setMarginRight(MARGIN);
        // 重置图片状态
        isCrop = false;
        isScale = false;
        isMirrorFlag = false;
        scaleX = 1.0f;
    }

    /**
     * Remotely setting images in the RightAbilitySlice
     */
    private void setImageRemote() {
        // 重新定义右边图片大小
        idIndex = remoteIndex; // 设置选择当前图片的索引
        if (!isOther) {
            pixelMap = Utils.getPixelMapByIndex(idIndex); // 保存当前图片，操作时用到
            imagePixelMap = pixelMap;
        }
        image.setPixelMap(pixelMap);
        image.setMarginLeft(MARGIN);
        image.setMarginRight(MARGIN);
        if (isCrop || isScale) { // 裁剪、缩放
            imagePixelMap = getPixelMapFromResource();
            image.setPixelMap(imagePixelMap);
        }
        if (isMirrorFlag) { // 镜像
            isMirror = true;
            scaleX = -scaleX;
            mirrorImage(imagePixelMap); // 镜像图片
        }
    }

    /**
     * Obtains the subscript of the selected image
     *
     * @return idIndex idIndex
     */
    public static int getIdIndex() {
        return idIndex;
    }

    // 保存图片
    private void saveImage() {
        if (!isCrop && !isScale && !isMirrorFlag) {
            // 未对图片进行操作 不保存
            Utils.creatToastDialog(getContext(), "图片未作修改！");
        } else {
            // 保存图片
            openSaveDialog();
        }
    }

    // 打开保存或替换的弹框
    private void openSaveDialog() {
        SaveDialog.open(getContext());
    }

    /**
     * Save the modified image, add or modify the image
     *
     * @param flag flag
     */
    public static void saveOrReplaceImage(int flag) {
        Map<String, Object> map = new HashMap<>(0);
        map.put(ISCROP, isCrop);
        map.put(ISSCALE, isScale);
        map.put(ISMIRRORFLAG, isMirrorFlag);
        map.put(INDEX, idIndex); // 操作的图片下标
        if (flag == 0) {
            // 新增
            Utils.addPixelMap(imagePixelMap);
            // 保存新增的操作指令
            map.put("nowIndex", Utils.getResourcePixelMaps().size() - 1); // 新增后的图片下标
            Utils.setAddOrderMaps(map);
        } else {
            // 替换
            Utils.updatePixelMap(imagePixelMap, idIndex);
            // 保存修改的操作指令
            Utils.setUpdateOrderMaps(map);
        }
        // Refresh the listContainer data
        MainAbilitySlice.initData(Utils.transIdToPixelMap(context));
        // 重置状态
        isCrop = false;
        isScale = false;
        isMirrorFlag = false;
    }

    /**
     * Obtains the device ID
     *
     * @param deviceId deviceId
     */
    public static void setDeviceId(String deviceId) {
        selectDeviceId = deviceId;
    }

    // 流转
    private void circulation() {
        if (isCirculation) {
            Utils.creatToastDialog(context, "正在流转，请稍后再试！");
            return;
        }
        if (selectDeviceId == null || "".equals(selectDeviceId)) {
            // 选择设备以后才能流转（迁移）
            Utils.creatToastDialog(context, "请选择连接设备后进行流转操作！");
            return;
        }
        isCirculation = true;
        continueAbility(selectDeviceId);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 断开流转任务管理服务
        deviceDialog.clearRegisterManager();
    }

    @Override
    public boolean onStartContinuation() {
        return true;
    }

    @Override
    public boolean onSaveData(IntentParams saveData) {
        saveData.setParam("continueParam", "remote");
        saveData.setParam(ISCROP, isCrop); // 裁剪
        saveData.setParam(ISSCALE, isScale); // 缩放
        saveData.setParam(ISMIRRORFLAG, isMirrorFlag); // 镜像
        saveData.setParam("scaleX", scaleX); // 镜像值
        saveData.setParam("remoteIndex", idIndex); // 当前图片下标
        saveData.setParam("add", Utils.getAddOrderMaps());
        saveData.setParam("update",Utils.getUpdateOrderMaps());
        return true;
    }

    @Override
    public boolean onRestoreData(IntentParams restoreData) {
        isOther = false;
        this.remoteIndex = Integer.parseInt(restoreData.getParam("remoteIndex").toString());

        // 获取设备类型
        int localDeviceType = Integer.parseInt(KvManagerFactory.getInstance().createKvManager(
                new KvManagerConfig(this)).getLocalDeviceInfo().getType());
        // 根据设备类型显示隐藏保存组件
        if (localDeviceType == DeviceInfo.DeviceType.PHONE.value()) {
            // 重新初始化数据
            Utils.transResourceIdsToListOnce(getContext());
            // 清空修改、新增的缓存
            Utils.clearCache();
            Object add = restoreData.getParam("add"); // 新增图片数据
            Object update = restoreData.getParam("update"); // 修改图片数据
            List<? extends Map> addOrderMaps =
                    ZSONArray.stringToClassList(ZSONObject.toZSONString(add), HashMap.class);
            List<? extends Map> updateOrderMaps =
                    ZSONArray.stringToClassList(ZSONObject.toZSONString(update), HashMap.class);
            // 添加、更新数据
            addAndUpdatePixelMap((List<? extends Map<String, Object>>) addOrderMaps, 0);
            addAndUpdatePixelMap((List<? extends Map<String, Object>>) updateOrderMaps, 1);
        }
        // 远端FA迁移传来的状态数据，开发者可以按照特定的场景对这些数据进行处理
        this.remoteData = restoreData.getParam("continueParam").toString();
        isCrop = (boolean) restoreData.getParam(ISCROP); // 裁剪
        isScale = (boolean) restoreData.getParam(ISSCALE); // 缩放
        isMirrorFlag = (boolean) restoreData.getParam(ISMIRRORFLAG); // 镜像
        scaleX = Float.parseFloat(restoreData.getParam("scaleX").toString()); // 镜像值
        return true;
    }

    @Override
    public void onCompleteContinuation(int result) {
        isCirculation = false;
    }

    // 手机端获取平板端新增、修改的数据，并更新到内存
    private void addAndUpdatePixelMap(List<? extends Map<String, Object>> orderMaps, int flag) {
        for (Map<String, Object> map: orderMaps) {
            // 获取原始图片的下标
            int index = Integer.parseInt(map.get(INDEX).toString());
            if (flag == 0) { // 处理新增数据
                // 获取当前图片的下标
                int nowIndex = Integer.parseInt(map.get("nowIndex").toString());
                if (remoteIndex == nowIndex) {
                    isOther = true;
                }
            } else { // 处理修改数据
                if (remoteIndex == index) {
                    isOther = true;
                }
            }
            // 获取原始图片的pixelMap对象
            pixelMap = Utils.getPixelMapByIndex(index);
            imagePixelMap = pixelMap;
            image = new Image(getContext()); // 初始化image
            image.setPixelMap(pixelMap);
            image.setMarginLeft(MARGIN);
            image.setMarginRight(MARGIN);
            isCrop = (boolean) map.get("isCrop"); // 缩放指令
            isScale = (boolean) map.get("isScale"); // 裁剪指令
            isMirrorFlag = (boolean) map.get("isMirrorFlag"); // 镜像指令
            if (isCrop || isScale) { // 裁剪、缩放
                imagePixelMap = getPixelMapFromResource();
                image.setPixelMap(imagePixelMap);
            }
            if (isMirrorFlag) { // 镜像
                imagePixelMap = Utils.mirrorPixelMap(imagePixelMap, -scaleX);
            }
            pixelMap = imagePixelMap;
            if (flag == 0) { // 处理新增数据
                // 将新增的pixelMap对象添加到list中
                Utils.addPixelMap(imagePixelMap);
            } else { // 处理修改数据
                // 将新增的pixelMap对象添加到list中
                Utils.updatePixelMap(imagePixelMap, index);
            }
        }
    }
}
