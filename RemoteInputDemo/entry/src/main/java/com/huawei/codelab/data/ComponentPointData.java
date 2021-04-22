/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.huawei.codelab.data;

/**
 * 影片实体
 *
 * @since 2021-03-03
 */
public class ComponentPointData {
    private String movieName;

    // 影片首字母名称
    private String movieFirstName;

    // 组件id
    private int componentId;

    // 组件X轴坐标
    private int pointX;

    // 组件Y轴坐标
    private int pointY;

    /**
     * 功能描述
     *
     * @since 2021-03-12
     */
    public ComponentPointData() {
    }

    /**
     * ComponentPointData
     *
     * @param movieName movieName
     * @param movieFirstName movieFirstName
     * @param pointX pointX
     * @param pointY pointY
     * @since 2021-03-03
     */
    public ComponentPointData(String movieName, String movieFirstName, int pointX, int pointY) {
        this.movieName = movieName;
        this.movieFirstName = movieFirstName;
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieFirstName() {
        return movieFirstName;
    }

    public int getPointX() {
        return pointX;
    }

    public int getPointY() {
        return pointY;
    }

    public void setPointX(int pointX) {
        this.pointX = pointX;
    }

    public void setPointY(int pointY) {
        this.pointY = pointY;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }
}
