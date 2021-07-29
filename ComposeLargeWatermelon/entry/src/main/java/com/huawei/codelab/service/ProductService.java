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

package com.huawei.codelab.service;

import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

import com.huawei.codelab.model.Product;
import com.huawei.codelab.utils.ComposeUtil;
import com.huawei.codelab.utils.ImageUtil;
import com.huawei.codelab.utils.NumbersUtil;

/**
 * Sports service analysis
 *
 * @since 2021-04-20
 */
public class ProductService {
    // state = 0正常运行  state = 1合成大西瓜  state = 2满屏游戏结束
    private int state;

    // context
    private Context context;

    // Canvas width
    private int width;

    // Canvas Height
    private int height;

    // All Drawing Collections
    private List<Product> productList;

    /**
     * constructor
     *
     * @param context context
     * @param width Canvas width
     * @param height Canvas Height
     */
    public ProductService(Context context, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
        productList = new ArrayList<>(NumbersUtil.VAULE_SIXTEEN);
    }

    /**
     * Obtains all the graphs to be drawn
     *
     * @return Obtains all the graphs to be drawn
     */
    public List<Product> getProductList() {
        return productList;
    }

    /**
     * Game status value
     *
     * @return status value
     */
    public int getState() {
        return state;
    }

    /**
     * Handles core services, including collision detection, merge, and graph drop
     */
    public void dealProduct() {
        // 获取运行中的图形
        List<Product> runproducts = ComposeUtil.getRunProduct(productList);

        if (runproducts.size() == 0) {
            withOutRunProduct();
        }

        // Handles motion graphics collisions and composition
        for (Product runProduct : runproducts) {
            dealRunProduct(runProduct);
        }

        // Dealing with Overlapping Issues
        updateOverlap();
        // boundary judgment
        updateView();
    }

    /**
     * Graphics without motion
     */
    private void withOutRunProduct() {
        // There is no moving figure. Determine whether there is a figure that can fall.
        if (!ComposeUtil.dropProduct(productList, width, height)) {
            // Check whether the graphics occupy the screen.
            boolean fullScreen = fullScreen();
            if (!fullScreen) {
                // No full screen, create a new graphic
                createNewProduct();
            } else {
                // Full screen modification status
                state = NumbersUtil.VAULE_TWO;
            }
        }
    }

    /**
     * Check whether the screen is full
     *
     * @return Full Screen
     */
    private boolean fullScreen() {
        for (Product product : productList) {
            if (product.getCenterY() - NumbersUtil.VAULE_TWO * product.getRadius() < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles motion graphics collisions and composition
     *
     * @param runProduct Motion graphics
     */
    private void dealRunProduct(Product runProduct) {
        List<Product> occursProducts = ComposeUtil.occursCollision(runProduct, productList);
        if (occursProducts.size() == 0) {
            // Continue without collision
            withOutCollision(runProduct);
        } else {
            for (Product occursProduct : occursProducts) {
                // Compose new graphics
                composeProduct(occursProduct, runProduct);
            }
            // impact velocity change
            updateCollisionSpeed(occursProducts, runProduct);
        }
    }

    /**
     * Conditional detection is performed if no collision occurs
     *
     * @param runProduct Motion graphics
     */
    private void withOutCollision(Product runProduct) {
        if (runProduct.getCenterX() <= runProduct.getRadius()
                || runProduct.getCenterX() + runProduct.getRadius() >= width) {
            runProduct.setSpeedX(-runProduct.getSpeedX());
        }
        if (runProduct.getCenterY() + runProduct.getRadius() >= height) {
            runProduct.setSpeedY(0);
        }
        if (runProduct.getSpeedY() == 0) {
            runProduct.setSpeedX(0);
        }
    }

    /**
     * Compose new graphics
     *
     * @param occursProduct Collision Graphics
     * @param runProduct Current Motion Drawings
     */
    private void composeProduct(Product occursProduct, Product runProduct) {
        if (ComposeUtil.isCompose(runProduct, occursProduct)) {
            Product composeProduct = ComposeUtil.getComposeProduct(context, runProduct, occursProduct, width);
            if (composeProduct != null) {
                productList.remove(runProduct);
                productList.remove(occursProduct);
                productList.add(composeProduct);
                if (composeProduct.getLevel() == ImageUtil.getLength() - 1) {
                    state = 1;
                }
            }
        }
    }

    /**
     * impact velocity change
     *
     * @param occursProductArray A graphic that collides with a moving graphic
     * @param runProduct Current Motion Drawings
     */
    private void updateCollisionSpeed(List<Product> occursProductArray, Product runProduct) {
        if (ComposeUtil.isStopProduct(runProduct, width, height) || occursProductArray.size() > 1) {
            runProduct.setSpeedX(0);
            runProduct.setSpeedY(0);
        } else {
            Product occursProduct = occursProductArray.get(0);
            float speed;
            if (runProduct.getCenterX() > occursProduct.getCenterX()) {
                speed = ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDX;
            } else if (runProduct.getCenterX() < occursProduct.getCenterX()) {
                speed = -ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDX;
            } else {
                speed = 0;
            }
            runProduct.setSpeedX(speed);
            runProduct.setSpeedY(ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDY);
        }
    }

    /**
     * Update Overlapping Drawings
     */
    private void updateOverlap() {
        for (Product productA : productList) {
            if (productA.getCenterY() >= height - productA.getRadius()) {
                continue;
            }
            for (Product productB : productList) {
                if (productB == productA) {
                    continue;
                }
                if (productA.getCenterY() < productB.getCenterY()) {
                    dealOverlap(productA, productB);
                }
            }
        }
    }

    /**
     * Compare two shapes for significant overlap
     *
     * @param productA Graph A
     * @param productB Graph B
     */
    private void dealOverlap(Product productA, Product productB) {
        double minLength = Math.pow(productA.getRadius() + productB.getRadius(), NumbersUtil.VAULE_TWO);
        double powX = Math.pow(Math.abs(productA.getCenterX() - productB.getCenterX()), NumbersUtil.VAULE_TWO);
        double powY = Math.pow(Math.abs(productA.getCenterY() - productB.getCenterY()), NumbersUtil.VAULE_TWO);
        double maxLength = powX + powY;
        if (Math.sqrt(maxLength) < Math.sqrt(minLength) - NumbersUtil.OVERLAP_SIZE) {
            double rad = productA.getRadius() + productB.getRadius() - Math.sqrt(maxLength);
            double moveX = rad / Math.sqrt(1 + powY / powX);
            double moveY = rad / Math.sqrt(1 + powX / powY);
            if (productA.getCenterX() > productB.getCenterX()) {
                float centerX = (float) (productA.getCenterX() + moveX);
                float centerY = (float) (productA.getCenterY() - moveY);
                if (width - productA.getRadius() <= centerX) {
                    centerY = centerY - (centerX + productA.getRadius() - width);
                    centerX = width - productA.getRadius();
                }
                productA.setCenterX(centerX);
                productA.setCenterY(centerY);
            } else {
                float centerX = (float) (productA.getCenterX() - moveX);
                float centerY = (float) (productA.getCenterY() - moveY);
                if (productA.getRadius() >= centerX) {
                    centerY = centerY - (productA.getRadius() - centerX);
                    centerX = productA.getRadius();
                }
                productA.setCenterX(centerX);
                productA.setCenterY(centerY);
            }
        }
    }

    /**
     * Set the center point and check the boundary
     */
    private void updateView() {
        for (Product product : productList) {
            float centerX = product.getCenterX() + product.getSpeedX();
            if (product.getRadius() >= centerX) {
                centerX = product.getRadius();
            }
            if (width - product.getRadius() <= centerX) {
                centerX = width - product.getRadius();
            }
            float centerY = product.getCenterY() + product.getSpeedY();
            if (height - product.getRadius() <= centerY) {
                centerY = height - product.getRadius();
            }
            product.setCenterX(centerX);
            product.setCenterY(centerY);
        }
    }

    /**
     * Create a new drawing
     */
    private void createNewProduct() {
        int level = (int) (Math.random() * NumbersUtil.VAULE_FOUR);
        Product newProduct = ImageUtil.generateProduct(context, level);
        int value = Math.abs(width - NumbersUtil.VAULE_TWO * newProduct.getRadius());
        newProduct.setCenterX((float) (newProduct.getRadius() + Math.random() * value));
        newProduct.setCenterY(newProduct.getRadius());
        newProduct.setSpeedX(0);
        newProduct.setSpeedY(ComposeUtil.BIT);
        productList.add(newProduct);
    }
}
