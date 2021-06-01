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
 *  explosion
 *
 * @since 2021-03-15
 *
 */
public class Explosion extends Spirit {
    private static final int CLEAN_DESTROY_FRAME = 8; // 清除失效飞机帧数

    /**
     *  explosion
     *
     * @param pixelMapHolder pixelMapHolder
     * @param planeX planeX
     * @param planeY planeY
     * @since 2021-03-15
     *
     */
    public Explosion(PixelMapHolder pixelMapHolder, int planeX, int planeY) {
        super(pixelMapHolder);
        this.setPlaneX(planeX);
        this.setPlaneY(planeY);
        this.setSpeed(0);
    }

    @Override
    public void afterDraw(Canvas canvas, Paint paint) {
        if (0 == GameView.getFrame() % CLEAN_DESTROY_FRAME) {
            destroy();
        }
    }
}
