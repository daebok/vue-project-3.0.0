<template>
  <div>
    <div class="recharge-top">
      <p>钱包余额</p>
      <p v-if="resdata !=''">{{ resdata.money | currency('', 2)}}元</p>
    </div>
    <form :action="submitUrl" method="post" id="formId">
      <div class="bank margin-t-15">
        <label><img class="bank-logo" :src="resdata.picPath" alt="">{{resdata.bankName}} <i class="color-999">{{resdata.hideBankNo}}</i></label>
        <span class="tip-color" @click="readLimit">查看限额</span>
      </div>
      <div class="margin-t-15">
        <rd-money label="充值金额(元)" maxlength="8" type="tel" name="amount" v-model.trim="inputMoney" placeholder="请输入金额" :readonly="resdata==''"></rd-money>
      </div>
      <input type="hidden" name="rechargeToken" :value="resdata.rechargeToken">
      <input type="hidden" name="payPwd" :value="payPwd">
      <input type="hidden" name="__sid" :value="getParams.__sid">
      <input type="hidden" name="userId" :value="getParams.userId">
    </form>
    <div class="protocol-box">
        <span @click="switchImg">
            <span v-if="switch_status">
              <img src="../../assets/images/fund/xieyi_check_02.png" />
            </span>
            <span v-else>
              <img src="../../assets/images/fund/xieyi_check_01.png" />
            </span>
        </span>
      我已阅读并同意
      <a v-for="item in resdata.proList" @click="readProtocol(item.id, item.name)" class="protocol">《{{ item.name }}》</a>
    </div>
    <div class="confirm-btn">
      <mt-button v-if="inputMoney && switch_status" size="large" type="danger" @click.prevent="$refs.pwd.show()" :disabled="resdata==''" >充 值</mt-button>
      <mt-button v-else size="large" type="default" disabled>充 值</mt-button>
    </div>
    <div class="b-tips margin-b-30">
      <router-link to="/account/cash_record/?type=tab-con1" slot="right">
        <span class="tip-color"><img src="../../assets/images/me/btn_czjl.png"> 充值记录</span>
      </router-link>
    </div>
    <div class="form-prompt">
      <p class="prompt-title">温馨提示：</p>
      <p v-html="resdata.warmTips"></p>
    </div>
    <pay-pwd @submit="confirmSub" v-model="payPwd" ref="pwd" tipTxt="充值金额" :tipMoney="inputMoney"></pay-pwd>
    <!-- S协议内容隐藏域 -->
    <div class="protocol-con">
      <mt-popup v-model="popupVisibleProtocol" position="bottom" class="mint-popup-3" :modal="false">
        <mt-header class="bar-nav" :title="protocolTitle">
          <mt-button slot="right" @click="popupVisibleProtocol = false">关闭</mt-button>
        </mt-header>
        <div class="con" :style="{ height: protocolHeight + 'px' }"><section v-html="protocolHtml"></section></div>
      </mt-popup>
    </div>
    <!-- E协议内容隐藏域 -->
    <!--<router-link to="/account/recharge/support_bank">-->
      <!--<div class="bank-description-tip secondary-color" >支持银行说明</div>-->
    <!--</router-link>-->
  </div>
</template>
<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config'
  import qs from 'qs'
  import RdMoney from '../../components/MoneyInput.vue'
  import PayPwd from '../../components/PayPwd.vue'
  import base64code from '../../mixins/base64code.js'; // 引入密码加密插件
  export default {
    data(){
      return {
        submitUrl: ajaxUrl.doRecharge,
        resdata: '',
        inputMoney:'',
        repeatFlag: true,
        payPwd: '',
        popupVisibleProtocol: false, // 协议下拉菜单显示
        switch_status: true, // 是否同意协议，true已同意，false不同意
        protocolTitle: '', // 协议标题
        protocolHtml: '', // 协议内容
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        protocolHeight: 0
      };
    },
    created(){
      this.$nextTick(() => {
        this.protocolHeight = document.documentElement.clientHeight - 40;
      })
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      this.$http.get(ajaxUrl.toRecharge, {params: this.getParams}).then((res) => {
        if(res.data.resData == '') return;
        this.resdata = res.data.resData;
        this.resdata.warmTips = res.data.resData.warmTips.replace(/\n/g, '<br/>')
        this.$indicator.close()
      })
    },
    watch: {
      inputMoney (newV, oldV) {
        if( newV.search(/^([1-9]\d*|0)/) == -1){ //解决刚开始输入不符合规则的bug
          this.inputMoney = ''
        }
      }
    },
    components:{RdMoney, PayPwd},
    mixins: [ base64code],
    methods: {
      confirmSub(pwd){
        if(this.inputMoney < this.resdata.rechargeMinAmount){ //
          this.$toast('充值金额不能小于' + this.resdata.rechargeMinAmount +'元')
          return ;
        }
        if(this.inputMoney > this.resdata.rechargeMaxAmount){
          this.$toast({
            message: '充值金额不能大于' + this.resdata.rechargeMaxAmount +'元',
          })
          return ;
        }
        if(this.repeatFlag){
          this.repeatFlag = false // 防重复提交
          // document.getElementById('formId').submit()
          let params = {
            userId: this.$store.state.user.userId,
            __sid: this.$store.state.user.__sid,
            amount: this.inputMoney,
            rechargeToken: this.resdata.rechargeToken,
            payPwd: this.base64Encode(this.payPwd)
          }
          this.$http.post(ajaxUrl.doRecharge, qs.stringify(params)).then((res) => {
            this.$refs.pwd.close() //关闭支付密码框
            if(res.data.resCode == 39321){
              this.$messagebox({
                title: ' ',
                confirmButtonText: '知道了',
                showCancelButton: false,
                message: '受理成功，详情请查看充值记录'
              }).then(action => {
                if(action == 'confirm') {
                  this.$router.push('/account');
                }
              });
            }else{
              this.repeatFlag = true
              this.$toast(res.data.resMsg)
            }
          })
          if(this.$route.query.from == 'investBid'){
            sessionStorage.from_investBid_id = this.$route.query.id
          }else if(this.$route.query.from == 'realizeBid'){
            sessionStorage.from_realizeBid_id =this.$route.query.id
          }
        }
      },
      // 同意协议按钮点击切换
      switchImg() {
        this.switch_status = !this.switch_status;
      },
      // 查看协议
      readProtocol(id, name) {
        this.popupVisibleProtocol = true;
        this.protocolHtml = '';
        this.protocolTitle = name;
        this.$http.get(ajaxUrl.registerProtocolDetail,{ params: { protocolId: id }}).then((res) => {
          this.protocolHtml = res.data.resData.content;
        })
      },
      readLimit() {
        this.protocolHtml = '';
        this.popupVisibleProtocol = true;
        this.$http.get(ajaxUrl.top5Help,{ params: { sectionCode: 'bankDeclare' }}).then((res) => {
          this.protocolHtml = res.data.resData.articleList[0].content;
          this.protocolTitle = res.data.resData.articleList[0].title
        })
      }
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../assets/scss/var.scss';
  .bank {
    height: .6rem;
    display:flex;
    align-items: center;
    justify-content: space-between;
    padding: .15rem;
    background: #fff;
  }
  .rd-field {padding: .06rem 0;}
  .recharge-top {
    height: .8rem;
    width: 100%;
    background: $main-color;
    padding: 0 5%;
  }
  .form-text {position:relative;}
  .input-text {width: 60%;}
  .rd-field-clear {position:absolute;right: .15rem;opacity:.2;top: .15rem;}
  .recharge-top p {
    margin-top: .16rem;
    float: left;
    width: 100%;
    line-height: 1;
    font-size: .16rem;
    font-family: arial;
    color: #fff;
  }
  .confirm-btn {
    margin: .3rem 5% 0;
  }
  .b-tips {
    font-size: .13rem;
    margin-top: .1rem;
    margin-right: .15rem;
    text-align: right;
    img {width: .16rem;  vertical-align: middle;}
  }
  .bank-description-tip {
    display: inline-block;
    padding: 0 5%;
    font-size: .12rem;
    line-height: .42rem;
  }
  .bank-logo {
    width: .2rem;
    vertical-align: middle;
    margin-right: .1rem;
  }
  .protocol-box{
    padding: 0.1rem 5%;
    margin-bottom: 0.1rem;
    font-size: 0.12rem;
    color: #333333;
  }
  .protocol-box img{
    width: .14rem;
    vertical-align: -2px;
  }
  .protocol {
    color: #5a6885;
  }
  /*.protocol-con .content{
    padding: 0;
    background: #F5F5F5;
  }*/
</style>
