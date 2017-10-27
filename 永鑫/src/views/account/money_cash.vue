<template>
  <div class="page" id="realization">
    <div class="realization">
      <div class="realization-top">
        <ul>
          <li class="realization-left">
            <p class="margin-t-15 margin-b-10">所持资产</p>
            <p class="font-arial">{{resdata.waitAmount | currency('', 2)}}元</p>
            <p class="realization-asset-title font-arial">(含预期收益{{resdata.waitInterest | currency('', 2)}}元)</p>
          </li>
          <li class="realization-right">
            <p>变现期限</p>
            <p class="realization-timeLimit font-arial">{{resdata.timeLimit}}天</p>
          </li>
        </ul>
      </div>
      <div class="form-text margin-t-15 clearfix">
        <label>变现利率(%)</label>
        <input type="number" name="" v-model="discountApr" class="input-text" placeholder="请输入变现利率" />
      </div>
      <div class="form-remind">
        变现利率范围为<i class="main-color font-arial">{{resdata.realizeRateMin}}%~{{resdata.realizeRateMax}}%</i>
      </div>
      <div class="form-text ">
        <label>变现金额(元)</label>
        <input type="tel" pattern="\d*" placeholder="请输入变现金额" v-model="inputMoney" class="input-text" v-money:7 :readonly="resdata.sellStyle == 1"/>
      </div>
      <div v-show="maxMoney != ''" class="form-remind">最高可变现<i class="main-color font-arial">{{maxMoney | currency('', 0)}}</i>元</div>
      <div class="protocol-contract margin-t-25 margin-b-15">
        <span @click="switchImg">
          <span v-if="switch_status">
            <img src="../../assets/images/public/xieyi_check_02.png" class="select-img"/>
          </span>
          <span v-else>
            <img  src="../../assets/images/public/xieyi_check_01.png" class="select-img" />
          </span>
        </span>
        <span>我已阅读并同意<i class="main-color" @click="readProtocol()">《变现协议》</i></span>
      </div>
      <div class="margin-lr-15">
        <mt-button v-if="!discountApr || !inputMoney || !switch_status"  type="default" disabled size="large" >确定变现</mt-button>
        <mt-button v-else type="danger" size="large" @click.native="submitAjax">
          <loading v-if="submitData" :task="false"></loading>
          <span v-else>确定变现</span>
        </mt-button>
      </div>
      <div class="form-prompt margin-t-25">
        <p class="prompt-title">温馨提示</p>
        <p v-html="resdata.warmTips"></p>
      </div>
    </div>
    <div class="protocol-con">
      <mt-popup v-model="popupVisible" position="bottom" class="mint-popup-3" :modal="false">
        <mt-header class="bar-nav" title="变现协议" >
          <mt-button slot="right" @click.native="popupVisible = false">关闭</mt-button>
        </mt-header>
        <div class="content" v-html="protocolHtml"></div>
      </mt-popup>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config'
  import {setApr, setMoney} from '../../directives/inputRegex'
  import Loading from '../../components/Loading.vue'
  import qs from 'qs'
  export default {
    methods: {
      switchImg () { //protocol
        this.switch_status = !this.switch_status
      },
      readProtocol(){
        this.popupVisible = true
        let params = {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
        this.$http.get(ajaxUrl.realizeProtocol,{params: params}).then((res) => {
          this.protocolHtml = res.data.resData.content
        })
      },
      submitAjax(){
        if(this.discountApr < this.resdata.realizeRateMin || this.discountApr > this.resdata.realizeRateMax){
          this.$toast('请输入正确的变现利率')
          return ;
        }
        if(!this.switch_status){
          this.$toast('请阅读并同意协议')
          return ;
        }
        let params = {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          agree: 1,
          account: this.inputMoney,
          apr: this.discountApr,
          commitRealizeToken: this.resdata.commitRealizeToken,
          investId: this.$route.params.id
        }
        this.submitData = true;
        this.$http.post(ajaxUrl.doRealizeSet, qs.stringify(params)).then((res) => {
          if(res.data.resMsg == '变现成功'){
            this.$messagebox({
              title: ' ',
              showCancelButton: false,
              confirmButtonText: '我知道了',
              message: '操作成功'
            }).then(action => {
              if(action == 'confirm'){
                this.$router.go(-1)
              }
            });
          } else {
            this.$toast(res.data.resMsg)
          }
          this.submitData = false;
        })
      }
    },
    watch: {
      discountApr(value, oldVal){
        if(value != ''){
          if(value.toString().search(/^([1-9]\d{0,1}|0)(?:\.\d{0,2})?$/)==-1){
            this.discountApr = oldVal
          }
          this.maxMoney = parseInt(this.resdata.waitAmount/(1+(value*this.resdata.timeLimit)/(this.resdata.daysOfYear*100)));
          if(this.resdata.sellStyle == 1){
            this.inputMoney = this.maxMoney
          }
        }else{
          this.maxMoney = ''
          this.discountApr = ''
        }
      }
    },
    directives:{money: setMoney},
    components:{Loading},
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      let urlParams = {
        projectInvestId: this.$route.params.id,
        userId: this.$store.state.user.userId,
        __sid: this.$store.state.user.__sid
      }
      this.$http.get(ajaxUrl.toRealizeSet, { params: urlParams }).then((res) => {
        this.$indicator.close()
        if(!res.data.resData) return
        this.resdata = res.data.resData
        this.resdata.warmTips = this.resdata.warmTips.replace(/\n/g, '<br/>')
      })
    },
    data(){
      return {
        resdata: '',
        switch_status: false,
        popupVisible: false,
        protocolHtml: '',
        submitData: false,
        inputMoney: '',
        maxMoney: '',
        discountApr: ''
      }
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../assets/scss/var.scss';
  .margin-t-25 {margin-top: .25rem;}
  .input-text {float: left;}
  .realization-top{
    height: .9rem;
    width: 100%;
    background: $main-color;
    color: #fff;
  }
  .realization-top ul{
    width: 100%;
    height: 100%;
  }
  .realization-top ul li{
    width: 49.99%;
    display: inline-block;
    float: left;
    border-left: 1px solid rgba(255,255,255,0.4);
    text-align: center;
  }
  .realization-left p{
    font-size: .16rem;
    line-height: 1;
  }
  .realization-asset-title{
    font-size: .12rem!important;
    margin-top: .075rem;
  }
  .realization-right{
    margin-top: .2rem;
  }
  .realization-right p{
    font-size: .16rem;
    line-height: 1;
  }
  .realization-timeLimit{
    margin-top: .18rem;
  }
</style>
