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

package com.huawei.metadatabindingdemo;

import ohos.aafwk.ability.AbilityPackage;
import ohos.app.Context;
import ohos.mp.metadata.annotation.MetaDataApplication;
import ohos.mp.metadata.binding.MetaDataFramework;

/**
 * The entrance of this application. the {@link MetaDataApplication} annotation must be tagged on the Application class
 * if the application wants to use MetaDataBinding framework.
 *
 * @since 2021-05-15
 */
@MetaDataApplication(requireData = true, exportData = false)
public class MyApplication extends AbilityPackage {
    private static Context context;

    @Override
    public void onInitialize() {
        super.onInitialize();
        context = this.getContext();
        // innit MetaDataFramework with the application context
        MetaDataFramework.init(this);
    }

    /**
     * get application context
     *
     * @return context
     */
    public static Context getApplication() {
        return context;
    }
}
