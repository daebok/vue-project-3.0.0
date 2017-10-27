<!-- 首页 -->
<template>
  <div class="page page-index">
    <div class="content" v-scroll-record>
      <!-- S首页banner图 -->
      <div id="banner">
        <mt-swipe :auto="3000">
          <mt-swipe-item v-if="bannerList && bannerList.length === 0">
            <img src="../assets/images/banner_default.png">
          </mt-swipe-item>
          <mt-swipe-item v-else v-for="banner in bannerList">
            <a :href="banner.url">
              <img :src="banner.picPath">
            </a>
          </mt-swipe-item>
        </mt-swipe>
      </div>
      <!-- E首页banner图 -->
      <!-- S首页导航 -->
      <div class="aui-flex-col aui-flex-middle text-center">
        <div class="aui-flex-item-3" @click="linkTo('/account/appoint_loan')">
          <img src="../assets/images/index/home_icon_reserve.png">
          <p>预约借款</p>
        </div>
        <div class="aui-flex-item-3" @click="linkTo('/mine/siteData')">
          <img src="../assets/images/index/home_icon_data.png">
          <p>平台数据</p>
        </div>
        <div class="aui-flex-item-3" @click="linkTo('/mine/about_us/security')">
          <img src="../assets/images/index/home_icon_safety.png">
          <p>安全保障</p>
        </div>
        <div class="aui-flex-item-3" @click="linkTo('/mine/invite')">
          <img src="../assets/images/index/home_icon_invitation.png">
          <p>邀请有礼</p>
        </div>
      </div>
      <!-- E首页导航 -->
      <!-- S平台累计投资金额 -->
      <div class="data-info" v-show="$store.state.user.userId === undefined ">
        <h4>平台累计投资金额(元)</h4>
        <h2 class="main-color font-arial">{{ investTotal | currency('',2) }}</h2>
        <router-link to="/login?redirect=/index">
          <div class="btn-log main-btn-bg">注册/登录</div>
        </router-link>
      </div>
      <!-- E平台累计投资金额 -->
      <!-- S新手标列表 -->
      <div class="newbid">
        <h4 class="aui-border-b">新客专享 <span class="aui-border-l">给您更多的福利</span></h4>
        <ul>
          <li class="bid-con aui-border-b" :class="{'gray-status': item.remainAccount === 0}" v-for="(item, index) in newerBidList">
            <router-link :to="{name:'investDetail', params: {projectId: item.projectId}}">
              <h3>{{ item.projectName | hideBorrowName(24) }}
                <span v-if="item.novice == 1" class="bid-icon new aui-border">新客</span>
                <span v-if="item.bondUseful == 1" class="bid-icon kzr aui-border">可转让</span>
                <span v-if="item.realizeUseful == 1" class="bid-icon kbx aui-border">可变现</span>
                <span v-if="item.specificSale == 1 || item.specificSale == 3" class="bid-icon dx aui-border">定向</span>
                <span v-if="item.specificSale == 2" class="bid-icon vip aui-border">vip{{item.specificSaleRule}}</span>
              </h3>
              <div class="bid-info clearfix">
                <div class="apr-part pull-left">
                  <h1 class="apr main-color">{{ item.apr.toFixed(2) }}<b>%<span v-if="item.addApr > 0">+{{ item.addApr.toFixed(2) }}%</span></b></h1>
                  <p>预期年化收益率</p>
                </div>
                <div class="time-part pull-left">
                  <p>起投  <b>{{ item.lowestAccount }}元</b></p>
                  <p v-if="item.timeType === 0">期限  <b>{{ item.timeLimit }}个月</b></p>
                  <p v-else>期限  <b>{{ item.timeLimit }}天</b></p>
                </div>
              </div>
              <div class="circle-area">
                <div v-if="item.remainAccount > 0">
                  <circle-progress v-if="item.saleTime > item.timeNow" :percent="0" :diameter="60" :text-size="11" text-color="#f60" foreground-color="#F95A28" backgroundColor="#f8f8f8" :text-format="item.saleTime | dateFormatFun(3)" :showFlag="false" :showTime="true" :line-width="2"></circle-progress>
                  <circle-progress v-else :percent="item.remainAccount | scalesFun(item.account)" :diameter="60" :text-size="14" text-color="#f60" foreground-color="#F95A28" backgroundColor="#f8f8f8" text-format="{percent}" :line-width="2"></circle-progress>
                </div>
                <div v-else>
                  <circle-progress :percent="100" :diameter="60" :text-size="14" foreground-color="#999" backgroundColor="#f8f8f8"  text-format="已抢光" :showFlag="false" :line-width="2"></circle-progress>
                </div>
              </div>
            </router-link>
          </li>
        </ul>
      </div>
      <div class="newbid recommand">
        <h4 class="aui-border-b">优选产品 <span class="aui-border-l">给您更高的收益</span></h4>
        <ul>
          <li class="bid-con aui-border-b" :class="{'gray-status': item.remainAccount == 0}" v-for="item in recommandBidList">
            <router-link :to="{name:'investDetail', params: {projectId: item.projectId}}">
              <h3>{{ item.projectName | hideBorrowName(24) }}
                <span v-if="item.novice == 1" class="bid-icon new aui-border">新客</span>
                <span v-if="item.bondUseful == 1" class="bid-icon kzr aui-border">可转让</span>
                <span v-if="item.realizeUseful == 1" class="bid-icon kbx aui-border">可变现</span>
                <span v-if="item.specificSale == 1 || item.specificSale == 3" class="bid-icon dx aui-border">定向</span>
                <span v-if="item.specificSale == 2" class="bid-icon vip aui-border">VIP{{item.specificSaleRule}}</span>

              </h3>
              <div class="bid-info clearfix">
                <div class="apr-part pull-left">
                  <h1 class="apr main-color">{{ item.apr.toFixed(2) }}<b>%<span v-if="item.addApr > 0">+{{ item.addApr.toFixed(2) }}%</span></b></h1>
                  <p>预期年化收益率</p>
                </div>
                <div class="time-part pull-left">
                  <p>起投  <b>{{ item.lowestAccount }}元</b></p>
                  <p v-if="item.timeType == 0">期限  <b>{{ item.timeLimit }}个月</b></p>
                  <p v-if="item.timeType == 1">期限  <b>{{ item.timeLimit }}天</b></p>
                </div>
              </div>
              <div class="circle-area">
                <div v-if="item.remainAccount > 0">
                  <circle-progress v-if="item.saleTime > item.timeNow" :percent="0" :diameter="60" :text-size="11" text-color="#f60" foreground-color="#F95A28" backgroundColor="#f8f8f8" :text-format="item.saleTime | saleTime(item.timeNow)" :showFlag="false" :showTime="true" :line-width="2"></circle-progress>
                  <circle-progress v-else :percent="item.remainAccount | scalesFun(item.account)" :diameter="60" :text-size="14" text-color="#f60" foreground-color="#F95A28" backgroundColor="#f8f8f8" text-format="{percent}" :line-width="2"></circle-progress>
                </div>
                <div v-else>
                  <circle-progress :percent="100" :diameter="60" :text-size="14" foreground-color="#999" backgroundColor="#f8f8f8"  text-format="已抢光" :showFlag="false" :line-width="2"></circle-progress>
                </div>
              </div>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import CircleProgress from '../components/ProgressCircle'; // 引入投资进度圆形进度条组件
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
      CircleProgress
    }
  }
</script>

<style type="text/css" scoped>
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
    display: block;padding-top:.17rem;
  }
  .aui-flex-item-3 img {width: .31rem;}
  .aui-flex-item-3 p {color: #333; font-size: .14rem; margin-top: .05rem;}
  .data-info {margin-top: .1rem;height: .9rem; padding:.15rem;background:#fff;position: relative;}
  .data-info h4 {font-size: .14rem; color:#666; line-height: 1;}
  .data-info h2 {font-size: .28rem;margin-top: .1rem;}
  .data-info .btn-log {width: 1.03rem;color:#fff;text-align:center;font-size: .16rem; line-height: .33rem; position: absolute;right:.15rem;top:50%;border-radius:.33rem; margin-top:-.18rem;}
  .newbid {background:#fff;margin-top: .1rem;}
  .newbid h4 {line-height:.4rem;padding-left:.15rem; color: #333;}
  .newbid h4 span {color:#999;margin-left: .03rem; padding-left: .08rem;font-size: .12rem;}
  .bid-con {height: 1.24rem;padding:.15rem; background:#fff;position: relative;}
  .bid-con:last-child:after {border-bottom: 0;}
  .bid-con h3 {font-size: .15rem; color:#333;}
  .bid-con h3 img {width:.35rem;vertical-align: -.02rem;}
  .bid-con:last-child {border-bottom: 0;}
  .bid-info { margin-top: .15rem; }
  .apr-part .apr {font-size: .31rem; font-family: Arial; margin:-.05rem 0 .03rem 0; line-height:1;}
  .apr-part p {color:#666;}
  .apr-part .apr b {font-size: .16rem;}
  .circle-area {position: absolute; right: .15rem; top: .3rem;}
  .time-part {font-size: .14rem; color: #666;}
  .time-part p:first-child {margin-bottom: .1rem;}
  .apr-part {  width: 1.8rem;  }
  /*icon分类*/
  .bid-icon {font-size: .11rem;  padding: 0 .05rem; display: inline-block;text-align:center; line-height: .18rem;}
  .bid-icon.aui-border:after { border-radius:.2rem;}
  /*新客*/
  .bid-icon.new {color:#F95A28!important;}
  .new.aui-border:after { border-color:#F95A28;}
  /*定向*/
  .bid-icon.dx {color:#97d1fd!important;}
  .dx.aui-border:after { border-color:#97d1fd;}
  /*可转让*/
  .bid-icon.kzr {color:#89d096!important;}
  .kzr.aui-border:after { border-color:#89d096;}
  /*vip*/
  .bid-icon.vip {color:#f9b728!important;}
  .vip.aui-border:after { border-color:#f9b728;}
  /*可变现*/
  .bid-icon.kbx {color: #ad60f9 !important;}
  .kbx.aui-border:after { border-color:#AD60F9;}
</style>
