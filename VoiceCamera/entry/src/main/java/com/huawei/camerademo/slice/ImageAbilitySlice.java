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

package com.huawei.camerademo.slice;

import static com.huawei.camerademo.utils.CameraUtil.CAMERA_IMAGE_FILE_PATH;

import com.huawei.camerademo.ResourceTable;
import com.huawei.camerademo.utils.CameraUtil;
import com.huawei.camerademo.utils.DistributeFileUtil;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Image;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;

/**
 * ImageAbilitySlice
 *
 * @since 2021-03-08
 */
public class ImageAbilitySlice extends AbilitySlice {
    static final HiLogLabel TAG = new HiLogLabel(HiLog.LOG_APP, 0x00201, ImageAbilitySlice.class.getName());
    private Image image;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_image);
        initView(intent);
    }

    private void initView(Intent intent) {
        HiLog.info(TAG, "initView");
        if (findComponentById(ResourceTable.Id_image) instanceof Image) {
            image = (Image) findComponentById(ResourceTable.Id_image);
        }
        if (intent.getStringParam(CAMERA_IMAGE_FILE_PATH) != null
                && !intent.getStringParam(CAMERA_IMAGE_FILE_PATH).isEmpty()) {
            setImage(intent.getStringParam(CAMERA_IMAGE_FILE_PATH));
        } else {
            setDisImage(intent);
        }
    }

    /**
     * read image form distribute
     *
     * @param intent intent
     */
    private void setDisImage(Intent intent) {
        String filePath = intent.getStringParam("filePath");
        if (filePath != null && !filePath.isEmpty()) {
            image.setPixelMap(DistributeFileUtil.readToDistributedDir(this, filePath));
        }
    }

    private void setImage(String picPath) {
        if (picPath != null && !picPath.isEmpty()) {
            PixelMap pixelMap = CameraUtil.getPixelMap(null, picPath, 1);
            image.setPixelMap(pixelMap);
        }
    }
}
