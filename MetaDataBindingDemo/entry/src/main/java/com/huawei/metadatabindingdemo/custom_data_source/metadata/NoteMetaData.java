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

package com.huawei.metadatabindingdemo.custom_data_source.metadata;

import com.huawei.metadatabindingdemo.MyLog;

import ohos.mp.metadata.binding.metadata.MetaData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This MetaData indicates a row of note dataset, wraps the information of a note.
 * This class is used in Note List binding demo.
 * For the MetaData content see: src/main/resources/rawfile/jsonschema/note_schema.json.
 *
 * @since 2021-05-15
 */
public class NoteMetaData extends MetaData {
    private static final int UNIT_1000 = 1000;
    private static final long UNIT_1000_LONG = 1000L;
    private static final int UNIT_60 = 60;

    /**
     * Conversion Format
     *
     * @param createTime Note create time
     * @return Note create time show format
     */
    public String getTime(int createTime) {
        long nowTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(nowTime));
        Date date = null;
        try {
            date = sdf.parse(sd);
        } catch (ParseException e) {
            MyLog.error("NoteMetaData getTime exception");
        }
        assert date != null;
        int todayTime = (int) (date.getTime() / UNIT_1000);
        int howLongPass = (int) (nowTime / UNIT_1000 - createTime);
        if (howLongPass < UNIT_60) {
            return "Just now";
        } else if (howLongPass < UNIT_60 * UNIT_60) {
            return (howLongPass / UNIT_60) + " minutes ago";
        } else if (createTime > todayTime) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("a hh:mm");
            long createTimeL = createTime * UNIT_1000_LONG;
            return sdf1.format(createTimeL);
        } else {
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            long createTimeL = createTime * UNIT_1000_LONG;
            return sdf2.format(createTimeL);
        }
    }
}