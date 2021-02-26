/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.codelab.point;

import com.huawei.codelab.utils.CommonData;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * DrawPoint
 *
 * @since 2021-01-11
 */
public class DrawPoint extends Component implements Component.DrawTask {
    private static final String TAG = CommonData.TAG + DrawPoint.class.getSimpleName();
    private static final int STROKE_WIDTH = 15;
    private float[] pointsX;
    private float[] pointsY;
    private boolean[] isLastPoint;
    private Paint paint;
    private OnDrawCallBack callBack;
    private boolean isLocal;
    private List<MyPoint> localPoints = new ArrayList<>();
    private List<MyPoint> remotePoints = new ArrayList<>();

    /**
     * DrawPoint CallBack
     */
    public interface OnDrawCallBack {
        void callBack(List<MyPoint> points);
    }

    /**
     * DrawPoint
     *
     * @param context context
     * @param isLocal is local or not
     */
    public DrawPoint(Context context, boolean isLocal) {
        super(context);
        this.isLocal = isLocal;
        init();
    }

    /**
     * setDrawParams
     *
     * @param isLastPoint isLastPoint
     * @param pointsX     pointsX
     * @param pointsY     pointsY
     */
    public void setDrawParams(boolean[] isLastPoint, float[] pointsX, float[] pointsY) {
        this.pointsX = pointsX;
        this.pointsY = pointsY;
        this.isLastPoint = isLastPoint;
        invalidate();
    }


    /**
     * setOnDrawBack
     *
     * @param callBack setOnDrawBack
     */
    public void setOnDrawBack(OnDrawCallBack callBack) {
        this.callBack = callBack;
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE_STYLE);
        paint.setStrokeWidth(STROKE_WIDTH);
        addDrawTask(this);

        setTouchEventListener(
                (component, touchEvent) -> {
                    int crtX = (int) touchEvent.getPointerPosition(touchEvent.getIndex()).getX();
                    int crtY = (int) touchEvent.getPointerPosition(touchEvent.getIndex()).getY();
                    MyPoint point = new MyPoint(crtX, crtY);

                    if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_UP) {
                        point.setLastPoint(true);
                        localPoints.add(point);
                        callBack.callBack(localPoints);
                    }

                    if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_DOWN) {
                        localPoints.add(point);
                    }

                    if (touchEvent.getAction() == TouchEvent.POINT_MOVE) {
                        localPoints.add(point);
                    }

                    invalidate();
                    return true;
                });
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        remotePoints.clear();
        if (pointsX != null && pointsX.length > 1) {
            for (int i = 0; i < pointsX.length; i++) {
                float finalX = pointsX[i];
                float finalY = pointsY[i];
                boolean isLast = isLastPoint[i];
                remotePoints.add(new MyPoint(finalX, finalY, isLast));
            }
            drawPoints(canvas, remotePoints, false);
        }
        drawPoints(canvas, localPoints, true);
    }

    private void drawPoints(Canvas canvas, List<MyPoint> points, boolean isStudent) {
        if (points.size() < 1) {
            return;
        }
        if (isStudent) {
            paint.setColor(isLocal ? Color.BLACK : Color.RED);
        } else {
            paint.setColor(isLocal ? Color.RED : Color.BLACK);
        }
        Point first = null;
        Point last = null;
        for (MyPoint myPoint : points) {
            float finalX = myPoint.getPositionX();
            float finalY = myPoint.getPositionY();
            Point finalPoint = new Point(finalX, finalY);
            if (myPoint.isLastPoint()) {
                first = null;
                last = null;
                continue;
            }
            if (first == null) {
                first = finalPoint;
            } else {
                if (last != null) {
                    first = last;
                }
                last = finalPoint;
                canvas.drawLine(first, last, paint);
            }
        }
    }
}
