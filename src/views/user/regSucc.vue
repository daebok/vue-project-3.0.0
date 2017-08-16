<!-- 注册成功 -->
<template>
  <div class="page">
    <mt-header class="bar-nav" title="注册成功" ></mt-header>
    <div class="content">
      <div class="text-center">
        <img src="../../assets/images/logReg/tips_registered_success.png">
        <h4>恭喜您注册成功！</h4>
        <p class="color-999">为了顺利投资，请先开通资金托管账户！</p>
      </div>
      <div class="btns clearfix">
        <router-link to="/mine/safe/realname/"><span class="main-color border-col">立即开通</span></router-link>
        <router-link to="/index"><span class="last-span">先去逛逛</span></router-link>
      </div>
    </div>
  </div>
</template>

<style type="text/css" scoped>
  img {width: .9rem;margin-top: .61rem;}
  h4 {margin-top: .21rem;font-size: .18rem;}
  p { font-size: .15rem;}
  .btns {text-align:center; margin-top: .38rem;}
  .btns span {width: 1.4rem; text-align: center; border: 1px solid #333; line-height: .38rem;display:inline-block;border-radius: 3px; font-size: .16rem;}
  .btns span.border-col {border-color:#F95A28;}
  .btns span.last-span {margin-left: .31rem;}
</style>
