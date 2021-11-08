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

import app from '@system.app';
import prompt from '@system.prompt';
export default {
  data: {
    title: '',
    titleAppBar: '',
    picFrame: '',
    cropping: '',
    adjust: '',
    picFraImgSrc: '',
    conBotFirImgSrc: '',
    conBotSecImgSrc: '',
    conBotThrImgSrc: '',
    conBotFouImgSrc: '',
    brightness: '',
    brightnessImgSrc: '',
    brightnessColor: '',
    contrast: '',
    contrastImgSrc: '',
    contrastColor: '',
    saturation: '',
    saturationImgSrc: '',
    saturationColor: '',
    cropWidth: 0,
    cropHeight: 0,
    cropTop: 38,
    cropLeft: 0,
    cropBoxLeftOne: 0,
    cropBoxLeftTwo: 0,
    cropBoxLeftThr: 0,
    cropBoxLeftFou: 0,
    brightnessValue: 10,
    oldBrightnessValue: 10,
    contrastValue: 10,
    oldContrastValue: 10,
    saturationValue: 10,
    oldSaturationValue: 10,
    beginBright:true,
    beginContrast:true,
    beginSaturation:true,
    brightnessImgData: null,
    contrastImgData: null,
    saturationImgData: null,
    dWidth: 0,
    dHeight: 0,
    ratios: 0,
    sx: 0,
    sy: 0,
    dx: 0,
    dy: 0,
    offset: 30,
    showFlag1: true,
    showFlag2: false,
    showBrightness: true,
    showContrast: false,
    showSaturation: false,
    canvasWidth: 300,
    canvasHeight: 300,
    originalImageTop: 0,
    originalImageWidth: 300,
    originalImageHeight: 224,
    cropImgMaxWidth: 300,
    cropImgMaxHeight: 300,
    zero: 0,
    two: 2,
    sliderMaxValue: 10
  },
  onInit() {
    // 裁剪框宽和高分别为300、224(对应原图宽和高)
    this.cropWidth = this.originalImageWidth;
    this.cropHeight = this.originalImageHeight;
    console.log('onInit()裁剪框宽:' + this.cropWidth + '高:' + this.cropHeight);
    // 裁剪框左边距(等于画布左边距)
    this.cropLeft = this.offset;
    this.title = this.$t('strings.title');
    this.titleAppBar = this.$t('strings.titleAppBar');
    this.picFrame = this.$t('strings.picFrame');
    this.cropping = this.$t('strings.cropping');
    this.adjust = this.$t('strings.adjust');
    this.brightness = this.$t('strings.brightness');
    this.contrast = this.$t('strings.contrast');
    this.saturation = this.$t('strings.saturation');
    this.picFraImgSrc = this.$t('strings.picFraImgSrc');
    this.conBotFirImgSrc = 'common/images/image_frame_white_blue.svg';
    this.conBotSecImgSrc = this.$t('strings.conBotSecImgSrc');
    this.conBotThrImgSrc = this.$t('strings.conBotThrImgSrc');
    this.conBotFouImgSrc = this.$t('strings.conBotFouImgSrc');
    this.brightnessImgSrc = 'common/images/brightness_blue.svg';
    this.contrastImgSrc = this.$t('strings.contrastImgSrc');
    this.saturationImgSrc = this.$t('strings.saturationImgSrc');
  },
  // 画布canvas初始化图片
  onShow() {
    const img = new Image();
    img.src = 'common/images/image.jpg';
    const el = this.$element('canvasOne');
    const ctx = el.getContext('2d');
    // 清除画布内容
    ctx.clearRect(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    // 原图相对画布的上边距(画布高度减原图高度除2)
    this.originalImageTop = (this.canvasWidth - this.originalImageHeight) / this.two;
    // 绘制原始图片
    ctx.drawImage(img, this.zero, this.originalImageTop, this.originalImageWidth, this.originalImageHeight);
    // 保存图片(后续调节图片亮度、对比度和饱和度restore)
    this.brightnessImgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    this.contrastImgData = this.brightnessImgData;
    this.saturationImgData = this.brightnessImgData;
    ctx.save();
    // 裁剪框左边距(原图)
    this.cropBoxLeftOne = (this.originalImageWidth - this.originalImageWidth) / this.two + this.offset;
    // 裁剪框左边距(1:1)
    this.cropBoxLeftTwo = (this.originalImageWidth - this.originalImageHeight) / this.two + this.offset;
    // 裁剪框左边距(16:9)
    this.cropBoxLeftThr = (this.originalImageWidth - this.originalImageWidth) / this.two + this.offset;
    // 裁剪框左边距(9:16)
    this.cropBoxLeftFou = (this.originalImageWidth - this.originalImageHeight * 9 / 16) / this.two + this.offset;
    // 裁剪框上边距(原图;图片相对于画布)
    this.cropBoxTopOne = this.originalImageTop;
    // 裁剪框上边距(1:1;图片相对于画布)
    this.cropBoxTopTwo = this.originalImageTop;
    // 裁剪框上边距(16:9;图片相对于画布)
    this.cropBoxTopThr = (this.canvasHeight - this.originalImageWidth * 9 / 16) / this.two;
    // 裁剪框上边距(9:16;图片相对于画布)
    this.cropBoxTopFou = this.originalImageTop;
    // 绘制完成后图片宽高
    this.dWidth = this.originalImageWidth;
    this.dHeight = this.originalImageHeight;
    console.log('onShow()绘制完图片宽:' + this.dWidth + '高:' + this.dHeight);
  },
  // 裁剪页面
  showCropPage() {
    this.showFlag1 = true;
    this.showFlag2 = false;
    // 调节裁剪框位置。ratios:裁剪比例
    switch (this.ratios) {
      // 原图
      case 0:
        this.conBotFirImage();
        break;
      // 1:1
      case 1:
        this.conBotSecImage();
        break;
      // 16:9
      case 16 / 9:
        this.conBotThrImage();
        break;
      // 9:16
      case 9 / 16:
        this.conBotFouImage();
        break;
      default:
        break;
    }
  },
  // 调节页面
  showAdjustPage() {
    this.showFlag1 = false;
    this.showFlag2 = true;
    this.showBrightness = true;
    this.showContrast = false;
    this.showSaturation = false;
    this.brightnessColor = '#2788B9';
    this.contrastColor = '#ffffff';
    this.saturationColor = '#ffffff';
    switch (this.ratios) {
      case 0:
        break;
      case 1:
        this.cropOne();
        break;
      case 16 / 9:
        this.cropThr();
        break;
      case 9 / 16:
        this.cropFour();
        break;
      default:
        break;
    }
  },
  // 1:1比例裁剪
  cropOne() {
    const el = this.$element('canvasOne');
    const ctx = el.getContext('2d');
    console.log('1:1比例裁剪cropOne()裁剪框宽:' + this.cropWidth + '高:' + this.cropHeight);
    // 需要输出的区域的左上角x坐标、y坐标
    this.sx = (this.dWidth - this.cropWidth) / this.two;
    // 裁剪后图片的高度(最大值设置300)
    this.sy = (this.cropImgMaxHeight - this.cropHeight) / this.two;
    let imageData;
    // 图片高度为最大高度300(9:16裁剪后)
    if (this.dHeight === this.cropImgMaxHeight) {
      // 9:16后宽度按比例变大
      imageData = ctx.getImageData(this.sx, this.sy, this.cropWidth + (this.cropImgMaxWidth - this.originalImageHeight
      * 9 / 16) / this.two, this.cropWidth);
    } else {
      imageData = ctx.getImageData(this.sx, this.sy, this.cropWidth, this.cropWidth);
    }
    // 清除画布内容
    ctx.clearRect(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    // 放大
    ctx.scale(this.cropImgMaxWidth / this.cropWidth, this.cropImgMaxHeight / this.cropHeight);
    this.dx = -(this.cropImgMaxWidth - this.dWidth) / this.two;
    this.dy = this.zero;
    console.log('1:1比例裁剪cropOne()ImageData对象:' + imageData);
    // 绘制图片
    ctx.putImageData(imageData, this.dx, this.dy);
    // 缩小
    ctx.scale(this.cropWidth / this.cropImgMaxWidth, this.cropHeight / this.cropImgMaxHeight);
    // 保存图片
    this.beginBright = this.brightnessValue === 0 ? true : false
    this.beginContrast = this.contrastValue === 0 ? true : false
    this.beginSaturation = this.saturationValue === 0 ? true : false
    this.brightnessImgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    this.contrastImgData = this.brightnessImgData;
    this.saturationImgData = this.brightnessImgData;
    ctx.save();
    // 裁剪后裁剪框距离左边距离(9:16)
    this.cropBoxLeftFou = (this.cropImgMaxWidth - this.cropImgMaxHeight * 9 / 16) / this.two + this.offset;
    // 裁剪后裁剪框距离左边距离(16:9)
    this.cropBoxLeftThr = this.offset;
    // 裁剪后裁剪框距离左边距离(1:1)
    this.cropBoxLeftTwo = this.offset;
    // 裁剪后裁剪框距离左边距离(原图)
    this.cropBoxLeftOne = this.offset;
    // 裁剪后裁剪框距离上边距离(原图)
    this.cropBoxTopOne = (this.cropImgMaxHeight - this.cropImgMaxWidth) / this.two;
    // 裁剪后裁剪框距离上边距离(1:1)
    this.cropBoxTopTwo = (this.cropImgMaxHeight - this.cropImgMaxWidth) / this.two;
    // 裁剪后裁剪框距离上边距离(16:9)
    this.cropBoxTopThr = (this.cropImgMaxHeight - this.cropImgMaxWidth * 9 / 16) / this.two;
    // 裁剪后裁剪框距离上边距离(9:16)
    this.cropBoxTopFou = (this.cropImgMaxHeight - this.cropImgMaxWidth) / this.two;
    // 裁剪后图片宽高等于裁剪图片最大宽高
    this.dWidth = this.cropImgMaxWidth;
    this.dHeight = this.cropImgMaxHeight;
    console.log('1:1比例裁剪cropOne()后图片宽:' + this.dWidth + '高:' + this.dHeight);
  },
  // 16:9比例裁剪
  cropThr() {
    const el = this.$element('canvasOne');
    const ctx = el.getContext('2d');
    console.log('16:9比例裁剪cropThr()裁剪框宽:' + this.cropWidth + '高:' + this.cropHeight);
    // 需要输出的区域的左上角x坐标、y坐标
    this.sx = (this.cropImgMaxWidth - this.dWidth) / this.two;
    this.sy = (this.cropImgMaxHeight - this.cropHeight) / this.two;
    const imageData = ctx.getImageData(this.sx, this.sy, this.cropWidth, this.cropHeight);
    // 清除画布内容
    ctx.clearRect(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    if (this.dHeight === this.cropImgMaxHeight) {
      // 放大
      ctx.scale(this.cropImgMaxWidth / this.cropWidth, this.cropImgMaxWidth / this.cropWidth);
    }
    // 绘制时y轴偏移量
    this.dy = (this.cropImgMaxHeight - this.cropHeight * (this.cropImgMaxWidth / this.cropWidth)) /
    (this.two * (this.cropImgMaxWidth / this.cropWidth));
    console.log('16:9比例裁剪cropThr()ImageData对象:' + imageData);
    // 绘制图片
    ctx.putImageData(imageData, this.zero, this.dy);
    // 图片高度为最大高度300(9:16裁剪后)
    if (this.dHeight === this.cropImgMaxHeight) {
      ctx.scale(this.cropWidth / this.cropImgMaxWidth, this.cropWidth / this.cropImgMaxWidth);
    }
    // 保存图片
    this.beginBright = this.brightnessValue === 0 ? true : false
    this.beginContrast = this.contrastValue === 0 ? true : false
    this.beginSaturation = this.saturationValue === 0 ? true : false
    this.brightnessImgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    this.contrastImgData = this.brightnessImgData;
    this.saturationImgData = this.brightnessImgData;
    ctx.save();
    // 裁剪后裁剪框距离左边距离(9:16)
    this.cropBoxLeftFou = (this.cropImgMaxWidth - this.cropImgMaxWidth * 9 / 16 * 9 / 16) / this.two + this.offset;
    // 裁剪后裁剪框距离左边距离(16:9)
    this.cropBoxLeftThr = this.offset;
    // 裁剪后裁剪框距离左边距离(1:1)
    this.cropBoxLeftTwo = (this.cropImgMaxWidth - this.cropImgMaxWidth * 9 / 16 * 1 / 1) / this.two + this.offset;
    // 裁剪后裁剪框距离左边距离(原图)
    this.cropBoxLeftOne = this.offset;
    // 裁剪后裁剪框距离上边距离(原图)
    this.cropBoxTopOne = (this.cropImgMaxHeight - this.cropImgMaxWidth * 9 / 16) / this.two;
    // 裁剪后裁剪框距离上边距离(1:1)
    this.cropBoxTopTwo = (this.cropImgMaxHeight - this.cropImgMaxWidth * 9 / 16) / this.two;
    // 裁剪后裁剪框距离上边距离(16:9)
    this.cropBoxTopThr = (this.cropImgMaxHeight - this.cropImgMaxWidth * 9 / 16) / this.two;
    // 裁剪后裁剪框距离上边距离(9:16)
    this.cropBoxTopFou = (this.cropImgMaxHeight - this.cropImgMaxWidth * 9 / 16) / this.two;
    // 裁剪完图片宽高等于裁剪比例缩放后宽高
    this.dWidth = this.cropImgMaxWidth;
    this.dHeight = this.cropImgMaxWidth * 9 / 16;
    console.log('16:9比例裁剪cropThr()后图片宽:' + this.dWidth + '高:' + this.dHeight);
    // 裁剪框宽高等于裁剪完图片宽高
    this.cropWidth = this.dWidth;
    this.cropHeight = this.dHeight;
  },
  // 9:16比例裁剪
  cropFour() {
    const el = this.$element('canvasOne');
    const ctx = el.getContext('2d');
    console.log('9:16比例裁剪cropFour()裁剪框宽:' + this.cropWidth + '高:' + this.cropHeight);
    // 需要输出的区域的左上角x坐标、y坐标
    this.sx = (this.dWidth - this.cropWidth) / this.two;
    this.sy = (this.cropImgMaxHeight - this.cropHeight) / this.two;
    let imageData;
    // 以当前canvas指定区域内的像素创建ImageData对象
    if (this.dHeight === this.cropImgMaxHeight && this.dWidth !== this.cropImgMaxWidth) {
      imageData = ctx.getImageData(this.sx, this.sy, this.cropWidth + (this.cropImgMaxWidth -
      this.originalImageHeight * 9 / 16) / this.two, this.cropHeight);
    } else {
      imageData = ctx.getImageData(this.sx, this.sy, this.cropWidth, this.cropHeight);
    }
    // 删除指定区域内的绘制内容
    ctx.clearRect(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    if (this.dWidth === this.cropImgMaxWidth) {
      // 放大
      ctx.scale(this.cropImgMaxHeight / this.cropHeight, this.cropImgMaxHeight / this.cropHeight);
    }
    // 绘制时x轴偏移量
    if (this.dHeight === this.cropImgMaxHeight && this.dWidth !== this.cropImgMaxWidth) {
      // 9:16裁剪后继续9:16裁剪
      this.dx = this.zero;
    } else {
      this.dx = (this.cropImgMaxWidth - this.cropWidth * (this.cropImgMaxHeight / this.cropHeight)) /
      (this.two * (this.cropImgMaxHeight / this.cropHeight));
    }
    // 绘制时y轴偏移量
    this.dy = this.zero;
    console.log('9:16比例裁剪cropThr()ImageData对象:' + imageData);
    // 绘制图片
    ctx.putImageData(imageData, this.dx, this.dy);
    // 图片宽度为最大宽度300
    if (this.dWidth === this.cropImgMaxWidth) {
      ctx.scale(this.cropHeight / this.cropImgMaxHeight, this.cropHeight / this.cropImgMaxHeight);
    }
    // 保存图片
    this.beginBright = this.brightnessValue === 0 ? true : false
    this.beginContrast = this.contrastValue === 0 ? true : false
    this.beginSaturation = this.saturationValue === 0 ? true : false
    this.brightnessImgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    this.contrastImgData = this.brightnessImgData;
    this.saturationImgData = this.brightnessImgData;
    ctx.save();
    // 裁剪后裁剪框距离左边距离(9:16)
    this.cropBoxLeftFou = (this.cropImgMaxWidth - this.cropImgMaxHeight * 9 / 16) / this.two + this.offset;
    // 裁剪后裁剪框距离左边距离(16:9)
    this.cropBoxLeftThr = (this.cropImgMaxWidth - this.cropImgMaxHeight * 9 / 16) / this.two + this.offset;
    // 裁剪后裁剪框距离左边距离(1:1)
    this.cropBoxLeftTwo = (this.cropImgMaxWidth - this.cropImgMaxHeight * 9 / 16) / this.two + this.offset;
    // 裁剪后裁剪框距离左边距离(原图)
    this.cropBoxLeftOne = (this.cropImgMaxWidth - this.cropImgMaxHeight * 9 / 16) / this.two + this.offset;
    // 裁剪后裁剪框距离上边距离(原图)
    this.cropBoxTopOne = (this.cropImgMaxHeight - this.cropImgMaxHeight * 9 / 16 * 16 / 9) / this.two;
    // 裁剪后裁剪框距离上边距离(1:1)
    this.cropBoxTopTwo = (this.cropImgMaxHeight - this.cropImgMaxHeight * 9 / 16) / this.two;
    // 裁剪后裁剪框距离上边距离(16:9)
    this.cropBoxTopThr = (this.cropImgMaxHeight - this.cropImgMaxHeight * 9 / 16 * 9 / 16) / this.two;
    // 裁剪后裁剪框距离上边距离(9:16)
    this.cropBoxTopFou = (this.cropImgMaxHeight - this.cropImgMaxHeight * 9 / 16 * 16 / 9) / this.two;
    // 裁剪完图片宽高等于裁剪比例缩放后宽高
    this.dHeight = this.cropImgMaxHeight;
    this.dWidth = this.dHeight * 9 / 16;
    console.log('9:16比例裁剪cropFour()后图片宽:' + this.dWidth + '高:' + this.dHeight);
    // 裁剪框宽高等于裁剪完图片宽高
    this.cropWidth = this.dWidth;
    this.cropHeight = this.dHeight;
  },

  // 原图尺寸
  conBotFirImage() {
    this.conBotFirImgSrc = 'common/images/image_frame_white_blue.svg';
    this.conBotSecImgSrc = this.$t('strings.conBotSecImgSrc');
    this.conBotThrImgSrc = this.$t('strings.conBotThrImgSrc');
    this.conBotFouImgSrc = this.$t('strings.conBotFouImgSrc');
    // 裁剪框上边距
    this.cropTop = this.cropBoxTopOne;
    // 裁剪框左边距
    this.cropLeft = this.cropBoxLeftOne;
    // 裁剪框宽高为图片宽高
    this.cropWidth = this.dWidth;
    this.cropHeight = this.dHeight;
    console.log('原图调整裁剪框位置后宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.ratios = 0;
  },
  // 1:1
  conBotSecImage() {
    this.conBotFirImgSrc = this.$t('strings.conBotFirImgSrc');
    this.conBotSecImgSrc = 'common/images/ratios_1-1_blue.png';
    this.conBotThrImgSrc = this.$t('strings.conBotThrImgSrc');
    this.conBotFouImgSrc = this.$t('strings.conBotFouImgSrc');
    // 裁剪框左边距
    this.cropLeft = this.cropBoxLeftTwo;
    if (this.dWidth < this.dHeight) {
      // 裁剪框上边距
      this.cropTop = (this.dHeight - this.dWidth) / this.two;
      // 裁剪框宽高
      this.cropWidth = this.dWidth;
      this.cropHeight = this.dWidth;
    } else {
      // 裁剪框上边距
      this.cropTop = this.cropBoxTopTwo;
      // 裁剪框宽高
      this.cropWidth = this.dHeight;
      this.cropHeight = this.dHeight;
    }
    console.log('1:1调整裁剪框位置后宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.ratios = 1;
  },
  // 16:9
  conBotThrImage() {
    this.conBotFirImgSrc = this.$t('strings.conBotFirImgSrc');
    this.conBotSecImgSrc = this.$t('strings.conBotSecImgSrc');
    this.conBotThrImgSrc = 'common/images/ratios_16-9_blue.png';
    this.conBotFouImgSrc = this.$t('strings.conBotFouImgSrc');
    // 裁剪框上边距
    this.cropTop = this.cropBoxTopThr;
    // 裁剪框左边距
    this.cropLeft = this.cropBoxLeftThr;
    // 裁剪框宽高(裁剪框宽为图片宽)
    this.cropWidth = this.dWidth;
    this.cropHeight = this.cropWidth * 9 / 16;
    console.log('16:9调整裁剪框位置后宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.ratios = 16 / 9;
  },
  // 9:16
  conBotFouImage() {
    this.conBotFirImgSrc = this.$t('strings.conBotFirImgSrc');
    this.conBotSecImgSrc = this.$t('strings.conBotSecImgSrc');
    this.conBotThrImgSrc = this.$t('strings.conBotThrImgSrc');
    this.conBotFouImgSrc = 'common/images/ratios_9-16_blue.png';
    // 裁剪框上边距
    this.cropTop = this.cropBoxTopFou;
    // 裁剪框左边距
    this.cropLeft = this.cropBoxLeftFou;
    // 裁剪框宽高(裁剪框高为图片高)
    this.cropHeight = this.dHeight;
    this.cropWidth = this.dHeight * 9 / 16;
    console.log('9:16调整裁剪框位置后宽:' + this.cropWidth + '高:' + this.cropHeight);
    this.ratios = 9 / 16;
  },
  // 亮度
  brightnessAdj() {
    this.brightnessColor = '#2788B9';
    this.contrastColor = '#ffffff';
    this.saturationColor = '#ffffff';
    this.brightnessImgSrc = 'common/images/brightness_blue.svg';
    this.contrastImgSrc = this.$t('strings.contrastImgSrc');
    this.saturationImgSrc = this.$t('strings.saturationImgSrc');
    this.showBrightness = true;
    this.showContrast = false;
    this.showSaturation = false;
    // 还原成裁剪后的图片
    const test = this.$element('canvasOne');
    const ctx = test.getContext('2d');
    ctx.restore();
    ctx.putImageData(this.brightnessImgData, this.zero, this.zero);
    // 将slider值设置成10(最大为10)
    this.brightnessValue = this.sliderMaxValue;
  },
  // 对比度
  contrastAdj() {
    this.brightnessColor = '#ffffff';
    this.contrastColor = '#2788B9';
    this.saturationColor = '#ffffff';
    this.brightnessImgSrc = this.$t('strings.brightnessImgSrc');
    this.contrastImgSrc = 'common/images/contrast_blue.svg';
    this.saturationImgSrc = this.$t('strings.saturationImgSrc');
    this.showBrightness = false;
    this.showContrast = true;
    this.showSaturation = false;
    // 还原成裁剪后的图片
    const test = this.$element('canvasOne');
    const ctx = test.getContext('2d');
    ctx.restore();
    ctx.putImageData(this.contrastImgData, this.zero, this.zero);
    // 将slider值设置成10(最大为10)
    this.contrastValue = this.sliderMaxValue;
  },
  // 饱和度
  saturationAdj() {
    this.brightnessColor = '#ffffff';
    this.contrastColor = '#ffffff';
    this.saturationColor = '#2788B9';
    this.brightnessImgSrc = this.$t('strings.brightnessImgSrc');
    this.contrastImgSrc = this.$t('strings.contrastImgSrc');
    this.saturationImgSrc = 'common/images/saturation_blue.svg';
    this.showBrightness = false;
    this.showContrast = false;
    this.showSaturation = true;
    // 还原成裁剪后的图片
    const test = this.$element('canvasOne');
    const ctx = test.getContext('2d');
    ctx.restore();
    ctx.putImageData(this.saturationImgData, this.zero, this.zero);
    // 将slider值设置成10(最大为10)
    this.saturationValue = this.sliderMaxValue;
  },
  // 亮度调节
  setBrightnessValue(e) {
    if (e.mode === 'start') {
      this.oldBrightnessValue = e.value;
    } else if (e.mode === 'end') {
      this.brightnessValue = e.value;
      // 亮度调节值最大时，恢复裁剪后的图片
      if (e.value === this.sliderMaxValue && this.beginBright) {
        const test = this.$element('canvasOne');
        const ctx = test.getContext('2d');
        // 恢复图片
        ctx.restore();
        ctx.putImageData(this.brightnessImgData, this.zero, this.zero);
      } else {
        // 亮度调节系数（新亮度值比原来亮度值）
        const adjustValue = e.value / this.oldBrightnessValue;
        console.log('adjustValue:: ' + adjustValue)
        this.adjustBrightness(adjustValue);
        this.oldBrightnessValue = e.value;
      }
    }
  },
  adjustBrightness(value) {
    const test = this.$element('canvasOne');
    const ctx = test.getContext('2d');
    // 获取当前ImageData对象
    const imgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    // 绘制返回的新ImageData对象
    ctx.putImageData(this.changeBrightness(imgData, value), this.zero, this.zero);
  },
  // 调节图片亮度
  changeBrightness(imgdata, value) {
    const data = imgdata.data;
    // 循环遍历图片上的像素点，通过rgb2hsv和hsv2rgb计算公式，算出新的hsv和rgb
    for (let i = 0; i < data.length; i += 4) {
      const hsv = this.rgb2hsv([data[i], data[i + 1], data[i + 2]]);
      // 像素值乘以增强系数
      hsv[2] *= value;
      const rgb = this.hsv2rgb([...hsv]);
      data[i] = rgb[0];
      data[i + 1] = rgb[1];
      data[i + 2] = rgb[2];
    }
    return imgdata;
  },
  // 对比度调节
  setContrastValue(e) {
    if (e.mode === 'start') {
      this.oldContrastValue = e.value;
    } else if (e.mode === 'end') {
      this.contrastValue = e.value;
      if (e.value === this.sliderMaxValue && this.beginContrast) {
        const test = this.$element('canvasOne');
        const ctx = test.getContext('2d');
        ctx.restore();
        ctx.putImageData(this.contrastImgData, this.zero, this.zero);
      } else {
        const adjustValue = e.value / this.oldContrastValue;
        this.adjustContrast(adjustValue);
        this.oldContrastValue = e.value;
      }
    }
  },
  adjustContrast(value) {
    const test = this.$element('canvasOne');
    const ctx = test.getContext('2d');
    const imgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    ctx.putImageData(this.changeContrast(imgData, value), this.zero, this.zero);
  },
  changeContrast(imgdata, value) {
    const data = imgdata.data;
    for (let i = 0; i < data.length; i += 4) {
      const hsv = this.rgb2hsv([data[i], data[i + 1], data[i + 2]]);
      hsv[0] *= value;
      const rgb = this.hsv2rgb([...hsv]);
      data[i] = rgb[0];
      data[i + 1] = rgb[1];
      data[i + 2] = rgb[2];
    }
    return imgdata;
  },
  // 饱和度调节
  setSaturationValue(e) {
    if (e.mode === 'start') {
      this.oldSaturationValue = e.value;
    } else if (e.mode === 'end') {
      this.saturationValue = e.value;
      if (e.value === this.sliderMaxValue && this.beginSaturation) {
        const test = this.$element('canvasOne');
        const ctx = test.getContext('2d');
        ctx.restore();
        ctx.putImageData(this.saturationImgData, this.zero, this.zero);
      } else {
        const adjustValue = e.value / this.oldSaturationValue;
        this.adjustSaturation(adjustValue);
        this.oldSaturationValue = e.value;
      }
    }
  },
  adjustSaturation(value) {
    const test = this.$element('canvasOne');
    const ctx = test.getContext('2d');

    const imgData = ctx.getImageData(this.zero, this.zero, this.canvasWidth, this.canvasHeight);
    ctx.putImageData(this.changeSaturation(imgData, value), this.zero, this.zero);
  },
  changeSaturation(imgdata, value) {
    const data = imgdata.data;
    for (let i = 0; i < data.length; i += 4) {
      const hsv = this.rgb2hsv([data[i], data[i + 1], data[i + 2]]);
      hsv[1] *= value;
      const rgb = this.hsv2rgb([...hsv]);
      data[i] = rgb[0];
      data[i + 1] = rgb[1];
      data[i + 2] = rgb[2];
    }
    return imgdata;
  },
  // RGB转HSV
  rgb2hsv(arr) {
    let rr;
    let gg;
    let bb;
    const r = arr[0] / 255;
    const g = arr[1] / 255;
    const b = arr[2] / 255;
    let h;
    let s;
    const v = Math.max(r, g, b);
    const diff = v - Math.min(r, g, b);
    const diffc = function(c) {
      return (v - c) / 6 / diff + 1 / 2;
    };

    if (diff === 0) {
      h = s = 0;
    } else {
      s = diff / v;
      rr = diffc(r);
      gg = diffc(g);
      bb = diffc(b);

      if (r === v) {
        h = bb - gg;
      } else if (g === v) {
        h = 1 / 3 + rr - bb;
      } else if (b === v) {
        h = 2 / 3 + gg - rr;
      }
      if (h < 0) {
        h += 1;
      } else if (h > 1) {
        h -= 1;
      }
    }
    return [Math.round(h * 360), Math.round(s * 100), Math.round(v * 100)];
  },
  // HSV转RGB
  hsv2rgb(hsv) {
    let _l = hsv[0];
    let _m = hsv[1];
    let _n = hsv[2];
    let newR;
    let newG;
    let newB;
    if (_m === 0) {
      _l = _m = _n = Math.round(255 * _n / 100);
      newR = _l;
      newG = _m;
      newB = _n;
    } else {
      _m = _m / 100;
      _n = _n / 100;
      const p = Math.floor(_l / 60) % 6;
      const f = _l / 60 - p;
      const a = _n * (1 - _m);
      const b = _n * (1 - _m * f);
      const c = _n * (1 - _m * (1 - f));
      switch (p) {
        case 0:
          newR = _n; newG = c; newB = a;
          break;
        case 1:
          newR = b; newG = _n; newB = a;
          break;
        case 2:
          newR = a; newG = _n; newB = c;
          break;
        case 3:
          newR = a; newG = b; newB = _n;
          break;
        case 4:
          newR = c; newG = a; newB = _n;
          break;
        case 5:
          newR = _n; newG = a; newB = b;
          break;
      }
      newR = Math.round(255 * newR);
      newG = Math.round(255 * newG);
      newB = Math.round(255 * newB);
    }
    return [newR, newG, newB];
  },
  // 回退
  redo() {
    prompt.showToast({
      message: 'Please implement your redo function'
    });
  },
  // 前进
  undo() {
    prompt.showToast({
      message: 'Please implement your undo function'
    });
  },
  // 保存
  save() {
    prompt.showToast({
      message: 'Please implement your save function'
    });
  },
  // 退出弹窗
  showDialog(e) {
    this.$element('simpleDialog').show();
  },
  cancelSchedule(e) {
    this.$element('simpleDialog').close();
  },
  setSchedule(e) {
    this.$element('simpleDialog').close();
    app.terminate();
  }
};
