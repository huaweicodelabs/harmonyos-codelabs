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

/**
 * RegionDetailResult
 *
 * @since 2021-03-12
 */
public class RegionDetailResult {
    private RegeocodeEntity regeocode;

    public RegeocodeEntity getRegeocode() {
        return regeocode;
    }

    public void setRegeocode(RegeocodeEntity regeocode) {
        this.regeocode = regeocode;
    }

    /**
     * RegeocodeEntity
     *
     * @since 2021-03-12
     */
    public static class RegeocodeEntity {
        private AddressComponentEntity addressComponent;

        public AddressComponentEntity getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponentEntity addressComponent) {
            this.addressComponent = addressComponent;
        }

        /**
         * AddressComponentEntity
         *
         * @since 2021-03-12
         */
        public static class AddressComponentEntity {
            private String citycode;

            public String getCitycode() {
                return citycode;
            }

            public void setCitycode(String citycode) {
                this.citycode = citycode;
            }
        }
    }
}
