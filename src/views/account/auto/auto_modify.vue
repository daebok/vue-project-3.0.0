<template>
  <div class="page">
    <mt-header class="bar-nav" title="自动投资参数">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <ul class="auto-param margin-t-10">
      <li class="aui-border-b">
        <span>单日最高可投</span>
        <span class="pull-right color-333">{{resdata.amountDayMax | currency('',2)}}元</span>
      </li>
      <li class="aui-flex aui-flex-col aui-border-b">
        <div class="aui-flex-item-2">收益方式</div>
        <div class="aui-flex-item-10 color-333">
          <div v-if="repayStyles && repayStyles.match(1)">等额本息</div>
          <div v-if="repayStyles && repayStyles.match(2)">一次性还款</div>
          <div v-if="repayStyles && repayStyles.match(3)">每月还息到期还本</div>
          <div v-if="repayStyles && repayStyles.match(4)">等额本金</div>
          <div v-if="repayStyles && repayStyles.match(5)">每季还息到期还本</div>
        </div>
      </li>
      <li class="aui-flex aui-flex-col aui-border-b aui-flex-middle">
        <div class="aui-flex-item-2">投资期限</div>
        <div class="aui-flex-item-10 color-333">
          <div v-if="resdata.dayType == 1">{{resdata.dayLimitMin}}-{{resdata.dayLimitMax}}天</div>
          <div v-if="resdata.monthType == 1">{{resdata.monthLimitMin}}-{{resdata.monthLimitMax}}个月</div>
        </div>
      </li>
      <li class="aui-border-b">
        <span>投资收益</span>
        <span v-if="resdata.aprMin" class="pull-right color-333">{{resdata.aprMin | decimals}}%起</span>
      </li>
      <li class="aui-flex aui-flex-col aui-border-b">
        <div class="aui-flex-item-2">产品类型</div>
        <div class="aui-flex-item-10 color-333">
          <div v-if="resdata.bondUseful==1 && resdata.realizeUseful==1">可转让产品<br/>可变现产品</div>
          <div v-if="resdata.bondUseful!=1 && resdata.realizeUseful==1">可变现产品</div>
          <span v-if="resdata.bondUseful==1 && resdata.realizeUseful!=1">可转让产品</span>
          <span v-if="resdata.bondUseful!=1 && resdata.realizeUseful!=1">不限</span>
        </div>
      </li>
    </ul>
    <div class="margin-t-30 margin-lr-15">
      <router-link to="/account/auto/setting/">
        <mt-button type="danger" size="large" class="update-btn">修改参数</mt-button>
      </router-link>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  export default {
    data(){
      return {
        resdata: '',
        repayStyles: ''
      }
    },
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      let getParams = {
        userId: this.$store.state.user.userId,
        __sid: this.$store.state.user.__sid
      }
      this.$http.get(ajaxUrl.autoInvestDetails, {params: getParams}).then((res) => {
        this.resdata = res.data.resData
        this.repayStyles = this.resdata.repayStyles
        this.$indicator.close() // 关闭提示
      })
    },
  }
</script>

<style scoped>
  .auto-param li{
    background:#fff;
    padding:.15rem;
    color: #666;
    overflow: auto;
  }
  .aui-flex-item-2 {
    line-height:.24rem;
  }
  .aui-flex-item-10 {
    text-align: right;
  }
</style>
