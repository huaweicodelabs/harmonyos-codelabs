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

package com.huawei.cookbook.slice;

import com.huawei.cookbook.MoviesDetailAbility;
import com.huawei.cookbook.ResourceTable;
import com.huawei.cookbook.annotation.Bind;
import com.huawei.cookbook.database.MovieDatabase;
import com.huawei.cookbook.database.MovieInfo;
import com.huawei.cookbook.provider.MoviesListProvider;
import com.huawei.cookbook.util.DatabaseUtils;
import com.huawei.cookbook.util.LogUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.ListContainer;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;

import java.lang.reflect.Field;
import java.util.List;

/**
 * MainAbilitySlice
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final int ERRORCODE = -1;
    private static final String ACTION_MOVIE_DETAIL = "action.movie.detail";

    @Bind(ResourceTable.Id_movies_container)
    private ListContainer moviesListContainer;

    private List<MovieInfo> movieInfoList;

    private MoviesListProvider moviesListProvider;
    private DatabaseHelper helper;

    private OrmContext connect;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initViewAnnotation();
        initMoviesContainer();
        initListener();
    }

    private void initViewAnnotation() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            Bind bind = field.getAnnotation(Bind.class);
            if (bind != null) {
                if (bind.value() == ERRORCODE) {
                    LogUtils.error("TAG", "bind.value must set!");
                    return;
                }
                try {
                    field.setAccessible(true);
                    field.set(this, findComponentById(bind.value()));
                } catch (IllegalAccessException e) {
                    LogUtils.error("TAG", "IllegalAccessException :" + e.getMessage());
                }
            }
        }
    }

    private void initMoviesContainer() {
        // 获取数据库连接
        helper = new DatabaseHelper(this);
        connect = helper.getOrmContext("MovieDatabase", "MovieDatabase.db", MovieDatabase.class);
        // 查询电影列表
        movieInfoList = DatabaseUtils.queryMovieList(this, null);
        // 创建MoviesListProvider对象，用于列表的展示
        moviesListProvider = new MoviesListProvider(movieInfoList, this);
        moviesListContainer.setItemProvider(moviesListProvider);
    }

    private void initListener() {
        moviesListContainer.setItemClickedListener((listContainer, component, position, id) -> {
            getUITaskDispatcher().asyncDispatch(new Runnable() {
                @Override
                public void run() {
                    // 跳转到电影详情页，携带参数，电影id
                    toAnotherPage(position);
                }
            });
        });
    }

    private void toAnotherPage(int position) {
        Intent intent = new Intent();
        Operation operation =
                new Intent.OperationBuilder()
                        .withDeviceId("")
                        .withBundleName(getBundleName())
                        .withAbilityName(MoviesDetailAbility.class.getName())
                        .build();
        MovieInfo movieInfo = movieInfoList.get(position);
        startAbility(builedIntent(intent, operation, movieInfo.getId()));
    }

    private Intent builedIntent(Intent intent, Operation operation, long movieId) {
        intent.setOperation(operation);
        intent.setParam("movieId", movieId);
        return intent;
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
