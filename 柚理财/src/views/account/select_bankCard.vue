<template>
  <div class="page bank-show" id="bank_show">
    <mt-header class="bar-nav" title="选择银行卡">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="cash-top margin-t-15 aui-border-b" >
      <div class="bank-img">
        <img src="../../assets/images/public/bank_logo/withdraw/withdraw_icon_ABOC.png" >
      </div>
      <div class="bank-info">
        <p class="margin-t-10">中国农业银行</p>
        <p class="bank-no">尾号6371　储蓄卡</p>
      </div>
    </div>
    <div class="cash-top" >
      <div class="bank-img">
        <img src="../../assets/images/public/bank_logo/withdraw/withdraw_icon_CBC.png" >
      </div>
      <div class="bank-info">
        <p class="margin-t-10">中国工商银行</p>
        <p class="bank-no">尾号6372　储蓄卡</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .cash-top{
    height: .6rem;
    width: 100%;
    padding: 0 5%;
    background: #fff;
  }
  .bank-img{
    float: left;
    width: .4rem;
    height: 100%;
  }
  .bank-img img{
    height: .24rem;
    width: .24rem;
    vertical-align: middle;
    margin-top: .11rem;
  }
  .bank-info{
    width: 60%;
    float: left;
    height: 100%;
  }
  .bank-info p{
    font-size: .15rem;
    color: #333;
  }
  .bank-info .bank-no{
    color: #999;
    margin-top: .06rem;
    font-size: .13rem;
  }
  .cash-arrow-right{
    height: .14rem;
    float: right;
    margin-top: .23rem;
  }
</style>
