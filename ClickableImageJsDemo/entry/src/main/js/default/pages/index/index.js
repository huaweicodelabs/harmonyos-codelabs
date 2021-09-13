export default {
  data: {
    imageNormal: {
      classType: 'main-img-unTouch',
      src: '/common/images/sky_blue.png',
      title: '点击阴影'
    },
    imageSelect: {
      src: '/common/images/hook.png',
      title: '点击切换状态',
      hook: true
    },
    frameContainerPhone: {
      frames: [
        {src: '/common/images/frames/phone/phone_0.png'},
        {src: '/common/images/frames/phone/phone_1.png'},
        {src: '/common/images/frames/phone/phone_2.png'},
        {src: '/common/images/frames/phone/phone_3.png'},
        {src: '/common/images/frames/phone/phone_4.png'},
        {src: '/common/images/frames/phone/phone_5.png'},
        {src: '/common/images/frames/phone/phone_6.png'},
        {src: '/common/images/frames/phone/phone_7.png'},
        {src: '/common/images/frames/phone/phone_8.png'},
        {src: '/common/images/frames/phone/phone_9.png'},
        {src: '/common/images/frames/phone/phone_10.png'},
        {src: '/common/images/frames/phone/phone_11.png'},
        {src: '/common/images/frames/phone/phone_12.png'},
        {src: '/common/images/frames/phone/phone_13.png'},
        {src: '/common/images/frames/phone/phone_14.png'},
        {src: '/common/images/frames/phone/phone_15.png'},
        {src: '/common/images/frames/phone/phone_16.png'},
        {src: '/common/images/frames/phone/phone_17.png'},
        {src: '/common/images/frames/phone/phone_18.png'},
        {src: '/common/images/frames/phone/phone_19.png'},
        {src: '/common/images/frames/phone/phone_20.png'},
        {src: '/common/images/frames/phone/phone_21.png'},
        {src: '/common/images/frames/phone/phone_22.png'},
        {src: '/common/images/frames/phone/phone_23.png'},
        {src: '/common/images/frames/phone/phone_24.png'},
        {src: '/common/images/frames/phone/phone_25.png'},
        {src: '/common/images/frames/phone/phone_26.png'},
        {src: '/common/images/frames/phone/phone_27.png'},
        {src: '/common/images/frames/phone/phone_28.png'},
        {src: '/common/images/frames/phone/phone_29.png'},
        {src: '/common/images/frames/phone/phone_30.png'},
        {src: '/common/images/frames/phone/phone_31.png'},
        {src: '/common/images/frames/phone/phone_32.png'},
        {src: '/common/images/frames/phone/phone_33.png'},
        {src: '/common/images/frames/phone/phone_34.png'},
        {src: '/common/images/frames/phone/phone_35.png'}
      ],
      title: '点击动画效果',
      durationTime: 3600
    },
    frameContainerState: {
      frames: [],
      title: '点击切换状态动效',
      durationTime: 0,
      flag: true
    },
    durationTimeArray: [1400, 1400],
    back: [
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_0.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_1.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_2.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_3.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_4.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_5.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_6.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_7.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_8.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_9.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_10.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_11.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_12.png'},
      {src: '/common/images/frames/arrowheadBack/arrowhead_back_13.png'}
    ],
    collapse: [
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_0.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_1.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_2.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_3.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_4.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_5.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_6.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_7.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_8.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_9.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_10.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_11.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_12.png'},
      {src: '/common/images/frames/arrowheadCollapse/arrowhead_collapse_13.png'}
    ]
  },
  // 初始化
  onInit() {
    this.frameContainerState.frames = this.back;
    this.frameContainerState.durationTime = 0;
    this.frameContainerPhone.durationTime = 0;
  },
  // 触碰阴影方法
  changeHookState() {
    if (this.imageSelect.hook) {
      this.imageSelect.src = '/common/images/fork.png';
      this.imageSelect.hook = false;
    } else {
      this.imageSelect.src = '/common/images/hook.png';
      this.imageSelect.hook = true;
    }
  },
  // 点击切换状态
  changeShadow(flag) {
    if (flag) {
      this.imageNormal.classType = 'main-img-touch';
    } else {
      this.imageNormal.classType = 'main-img-unTouch';
    }
  },
  // 点击动画效果方法
  handleStartFrameContainerPhone() {
    this.frameContainerPhone.durationTime = 3600;
    this.$refs.frameContainerPhone.start();
  },
  // 点击切换状态动效方法
  handleStartFrameContainerState() {
    if (this.frameContainerState.flag) {
      this.frameContainerState.frames = this.collapse;
      this.frameContainerState.durationTime = this.durationTimeArray[0];
      this.$refs.frameContainerState.start();
      this.frameContainerState.flag = false;
      this.$refs.frameContainerState.stop();
    } else {
      this.frameContainerState.frames = this.back;
      this.frameContainerState.durationTime = this.durationTimeArray[1];
      this.$refs.frameContainerState.start();
      this.frameContainerState.flag = true;
      this.$refs.frameContainerState.stop();
    }
  }
};
