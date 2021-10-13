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

package com.huawei.cookbooks.component;

import com.huawei.cookbooks.ResourceTable;

import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.components.element.ShapeElement;
import ohos.app.Context;

/**
 * add/edit contact layout
 *
 * @since 2021-01-07
 */
public class ContactComponent extends DirectionalLayout {
    // 表示姓名输入框
    private static final int NAME_FLAG = 1;

    private Component dialogComponent;

    /**
     * input name
     */
    private TextField nameTextField;

    /**
     * input phone
     */
    private TextField phoneTextField;

    private Text title;

    private DialogCallBack dialogCallBack;

    /**
     * constructor of ContactComponent
     *
     * @param context context
     */
    public ContactComponent(Context context) {
        super(context);
        addComponent(context);
        initView();
        initEvent();
    }

    private void initEvent() {
        dialogComponent.findComponentById(ResourceTable.Id_confirm).setClickedListener(component -> confirmContact());
    }

    private void confirmContact() {
        String nameInput = nameTextField.getText();
        String phoneInput = phoneTextField.getText();
        if (dialogCallBack != null) {
            dialogCallBack.result(nameInput, phoneInput);
        }
    }

    private void initView() {
        Component componentText = dialogComponent.findComponentById(ResourceTable.Id_name);
        if (componentText instanceof TextField) {
            nameTextField = (TextField) componentText;
        }
        componentText = dialogComponent.findComponentById(ResourceTable.Id_phone);
        if (componentText instanceof TextField) {
            phoneTextField = (TextField) componentText;
        }
        componentText = dialogComponent.findComponentById(ResourceTable.Id_title);
        if (componentText instanceof Text) {
            title = (Text) componentText;
        }

        // 姓名输入框响应焦点变化
        textFiledSetFocusChangedGraphic(nameTextField);
        // 电话号码输入框响应焦点变化
        textFiledSetFocusChangedGraphic(phoneTextField);
    }

    // TextField输入框响应焦点变化
    private void textFiledSetFocusChangedGraphic(TextField textField) {
        textField.setFocusChangedListener((component, isFocused) -> {
            if (isFocused) {
                // 显示获取到焦点状态下的样式
                ShapeElement focusElement = new ShapeElement(getContext(),
                        ResourceTable.Graphic_background_text_field_focus);
                component.setBackground(focusElement);
            } else {
                // 显示失去焦点状态下的样式
                ShapeElement normalElement = new ShapeElement(getContext(), ResourceTable.Graphic_background_input);
                component.setBackground(normalElement);
            }
        });
    }

    private void addComponent(Context context) {
        dialogComponent = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_item_dialog,
                null, false);
        addComponent(dialogComponent);
        LayoutConfig layoutConfig = new LayoutConfig(LayoutConfig.MATCH_PARENT, LayoutConfig.MATCH_CONTENT);
        setLayoutConfig(layoutConfig);
    }

    /**
     * edit contact info
     *
     * @param name name
     * @param phone phone
     */
    public void initData(String name, String phone) {
        nameTextField.setBubbleSize(0, 0);
        phoneTextField.setBubbleSize(0, 0);
        if (name != null) {
            nameTextField.setText(name);
        }
        if (phone != null) {
            phoneTextField.setText(phone);
            phoneTextField.setEnabled(false);
            ShapeElement editElement = new ShapeElement(getContext(), ResourceTable.Graphic_background_text_field_edit);
            phoneTextField.setBackground(editElement);
            title.setText("修改信息");
        }
    }

    /**
     * set textFiled error graphic
     *
     * @param flag flag 1:name input， 2:phone input
     */
    public void setTextFiledErrorGraphic(int flag) {
        ShapeElement errorElement = new ShapeElement(getContext(), ResourceTable.Graphic_background_text_field_error);
        nameTextField.clearFocus();
        phoneTextField.clearFocus();
        if (NAME_FLAG == flag) {
            nameTextField.setBackground(errorElement);
        } else {
            phoneTextField.setBackground(errorElement);
        }
    }

    /**
     * set textFiled normal graphic
     */
    public void setTextFiledNormalGraphic() {
        ShapeElement normalElement = new ShapeElement(getContext(), ResourceTable.Graphic_background_input);
        nameTextField.setBackground(normalElement);
        phoneTextField.setBackground(normalElement);
    }

    public void setDialogCallBack(DialogCallBack dialogCallBack) {
        this.dialogCallBack = dialogCallBack;
    }

    /**
     * Callback interface for inputting text in a dialog box
     *
     * @since 2021-01-07
     */
    public interface DialogCallBack {
        /**
         * result method
         *
         * @param name name
         * @param phone phone
         */
        void result(String name, String phone);
    }
}
