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

package com.huawei.camerademo.utils;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.media.image.PixelMap;

/**
 * MyDrawTask
 *
 * @since 2021-03-08
 */
public class MyDrawTask implements Component.DrawTask {
    private static final int HALF = 2;
    private static final int STROKE_WIDTH = 3;
    private static final int MAX_SIZE = 66;
    private Component component;
    private PixelMap pixelMap;
    private RectFloat rectSrc;

    /**
     * MyDrawTask
     *
     * @param component component
     */
    public MyDrawTask(Component component) {
        this.component = component;
    }

    /**
     * putPixelMap
     *
     * @param pm Provides images in forms of pixel matrices
     */
    public void putPixelMap(PixelMap pm) {
        if (pm != null) {
            pixelMap = pm;
            int halfWidth = pixelMap.getImageInfo().size.width / HALF;
            int halfHeight = pixelMap.getImageInfo().size.height / HALF;
            int center = Math.min(halfWidth, halfHeight);
            rectSrc = new RectFloat(halfWidth - center, halfHeight - center, halfWidth + center, halfHeight + center);
            component.invalidate();
        }
    }

    @Override
    public void onDraw(Component component1, Canvas canvas) {
        int halfWidth = component.getWidth() / HALF;
        int halfHeight = component.getHeight() / HALF;
        int center = Math.min(halfWidth, halfHeight);
        if (center > MAX_SIZE) {
            center = MAX_SIZE;
        }

        int radius = center - STROKE_WIDTH / HALF;
        if (pixelMap != null) {
            canvas.drawPixelMapHolderCircleShape(new PixelMapHolder(pixelMap), rectSrc, halfWidth, halfHeight, radius);
        }

        Paint roundPaint = new Paint();
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.STROKE_STYLE);
        roundPaint.setStrokeWidth(STROKE_WIDTH);
        roundPaint.setStrokeCap(Paint.StrokeCap.ROUND_CAP);
        roundPaint.setColor(Color.WHITE);
        canvas.drawCircle(halfWidth, halfHeight, radius, roundPaint);
    }
}
