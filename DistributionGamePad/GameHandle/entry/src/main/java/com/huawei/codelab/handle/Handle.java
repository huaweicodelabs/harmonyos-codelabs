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

package com.huawei.codelab.handle;

/**
 * 手柄类
 *
 * @since 2021-03-15
 */
public class Handle {
    private int isAbtnClick; // A按钮是否被点击 0-否，1-是

    private int isBbtnClick; // B按钮是否被点击 0-否，1-是

    private int isPause; // 暂停

    private int isStart; // 开始

    public int getIsAbtnClick() {
        return isAbtnClick;
    }

    public void setIsAbtnClick(int isAbtnClick) {
        this.isAbtnClick = isAbtnClick;
    }

    public int getIsBbtnClick() {
        return isBbtnClick;
    }

    public void setIsBbtnClick(int isBbtnClick) {
        this.isBbtnClick = isBbtnClick;
    }

    public int getIsPause() {
        return isPause;
    }

    public void setIsPause(int isPause) {
        this.isPause = isPause;
    }

    public int getIsStart() {
        return isStart;
    }

    public void setIsStart(int isStart) {
        this.isStart = isStart;
    }
}
