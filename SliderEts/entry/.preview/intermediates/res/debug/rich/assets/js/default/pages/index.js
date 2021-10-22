/******/ (() => { // webpackBootstrap
var __webpack_exports__ = {};
/*!*****************************************************************************************************!*\
  !*** ../../../../../Code/HarmonyPhoneDemo/etsDemo/entry/src/main/ets/default/pages/index.ets?entry ***!
  \*****************************************************************************************************/
// @ts-nocheck
// Slider Demo
class SliderTest extends View {
    constructor(compilerAssignedUniqueChildId, parent, params) {
        super(compilerAssignedUniqueChildId, parent);
        this.__value = new ObservedPropertySimple(40, this, "value");
        this.__speed = new ObservedPropertySimple(50, this, "speed");
        this.__imageSize = new ObservedPropertySimple(1, this, "imageSize");
        this.__angle = new ObservedPropertySimple(0, this, "angle");
        this.__setInt = new ObservedPropertySimple(0, this, "setInt");
        this.updateWithValueParams(params);
    }
    updateWithValueParams(params) {
        if (params.value !== undefined) {
            this.value = params.value;
        }
        if (params.speed !== undefined) {
            this.speed = params.speed;
        }
        if (params.imageSize !== undefined) {
            this.imageSize = params.imageSize;
        }
        if (params.angle !== undefined) {
            this.angle = params.angle;
        }
        if (params.setInt !== undefined) {
            this.setInt = params.setInt;
        }
    }
    aboutToBeDeleted() {
        this.__value.aboutToBeDeleted();
        this.__speed.aboutToBeDeleted();
        this.__imageSize.aboutToBeDeleted();
        this.__angle.aboutToBeDeleted();
        this.__setInt.aboutToBeDeleted();
        SubscriberManager.Get().delete(this.id());
    }
    get value() {
        return this.__value.get();
    }
    set value(newValue) {
        this.__value.set(newValue);
    }
    get speed() {
        return this.__speed.get();
    }
    set speed(newValue) {
        this.__speed.set(newValue);
    }
    get imageSize() {
        return this.__imageSize.get();
    }
    set imageSize(newValue) {
        this.__imageSize.set(newValue);
    }
    get angle() {
        return this.__angle.get();
    }
    set angle(newValue) {
        this.__angle.set(newValue);
    }
    get setInt() {
        return this.__setInt.get();
    }
    set setInt(newValue) {
        this.__setInt.set(newValue);
    }
    DescribeText(text, speed) {
        Stack.create();
        Stack.debugLine("pages/index.ets(13:4)");
        Text.create(text + speed);
        Text.debugLine("pages/index.ets(14:6)");
        Text.margin({ top: 30 });
        Text.fontSize(20);
        Text.fontWeight(FontWeight.Bold);
        Text.pop();
        Stack.pop();
    }
    render() {
        Column.create({ justifyContent: FlexAlign.Start, alignItems: ItemAlign.Center });
        Column.debugLine("pages/index.ets(22:5)");
        Image.create($rawfile('windmill.png'));
        Image.debugLine("pages/index.ets(23:7)");
        Image.objectFit(ImageFit.Contain);
        Image.height(150);
        Image.width(150);
        Image.margin({ top: 50, bottom: 150, right: 16 });
        Image.rotate({ x: 0, y: 0, z: 1, angle: this.angle });
        Image.scale({ x: this.imageSize, y: this.imageSize });
        this.DescribeText('速度：', this.speed);
        Slider.create({ value: this.speed, min: 30, max: 200, style: SliderStyle.OUTSET });
        Slider.debugLine("pages/index.ets(33:7)");
        Slider.showTips(true);
        Slider.blockColor("#d5e4ef");
        Slider.onChange((value, mode) => {
            this.speed = value;
            clearInterval(this.setInt);
            this.speedChange();
        });
        this.DescribeText('缩放比例：', this.imageSize);
        Slider.create({ value: this.imageSize, min: 0.5, max: 2.5, style: SliderStyle.OUTSET });
        Slider.debugLine("pages/index.ets(45:7)");
        Slider.showTips(true);
        Slider.blockColor("#d5e4ef");
        Slider.onChange((value, mode) => {
            this.imageSize = value;
        });
        Column.pop();
    }
    speedChange() {
        var that = this;
        this.setInt = setInterval(function () {
            console.log(that.angle + '');
            that.angle += 10;
        }, 210 - this.speed);
    }
    onPageShow() {
        this.speedChange();
    }
}
loadDocument(new SliderTest("1", undefined, {}));

/******/ })()
;
//# sourceMappingURL=index.js.map