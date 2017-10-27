<!-- 关于我们 -->
<template>
  <div class="page">
    <mt-header v-if="!hideHeader" class="bar-nav" title="关于我们">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="about">
      <div class="about-img text-center">
        <img src="../../assets/images/logReg/login_logo.png">
      </div>
      <mt-cell title="{app_name}介绍" @click.native="toUrl('companIntro')" is-link></mt-cell>
      <mt-cell title="安全保障" @click.native="toUrl('security')" is-link></mt-cell>
      <mt-cell title="客户服务" @click.native="toUrl('customService')" is-link></mt-cell>
    </div>
  </div>
</template>
<script>
  import  hideHead from '../../mixins/hideHeader'
  export default {
    created() {
      if(this.hideHeader) {
        document.title = this.$route.meta.title
      }
    },
    methods: {
      toUrl(name) {
        if(this.hideHeader) {
          name = 'app' + name
        }
        this.$router.push({name: name})
      }
    },
    mixins: [ hideHead ]
  }
</script>
<style type="text/css" scoped>
  .about-img img{
    width: 1.5rem;
    margin-top: .43rem;
    margin-bottom: .35rem;
  }
</style>
