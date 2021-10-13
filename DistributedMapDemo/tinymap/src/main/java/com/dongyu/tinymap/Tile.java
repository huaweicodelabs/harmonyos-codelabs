/*
 * Copyright (c) 2021 dongyu Co., Ltd.
 *
 * Copyright (c) 2021.9.24 Modified method name and added comments
 *                         Huawei Device Co., Ltd.
 */

package com.dongyu.tinymap;

import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Point;
import ohos.media.image.PixelMap;

/**
 * Tile
 *
 * @since 2021-03-12
 */
public class Tile extends PixelMapHolder {
    private Point originPoint = new Point(0, 0);

    private Point nowPoint = new Point(0, 0);

    private int row;

    private int col;

    /**
     * MapElement
     *
     * @param pixelMap pixelMap
     * @param pointX pointX
     * @param pointY pointY
     * @since 2021-03-12
     */
    public Tile(PixelMap pixelMap, float pointX, float pointY) {
        super(pixelMap);
        originPoint = new Point(pointX, pointY);
        nowPoint = new Point(pointX, pointY);
    }

    /**
     * 获取元素当前x轴坐标
     *
     * @return 类型为float
     */
    public float getNowPointX() {
        return nowPoint.getPointX();
    }

    /**
     * 设置元素当前x轴坐标
     *
     * @param pointX pointX
     */
    public void setNowPointX(float pointX) {
        nowPoint = new Point(pointX, nowPoint.getPointY());
    }

    /**
     * 获取元素当前y轴坐标
     *
     * @return 类型为float
     */
    public float getNowPointY() {
        return nowPoint.getPointY();
    }

    /**
     * 设置元素当前y轴坐标
     *
     * @param pointY pointY
     */
    public void setNowPointY(float pointY) {
        nowPoint = new Point(nowPoint.getPointX(), pointY);
    }

    /**
     * 获取元素x轴坐标
     *
     * @return 类型为float
     */
    public float getOriginX() {
        return originPoint.getPointX();
    }

    /**
     * 设置元素x轴坐标
     *
     * @param pointX pointX
     */
    public void setOriginX(float pointX) {
        originPoint = new Point(pointX, originPoint.getPointY());
    }

    /**
     * 获取元素y轴坐标
     *
     * @return 类型为float
     */
    public float getOriginY() {
        return originPoint.getPointY();
    }

    /**
     * 设置元素y轴坐标
     *
     * @param pointY 类型为float
     */
    public void setOriginY(float pointY) {
        originPoint = new Point(originPoint.getPointX(), pointY);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    /**
     * isInBoundary
     *
     * @param rowMin rowMin
     * @param rowMax rowMax
     * @param colMin colMin
     * @param colMax colMax
     * @return isInBoundary
     */
    public boolean isInBoundary(int rowMin, int rowMax, int colMin, int colMax) {
        if (row < rowMin || row > rowMax || col < colMin || col > colMax) {
            return false;
        }
        return true;
    }
}
