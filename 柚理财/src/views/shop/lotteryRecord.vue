<template>
  <div id="lotteryRecord">
    <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-header class="bar-nav aui-border-b" title="中奖记录">
        <mt-button slot="left" icon="back" v-back-link></mt-button>
      </mt-header>
      <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadMore">
        <ul>
          <li v-for="(item, index) in dataList">
            <div class="left">
              <span class="text" :class="{ 'ellipsis-2': item.lotteryValue.length > 14 }">{{ item.lotteryValue }}</span>
            </div>
            <div class="right">
              <span class="time">{{ item.lotteryTime | dateFormatFun(6) }}</span>
            </div>
          </li>
        </ul>
      </mt-loadmore>
      <div v-show="noData" class="no-data">
        <img src="../../assets/images/public/default/default_icon_no_record.png">
        <p>暂无记录</p>
      </div>
    </div>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config.js';
  export default {
    data() {
      return {
        params: {
          __sid: this.$store.state.user.__sid,
          'page.page': 1,
          'page.pageSize': 10
        },
        dataList: [],
        noData: false,
        allLoaded: false,
        wrapperHeight: ''
      };
    },
    created(){
      this.$nextTick(() => {
        this.wrapperHeight = document.documentElement.clientHeight
      })
      this.dataLoad();
    },
    methods: {
      // 数据列表加载
      // 当无数据时，noData置为true，显示暂无数据，dataList为空
      // 当当前页数大于数据总页数并由上拉刷新loadMore操作时，提示无更多数据，allLoaded置为true
      // 当数据总页数只有1页时，allLoaded置为true，否则置为false
      dataLoad(type) {
        this.$http.get(ajaxUrl.getScoreLotteryAjax, { params: this.params }).then((res) => {
          this.noData = false;
          if (res.data.resData) {
            if (res.data.resData.list.length <= 0) {
              this.noData = true;
              return false;
            }
            if (res.data.resData.page > res.data.resData.totalPage && type == 'loadMore') {
              this.$toast('无更多数据加载哦~');
              this.allLoaded = true;
            } else {
              if (res.data.resData.totalPage == 1) {
                this.allLoaded = true;
              } else {
                this.allLoaded = false;
              }
              this.dataList = this.dataList.concat(res.data.resData.list);
            }
          }
        })
      },
      // 重置数据列表，dataList置空，page置回1
      resetData() {
        this.dataList = [];
        this.params['page.page'] = 1;
      },
      // 下拉刷新，结合mint-ui的loadmore组件使用
      // 设置1秒延迟以显示下拉动画，下拉后重置数据列表及参数，重新dataLoad数据
      loadTop() {
        setTimeout(() => {
          this.resetData();
          this.$refs.loadMore.onTopLoaded();
          this.dataLoad('reload');
        }, 1000);
      },
      // 上拉加载，结合mint-ui的loadmore组件使用
      // 设置0.5秒延迟以显示上拉动画，下拉后page累计加1，调用dataLoad加载下一页数据
      loadBottom() {
        var t = setTimeout(() => {
          this.params['page.page']++;
          this.$refs.loadMore.onBottomLoaded();
          this.dataLoad('loadMore');
        }, 500);
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "../../assets/sass/border_1px"
  ul
    padding-left: 5%
    background: #FFF
    li
      padding: 0.2rem 5% 0.2rem 0
      @include hairline('bottom', #DDD)
      &:last-child
        @include hairline('bottom', #FFF)
        font-size: 0
      .left
        display: inline-block
        vertical-align: top
        // width: 60%
        .text
          margin-top: 0.1rem
          &.ellipsis-2
            margin-top: 0
      .right
        position: absolute
        right: .15rem
        top: .30rem;
        .time
          color: #999
      span
        display: inline-block
        width: 100%
        line-height: 1.2em
        font-size: 0.16rem
</style>