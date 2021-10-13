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

package com.huawei.codelab.component;

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.RightAbility;
import com.huawei.codelab.util.Utils;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;
import ohos.bundle.ElementName;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.media.image.PixelMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * listContainer Picture list
 *
 * @since 2021-09-10
 */
public class PictureAdapter extends BaseItemProvider {
    private static final int MARGIN = 2;
    private static Context context;
    private List<PixelMap[]> list;
    private MyEventHandle myEventHandle;

    /**
     * Constructing method, initializing the image
     *
     * @param pixelMaps pixelMaps
     * @param slice slice
     * @since 2021-09-10
     */
    public PictureAdapter(List<PixelMap[]> pixelMaps, Context slice) {
        this.list = pixelMaps == null ? new ArrayList<>(0) : pixelMaps;
        context = slice;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int index) {
        return Optional.of(this.list.get(index));
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public Component getComponent(int indexArray, Component component, ComponentContainer componentContainer) {
        ViewHolder viewHolder = null;
        Component cmp = component;
        if (cmp == null) {
            cmp = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_layout,
                    null, false);
            viewHolder = new ViewHolder(); // 初始化行布局
            Component componentLayout = cmp.findComponentById(ResourceTable.Id_select_image_list);
            if (componentLayout instanceof DirectionalLayout) {
                viewHolder.directionalLayout = (DirectionalLayout) componentLayout;
            }
            cmp.setTag(viewHolder);
        } else {
            if (cmp.getTag() instanceof ViewHolder) {
                viewHolder = (ViewHolder) cmp.getTag();
            }
        }
        if (viewHolder != null) {
            viewHolder.directionalLayout.removeAllComponents();
            PixelMap[] pixelMaps = list.get(indexArray); // 获取行数据（每行可显示多张图片）
            for (int j = 0; j < pixelMaps.length; j++) { // 遍历每行图片，将图片放入viewHolder布局中
                Image image = new Image(context);
                image.setPixelMap(pixelMaps[j]);
                image.setMarginLeft(MARGIN);
                image.setMarginRight(MARGIN);
                // 获取当前图片下标
                int index = Utils.getColumns() * indexArray + j;
                image.setClickedListener(listener -> imageClick(index)); // 点击图片将此图片设置到右侧界面
                viewHolder.directionalLayout.addComponent(image); // 每行为一个线性布局，存放对应的图片
            }
        }
        return cmp;
    }

    /**
     * Elements in listContainer
     *
     * @since 2021-09-10
     */
    private static class ViewHolder {
        DirectionalLayout directionalLayout;
    }

    /**
     * Refresh the picture in the listContainer
     *
     * @param listConstructor listConstructor
     * @since 2021-09-10
     */
    public void replace(Collection<PixelMap[]> listConstructor) {
        if (listConstructor == null) {
            return;
        }
        this.list = null;
        this.list = new ArrayList<>(0); // 重新初始化listContainer中的图片
        this.list.addAll(listConstructor); // 将重新得到的图片放到listContainer中
        notifyDataChanged(); // 刷新listContainer，调用getComponent方法重新设置页面元素布局
    }

    // 点击图片
    private void imageClick(int index) {
        initHandler();
        InnerEvent event = InnerEvent.get(1, 0, index);
        myEventHandle.sendEvent(event);
    }

    private void initHandler() {
        EventRunner runner = EventRunner.getMainEventRunner();
        if (runner == null) {
            return;
        }
        myEventHandle = new MyEventHandle(runner);
    }

    /**
     *  MyEventHandle
     *
     * @since 2021-09-08
     *
     */
    public static class MyEventHandle extends EventHandler {
        MyEventHandle(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            int eventId = event.eventId;
            int index = (Integer) event.object;
            if (eventId == 1) {
                IntentParams intentParams = new IntentParams();
                intentParams.setParam("index", index); // 选择图片下标
                // Jump to RightAbility split screen
                Intent intent = new Intent();
                intent.setParams(intentParams);
                ElementName element = new ElementName("", context.getBundleName(), RightAbility.class.getName());
                intent.setElement(element);
                context.startAbility(intent, 0);
            }
        }
    }
}
