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

package com.huawei.cookbook;

import com.huawei.cookbook.util.ShareFaManager;
import com.huawei.cookbook.util.ShareInfo;

import ohos.app.AbilityContext;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.interwork.utils.PacMapEx;
import ohos.utils.zson.ZSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * huawei share
 */
public class HwShareJSInterface {
    private static final String APP_ID = "538229280904063872";
    private static final int BYTE_LENGTH = 1000;
    private static final int READ_FINISH = -1;
    private static final String LOG_FORMAT = "%{public}s: %{public}s";
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD000F00, "MainAbilitySlice");
    private static final String TAG = "ShareHmFaManager";
    private final AbilityContext context;

    public HwShareJSInterface(AbilityContext context) {
        super();
        this.context = context;
    }

    /**
     * share
     * @param prodId prodId
     * @param macAddress macAddress
     */
    public void share(String prodId,String macAddress) {
        try {
            ShareInfo shareInfo = getShareInfo();
            hwShare(shareInfo,prodId,macAddress);
        } catch (IOException e) {
            HiLog.error(LABEL_LOG, LOG_FORMAT, TAG, "startShare read image error.");
        } catch (NotExistException e) {
            HiLog.error(LABEL_LOG, LOG_FORMAT, TAG, "startShare image not exist error.");
        }
    }

    // 组装华为分享需要的数据
    private ShareInfo getShareInfo() throws IOException, NotExistException {
        ShareInfo shareInfo = new ShareInfo();
        shareInfo.setType(0);
        shareInfo.setBundleName(context.getBundleName());
        shareInfo.setAbilityName(MainAbility.class.getName());
        shareInfo.setFaName("华为商城");
        shareInfo.setContent("香百年智能香薰机");
        shareInfo.setIcons(image2Byte(ResourceTable.Media_icon_buy));
        shareInfo.setIntroductoryImages(image2Byte(ResourceTable.Media_device));
        return shareInfo;
    }

    // 将图片转化为byte数组
    private byte[] image2Byte(int resourceId) throws IOException, NotExistException {
        Resource resource = context.getResourceManager().getResource(resourceId);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(BYTE_LENGTH);
        byte[] readBytes = new byte[BYTE_LENGTH];
        int index;
        while ((index = resource.read(readBytes)) != READ_FINISH) {
            bos.write(readBytes, 0, index);
        }
        resource.close();
        return bos.toByteArray();
    }

    // start share
    private void hwShare(ShareInfo shareInfo,String prodId,String macAddress) {
        PacMapEx pacMap = new PacMapEx();
        // 分享的服务类型，当前只支持默认值0，非必选参数。如果不传递此参数，则接收方默认赋值为0。
        pacMap.putObjectValue(ShareFaManager.SHARING_FA_TYPE, 0);
        // 分享的服务的bundleName，最大长度1024，必选参数。
        pacMap.putObjectValue(ShareFaManager.HM_BUNDLE_NAME, shareInfo.getBundleName());
        // 分享的服务的Ability类名，最大长度1024，必选参数。
        pacMap.putObjectValue(ShareFaManager.HM_ABILITY_NAME, shareInfo.getAbilityName());
        // 两台手机登录的华为帐号不同时弹出的卡片展示的服务介绍信息，最大长度1024，必选参数。
        pacMap.putObjectValue(ShareFaManager.SHARING_CONTENT_INFO, shareInfo.getContent());
        // 需要传递到分享对应的Ability的数据
        ZSONObject object = new ZSONObject();
        object.put("macAddress",macAddress);
        object.put("prodId",prodId);
        object.put("isShare",true);
        // 携带的额外信息，可传递到被拉起的服务界面 ，最大长度10240，非必选参数。
        pacMap.putObjectValue(ShareFaManager.SHARING_EXTRA_INFO, object.toString());
        // 两台手机登录的华为帐号不同时弹出的卡片展示服务介绍图片，最大长度153600，必选参数。
        pacMap.putObjectValue(ShareFaManager.SHARING_THUMB_DATA, shareInfo.getIntroductoryImages());
        // 两台手机登录的华为帐号不同时弹出的卡片服务图标，如果不传递此参数，取分享方默认服务图标，最大长度32768，非必选参数。
        pacMap.putObjectValue( ShareFaManager.HM_FA_ICON, shareInfo.getIcons());
        // 两台手机登录的华为帐号不同时弹出的卡片展示的服务名称，最大长度1024，非必选参数。如果不传递此参数，取分享方默认服务名称。
        pacMap.putObjectValue(ShareFaManager.HM_FA_NAME, shareInfo.getFaName());
        ShareFaManager instance = ShareFaManager.getInstance(context);
        // 发起分享 第一个参数为appid，在华为AGC创建原子化服务时自动生成。
        instance.shareFaInfo(APP_ID, pacMap,macAddress);
    }
}
