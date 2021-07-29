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

package com.huawei.codelab.utils;

import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

import com.huawei.codelab.model.Product;

/**
 * Graphic Collisions, Composites, Overlaps, Drops
 *
 * @since 2021-04-14
 */
public class ComposeUtil {
    /**
     * The speed of movement
     */
    public static final int BIT = 6;

    private ComposeUtil() {
    }

    /**
     * Determine whether two graphs collide.
     *
     * @param productA Falling figure
     * @param productB still figure
     * @return Collision or not
     */
    private static boolean isCollision(Product productA, Product productB) {
        double minLength = productA.getRadius() + productB.getRadius();
        double maxLength = Math.pow(Math.abs(productA.getCenterX() - productB.getCenterX()), NumbersUtil.VAULE_TWO)
                + Math.pow(Math.abs(productA.getCenterY() - productB.getCenterY()), NumbersUtil.VAULE_TWO);
        // 发生碰撞
        if (maxLength <= Math.pow(minLength, NumbersUtil.VAULE_TWO)) {
            return true;
        }
        return false;
    }

    /**
     * Determine whether a graphic collision occurs
     *
     * @param productA Falling figure
     * @param productList All Drawings
     * @return Collision or not
     */
    public static List<Product> occursCollision(Product productA, List<Product> productList) {
        List<Product> products = new ArrayList<>(NumbersUtil.VAULE_SIXTEEN);
        for (Product product : productList) {
            if (product == productA) {
                continue;
            }
            if (isCollision(productA, product)) {
                products.add(product);
            }
        }
        return products;
    }

    /**
     * Determine whether two graphs can be combined
     *
     * @param productA Falling figure
     * @param productB still figure
     * @return Whether to synthesize
     */
    public static boolean isCompose(Product productA, Product productB) {
        return productA.getLevel() == productB.getLevel();
    }

    /**
     * Combines two shapes into a new shape
     *
     * @param context context
     * @param productA Previous graphic
     * @param productB The latter figure
     * @param width Canvas width
     * @return Composite graphic
     */
    public static Product getComposeProduct(Context context, Product productA, Product productB, int width) {
        // Level of the new graph
        int level = productA.getLevel() + 1;
        // Start creating a new drawing
        Product newProduct = ImageUtil.generateProduct(context, level);
        // Center x position of new drawing
        float centerX = Math.abs((productA.getCenterX() + productB.getCenterX()) / NumbersUtil.VAULE_TWO);
        // Get a drop graphic
        Product temp = productA.getCenterY() < productB.getCenterY() ? productA : productB;
        if (newProduct.getRadius() >= centerX) {
            centerX = newProduct.getRadius();
        }
        if (width - newProduct.getRadius() <= centerX) {
            centerX = width - newProduct.getRadius();
        }
        // Center y position of graph
        float centerY = temp.getCenterY() - temp.getRadius() + newProduct.getRadius();
        newProduct.setCenterX(centerX);
        newProduct.setCenterY(centerY);

        newProduct.setSpeedX(0);
        newProduct.setSpeedY(ComposeUtil.BIT);

        return newProduct;
    }

    /**
     * Determine the figure that can be dropped
     *
     * @param products All Drawings
     * @param width Canvas width
     * @param height Canvas Height
     * @return Are there any dropped graphics
     */
    public static boolean dropProduct(List<Product> products, int width, int height) {
        boolean isDrop = false;
        for (Product product : products) {
            if (isStopProduct(product)) {
                if (product.getCenterY() + product.getRadius() >= height) {
                    continue;
                }
                List<Product> occursProducts = occursCollision(product, products);
                // Any touch of the graphics can be dropped.
                if (occursProducts.size() == 0) {
                    product.setSpeedY(ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDY);
                    isDrop = true;
                }

                // Only 1 of the graphics to be touched needs to be dropped.
                boolean result = dropProduct(occursProducts, product, width);
                if (result) {
                    isDrop = true;
                }

                result = dropProduct(occursProducts, product);
                if (result) {
                    isDrop = true;
                }

                result = dropProduct(product, occursProducts, width, height);
                if (result) {
                    isDrop = true;
                }
            }
        }
        return isDrop;
    }

    /**
     * Graphics that can be dropped
     *
     * @param occursProductArray Drawings that collide with the current drawing
     * @param product Current Drawing
     * @return Whether to drop
     */
    private static boolean dropProduct(List<Product> occursProductArray, Product product) {
        if (occursProductArray.size() == NumbersUtil.VAULE_TWO) {
            Product occursProduct0 = occursProductArray.get(0);
            Product occursProduct1 = occursProductArray.get(1);
            if (occursProduct0.getCenterY() < product.getCenterY()
                    && occursProduct1.getCenterY() < product.getCenterY()) {
                product.setSpeedY(ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDY);
                return true;
            }
        }
        return false;
    }

    /**
     * Graphics that can be dropped
     *
     * @param occursProductArray Drawings that collide with the current drawing
     * @param product Current Drawing
     * @param width Canvas width
     * @return Whether to drop
     */
    private static boolean dropProduct(List<Product> occursProductArray, Product product, int width) {
        if (occursProductArray.size() == 1) {
            Product occursProduct = occursProductArray.get(0);
            if (occursProduct.getCenterY() < product.getCenterY()) {
                product.setSpeedY(ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDY);
                return true;
            } else {
                if (product.getCenterX() <= product.getRadius()) {
                    return false;
                }
                if (product.getCenterX() + product.getRadius() >= width) {
                    return false;
                }
                product.setSpeedY(1);
                product.setSpeedX(product.getCenterX() > occursProduct.getCenterX()
                        ? ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDX
                        : -ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDX);
                return true;
            }
        }
        return false;
    }

    /**
     * Graphics that can be dropped
     *
     * @param product Current Drawing
     * @param occursProductArray Drawings that collide with the current drawing
     * @param width Canvas width
     * @param height Canvas Height
     * @return Whether to drop
     */
    private static boolean dropProduct(Product product, List<Product> occursProductArray, int width, int height) {
        if (occursProductArray.size() == NumbersUtil.VAULE_TWO && !isStopProduct(product, width, height)) {
            Product occursProduct0 = occursProductArray.get(0).getCenterY()
                    > occursProductArray.get(1).getCenterY()
                    ? occursProductArray.get(0) : occursProductArray.get(1);
            Product occursProduct1 = occursProductArray.get(0).getCenterY()
                    > occursProductArray.get(1).getCenterY()
                    ? occursProductArray.get(1) : occursProductArray.get(0);
            if (product.getCenterY() > occursProduct1.getSpeedY()
                    && product.getCenterY() < occursProduct0.getCenterY()) {
                if (product.getCenterX() > occursProduct0.getCenterX()
                        && product.getCenterX() > occursProduct1.getCenterX()) {
                    product.setSpeedX(ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDX);
                    product.setSpeedY(1);
                    return true;
                }
                if (product.getCenterX() < occursProduct0.getCenterX()
                        && product.getCenterX() < occursProduct1.getCenterX()) {
                    product.setSpeedX(-ComposeUtil.BIT * NumbersUtil.COLLISION_SPEEDX);
                    product.setSpeedY(1);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get Motion's Graphics
     *
     * @param products All Drawings
     * @return Get Motion's Graphics
     */
    public static List<Product> getRunProduct(List<Product> products) {
        List<Product> results = new ArrayList<>(NumbersUtil.VAULE_SIXTEEN);
        for (Product product : products) {
            if (!isStopProduct(product)) {
                results.add(product);
                break;
            }
        }
        return results;
    }

    /**
     * Determines whether the graph stops
     *
     * @param product Current Drawing
     * @return Whether to stop
     */
    public static boolean isStopProduct(Product product) {
        if (product.getSpeedX() == 0 && product.getSpeedY() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Stop when collision meets boundary conditions
     *
     * @param product Current Drawing
     * @param width Canvas width
     * @param height Canvas Height
     * @return Whether to move to boundary
     */
    public static boolean isStopProduct(Product product, int width, int height) {
        if (product.getCenterY() + product.getRadius() >= height) {
            return true;
        }
        if (product.getCenterX() <= product.getRadius()) {
            return true;
        }
        if (product.getCenterX() + product.getRadius() >= width) {
            return true;
        }
        return false;
    }
}
