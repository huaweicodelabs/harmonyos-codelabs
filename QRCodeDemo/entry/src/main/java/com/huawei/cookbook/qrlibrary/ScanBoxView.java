/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.huawei.cookbook.qrlibrary;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;

import ohos.media.image.PixelMap;

import com.huawei.cookbook.qrlibrary.util.PixelMapUtil;
import com.huawei.cookbook.qrlibrary.util.QRCodeUtil;

/**
 * Preview Camera .
 *
 * @since 2021-05-25
 */
public class ScanBoxView extends Component implements Component.DrawTask {
    /**
     * number 3.
     */
    private static final int NUMBER_3 = 3;
    /**
     * number 20.
     */
    private static final int NUMBER_20 = 20;
    /**
     * number 90.
     */
    private static final int NUMBER_90 = 90;
    /**
     * number 200.
     */
    private static final int NUMBER_200 = 200;
    /**
     * diff 0.5.
     */
    private static final float NUMBER_0_5 = 0.5f;
    /**
     * RES_PIX_LINE.
     */
    private static final String LINE =
            "entry/resources/base/media/line_broad.png";
    /**
     * RES_PIX_GRID.
     */
    private static final String GRID =
            "entry/resources/base/media/line_grid.png";
    /**
     * moveStepDistance.
     */
    private int moveStepDistance;
    /**
     * framingRect.
     */
    private Rect framingRect;
    /**
     * scanLineTop.
     */
    private float scanLineTop;
    /**
     * paint.
     */
    private Paint paint;
    /**
     * maskColor.
     */
    private int maskColor;
    /**
     * cornerColor.
     */
    private final int cornerColor;
    /**
     * cornerLength.
     */
    private final int cornerLength;
    /**
     * cornerSize.
     */
    private final int cornerSize;
    /**
     * rectWidth.
     */
    private final int rectWidth;
    /**
     * rectHeight.
     */
    private int rectHeight;
    /**
     * topOffset.
     */
    private int topOffset;
    /**
     * scanLineSize.
     */
    private int scanLineSize;
    /**
     * scanLineColor.
     */
    private final int scanLineColor;
    /**
     * scanLineMargin.
     */
    private final int scanLineMargin;
    /**
     * isShowDefaultScanLineDrawable.
     */
    private final boolean isShowDefaultScanLineDrawable;
    /**
     * scanLineBitmap.
     */
    private PixelMap scanLineBitmap;
    /**
     * borderSize.
     */
    private final int borderSize;
    /**
     * borderColor.
     */
    private final int borderColor;
    /**
     * verticalBias.
     */
    private final float verticalBias;
    /**
     * cornerDisplayType.
     */
    private final int cornerDisplayType;
    /**
     * toolbarHeight.
     */
    private final int toolbarHeight;
    /**
     * isScanLineReverse.
     */
    private final boolean isScanLineReverse;
    /**
     * isShowDefaultGridScanLineDrawable.
     */
    private final boolean isShowDefaultGridScanLineDrawable;
    /**
     * gridLineMap.
     */
    private PixelMap gridLineMap;
    /**
     * gridScanLineBottom.
     */
    private float gridScanLineBottom;
    /**
     * originQRCodeScanLineBitmap.
     */
    private PixelMap originQRCodeScanLineBitmap;
    /**
     * originQRCodegridLineMap.
     */
    private PixelMap originQRCodegridLineMap;
    /**
     * originBarCodegridLineMap.
     */
    private PixelMap originBarCodegridLineMap;
    /**
     * halfCornerSize.
     */
    private float halfCornerSize;
    /**
     * sizeChange.
     */
    private boolean sizeChange;

    /**
     * 构造函数.
     *
     * @param context 上下文
     */
    public ScanBoxView(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        maskColor = Color.getIntColor("#33FFFFFF");
        this.addDrawTask(this);

        paint = new Paint();
        paint.setAntiAlias(true);
        maskColor = Color.getIntColor("#33FFFFFF");
        cornerColor = Color.getIntColor("#8800FF00");
        cornerLength = QRCodeUtil.dp2px(context, NUMBER_20);
        cornerSize = QRCodeUtil.dp2px(context, NUMBER_3);
        scanLineSize = QRCodeUtil.dp2px(context, 1);
        scanLineColor = Color.getIntColor("#8800FF00");
        topOffset = QRCodeUtil.dp2px(context, NUMBER_90);
        rectWidth = QRCodeUtil.dp2px(context, NUMBER_200);
        rectHeight = QRCodeUtil.dp2px(context, NUMBER_200);
        scanLineMargin = 0;
        isShowDefaultScanLineDrawable = false;

        scanLineBitmap = null;
        borderSize = QRCodeUtil.dp2px(context, 1);
        borderColor = Color.WHITE.getValue();
        verticalBias = -1;
        cornerDisplayType = 1;
        toolbarHeight = 0;
        moveStepDistance = QRCodeUtil.dp2px(context, 2);
        isScanLineReverse = false;
        isShowDefaultGridScanLineDrawable = false;
        sizeChange = true;
        afterInitCustomAttrs();
    }

    private void afterInitCustomAttrs() {
        if (originQRCodegridLineMap == null) {
            originQRCodegridLineMap =
                    PixelMapUtil.getPixelMapByResPath(getContext(), GRID);
            originQRCodegridLineMap =
                    PixelMapUtil.makeTintBitmap(originQRCodegridLineMap,
                            scanLineColor);
        }
        originBarCodegridLineMap =
                PixelMapUtil.adjustRotation(originQRCodegridLineMap,
                        NUMBER_90);
        originBarCodegridLineMap =
                PixelMapUtil.adjustRotation(originBarCodegridLineMap,
                        NUMBER_90);
        originBarCodegridLineMap =
                PixelMapUtil.adjustRotation(originBarCodegridLineMap,
                        NUMBER_90);

        if (originQRCodeScanLineBitmap == null) {
            originQRCodeScanLineBitmap =
                    PixelMapUtil.getPixelMapByResPath(getContext(), LINE);
            originQRCodeScanLineBitmap =
                    PixelMapUtil.makeTintBitmap(originQRCodeScanLineBitmap,
                            scanLineColor);
        }

        topOffset += toolbarHeight;
        halfCornerSize = 1.0f * cornerSize / 2;

        refreshScanBox();
    }


    private void onSizeChanged() {
        if (!sizeChange) {
            return;
        }
        sizeChange = false;
        calFramingRect();
    }

    /**
     * onDraw.
     * @param component component
     * @param canvas canvas
     */
    @Override
    public void onDraw(Component component, Canvas canvas) {
        // 初始化大小
        onSizeChanged();

        // 画遮罩层
        drawMask(component, canvas);

        // 画边框线
        drawBorderLine(canvas);

        // 画四个直角的线
        drawCornerLine(canvas);

        // 画扫描线
        drawScanLine(canvas);

        // 移动扫描线的位置
        moveScanLine();
    }


    private void calFramingRect() {
        int leftOffset = (getWidth() - rectWidth) / 2;
        framingRect = new Rect(leftOffset, topOffset,
                leftOffset + rectWidth, topOffset + rectHeight);

        scanLineTop = framingRect.top + halfCornerSize + NUMBER_0_5;
        gridScanLineBottom = scanLineTop;
    }

    /**
     * 画遮罩层.
     *
     * @param component 当前组件
     * @param canvas    画布
     */
    private void drawMask(Component component, Canvas canvas) {
        int width = component.getWidth();

        if (maskColor != Color.TRANSPARENT.getValue()) {
            paint.setStyle(Paint.Style.FILL_STYLE);
            paint.setColor(new Color(maskColor));
            canvas.drawRect(new RectFloat(0, 0, width, framingRect.top), paint);
            canvas.drawRect(new RectFloat(0, framingRect.top,
                    framingRect.left, framingRect.bottom + 1), paint);
            canvas.drawRect(new RectFloat(framingRect.right + 1,
                    framingRect.top, width, framingRect.bottom + 1), paint);
            canvas.drawRect(new RectFloat(0, framingRect.bottom + 1,
                    width, component.getHeight()), paint);
        }
    }

    /**
     * 画边框线.
     *
     * @param canvas 画布
     */
    private void drawBorderLine(Canvas canvas) {
        if (borderSize > 0) {
            paint.setStyle(Paint.Style.STROKE_STYLE);
            paint.setColor(new Color(borderColor));
            paint.setStrokeWidth(borderSize);
            canvas.drawRect(QRCodeUtil.trasnsRectFfromRect(framingRect), paint);
        }
    }

    /**
     * 画四个直角的线.
     *
     * @param canvas 画布
     */
    private void drawCornerLine(Canvas canvas) {
        if (halfCornerSize > 0) {
            paint.setStyle(Paint.Style.STROKE_STYLE);
            paint.setColor(new Color(cornerColor));
            paint.setStrokeWidth(cornerSize);
            switch (cornerDisplayType) {
                case 1:
                    drawCornerLineOne(canvas);
                    break;
                case 2:
                    drawCornerLineTwo(canvas);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 画扫描线.
     *
     * @param canvas 画布
     */
    private void drawScanLine(Canvas canvas) {
        if (gridLineMap != null) {
            RectFloat dstGridRectF = new RectFloat(
                    framingRect.left + halfCornerSize + scanLineMargin,
                    NUMBER_0_5 + framingRect.top + halfCornerSize,
                    framingRect.right - halfCornerSize - scanLineMargin,
                    gridScanLineBottom);
            Rect srcRect = new Rect(0,
                    (int) (gridLineMap.getImageInfo().size.height
                            - dstGridRectF.getHeight()),
                    gridLineMap.getImageInfo().size.width,
                    gridLineMap.getImageInfo().size.height);

            if (srcRect.top < 0) {
                srcRect.top = 0;
                dstGridRectF.top = dstGridRectF.bottom - srcRect.getHeight();
            }
            canvas.drawPixelMapHolderRect(new PixelMapHolder(gridLineMap),
                    QRCodeUtil.trasnsRectFfromRect(srcRect),
                    dstGridRectF, paint);
        } else if (scanLineBitmap != null) {
            RectFloat lineRect = new RectFloat(
                    framingRect.left + halfCornerSize + scanLineMargin,
                    scanLineTop,
                    framingRect.right - halfCornerSize - scanLineMargin,
                    scanLineTop + scanLineBitmap.getImageInfo().size.height);
            canvas.drawPixelMapHolderRect(new PixelMapHolder(scanLineBitmap),
                    null, lineRect, paint);
        } else {
            paint.setStyle(Paint.Style.FILL_STYLE);
            paint.setColor(new Color(scanLineColor));
            canvas.drawRect(new RectFloat(
                    framingRect.left + halfCornerSize + scanLineMargin,
                    scanLineTop,
                    framingRect.right - halfCornerSize - scanLineMargin,
                    scanLineTop + scanLineSize), paint);
        }
    }

    /**
     * 移动扫描线的位置.
     */
    private void moveScanLine() {
        if (gridLineMap == null) {
            // 处理非网格扫描图片的情况
            scanLineTop += moveStepDistance;
            if (scanLineBitmap != null) {
                scanLineSize = scanLineBitmap.getImageInfo().size.width;
            }
            if (isScanLineReverse) {
                if (scanLineTop + scanLineSize
                        > framingRect.bottom - halfCornerSize
                        || scanLineTop < framingRect.top + halfCornerSize) {
                    moveStepDistance = -moveStepDistance;
                }
            } else {
                if (scanLineTop + scanLineSize
                        > framingRect.bottom - halfCornerSize) {
                    scanLineTop = framingRect.top + halfCornerSize + NUMBER_0_5;
                }
            }
        } else {
            // 处理网格扫描图片的情况
            gridScanLineBottom += moveStepDistance;
            if (gridScanLineBottom > framingRect.bottom - halfCornerSize) {
                gridScanLineBottom = framingRect.top + halfCornerSize
                        + NUMBER_0_5;
            }
        }
        getContext().getUITaskDispatcher().delayDispatch(() -> {
            invalidate();
        }, NUMBER_20);
    }

    /**
     * 画左边两个直角的线.
     *
     * @param canvas 画布
     */
    private void drawCornerLineOne(Canvas canvas) {
        canvas.drawLine(new Point(framingRect.left - halfCornerSize,
                        framingRect.top),
                new Point(framingRect.left - halfCornerSize + cornerLength,
                        framingRect.top),
                paint);
        canvas.drawLine(new Point(framingRect.left,
                        framingRect.top - halfCornerSize),
                new Point(framingRect.left,
                        framingRect.top - halfCornerSize + cornerLength),
                paint);
        canvas.drawLine(new Point(framingRect.right + halfCornerSize,
                        framingRect.top),
                new Point(framingRect.right + halfCornerSize - cornerLength,
                        framingRect.top),
                paint);
        canvas.drawLine(new Point(framingRect.right,
                        framingRect.top - halfCornerSize),
                new Point(framingRect.right,
                        framingRect.top - halfCornerSize + cornerLength),
                paint);

        canvas.drawLine(new Point(framingRect.left - halfCornerSize,
                        framingRect.bottom),
                new Point(framingRect.left - halfCornerSize + cornerLength,
                        framingRect.bottom),
                paint);
        canvas.drawLine(new Point(framingRect.left,
                        framingRect.bottom + halfCornerSize),
                new Point(framingRect.left,
                        framingRect.bottom + halfCornerSize - cornerLength),
                paint);
        canvas.drawLine(new Point(framingRect.right + halfCornerSize,
                        framingRect.bottom),
                new Point(framingRect.right + halfCornerSize - cornerLength,
                        framingRect.bottom),
                paint);
        canvas.drawLine(new Point(framingRect.right,
                        framingRect.bottom + halfCornerSize),
                new Point(framingRect.right,
                        framingRect.bottom + halfCornerSize - cornerLength),
                paint);
    }

    /**
     * 画右边两个直角的线.
     *
     * @param canvas 画布
     */
    private void drawCornerLineTwo(Canvas canvas) {
        canvas.drawLine(new Point(framingRect.left,
                        framingRect.top + halfCornerSize),
                new Point(framingRect.left + cornerLength,
                        framingRect.top + halfCornerSize),
                paint);
        canvas.drawLine(new Point(framingRect.left + halfCornerSize,
                        framingRect.top),
                new Point(framingRect.left + halfCornerSize,
                        framingRect.top + cornerLength),
                paint);
        canvas.drawLine(new Point(framingRect.right,
                        framingRect.top + halfCornerSize),
                new Point(framingRect.right - cornerLength,
                        framingRect.top + halfCornerSize),
                paint);
        canvas.drawLine(new Point(framingRect.right - halfCornerSize,
                        framingRect.top),
                new Point(framingRect.right - halfCornerSize,
                        framingRect.top + cornerLength),
                paint);

        canvas.drawLine(new Point(framingRect.left,
                        framingRect.bottom - halfCornerSize),
                new Point(framingRect.left + cornerLength,
                        framingRect.bottom - halfCornerSize), paint);
        canvas.drawLine(new Point(framingRect.left + halfCornerSize,
                        framingRect.bottom),
                new Point(framingRect.left + halfCornerSize,
                        framingRect.bottom - cornerLength), paint);
        canvas.drawLine(new Point(framingRect.right,
                        framingRect.bottom - halfCornerSize),
                new Point(framingRect.right - cornerLength,
                        framingRect.bottom - halfCornerSize),
                paint);
        canvas.drawLine(new Point(framingRect.right - halfCornerSize,
                        framingRect.bottom),
                new Point(framingRect.right - halfCornerSize,
                        framingRect.bottom - cornerLength),
                paint);
    }

    /**
     * 获取扫描框的大小.
     *
     * @return 获取扫描框的大小
     */
    public Rect getFramingRect() {
        return framingRect;
    }

    /**
     * update ScanBox.
     */
    private void refreshScanBox() {
        if (isShowDefaultGridScanLineDrawable) {
            gridLineMap = originQRCodegridLineMap;
        }

        if (isShowDefaultScanLineDrawable) {
            scanLineBitmap = originQRCodeScanLineBitmap;
        }

        rectHeight = rectWidth;

        if (verticalBias != -1) {
            int screenHeight = QRCodeUtil.getScreenResolution(getContext())
                    .getPointYToInt();
            if (toolbarHeight == 0) {
                topOffset = (int) (screenHeight * verticalBias
                        - rectHeight / 2);
            } else {
                topOffset = toolbarHeight + (int) ((screenHeight
                        - toolbarHeight) * verticalBias - rectHeight / 2);
            }
        }

        calFramingRect();

        invalidate();
    }
}
