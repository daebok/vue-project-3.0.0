<template>
  <div class="page" id="pageDetail">
    <mt-header class="bar-nav" title="充值">
      <!-- <mt-button slot="left" icon="back" v-back-link></mt-button> -->
      <router-link to="/account" slot="left">
        <mt-button icon="back"></mt-button>
      </router-link>
      <router-link to="/account/cash_record/?type=tab-con1" slot="right">
        <mt-button >记录</mt-button>
      </router-link>
    </mt-header>
    <div class="recharge-top">
      <p>账户余额</p>
      <p v-if="resdata !=''">{{ resdata.money | currency('', 2)}}元</p>
    </div>
    <form :action="submitUrl" method="post" id="formId" class="form-area">
      <ul class="margin-t-15">
        <li>
          <!-- <rd-money label="充值金额(元)" maxlength="8" type="text" name="amount" v-model.trim="inputMoney" placeholder="请输入金额" :readonly="resdata==''"></rd-money> -->
          <rd-field type="text" name="amount" v-model.trim="inputMoney" :attr="{maxlength: 8}" label="充值金额(元)" placeholder="请输入金额" :readonly="resdata==''"></rd-field>
        </li>
        <li>
          <rd-field type="text" label="银行卡号" :attr="{value: bankNo}" readonly></rd-field>
        </li>
        <li>
          <rd-field type="text" label="绑定手机" :attr="{value: mobile}" readonly></rd-field>
        </li>
        <li>
          <rd-field type="tel" name="smsCode" v-model="smsCode" :attr="{maxlength: 6}" label="验证码" placeholder="请输入验证码">
            <button @click.prevent="getCode" class="yzm main-color aui-border-l ellipsis">发送验证码</button>
          </rd-field>
        </li>
      </ul>
      <input type="hidden" name="rechargeToken" :value="resdata.rechargeToken">
      <input type="hidden" name="__sid" :value="getParams.__sid">
      <input type="hidden" name="userId" :value="getParams.userId">
    </form>
    <div class="confirm-btn">
      <mt-button v-if="!inputMoney || !smsCode" size="large" type="default" disabled>充 值</mt-button>
      <mt-button v-else size="large" type="danger" @click.native="confirmSub" :disabled="resdata==''" >充 值</mt-button>
    </div>

    <div class="b-tips margin-b-30">
      点击将跳转到资金托管账户进行充值
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
  import RdField from '../../components/FieldInput.vue'
  import RdMoney from '../../components/MoneyInput.vue'
  import getCodeTime from '../../mixins/getCodeTime'
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
        },
        smsClick: false,
        smsCode: '',
        mobile: '',
        bankNo: ''
      };
    },
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      this.$http.get(ajaxUrl.toRecharge, {params: this.getParams}).then((res) => {
        if(res.data.resData == '') return;
        this.resdata = res.data.resData;
        if(this.resdata.bankNo == ''){
          this.$messagebox({
            title: ' ',
            showCancelButton: false,
            confirmButtonText: '知道了',
            closeOnClickModal: false,
            message: '请先绑定与身份信息一致的银行卡再操作！'
          }).then(action => {
            if(action == 'confirm'){
              this.$router.push('/mine/safe/bank?from=account');
            }
          })
        }
        this.mobile = this.resdata.mobile
        this.bankNo = this.resdata.bankNo
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
    components:{RdMoney,RdField},
    mixins: [backUrl, getCodeTime],
    methods: {
      //获取验证码
      getCode (evt) {
        this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
        this.smsClick = true;
        let smsParams = { 
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          srvTxCode: 'directRechargeOnline', //业务交易代码 
          reqType: 2, //请求类型---充值
          cardId: this.resdata.bankList[0].bankNo,
          mobile: this.mobile
        }
        this.$http.get(ajaxUrl.smsCodeApply, {params: smsParams}).then((res) => {
          this.$indicator.close()
          if (res.data.resCode == '39321') { //成功
            this.getCodeTime(evt, 60)
          }else {
            this.$toast(res.data.resMsg)
          }
        })
      },
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
          //this.repeatFlag = false // 防重复提交
          //充值请求
          let tppParams = { 
            userId: this.$store.state.user.userId,
            __sid: this.$store.state.user.__sid,
            amount: this.inputMoney,
            cardId: this.resdata.bankList[0].bankNo, //银行卡号
            mobile: this.mobile,
            rechargeToken: this.resdata.rechargeToken,
            smsCode: this.smsCode
          }
          //console.log(this.smsClick);
          if(this.smsClick){
            //接口全是返回成功结果无法修改改为跳处理中页面
            this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
            this.$http.get(ajaxUrl.doRecharge, {params: tppParams}).then((res) => {
              this.$indicator.close()
              if (res.data.resCode == '39321') {//跳处理中页面
                // this.$messagebox({
                //   title: ' ',
                //   showCancelButton: false,
                //   confirmButtonText: '知道了',
                //   closeOnClickModal: false,
                //   message: res.data.resMsg
                // }).then(action => {
                //   if(action == 'confirm'){
                //     this.$router.push('/account/cash_record/?type=tab-con1');
                //   }
                // });
                this.$router.push('/msg_result?code=recharge_success');
              }else {//报错信息
                this.$toast(res.data.resMsg)
              }
            })
          }else{
            this.$toast({message:'请先点击获取验证码'})
          }
          //从哪来
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
    background: #ff9545;
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