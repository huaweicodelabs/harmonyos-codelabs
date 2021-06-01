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

package ohos.codelabs.distributedvideo.player.view;

import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Component.LayoutRefreshedListener;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.agp.window.service.WindowManager;
import ohos.app.Context;
import ohos.codelabs.distributedvideo.player.api.ImplPlayModule;
import ohos.codelabs.distributedvideo.player.api.ImplPlayer;
import ohos.codelabs.distributedvideo.player.constant.Constants;
import ohos.codelabs.distributedvideo.player.manager.GestureDetector;

import java.util.Optional;

/**
 * PlayerView
 *
 * @since 2021-04-04
 */
public class PlayerView extends DependentLayout implements ImplPlayModule, LayoutRefreshedListener {
    private ImplPlayer player;
    private SurfaceProvider surfaceView;
    private Surface surface;
    private PlayerGestureView gestureView;
    private GestureDetector gestureDetector;
    private boolean isTopPlay;
    private int viewWidth;
    private int viewHeight;

    /**
     * constructor of PlayerView
     *
     * @param context context
     */
    public PlayerView(Context context) {
        this(context, null);
    }

    /**
     * constructor of PlayerView
     *
     * @param context context
     * @param attrSet attSet
     */
    public PlayerView(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    /**
     * constructor of PlayerView
     *
     * @param context context
     * @param attrSet attSet
     * @param styleName styleName
     */
    public PlayerView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        WindowManager.getInstance().getTopWindow().get().setTransparent(true); // 不设置窗体透明会挡住播放内容，除非设置pinToZTop为true
        if (attrSet != null) {
            Optional<Attr> optIsTopPlay = attrSet.getAttr("top_play");
            optIsTopPlay.ifPresent(attr -> isTopPlay = "true".equals(attr.getStringValue()));
        }
        initView();
        initListener();
        setLayoutRefreshedListener(this);
    }

    private void initView() {
        surfaceView = new SurfaceProvider(mContext);
        LayoutConfig layoutConfig = new LayoutConfig();
        layoutConfig.addRule(LayoutConfig.CENTER_IN_PARENT);
        surfaceView.setLayoutConfig(layoutConfig);
        surfaceView.pinToZTop(isTopPlay);
        addComponent(surfaceView);
        addGestureView();
    }

    private void addGestureView() {
        gestureView = new PlayerGestureView(mContext);
        LayoutConfig config =
                new LayoutConfig(Constants.NUMBER_300, ComponentContainer.LayoutConfig.MATCH_CONTENT);
        config.addRule(LayoutConfig.CENTER_IN_PARENT);
        gestureView.setLayoutConfig(config);
        addComponent(gestureView);
    }

    private void initListener() {
        gestureDetector = new GestureDetector(gestureView);
        surfaceView.setTouchEventListener((component, touchEvent) ->
                canGesture() && gestureDetector.onTouchEvent(touchEvent));
        surfaceView.getSurfaceOps().ifPresent(surfaceOps -> surfaceOps.addCallback(new SurfaceOps.Callback() {
            @Override
            public void surfaceCreated(SurfaceOps surfaceOps) {
                surface = surfaceOps.getSurface();
                if (player != null) {
                    player.addSurface(surface);
                }
            }

            @Override
            public void surfaceChanged(SurfaceOps surfaceOps, int info, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceOps surfaceOps) {
            }
        }));
    }

    private boolean canGesture() {
        return gestureDetector != null
                && player != null
                && player.isGestureOpen();
    }

    private void updateVideoSize(double videoScale) {
        if (videoScale > 1) {
            surfaceView.setWidth(viewWidth);
            surfaceView.setHeight((int) Math.min(viewWidth / videoScale, viewHeight));
        } else {
            surfaceView.setHeight(viewHeight);
            surfaceView.setWidth((int) Math.min(viewHeight * videoScale, viewWidth));
        }
    }

    @Override
    public void bind(ImplPlayer implPlayer) {
        this.player = implPlayer;
        gestureView.bind(player);
        this.player.addPlayerViewCallback((width, height) -> mContext.getUITaskDispatcher().asyncDispatch(() -> {
            if (width > 0) {
                setWidth(width);
            }
            if (height > 0) {
                setHeight(height);
            }
        }));
    }

    @Override
    public void unbind() {
        surfaceView.removeFromWindow();
        surfaceView = null;
        surface = null;
    }

    @Override
    public void onRefreshed(Component component) {
        int newWidth = component.getWidth();
        int newHeight = component.getHeight();
        double videoScale = player.getVideoScale();
        if (videoScale != Constants.NUMBER_NEGATIVE_1 && (newWidth != viewWidth || newHeight != viewHeight)) {
            viewWidth = newWidth;
            viewHeight = newHeight;
            mContext.getUITaskDispatcher().asyncDispatch(() -> updateVideoSize(videoScale));
        }
    }
}
