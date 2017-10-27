<!-- 关于我们 -->
<template>
  <div class="page">
    <mt-header class="bar-nav" title="关于我们">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="about">
      <div class="about-img text-center">
        <img src="../../assets/images/logReg/login_logo.png">
      </div>
      <mt-cell title="融都介绍" to="/mine/about_us/compan_intro"  is-link></mt-cell>
      <mt-cell title="安全保障" to="/mine/about_us/security"  is-link></mt-cell>
      <mt-cell title="客户服务" to="/mine/about_us/custom_service"  is-link></mt-cell>
    </div>
  </div>
</template>

<style type="text/css" scoped>
  .about-img img{
    width: 1.5rem;
    margin-top: .43rem;
    margin-bottom: .35rem;
  }
</style>
