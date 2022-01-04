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

package com.huawei.maildemo.slice;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

import com.huawei.maildemo.ResourceTable;
import com.huawei.maildemo.bean.MailDataBean;
import com.huawei.maildemo.ui.DeviceSelectDialog;
import com.huawei.maildemo.ui.WidgetHelper;
import com.huawei.maildemo.ui.listcomponent.CommentViewHolder;
import com.huawei.maildemo.ui.listcomponent.ListComponentAdapter;
import com.huawei.maildemo.utils.DeviceUtils;
import com.huawei.maildemo.utils.LogUtil;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.TextField;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.window.dialog.CommonDialog;
import ohos.app.Context;
import ohos.data.resultset.ResultSet;
import ohos.distributedschedule.interwork.DeviceInfo;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Mail edit slice
 *
 * @since 2021-02-04
 */
public class MailEditSlice extends AbilitySlice implements IAbilityContinuation {
    private static final String TAG = MailEditSlice.class.getSimpleName();
    private static final int CACHE_SIZE = 8 * 1024;
    private static final int IO_END_LEN = -1;
    private static final int TIPS_DURATION_TIME = 5;

    private TextField receiver;
    private TextField cc;
    private TextField title;
    private TextField content;
    private MailDataBean cachedMailData;
    private Image doConnectImg;

    private final List<String> mDialogDataList = new ArrayList<>();
    private ListComponentAdapter<String> mRecycleItemProvider;
    private CommonDialog fileDialog;
    private final List<String> mAttachmentDataList = new ArrayList<>();
    private ListComponentAdapter<String> mAttachmentProvider;

    private Boolean picIsClicked=false;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        LogUtil.info(TAG, "is onStart begin");
        Component view = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_moudle_mail_edit, null, false);
        ComponentContainer rootLayout;
        if (!(view instanceof ComponentContainer)) {
            rootLayout = new DirectionalLayout(this);
        } else {
            adaptView(view);
            setClickAction(view);
            rootLayout = (ComponentContainer) view;
        }
        super.setUIContent(rootLayout);

        LogUtil.info(TAG, "is onStart end");
    }

    private void adaptView(Component rootView) {
        Component view = rootView.findComponentById(ResourceTable.Id_mail_edit_receiver);
        if (view instanceof TextField) {
            receiver = (TextField) view;
        }
        view = rootView.findComponentById(ResourceTable.Id_mail_edit_cc);
        if (view instanceof TextField) {
            cc = (TextField) view;
        }
        view = rootView.findComponentById(ResourceTable.Id_mail_edit_title);
        if (view instanceof TextField) {
            title = (TextField) view;
        }
        doConnectImg = (Image) rootView.findComponentById(ResourceTable.Id_mail_edit_continue);
        view = rootView.findComponentById(ResourceTable.Id_mail_edit_content);
        if (view instanceof TextField) {
            content = (TextField) view;
        }
        // 初始化界面
        fillView();
        setAttachmentProvider(rootView);

        rootView.findComponentById(ResourceTable.Id_call_test).setClickedListener(listener -> { });
    }

    private void setAttachmentProvider(Component rootView) {
        ListContainer mAttachmentContainer = (ListContainer) rootView.findComponentById(ResourceTable.Id_attachment_list);
        mAttachmentProvider =
            new ListComponentAdapter<String>(
                    getContext(), mAttachmentDataList, ResourceTable.Layout_attachment_item_horizontal) {
                @Override
                public void onBindViewHolder(CommentViewHolder commonViewHolder, String item, int position) {
                    commonViewHolder
                            .getTextView(ResourceTable.Id_item_title1)
                            .setText(item.substring(item.lastIndexOf(File.separator) + 1));
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream(item);
                        ImageSource source = ImageSource.create(fileInputStream, null);
                        commonViewHolder
                                .getImageView(ResourceTable.Id_image)
                                .setPixelMap(source.createPixelmap(0, null));
                    } catch (FileNotFoundException e) {
                        LogUtil.error(TAG, "setAttachmentProvider Error");
                    } finally {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            LogUtil.error(TAG, "close fileInputStream Error");
                        }
                    }
                }
            };
        mAttachmentContainer.setItemProvider(mAttachmentProvider);
    }

    private void fillView() {
        if (cachedMailData == null) {
            receiver.setText("user1;user2");
            cc.setText("user3");
            ohos.global.resource.ResourceManager resManager = this.getResourceManager();
            try {
                title.setText(resManager.getElement(ResourceTable.String_text_reply_title).getString());
            } catch (IOException | NotExistException | WrongTypeException e) {
                e.printStackTrace();
            }
        } else {
            receiver.setText(cachedMailData.getReceiver());
            cc.setText(cachedMailData.getCc());
            title.setText(cachedMailData.getTitle());
            content.setText(cachedMailData.getContent());
            if (cachedMailData.getPictureDataList().size() > 0) {
                // 清空现有数据，并刷新
                mAttachmentDataList.clear();
                mAttachmentDataList.addAll(cachedMailData.getPictureDataList());
            }
        }
    }

    private void setClickAction(Component rootView) {
        doConnectImg.setClickedListener(
            clickedView -> {
                // 通过FLAG_GET_ONLINE_DEVICE标记获得在线设备列表
                List<DeviceInfo> deviceInfoList = DeviceManager.getDeviceList(DeviceInfo.FLAG_GET_ONLINE_DEVICE);
                if (deviceInfoList.size() < 1) {
                    WidgetHelper.showTips(this, "无在网设备");
                } else {
                    DeviceSelectDialog dialog = new DeviceSelectDialog(this);
                    // 点击后迁移到指定设备
                    dialog.setListener(
                        deviceInfo -> {
                            LogUtil.debug(TAG, deviceInfo.getDeviceName());
                            LogUtil.info(TAG, "continue button click");
                            try {
                                // 开始任务迁移
                                continueAbility(deviceInfo.getDeviceId());
                                LogUtil.info(TAG, "continue button click end");
                            } catch (IllegalStateException | UnsupportedOperationException e) {
                                WidgetHelper.showTips(this, ResourceTable.String_tips_mail_continue_failed);
                            }
                            dialog.hide();
                        });
                    dialog.show();
                }
            });

        rootView.findComponentById(ResourceTable.Id_open_dir)
            .setClickedListener(
                c -> {
                    if (picIsClicked) {
                        //WidgetHelper.showTips(this, "请勿连续点击！", TIPS_DURATION_TIME);
                        return;
                    }
                    if (mAttachmentDataList.size() > 4) {
                        WidgetHelper.showTips(this, "最多支持5个附件哦！", TIPS_DURATION_TIME);
                        return;
                    }

                    // 防止多次点击一直弹窗
                    if (fileDialog != null && fileDialog.isShowing()) {
                        return;
                    }
                    picIsClicked=true;
                    // 先获取文件并上传到共享库distributedir
                    if (mDialogDataList.size() < 1) {
                        // 获取设备的分布式文件
                        List<String> tempListRemotes = DeviceUtils.getFile(this);
                        mDialogDataList.addAll(tempListRemotes);
                    }
                    // 弹窗
                    showDialog(getContext());

                });
    }

    /**
     * showDialog
     *
     * @param context context
     */
    public void showDialog(Context context) {
        Component rootView = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_dialog_picture, null, false);
        ListContainer listContainer = (ListContainer) rootView.findComponentById(ResourceTable.Id_list_container_pic);
        if (mRecycleItemProvider == null) {
            mRecycleItemProvider =
                new ListComponentAdapter<String>(
                        getContext(), mDialogDataList, ResourceTable.Layout_dialog_picture_item) {
                    @Override
                    public void onBindViewHolder(CommentViewHolder commonViewHolder, String item, int position) {
                        commonViewHolder
                                .getTextView(ResourceTable.Id_item_desc)
                                .setText(item.substring(item.lastIndexOf(File.separator) + 1));
                        FileInputStream fileInputStream = null;
                        try {
                            fileInputStream = new FileInputStream(item);
                            ImageSource source = ImageSource.create(fileInputStream, null);
                            commonViewHolder
                                    .getImageView(ResourceTable.Id_image)
                                    .setPixelMap(source.createPixelmap(0, null));
                        } catch (FileNotFoundException e) {
                            LogUtil.error(TAG, "showDialog() FileNotFound Exception");
                        } finally {
                            try {
                                fileInputStream.close();
                            } catch (IOException e) {
                                LogUtil.error(TAG, "close fileInputStream error");
                            }
                        }
                    }
                };
        } else {
            mRecycleItemProvider.notifyDataChanged();
        }

        clickOnAttachment(listContainer);
        fileDialog = new CommonDialog(context);
        fileDialog.setSize(MATCH_PARENT, MATCH_PARENT);
        fileDialog.setAlignment(LayoutAlignment.BOTTOM);
        fileDialog.setAutoClosable(true);
        fileDialog.setContentCustomComponent(rootView);
        fileDialog.show();
    }

    private void clickOnAttachment(ListContainer listContainer) {
        listContainer.setItemProvider(mRecycleItemProvider);
        listContainer.setItemClickedListener(
            (listContainer1, component, i, l) -> {
                if (mAttachmentDataList.contains(mDialogDataList.get(i))) {
                    WidgetHelper.showTips(this, "此附件已添加！", TIPS_DURATION_TIME);
                } else {
                    mAttachmentDataList.add(mDialogDataList.get(i));
                    mAttachmentProvider.notifyDataChanged();
                    fileDialog.destroy();
                    picIsClicked= false;
                }
            });
    }

    /**
     * 获取公共目录照片
     *
     * @param context context
     */
    public void initPublicPictureFile(Context context) {
        DataAbilityHelper helper = DataAbilityHelper.creator(context);
        InputStream in = null;
        OutputStream out = null;
        String[] projections =
                new String[] {
                    AVStorage.Images.Media.ID, AVStorage.Images.Media.DISPLAY_NAME, AVStorage.Images.Media.DATA
                };
        try {
            ResultSet results = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, projections, null);
            while (results != null && results.goToNextRow()) {
                int mediaId = results.getInt(results.getColumnIndexForName(AVStorage.Images.Media.ID));
                String fullFileName = results.getString(results.getColumnIndexForName(AVStorage.Images.Media.DATA));
                String fileName = fullFileName.substring(fullFileName.lastIndexOf(File.separator) + 1);

                Uri contentUri =
                        Uri.appendEncodedPathToUri(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, "" + mediaId);
                FileDescriptor fileDescriptor = helper.openFile(contentUri, "r");

                if (getDistributedDir() == null) {
                    WidgetHelper.showTips(this, "注意：分布式文件异常！", TIPS_DURATION_TIME);
                    return;
                }
                String distributedFilePath = getContext().getDistributedDir().getPath() + File.separator + fileName;
                File fr = new File(distributedFilePath);
                in = new FileInputStream(fileDescriptor);
                out = new FileOutputStream(fr);
                byte[] buffers = new byte[CACHE_SIZE];
                int count;
                LogUtil.info(TAG, "START WRITING");
                while ((count = in.read(buffers)) != IO_END_LEN) {
                    out.write(buffers, 0, count);
                }
                out.close();
                LogUtil.info(TAG, "STOP WRITING");
            }
        } catch (DataAbilityRemoteException | IOException e) {
            LogUtil.error(TAG, "initPublicPictureFile exception");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                LogUtil.error(TAG, "io exception");
            }
        }
    }

    @Override
    public boolean onStartContinuation() {
        LogUtil.info(TAG, "is start continue");
        return true;
    }

    @Override
    public boolean onSaveData(IntentParams params) {
        MailDataBean mailData = getMailData();
        LogUtil.info(TAG, "begin onSaveData");
        mailData.saveDataToParams(params);
        LogUtil.info(TAG, "end onSaveData");
        return true;
    }

    @Override
    public boolean onRestoreData(IntentParams params) {
        LogUtil.info(TAG, "begin onRestoreData");
        cachedMailData = new MailDataBean(params);
        LogUtil.info(TAG, "end onRestoreData, mail data");
        return true;
    }

    @Override
    public void onCompleteContinuation(int i) {
        LogUtil.info(TAG, "onCompleteContinuation");
        terminateAbility();
    }

    private MailDataBean getMailData() {
        MailDataBean data = new MailDataBean();
        data.setReceiver(receiver.getText());
        data.setCc(cc.getText());
        data.setTitle(title.getText());
        data.setContent(content.getText());
        data.setPictureDataList(mAttachmentDataList);
        return data;
    }

    @Override
    protected void onActive() {
        super.onActive();
        LogUtil.info(TAG, "is onActive begin");
        initPublicPictureFile(this);
        LogUtil.info(TAG, "is onActive end");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.info(TAG, "is onStop");
    }
}
