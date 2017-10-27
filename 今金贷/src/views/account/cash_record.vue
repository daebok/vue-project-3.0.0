<template>
  <div class="page" id="record">
    <mt-header class="bar-nav" title="记录" >
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <ul class="rd-tab-title">
      <li @click="changeTab(1)">
        <span :class="{current: active == 'tab-con1'}">充值记录</span>
      </li>
      <li @click="changeTab(2)">
        <span :class="{current: active == 'tab-con2'}">提现记录</span>
      </li>
    </ul>
    <!--<div class="record-title clearfix">-->
      <!--<span v-show="active == 'tab-con1'" class="child text-left">充值状态/时间</span>-->
      <!--<span v-show="active == 'tab-con2'"  class="child text-left">提现状态/时间</span>-->
      <!--<span class="child text-right">交易金额(元)/流水号</span>-->
    <!--</div>-->
    <div class="page-tab-container">
      <mt-tab-container class="page-tabbar-tab-container" v-model="active">
        <mt-tab-container-item id="tab-con1">
          <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
            <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
                <ul class="record-list">
                  <div v-for="newItem in newList">
                    <div class="record-date">{{newItem.timeF}}</div>
                    <li v-for="item in newItem.item" class="aui-border-b clearfix" @click="openRemark(item.orderNo)">
                      <span class="padding-l-15 text-left color-333 fz-16">{{item.statusStr}}</span>
                      <span class="padding-r-15 text-right main-color fz-16">+{{item.amount | currency('',2)}}</span>
                      <span class="padding-l-15 padding-b-15 text-left color-999 fz-13">{{item.createTime | dateFormatFun(4)}}</span>
                      <span class="text-right padding-b-15 color-999 fz-13"> </span>
                      <div class="remark-box hide" :ref="item.orderNo">
                        <p><label class="color-999">交易流水号</label>{{item.orderNo}}</p>
                        <p><label class="color-999">备注</label>{{item.remark}}</p>
                      </div>
                    </li>
                  </div>
                </ul>
                <div class="text-center no-data" v-show="noData">
                  <img src="../../assets/images/public/default/default_icon_no_record.png" alt="缺省图片">
                  <p>暂无记录</p>
                </div>
            </mt-loadmore>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab-con2">
          <div class="page-loadmore-wrapper" ref="" :style="{ height: wrapperHeight + 'px' }">
            <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded2" ref="loadmore2">
              <ul class="record-list">
                <div v-for="newItem in newList2">
                  <div class="record-date">{{newItem.timeF}}</div>
                  <li v-for="item in newItem.item" class="aui-border-b clearfix" @click="openRemark(item.orderNo)">
                    <span class="padding-l-15 text-left color-333 fz-16">{{item.statusStr}}</span>
                    <span class="padding-r-15 text-right color-green fz-16">-{{item.amount | currency('',2)}}</span>
                    <span class="padding-l-15 padding-b-15 text-left color-999 fz-13">{{item.addTime | dateFormatFun(4)}}</span>
                    <span class="padding-r-15 padding-b-15 text-right color-999 fz-13"></span>
                    <div class="remark-box hide" :ref="item.orderNo">
                      <p><label class="color-999">提现手续费</label>{{item.cashFee ? item.cashFee : 0}}元</p>
                      <p><label class="color-999">交易流水号</label>{{item.orderNo}}</p>
                      <p><label class="color-999">备注</label>{{item.remark}}</p>
                    </div>
                  </li>
                </div>
              </ul>
              <div class="text-center no-data" v-show="noData">
                <img src="../../assets/images/public/default/default_icon_no_record.png" alt="缺省图片">
                <p>暂无记录</p>
              </div>
            </mt-loadmore>
          </div>
        </mt-tab-container-item>
      </mt-tab-container>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config'
  export default {
    methods: {
      changeTab(index=1){
        if(index == 1){
          this.active = 'tab-con1'
          // this.list = []
          this.obj = {}
          this.newList = []
        }else{
          this.active = 'tab-con2'
          // this.list2 = []
          this.obj2 = {}
          this.newList2 = []
        }
        this.projectList()
      },
      openRemark(ref){
        var remark_box = this.$refs[ref][0]
        if(remark_box.classList.contains('hide')){
          remark_box.classList.remove('hide')
        }else{
          remark_box.classList.add('hide')
        }
      },
      projectList(page=1){
        this.noData = false;
        let url
        if(this.active == 'tab-con1'){
          url = ajaxUrl.getRechargeList
        }else if(this.active == 'tab-con2'){
          url = ajaxUrl.getCashList
        }
        let urlParams = {
          'page.page': page,
          'page.pageSize': 10,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
        }
        this.$http.get(url, { params: urlParams }).then((res) => {
          if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
          if(res.data.resData.totalPage <= 1){
            if(this.active == 'tab-con1' ){ this.allLoaded = true;}
            if(this.active == 'tab-con2' ){ this.allLoaded2 = true;}
          }
          if(page > res.data.resData.totalPage){
            if(res.data.resData.totalPage > 0){
              this.$toast('无更多数据加载哦~')
            }
            return ; // 请求页大于总页数就返回不执行
          }
          let rawList = res.data.resData.list
          var index = 0;
          if(this.active == 'tab-con1'){
            rawList.forEach(val => {
              let y = new Date(val.createTime).getFullYear()
              let m = new Date(val.createTime).getMonth() + 1
              let timeF =  y + '年' + m +'月'
              if(this.obj.hasOwnProperty(timeF)){
                this.newList[this.obj[timeF]].item.push(val)
              }else{
                this.obj[timeF] = index++
                this.newList.push({
                  timeF: timeF,
                  item: [val]
                })
              }
            })
            // this.list = this.list.concat(res.data.resData.list)
            this.totalPage1 = res.data.resData.totalPage
          }else if(this.active == 'tab-con2'){
            rawList.forEach(val => {
              let y = new Date(val.addTime).getFullYear()
              let m = new Date(val.addTime).getMonth() + 1
              let timeF =  y + '年' + m +'月'
              if(this.obj2.hasOwnProperty(timeF)){
                this.newList2[this.obj2[timeF]].item.push(val)
              }else{
                this.obj2[timeF] = index++
                this.newList2.push({
                  timeF: timeF,
                  item: [val]
                })
              }
            })
            // this.list2 = this.list2.concat(res.data.resData.list)
            this.totalPage2 = res.data.resData.totalPage
          }
        })
      },
      loadTop(id){
        setTimeout(() => {
          if(this.active == 'tab-con1'){
            this.obj = {}
            this.newList = []
            this.page1 = 1
            this.allLoaded = false;
            this.$refs.loadmore.onTopLoaded(id);
          }else if(this.active == 'tab-con2'){
            this.obj2 = {}
            this.newList2 = []
            this.page2 = 1
            this.allLoaded2 = false;
            this.$refs.loadmore2.onTopLoaded(id);
          }
          this.projectList()
        }, 1000);
      },
      loadBottom(id) {
        setTimeout(() => {
          if(this.active == 'tab-con1'){
            let page = ++this.page1
            this.projectList(page)
            this.$refs.loadmore.onBottomLoaded(id);
            if(page > this.totalPage1){
              this.allLoaded = true;// 若数据已全部获取完毕
            }
          }else if(this.active == 'tab-con2'){
            let page = ++this.page2
            this.projectList(page)
            this.$refs.loadmore2.onBottomLoaded(id);
            if(page > this.totalPage2){
              this.allLoaded2 = true;// 若数据已全部获取完毕
            }
          }
        }, 500);
      }
    },
    created() {
      this.projectList()
      this.$nextTick(()=>{
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    data() {
      return {
        active: this.$route.query.type,
        wrapperHeight: 0,
        list: [],
        list2: [],
        obj: {},
        newList: [],
        obj2: {},
        newList2: [],
        noData: false,
        allLoaded: false,
        allLoaded2: false,
        bottomStatus: '',
        bottomStatus2: '',
        page1: 1,
        page2: 1,
        totalPage1:1, //tab1默认总页数为1
        totalPage2:1, //tab2默认总页数为1
      }
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var.scss";
  .rd-tab-title {
    width: 100%;
    height: .4rem;
    background: #fff;
  }
  .rd-tab-title li{
    width: 50%;
    line-height: .36rem;
    text-align: center;
    display: inline-block;
    float: left;
  }
  .current {
    color: $main-color;
    border-bottom: 2px solid $main-color;
  }
  .rd-tab-title li span {
    width: .6rem;
    display: block;
    margin: 0 auto;
  }
  .record-title{
    padding: 0 .15rem;
  }
  .record-title .child{
    display:inline-block;
    width: 49%;
    height: .35rem;
    line-height: .35rem;
    color: #666;
  }
  .text-left{  text-align: left;  }
  .text-right{  text-align: right;  }
  .record-list li{
    background: #fff;
  }
  .record-list li span{
    width: 49%;
    display: inline-block;
    line-height: 1;
    padding-top: .14rem;
  }
  .record-date{
    line-height: .3rem;
    color: #999;
    padding: 0 .15rem;
    background: $page-bg;
  }
  .remark-box {
    padding: 0 .15rem .1rem;
    background: $page-bg;
  }
  .remark-box p {
    padding-top:.1rem;
  }
  .remark-box label { width: 23%;display: inline-block;}

</style>
