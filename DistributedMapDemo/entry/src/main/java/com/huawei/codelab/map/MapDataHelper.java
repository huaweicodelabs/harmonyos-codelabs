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

package com.huawei.codelab.map;

import com.huawei.codelab.bean.InputTipsResult;
import com.huawei.codelab.bean.RegionDetailResult;
import com.huawei.codelab.bean.RouteResult;
import com.huawei.codelab.util.GsonUtils;
import com.huawei.codelab.util.HttpUtils;
import com.huawei.codelab.util.LogUtils;
import com.huawei.codelab.util.MapUtils;

import ohos.agp.utils.Point;
import ohos.app.Context;

import java.util.List;

/**
 * 地图导航逻辑控制类
 *
 * @since 2021-03-29
 */
public class MapDataHelper {
    private static final String TAG = MapDataHelper.class.getName();

    private String location;

    private NavMap navMap;

    private Context context;

    private DataCallBack dataCallBack;

    private String localCityCode;

    /**
     * 构造方法
     *
     * @param navMap navMap
     * @param context context
     */
    public MapDataHelper(NavMap navMap, Context context) {
        this.navMap = navMap;
        this.context = context;
    }

    /**
     * 设置路径起点
     *
     * @param startLocation location
     */
    public void setLocation(String startLocation) {
        location = startLocation;
    }

    public void setDataCallBack(DataCallBack dataCallBack) {
        this.dataCallBack = dataCallBack;
    }

    /**
     * 获取本机位置信息
     */
    public void getMyLocation() {
        new LocationHelper().getMyLocation(context, loc -> {
            double locLongitude = loc.getLongitude();
            double locLatitude = loc.getLatitude();
            location = locLongitude + "," + locLatitude;
            if (navMap.getMapElements() == null) {
                setMapCenter(locLongitude, locLatitude);
                getRegionDetail();
            }
        });
    }

    /**
     * 调用高德地图逆地理编码api，获取城市编码
     */
    private void getRegionDetail() {
        String url = String.format(Const.REGION_DETAIL_URL, location, Const.MAP_KEY);
        HttpUtils.getInstance(context).get(url, result -> {
            RegionDetailResult regionDetailResult = GsonUtils.jsonToBean(result, RegionDetailResult.class);
            localCityCode = regionDetailResult.getRegeocode().getAddressComponent().getCitycode();
        });
    }

    /**
     * 调用高德地图输入提示api
     *
     * @param keyWords input content
     */
    public void getInputTips(String keyWords) {
        String url = String.format(Const.INPUT_TIPS_URL, keyWords, Const.MAP_KEY, location, localCityCode);
        HttpUtils.getInstance(context).get(url, result -> {
            InputTipsResult inputTipsResult = GsonUtils.jsonToBean(result, InputTipsResult.class);
            if (inputTipsResult == null) {
                return;
            }
            dataCallBack.setInputTipsView(inputTipsResult.getTips());
            LogUtils.info(TAG, "localCityCode:" + localCityCode);
        });
    }

    /**
     * 调用高德地图路径规划api
     *
     * @param endLocation 终点路径
     */
    public void getRouteResult(String endLocation) {
        String url = String.format(Const.ROUTE_URL, location, endLocation, Const.MAP_KEY);
        HttpUtils.getInstance(context).get(url, result -> dataCallBack.setRouteView(result));
    }

    /**
     * 解析高德地图路径规划api返回的结果，获取导航路径元素集合，并将此集合添加到NavMap对象
     *
     * @param result 高德地图路径规划api返回的结果
     */
    public void parseRoute(String result) {
        navMap.getMapElements().clear();
        RouteResult routeResult = GsonUtils.jsonToBean(result, RouteResult.class);
        List<RouteResult.RouteEntity.PathsEntity> paths = routeResult.getRoute().getPaths();
        RouteResult.RouteEntity.PathsEntity pathsEntity = paths.get(0);
        List<RouteResult.RouteEntity.PathsEntity.StepsEntity> steps = pathsEntity.getSteps();
        for (int i = 0; i < steps.size(); i++) {
            RouteResult.RouteEntity.PathsEntity.StepsEntity stepsEntity = steps.get(i);
            Object action = stepsEntity.getAction();
            String instruction = stepsEntity.getInstruction();
            String polyLine = stepsEntity.getPolyline();
            String[] points = polyLine.split(";");
            for (int j = 0; j < points.length; j++) {
                String[] pointCoordinates = points[j].split(",");
                double[] coordinates = MapUtils.lonLat2Mercator(Double.parseDouble(pointCoordinates[0]),
                    Double.parseDouble(pointCoordinates[1]));

                if (i == 0 && j == 0) {
                    // 添加定位图标元素
                    addElementToMap(coordinates, Const.ROUTE_PEOPLE, "", true);

                    // 添加起点位置元素
                    addElementToMap(coordinates, Const.ROUTE_START, "", true);
                }

                if (j == 0) {
                    // 添加每一个step第一个元素，用于显示导航时提示信息
                    addElementToMap(coordinates, action.toString(), action.toString(), false);
                } else {
                    addElementToMap(coordinates, action.toString(), instruction, false);
                }

                // 添加终点位置元素
                if (i == steps.size() - 1 && j == points.length - 1) {
                    addElementToMap(coordinates, Const.ROUTE_END, "到达终点", true);
                }
            }
        }
    }

    /**
     * 设置地图中心点坐标
     *
     * @param lon Longitude
     * @param lat latitude
     */
    public void setMapCenter(double lon, double lat) {
        double[] mercators = MapUtils.lonLat2Mercator(lon, lat);
        Point centerPoint = new Point((float) mercators[0], (float) mercators[1]);
        navMap.setCenterPoint(centerPoint);
        MapElement peopleEle = new MapElement(centerPoint.getPointX(), centerPoint.getPointY(), true);
        peopleEle.setActionType(Const.ROUTE_PEOPLE);
        navMap.addElement(peopleEle);
    }

    private void addElementToMap(double[] coordinates, String actionType, String content, boolean isImage) {
        MapElement mapElementEnd = new MapElement((float) coordinates[0], (float) coordinates[1], isImage);
        mapElementEnd.setActionType(actionType);
        mapElementEnd.setActionContent(content);
        navMap.addElement(mapElementEnd);
    }

    /**
     * MapInterface
     *
     * @since 2021-03-29
     */
    public interface DataCallBack {
        /**
         * 设置输入提示视图
         *
         * @param tips tips
         */
        void setInputTipsView(List<InputTipsResult.TipsEntity> tips);

        /**
         * 设置导航路径视图
         *
         * @param route route
         */
        void setRouteView(String route);
    }
}
