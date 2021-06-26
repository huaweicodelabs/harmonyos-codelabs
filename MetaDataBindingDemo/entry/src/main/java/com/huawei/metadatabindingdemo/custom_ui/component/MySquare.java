/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.metadatabindingdemo.custom_ui.component;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.app.Context;

/**
 * This is a self defined component. This component will display a wheel.
 *
 * @since 2021-05-15
 */
public class MySquare extends Component implements Component.EstimateSizeListener, Component.DrawTask {
    private static final float CIRCLE_STROKE_WIDTH = 100f;

    /**
     * circle is green or not
     */
    private boolean isGreen = false;

    private Paint rectPaint;

    /**
     * default constructor
     *
     * @param context context
     */
    public MySquare(Context context) {
        super(context);
        init();
    }

    /**
     * must be overloaded
     *
     * @param context context
     * @param attrSet attrset from xml
     */
    public MySquare(Context context, AttrSet attrSet) {
        super(context, attrSet);
        init();
    }

    private void init() {
        setEstimateSizeListener(this);
        initPaint();
        addDrawTask(this);
    }

    private void initPaint() {
        rectPaint = new Paint();
        rectPaint.setColor(Color.BLUE);
        rectPaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        rectPaint.setStyle(Paint.Style.STROKE_STYLE);
    }

    /**
     * draw component
     *
     * @param component component
     * @param canvas canvas
     */
    @Override
    public void onDraw(Component component, Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        canvas.drawRect(left, top, left + width, top + height, rectPaint);
    }

    /**
     * Measuring this component' size.
     *
     * @param widthEstimateConfig config width:wq:wq
     *
     * @param heightEstimateConfig config height
     * @return boolean true.
     */
    @Override
    public boolean onEstimateSize(int widthEstimateConfig, int heightEstimateConfig) {
        int width = EstimateSpec.getSize(widthEstimateConfig);
        int height = EstimateSpec.getSize(heightEstimateConfig);
        setEstimatedSize(
                EstimateSpec.getChildSizeWithMode(width, width, EstimateSpec.NOT_EXCEED),
                EstimateSpec.getChildSizeWithMode(height, height, EstimateSpec.NOT_EXCEED));
        return true;
    }

    /**
     * set Circle color
     *
     * @param isGreen boolean, green or not
     */
    public void setColor(boolean isGreen) {
        this.isGreen = isGreen;
        if (isGreen) {
            rectPaint.setColor(Color.GREEN);
            invalidate();
        } else {
            rectPaint.setColor(Color.YELLOW);
            invalidate();
        }
    }
}
