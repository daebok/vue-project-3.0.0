<template>
  <div class="page page-invest">
    <div class="content">
      <mt-header class="bar-nav" title="理财" ></mt-header>
      <!--<div class="hor-scroll">-->
      <div class="wrapper-scroll">
        <div id="wrapper">
          <div id="scroller" class="aui-border-b">
            <!--<div class="nav aui-flex-col text-center aui-border-b">-->
            <div v-for="(item, index) in projectItemTypeList" @click="tabChange(item, index+1)" :ref="index+1" :class="{current: active == 'tab-container'+(index+1)}" class="menu-item ellipsis aui-flex-item-3">
              <p>{{item.typeName}}</p>
              <!--<router-link :to="{query: {order: index+1, uuid: item.uuid, type: item.typeName}}">-->
              <!--<p>{{item.typeName}}</p>-->
              <!--</router-link>-->
            </div>
            <!--<div @click="active = 'tab-container3'" :class="{current: active == 'tab-container3'}" class="aui-flex-item-3"><p>变现通</p></div>-->
          </div>
        </div>
      </div>
      <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
        <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
          <ul class="page-infinite-list">
            <li class="bid-con aui-border-b" :class="{'gray-status': item.remainAccount == 0 || item.remainMoney == 0}" v-for="(item,index) in bidList" @click="toUrl(item.projectId, item.bondName)">
              <h3 v-if="item.projectName" :id="item.projectId">{{ item.projectName | hideBorrowName(24) }}
                <span v-if="item.novice == 1" class="bid-icon new aui-border">新客</span>
                <span v-if="item.bondUseful == 1" class="bid-icon kzr aui-border">可转让</span>
                <span v-if="item.realizeUseful == 1" class="bid-icon kbx aui-border">可变现</span>
                <span v-if="item.specificSale == 1 || item.specificSale == 3" class="bid-icon dx aui-border">定向</span>
                <span v-if="item.specificSale == 2" class="bid-icon vip aui-border">VIP{{item.specificSaleRule}}</span>
              </h3>
              <!--债权转让-->
              <h3 v-if="item.bondName" :id="item.projectId">{{ item.bondName | hideBorrowName(24) }}</h3>
              <div class="bid-info clearfix">
                <div class="apr-part pull-left">
                  <h1 class="apr main-color">{{ item.apr | currency('', 2) }}<b>%<span v-if="item.addApr > 0">+{{ item.addApr | currency('', 2) }}%</span></b></h1>
                  <p>预期年化收益率</p>
                </div>
                <div v-if="item.projectName" class="time-part pull-left">
                  <p>起投  <b>{{ item.lowestAccount | currency('', 0) }}元</b></p>
                  <p v-if="item.timeType == 0">期限  <b>{{ item.timeLimit }}个月</b></p>
                  <p v-if="item.timeType == 1">期限  <b>{{ item.timeLimit }}天</b></p>
                </div>
                <!--债权转让-->
                <div v-if="item.bondName" class="time-part pull-left">
                  <p v-if="item.bondApr >= 0">溢价  <b class="bondApr">+{{ item.bondApr | currency('', 1) }}%</b></p>
                  <p v-else>折价  <b class="bondApr">{{ item.bondApr | currency('', 1) }}%</b></p>
                  <p>期限  <b>{{ item.remainDays }}天</b></p>
                </div>
              </div>
              <div v-if="item.projectName" class="circle-area">
                <div v-if="item.remainAccount > 0">
                  <circle-progress v-if="item.saleTime > item.timeNow" :percent="0" :diameter="60" :text-size="11" text-color="#f60" foreground-color="#F95A28" backgroundColor="#f8f8f8" :text-format="item.saleTime | saleTime(item.timeNow)" :showFlag="false" :showTime="true" :line-width="2"></circle-progress>
                  <circle-progress v-else :percent="item.remainAccount | scalesFun(item.account)" :diameter="60" :text-size="14" text-color="#f60" foreground-color="#F95A28" backgroundColor="#f8f8f8" text-format="{percent}" :line-width="2"></circle-progress>
                </div>
                <div v-else>
                  <circle-progress :percent="100" :diameter="60" :text-size="14" foreground-color="#999" backgroundColor="#f8f8f8"  text-format="已抢光" :showFlag="false" :line-width="2"></circle-progress>
                </div>
              </div>
              <!--债权转让-->
              <div v-if="item.bondName" class="circle-area">
                <circle-progress v-if="item.remainMoney > 0" :percent="item.remainMoney | scalesFun(item.bondMoney)" :diameter="60" :text-size="14" text-color="#f60" foreground-color="#F95A28" backgroundColor="#f8f8f8" text-format="{percent}" :line-width="2"></circle-progress>
                <circle-progress v-else :percent="100" :diameter="60" :text-size="14" foreground-color="#999" backgroundColor="#f8f8f8"  text-format="已抢光" :showFlag="false" :line-width="2"></circle-progress>
              </div>
            </li>
            <div class="text-center no-data" v-show="noData">
              <img class="invest-default-nodata" src="../../assets/images/public/default/nodata_trilateral.png" alt="缺省图片">
              <p>产品准备中，先去看看其他的吧~</p>
            </div>
          </ul>
        </mt-loadmore>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import CircleProgress from '../../components/ProgressCircle'
  import * as ajaxUrl from '../../ajax.config'
  import iScroll from 'iscroll/build/iscroll'
  import routeData from 'route-data'
  import is from 'is'
  const { history, location } = window
  const getPageKey = () => {
    return is.object(history.state) ? history.state.key : location.href
  }
  export default {
    name: 'page-invest',
    data () {
      return {
        active: 'tab-container1',
        bidList: [],
        projectItemTypeList: [],
        scales: 74,
        allLoaded: false,
        noData: false,
        wrapperHeight: 0,
        page: 1,
        totalPage:1,
        menuScroll: '',
        scrollWidth: 878, // 先默认宽度
        leftVal: 0 // 默认从零开始滑
      }
    },
    created(){
      let indexN = this.$route.query.order || 1;
      this.active = 'tab-container' + indexN;
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      this.$http.get(ajaxUrl.projectTypeList).then((res) => {  //获取产品列表类型
        this.$indicator.close()
        this.projectItemTypeList = res.data.resData.projectItemTypeList;
        if (this.$route.query.uuid) {
          if (this.$route.query.type == '变现') {
            this.projectList(this.$route.query.uuid, 1, 'realize')
          } else if (this.$route.query.type == '转让专区') {
            this.projectList(this.$route.query.uuid, 1, 'bond')
          }
          else {
            this.projectList(this.$route.query.uuid)
          }
        } else {
          // 默认加载初始数据
          this.projectList(this.projectItemTypeList[0].uuid)
          sessionStorage.init_invest_list_uuid = this.projectItemTypeList[0].uuid
        }
      })
      this.$nextTick(function () {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top - 50;
      })
    },
    mounted(){
      let startX = this.$route.query.left || 0
      startX = startX < 0 ? startX : 0
      document.querySelector('#scroller').style.width = this.scrollWidth/100 + 'rem'
      this.menuScroll = new iScroll('#wrapper',{
        scrollY: false,
        scrollX: true,
        startX: startX,
        mouseWheel: false
      })
      let flag = true
      this.menuScroll.on('beforeScrollStart', () => {
        if(flag){ // 执行一次计算宽度
          flag = false
          let swipe_item = document.querySelectorAll('.menu-item')
          let total_w = 10
          swipe_item.forEach( val => {
            total_w += val.clientWidth
          })
          this.scrollWidth = total_w
          document.querySelector('#scroller').style.width = total_w + 'px'
          setTimeout(() => {
            this.menuScroll.refresh()
          }, 10)
        }
      })
      document.getElementById('wrapper').addEventListener('touchmove', function (e) {
        e.preventDefault()
      },false)
    },
    methods: {
      tabChange(arg, index){
        this.active = 'tab-container' + index;
        this.allLoaded = false; //切换时候重新启用底部加载
        this.bidList = [];
        if(arg.typeName == '变现'){
          this.projectList(arg.uuid, 1, 'realize')
        }else if(arg.typeName == '转让专区'){
          this.projectList(arg.uuid, 1, 'bond')
        }else{
          this.projectList(arg.uuid)
        }
        let off_left = this.$refs[index][0].offsetLeft
        let self_width = this.$refs[index][0].clientWidth
        let doc_w = document.documentElement.clientWidth
        this.leftVal = doc_w - off_left - self_width
        this.$router.push({query: {
          order: index,
          type: arg.typeName,
          uuid: arg.uuid,
          left: this.leftVal
        }})
      },
      toUrl(projectId, bondName){
        // 债权转让
        if(this.$route.query.uuid == 'a31bd335e12ac0dced8849a16fd4a894'){
          this.$router.push({name:'bondDetail', params: {projectId: projectId}, query: {bondName:bondName}})
        }
        // 变现转让
        else if(this.$route.query.uuid == '090d5d939784fe33aceff143ba1c198c'){
          this.$router.push({name:'realizeDetail', params: {projectId: projectId}})
        }
        // 普通投资
        else{
          this.$router.push({name:'investDetail', params: {projectId: projectId}})
        }
      },
      projectList(uuid, page = 1, type){
        this.noData = false;
        let params = {projectTypeId: uuid, 'page.page': page}
        let urlAjax = ajaxUrl.productListAjax
        if(type=='realize'){
          urlAjax = ajaxUrl.realizeListData
        }else if(type=='bond'){
          // params = {'page.page': page}
          urlAjax = ajaxUrl.bondListData
        }
        this.$http.get(urlAjax, {params: params}).then((res) => {
          if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
          if(res.data.resData.totalPage <= 1){ this.allLoaded = true;} // 只有一页数据就不显示上拉加载
          if(page > res.data.resData.totalPage){
            if(res.data.resData.totalPage > 0){
              this.$toast('无更多数据加载哦~')
            }
            return ; // 请求页大于总页数就返回不执行
          }
          this.bidList = this.bidList.concat(res.data.resData.list)
          this.totalPage = res.data.resData.totalPage
        })
      },
      loadTop(id){
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          this.bidList = [];
          this.page = 1;
          this.allLoaded = false;
          if(this.$route.query.uuid){
            if(this.$route.query.type == '变现'){
              this.projectList(this.$route.query.uuid, 1, 'realize')
            }else if(this.$route.query.type == '转让专区'){
              this.projectList(this.$route.query.uuid, 1, 'bond')
            }
            else{
              this.projectList(this.$route.query.uuid)
            }
          }else{
            // 默认加载初始数据
            this.projectList(this.projectItemTypeList[0].uuid)
            sessionStorage.init_invest_list_uuid = this.projectItemTypeList[0].uuid
          }
        }, 1000);
      },
      loadBottom(id) {
        setTimeout(() => {
          this.page++
          let uuid = this.$route.query.uuid || sessionStorage.init_invest_list_uuid
          this.projectList(uuid, this.page)
          this.$refs.loadmore.onBottomLoaded(id);
          if(this.page > this.totalPage){
            this.allLoaded = true;// 若数据已全部获取完毕
          }
        }, 500);
      }
    },
    components: {
      CircleProgress
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var.scss";
  .gray-status * {color: #999!important;}/*投满时变灰*/
  .hor-scroll {  overflow-x: auto;  }
  .wrapper-scroll {
    height: .45rem;
    position:relative;
  }
  #wrapper {
    position: absolute;
    z-index: 1;
    top: 0;
    left: 0;
    width: 100%;
    background: #ccc;
    overflow: hidden;
    height: .36rem;
  }
  #scroller {
    position: absolute;
    z-index: 1;
    /*-webkit-tap-highlight-color: rgba(0,0,0,0);*/
    min-width: 400px;
    height: 100%;
    background-color: #a00;
    /*-webkit-touch-callout: none;*/
    /*-webkit-user-select: none;*/
    /*user-select: none;*/
    /*-webkit-text-size-adjust: none;*/
    text-size-adjust: none;
    background:#fff;
  }
  .aui-flex-col {
    height: .36rem;
    overflow-y: hidden;
    background:#fff;
    margin-bottom: .1rem;
    width: 240%;
    display: block;
  }
  .aui-flex-item-3 {  padding-top:.02rem;  }
  .aui-flex-item-3 p {
    color: #333;
    line-height:.2rem;
    padding: .05rem .05rem .07rem;
    margin: 0 .1rem;
  }
  .aui-flex-item-3.current p {color: $main-color;border-bottom: 1px solid $main-color; }
  .data-info {margin-top: .1rem;height: .9rem; padding:.15rem;background:#fff;position: relative;}
  .data-info h4 {font-size: .14rem; color:#666; line-height: 1;}
  .data-info h2 {font-size: .28rem;margin-top: .1rem;}
  .data-info .btn-log {width: 1.03rem;color:#fff;text-align:center;font-size: .16rem; line-height: .33rem; position: absolute;right:.15rem;top:50%;border-radius:.33rem; margin-top:-.18rem;}
  .newbid {background:#fff;margin-top: .1rem;}
  .newbid h4 {line-height:.4rem;padding-left:.15rem; color: #333;}
  .newbid h4 span {color:#999;margin-left: .03rem; padding-left: .08rem;font-size: .12rem;}
  .bid-con {height: 1.24rem;padding:.15rem; background:#fff;position: relative;}
  .bid-con:last-child:after {border-bottom: 0;}
  .bid-con h3 {font-size: .15rem; color:#333;}
  .bid-con h3 img {width:.35rem;vertical-align: -.02rem;}
  .bid-info { margin-top: .15rem; }
  .apr-part .apr {font-size: .31rem; font-family: Arial; margin:-.05rem 0 .03rem 0; line-height:1;}
  .apr-part p {color:#666;}
  .apr-part .apr b {font-size: .16rem;}
  .circle-area {position: absolute; right: .15rem; top: .3rem;}
  .time-part {font-size: .14rem; color: #666;}
  .time-part p:first-child {margin-bottom: .1rem;}
  .time-part p b.bondApr{color: #48affb}
  .apr-part {  width: 1.8rem;  }
  /*icon分类*/
  .bid-icon {font-size: .11rem;padding: 0 .05rem; display: inline-block;text-align:center; line-height: .16rem;}
  .bid-icon.aui-border:after { border-radius:.18rem;}
  /*新客*/
  .bid-icon.new {color:#F95A28!important;}
  .new.aui-border:after { border-color:#F95A28;}
  /*定向*/
  .bid-icon.dx {color:#97d1fd!important;}
  .dx.aui-border:after { border-color:#97d1fd;}
  /*可转让*/
  .bid-icon.kzr {color:#89d096!important;}
  .kzr.aui-border:after { border-color:#89d096;}
  /*vip*/
  .bid-icon.vip {color:#f9b728!important;}
  .vip.aui-border:after { border-color:#f9b728;}
  /*可变现*/
  .bid-icon.kbx {color: #ad60f9 !important;}
  .kbx.aui-border:after { border-color:#AD60F9;}

</style>
