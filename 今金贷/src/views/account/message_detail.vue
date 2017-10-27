<template>
  <div class="page" id="message">
    <mt-header class="bar-nav" title="消息详情">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="message aui-border-t">
      <div class="message-top">
        <p class="message-title">{{ resdata.title }}</p>
        <p class="message-time">{{ resdata.createTime | dateFormatFun(4)}}</p>
      </div>
      <div class="message-content" v-html="resdata.content">
        <!--<p>您投资项目</p>-->
      </div>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config'
  export default {
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      let urlParams = {
        id: this.$route.query.id,
        userId: this.$store.state.user.userId,
        __sid: this.$store.state.user.__sid
      }
      this.$http.get(ajaxUrl.letterInfo, { params: urlParams }).then((res) => {
        this.$indicator.close()
        this.resdata = res.data.resData
      })
    },
    data(){
      return {
        resdata: ''
      }
    }
  }
</script>
<style scoped>
  .page {background:#fff;}
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
    line-height:.24rem;
  }
  .message-content{
    width: 100%;
    margin-top: .15rem;
    color: #333;
    line-height:.24rem;
  }
</style>
