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

package com.huawei.metadatabindingdemo.custom_ui.metadata;

import ohos.agp.utils.Color;
import ohos.mp.metadata.binding.metadata.DataAbilityMetaData;

/**
 * metadata wrapper. Using dataability as data source, so this one extends {@link DataAbilityMetaData}.
 *
 * @since 2021-05-15
 */
public class CustomMetaData extends DataAbilityMetaData {
    /**
     * self defined transfer function to transfer boolean metadata to Color
     *
     * @param bol boolean
     * @return color false-BLUE or true-BLACK
     */
    public Color getColorOn(boolean bol) {
        if (bol) {
            return Color.BLACK;
        } else {
            return Color.BLUE;
        }
    }

    /**
     * self defined transfer function to transfer boolean metadata to Color
     *
     * @param bol boolean
     * @return color true-BLUE or false-BLACK
     */
    public Color getColorOff(boolean bol) {
        if (bol) {
            return Color.BLUE;
        } else {
            return Color.BLACK;
        }
    }
}