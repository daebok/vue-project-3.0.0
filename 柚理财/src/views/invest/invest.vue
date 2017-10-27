<template>
  <div class="page page-invest">
    <div class="content">
      <mt-header class="bar-nav aui-border-b" title="理财" ></mt-header>
      <i-scroll class="margin-b-10" ref="scroll" h="0.36" :data="projectItemTypeList">
        <div v-for="(item, index) in projectItemTypeList" @click="tabChange(item, index+1)" :ref="index+1" :class="{current: active == 'tab-con'+(index+1)}" class="menu-item ellipsis aui-flex-item-3" :id="`menuItem${index+1}`">
          <p>{{item.typeName}}</p>
        </div>
      </i-scroll>
      <loadmore @loadTop="_loadTop" @loadBottom="_loadBottom" :allLoaded="allLoaded" ref="loadmore">
        <bid-list :list="bidList"></bid-list>
        <div class="text-center no-data" v-show="noData">
          <img class="invest-default-nodata" src="../../assets/images/public/default/nodata_trilateral.png" alt="缺省图片">
          <p>产品准备中，先去看看其他的吧~</p>
        </div>
      </loadmore>
    </div>
  </div>
</template>

<script>
  import IScroll from '../../components/scroll/IScroll.vue'
  import Loadmore from '../../components/Loadmore.vue'
  import BidList from '../../components/BidList.vue'
  import * as ajaxUrl from '../../ajax.config'

  export default {
    name: 'page-invest',
    data () {
      return {
        active: 'tab-con1',
        bidList: [],
        projectItemTypeList: [],
        scales: 74,
        allLoaded: false,
        noData: false,
        page: 1,
        totalPage: 1
      }
    },
    created(){
      this.itemStore = 1
      let indexN = this.$route.query.order || 1;
      this.active = 'tab-con' + indexN;
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
    },
    watch: {
      projectItemTypeList() {
        let index = this.$route.query.order || 1;
        this.scrollToEle(index) // 初始化跳转该位置
      }
    },
    methods: {
      scrollToEle(index) {
        setTimeout(() => {
          let el = document.getElementById(`menuItem${index}`)
          this.$refs.scroll.scrollToElement(el)
          this.$refs.scroll.refresh()
        }, 10)
      },
      tabChange(arg, index){
        this.active = 'tab-con' + index;
        //console.log(index+'索引');
        //console.log(this.itemStore+'存');
        //if(index == this.itemStore) return //先判断点击的item是否重复点击，重复点击事不刷新不执行下面代码
        this.itemStore = index
        if(index > 5 ) {
          this.scrollToEle(index) // 点击大于第5个的时候跳转
        }
        this.allLoaded = false; //切换时候重新启用底部加载
        this.bidList = [];
        this.page = 1;
        if(arg.typeName == '变现'){
          this.projectList(arg.uuid, 1, 'realize')
        }else if(arg.typeName == '转让专区'){
          this.projectList(arg.uuid, 1, 'bond')
        }else{
          this.projectList(arg.uuid)
        }
        this.$router.push({query: {
          order: index,
          type: arg.typeName,
          uuid: arg.uuid
        }})
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
          if(res.data.resData.list.length == 0 && this.bidList.length == 0){this.noData = true;}// 数据为空判断
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
      _loadTop(id){
        setTimeout(() => {
          this.$refs.loadmore.onTop(id);
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
      _loadBottom(id) {
        setTimeout(() => {
          this.page++
          let uuid = this.$route.query.uuid || sessionStorage.init_invest_list_uuid
          //this.projectList(uuid, this.page)
          if(this.$route.query.type == '变现'){
            this.projectList(uuid, this.page, 'realize')
          }else if(this.$route.query.type == '转让专区'){
            this.projectList(uuid, this.page, 'bond')
          }
          else{
            this.projectList(uuid, this.page)
          }
          this.$refs.loadmore.onBottom(id);
          if(this.page > this.totalPage){
            this.allLoaded = true;// 若数据已全部获取完毕
          }
        }, 500);
      }
    },
    components: {
      IScroll,
      Loadmore,
      BidList
    }
  }
</script>
<style lang="sass" rel="stylesheet/sass" scoped>
  @import "../../assets/scss/var.scss";
  .gray-status *
    color: #999!important /*投满时变灰*/

  .aui-flex-item-3
    padding: 0 .2rem
    /*flex: 1*/
    text-align: center
    &.current
      p
        color: $main-color
        border-bottom: 1px solid $main-color
    p
      display: inline-flex
      color: #333
      line-height: .2rem
      margin: 0 0
      padding: .05rem 0 .07rem

  .data-info
    position: relative
    height: .9rem
    margin-top: .1rem
    padding: .15rem
    background: #fff
    h4
      font-size: .14rem
      color: #666
      line-height: 1
    h2
      font-size: .28rem
      margin-top: .1rem
    .btn-log
      position: absolute
      right: .15rem
      top: 50%
      font-size: .16rem
      color: #fff
      text-align: center
      width: 1.03rem
      line-height: .33rem
      margin-top: -.18rem
      border-radius: .33rem
  .newbid
    margin-top: .1rem
    background: #fff
    h4
      color: #333
      line-height: .4rem
      padding-left: .15rem
      span
        font-size: .12rem
        color: #999
        margin-left: .03rem
        padding-left: .08rem
  .bid-con
    position: relative
    height: 1.24rem
    padding: .15rem
    background: #fff
    &:last-child:after
      border-bottom: 0
    h3
      font-size: .15rem
      color: #333
      img
        width: .35rem
        vertical-align: -.02rem

  .bid-info
    margin-top: .15rem
  .apr-part
    .apr
      font-size: .31rem
      font-family: Arial
      margin: -.05rem 0 .03rem 0
      line-height: 1
      b
        font-size: .16rem
    p
      color: #666

  .circle-area
    position: absolute
    right: .15rem
    top: .3rem
  .time-part
    font-size: .14rem
    color: #666
    p
      &:first-child
        margin-bottom: .1rem
      b.bondApr
        color: #48affb
  .apr-part
    width: 1.8rem
  /*icon分类*/
  .bid-icon
    font-size: .11rem
    line-height: .16rem
    padding: 0 .05rem
    display: inline-block
    text-align: center
    &.aui-border:after
      border-radius: .18rem
    /*新客*/
    &.new
      color: #F95A28!important
      &.aui-border:after
        border-color: #F95A28
    /*定向*/
    &.dx
      color: #97d1fd!important
      &.aui-border:after
        border-color: #97d1fd
    /*可转让*/
    &.kzr
      color: #89d096!important
      &.aui-border:after
        border-color: #89d096
    /*vip*/
    &.vip
      color: #f9b728!important
      &.aui-border:after
        border-color: #f9b728
    /*可变现*/
    &.kbx
      color: #ad60f9!important
      &.aui-border:after
        border-color: #ad60f9

</style>
