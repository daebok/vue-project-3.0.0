<template>
  <div class="page" id="result">
    <mt-header class="bar-nav" title="投资结果"></mt-header>
    <div class="result">
      <div class="result-img">
        <img src="../../../assets/images/finance/tips_invest_result.png" />
      </div>
      <div class="result-msg">
        您的支付请求第三方已受理，结果可在我的资产>投资管理中查看
      </div>
      <div class="result-borrow">
        <p class="borrow-name">爱定存-RDY16081605</p>
        <div class="borrow-show">
          <span class="borrow-account-title">投资金额(元)：</span>
          <span class="borrow-account">7,400.00</span>
        </div>
      </div>
      <div class="result-btn">
        <span class="result-left-btn">其他产品</span>
        <span class="result-right-btn">投资记录</span>
      </div>
    </div>
  </div>
</template>


<style scoped>
  .page {background:#fff;}
  .project-detail{
    height: 100%;
    width: 100%;
  }
  .result-img{
    width: 100%;
    text-align: center;
    margin-top: 20%;
  }
  .result-img img{
    width: .9rem;
    height: .9rem;
    margin: 0 auto;
  }
  .result-msg{
    width: 80%;
    margin-left: 10%;
    font-size: .16rem;
    color: #333;
    margin-bottom: .5rem;
  }
  .result-borrow{
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
  .result-btn{
    width: 100%;
    margin-top: .3rem;
  }
  .result-left-btn,.result-right-btn{
    width: 40%;
    float: left;
    height: .36rem;
    display: inline-block;
    text-align: center;
    line-height: .36rem;
  }
  .result-left-btn{
    border: 1px solid #F95A28;
    color: #F95A28;
    margin-left: 5%;
    margin-right: 10%;
  }
  .result-right-btn{
    border: 1px solid #DDD;
    color: #666;
  }
</style>
