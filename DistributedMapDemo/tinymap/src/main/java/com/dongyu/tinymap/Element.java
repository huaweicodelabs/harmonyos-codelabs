/*
 * Copyright (c) 2021 dongyu Co., Ltd.
 *
 * Copyright (c) 2021.9.24 Added some variables and methods..
 *                         Huawei Device Co., Ltd.
 */

package com.dongyu.tinymap;

import ohos.agp.utils.Point;

/**
 * MapElement地图元素类，用于封装导航路径上的点和图标元素
 *
 * @since 2021-02-01
 */
public class Element {
    // 元素的墨卡托影坐标
    private Point mercatorPoint = new Point(0, 0);

    // 元素在屏幕上的坐标（原始）
    private Point originPoint = new Point(0, 0);

    // 元素在屏幕上的坐标（拖动后）
    private Point nowPoint = new Point(0, 0);

    private String actionType;

    private String actionContent;

    private boolean isImage = false;

    /**
     * MapElement
     *
     * @param mercatorX 墨卡托影X轴坐标
     * @param mercatorY 墨卡托影y轴坐标
     * @since 2021-03-12
     */
    public Element(float mercatorX, float mercatorY) {
        mercatorPoint = new Point(mercatorX, mercatorY);
    }

    /**
     * MapElement
     *
     * @param mercatorX 墨卡托影X轴坐标
     * @param mercatorY 墨卡托影y轴坐标
     * @param isImage   元素是否为图片
     * @since 2021-03-12
     */
    public Element(float mercatorX, float mercatorY, boolean isImage) {
        mercatorPoint = new Point(mercatorX, mercatorY);
        this.isImage = isImage;
    }

    public boolean isImage() {
        return isImage;
    }

    public String getActionContent() {
        return actionContent;
    }

    public void setActionContent(String actionContent) {
        this.actionContent = actionContent;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    /**
     * 获取元素当前x轴坐标
     *
     * @return float类型
     */
    public float getNowPointX() {
        return nowPoint.getPointX();
    }

    /**
     * 设置元素当前x轴坐标
     *
     * @param pointX 当前x轴坐标
     */
    public void setNowPointX(float pointX) {
        nowPoint = new Point(pointX, nowPoint.getPointY());
    }

    /**
     * 获取元素当前y轴坐标
     *
     * @return float类型
     */
    public float getNowPointY() {
        return nowPoint.getPointY();
    }

    /**
     * 设置元素当前y轴坐标
     *
     * @param pointY 元素当前y轴坐标
     */
    public void setNowPointY(float pointY) {
        nowPoint = new Point(nowPoint.getPointX(), pointY);
    }

    /**
     * 获取元素x轴坐标
     *
     * @return float类型
     */
    public float getOriginX() {
        return originPoint.getPointX();
    }

    /**
     * 设置元素x轴坐标
     *
     * @param pointX x-axis
     */
    public void setOriginX(float pointX) {
        originPoint = new Point(pointX, originPoint.getPointY());
    }

    /**
     * 获取元素y轴坐标
     *
     * @return float类型
     */
    public float getOriginY() {
        return originPoint.getPointY();
    }

    /**
     * 设置元素y轴坐标
     *
     * @param pointY 元素y轴坐标
     */
    public void setOriginY(float pointY) {
        originPoint = new Point(originPoint.getPointX(), pointY);
    }

    public Point getMercatorPoint() {
        return mercatorPoint;
    }

    public void setMercatorPoint(Point mercatorPoint) {
        this.mercatorPoint = mercatorPoint;
    }

    public Point getOriginPoint() {
        return originPoint;
    }

    public void setOriginPoint(Point originPoint) {
        this.originPoint = originPoint;
    }

    public Point getNowPoint() {
        return nowPoint;
    }

    public void setNowPoint(Point nowPoint) {
        this.nowPoint = nowPoint;
    }
}
