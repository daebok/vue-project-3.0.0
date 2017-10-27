<template>
  <div>
    <div class="money-box">
      <h3>粮票宝余额</h3>
      <p ><span class="font-arial">{{resdata.fundMoney | currency('',2)}}</span>元</p>
    </div>
    <div class="form-invest">
      <div class="group-head aui-border-b">转出至钱包余额</div>
      <div class="form-list">
        <label>转出金额(元)</label>
        <input ref="inputMoney" type="tel" pattern="\d*" v-model.trim="investMoney" class="invest-money" id="invest-money" placeholder="请输入金额" @input="inputNum" />
      </div>
      <div class="group-head margin-t-10 aui-border-b">转出方式
        <router-link :to="{ name: 'fundOutRule' }">
          <span>转出规则</span>
        </router-link>
      </div>
      <ul class="form-group-select">
        <li class="pay-style aui-border-b">
          <label>
            <input type="radio" class="checkbox hide" value="2" name="payout" v-model="payout" />
            <em></em>
            快速转出 <i class="color-999">当日到账 当日无收益</i>
          </label>
        </li>
        <li class="pay-style">
          <label>
            <input type="radio" class="checkbox hide" value="1" name="payout" v-model="payout" />
            <em></em>
            普通转出 <i class="color-999">下个工作日到账 当日有收益</i></label>
        </li>
      </ul>
      <div class="operator">
        <mt-button v-show="!investMoney" size="large" type="default" disabled>确认转出</mt-button>
        <mt-button v-show="investMoney" size="large" type="danger" @click.prevent="openPwd" :disabled="resdata == ''">确认转出
        </mt-button>
      </div>
    </div>
    <pay-pwd @submit="submitForm" ref="pwd" v-model="payPwd" :tipMoney="investMoney" tipTxt="转出金额"></pay-pwd>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config'
  import Loading from '../../components/Loading.vue'
  import PayPwd from '../../components/PayPwd.vue'
  import base64code from '../../mixins/base64code.js'; // 引入密码加密插件
  import qs from 'qs'
  export default {
    name: 'fundOut',
    data(){
      return {
        investMoney: '',
        resdata: '',
        payout: sessionStorage.payout || 1,
        payPwd: '',
        urlParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      }
    },
    created(){
      this.$http.get(ajaxUrl.redeemPage, {params:this.urlParams}).then((res) => {
        this.resdata = res.data.resData
      })
    },
    watch: {
      investMoney (newV, oldV) {
        newV = parseFloat(newV);
        if(newV){
          if(newV == 0){ this.investMoney = ''}
          if(newV > this.resdata.fundMoney){
            this.$toast('转出金额不能大于'+ this.resdata.fundMoney +'元')
            this.investMoney = ''
          }
          sessionStorage.investMoney = newV
        }else{
          sessionStorage.investMoney = ''
        }
      },
      payout (newV, oldV) {
        sessionStorage.payout = newV
      }
    },
    mixins: [ base64code],
    methods: {
      inputNum(evt){
        let val = evt.target.value
        this.investMoney = val.replace(/\D*$/, '')
      },
      submitForm(){
        let amount = this.investMoney
        if( amount < this.resdata.lowestAccount) { //小于起投金额
          if (this.resdata.remainAccount >= this.resdata.lowestAccount) { // 当剩余可投金额大于等于最小可投金额时判断
            let str = '最低起投金额为' + this.resdata.lowestAccount + '元'
            this.$toast(str)
            return;
          }
        }
        // 用实际投资金额判断
        if(this.investMoney > this.resdata.useMoney){
          let str = '您的可用余额不足'
          return ;
        }
        let params = {
          money: amount,
          payPwd: this.base64Encode(this.payPwd),
          type: this.payout.toString(),
          redeemToken: this.resdata.redeemToken,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
        }
        this.$indicator.open({ spinnerType: 'fading-circle' });
        this.$http.post(ajaxUrl.redeem, qs.stringify(params)).then((res) => {
          if(res.data.resData == ''){ //主要是有请勿重复提交信息提示
            this.$refs.pwd.close() // 关闭密码框
            this.$toast(res.data.resMsg);
            /*this.$messagebox({
              title: ' ',
              confirmButtonText: '确认',
              showCancelButton: false,
              message: res.data.resMsg
            }).then(action => {
              if(action == 'confirm'){
              location.reload();
            }
          })*/
          }else{
            sessionStorage.investMoney = ''
            sessionStorage.fundOut_resdata = JSON.stringify(res.data.resData)
            this.$router.push('/fund/msg?from=out')
          }
        })
      },
      openPwd() {
        if(this.investMoney > this.resdata.fundMoney){
          this.$toast('转出金额不能大于粮票宝余额')
          return ;
        }
        this.$refs.pwd.show() // 打开支付密码区
      },
    },
    components: { Loading,PayPwd }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var";
  .money-box {
    background: $main-color url('../../assets/images/bg.png') no-repeat;
    background-size: 100% ;
    color: #fff;
    padding: .15rem;
    margin-bottom: .12rem;
    font-size: .16rem;
    h3 { line-height: 1; margin-bottom: .15rem;}
  }
  .disabled {color:#999;}
  .form-group-select li.disabled span {color:#999;}
  .form-group {
    line-height: 1;
    float: left;
    width: 100%;
    padding: 0 5%;
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
    font-family: arial;
  }
  .form-list {
    float: left;
    width: 100%;
    background: #fff;
    padding: 0 5%;
    height: .44rem;
    line-height: .44rem;
    label {
      display: inline-block;
      float: left;
      width: 30%;
    }
    input{
      width: 70%;
      border: none;
      outline: none;
      float: left;
      height: .3rem;
      margin-top: .07rem;
    }
  }
  .earnings{
    margin: .1rem 0;
    p {color:#666;margin-top:.05rem;margin-left:30%;float:left;}
  }
  .group-head {
    line-height: .48rem; width: 100%; float: left;
    background: #fff;
    padding-left: .15rem; color: #666;
    span {
      margin-right: .15rem;
      float: right;
      color: #67748d;
    }
  }
  .form-group-select {
    width: 100%;
    padding-left: .15rem;
    background: #fff;
    float: left;
    line-height: .48rem;
    li {
      position: relative;
      color: #333;
      padding-right: .15rem;
      &.pay-style {
        em {
          width: .15rem;
          height: .15rem;
          background:url('../../assets/images/fund/btn_check_01.png') no-repeat;
          background-size: contain;
          display: inline-block;
          vertical-align: middle;
          margin-right: .06rem;
        }
        .checkbox:checked + em {background-image: url('../../assets/images/fund/btn_check_02.png')}
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
    right: .15rem;
    top: .17rem;
    height: .14rem;
    width: .14rem;
  }
  .operator{
    float: left;
    width: 100%;
    padding:  .15rem;
  }
  .tips{
    float: left;
    width: 100%;
    padding: 0 5%;
    font-size: .12rem;
    color: #999;
    line-height: .2rem;
  }
</style>
