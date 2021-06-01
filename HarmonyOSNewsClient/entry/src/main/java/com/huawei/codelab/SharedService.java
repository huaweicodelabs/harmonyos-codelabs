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

package com.huawei.codelab;

import com.huawei.codelab.slice.NewsDetailAbilitySlice;

import manager.NewsDemoIDLStub;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.rpc.IRemoteObject;

/**
 * Shared Service
 *
 * @since 2020-12-04
 */
public class SharedService extends Ability {
    private static final String DESCRIPTOR = "com.huawei.codelab.idl.INewsDemoIDL";

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    protected IRemoteObject onConnect(Intent intent) {
        return new MyBinder(DESCRIPTOR);
    }

    /**
     * MyBinder
     *
     * @since 2020-12-04
     */
    private class MyBinder extends NewsDemoIDLStub {
        MyBinder(String descriptor) {
            super(descriptor);
        }

        @Override
        public void tranShare(String title, String reads, String likes, String content, String image) {
            Intent intent = new Intent();
            Operation operation =
                    new Intent.OperationBuilder()
                            .withBundleName(getBundleName())
                            .withAbilityName(NewsAbility.class.getName())
                            .withAction("action.detail")
                            .build();
            intent.setOperation(operation);
            intent.setParam(NewsDetailAbilitySlice.INTENT_TITLE, title);
            intent.setParam(NewsDetailAbilitySlice.INTENT_READ, reads);
            intent.setParam(NewsDetailAbilitySlice.INTENT_LIKE, likes);
            intent.setParam(NewsDetailAbilitySlice.INTENT_CONTENT, content);
            intent.setParam(NewsDetailAbilitySlice.INTENT_IMAGE, image);
            startAbility(intent);
        }
    }
}
