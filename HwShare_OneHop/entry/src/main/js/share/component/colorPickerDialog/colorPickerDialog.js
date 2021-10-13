/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
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
export default {
  props: {
    showObj: {
      default: false
    },
    title: {
      default: '单色呼吸'
    },
    dialogWidth: {
      default: '336px'
    },
    dialogHeight: {
      default: '386px'
    },
    canvasWidth: {
      default: '240'
    },
    canvasHeight: {
      default: '240'
    },
    pointWidth: {
      default: '34'
    },
    pointHeight: {
      default: '34'
    },
    pointLeft: {
      default: '50%'
    },
    pointTop: {
      default: '50%'
    },
    cancelButtonText: {
      default: '取消'
    },
    showCancelButton: {
      default: true
    },
    showConfirmButton: {
      default: true
    },
    confirmButtonText: {
      default: '确定'
    }
  },
  data: {
    oTimer: null
  },
  onInit() {
    clearTimeout(this.oTimer);
    const This = this;
    this.oTimer = setTimeout(function() {
      This.drawGradient();
    }, 500);
  },
  onPageShow() {

  },
  // 绘图1
  drawGradient() {
    const iSectors = 360;
    const width = 240;
    const height = 240;
    const iSectorAngle = 360 / iSectors / 180 * Math.PI;
    const canvas = this.$refs.canvas;
    const ctx = canvas.getContext('2d');
    // 清除画布内容
    ctx.clearRect(0, 0, width, height);
    // 存储当前环境
    ctx.save();
    // 画布（0,0）初始点映射到中间圆点
    ctx.translate(width / 2, height / 2);

    for (let i = 0; i < 360; i++) {
      const startAngle = 0;
      const endAngle = startAngle + iSectorAngle;
      const radius = width / 2 - 1;
      // iSectorAngle * i  变量弧度的计算
      const grd = ctx.createLinearGradient(
        0,
        0,
        width / 4,
        height / 4
      );
      grd.addColorStop(0, `rgb(${this.hslToRgb(i / 360, 0, 1)})`);
      grd.addColorStop(1, `rgb(${this.hslToRgb(i / 360, 1, 0.5)})`);
      ctx.beginPath();
      ctx.moveTo(0, 0);
      // False = 顺时针，true = 逆时针
      ctx.arc(0, 0, radius, startAngle, endAngle, false);

      ctx.closePath();
      // 设置或返回用于笔触的颜色、渐变或模式。（边）
      ctx.strokeStyle = grd;

      // 绘制已定义的路径。
      ctx.stroke();
      // 设置或返回用于填充绘画的颜色、渐变或模式。（内容）
      ctx.fillStyle = grd;
      // 填充当前绘图（路径）。
      ctx.fill();

      /*
       *都是在同一地方绘出的点，需要通过旋转来调整位置。
       *重复360，调整360
       */
      ctx.rotate(-iSectorAngle);
    }
    // 返回之前保存过的路径状态和属性。
    ctx.restore();
    this.dragTouchstart();
  },
  // 将rgb转换为hsl对象()
  rgbToHslObj(r, g, b) {
    r /= 255;
    g /= 255;
    b /= 255;
    const max = Math.max(r, g, b);
    const min = Math.min(r, g, b);
    const diff = max - min;
    const twoValue = max + min;
    const obj = {h: 0, s: 0, l: 0};
    if (max === min) {
      obj.h = 0;
    } else if (max === r && g >= b) {
      obj.h = 60 * (g - b) / diff;
    } else if (max === r && g < b) {
      obj.h = 60 * (g - b) / diff + 360;
    } else if (max === g) {
      obj.h = 60 * (b - r) / diff + 120;
    } else if (max === b) {
      obj.h = 60 * (r - g) / diff + 240;
    }
    obj.l = twoValue / 2;
    if (obj.l === 0 || max === min) {
      obj.s = 0;
    } else if (0 < obj.l && obj.l <= 0.5) {
      obj.s = diff / twoValue;
    } else {
      obj.s = diff / (2 - twoValue);
    }
    obj.h = Math.round(obj.h);
    return obj;
  },

  /*
   *HSL颜色值转换为RGB.
   *换算公式改编自 http://en.wikipedia.org/wiki/HSL_color_space.
   *h, s, 和 l 设定在 [0, 1] 之间
   *返回的 r, g, 和 b 在 [0, 255]之间
   *
   *@param Number h 色相
   *@param Number s 饱和度
   *@param Number l 亮度
   *@return Array RGB色值数值
   */
  hslToRgb(hue, saturation, luminance) {
    let red, green, blue;

    if (saturation === 0) {
      red = green = blue = luminance; // achromatic
    } else {
      const hue2rgb = function hue2rgb(pp, qq, tt) {
        if (tt < 0) {
          tt += 1;
        }
        if (tt > 1) {
          tt -= 1;
        }
        if (tt< 1 / 6) {
          return pp + (qq - pp) * 6 * tt;
        }
        if (tt < 1 / 2) {
          return qq;
        }
        if (tt < 2 / 3) {
          return pp+ (qq - pp) * (2 / 3 - tt) * 6;
        }
        return pp;
      };

      const q = luminance < 0.5 ? luminance * (1 + saturation) : luminance + saturation - luminance * saturation;
      const p = 2 * luminance - q;
      red = hue2rgb(p, q, hue + 1 / 3);
      green = hue2rgb(p, q, hue);
      blue = hue2rgb(p, q, hue - 1 / 3);
    }
    // console.log([Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)])
    return [Math.round(red * 255), Math.round(green * 255), Math.round(blue * 255)];
  },
  moveEvent(e,width){
    const _this = this;
    const movePos = {
      x: e.changedTouches[0].clientX,
      y: e.changedTouches[0].clientY
    };
    const arr = _this.getOffset();
    const pointPos = {
      x: movePos.x - arr[0],
      y: movePos.y - arr[1]
    };
    _this.limitArea(pointPos, width);
  },
  // 色板点击事件
  dragTouchstart() {
    const _this = this;
    const canvas = this.$refs.canvas;
    const point = this.$refs.point;
    const width = canvas.width;
    // canvas本身点击监听
    canvas.addEventListener('click', function (e) {
      const ePos = {
        x: e.offsetX || e.layerX,
        y: e.offsetY || e.layerY
      };
      _this.limitArea(ePos, width);
    });
    // 圆圈本身移动监听
    point.addEventListener('touchmove', function (e) {
      _this.moveEvent(e, width);
    });
    // 点击canvas移动事件
    canvas.addEventListener('touchmove', function (e) {
      _this.moveEvent(e, width);
    });
  },
  // 在canvas拖拽移动
  getCanvasTouchMove(e) {
    const local = e.touches[0];
    this.pointLeft = local.localX + 'px';
    this.pointTop = local.localY + 'px';
  },
  // 点击canvas移动
  getCanvasTouchEnd(e) {
    const local = e.touches[0];
    this.pointLeft = local.localX + 'px';
    this.pointTop = local.localY + 'px';
  },
  // 点击canvas移动
  getPointTouchMove(e) {
    const local = e.touches[0];
    this.pointLeft = local.localX + 'px';
    this.pointTop = local.localY + 'px';
  },
  // 获取rgba颜色
  getRgbaAtPoint(pos) {
    const canvas = this.$refs.canvas;
    const ctx = canvas.getContext('2d');
    const imgData = ctx.getImageData(0, 0, canvas.width, canvas.height);
    const data = imgData.data;

    /*
     *getImageData数组的数量是canvas的宽度 * 高度 * 4 ，因为图片是rgba颜色组成的
     *因为有rgba四个数值所以要对应*4，要获取四个数值对应一个点
     *比如原先是x(1)对应一个y(1234)数组，但现在y数组对应一个点需要有对应4个参数(rgba),所以y就变成了(1234....16)
     *现在x从y数组里需要获取到一个点，就需要*4，因为y数组4个参数代表一个点。
     *索引值获取方式：纵坐标*图像宽度+横坐标 canvas的颜色点从(0,0)到（240,240）采用线性分布的方式
     */
    const dataIndex = (pos.y * imgData.width + pos.x) * 4;
    const colorArr = [
      data[dataIndex],
      data[dataIndex + 1],
      data[dataIndex + 2],
      (data[dataIndex + 3] / 255).toFixed(2)
    ];
    return colorArr;
  },
  // RGB16进制
  showRGB(str) {
    let hexcode = '#';
    for (let i = 0; i < 3; i++) {
      if (str[i] < 16) {
        str[i] = '0' + str[i].toString(16);
      } else {
        str[i] = str[i].toString(16);
      }
      hexcode += str[i];
    }
    return hexcode.toUpperCase();
  },
  // 范围限定与颜色渲染
  limitArea(posValue, widthValue) {
    const x = posValue.x - widthValue / 2;
    const y = posValue.y - widthValue / 2;
    const r = widthValue / 2 - 1;
    if (x * x + y * y < r * r) {
      const rgbaStr = this.getRgbaAtPoint(posValue);
      const colorStr = 'rgba(' + rgbaStr + ')';
      // 显示背景色
      this.$refs.point.style.left = posValue.x + 'px';
      this.$refs.point.style.top = posValue.y + 'px';
      this.$refs.point.style.backgroundColor = colorStr;
      this.$refs.point.style.boxShadow =
            '0px 0.1388rem 0.1388rem rgb (0 0 0 / 20%)';
      this.$refs.point.style.border = '2px solid #fff';
      this.colorNum = this.showRGB(rgbaStr);
    } else {
    }
  },
  getOffset() {
    const conName = this.$refs.container;
    let conTop = this.$refs.container.offsetTop;
    let conLeft = this.$refs.container.offsetLeft;
    let conObj = conName.offsetParent;
    while (conObj !== null) {
      conTop += conObj.offsetTop;
      conLeft += conObj.offsetLeft;
      conObj = conObj.offsetParent;
    }
    const realNum = [conLeft, conTop];
    return realNum;
  },
  colorPickerDialogHide() {
  },
  submitColor() {
    this.$emit('submitColor', {'colorValue': 123});
  }

};
