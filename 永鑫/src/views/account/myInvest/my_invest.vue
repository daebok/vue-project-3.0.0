<template>
  <div class="page" id="pageDetail">
    <div class="page-loadmore-wrapper site-list" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">
        <!-- <section v-for="item in list" @click="linkTo('siteIntroDetail', item.uuid)" class="invest-am-list"> -->
        <section v-for="item in list" @click="Redeem(item.tradeAcco,item.fundCode,item.tradeId,item.wayStatus,item.businessType)" class="invest-am-list">
          <a>
            <div class="invest-am-title">
              <p class="am-title">
                {{ item.name }} {{ item.code }}
                <!-- 赎回状态 -->
                <i v-if="item.wayStatus == 1" class="am-status color-disabled">待确认</i>
                <i v-else-if="item.wayStatus == 0 && item.businessType == '申购'" class="am-status ">赎回</i>
                <i v-else="item.wayStatus == 0 && item.businessType == '赎回'" class="am-status color-disabled">确认成功</i>
              </p>
              <ul class="invest-am-content">
                <li class="invest-am-content-left margin-t-10">交易时间</li>
                <li class="invest-am-content-right margin-t-10">{{ item.buyDate }}</li>
                <li v-if="item.businessType == '申购'" class="invest-am-content-left margin-b-10">购买金额</li>
                <li v-else="item.businessType == '赎回'" class="invest-am-content-left margin-b-10">赎回份额</li>
                <li v-if="item.businessType == '申购'" class="invest-am-content-right margin-b-10">{{ item.money }}元</li>
                <li v-else="item.businessType == '赎回'" class="invest-am-content-right margin-b-10">{{ item.money }}份</li>
              </ul>
            </div>
          </a>
        </section>
      </mt-loadmore>
      <div class="text-center no-data" v-show="noData">
        <img src="../../../assets/images/public/default/default_icon_no_record.png" alt="缺省图片">
        <p>暂无记录</p>
      </div>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  export default {
    name: 'myInvest',
    data() {
      return {
        wrapperHeight: 0,
        noData: false,
        list: [],
        allLoaded: false,
        getParams: {
          //'page.page': 1,
          //'page.pageSize': 10
          //userId: this.$store.state.user.userId,//用户登录信息
          //__sid: this.$store.state.user.__sid
          openId: this.$route.query.openId//微信openId
        }
        //传给接口数据
        // params: {
        //   fundCode: '001909',//产品id
        //   userId: this.$store.state.user.userId,//用户登录信息
        //   __sid: this.$store.state.user.__sid
        // }
      }
    },
    created() {
      //console.log(this.getParams.openId);
      this.projectList();
      this.$nextTick(()=> {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    methods: {
      //接口请求
      projectList(type) {
        this.$http.get(ajaxUrl.getPositionList, { params: this.getParams }).then((res) => {
          if(res.data.resMsg == '请先注册后再进入'){
            //未注册进入注册页面
            //this.$router.push({name:'register', params: {openId: this.params.openId}})
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去注册',
              showCancelButton: false,
              message: '请先注册后再进入'
            }).then(res => {
              this.$router.push({name:'register', params: {openId: this.getParams.openId}})
              //进入注册
            });
          }else{
            if (res.data.resData) {
              if(res.data.resData.respFail){//查询持仓记录失败
                this.$messagebox({
                  title: ' ',
                  confirmButtonText: '确定',
                  showCancelButton: false,
                  message: res.data.resData.respFail
                }).then(res => {
                  window.location.href = 'http://weixin.360mili.com/usercenter';
                  //跳转永鑫个人中心
                });
              }else{
                if (res.data.resData.CjhxTradingRecordList.length <= 0) { // 无数据
                  this.noData = true;
                  return false;
                }else{
                  //回获取列表信息
                  this.list = this.list.concat(res.data.resData.CjhxTradingRecordList);
                }
              }
            }
          }
        })
      },
      // linkTo(name,uuid) {
      //   this.$router.push({ name: name, params: { uuid: uuid }});
      // },
      Redeem(tradeAcco,fundCode,tradeId,wayStatus,businessType){
        //console.log('进来了');
        //let x = 'something';
        //赎回请求
        if(wayStatus == 0 && businessType == '申购'){
          let params = {
            //idcard: idcard, 
            //phone: phone, 
            //name: name, 
            tradeAcco: tradeAcco, 
            fundCode: fundCode, 
            tradeId: tradeId,
            openId: this.$route.query.openId//微信openId
            //userId: this.$store.state.user.userId,//用户登录信息
            //__sid: this.$store.state.user.__sid
          }
          this.$http.get(ajaxUrl.projectRedeemAjax, { params: params }).then((res) => {
            document.write(res.data);
          })
        }
      },
      loadTop(id) {
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          this.list = [];
          this.allLoaded = false;
          //this.getParams['page.page'] = 1;
          this.projectList('reload');
        },1000)
      },
      loadBottom(id) {
        setTimeout(() => {
          this.getParams['page.page']++;
          this.$refs.loadmore.onBottomLoaded(id);
          this.projectList('loadMore');
        }, 500);
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../../assets/scss/var.scss";
  .current {color: $main-color;border-bottom: 2px solid $main-color; }
  .tab-title {
    width: 100%;
    height: .4rem;
    background: #fff;
  }
  .tab-title li{
    width: 33.33%;
    line-height: .36rem;
    text-align: center;
    display: inline-block;
    float: left;
  }
  .tab-title li span{
    width: .6rem;
    display: block;
    margin: 0 auto;
  }
  .invest-am-list {
    padding-left:.15rem;
    background: #fff;
    margin-bottom: .05rem;
    float: left;
    width: 100%;
  }
  .invest-am-title {
    // border-bottom: 1px solid #DEDEDE;
  }
  .am-title {
    height: .42rem;
    line-height: .42rem;
    font-size: .15rem;
    color:#333;
    border-bottom:1px solid #ddd;
    padding-right:.15rem;
  }
  .invest-am-icon {
    background: #4abe89;
    color: #fff;
    padding: 1px;
    margin-left: .1rem;
    font-size: .12rem;
    border-radius: .02rem;
  }
  .am-status {
    float: right;
    font-size: .14rem;
    font-family: arial;
    color: #fff;
    background-color:#fbca15;
    width:.70rem;
    height:.27rem;
    text-align:center;
    border-radius:.20rem;
    margin-top:.06rem;
    line-height:.27rem;
  }
  .color-disabled{
    background-color:#ccc;
  }
  .invest-am-content li {
    width: 49%;
    height: .28rem;
    line-height: .28rem;
    display: inline-block;
  }
  .invest-am-content-left {
    color: #666;
  }
  .invest-am-content-right {
    text-align: right;
    color: #333;
    font-family: arial;
    padding-right:.15rem;
  }
  .invest-am-bottom {
    border-top: 1px solid #DEDEDE;
  }
  .count-time {
    float: left;
    line-height: .5rem;
    color: #666;
    font-size: .12rem;
  }
  .am-bottom-left, .am-bottom-right {
    line-height: .5rem;
    color: #666;
  }
  .surplus-time {
    float: left;
    line-height: .5rem;
    font-size: .12rem;
  }
  .time-show {
    color: #F95A28;
    margin-left: .1rem;
  }
  .plan-btn, .download-btn {
    height: .3rem;
    margin: .1rem 0;
    display: block;
    float: right;
    text-align: center;
    width: .75rem;
    border-radius: .05rem;
    background: #fff;
    font-size: .14rem;
    padding: 0;
    box-shadow: none;
  }
  .plan-btn {
    border: 1px solid #F95A28;
    color: #F95A28;
  }
  .download-btn {
    border: 1px solid #999;
    color: #666;
    margin-left: .1rem;
  }
  .repayment-time {
    margin-bottom: .1rem;
    color: #666;
  }
  .mint-button:not(.is-disabled):active::after {opacity: 0;}
</style>