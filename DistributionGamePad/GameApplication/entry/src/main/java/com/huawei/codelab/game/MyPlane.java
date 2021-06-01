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

package com.huawei.codelab.game;

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;

/**
 *  myPlane
 *
 * @since 2021-03-15
 *
 */
public class MyPlane extends Spirit {
    private static final int SPEED = 30;
    private String deviceId; // 飞机id
    private int score; // 分数
    private int index; // 飞机下标，目前两架飞机，取值0、1
    private int bombNum; // 炸弹数量

    /**
     *  myPlane constructor
     *
     * @param pixelMapHolder pixelMapHolder
     * @param planeX planeX
     * @param planeY planeY
     * @since 2021-03-15
     *
     */
    public MyPlane(PixelMapHolder pixelMapHolder, int planeX, int planeY) {
        super(pixelMapHolder);
        this.setPlaneX(planeX);
        this.setPlaneY(planeY);
        this.setSpeed(SPEED);
        this.setScore(0);
        this.setBombNum(0);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getBombNum() {
        return bombNum;
    }

    public void setBombNum(int bombNum) {
        this.bombNum = bombNum;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawPixelMapHolder(getPixelMapHolder(), getPlaneX(), getPlaneY(), paint);
    }
}
