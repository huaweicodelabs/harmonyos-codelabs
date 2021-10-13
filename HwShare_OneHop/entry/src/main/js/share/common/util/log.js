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
const logSetting = {
  LOG_DEBUG: true,
  LOG_TAG: 'net_work'
};
class Log {
  info(data) {
    if (logSetting.LOG_DEBUG) {
    }
  }

  error(data) {
    if (logSetting.LOG_DEBUG) {
    }
  }
}

const log = new Log();

export default log;
