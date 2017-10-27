<!-- 理财投资页 -->
<template>
  <div class="payment-page">
    <div v-show="showRes" class="margin-t-10" id="resHtml" data-tip="给投资提交返回信息使用"></div>
    <div v-show="!showRes" class="form-invest">
      <div v-if="resdata.saleStyle == 1" class="form-group margin-t-20 margin-b-10">
        <label>剩余可购金额</label>
        <span class="main-color">{{ resdata.remainAccount | currency('',2) }}元</span>
      </div>
      <div v-else class="form-group margin-t-20 margin-b-10">
        <label>剩余可购份数</label>
        <span class="main-color">{{ resdata.remainAccount / resdata.copyAccount }}份</span>
      </div>
      <div class="form-group margin-b-20" v-if="!times2">
        <label>投资区间</label>
        <span v-if="resdata.saleStyle==1 && userCanInvest > resdata.lowestAccount" class="color-333">{{ resdata.lowestAccount | currency('',2) }}元 - {{ userCanInvest | currency('',2) }}元</span>
        <span v-if="resdata.saleStyle==2 && userCanInvest > resdata.lowestCopies" class="color-333">{{ resdata.lowestCopies }}份 - {{ userCanInvest }}份</span>
      </div>
      <div v-if="resdata.saleStyle==2" class="form-list">
        <label>投资份数(份)</label>
        <input type="tel" pattern="\d*" name="" v-model.trim="investMoney" class="invest-money"  :placeholder="'每份' + resdata.copyAccount + '元'" :readonly="times2" @input="inputNum">
      </div>
      <div v-else class="form-list">
        <label>投资金额(元)</label>
        <input ref="inputMoney" type="tel" pattern="\d*" v-model.trim="investMoney" class="invest-money" id="invest-money" :placeholder="resdata.stepAccount > 0 ? '递增金额为' + resdata.stepAccount + '元': '请输入金额'" :readonly="times2" @input="inputNum">
      </div>
      <div class="form-group margin-b-20 earnings">
        <label style="width: 30%;">预期收益</label>
        <span class="main-color">{{ interest }}元</span>
        <p v-if="upAprInterest > 0">(其中加息券收益{{ upAprInterest }}元)</p>
      </div>
      <div class="group-head aui-border-b">使用优惠</div>
      <ul class="form-group-select">
        <li v-if="resdata.redEnvelopeUseful == 1 && resdata.ifRedEnvelopestatus == 1" class="form-red aui-border-b border-color" @click="selectCoupon('investRedpacket')">
          <label>红包</label>
          <span>{{ redpacketNum }}</span>
          <img src="../../../assets/images/public/arrow_right.png" class="right-go">
        </li>
        <li v-else class="form-red aui-border-b border-color disabled">
          <label>红包</label>
          <span v-if="resdata.redEnvelopeUseful == 0">该产品不可使用红包</span>
          <span v-else="resdata.ifRedEnvelopestatus == 0">无可用红包</span>
          <img src="../../../assets/images/public/arrow_right.png" class="right-go">
        </li>
        <li v-if="resdata.additionalRateUseful == 1 && resdata.ifAdditionalRate == 1" class="form-coupon aui-border-b border-color" @click="selectCoupon('investCoupon')">
          <label>加息券</label>
          <span>{{ selected_upApr }}</span>
          <img src="../../../assets/images/public/arrow_right.png" class="right-go">
        </li>
        <li v-else class="form-coupon aui-border-b border-color disabled">
          <label>加息券</label>
          <span v-if="resdata.additionalRateUseful == 0">该产品不可使用加息券</span>
          <span v-else="resdata.ifAdditionalRate == 0">无可用加息券</span>
          <img src="../../../assets/images/public/arrow_right.png" class="right-go">
        </li>
      </ul>
      <div class="group-head margin-t-10 aui-border-b">支付方式</div>
      <ul class="form-group-select">
        <li class="pay-style aui-border-b">
          <label>
            <input type="radio" class="checkbox hide" value="1" name="bankPay" v-model="bankPay">
            <em></em>
            <img class="bank-logo" :src="resdata.picPath">{{ resdata.bankName }} <i class="color-999">{{ resdata.hideBankNo }}</i>
          </label>
          <span class="tip-color" @click="readLimit">查看限额</span>
        </li>
        <li class="pay-style aui-border-b">
          <label>
            <input type="radio" class="checkbox hide" value="2" name="bankPay" v-model="bankPay" :disabled="resdata.moneyFundMoney <= 0">
            <em></em>
            粮票宝</label>
          <span>{{ resdata.moneyFundMoney | currency('',2) }}元</span>
        </li>
        <li class="pay-style">
          <label>
            <input type="radio" class="checkbox hide" value="0" name="bankPay" v-model="bankPay" :disabled="resdata.userMoney <= 0">
            <em></em>
            钱包余额</label>
          <span>{{ resdata.userMoney | currency('',2) }}元</span>
        </li>
      </ul>
      <div class="operator">
        <mt-button v-show="!investMoney" size="large" type="default" disabled>确认支付 0.00元</mt-button>
        <mt-button v-show="investMoney" size="large" type="danger" @click.prevent="openPwd" :disabled="resdata == ''">确认支付
          <span class="font-arial">{{ actualInvestMoney | currency('',2) }}</span>元
        </mt-button>
      </div>
      <p class="tips">
        <span class="allIn" v-if="times2">剩余金额小于等于起投金额的两倍，请全部投资<br/></span>
        <span @click="switch_status =! switch_status">
          <span v-if="switch_status">
            <img src="../../../assets/images/public/xieyi_check_02.png">
          </span>
          <span v-else>
            <img src="../../../assets/images/public/xieyi_check_01.png">
          </span>
        </span>
        <span class="protocol">同意
          <i v-if="resdata.infoProtocolId" @click="readProtocol(resdata.infoProtocolId, '信息平台服务协议')">《信息平台服务协议》</i>
          <i v-if="resdata.protocolId" @click="readProtocol(resdata.protocolId,'产品协议')">《产品协议》</i>
          <i v-if="resdata.initiationProtocolId" @click="readProtocol(resdata.initiationProtocolId, '入会协议')">《入会协议》</i>
        </span>
      </p>
    </div>
    <pay-pwd @submit="submitForm" ref="pwd" v-model="payPwd" :tipMoney="actualInvestMoney"></pay-pwd>
    <div class="protocol-con">
      <mt-popup v-model="popupVisible" position="bottom" class="mint-popup-3" :modal="false">
        <mt-header class="bar-nav" :title="protocolTitle" >
          <mt-button slot="right" @click.native="popupVisible = false">关闭</mt-button>
        </mt-header>
        <div class="con" :style="{ height: protocolHeight + 'px' }"><section v-html="protocolHtml"></section></div>
      </mt-popup>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../../ajax.config';
  import PayPwd from '../../../components/PayPwd.vue';
  import base64code from '../../../mixins/base64code';
  import qs from 'qs';

  export default {
    name: 'investBid',
    data() {
      return {
        investMoney: sessionStorage.investMoney,
        toName: sessionStorage.toName,
        resdata: '',
        bankPay: sessionStorage.bankPay || 1,
        times2: false,
        payPwd: '',
        userCanInvest: '',
        urlParams: {
          projectId: this.$route.params.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        interest: '0.00',
        upAprInterest: 0, // 加息券收益默认0
        redpacketNum: this.$store.state.redpack.selected_total + '元',
        selected_upApr: sessionStorage.selected_upApr + '%',
        popupVisible: false,
        protocolTitle: '',
        protocolHtml: '',
        showRes: false,
        switch_status: true, //是否同意注册协议，true已同意，false不同意
        showProtocol: true, // 入会协议控制显示
        protocolHeight: 0
      };
    },
    computed: {
      actualInvestMoney() {
        let red_num = parseInt(this.redpacketNum);
        if (this.resdata.saleStyle == 2) { //判断是否份额购买
          if (!isNaN(red_num)) {
            return this.investMoney*this.resdata.copyAccount - red_num;
          } else {
            return this.investMoney*this.resdata.copyAccount;
          }
        } else {
          if (!isNaN(red_num)) {
            return this.investMoney - red_num;
          } else {
            return this.investMoney;
          }
        }
      }
    },
    created() {
      this.$nextTick(() => {
        this.protocolHeight = document.documentElement.clientHeight - 40;
      })
      if (/Android/gi.test(navigator.userAgent)) { // 解决安卓机调用键盘后页面重绘优惠券页面
        window.addEventListener('resize', () => {
         if (document.activeElement.tagName == 'INPUT') {
           this.resize = true;
         }
        })
      }
      if (this.$route.query.from) {  // 提交判断是否为变现提交
        this.submitUrl = ajaxUrl.doRealizeInvest;
      }
      if (this.investMoney == undefined || this.investMoney == '') {
        this.redpacketNum = '请选择红包';
        this.selected_upApr = '请选择加息券';
      }
      if (this.$store.state.redpack.selected_total == 0) {
        this.redpacketNum = '请选择红包';
      }
      if (sessionStorage.selected_upApr == 0) {
        this.selected_upApr = '请选择加息券';
      }
      let getParams = Object.assign({ investType: 'invest' }, this.urlParams);
      if (this.$route.query.prevPage == 'realizeDetail') {
        getParams.investType = 'realize';
      }
      this.$http.get(ajaxUrl.initInvest, { params:getParams }).then((res) => {
        this.resdata = res.data.resData;
        this.userCanInvest = this.resdata.userCanInvestAmount;
        if (this.resdata.saleStyle == 2) {  // 用户最多可投份额
          this.userCanInvest = this.resdata.userCanInvestAmount / this.resdata.copyAccount;
        }
        // 用户已达到投资上限
        if (!this.userCanInvest || this.userCanInvest == 0) {
          this.$messagebox({
            title: ' ',
            showCancelButton: false,
            message: '您已达到投资上限，无法投资'
          }).then(action => {
            this.goBack();
          });
        }
        // 产品剩余金额 <= 起投金额的2倍
        if (this.resdata.remainAccount <= (this.resdata.lowestAccount * 2)) {
          this.times2 = true; //投资区间隐藏以及输入框不可输入值
          this.investMoney = this.resdata.remainAccount;
          if (this.resdata.saleStyle == 2) {  // 投资为份额时
            this.investMoney = this.resdata.remainAccount/this.resdata.copyAccount;
          }
        }
        if (this.resdata == '') {
          if (res.data.resMsg.match('未支付订单')) {
            this.$messagebox({
              title: ' ',
              cancelButtonText: '取消',
              confirmButtonText: '去完成',
              showCancelButton: true,
              message: res.data.resMsg
            }).then(action => {
              if (action == 'cancel') {
                this.goBack();
              } else if(action == 'confirm') {
                this.$router.push({ name: 'myInvest', query: {
                  from: 'investBid',
                  tab: 'tab-con2',
                  uuid: this.$route.params.projectId
                }})
              }
            });
          } else {
            this.$messagebox({
              title: ' ',
              showCancelButton: false,
              message: res.data.resMsg
            }).then(action => {
              this.goBack();
            });
          }
        }
        if (this.investMoney != '') { //不为空时初始化
          this.investInterest(this.investMoney, this.selected_upApr);
        }
        // 判断入会协议
        let params = Object.assign(this.urlParams, { protocolId: this.resdata.initiationProtocolId });
        this.$http.get(ajaxUrl.protocolSearch,{ params: params }).then((res) => {
          if(!res.data.resData) this.showProtocol = false;
        })
      })
    },
    watch: {
      bankPay(newV, oldV) {
        sessionStorage.bankPay = newV;
      },
      investMoney(newV, oldV) {
        newV = parseFloat(newV);
        if (newV) {
          if (newV == 0) {
            this.investMoney = '';
          }
          if (newV > this.userCanInvest) {
            if (this.resdata.saleStyle == 2) {
              this.$toast('您当前的最高投资份数为'+ this.userCanInvest +'份');
            } else {
              this.$toast('您当前的最高投资金额为'+ this.userCanInvest +'元');
            }
            this.investMoney = this.userCanInvest;
          }
          sessionStorage.investMoney = newV;
          this.redpacketNum = '请选择红包';
          this.$store.dispatch('selectRedpacket', []);
          this.$store.dispatch('selectRedpacketTotal', 0);
          this.selected_upApr = '请选择加息券';
          sessionStorage.selected_coupon = '';
          sessionStorage.selected_upApr = '0.00';
          this.investInterest(newV, this.selected_upApr);
        } else {
          sessionStorage.investMoney = '';
          this.interest = '0.00';
        }
      }
    },
    mixins: [ base64code ],
    methods: {
      // 点击返回
      goBack() {
        sessionStorage.investMoney = '';
        this.$router.go(-1);
      },
      inputNum(evt) {
        let val = evt.target.value;
        this.investMoney = val.replace(/\D*$/, '');
      },
      calcuInterest(money,apr,timeLimit,style) {
        var m_par = parseFloat(apr/100/12); // 月利率
        //等额本息
        if (style == 1) {
          var m_total = parseFloat((money*m_par*Math.pow((m_par+1),timeLimit))/(Math.pow((m_par+1),timeLimit)-1)); // 月还款本息
          this.interest = (m_total * timeLimit) - money;
        } else if(style == 4) { //等额本金
          this.interest = (timeLimit + 1) * money * m_par / 2;
        } else {
          this.interest = money * this.resdata.totalInterest / this.resdata.account;
        }
      },
      //预期收益
      investInterest(money, upApr=0) {
        upApr = parseFloat(upApr) || 0;
        if (this.resdata.saleStyle == 2) {
          money = money * this.resdata.copyAccount;
        }
        money = money || 0;
        //this.calcuInterest(money,this.resdata.apr,this.resdata.timeLimit,this.resdata.repayStyle);
        // 选择加息券后
        let reqParams = Object.assign({ amount: money, upApr: upApr }, this.urlParams);
        this.$http.post(ajaxUrl.interest, qs.stringify(reqParams)).then((res) => {
          this.upAprInterest = res.data.resData.upInterest;
          this.interest = res.data.resData.interest;
        })
      },
      // 选择红包加息券
      selectCoupon(arg){
        var lowest = this.resdata.saleStyle == 2 ? this.resdata.lowestCopies : this.resdata.lowestAccount;
        if (!this.investMoney) {
          let str = this.resdata.saleStyle == 2 ? '请输入投资份数' : '请输入投资金额';
          this.$toast(str);
        } else if (this.investMoney < lowest && (this.resdata.remainAccount >= this.resdata.lowestAccount)) { // 小于起投金额
            let str = this.resdata.saleStyle == 2 ? '最低起投份数为' + this.resdata.lowestCopies + '份' : '最低起投金额为' + this.resdata.lowestAccount + '元';
            this.$toast(str);
        } else {
          if (this.resdata.stepAccount) { // 判断是否有递增金额 金额必须为起投金额+递增金额的整数倍
            var times = (this.investMoney - this.resdata.lowestAccount) % this.resdata.stepAccount;
            if (times > 0) {
              this.$toast('金额必须为起投金额+递增金额的整数倍');
              return false;
            }
          }
          //红包可用最大限额
          let limit_num = this.resdata.redEnvelopeRate * Number(this.investMoney) / 100;
          if (this.resdata.saleStyle == 2) {
            limit_num = limit_num * this.resdata.copyAccount;
          }
          sessionStorage.redpack_max_limit = limit_num;
          if (this.resize) { // 安卓机调用键盘有页面重绘bug
            setTimeout(() => {
              this.$router.push({ name: arg,query: { copyAccount: this.resdata.copyAccount, rate: this.resdata.redEnvelopeRate }});
            }, 600)
          } else {
            this.$router.push({ name: arg,query: { copyAccount: this.resdata.copyAccount, rate: this.resdata.redEnvelopeRate }});
          }
        }
      },
      openPwd() {
        let amount = this.resdata.saleStyle == 2 ? this.investMoney * this.resdata.copyAccount : this.investMoney;
        if ( amount < this.resdata.lowestAccount) { //小于起投金额
          if (this.resdata.remainAccount >= this.resdata.lowestAccount) { // 当剩余可投金额大于等于最小可投金额时判断
            let str = this.resdata.saleStyle == 2 ? '最低起投份数为' + this.resdata.lowestCopies + '份' : '最低起投金额为' + this.resdata.lowestAccount + '元';
            this.$toast(str);
            return false;
          }
        }
        if (this.resdata.stepAccount && !this.times2) { // 判断是否有递增金额 金额必须为起投金额+递增金额的整数倍
          var times = (this.investMoney - this.resdata.lowestAccount) % this.resdata.stepAccount;
          if (times > 0) {
            this.$toast('金额必须为起投金额+递增金额的整数倍');
            return false;
          }
        }
        // 钱包余额小于投资金额
        if ((this.bankPay == 0) && (this.actualInvestMoney > this.resdata.userMoney)) {
          let str = '钱包余额小于投资金额，请先充值再进行投资。';
          this.$messagebox({
            title: ' ',
            cancelButtonText: '再看看',
            confirmButtonText: '去充值',
            showCancelButton: true,
            message: str
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push('/account/recharge');
            }
          });
          return false;
        }
        // 粮票宝小于投资金额
        if ((this.bankPay == 2) && (this.actualInvestMoney > this.resdata.moneyFundMoney)) {
          let str = '钱包余额小于粮票宝，请先转入再进行投资。';
          this.$messagebox({
            title: ' ',
            cancelButtonText: '再看看',
            confirmButtonText: '去转入',
            showCancelButton: true,
            message: str
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push('/fund/in');
            }
          });
          return false;
        }
        // 协议要同意
        if (!this.switch_status) {
          this.$toast('请同意并勾选协议');
          return false;
        }
        this.$refs.pwd.show(); // 打开支付密码区
      },
      submitForm() {
        let rateCouponId = sessionStorage.selected_coupon;
        let redEnvelopeIds = this.$store.state.redpack.selectedList.join(',');
        let params = {
          agree: 1,
          payPwd: this.base64Encode(this.payPwd),
          payStyle: this.bankPay,
          amount: this.investMoney,
          redEnvelopeIds: redEnvelopeIds,
          rateCouponId:rateCouponId,
          investToken: this.resdata.investToken,
          projectId: this.$route.params.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$http.post(ajaxUrl.doInvest, qs.stringify(params)).then((res) => {
          if (res.data.resData == '') { //主要是有请勿重复提交信息提示
            this.$refs.pwd.close(); // 关闭支付密码区
            this.$toast(res.data.resMsg);
          } else {
            sessionStorage.investMoney = '';
            sessionStorage.invest_resdata = JSON.stringify(res.data.resData);
            this.$router.push({ name: 'tenderResult', query: { uuid: res.data.resData.uuid }});
          }
        })
      },
      readProtocol(arg,arg2) {
        this.popupVisible = true;
        this.protocolTitle = arg2;
        let params = Object.assign(this.urlParams, {protocolId: arg});
        this.$http.get(ajaxUrl.protocolSearch,{ params: params }).then((res) => {
          this.protocolHtml = res.data.resData.content;
        })
      },
      readLimit() {
        this.popupVisible = true;
        this.$http.get(ajaxUrl.top5Help,{ params: { sectionCode: 'bankDeclare' }}).then((res) => {
          this.protocolHtml = res.data.resData.articleList[0].content;
          this.protocolTitle = res.data.resData.articleList[0].title
        })
      }
    },
    filters: {
      // 小数位数截两位，不四舍五入
      numTofixed(value) {
        //if (!value) return '';
        value = Math.floor(value * 100) / 100 ;
        value = Number(value.toString().match(/^\d+(?:\.\d{0,2})?/));
        return value;
      }
    },
    components: { PayPwd }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
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
        width: .2rem;
        vertical-align: middle;
        margin-right: .05rem;
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
