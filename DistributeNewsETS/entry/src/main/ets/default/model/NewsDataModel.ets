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

import {NewsData} from './NewsData'

/**
 * 定义新闻数据源
 */
const NewsComposition: any[] = [
  {
    "newsId": "1",
    "title": "Best Enterprise Wi-Fi Network Award of the Wireless Broadband Alliance 2020",
    "newsType": "Health",
    "imgUrl": $rawfile('news_image1.jpg'),
    "reads": "54",
    "likes": "81",
    "content": "Recently, at the Wireless Broadband Alliance (WBA), an international industry organization, Huawei's AirEngine Wi-Fi 6 Solution Helps Factory Digital Transformation WBA 2020 Wi-Fi Industry Best Enterprise Wi-Fi Network Award. This is the first time that a Chinese Wi-Fi 6 vendor has won this award, which reflects the full recognition of Huawei AirEngine Wi-Fi 6 by global enterprise users."
  },
  {
    "newsId": "2",
    "title": "Latest technology and industry weather vane",
    "newsType": "Health",
    "imgUrl": $rawfile('news_image2.jpg'),
    "reads": "100",
    "likes": "354",
    "content": "With the large-scale commercial use of new technologies such as 5G, IoT, cloud computing, and AI, industry digital transformation has entered deep water. In addition, the sudden epidemic and carbon-neutral targets accelerate the transformation of society towards intelligence. When energy technologies are combined with power electronics and digital technologies, what direction will site energy develop?"
  },
  {
    "newsId": "3",
    "title": "Openness and Cooperation Facilitate Industry Upgrade",
    "newsType": "Finance",
    "imgUrl": $rawfile('news_image3.jpg'),
    "reads": "74",
    "likes": "91",
    "content": "Under the background of new infrastructure construction, digital transformation will face great pressure in power consumption. According to analysis data, it is estimated that the number of communication sites will increase to 70 million by 2025, and the annual power consumption will exceed 660 billion kWh. The data center will increase to 24 million racks, and the annual power consumption will exceed 950 billion kWh. In pan-industrial scenarios, the annual power consumption of rail transportation and industrial manufacturing alone will exceed 16 trillion kWh There are 40 billion smart terminals, and the annual power consumption will reach 210 billion kWh. The 40 billion mobile terminals under the 21 billion kWh power are driven by the transformation of social media and digital life."
  },
  {
    "newsId": "4",
    "title": "High-voltage super-fast charging is an inevitable trend",
    "newsType": "Finance",
    "imgUrl": $rawfile('news_image4.jpg'),
    "reads": "44",
    "likes": "82",
    "content": "Consumers have a lot of doubts about buying electric cars, compared to fuel cars. Wang Chao pointed out that among the factors affecting the purchase of electric vehicles, charging problems account for 80 percent, with a small number of charging piles (currently, the ratio of piles is 3.2:1) and a long charging time being the first to bear the brunt. As a result, many players in the charging infrastructure sector are looking for a break. To alleviate consumers' pain points of poor charging experience, we need to improve the pile ratio and shorten the charging time to meet consumers' requirements for fast charging."
  },
  {
    "newsId": "5",
    "title": "Huawei Releases the New Trend of Modular Power Supply, Facilitating Industry Upgrade Through Open Cooperation",
    "newsType": "Technology",
    "imgUrl": $rawfile('news_image5.jpg'),
    "reads": "73",
    "likes": "888",
    "content": "Under the background of new infrastructure construction, digital transformation will face great pressure in power consumption. According to analysis data, it is estimated that the number of communication sites will increase to 70 million by 2025, and the annual power consumption will exceed 660 billion kWh. The data center will increase to 24 million racks, and the annual power consumption will exceed 950 billion kWh. In pan-industrial scenarios, the annual power consumption of rail transportation and industrial manufacturing alone will exceed 16 trillion kWh. There are 40 billion smart terminals, and the annual power consumption will reach 210 billion kWh. The 40 billion mobile terminals under the 21 billion kWh power are driven by the transformation of social media and digital life."
  },
  {
    "newsId": "6",
    "title": "Ten Future Trends of Digital Energy",
    "newsType": "Technology",
    "imgUrl": $rawfile('news_image6.jpg'),
    "reads": "100",
    "likes": "354",
    "content": "Energy digitalization is an inevitable trend. Innovative integration of digital and energy technologies enables end-to-end visual, manageable, and controllable intelligent management of energy infrastructure, improving energy efficiency.\nGreen power will benefit thousands of industries and households in the future. Green power, represented by PV, will become the main energy source. The era of price-effective PV is coming, and the integration of distributed power generation and solar storage will become an inevitable trend. Green Power will also help the ICT industry reduce its carbon footprint. In the future, we will build a \"zero-carbon network\" and \"zero-carbon\" data center. In addition, Huawei proposed the trend of full-link efficiency for the first time and implemented global optimization in terms of architecture and system."
  },
  {
    "newsId": "7",
    "title": "Ascend Helps Industry, Learning, and Research Promote AI Industry Development in the National AI Contest",
    "newsType": "Sport",
    "imgUrl": $rawfile('news_image7.jpg'),
    "reads": "123",
    "likes": "911",
    "content": "The holding of the National AI Contest further fulfilled the requirements of the Ministry of Science and Technology and the Ministry of Industry and Information Technology for Shenzhen to build the national new-generation AI innovation and development trial zone and the AI innovation and application pilot zone. It also promoted the integration and development of innovation elements such as industry, academia, capital, and talent, create an AI innovation atmosphere. Huawei has co-hosted two National AI Competitions in a row, aiming to promote technological progress, industrial upgrade, economic transformation, and social progress, and jointly promote the implementation of AI technologies. This is the most practical point for Huawei and the National AI Competition."
  },
  {
    "newsId": "8",
    "title": "Enterprise data centers are moving towards autonomous driving network",
    "newsType": "Sport",
    "imgUrl": $rawfile('news_image8.jpg'),
    "reads": "754",
    "likes": "149",
    "content": "More than 90% of enterprises say that fully autonomous driving data center network is their goal to achieve business agility, flexibility, and cost-effectiveness. This is a key research result in the data center network Autonomous Driving Index Report released by Huawei and IDC. Autonomous driving data center network helps enterprises restructure network architectures and operation models and enhance business resilience and continuity. In addition, regardless of the current level of data center network automation, IDC offers some guidance on how enterprises can move forward and move towards full automation."
  },
  {
    "newsId": "9",
    "title": "One optical fiber lights up a green smart room",
    "newsType": "Internet",
    "imgUrl": $rawfile('news_image9.jpg'),
    "reads": "631",
    "likes": "714",
    "content": "At the 2020 China Real Estate Development Summit held in Guangzhou, Jin Yuzhi, President of Huawei's Transmission and Access Product Line, delivered a keynote speech entitled \"One Fiber Lights Green Smart Rooms\" to discuss the convergence development trend of optical networks and real estate industries, proposes that optical fibers are the standard configuration of F5G smart real estate, and shares seven reasons for choosing Fiber to the Room (FTTR) all-optical home networking, we call on industry partners to work together to build an F5G real gigabit all-optical room ecosystem."
  },
  {
    "newsId": "10",
    "title": "BWS2020: Accelerate Network Autonomy and Enable Agile Business",
    "newsType": "Internet",
    "imgUrl": $rawfile('news_image10.jpg'),
    "reads": "53",
    "likes": "824",
    "content": "Currently, millions of enterprises embrace changes and accelerate their cloudification. SaaS traffic surges. Enterprise cloudification and multi-cloud collaboration become the new focus of cloud-network synergy . To address this challenge, Guo Dazheng, president of Huawei's data communications field, said: \"In cloud-network scenarios, iMaster NCE implements network as a service to help carriers provide cloud-network integration services and meet enterprise cloud access requirements.\" In 5G transport scenarios, improve the automation capability of the entire process of planning, construction, maintenance, and optimization to meet the requirements of large-scale 5G network construction and cloud network cost reduction and efficiency improvement."
  },
  {
    "newsId": "11",
    "title": "Trust technology, embrace openness, and share the world prosperity brought by technology",
    "newsType": "Game",
    "imgUrl": $rawfile('news_image11.jpg'),
    "reads": "1500",
    "likes": "3542",
    "content": "Huawei successfully held the TrustInTech 2020 online summit today. Ryan Ding, Executive Director of Huawei, President of the Carrier BG, and Jim Rogers, a senior Wall Street investor, GSMA Chief Marketing Officer Stephanie Lynch-Habib and other ICT industry experts and economists from around the world attended the summit. The summit pointed out that ICT has become a digital foundation for economic development and people's livelihood. In an era of accelerated commercial use of 5G, the world needs to embrace openness and cooperation to eliminate unnecessary resistance and fears about new technologies and transnational cooperation, thereby sharing the world prosperity brought by technology."
  },
  {
    "newsId": "12",
    "title": "Intelligent Twins Won the Leading Technology Achievement Award at the 7th World Internet Conference",
    "newsType": "Game",
    "imgUrl": $rawfile('news_image12.jpg'),
    "reads": "7451",
    "likes": "9511",
    "content": "Today, the Leading Technology Award was unveiled at the 7th World Internet Conference. As the industry's first systematic technical reference architecture for government and enterprise intelligence upgrade, intelligent virtual appliances have been recognized by experts and judges and won the Leading Scientific Achievement Award for their exploration and practice in various industries. This is the fifth time Huawei has won this award since 2016. The World Internet Leading Science and Technology Award showcases the latest technologies in the global ICT field and focuses on the best practices of innovative technologies in the fields of science and technology fight against epidemics, recovery of work, and promotion of digital economic development and cooperation."
  },
  {
    "newsId": "13",
    "title": "4G/5G FWA, New Engine for Revenue Growth",
    "newsType": "Health",
    "imgUrl": $rawfile('news_image13.jpg'),
    "reads": "445",
    "likes": "872",
    "content": "The reason why FWA is growing so fast is that it provides new opportunities for carriers in the consumer market. For example, a Philippine operator used 4G FWA to rapidly develop home broadband users. According to its third quarter financial report, the operator has successfully developed 2.78 million new users this year, accounting for 80% of the total broadband users. The percentage of broadband revenue increased from 12.3% in 2017 to 17.9%. With the development of wireless technologies, 5G FWA can provide gigabit home access experience similar to that of optical fibers, meeting services such as 4K/8K HD video and AR/VR interactive experience."
  },
  {
    "newsId": "14",
    "title": "Down! CPI released in November! These things are cheap",
    "newsType": "Finance",
    "imgUrl": $rawfile('news_image14.jpg'),
    "reads": "734",
    "likes": "8788",
    "content": "Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap.Down! CPI released in November! These things are cheap."
  },
  {
    "newsId": "15",
    "title": "Comedy movie \"Big Red Envelope\" is set to celebrate the New Year",
    "newsType": "Technology",
    "imgUrl": $rawfile('news_image15.jpg'),
    "reads": "1010",
    "likes": "3534",
    "content": "Comedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New YearComedy movie \"Big Red Envelope\" is set to celebrate the New Year"
  },
  {
    "newsId": "16",
    "title": "Three living things are smart",
    "newsType": "Sport",
    "imgUrl": $rawfile('news_image16.jpg'),
    "reads": "1243",
    "likes": "9141",
    "content": "Lifan said at the press conference: \"Retrospective on the journey, we have always adhered to the spirit of initiative, innovation and science. Build core capabilities in terms of networks, technologies, and platforms, and deliver excellent services, technologies, and quality. Internal maintenance, optimization, and sharing are in place. Carriers, equipment vendors, and partners are working together to build an end-to-end network capability and win-win ecosystem to provide users with the best 5G experience and services.\""
  },
  {
    "newsId": "17",
    "title": "Maximizing the Value of Wireless Networks and Ushering in the Golden Decade of 5G",
    "newsType": "Internet",
    "imgUrl": $rawfile('news_image17.jpg'),
    "reads": "7574",
    "likes": "1439",
    "content": "The 5G industry is developing faster than the previous standards. Currently, there are more than 100 5G commercial networks around the world, and the price of entry-level 5G mobile phones has fallen to CNY1,000, which has led to the rapid growth of 5G users worldwide. Thanks to this, leading operators have enjoyed the data dividend brought by 5G. The multi-dimensional package design and 5G message and 5G new communication services are upgraded to increase the ARPU of 5G users to different degrees.\nTo promote the further development of 5G networks and encourage more users to choose and prefer 5G networks, operators need to build 5G top-quality networks for individual users to achieve full-scenario coverage in densely populated urban areas, suburban areas, and indoor areas, allowing mobile phone users to access 5G services anytime, anywhere. In addition, 5G connection experience is optimized to ensure consistent user experience."
  },
  {
    "newsId": "18",
    "title": "Technology Helps Art, Leads a New Era",
    "newsType": "Game",
    "imgUrl": $rawfile('news_image18.jpg'),
    "reads": "6311",
    "likes": "7114",
    "content": "Zhang Wenlin spoke highly of the \"Dance Storm\". He believes that since the second season's premiere, the show has brought the contestants' exquisite dance moves, the wonderful host of Mr Ho Kung, the excellent comments of the judges' tutors, and the exquisite design of the staff to the audience, dedicate a beautiful visual feast! To help achieve fantastic visual effects, Hunan Radio and TV set up a joint team with Huawei to develop a spatio-temporal condensation system for Dance Storm 2. The system supports AI algorithms such as intelligent fast focusing, butterfly shooting, zoom-in, and multi-focus, with the help of the video 3.0+ platform of device-cloud synergy, the program team has made several industry-leading achievements, such as the three-dimensional storm moment with fantastic visual changes, free-view Dance Storm program with interactive control and rotation, and dance Storm with 360-degree panoramic view. VR programs."
  },
  {
    "newsId": "19",
    "title": "Open Intelligent Twin Ecosystem Is the Key to All-Scenario Intelligence",
    "newsType": "Health",
    "imgUrl": $rawfile('news_image19.jpg'),
    "reads": "6341",
    "likes": "7164",
    "content": "Intelligent upgrade will build core competitiveness in various industries. Huawei works with partners to integrate 5G, cloud, AI, intelligent edge, and industry applications to form an integrated intelligent system and create industry-leading smart experience. In the transportation industry, the abolition of highway toll stations at the provincial boundary enables fast and insensitive traffic, greatly improving traffic efficiency and reducing logistics transportation costs. Intelligent cameras are deployed on highway portals to collect vehicle traffic data 24 hours a day and send the data to the cloud in real time over the high-speed network for real-time charging. In addition, AI models trained on the cloud can be pushed to the edge so that cameras can have capabilities such as license plate recognition and vehicle feature extraction, and the capabilities can be continuously evolved. For example, in extreme weather conditions such as rain and snow, one-click upgrade can be performed on the cloud."
  }
]

/**
 * 用于初始化NewsData的数组
 */
export function getNewsDatas(): Array<NewsData> {
  let newsDataArray: Array<NewsData> = []
  NewsComposition.forEach(item => {
    newsDataArray.push(new NewsData(item.newsId, item.title, item.newsType, item.imgUrl, item.reads, item.likes, item.content))
  })
  return newsDataArray;
}

/**
 * 获取默认新闻（第一条新闻数据）
 */
export function getDefaultData(): NewsData {
  let item = NewsComposition[0]
  let newsData: NewsData = new NewsData(item.newsId, item.title, item.newsType, item.imgUrl, item.reads, item.likes, item.content)
  return newsData;
}