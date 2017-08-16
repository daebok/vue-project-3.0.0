<!-- 平台公告 -->
<template>
  <div class="page" id="siteIntro">
    <mt-header class="bar-nav margin-b-10" title="平台公告">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="page-loadmore-wrapper" ref="wrapper" :style="{height: wrapperHeight + 'px'}">
      <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
        <mt-cell v-for="item in list" :key="item.id" :title="item.title" :label="item.remark" :to="'/mine/siteIntro/detail/'+item.uuid" is-link></mt-cell>
      </mt-loadmore>
      <div class="text-center no-data" v-show="noData">
        <img src="../../assets/images/public/default/default_icon_no_notice.png" alt="缺省图片">
        <p>暂无公告</p>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';
  export default {
    data() {
      return {
        list: [], // 公告列表数据数据
        noData: false, // 有无数据，true无数据，false有数据
        allLoaded: false, // 有无下一页数据，true无数据，false有数据
        page: 1, // 页数
        totalPage:1, // 总页数
        wrapperHeight: 0 // 列表容器可视高度
      };
    },
    created() {
      this.projectList();
      this.$nextTick(()=> {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    methods: {
      // 数据列表加载
      projectList(page = 1) {
        this.noData = false;
        this.$indicator.open({ spinnerType: 'fading-circle' }); // mint-ui加载中动画效果
        let getParams = {
          sectionCode: 'notice',
          'page.page': page
        };
        this.$http.get(ajaxUrl.getArticleList, { params: getParams }).then((res) => {
          this.$indicator.close();
          if (res.data.resData.list.length === 0) {
            this.noData = true;
          }
          if (page > res.data.resData.totalPage) {
            if (res.data.resData.totalPage > 0) {
              this.$toast('无更多数据加载哦~');
            }
            return; // 请求页大于总页数就返回不执行
          }
          if (res.data.resData.totalPage <= 1) {
            this.allLoaded = true;
          }
          this.list = this.list.concat(res.data.resData.list);
        })
      },
      // 下拉拖动加载更多数据
      loadBottom(id) {
        setTimeout(() => {
          this.page++;
          this.projectList(this.page);
          this.$refs.loadmore.onBottomLoaded(id);
          if (this.page > this.totalPage) {
            this.allLoaded = true; // 若数据已全部获取完毕
          }
        }, 500);
      }
    }
  }
</script>
