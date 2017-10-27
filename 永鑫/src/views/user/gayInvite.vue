<!-- 好友邀请落地页 -->
<template>
  <div class="gay" :style="{ height: wrapperHeight + 'px' }">
    <div @click="toLink" class="register"></div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config';

  export default {
    data() {
      return {
        wrapperHeight: 0
      };
    },
    created() {
      this.$nextTick(() => {
        this.wrapperHeight = document.documentElement.clientHeight;
      });
    },
    methods: {
      toLink() {
        let params = {
          ui: this.$route.query.ui
        };
        this.$http.get(ajaxUrl.gayInvite, { params: params }).then((res) => {
          location.href = res.data.resMsg;
        })
      }
    }
  }
</script>

<style type="text/css">
  .gay {
    position: relative;
    width: 100%;
    background-image: url(../../assets/images/me/gayInvite.png);
    background-repeat: no-repeat;
    background-size: 100% 100%;
  }
  .register {
    position: absolute;
    width: 100%;
    height: 10%;
    bottom: 6%;
  }
</style>
