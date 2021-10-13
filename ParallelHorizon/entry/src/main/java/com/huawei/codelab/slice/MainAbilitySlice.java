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
import com.huawei.codelab.component.PictureAdapter;
import com.huawei.codelab.util.Utils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.app.Context;
import ohos.media.image.PixelMap;

import java.util.List;

/**
 * main abilitySlice
 *
 * @since 2021-09-10
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final int DELAY_TIME = 10; // 延迟执行
    private static ListContainer listContainer;
    private static Context context;
    private static PictureAdapter pictureAdapter; // 图片适配器类

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        context = getContext();
        // 设置宽度
        Utils.setLeftWidth(context);
        // 计算屏幕总宽度
        Utils.calSumWidth();
        // Initializing the listContainer Component
        initListContainer();
        // Converts images in Media to List<PixelMap>. The default size is used
        Utils.transResourceIdsToListOnce(context);
        getUITaskDispatcher().delayDispatch(() -> initData(Utils.transIdToPixelMap(context)), DELAY_TIME);
    }

    // Initializing the listContainer
    private void initListContainer() {
        pictureAdapter = new PictureAdapter(null, context);
        Component component = findComponentById(ResourceTable.Id_image_list);
        if (component instanceof ListContainer) {
            listContainer = (ListContainer) component;
            listContainer.setItemProvider(pictureAdapter);
        }
    }

    /**
     * Image data in listContainer
     * The RightAbilitySlice in the parallel horizon is initialized after the RightAbilitySlice is initialized.
     * Therefore, the width of the picture in the listContainer is calculated in the RightAbilitySlice,
     * and then this method is called to set the picture.
     *
     * @param pixelListArray pixelListArray
     */
    public static void initData(List<PixelMap[]> pixelListArray) {
        context.getUITaskDispatcher().asyncDispatch(() -> {
            listContainer.setItemProvider(pictureAdapter);
            pictureAdapter.replace(pixelListArray);
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
