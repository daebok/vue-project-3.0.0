<!-- 注册成功 -->
<template>
  <div class="page">
    <div class="content">
      <div class="text-center">
        <img src="../../assets/images/logReg/tips_registered_success.png">
        <h4>恭喜您注册成功！</h4>
        <p id="cutTime" class="color-999"></p>
      </div>
      <div class="btns clearfix">
        <!-- <router-link to="/invest?openId=2"><span class="main-color border-col">马上投资</span></router-link> -->
        <span class="main-color border-col" @click="toList()">马上投资</span>
        <!-- <router-link to="/index"><span class="last-span">先去逛逛</span></router-link> -->
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        openId: this.$route.params.openId,//微信openId
      }
    },
    created() {
      //console.log(this.openId);
      this.Load();
    },
    methods: {
      toList(){
        this.$router.push('/invest?openId='+this.openId);
      },
      Load(){
        let secs = 3; //倒计时的秒数 
        let cutThis=this;
        for(let i=secs;i>=0;i--) { 
          window.setTimeout(function(){
            document.getElementById('cutTime').innerHTML = '将在'+i+'秒后自动跳转到产品列表';
            if(i == 0){
              //cutThis.$router.push('/invest?openId='+cutThis.openId);
              cutThis.toList();
            }
          },(secs-i) * 1000); 
        } 
      }
    }
  }
</script>
<style type="text/css" scoped>
  img {width: .9rem;margin-top: .61rem;}
  h4 {margin-top: .21rem;font-size: .18rem;}
  p { font-size: .15rem;}
  .btns {text-align:center; margin-top: .38rem;}
  .btns span {width: 1.4rem; text-align: center; border: 1px solid #333; line-height: .38rem;display:inline-block;border-radius: 3px; font-size: .16rem;}
  .btns span.border-col {border-color:#F95A28;}
  .btns span.last-span {margin-left: .31rem;}
</style>