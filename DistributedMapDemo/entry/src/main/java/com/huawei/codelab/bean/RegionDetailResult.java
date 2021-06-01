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
 * RegionDetailResult
 *
 * @since 2021-03-12
 */
public class RegionDetailResult {
    private String infocode;

    private RegeocodeEntity regeocode;

    private String status;

    private String info;

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public RegeocodeEntity getRegeocode() {
        return regeocode;
    }

    public void setRegeocode(RegeocodeEntity regeocode) {
        this.regeocode = regeocode;
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
     * RegeocodeEntity
     *
     * @since 2021-03-12
     */
    public class RegeocodeEntity {
        private String formattedAddress;

        private AddressComponentEntity addressComponent;

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public void setFormattedAddress(String formattedAddress) {
            this.formattedAddress = formattedAddress;
        }

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
        public class AddressComponentEntity {
            private List<List<?>> businessAreas;

            private String country;

            private String province;

            private String citycode;

            private String city;

            private String adcode;

            private StreetNumberEntity streetNumber;

            private String towncode;

            private String district;

            private BuildingEntity neighborhood;

            private String township;

            private BuildingEntity building;

            public List<List<?>> getBusinessAreas() {
                return businessAreas;
            }

            public void setBusinessAreas(List<List<?>> businessAreas) {
                this.businessAreas = businessAreas;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCitycode() {
                return citycode;
            }

            public void setCitycode(String citycode) {
                this.citycode = citycode;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getAdcode() {
                return adcode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public StreetNumberEntity getStreetNumber() {
                return streetNumber;
            }

            public void setStreetNumber(StreetNumberEntity streetNumber) {
                this.streetNumber = streetNumber;
            }

            public String getTowncode() {
                return towncode;
            }

            public void setTowncode(String towncode) {
                this.towncode = towncode;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public BuildingEntity getNeighborhood() {
                return neighborhood;
            }

            public void setNeighborhood(BuildingEntity neighborhood) {
                this.neighborhood = neighborhood;
            }

            public String getTownship() {
                return township;
            }

            public void setTownship(String township) {
                this.township = township;
            }

            public BuildingEntity getBuilding() {
                return building;
            }

            public void setBuilding(BuildingEntity building) {
                this.building = building;
            }

            /**
             * StreetNumberEntity
             *
             * @since 2021-03-12
             */
            public class StreetNumberEntity {
                private String number;

                private String distance;

                private String street;

                private String location;

                private String direction;

                public String getNumber() {
                    return number;
                }

                public void setNumber(String number) {
                    this.number = number;
                }

                public String getDistance() {
                    return distance;
                }

                public void setDistance(String distance) {
                    this.distance = distance;
                }

                public String getStreet() {
                    return street;
                }

                public void setStreet(String street) {
                    this.street = street;
                }

                public String getLocation() {
                    return location;
                }

                public void setLocation(String location) {
                    this.location = location;
                }

                public String getDirection() {
                    return direction;
                }

                public void setDirection(String direction) {
                    this.direction = direction;
                }
            }

            /**
             * BuildingEntity
             *
             * @since 2021-03-12
             */
            public class BuildingEntity {
                private List<?> name;

                private List<?> type;

                public List<?> getName() {
                    return name;
                }

                public void setName(List<?> name) {
                    this.name = name;
                }

                public List<?> getType() {
                    return type;
                }

                public void setType(List<?> type) {
                    this.type = type;
                }
            }
        }
    }
}
