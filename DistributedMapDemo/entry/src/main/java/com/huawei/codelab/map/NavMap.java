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

package com.huawei.codelab.map;

import com.huawei.codelab.util.ImageUtils;
import com.huawei.codelab.util.ScreenUtils;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.app.Context;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.media.image.PixelMap;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * NavMap
 *
 * @since 2021-03-12
 */
public class NavMap extends Component implements Component.DrawTask, Component.TouchEventListener {
    private static final int TILE_LENGTH = 512;

    private static final int ROUTE_WIDTH = 20;

    private static final int ROUTE_COLOR_R = 61;

    private static final int ROUTE_COLOR_G = 89;

    private static final int ROUTE_COLOR_B = 171;

    private static final int COMPONENT_HALF = 2;

    private static final double COMPONENT_HALF_F = 2;

    private static final int LEFT_MARGIN = 40;

    private static final int RIGHT_MARGIN = 80;

    // 墨卡托总投影半轴长度
    private static final double OVER_LENGTH = 20037508.3427892;

    // 高德地图的缩放级别， 取值范围3~18
    private static final int ZOOM = 15;

    private double tileRealLength;

    private Point centerPoint;

    private int rowMin;

    private int rowMax;

    private int colMin;

    private int colMax;

    private float touchedDownX;

    private float touchedDownY;

    private double mapComponentWidth;

    private double mapComponentHeight;

    private List<MapTile> mapTiles;

    private List<MapElement> elements;

    private Paint paint;

    private Paint linePaint;

    private Paint grayPaint;

    private Path path = new Path();

    private Path grayPath = new Path();

    private int stepPoint;

    /**
     * 构造方法
     *
     * @param context Context
     * @param attrSet AttrSet
     */

    public NavMap(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(new Color(Color.rgb(ROUTE_COLOR_R, ROUTE_COLOR_G, ROUTE_COLOR_B)));
        paint.setStrokeWidth(ROUTE_WIDTH);
        paint.setStyle(Paint.Style.STROKE_STYLE);
        paint.setAntiAlias(true);

        grayPaint = new Paint();
        grayPaint.setColor(Color.GRAY);
        grayPaint.setStrokeWidth(ROUTE_WIDTH);
        grayPaint.setStyle(Paint.Style.STROKE_STYLE);
        grayPaint.setAntiAlias(true);

        linePaint = new Paint();
    }

    /**
     * 获取地图元素集合
     *
     * @return 类型为List
     */
    public List<MapElement> getMapElements() {
        return elements;
    }

    /**
     * 设置中心点坐标
     *
     * @param centerPoint centerPoint
     */
    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
        initMapCanvas(true);
    }

    public void setStepPoint(int stepPoint) {
        this.stepPoint = stepPoint;
    }

    /**
     * 往地图中添加地图元素
     *
     * @param mapElement 地图元素
     */
    public void addElement(MapElement mapElement) {
        if (elements == null) {
            elements = new ArrayList<>(0);
        }
        double componentX =
            (mapElement.getMercatorPoint().getPointX() - centerPoint.getPointX()) / tileRealLength * TILE_LENGTH
                + mapComponentWidth / COMPONENT_HALF;
        double componentY =
            (centerPoint.getPointY() - mapElement.getMercatorPoint().getPointY()) / tileRealLength * TILE_LENGTH
                + mapComponentHeight / COMPONENT_HALF;
        mapElement.setNowPoint(new Point((float) componentX, (float) componentY));
        mapElement.setOriginPoint(new Point((float) componentX, (float) componentY));
        elements.add(mapElement);
        invalidate();
    }

    /**
     * 初始化地图
     *
     * @param isRefresh 是否为刷新
     */
    public void initMapCanvas(boolean isRefresh) {
        // 在某个缩放级别下，瓦片的行数（列数）
        int rowCount = (int) Math.pow(COMPONENT_HALF, ZOOM);
        tileRealLength = OVER_LENGTH * COMPONENT_HALF / rowCount;
        mapComponentWidth = ScreenUtils.getScreenWidth(getContext());
        mapComponentHeight = ScreenUtils.getScreenHeight(getContext());

        double minX = centerPoint.getPointX() - mapComponentWidth / COMPONENT_HALF_F * tileRealLength / TILE_LENGTH;
        colMin = (int) Math.floor((minX + OVER_LENGTH) / tileRealLength);
        double maxX = centerPoint.getPointX() + mapComponentWidth / COMPONENT_HALF_F * tileRealLength / TILE_LENGTH;
        colMax = Math.min((int) Math.floor((maxX + OVER_LENGTH) / tileRealLength), rowCount - 1);
        double maxY = centerPoint.getPointY() + mapComponentHeight / COMPONENT_HALF_F * tileRealLength / TILE_LENGTH;
        rowMin = Math.min(rowCount - 1 - (int) Math.floor((maxY + OVER_LENGTH) / tileRealLength), rowCount - 1);
        double minY = centerPoint.getPointY() - mapComponentHeight / COMPONENT_HALF_F * tileRealLength / TILE_LENGTH;
        rowMax = rowCount - 1 - (int) Math.floor((minY + OVER_LENGTH) / tileRealLength);

        addDrawTask(this);
        setTouchEventListener(this);

        initTiles(isRefresh);
        initElement(isRefresh);
    }

    /**
     * 初始化地图瓦片集合
     *
     * @param isRefresh 是否为刷新
     */
    private void initTiles(boolean isRefresh) {
        if (mapTiles == null || isRefresh) {
            mapTiles = new CopyOnWriteArrayList<>();
        }
        mapTiles.removeIf(mapTile -> !mapTile.isInBoundary(rowMin, rowMax, colMin, colMax));

        getContext().getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(this::setTiles);
    }

    private void setTiles() {
        for (int row = rowMin; row <= rowMax; row++) {
            for (int col = colMin; col <= colMax; col++) {
                String urlString = String.format(Const.TILE_URL, col, row, ZOOM);

                if (hasThisTile(row, col)) {
                    continue;
                }

                PixelMap pixelMap = ImageUtils.getMapPixelMap(urlString);
                if (pixelMap == null) {
                    continue;
                }

                double tileX = col * tileRealLength - OVER_LENGTH;
                double tileY = OVER_LENGTH - row * tileRealLength;
                double componentX = (tileX - centerPoint.getPointX()) / tileRealLength * TILE_LENGTH
                    + mapComponentWidth / COMPONENT_HALF;
                double componentY = (centerPoint.getPointY() - tileY) / tileRealLength * TILE_LENGTH
                    + mapComponentHeight / COMPONENT_HALF;
                MapTile mapTile = new MapTile(pixelMap, (float) componentX, (float) componentY);
                mapTile.setCol(col);
                mapTile.setRow(row);
                mapTiles.add(mapTile);
                getContext().getUITaskDispatcher().asyncDispatch(this::invalidate);
            }
        }
    }

    private boolean hasThisTile(int row, int col) {
        for (MapTile mapTile : mapTiles) {
            if (mapTile.getRow() == row && mapTile.getCol() == col) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化地图元素集合
     *
     * @param isRefresh refresh
     */
    private void initElement(boolean isRefresh) {
        if (elements == null || isRefresh) {
            elements = new ArrayList<>(0);
        }
    }

    /**
     * onDraw
     *
     * @param component component
     * @param canvas canvas
     */
    @Override
    public void onDraw(Component component, Canvas canvas) {
        path.reset();
        grayPath.reset();
        drawTiles(canvas);
        drawRoutePath(canvas);
        drawImageElement(canvas);
    }

    private void drawTiles(Canvas canvas) {
        for (MapTile mapTile : mapTiles) {
            canvas.drawPixelMapHolder(mapTile, mapTile.getNowPointX(), mapTile.getNowPointY(), linePaint);
        }
    }

    private void drawRoutePath(Canvas canvas) {
        for (int i = 1; i < elements.size(); i++) {
            MapElement mapElement = elements.get(i);
            Point point = mapElement.getNowPoint();
            float pointX = point.getPointX();
            float pointY = point.getPointY();
            if (i == 1 || i == elements.size() - 1) {
                path.moveTo(pointX, pointY);
            } else {
                path.lineTo(pointX, pointY);
            }

            if (i <= stepPoint) {
                if (i == 1 || i == elements.size() - 1) {
                    grayPath.moveTo(pointX, pointY);
                } else {
                    grayPath.lineTo(pointX, pointY);
                }
            }
        }

        // 绘制导航路径（未走过的路径）
        canvas.drawPath(path, paint);

        // 绘制导航路径（已走过的路径）
        canvas.drawPath(grayPath, grayPaint);
    }

    private void drawImageElement(Canvas canvas) {
        for (MapElement element : elements) {
            if (element.isImage()) {
                PixelMapHolder pmh = new PixelMapHolder(ImageUtils.getPixelMap(getContext(), element.getActionType()));
                canvas.drawPixelMapHolder(pmh, element.getNowPointX() - LEFT_MARGIN,
                    element.getNowPointY() - RIGHT_MARGIN, new Paint());
            }
        }
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        MmiPoint point = touchEvent.getPointerPosition(touchEvent.getIndex());

        // down
        if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_DOWN) {
            touchedDownX = point.getX() - component.getContentPositionX();
            touchedDownY = point.getY() - component.getContentPositionY();
            return true;
        }

        // move
        if (touchEvent.getAction() == TouchEvent.POINT_MOVE) {
            for (MapTile mapTile : mapTiles) {
                mapTile.setNowPointX(
                    (point.getX() - component.getContentPositionX()) - touchedDownX + mapTile.getOriginX());
                mapTile.setNowPointY(
                    (point.getY() - component.getContentPositionY()) - touchedDownY + mapTile.getOriginY());
            }
            for (MapElement element : elements) {
                element.setNowPointX(
                    (point.getX() - component.getContentPositionX()) - touchedDownX + element.getOriginX());
                element.setNowPointY(
                    (point.getY() - component.getContentPositionY()) - touchedDownY + element.getOriginY());
            }
            component.invalidate();
        }

        // up
        if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_UP) {
            float newCenterPointX = (float) (centerPoint.getPointX()
                - (point.getX() - component.getContentPositionX() - touchedDownX) / TILE_LENGTH * tileRealLength);
            float newCenterPointY = (float) (centerPoint.getPointY()
                + (point.getY() - component.getContentPositionY() - touchedDownY) / TILE_LENGTH * tileRealLength);
            centerPoint = new Point(newCenterPointX, newCenterPointY);

            for (MapTile mapTile : mapTiles) {
                mapTile.setNowPointX(
                    (point.getX() - component.getContentPositionX()) - touchedDownX + mapTile.getOriginX());
                mapTile.setNowPointY(
                    (point.getY() - component.getContentPositionY()) - touchedDownY + mapTile.getOriginY());
                mapTile.setOriginX(mapTile.getNowPointX());
                mapTile.setOriginY(mapTile.getNowPointY());
            }

            for (MapElement element : elements) {
                element.setNowPointX(
                    (point.getX() - component.getContentPositionX()) - touchedDownX + element.getOriginX());
                element.setNowPointY(
                    (point.getY() - component.getContentPositionY()) - touchedDownY + element.getOriginY());
                element.setOriginX(element.getNowPointX());
                element.setOriginY(element.getNowPointY());
            }
            initMapCanvas(false);
        }
        return false;
    }
}
