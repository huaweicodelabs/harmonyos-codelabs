export default {
  data: {
    imgUrl: '/common/images/windmill.png',
    animationDuration: '5000ms',
    animationDurationNum: 5000,
    speed: 50,
    minSpeed: 0,
    maxSpeed: 100,
    imageSize: 1,
    size: 50,
    minSize: 0,
    maxSize: 100
  },
  // 改变转速
  changeValue(e) {
    if (e.mode === 'end' || e.mode === 'click') {
      this.speed = e.value;
      this.animationDurationNum = 10000 - e.value * 95;
      this.animationDuration = this.animationDurationNum + 'ms';
    }
  },
  // 改变大小
  changeSize(e) {
    if (e.mode === 'end' || e.mode === 'click') {
      this.size = e.value;
      this.imageSize = this.size / 50 < 0.1 ? 0.1 : this.size / 50;
    }
  }
};
