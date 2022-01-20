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

package com.huawei.harmonyaudiodemo.component;

import com.huawei.harmonyaudiodemo.ResourceTable;
import com.huawei.harmonyaudiodemo.util.ResourceUtils;

import ohos.agp.animation.AnimatorProperty;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.LayoutAlignment;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WaveView
 *
 * @since 2021-04-04
 */
public class WaveView extends DirectionalLayout {
    private static final int WAVE_ITEM_TOTAL = 10;
    private static final int MARGIN_LEFT = 10;
    private static final int PADDING_RIGHT = 10;
    private static final int PADDING_TOP = 30;
    private static final int FOUR_TIME = 4;
    private int currentWaveItemPos = 0;
    private final List<AnimatorProperty> animatorProperties;
    private final ExecutorService cachedThreadPool;

    /**
     * constructor of WaveView
     *
     * @param context context
     */
    public WaveView(Context context) {
        this(context, null);
    }

    /**
     * constructor of WaveView
     *
     * @param context context
     * @param attrSet attrSet
     */
    public WaveView(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    /**
     * constructor of WaveView
     *
     * @param context context
     * @param attrSet attrSet
     * @param styleName styleName
     */
    public WaveView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        animatorProperties = new ArrayList<>(0);
        cachedThreadPool = Executors.newCachedThreadPool();
        initView();
    }

    private void initView() {
        setOrientation(DirectionalLayout.HORIZONTAL);
        setAlignment(LayoutAlignment.BOTTOM);
        setPaddingRight(PADDING_RIGHT);
        setPaddingTop(PADDING_TOP);
        for (int i = 0; i < WAVE_ITEM_TOTAL; i++) {
            Component component = new Component(mContext);
            component.setId(i);
            LayoutConfig layoutConfig = new LayoutConfig(0, getHeight() / FOUR_TIME);
            layoutConfig.weight = 1;
            layoutConfig.setMarginLeft(MARGIN_LEFT);
            component.setLayoutConfig(layoutConfig);
            ShapeElement element = new ShapeElement();
            element.setRgbColor(RgbColor.fromArgbInt(ResourceUtils.getColor(mContext, ResourceTable.Color_white)));
            component.setBackground(element);
            addComponent(component);
            AnimatorProperty animatorProperty = component.createAnimatorProperty();
            animatorProperties.add(animatorProperty);
        }
    }

    /**
     * start of WaveView
     *
     * @param value value
     * @param time time
     */
    public void start(int value, long time) {
        cachedThreadPool.execute(() -> {
            AnimatorProperty animatorProperty = animatorProperties.get(currentWaveItemPos);
            animatorProperty.scaleY(value <= 0 ? 1 : value).setDuration(time).start();
        });
        currentWaveItemPos++;
        currentWaveItemPos = currentWaveItemPos == WAVE_ITEM_TOTAL ? 0 : currentWaveItemPos;
    }

    /**
     * stop of WaveView
     */
    public void stop() {
        for (AnimatorProperty animatorProperty : animatorProperties) {
            animatorProperty.stop();
            animatorProperty.cancel();
        }
    }
}
