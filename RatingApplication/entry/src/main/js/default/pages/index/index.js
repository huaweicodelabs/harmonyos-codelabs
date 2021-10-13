/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import prompt from '@system.prompt';

export default {
    data: {
        red: 2.5,
        blue: 3,
        green: 1.5,
        avg: 2.3
    },
    rateRed(e) {
        this.red = e.rating;
        const value = (this.red + this.blue + this.green) / 3;
        this.avg = value.toFixed(1);
        prompt.showToast({
            message: '平均分 ' + this.avg + '分',
            duration: 3000
        });
    },
    rateBlue(e) {
        this.blue = e.rating;
        const value = (this.red + this.blue + this.green) / 3;
        this.avg = value.toFixed(1);
        prompt.showToast({
            message: '平均分 ' + this.avg + '分',
            duration: 3000
        });
    },
    rateGreen(e) {
        this.green = e.rating;
        const value = (this.red + this.blue + this.green) / 3;
        this.avg = value.toFixed(1);
        prompt.showToast({
            message: '平均分 ' + this.avg + '分',
            duration: 3000
        });
    }
};