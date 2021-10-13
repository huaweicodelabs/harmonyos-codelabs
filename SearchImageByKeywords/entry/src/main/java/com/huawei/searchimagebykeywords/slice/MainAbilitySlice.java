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

package com.huawei.searchimagebykeywords.slice;

import com.huawei.searchimagebykeywords.ResourceTable;
import com.huawei.searchimagebykeywords.helper.WidgetHelper;
import com.huawei.searchimagebykeywords.provider.PictureProvider;
import com.huawei.searchimagebykeywords.util.TypeUtil;
import com.huawei.searchimagebykeywords.util.WindowUtil;
import com.huawei.searchimagebykeywords.util.WordRecognition;
import com.huawei.searchimagebykeywords.util.WordSegment;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.components.TextField;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 主页面
 *
 * @since 2021-01-21
 *
 */
public class MainAbilitySlice extends AbilitySlice {
    private static final int LIST_CONTAINER_ID_SHOW = ResourceTable.Id_picture_list_show;
    private static final int LIST_CONTAINER_ID_MATCH = ResourceTable.Id_picture_list_match;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private Context slice;
    private MyEventHandle myEventHandle;
    private final int[] pictureLists = new int[]{ResourceTable.Media_1, ResourceTable.Media_2,
        ResourceTable.Media_3, ResourceTable.Media_4, ResourceTable.Media_5,
        ResourceTable.Media_6, ResourceTable.Media_7, ResourceTable.Media_8};
    private Button button;
    private TextField textField;
    private Map<Integer, String> imageInfoMap;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        slice = MainAbilitySlice.this;

        // 展示图片列表
        setSelectPicture(pictureLists, LIST_CONTAINER_ID_SHOW);

        // 所有图片通用文字识别
        wordRecognition();

        // 设置需要分词的语句
        Component componentText = findComponentById(ResourceTable.Id_word_seg_text);
        if (componentText instanceof TextField) {
            textField = (TextField) componentText;
        }

        // 点击按钮进行文字识别
        Component componentSearch = findComponentById(ResourceTable.Id_button_search);
        if (componentSearch instanceof Button) {
            button = (Button) componentSearch;
            button.setClickedListener(listener -> {
                if (textField.getText() == null || "".equals(textField.getText())) {
                    WidgetHelper.showTips(this, "请输入关键词");
                }
                wordSegment();
                WindowUtil.clearFocus(button);
            });
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

    // 设置图片选择区域
    private void setSelectPicture(int[] pictures, int id) {
        // 获取图片
        PictureProvider newsTypeAdapter = new PictureProvider(pictures, this);

        Component componentById = findComponentById(id);
        if (componentById instanceof ListContainer) {
            ListContainer listContainer = (ListContainer) componentById;
            listContainer.setItemProvider(newsTypeAdapter);
        }
    }

    // 通用文字识别
    private void wordRecognition() {
        initHandler();
        WordRecognition wordRecognition = new WordRecognition();
        wordRecognition.setParams(slice, pictureLists, myEventHandle);
        wordRecognition.sendResult(null);
    }

    // 分词
    private void wordSegment() {
        // 组装关键词，作为分词对象
        String requestData = "{\"text\":" + textField.getText() + ",\"type\":0}";
        initHandler();
        new WordSegment().wordSegment(slice, requestData, myEventHandle);
    }

    // 匹配图片
    private void matchImage(List<String> list) {
        Set<Integer> matchSets = new HashSet<>();
        for (String str: list) {
            for (Integer key : imageInfoMap.keySet()) {
                if (imageInfoMap.get(key).contains(str)) {
                    matchSets.add(key);
                }
            }
        }

        // 获得匹配的图片
        int[] matchPictures = new int[matchSets.size()];
        int index = 0;
        for (int match: matchSets) {
            matchPictures[index] = match;
            index++;
        }

        // 展示图片
        setSelectPicture(matchPictures, LIST_CONTAINER_ID_MATCH);
    }

    private void initHandler() {
        EventRunner runner = EventRunner.getMainEventRunner();
        if (runner == null) {
            return;
        }
        myEventHandle = new MyEventHandle(runner);
    }

    /**
     * 事件Handle
     *
     * @since 2021-01-15
     *
     */
    public class MyEventHandle extends EventHandler {
        MyEventHandle(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            int eventId = event.eventId;
            if (eventId == ONE) {
                // 通用文字识别
                if (event.object instanceof Map) {
                    Optional<Map<Integer, String>> optionalMap =
                            TypeUtil.castMap(event.object, Integer.class, String.class);
                    optionalMap.ifPresent(integerStringMap -> imageInfoMap = integerStringMap);
                }
            }
            if (eventId == TWO) {
                // 分词
                if (event.object instanceof List) {
                    Optional<List<String>> optionalStringList = TypeUtil.castList(event.object, String.class);
                    if (optionalStringList.isPresent() && optionalStringList.get().size() > ZERO
                            && (!"no keywords".equals(optionalStringList.get().get(ZERO)))) {
                        List<String> lists = optionalStringList.get();
                        matchImage(lists);
                    }
                }
            }
        }
    }
}