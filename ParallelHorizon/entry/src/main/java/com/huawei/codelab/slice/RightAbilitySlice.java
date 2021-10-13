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
import ohos.app.dispatcher.task.TaskPriority;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.distributedhardware.devicemanager.DeviceInfo;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

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
    private static Context context;
    private static PixelMap pixelMap; // 被操作的原始图片
    private static PixelMap imagePixelMap; // 裁剪缩放镜像后的图片
    private static int idIndex; // Image ID index
    private static boolean isCorp = false; // 裁剪标识
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
    private boolean circulationFlag = false; // 是否流转中

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_right);
        context = getContext();
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
        } else {
            saveComponent.setVisibility(Component.VISIBLE);
        }
        if ("remote".equals(this.remoteData)) {
            // 设置远程图片
            setImageRemote();
            // 根据缩放、镜像标识设置图片
            setImageByFlag();
        } else {
            // 获取图片下标 设置图片
            int index = Integer.parseInt(intent.getParams().getParam("index").toString());
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
        saveComponent.setClickedListener(new ComponentClick());
        deviceComponent.setClickedListener(new ComponentClick());
    }

    /**
     * 裁剪、缩放、镜像绑定点击事件
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
                    isCorp = !isCorp;
                    isScale = false;
                    isMirror = false;
                    isMirrorFlag = false;
                    imagePixelMap = getPixelMapFromResource();
                    image.setPixelMap(imagePixelMap);
                    break;
                case ResourceTable.Id_scale_image:
                    // 缩放图片
                    isCorp = false;
                    isScale = !isScale;
                    isMirror = false;
                    isMirrorFlag = false;
                    imagePixelMap = getPixelMapFromResource();
                    image.setPixelMap(imagePixelMap);
                    break;
                case ResourceTable.Id_mirror_image:
                    // 镜像图片
                    isCorp = false;
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
                    // 打开设备选择框（链接）
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
                    canvas.scale(// 进行镜像操作
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
        if (isCorp) {
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
        isCorp = false;
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
        getGlobalTaskDispatcher(TaskPriority.DEFAULT).syncDispatch(() -> {
            // Obtaining PixelMaps from Distributed Files
            byte[] remoteByteArray = Utils.getByteArrayFromFile(context);
            // Convert byte[] into PixelMap
            pixelMap = Utils.resizePixelMap(Utils.byteArrayToPixelmap(remoteByteArray));
        });
        imagePixelMap = pixelMap;
        image.setPixelMap(pixelMap);
        image.setMarginLeft(MARGIN);
        image.setMarginRight(MARGIN);
    }

    // 根据缩放、镜像标识设置图片
    private void setImageByFlag() {
        if (isScale) {
            imagePixelMap = getPixelMapFromResource();
            image.setPixelMap(imagePixelMap);
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
        if (!isCorp && !isScale && !isMirrorFlag) {
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
        if (flag == 0) {
            // 新增
            Utils.addPixelMap(imagePixelMap);
        } else {
            // 替换
            Utils.updatePixelMap(imagePixelMap, idIndex);
        }
        // Refresh the listContainer data
        MainAbilitySlice.initData(Utils.transIdToPixelMap(context));
        // 重置状态
        isCorp = false;
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
        if (circulationFlag) {
            Utils.creatToastDialog(context, "正在流转，请稍后再试！");
            return;
        }
        circulationFlag = true;
        if (selectDeviceId == null || "".equals(selectDeviceId)) {
            // 选择设备以后才能流转（迁移）
            Utils.creatToastDialog(context, "请选择链接设备后进行流转操作！");
            return;
        }
        getGlobalTaskDispatcher(TaskPriority.DEFAULT).syncDispatch(() -> {
            // Convert PixelMap to byte[] and save it to a distributed file
            byte[] bytes = Utils.pixelMapToByteArray(imagePixelMap);
            Utils.saveByteArrayToFile(context, bytes);
        });
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
        saveData.setParam("isCorp", isCorp); // 裁剪
        saveData.setParam("isScale", isScale); // 缩放
        saveData.setParam("isMirrorFlag", isMirrorFlag); // 镜像
        saveData.setParam("scaleX", scaleX); // 镜像值
        saveData.setParam("remoteIndex", idIndex); // 当前图片下标
        return true;
    }

    @Override
    public boolean onRestoreData(IntentParams restoreData) {
        // 远端FA迁移传来的状态数据，开发者可以按照特定的场景对这些数据进行处理
        this.remoteData = restoreData.getParam("continueParam").toString();
        isCorp = (boolean) restoreData.getParam("isCorp"); // 裁剪
        isScale = (boolean) restoreData.getParam("isScale"); // 缩放
        isMirrorFlag = (boolean) restoreData.getParam("isMirrorFlag"); // 镜像
        scaleX = Float.parseFloat(restoreData.getParam("scaleX").toString()); // 镜像值
        this.remoteIndex = Integer.parseInt(restoreData.getParam("remoteIndex").toString());
        return true;
    }

    @Override
    public void onCompleteContinuation(int result) {
        circulationFlag = false;
    }
}
