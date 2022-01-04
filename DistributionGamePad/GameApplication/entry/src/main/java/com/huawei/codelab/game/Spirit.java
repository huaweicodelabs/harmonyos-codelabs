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

import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.RectFloat;

import java.security.SecureRandom;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 *  spirit
 *
 * @since 2021-03-15
 *
 */
public abstract class Spirit {
    private final SecureRandom random = new SecureRandom();
    private final Optional<Spirit> optionalEmpty = Optional.empty();
    private PixelMapHolder pixelMapHolder; // 图片
    private int planeX; // x坐标
    private int planeY; // y坐标
    private final int width; // 宽
    private final int height; // 高
    private int speed; // 速度
    private boolean isDestroyed; // 是否被销毁

    /**
     *  spirit constructor
     *
     * @param pixelMapHolder pixelMapHolder
     * @since 2021-03-15
     *
     */
    public Spirit(PixelMapHolder pixelMapHolder) {
        this.pixelMapHolder = pixelMapHolder;
        this.isDestroyed = false;
        this.width = pixelMapHolder.getPixelMap().getImageInfo().size.width;
        this.height = pixelMapHolder.getPixelMap().getImageInfo().size.height;
    }

    public SecureRandom getRandom() {
        return random;
    }

    public PixelMapHolder getPixelMapHolder() {
        return pixelMapHolder;
    }

    public int getPlaneX() {
        return planeX;
    }

    public void setPlaneX(int planeX) {
        this.planeX = planeX;
    }

    public int getPlaneY() {
        return planeY;
    }

    public void setPlaneY(int planeY) {
        this.planeY = planeY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     *  destroy
     *
     * @since 2021-03-15
     *
     */
    public void destroy() {
        pixelMapHolder = null;
        isDestroyed = true;
    }

    /**
     *  collideWithOther
     *
     * @param spirits spirits
     * @return true
     *
     */
    public Spirit collideWithOther(List<? extends Spirit> spirits) {
        Iterator<? extends Spirit> iterator = spirits.iterator();
        while (iterator.hasNext()) {
            Spirit spirit = iterator.next();
            RectFloat r1 = new RectFloat(spirit.getPlaneX(), spirit.getPlaneY(),
                    spirit.getPlaneX() + spirit.getWidth(),
                    spirit.getPlaneY() + spirit.getHeight());
            RectFloat r2 = new RectFloat(planeX, planeY, planeX + width,
                    planeY + height);
            if (r1.getIntersectRect(r2)) { // 碰撞
                return spirit;
            }
        }
        return optionalEmpty.orElse(null);
    }

    /**
     *  draw
     *
     * @param canvas canvas
     * @param paint paint
     *
     */
    public void draw(Canvas canvas, Paint paint) {
        beforeOndraw();
        onDraw(canvas, paint);
        afterDraw(canvas, paint);
    }

    /**
     *  before draw
     *
     * @since 2021-03-15
     *
     */
    public void beforeOndraw() {
        move();
    }

    /**
     *  on draw
     *
     * @param canvas canvas
     * @param paint paint
     *
     */
    public void onDraw(Canvas canvas, Paint paint) {
        canvas.drawPixelMapHolder(pixelMapHolder, planeX, planeY, paint);
    }

    /**
     *  after draw
     *
     * @param canvas canvas
     * @param paint paint
     *
     */
    public void afterDraw(Canvas canvas, Paint paint) {
        if (GameUtils.getScreenHeight() < planeY) {
            destroy();
        }
    }

    /**
     *  myPlane
     *
     * @since 2021-03-15
     *
     */
    public void move() {
        planeY += speed;
    }
}
