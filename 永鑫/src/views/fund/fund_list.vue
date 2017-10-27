<template>
  <div class="page">
    <ul class="rd-tab-title">
      <li @click="changeTab(1)">
        <span :class="{current: active == 'tab-con1'}">转入</span>
      </li>
      <li @click="changeTab(2)">
        <span :class="{current: active == 'tab-con2'}">转出</span>
      </li>
      <li @click="changeTab(3)">
        <span :class="{current: active == 'tab-con3'}">收益</span>
      </li>
    </ul>
    <div class="text-center no-data" v-show="noData">
      <img src="../../assets/images/public/default/default_icon_no_record.png" alt="缺省图片">
      <p>暂无记录</p>
    </div>
    <div class="content page-tab-container">
      <mt-tab-container class="page-tabbar-tab-container" v-model="active">
        <mt-tab-container-item id="tab-con1">
          <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded1" ref="loadmore1">
            <ul class="record-list">
              <li v-for="item in list1" class="aui-border-b clearfix">
                <span class="padding-l-15 text-left color-333 fz-16">转入</span>
                <span class="padding-r-15 text-right main-color fz-16">+{{item.money | currency('',2)}}</span>
                <span class="padding-l-15 padding-b-15 text-left color-999 fz-13">{{item.createTime | dateFormatFun(4)}}</span>
                <span class="text-right padding-b-15 color-999 fz-13 padding-r-15">{{ item.statusStr }}</span>
              </li>
            </ul>
          </mt-loadmore>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab-con2">
          <div class="page-loadmore-wrapper" ref="" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded2" ref="loadmore2">
            <ul class="record-list">
              <li v-for="item in list2" class="aui-border-b clearfix">
                <span class="padding-l-15 text-left color-333 fz-16">转出</span>
                <span class="padding-r-15 text-right color-green fz-16">-{{item.money | currency('',2)}}</span>
                <span class="padding-l-15 padding-b-15 text-left color-999 fz-13">{{item.createTime | dateFormatFun(4)}}</span>
                <span class="text-right padding-b-15 color-999 fz-13 padding-r-15">{{ item.statusStr }}</span>
              </li>
            </ul>
          </mt-loadmore>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab-con3">
          <div class="page-loadmore-wrapper" ref="" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded3" ref="loadmore3">
            <ul class="record-list">
              <li v-for="item in list3" class="aui-border-b clearfix">
                <span class="padding-l-15 text-left color-333 fz-16">收益</span>
                <span class="padding-r-15 text-right main-color fz-16">+{{item.profit | currency('',2)}}</span>
                <span class="padding-l-15 padding-b-15 text-left color-999 fz-13">{{item.createTime | dateFormatFun(4)}}</span>
                <span class="text-right padding-r-15 padding-b-15 color-999 fz-13">成功</span>
              </li>
            </ul>
          </mt-loadmore>
          </div>
        </mt-tab-container-item>
      </mt-tab-container>
    </div>
  </div>
</template>
<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';

  export default {
    data() {
      return {
        active: 'tab-con1',
        wrapperHeight: 0,
        noData: false,
        list1: [],
        list2: [],
        list3: [],
        allLoaded1: false,
        allLoaded2: false,
        allLoaded3: false,
        page1: 1,
        page2: 1,
        page3: 1,
        totalPage1: 1, //tab1默认总页数为1
        totalPage2: 1, //tab2默认总页数为1
        totalPage3: 1 //tab3默认总页数为1
      };
    },
    created() {
      this.$indicator.open({ spinnerType: 'fading-circle' });
      this.projectList()
      this.$nextTick(()=>{
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    methods: {
      changeTab(index=1){
        this.active = 'tab-con' + index
        this[`list${index}`] = []
        this.projectList()
      },
      projectList(page=1){
        this.noData = false;
        let url
        if(this.active == 'tab-con1'){
          url = ajaxUrl.getFundLogList + '?type=0'
        }else if(this.active == 'tab-con2'){
          url = ajaxUrl.getFundLogList + '?type=1'
        }else{
          url = ajaxUrl.getProfitList
        }
        let urlParams = {
          'page.page': page,
          'page.pageSize': 10,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
        }
        this.$http.get(url, { params: urlParams }).then((res) => {
          if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
          let order = this.active.slice(-1)
          if(res.data.resData.totalPage <= 1){
            this['allLoaded' + order] = true;
          }
          if(page > res.data.resData.totalPage){
            if(res.data.resData.totalPage > 0){
              this.$toast('无更多数据加载哦~')
            }
            return false; // 请求页大于总页数就返回不执行
          }
          this['list' + order] = this['list' + order].concat(res.data.resData.list)
          this['totalPage' + order] = res.data.resData.totalPage

        })
      },
      loadTop(id){
        setTimeout(() => {
          let order = this.active.slice(-1);
          this['list' + order] = [];
          this['page' + order] = 1;
          this['allLoaded' + order] = false;
          this.$refs['loadmore' + order].onBottomLoaded(id);
          this.projectList()
        }, 1000);
      },
      loadBottom(id) {
        setTimeout(() => {
          let order = this.active.slice(-1);
          this['page' + order]++;
          this.projectList(this['page' + order]);
          this.$refs['loadmore' + order].onBottomLoaded(id);
          if(this['page' + order] > this['totalPage' + order]){
            this['allLoaded' + order] = true; // 若数据已全部获取完毕
          }
        }, 500);
      }
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../assets/scss/var';
  @import '../../assets/scss/border_1px';
  .rd-tab-title {
    height: .25rem;
    background: #fff;
    margin: .1rem .15rem;
    @include hairline('all', $main-color, 5px);
    li {
      width: 33.33%;
      line-height: .25rem;
      text-align: center;
      float: left;
      @include hairline('right', $main-color);
      color: $main-color;
      &:last-child {
        @include hairline('none');
      }
      span {
        display: block;
        &.current {
          background: $main-color;
          color: #fff;
        }
      }
    }
  }
  .content {
    margin: .5rem 0 0;
  }
  .record-list li{
    background: #fff;
    span {
      width: 49%;
      display: inline-block;
      line-height: 1;
      padding-top: .14rem;
      &.text-right { text-align: right; }
    }
  }
</style>
