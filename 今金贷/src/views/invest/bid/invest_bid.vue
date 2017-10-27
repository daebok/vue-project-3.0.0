<template>
  <div class="page payment-page" id="pageDetail">
    <mt-header class="bar-nav" title="投资" >
      <mt-button slot="left" icon="back" @click.native="goBack"></mt-button>
    </mt-header>
    <div v-show="showRes" class="margin-t-10" id="resHtml" data-tip="给投资提交返回信息使用"></div>
    <div v-show="!showRes" class="form-invest">
      <div v-if="resdata.saleStyle==1" class="form-group margin-t-20 margin-b-10">
        <label>剩余可购金额</label>
        <span class="main-color">{{ resdata.remainAccount | currency('',2)}}元</span>
      </div>
      <div v-if="resdata.saleStyle==2" class="form-group margin-t-20 margin-b-10">
        <label>剩余可购份数</label>
        <span class="main-color">{{ resdata.remainAccount/resdata.copyAccount}}份</span>
      </div>
      <div class="form-group margin-b-20" v-if="!times2">
        <label>投资区间</label>
        <span v-if="resdata.saleStyle==1 && userCanInvest > resdata.lowestAccount" class="color-333">{{ resdata.lowestAccount | currency('',2)}}元 - {{ userCanInvest | currency('',2)}}元</span>
        <span v-if="resdata.saleStyle==2 && userCanInvest > resdata.lowestCopies" class="color-333">{{ resdata.lowestCopies}}份 - {{ userCanInvest}}份</span>
      </div>
      <div v-if="resdata.saleStyle==2" class="form-list">
        <label>投资份数(份)</label>
        <input type="tel" pattern="\d*" name="" v-model.trim="investMoney" class="invest-money"  :placeholder="'每份'+resdata.copyAccount+'元'" :readonly="times2" @input="inputNum"/>
      </div>
      <div v-else class="form-list">
        <label>投资金额(元)</label>
        <input ref="inputMoney" type="tel" pattern="\d*" v-model.trim="investMoney" class="invest-money" id="invest-money" :placeholder="resdata.stepAccount > 0 ? '递增金额为'+resdata.stepAccount+'元': '请输入金额'" :readonly="times2" @input="inputNum" />
      </div>
      <div class="form-group margin-b-20 earnings">
        <label style="width: 30%;">预期收益</label>
        <span class="main-color">{{ interest | currency('',2) }}元</span>
        <p v-if="upAprInterest > 0">(其中加息券收益{{ upAprInterest | currency('',2)}}元)</p>
      </div>
      <ul class="form-group-select">
        <li v-if="resdata.redEnvelopeUseful == 1 && resdata.ifRedEnvelopestatus == 1" class="form-red aui-border-b border-color" @click="selectCoupon('investRedpacket')">
          <label>红包</label>
          <span class="margin-r-20">{{ redpacketNum }}</span>
          <img src="../../../assets/images/public/arrow_right.png" class="right-go"/>
        </li>
        <li v-else class="form-red aui-border-b border-color disabled">
          <label>红包</label>
          <span v-if="resdata.ifRedEnvelopestatus == '' || resdata.ifRedEnvelopestatus == 0" class="margin-r-20">无可用红包</span>
          <span v-else class="margin-r-20">该产品不可使用红包</span>
          <img src="../../../assets/images/public/arrow_right.png" class="right-go"/>
        </li>

        <li v-if="resdata.additionalRateUseful == 1 && resdata.ifAdditionalRate == 1" class="form-coupon aui-border-b border-color" @click="selectCoupon('investCoupon')">
          <label>加息券</label>
          <span class="margin-r-20">{{selected_upApr}}</span>
          <img src="../../../assets/images/public/arrow_right.png" class="right-go"/>
        </li>
        <li v-else class="form-coupon aui-border-b border-color disabled">
          <label>加息券</label>
          <span v-if="resdata.ifAdditionalRate == '' || resdata.ifAdditionalRate == 0" class="margin-r-20">无可用加息券</span>
          <span v-else class="margin-r-20">该产品不可使用加息券</span>
          <img src="../../../assets/images/public/arrow_right.png" class="right-go"/>
        </li>
        <li class="form-coupon">
          <label>可用余额</label>
          <span>{{ resdata.userMoney | currency('',2)}}元</span>
        </li>
      </ul>
      <div class="operator">
        <mt-button v-show="!investMoney" size="large" type="default" disabled>确认支付 0.00元</mt-button>
        <mt-button v-show="investMoney" size="large" type="danger" @click.native.prevent="submitForm" :disabled="resdata == ''">确认支付
          <!-- 红包返现开关 -->
          <!-- 进行返现,投资额为实际金额 -->
          <span v-if="features.EnableReturnCashFeature" class="font-arial">{{ investMoney | currency('',2) }}</span>
          <!-- 不进行返现,投资额为减去使用红包后金额 -->
          <span v-else class="font-arial">{{ actualInvestMoney | currency('',2) }}</span>
          元
        </mt-button>
        <loading v-if="false" ></loading>
      </div>
      <p class="tips">
        <span class="allIn" v-show="times2">剩余金额小于等于起投金额的两倍，请全部投资</span><br/>
        <span>点击按钮，表示您同意<i class="main-color" @click="readProtocol">《投资协议》</i></span><br/>
        <i class="main-color">*</i>点击确认支付后请在<i class="main-color">{{ resdata.orderEnableTime }}</i>内完成支付，若超出时间未完成，将取消订单
      </p>
    </div>
    <div class="protocol-con">
      <mt-popup v-model="popupVisible" position="bottom" class="mint-popup-3" :modal="false">
        <mt-header class="bar-nav" title="投资协议" >
          <mt-button slot="right" @click="popupVisible = false">关闭</mt-button>
        </mt-header>
        <div class="con" :style="{ height: protocolHeight + 'px' }" v-html="protocolHtml"></div>
      </mt-popup>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import * as features from '../../../features.config';
  import Loading from '../../../components/Loading.vue'
  import qs from 'qs'
  export default {
    data(){
      return {
        investMoney: sessionStorage.investMoney,
        resdata: '',
        times2: false,
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
        protocolHtml: '',
        showRes: false,
        protocolHeight: 0,
        features: features // 将配置项features赋值给features
      }
    },
    computed: {
      actualInvestMoney(){
        let red_num = parseInt(this.redpacketNum)
        if(this.resdata.saleStyle == 2) { //判断是否份额购买
          if(!isNaN(red_num)){
            return this.investMoney*this.resdata.copyAccount - red_num
          }else{
            return this.investMoney*this.resdata.copyAccount
          }
        }else{
          if(!isNaN(red_num)){
            return this.investMoney - red_num
          }else{
            return this.investMoney
          }
        }
      }
    },
    created(){
      if (/Android/gi.test(navigator.userAgent)) { // 解决安卓机调用键盘后页面重绘优惠券页面
        window.addEventListener('resize', () => {
         if (document.activeElement.tagName == 'INPUT'){
            this.resize = true
          }
        })
      }
      this.$nextTick(() => {
        this.protocolHeight = document.documentElement.clientHeight - 40;
      })
      if(this.$route.query.from){  //提交判断是否为变现提交
        this.submitUrl = ajaxUrl.doRealizeInvest
      }
      if(this.investMoney == undefined || this.investMoney == ''){
        this.redpacketNum = '请选择红包'
        this.selected_upApr = '请选择加息券'
      }
      if(this.$store.state.redpack.selected_total == 0){this.redpacketNum = '请选择红包'}
      if(sessionStorage.selected_upApr == 0){this.selected_upApr = '请选择加息券'}
      this._init()
    },
    watch: {
      investMoney (newV, oldV) {
        var pattern = /^[0-9]*$/
        // 只能输入数字
        if(!pattern.test(newV)){
          this.investMoney = oldV
          return
        }
        newV = parseFloat(newV);
        if(newV){
          if(newV == 0){ this.investMoney = ''}
          if(newV > this.userCanInvest){
            if(this.resdata.saleStyle == 2){
              this.$toast('您当前的最高投资份数为'+ this.userCanInvest +'份')
            }else{
              this.$toast('您当前的最高投资金额为'+ this.userCanInvest +'元')
            }
            this.investMoney = this.userCanInvest
          }
          sessionStorage.investMoney = newV
          this.redpacketNum = '请选择红包'
          this.$store.dispatch('selectRedpacket', [])
          this.$store.dispatch('selectRedpacketTotal', 0)
          this.selected_upApr = '请选择加息券'
          sessionStorage.selected_coupon = ''
          sessionStorage.selected_upApr = '0.00'
          this.investInterest(newV, this.selected_upApr)
        }else{
          sessionStorage.investMoney = ''
          this.interest = '0.00'
        }
      }
    },
    methods: {
      // 点击返回
      goBack(){
        sessionStorage.investMoney = ''
        if(this.$route.query.from){
          if(this.$route.query.prevPage){
            this.$router.push('/realizeDetail/'+this.$route.params.projectId+'?from=' + this.$route.query.from)
          }else{
            this.$router.push('/investDetail/'+this.$route.params.projectId+'?from=' + this.$route.query.from)
          }
        }else if(this.showRes){
          // 点击返回显示表单 reset数据
          this.showRes = false
          this.investMoney = ''
          this.interest = '0.00'
          location.reload()
        }else{
          this.$router.go(-1)
        }
      },
      _init() {
        let getParams = Object.assign({investType: 'invest'}, this.urlParams)
        this.$http.get(ajaxUrl.initInvest, {params:getParams}).then((res) => {
          this.resdata = res.data.resData
          this.userCanInvest = this.resdata.userCanInvestAmount
          if(this.resdata.saleStyle == 2){  // 用户最多可投份额
            this.userCanInvest = this.resdata.userCanInvestAmount/this.resdata.copyAccount
          }
          // 用户已达到投资上限
          if(!this.userCanInvest || this.userCanInvest == 0){
            this.$messagebox({
              title: ' ',
              showCancelButton: false,
              message: '您已达到投资上限，无法投资'
            }).then(action => {
              this.goBack()
            });
          }
          // 产品剩余金额 <= 起投金额的2倍
          if(this.resdata.remainAccount <= (this.resdata.lowestAccount * 2)){
            this.times2 = true; //投资区间隐藏以及输入框不可输入值
            this.investMoney = this.resdata.remainAccount
            if(this.resdata.saleStyle == 2){  // 投资为份额时
              this.investMoney = this.resdata.remainAccount/this.resdata.copyAccount
            }
          }
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
                    from: 'investBid',
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
                this.goBack()
              });
            }
          }
          if(this.investMoney != ''){ //不为空时初始化
            this.investInterest(this.investMoney, this.selected_upApr)
          }
        })
      },
      inputNum(evt){
        // let val = evt.target.value
        // this.investMoney = val.replace(/\D*$/g, '')
      },
      calcuInterest(money,apr,timeLimit,style){
        var m_par = parseFloat(apr/100/12);//月利率
        //等额本息
        if(style == 1){
          var m_total = parseFloat((money*m_par*Math.pow((m_par+1),timeLimit))/(Math.pow((m_par+1),timeLimit)-1)) //月还款本息
          this.interest = (m_total * timeLimit) - money
        }else if(style == 4){ //等额本金
          this.interest = (timeLimit + 1) * money * m_par / 2
        }
        else{
          this.interest = money * this.resdata.totalInterest / this.resdata.account
        }
      },
      //预期收益
      investInterest(money, upApr=0){
        upApr = parseFloat(upApr) || 0
        if(this.resdata.saleStyle == 2){
          money = money * this.resdata.copyAccount
        }
        money = money || 0
        // this.calcuInterest(money,this.resdata.apr,this.resdata.timeLimit,this.resdata.repayStyle) // 调用接口，前端不计算利息
        // 选择加息券后
        let reqParams = Object.assign({amount: money, upApr: upApr}, this.urlParams)
        this.$http.post(ajaxUrl.interest, qs.stringify(reqParams)).then((res) => {
          if(parseFloat(this.selected_upApr) > 0) {
            this.upAprInterest = res.data.resData.addAprInterest
          }
          this.interest = res.data.resData.interest
        })
      },
      // 选择红包加息券
      selectCoupon(arg){
        var lowest = this.resdata.saleStyle == 2 ? this.resdata.lowestCopies : this.resdata.lowestAccount
        if(!this.investMoney){
          let str = this.resdata.saleStyle == 2 ? '请输入投资份数' : '请输入投资金额'
          this.$toast(str)
        }else if(this.investMoney < lowest && (this.resdata.remainAccount >= this.resdata.lowestAccount)){ //小于起投金额
            let str = this.resdata.saleStyle == 2 ? '最低起投份数为'+this.resdata.lowestCopies+'份' : '最低起投金额为' + this.resdata.lowestAccount+'元'
            this.$toast(str)
        } else {
          if(this.resdata.stepAccount){ // 判断是否有递增金额 金额必须为起投金额+递增金额的整数倍
            var times = (this.investMoney - this.resdata.lowestAccount)%this.resdata.stepAccount
            if(times > 0){
              this.$toast('金额必须为起投金额+递增金额的整数倍')
              return
            }
          }
          //红包可用最大限额
          let limit_num = this.resdata.redEnvelopeRate * Number(this.investMoney)/100
          if(this.resdata.saleStyle == 2){
            limit_num = limit_num*this.resdata.copyAccount
          }
          sessionStorage.redpack_max_limit = limit_num
          if(this.resize) { // 安卓机调用键盘有页面重绘bug
            setTimeout(() => {
              this.$router.push({name: arg,query: {copyAccount: this.resdata.copyAccount, rate: this.resdata.redEnvelopeRate}})
            }, 600)
          }else{
            this.$router.push({name: arg,query: {copyAccount: this.resdata.copyAccount, rate: this.resdata.redEnvelopeRate}})
          }
        }
      },
      submitForm(){
        let amount = this.resdata.saleStyle == 2 ? this.investMoney*this.resdata.copyAccount : this.investMoney;
        if( amount < this.resdata.lowestAccount) { //小于起投金额
          if (this.resdata.remainAccount >= this.resdata.lowestAccount) { // 当剩余可投金额大于等于最小可投金额时判断
            let str = this.resdata.saleStyle == 2 ? '最低起投份数为' + this.resdata.lowestCopies + '份' : '最低起投金额为' + this.resdata.lowestAccount + '元'
            this.$toast(str)
            return;
          }
        }
        if(this.resdata.stepAccount){ // 判断是否有递增金额 金额必须为起投金额+递增金额的整数倍
          var times = (this.investMoney - this.resdata.lowestAccount)%this.resdata.stepAccount
          if(times > 0){
            this.$toast('金额必须为起投金额+递增金额的整数倍')
            return
          }
        }
        // 用实际投资金额判断
        if(this.actualInvestMoney > this.resdata.userMoney){
          //let str = this.resdata.saleStyle == 2 ? '您的购买份数大于可用余额' : '您的投资金额大于可用余额'
          let str = '您的可用余额不足'
          this.$messagebox({
            title: ' ',
            cancelButtonText: '重新输入',
            confirmButtonText: '去充值',
            showCancelButton: true,
            message: str
          }).then(action => {
            if(action == 'cancel'){
              this.investMoney = ''
              this.$refs.inputMoney.focus()
            }else{
              if(this.$route.query.prevPage){
                this.$router.push({name: 'recharge',query:{from:'realizeBid',id:this.urlParams.projectId}})
              }else{
                this.$router.push({name: 'recharge',query:{from:'investBid',id:this.urlParams.projectId}})
              }
            }
          })
          return ;
        }
        let rateCouponId = sessionStorage.selected_coupon
        let redEnvelopeIds = this.$store.state.redpack.selectedList.join(',')
        let params = {
          amount: amount,
          redEnvelopeIds: redEnvelopeIds,
          rateCouponId:rateCouponId,
          investToken: this.resdata.investToken,
          projectId: this.$route.params.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
        }
        this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
        this.$http.post(ajaxUrl.doInvest, qs.stringify(params)).then((res) => {
          if(res.data.resData == ''){ //主要是有请勿重复提交信息提示
            this.$toast(res.data.resMsg)
          }else{
            this.showRes = true; // 隐藏表单
            // console.log(typeof res.data)
            // if(res.data.indexOf('系统提示信息') > 0 && res.data.indexOf('处理中') == -1){
              // document.querySelector('#resHtml').innerHTML = res.data
            // }else{
              document.write(res.data)
            // }
            sessionStorage.investMoney = ''
            sessionStorage.investBidName = this.resdata.projectName
            sessionStorage.investBidMoney = amount
            sessionStorage.investBidId = this.$route.params.projectId
          }
        })
      },
      readProtocol(){
        this.popupVisible = true
        let params = Object.assign(this.urlParams, {protocolId: this.resdata.protocolId})
        this.$http.get(ajaxUrl.protocolSearch, {params: params}).then((res) => {
          this.protocolHtml = res.data.resData.content
        })
      }
    },
    components: { Loading }
  }
</script>
<style scoped>
  .disabled {color:#999;}
  .form-group-select li.disabled span {color:#999;}
  .form-invest{
    width: 100%;
  }
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
  .tips .allIn{
    color: #F95A28;
    display: inline-block;
    width: 100%;
    margin-bottom: 0.1rem;
  }
</style>
