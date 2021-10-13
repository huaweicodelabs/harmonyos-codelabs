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

package ohos.codelabs.distributedvideo;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.codelabs.distributedvideo.slice.SimplePlayerAbilitySlice;

/**
 * the main page
 *
 * @since 2021-09-07
 *
 */
public class PlayerAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(SimplePlayerAbilitySlice.class.getName());
    }
}
