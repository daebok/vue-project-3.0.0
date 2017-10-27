<!-- 帮助中心二级列表 by fenglei -->
<template>
  <div class="page">
    <div class="page-loadmore-wrapper help-list" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :bottom-method="loadBottom" :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">
        <ul>
          <li v-for="item in list" @click="linkTo('helpDetails', item.uuid)">
            <img class="qusetion-q" src="./../../assets/images/invest/qusetion_q.png">
            <span>{{ item.title }}</span>
            <img class="arrow-right" src="./../../assets/images/public/arrow_right.png">
          </li>
        </ul>
      </mt-loadmore>
      <div class="no-data" v-show="noData">
        <img src="../../assets/images/public/default/default_icon_no_notice.png">
        <p>暂无信息</p>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config';

  export default {
    data() {
      return {
        list: [],
        noData: false,
        allLoaded: false,
        wrapperHeight: 0,
        getParams: {
          sectionCode: this.$route.query.code,
          'page.page': 1,
          'page.pageSize': 10
        }
      };
    },
    created() {
      this.projectList();
      this.$nextTick(()=> {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    methods: {
      projectList(type) {
        this.$http.get(ajaxUrl.getArticleList, { params: this.getParams }).then((res) => {
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
      linkTo(name,uuid) {
        this.$router.push({ name: name, params: { uuid: uuid }});
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
  @import "../../assets/sass/help_column.sass";
</style>
