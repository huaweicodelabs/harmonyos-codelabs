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

package com.huawei.cookbook.util;

/**
 * 人脸认证返回码
 *
 * @since 2021-04-12
 */
public class FaceAuthResult {
    /**
     * 认证成功
     */
    public static final int AUTH_SUCCESS = 0;
    /**
     * AUTH_SUCCESS_MSG
     */
    public static final String AUTH_SUCCESS_MSG = "认证成功";
    /**
     * 认证失败
     */
    public static final int AUTH_FAIL = 1;
    /**
     * AUTH_FAIL_MSG
     */
    public static final String AUTH_FAIL_MSG = "比对失败";
    /**
     * 取消认证
     */
    public static final int AUTH_CANCLE = 2;
    /**
     * AUTH_CANCLE_MSG
     */
    public static final String AUTH_CANCLE_MSG = "取消认证";
    /**
     * 认证超时
     */
    public static final int AUTH_TIME_OUT = 3;
    /**
     * AUTH_TIME_OUT_MSG
     */
    public static final String AUTH_TIME_OUT_MSG = "认证超时";
    /**
     * 打开相机失败
     */
    public static final int AUTH_OPEN_CAMERA_FAIL = 4;
    /**
     * AUTH_OPEN_CAMERA_FAIL_MSG
     */
    public static final String AUTH_OPEN_CAMERA_FAIL_MSG = "打开相机失败";
    /**
     * busy，可能上一个认证没有结束
     */
    public static final int AUTH_BUSY = 5;
    /**
     * AUTH_BUSY_MSG
     */
    public static final String AUTH_BUSY_MSG = "busy，可能上一个认证没有结束";
    /**
     * 入参错误
     */
    public static final int AUTH_PARAM_ERROR = 6;
    /**
     * AUTH_PARAM_ERROR_MSG
     */
    public static final String AUTH_PARAM_ERROR_MSG = "入参错误";
    /**
     * 人脸认证锁定（达到错误认证次数了)
     */
    public static final int AUTH_FACE_LOCKED = 7;
    /**
     * AUTH_FACE_LOCKED_MSG
     */
    public static final String AUTH_FACE_LOCKED_MSG = "人脸认证锁定（达到错误认证次数了)";
    /**
     * 没有录入人脸
     */
    public static final int AUTH_NO_FACE = 8;
    /**
     * AUTH_NO_FACE_MSG
     */
    public static final String AUTH_NO_FACE_MSG = "无人脸录入,请录入人脸。";
    /**
     * 不支持2D人脸识别。
     */
    public static final int AUTH_2D_NOT_SUPPORTED = 9;
    /**
     * AUTH_2D_NOT_SUPPORTED_MSG
     */
    public static final String AUTH_2D_NOT_SUPPORTED_MSG = "不支持2D人脸识别。";
    /**
     * 安全级别不支持
     */
    public static final int AUTH_SAFE_LEVEL_NOT_SUPPORTED = 10;
    /**
     * AUTH_SAFE_LEVEL_NOT_SUPPORTED_MSG
     */
    public static final String AUTH_SAFE_LEVEL_NOT_SUPPORTED_MSG = "安全级别不支持。";
    /**
     * 不是本地认证
     */
    public static final int AUTH_NOT_LOCAL = 11;
    /**
     * AUTH_NOT_LOCAL_MSG
     */
    public static final String AUTH_NOT_LOCAL_MSG = "不是本地认证。";
    /**
     * 其他问题
     */
    public static final int AUTH_OTHER_ERROR = 100;
    /**
     * AUTH_OTHER_ERROR_MSG
     */
    public static final String AUTH_OTHER_ERROR_MSG = "其他错误。";
    /**
     * BASE_MSG
     */
    public static final String BASE_MSG = "开始认证，请将视线对准摄像头。";

    private FaceAuthResult() {
        super();
    }
}
