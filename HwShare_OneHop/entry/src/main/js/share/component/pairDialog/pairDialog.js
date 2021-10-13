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
import app from '@system.app';
import state from '../../common/util/state.js';
import Log from '../../common/util/log.js';

const showState = [state.pair.PAIR];

export default {
  props: ['state', 'productId'],
  computed: {
    showObj() {
      return showState.indexOf(this.state) > -1;
    },
    deviceResource() {
      Log.info(this.$t('resources.' + this.productId));
      return this.$t('resources.' + this.productId);
    }
  },
  onInit() {
  },
  exit() {
    app.terminate();
  }
};
