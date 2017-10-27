<template>
 <div class="page" id="immediate_repayment">
    <mt-header class="bar-nav" title="立即还款">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
  <div class="invest-am clearfix">
    <div class="invest-am-list">
      <div class="invest-am-title">
        <span class="am-title">应还金额(元)</span>
        <p class="repayment-time pull-right main-color">{{resdata.amount | currency('',2)}}</p>
      </div>
      <ul class="info-con clearfix color-999 ">
        <li class="margin-t-10">
          <span class="color-999">应还本金(元)</span>
          <span class="font-arial main-color pull-right">{{resdata.capital | currency('',2)}}</span>
        </li>
        <li class="margin-t-10">
          <span >应还利息(元)</span>
          <span class="font-arial pull-right">{{resdata.interest | currency('',2)}}</span>
        </li>
        <li class="margin-t-10">
          <span >逾期罚息(元)</span>
          <span class="font-arial pull-right">{{resdata.lateInterest | currency('',2)}}</span>
        </li>
      </ul>
    </div>
  </div>
   <div class="form-area margin-t-10">
     <ul><li>
       <rd-field type="tel" :attr="{maxlength: 6}" v-model.trim="codeVal" label="验证码" placeholder="请输入手机验证码">
         <button ref="sendCode" @click.prevent="getCode" class="yzm main-color aui-border-l ellipsis">发送验证码</button>
       </rd-field>
     </li></ul>
   </div>

   <div class="margin-t-30 margin-lr-15">
     <mt-button v-if="!codeVal || clickNum == false" type="default" size="large" class="confirm-btn" disabled>确定还款</mt-button>
     <mt-button v-else type="danger" size="large" @click.native="submitAjax" class="confirm-btn">
       <loading v-if="submitData" :task="false"></loading>
       <span v-else>确定还款</span>
     </mt-button>
   </div>

 </div>
</template>
<script>
  import Loading from '../../components/Loading.vue'
  import RdField from '../../components/FieldInput.vue'
  import validator from '../../mixins/formValidator'
  import getCodeTime from '../../mixins/getRepaymentCodeTime' //引入还款倒计时
  import * as ajaxUrl from '../../ajax.config'
  import qs from 'qs'
  export default {
    data(){
      return {
        codeVal: '',
        resdata:'',
        submitData: false,
        clickNum: false //是否点击获取验证码
      }
    },
    created(){
      if(this.$route.params.tab){
        sessionStorage.tab = this.$route.params.tab
      }
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      let urlParams = {
        userId: this.$store.state.user.userId,
        __sid: this.$store.state.user.__sid,
        projectId: this.$route.params.projectId,
        uuid: this.$route.params.uuid,
        period: this.$route.params.period,
      }
      this.$http.get(ajaxUrl.toRepay, { params: urlParams }).then((res) => {
        this.resdata = res.data.resData
        this.$indicator.close()
      })
    },
    mixins: [validator, getCodeTime],
    methods: {
      getCode (evt) {
        this.clickNum = true;
        //console.log(this.clickNum);
        let params = {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          repaymentId: this.$route.params.projectId
        }
        this.$http.post(ajaxUrl.getRepayCode, qs.stringify(params)).then((res) => {
          if(res.data.resCode == 39321) {
            this.getCodeTime(evt, 60)//解决倒计时发生共享
          }else{
            this.$toast(res.data.resMsg)
          }
        })
        
        //this.getCodeTime(evt,60)//解决倒计时发生共享
      },
      submitAjax(){
        let params = {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          uuid: this.$route.params.uuid,  //添加uuid参数
          code: this.codeVal,
          repayToken: this.resdata.repayToken
        }
        this.submitData = true;
        this.$http.post(ajaxUrl.doRepay, qs.stringify(params)).then((res) => {
          // if(res.data.resData != ''){
          //   this.$messagebox({
          //     title: ' ',
          //     showCancelButton: false,
          //     confirmButtonText: '我知道了',
          //     message: '操作成功'
          //   }).then(action => {
          //     if(action == 'confirm'){
          //       this.$router.go(-1)
          //     }
          //   });
          // }else{
          //   this.$toast(res.data.resMsg)
          // }
          this.$messagebox({
            title: ' ',
            showCancelButton: false,
            confirmButtonText: '我知道了',
            closeOnClickModal: false,
            message: res.data.resMsg
          }).then(action => {
            //if(action == 'confirm'){
            //还款成功返回上一页
            if(res.data.resCode != 0){
              this.$router.go(-1)
            }
            //}
          });
          this.submitData = false;
        })
      }
    },
    components: { Loading, RdField }
  }
</script>
<style scoped>
  .margin-15 {margin:.15rem;}
  .invest-am-list {
    padding: 0 .15rem;
    background: #fff;
    margin-top: .1rem;
  }
  .invest-am-title {
    border-bottom: 1px solid #DEDEDE;
  }
  .am-title {
    height: .42rem;
    line-height: .42rem;
    font-size: .15rem;
  }
  .repayment-time {
    line-height: .42rem;
    font-size: .16rem;
  }
  .info-con {font-size: .12rem;padding-bottom:.15rem;}
  .info-con li span:last-child { font-size: .14rem;}
  .mint-cell { margin-top: .1rem; }
  .mint-cell::after {border:0;}
</style>
