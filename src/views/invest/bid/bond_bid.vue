<template>
  <div class="page payment-page" id="pageDetail">
    <mt-header class="bar-nav" title="投资" >
      <mt-button slot="left" icon="back" @click.native="goBack"></mt-button>
    </mt-header>
    <div class="form-invest">
      <div class="form-group margin-t-20 margin-b-10">
        <label>剩余可购金额</label>
        <span class="main-color">{{ resdata.remainMoney | currency('',2)}}元</span>
      </div>
      <div class="form-group margin-b-20">
        <label>折溢价率</label>
        <span class="color-333">{{resdata.bondApr | currency('',2)}}%</span>
      </div>
      <form :action="submitUrl" id="formData" method="post">
      <div class="form-list">
        <label>投资金额(元)</label>
        <input ref="inputMoney" type="number" v-model.trim="investMoney" readonly />
      </div>
      <div class="form-group margin-b-20 earnings">
        <label v-if="resdata.bondApr >= 0" style="width: 20%;">溢价金额</label>
        <label v-else="resdata.bondApr < 0" style="width: 20%;">折价金额</label>
        <span class="main-color">{{ aprMoney | currency('',2) }}元</span>
      </div>
      <div class="form-group margin-b-20 earnings">
        <label style="width: 20%;">预期收益</label>
        <span class="main-color">{{ interest | currency('',2) }}元</span>
      </div>
      <ul class="form-group-select">
        <li class="form-coupon">
          <label>可用余额</label>
          <span>{{ resdata.userMoney | currency('',2)}}元</span>
        </li>
      </ul>
      <div class="operator">
        <mt-button v-show="!investMoney" size="large" type="default" disabled>确认支付 0.00元</mt-button>
        <mt-button v-show="investMoney" size="large" type="danger" @click.native.prevent="submitForm">确认支付
          <span class="font-arial">{{ actualInvestMoney | currency('',2) }}</span>元
        </mt-button>
        <loading v-if="false" ></loading>
      </div>
        <input type="hidden" name="amount" :value="investMoney">
        <input type="hidden" name="receivedAccount" :value="actualInvestMoney">
        <input type="hidden" name="bondToken" :value="resdata.bondToken">
        <input type="hidden" name="investId" :value="resdata.investId">
        <input type="hidden" name="projectId" :value="resdata.projectId">
        <input type="hidden" name="bondId" :value="$route.params.projectId">
        <input type="hidden" name="userId" :value="$store.state.user.userId">
        <input type="hidden" name="__sid" :value="$store.state.user.__sid">
      </form>
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
          <div class="content" v-html="protocolHtml"></div>
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
        investMoney: 0,
        aprMoney: 0,
        actualInvestMoney: 0,
        resdata: '',
        interest: '0.00',
        submitUrl: ajaxUrl.doBondInvest,
        params: {
          bondId: this.$route.params.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        popupVisible: false,
        protocolHtml: ''
      }
    },
    created(){
      this.$http.post(ajaxUrl.initBond, qs.stringify(this.params)).then((res) => {
        this.resdata = res.data.resData
        this.investMoney = this.resdata.remainMoney
        this.interest = this.resdata.totalInterest
        this.aprMoney = this.investMoney * Math.abs(this.resdata.bondApr) / 100
        this.actualInvestMoney = this.resdata.totalrealPayAmount
      })
    },
    methods: {
      // 点击返回
      goBack(){
        sessionStorage.investMoney = ''
        if(this.$route.query.from){
          this.$router.push('/bondDetail/'+this.$route.params.projectId+'?from=msg_result')
        }else{
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
        document.getElementById('formData').submit()
        sessionStorage.investBidName = this.resdata.projectName
        sessionStorage.investBidMoney = this.investMoney
        sessionStorage.investBidId = this.$route.params.projectId
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
