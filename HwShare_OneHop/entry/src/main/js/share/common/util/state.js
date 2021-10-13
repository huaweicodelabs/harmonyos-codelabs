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
/*
 * 在此文件中，修改增加状态以适配特定流程。
 * 修改后，需要进入main/src/main/js/share/component中，
 * 将对应组件中的showState修改增加需要显示的对应状态
 */
const data = {
  pair: {
    PRIVACY: 'PRIVACY', // 隐私协议及登录页面
    PERMISSION_FORBIDDEN: 'PERMISSION_FORBIDDEN', // 权限被禁止授予引导页面
    PAIR: 'PAIR', // 配对页面
    PAIR_FAILED: 'PAIR_FAILED', // 配对失败页面
    REPAIR_REQUESTED: 'REPAIR_REQUESTED' // 配对失败，请求重连，同时
  }
};

export default data;
