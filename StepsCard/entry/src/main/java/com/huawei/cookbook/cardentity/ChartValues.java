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

package com.huawei.cookbook.cardentity;

import java.util.List;

/**
 * 表单数据
 */
public class ChartValues {
    // 填充颜色
    private String fillColor;

    // 线条颜色
    private String strokeColor;

    // 设置是否显示填充渐变颜色
    private boolean gradient;

    // 设置绘制线型图中的点集
    private List<ChartPoint> data;

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public boolean getGradient() {
        return gradient;
    }

    public void setGradient(boolean gradient) {
        this.gradient = gradient;
    }

    public List<ChartPoint> getData() {
        return data;
    }

    public void setData(List<ChartPoint> data) {
        this.data = data;
    }
}
