<template>
  <div id="result">
    <!--投资结果-->
    <div class="result bg-fff text-center">
      <div class="result-img">
        <img v-if="investResdata.payStatus == 1" src="../../../assets/images/finance/pic_failed.png" />
        <img v-else src="../../../assets/images/finance/pic_gmcg.png" />
      </div>
      <div v-if="investResdata.payStatus == 1" class="result-msg">支付失败，请在30分钟内重新支付。</div>
      <div v-else class="result-msg">受理成功，详情请查看购买记录！</div>
      <div class="result-btn">
        <router-link to="/invest">
          <span class="result-left-btn">其他产品</span>
        </router-link>
        <span v-if="investResdata.payStatus == 1" @click="toMyInvestBid" class="result-right-btn">重新支付</span>
        <span v-else @click="toMyInvest" class="result-right-btn">投资记录</span>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    data(){
      return {
        investResdata: ''
      }
    },
    created(){
      // 从投资结果页进入我的投资，点击浏览器返回时判断是否跳到账户中心
      if(sessionStorage.fromName == 'myInvest'){
        this.$router.push('/account')
      }
      this.investResdata = JSON.parse(sessionStorage.invest_resdata)
    },
    methods: {
      toMyInvest() {
        sessionStorage.tab = 'tab-con2';
        this.$router.push({ name: 'myInvest' });
      },
      toMyInvestBid() {
        this.$router.push({ name: 'myInvestBid', params: { uuid: this.$route.query.uuid }});
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../../assets/scss/var.scss';
  .bar-nav h1 {color:#000;}
  .box-msg {  padding: 0 10%;  }
  .msg-text { line-height: .24rem; }
  .one-btn {
    border: 1px solid $main-color;
    color: $main-color;
    border-radius: .03rem;
    display: inline-block;
    padding:.1rem .3rem;
  }
  .result-img{ text-align: center;  padding-top: 20%;  }
  .result-img img{
    width: .84rem;
    margin: 0 auto;
  }
  .result-msg {
    font-size: .16rem;
    color: #333;
  }
  .result-borrow {
    width: 100%;
    padding: 0 5%;
  }
  .borrow-name{
    font-size: .16rem;
    color: #999;
    line-height: .3rem;
  }
  .borrow-show{
    width: 100%;
    height: .3rem;
    line-height: .3rem;
    color: #999;
  }
  .borrow-account-title{
    float: left;
    text-align: left;
  }
  .borrow-account{
    float: right;
    text-align: right;
  }
  .result-btn {
    display: inline-block;
    padding: .3rem 0 .6rem;
  }
  .result-left-btn,.result-right-btn{
    display: inline-block;
    line-height: .3rem;
    padding: 0 .15rem;
    border-radius: .03rem;
  }
  .result-left-btn{
    border: 1px solid $main-color;
    color: $main-color;
    margin-right: .15rem;
  }
  .result-right-btn{
    border: 1px solid #DDD;
    color: #666;
  }
</style>
