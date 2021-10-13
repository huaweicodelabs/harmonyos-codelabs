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

package com.huawei.codecdemo.manager;

import com.huawei.codecdemo.camera.CameraController;
import com.huawei.codecdemo.camera.api.CameraStateListener;
import com.huawei.codecdemo.codec.CodecDecoder;
import com.huawei.codecdemo.codec.CodecEncoder;
import com.huawei.codecdemo.codec.constant.CodecConst;
import com.huawei.codecdemo.media.constant.MediaConst;

import ohos.agp.graphics.Surface;
import ohos.media.common.Format;

/**
 * CodecPlayer
 *
 * @since 2021-04-09
 */
public class CodecPlayer implements CameraStateListener {
    private CodecEncoder videoEncoder;
    private CodecDecoder videoDecoder;
    private final Surface surface;

    /**
     * CodecPlayer
     *
     * @param surface surface
     */
    public CodecPlayer(Surface surface) {
        this.surface = surface;
    }

    @Override
    public void onCameraConfigured(CameraController controller) {
        Format fmt = new Format();
        fmt.putStringValue(Format.MIME, Format.VIDEO_AVC);
        // yuv数据解码视频旋转了90度，解码宽高对换
        fmt.putIntValue(Format.WIDTH, controller.getResolution().height);
        fmt.putIntValue(Format.HEIGHT, controller.getResolution().width);
        fmt.putIntValue(Format.BIT_RATE, MediaConst.RECORDER_BIT_RATE);
        fmt.putIntValue(Format.COLOR_MODEL, CodecConst.CODEC_COLOR_MODEL);
        fmt.putIntValue(Format.FRAME_RATE, MediaConst.RECORDER_FRAME_RATE);
        fmt.putIntValue(Format.FRAME_INTERVAL, CodecConst.CODEC_FRAME_INTERVAL);
        fmt.putIntValue(Format.BITRATE_MODE, CodecConst.CODEC_BITRATE_MODE);
        initEncoder(fmt);
        initDecoder(fmt);
        if (controller.isCapturing()) {
            controller.capture();
        }
    }

    private void initEncoder(Format fmt) {
        videoEncoder = new CodecEncoder.Builder().setFormat(fmt).create();
        videoEncoder.setEncodeListener((byteBuffer, bufferInfo) -> {
            byte[] buffers = new byte[bufferInfo.size];
            byteBuffer.clear();
            byteBuffer.get(buffers);
            videoDecoder.startDecode(buffers);
        });
        videoEncoder.openEncoder();
    }

    private void initDecoder(Format fmt) {
        videoDecoder = new CodecDecoder.Builder().setFormat(fmt).setSurface(surface).create();
        videoDecoder.openDecoder();
    }

    @Override
    public void onGetFrameResult(byte[] frame) {
        if (videoEncoder.isOpen()) {
            videoEncoder.startEncode(frame);
        }
    }

    @Override
    public void onCameraReleased() {
        stop();
    }

    /**
     * stop
     */
    public void stop() {
        if (videoEncoder != null) {
            videoEncoder.stopEncode();
        }
        if (videoDecoder != null) {
            videoDecoder.stopDecode();
        }
    }
}
