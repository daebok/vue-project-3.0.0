<!-- 邀请记录 by fenglei -->
<template>
  <div class="page">
    <ul class="rd-tab-title">
      <li @click="changeTab(0)">
        <span :class="{ current: active == 0 }">一级人脉</span>
      </li>
      <li @click="changeTab(1)">
        <span :class="{ current: active == 1 }">二级人脉</span>
      </li>
    </ul>
    <div class="page-tab-container">
        <div class="record-title clearfix">
          <span class="text-left">被邀请人/邀请时间</span>
          <span class="text-right pull-right">获得奖励</span>
        </div>
        <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :bottom-method="loadBottom" :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">
            <ul v-for="item in activeList" class="record-list">
              <li>
                <div class="left">
                  <span v-if="item.inviteeUserMobile" class="text-left color-333 fz-16">{{ item.inviteeUserMobile }}</span>
                  <span v-else class="text-left color-333 fz-16">{{ item.inviteeUserName }}</span>
                  <span class="text-left color-999 fz-13">{{ item.inviteTime | dateFormatFun(4) }}</span>
                </div>
                <span class="awardRedTotal text-right color-333 fz-16">红包 <i class="main-color">{{ item.awardRedTotal }}元</i></span>
              </li>
            </ul>
            <div class="text-center no-data" v-show="noData">
              <img src="../../assets/images/public/default/default_icon_no_record.png">
              <p>暂无记录</p>
            </div>
          </mt-loadmore>
        </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config'

  export default {
    data() {
      return {
        active: 0, // 导航切换，0一级好友，1二级好友
        wrapperHeight: 0, // 列表容器可视高度
        logList: [
          {
            name: 'tab-con1',
            inviteLevel: 1,
            getParams: {
              userId: this.$store.state.user.userId,
              __sid: this.$store.state.user.__sid,
              'page.page': 1,
              inviteLevel: 1
            }
          },
          {
            name: 'tab-con2',
            inviteLevel: 2,
            getParams: {
              userId: this.$store.state.user.userId,
              __sid: this.$store.state.user.__sid,
              'page.page': 1,
              inviteLevel: 2
            }
          }
        ],
        allLoaded: false,
        noData: false,
        activeList: []
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
      changeTab(index) {
        if (index != this.active) {
          this.active = index;
          this.activeList = [];
          this.logList[this.active].getParams['page.page'] = 1;
          this.projectList();
        }
      },
      // 数据加载
      projectList() {
        this.$http.get(ajaxUrl.inviteLogList, { params: this.logList[this.active].getParams }).then((res) => {
          if (res.data.resData) {
            if (res.data.resData.list.length <= 0) { // 无数据
              this.noData = true;
              return false;
            }
            if (res.data.resData.page > res.data.resData.totalPage && type == 'loadMore') { // 最后一页就不显示上拉加载
              this.$toast('无更多数据加载哦~');
              this.allLoaded = true;
            } else {
              if (res.data.resData.totalPage == 1) { // 只有一页数据就不显示上拉加载
                this.allLoaded = true;
              } else {
                this.allLoaded = false;
              }
              this.activeList = this.activeList.concat(res.data.resData.list);
            }
          }
        })
      },
      loadTop(id) {
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          this.activeList = [];
          this.allLoaded = false;
          this.logList[this.active].getParams['page.page'] = 1;
          this.projectList('reload');
        },1000)
      },
      loadBottom(id) {
        setTimeout(() => {
          this.logList[this.active].getParams['page.page']++;
          this.$refs.loadmore.onBottomLoaded(id);
          this.projectList('loadMore');
        }, 500);
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
    font-size: 0
  }
  .rd-tab-title li{
    width: 50%;
    line-height: .4rem;
    text-align: center;
    display: inline-block;
    font-size: .14rem;
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
    font-size: 0;
  }
  .record-list li:last-child{
    border: none;
  }
  .record-list li .left{
    width: 50%;
    height: 0.7rem;
    display: inline-block;
    vertical-align: top;
  }
  .record-list li span:first-child{
    padding-top: 0.16rem;
  }
  .record-list li span:last-child{
    padding-top: 0.1rem;
  }
  .record-list li span{
    font-size: 0.14rem;
    display: inline-block;
    width: 100%;
  }
  .record-list li span.awardRedTotal{
    display: inline-block;
    width: 50%;
    height: 0.7rem;
    vertical-align: top;
    line-height: 0.7rem;
    padding-top: 0;
  }
</style>
