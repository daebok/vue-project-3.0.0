<template>
  <div class="page">
    <section class="rule-wrapper">
      <p class="rule-info">
        交易日15:00前申购,下一个交易日开始享受收益<br>
        交易日15:00后/非交易日申购,下2交易日开始享受收益<br>
        交易日15:00前赎回,想截止日当日收益,预计下一交易日钱到账<br>
        交易日15:00后/非交易日赎回,享截止下一交易日收益,预计2交易日钱到账
      </p>
      <p class="rule-tip">
        <img src="../../../assets/images/yxbx/xiangqing_tip.png" class="icon-tip"/>
        赎回资金实际到账时间请以银行短信通知为主
      </p>
    </section>
  </div>
</template>
<script>
  export default {
    data(){
      return {
      }
    },
    created(){
    }
  }
</script>
<style scoped>
  /*基金项目*/
  .rule-wrapper{background-color: #fff; padding: .15rem .15rem .25rem .15rem;}
  .rule-info{color: #333;}
  .rule-tip{color: #999; margin-top: .15rem;}
  .icon-tip{width: .14rem; position: relative; top:.01rem; margin-right: .03rem;}
</style>