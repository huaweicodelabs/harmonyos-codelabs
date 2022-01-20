import router from '@system.router';
import prompt from '@system.prompt';

export default {
    data: {
        pageWord: {
            searchKeyWord: '寻找宝贝、店铺',
            searchValue: '手机',
            shopingCart: '购物车',
            settlement: '结算',
            nickname: '小明同学',
            memberName: '会员名:',
            myDeals: '我的交易',
            more: '更多'
        },
        priceTotal: 0,
        flag: 1,
        zeroFlag: 0,
        oneFlag: 1,
        twoFlag: 2,
        threeFlag: 3,
        fourFlag: 4,
        titileList: ['热销单品', '精品推荐', '智慧生活', '年货节'],
        contentList: ['First', 'Second', 'Third', 'Four'],
        latestList: [],
        hotList: [
            {
                title: 'HUAWEI nova 8 Pro ',
                content: '10:08限时开售',
                price: '3999',
                imgSrc: "/common/picture/HW (1).png"
            }, {
                title: 'HUAWEI Mate 30E Pro 5G',
                content: '享3期免息  ',
                price: '5299',
                imgSrc: "/common/picture/HW (2).png"
            }, {
                title: 'HUAWEI MatePad Pro',
                content: '旗舰甄选 ',
                price: '3799',
                imgSrc: "/common/picture/HW (3).png"
            }, {
                title: '华为畅享20 SE',
                content: '新品上市 ',
                price: '1499',
                imgSrc: "/common/picture/HW (4).png"
            }, {
                title: 'HUAWEI WATCH FIT',
                content: '智能生活助手',
                price: '769',
                imgSrc: "/common/picture/HW (5).png"
            }
        ],
        fineProductList: [
            {
                title: 'HUAWEI MateBook X Pro 2021款',
                content: '商务轻薄笔记本  ',
                price: '9999',
                imgSrc: "/common/picture/HW (6).png"
            }, {
                title: 'HUAWEI Mate 30 RS 保时捷设计',
                content: '致敬时代 ',
                price: '12999',
                imgSrc: "/common/picture/HW (7).png"
            }, {
                title: '华为智慧屏 55英寸',
                content: '享3期免息',
                price: '4299',
                imgSrc: "/common/picture/HW (8).png"
            }, {
                title: '华为畅享20 Pro',
                content: '购机赠耳机 ',
                price: '1999',
                imgSrc: "/common/picture/HW (9).png"
            }, {
                title: '华为智能体脂秤 3',
                content: '享3期免息',
                price: '169',
                imgSrc: "/common/picture/HW (10).png"
            }
        ],
        wisdomList: [
            {
                title: 'HUAWEI P40 Pro+ 5G',
                content: '限量赠保护壳   ',
                price: '7988',
                imgSrc: "/common/picture/HW (11).png"
            }, {
                title: 'HUAWEI FreeBuds Pro',
                content: '享3期免息',
                price: '1099',
                imgSrc: "/common/picture/HW (12).png"
            }, {
                title: 'HUAWEI MateBook X',
                content: '享3期免息   ',
                price: '8999',
                imgSrc: "/common/picture/HW (13).png"
            }, {
                title: '红帕智能降温杯',
                content: '智能喝水提醒   ',
                price: '179',
                imgSrc: "/common/picture/HW (14).png"
            }, {
                title: 'YESOUL野小兽智能动感单车S1',
                content: '静音不扰邻   ',
                price: '1299',
                imgSrc: "/common/picture/HW (15).png"
            }],
        allList: [],
        icon: {
            buys: "/common/nav/icon-buy.png",
            buy: "/common/nav/icon-buy.png",
            home: '/common/nav/icon-home.png',
            messages: '/common/nav/icon-message.png',
            message: '/common/nav/icon-message.png',
            messageSelect: '/common/nav/icon-message-select.png',
            shoppingCarts: '/common/nav/icon-shopping-cart.png',
            shoppingCart: '/common/nav/icon-shopping-cart.png',
            shoppingCartSelect: '/common/nav/icon-shopping-cart-select.png',
            mys: '/common/nav/iocn-my.png',
            my: '/common/nav/iocn-my.png',
            mySelect: '/common/nav/icon-my-select.png',
        },
        iconImageFlag: 1,
        menu: [
            {
                id: '01',
                title: '收藏',
                num: '10'
            },
            {
                id: '02',
                title: '历史浏览',
                num: '1000'
            },
            {
                id: '03',
                title: '关注',
                num: '10'
            },
            {
                id: '04',
                title: '粉丝',
                num: '10000'
            }
        ],
        transaction: [
            {
                id: '01',
                title: '我发布的',
                num: '520',
                src: '/common/nav/icon-menu-release.png'
            },
            {
                id: '02',
                title: '我卖出的',
                num: '520',
                src: '/common/nav/icon-menu-sell.png'
            },
            {
                id: '03',
                title: '我买到的',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            }
        ],
        more1: [
            {
                id: '01',
                title: '发布规范',
                num: '520',
                src: '/common/nav/icon-menu-release.png'
            },
            {
                id: '02',
                title: '创作中心',
                num: '520',
                src: '/common/nav/icon-menu-sell.png'
            },
            {
                id: '03',
                title: '我的帖子',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '04',
                title: '我的游戏',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '05',
                title: '我租到的',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '06',
                title: '我的拍卖',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '07',
                title: '我的兼职',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '08',
                title: '我的租房',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '09',
                title: '实名认证',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '10',
                title: '我的红包',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '11',
                title: '客服中心',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '12',
                title: '招商认证',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '13',
                title: '天天拍卖',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '14',
                title: '我的设置',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            },
            {
                id: '15',
                title: '关于',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            }, {
                id: '16',
                title: '作品认证',
                num: '10',
                src: '/common/nav/icon-menu-buy.png'
            }
        ],
        more: [
            [
                {
                    id: '01',
                    title: '发布规范',
                    num: '520',
                    src: '/common/nav/icon-menu-release.png'
                },
                {
                    id: '02',
                    title: '创作中心',
                    num: '520',
                    src: '/common/nav/icon-menu-sell.png'
                },
                {
                    id: '03',
                    title: '我的帖子',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                }
            ],
            [
                {
                    id: '04',
                    title: '我的游戏',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '05',
                    title: '我租到的',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '06',
                    title: '我的拍卖',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                }
            ],
            [
                {
                    id: '07',
                    title: '我的兼职',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '08',
                    title: '我的租房',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '09',
                    title: '实名认证',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                }
            ],
            [
                {
                    id: '10',
                    title: '我的红包',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '11',
                    title: '客服中心',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '12',
                    title: '招商认证',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                }
            ],
            [
                {
                    id: '13',
                    title: '天天拍卖',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '14',
                    title: '我的设置',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '15',
                    title: '关于软件',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                }
            ],
            [
                {
                    id: '16',
                    title: '作品认证',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '17',
                    title: '天天拍卖',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '18',
                    title: '我的设置',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                }
            ],
            [
                {
                    id: '19',
                    title: '关于软件',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                }, {
                    id: '20',
                    title: '作品认证',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                },
                {
                    id: '21',
                    title: '天天拍卖',
                    num: '10',
                    src: '/common/nav/icon-menu-buy.png'
                }
            ],
            [
             {
                 id: '22',
                 title: '我的设置',
                 num: '10',
                 src: '/common/nav/icon-menu-buy.png'
             },
             {
                 id: '23',
                 title: '关于软件',
                 num: '10',
                 src: '/common/nav/icon-menu-buy.png'
             }, {
                 id: '24',
                 title: '作品认证',
                 num: '10',
                 src: '/common/nav/icon-menu-buy.png'
             }
            ]
        ],
    },
    clickAction() {
        router.replace({
            uri: 'pages/index/index',
            params: {}
        });
    },
    change(e) {
        this.allList = [];
        if (e.index === this.zeroFlag) {
            this.allList = [...this.hotList, ...this.fineProductList, ...this.wisdomList];
        } else if (e.index === this.oneFlag) {
            this.allList = this.hotList;
        } else if (e.index === this.twoFlag) {
            this.allList = this.fineProductList;
        } else if (e.index === this.threeFlag) {
            this.allList = this.wisdomList;
        }
    },
    onInit() {
        this.latestList = [...this.hotList, ...this.fineProductList, ...this.wisdomList]
        this.allList = [...this.hotList, ...this.fineProductList, ...this.wisdomList]
    },
    submitColumn(e) {
        prompt.showToast({
            message: e.text + '   正在搜索中...'
        });
    },
    detailPage() {
        router.push({
            uri: "pages/shoppingDetailsPage/shoppingDetailsPage"
        });
    },
    stickFloat() {
    },
    clickBuy() {
        this.iconImageFlag = this.oneFlag;
        this.flag = this.oneFlag;
        this.ifFlag();
    },
    clickShoppingCart() {
        this.iconImageFlag = this.threeFlag;
        this.flag = this.threeFlag;
        this.ifFlag();
    },
    clickMy() {
        this.iconImageFlag = this.fourFlag;
        this.flag = this.fourFlag;
        this.ifFlag();
    },
    ifFlag() {
        if (this.iconImageFlag === this.oneFlag) {
            this.icon.buys = this.icon.buy;
            this.icon.messages = this.icon.message;
            this.icon.shoppingCarts = this.icon.shoppingCart;
            this.icon.mys = this.icon.my;
        } else if (this.iconImageFlag === this.twoFlag) {
            this.icon.buys = this.icon.home;
            this.icon.messages = this.icon.messageSelect;
            this.icon.shoppingCarts = this.icon.shoppingCart;
            this.icon.mys = this.icon.my;
        } else if (this.iconImageFlag === this.threeFlag) {
            this.icon.buys = this.icon.home;
            this.icon.messages = this.icon.message;
            this.icon.shoppingCarts = this.icon.shoppingCartSelect;
            this.icon.mys = this.icon.my;
        } else if (this.iconImageFlag === this.fourFlag) {
            this.icon.buys = this.icon.home;
            this.icon.messages = this.icon.message;
            this.icon.shoppingCarts = this.icon.shoppingCart;
            this.icon.mys = this.icon.mySelect;
        }
    },
    addShopping(e) {
        if (e.checked) {
            this.priceTotal += parseInt(e.target.attr.value);
        } else {
            this.priceTotal -= parseInt(e.target.attr.value);
        }
    },
    clickSettlement() {
        prompt.showToast({
            message: "正在结算......"
        });
    }
}