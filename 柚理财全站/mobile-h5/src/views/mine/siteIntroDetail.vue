<!-- 平台公告详情 -->
<template>
  <div class="page" id="message">
    <mt-header class="bar-nav" title="平台公告详情">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="message aui-border-t">
      <div class="message-top">
        <p class="message-title">{{ resdata.title }}</p>
        <p class="message-time">{{resdata.createTime | dateFormatFun(4)}}</p>
      </div>
      <div class="message-content" id="con" v-html="resdata.content"></div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js'
  import 'assets/css/siteIntro_detail.css'

  export default {
    data(){
      return {
        resdata: ''
      };
    },
    created() {
      this.$indicator.open({ spinnerType: 'fading-circle' }); // mint-ui加载中动画效果
      let urlParams = {
        uuid: this.$route.params.uuid
      };
      this.$http.get(ajaxUrl.articleList, { params: urlParams }).then((res) => {
        this.resdata = res.data.resData;
        this.$indicator.close();
      })
    }
  }
</script>

<style type="text/css" scoped>
  .page {background:#fff;}
  p {line-height:.24rem;}
  .message{
    width: 100%;
    padding: 0 .15rem .5rem;
  }
  .message-top{
    width: 100%;
    padding-top: .15rem;
  }
  .message-title{
    font-size: .16rem;
    color: #333;
  }
  .message-time{
    font-size: .13rem;
    color: #999;
  }
  .message-content{
    width: 100%;
    margin-top: .15rem;
  }
  .message-content img{
    width: 100%;
  }
  .message-content {
    line-height: .24rem;
    color: #333;
  }
</style>
