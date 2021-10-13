import router from '@system.router';

export default {
  data: {
    title: '热门电影排行榜',
    titleDesc: '根据电影实时热度得出的综合排名',
    movieList: [
      {
        'sort': '1',
        'title': '电影1',
        'type': '剧情',
        'imgUrl': '/common/images/movies_image3.jpg',
        'commentCount': '2319996',
        'rating': '9.7',
        'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
      },
      {
        'sort': '2',
        'title': '电影2',
        'type': '剧情/爱情',
        'imgUrl': '/common/images/movies_image5.jpg',
        'commentCount': '1723017',
        'rating': '9.6',
        'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
      },
      {
        'sort': '3',
        'title': '电影3',
        'type': '剧情',
        'imgUrl': '/common/images/movies_image6.jpg',
        'commentCount': '358932',
        'rating': '9.6',
        'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
      },
      {
        'sort': '4',
        'title': '电影4',
        'type': '剧情/爱情',
        'imgUrl': '/common/images/movies_image7.jpg',
        'commentCount': '1745644',
        'rating': '9.6',
        'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
      },
      {
        'sort': '5',
        'title': '电影5',
        'type': '剧情/喜剧',
        'imgUrl': '/common/images/movies_image8.jpg',
        'commentCount': '1076203',
        'rating': '9.5',
        'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
      },
      {
        'sort': '6',
        'title': '电影6',
        'type': '剧情/喜剧',
        'imgUrl': '/common/images/movies_image9.jpg',
        'commentCount': '10762011',
        'rating': '9.5',
        'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
      },
      {
        'sort': '7',
        'title': '电影7',
        'type': '剧情/喜剧',
        'imgUrl': '/common/images/movies_image10.jpg',
        'commentCount': '1076277',
        'rating': '9.5',
        'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
      },
      {
        'sort': '8',
        'title': '电影8',
        'type': '剧情/喜剧',
        'imgUrl': '/common/images/movies_image11.jpg',
        'commentCount': '1076203',
        'rating': '9.5',
        'introduction': '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介' +
        '剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介剧情简介。'
      }
    ],
    'likeText': '想看'
  },
  onInit() {

  },
  jump2Detail(e) {
    const movieInfo = this.movieList[e - 1];
    router.push({
      uri: 'pages/index/detail',
      params: {
        'sort': movieInfo.sort,
        'movieName': movieInfo.title,
        'type': movieInfo.type,
        'imgUrl': movieInfo.imgUrl,
        'rating': movieInfo.rating,
        'introduction': movieInfo.introduction
      }
    });
  }
};
