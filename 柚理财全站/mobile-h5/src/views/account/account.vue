<!-- 资产 -->
<template>
  <div class="page" id="account">
    <div class="content">
      <mt-header class="bar-nav" title="资产">
        <router-link slot="right" to="/account/message">
          <mt-button >
            <img v-if="resdata.unreadMessageCount > 0" src="./../../assets/images/money/asset_nav_new_message.png" class="account-title-img">
            <img v-else src="./../../assets/images/money/asset_nav_message.png" class="account-title-img">
          </mt-button>
        </router-link>
      </mt-header>
      <section class="account-info">
        <span class="total-t">
          <span class="rd-text">总资产(元)</span>
          <span class="pos-right" @click="switchFlag">
            <img v-show="flag" src="./../../assets/images/money/asset_icon_show.png" class="total-img">
            <img v-show="!flag" src="./../../assets/images/money/asset_icon_hide.png" class="total-img">
          </span>
        </span>
        <router-link to="/account/logs">
          <div class="total-n font-arial">
            <span v-show="flag">{{ resdata.totalMoney | currency('', 2) }}</span>
            <span v-show="!flag">****</span>
          </div>
        </router-link>
        <div class="profit-n">
          <span class="rd-text">累计收益(元)</span>
          <span v-show="flag" class="num font-arial">{{ resdata.earnAmount | currency('', 2) }}</span>
          <span v-show="!flag" class="num">****</span>
        </div>
        <ul class="accountInfo-list font-arial">
          <li>
            <span class="rd-text">冻结金额(元)</span>
            <p v-show="flag">{{ resdata.noUseMoney | currency('', 2) }}</p>
            <p v-show="!flag">****</p>
          </li>
          <li>
            <span class="rd-text">可用金额(元)</span>
            <p v-show="flag">{{ resdata.useMoney | currency('', 2) }}</p>
            <p v-show="!flag">****</p>
          </li>
        </ul>
      </section>
      <ul class="link-money">
        <li @click="realnameStatus('recharge')"><p class="aui-border-r">充值</p></li>
        <li @click="realnameStatus('withdraw')">提现</li>
      </ul>
      <ul class="account-list text-center aui-flex aui-flex-col">
        <li class="aui-border-b aui-border-r">
          <router-link to="/account/myInvest">
            <p><img src="./../../assets/images/money/asset_icon_invest.png"></p>
            <p>我的投资</p>
          </router-link>
        </li>
        <li class="aui-border-b aui-border-r">
          <router-link to="/account/bond/bond">
              <p><img src="./../../assets/images/money/asset_icon_transfer1.png"></p>
              <p>债权转让</p>
          </router-link>
        </li>
        <li class="aui-border-b">
          <router-link to="/account/money_logs">
            <p><img src="./../../assets/images/money/asset_icon_realize.png"></p>
            <p>变现</p>
          </router-link>
        </li>
        <li class="aui-border-r">
          <router-link to="/account/my_loan">
            <p><img src="./../../assets/images/money/asset_icon_loan.png"></p>
            <p>我的借款</p>
          </router-link>
        </li>
        <li class="aui-border-r" @click="autoStatus">
          <p><img src="./../../assets/images/money/asset_icon_bid.png"></p>
          <p>自动投标</p>
        </li>
        <li class="text-center">
          <router-link to="/account/my_coupon">
            <p><img src="./../../assets/images/money/asset_icon_coupon.png"></p>
            <p>我的优惠</p>
          </router-link>
        </li>
      </ul>
    </div>
    <form :action="authSign" method="post" id="submitAuth">
      <input type="hidden" name="__sid" :value="getParams.__sid">
      <input type="hidden" name="userId" :value="getParams.userId">
    </form>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';

  export default {
    data() {
      return {
        resdata: '',
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
      this.$indicator.open({ spinnerType: 'fading-circle' });
      this.$http.get(ajaxUrl.getAccountInfo, { params: this.getParams }).then((res) => {
        this.resdata = res.data.resData;
        this.$indicator.close();
      })
    },
    methods: {
      // 金额数据明文密文切换
      switchFlag() {
        this.flag = !this.flag;
        if (!this.flag) {
          localStorage.hideAccountMoney = 1;
        } else {
          delete localStorage.hideAccountMoney;
        }
      },
      // 点击充值提现需要判断
      realnameStatus(name) {
        this.$indicator.open({ spinnerType: 'fading-circle' });
        this.$http.get(ajaxUrl.basicInfo, { params: this.getParams }).then((res) => {
          this.$indicator.close();
          // 判断是否实名
          if (res.data.resData.realnameStatus != 1) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去开通',
              showCancelButton: true,
              message: '为了您的资金安全，请您开通第三方资金托管账户！'
            }).then(action => {
              if(action == 'confirm') {
                this.$router.push('/mine/safe/realname?from=account');
              }
            });
            return;
          }
          // 判断是否实名
          if (name == 'withdraw' && res.data.resData.bankNum == 0) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '立即绑卡',
              showCancelButton: true,
              message: '请先绑定与身份信息一致的银行卡再提现！'
            }).then(action => {
              if(action == 'confirm') {
                this.$router.push('/mine/safe/bank?from=account');
              }
            });
            return;
          }
          this.$router.push('/account/' + name);
        })
      },
      // 去自动投标之前判断
      autoStatus() {
        this.$indicator.open({ spinnerType: 'fading-circle' });
        this.$http.get(ajaxUrl.basicInfo, { params: this.getParams }).then((res) => {
          this.$indicator.close();
          // 判断是否实名
          if(res.data.resData.realnameStatus != 1) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去开通',
              showCancelButton: true,
              message: '为了您的资金安全，请您开通第三方资金托管账户！'
            }).then(action => {
              if (action == 'confirm') {
                this.$router.push('/mine/safe/realname?from=account');
              }
            });
            return;
          }
          // 是否风险评估
          if (res.data.resData.riskLevel == '') {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去评测',
              showCancelButton: true,
              message: '为了您的资金安全，请您完成风险测评！'
            }).then(action => {
              if (action == 'confirm') {
                this.$router.push('/mine/safe/risk_tips?from=account')
              }
            });
            return;
          }
          //判断是否授权
          if (res.data.resData.authorizated == 0) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去授权',
              showCancelButton: true,
              message: '您需要开通业务授权，才能开启自动投标'
            }).then(action => {
              if (action == 'confirm') {
                document.getElementById('submitAuth').submit();
                sessionStorage.from_account_auth = '来自资产账户页授权';
              }
            });
            return;
          }
          this.$router.push('/account/auto');
        })
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../assets/scss/var.scss';
  @import '../../assets/scss/border_1px.scss';
  .bar-nav{background-color: $main-color;color:#fff; }
  .mint-button {height: 30px;}
  .account-title-img {width: .2rem;}
  .account-info{background-color: $main-color; text-align: center; padding-top: .13rem; margin-top:-1px; }
  .account-info .rd-text {font-size: .14rem; opacity: .5; color:#fff;}
  .total-img {width: .20rem; vertical-align: -.06rem;}
  .total-t{display: inline-block;position: relative;}
  span.pos-right {
    position: absolute;
    right: -25px;
    top: 0;
  }
  .total-n{color: #fff; font-size: .32rem; display: block; margin-top: .12rem;line-height:1;}
  .profit-n{margin: .2rem 0 .3rem; }
  .profit-n .num {color: #fff; font-size: .18rem;vertical-align: -.02rem;}
  .accountInfo-list{overflow: auto; padding-bottom:.12rem;}
  .accountInfo-list li,.link-money li{float: left; width: 50%; text-align: center;}
  .accountInfo-list p {color: #fff; font-size: .2rem; line-height:1; margin-top: .12rem;}
  .link-money{background-color: #fff; overflow: auto;}
  .link-money li{padding: .1rem 0; background-color: #fff; color: $main-color; font-size: .18rem;}
  .link-money li a{display: block; height: .30rem; line-height: .30rem;}
  .account-list{margin-top:.1rem;}
  .account-list li {
    width:33.33%; float:left; background-color: #fff; height:1.25rem;padding-top:.2rem;
  }
  .account-list li p{color: #333; margin-top: .1rem; font-size: .16rem;}
  .account-list li img{width: .24rem;}
  .fix-area {
    position: absolute;
    width: 100%; top:0;
  }
  /*.content { top: 2.46rem;}*/
</style>
