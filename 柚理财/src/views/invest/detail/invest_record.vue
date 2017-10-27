<template>
  <div class="page" id="record">
    <mt-header class="bar-nav" title="投资记录" >
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div v-if="!$route.query.bondName" class="record-title clearfix">
      <p class="text-left title-item">投资人/时间</p>
      <p class="text-right title-item">金额(元)/使用奖励</p>
    </div>
    <div v-else class="record-title clearfix">
      <p class="text-left title-item">受让人/时间</p>
      <p class="text-right title-item">受让金额(元)</p>
    </div>
    <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
       <mt-loadmore :bottom-method="loadBottom" :top-method="list.length > 6 ? loadTop : null" :bottom-all-loaded="allLoaded" ref="loadmore" bottomPullText="上拉加载更多">
        <ul class="record-list" v-show="!noData">
          <li v-for="item in list">
            <span class="text-left color-333 font-size-16">{{ item.userName }}</span>
            <span class="text-right main-color font-size-16">{{ item.money | currency('', 2) }}</span>
            <span class="text-left color-999 font-size-13">{{item.createTime | dateFormatFun }}</span>
            <span v-if="!$route.query.bondName" class="text-right color-666 font-size-13">
              <em v-if="item.redenvelopeAmount > 0">红包 {{ item.redenvelopeAmount }}元</em>
              <em v-if="item.rateCouponVal > 0">加息券 {{ item.rateCouponVal }}%</em>
            </span>
          </li>
        </ul>
        <no-data v-show="noData"></no-data>
      </mt-loadmore>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import NoData from '../../../components/NoData.vue'
  export default {
    name: 'investDetail',
    data(){
      return {
        list: [],
        wrapperHeight: 0,
        noData: false,
        allLoaded: false,
        page: 1,
        totalPage: 1
      }
    },
    created(){
      this.projectList()
      this.$nextTick(function () {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    methods: {
      projectList( page = 1){
        this.noData = false;
        this.$indicator.open({spinnerType: 'fading-circle'})
        let getParams = {
          projectId: this.$route.params.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          'page.pageSize': 10,
          'page.page': page
        }
        let ajax = ajaxUrl.recordList
        if(this.$route.query.bondName){ // 债权记录
          ajax = ajaxUrl.bondRecordList
        }
        this.$http.get(ajax, {params: getParams}).then((res) => {
          this.$indicator.close()
          if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
          if(res.data.resData.totalPage <= 1){ this.allLoaded = true;} // 只有一页数据就不显示上拉加载
          if(!this.$route.query.bondName){ // 债权记录不考虑分页
            if(page > res.data.resData.totalPage){
              if(res.data.resData.totalPage > 0){
                this.$toast('无更多数据加载哦~')
              }
              return ; // 请求页大于总页数就返回不执行
            }
          }
          this.list = this.list.concat(res.data.resData.list)
          this.totalPage = res.data.resData.totalPage
        })
      },
      loadTop(id){
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          this.list = []  //刷新时清空
          this.allLoaded = false;
          this.page = 1;
          this.projectList()
        },1000)
      },
      loadBottom(id) {
        setTimeout(() => {
          this.page++
          this.projectList(this.page)
          this.$refs.loadmore.onBottomLoaded(id);
          if(this.page >= this.totalPage){
            this.allLoaded = true;// 若数据已全部获取完毕
          }
        }, 500);
      }
    },
    components: {NoData}
  }
</script>
<style scoped>
  .record-title{ padding: 0 .15rem;  }
  .record-title .title-item{
    width: 49%;
    line-height: .45rem;
    color: #666;
    display: inline-block;
  }
  .text-left{  text-align: left;  }
  .text-right{  text-align: right;  }
  .record-list{
    background: #fff;
    min-height: 600px;
  }
  .record-list li{
    height: .7rem;
    padding: 0 .15rem;
    border-bottom: 1px solid #DDD;
  }
  .record-list li span{
    width: 50%;
    display: inline-block;
    float: left;
    line-height: 1;
    padding-top: .14rem;
  }
  .font-size-16{ font-size: .16rem; }
  .font-size-13{ font-size: .13rem; }

</style>
