export default {
  data: {
    title: '电影',
    imgUrl: '',
    movieName: '',
    movieType: '电影类型：',
    type: '',
    actors: '主演',
    actorsImg: ['common/images/movies_image13.jpg',
      'common/images/movies_image12.jpg',
      'common/images/movies_image11.jpg',
      'common/images/movies_image10.jpg'],
    rating: '',
    storyTitle: '剧情简介',
    introduction: '',
    expressText: '更多',
    flag: true,
    stillsTitle: '剧照',
    isShow: false,
    maxLinesL: 3
  },
  onInit() {
    if (this.introduction.length > 60) {
      this.isShow = true;
    }
  },
  showContent() {
    if (this.flag) {
      this.maxLinesL = 10;
      this.expressText = '收起';
      this.flag = false;
    } else {
      this.maxLinesL = 3;
      this.expressText = '更多';
      this.flag = true;
    }
  }
};
