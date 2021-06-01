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

import com.huawei.codelab.util.GameUtils;

import ohos.agp.render.PixelMapHolder;

/**
 *  bomb
 *
 * @since 2021-03-15
 *
 */
public class Bomb extends Spirit {
    private static final int SPEED_RATE = 2;
    private static final int BOMB_Y_POSITION = 120;

    /**
     *  bomb constructor
     *
     * @param pixelMapHolder pixelMapHolder
     * @since 2021-03-15
     *
     */
    public Bomb(PixelMapHolder pixelMapHolder) {
        super(pixelMapHolder);
        this.setPlaneX(getRandom().nextInt(GameUtils.getScreenWidth() - BOMB_Y_POSITION));
        this.setPlaneY(-BOMB_Y_POSITION);
        this.setSpeed((getRandom().nextInt(SPEED_RATE) + SPEED_RATE) * SPEED_RATE);
    }
}
