<template>
  <div>
    <div class="form-invest">
      <p class="head-p">转入粮票宝</p>
      <div class="form-list">
        <label>转入金额(元)</label>
        <input ref="inputMoney" type="tel" pattern="\d*" v-model.trim="investMoney" class="invest-money" id="invest-money" placeholder="请输入金额" @input="inputNum" />
      </div>
      <div class="form-group margin-b-20 earnings">
        <label style="font-size: .13rem;">预期收益</label>
        <span class="main-color">{{ interest | currency('',2) }}元</span>
      </div>
      <!--<div class="group-head aui-border-b">使用优惠</div>
      <ul class="form-group-select">
        <li v-if="redEnvelopeUseful" class="form-red border-color" @click="selectCoupon">
          <label>红包</label>
          <span class="margin-r-20">{{ redpacketNum }}</span>
          <img src="../../assets/images/public/arrow_right.png" class="right-go"/>
        </li>
        <li v-else class="form-red border-color disabled" @click="selectCoupon">
          <label>红包</label>
          <span class="margin-r-20">当前无可用红包</span>
          <img src="../../assets/images/public/arrow_right.png" class="right-go"/>
        </li>
      </ul>-->
      <div class="group-head margin-t-10 aui-border-b">支付方式</div>
      <ul class="form-group-select">
        <li class="pay-style aui-border-b">
          <label>
            <input type="radio" class="checkbox hide" value="2" name="bankPay" v-model="bankPay" />
            <em></em>
            <img class="bank-logo" :src="resdata.picPath">{{resdata.bankName}} <i class="color-999">{{resdata.hideBankNo}}</i>
          </label>
          <span class="tip-color" @click="readLimit">查看限额</span>
        </li>
        <li class="pay-style">
          <label>
            <input type="radio" class="checkbox hide" value="1" name="bankPay" v-model="bankPay" />
            <em></em>
            钱包余额</label>
          <span>{{ resdata.useMoney | currency('',2)}}元</span>
        </li>
      </ul>
      <p class="interest-tip">预计{{resdata.preProfitTime}}产生收益，{{resdata.preInterestTime}}收益到账</p>
      <div class="operator">
        <mt-button v-if="investMoney && switch_status" size="large" type="danger" @click.prevent="openPwd" :disabled="resdata == ''">确认转入</mt-button>
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
          <i v-for="item in resdata.proList" @click="readProtocol(item.id, item.name)">《{{ item.name }}》</i>
        </span><br/>
      </p>
    </div>
    <div class="form-prompt">
      <p class="prompt-title margin-b-10">温馨提示：</p>
      <p>根据您阅读并同意的货基申购相关协议内容自动为您申购嘉实活期宝。本产品由嘉实基金管理公司发行和管理，中国光大银行不对其承担产品的投资，兑付和风险责任</p>
    </div>
    <pay-pwd @submit="submitForm" ref="pwd" v-model="payPwd" :tipMoney="actualInvestMoney" tipTxt="转入金额"></pay-pwd>
    <div class="protocol-con">
      <mt-popup v-model="popupVisible" position="bottom" class="mint-popup-3" :modal="false">
        <mt-header class="bar-nav" :title="protocolTitle" >
          <mt-button slot="right" @click="popupVisible = false">关闭</mt-button>
        </mt-header>
        <div class="con" :style="{ height: protocolHeight + 'px' }"><section v-html="protocolHtml"></section></div>
      </mt-popup>
    </div>
  </div>
</template>
<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config'
  import PayPwd from '../../components/PayPwd.vue'
  import base64code from '../../mixins/base64code.js'; // 引入密码加密插件
  import qs from 'qs'
  export default {
    name: 'fundIn',
    data(){
      return {
        investMoney: '',
        resdata: '',
        bankPay: sessionStorage.bankPay || 2,
        payPwd: '',
        urlParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        interest: '0.00',
        redpacketNum: this.$store.state.redpack.selected_total + '元',
        popupVisible: false,
        protocolHtml: '',
        redEnvelopeUseful: false, // 判断红包是否可用
        protocolTitle: '投资协议',
        switch_status: true, //是否同意注册协议，true已同意，false不同意
        protocolHeight: 0
      }
    },
    computed: {
      actualInvestMoney(){
        let red_num = parseInt(this.redpacketNum)
        if(!isNaN(red_num)){
          return this.investMoney - red_num
        }else{
          return this.investMoney
        }
      }
    },
    created(){
      this.$nextTick(() => {
        this.protocolHeight = document.documentElement.clientHeight - 40;
      })
      if (/Android/gi.test(navigator.userAgent)) { // 解决安卓机调用键盘后页面重绘优惠券页面
        window.addEventListener('resize', () => {
          if (document.activeElement.tagName == 'INPUT'){this.resize = true}
      })
      }
      if(this.investMoney == undefined || this.investMoney == ''){
        this.redpacketNum = '请选择红包'
      }
      if(this.$store.state.redpack.selected_total == 0){this.redpacketNum = '请选择红包'}
      this.$http.get(ajaxUrl.perchasePage, {params:this.urlParams}).then((res) => {
        this.resdata = res.data.resData
      if(this.resdata == ''){
        if(res.data.resMsg.match('未支付订单')){
          this.$messagebox({
            title: ' ',
            cancelButtonText: '取消',
            confirmButtonText: '去完成',
            showCancelButton: true,
            message: res.data.resMsg
          }).then(action => {
            if(action == 'cancel'){
            this.goBack()
          }else if(action == 'confirm'){
            this.$router.push({name: 'myInvest',query: {
              from: 'fundIn',
              tab: 'tab-con2',
              uuid: this.$route.params.projectId
            }})
          }
        });
        }else{
          this.$messagebox({
            title: ' ',
            showCancelButton: false,
            message: res.data.resMsg
          }).then(action => {
            // this.goBack()
          });
        }
      }
      if(this.investMoney != ''){ //不为空时初始化
        this.investInterest(this.investMoney)
        this.queryCoupon(this.investMoney)
      }
    })
    },
    watch: {
      bankPay(newV, oldV) {
        sessionStorage.bankPay = newV;
      }/*,
      investMoney (newV, oldV) {
        newV = parseFloat(newV);
        if(newV){
          if(newV == 0){ this.investMoney = ''}
          sessionStorage.investMoney = newV
          this.redpacketNum = '请选择红包'
          this.$store.dispatch('selectRedpacket', [])
          this.$store.dispatch('selectRedpacketTotal', 0)
          sessionStorage.selected_coupon = ''
          this.investInterest(newV)
          this.queryCoupon(newV)
        }else{
          sessionStorage.investMoney = ''
          this.interest = '0.00'
        }
      }*/
    },
    mixins: [ base64code],
    methods: {
      // 点击返回
      goBack(){
        sessionStorage.investMoney = ''
        if(this.$route.query.from){

        }else{
          this.$router.go(-1)
        }
      },
      inputNum(evt){
        let val = evt.target.value
        this.investMoney = val.replace(/\D*$/, '')
      },
      //预期收益
      investInterest(money){
        money = money || 0
        let reqParams = Object.assign({money: money}, this.urlParams)
        this.$http.post(ajaxUrl.calculate, qs.stringify(reqParams)).then((res) => {
          this.interest = res.data.resData.profit
      })
      },
      //查看优惠券是否可用
      queryCoupon(money){
        money = money || 0
        let reqParams = Object.assign({tenderMoney: money, type: 'fund'}, this.urlParams)
        this.$http.post(ajaxUrl.queryCoupon, qs.stringify(reqParams)).then((res) => {
          this.redEnvelopeUseful = res.data.resData.hasCoupon
      })
      },
      // 选择红包加息券
      selectCoupon(){
        if(!this.investMoney){
          this.$toast('请输入投资金额')
        }else {
          //红包可用最大限额
          let limit_num = this.resdata.redEnvelopeRateMax * Number(this.investMoney)/100
          sessionStorage.redpack_max_limit = limit_num
          if(this.resize) { // 安卓机调用键盘有页面重绘bug
            setTimeout(() => {
              this.$router.push({path: '/investDetail/fund/investBid/redpacket',query: { rate: this.resdata.redEnvelopeRateMax}})
          }, 600)
          }else{
            this.$router.push({path: '/investDetail/fund/investBid/redpacket',query: { rate: this.resdata.redEnvelopeRateMax}})
          }
        }
      },
      submitForm(pwd){
        let amount = this.actualInvestMoney;
        let rateCouponId = sessionStorage.selected_coupon
        let redEnvelopeIds = this.$store.state.redpack.selectedList.join(',')
        let params = {
          money: amount,
          agree: 1,  // 协议
          redEnvelopeIds: redEnvelopeIds,
          type: this.bankPay.toString(),
          payPwd: this.base64Encode(this.payPwd),
          perchaseToken: this.resdata.perchaseToken,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
        }
        this.$http.post(ajaxUrl.perchase, qs.stringify(params)).then((res) => {
          if(res.data.resData == ''){ //主要是有请勿重复提交信息提示
          this.$refs.pwd.close() // 关闭支付密码区
          this.$toast(res.data.resMsg)
        }else{
          sessionStorage.investMoney = ''
          sessionStorage.fundIn_resdata = JSON.stringify(res.data.resData)
          this.$router.push('/fund/msg?from=in')
        }
      })
      },
      openPwd() {
        // 用实际投资金额判断
        if((this.bankPay == 1) && (this.actualInvestMoney > this.resdata.useMoney)){
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
        if (this.investMoney < this.resdata.minAmount) {
          this.$toast('粮票宝单笔申购最低金额' + this.resdata.minAmount + '元');
          return false;
        }
        this.$refs.pwd.show() // 打开支付密码区
      },
      readProtocol(id, name){
        this.popupVisible = true;
        this.protocolHtml = '';
        this.protocolTitle = name;
        this.$http.get(ajaxUrl.registerProtocolDetail,{ params: { protocolId: id }}).then((res) => {
          this.protocolHtml = res.data.resData.content;
        })
      },
      readLimit() {
        this.popupVisible = true;
        this.$http.get(ajaxUrl.top5Help, {params: {sectionCode: 'bankDeclare'}}).then((res) => {
          this.protocolHtml = res.data.resData.articleList[0].content;
          this.protocolTitle = res.data.resData.articleList[0].title
        })
      }
    },
    components: { PayPwd }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  .head-p {margin: .1rem 0; color: #666; font-size: .13rem; padding-left: .15rem;}
  .interest-tip {float:left; margin: .1rem 0; color: #999; font-size: .13rem; padding-left: .15rem;}
  .protocol-color { color: #67748d; }
  .disabled {color:#999;}
  .form-group-select li.disabled span {color:#999;}
  .form-group{
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
  .right-go {
    position: absolute;
    right: .15rem;
    top: .17rem;
    height: .14rem;
    width: .14rem;
  }
  .operator{
    float: left;
    width: 100%;
    padding: 0 .15rem .15rem;
  }
  .tips{
    float: left;
    width: 100%;
    padding: 0 5%;
    font-size: .12rem;
    color: #999;
    line-height: .2rem;
  img {width: .13rem;  vertical-align: -2px;}
  }
  .form-prompt{
    float: left;
    width: 100%;
    margin-top: 0.1rem;
  }
</style>
