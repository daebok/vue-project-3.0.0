<template>
  <ul>
  <li class="bid-con aui-border-b" :class="{'gray-status': item.remainAccount == 0 || item.remainMoney == 0}" v-for="(item,index) in list" @click="toUrl(item.projectId, item.bondName)">
    <h3 v-if="item.projectName" :id="item.projectId">{{ item.projectName | hideBorrowName(24) }}
      <span v-if="item.novice == 1" class="bid-icon new aui-border">新客</span>
      <span v-if="item.bondUseful == 1" class="bid-icon kzr aui-border">可转让</span>
      <span v-if="item.realizeUseful == 1" class="bid-icon kbx aui-border">可变现</span>
      <span v-if="item.specificSale == 1 || item.specificSale == 3" class="bid-icon dx aui-border">定向</span>
      <span v-if="item.specificSale == 2" class="bid-icon vip aui-border">VIP{{item.specificSaleRule}}</span>
    </h3>
    <!--债权转让-->
    <h3 v-if="item.bondName" :id="item.projectId">{{ item.bondName | hideBorrowName(24) }}</h3>
    <div class="bid-info clearfix">
      <div class="apr-part pull-left">
        <h1 class="apr main-color">{{ item.apr | currency('', 2) }}<b>%<span v-if="item.addApr > 0">+{{ item.addApr | currency('', 2) }}%</span></b></h1>
        <p>预期年化收益率</p>
      </div>
      <div v-if="item.projectName" class="time-part pull-left">
        <p>起投  <b>{{ item.lowestAccount | currency('', 0) }}元</b></p>
        <p v-if="item.timeType == 0">期限  <b>{{ item.timeLimit }}个月</b></p>
        <p v-if="item.timeType == 1">期限  <b>{{ item.timeLimit }}天</b></p>
      </div>
      <!--债权转让-->
      <div v-if="item.bondName" class="time-part pull-left">
        <p v-if="item.bondApr >= 0">溢价  <b class="bondApr">+{{ item.bondApr | currency('', 1) }}%</b></p>
        <p v-else>折价  <b class="bondApr">{{ item.bondApr | currency('', 1) }}%</b></p>
        <p>期限  <b>{{ item.remainDays }}天</b></p>
      </div>
    </div>
    <div v-if="item.projectName" class="circle-area">
      <div v-if="item.remainAccount > 0">
        <circle-progress v-if="item.saleTime > item.timeNow" :percent="0" :diameter="60" :text-size="11" text-color="#f60" foreground-color="#F95A28" backgroundColor="#f8f8f8" :text-format="item.saleTime | saleTime(item.timeNow)" :showFlag="false" :showTime="true" :line-width="2"></circle-progress>
        <circle-progress v-else :percent="item.remainAccount | scalesFun(item.account)" :diameter="60" :text-size="14" text-color="#f60" foreground-color="#F95A28" backgroundColor="#f8f8f8" text-format="{percent}" :line-width="2"></circle-progress>
      </div>
      <div v-else>
        <circle-progress :percent="100" :diameter="60" :text-size="14" foreground-color="#999" backgroundColor="#f8f8f8"  text-format="已抢光" :showFlag="false" :line-width="2"></circle-progress>
      </div>
    </div>
    <!--债权转让-->
    <div v-if="item.bondName" class="circle-area">
      <circle-progress v-if="item.remainMoney > 0" :percent="item.remainMoney | scalesFun(item.bondMoney)" :diameter="60" :text-size="14" text-color="#f60" foreground-color="#F95A28" backgroundColor="#f8f8f8" text-format="{percent}" :line-width="2"></circle-progress>
      <circle-progress v-else :percent="100" :diameter="60" :text-size="14" foreground-color="#999" backgroundColor="#f8f8f8"  text-format="已抢光" :showFlag="false" :line-width="2"></circle-progress>
    </div>
  </li>
  </ul>
</template>

<script type="text/ecmascript-6">
  import CircleProgress from './ProgressCircle'
  export default {
    props: {
      list: {
        type: Array,
        default: null
      }
    },
    methods: {
      toUrl(projectId, bondName){
        // 债权转让
        if(this.$route.query.uuid == 'a31bd335e12ac0dced8849a16fd4a894'){
          this.$router.push({name:'bondDetail', params: {projectId: projectId}, query: {bondName:bondName}})
        }
        // 变现转让
        else if(this.$route.query.uuid == '090d5d939784fe33aceff143ba1c198c'){
          this.$router.push({name:'realizeDetail', params: {projectId: projectId}})
        }
        // 普通投资
        else{
          this.$router.push({name:'investDetail', params: {projectId: projectId}})
        }
      },
    },
    components: {
      CircleProgress
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  .bid-con
    position: relative
    height: 1.24rem
    padding: .15rem
    background: #fff
    &:last-child:after
      border-bottom: 0
    h3
      font-size: .15rem
      color: #333
      img
        width: .35rem
        vertical-align: -.02rem

  .bid-info
    margin-top: .15rem
  .apr-part
    .apr
      font-size: .31rem
      font-family: Arial
      margin: -.05rem 0 .03rem 0
      line-height: 1
      b
        font-size: .16rem
    p
      color: #666

  .circle-area
    position: absolute
    right: .15rem
    top: .3rem
  .time-part
    font-size: .14rem
    color: #666
    p
      &:first-child
        margin-bottom: .1rem
      b.bondApr
        color: #48affb
  .apr-part
    width: 1.8rem
  /*icon分类*/
  .bid-icon
    font-size: .11rem
    line-height: .16rem
    padding: 0 .05rem
    display: inline-block
    text-align: center
    &.aui-border:after
      border-radius: .18rem
    /*新客*/
    &.new
      color: #F95A28!important
      &.aui-border:after
        border-color: #F95A28
    /*定向*/
    &.dx
      color: #97d1fd!important
      &.aui-border:after
        border-color: #97d1fd
    /*可转让*/
    &.kzr
      color: #89d096!important
      &.aui-border:after
        border-color: #89d096
    /*vip*/
    &.vip
      color: #f9b728!important
      &.aui-border:after
        border-color: #f9b728
    /*可变现*/
    &.kbx
      color: #ad60f9!important
      &.aui-border:after
        border-color: #ad60f9
</style>
