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

package com.huawei.camerademo.utils;

import com.huawei.camerademo.ImageAbility;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.SourceDataMalformedException;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * DistributeFileUtil
 *
 * @since 2021-03-08
 */
public class DistributeFileUtil {
    private static final String TAG = DistributeFileUtil.class.getSimpleName();
    private static final int CACHE_SIZE = 1024;
    private static final int IO_END_LEN = -1;
    private static final int DEGREES_90 = 90;

    private static File getDisFile(Context context, String fileName) {
        File distributedDir = context.getDistributedDir();
        File distributedFile;
        if (distributedDir == null) {
            showTip(context, "分布式文件服务异常，目录为空！");
            return null;
        } else {
            distributedFile = new File(distributedDir, fileName);
        }
        return distributedFile;
    }

    /**
     * Copying Images to the Distributed File Service Address
     *
     * @param context context
     * @param sourceFile file
     * @param fileName filename
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public static void copyPicToDistributedDir(Context context, File sourceFile, String fileName) {
        InputStream in = null;
        OutputStream out = null;
        File disPath = getDisFile(context, fileName);
        if (disPath == null) {
            return;
        }
        try {
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(disPath);
            byte[] buffer = new byte[CACHE_SIZE];
            int len;
            while ((len = in.read(buffer)) != IO_END_LEN) {
                out.write(buffer, 0, len);
            }

            // 拷贝成功，启动远程FA
            startRemoteFas(context, disPath.getCanonicalPath());
        } catch (IOException e) {
            LogUtil.error(TAG, "copy error occur while copy");
            showTip(context, "分布式文件服务异常，拷贝异常！");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                LogUtil.error(TAG, "io close exception");
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogUtil.error(TAG, "io close exception");
            }
        }
    }

    /**
     * Reads the distributed file service address image.
     *
     * @param context context
     * @param filePath filePath
     * @return Provides images in forms of pixel matrices
     * @throws SourceDataMalformedException Provides the exception for unsupported image format
     */
    public static PixelMap readToDistributedDir(Context context, String filePath) {
        File disPath = new File(filePath);
        if (disPath == null) {
            return null;
        }
        try {
            ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
            ImageSource imageSource = ImageSource.create(disPath, srcOpts);
            if (imageSource != null) {
                LogUtil.info(TAG, "imageSource != null ok ok ok");
                showTip(context, "imageSource != null ok ok ok");
                ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
                decodingOpts.desiredSize = new Size(0, 0);
                decodingOpts.desiredRegion = new Rect(0, 0, 0, 0);
                decodingOpts.desiredPixelFormat = PixelFormat.ARGB_8888;
                decodingOpts.rotateDegrees = DEGREES_90;
                return imageSource.createPixelmap(decodingOpts);
            } else {
                showTip(context, "imageSource == null error error error");
            }
        } catch (SourceDataMalformedException e) {
            LogUtil.error(TAG, "readToDistributedDir SourceDataMalformedException ");
        }

        return null;
    }

    /**
     * Deleting a Distributed File Service Address
     *
     * @param context context
     * @param fileName fileName
     */
    private void deleteDistributedDir(Context context, String fileName) {
        File disPath = getDisFile(context, fileName);
        if (disPath != null && disPath.exists() && disPath.isFile()) {
            boolean result = disPath.delete();
            showTip(context, "delete :" + (result ? "success" : "fail"));
        } else {
            showTip(context, "No pictures exists in the distributedDir");
        }
    }

    private static void showTip(Context context, String msg) {
        context.getUITaskDispatcher()
                .delayDispatch(
                        new Runnable() {
                            @Override
                            public void run() {
                                ToastDialog toastDialog = new ToastDialog(context);
                                toastDialog.setAutoClosable(false);
                                toastDialog.setContentText(msg);
                                toastDialog.show();
                            }
                        },
                        0);
    }

    /**
     * Start Remote
     *
     * @param context context
     * @param filePath filePath
     */
    private static void startRemoteFas(Context context, String filePath) {
        List<DeviceInfo> deviceInfos =
                DeviceManager.getDeviceList(ohos.distributedschedule.interwork.DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        if (deviceInfos == null || deviceInfos.size() < 1) {
            return;
        }
        Intent[] intents = new Intent[deviceInfos.size()];
        for (int i = 0; i < deviceInfos.size(); i++) {
            Intent intent = new Intent();
            intent.setParam("filePath", filePath);
            Operation operation =
                    new Intent.OperationBuilder()
                            .withDeviceId(deviceInfos.get(i).getDeviceId())
                            .withBundleName(context.getBundleName())
                            .withAbilityName(ImageAbility.class.getName())
                            .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                            .build();
            intent.setOperation(operation);
            intents[i] = intent;
        }
        context.startAbilities(intents);
    }
}
