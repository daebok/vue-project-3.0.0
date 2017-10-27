<!-- 我的理财-处理中-去支付 by fenglei -->
<template>
  <div class="page">
    <div class="count-time">
      <span class="text">剩余时间</span>
      <span class="value">{{ countTime }}</span>
    </div>
    <div class="pay-money">
      <span class="text">支付金额</span>
      <span class="value">¥&nbsp;{{ investMoney | currency('', 2) }}</span>
    </div>
    <div class="pay-style">
      <span class="title">支付方式</span>
      <ul>
        <li>
          <div class="select">
            <em :class="{ 'current': payValue == 1 }" @click="payValue = 1"></em>
          </div>
          <div class="bank-card">
            <span class="bank-logo"><img :src="dataBank.picPath"></span>
            <span class="bank-info">{{ dataBank.bankName }}&nbsp;<i>{{ dataBank.hideBankNo }}</i></span>
          </div>
          <div class="bank-declare" @click="readProtocol('bankDeclare','top5Help')">查看限额</div>
        </li>
        <li>
          <div class="select">
            <em :class="{ 'current': payValue == 2 }" @click="payValue = 2"></em>
          </div>
          <div class="other">
            <span class="text">粮票宝</span>
            <span class="value">{{ dataAjax.moneyFundMoney | currency('',2)}}元</span>
          </div>
        </li>
        <li>
          <div class="select">
            <em :class="{ 'current': payValue == 0 }" @click="payValue = 0"></em>
          </div>
          <div class="other">
            <span class="text">钱包余额</span>
            <span class="value">{{ dataAjax.useMoney | currency('',2)}}元</span>
          </div>
        </li>
      </ul>
    </div>
    <div class="operator">
      <mt-button size="large" type="danger" @click="openPwd">确认支付&nbsp;{{ investMoney | currency('', 2) }}元</mt-button>
    </div>
    <transition name="slide-bottom">
      <protocol v-show="protocolShow" ref="procl"></protocol>
    </transition>
    <pay-pwd v-show="payPwdShow" ref="pwd" :flag="payPwdShow" :tipMoney="investMoney" @submit="submitPay" @forget="toGetPayPwd"></pay-pwd>
    <get-pay-pwd v-show="getPayPwdShow" ref="getPayPwd" :flag="getPayPwdShow" @getPayPwdOver="toSetPayPwd"></get-pay-pwd>
    <set-pay-pwd v-show="setPayPwdShow" ref="setPayPwd" :flag="setPayPwdShow" @setPayPwdOver="toPayPwd"></set-pay-pwd>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';
  import protocol from '../../components/protocol/protocol.vue'; // 协议组件
  import payPwd from '../../components/payPwd/payPwd.vue'; // 输入支付密码组件
  import getPayPwd from '../../components/forgetPwd/getPayPwd.vue'; // 找回支付密码组件
  import setPayPwd from '../../components/forgetPwd/setPayPwd.vue'; // 设置支付密码组件
  import base64code from '../../mixins/base64code.js';
  import qs from 'qs';

  export default {
    data() {
      return {
        interval: '',
        dataAjax: '',
        dataInvest: '',
        dataBank: '',
        countTime: '',
        investMoney: '',
        payValue: 1,
        params: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        protocolShow: false,
        payPwdShow: false,
        getPayPwdShow: false,
        setPayPwdShow: false,
        rechargeShow: false
      };
    },
    prop: {
      uuid: {
        type: String,
        default: ''
      }
    },
    components: { payPwd, protocol, getPayPwd, setPayPwd },
    mixins: [ base64code ],
    methods: {
      dataLoad(uuid) {
        this.countTime = '00:00';
        this.payValue = 1;
        let getParams = Object.assign({ uuid: uuid }, this.params);
        this.$http.get(ajaxUrl.toPay, { params: getParams }).then((res) => {
          if (res.data.resData) {
            this.dataAjax = res.data.resData;
            this.dataInvest = res.data.resData.invest;
            this.dataBank = res.data.resData.userBank;
            this.investMoney = res.data.resData.invest.realAmount;
            this.countDown(res.data.resData.invest.remainTimes);
          }
        })
      },
      // 剩余支付时间倒计时
      // 当剩余支付时间归零后调用toBack方法
      countDown(remainTimes) {
        let sys_sec = Number(remainTimes);
        if (sys_sec > 0) {
          clearInterval(this.interval);
          this.interval = setInterval(() => {
            if (sys_sec > 1) {
              sys_sec -= 1;
              let min = Math.floor(sys_sec / 60);
              let sec = Math.floor(sys_sec % 60);
              let showMin = min < 10 ? '0' + min : min; // 计算分钟
              let showSec = sec < 10 ? '0' + sec : sec; // 计算秒数
              this.countTime = showMin + ':' + showSec;
            } else {
              clearInterval(this.interval);
              this.toBack();
            }
          }, 1000)
        }
      },
      // 调用协议组件
      // sectionCode: 协议的标识，url：调用协议接口的地址名称
      readProtocol(sectionCode, url) {
        history.pushState(document.title, '#');
        this.protocolShow = true;
        this.$refs.procl.dataLoad(sectionCode, url);
      },
      // 确认支付按钮单击
      // 判断当前支付方式
      // 使用钱包余额支付时，如果钱包余额不足，用户想去充值，则向父组件派发事件rechargeOpen
      // 使用粮票宝余额支付时，如果粮票宝余额不足，用户想去转入，则向父组件派发事件fundInOpen
      // 余额足够的时候，可以支付，调用输入支付密码组件，让用户输入支付密码
      openPwd() {
        if ((this.payValue == 0) && (this.investMoney > this.dataAjax.useMoney)) { // 钱包余额小于投资金额
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
        if ((this.payValue == 2) && (this.investMoney > this.dataAjax.moneyFundMoney)) { // 粮票宝小于投资金额
          this.$messagebox({
            cancelButtonText: '再看看',
            confirmButtonText: '去转入',
            message: '粮票宝余额不足，请先转入再进行投资',
            showCancelButton: true,
            closeOnClickModal: false
          }).then(action => {
            if (action == 'confirm') {
              this.$emit('fundInOpen');
            }
          });
          return false;
        }
        history.pushState(document.title, '#');
        this.payPwdShow = true;
        this.$refs.pwd.showLoad();
      },
      // 从输入支付密码组件中监听到submit事件并执行submitPay方法
      // 支付密码输入成功且支付成功，调用toBack方法
      // 支付密码输入成功但支付失败，提示错误信息，调用dataLoad方法重置payToken
      // 支付密码输入失败，仅提示错误信息
      submitPay(pwd) {
        let getParams = {
          payPwd: this.base64Encode(pwd),
          payStyle: this.payValue,
          realAmount: this.investMoney,
          payToken: this.dataAjax.payToken,
          investOrderNo: this.dataInvest.investOrderNo,
          upApr: this.dataInvest.upApr,
          projectId: this.dataInvest.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$indicator.open({ spinnerType: 'fading-circle' });
        this.$http.post(ajaxUrl.doPay, qs.stringify(getParams)).then((res) => {
          if (res.data.resData.payStatus == '2') {
            this.$toast('支付成功');
            this.toBack();
          } else if (res.data.resData.payStatus == '1') {
            this.$toast(res.data.resMsg);
            this.dataLoad(res.data.resData.uuid);
          } else {
            this.$toast(res.data.resMsg);
          }
        })
      },
      // 放回上一层，并向上一层派发paySuccess事件
      // 剩余支付时间归零时，放回上一层，若此时有其他子组件已被调用，则执行两次history.back()
      // 支付成功后，返回上一层
      toBack() {
        this.$emit('paySuccess');
        if (this.protocolShow || this.payPwdShow || this.getPayPwdShow || this.setPayPwdShow) {
          history.back();
          history.back();
        } else {
          history.back();
        }
      },
      // 从输入支付密码组件中监听到forget事件并执行toGetPayPwd方法
      // 调用找回支付密码组件时，关闭输入支付密码组件
      toGetPayPwd() {
        this.payPwdShow = false;
        this.getPayPwdShow = true;
        this.$refs.getPayPwd.showLoad();
      },
      // 从找回支付密码组件中监听到getPayPwdOver事件并执行toSetPayPwd方法
      // 调用设置支付密码组件时，关闭找回支付密码组件
      toSetPayPwd() {
        this.getPayPwdShow = false;
        this.setPayPwdShow = true;
        this.$refs.setPayPwd.showLoad();
      },
      // 从设置支付密码组件中监听到setPayPwdOver事件并执行toPayPwd方法
      // 调用输入支付密码组件时，关闭设置支付密码组件
      toPayPwd() {
        this.setPayPwdShow = false;
        this.payPwdShow = true;
        this.$refs.pwd.showLoad();
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "myInvest_bid.sass";
</style>
