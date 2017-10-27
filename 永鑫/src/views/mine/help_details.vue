<!-- 平台公告详情 by fenglei -->
<template>
  <div class="page">
    <div class="message">
      <div class="message-top">
        <p class="message-title">{{ dsiteIntroData.title }}</p>
      </div>
      <div class="message-content" v-html="dsiteIntroData.content"></div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config';

  export default {
    data() {
      return {
        dsiteIntroData: '',
        getParams: {
          uuid: this.$route.params.uuid
        }
      };
    },
    created() {
      this.$http.get(ajaxUrl.helpArticleDetail, { params: this.getParams }).then((res) => {
        this.dsiteIntroData = res.data.resData;
      })
    }
  }
</script>

<style type="text/css" scoped>
  .page{
    background: #FFFFFF;
  }
  .message{
    padding: 0.2rem 5%;
    background: #FFFFFF;
  }
  .message-top{
    display: inline-block;
    width: 100%;
    text-align: left;
  }
  .message-title{
    margin-bottom: 0.05rem;
    font-size: 0.18rem;
    font-weight: bold;
    color: #333;
  }
  .message-time{
    margin-bottom: 0.15rem;
    font-size: 0.14rem;
    color: #999;
  }
  .message-content{
    margin-top: 0.1rem;
    font-size: 0.14rem;
    color: #666;
  }
</style>
