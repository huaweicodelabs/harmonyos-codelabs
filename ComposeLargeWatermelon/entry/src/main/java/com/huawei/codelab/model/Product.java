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

package com.huawei.codelab.model;

import ohos.agp.render.PixelMapHolder;

/**
 * Graphical model
 *
 * @since 2021-04-14
 */
public class Product {
    // Graph levels 0 to 6 indicate different sizes of graphs.
    private int level;

    // Graph Radius
    private int radius;

    // X-axis coordinate
    private float centerX;

    // Y-axis coordinate
    private float centerY;

    // Speed in the x-axis direction
    private float speedX;

    // Speed in the y-axis direction
    private float speedY;

    // The image of the graphic
    private PixelMapHolder pixelMapHolder;

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public PixelMapHolder getPixelMapHolder() {
        return pixelMapHolder;
    }

    public void setPixelMapHolder(PixelMapHolder pixelMapHolder) {
        this.pixelMapHolder = pixelMapHolder;
    }
}
