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

    private String info;

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
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
    public static class RouteEntity {
        private List<PathsEntity> paths;

        public List<PathsEntity> getPaths() {
            return paths;
        }

        public void setPaths(List<PathsEntity> paths) {
            this.paths = paths;
        }

        /**
         * PathsEntity
         *
         * @since 2021-03-12
         */
        public class PathsEntity {
            private List<StepsEntity> steps;

            public List<StepsEntity> getSteps() {
                return steps;
            }

            public void setSteps(List<StepsEntity> steps) {
                this.steps = steps;
            }

            /**
             * StepsEntity
             *
             * @param <E> type
             * @since 2021-03-12
             */
            public class StepsEntity<E> {
                private String instruction;

                private E action;

                private String polyline;

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
            }
        }
    }
}
