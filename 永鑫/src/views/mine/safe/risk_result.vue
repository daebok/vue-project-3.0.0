<!-- 风险评测结果 -->
<template>
  <div class="page">
    <div class="rat-result">
      <div class="rat-1">
        <img v-if="$route.query.level == 0" src="../../../assets/images/me/icon_evalution_results_bsx.png" alt="保守型" />
        <img v-if="$route.query.level == 1" src="../../../assets/images/me/icon_evalution_results_wjx.png" alt="稳健型" />
        <img v-if="$route.query.level == 2" src="../../../assets/images/me/icon_evalution_results_phx.png" alt="平衡型" />
        <img v-if="$route.query.level == 3" src="../../../assets/images/me/icon_evalution_results_czx.png" alt="成长型" />
        <img v-if="$route.query.level == 4" src="../../../assets/images/me/icon_evalution_results_jqx.png" alt="进取型" />
        <img v-if="$route.query.level > 4" src="../../../assets/images/me/icon_evalution_results_jqx.png" />
      </div>
      <div calss="rat-2">
        <p class="result-title">您的风险承受能力类型为</p>
        <p class="your-style">{{$route.query.levelName}}</p>
      </div>
      <div class="rat-promt">
        <p>评测结果并不具备全面性及承诺性</p>
        <p>理财有风险，投资需谨慎。</p>
      </div>
      <div class="rat-btn">
        <router-link :to="investUrl">
          <mt-button type="default" class="rat-btn-left">{{ investBtn }}</mt-button>
        </router-link>
        <router-link :to="backUrl">
          <mt-button type="default" class="rat-btn-right">重新评测</mt-button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    data() {
      return {
        investBtn: '立即投资',
        investUrl: '/invest',
        toSkip: '/mine/safe?from=risk_result',
        backUrl: '/mine/safe/risk_tips'
      };
    },
    created() {
      if (this.$route.query.from) {
        this.investBtn = '继续投资';
        this.toSkip = this.investUrl = '/' + this.$route.query.from + '/' + this.$route.query.id + '?from=risk_result';
        this.backUrl = '/mine/safe/risk_tips?from=' + this.$route.query.from + '&id=' + this.$route.query.id;
        // 来自资产账户的风险测评
        if (this.$route.query.from == 'account') {
          this.toSkip = this.investUrl = '/' + this.$route.query.from;
        }
      }
    }
  }
</script>

<style type="text/css" scoped>
  body,.page { background: #fff;  }
  .rat-1{
    margin-top: .55rem;
    text-align: center;
  }
  .rat-1 img{
    width: 1.5rem;
    height: 1.5rem;
  }
  .result-title{
    font-size: .15rem;
    color: #333;
    text-align: center;
    padding: .23rem 0 .17rem 0;
  }
  .your-style{
    font-size: .36rem;
    color: #333;
    text-align: center;
  }
  .rat-promt{
    margin-top: .35rem;
    margin-bottom: .35rem;
  }
  .rat-promt p{
    text-align: center;
    font-size: .13rem;
    color: #666;
    line-height: .2rem;
  }
  .rat-btn-left,.rat-btn-right{
    font-size: .14rem;
    width: 40%;
    background: #fff;
    padding: 0;
    height: .38rem;
    box-shadow: none;
  }
  .rat-btn-left{
    margin-left: 7%;
    color: #F95A28;
    border: 1px solid #F95A28;
  }
  .rat-btn-right{
    margin-left: 4%;
    color:#999;
    border: 1px solid #999;
  }
</style>
