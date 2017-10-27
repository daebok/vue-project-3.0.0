<template>
  <div class="page">
    <mt-header class="bar-nav" title="自动投标">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="auto-top">
      <p>您的可用余额</p>
      <p>{{resdata.userMoney | currency('',2)}}元</p>
    </div>
    <mt-cell title="自动投标状态" class="margin-t-15">
      <label class="mint-switch">
        <input class="mint-switch-input" type="checkbox" v-model="switch_value">
        <span class="mint-switch-core"></span>
      </label>
    </mt-cell>
    <router-link to="/account/auto/modify">
    <div class="form-text" v-show="switch_value">
        <label>投标参数详情</label>
        <img src="../../../assets/images/public/arrow_right.png" class="arrow-right">
    </div>
    </router-link>
    <div class="form-prompt margin-t-25">
      <div class="prompt-title margin-t-15">温馨提示：</div>
      <p class="" v-html="resdata.warmTips"></p>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
 export default {
   data(){
     return {
       switch_value: false,
       resdata: '',
       getParams: {
         userId: this.$store.state.user.userId,
         __sid: this.$store.state.user.__sid
       },
       closeStatus: 0
     }
   },
   created(){
     this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
     this.$http.get(ajaxUrl.autoInit, {params: this.getParams}).then((res) => {
       if(res.data.resData == '') return;
       this.resdata = res.data.resData
       this.resdata.warmTips = res.data.resData.warmTips.replace(/\n/g, '<br/>')
       //判断是否开启了自动投标
       if(this.resdata.isAuto == 1){
         this.switch_value = true;
       }
       this.$indicator.close() // 关闭提示
     })
   },
   mounted(){
     // 清除边框
     document.getElementsByClassName('mint-cell-wrapper')[0].style.backgroundImage = 'none'
   },
   watch: {
     switch_value(newVal, oldVal){
       if(newVal && this.resdata.minAmount > this.resdata.userMoney){ //开启自动投标判断可用余额
         this.switch_value = false
         this.$messagebox({
           title: ' ',
           confirmButtonText: '去充值',
           showCancelButton: true,
           message: '账户可用余额达'+ this.resdata.minAmount +'元才能开启自动投标，请充值'
         }).then(action => {
           if(action == 'confirm'){
             this.$router.push('/account/recharge')
           }
         });
       }
       if(newVal && this.resdata.isAuto != 1 && this.resdata.minAmount < this.resdata.userMoney){ //余额充足的时
         this.$router.push('/account/auto/setting')
       }
       if(!newVal && this.resdata.isAuto == 1){
         this.switch_value = true
         if(this.closeStatus){
           this.switch_value = false
           return ;
         }
         this.$messagebox({
           title: ' ',
           showCancelButton: true,
           message: '您确定要关闭自动投资吗？'
         }).then(action => {
           if(action == 'confirm'){
             this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
             this.$http.get(ajaxUrl.closeAutoInvest, {params: this.getParams}).then((res) => {
               this.$indicator.close() // 关闭提示
               if(res.data.resMsg == '关闭成功'){
                 this.switch_value = false
                 this.closeStatus = 1
                 this.resdata.isAuto = 0  // 不需要再请求，手动设置关闭状态
               }
             })
           }
         });
       }
     }
   }
 }
</script>

<style scoped>
  .auto-top{
    height: .8rem;
    width: 100%;
    background: #F95A28;
    padding: 0 5%;
  }
  .auto-top p{
    margin-top: .16rem;
    float: left;
    width: 100%;
    line-height: 1;
    font-size: .16rem;
    font-family: arial;
    color: #fff;
  }
  .mint-cell-text{
    font-size: .16rem;
  }
  .form-text{
    height: .48rem;
    padding: 0 .15rem;
  }
  .form-text label{
    line-height: .48rem;
    font-size: .14rem;
  }
  .arrow-right{
    height: .14rem;
    float: right;
    margin-top: .17rem;
  }
  .form-prompt p{
    line-height: .24rem;
  }
</style>

