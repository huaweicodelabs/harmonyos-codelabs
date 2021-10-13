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

package com.huawei.cookbook;

import com.huawei.cookbook.util.CommonUtils;
import com.huawei.cookbook.util.MovieInfo;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.ace.ability.AceAbility;
import ohos.utils.zson.ZSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * movie detail page
 */
public class MovieDetail extends AceAbility {
    /**
     * movie list
     */
    private static final List<MovieInfo> MOVIE_INFO_LIST = new ArrayList<>();

    @Override
    public void onStart(Intent intent) {
        if (MOVIE_INFO_LIST.size() == 0) {
            MOVIE_INFO_LIST.addAll(CommonUtils.getMoviesData(this));
        }
        String paramsJson = intent.getParams().getParam("params").toString();
        int index = ZSONObject.stringToZSON(paramsJson).getIntValue("index");
        MovieInfo movieInfo = MOVIE_INFO_LIST.get(index);
        IntentParams params = new IntentParams();
        params.setParam("movieName",movieInfo.getTitle());
        params.setParam("sort",movieInfo.getSort());
        params.setParam("type",movieInfo.getType());
        params.setParam("imgUrl",movieInfo.getImgUrl());
        params.setParam("rating",movieInfo.getRating());
        params.setParam("introduction",movieInfo.getIntroduction());
        String url = "pages/index/detail";
        setPageParams(url,params);
        super.onStart(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
