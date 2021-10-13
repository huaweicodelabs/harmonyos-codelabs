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
export default {
    data: {
        fontWeight0: 'bolder',
        fontWeight1: null,
        fontWeight2: null,
        fontWeight3: null,
        fontWeight4: null,
        display1: 'flex',
        display2: 'none',
        display3: 'none',
        display4: 'none',
        display5: 'none',
        imgGr: 'common/Icon/1.png',
        imgDu: 'common/Icon/2.png',
        imgGu: 'common/Icon/3.png',
        imgWz: 'common/Icon/4.png',
        imgXx: 'common/Icon/5.png'
    },
    change(e) {
        if (e.currentTarget.attr.value === 'Option 1') {
            this.function1();
        } else if (e.currentTarget.attr.value === 'Option 2') {
            this.function2();
        } else if (e.currentTarget.attr.value === 'Option 3') {
            this.function3();
        } else if (e.currentTarget.attr.value === 'Option 4') {
            this.function4();
        } else if (e.currentTarget.attr.value === 'Option 5') {
            this.function5();
        }
    },
    function1() {
        this.fontWeight0 = 'bolder';
        this.fontWeight1 = 'normal';
        this.fontWeight2 = 'normal';
        this.fontWeight3 = 'normal';
        this.fontWeight4 = 'normal';
        this.display1 = 'flex';
        this.display2 = 'none';
        this.display3 = 'none';
        this.display4 = 'none';
        this.display5 = 'none';
    },
    function2() {
        this.fontWeight0 = 'normal';
        this.fontWeight1 = 'bolder';
        this.fontWeight2 = 'normal';
        this.fontWeight3 = 'normal';
        this.fontWeight4 = 'normal';
        this.display1 = 'none';
        this.display2 = 'flex';
        this.display3 = 'none';
        this.display4 = 'none';
        this.display5 = 'none';
    },
    function3() {
        this.fontWeight0 = 'normal';
        this.fontWeight1 = 'normal';
        this.fontWeight2 = 'bolder';
        this.fontWeight3 = 'normal';
        this.fontWeight4 = 'normal';
        this.display1 = 'none';
        this.display2 = 'none';
        this.display3 = 'flex';
        this.display4 = 'none';
        this.display5 = 'none';
    },
    function4() {
        this.fontWeight0 = 'normal';
        this.fontWeight1 = 'normal';
        this.fontWeight2 = 'normal';
        this.fontWeight3 = 'bolder';
        this.fontWeight4 = 'normal';
        this.display1 = 'none';
        this.display2 = 'none';
        this.display3 = 'none';
        this.display4 = 'flex';
        this.display5 = 'none';
    },
    function5() {
        this.fontWeight0 = 'normal';
        this.fontWeight1 = 'normal';
        this.fontWeight2 = 'normal';
        this.fontWeight3 = 'normal';
        this.fontWeight4 = 'bolder';
        this.display1 = 'none';
        this.display2 = 'none';
        this.display3 = 'none';
        this.display4 = 'none';
        this.display5 = 'flex';
    }
};
