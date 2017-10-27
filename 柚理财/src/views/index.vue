<!-- 首页 -->
<template>
  <div class="page page-index">
    <div class="content">
      <!-- S首页banner图 -->
      <div id="banner">
        <mt-swipe :auto="3000">
          <mt-swipe-item v-if="bannerList && bannerList.length === 0">
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
      <!-- S首页导航 -->
      <div class="aui-flex-col aui-flex-middle text-center">
        <!-- <div class="aui-flex-item-3" @click="linkTo('/account/appoint_loan')">
          <img src="../assets/images/index/home_icon_reserve.png">
          <p>预约借款</p>
        </div> -->
        <div class="aui-flex-item-3" @click="linkTo('/mine/about_us/security')">
          <img src="../assets/images/index/home_icon_safety.png">
          <p>安全保障</p>
        </div>
        <div class="aui-flex-item-3" @click="linkTo('/mine/siteData')">
          <img src="../assets/images/index/home_icon_xinshou.png">
          <p>平台数据</p>
        </div>
        <div class="aui-flex-item-3" @click="linkTo('mine/about_us/compan_intro')">
          <img src="../assets/images/index/home_icon_huodong.png">
          <p>平台介绍</p>
        </div>
        <div class="aui-flex-item-3" @click="linkTo('/mine/invite')">
          <img src="../assets/images/index/home_icon_yaoqing.png">
          <p>邀请好友</p>
        </div>
      </div>
      <!-- E首页导航 -->
      <!-- S平台累计投资金额 -->
      <div class="data-info" v-show="user.userId === undefined ">
        <h4>平台累计投资金额(元)</h4>
        <h2 class="main-auxiliary-color font-arial">{{ investTotal | currency('',2) }}</h2>
        <router-link to="/login?redirect=/index">
          <div class="btn-log main-btn-bg">注册/登录</div>
        </router-link>
      </div>
      <!-- E平台累计投资金额 -->
      <!-- S新手标列表 -->
      <div class="newbid">
        <h4 class="aui-border-b">新客专享 <span class="aui-border-l">放心首选</span></h4>
        <bid-list :list="newerBidList"></bid-list>
      </div>
      <div class="newbid recommand">
        <h4 class="aui-border-b">理财精选 <span class="aui-border-l">推荐购买</span></h4>
        <bid-list :list="recommandBidList"></bid-list>
      </div>
    </div>
  </div>
</template>

<script>
  import { mapState } from 'vuex'
  import BidList from '../components/BidList.vue'; // 列表组件
  import * as ajaxUrl from '../ajax.config.js'; // 引入所有接口地址

  export default {
    name: 'page-index',
    data() {
      return {
        bannerList: [], // 首页banner图列表数组
        investTotal: 0, // 平台累计投资金额
        newerBidList: [], // 新手标列表数组
        recommandBidList: [] // 推荐标列表数组
      }
    },
    computed: mapState({ user: (state) => state.user }),
    created(){
      this.$http.get(ajaxUrl.bannerAjax).then((res) => {
        this.bannerList = res.data.resData.list;
      })
      this.$http.get(ajaxUrl.investTotalAjax).then((res) => {
        this.investTotal = res.data.resData.investTotal;
      })
      this.$http.get(ajaxUrl.recommandProjectAjax).then((res) => {
        this.newerBidList = res.data.resData.novicelist;
        this.recommandBidList = res.data.resData.choicelist;
      })
    },
    methods: {
      linkTo(url) {
        this.$router.push(url);
      }
    },
    components: {
      BidList
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .gray-status * {color: #999!important;}/*投满时变灰*/
  .mint-swipe {
    height: 1.64rem;
    color: #fff;
    font-size: 30px;
    text-align: center;
  }
  .mint-swipe-item {
    line-height: 1.64rem;
    background:#ccc center no-repeat;
    background-size: contain;
    display:block;
    height:100%;
  }
  .mint-swipe-item img {
    width: 100%;
    height: 100%;
  }
  .aui-flex-col {
    height: .9rem;background:#fff;
    display: block;padding-top:.10rem;
  }
  .aui-flex-item-3 img {width: .45rem;}
  .aui-flex-item-3 p {color: #333; font-size: .14rem; margin-top: .05rem;}
  .data-info {margin-top: .1rem;height: .9rem; padding:.15rem;background:#fff;position: relative;}
  .data-info h4 {font-size: .14rem; color:#666; line-height: 1;}
  .data-info h2 {font-size: .28rem;margin-top: .1rem;}
  .data-info .btn-log {width: 1.03rem;color:#fff;text-align:center;font-size: .16rem; line-height: .33rem; position: absolute;right:.15rem;top:50%;border-radius:.33rem; margin-top:-.18rem;}
  .newbid {background:#fff;margin-top: .1rem;}
  .newbid h4 {line-height:.4rem;padding-left:.15rem; color: #333;}
  .newbid h4 span {color:#999;margin-left: .03rem; padding-left: .08rem;font-size: .12rem;}
</style>
