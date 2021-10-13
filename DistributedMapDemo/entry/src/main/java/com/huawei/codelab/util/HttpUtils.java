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

import ohos.app.Context;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.net.HttpResponseCache;
import ohos.net.NetHandle;
import ohos.net.NetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 网络请求管理类
 *
 * @since 2021-03-12
 */
public class HttpUtils {
    private static final String TAG = HttpUtils.class.getName();

    private static final String REQUEST_METHOD_GET = "GET";

    private static final String SSL_PROVIDER_TLS = "TLS";

    private static final int CONNECT_TIME_OUT = 5000;

    private static final int READ_TIME_OUT = 5000;

    private static HttpUtils instance;

    private final HttpResponseCache httpResponseCache;

    private final Context context;

    private final TaskDispatcher globalTaskDispatcher;

    private InputStream dataStream;

    private InputStreamReader inputStreamReader;

    private BufferedReader reader;

    /**
     * 构造方法
     *
     * @param context context
     */
    private HttpUtils(Context context) {
        this.context = context;
        globalTaskDispatcher = context.getGlobalTaskDispatcher(TaskPriority.DEFAULT);
        httpResponseCache = HttpResponseCache.getInstalled();
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
            showError("MAP_KAY不能为空");
        } else {
            globalTaskDispatcher.asyncDispatch(() -> {
                try {
                    doGet(url, callback);
                } catch (IOException e) {
                    showError("网络或API接口请求异常");
                }
            });
        }
    }

    /**
     * get请求
     *
     * @param address request address
     * @param callback response callback
     * @throws IOException Exception
     */
    private void doGet(String address, ResponseCallback callback) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = getNetConnection(address, REQUEST_METHOD_GET);
            StringBuilder resultBuffer = new StringBuilder();
            int reqCode = connection.getResponseCode();
            if (reqCode == HttpURLConnection.HTTP_OK) {
                dataStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(dataStream, StandardCharsets.UTF_8);
                reader = new BufferedReader(inputStreamReader);
                String tempLine;
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                }
                if (callback != null) {
                    context.getUITaskDispatcher().syncDispatch(() -> callback.onSuccess(resultBuffer.toString()));
                }
                if (httpResponseCache != null) {
                    httpResponseCache.flush();
                }
            } else {
                throw new IOException(connection.getResponseMessage());
            }
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            if (callback != null) {
                showError("网络或API接口请求异常");
                LogUtils.error(TAG, "doGet IOException:" + e.getMessage());
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (dataStream != null) {
                dataStream.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * 获取Connection
     *
     * @param strUrl 请求链接
     * @param method 请求方式
     * @return HttpURLConnection对象
     * @throws KeyManagementException exception
     * @throws IOException exception
     * @throws NoSuchAlgorithmException exception
     */
    private HttpURLConnection getNetConnection(String strUrl, String method)
            throws KeyManagementException, IOException, NoSuchAlgorithmException {
        NetManager netManager = NetManager.getInstance(context);
        NetHandle netHandle = netManager.getDefaultNet();
        if (netHandle == null) {
            throw new NullPointerException();
        }
        HttpURLConnection connection;
        SSLContext sslcontext = SSLContext.getInstance(SSL_PROVIDER_TLS);
        sslcontext.init(null, new TrustManager[]{new MyX509TrustManager()}, new java.security.SecureRandom());
        HostnameVerifier ignoreHostnameVerifier = (hostname, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
        URL url = new URL(strUrl);
        URLConnection urlConnection = netHandle.openConnection(url, Proxy.NO_PROXY);
        if (urlConnection instanceof HttpsURLConnection) {
            connection = (HttpsURLConnection) urlConnection;
        } else if (urlConnection instanceof HttpURLConnection) {
            connection = (HttpURLConnection) urlConnection;
        } else {
            throw new NullPointerException();
        }
        connection.setRequestMethod(method);
        connection.setConnectTimeout(CONNECT_TIME_OUT);
        connection.setReadTimeout(READ_TIME_OUT);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();
        return connection;
    }

    private void showError(String content) {
        context.getUITaskDispatcher().asyncDispatch(() -> MapUtils.showToast(context,content));
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

    /**
     * MyX509TrustManager
     *
     * @since 2021-03-12
     */
    private static class MyX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] certificates, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] ax509certificate, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
