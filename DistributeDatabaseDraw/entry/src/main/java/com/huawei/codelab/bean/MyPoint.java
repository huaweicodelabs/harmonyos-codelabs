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

package com.huawei.codelab.bean;

import ohos.agp.utils.Color;

/**
 * point entity
 *
 * @since 2021-04-06
 */
public class MyPoint extends ohos.agp.utils.Point {
    private final float positionX;
    private final float positionY;
    private boolean isLastPoint = false;
    private Color paintColor;

    /**
     * constructor
     *
     * @param positionX x
     * @param positionY y
     */
    public MyPoint(float positionX, float positionY) {
        super();
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public boolean isLastPoint() {
        return isLastPoint;
    }

    public void setLastPoint(boolean lastPoint) {
        isLastPoint = lastPoint;
    }

    public Color getPaintColor() {
        return paintColor;
    }

    public void setPaintColor(Color paintColor) {
        this.paintColor = paintColor;
    }
}
