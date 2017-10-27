<template>
  <div class="page" id="result">
    <div class="modal" v-if="modal">
      <div class="alert bounceIn animated">
        <div class="vip">{{vipLevel}}</div>
        <router-link to="/account/my_coupon?from=msg_result">
          <div><img class="btn" src="../assets/images/finance/btn.png"></div>
        </router-link>
        <div class="close" @click="modal = false">
          <div class="line"></div>
        </div>
      </div>
    </div>
    <mt-header class="bar-nav" :title="title">
      <router-link :to="actSkip" slot="right">
        <mt-button>完成</mt-button>
      </router-link>
    </mt-header>
    <!--实名认证结果-->
    <div v-if="$route.query.code=='register_success'" class="box-msg">
      <div class="result-img">
        <img src="../assets/images/me/tips_escrow_account.png" />
      </div>
      <div class="msg-text text-center">开通托管账户成功！</div>
      <div class="msg-text text-center color-666">离赚钱还差一步之遥！去评估风险承受能力</div>
      <div class="result-btn">
        <router-link to="/mine/safe/risk_tips">
          <span class="result-left-btn">立即评估</span>
        </router-link>
        <router-link :to="skipUrl">
          <span class="result-right-btn">{{investBtn}}</span>
        </router-link>
      </div>
    </div>
    <!--授权结果-->
    <div v-if="$route.query.code=='auth_success'" class="box-msg">
      <div class="result-img">
        <img src="../assets/images/public/business_trilateral.png" />
      </div>
      <div class="msg-text text-center">您的业务授权请求第三方已受理，结果可在账户与安全>业务授权处查看</div>
    </div>
    <!--投资结果-->
    <div class="result" v-if="$route.query.code == 'invest_success' || $route.query.code == 'bond_success' || $route.query.code == 'realize_success' ">
      <div class="result-img">
        <img src="../assets/images/finance/tips_invest_result.png" />
      </div>
      <div class="result-msg">您的支付请求第三方已受理，结果可在资产>我的投资中查看</div>
      <div class="result-borrow">
        <p class="borrow-name">{{bidName}}</p>
        <div class="borrow-show">
          <span class="borrow-account-title">投资金额(元)：</span>
          <span class="borrow-account">{{bidMoney | currency('', 2)}}</span>
        </div>
      </div>
      <div class="result-btn">
        <router-link to="/invest">
          <span class="result-left-btn">其他产品</span>
        </router-link>
        <router-link to="/account/myInvest?tab=tab-con2&from=msg_result">
          <span class="result-right-btn">投资记录</span>
        </router-link>
      </div>
    </div>
    <!--充值结果-->
    <div v-if="$route.query.code=='recharge_success'" class="box-msg">
      <div class="result-img">
        <img src="../assets/images/public/recharge_trilateral.png" />
      </div>
      <div class="msg-text text-center color-666">您的充值请求第三方已受理，结果可在充值>记录页查看！</div>
      <div class="result-btn text-center">
        <router-link :to="skipUrl">
          <span class="one-btn">{{investBtn}}</span>
        </router-link>
      </div>
    </div>
    <!--提现结果-->
    <div v-if="$route.query.code=='cash_success'" class="box-msg">
      <div class="result-img">
        <img src="../assets/images/public/withdrawals_trilateral.png" />
      </div>
      <div class="msg-text text-center color-666">
        您的提现请求第三方已受理，结果可在提现>记录页查看！
      </div>
    </div>
    <!--绑卡成功-->
    <div v-if="$route.query.code=='bindBank_success'" class="box-msg">
      <div class="result-img">
        <img src="../assets/images/public/bankcard_success.png" />
      </div>
      <div class="msg-text text-center color-666">
        您的绑卡请求第三方已受理，结果可在账户与安全>银行卡页查看！
      </div>
    </div>
    <!--解绑成功-->
    <div v-if="$route.query.code=='delBindBank_success'" class="box-msg">
      <div class="result-img">
        <img src="../assets/images/public/bankcard_success.png" />
      </div>
      <div class="msg-text text-center color-666">
        您的解绑请求第三方已受理，结果可在账户与安全>银行卡页查看！
      </div>
    </div>

    <!--签约成功-->
    <div v-if="$route.query.code=='signCode_success'" class="box-msg">
      <div class="result-img">
        <img src="../assets/images/me/tips_escrow_account.png" />
      </div>
      <div class="msg-text text-center color-666">
        您的签约请求第三方已受理，结果可在账户与安全页查看！
      </div>
    </div>

  </div>
</template>
<script>
  import * as ajaxUrl from '../ajax.config'
  export default {
    data(){
      return {
        title: '投资结果',
        bidName: '',
        bidMoney: 0,
        investBtn: '开始投资',
        skipUrl: '/index',
        actSkip: '/index',
        modal: false,
        vipLevel: ''
      }
    },
    created(){
      this.getVipLevel(this.$store.state.user.__sid)
      // title变化
      switch (this.$route.query.code) { //返回详情
        case 'register_success':
          this.title = '托管账户'
          this.actSkip = '/mine/safe?from=msg_result'
          break;
        case 'auth_success':
          this.title = '业务授权结果';
          this.actSkip = '/mine/safe?from=msg_result'
          break;
        case 'bindBank_success':
          this.title = '绑卡结果';
          this.actSkip = '/mine/safe?from=msg_result'
          break;
        case 'delBindBank_success':
          this.title = '解绑结果';
          this.actSkip = '/mine/safe?from=msg_result'
          break;
        case 'recharge_success':
          this.title = '充值结果';
          this.actSkip = '/account/recharge?from=msg_result'
          break;
        case 'cash_success':
          this.title = '提现结果';
          this.actSkip = '/account/withdraw?from=msg_result'
          break;
        //签约
        case 'signCode_success':
          this.title = '签约结果';
          this.actSkip = '/mine/safe?from=msg_result'
          break;
      }
      // 实名成功后的跳转投资
      if(sessionStorage.from_id){
        var urlArr = sessionStorage.from_id.split(',')
        this.skipUrl = this.actSkip = '/'+urlArr[0]+'/' + urlArr[1] + '?from=msg_result';
        this.investBtn = '继续投资'
        delete sessionStorage.name_id
      }
      // 实名成功后跳转资产账户
      if(sessionStorage.from_account){
        this.skipUrl = this.actSkip = '/account';
        delete sessionStorage.from_account
      }
      // 授权后的跳转资产账户
      if(sessionStorage.from_account_auth){
        this.skipUrl = this.actSkip = '/account';
        delete sessionStorage.from_account_auth
      }
      // 充值成功后跳转投资页面
      if(sessionStorage.from_investBid_id){
        var id = sessionStorage.from_investBid_id
        this.skipUrl= this.actSkip = '/investDetail/' + id + '/investBid?from=msg_result';
        this.investBtn = '继续投资'
        delete sessionStorage.from_investId_id
      }
      // 充值成功后跳转投资页面
      if(sessionStorage.from_realizeBid_id){
        var id = sessionStorage.from_realizeBid_id
        this.skipUrl= this.actSkip = '/investDetail/' + id + '/investBid?prevPage=realizeDetail&from=msg_result';
        this.investBtn = '继续投资'
        delete sessionStorage.from_realizeBid_id
      }
      // 充值成功后跳转投资转让页面
      if(sessionStorage.from_bondBid_id){
        var id = sessionStorage.from_bondBid_id
        this.skipUrl = this.actSkip = '/bondDetail/' + id + '/bondBid?from=msg_result';
        this.investBtn = '继续转入'
        delete sessionStorage.from_bondBid_id
      }
      // 投资成功后的信息获取
      this.bidName = sessionStorage.investBidName || ''
      this.bidMoney = sessionStorage.investBidMoney || ''
      // 投资成功后跳转详情
      if(sessionStorage.investBidId){
        let investId = sessionStorage.investBidId   // 怕investId失效丢失
        switch (this.$route.query.code) { //返回详情
          case 'invest_success':
            this.actSkip = '/investDetail/' + investId + '?from=msg_result';
            break;
          case 'realize_success':
            this.actSkip = '/realizeDetail/' + investId + '?from=msg_result';
            break;
          case 'bond_success':
            this.actSkip = '/bondDetail/' + investId + '?from=msg_result';
            break;
        }
      }
      if(this.$route.query.code == 'delBindBank_success'){
        this.$router.replace('/mine/safe/bank?from=msg_result')
      }
    },
    methods: {
      getVipLevel(sid) {
        this.$http.get(ajaxUrl.vip, {params: { __sid: sid }}).then((res) => {
          this.vipLevel = res.data.resData.userVipLevel;
          let localVipLevel = sessionStorage.vipLevel
          let code = this.$route.query.code
          if(localVipLevel && localVipLevel != this.vipLevel && code == 'invest_success') {
            this.modal = true
          }
        })
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../assets/css/animate.min.css';
  @import '../assets/scss/var.scss';
  .bar-nav h1 {color:#000;}
  .page {background:#fff;}
  .box-msg {  padding: 0 10%;  }
  .msg-text { line-height: .24rem; }
  .one-btn {
    border: 1px solid $main-color;
    color: $main-color;
    border-radius: .03rem;
    display: inline-block;
    padding:.1rem .3rem;
  }
  .result-img{  text-align: center;  margin-top: 20%;  }
  .result-img img{
    width: .9rem;
    height: .9rem;
    margin: 0 auto;
  }
  .result-msg{
    width: 80%;
    margin-left: 10%;
    font-size: .16rem;
    color: #333;
    margin-bottom: .5rem;
    line-height:.2rem;
  }
  .result-borrow{
    width: 100%;
    padding: 0 5%;
  }
  .borrow-name{
    font-size: .16rem;
    color: #999;
    line-height: .3rem;
  }
  .borrow-show{
    width: 100%;
    height: .3rem;
    line-height: .3rem;
    color: #999;
  }
  .borrow-account-title{
    float: left;
    text-align: left;
  }
  .borrow-account{
    float: right;
    text-align: right;
  }
  .result-btn{  margin-top: .3rem;  }
  .result-left-btn,.result-right-btn{
    width: 40%;
    float: left;
    height: .36rem;
    display: inline-block;
    text-align: center;
    line-height: .36rem;
  }
  .result-left-btn{
    border: 1px solid $main-color;
    color: $main-color;
    margin-left: 5%;
    margin-right: 10%;
    border-radius: .03rem;
  }
  .result-right-btn{
    border: 1px solid #DDD;
    color: #666;
    border-radius: .03rem;
  }
  .modal {
    position: absolute;
    width: 100%;
    height: 100%;
    z-index: 3;
    background: rgba(0, 0, 0, .35);
    .alert {
      position: absolute;
      left: 8%;
      top: 45%;
      width: 84%;
      height: 3.4rem;
      padding-top: 1.9rem;
      margin-top: -1.7rem;
      text-align: center;
      background: url(../assets/images/finance/bg.png) center/100% auto no-repeat;
      .vip {
        line-height: .8rem;
        color: #fff;
        font-size: .18rem;
        background: url(../assets/images/finance/vip.png) center/.6rem auto no-repeat;
      }
      .btn {
        width: auto; height: .45rem; margin-top: .1rem;
      }
      .close {
        display: inline-block;
        height: .8rem;
        width: 1rem;
        margin-top: .06rem;
        background: url(../assets/images/finance/close1.png) center bottom/.29rem auto no-repeat;
        .line {
          display: inline-block;
          width: .01rem;
          background: #dd0f22;
          height: .5rem;
        }
      }
    }
  }
</style>
