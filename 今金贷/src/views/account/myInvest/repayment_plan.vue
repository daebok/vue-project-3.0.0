<template>
  <div class="page" id="pageDetail">
    <mt-header class="bar-nav" title="回款计划">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="repayment">
      <ul class="repayment-title">
        <li>期数/回款日期</li>
        <li class="repayment-right">回款金额(元)/状态</li>
      </ul>
      <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
        <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
          <ul v-for="item in list" class="repayment-content">
            <li class="repayment-periods">{{item.periodsStr}}</li>
            <li v-if="item.collectionType == 1" class="repayment-money color-666">已转让</li>
            <li v-else class="repayment-money color-666">{{item.repayedAmount | currency('',2)}}</li>
            <li v-if="item.collectionType == 1" class="repayment-time color-999">已转让</li>
            <li v-else class="repayment-time color-999">{{item.repayTime | dateFormatFun(1)}}</li>
            <li class="repayment-status">
              <span v-if="item.repayedStatus == 1">
                <img src="../../../assets/images/money/status_waiting.png" class="tips-icon"/>
                <i>{{item.repayedAmountStr}}</i>
              </span>
              <span v-if="item.repayedStatus == 2">
                <img src="../../../assets/images/money/status_received.png" class="tips-icon"/>
                <i>{{item.repayedAmountStr}}</i>
              </span>
              <span v-if="item.repayedStatus == 3">
                <img src="../../../assets/images/money/status_received.png" class="tips-icon"/>
                <i>{{item.repayedAmountStr}}</i>
              </span>
            </li>
          </ul>
          <div class="text-center no-data" v-show="noData">
            <img src="../../../assets/images/public/default/default_icon_no_record.png" alt="缺省图片">
            <p>暂无记录</p>
          </div>
        </mt-loadmore>
      </div>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  export default {
    name: 'repaymentPlan',
    methods: {
      projectList( page = 1){
        this.noData = false;
        this.$indicator.open({spinnerType: 'fading-circle'})
        let getParams = {
          investId: this.$route.query.investId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          'page.page': page
        }
        this.$http.get(ajaxUrl.getProjectCollectionList, {params: getParams}).then((res) => {
          this.$indicator.close()
          if(res.data.resData != ''){
            if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
            if(page > res.data.resData.totalPage){
              if(res.data.resData.totalPage > 0){
                this.$toast('无更多数据加载哦~')
              }
              return ; // 请求页大于总页数就返回不执行
            }
            this.list = this.list.concat(res.data.resData.list)
            this.totalPage = res.data.resData.totalPage
          }
        })
      },
      loadBottom(id) {
        setTimeout(() => {
          let page = ++this.page
          this.projectList(page)
          this.$refs.loadmore.onBottomLoaded(id);
          if(page > this.totalPage){
            this.allLoaded = true;// 若数据已全部获取完毕
          }
        }, 500);
      }
    },
    created(){
      if(this.$route.query.tab){  //返回上一页需要用
        sessionStorage.tab = this.$route.query.tab
      }
      this.projectList()
      this.$nextTick(function () {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    data(){
      return {
        list: [],
        noData: false,
        allLoaded: false,
        page: 1,
        totalPage:1
      }
    }
  }
</script>
<style scoped>
  .repayment-title{
    width: 100%;
    height: .5rem;
    padding: 0 .15rem;
  }
  .repayment-title li{
    float: left;
    width: 50%;
    display: inline-block;
    line-height: .5rem;
    color: #666;
  }
  .repayment-right{
    text-align: right;
  }
  .repayment-content{
    height: .7rem;
    padding: 0 .15rem;
    border-bottom: 1px solid #DDD;
    background:#fff;
  }
  .repayment-content:last-child {border:0;}
  .repayment-content li{
    width: 50%;
    float: left;
    display: block;
    height: .3rem;
    line-height: .3rem;
  }
  .repayment-content li i{
    vertical-align: middle;
  }
  .repayment-periods{
    font-size: .16rem;
    color: #333;
  }
  .repayment-money{
    text-align: right;
    font-size: .16rem;
  }
  .repayment-status{
    text-align: right;
    font-size: .13rem;
    color: #999;
  }
  .repayment-periods,.repayment-money{
    margin-top: .05rem;
  }
  .tips-icon{
    width: .15rem;
    height: .15rem;
    vertical-align: middle;
  }
  
</style>
