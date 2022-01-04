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

package com.huawei.maildemo.bean;

import com.huawei.maildemo.utils.LogUtil;

import ohos.aafwk.content.IntentParams;

import java.util.List;

/**
 * Mail data
 *
 * @since 2021-02-04
 */
public class MailDataBean {
    private static final String ARGS_RECEIVER = "receiver";

    private static final String ARGS_CC = "cc";

    private static final String ARGS_TITLE = "title";

    private static final String ARGS_CONTENT = "content";

    private static final String ARGS_PIC_LIST = "pic_list";

    private String receiver;

    private String cc;

    private String title;

    private String content;

    private List<String> pictureDataList;

    /**
     * ConstructorQ
     */
    public MailDataBean() {
        super();
    }

    /**
     * Constructor with IntentParams
     *
     * @param params IntentParams
     */
    @SuppressWarnings("unchecked")
    public MailDataBean(IntentParams params) {
        if (params == null) {
            LogUtil.info(this.getClass(), "Invalid intent params, can't create MailDataBean");
            return;
        }

        this.receiver = getStringParam(params, ARGS_RECEIVER);
        this.cc = getStringParam(params, ARGS_CC);
        this.title = getStringParam(params, ARGS_TITLE);
        this.content = getStringParam(params, ARGS_CONTENT);
        this.pictureDataList = (List<String>) params.getParam(ARGS_PIC_LIST);
    }

    private String getStringParam(IntentParams intentParams, String key) {
        Object value = intentParams.getParam(key);
        if ((value instanceof String)) {
            return (String) value;
        }
        return "";
    }

    /**
     * MailDataBean to IntentParams
     *
     * @param params intent params
     */
    public void saveDataToParams(IntentParams params) {
        params.setParam(ARGS_RECEIVER, this.receiver == null ? "" : this.receiver);
        params.setParam(ARGS_CC, this.cc == null ? "" : this.cc);
        params.setParam(ARGS_TITLE, this.title == null ? "" : this.title);
        params.setParam(ARGS_CONTENT, this.content == null ? "" : this.content);
        params.setParam(ARGS_PIC_LIST, this.pictureDataList == null ? null : this.pictureDataList);
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPictureDataList() {
        return pictureDataList;
    }

    public void setPictureDataList(List<String> pictureDataList) {
        this.pictureDataList = pictureDataList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }
}
