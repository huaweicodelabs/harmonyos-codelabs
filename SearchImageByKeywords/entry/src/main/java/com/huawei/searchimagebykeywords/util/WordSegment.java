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

package com.huawei.searchimagebykeywords.util;

import com.huawei.searchimagebykeywords.slice.MainAbilitySlice;

import ohos.ai.nlu.NluClient;
import ohos.ai.nlu.NluRequestType;
import ohos.ai.nlu.ResponseResult;
import ohos.app.Context;
import ohos.eventhandler.InnerEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 分词
 *
 * @since 2021-01-21
 *
 */
public class WordSegment {
    private static final boolean IS_ASYNC = true;
    private static final String WORDS = "words";
    private static final int ZERO = 0;
    private static final int TWO = 2;
    private static final int STEP = 8;
    private Context slice;
    private MainAbilitySlice.MyEventHandle handle;

    /**
     * 分词方法
     *
     * @param context 上下文对象
     * @param requestData 输入的关键词
     * @param myEventHandle MyEventHandle对象
     * @since 2021-01-21
     *
     */
    public void wordSegment(Context context, String requestData, MainAbilitySlice.MyEventHandle myEventHandle) {
        slice = context;
        handle = myEventHandle;

        // 使用NluClient静态类进行初始化，通过异步方式获取服务的连接。
        NluClient.getInstance().init(context, resultCode -> {
            if (!IS_ASYNC) {
                // 分词同步方法
                ResponseResult responseResult = NluClient.getInstance().getWordSegment(requestData,
                        NluRequestType.REQUEST_TYPE_LOCAL);
                sendResult(responseResult.getResponseResult());
                release();
            } else {
                // 分词异步方法
                wordSegmentAsync(requestData);
            }
        }, true);
    }

    private void wordSegmentAsync(String requestData) {
        NluClient.getInstance().getWordSegment(requestData,
            NluRequestType.REQUEST_TYPE_LOCAL, asyncResult -> {
                sendResult(asyncResult.getResponseResult());
                release();
            });
    }

    private void sendResult(String result) {
        List<String> lists = null; // 分词识别结果
        // 将result中分词结果转换成list
        if (result.contains("\"message\":\"success\"")) {
            String words = result.substring(result.indexOf(WORDS) + STEP,
                    result.lastIndexOf("]")).replaceAll("\"", "");
            if ((words == null) || ("".equals(words))) {
                lists = new ArrayList<>(1); // 未识别到分词结果，返回"no keywords"
                lists.add("no keywords");
            } else {
                lists = Arrays.asList(words.split(","));
            }
        }

        InnerEvent event = InnerEvent.get(TWO, ZERO, lists);
        handle.sendEvent(event);
    }

    private void release() {
        NluClient.getInstance().destroy(slice);
    }
}