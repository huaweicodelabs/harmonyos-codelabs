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
    private String count;

    private String infocode;

    private List<TipsEntity> tips;

    private String status;

    private String info;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public List<TipsEntity> getTips() {
        return tips;
    }

    public void setTips(List<TipsEntity> tips) {
        this.tips = tips;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        private String typecode;

        private E address;

        private E adcode;

        private String name;

        private String location;

        public String getTypecode() {
            return typecode;
        }

        public E getAddress() {
            return address;
        }

        public E getAdcode() {
            return adcode;
        }

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }
    }
}
