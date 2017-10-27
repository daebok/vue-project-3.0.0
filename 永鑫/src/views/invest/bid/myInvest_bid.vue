<template>
  <div class="payment-page" >
    <div class="form-invest">
      <div class="count-time">
        <span>
          <p>剩余时间</p>
          <h3 class="font-arial">{{countTime}}</h3>
        </span>
      </div>
      <div class="pay-money aui-border-t">支付金额 <span class="pull-right">￥{{investMoney | currency('', 2)}}</span></div>
      <div class="group-head margin-t-10 aui-border-b">支付方式</div>
      <ul class="form-group-select">
        <li class="pay-style aui-border-b">
          <label>
            <input type="radio" class="checkbox hide" value="1" name="bankPay" v-model="bankPay" />
            <em></em>
            <img class="bank-logo" :src="userBank.picPath">{{userBank.bankName}} <i class="color-999">{{userBank.hideBankNo}}</i>
          </label>
          <span class="tip-color" @click="readLimit">查看限额</span>
        </li>
        <li class="pay-style aui-border-b">
          <label>
            <input type="radio" class="checkbox hide" value="2" name="bankPay" v-model="bankPay" />
            <em></em>
            粮票宝</label>
          <span>{{ resdata.moneyFundMoney | currency('',2)}}元</span>
        </li>
        <li class="pay-style">
          <label>
            <input type="radio" class="checkbox hide" value="0" name="bankPay" v-model="bankPay" />
            <em></em>
            钱包余额</label>
          <span>{{ resdata.useMoney | currency('',2)}}元</span>
        </li>
      </ul>
      <div class="operator">
        <mt-button  size="large" type="danger" @click.prevent="openPwd" :disabled="resdata == ''">确认支付
          <span class="font-arial">{{investMoney | currency('', 2)}}</span>元
        </mt-button>
      </div>
    </div>
    <pay-pwd @submit="submitForm" ref="pwd" v-model="payPwd" :tipMoney="investMoney"></pay-pwd>
    <div class="protocol-con">
      <mt-popup v-model="popupVisible" position="bottom" class="mint-popup-3" :modal="false">
        <mt-header class="bar-nav" :title="protocolTitle" >
          <mt-button slot="right" @click.native="popupVisible = false">关闭</mt-button>
        </mt-header>
        <div class="content" v-html="protocolHtml"></div>
      </mt-popup>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import PayPwd from '../../../components/PayPwd.vue'
  import CountDown from '../../../components/myInvest_countTime.vue'
  import base64code from '../../../mixins/base64code.js'; // 引入密码加密插件
  import qs from 'qs'
  export default {
    name: 'myInvestBid',
    data(){
      return {
        investMoney: '',
        userBank: '',
        resdata: '',
        countTime: '',
        bankPay: 1,
        times2: false,
        payPwd: '',
        urlParams: {
          uuid: this.$route.params.uuid,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        popupVisible: false,
        protocolTitle: '',
        protocolHtml: ''
      }
    },
    created(){
      this.$http.get(ajaxUrl.toPay, {params:this.urlParams}).then((res) => {
        this.resdata = res.data.resData
        this.userBank = res.data.resData.userBank
        this.investMoney = res.data.resData.invest.realAmount // 投资有效金额
        let remainTimes = res.data.resData.invest.remainTimes // 投资剩余时间
        this.countDown(remainTimes)
        if(this.resdata == ''){
          this.$toast(res.data.resMsg)
        }
      })
    },
    mixins: [ base64code],
    methods: {
      countDown (remainTimes) {
        var sys_sec = Number(remainTimes)
        if(sys_sec > 0){
          clearInterval(this.interval)
          this.interval = setInterval(() => {
            if(sys_sec > 1){
              sys_sec -= 1
              let min = Math.floor(sys_sec/60)
              let sec = Math.floor(sys_sec%60)
              let showMin = min<10?'0'+min : min  //计算分钟
              let showSec = sec<10?'0'+sec : sec  //计算秒数
              this.countTime = showMin + ':' + showSec
            }else{
              clearInterval(this.interval)
              window.location.reload()
            }
          }, 1000)
        }
      },
      // 点击返回
      goBack() {
        sessionStorage.investMoney = ''
        this.$router.go(-1)
      },
      openPwd() {
        let amount = this.investMoney
        if( amount < this.resdata.invest.lowestAccount) { //小于起投金额
          if (this.resdata.invest.remainAccount >= this.resdata.invest.lowestAccount) { // 当剩余可投金额大于等于最小可投金额时判断
            let str = '最低起投金额为' + this.resdata.lowestAccount + '元'
            this.$toast(str)
            return;
          }
        }
        if(this.resdata.invest.stepAccount){ // 判断是否有递增金额 金额必须为起投金额+递增金额的整数倍
          var times = (this.investMoney - this.resdata.invest.lowestAccount)%this.resdata.stepAccount
          if(times > 0){
            this.$toast('金额必须为起投金额+递增金额的整数倍')
            return
          }
        }
        // 钱包余额小于投资金额
        if((this.bankPay == 0) && (this.nvestMoney > this.resdata.userMoney)){
          let str = '钱包余额小于投资金额，请先充值再进行投资。'
          this.$messagebox({
            title: ' ',
            cancelButtonText: '再看看',
            confirmButtonText: '去充值',
            showCancelButton: true,
            message: str
          }).then(action => {
            if(action == 'confirm'){
              this.$router.push('/account/recharge')
            }
          })
          return ;
        }
        // 粮票宝小于投资金额
        if((this.bankPay == 2) && (this.nvestMoney > this.resdata.moneyFundMoney)){
          let str = '钱包余额小于粮票宝，请先转入再进行投资。'
          this.$messagebox({
            title: ' ',
            cancelButtonText: '再看看',
            confirmButtonText: '去转入',
            showCancelButton: true,
            message: str
          }).then(action => {
            if(action == 'confirm'){
              this.$router.push('/fund/in')
            }
          })
          return ;
        }
        this.$refs.pwd.show() // 打开支付密码区
      },
      submitForm(){
        let params = {
          payPwd: this.base64Encode(this.payPwd),
          payStyle: this.bankPay,
          realAmount: this.investMoney,
          payToken: this.resdata.payToken,
          investOrderNo: this.resdata.invest.investOrderNo,
          upApr: this.resdata.invest.upApr,
          projectId: this.resdata.invest.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
        }
        this.$http.post(ajaxUrl.doPay, qs.stringify(params)).then((res) => {
          if(res.data.resData == ''){ //主要是有请勿重复提交信息提示
            this.$refs.pwd.close() // 关闭支付密码区
            this.$toast(res.data.resMsg)
          } else {
            sessionStorage.investMoney = ''
            sessionStorage.invest_resdata = JSON.stringify(res.data.resData)
            this.$router.push({name: 'tenderResult'})
          }
        })
      },
      readLimit(){
        this.popupVisible = true
        this.$http.get(ajaxUrl.top5Help,{ params: {sectionCode: 'bankDeclare'}}).then((res) => {
          this.protocolHtml = res.data.resData.articleList[0].content;
          this.protocolTitle = res.data.resData.articleList[0].title
        })
      }
    },
    components: { PayPwd, CountDown }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../../assets/scss/var";
  .count-time {
    display: table;
    background: #fff;
    text-align: center;
    width: 100%;
    height: 1.15rem;
    span {
      display: table-cell;
      vertical-align: middle;
      p { color: #666;font-size: .13rem;}
      h3 {
        color: #333;
        font-size: .26rem;
      }
    }
  }
  .pay-money {
    background: #fff;
    line-height: .44rem;
    font-size: .15rem;
    color: #333;
    padding: 0 .15rem;
    span {
      font-size: .17rem;
      color: $main-color;
    }
  }
  .disabled {color:#999;}
  .form-group-select li.disabled span {color:#999;}
  .group-head {
    line-height: .48rem; width: 100%; float: left;
    background: #fff;
    padding-left: .15rem; color: #666;
  }
  .form-group{
    line-height: 1;
    float: left;
    width: 100%;
    padding: 0 .15rem;
  }
  .form-group label{
    color: #666;
    display: inline-block;
    width: 30%;
    float: left;
  }
  .form-group span{
    display: inline-block;
    float: left;
    width: 70%;
    font-family: sarial;
  }
  .form-list{
    float: left;
    width: 100%;
    background: #fff;
    padding: 0 5%;
    height: .44rem;
    line-height: .44rem;
  }
  .form-list label{
    display: inline-block;
    float: left;
    width: 30%;
  }
  .form-list input{
    width: 70%;
    border: none;
    outline: none;
    float: left;
    height: .3rem;
    /*line-height: .3rem;*/
    margin-top: .07rem;
  }
  .earnings{
    margin: .1rem 0;
  }
  .earnings p {color:#666;margin-top:.05rem;margin-left:30%;float:left;}
  .form-group-select{
    width: 100%;
    padding: 0 5%;
    background: #fff;
    float: left;
    line-height: .48rem;
    li {
      position: relative;
      color: #333;
      padding-right: .15rem;
      &.pay-style {
        padding-right: 0;
        em {
          width: .15rem;
          height: .15rem;
          background:url('../../../assets/images/fund/btn_check_01.png') no-repeat;
          background-size: contain;
          display: inline-block;
          vertical-align: middle;
          margin-right: .06rem;
        }
        .checkbox:checked + em {
          background-image: url('../../../assets/images/fund/btn_check_02.png')
        }
      }
      .bank-logo {
        width: .24rem;
        vertical-align: middle;
      }
      span {
        float: right;
        font-family: arial;
        color: #666;
      }
    }
  }
  .right-go{
    position: absolute;
    right: 0;
    top: .17rem;
    height: .14rem;
    width: .14rem;
  }
  .operator{
    float: left;
    width: 100%;
    padding: .15rem;
  }
  .tips{
    float: left;
    width: 100%;
    padding: 0 .15rem;
    font-size: .12rem;
    color: #999;
    line-height: .2rem;
    img {width: .13rem;  vertical-align: -2px;}
    .allIn{
      color: #F95A28;
      display: inline-block;
      width: 100%;
      margin-bottom: 0.1rem;
    }
  }
</style>
