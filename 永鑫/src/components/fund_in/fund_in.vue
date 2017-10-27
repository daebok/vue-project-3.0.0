<!-- 粮票宝转入 by fenglei -->
<template>
  <div class="page">
    <div class="form-invest">
      <p class="head-p">转入粮票宝</p>
      <div class="form-list">
        <label>转入金额(元)</label>
        <input ref="inputMoney" type="tel" pattern="\d*" v-model.trim="inputMoney" class="invest-money" id="invest-money" placeholder="请输入金额" @input="inputNum">
      </div>
      <div class="form-group margin-b-20 earnings">
        <label>预期收益</label>
        <span class="main-color">{{ interest | currency('',2) }}元</span>
      </div>
      <div class="group-head margin-t-10 aui-border-b">支付方式</div>
      <ul class="form-group-select">
        <li class="pay-style aui-border-b">
          <label>
            <input type="radio" class="checkbox hide" value="2" v-model="bankPay">
            <em></em>
            <img class="bank-logo" :src="resdata.picPath">{{ resdata.bankName }} <i class="color-999">{{ resdata.hideBankNo }}</i>
          </label>
          <span class="tip-color" @click="readProtocol('bankDeclare','top5Help')">查看限额</span>
        </li>
        <li class="pay-style">
          <label>
            <input type="radio" class="checkbox hide" value="1" name="bankPay" v-model="bankPay">
            <em></em>
            钱包余额</label>
          <span>{{ resdata.useMoney | currency('',2) }}元</span>
        </li>
      </ul>
      <p class="interest-tip">预计{{ resdata.preProfitTime }}产生收益，{{ resdata.preInterestTime }}收益到账</p>
      <div class="operator">
        <mt-button v-if="inputMoney && switch_status" size="large" type="danger" @click.prevent="openPwd">确认转入</mt-button>
        <mt-button v-else size="large" type="default" disabled>确认转入</mt-button>
      </div>
      <p class="tips">
        <span @click="switch_status=!switch_status;">
            <span v-if="switch_status">
              <img src="../../assets/images/public/xieyi_check_02.png" />
            </span>
            <span v-else>
              <img  src="../../assets/images/public/xieyi_check_01.png" />
            </span>
        </span>
        <span class="protocol">同意
          <i v-for="item in resdata.proList" @click="readProtocol(item.id, 'registerProtocolDetail')">《{{ item.name }}》</i>
        </span><br/>
      </p>
    </div>
    <div class="form-prompt">
      <p class="prompt-title margin-b-10">温馨提示：</p>
      <p>根据您阅读并同意的货基申购相关协议内容自动为您申购嘉实活期宝。本产品由嘉实基金管理公司发行和管理，中国光大银行不对其承担产品的投资，兑付和风险责任</p>
    </div>
    <transition name="slide-bottom">
      <protocol v-show="protocolShow" ref="procl"></protocol>
    </transition>
    <pay-pwd v-show="payPwdShow" ref="pwd" :flag="payPwdShow" :tipMoney="inputMoney" @submit="submitPay" @forget="forgetPayPwd"></pay-pwd>
    <get-pay-pwd v-show="getPayPwdShow" ref="getPayPwd" :flag="getPayPwdShow" @getPayPwdOver="toSetPayPwd"></get-pay-pwd>
    <set-pay-pwd v-show="setPayPwdShow" ref="setPayPwd" :flag="setPayPwdShow" @setPayPwdOver="toPayPwd"></set-pay-pwd>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config';
  import protocol from '../../components/protocol/protocol.vue';
  import payPwd from '../../components/payPwd/payPwd.vue';
  import getPayPwd from '../../components/forgetPwd/getPayPwd.vue';
  import setPayPwd from '../../components/forgetPwd/setPayPwd.vue';
  import base64code from '../../mixins/base64code.js';
  import qs from 'qs';

  export default {
    data() {
      return {
        inputMoney: '',
        interest: '0.00',
        resdata: '',
        bankPay: 2,
        params: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        switch_status: true,
        protocolShow: false,
        payPwdShow: false,
        getPayPwdShow: false,
        setPayPwdShow: false
      }
    },
    components: { protocol, payPwd, getPayPwd, setPayPwd },
    mixins: [ base64code],
    watch: {
      inputMoney(newV, oldV) {
        if (newV) {
          newV = parseFloat(newV);
          if (newV != 0) {
            this.investInterest(newV);
          }
        }
      }
    },
    methods: {
      dataLoad() {
        this.$http.get(ajaxUrl.perchasePage, { params: this.params }).then((res) => {
          this.resdata = res.data.resData;
          if (this.inputMoney != '') {
            this.investInterest(this.inputMoney);
          }
        })
      },
      inputNum(evt){
        let val = evt.target.value;
        this.inputMoney = val.replace(/\D*$/, '');
      },
      // 预期收益
      investInterest(money){
        money = money || 0;
        let reqParams = Object.assign({ money: money }, this.params);
        this.$http.post(ajaxUrl.calculate, qs.stringify(reqParams)).then((res) => {
          this.interest = res.data.resData.profit;
        })
      },
      openPwd() {
        // 用实际投资金额判断
        if((this.bankPay == 1) && (this.inputMoney > this.resdata.useMoney)){
          this.$messagebox({
            cancelButtonText: '再看看',
            confirmButtonText: '去充值',
            message: '钱包余额不足，请先充值再进行投资',
            showCancelButton: true,
            closeOnClickModal: false
          }).then(action => {
            if (action == 'confirm') {
              this.$emit('rechargeOpen');
            }
          });
          return false;
        }
        if (this.inputMoney < this.resdata.minAmount) {
          this.$toast('粮票宝单笔申购最低金额' + this.resdata.minAmount + '元');
          this.inputMoney = this.resdata.minAmount;
          return false;
        }
        history.pushState(document.title, '#');
        this.payPwdShow = true;
        this.$refs.pwd.showLoad();
      },
      submitPay(pwd){
        let params = {
          money: this.inputMoney,
          agree: 1,
          type: this.bankPay,
          payPwd: this.base64Encode(pwd),
          perchaseToken: this.resdata.perchaseToken,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$indicator.open({ spinnerType: 'fading-circle' });
        this.$http.post(ajaxUrl.perchase, qs.stringify(params)).then((res) => {
          if (res.data.resCode == 39321) {
            this.$emit('fundInOver', res.data.resData);
          } else {
            this.$toast(res.data.resMsg);
            this.dataLoad();
          }
        })
      },
      readProtocol(params, url) {
        history.pushState(document.title, '#');
        this.protocolShow = true;
        this.$refs.procl.dataLoad(params, url);
      },
      forgetPayPwd() {
        this.payPwdShow = false;
        this.getPayPwdShow = true;
        this.$refs.getPayPwd.showLoad();
      },
      toSetPayPwd() {
        this.getPayPwdShow = false;
        this.setPayPwdShow = true;
        this.$refs.setPayPwd.showLoad();
      },
      toPayPwd() {
        this.setPayPwdShow = false;
        this.payPwdShow = true;
        this.$refs.pwd.showLoad();
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "fund_in.sass";
</style>
