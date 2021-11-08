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

import com.huawei.codelab.NewsAbility;
import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.distribute.api.SelectDeviceResultListener;
import com.huawei.codelab.distribute.factory.DeviceSelector;
import com.huawei.codelab.utils.CommonUtils;
import com.huawei.codelab.utils.LogUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.continuation.ExtraParams;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.distributedschedule.interwork.DeviceInfo;

/**
 * News detail slice
 *
 * @since 2020-12-04
 */
public class NewsDetailAbilitySlice extends AbilitySlice {
    public static final String INTENT_TITLE = "intent_title";
    public static final String INTENT_READ = "intent_read";
    public static final String INTENT_LIKE = "intent_like";
    public static final String INTENT_CONTENT = "intent_content";
    public static final String INTENT_IMAGE = "intent_image";
    private DependentLayout parentLayout;
    private TextField commentFocus;
    private Image iconShared;
    private DeviceSelector deviceSelector;
    private String reads;
    private String likes;
    private String title;
    private String content;
    private String image;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_news_detail_layout);
        reads = intent.getStringParam(INTENT_READ);
        likes = intent.getStringParam(INTENT_LIKE);
        title = intent.getStringParam(INTENT_TITLE);
        content = intent.getStringParam(INTENT_CONTENT);
        image = intent.getStringParam(INTENT_IMAGE);
        initView();
        initListener();
        initDistributeComponent();
    }

    private void initView() {
        parentLayout = (DependentLayout) findComponentById(ResourceTable.Id_parent_layout);
        commentFocus = (TextField) findComponentById(ResourceTable.Id_text_file);
        iconShared = (Image) findComponentById(ResourceTable.Id_button4);
        Text newsRead = (Text) findComponentById(ResourceTable.Id_read_num);
        Text newsLike = (Text) findComponentById(ResourceTable.Id_like_num);
        Text newsTitle = (Text) findComponentById(ResourceTable.Id_title_text);
        Text newsContent = (Text) findComponentById(ResourceTable.Id_title_content);
        Image newsImage = (Image) findComponentById(ResourceTable.Id_image_content);
        newsRead.setText("reads: " + reads);
        newsLike.setText("likes: " + likes);
        newsTitle.setText("Original title: " + title);
        newsContent.setText(content);
        newsImage.setPixelMap(CommonUtils.getPixelMapFromPath(this, image));
    }

    private void initListener() {
        parentLayout.setTouchEventListener(
                (component, touchEvent) -> {
                    if (commentFocus.hasFocus()) {
                        commentFocus.clearFocus();
                    }
                    return true;
                });
        iconShared.setClickedListener(component -> deviceSelector.showDistributeDevices(
                new String[]{ExtraParams.DEVICETYPE_SMART_PAD, ExtraParams.DEVICETYPE_SMART_PHONE},
                null));
    }

    private void initDistributeComponent() {
        deviceSelector = new DeviceSelector();
        deviceSelector.setup(getAbility());
        deviceSelector.setSelectDeviceResultListener(new SelectDeviceResultListener() {
            @Override
            public void onSuccess(DeviceInfo info) {
                Intent intent = new Intent();
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId(info.getDeviceId())
                        .withBundleName(getBundleName())
                        .withAbilityName(NewsAbility.class.getName())
                        .withAction("action.detail")
                        .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                        .build();
                intent.setOperation(operation);
                intent.setParam(NewsDetailAbilitySlice.INTENT_TITLE, title);
                intent.setParam(NewsDetailAbilitySlice.INTENT_READ, reads);
                intent.setParam(NewsDetailAbilitySlice.INTENT_LIKE, likes);
                intent.setParam(NewsDetailAbilitySlice.INTENT_CONTENT, content);
                intent.setParam(NewsDetailAbilitySlice.INTENT_IMAGE, image);
                startAbility(intent);
            }

            @Override
            public void onFail(DeviceInfo info) {
                LogUtils.error("cwq","onFail is called,info is "+info.getDeviceState());
            }
        });
    }
}
