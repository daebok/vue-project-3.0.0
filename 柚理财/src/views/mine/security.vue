<!-- 安全保障 -->
<template>
  <div class="page">
    <mt-header v-if="!hideHeader" class="bar-nav aui-border-b" title="安全保障">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="security">
      <img src="../../assets/images/me/me_pic_safety.png" />
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
    mixins: [ hideHead ]
  }
</script>
<style type="text/css" scoped>
  .security img{
    width: 100%;
    vertical-align: top;
  }
</style>

