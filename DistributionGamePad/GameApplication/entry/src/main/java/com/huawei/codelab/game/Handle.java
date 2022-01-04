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

package com.huawei.codelab.game;

import com.huawei.codelab.slice.MainAbilitySlice;
import com.huawei.codelab.util.Constants;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.bundle.ElementName;

/**
 *  handle
 *
 * @since 2021-03-15
 *
 */
public class Handle {
    private String deviceId; // 连接设备id
    private boolean isConn; // 是否连接
    private MainAbilitySlice.GameRemoteProxy proxy; // 远程连接类
    private final AbilitySlice abilitySlice; // 当前slice对象
    private GameView gameView;

    /**
     *  handle constructor
     *
     * @param abilitySlice abilitySlice
     * @since 2021-03-15
     *
     */
    public Handle(AbilitySlice abilitySlice) {
        this.abilitySlice = abilitySlice;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isConn() {
        return isConn;
    }

    public void setConn(boolean isNowConn) {
        this.isConn = isNowConn;
    }

    public MainAbilitySlice.GameRemoteProxy getProxy() {
        return proxy;
    }

    public void setProxy(MainAbilitySlice.GameRemoteProxy proxy) {
        this.proxy = proxy;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    // 开始/重新开始
    private void startGame() {
        Intent intent = new Intent();
        ElementName element = new ElementName("", abilitySlice.getBundleName(),
                abilitySlice.getBundleName() + ".PlaneGameAbility");
        intent.setElement(element);
        abilitySlice.startAbility(intent);
    }

    // 退出到主页面
    private void quitGame() {
        Intent intent = new Intent();
        ElementName element = new ElementName("", abilitySlice.getBundleName(),
                abilitySlice.getBundleName() + ".MainAbility");
        intent.setElement(element);
        abilitySlice.startAbility(intent);
    }

    /**
     * game over
     *
     * @param playerOneScore playerOneScore
     * @param playerTwoScore playerTwoScore
     */
    public void gameOver(int playerOneScore, int playerTwoScore) {
        Intent intent = new Intent();
        intent.setParam("playerOneScore", playerOneScore);
        intent.setParam("playerTwoScore", playerTwoScore);
        ElementName element = new ElementName("", abilitySlice.getBundleName(),
                abilitySlice.getBundleName() + ".GameOverAbility");
        intent.setElement(element);
        abilitySlice.startAbility(intent);
        GameView.setStatus(Constants.GAME_STOP);
    }

    // 移动飞机
    private void movePlane(int angle) {
        gameView.movePlaneByHandles(angle, deviceId);
    }

    // 发射子弹
    private void createBullet() {
        gameView.createBulletByDeviceId(deviceId); // 发射子弹
    }

    // 使用炸弹
    private void useBomb() {
        gameView.bombEnemyPlaneByDeviceId(deviceId);
    }

    // 暂停
    private void pause() {
        gameView.pause(deviceId);
    }

    // 取消暂停
    private void reStart() {
        gameView.restart(deviceId);
    }

    /**
     * Processing Handle End Data
     *
     * @param angle angle
     * @param buttonA buttonA
     * @param buttonB buttonB
     * @param pause pause
     * @param start start
     */
    public void operation(int angle, int buttonA, int buttonB, int pause, int start) {
        switch (GameView.getStatus()) {
            case Constants.GAME_UNREADY: // 0游戏未开始
                if (start != 0) {
                    startGame(); // 开始游戏
                    GameView.setStatus(Constants.GAME_START);
                }
                break;
            case Constants.GAME_START: // 1游戏进行中
                if (angle != 0) {
                    movePlane(angle); // 移动飞机
                }
                if (buttonA != 0) {
                    createBullet(); // 发射子弹
                }
                if (buttonB != 0) {
                    useBomb(); // 使用炸弹（清空屏幕敌机）
                }
                if (pause != 0) {
                    pause(); // 暂停
                }
                break;
            case Constants.GAME_PAUSE: // 2游戏暂停
                if (start != 0) { // 取消暂停
                    reStart();
                    GameView.setStatus(Constants.GAME_START);
                }
                break;
            case Constants.GAME_STOP: // 3游戏结束
                if (pause != 0) { // 返回到主页
                    quitGame();
                    GameView.setStatus(Constants.GAME_UNREADY);
                }
                if (start != 0) { // 重新开始游戏
                    startGame();
                    GameView.setStatus(Constants.GAME_START);
                }
                break;
            default:
                break;
        }
    }
}
