<template>
  <div class="page" id="pageDetail">
    <mt-header class="bar-nav" title="支持银行说明">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <ul class="bank-list margin-t-10">
      <li class="aui-border-b">
        <div class="list-left">
          <img src="../../assets/images/public/bank_logo/recharge/recharge_icon_ICBC.png">
        </div>
        <div class="list-right">
          <p class="bank-name">中国工商银行</p>
          <p class="list-desc">单笔5万，日累计5万</p>
        </div>
      </li>
      <li class="aui-border-b">
        <div class="list-left">
          <img src="../../assets/images/public/bank_logo/recharge/recharge_icon_AOBC.png">
        </div>
        <div class="list-right">
          <p class="bank-name">中国农业银行</p>
          <p class="list-desc">单笔2万，日累计2万</p>
        </div>
      </li>
      <li class="aui-border-b">
        <div class="list-left">
          <img src="../../assets/images/public/bank_logo/recharge/recharge_icon_BOC.png">
        </div>
        <div class="list-right">
          <p class="bank-name">中国银行</p>
          <p class="list-desc">单笔1万，日累计1万</p>
        </div>
      </li>
      <li class="aui-border-b">
        <div class="list-left">
          <img src="../../assets/images/public/bank_logo/recharge/recharge_icon_CCB.png">
        </div>
        <div class="list-right">
          <p class="bank-name">中国建设银行</p>
          <p class="list-desc">单笔5千，日累计1万</p>
        </div>
      </li>
      <li class="aui-border-b">
        <div class="list-left">
          <img src="../../assets/images/public/bank_logo/recharge/recharge_icon_CEB.png">
        </div>
        <div class="list-right">
          <p class="bank-name">中国光大银行</p>
          <p class="list-desc">单笔50万，日累计50万</p>
        </div>
      </li>
    </ul>
  </div>
</template>
<style scoped>
  .bank-list{
    width: 100%;
    background: #fff;
  }
  .bank-list li{
    display: block;
    height: .65rem;
    width: 100%;
    padding: 0 5%;
  }
  .list-left{
    width: 11%;
    float: left;
    height: 100%;
  }
  .list-left img{
    width: .22rem;
    height: .22rem;
    margin-top: .1rem;
  }
  .list-right{
    float: left;
    width: 80%;
    height: 100%;
  }
  .bank-name{
    font-size: .16rem;
    margin-top: .09rem;
    color: #666;
  }
  .list-desc{
    line-height: .38rem;
    color: #999;
    font-size: .12rem;
  }
</style>
