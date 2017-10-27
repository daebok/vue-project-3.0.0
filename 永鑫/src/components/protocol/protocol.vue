<!-- 协议预览 by fenglei -->
<template>
  <div class="page protocol-con">
    <mt-header class="bar-nav" :title="title"></mt-header>
    <div class="content" v-html="content"></div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';

  export default {
    data() {
      return {
        content: '',
        title: ''
      };
    },
    methods: {
      dataLoad(params, url) {
        let getParams;
        if (url == 'top5Help') {
          this.$http.get(ajaxUrl[url], { params: { sectionCode: params }}).then((res) => {
            if (res.data.resData) {
              this.content = res.data.resData.articleList[0].content;
              this.title = res.data.resData.articleList[0].title;
            }
          })
        } else if (url == 'registerProtocolDetail') {
          this.$http.get(ajaxUrl[url], { params: { protocolId: params }}).then((res) => {
            if (res.data.resData) {
              this.content = res.data.resData.content;
              this.title = res.data.resData.titleName;
            }
          })
        }
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  .page
    background: #FFFFFF
</style>
