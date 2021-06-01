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

package com.huawei.codelab.util;

import com.huawei.codelab.map.Const;

import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/**
 * 网络请求管理类
 *
 * @since 2021-03-12
 */
public class HttpUtils {
    private static final String TAG = HttpUtils.class.getName();

    private static HttpUtils instance;

    private Context context;

    private TaskDispatcher globalTaskDispatcher;

    /**
     * 构造方法
     *
     * @param context context
     */
    private HttpUtils(Context context) {
        this.context = context;
        globalTaskDispatcher = context.getGlobalTaskDispatcher(TaskPriority.DEFAULT);
    }

    /**
     * 获取NetworkManager单例对象
     *
     * @param context context
     * @return NetworkManager
     */
    public static synchronized HttpUtils getInstance(Context context) {
        if (instance == null) {
            instance = new HttpUtils(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * get
     *
     * @param url url
     * @param callback callback
     */
    public void get(String url, ResponseCallback callback) {
        if (Const.MAP_KEY.isEmpty()) {
            showError("MAP_KAY cannot be empty");
        } else {
            globalTaskDispatcher.asyncDispatch(() -> {
                try {
                    doGet(url, callback);
                } catch (IOException e) {
                    showError(e.getMessage());
                }
            });
        }
    }

    /**
     * get请求
     *
     * @param address request address
     * @param callback response callback
     * @throws IOException
     */
    private void doGet(String address, ResponseCallback callback) throws IOException {
        LogUtils.info(TAG, "doGet:");
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
            } else {
                doGet(address, callback);
                return;
            }
            inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder(0);
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String result = stringBuilder.toString();
            LogUtils.info(TAG, "result:" + result);
            context.getUITaskDispatcher().syncDispatch(() -> callback.onSuccess(result));
        } catch (IOException e) {
            showError(e.getMessage());
            LogUtils.error(TAG, "doGet IOException:" + e.getMessage());
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private void showError(String content) {
        context.getUITaskDispatcher().asyncDispatch(new Runnable() {
            @Override
            public void run() {
                ToastDialog toastDialog = new ToastDialog(context);
                toastDialog.setAutoClosable(false);
                toastDialog.setContentText(content);
                toastDialog.setAlignment(LayoutAlignment.CENTER);
                toastDialog.show();
            }
        });
    }

    /**
     * 网络请求成功回调接口
     *
     * @since 2021-03-12
     */
    public interface ResponseCallback {
        /**
         * onSuccess
         *
         * @param result result
         */
        void onSuccess(String result);
    }
}
