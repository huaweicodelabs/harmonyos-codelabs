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

import device from '@system.device';

const BUTTON_STATE_IMAGE = ['/common/checkbutton.png', '/common/done.png'];
const TAG_STATE = ['show', 'hide'];
const TEXT_COLOR = ['text-default', 'text-gray'];
const EVENT_LEVEL = ['urgent', 'senior', 'middle', 'low'];

export default {
    data: {
        title: "",
        eventList: [
                "拿/获取",
                "约会/安排",
                "电子邮件",
                "清除",
                "购买"],
        initialIndex: 0
    },
    onInit() {
        this.title = this.$t('strings.world');
        this.$set('taskList', []);
    },
    onShow() {
        for (var index = 0; index < this.eventList.length; index++) {
            const element = {};
            element.id = 'id-' + index;
            element.event = this.eventList[index];
            element.time = this.getRandomTime();
            var completeState = this.getRandom(100) % 2;
            element.checkBtn = BUTTON_STATE_IMAGE[completeState];
            element.color = TEXT_COLOR[completeState];
            element.showTag = TAG_STATE[completeState];
            element.tag = EVENT_LEVEL[this.getRandom(EVENT_LEVEL.length)];
            this.taskList.push(element);
        }

        const _this = this;
        device.getInfo({
            success: function (data) {
                if (data.deviceType && data.deviceType === 'wearable') {
                    _this.initialIndex = 2;
                }
            },
        });
    },
    completeEvent(e) {
        for (var i of this.taskList) {
            if (i.id == e) {
                if (i.checkBtn == "/common/done.png") {
                    i.checkBtn = "/common/checkbutton.png";
                    i.showTag = 'show';
                    i.color = 'text-default';
                    i.completeState = false;
                } else {
                    i.checkBtn = "/common/done.png";
                    i.showTag = 'hide';
                    i.color = 'text-gray';
                    i.completeState = true;
                }
                return;
            }
        }
    },
    getRandomTime() {
        var hour = this.getRandom(24);
        var minute = this.getRandom(60);
        if (minute < 10) {
            minute = '0' + minute;
        }
        return hour + ':' + minute;
    },
    getRandom(range) {
        var num = Math.random();
        num = num * range;
        num = Math.floor(num);
        return num;
    }
}
