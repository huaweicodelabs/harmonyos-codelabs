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
 *  bullet
 *
 * @since 2021-03-15
 *
 */
public class Bullet extends Spirit {
    private static final int BULLET_X_OFFSET = 85;
    private static final int BULLET_Y_OFFSET = 10;
    private static final int BULLET_SPEED = -20;
    private String deviceId; // 设备id

    /**
     *  bullet constructor
     *
     * @param pixelMapHolder pixelMapHolder
     * @param planeX planeX
     * @param planeY planeY
     * @since 2021-03-15
     *
     */
    public Bullet(PixelMapHolder pixelMapHolder, int planeX, int planeY) {
        super(pixelMapHolder);
        this.setPlaneX(planeX + BULLET_X_OFFSET);
        this.setPlaneY(planeY - BULLET_Y_OFFSET);
        this.setSpeed(BULLET_SPEED);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public void afterDraw(Canvas canvas, Paint paint) {
        if (getPlaneY() < 0) {
            destroy();
        }
    }
}
