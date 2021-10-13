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

package com.huawei.codelab.map;

import com.huawei.codelab.util.MapUtils;
import ohos.app.Context;
import ohos.location.Location;
import ohos.location.Locator;
import ohos.location.LocatorCallback;
import ohos.location.RequestParam;

/**
 * 获取本机位置信息实现类
 *
 * @since 2021-03-15
 */
public class LocationHelper {
    private LocCallBack locCallBack;

    private Context context;

    /**
     * 获取本机位置信息
     *
     * @param con      Context con
     * @param callBack LocCallBack callBack
     */
    public void getMyLocation(Context con, LocCallBack callBack) {
        locCallBack = callBack;
        context = con;
        Locator locator = new Locator(context);
        if (locator.isLocationSwitchOn()) {
            RequestParam requestParam = new RequestParam(RequestParam.SCENE_NAVIGATION);
            MyLocatorCallback locatorCallback = new MyLocatorCallback();
            locator.requestOnce(requestParam, locatorCallback);
        } else {
            MapUtils.showToast(context, "请前往设置打开位置信息开关");
        }
    }

    /**
     * 获取位置信息后的回调接口
     *
     * @since 2021-03-15
     */
    public interface LocCallBack {
        /**
         * locCallBack
         *
         * @param loc loc
         */
        void locCallBack(Location loc);
    }

    /**
     * MyLocatorCallback
     *
     * @since 2021-03-12
     */
    private class MyLocatorCallback implements LocatorCallback {
        @Override
        public void onLocationReport(Location location) {
            if (locCallBack != null) {
                context.getUITaskDispatcher().asyncDispatch(() -> locCallBack.locCallBack(location));
            }
        }

        @Override
        public void onStatusChanged(int type) {
        }

        @Override
        public void onErrorReport(int type) {
        }
    }
}
