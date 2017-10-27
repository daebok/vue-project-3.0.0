<!-- 首页 by fenglei -->
<template>
  <div class="page page-index">
    <div class="content">
      <!-- S首页banner图 -->
      <div id="banner">
        <mt-swipe :auto="3000">
          <mt-swipe-item v-if="bannerList && bannerList.length == 0">
            <img src="../assets/images/banner_default.png">
          </mt-swipe-item>
          <mt-swipe-item v-else v-for="item in bannerList" :key="item.id">
            <a :href="item.url">
              <img :src="item.picPath">
            </a>
          </mt-swipe-item>
        </mt-swipe>
      </div>
      <!-- E首页banner图 -->
      <!-- S滚动通告 -->
      <div class="rolling-notice" v-if="noticeList && noticeList.length > 0">
        <img src="../assets/images/index/home_ico_gonggao.png">
        <div class="scroll-wrap" ref="scrollWrap">
          <ul class="scroll-content" :style="{ top, transition }">
            <li v-for="item in noticeList" @click="linkToParams('siteIntroDetail', item.id)">{{ item.title }}</li>
          </ul>
        </div>
      </div>
      <!-- E滚动通告 -->
      <!-- S推广 -->
      <div class="promotion-content">
        <ul>
          <li v-if="advertList && advertList.length < 1">
            <router-link :to="{ name: 'invest' }">
              <div class="left">
                <span class="title">年度狂欢 为你而来</span>
                <span class="introduction">最高送<u> 188 元</u>现金红包</span>
              </div>
              <div class="right">
                <img src="../assets/images/index/home_pic_ad1.png">
              </div>
            </router-link>
          </li>
          <li v-if="advertList && advertList.length < 2">
            <router-link :to="{ name: 'invite' }">
              <div class="left">
                <span class="title">人脉变钱脉,<u>奖励多多</u></span>
                <span class="introduction">邀请好友就能送红包哦</span>
              </div>
              <div class="right">
                <img src="../assets/images/index/home_pic_ad2.png">
              </div>
            </router-link>
          </li>
          <li v-if="advertList && advertList.length > 0" v-for="item in advertList">
            <div class="left">
              <span class="title">{{ item.title }}</span>
              <span class="introduction">{{ item.content | htmlTagsNo }}</span>
            </div>
            <div class="right">
              <img :src="item.picPath">
            </div>
          </li>
        </ul>
      </div>
      <!-- E推广 -->
      <!-- S粮票宝 -->
      <div v-if="lastFundUnitData != ''" class="treasure-coupons">
        <div class="header-title">
          <i class="treasure-coupons-color"></i>
          <span>粮票宝</span>
          <b>|</b>
          <label>安全稳定 随存随取</label>
        </div>
        <div class="data">
          <router-link to="/fund">
          <div class="left">
            <span class="sevenProfit-value">{{ lastFundUnitData.sevenProfit }}<i>%</i></span>
            <span class="sevenProfit-text">七日年化收益率</span>
          </div>
          <div class="right">
            <span class="minx-investment">随存随取·1元起投</span>
            <span class="fundName">{{ lastFundUnitData.fundName }}</span>
          </div>
          </router-link>
        </div>
      </div>
      <!-- E粮票宝 -->
      <!-- S固收理财 -->
      <div v-if="normalinvestList && normalinvestList.length > 0" class="recommended-invest">
        <div class="header-title">
          <i class="recommended-invest-color"></i>
          <span>固收理财</span>
          <b>|</b>
          <label>固定收益，稳健之选</label>
        </div>
        <div class="list">
          <ul>
            <li v-for="(item, index) in normalinvestList" :class="{ 'no-border': normalinvestList.length == 1 || index == normalinvestList.length - 1, 'saleOut': item.remainAccount == 0 }">
              <router-link :to="{ name:'investDetail', params: { projectId: item.projectId }}">
                <div class="projectName">{{ item.projectName }}</div>
                <div class="invest-tag">
                  <label v-if="item.choice == 1" class="tag-remen"></label>
                  <label v-if="item.novice == 1" class="tag-xinshou"></label>
                  <label v-if="item.additionalRateUseful == 1 || item.redEnvelopeUseful == 1" class="tag-youhui"></label>
                  <label v-if="item.specificSale != 0" class="tag-dingxiang"></label>
                  <!--<label v-if="item.realizeUseful == 1" class="tag-bainxian"></label>-->
                  <label v-if="item.specificSale == 2" class="tag-vip" :class="'tag-vip' + item.specificSaleRule"></label>
                </div>
                <div class="left">
                  <span class="apr-value">{{ item.apr | currency('', 2) }}<i v-if="item.addApr > 0">%+{{ item.addApr }}%</i><i v-else>%</i></span>
                  <span class="apr-text">预期年化收益率</span>
                </div>
                <div class="right">
                  <span class="lowestAccount">起投&nbsp;&nbsp;{{ item.lowestAccount }}元</span>
                  <span class="timeLimit">期限&nbsp;&nbsp;{{ item.timeLimit }}<i v-if="item.timeType == 0">个月</i><i v-else>天</i></span>
                </div>
              </router-link>
            </li>
          </ul>
          <router-link :to="{ name: 'invest', params: { activeTagIndex: 0 }}" v-if="normalinvestList.length >= 4">
            <div class="footer-more"><span>查看更多产品</span><img class="home_arrow_r" src="../assets/images/index/home_arrow_r.png"></div>
          </router-link>
        </div>
      </div>
      <!-- E固收理财 -->
      <!-- S票据理财 -->
      <div v-if="optimizationList && optimizationList.length > 0" class="recommended-invest">
        <div class="header-title">
          <i class="bill-financing-color"></i>
          <span>票据理财</span>
          <b>|</b>
          <label>银行兑现，放心理财</label>
        </div>
        <div class="list">
          <ul>
            <li v-for="(item, index) in optimizationList" :class="{ 'no-border': optimizationList.length == 1 || index == optimizationList.length - 1, 'saleOut': item.remainAccount == 0 }">
              <router-link :to="{ name:'investDetail', params: { projectId: item.projectId }}">
                <div class="projectName">{{ item.projectName }}</div>
                <div class="invest-tag">
                  <label v-if="item.choice == 1" class="tag-remen"></label>
                  <label v-if="item.novice == 1" class="tag-xinshou"></label>
                  <label v-if="item.additionalRateUseful == 1 || item.redEnvelopeUseful == 1" class="tag-youhui"></label>
                  <label v-if="item.specificSale != 0" class="tag-dingxiang"></label>
                  <!--<label v-if="item.realizeUseful == 1" class="tag-bainxian"></label>-->
                  <label v-if="item.specificSale == 2" class="tag-vip" :class="'tag-vip' + item.specificSaleRule"></label>
                </div>
                <div class="left">
                  <span class="apr-value">{{ item.apr | currency('', 2) }}<i v-if="item.addApr > 0">%+{{ item.addApr }}%</i><i v-else>%</i></span>
                  <span class="apr-text">预期年化收益率</span>
                </div>
                <div class="right">
                  <span class="lowestAccount">起投&nbsp;&nbsp;{{ item.lowestAccount }}元</span>
                  <span class="timeLimit">期限&nbsp;&nbsp;{{ item.timeLimit }}<i v-if="item.timeType == 0">个月</i><i v-else>天</i></span>
                </div>
              </router-link>
            </li>
          </ul>
          <router-link :to="{ name: 'invest', params: { activeTagIndex: 1 }}" v-if="optimizationList.length >= 4">
            <div class="footer-more"><span>查看更多产品</span><img class="home_arrow_r" src="../assets/images/index/home_arrow_r.png"></div>
          </router-link>
        </div>
      </div>
      <!-- E票据理财 -->
      <!-- S动态资讯 -->
      <div class="dynamic-information" v-if="newsList && newsList.length > 0">
        <div class="header-title">
          <i class="dynamic-information-color"></i>
          <span>动态资讯</span>
        </div>
        <div class="list">
          <ul>
              <li v-for="(item, index) in newsList" :class="{ 'no-border': newsList.length == 1 || index == newsList.length - 1 }">
                <router-link :to="{ name:'columnDetail', params: { uuid: item.id }}">
                  <span class="title">{{ item.title }}</span>
                  <span class="time">{{ item.createTime | dateFormatFun(4) }}</span>
                </router-link>
              </li>
          </ul>
          <router-link :to="{ name: 'column' }" v-if="newsList.length >= 4">
            <div  class="footer-more"><span>查看更多资讯</span><img class="home_arrow_r" src="../assets/images/index/home_arrow_r.png"></div>
          </router-link>
        </div>
      </div>
      <!-- E动态资讯 -->
    </div>
  </div>
</template>

<script>
  import * as ajaxUrl from '../ajax.config'; // 引入所有接口地址

  export default {
    name: 'page-index',
    data() {
      return {
        bannerList: [], // 首页banner图
        noticeList: [], // 首页获取通告列表
        noticeListParams: {
          code: 'notice', // 通告类型
          pageSize: 6 // 显示通告的数量
        },
        activeIndex: 0, // 通告数量计数单位
        scrollWrapHeight: 0, // 通告列表滚动高度
        newsList: [], // 动态资讯列表
        newsListParams: {
          code: 'news', // 资讯类型
          pageSize: 4 // 显示资讯的数量
        },
        advertList: [], // 推广列表
        advertListParams: {
          code: 'app_advert', // 推广类型
          pageSize: 2 // 显示推广的数量
        },
        normalinvestList: [], // 固收理财列表
        normalinvestListParams: {
          projectTypeId: '93ea6a3266154b37bab7761717621685', // 固收理财类型
          'page.page': '1', // 初适化页数
          'page.pageSize': 4 // 显示固收理财的数量
        },
        optimizationList: [], // 票据理财列表
        optimizationListParams: {
          projectTypeId: 'b96ac6f1ff8b4639b32a3cb4ad5b524e', // 票据理财类型
          'page.page': '1', // 初适化页数
          'page.pageSize': 4 // 显示票据理财的数量
        },
        lastFundUnitData: '' // 粮票宝
      };
    },
    computed: {
      top() {
        return -this.activeIndex * this.scrollWrapHeight + 'px';
      },
      transition() {
        return this.activeIndex === 0 ? 'none' : 'top 0.5s';
      }
    },
    created() {
      // banner图数据获取
      this.$http.get(ajaxUrl.bannerAjax).then((res) => {
        if (res.data.resData) {
          this.bannerList = res.data.resData.list;
        }
      });
      // 滚动通知数据获取
      this.$http.get(ajaxUrl.articleListAjax, { params: this.noticeListParams }).then((res) => {
        if (res.data.resData) {
          this.noticeList = res.data.resData.list;
          this.$nextTick(() => {
            this.scrollWrapHeight = this.$refs.scrollWrap.offsetHeight;
          });
        }
      });
      // 动态资讯数据获取
      this.$http.get(ajaxUrl.articleListAjax, { params: this.newsListParams }).then((res) => {
        if (res.data.resData) {
          this.newsList = res.data.resData.list;
        }
      });
      // 推广数据获取
      this.$http.get(ajaxUrl.articleListAjax, { params: this.advertListParams }).then((res) => {
        if (res.data.resData) {
          this.advertList = res.data.resData.list;
        }
      });
      // 固收理财数据获取
      this.$http.get(ajaxUrl.projectListAjax, { params: this.normalinvestListParams }).then((res) => {
        if (res.data.resData) {
          this.normalinvestList = res.data.resData.list;
        }
      });
      // 票据理财数据获取
      this.$http.get(ajaxUrl.projectListAjax, { params: this.optimizationListParams }).then((res) => {
        if (res.data.resData) {
          this.optimizationList = res.data.resData.list;
        }
      });
      // 粮票宝数据获取
      this.$http.get(ajaxUrl.lastFundUnitAjax).then((res) => {
        if (res.data.resData) {
          this.lastFundUnitData = res.data.resData.lastFundUnit;
        }
      });

      // 安卓下部分样式调整
      this.$nextTick(() => {
        if (/Android/gi.test(navigator.userAgent)) {
          var footerMore = document.getElementsByClassName('home_arrow_r');
          var i;
          for (i = 0; i < footerMore.length; i++) {
            footerMore[i].style.marginTop = "0.03rem";
          }
        }
      });
    },
    mounted() {
      // 滚动通告效果
      var t = setInterval(() => {
        if (this.noticeList.length == 1) {
          clearInterval(t);
        }
        if (this.activeIndex < this.noticeList.length - 1) {
          this.activeIndex += 1;
        } else {
          this.activeIndex = 0;
        }
      }, 3000);
    },
    methods: {
      linkTo(url) {
        this.$router.push(url);
      },
      linkToParams( name, id) {
        this.$router.push({ name: name, params: { uuid: id }});
      }
    },
    filters: {
      // 去html标签过滤器
      htmlTagsNo(value) {
        if (!value) return '';
        value = value.replace(/<[^>]+>/g,'');
        return value;
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass">
  @import "../assets/sass/index.sass";
</style>
