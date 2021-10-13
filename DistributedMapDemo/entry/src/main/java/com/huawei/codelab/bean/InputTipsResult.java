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

package com.huawei.codelab.bean;

import java.util.List;

/**
 * InputTipsResult
 *
 * @since 2021-03-12
 */
public class InputTipsResult {
    private List<TipsEntity> tips;

    private String info;

    public List<TipsEntity> getTips() {
        return tips;
    }

    public void setTips(List<TipsEntity> tips) {
        this.tips = tips;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * TipsEntity
     *
     * @param <E> type
     * @since 2021-03-12
     */
    public static class TipsEntity<E> {
        private E address;

        private E adcode;

        private String name;

        private String location;

        public E getAddress() {
            return address;
        }

        public void setAddress(E address) {
            this.address = address;
        }

        public E getAdcode() {
            return adcode;
        }

        public void setAdcode(E adcode) {
            this.adcode = adcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
