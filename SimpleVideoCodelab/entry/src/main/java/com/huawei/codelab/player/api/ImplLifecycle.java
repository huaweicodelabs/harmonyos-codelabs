/*
 * Copyright (c) 2021 Huawei Device Co., Ltd. All rights reserved.
 *
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

package com.huawei.codelab.player.api;

/**
 * ImplLifecycle
 *
 * @since 2020-12-04
 */

public interface ImplLifecycle {
    /**
     * start the HmPlayer
     *
     */
    void onStart();

    /**
     * turn the HmPlayer foreground
     *
     */
    void onForeground();

    /**
     * turn the HmPlayer to background
     *
     */
    void onBackground();

    /**
     * stop the HmPlayer
     *
     */
    void onStop();
}
