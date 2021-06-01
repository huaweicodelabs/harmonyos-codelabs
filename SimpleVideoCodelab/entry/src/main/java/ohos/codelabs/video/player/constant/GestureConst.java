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

package ohos.codelabs.video.player.constant;

/**
 * GestureConst
 *
 * @since 2021-04-04
 */
public class GestureConst {
    private static final int DEFAULT_LONG_PRESS_TIMEOUT = 500;

    private static final int TOUCH_SLOP = 64;

    private static final int TAP_TIMEOUT = 100;

    private static final int DOUBLE_TAP_TIMEOUT = 300;

    private static final int DOUBLE_TAP_MIN_TIME = 40;

    private static final int DOUBLE_TAP_SLOP = 100;

    private static final int MINIMUM_FLING_VELOCITY = 50;

    private static final int MAXIMUM_FLING_VELOCITY = 8000;

    private static final int DOUBLE_TAP_TOUCH_SLOP = TOUCH_SLOP;

    private static final float AMBIGUOUS_GESTURE_MULTIPLIER = 2f;

    private GestureConst() {
    }

    /**
     * getDoubleTapTimeout
     *
     * @return int
     */
    public static int getDoubleTapTimeout() {
        return DOUBLE_TAP_TIMEOUT;
    }

    /**
     * getDoubleTapMinTime
     *
     * @return int
     */
    public static int getDoubleTapMinTime() {
        return DOUBLE_TAP_MIN_TIME;
    }

    /**
     * getTouchSlop
     *
     * @return int
     */
    public static int getTouchSlop() {
        return TOUCH_SLOP;
    }

    /**
     * getLongPressTimeout
     *
     * @return int
     */
    public static int getLongPressTimeout() {
        return DEFAULT_LONG_PRESS_TIMEOUT;
    }

    /**
     * getTapTimeout
     *
     * @return int
     */
    public static int getTapTimeout() {
        return TAP_TIMEOUT;
    }

    /**
     * getDoubleTapSlop
     *
     * @return int
     */
    public static int getDoubleTapSlop() {
        return DOUBLE_TAP_SLOP;
    }

    /**
     * getMinimumFlingVelocity
     *
     * @return int
     */
    public static int getMinimumFlingVelocity() {
        return MINIMUM_FLING_VELOCITY;
    }

    /**
     * getMaximumFlingVelocity
     *
     * @return int
     */
    public static int getMaximumFlingVelocity() {
        return MAXIMUM_FLING_VELOCITY;
    }

    /**
     * getScaledTouchSlop
     *
     * @return int
     */
    public static int getScaledTouchSlop() {
        return TOUCH_SLOP;
    }

    /**
     * getScaledDoubleTapTouchSlop
     *
     * @return int
     */
    public static int getScaledDoubleTapTouchSlop() {
        return DOUBLE_TAP_TOUCH_SLOP;
    }

    /**
     * getScaledDoubleTapSlop
     *
     * @return int
     */
    public static int getScaledDoubleTapSlop() {
        return DOUBLE_TAP_SLOP;
    }

    /**
     * getScaledMinimumFlingVelocity
     *
     * @return int
     */
    public static int getScaledMinimumFlingVelocity() {
        return MINIMUM_FLING_VELOCITY;
    }

    /**
     * getScaledMaximumFlingVelocity
     *
     * @return int
     */
    public static int getScaledMaximumFlingVelocity() {
        return MAXIMUM_FLING_VELOCITY;
    }

    /**
     * getAmbiguousGestureMultiplier
     *
     * @return int
     */
    public static float getAmbiguousGestureMultiplier() {
        return AMBIGUOUS_GESTURE_MULTIPLIER;
    }
}
