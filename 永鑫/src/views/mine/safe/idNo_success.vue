<!-- 身份证认证成功 by fenglei -->
<template>
  <div>
    <div class="bg-fff">
      <div class="icon-suc">
        <img src="../../../assets/images/me/shenfen_sc_chenggong.png" />
      </div>
      <div class="succ-tips">
        <p>您的身份证已上传，请耐心等待审核</p>
        <router-link to="/account?from=idNo_success">
          <mt-button size="small" type="danger" plain>我知道了</mt-button>
        </router-link>
      </div>
    </div>
  </div>
</template>
<style lang="scss" rel="stylesheet/scss" scoped>
  .icon-suc{
    padding: .4rem 0 0;
    text-align: center;
  }
  .icon-suc img{  width: .65rem; }
  .succ-tips{
    line-height: .18rem;
    padding: .1rem 20% .2rem;
    text-align: center;
    p {color: #666; margin-bottom: .15rem;}
  }
</style>
