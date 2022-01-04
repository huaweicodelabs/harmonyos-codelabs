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

import static ohos.agp.components.Component.HIDE;
import static ohos.agp.components.Component.VISIBLE;

import com.huawei.cookbook.ResourceTable;
import com.huawei.cookbook.annotation.Bind;
import com.huawei.cookbook.database.MovieInfo;
import com.huawei.cookbook.util.CommonUtils;
import com.huawei.cookbook.util.DatabaseUtils;
import com.huawei.cookbook.util.LogUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.CommonDialog;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.utils.zson.ZSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * News detail slice
 */
public class MoviesDetailAbilitySlice extends AbilitySlice {
    private static final int MMAXLINE = 3;
    private static final int NUM = -1;
    private static final int WIDTH = 150;
    private static final int HIGHT = 230;
    private static final int RADIUS = 50;
    private boolean mIsExpansion;

    @Bind(ResourceTable.Id_parent_layout)
    private DependentLayout parentLayout;

    @Bind(ResourceTable.Id_read_num)
    private Text moviesRating;

    @Bind(ResourceTable.Id_like_num)
    private Text moviesCommentCount;

    @Bind(ResourceTable.Id_title_text)
    private Text moviesTitle;

    @Bind(ResourceTable.Id_title_content)
    private Text moviesIntroduction;

    @Bind(ResourceTable.Id_v_expansion)
    private Text mExpansionButton;

    @Bind(ResourceTable.Id_image_content)
    private Image moviesImage;

    private CommonDialog dialog;

    private String title;
    private Long image;
    private String rating;
    private String type;
    private String introduction;
    private String commentCount;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_movies_detail_layout);
        initParam(intent);
        initView();
    }

    /**
     * init params
     * @param intent intent
     */
    public void initParam(Intent intent) {
        // 获取携带的电影id参数
        if (intent.hasParameter("movieId")) {
            Long movieId = intent.getLongParam("movieId", 0);
            if (movieId == 0) {
                return;
            }
            LogUtils.info(MoviesDetailAbilitySlice.class.getName(),"initParam context"+this.getContext());
            // 根据电影id查询电影信息
            List<MovieInfo> movies = DatabaseUtils.queryMovieList(this, movieId);
            LogUtils.info(MoviesDetailAbilitySlice.class.getName(),"movies："+ movies.size());
            if (movies.size() <= 0) {
                terminate();
                return;
            }
            MovieInfo movieInfo = movies.get(0);
            LogUtils.info(MoviesDetailAbilitySlice.class.getName(),"movieInfo："+ ZSONObject.toZSONString(movieInfo));
            title = movieInfo.getTitle();
            image = movieInfo.getImgUrl();
            rating = movieInfo.getRating();
            type = movieInfo.getType();
            introduction = movieInfo.getIntroduction();
            commentCount = movieInfo.getCommentcount();
        }
    }

    private void initView() {
        initViewAnnotation();
        moviesRating.setText(rating);
        try {
            moviesCommentCount.setText(getResourceManager().getElement(ResourceTable.String_movie_type).getString()+": " + type);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (WrongTypeException e) {
            e.printStackTrace();
        }
        moviesTitle.setText(title);
        // CommonUtils.getPixelMapFromPath(this, image, WIDTH, HIGHT).get()
        moviesImage.setPixelMap(image.intValue());
        moviesImage.setCornerRadius(RADIUS);
        setText(introduction);
        mExpansionButton.setVisibility(VISIBLE);
        mExpansionButton.setClickedListener(va -> {
            toggleExpansionStatus();
        });
    }

    private void toggleExpansionStatus() {
        ResourceManager resourceManager = getResourceManager();
        mIsExpansion = !mIsExpansion;
        if (mIsExpansion) {
            try {
                mExpansionButton.setText(resourceManager.getElement(ResourceTable.String_tucked).getString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NotExistException e) {
                e.printStackTrace();
            } catch (WrongTypeException e) {
                e.printStackTrace();
            }
            moviesIntroduction.setMaxTextLines(Integer.MAX_VALUE);
        } else {
            try {
                mExpansionButton.setText(resourceManager.getElement(ResourceTable.String_full_text).getString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NotExistException e) {
                e.printStackTrace();
            } catch (WrongTypeException e) {
                e.printStackTrace();
            }
            moviesIntroduction.setMaxTextLines(MMAXLINE);
        }
    }

    /**
     * setText
     *
     * @param text text
     */
    public void setText(String text) {
        moviesIntroduction.setMaxTextLines(Integer.MAX_VALUE);
        moviesIntroduction.setText(text);
        int lineCount = moviesIntroduction.getMaxTextLines();
        if (lineCount > MMAXLINE) {
            mExpansionButton.setVisibility(VISIBLE);
            moviesIntroduction.setMaxTextLines(MMAXLINE);
            mIsExpansion = false;
        } else {
            mExpansionButton.setVisibility(HIDE);
        }
    }

    private void initViewAnnotation() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            Bind bind = field.getAnnotation(Bind.class);
            if (bind != null) {
                if (bind.value() == NUM) {
                    LogUtils.error("TAG", "bind value must be set!");
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

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        title = null;
        image = null;
        rating = null;
        type = null;
        introduction = null;
        commentCount = null;
    }
}
