<template>
  <div id="result">
    <!--投资结果-->
    <div class="result">
      <div class="result-img">
        <img src="../../assets/images/finance/pic_gmcg.png" />
      </div>
      <div class="result-msg">受理成功，详情请查看购买记录！</div>
      <div class="result-btn">
        <router-link to="/invest">
          <span class="result-left-btn">其他产品</span>
        </router-link>
        <!--<router-link to="/account/myInvest?tab=tab-con2&from=msg_result">-->
        <span class="result-right-btn" @click="toMyInvest">投资记录</span>
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
      this.investResdata = JSON.parse(sessionStorage.invest_resdata)
    },
    methods: {
      toMyInvest() {
        sessionStorage.tab = 'tab-con2';
        this.$router.push({ name: 'myInvest' });
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../assets/scss/var.scss';
  .bar-nav h1 {color:#000;}
  .page {background:#fff;}
  .box-msg {  padding: 0 10%;  }
  .msg-text { line-height: .24rem; }
  .one-btn {
    border: 1px solid $main-color;
    color: $main-color;
    border-radius: .03rem;
    display: inline-block;
    padding:.1rem .3rem;
  }
  .result-img{ text-align: center;  margin-top: 20%;  }
  .result-img img{
    width: .84rem;
    margin: 0 auto;
  }
  .result-msg{
    width: 80%;
    margin-left: 10%;
    font-size: .16rem;
    color: #333;
    margin-bottom: .5rem;
    line-height:.2rem;
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
  .result-btn{  margin-top: .3rem;  }
  .result-left-btn,.result-right-btn{
    width: 40%;
    float: left;
    height: .36rem;
    display: inline-block;
    text-align: center;
    line-height: .36rem;
  }
  .result-left-btn{
    border: 1px solid $main-color;
    color: $main-color;
    margin-left: 5%;
    margin-right: 10%;
    border-radius: .03rem;
  }
  .result-right-btn{
    border: 1px solid #DDD;
    color: #666;
    border-radius: .03rem;
  }
</style>
