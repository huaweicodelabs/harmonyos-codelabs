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

package com.huawei.metadatabindingdemo.alarm.db;

import com.huawei.metadatabindingdemo.custom_data_source.db.Note;

import ohos.data.orm.OrmDatabase;
import ohos.data.orm.annotation.Database;

/**
 * An ORM Database
 *
 * @since 2021-05-15
 */
@Database(entities = {Alarm.class, Note.class}, version = 1)
public abstract class AlarmOrmDatabase extends OrmDatabase {
    /**
     * Configuring this Database's name.
     */
    public static final String DATABASE_NAME = "AlarmOrmDatabase.db";

    /**
     * Configuring this Database's alias.
     */
    public static final String DATABASE_NAME_ALIAS = "AlarmOrmDatabase";
}