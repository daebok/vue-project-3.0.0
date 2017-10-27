<template>
  <div class="page">
    <ul class="prodect-detail">
      <li class="aui-border-b">
        基金类型
        <span class="pull-right">货币型</span>
      </li>
      <li class="aui-border-b">
        基金管理人
        <span class="pull-right">创金合信基金管理有限公司</span>
      </li>
      <li class="aui-border-b">
        成立时间
        <span class="pull-right">2015-10-22</span>
      </li>
      <li class="aui-border-b">
        风险收益特征
        <p>本基金为货币市场基金，预期风险和预期收益低于股票型基金、混合型基金和债券型基金。</p>
      </li>
      <li>
        业绩比较基准
        <p>业绩比较基准：中国人民银行公布的七天通知存款利率（税后）</p>
      </li>
    </ul>
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
  .prodect-detail{padding-left: .15rem; background-color: #fff;}
  .prodect-detail li{color: #666; padding: .15rem .15rem .15rem 0;}
  .prodect-detail span{color: #333;}
  .prodect-detail p{color: #333; margin-top: .15rem;}
</style>