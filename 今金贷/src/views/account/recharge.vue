<template>
  <div class="page" id="pageDetail">
    <mt-header class="bar-nav" title="充值">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
      <router-link to="/account/cash_record/?type=tab-con1" slot="right">
        <mt-button >记录</mt-button>
      </router-link>
    </mt-header>
    <div class="recharge-top">
      <p>账户余额</p>
      <p v-if="resdata !=''">{{ resdata.money | currency('', 2)}}元</p>
    </div>
    <form :action="submitUrl" method="post" id="formId">
      <div class="margin-t-15">
        <rd-money label="充值金额(元)" maxlength="8" type="text" name="amount" v-model.trim="inputMoney" placeholder="请输入金额" :readonly="resdata==''"></rd-money>
      </div>
      <input type="hidden" name="rechargeToken" :value="resdata.rechargeToken">
      <input type="hidden" name="__sid" :value="getParams.__sid">
      <input type="hidden" name="userId" :value="getParams.userId">
    </form>
    <div class="confirm-btn">
      <mt-button v-if="!inputMoney" size="large" type="default" disabled>充 值</mt-button>
      <mt-button v-else size="large" type="danger" @click.native="confirmSub" :disabled="resdata==''" >充 值</mt-button>
    </div>

    <div class="b-tips margin-b-30">
      <i class="main-color">*</i>点击将跳转到资金托管账户进行充值
    </div>
    <div class="form-prompt">
      <p class="prompt-title">温馨提示：</p>
      <p v-html="resdata.warmTips"></p>
    </div>
    <!--<router-link to="/account/recharge/support_bank">-->
      <!--<div class="bank-description-tip secondary-color" >支持银行说明</div>-->
    <!--</router-link>-->
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config'
  import backUrl from '../../mixins/backUrl'
  import RdMoney from '../../components/MoneyInput.vue'
  export default {
    data(){
      return {
        submitUrl: ajaxUrl.doRecharge,
        resdata: '',
        inputMoney:'',
        repeatFlag: true,
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      };
    },
    created(){
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
    components:{RdMoney},
    mixins: [backUrl],
    methods: {
      confirmSub(){
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
          document.getElementById('formId').submit()
          if(this.$route.query.from == 'investBid'){
            sessionStorage.from_investBid_id = this.$route.query.id
          }else if(this.$route.query.from == 'realizeBid'){
            sessionStorage.from_realizeBid_id =this.$route.query.id
          }
          else if(this.$route.query.from == 'bondBid'){
            sessionStorage.from_bondBid_id =this.$route.query.id
          }
        }
      }
    }
  }
</script>
<style scoped>
  .recharge-top{
    height: .8rem;
    width: 100%;
    background: #F95A28;
    padding: 0 5%;
  }
  .form-text {position:relative;}
  .input-text {width: 60%;}
  .rd-field-clear {position:absolute;right: .15rem;opacity:.2;top: .15rem;}
  .recharge-top p{
    margin-top: .16rem;
    float: left;
    width: 100%;
    line-height: 1;
    font-size: .16rem;
    font-family: arial;
    color: #fff;
  }
  .confirm-btn{
    margin: .3rem 5% 0;
  }
  .b-tips{
    font-size: .12rem;
    color: #999;
    margin-top: .18rem;
    text-align: center;
  }
  .bank-description-tip{
    display: inline-block;
    padding: 0 5%;
    font-size: .12rem;
    line-height: .42rem;
  }
</style>
