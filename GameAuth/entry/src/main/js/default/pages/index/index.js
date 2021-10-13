/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import device from '@system.device';
import prompt from '@system.prompt';
import app from '@system.app';


var intervalId;

var BIT = 4;
var COLLISION_SPEEDY = 0.3;
var COLLISION_SPEEDX = 0.8;
var targetItem;
var intervalTime = 30;
var JS_PATH = 'com.huawei.gameauth.manager.JSInterface';

var BUNDLE_NAME = '';
var ABILITY_NAME = '';

export default {
    data: {
        // 图片数组
        imgArray: ['common/images/product0.png', 'common/images/product1.png',
        'common/images/product2.png', 'common/images/product3.png',
        'common/images/product4.png', 'common/images/product5.png',
        'common/images/product6.png'],
        //在屏幕中出现的数据
        modes: [],
        //屏幕宽度
        screenWidth: 0,
        //屏幕高度
        screenHeight: 0,
        //state=0开始游戏state=1合成成功state=2合成失败
        state: 0,
        //每次定时器中保证任务执行一次
        taskCompleted: false,
        // 是否开始游戏页面，开始游戏按钮界面和图形运动界面
        isStart: false,
        // 是否是游戏界面，不是游戏界面就是授权的界面
        isGame: true,
        // 游戏是否结束，结束出现倒计时界面
        isEnd: false,
        //倒计时退出应用
        countdown: 3,
        //可授权的设备
        deviceList: [],
        // 本地的NetWorkId
        localNetWorkId: '',
        //type=0 忽略 type=1允许玩游戏 type=2 拒绝玩游戏
        type: 0
    },
    onInit() {
        if (this.networkId == undefined || this.networkId == '') {
            this.isGame = true;
        } else {
            this.isGame = false;
        }

        var that = this;
        setTimeout(function () {
            that.initBundleAndName();
        }, 1000);
    },
    /**
     * 开始游戏
     */
    startGame() {
        if (this.isStart) {
            var that = this;
            setTimeout(function () {
                // 首先得到屏幕的宽度和高度
                that.getscreenWidthAndHeight();
                // 启动定时器，循环刷新页面
                intervalId = setInterval(that.forInterval, intervalTime);
            }, 1000);
        }
    },
    /**
     * 关闭页面，取消定时器.
     */
    onDestroy() {
        if (intervalId != undefined) {
            clearInterval(intervalId);
        }
    },
    /**
     * 得到屏幕的宽度和高度
     */
    getscreenWidthAndHeight() {
        var that = this;
        device.getInfo({
            success: function (data) {
                that.screenWidth = data.windowWidth / data.screenDensity;
                that.screenHeight = data.windowHeight / data.screenDensity;
            },
            fail: function (data, code) {
                console.info('==:' + code + '; ==: ' + data);
            },
        });
    },
    /**
     * 循环计算与刷新页面
     */
    forInterval() {
        // state=0才计算刷新页面，并保证每隔50毫秒执行一次
        if (this.state == 0) {
            if (this.taskCompleted) {
                return;
            }
            this.taskCompleted = true;
            this.excuteTask();
            this.taskCompleted = false;
        }
    },
    /**
     * 每一帧任务
     */
    excuteTask() {
        // 获取运行中的图形
        var runproducts = this.getRunData();

        if (runproducts.length == 0) {
            this.withOutRunProduct();
        } else {
            for (var i = 0; i < runproducts.length; i++) {
                this.dealRunProduct(runproducts[i]);
            }
            this.updateOverlap();
        }

        this.boundaryCheck();
    },
    /**
     * 处理运动中的图形
     */
    dealRunProduct(runProduct) {
        var occursProducts = this.occursCollision(runProduct);
        // 没有发生碰撞
        if (occursProducts.length == 0) {
            this.withOutCollision(runProduct);
        } else {
            //碰撞是否可以合成
            var compose = false;
            for (var i = 0; i < occursProducts.length; i++) {
                var temp = this.composeProduct(occursProducts[i], runProduct);
                if (temp) {
                    compose = true;
                }
            }
            // 碰撞不能合成的话
            if (!compose) {
                this.updateCollisionSpeed(occursProducts, runProduct);
            }
        }
    },
    updateCollisionSpeed(occursProductArray, runProduct) {
        if (this.reachBoundary(runProduct) || occursProductArray.length > 1) {
            runProduct.speedX = 0;
            runProduct.speedY = 0;
            targetItem = undefined;
        } else {
            var occursProduct = occursProductArray[0];
            var speed = 0;
            if (this.getCenterX(runProduct) > this.getCenterX(occursProduct)) {
                speed = BIT * 1.1;
            } else if (this.getCenterX(runProduct) <= this.getCenterX(occursProduct)) {
                speed = -BIT * 1.1;
            }
            runProduct.speedX = speed;
            runProduct.speedY = BIT * COLLISION_SPEEDY;
        }
    },
    /**
     * 合成图形
     */
    composeProduct(occursProduct, runProduct) {
        if (this.isCompose(runProduct, occursProduct)) {
            this.compoundData(runProduct, occursProduct)


            // 有闪烁问题，如何解决？
            this.deleteData(occursProduct);
            this.deleteData(runProduct);


            if (runProduct.lever + 1 == this.imgArray.length - 1) {
                this.state = 1;
                clearInterval(intervalId);
                this.showGameOver('合成成功，是否再玩一次?');
            }
            return true;
        }
        return false;
    },

    /**
     * 删除数据
     */
    deleteData(data) {

        this.modes.forEach((item, index) => {
            if (data === item) {
                this.modes.splice(index, 1);
                return;
            }
        });
    },
    /**
     * 图形能否合成
     */
    isCompose(productA, productB) {
        return productA.lever == productB.lever;
    },
    /**
     * 运动图形没有碰撞的，但与边界有接触的处理
     */
    withOutCollision(runProduct) {
        if (runProduct.left <= 0 || runProduct.left + runProduct.width >= this.screenWidth) {
            runProduct.speedX = -runProduct.speedX;
        }
        if (runProduct.top + runProduct.height >= this.screenHeight) {
            runProduct.speedY = 0;
        }
        if (runProduct.speedY == 0) {
            runProduct.speedX = 0;
            targetItem = undefined;
        }
    },
    /**
     * 没有运动的图形时
     */
    withOutRunProduct() {
        // 先判断有没有需要掉落的图形
        if (!this.dropProduct()) {
            var fullScreen = this.fullScreen();
            if (!fullScreen) {
                this.addNewData();
            } else {
                this.state = 2;
                clearInterval(intervalId);
                this.showGameOver('合成失败，游戏结束');
            }
        }
    },
    /**
     * 显示游戏合成成功或失败的对话框
     */
    showGameOver(title) {
        var that = this;
        prompt.showDialog({
            title: '提示',
            message: title,
            buttons: [
                {
                    text: '重玩',
                    color: '#666666',
                },
                {
                    text: '退出游戏',
                    color: '#666666',
                },
            ],
            success: function (data) {
                if (data.index == 0) {
                    that.reStartGame();
                } else if (data.index == 1) {
                    app.terminate();
                }
            },
            cancel: function () {
                that.reStartGame();
            },
        });
    },
    /**
     * 重新玩游戏
     */
    reStartGame() {
        this.modes = [];
        intervalId = setInterval(this.forInterval, intervalTime);
        this.state = 0;
    },
    /**
     * 是否满屏了
     */
    fullScreen() {
        var full = false;
        this.modes.forEach(product => {
            if (this.getCenterY(product) - product.height < 0) {
                full = true;
                return true;
            }
        });
        return full;
    },
    /**
     * 判断有没有可以掉落的图形
     */
    dropProduct() {
        var isDrop = false;
        this.modes.forEach(product => {
            if (this.isStopProduct(product)) {
                if (product.top + product.height < this.screenHeight) {
                    // 没有与其他图形发生接触的需要继续运动
                    var occursProducts = this.occursCollision(product);
                    if (occursProducts.length == 0) {
                        product.speedY = BIT * COLLISION_SPEEDY;
                        isDrop = true;
                        return isDrop;
                    }
                    // 接触图形一个
                    var result = this.dropWith2Contact(occursProducts, product);
                    if (result) {
                        isDrop = true;
                        return isDrop;
                    }
                    // 接触图形两个
                    result = this.dropWith3Contact(occursProducts, product);
                    if (result) {
                        isDrop = true;
                        return isDrop;
                    }
                    result = this.dropProduct4(product, occursProducts);
                    if (result) {
                        isDrop = true;
                        return isDrop;
                    }
                }
            }
        });
        return isDrop;
    },
    // 掉落的情况
    dropProduct4(product, occursProductArray) {
        if (occursProductArray.length == 2 && !this.reachBoundary(product)) {
            var occursProduct0 = this.getCenterY(occursProductArray[0]) > this.getCenterY(occursProductArray[1])
                ? occursProductArray[0] : occursProductArray[1];
            var occursProduct1 = this.getCenterY(occursProductArray[0]) > this.getCenterY(occursProductArray[1])
                ? occursProductArray[1] : occursProductArray[0];
            //
            if (this.getCenterY(product) > this.getCenterY(occursProduct1)
            && this.getCenterY(product) < this.getCenterY(occursProduct0)) {
                if (this.getCenterX(product) > this.getCenterX(occursProduct0)
                && this.getCenterX(product) > this.getCenterX(occursProduct1)) {
                    product.speedX = (BIT * COLLISION_SPEEDX);
                    product.speedY = 1;
                    return true;
                }
                if (this.getCenterX(product) < this.getCenterX(occursProduct0)
                && this.getCenterX(product) < this.getCenterX(occursProduct1)) {
                    product.sepeedX = (-BIT * COLLISION_SPEEDX);
                    product.speedY = 1;
                    return true;
                }
            }
        }
        return false;
    },
    getCenterY(product) {
        return product.top + product.height / 2;
    },
    getCenterX(product) {
        return product.left + product.width / 2;
    },
    /**
     * 接触的有两个图形
     */
    dropWith3Contact(occursProductArray, product) {
        if (occursProductArray.length == 2) {
            var occursProduct0 = occursProductArray[0];
            var occursProduct1 = occursProductArray[1];
            // 接触的两个图形都在上方
            if (occursProduct0.top + occursProduct0.heigth / 2 < product.top + product.heigth / 2
            && occursProduct1.top + occursProduct1.heigth / 2 < product.top + product.heigth / 2) {
                product.speedY = BIT /** COLLISION_SPEEDY*/
                ;
                return true;
            }
        }
        return false;
    },
    /**
     * 接触的有一个图形
     */
    dropWith2Contact(occursProductArray, product) {
        if (occursProductArray.length == 1) {
            var occursProduct = occursProductArray[0];
            //在接触图形的下方，直接下落
            if (occursProduct.top + occursProduct.width / 2 < product.top + product.width / 2) {
                product.speedY = BIT;
            } else {
                // 在接触图形的上方，但与左右两边界接触 忽略
                if (product.left <= 0) {
                    return false;
                }
                if (product.left + product.width >= this.screenWidth) {
                    return false;
                }

                // 在接触图形的上方 通过x在左边还是右边确定x的速度
                product.speedY = BIT * COLLISION_SPEEDY;
                product.speedX = (product.left + product.width / 2 > occursProduct.left + occursProduct.width / 2
                    ? BIT
                    : -BIT);
            }
            return true;
        }
        return false;
    },
    /**
     * 获取与图形A所有碰撞的图形
     */
    occursCollision(productA) {
        var products = [];
        this.modes.forEach(item => {
            if (item != productA) {
                if (this.isCollision(productA, item)) {
                    products.push(item);
                }
            }
        });
        return products;
    },
    /**
     * 合成图形
     */
    compoundData(run, still) {
        var index = run.lever + 1;
        var src = this.imgArray[index];
        var width = 50 + index * 10;
        var height = 50 + index * 10;
        var top;
        var left;

        top = run.top;
        left = (run.left + still.left) / 2;

        this.modes.push({
            lever: index,
            width: width,
            height: height,
            src: src,
            top: top,
            left: left,
            speedX: 0,
            speedY: BIT,
        })
    },


    /**
     * 判断是否碰撞
     */
    isCollision(run, other) {
        var runCenterX = run.left + run.width / 2;
        var runCenterY = run.top + run.width / 2;
        var otherCenterX = other.left + other.width / 2;
        var otherCenterY = other.top + other.width / 2;
        var distance = Math.sqrt(Math.abs(runCenterX - otherCenterX) * Math.abs(runCenterX - otherCenterX) +
        Math.abs(runCenterY - otherCenterY) * Math.abs(runCenterY - otherCenterY));
        if (distance < (run.width + other.width) / 2) {
            return true;
        }


        return false;
    },

    /**
     * 添加新的图形
     */
    addNewData() {
        var index = Math.floor(Math.random() * 4);
        var src = this.imgArray[index];
        var width = 50 + index * 10;
        var height = 50 + index * 10;
        var left = Math.floor(Math.random() * (this.screenWidth - width));

        this.modes.push({
            lever: index,
            width: width,
            height: height,
            src: src,
            top: 0,
            left: left,
            speedX: 0,
            speedY: BIT,
        })
    },

     /**
     * 获取运动的图形
     */
    getRunData() {
        var run = [];
        this.modes.forEach(item => {
            if (!this.isStopProduct(item)) {
                run.push(item);
            }
        });
        return run;
    },
    /**
     * 图形是否停止
     */
    isStopProduct(product) {
        return product.speedX == 0 && product.speedY == 0;
    },
    /**
     * 图形是否在边界
     */
    reachBoundary(product) {
        if (product.top + product.height >= this.screenHeight) {
            return true;
        }
        if (product.left <= 0) {
            return true;
        }
        return product.left + product.width >= this.screenWidth;
    },
    /**
     * 处理重叠的问题
     */
    updateOverlap() {
        this.modes.forEach(item => {
            if (item.top + item.height < this.screenHeight) {
                this.modes.forEach(item2 => {
                    if (item != item2) {
                        if (this.getCenterY(item) < this.getCenterY(item2)) {
                            this.dealOverlap(item, item2);
                        }
                    }
                });
            }
        });
    },
    //productA运动的图形
    dealOverlap(productA, productB) {
        var runCenterX = productA.left + productA.width / 2;
        var runCenterY = productA.top + productA.width / 2;
        var otherCenterX = productB.left + productB.width / 2;
        var otherCenterY = productB.top + productB.width / 2;


        var minLength = Math.pow((productA.width + productB.width) / 2 - 6, 2);
        var powX = Math.pow(Math.abs(runCenterX - otherCenterX), 2);
        var powY = Math.pow(Math.abs(runCenterY - otherCenterY), 2);
        var maxLength = powX + powY;


        if (Math.sqrt(maxLength) < Math.sqrt(minLength)) {
            var rad = (productA.width + productB.width) / 2 - Math.sqrt(maxLength);
            var moveX = rad / Math.sqrt(1 + powY / powX);
            var moveY = rad / Math.sqrt(1 + powX / powY);
            var centerX;
            var centerY = (this.getCenterY(productA) - moveY);
            if (this.getCenterX(productA) > this.getCenterX(productB)) {
                centerX = (this.getCenterX(productA) + moveX);
                if (this.screenWidth - productA.width / 2 <= centerX) {
                    centerY = centerY - (centerX + productA.width / 2 - this.screenWidth);
                    centerX = this.screenWidth - productA.width / 2;
                }
            } else {
                centerX = (this.getCenterX(productA) - moveX);
                if (productA.width / 2 >= centerX) {
                    centerY = centerY - (productA.width / 2 - centerX);
                    centerX = productA.width / 2;
                }
            }
            productA.left = (centerX - productA.width / 2);
            productA.top = (centerY - productA.width / 2);
        }
    },
    /**
     * 边界检查
     */
    boundaryCheck() {
        this.modes.forEach(item => {
            item.top += item.speedY;
            item.left += item.speedX;

            if (item.left <= 0) {
                item.left = 0;
            }
            if (item.left >= this.screenWidth - item.width) {
                item.left = this.screenWidth - item.width;
            }
            if (item.top >= this.screenHeight - item.width) {
                item.top = this.screenHeight - item.width;
                item.speedY = 0;
                item.speedX = 0;
            }
        });
    },
    /**
     * 手指开始按下事件
     * @param msg 事件
     */
    touchStart(msg) {
        // 按下时就确定是不是触摸到运动的图形并赋值给targetItem
        var xx = msg.touches[0].globalX;
        var yy = msg.touches[0].globalY;
        this.modes.forEach(item => {
            if (!this.isStopProduct(item)) {
                if (item.left < xx && xx < item.left + item.width && item.top < yy && yy < item.top + item.height) {
                    targetItem = item;
                    return;
                }
            }
        });
    },
    /**
     * 手指移动事件
     * @param msg 事件
     */
    touchMove(msg) {
        if (targetItem != undefined) {
            targetItem.left = msg.touches[0].globalX - targetItem.width / 2;
        }
    },
    /**
     * 手指取消事件(焦点失去)
     * @param msg 事件
     */
    touchCancel() {
        // 手指失去焦点 把targetItem置空
        targetItem = undefined;
    },
    /**
     * 手指抬起事件
     * @param msg 事件
     */
    touchEnd() {
        // 手指失去焦点 把targetItem置空
        targetItem = undefined;
    },

    /**
     * 定时器，倒计时3秒退出应用
     */
    endGame() {
        intervalId = setInterval(this.endGameDown, 1000);
    },
    /**
     * 定时器，倒计时3秒退出应用
     */
    endGameDown() {
        if (this.countdown <= 0) {
            clearInterval(intervalId);
            app.terminate();
        } else {
            this.countdown --;
        }
    },

    /**
     * 初始化Bundle
     */
    initBundleAndName(){
        // @ts-ignore
        this.javaInterface = createLocalParticleAbility(JS_PATH);
        this.javaInterface.getBundleName().then(result => {
            BUNDLE_NAME=result;
        });
        this.javaInterface.getAbilityName().then(result => {
            ABILITY_NAME=result;
        });
    },

    /**
     * 点击‘启动游戏’按钮，申请权限，得到本地的NetWorkId
     */
    async startFA() {
        var that = this;
        // @ts-ignore
        this.javaInterface = createLocalParticleAbility(JS_PATH);
        // 使用calculateAsync异步返回，callback返回结果。
        // getNetWorkIdAsync方法名字要和JAVA中JSInterface类的getNetWorkIdAsync方法名字一致
        this.javaInterface.getNetWorkIdAsync(result => {
            // 不能使用this.localNetWorkId访问data下的字段，先把this赋值给that
            that.localNetWorkId = result;
            this.showDeviceList();
        })
    },
    /**
     * 显示设备列表
     */
    async showDeviceList() {
        // 获取所有在线设备列表
        // @ts-ignore
        var ret = await FeatureAbility.getDeviceList(0);
        if (ret.code === 0) {
            this.deviceList = [];
            for (var i = 0; i < ret.data.length; i++) {
                // 将结果集存放在数组中
                this.deviceList.push({
                    deviceName: ret.data[i].deviceName,
                    networkId: ret.data[i].networkId
                });
            }
        }
        // 在对话框中显示列表
        if (this.deviceList.length > 0) {
            this.$element('showDialog').show();
        } else {
            prompt.showToast({
                message: '附近没有搜索到可授权设备',
                duration: 1000,
            });
        }
    },
    /**
     * 打开远程设备Ability
     */
    async selectDevice(networkId) {
        this.$element('showDialog').close();
        let actionData = {
            networkId: this.localNetWorkId,
        };

        let target = {
            bundleName: BUNDLE_NAME,
            abilityName: ABILITY_NAME,
            networkId: networkId,
            data: actionData
        };

        let result = await FeatureAbility.startAbility(target);
        let ret = JSON.parse(result);
        if (ret.code == 0) {
            prompt.showToast({
                message: '请求权限中...',
                duration: 1000,
            });
        }
    },

    /**
     * 手表端两个按钮事件(允许，不允许)
     * @param val 类型
     */
    isAllowGame(val) {
        // JS调用java的方法
        // @ts-ignore
        this.javaInterface = createLocalParticleAbility(JS_PATH);
        this.javaInterface.isAllowGameAsync(val, result => {
            // 远程拉起FA
            this.startRemoteAbility(result == 0 ? 1 : 2);
            // 显示一下
            if (result == 0) {
                prompt.showToast({
                    message: '允许玩游戏',
                    duration: 1000,
                });
            } else {
                prompt.showToast({
                    message: '已拒绝玩游戏',
                    duration: 1000,
                });
            }
            // 1.5秒后关闭应用
            setTimeout(function () {
                app.terminate();
            }, 1500);
        });
    },

    /**
     * 手表端同意或不同意 ，启动手机端FA(这时的手机端FA的实例已经存在)
     * @param type
     */
    startRemoteAbility(type) {
        //通过type传给手机端，type=1允许玩游戏 type=2拒绝玩游戏
        let actionData = {
            type: type,
        };

        let target = {
            bundleName: BUNDLE_NAME,
            abilityName: ABILITY_NAME,
            //这个networkId是手机端发起FA时发送过来的哦，在data中没有指定，但可以这么调用传递过来的字段哦
            networkId: this.networkId,
            data: actionData
        };

        FeatureAbility.startAbility(target);
    },

    /**
     * launchType设置为singleton模式，MainAbility如果已经启动再次调用FeatureAbility.startAbility()方法，那么这个onNewRequest会被调用哦
     * 但是有种情况也会调用，就是按下Home键，再次点击桌面图标，这个方法也会调用，所以使用type=0控制不做任何处理
     */
    onNewRequest() {
        if (this.type == 1) {
            this.isGame = true;
            this.isStart = true;
            this.startGame();
        } else if (this.type == 2) {
            this.isGame = true;
            this.isStart = false;
            this.isEnd = true;
            this.endGame();
        }
    }
}
