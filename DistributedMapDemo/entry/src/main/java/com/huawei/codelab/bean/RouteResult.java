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
 * RouteResult
 *
 * @since 2021-03-12
 */
public class RouteResult {
    private RouteEntity route;

    private String count;

    private String infocode;

    private String status;

    private String info;

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

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
     * RouteEntity
     *
     * @since 2021-03-12
     */
    public class RouteEntity {
        private List<PathsEntity> paths;

        private String origin;

        private String destination;

        private String taxiCost;

        public List<PathsEntity> getPaths() {
            return paths;
        }

        public void setPaths(List<PathsEntity> paths) {
            this.paths = paths;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getTaxiCost() {
            return taxiCost;
        }

        public void setTaxiCost(String taxiCost) {
            this.taxiCost = taxiCost;
        }

        /**
         * PathsEntity
         *
         * @since 2021-03-12
         */
        public class PathsEntity {
            private String duration;

            private String distance;

            private String restriction;

            private String tollDistance;

            private String strategy;

            private String trafficLights;

            private List<StepsEntity> steps;

            private String tolls;

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getRestriction() {
                return restriction;
            }

            public void setRestriction(String restriction) {
                this.restriction = restriction;
            }

            public String getTollDistance() {
                return tollDistance;
            }

            public void setTollDistance(String tollDistance) {
                this.tollDistance = tollDistance;
            }

            public String getStrategy() {
                return strategy;
            }

            public void setStrategy(String strategy) {
                this.strategy = strategy;
            }

            public String getTrafficLights() {
                return trafficLights;
            }

            public void setTrafficLights(String trafficLights) {
                this.trafficLights = trafficLights;
            }

            public List<StepsEntity> getSteps() {
                return steps;
            }

            public void setSteps(List<StepsEntity> steps) {
                this.steps = steps;
            }

            public String getTolls() {
                return tolls;
            }

            public void setTolls(String tolls) {
                this.tolls = tolls;
            }

            /**
             * StepsEntity
             *
             * @param <E> type
             * @since 2021-03-12
             */
            public class StepsEntity<E> {
                private String orientation;

                private String distance;

                private List<CitiesEntity> cities;

                private E tollRoad;

                private String tollDistance;

                private String tolls;

                private String duration;

                private E assistantAction;

                private String road;

                private String instruction;

                private E action;

                private String polyline;

                private List<TmcsEntity> tmcs;

                public String getOrientation() {
                    return orientation;
                }

                public void setOrientation(String orientation) {
                    this.orientation = orientation;
                }

                public String getDistance() {
                    return distance;
                }

                public void setDistance(String distance) {
                    this.distance = distance;
                }

                public List<CitiesEntity> getCities() {
                    return cities;
                }

                public void setCities(List<CitiesEntity> cities) {
                    this.cities = cities;
                }

                public E getTollRoad() {
                    return tollRoad;
                }

                public void setTollRoad(E tollRoad) {
                    this.tollRoad = tollRoad;
                }

                public String getTollDistance() {
                    return tollDistance;
                }

                public void setTollDistance(String tollDistance) {
                    this.tollDistance = tollDistance;
                }

                public String getTolls() {
                    return tolls;
                }

                public void setTolls(String tolls) {
                    this.tolls = tolls;
                }

                public String getDuration() {
                    return duration;
                }

                public void setDuration(String duration) {
                    this.duration = duration;
                }

                public E getAssistantAction() {
                    return assistantAction;
                }

                public void setAssistantAction(E assistantAction) {
                    this.assistantAction = assistantAction;
                }

                public String getRoad() {
                    return road;
                }

                public void setRoad(String road) {
                    this.road = road;
                }

                public String getInstruction() {
                    return instruction;
                }

                public void setInstruction(String instruction) {
                    this.instruction = instruction;
                }

                public E getAction() {
                    return action;
                }

                public void setAction(E action) {
                    this.action = action;
                }

                public String getPolyline() {
                    return polyline;
                }

                public void setPolyline(String polyline) {
                    this.polyline = polyline;
                }

                public List<TmcsEntity> getTmcs() {
                    return tmcs;
                }

                public void setTmcs(List<TmcsEntity> tmcs) {
                    this.tmcs = tmcs;
                }

                /**
                 * CitiesEntity
                 *
                 * @since 2021-03-12
                 */
                public class CitiesEntity {
                    private String citycode;

                    private String adcode;

                    private String name;

                    private List<DistrictsEntity> districts;

                    public String getCitycode() {
                        return citycode;
                    }

                    public void setCitycode(String citycode) {
                        this.citycode = citycode;
                    }

                    public String getAdcode() {
                        return adcode;
                    }

                    public void setAdcode(String adcode) {
                        this.adcode = adcode;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public List<DistrictsEntity> getDistricts() {
                        return districts;
                    }

                    public void setDistricts(List<DistrictsEntity> districts) {
                        this.districts = districts;
                    }

                    /**
                     * DistrictsEntity
                     *
                     * @since 2021-03-12
                     */
                    public class DistrictsEntity {
                        private String adcode;

                        private String name;

                        public String getAdcode() {
                            return adcode;
                        }

                        public void setAdcode(String adcode) {
                            this.adcode = adcode;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }
                    }
                }

                /**
                 * TmcsEntity
                 *
                 * @since 2021-03-12
                 */
                public class TmcsEntity {
                    private String distance;

                    private E lcode;

                    private String polyline;

                    private String status;

                    public String getDistance() {
                        return distance;
                    }

                    public void setDistance(String distance) {
                        this.distance = distance;
                    }

                    public E getLcode() {
                        return lcode;
                    }

                    public void setLcode(E lcode) {
                        this.lcode = lcode;
                    }

                    public String getPolyline() {
                        return polyline;
                    }

                    public void setPolyline(String polyline) {
                        this.polyline = polyline;
                    }

                    public String getStatus() {
                        return status;
                    }

                    public void setStatus(String status) {
                        this.status = status;
                    }
                }
            }
        }
    }
}
