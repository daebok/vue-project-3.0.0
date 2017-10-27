<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" title="绑卡结果">
      <mt-button slot="right" class="color-666" v-back-link>完成</mt-button>
    </mt-header>
    <div class="addBank">
      <div class="icon-suc">
        <img src="../../../assets/images/public/bankcard_success.png" />
      </div>
      <div class="addBank-tips">
        您的绑卡请求第三方已受理，结果可在账户与安全>银行卡页查看！
      </div>
    </div>
  </div>
</template>
<style scoped>
  .icon-suc{
    margin-top: 30%;
    text-align: center;
  }
  .icon-suc img{
    width: .8rem;
    height: .8rem;
  }
  .addBank-tips{
    width: 100%;
    padding: 0 20%;
    line-height: .18rem;
    margin-top: .25rem;
  }
</style>
