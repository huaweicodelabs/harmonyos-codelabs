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

package com.huawei.codelab.component;

import com.huawei.codelab.bean.MyPoint;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Drawl
 *
 * @since 2021-04-06
 */
public class DrawPoint extends Component implements Component.DrawTask {
    private static final String TAG = "DrawPoint";
    private static final int STROKE_WIDTH = 15;
    private static final int TIME = 200;
    private static final int ITEM_HEIGHT = 200;
    private static final int EVENT_MSG_STORE = 0x1000002;
    private List<MyPoint> points = new ArrayList<>();
    private Paint paint;
    private OnDrawCallBack callBack;
    private Color[] paintColors = new Color[]{Color.RED, Color.BLUE, Color.BLACK};
    private Timer timer = null;
    private TimerTask timerTask = null;

    private EventHandler handler = new EventHandler(EventRunner.current()) {
        @Override
        protected void processEvent(InnerEvent event) {
            switch (event.eventId) {
                case EVENT_MSG_STORE:
                    callBack.callBack(points);
                    break;
                default:
                    break;
            }
        }
    };

    public List<MyPoint> getPoints() {
        return points;
    }

    /**
     * SelectResult
     *
     * @since 2020-12-03
     */
    public interface OnDrawCallBack {
        /**
         * touchListener
         *
         * @param points points
         */
        void callBack(List<MyPoint> points);
    }

    /**
     * Drawl constructor
     *
     * @param context context
     * @param colorIndex colorIndex
     */
    public DrawPoint(Context context, int colorIndex) {
        super(context);
        init(colorIndex);
    }

    /**
     * setPoints
     *
     * @param points points
     */
    public void setDrawParams(List<MyPoint> points) {
        this.points = points;
        invalidate();
    }

    /**
     * setOnDrawBack
     *
     * @param callBack callBack
     */
    public void setOnDrawBack(OnDrawCallBack callBack) {
        this.callBack = callBack;
    }

    private void init(int colorIndex) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE_STYLE);
        paint.setStrokeWidth(STROKE_WIDTH);
        addDrawTask(this);
        Color color = getRandomColor(colorIndex);

        setTouchEventListener((component, touchEvent) -> {
            scheduledTask();
            int crtX = (int) touchEvent.getPointerPosition(touchEvent.getIndex()).getX();
            int crtY = (int) touchEvent.getPointerPosition(touchEvent.getIndex()).getY() - ITEM_HEIGHT;

            MyPoint point = new MyPoint(crtX, crtY);
            point.setPaintColor(color);

            switch (touchEvent.getAction()) {
                case TouchEvent.POINT_MOVE:
                    points.add(point);
                    break;
                case TouchEvent.PRIMARY_POINT_UP:
                    points.add(point);
                    point.setLastPoint(true);
                    callBack.callBack(points);
                    onTimerFinish();
                    break;
                default:
                    break;
            }
            invalidate();
            return true;
        });
    }

    /**
     * scheduled task start
     */
    public void scheduledTask() {
        if (timer == null && timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEvent(EVENT_MSG_STORE);
                }
            };
            timer.schedule(timerTask, 0, TIME);
        }
    }

    /**
     * Canceling a Scheduled Task
     */
    public void onTimerFinish() {
        timer.cancel();
        timer = null;
        timerTask = null;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        draw(points, canvas);
    }

    private void draw(List<MyPoint> points, Canvas canvas) {
        if (points == null || points.size() <= 1) {
            return;
        }
        Point first = null;
        Point last = null;
        for (MyPoint myPoint : points) {
            paint.setColor(myPoint.getPaintColor());
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

    private Color getRandomColor(int index) {
        return index > paintColors.length - 1 ? paintColors[0] : paintColors[index];
    }
}