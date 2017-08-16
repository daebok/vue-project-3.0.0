<style lang="scss" rel="stylesheet/scss" scoped>
  .box {
    /*position: absolute;*/
    width: 100%;
    min-height: 1.8rem;
    top: 0; bottom: .55rem;
    background: #fff url(../../assets/images/shop/score_bg.png) top center no-repeat;
    background-size: 100% auto;
    .use-score {
      padding-top: .63rem;
      text-align: center;
      color: #fff;
      h2 { font-size: .35rem; line-height: 1; }
      p { opacity: .8; font-size: .16rem; padding-top: .08rem; }
    }
    .score-info {
      font-size: 0;
      text-align: center;
      margin-top: .35rem;
      li {
        display: inline-block; width: 50%;
        h3 { color: #fff; font-size: .22rem; line-height: 1; }
        p { color: #fff; opacity: .8; font-size: .13rem; padding-top: .05rem; }
      }
    }
    .score-link {
      font-size: 0;
      text-align: center;
      margin-top: 1rem;
      position: relative;
      .vertical-line { height: .4rem; width: 1px; position: absolute; top: 50%; margin-top: -.2rem; left: 50%; margin-left: -1px;  }
      li {
        display: inline-block; width: 50%;
        img { width: .42rem; }
        p { color: #666; font-size: .15rem; padding-top: .1rem; }
      }
    }
    .btn {
      margin-top: .7rem;
      background: url('../../assets/images/shop/btn_change.png') top center no-repeat;
      background-size: auto .72rem;
      height: 1.1rem;
    }
  }
</style>
<template>
  <div class="page">
  <mt-loadmore :top-method="loadTop" ref="loadmore">
  <div class="box">
    <div class="use-score">
      <h2 class="font-arial">{{scoreData | currency('', 0)}}</h2>
      <p>可用积分</p>
    </div>
    <ul class="score-info">
      <li><h3 class="font-arial">{{resdata.waitVerifyNum | currency('', 0)}}</h3><p>待审核</p></li>
      <li><h3 class="font-arial">{{resdata.verifySuccessNum | currency('', 0)}}</h3><p>审核通过</p></li>
    </ul>
    <ul class="score-link">
      <router-link to="/shop/myScore/log"><li><img src="../../assets/images/shop/score_log.png"><p>积分记录</p></li></router-link>
      <router-link to="/shop/myScore/change"><li><img src="../../assets/images/shop/my_exchange.png"><p>我的兑换</p></li></router-link>
      <div class="vertical-line aui-border-l"></div>
    </ul>
    <router-link to="/shop/list"><div class="btn"></div></router-link>
  </div>
  </mt-loadmore>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config.js';
  export default {
    data () {
      return {
        scoreData: '',
        resdata: '',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      }
    },
    created() {
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      this.$http.get(ajaxUrl.getVerifyNum, {params: this.getParams}).then((res) => {
        this.resdata = res.data.resData
        this.$indicator.close()
      })
      this.$http.get(ajaxUrl.getUserScore, {params: this.getParams}).then((res) => { // 用户积分
        this.scoreData = res.data.resData.userScore.useScore
      })
    },
    methods: {
      loadTop(id) {
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          window.location.reload();
        }, 1000)
      }
    }
  }
</script>
