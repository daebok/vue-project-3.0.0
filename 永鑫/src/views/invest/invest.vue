<template>
  <div class="page page-invest">
    <div class="page-loadmore-wrapper" ref="wrapper">
      <mt-loadmore :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">
        <ul class="page-infinite-list">
          <li class="bid-con" @click="toUrl(cjhxList.fundCode,1,cjhxList.fundType)">
            <h3>
              <em>{{ cjhxList.fundName | hideBorrowName(18) }} {{ cjhxList.fundCode }}</em>
              <span class="service-charge pull-right">0手续费</span>
            </h3>
            <div class="bid-info clearfix">
              <div class="apr-part pull-left">
                <h1 class="apr main-color">{{ cjhxList.dayIncomeratio }}<b >%</b></h1>
                <p>七日年化收益</p>
              </div>
              <div class="time-part pull-left">
                <p class="mar-dx">
                  <b class="strong">{{ cjhxList.hxHfIncomeratio }}<i>元</i></b>
                </p>
                <p>
                  <!-- 万份收益  <b>({{Date.parse(new Date()) | dateFormatFun(6)}})</b> -->
                  万份收益  <b>({{ cjhxList.dayincdate }})</b>
                </p>
              </div>
            </div>
            <span class="main-color from">本产品由创金合信基金管理有限公司提供</span>
          </li>

          <li v-for="item in hxList"   @click="toUrl(item.fundCode,item.saleStatus,item.fundType)" class="bid-con">
            <h3>
              <em>{{ item.fundName | hideBorrowName(7)}} {{ item.fundCode }}</em>
              <!-- 时间戳判断 -->
              <!-- <span v-if="Date.parse(new Date()) < item.saleTime" class="service-charge pull-right">敬请期待</span>
              <span v-else-if="Date.parse(new Date()) > item.stopTime" class="service-charge pull-right gray-status">已售罄</span> -->
              <!-- <span v-if="item.saleStatus == 0" class="service-charge pull-right gray-status">{{ item.saleTime | saleTime('')}}</span> -->
              <span v-if="item.saleStatus == 0" class="service-charge pull-right gray-status">敬请期待</span>
              <span v-else-if="item.saleStatus == 2" class="service-charge pull-right gray-status">已售罄</span>
              <span v-else-if="item.saleStatus == 1" class="service-charge pull-right">抢购中</span>
              <i class="start-num">起投金额：{{ item.lowestAmount | moneyFormat('')}}</i>
            </h3>
            <div class="bid-info clearfix">
              <div class="apr-part pull-left">
                <h1 class="apr main-color">{{ item.yield }}<b >%</b></h1>
                <p>年化业绩比较基准</p>
              </div>
              <div class="time-part pull-left">
                <p class="mar-dx">
                  <b v-if="item.timeType == 1" class="strong">{{ item.timeLimit }}<i>天</i></b>
                  <b v-else-if="item.timeType == 0" class="strong">{{ item.timeLimit }}<i>月</i></b>
                </p>
                <p>
                  产品期限
                </p>
              </div>
            </div>
            <span class="main-color from">本产品由上海华信证券有限责任公司提供</span>
            <!-- <span v-if="item.saleStatus == 0" class="product-djs clearfix">该产品于{{ item.saleTime | saleTime('')}}</span> -->
            <count-down v-if="item.saleStatus == 0" :countdownTime="item.countdownTime" :saleTime="item.saleTime"></count-down>
          </li>

          <!-- 无数据 -->
          <div class="text-center no-data" v-show="noData">
            <img class="invest-default-nodata" src="../../assets/images/public/default/nodata_trilateral.png" alt="缺省图片">
            <p>产品准备中，先去看看其他的吧~</p>
          </div>
        </ul>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config'; // 引入所有接口地址
  //import countDown from '../../mixins/countTime' //单项倒计时组件
  import CountDown from '../../components/myInvest_countTime.vue' //列表倒计时组件
  export default {
    data() {
      return {
        wrapperHeight: 0, // 产品列表高度
        allLoaded: false, // 是否全部数据加载完毕，true已全部加载完毕，false否
        noData: false, // 是否有无产品，true无产品信息，false有
        cjhxList: '', // 理财列表数据1
        hxList: '', // 理财列表数据2
        openId: this.$route.query.openId//微信openId
      }
    },
    created() {
      //console.log(this.openId);
      //localStorage.clear();//清楚localStorage
      //localStorage.openId = this.openId;
      // 理财列表数据初始化
      this.projectList();
      // 计算产品列表高度
      this.$nextTick(() => {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top - 55;
      });
    },
    //mixins:[CountDown],
    components: {CountDown},
    methods: {
      //接口请求
      projectList(type) {
        this.$indicator.open({spinnerType: 'fading-circle'}) //等待接口加载中状态
        this.$http.get(ajaxUrl.projectListAjax).then((res) => {
          this.$indicator.close();
          if (res.data.resData) {
            this.cjhxList = res.data.resData.cjhxProduct;
            this.hxList = res.data.resData.hxFundList;
          }
        });
      },
      //上拉加载
      loadTop(id){
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          this.cjhxList = [];
          this.hxList = [];
          //this.page = 1;
          this.allLoaded = false;
          this.projectList('reload');
        }, 1000)
      },
      toUrl(fundCode,saleStatus,fundType){
        if(fundType == 'cjhx'){
          //点击跳转cjhx详情
          this.$router.push({name:'investDetail', params: {projectId: fundCode,openId: this.openId}})
        }else if(fundType == 'hx' && saleStatus == 1){
          this.$indicator.open({spinnerType: 'fading-circle'}) //等待接口加载中状态
          //去hx详情
          let tokenParams = {
            openId: this.openId,
            fundCode: fundCode
          }
          this.$http.get(ajaxUrl.creatToken, { params: tokenParams }).then((res) => {
            this.$indicator.close();
            if(res.data.resCode != '32771'){
              if(res.data.resMsg == '请先注册后再进入'){
                //未注册进入注册页面
                this.$messagebox({
                  title: ' ',
                  confirmButtonText: '去注册',
                  showCancelButton: false,
                  message: '请先注册后再进入'
                }).then(res => {
                  this.$router.push({name:'register', params: {openId: this.openId}}) //进入注册
                });
              }else{
                //console.log(res.data.resData.hxProductUrl);
                window.location.href = res.data.resData.hxProductUrl;
              }
            }
          })
        }
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var.scss";
  .from{margin-top:.05rem; display:block;}
  .product-djs{display:block; color:#666; margin-top:.10rem;}
  .gray-status{color: #999!important; border:1px solid #999!important;}/*投满时变灰*/
  .start-num{color:#333; float:right; margin-right:.05rem; font-size:.14rem; position:relative; top:.01rem;}
  .hor-scroll {  overflow-x: auto;  }
  .wrapper-scroll {
    height: .45rem;
    position:relative;
  }
  #wrapper {
    position: absolute;
    z-index: 1;
    top: 0;
    left: 0;
    width: 100%;
    background: #ccc;
    overflow: hidden;
    height: .36rem;
  }
  #scroller {
    position: absolute;
    z-index: 1;
    /*-webkit-tap-highlight-color: rgba(0,0,0,0);*/
    min-width: 400px;
    height: 100%;
    background-color: #a00;
    /*-webkit-touch-callout: none;*/
    /*-webkit-user-select: none;*/
    /*user-select: none;*/
    /*-webkit-text-size-adjust: none;*/
    text-size-adjust: none;
    background:#fff;
  }
  .aui-flex-col {
    height: .36rem;
    overflow-y: hidden;
    background:#fff;
    margin-bottom: .1rem;
    width: 240%;
    display: block;
  }
  .aui-flex-item-3 {  padding-top:.02rem;  }
  .aui-flex-item-3 p {
    color: #333;
    line-height:.2rem;
    padding: .05rem .05rem .07rem;
    margin: 0 .1rem;
  }
  .aui-flex-item-3.current p {color: $main-color;border-bottom: 1px solid $main-color; }
  .data-info {margin-top: .1rem;height: .9rem; padding:.15rem;background:#fff;position: relative;}
  .data-info h4 {font-size: .14rem; color:#666; line-height: 1;}
  .data-info h2 {font-size: .28rem;margin-top: .1rem;}
  .data-info .btn-log {width: 1.03rem;color:#fff;text-align:center;font-size: .16rem; line-height: .33rem; position: absolute;right:.15rem;top:50%;border-radius:.33rem; margin-top:-.18rem;}
  .newbid {background:#fff;margin-top: .1rem;}
  .newbid h4 {line-height:.4rem;padding-left:.15rem; color: #333;}
  .newbid h4 span {color:#999;margin-left: .03rem; padding-left: .08rem;font-size: .12rem;}
  .bid-con {padding:.15rem; background:#fff;position: relative;margin-bottom:.05rem;}
  .bid-con:last-child:after {border-bottom: 0;}
  .bid-con h3 {font-size: .15rem; color:#333;}
  .bid-con h3 img {width:.35rem;vertical-align: -.02rem;}
  .bid-info { padding-top: .15rem; overflow:auto;}
  .apr-part .apr {font-size: .31rem; font-family: Arial; margin:-.05rem 0 .03rem 0; line-height:1;}
  .apr-part p {color:#666;}
  .apr-part .apr b {font-size: .16rem;}
  .circle-area {position: absolute; right: .15rem; top: .3rem;}
  .time-part {font-size: .14rem; color: #666;}
  .time-part p:first-child {margin-bottom: .1rem;}
  .time-part p b.bondApr{color: #48affb}
  .apr-part {  width: 1.8rem;  }
  
  /*保险项目*/
  .strong{font-size:.22rem; color:#333;}
  .strong i{font-size:.16rem;}
  .time-part p.mar-dx{margin-bottom:.03rem;}
  .service-charge{color:#fbca15; font-size:.12rem; display:inline-block; border:1px solid #fbca15; border-radius:.05rem; padding:0 .05rem;
    height:.20rem; text-align:center; line-height:.20rem; position:relative; top:.05rem;}
</style>