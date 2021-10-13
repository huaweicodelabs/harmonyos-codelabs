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

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.app.Context;
import ohos.codelabs.distributedvideo.ResourceTable;
import ohos.codelabs.distributedvideo.player.api.ImplPlayModule;
import ohos.codelabs.distributedvideo.player.api.ImplPlayer;
import ohos.codelabs.distributedvideo.player.constant.Constants;
import ohos.codelabs.distributedvideo.player.constant.PlayerStatu;
import ohos.codelabs.distributedvideo.player.manager.GestureDetector;
import ohos.codelabs.distributedvideo.player.util.CommonUtils;
import ohos.codelabs.distributedvideo.player.util.DateUtils;

/**
 * PlayerGestureView
 *
 * @since 2021-04-04
 */
public class PlayerGestureView extends DirectionalLayout implements ImplPlayModule, GestureDetector.OnGestureListener {
    private static final int MOVING_TYPE_INIT = -1;
    private static final int VOLUME_TYPE = 0;
    private static final int PROGRESS_DOWN_TYPE = 1;
    private static final int PROGRESS_UP_TYPE = 2;
    private static final int LIGHT_BRIGHT_TYPE = 3;
    private ImplPlayer player;
    private int currentPercent;
    private int currentMoveType;
    private int beginPosition;

    private Image gestureImage;
    private Text gestureText;

    /**
     * constructor of PlayerGestureView
     *
     * @param context context
     */
    public PlayerGestureView(Context context) {
        this(context, null);
    }

    /**
     * constructor of PlayerGestureView
     *
     * @param context context
     * @param attrSet attSet
     */
    public PlayerGestureView(Context context, AttrSet attrSet) {
        this(context, attrSet, null);
    }

    /**
     * constructor of PlayerGestureView
     *
     * @param context context
     * @param attrSet attSet
     * @param styleName styleName
     */
    public PlayerGestureView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView();
        hide();
    }

    private void initView() {
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(
                RgbColor.fromArgbInt(CommonUtils.getColor(mContext, ResourceTable.Color_half_transparent)));
        shapeElement.setCornerRadius(Constants.NUMBER_25);
        setOrientation(DirectionalLayout.VERTICAL);
        setAlignment(LayoutAlignment.HORIZONTAL_CENTER);
        setBackground(shapeElement);
        gestureImage = new Image(mContext);
        gestureImage.setWidth(Constants.NUMBER_150);
        gestureImage.setHeight(Constants.NUMBER_150);
        gestureImage.setScaleMode(Image.ScaleMode.STRETCH);
        gestureImage.setMarginsTopAndBottom(Constants.NUMBER_40, Constants.NUMBER_10);
        gestureText = new Text(mContext);
        gestureText.setTextSize(Constants.NUMBER_36);
        gestureText.setTextColor(new Color(CommonUtils.getColor(mContext, ResourceTable.Color_white)));
        gestureText.setMarginsTopAndBottom(Constants.NUMBER_10, Constants.NUMBER_40);
        addComponent(gestureImage);
        addComponent(gestureText);
    }

    private void show(int type, String content) {
        int gestureImg = ResourceTable.Media_ic_horns;
        switch (type) {
            case VOLUME_TYPE:
                gestureImg = ResourceTable.Media_ic_horns;
                break;
            case PROGRESS_DOWN_TYPE:
                gestureImg = ResourceTable.Media_ic_backward;
                break;
            case PROGRESS_UP_TYPE:
                gestureImg = ResourceTable.Media_ic_forward;
                break;
            case LIGHT_BRIGHT_TYPE:
                gestureImg = ResourceTable.Media_ic_bright;
                break;
            default:
                break;
        }
        gestureImage.setPixelMap(gestureImg);
        gestureText.setText(content);
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
    }

    private void hide() {
        if (getVisibility() == VISIBLE) {
            setVisibility(HIDE);
        }
    }

    @Override
    public void bind(ImplPlayer implPlayer) {
        this.player = implPlayer;
        this.player.addPlayerStatuCallback(status -> mContext.getUITaskDispatcher().asyncDispatch(() -> {
            if (status == PlayerStatu.STOP || status == PlayerStatu.COMPLETE) {
                hide();
            }
        }));
    }

    @Override
    public void unbind() {
    }

    @Override
    public void onTouchBegin(float focusX, float focusY) {
        if (player != null) {
            currentPercent = 0;
            currentMoveType = MOVING_TYPE_INIT;
            beginPosition = player.getCurrentPosition();
        }
    }

    @Override
    public boolean onTouchMoving(int direction, float focusX, float focusY, float distance) {
        if (currentMoveType == MOVING_TYPE_INIT) {
            currentMoveType = direction;
        }
        if (currentMoveType == direction && player != null) {
            if (currentMoveType == GestureDetector.MOVING_HORIZONTAL) {
                currentPercent += distance < 0 ? Constants.NUMBER_1000 : -Constants.NUMBER_1000;
                int type = currentPercent < 0
                        ? PlayerGestureView.PROGRESS_DOWN_TYPE : PlayerGestureView.PROGRESS_UP_TYPE;
                int value = Math.abs(currentPercent);
                String content = (currentPercent < 0 ? "- " : "+ ") + DateUtils.msToString(value);
                show(type, content);
            } else if (currentMoveType == GestureDetector.MOVING_VERTICAL) {
                int interval = getHeight() / Constants.NUMBER_2;
                if (focusX <= getWidth() / Constants.NUMBER_FLOAT_2) {
                    show(PlayerGestureView.LIGHT_BRIGHT_TYPE, "100%");
                } else {
                    float volume = player.getVolume() + distance / interval;
                    volume = (volume < 0) ? 0 : Math.min(volume, 1);
                    show(PlayerGestureView.VOLUME_TYPE, (int) (volume * Constants.NUMBER_100) + "%");
                    player.setVolume(volume);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouchCancel(int direction) {
        if (currentMoveType == GestureDetector.MOVING_HORIZONTAL && player != null) {
            player.rewindTo(beginPosition + currentPercent);
        }
        hide();
        return true;
    }
}
