<!-- 我的优惠-红包 by fenglei -->
<template>
  <div class="red-envelope-list">
    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadMore">
      <ul>
        <li v-for="(item, index) in dataList">
          <div class="top-left">
            <span :class="{ 'gay': item.status != 0 }">¥<i>{{ item.amount }}</i></span>
          </div>
          <div class="top-right">
            <span class="name">{{ item.ruleName }}</span>
            <span v-if="!item.useExpireTime" class="time">长期有效</span>
            <span v-else class="time">到期日期&nbsp;{{ item.useExpireTime | dateFormatFun(4) }}</span>
            <span class="rule">使用条件&nbsp;&nbsp;{{ item.ruleRemark }}</span>
          </div>
          <div class="bottom">
            <div v-if="item.useProjectType == 0" class="text all">适用于所有产品</div>
            <div v-else class="text part showed" @click="showTypeName(index + 1)" :id="'red-part' + (index + 1)">
              <span>点击查看适用产品</span>
              <div class="icon-img">
                <img src="../../assets/images/public/arrow_right.png" :id="'red-icon-show' + (index + 1)" class="icon-show">
              </div>
            </div>
          </div>
          <div v-if="item.useProjectType != 0" :id="'red-type-name' + (index + 1)" class="type-name"><span>{{ item.useProjectTypeName }}</span></div>
          <div class="status">
            <img v-if="item.status == 1" src="../../assets/images/money/coupon_used.png">
            <img v-if="item.status == 2" src="../../assets/images/money/coupon_expired.png">
            <img v-if="item.status == 3" src="../../assets/images/money/coupon_invalid.png">
          </div>
          <div class="left-icon"></div>
          <div class="right-icon"></div>
        </li>
      </ul>
    </mt-loadmore>
    <div v-show="noData" class="no-data">
      <img src="../../assets/images/public/default/default_icon_no_hb.png">
      <p>暂无红包</p>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';

  export default {
    data() {
      return {
        dataList: [],
        allLoaded: false, // 数据列表是否已经是最后一页或者只有一页，当为true时，禁止上拉刷新，反之则可以，结合mint-ui的loadmore组件使用
        noData: false,
        params: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          'page.page': 1,
          'page.pageSize': 10
        }
      };
    },
    created() {
      this.dataLoad();
    },
    methods: {
      // 数据列表加载
      // 当无数据时，noData置为true，显示暂无数据，dataList为空
      // 当当前页数大于数据总页数并由上拉刷新loadMore操作时，提示无更多数据，allLoaded置为true
      // 当数据总页数只有1页时，allLoaded置为true，否则置为false
      dataLoad(type) {
        this.$http.get(ajaxUrl.userRedenvelopeList, { params: this.params }).then((res) => {
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
      },
      // 点击查看适用产品
      // 利用css3样式transition和transform设置动画效果
      // 获取当前所单击的节点
      // 获取当前节点下red-type-name节点的第一个子节点的高度
      // 为red-icon-show添加rotate-up样式使之产生180旋转动画，去掉rotate-up则恢复原样
      // 将red-type-name高度置为height，使之产生0.5秒的显示动画，反之将高度置为0则产生影藏动画
      showTypeName(ref) {
        let target = document.getElementById('red-part' + ref);
        let height = document.getElementById('red-type-name' + ref).children[0].getBoundingClientRect().height;

        if (target.className.indexOf('showed') >= 0) {
          target.className = 'text part';
          document.getElementById('red-icon-show' + ref).className = 'icon-show rotate-up';
          document.getElementById('red-type-name' + ref).style.height = height + 'px';
        } else {
          target.className = 'text part showed';
          document.getElementById('red-icon-show' + ref).className = 'icon-show';
          document.getElementById('red-type-name' + ref).style.height = 0;
        }
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "redEnvelope.sass";
</style>
