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

package com.huawei.cookbooks.slice;

import com.huawei.cookbooks.ResourceTable;
import com.huawei.cookbooks.bean.Contactor;
import com.huawei.cookbooks.component.ContactComponent;
import com.huawei.cookbooks.provider.ContactProvider;
import com.huawei.cookbooks.util.ToastUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ListContainer;
import ohos.agp.window.dialog.CommonDialog;
import ohos.data.distributed.common.ChangeNotification;
import ohos.data.distributed.common.Entry;
import ohos.data.distributed.common.KvManager;
import ohos.data.distributed.common.KvManagerConfig;
import ohos.data.distributed.common.KvManagerFactory;
import ohos.data.distributed.common.KvStoreException;
import ohos.data.distributed.common.KvStoreObserver;
import ohos.data.distributed.common.KvStoreType;
import ohos.data.distributed.common.Options;
import ohos.data.distributed.common.SubscribeType;
import ohos.data.distributed.common.SyncMode;
import ohos.data.distributed.device.DeviceFilterStrategy;
import ohos.data.distributed.device.DeviceInfo;
import ohos.data.distributed.user.SingleKvStore;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Distributed database Sample Code
 *
 * @since 2021-01-06
 */
public class ContactSlice extends AbilitySlice implements ContactProvider.AdapterClickListener {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(3, 0xD001100, "HiContact");

    private static final String LOG_FORMAT = "%{public}s: %{public}s";

    private static final String TAG = "ContactSlice";

    private static final String STORE_ID = "contact_db1";

    private static final int DIALOG_SIZE_WIDTH = 800;

    private static final int DIALOG_SIZE_HEIGHT = 800;

    private static final long DELAY_MS = 100L;

    // 表示姓名输入框
    private static final int NAME_FLAG = 1;

    // 表示电话号码输入框
    private static final int PHONE_FLAG = 2;

    // 表示正常提示信息
    private static final int NORMAL_TIP_FLAG = 0;

    // 表示错误提示信息
    private static final int ERROR_TIP_FLAG = 1;

    /**
     * List Adapter
     */
    private ContactProvider contactAdapter;

    /**
     * Contact list data
     */
    private List<Contactor> contactArrays;
    /**
     * Database management
     */
    private KvManager kvManager;
    /**
     * Database Operations
     */
    private SingleKvStore singleKvStore;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_contact);
        initDbManager();
        initList();
        initEvent();
        getUITaskDispatcher().delayDispatch(this::queryContact, DELAY_MS);
    }

    /**
     * Initialize click event
     */
    private void initEvent() {
        findComponentById(ResourceTable.Id_addContact).setClickedListener(component -> addContact());
        findComponentById(ResourceTable.Id_sync).setClickedListener(component -> syncContact());
    }

    /**
     * Initialize ListContainer
     */
    private void initList() {
        Component component = findComponentById(ResourceTable.Id_listContainer);
        ListContainer listContainer;
        if (component instanceof ListContainer) {
            listContainer = (ListContainer) component;
            contactArrays = new ArrayList<>(0);
            contactAdapter = new ContactProvider(this, contactArrays);
            listContainer.setItemProvider(contactAdapter);
            contactAdapter.setAdapterClickListener(this);
        }
    }

    /**
     * Synchronizing contacts data
     */
    private void syncContact() {
        List<DeviceInfo> deviceInfoList = kvManager.getConnectedDevicesInfo(DeviceFilterStrategy.NO_FILTER);
        List<String> deviceIdList = new ArrayList<>(0);
        for (DeviceInfo deviceInfo : deviceInfoList) {
            deviceIdList.add(deviceInfo.getId());
        }
        HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "device size= " + deviceIdList.size());
        if (deviceIdList.size() == 0) {
            ToastUtils.showTips(getContext(), "组网失败", ERROR_TIP_FLAG);
            return;
        }
        singleKvStore.registerSyncCallback(map -> {
            getUITaskDispatcher().asyncDispatch(() -> {
                HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "sync success");
                queryContact();
                ToastUtils.showTips(getContext(), "同步成功", NORMAL_TIP_FLAG);
            });
            singleKvStore.unRegisterSyncCallback();
        });
        singleKvStore.sync(deviceIdList, SyncMode.PUSH_PULL);
    }

    /**
     * Initializing Database Management
     */
    private void initDbManager() {
        kvManager = createManager();
        singleKvStore = createDb(kvManager);
        subscribeDb(singleKvStore);
    }

    /**
     * Create a distributed database manager instance
     *
     * @return database manager
     */
    private KvManager createManager() {
        KvManager manager = null;
        try {
            KvManagerConfig config = new KvManagerConfig(this);
            manager = KvManagerFactory.getInstance().createKvManager(config);
        } catch (KvStoreException exception) {
            HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "some exception happen");
        }
        return manager;
    }

    /**
     * Creating a Single-Version Distributed Database
     *
     * @param manager Database management
     * @return SingleKvStore
     */
    private SingleKvStore createDb(KvManager manager) {
        SingleKvStore kvStore = null;
        try {
            Options options = new Options();
            options.setCreateIfMissing(true).setEncrypt(false).setKvStoreType(KvStoreType.SINGLE_VERSION);
            kvStore = manager.getKvStore(options, STORE_ID);
        } catch (KvStoreException exception) {
            HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "some exception happen");
        }
        return kvStore;
    }

    /**
     * Subscribing to All (Currently, Remote) Data Change Notifications of a Single-Version Distributed Database
     *
     * @param kvStore Data operation
     */
    private void subscribeDb(SingleKvStore kvStore) {
        KvStoreObserver kvStoreObserverClient = new KvStoreObserverClient();
        kvStore.subscribe(SubscribeType.SUBSCRIBE_TYPE_REMOTE, kvStoreObserverClient);
    }

    /**
     * Receive database messages
     *
     * @since 2021-01-06
     */
    private class KvStoreObserverClient implements KvStoreObserver {
        @Override
        public void onChange(ChangeNotification notification) {
            getUITaskDispatcher().asyncDispatch(() -> {
                HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "come to auto sync");
                queryContact();
                ToastUtils.showTips(getContext(), "同步成功", NORMAL_TIP_FLAG);
            });
        }
    }

    /**
     * Query Local Contacts
     */
    private void queryContact() {
        List<Entry> entryList = singleKvStore.getEntries("");
        HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "entryList size" + entryList.size());
        contactArrays.clear();
        try {
            for (Entry entry : entryList) {
                contactArrays.add(new Contactor(entry.getValue().getString(), entry.getKey()));
            }
        } catch (KvStoreException exception) {
            HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "the value must be String");
        }
        contactAdapter.notifyDataChanged();
    }

    /**
     * Write key-value data to the single-version distributed database.
     *
     * @param key Stored key
     * @param value Stored value
     */
    private void writeData(String key, String value) {
        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
            return;
        }
        singleKvStore.putString(key, value);
        HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "writeContact key= " + key + " writeContact value= " + value);
    }

    /**
     * Deleting Key Value Data from the Single-Version Distributed Database
     *
     * @param key Deleted Key
     */
    private void deleteData(String key) {
        if (key.isEmpty()) {
            return;
        }
        singleKvStore.delete(key);
        HiLog.info(LABEL_LOG, LOG_FORMAT, TAG, "deleteContact key= " + key);
    }

    /**
     * Add Contact
     */
    private void addContact() {
        showDialog(null, null, (name, phone) -> {
            writeData(phone, name);
            contactArrays.add(new Contactor(name, phone));
            contactAdapter.notifyDataSetItemInserted(contactAdapter.getCount());
            queryContact();
        });
    }

    /**
     * Display dialog box
     *
     * @param name Contacts
     * @param phone phone
     * @param dialogCallBack callback result
     */
    private void showDialog(String name, String phone, ContactComponent.DialogCallBack dialogCallBack) {
        CommonDialog commonDialog = new CommonDialog(this);
        ContactComponent component = new ContactComponent(this);
        component.initData(name, phone);
        component.setDialogCallBack((nameInput, phoneInput) -> {
            component.setTextFiledNormalGraphic();
            if (nameInput.isEmpty() || nameInput.trim().isEmpty()) {
                ToastUtils.showTips(getContext(), "姓名必填", ERROR_TIP_FLAG);
                component.setTextFiledErrorGraphic(NAME_FLAG);
                return;
            }

            if (phoneInput.isEmpty() || phoneInput.trim().isEmpty()) {
                ToastUtils.showTips(getContext(), "手机号码必填", ERROR_TIP_FLAG);
                component.setTextFiledErrorGraphic(PHONE_FLAG);
                return;
            }

            // 校验姓名：长度为1-20个中文或者英文字符、不能有特殊字符和数字、中文英文不能同时出现、可以输入英文，可以有空格，可以输入英文名字中的点
            if (!nameInput.matches("^([\\u4e00-\\u9fa5]{1,20}|[a-zA-Z\\.\\s]{1,20})$")) {
                ToastUtils.showTips(getContext(), "姓名填写内容不正确", ERROR_TIP_FLAG);
                component.setTextFiledErrorGraphic(NAME_FLAG);
                return;
            }

            if (phone == null && phoneIsExist(phoneInput)) {
                ToastUtils.showTips(getContext(), "手机号码已经存在了", ERROR_TIP_FLAG);
                component.setTextFiledErrorGraphic(PHONE_FLAG);
                return;
            }

            // 检验手机号码 只能是11位以内的数字
            if (!phoneInput.matches("^\\d{1,11}$")) {
                ToastUtils.showTips(getContext(), "手机号码填写内容不正确", ERROR_TIP_FLAG);
                component.setTextFiledErrorGraphic(PHONE_FLAG);
                return;
            }

            if (dialogCallBack != null) {
                dialogCallBack.result(nameInput, phoneInput);
            }
            commonDialog.remove();
        });

        commonDialog.setAutoClosable(true);
        commonDialog.setContentCustomComponent(component);
        commonDialog.show();
    }

    /**
     * Check whether the mobile number exists
     *
     * @param phone phone
     * @return boolean
     */
    private boolean phoneIsExist(String phone) {
        List<Entry> entryList = singleKvStore.getEntries("");
        for (Entry entry : entryList) {
            if (entry.getKey().equals(phone)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Event triggered by clicking each edit button
     *
     * @param position Position of the item number
     */
    @Override
    public void edit(int position) {
        Contactor contactor = contactArrays.get(position);
        showDialog(contactor.getName(), contactor.getPhone(), (name, phone) -> {
            writeData(phone, name);
            contactArrays.set(position, new Contactor(name, phone));
            contactAdapter.notifyDataSetItemChanged(position);
            queryContact();
        });
    }

    /**
     * Event triggered by clicking each delete button
     *
     * @param position Position of the item number
     */
    @Override
    public void delete(int position) {
        CommonDialog commonDialog = new CommonDialog(this);
        commonDialog.setSize(DIALOG_SIZE_WIDTH, DIALOG_SIZE_HEIGHT);
        commonDialog.setAutoClosable(true);
        commonDialog.setTitleText("    警告")
                .setContentText("    确定要删除吗？")
                .setButton(0, "取消", (iDialog, id) -> iDialog.destroy())
                .setButton(1, "确认", (iDialog, id) -> {
                    if (position > contactArrays.size() - 1) {
                        ToastUtils.showTips(getContext(), "要删除的元素不存在", NORMAL_TIP_FLAG);
                        return;
                    }
                    deleteData(contactArrays.get(position).getPhone());
                    contactArrays.remove(position);
                    contactAdapter.notifyDataChanged();
                    ToastUtils.showTips(getContext(), "删除成功", NORMAL_TIP_FLAG);
                    iDialog.destroy();
                }).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        kvManager.closeKvStore(singleKvStore);
        kvManager.deleteKvStore(STORE_ID);
    }
}