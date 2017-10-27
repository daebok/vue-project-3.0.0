<template>
  <div>
  <div class="page cash" id="cash">
    <form :action="submitUrl" method="post" id="cashForm">
      <div v-if="canDisable" class="bank margin-t-15" >
        <img :src="defaultBankCard.picPath" >
        <label>{{defaultBankCard.bankName}} <i class="color-999">{{defaultBankCard.bankNoSuffix}}</i></label>
        <input type="hidden" name="bankCode" :value="bankCode">
        <input type="hidden" name="bankNo" :value="bankNo">
      </div>
      <div v-else class="bank margin-t-15">
        <label>未默认银行卡 <i class="color-999">未默认</i></label>
        <input type="hidden" name="bankCode" :value="bankCode">
        <input type="hidden" name="bankNo" :value="bankNo">
      </div>
    <div class="cash-limit" >
      <div class="use-cash-money">
        可提现金额 <i v-if="resdata !=''" class="main-color font-arial">{{canCashMoney | currency('',2)}}</i> 元
      </div>
      <i class="all-cash" @click="allWithdraw">全部提现</i>
    </div>
    <div class="">
      <rd-money label="提现金额(元)" maxlength="8" type="tel" name="amount" v-model.trim="inputMoney" placeholder="请输入金额" :readonly="resdata==''"></rd-money>
    </div>
      <input type="hidden" name="cashToken" :value="resdata.cashToken">
      <input type="hidden" name="__sid" :value="getParams.__sid">
      <input type="hidden" name="userId" :value="getParams.userId">
    </form>
    <div class="confirm-btn">
      <mt-button v-show="!inputMoney" type="default" size="large" disabled>提 现</mt-button>
      <mt-button v-show="inputMoney" @click.prevent="$refs.pwd.show()" type="danger" size="large" :disabled="resdata==''">提 现</mt-button>
    </div>
    <div class="b-tips margin-b-15">
      <router-link to="/account/cash_record/?type=tab-con2" slot="right">
        <p class="tip-color"><img src="../../assets/images/me/btn_czjl.png"> 提现记录</p>
      </router-link>
    </div>
    <div class="form-prompt">
      <p class="prompt-title margin-b-10">温馨提示：</p>
      <p v-html="resdata.warmTips"></p>
    </div>
    <pay-pwd @submit="submitAjax" v-model="payPwd" ref="pwd" tipTxt="提现金额" :tipMoney="inputMoney"></pay-pwd>
  </div>
    <!--银行卡列表-->
  <transition
    name="custom-classes-transition"
    enter-active-class="animated slideInRight"
    leave-active-class="animated slideOutRight"
  >
  <div v-show="showBankList" class="page bank-show" id="bank_show">
    <mt-header class="bar-nav" title="选择银行卡">
      <mt-button slot="left" icon="back" @click.native="hideBankListFun"></mt-button>
    </mt-header>
    <div v-for="(item,index) in resdata.bankList" :class="{'selected': !item.canDisable}" class="cash-top margin-t-15 aui-border-b" @click="selectBank('b'+index)" >
      <div class="bank-img">
        <img :src="item.picPath" >
      </div>
      <div class="bank-info" :ref="'b'+index" :data-code="item.bankCode" :data-no="item.bankNo">
        <p class="margin-tb-10">{{item.bankName}}</p>
        <p class="bank-no">尾号{{item.hideBankNo}}　{{item.bankCardType}}</p>
      </div>
    </div>
  </div>
  </transition>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config'
  import RdMoney from '../../components/MoneyInput.vue'
  import PayPwd from '../../components/PayPwd.vue'
  import base64code from '../../mixins/base64code.js'; // 引入密码加密插件
  import qs from 'qs'
  export default {
    name: 'withdraw',
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      this.$http.get(ajaxUrl.toCash, {params: this.getParams}).then((res) => {
        if(res.data.resData == '') return;
        var bankList = res.data.resData.bankList
        for(var i = 0; i < bankList.length; i++){
          if(bankList[i].canDisable){
            this.canDisable = true;
            this.defaultBankCard = bankList[i]
            this.bankCode = bankList[i].bankCode
            this.bankNo = bankList[i].bankNo
            this.curSelectRef = 'b' + i
          }
        }
        this.resdata = res.data.resData
        this.canCashMoney = this.resdata.cashMoney < this.resdata.userMoney ? this.resdata.cashMoney : this.resdata.userMoney
        this.resdata.warmTips = res.data.resData.warmTips.replace(/\n/g, '<br/>')
        this.$indicator.close()
        if (this.resdata.idCardStatus == 0) {
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去上传',
            showCancelButton: true,
            message: '首次提现需上传身份证，审核成功后才能提现！'
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push({ name: 'idNo', query: { from: 'withdraw' }});
            } else {
              this.$router.go(-1);
            }
          });
        }
      })
    },
    components:{RdMoney, PayPwd},
    mixins: [ base64code],
    methods: {
      showBankListFun(){
        this.showBankList = true;
        // 进入银行列表标记已✔️中的列表
        if(!this.curSelectRef) return // 为空时返回不执行
        this.$refs[this.curSelectRef][0].parentElement.className = 'cash-top selected margin-t-15 aui-border-b'
      },
      hideBankListFun(){this.showBankList = false;},
      selectBank(ref){
        let selectNode = this.$refs[ref][0]
        let bank_code = selectNode.dataset.code
        let bank_no = selectNode.dataset.no
        let bank_pic = selectNode.previousElementSibling.children[0].src
        let bankInfo = document.getElementById('bank_info')
        bankInfo.previousElementSibling.children[0].src = bank_pic
        bankInfo.children[0].innerText = selectNode.children[0].innerText
        bankInfo.children[1].innerText = selectNode.children[1].innerText
        // 给选择的银行卡加✔️
        let card_list = document.getElementsByClassName('cash-top')
        for(let i = 0; i < card_list.length; i++){
          card_list[i].className = 'cash-top margin-t-15 aui-border-b'
        }
        // 给当前点击的父节点加✔️
        selectNode.parentElement.className = 'cash-top selected margin-t-15 aui-border-b'
        this.curSelectRef = ref; // 把当前点击选择的ref赋值给data作为下次进来的标记
        //选择时传递银行的bankCode 和 bankNo
        this.bankCode = bank_code
        this.bankNo = bank_no
        this.showBankList = false
      },
      allWithdraw () { // 点击全部提现
        if(this.canCashMoney > this.resdata.cashSingleMaxAmountLimit){ // 可提现金额大于单笔可提现额度
          this.inputMoney = this.resdata.cashSingleMaxAmountLimit
        }else{
          this.inputMoney = this.canCashMoney
        }
      },
      submitAjax(){
        if(this.resdata.todayDoCashTimes >= this.resdata.cashSingleDayTimeLimit){ //
          this.$toast('今日提现次数已达上限')
          return
        }
        if(this.inputMoney < this.resdata.cashMinAmount){ //
          this.$toast('最小提现金额为' + this.resdata.cashMinAmount +'元')
          return
        }
        if(this.inputMoney > this.resdata.cashSingleMaxAmountLimit){ //
          this.$toast('单笔提现不能超过' + this.resdata.cashSingleMaxAmountLimit +'元')
          return
        }
        if(this.inputMoney > this.resdata.userMoney){ //
          this.$toast('提现金额不能大于您的可用余额')
          return
        }
        if(this.inputMoney > this.resdata.cashMoney){ //
          this.$toast('您今日还可提现' + this.resdata.cashMoney +'元')
          return
        }
        if(this.repeatFlag){
          this.repeatFlag = false  // 防重复提交
          // document.getElementById('cashForm').submit()
          let params = {
            userId: this.$store.state.user.userId,
            __sid: this.$store.state.user.__sid,
            amount: this.inputMoney,
            cashToken: this.resdata.cashToken,
            payPwd: this.base64Encode(this.payPwd),
            bankNo: this.bankNo,
            openBankNo: this.bankCode
          }
          this.$http.post(ajaxUrl.doCash, qs.stringify(params)).then((res) => {
            this.$refs.pwd.close() //关闭支付密码框
            if(res.data.resCode == 39321){
              this.$messagebox({
                title: ' ',
                confirmButtonText: '知道了',
                showCancelButton: false,
                message: '提现申请成功！'
              }).then(action => {
                if(action == 'confirm') {
                  this.$router.push('/account');
                }
              });
            }else {
              this.repeatFlag = true
              this.$toast(res.data.resMsg)
            }
          })
        }

      }
    },
    watch: {
      inputMoney (newV, oldV) {
        if( newV.toString().search(/^([1-9]\d*|0)/) == -1){ //解决刚开始输入不符合规则的bug
          this.inputMoney = ''
        }
      }
    },
    data(){
      return {
        submitUrl: ajaxUrl.doCash,
        payPwd: '',
        curSelectRef: '',
        showBankList: false,
        resdata: '',
        bankNo:'',
        bankCode: '',
        canDisable: false,
        repeatFlag: true,
        defaultBankCard: '',
        inputMoney:'',
        canCashMoney: 0,
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      }
    }
  }
</script>
<style lang="scss" rel="styleSheet/scss" scoped>
  @import "../../assets/css/animate.min.css";
  @import "../../assets/scss/var.scss";
  .bank {
    height: .6rem;
    display:flex;
    align-items: center;
    justify-content: flex-start;
    padding: .15rem;
    background: #fff;
    img { width: .3rem; margin-right: .15rem;}
  }
  .rd-field {padding: .06rem 0;}
  .b-tips{
    font-size: .13rem;
    margin-top: .1rem;
    margin-right: .15rem;
    text-align: right;
    img {width: .16rem;  vertical-align: -2px;}
  }
  .confirm-btn{  margin: .2rem .15rem .15rem;  }
  .cash-top{
    height: .6rem;
    width: 100%;
    padding: 0 5%;
    background:#fff;
  }
  .cash-top.selected {
    background:#fff url('../../assets/images/money/card_selected.png') 90% center no-repeat;
    background-size: .16rem;
  }
  .bank-img{
    float: left;
    width: .55rem;
    height: 100%;
  }
  .bank-img img{
    height: .38rem;
    width: .38rem;
    vertical-align: middle;
    margin-top: .11rem;
  }
  .bank-info{
    width: 60%;
    float: left;
    height: 100%;
  }
  .bank-info p{
    font-size: .14rem;
  }
  .bank-no{
    color: #999;
    line-height: 1;
    margin-top: .06rem;
  }
  .cash-limit{
    height: .48rem;
    width: 100%;
    padding: 0 5%;
  }
  .use-cash-money{
    width: 50%;
    height: .48rem;
    line-height: .48rem;
    float: left;
    font-size: .12rem;
    color: #999;
  }
  .all-cash{
    height: .24rem;
    line-height: .22rem;
    padding: 0 .1rem;
    font-size: .13rem;
    display: inline-block;
    float: right;
    margin-top: .12rem;
    color: $main-color;
    border: 1px solid $main-color;
    border-radius: .12rem;
  }
</style>
