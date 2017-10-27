<template>
  <div class="page">
    <mt-header class="bar-nav" title="了解项目" >
      <mt-button slot="left" icon="back" v-back-link></mt-button>
      <router-link :to="'/investDetail/'+$route.query.oldProjectId" slot="right">
        <mt-button>查看原项目</mt-button>
      </router-link>
    </mt-header>
    <div class="bond-info">
      <div class="realize-describe-title aui-border-b border-color margin-b-15">
        <img src="../../../assets/images/finance/details_icon_jkf.png" />
        <span>借款方所持资产信息<p>(该项目回款将作为借款方还款来源)</p></span>
      </div>
      <table class="table-list margin-b-10" border="1">
        <tr >
          <td class="td-left">产品名称</td>
          <td class="td-right">{{resdata.projectName}}</td>
        </tr>
        <tr>
          <td class="td-left">投资金额</td>
          <td class="td-right">{{resdata.investAmount}}元</td>
        </tr>
        <tr>
          <td class="td-left">预期年化收益</td>
          <td class="td-right">{{resdata.Apr}}%</td>
        </tr>
        <tr>
          <td class="td-left">投资期限</td>
          <td class="td-right">{{resdata.timeLimitType}}</td>
        </tr>
        <tr>
          <td class="td-left">收益方式</td>
          <td class="td-right">{{resdata.repayStyle}}</td>
        </tr>
        <tr>
          <td class="td-left">收款日</td>
          <td class="td-right">{{resdata.oldRepayTime}}</td>
        </tr>
      </table>
      <div class="borrow-describe-title aui-border-b margin-b-15">
        <img src="../../../assets/images/finance/details_icon_cpjj.png" />
        <span>产品简介</span>
      </div>
      <div class="bond-content margin-b-10">
        <p>
          “变现通”是平台推出的投融资服务之一。平台向出借方和借款方提供变现服务，帮助双方快捷方便地完成投资和借贷。通过该服务，符合条件的产品持有人，可以以其持有的特定资产的本金及预期收益作为还款来源，通过平台向出借方发起融资请求，获得快速便捷的短期融资机会。借贷双方通过平台签署借贷协议，明确双方的债务与债权关系。
        </p>
      </div>
      <div class="borrow-describe-title aui-border-b margin-b-15">
        <img src="../../../assets/images/finance/details_icon_cptd.png" />
        <span>产品特点</span>
      </div>
      <div class="bond-content margin-b-20">
        <p>
          1、以特定资产的本金及预期收益作为直接还款来源 通过“变现通”达成的借款，由借款方投资并持有的特定资产的本金及预期收益作为还款来源。当借款方借款到期，系统自动以其持有的特定资产的回款本息来清偿其应还款项。
        </p>
        <p>
          2、放款后次日计息，到期一次性还本付息出借方通过“变现通”借出资金，产品投满后统一放款给借款方，放款后次日即可计息。借款到期后，该款项可在到期日当日一次性还本付息到出借方的平台账户中。
        </p>
        <p>
          3、预期年化利率为固定利率通过“变现通”服务达成的交易的预期年化利率为固定利率，不会随着中国人民银行同期贷款预期利率的波动而调整。
        </p>
      </div>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  export default {
    data(){
      return {
        resdata: ''
      }
    },
    created(){
      let getParams = {
        investId: this.$route.query.investId,
        userId: this.$store.state.user.userId,
        __sid: this.$store.state.user.__sid
      }
      this.$http.get(ajaxUrl.realizeInfo, {params:getParams}).then((res) => {
        this.resdata = res.data.resData
      })
    },
  }
</script>
<style scoped>
  @import "../../../assets/scss/var.scss";
  html,body {background:#fff;}
  .bond-info{
    width: 100%;
    padding: 0 .15rem;
    background:#fff;
  }
  .borrow-describe-title,.realize-describe-title{
    color: #666;
  }
  .borrow-describe-title{
    height: .45rem;
  }
  .realize-describe-title{
    height: .6rem;
  }
  .borrow-describe-title img,.realize-describe-title img{
    height: .2rem;
    float: left;
    margin-right: .1rem;
  }
  .borrow-describe-title img{
    margin-top: .125rem;
  }
  .realize-describe-title img{
    margin-top: .16rem;
  }
  .realize-describe-title span{
    margin-top: .19rem;
  }
  .borrow-describe-title span,.realize-describe-title span{
    float: left;
    color: #666;
  }
  .borrow-describe-title span{
    line-height: .45rem;
  }
  .realize-describe-title span,.realize-describe-title p{
    line-height: 1;
  }
  .realize-describe-title p{
    padding-top: .05rem;
    font-size: .12rem;
    color: #999;
  }
  .bond-content p{
    line-height: .28rem;
  }
  .table-list{
    border-collapse: collapse;
    width: 100%;
    border: 1px solid #fff;
  }
  .table-list tr{
    line-height: .35rem;
  }
  .td-left{
    background: #F4F3F3;
    text-align: center;
    color: #666;
  }
  .td-right{
    background: #F9F9F9;
    padding-left: .1rem;
  }
</style>

