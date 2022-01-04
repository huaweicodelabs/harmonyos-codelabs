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

package com.huawei.maildemo.ui;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

import com.huawei.maildemo.ResourceTable;
import com.huawei.maildemo.data.DeviceData;
import com.huawei.maildemo.ui.listcomponent.CommentViewHolder;
import com.huawei.maildemo.ui.listcomponent.ListComponentAdapter;

import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * DeviceSelectDialog
 *
 * @since 2021-02-04
 */
public class DeviceSelectDialog extends CommonDialog {
    private static final int CORNER_RADIUS = 10;
    ListContainer mListContainer;
    List<DeviceData> mDeviceList = new ArrayList<>();
    ListComponentAdapter<DeviceData> listComponentAdapter;
    private DeviceInfo mCheckedDevice;

    /**
     * 设置确定按钮和取消被点击的接口
     *
     * @since 2021-02-04
     */
    public interface OnclickListener {
        /**
         * onYesClick
         *
         * @param deviceInfo deviceInfo
         */
        void onYesClick(DeviceInfo deviceInfo);
    }

    private final Context mContext;
    private OnclickListener mOnclickListener;

    /**
     * DeviceSelectDialog
     *
     * @param context context
     */
    public DeviceSelectDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * setListener
     *
     * @param listener listener
     */
    public void setListener(OnclickListener listener) {
        mOnclickListener = listener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Component rootView =
                LayoutScatter.getInstance(mContext).parse(ResourceTable.Layout_dialog_layout_device, null, false);
        mListContainer = (ListContainer) rootView.findComponentById(ResourceTable.Id_list_container_device);
        List<DeviceInfo> deviceInfoList = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
        mDeviceList.clear();
        for (DeviceInfo deviceInfo : deviceInfoList) {
            mDeviceList.add(new DeviceData(false, deviceInfo));
        }
        if (deviceInfoList.size() > 0) {
            mCheckedDevice = deviceInfoList.get(0);
        }
        setItemProvider();
        Text operateYes = (Text) rootView.findComponentById(ResourceTable.Id_operate_yes);
        operateYes.setClickedListener(
            component -> {
                if (mOnclickListener != null && mCheckedDevice != null) {
                    mOnclickListener.onYesClick(mCheckedDevice);
                }
            });
        Text operateNo = (Text) rootView.findComponentById(ResourceTable.Id_operate_no);
        operateNo.setClickedListener(component -> hide());
        setSize(MATCH_PARENT, MATCH_CONTENT);
        setAlignment(LayoutAlignment.BOTTOM);
        setCornerRadius(CORNER_RADIUS);
        setAutoClosable(true);
        setContentCustomComponent(rootView);
        setTransparent(true);
    }

    private void setItemProvider() {
        listComponentAdapter =
            new ListComponentAdapter<DeviceData>(mContext, mDeviceList, ResourceTable.Layout_dialog_device_item) {
                @Override
                public void onBindViewHolder(CommentViewHolder commonViewHolder, DeviceData item, int position) {
                    commonViewHolder.getTextView(ResourceTable.Id_item_desc)
                            .setText(item.getDeviceInfo().getDeviceName());
                    switch (item.getDeviceInfo().getDeviceType()) {
                        case SMART_PHONE:
                            commonViewHolder .getImageView(ResourceTable.Id_item_type)
                                    .setPixelMap(ResourceTable.Media_dv_phone);
                            break;
                        case SMART_PAD:
                            commonViewHolder.getImageView(ResourceTable.Id_item_type)
                                    .setPixelMap(ResourceTable.Media_dv_pad);
                            break;
                        case SMART_WATCH:
                            commonViewHolder.getImageView(ResourceTable.Id_item_type)
                                    .setPixelMap(ResourceTable.Media_dv_watch);
                            break;
                        default:
                    }
                    commonViewHolder
                        .getImageView(ResourceTable.Id_item_check)
                        .setPixelMap(
                            item.isChecked()
                                ? ResourceTable.Media_checked_point
                                : ResourceTable.Media_uncheck_point);
                }

                @Override
                public void onItemClick(Component component, DeviceData item, int position) {
                    super.onItemClick(component, item, position);
                    for (DeviceData mDevice : mDeviceList) {
                        mDevice.setChecked(false);
                    }
                    mDeviceList.get(position).setChecked(true);
                    listComponentAdapter.notifyDataChanged();
                    mCheckedDevice = item.getDeviceInfo();
                }
            };
        mListContainer.setItemProvider(listComponentAdapter);
    }
}