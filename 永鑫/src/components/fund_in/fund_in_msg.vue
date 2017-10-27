<!-- 粮票宝转入-结果页 by fenglei -->
<template>
  <div class="page">
    <div class="bg-fff">
      <div class="list-plan">
        <div class="item aui-border-l current">
          <img class="top-pos" src="../../assets/images/fund/ico_cg.png">
          <h3 class="main-color">成功转入{{ dataMsg.money }}元</h3>
          <p>{{ dataMsg.nowTime }}</p>
        </div>
        <div class="item aui-border-l">
          <img class="top-pos" src="../../assets/images/fund/ico_js.png">
          <h3>计算收益时间</h3>
          <p>{{ dataMsg.preProfitTime }}</p>
        </div>
        <div class="item">
          <img class="top-pos" src="../../assets/images/fund/ico_dq.png">
          <h3>收益到账时间</h3>
          <p>{{ dataMsg.preInterestTime }}</p>
        </div>
      </div>
      <div class="padding-lr-15 padding-b-20 confirm-btn">
        <mt-button size="large" type="danger" @click="toBack">完成</mt-button>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    data() {
      return {
        dataMsg: {}
      };
    },
    methods: {
      dataLoad(data) {
        this.dataMsg = data;
      },
      toBack() {
        history.back();
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "fund_in_msg.sass";
</style>
