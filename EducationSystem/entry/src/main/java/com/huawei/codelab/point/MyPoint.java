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

package com.huawei.codelab.point;

/**
 * MyPoint
 *
 * @since 2021-01-11
 */
public class MyPoint extends ohos.agp.utils.Point {
    private float positionX;

    private float positionY;

    private boolean isLastPoint = false;

    /**
     * MyPoint
     *
     * @param positionX x
     * @param positionY y
     */
    public MyPoint(float positionX, float positionY) {
        super();
        this.positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * 构造函数
     *
     * @param positionX x
     * @param positionY y
     * @param isLastPoint isLastPoint
     */
    public MyPoint(float positionX, float positionY, boolean isLastPoint) {
        super();
        this.positionX = positionX;
        this.positionY = positionY;
        this.isLastPoint = isLastPoint;
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

    public void setLastPoint(boolean isLast) {
        isLastPoint = isLast;
    }
}
