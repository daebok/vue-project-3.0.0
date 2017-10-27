<!-- 充值 by fenglei -->
<template>
  <div class="page">
    <div class="recharge-top">
      <p>钱包余额</p>
      <p v-if="resdata != ''">{{ resdata.money | currency('', 2)}}元</p>
    </div>
    <form>
      <div class="bank margin-t-15">
        <label><img class="bank-logo" :src="resdata.picPath" alt="">{{resdata.bankName}} <i class="color-999">{{resdata.hideBankNo}}</i></label>
        <span class="tip-color" @click="readProtocol('bankDeclare','top5Help')">查看限额</span>
      </div>
      <div class="margin-t-15">
        <rd-money label="充值金额(元)" maxlength="8" type="tel" v-model.trim="inputMoney" placeholder="请输入金额"></rd-money>
      </div>
    </form>
    <div class="protocol-box">
        <span @click="switch_status = !switch_status">
            <span v-if="switch_status">
              <img src="../../assets/images/fund/xieyi_check_02.png" />
            </span>
            <span v-else>
              <img src="../../assets/images/fund/xieyi_check_01.png" />
            </span>
        </span>
      我已阅读并同意
      <a v-for="item in resdata.proList" @click="readProtocol(item.id, 'registerProtocolDetail')" class="protocol">《{{ item.name }}》</a>
    </div>
    <div class="confirm-btn">
      <mt-button v-if="inputMoney && switch_status" size="large" type="danger" @click="openPwd">充&nbsp;值</mt-button>
      <mt-button v-else size="large" type="default" disabled>充 值</mt-button>
    </div>
    <div class="b-tips margin-b-30">
      <span class="tip-color" @click="toRecord"><img src="../../assets/images/me/btn_czjl.png">&nbsp;充值记录</span>
    </div>
    <div class="form-prompt">
      <p class="prompt-title">温馨提示：</p>
      <p v-html="resdata.warmTips"></p>
    </div>
    <transition name="slide-bottom">
      <protocol v-show="protocolShow" ref="procl"></protocol>
    </transition>
    <transition name="slide">
      <record v-show="recordShow" ref="record"></record>
    </transition>
    <pay-pwd v-show="payPwdShow" ref="pwd" :flag="payPwdShow" :tipMoney="inputMoney" @submit="submitPay" @forget="forgetPayPwd"></pay-pwd>
    <get-pay-pwd v-show="getPayPwdShow" ref="getPayPwd" :flag="getPayPwdShow" @getPayPwdOver="toSetPayPwd"></get-pay-pwd>
    <set-pay-pwd v-show="setPayPwdShow" ref="setPayPwd" :flag="setPayPwdShow" @setPayPwdOver="toPayPwd"></set-pay-pwd>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';
  import RdMoney from '../../components/MoneyInput.vue';
  import protocol from '../../components/protocol/protocol.vue';
  import payPwd from '../../components/payPwd/payPwd.vue';
  import getPayPwd from '../../components/forgetPwd/getPayPwd.vue';
  import setPayPwd from '../../components/forgetPwd/setPayPwd.vue';
  import record from '../../components/recharge/record.vue';
  import base64code from '../../mixins/base64code.js';
  import qs from 'qs';

  export default {
    data() {
      return {
        resdata: '',
        inputMoney: '',
        params: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        switch_status: true,
        protocolShow: false,
        payPwdShow: false,
        getPayPwdShow: false,
        setPayPwdShow: false,
        recordShow: false,
        repeatFlag: true
      };
    },
    components: { RdMoney, protocol, payPwd, getPayPwd, setPayPwd, record },
    mixins: [ base64code ],
    watch: {
      inputMoney(newV, oldV) {
        if (newV.search(/^([1-9]\d*|0)/) == -1) { // 解决刚开始输入不符合规则的bug
          this.inputMoney = '';
        }
      }
    },
    methods: {
      dataLoad() {
        this.inputMoney = '';
        this.$http.get(ajaxUrl.toRecharge, { params: this.params }).then((res) => {
          if(res.data.resData) {
            this.resdata = res.data.resData;
            this.resdata.warmTips = res.data.resData.warmTips.replace(/\n/g, '<br/>');
          }
        })
      },
      openPwd() {
        if (this.inputMoney < this.resdata.rechargeMinAmount) {
          this.$toast('充值金额不能小于' + this.resdata.rechargeMinAmount + '元');
          this.inputMoney = this.resdata.rechargeMinAmount;
          return false;
        }
        if (this.inputMoney > this.resdata.rechargeMaxAmount) {
          this.$toast('充值金额不能大于' + this.resdata.rechargeMaxAmount +'元');
          this.inputMoney = this.resdata.rechargeMaxAmount;
          return false;
        }
        history.pushState(document.title, '#');
        this.payPwdShow = true;
        this.$refs.pwd.showLoad();
      },
      submitPay(pwd) {
        let params = {
          amount: this.inputMoney,
          rechargeToken: this.resdata.rechargeToken,
          payPwd: this.base64Encode(pwd),
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$indicator.open({ spinnerType: 'fading-circle' });
        this.$http.post(ajaxUrl.doRecharge, qs.stringify(params)).then((res) => {
          if (res.data.resCode == 39321) {
            this.$toast('受理成功，详情请查看充值记录');
            this.toBack();
          } else {
            this.$toast(res.data.resMsg);
          }
        });
      },
      toBack() {
        history.back();
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
      },
      toRecord() {
        history.pushState(document.title, '#');
        this.recordShow = true;
        this.$refs.record.dataLoad();
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "recharge.sass";
</style>
