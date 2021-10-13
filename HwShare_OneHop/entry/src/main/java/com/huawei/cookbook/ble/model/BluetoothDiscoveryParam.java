/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package com.huawei.cookbook.ble.model;

/**
 * Bluetooth Discovery Param
 */
public class BluetoothDiscoveryParam {
    private String[] services;
    private String macAddress;
    private int interval;

    /**
     * whether allow duplicates key
     *
     * @return is allow or not
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * set whether allow duplicates key
     *
     * @param macAddress allowDuplicatesKey
     */
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * get time of interval
     *
     * @return time of interval
     */
    public int getInterval() {
        return interval;
    }

    /**
     * set interval time
     *
     * @param interval interval time
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * get array of uuid
     *
     * @return array of uuid
     */
    public String[] getServices() {
        return services.clone();
    }

    /**
     * set array of uuid
     *
     * @param services array of uuid
     */
    public void setServices(String[] services) {
        this.services = services.clone();
    }
}
