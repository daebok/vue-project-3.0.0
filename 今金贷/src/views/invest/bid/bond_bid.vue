<template>
  <div class="page payment-page" id="pageDetail">
    <mt-header class="bar-nav" title="投资" >
      <mt-button slot="left" icon="back" @click.native="goBack"></mt-button>
    </mt-header>
    <div v-show="showRes" class="margin-t-10" id="resHtml" data-tip="提交若有报错信息显示在这不跳页面"></div>
    <div v-show="!showRes" class="form-invest">
      <div class="form-group margin-t-20 margin-b-10">
        <label>剩余可购金额</label>
        <span class="main-color">{{ resdata.remainMoney | currency('',2)}}元</span>
      </div>
      <div class="form-group margin-b-20">
        <label>折溢价率</label>
        <span class="color-333">{{resdata.bondApr | currency('',2)}}%</span>
      </div>
      <div class="form-list">
        <label>投资金额(元)</label>
        <!-- 全额受让 -->
        <input v-if="buyStyle == 1" ref="inputMoney" type="number" v-model.trim="investMoney" readonly />
        <!-- 部分受让 -->
        <input v-else-if="buyStyle == 0" @keyup="getInvestData()" ref="inputMoney" type="number" v-model.trim="investMoney" placeholder="请输入投资金额"/>
      </div>
      <div class="form-group margin-b-20 earnings">
        <label v-if="resdata.bondApr >= 0" style="width: 20%;">溢价金额</label>
        <label v-else="resdata.bondApr < 0" style="width: 20%;">折价金额</label>
        <!-- <span class="main-color">{{ aprMoney | currency('',2) }}元</span> -->
        <span class="main-color">{{ changeMoney | currency('',2) }}元</span>
      </div>
      <div class="form-group margin-b-20 earnings">
        <label style="width: 20%;">预期收益</label>
        <!-- <span class="main-color">{{ interest | currency('',2) }}元</span> -->
        <span class="main-color">{{ preInterest | currency('',2) }}元</span>
      </div>
      <ul class="form-group-select">
        <li class="form-coupon">
          <label>可用余额</label>
          <span>{{ resdata.userMoney | currency('',2)}}元</span>
        </li>
      </ul>
      <div class="operator">
        <mt-button v-show="!investMoney" size="large" type="default" disabled>确认支付 0.00元</mt-button>
        <mt-button v-show="investMoney" size="large" type="danger" @click.prevent="submitForm">确认支付
          <!-- <span class="font-arial">{{ actualInvestMoney | currency('',2) }}</span>元 -->
          <span class="font-arial">{{ realPayAmount | currency('',2) }}</span>元
        </mt-button>
        <loading v-if="false" ></loading>
      </div>
      <p class="tips">
        <span>点击即同意<i class="main-color" @click="readProtocol">《投资协议》</i></span><br/>
        <i class="main-color">*</i>点击确认支付后请在<i class="main-color">{{ resdata.orderEnableTime }}</i>内完成支付，若超出时间未完成，将取消订单
      </p>
    </div>
    <div class="protocol-con">
      <mt-popup v-model="popupVisible" position="bottom" class="mint-popup-3" :modal="false">
          <mt-header class="bar-nav" title="投资协议" >
            <mt-button slot="right" @click.native="popupVisible = false">关闭</mt-button>
          </mt-header>
          <div class="con" :style="{ height: protocolHeight + 'px' }" v-html="protocolHtml"></div>
      </mt-popup>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import Loading from '../../../components/Loading.vue'
  import qs from 'qs'
  export default {
    name: 'investBid',
    data(){
      return {
        investMoney: '', //受让金额
        changeMoney: 0, //折溢价金额
        preInterest: 0, //预期收益
        realPayAmount: 0, //实际支付金额
        remainMoney: 0, //最大可投金额
        aprMoney: 0,
        actualInvestMoney: 0,
        resdata: '',
        investResdata: '',
        interest: '0.00',
        showRes: false,
        protocolHeight: 0,
        params: {
          bondId: this.$route.params.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        popupVisible: false,
        protocolHtml: '',
        buyStyle: 0, //默认部分受让
        sellStyle: 0 //默认部分转让
      }
    },
    created(){
      this.$nextTick(() => {
        this.protocolHeight = document.documentElement.clientHeight - 40;
      })
      this.$http.post(ajaxUrl.initBond, qs.stringify(this.params)).then((res) => {
        this.resdata = res.data.resData
        this.remainMoney = res.data.resData.remainMoney //最大可投金额
        this.buyStyle = res.data.resData.buyStyle //是否全额受让
        this.sellStyle = res.data.resData.sellStyle //是否全额转让
        if(this.buyStyle == 1){
          this.investMoney = this.resdata.remainMoney //全额受让
        }
        this.interest = this.resdata.totalInterest
        this.aprMoney = this.investMoney * Math.abs(this.resdata.bondApr) / 100
        this.actualInvestMoney = this.resdata.totalrealPayAmount
      })
    },
    methods: {
      // getrealPayAmount(){ //实时计算实际收入
      //   let investParams = {
      //     amount: this.investMoney, //输入金额
      //     id: this.$route.params.projectId, //债券id
      //     userId: this.$store.state.user.userId,
      //     __sid: this.$store.state.user.__sid
      //   }
      //   //受让实时计算
      //   this.$http.post(ajaxUrl.getInvestData, qs.stringify(investParams)).then((res) => {
      //     this.changeMoney = res.data.resData.changeMoney;
      //     this.preInterest = res.data.resData.preInterest;
      //     this.realPayAmount = res.data.resData.realPayAmount;
      //   })
      // },
      getInvestData(){
        //输入金额不能大于最大可投
        if(this.investMoney > this.remainMoney){
          this.investMoney = this.remainMoney;
        }
        //this.getrealPayAmount();//计算实际支付
        let investParams = {
          amount: this.investMoney, //输入金额
          id: this.$route.params.projectId, //债券id
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
        //受让实时计算
        this.$http.post(ajaxUrl.getInvestData, qs.stringify(investParams)).then((res) => {
          this.changeMoney = res.data.resData.changeMoney;
          this.preInterest = res.data.resData.preInterest;
          this.realPayAmount = res.data.resData.realPayAmount;
        })
      },
      // 点击返回
      goBack(){
        sessionStorage.investMoney = ''
        if(this.$route.query.from){
          this.$router.push('/bondDetail/'+this.$route.params.projectId+'?from=msg_result')
        }else if(this.showRes){  // 点击返回显示表单 reset数据
          this.showRes = false
          this.investMoney = ''
          location.reload()
        }
        else{
          this.$router.go(-1)
        }
      },
      readProtocol(){
        this.popupVisible = true
        let params = Object.assign(this.params, {protocolId: this.resdata.protocolId})
        this.$http.get(ajaxUrl.getBondProtocolContent, {params: params}).then((res) => {
          this.protocolHtml = res.data.resData.content
        })
      },
      submitForm(){
        let lowestMoney = parseFloat(this.resdata.bondLowestMoney)
        if (this.resdata.remainMoney < (2*lowestMoney) && this.investMoney != this.resdata.remainMoney) {
          this.$toast('剩余债权小于2倍的最小投资金额，则需要全部受让')
          this.investMoney = this.resdata.remainMoney;
          //this.getrealPayAmount();//计算实际支付
          return
        }
        if( this.investMoney < lowestMoney) { //小于起投金额
          if (this.resdata.remainMoney >= lowestMoney) { // 当剩余可投金额大于等于最小可投金额时判断
            let str = '最低起投金额为' + lowestMoney + '元'
            this.$toast(str)
            return
          }
        }
        if(this.investMoney > this.resdata.userMoney){
          this.$messagebox({
            title: ' ',
            cancelButtonText: '取消',
            confirmButtonText: '去充值',
            showCancelButton: true,
            message: '您的可用余额不足'
          }).then(action => {
            if(action == 'confirm'){
              this.$router.push('/account/recharge?from=bondBid&id='+this.$route.params.projectId)
            }
          });
          return ;
        }
        let params = Object.assign({}, this.params, {
          amount: this.investMoney,
          receivedAccount: this.realPayAmount,
          bondToken: this.resdata.bondToken,
          investId: this.resdata.investId,
          projectId: this.resdata.projectId,
        })
        this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
        this.$http.post(ajaxUrl.doBondInvest, qs.stringify(params)).then((res) => {
          this.$indicator.close()
          if (res.data.resData == '') { //主要是有请勿重复提交信息提示
            this.$toast(res.data.resMsg)
          } else {
            this.showRes = true; // 隐藏表单
            // console.log(res.data)
            if(res.data.indexOf('系统提示信息') > 0 && res.data.indexOf('处理中') == -1){
              document.querySelector('#resHtml').innerHTML = res.data
            } else {
              document.write(res.data)
            }
            sessionStorage.investBidName = this.resdata.projectName
            sessionStorage.investBidMoney = this.investMoney
            sessionStorage.investBidId = this.$route.params.projectId
          }
        })
      }
    },
    components: { Loading }
  }
</script>
<style scoped>
  .form-invest{ width: 100%;}
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
    line-height: .3rem;
    margin-top: .07rem;
  }
  .earnings{
    margin: .13rem 0;
  }
  .form-group-select{
    width: 100%;
    padding: 0 5%;
    background: #fff;
    float: left;
  }
  .form-group-select li{
    height: .48rem;
    line-height: .48rem;
  }
  .form-group-select li span{
    float: right;
    font-family: arial;
    color: #666;
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
    padding: .3rem .15rem .15rem;
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
