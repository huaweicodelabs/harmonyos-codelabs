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

package com.huawei.codelab.gif;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.huawei.codelab.gif.decoder.GifDecoder;
import com.huawei.codelab.utils.LogUtils;

/**
 * gif play
 *
 * @since 2021-05-26
 */
public class GifImage extends Image {
    // 帧图像集合
    private List<PixelMap> pixelMapList = new ArrayList<>();

    // 动画
    private AnimatorValue animatorValue;

    // gif编码
    private GifDecoder gifDecoder;

    // 播放暂停
    private Boolean ispaused = false;

    // 播放时间
    private int duration;

    // 获取资源id
    private String id;

    // 线程通信
    private EventHandler handler = new EventHandler(EventRunner.create("r4"));

    /**
     * constructor
     *
     * @param context context
     * @param attrSet attrSet
     */
    public GifImage(Context context, AttrSet attrSet) {
        super(context, attrSet);
        id = attrSet.getAttr("image_src").get().getStringValue();
    }

    private void start() {
        try {
            gifDecoder = new GifDecoder();
            ResourceManager resourceManager = getResourceManager();
            ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
            sourceOptions.formatHint = "image/gif";
            Pattern pattern = Pattern.compile("[^0-9]");
            Matcher matcher = pattern.matcher(id);
            String all = matcher.replaceAll("");
            RawFileEntry rawFileEntry = resourceManager.getRawFileEntry(
                    resourceManager.getMediaPath(Integer.parseInt(all)));
            ImageSource imageSource = ImageSource.create(rawFileEntry.openRawFile(), sourceOptions);
            gifDecoder.read(rawFileEntry.openRawFile(), (int) rawFileEntry.openRawFileDescriptor().getFileSize());
            if (imageSource != null) {
                init(imageSource);
            }
        } catch (NotExistException e) {
            LogUtils.error("tag", "error");
        } catch (WrongTypeException e) {
            LogUtils.error("tag", "error");
        } catch (IOException e) {
            LogUtils.error("tag", "error");
        }
    }

    // 动画侦听函数
    private final AnimatorValue.ValueUpdateListener mAnimatorUpdateListener
            = (animatorValue, v) -> {
        setPixelMap(pixelMapList.get((int) (v * pixelMapList.size())));
        invalidate();
    };

    /**
     * 初始化数据
     *
     * @param imageSource 图像解码
     */
    private void init(ImageSource imageSource) {
        pixelMapList.clear();
        duration = 0;
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        decodingOptions.allowPartialImage = true;
        decodingOptions.editable = true;
        int i = 1;
        if (gifDecoder.getFrameCount() > 0) {
            while (i < gifDecoder.getFrameCount()) {
                PixelMap map = imageSource.createPixelmap(i, decodingOptions);
                pixelMapList.add(map);
                duration += gifDecoder.getDelay(i);
                i++;
            }
        } else {
            while (imageSource.createPixelmap(i, decodingOptions) != null) {
                PixelMap map = imageSource.createPixelmap(i, decodingOptions);
                pixelMapList.add(map);
                duration += gifDecoder.getDelay(i);
                i++;
            }
        }
        // 启动动画
        animatorValue = new AnimatorValue();
        animatorValue.setCurveType(Animator.CurveType.LINEAR);
        animatorValue.setDelay(300);
        animatorValue.setLoopedCount(Animator.INFINITE);
        animatorValue.setDuration(duration == 0 ? 3000 : duration / 3);
    }

    /**
     * play gif
     */
    public void play() {
        handler.postTask(() -> {
            start();
            getContext().getMainTaskDispatcher().syncDispatch(() -> {
                if (ispaused) {
                    ispaused = false;
                }
                animatorValue.setValueUpdateListener(mAnimatorUpdateListener);
                animatorValue.start();
            });
        });
    }

    /**
     * pause
     */
    public void pause() {
        if (!ispaused) {
            ispaused = true;
        }
        animatorValue.pause();
        invalidate();
    }
}
