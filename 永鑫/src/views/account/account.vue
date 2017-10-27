<!-- 资产 by fenglei -->
<template>
  <div class="" id="account">
    <!-- <div class="modal" v-if="modal">
      <div class="alert bounceIn animated">
        <div class="head">
          <div class="table-cell">
            <h1>投资前准备</h1>
            <p v-if="statusdata.realnameStatus == 0">为了您能顺利投资请先实名认证</p>
            <p v-else-if="!statusdata.bankNum">请绑定银行卡</p>
            <p v-else-if="!statusdata.hasSetPayPwd">请设置支付密码</p>
          </div>
        </div>
        <ul>
          <li :class="{'color-999': statusdata.realnameStatus == 1}">
            <img class="icon" src="../../assets/images/money/realname_icon.png" >实名认证<img class="check" src="../../assets/images/money/check.png" v-if="statusdata.realnameStatus == 1" >
          </li>
          <li :class="{'color-999': statusdata.bankNum}">
            <img class="icon" src="../../assets/images/money/bank_icon.png" >绑定银行卡<img class="check" src="../../assets/images/money/check.png" v-if="statusdata.bankNum" >
          </li>
          <li :class="{'color-999': statusdata.hasSetPayPwd}">
            <img class="icon" src="../../assets/images/money/pwd_icon.png" >设置支付密码<img class="check" src="../../assets/images/money/check.png" v-if="statusdata.hasSetPayPwd" >
          </li>
        </ul>
        <div v-if="statusdata.realnameStatus == 0" @click="beforeInvest('/mine/safe/realname')" class="btn">去开通</div>
        <div v-else-if="!statusdata.bankNum" @click="beforeInvest('/mine/safe/addBank')" class="btn">去绑定</div>
        <div v-else-if="!statusdata.hasSetPayPwd" @click="beforeInvest('/mine/safe/setPwd')" class="btn">去设置</div>
        <p @click="iKnow" class="know">知道了</p>
      </div>
    </div> -->
    <div class="container">
      <section class="account-info">
        <router-link to="/account/message">
          <img v-if="resdata.unreadMessageCount > 0" src="./../../assets/images/money/asset_nav_new_message.png" class="message">
          <img v-else src="./../../assets/images/money/asset_nav_message.png" class="message">
        </router-link>
        <span class="total-t">
          <span class="rd-text">总资产(元)</span>
          <span class="pos-right" @click="switchFlag">
            <img v-show="flag" src="./../../assets/images/money/asset_icon_show.png" class="total-img">
            <img v-show="!flag" src="./../../assets/images/money/asset_icon_hide.png" class="total-img">
          </span>
        </span>
        <router-link to="/account/logs">
          <div class="total-n">
            <span v-show="flag" class="font-arial">{{ resdata.totalMoney | currency('', 2) }}</span>
            <span v-show="!flag">****</span>
          </div>
        </router-link>
        <div class="profit-n">
          <span class="rd-text">理财金额(元)</span>
          <span v-show="flag" class="num font-arial">{{ resdata.investAmount | currency('', 2) }}</span>
          <span v-show="!flag" class="num">****</span>
        </div>
      </section>
      <div class="info-list">
        <ul>
        <li class="aui-border-r">
          <span>钱包余额(元)</span>
          <p v-show="flag" class="font-arial">{{ resdata.useMoney | currency('', 2) }}</p>
          <p v-show="!flag">****</p>
        </li>
        <li>
          <span>粮票宝(元)</span>
          <p v-show="flag" class="font-arial">{{ resdata.fundMoney | currency('', 2) }}</p>
          <p v-show="!flag">****</p>
        </li>
        </ul>
        <ul class="link-money">
          <li class="aui-border-r">
            <mt-button @click="initStatus('/account/recharge')" size="normal" type="danger" plain>充值</mt-button>
            <mt-button @click="initStatus('/account/withdraw')" size="normal" type="default" plain>提现</mt-button>
          </li>
          <li>
            <mt-button @click="initStatus('/fund/in')" size="normal" type="danger" plain>转入</mt-button>
            <mt-button @click="initStatus('/fund/out')" size="normal" type="default" plain>转出</mt-button>
          </li>
        </ul>
      </div>
      <ul class="account-list text-center aui-flex aui-flex-col">
        <li class="aui-border-r border-bottom">
          <router-link to="/account/myInvest">
            <p><img src="./../../assets/images/me/wo_licai.png"></p>
            <p>我的理财</p>
          </router-link>
        </li>
        <li class="border-bottom">
          <router-link to="/account/logs">
            <p><img src="./../../assets/images/me/wo_jiaoyi.png"></p>
            <p>交易记录</p>
          </router-link>
        </li>
        <li class="aui-border-l border-bottom">
          <router-link to="/account/my_coupon">
            <p><img src="./../../assets/images/me/wo_youhui.png"></p>
            <p>我的优惠</p>
          </router-link>
        </li>
        <li class="aui-border-r">
          <router-link to="/mine">
          <p><img src="./../../assets/images/me/wo_geren.png"></p>
          <p>个人中心</p>
          </router-link>
        </li>
        <li class="text-center">
          <router-link to="/mine/safe">
            <p><img src="./../../assets/images/me/wo_anquan.png"></p>
            <p>账户安全</p>
          </router-link>
        </li>
        <li class="aui-border-l"></li>
      </ul>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';

  export default {
    data() {
      return {
        resdata: '',
        modal: false,
        statusdata: '',
        flag: true,
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        authSign: ajaxUrl.authSign
      };
    },
    created() {
      if (localStorage.hideAccountMoney) {
        this.flag = false;
      }
      this.dataLoad();
    },
    methods: {
      dataLoad() {
        this.$indicator.open({ spinnerType: 'fading-circle' });
        this.$http.get(ajaxUrl.getAccountInfo, { params: this.getParams }).then((res) => {
          this.resdata = res.data.resData;
        });
        this.$http.get(ajaxUrl.basicInfo, { params: this.getParams }).then((res) => {
          this.statusdata = res.data.resData;
          if (!(this.statusdata.realnameStatus == 1 && this.statusdata.bankNum > 0 && this.statusdata.hasSetPayPwd == 1)) {
            this.modal = true;
          }
        })
      },
      // 金额数据明文密文切换
      switchFlag() {
        this.flag = !this.flag;
        if (!this.flag) {
          localStorage.hideAccountMoney = 1;
        } else {
          delete localStorage.hideAccountMoney;
        }
      },
      beforeInvest (url) {
        this.initStatus(url, 1);
      },
      iKnow () {
        var box = document.querySelector('.alert');
        box.classList.remove('bounceIn');
        box.classList.add('fadeOut');
        setTimeout(() => {
          this.modal = false
        }, 300)
      },
      // 点击充值提现需要判断
      initStatus(url, type) {
        if (type != 1) {
          // 判断是否实名
          if (this.statusdata.realnameStatus != 1) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去认证',
              showCancelButton: true,
              message: '为了您的资金安全，请先实名认证'
            }).then(action => {
              if (action == 'confirm') {
                this.$router.push({ name: 'realname', query: { form: 'account' }});
              }
            });
            return;
          }
          // 判断是否绑卡
          if (this.statusdata.bankNum == 0) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去绑定',
              showCancelButton: true,
              message: '您尚未绑定银行卡，为了顺利投资请先绑定银行卡！'
            }).then(action => {
              if (action == 'confirm') {
                this.$router.push({ name: 'addBank', query: { form: 'account' }});
              }
            });
            return;
          }
          // 判断是否设置支付密码
          if (this.statusdata.hasSetPayPwd == 0) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去设置',
              showCancelButton: true,
              message: '您尚未设置支付密码，为了顺利投资请先设置支付密码！'
            }).then(action => {
              if (action == 'confirm') {
                this.$router.push({ name: 'setPwd' });
              }
            });
            return;
          }
          this.$router.push(url);
        } else {
          this.$router.push(url);
        }
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss">
  @import '../../assets/scss/var.scss';
  @import '../../assets/scss/border_1px.scss';
  @import "../../assets/css/animate.min.css";
  .container { margin-bottom: .75rem; }
  .account-info {
    background-color: $main-color;
    text-align: center;
    padding-top: .2rem;
    position: relative;
    .message {width: .2rem;position: absolute;top: .2rem;right: .15rem;}
    .rd-text {font-size: .14rem; opacity: .6; color:#fff;}
  }
  .total-img {width: .20rem; vertical-align: -.06rem;}
  .total-t{display: inline-block;position: relative;}
  span.pos-right {
    position: absolute;
    right: -25px;
    top: 0;
  }
  .total-n{color: #fff; font-size: .32rem; display: block; margin-top: .12rem;line-height:1;}
  .profit-n{padding: .2rem 0 .2rem; }
  .profit-n .num {color: #fff; font-size: .18rem;vertical-align: -.02rem;}
  .info-list {
    background: #fff;
    ul {
      display: flex;
      li {
        width: 50%;
        text-align: center;
        padding-top: .15rem;
        span { color: #666;  }
        p { font-size: .2rem; color: #333; line-height: 1; margin: .1rem 0 .05rem;}
      }
      &.link-money {
        li {
          padding-bottom: .25rem;
          .mint-button {font-size: .16rem;height: .34rem; padding: 0 .18rem; }
          .mint-button--default {border:none;}
        }
      }
    }
  }
  .account-list{
    margin-top:.1rem;
    li {
      width:33.33%; float:left; background-color: #fff; height:1.25rem;padding-top:.2rem;
      p {
        color: #333; margin-top: .1rem; font-size: .16rem;
      }
      img { width: .24rem;}
    }
  }
  .mint-button:not(.is-disabled):active::after {opacity: 0;}
  .border-right {
    @include hairline(right)
  }
  .border-bottom {
    @include hairline(bottom)
  }
  .modal {
    position: absolute;
    background: rgba(0,0,0,.3);
    width: 100%;
    height: 100%;
    z-index: 3;
    .alert {
      position: absolute;
      width: 84%;
      background: #fff;
      left: 8%;
      top: 50%;
      margin-top: -2rem;
      height: 4rem;
      border-radius: .2rem;
      text-align: center;
      .head {
        background: url(../../assets/images/money/head.png) no-repeat;
        background-size: cover;
        height: 1.1rem;
        color: #fff;
        display: table;
        width: 100%;
        .table-cell { display: table-cell; vertical-align: middle; }
        h1 { font-size: .25rem; line-height: 1.5;}
        p { font-size: .14rem;}
      }
      ul {
        margin: .3rem 0 .15rem;
        li {
          line-height: .44rem;
          text-align: left;
          padding-left: .71rem;
          .icon {
            width: .44rem;
            margin-right: .12rem;
            vertical-align: middle;
          }
          .check { width: .12rem; vertical-align: middle;margin-left: .1rem;}
        }
      }
      .btn {
        background: url(../../assets/images/money/button.png) no-repeat;
        background-size: cover;
        height: .61rem;
        width: 2.41rem;
        margin: 0 auto .05rem;
        color: #fff;
        font-size: .16rem;
        line-height: .44rem;
      }
      .know { font-size: .16rem; color: $main-color;}
    }
  }

  .slide-enter-active,.slide-leave-active {
    transition: all .2s linear
  }
  .slide-enter, .slide-leave-active{
    transform: translate3d(100%, 0, 0)
  }
</style>
