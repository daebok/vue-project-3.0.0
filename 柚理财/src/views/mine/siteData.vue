<!-- 平台数据 -->
<template>
  <div class="page" id="siteData">
    <mt-header v-if="!hideHeader" class="bar-nav" title="平台数据">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="bg">
      <div class="title">
        <h1>平台数据统计</h1>
        <p>数据截止至{{ resdata.systemTime }}</p>
      </div>
      <div class="data-area">
        <div class="total">
          <h3 class="color-666">累计投资金额(万元)</h3>
          <h1 class="second-color">{{ resdata.investTotal | currency('',2) }}</h1>
        </div>
        <div class="data-det clearfix text-center">
          <div class="aui-flex-item-6">
            <h4 class="color-666">累计注册人数(位)</h4>
            <p class="color-333">{{ resdata.registerTotal }}</p>
          </div>
          <div class="aui-flex-item-6">
            <h4 class="color-666">累计赚取收益(万元)</h4>
            <p class="color-333">{{ resdata.totalEarnAmount | currency('',2) }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';
  import  hideHead from '../../mixins/hideHeader'
  export default {
    data() {
      return {
        resdata: ''
      };
    },
    created() {
      if(this.hideHeader) {
        document.title = this.$route.meta.title
      }
      this.$http.get(ajaxUrl.siteData).then((res) => {
        if (res.data.resData !== '') {
          this.resdata = res.data.resData;
        }else{
          this.$toast(res.data.resMsg);
        }
      })
    },
    mixins: [ hideHead ]
  }
</script>

<style type="text/css" scoped>
  .bg {
    background:#0E243A url(../../assets/images/me/me_data_bg.png) 0 0 no-repeat;
    background-size:100% 6.03rem;
    min-height: 6.8rem;
    position: relative;
  }
  .title {color:#fff;text-align: center;padding-top: .95rem;}
  .title h1 { font-size: .37rem; line-height: 1;}
  .title p { font-size: .15rem; padding-top: .1rem;}
  .data-area {position: absolute;top: 2.71rem;left:5%; width: 90%; padding:0 3%; text-align: center;}
  .total {height: 1.3rem;border-bottom:1px dashed #666;padding-top: .3rem;}
  .total h3 { font-size: .15rem; line-height: 1;}
  .total h1 {font-size: .35rem;line-height: 1;margin-top: .24rem;}
  .data-det { margin-top: .4rem;}
  .data-det h4 { font-size: .15rem; }
  .data-det p { font-size: .23rem; line-height: 1; font-family: 'arial'; margin-top: .15rem;}
</style>
