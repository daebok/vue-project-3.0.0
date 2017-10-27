<!-- 邀请记录 -->
<template>
  <div class="page" id="record">
    <mt-header class="bar-nav" title="记录" >
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <ul class="rd-tab-title">
      <li @click="changeTab(1)">
        <span :class="{current: active === 'tab-con1'}">一级人脉</span>
      </li>
      <li @click="changeTab(2)">
        <span :class="{current: active === 'tab-con2'}">二级人脉</span>
      </li>
    </ul>
    <div class="page-tab-container">
      <mt-tab-container class="page-tabbar-tab-container" v-model="active" swipeable>
        <mt-tab-container-item id="tab-con1">
          <div class="record-title clearfix">
            <span class="text-left">被邀请人/邀请时间</span>
            <span class="text-right pull-right">获得奖励</span>
          </div>
          <div class="page-loadmore-wrapper" ref="wrapper" :style="{height: wrapperHeight + 'px'}">
            <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
              <ul v-for="item in list" class="record-list">
                <li>
                  <span class="text-left color-333 fz-16">{{ item.inviteeUserMobile }}</span>
                  <span class="text-right color-333 fz-16">红包 <i class="main-color">{{ item.awardRedTotal }}元</i></span>
                  <span class="text-left color-999 fz-13">{{ item.inviteTime | dateFormatFun(4) }}</span>
                </li>
              </ul>
              <div class="text-center no-data" v-show="noData">
                <img src="../../assets/images/public/default/default_icon_no_record.png" alt="缺省图片">
                <p>暂无记录</p>
              </div>
            </mt-loadmore>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab-con2">
          <div class="record-title clearfix">
            <span class="text-left">被邀请人/邀请时间</span>
            <span class="text-right pull-right">获得奖励</span>
          </div>
          <div class="page-loadmore-wrapper" ref="" :style="{height: wrapperHeight + 'px'}">
            <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded2" ref="loadmore2">
              <ul v-for="item in list2" class="record-list">
                <li>
                  <span class="text-left color-333 fz-16">{{ item.inviteeUserMobile }}</span>
                  <span class="text-right color-333 fz-16">红包 <i class="main-color">{{ item.awardRedTotal }}元</i></span>
                  <span class="text-left color-999 fz-13">{{ item.inviteTime | dateFormatFun(4) }}</span>
                </li>
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

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js'

  export default {
    data() {
      return {
        active: 'tab-con1', // 导航切换，tab-con1一级好友，tab-con2二级好友
        wrapperHeight: 0, // 列表容器可视高度
        list: [], // 一级好友数据列表数组
        list2: [], // 二级好友数据列表数组
        noData: false, // 有无数据，true无数据，false有数据
        allLoaded: false, // 有无下一页数据，true无数据，false有数据
        allLoaded2: false, // 有无下一页数据，true无数据，false有数据
        page1: 1, // 页数
        page2: 1, // 页数
        totalPage1:1, // 总页数
        totalPage2:1, // 总页数
      };
    },
    created() {
      this.projectList();
      this.$nextTick(() => {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    methods: {
      // 导航切换
      changeTab(index = 1) {
        if (index === 1) {
          this.active = 'tab-con1';
          this.list = [];
        } else {
          this.active = 'tab-con2';
          this.list2 = [];
        }
        this.projectList()
      },
      // 数据加载
      projectList(page = 1) {
        this.noData = false;
        let url;
        if (this.active === 'tab-con1') {
          url = ajaxUrl.inviteLogList + '?inviteLevel=1';
        } else if (this.active == 'tab-con2') {
          url = ajaxUrl.inviteLogList + '?inviteLevel=2';
        }
        let urlParams = {
          'page.page': page,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$http.get(url, { params: urlParams }).then((res) => {
          if (res.data.resData.list.length === 0) {
            this.noData = true;
          }
          if (page > res.data.resData.totalPage) {
            if (res.data.resData.totalPage > 0) {
              this.$toast('无更多数据加载哦~');
            }
            return; // 请求页大于总页数就返回不执行
          }
          if (this.active === 'tab-con1') {
            this.list = this.list.concat(res.data.resData.list);
            this.totalPage1 = res.data.resData.pageSize;
          } else if (this.active === 'tab-con2') {
            this.list2 = this.list2.concat(res.data.resData.list);
            this.totalPage2 = res.data.resData.pageSize;
          }
        })
      },
      // // 下拉拖动加载更多数据
      loadBottom(id) {
        setTimeout(() => {
          if (this.active === 'tab-con1') {
            let page = ++this.page1;
            if (page > this.totalPage1) {
              this.allLoaded = true; // 若数据已全部获取完毕
            }
            this.projectList(page);
            this.$refs.loadmore.onBottomLoaded(id);
          } else if (this.active === 'tab-con2') {
            let page = ++this.page2;
            if (page > this.totalPage2) {
              this.allLoaded2 = true; // 若数据已全部获取完毕
            }
            this.projectList(page);
            this.$refs.loadmore2.onBottomLoaded(id);
          }
        }, 1000);
      }
    }
  }
</script>

<style type="text/css" scoped>
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
  .record-title{  padding: 0 .15rem;  }
  .record-title span{
    line-height: .45rem;
    color: #666;
    display: inline-block;
  }
  .text-left{
    text-align: left;
  }
  .text-right{
    text-align: right;
  }
  .record-list{
    float: left;
    width: 100%;
    background: #fff;
  }
  .record-list li{
    float: left;
    width: 100%;
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
</style>
