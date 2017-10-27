<!-- 企业介绍 -->
<template>
  <div class="page">
    <mt-header v-if="!hideHeader" class="bar-nav" title="{app_name}介绍">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class=" intro">
      <div class="title-img margin-t-25 margin-b-25">
        <img src="../../assets/images/logReg/login_logo.png" />
      </div>
      <div class="intro-content" v-html="resdata">
        <!--<p>-->
          <!--融都科技具有丰富的互联网金融系统开发、运维及服务经验。目前 拥有网贷P2P平台、贷款自助营销平台、金融资产风控系统、金融产品管理系统、互联网金融交易平台、-->
          <!--众筹平台、银行资金存管系统、小贷业务管理系统等八大系列互联网金融产品，还有融都金融云、在线供应链金融解决方案、消费金融解决方案、金融O2O模式解决方案、-->
          <!--一站式理财解决方案等四大行业解决方案。-->
        <!--</p>-->
      </div>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config.js';
  import '../../assets/css/siteIntro_detail.css'
  import  hideHead from '../../mixins/hideHeader'
  export default {
    data() {
      return {
        resdata: ''
      }
    },
    created() {
      this.$http.get(ajaxUrl.getArticleListcolumn).then((res) => {
        this.resdata = res.data.resData.content;
      })
    },
    mixins: [ hideHead ]
  }
</script>
<style type="text/css">
  /*body,.page { background:#fff;}*/
  .intro{
    padding: .2rem .15rem 0;
  }
  .title-img{
    text-align: center;
  }
  .title-img img{
    width: 1.46rem;
    margin-bottom: .1rem;
  }
  .intro-content {
    color:#666;
    line-height: .24rem;
  }
  .intro-content p{width: auto!important;}
  .intro{background:#fff;}
</style>

