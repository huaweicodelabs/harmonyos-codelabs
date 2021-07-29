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

package com.huawei.codelab.view;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.multimodalinput.event.TouchEvent;

import java.util.List;

import com.huawei.codelab.model.Product;
import com.huawei.codelab.service.ProductService;
import com.huawei.codelab.utils.ComposeUtil;
import com.huawei.codelab.utils.NumbersUtil;
import com.huawei.codelab.utils.DialogUtil;

/**
 * Drawing Container
 *
 * @since 2021-04-13
 */
public class ProductContainer extends Component implements Component.DrawTask, Component.TouchEventListener {
    // Paint
    private Paint paint;

    // height
    private int height;

    // width
    private int width;

    // Processing service logic class
    private ProductService productService;

    // Finger Touch Graphics
    private Product touchProduct;

    // EventHandler
    private EventHandler runHandler;

    /**
     * Processes services in a thread and notifies the UI to be updated
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Core functions for processing services
            productService.dealProduct();
            // Update UI execution drawing
            getContext().getUITaskDispatcher().asyncDispatch(() -> {
                invalidate();
            });
        }
    };

    /**
     * constructor
     *
     * @param context context
     */
    public ProductContainer(Context context) {
        super(context);
        initView();
    }

    /**
     * Initialize the drawing event, touch event, and EventHandler
     */
    private void initView() {
        addDrawTask(this);
        setTouchEventListener(this);
        runHandler = new EventHandler(EventRunner.create());
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        // Initialize the canvas width, height, and business class before drawing.
        initProductFun(component);
        // Draw all the graphs
        drawProduct(canvas);
        // Status Result Processing
        dealStateResult();
    }

    /**
     * Initializing the Service ProductFun
     *
     * @param component Current Container Control
     */
    private void initProductFun(Component component) {
        if (productService == null) {
            height = component.getHeight();
            width = component.getWidth();
            productService = new ProductService(getContext(), width, height);
            paint = new Paint();
        }
    }

    /**
     * drawing graphs
     *
     * @param canvas Canvas
     */
    private void drawProduct(Canvas canvas) {
        List<Product> products = productService.getProductList();
        for (Product product : products) {
            canvas.drawPixelMapHolder(product.getPixelMapHolder(),
                    product.getCenterX() - product.getRadius(),
                    product.getCenterY() - product.getRadius(), paint);
        }
    }

    /**
     * Processing Game Status
     */
    private void dealStateResult() {
        // state=0 Run the runnable command to process services.
        if (productService.getState() == 0) {
            if (runHandler != null) {
                runHandler.postTask(runnable);
            }
        } else if (productService.getState() == 1) {
            // state=1 A dialog box is displayed, indicating that the synthesis is successful.
            showGifDialog();
        } else {
            // state=2 A dialog box is displayed, indicating that the game is over.
            showDialog("游戏结束,确定重新开始游戏吗?");
        }
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        float crtX = touchEvent.getPointerPosition(touchEvent.getIndex()).getX();
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                // Finger press event
                downEvent(touchEvent, crtX);
                break;
            case TouchEvent.POINT_MOVE:
                // Finger slide event
                moveEvent(crtX);
                break;
            case TouchEvent.PRIMARY_POINT_UP:
            case TouchEvent.CANCEL:
                // Finger Lift Event
                upEvent();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Finger press event
     *
     * @param touchEvent touchEvent
     * @param crtX X-coordinate value pressed by the finger
     */
    private void downEvent(TouchEvent touchEvent, float crtX) {
        float crtY = touchEvent.getPointerPosition(touchEvent.getIndex()).getY();
        if (productService != null) {
            List<Product> products = productService.getProductList();
            for (Product product : products) {
                if (!ComposeUtil.isStopProduct(product) &&
                        (product.getCenterX() - product.getRadius() <= crtX
                                && product.getCenterX() + product.getRadius() >= crtX
                                && product.getCenterY() - product.getRadius() <= crtY
                                && product.getCenterY() + product.getRadius() >= crtY)) {
                    touchProduct = product;
                    product.setSpeedY(ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDY);
                    break;
                }
            }
        }
    }

    /**
     * Finger movement event
     *
     * @param crtX X-coordinate value pressed by the finger
     */
    private void moveEvent(float crtX) {
        float moveX = crtX;
        if (touchProduct != null) {
            if (touchProduct.getRadius() >= moveX) {
                moveX = touchProduct.getRadius();
            }
            if (width - touchProduct.getRadius() <= moveX) {
                moveX = width - touchProduct.getRadius();
            }
            if (touchProduct.getSpeedY() != 0) {
                touchProduct.setCenterX(moveX);
                invalidate();
            }
        }
    }

    /**
     * Handling After Finger Lifting
     */
    private void upEvent() {
        if (touchProduct != null) {
            touchProduct.setSpeedY(ComposeUtil.BIT);
        }
        touchProduct = null;
    }

    /**
     * A dialog box is displayed, indicating the game status
     *
     * @param content What to Display
     */
    private void showDialog(String content) {
        if (paint != null) {
            paint = null;
            DialogUtil.showComfirmDialog(content, getContext(), () -> {
                productService = null;
                invalidate();
            }, () -> productService = null);
        }
    }

    /**
     * A dialog box is displayed, indicating the game status
     */
    private void showGifDialog() {
        DialogUtil.showGifDialog(getContext(), new CommonDialog.DestroyedListener() {
            @Override
            public void onDestroy() {
                showDialog("确定重新开始游戏吗?");
            }
        });
    }
}
