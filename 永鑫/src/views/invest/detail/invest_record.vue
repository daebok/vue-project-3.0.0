<!-- 理财详情页-投资记录 by fenglei -->
<template>
  <div class="page">
    <div class="record-title" v-if="!noData">
      <p class="title-user">投资人/时间</p>
      <p class="title-money">金额(元)</p>
    </div>
    <div class="record-list page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :bottom-method="loadBottom" :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">
        <ul>
          <li v-for="item in list">
            <div class="left">
              <span class="userName">{{ item.userName }}</span>
              <span class="createTime">{{ item.createTime | dateFormatFun }}</span>
            </div>
            <span class="money">{{ item.money | currency('', 2) }}</span>
          </li>
        </ul>
      </mt-loadmore>
      <div class="no-data" v-if="noData">
        <img src="../../../assets/images/public/default/default_icon_no_record.png">
        <p>暂无记录</p>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../../ajax.config';

  export default {
    name: 'investDetail',
    data() {
      return {
        list: [],
        noData: false,
        allLoaded: false,
        wrapperHeight: 0,
        getParams: {
          projectId: this.$route.params.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          'page.page': 1,
          'page.pageSize': 10
        }
      }
    },
    created() {
      this.projectList();
      this.$nextTick(() => {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      });
    },
    methods: {
      projectList(type) {
        this.$http.get(ajaxUrl.recordListAjax, { params: this.getParams }).then((res) => {
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
              this.list = this.list.concat(res.data.resData.list);
            }
          }
        })
      },
      loadTop(id) {
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          this.list = [];
          this.allLoaded = false;
          this.getParams['page.page'] = 1;
          this.projectList('reload');
        },1000)
      },
      loadBottom(id) {
        setTimeout(() => {
          this.getParams['page.page']++;
          this.$refs.loadmore.onBottomLoaded(id);
          this.projectList('loadMore');
        }, 500);
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "../../../assets/sass/invest_record.sass";
</style>
