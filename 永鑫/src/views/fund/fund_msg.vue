<template>
  <div class="bg-fff">
    <!--转入-->
    <div v-if="$route.query.from == 'in'" class="list-plan">
      <div class="item aui-border-l current">
        <img class="top-pos" src="../../assets/images/fund/ico_cg.png" >
        <h3 class="main-color">成功转入{{fundInResdata.money}}元</h3>
        <p>{{fundInResdata.nowTime}}</p>
      </div>
      <div class="item aui-border-l">
        <img class="top-pos" src="../../assets/images/fund/ico_js.png" >
        <h3>计算收益时间</h3>
        <p>{{fundInResdata.preProfitTime}}</p>
      </div>
      <div class="item">
        <img class="top-pos" src="../../assets/images/fund/ico_dq.png" >
        <h3>收益到账时间</h3>
        <p>{{fundInResdata.preInterestTime}}</p>
      </div>
    </div>
    <!--转出-->
    <div v-else class="list-plan">
      <div class="item aui-border-l current">
        <img class="top-pos" src="../../assets/images/fund/ico_cg.png" >
        <h3 class="main-color">申请已提交</h3>
        <p>{{fundOutResdata.nowTime}}</p>
      </div>
      <div class="item">
        <img class="top-pos" src="../../assets/images/fund/ico_js.png" >
        <h3>计算到账时间</h3>
        <p>{{fundOutResdata.preArrivalTime}}</p>
      </div>
    </div>
    <div class="padding-lr-15 padding-b-20 confirm-btn">
      <mt-button size="large" type="danger" @click="$router.push('/account')">完成</mt-button>
    </div>
  </div>
</template>
<script type="text/ecmascript-6">
  export default {
    data() {
      return {
        fundInResdata: '',
        fundOutResdata: ''
      };
    },
    created() {
      let from = this.$route.query.from
      if(from == 'out'){
        document.title = '转出结果'
        this.fundOutResdata = JSON.parse(sessionStorage.fundOut_resdata)
      }else{
        document.title = '转入结果'
        this.fundInResdata = JSON.parse(sessionStorage.fundIn_resdata)
      }
    },

  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../assets/scss/var';
  .bg-fff { background: #fff; }
  .list-plan {
    padding: .3rem .15rem 0 .45rem;
    color: #333;
    .item {
      position: relative;
      padding-left: .4rem;
      padding-bottom: .3rem;
      &.current {
        color: $main-color;
      }
      .top-pos {
        position: absolute;
        width: .3rem;
        top: 0;
        left: -.15rem;
        z-index: 1024;
      }
      h3 {color: #666;}
      p {
        font-size: .12rem;
        color: #999;
      }
    }
  }
</style>
