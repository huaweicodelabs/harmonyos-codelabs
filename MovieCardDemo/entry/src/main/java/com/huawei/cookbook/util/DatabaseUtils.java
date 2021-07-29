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

package com.huawei.cookbook.util;

import com.huawei.cookbook.database.FormInfo;
import com.huawei.cookbook.database.MovieDatabase;
import com.huawei.cookbook.database.MovieInfo;

import ohos.app.Context;
import ohos.data.DatabaseHelper;
import ohos.data.orm.OrmContext;
import ohos.data.orm.OrmPredicates;

import java.util.List;

/**
 * DatabaseUtils
 */
public class DatabaseUtils {
    private static final String MOVIE_ID = "id";
    private static final String FORM_ID = "formId";

    private DatabaseUtils() {
    }

    /**
     * insert service widget information
     * @param context context
     * @param formInfo formInfo
     * @return 是否成功
     */
    public static boolean insertForm(Context context, FormInfo formInfo) {
        OrmContext connect = getConnect(context);
        boolean isSuccess = connect.insert(formInfo);
        if (isSuccess) {
            isSuccess = connect.flush();
        }
        return isSuccess;
    }

    /**
     * query forms
     * @param context context
     * @param formId form id
     * @return forms
     */
    public static List<FormInfo> queryForms(Context context, Long formId) {
        OrmContext connect = getConnect(context);
        OrmPredicates where = connect.where(FormInfo.class);
        if (formId != null) {
            where.equalTo(FORM_ID, formId);
        }
        return connect.query(where);
    }

    /**
     * delete form by id
     * @param context context
     * @param formId formId
     */
    public static void deleteForm(Context context, Long formId) {
        OrmContext connect = getConnect(context);
        OrmPredicates where = connect.where(FormInfo.class);
        where.equalTo(FORM_ID, formId);
        connect.delete(where);
    }

    /**
     * insert movie information
     * @param context context
     * @param movieInfo movieInfo
     * @return 是否成功
     */
    public static boolean insertMovieInfo(Context context, MovieInfo movieInfo) {
        OrmContext connect = getConnect(context);
        boolean isSuccess = connect.insert(movieInfo);
        if (isSuccess) {
            isSuccess = connect.flush();
        }
        return isSuccess;
    }

    /**
     * query movie count
     * @param context context
     * @param movieId movie id
     * @return count
     */
    public static Long queryMovieCount(Context context, Long movieId) {
        OrmContext connect = getConnect(context);
        OrmPredicates where = connect.where(MovieInfo.class);
        if (movieId != null) {
            where.equalTo(MOVIE_ID, movieId);
        }
        Long count = connect.count(where);
        return count;
    }

    /**
     * get movies
     * @param context context
     * @param movieId movie id
     * @return movie list
     */
    public static List<MovieInfo> queryMovieList(Context context, Long movieId) {
        OrmContext connect = getConnect(context);
        OrmPredicates where = connect.where(MovieInfo.class);
        if (movieId != null) {
            where.equalTo(MOVIE_ID, movieId);
        }
        return connect.query(where);
    }

    /**
     * delete movie by id
     * @param context context
     * @param movieId movieId
     */
    public static void deletMovie(Context context, Long movieId) {
        OrmContext connect = getConnect(context);
        OrmPredicates where = connect.where(MovieInfo.class);
        if (movieId != null) {
            where.equalTo(MOVIE_ID, movieId);
        }
        connect.delete(where);
    }

    private static OrmContext getConnect(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        return helper.getOrmContext("MovieDatabase", "MovieDatabase.db", MovieDatabase.class);
    }
}
