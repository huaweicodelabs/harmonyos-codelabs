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

package com.huawei.camerademo.listener;

import ohos.ai.asr.AsrListener;
import ohos.utils.PacMap;

/**
 * MyAsrListener
 *
 * @since 2021-03-08
 */
public class MyAsrListener implements AsrListener {
    @Override
    public void onInit(PacMap pacMap) {
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    @Override
    public void onRmsChanged(float var1) {
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onError(int var1) {
    }

    @Override
    public void onResults(PacMap pacMap) {
    }

    @Override
    public void onIntermediateResults(PacMap pacMap) {
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void onEvent(int var1, PacMap pacMap) {
    }

    @Override
    public void onAudioStart() {
    }

    @Override
    public void onAudioEnd() {
    }
}
