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

import com.huawei.codelab.ResourceTable;
import com.huawei.codelab.slice.MainAbilitySlice;
import com.huawei.codelab.util.Constants;
import com.huawei.codelab.util.GameUtils;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * gameView
 *
 * @since 2021-03-05
 */
public class GameView extends Component implements Component.DrawTask {
    private static int status; // 状态 0主页面 1游戏进行中 2游戏暂停 3游戏结束
    private static int frame; // 画面帧数
    private static final int CREATE_ENEMY_PLANE_FRAME = 50; // 生成敌机帧数
    private static final int CREATE_BOMB_FRAME = 2000; // 生成手榴弹帧数
    private static final int CREATE_EXPLOSION_FRAME = 8; // 最后一架飞机爆炸帧数
    private static final int SPEED_RATE = 2;
    private static final int MAXMYPLANENUM = 2;
    private static final int SMALL_ENEMY_PLANE_INDEX = 2;

    private static final int PAINT_SIZE = 50; // 字体大小
    private static final int SCORE = 100; // 分数

    private static final int LEFT_BOMB_PIC_H_POSITION = 50; // 左下角炸弹图标水平位置
    private static final int LEFT_BOMB_PIC_V_POSITION = 300; // 左下角炸弹图标垂直位置
    private static final int LEFT_BOMB_NUM_H_POSITION = 150; // 左下角炸弹数量水平位置
    private static final int LEFT_BOMB_NUM_V_POSITION = 240; // 左下角炸弹数量垂直方向位置
    private static final int RIGHT_BOMB_PIC_H_POSITION = 200; // 右下角炸弹图标水平位置
    private static final int RIGHT_BOMB_PIC_V_POSITION = 300; // 右下角炸弹图标垂直位置
    private static final int RIGHT_BOMB_NUM_H_POSITION = 100; // 右下角炸弹数量水平位置
    private static final int RIGHT_BOMB_NUM_V_POSITION = 240; // 右下角炸弹数量垂直方向位置

    private static final int MY_PLANE_H_LIMIT = 200; // 我的飞机水平边界
    private static final int MY_PLANE_V_LIMIT = 400; // 我的飞机垂直边界
    private static final int MY_PLANE_INITIAL_POSITION = 300; // 我的飞机初始位置

    private static final int ANGULAR_180 = 180;

    private static final List<Explosion> explosions = new ArrayList<>(0); // 爆炸效果集合

    private static PixelMapHolder myPlaneOnePixelMapHolder; // 我的飞机一
    private static PixelMapHolder myPlaneTwoPixelMapHolder; // 我的飞机二
    private static PixelMapHolder smallEnemyPlanePixelMapHolder; // 小敌机
    private static PixelMapHolder bigEnemyPlanePixelMapHolder; // 大敌机
    private static PixelMapHolder bulletPixelMapHolder; // 子弹
    private static PixelMapHolder boomPixelMapHolder; // 爆炸
    private static PixelMapHolder bulletAwardPixelMapHolder; // 降落伞
    private static PixelMapHolder bombPixelMapHolder; // 炸弹
    private static PixelMapHolder bgPixelMapHolder; // 背景

    private int playerOneScore = 0; // 玩家一分数
    private int playerTwoScore = 0; // 玩家二分数
    private final int screenWidth;
    private final int screenHeight;
    private Canvas nowCanvas;

    private List<String> deviceIds; // 连接手柄id集合
    private List<MyPlane> myPlanes; // 创建飞机集合
    private final List<EnemyPlane> enemyPlanes = new ArrayList<>(0); // 敌机集合
    private final List<Bomb> bombs = new ArrayList<>(0); // 炸弹集合
    private final List<Bullet> bullets = new ArrayList<>(0); // 子弹集合

    private Paint paint;
    private Paint textPaint;

    private final MyEventHandler myEventHandler;
    private final SecureRandom random;

    /**
     * GameView constructor
     *
     * @param context context
     * @param attrSet attrSet
     */
    public GameView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        GameUtils.setContext(context);
        this.screenWidth = GameUtils.getScreenWidth();
        this.screenHeight = GameUtils.getScreenHeight();
        frame = 0;
        myEventHandler = new MyEventHandler(EventRunner.create(true));
        random = new SecureRandom();
        init();
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        this.nowCanvas = canvas;
        // 画背景
        drawBackground();
        // 画我的飞机
        drawMyPlane();
        // 画敌机
        drawEnemyPlane();
        // 画炸弹
        drawBomb();
        // 画爆炸效果
        drawExplosion();
        // 画子弹
        drawBullet();
        frame++;
        // 销毁敌机
        destroyEnemyPlane();
        // 我的飞机被敌机摧毁
        destroyMyPlane();
        // 捡降落伞
        gainBomb();
        // 游戏结束
        if ((status == Constants.GAME_STOP) && (0 == frame % CREATE_EXPLOSION_FRAME)) {
            gameOver();
            return;
        }
        // 通过调用Invalidate()方法使得View持续渲染，实现动态效果
        getContext().getUITaskDispatcher().asyncDispatch(() -> {
            if (status != Constants.GAME_PAUSE) { // 暂停
                invalidate();
            }
        });
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE_STYLE);
        textPaint = new Paint();
        textPaint.setTextAlign(TextAlignment.CENTER);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(PAINT_SIZE);
        addDrawTask(this);
    }

    /**
     * receive PlaneGameAbilitySlice send data
     *
     * @param deviceIdList handles deviceid
     * @since 2021-03-05
     */
    public void start(List<String> deviceIdList) {
        this.deviceIds = deviceIdList;
        this.myPlanes = new ArrayList<>(deviceIds.size());
        setPixelMapHolder();
        // 创建飞机
        createMyPlane();
    }

    // 将图片id转换成PixMapHolder
    private void setPixelMapHolder() {
        myPlaneOnePixelMapHolder = GameUtils.transIdToPixelMapHolder(ResourceTable.Media_planeOne,
                Constants.MY_PLANE_SIZE, Constants.MY_PLANE_SIZE); // 我的飞机一
        myPlaneTwoPixelMapHolder = GameUtils.transIdToPixelMapHolder(ResourceTable.Media_planeTwo,
                Constants.MY_PLANE_SIZE, Constants.MY_PLANE_SIZE); // 我的飞机二
        smallEnemyPlanePixelMapHolder = GameUtils.transIdToPixelMapHolder(ResourceTable.Media_small,
                Constants.SMALL_PLANE_SIZE, Constants.SMALL_PLANE_SIZE); // 小敌机
        bigEnemyPlanePixelMapHolder = GameUtils.transIdToPixelMapHolder(ResourceTable.Media_big,
                Constants.BIG_PLANE_SIZE, Constants.BIG_PLANE_SIZE); // 大敌机
        bulletPixelMapHolder = GameUtils.transIdToPixelMapHolder(ResourceTable.Media_bullet,
                Constants.BULLET_SIZE, Constants.BULLET_SIZE); // 子弹
        boomPixelMapHolder = GameUtils.transIdToPixelMapHolder(ResourceTable.Media_boom,
                Constants.BOOM_SIZE, Constants.BOOM_SIZE); // 爆炸
        bulletAwardPixelMapHolder = GameUtils.transIdToPixelMapHolder(ResourceTable.Media_bullet_award,
                Constants.AWARD_WIDTH, Constants.AWARD_HEIGHT); // 降落伞
        bombPixelMapHolder = GameUtils.transIdToPixelMapHolder(ResourceTable.Media_bomb,
                Constants.BOMB_WIDTH, Constants.BOMB_HEIGHT); // 炸弹
        bgPixelMapHolder = GameUtils.transIdToPixelMapHolder(ResourceTable.Media_bg,
                this.screenWidth, this.screenHeight); // 背景
    }

    // 画背景
    private void drawBackground() {
        // 添加背景
        nowCanvas.drawPixelMapHolder(bgPixelMapHolder, 0, 0, paint);
        // 添加左下角、右下角炸弹数量显示
        for (MyPlane myPlane: myPlanes) {
            if (myPlane.getIndex() == 0) {
                // 左下角添加炸弹
                nowCanvas.drawPixelMapHolder(bombPixelMapHolder, LEFT_BOMB_PIC_H_POSITION,
                        this.screenHeight - LEFT_BOMB_PIC_V_POSITION, paint);
                nowCanvas.drawText(textPaint, "X" + myPlane.getBombNum(), LEFT_BOMB_NUM_H_POSITION,
                        this.screenHeight - LEFT_BOMB_NUM_V_POSITION);
            } else {
                // 右下角添加炸弹
                nowCanvas.drawPixelMapHolder(bombPixelMapHolder,
                        this.screenWidth - RIGHT_BOMB_PIC_H_POSITION,
                        this.screenHeight - RIGHT_BOMB_PIC_V_POSITION, paint);
                nowCanvas.drawText(textPaint, "X" + myPlane.getBombNum(),
                        this.screenWidth - RIGHT_BOMB_NUM_H_POSITION, this.screenHeight - RIGHT_BOMB_NUM_V_POSITION);
            }
        }
    }

    // 创建我的飞机
    private void createMyPlane() {
        int index = 0;
        MyPlane myPlane;
        int position = this.screenWidth / MAXMYPLANENUM - MY_PLANE_INITIAL_POSITION;
        for (String deviceId : deviceIds) {
            if (index == 0) {
                myPlane = new MyPlane(myPlaneOnePixelMapHolder, position,
                        this.screenHeight - MY_PLANE_V_LIMIT);
            } else {
                myPlane = new MyPlane(myPlaneTwoPixelMapHolder, position + index * MY_PLANE_INITIAL_POSITION,
                        this.screenHeight - MY_PLANE_V_LIMIT);
            }
            myPlane.setDeviceId(deviceId);
            myPlane.setIndex(index);
            InnerEvent event = InnerEvent.get(1, 0, myPlane);
            myEventHandler.sendEvent(event);
            myPlanes.add(myPlane);
            index++;
        }
    }

    // 画我的飞机
    private void drawMyPlane() {
        for (MyPlane myPlane : myPlanes) {
            if (!myPlane.isDestroyed()) {
                myPlane.draw(nowCanvas, paint);
            }
        }
    }

    /**
     * handles moving plane
     *
     * @param angle handles send angle
     * @param deviceId handles deviceid
     * @since 2021-03-05
     */
    public void movePlaneByHandles(int angle, String deviceId) {
        MyPlane myPlane = null;
        for (MyPlane nowMyPlane : myPlanes) {
            if (nowMyPlane.getDeviceId().equals(deviceId)) {
                myPlane = nowMyPlane;
            }
        }
        if (myPlane == null) {
            return;
        }
        myPlane.setPlaneX(myPlane.getPlaneX()
                + ((int) (myPlane.getSpeed() * Math.cos(angle * (Math.PI / ANGULAR_180)))));
        myPlane.setPlaneY(myPlane.getPlaneY()
                - ((int) (myPlane.getSpeed() * Math.sin(angle * (Math.PI / ANGULAR_180)))));
        if (myPlane.getPlaneX() < 0) {
            myPlane.setPlaneX(0);
        }
        if (myPlane.getPlaneX() > this.screenWidth - MY_PLANE_H_LIMIT) {
            myPlane.setPlaneX(this.screenWidth - MY_PLANE_H_LIMIT);
        }
        if (myPlane.getPlaneY() < 0) {
            myPlane.setPlaneY(0);
        }
        if (myPlane.getPlaneY() > this.screenHeight - MY_PLANE_V_LIMIT) {
            myPlane.setPlaneY(this.screenHeight - MY_PLANE_V_LIMIT);
        }
    }

    // create enemy plane random
    private void createEnemyPlane() {
        // 随机生成敌机
        int planeIndex = random.nextInt(SPEED_RATE) + SPEED_RATE;
        PixelMapHolder enemyPlanePixelMapHolder = planeIndex == SMALL_ENEMY_PLANE_INDEX
                ? smallEnemyPlanePixelMapHolder : bigEnemyPlanePixelMapHolder;
        EnemyPlane enemyPlane = new EnemyPlane(enemyPlanePixelMapHolder, planeIndex);
        enemyPlanes.add(enemyPlane);
    }

    // create bomb random
    private void createBomb() {
        // 随机生成炸弹
        Bomb bomb = new Bomb(bulletAwardPixelMapHolder);
        bombs.add(bomb);
    }

    // 画敌机
    private void drawEnemyPlane() {
        // 随机生成敌机
        if (0 == frame % CREATE_ENEMY_PLANE_FRAME) {
            createEnemyPlane();
        }
        Iterator<EnemyPlane> iterator = enemyPlanes.iterator();
        while (iterator.hasNext()) {
            EnemyPlane enemyPlane = iterator.next();
            if (!enemyPlane.isDestroyed()) {
                enemyPlane.draw(nowCanvas, paint);
            } else {
                iterator.remove();
            }
        }
    }

    // 画炸弹（屏幕中降落伞）
    private void drawBomb() {
        // 随机生成炸弹
        if (0 == frame % CREATE_BOMB_FRAME) {
            frame = 0;
            createBomb();
        }
        Iterator<Bomb> iterator = bombs.iterator();
        while (iterator.hasNext()) {
            Bomb bomb = iterator.next();
            if (!bomb.isDestroyed()) {
                bomb.draw(nowCanvas, paint);
            } else {
                iterator.remove();
            }
        }
    }

    // 画爆炸效果
    private void drawExplosion() {
        Iterator<Explosion> iterator = explosions.iterator();
        while (iterator.hasNext()) {
            Explosion explosion = iterator.next();
            if (!explosion.isDestroyed()) {
                explosion.draw(nowCanvas, paint);
            } else {
                iterator.remove();
            }
        }
    }

    /**
     * Create a bullet based on deviceId.
     *
     * @param deviceId deviceId
     */
    public void createBulletByDeviceId(String deviceId) {
        createBullet(getMyPlaneByDeviceId(deviceId));
    }

    // 我的飞机创建子弹
    private void createBullet(MyPlane myPlane) {
        if (myPlane == null) {
            return;
        }
        Bullet bullet = new Bullet(bulletPixelMapHolder, myPlane.getPlaneX(), myPlane.getPlaneY());
        bullet.setDeviceId(myPlane.getDeviceId());
        bullets.add(bullet);
    }

    // 画子弹
    private void drawBullet() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            if (!bullet.isDestroyed()) {
                bullet.draw(nowCanvas, paint);
            } else {
                iterator.remove();
            }
        }
    }

    // 子弹碰撞敌机
    private void destroyEnemyPlane() {
        MyPlane myPlane = null;
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            for (MyPlane nowMyPlane : myPlanes) {
                if (nowMyPlane.getDeviceId().equals(bullet.getDeviceId())) {
                    myPlane = nowMyPlane;
                    break;
                }
            }
            if (!bullet.isDestroyed() && (myPlane != null)) {
                EnemyPlane enemyPlane = null;
                Spirit spirit = bullet.collideWithOther(enemyPlanes);
                if (spirit instanceof EnemyPlane) {
                    enemyPlane = (EnemyPlane) spirit;
                }
                if (enemyPlane != null) {
                    createExposion(enemyPlane.getPlaneX(), enemyPlane.getPlaneY());
                    iterator.remove();
                    enemyPlanes.remove(enemyPlane);
                    myPlane.setScore(myPlane.getScore() + SCORE);
                    InnerEvent event = InnerEvent.get(1, 0, myPlane);
                    myEventHandler.sendEvent(event);
                }
            }
        }
    }

    // 我的飞机碰撞敌机
    private void destroyMyPlane() {
        Iterator<MyPlane> iterator = myPlanes.iterator();
        while (iterator.hasNext()) {
            MyPlane myPlane = iterator.next();
            if (!myPlane.isDestroyed()) {
                EnemyPlane enemyPlane = null;
                Spirit spirit = myPlane.collideWithOther(enemyPlanes);
                if (spirit instanceof EnemyPlane) {
                    enemyPlane = (EnemyPlane) spirit;
                }
                judgeGameOver(enemyPlane, myPlane, iterator);
            }
        }
    }

    // 判断飞机被销毁或者游戏结束
    private void judgeGameOver(EnemyPlane enemyPlane, MyPlane myPlane, Iterator<MyPlane> iterator) {
        if (enemyPlane != null) {
            createExposion(myPlane.getPlaneX(), myPlane.getPlaneY());
            createExposion(enemyPlane.getPlaneX(), enemyPlane.getPlaneY());
            enemyPlanes.remove(enemyPlane);
            myPlane.setBombNum(0);
            myPlane.destroy();
            if (myPlanes.size() == MAXMYPLANENUM) {
                iterator.remove();
            } else {
                status = Constants.GAME_STOP; // 游戏结束标识
            }
        }
    }

    // 捡炸弹（我的飞机触碰降落伞）
    private void gainBomb() {
        Iterator<Bomb> iterator = bombs.iterator();
        while (iterator.hasNext()) {
            Bomb bomb = iterator.next();
            if (!bomb.isDestroyed()) {
                MyPlane myPlane = null;
                Spirit spirit = bomb.collideWithOther(myPlanes);
                if (spirit instanceof MyPlane) {
                    myPlane = (MyPlane) spirit;
                }
                if (myPlane != null) {
                    myPlane.setBombNum(myPlane.getBombNum() + 1);
                    bomb.destroy();
                    iterator.remove();
                }
            }
        }
    }

    // 游戏结束
    private void gameOver() {
        MainAbilitySlice.getHandles().get(0).gameOver(playerOneScore, playerTwoScore);
    }

    /**
     * Use bombs to destroy enemy planes based on device IDs.
     *
     * @param deviceId deviceId
     */
    public void bombEnemyPlaneByDeviceId(String deviceId) {
        MyPlane myPlane = getMyPlaneByDeviceId(deviceId);
        if (myPlane == null) {
            return;
        }
        if (myPlane.getBombNum() == 0) {
            return;
        }
        bombEnemyPlane(myPlane);
    }

    // 使用炸弹摧毁敌机
    private void bombEnemyPlane(MyPlane myPlane) {
        int score = 0; // 炸弹摧毁屏幕敌机获得分数
        myPlane.setBombNum(myPlane.getBombNum() - 1);
        for (EnemyPlane enemyPlane : enemyPlanes) {
            if (!enemyPlane.isDestroyed()) {
                createExposion(enemyPlane.getPlaneX(), enemyPlane.getPlaneY());
                score += SCORE;
                enemyPlane.destroy();
            }
        }
        myPlane.setScore(myPlane.getScore() + score);
        InnerEvent event = InnerEvent.get(1, 0, myPlane);
        myEventHandler.sendEvent(event);
    }

    /**
     * pause
     *
     * @param deviceId deviceId
     */
    public void pause(String deviceId) {
        if (getMyPlaneByDeviceId(deviceId) == null) {
            return;
        }
        status = Constants.GAME_PAUSE;
    }

    /**
     * restart game
     *
     * @param deviceId deviceId
     */
    public void restart(String deviceId) {
        if (getMyPlaneByDeviceId(deviceId) == null) {
            return;
        }
        status = Constants.GAME_START;
        getContext().getUITaskDispatcher().asyncDispatch(this::invalidate);
    }

    // 获取状态
    public static int getStatus() {
        return status;
    }

    // 设置状态
    public static void setStatus(int handleStatus) {
        status = handleStatus;
    }

    /**
     * Obtain the number of frames.
     *
     * @return frame
     */
    public static int getFrame() {
        return frame;
    }

    /**
     * Obtains the MyPlane based on deviceId.
     *
     * @param deviceId deviceId
     * @return myfPlane
     */
    private MyPlane getMyPlaneByDeviceId(String deviceId) {
        MyPlane myPlane = null;
        for (MyPlane nowMyPlane : myPlanes) {
            if (nowMyPlane.getDeviceId().equals(deviceId)) {
                myPlane = nowMyPlane;
            }
        }
        return myPlane;
    }

    /**
     * CircleProgressView
     *
     * @param planeX x Coordinates
     * @param planeY y Coordinates
     */
    public static void createExposion(int planeX, int planeY) {
        Explosion explosion = new Explosion(boomPixelMapHolder, planeX, planeY);
        explosions.add(explosion);
    }

    /**
     * myEventHandler
     *
     * @since 2021-03-15
     */
    private class MyEventHandler extends EventHandler {
        MyEventHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            int eventId = event.eventId;
            if (eventId == 1) {
                if (event.object instanceof MyPlane) {
                    MyPlane myPlane = (MyPlane) event.object;
                    if (myPlane.getIndex() == 0) {
                        playerOneScore = myPlane.getScore();
                    } else {
                        playerTwoScore = myPlane.getScore();
                    }
                    MainAbilitySlice.returnScore(myPlane.getScore(), myPlane.getDeviceId());
                }
            }
        }
    }
}