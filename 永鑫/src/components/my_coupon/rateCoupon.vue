<!-- 我的优惠-加息券 by fenglei -->
<template>
  <div class="rate-coupon-list">
    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadMore">
      <ul>
        <li v-for="(item, index) in dataList">
          <div class="top-left">
            <span :class="{ 'gay': item.status != 0 }">+<i>{{ item.upApr }}</i>%</span>
          </div>
          <div class="top-right">
            <span class="name">{{ item.ruleName }}</span>
            <span v-if="!item.useExpireTime" class="time">长期有效</span>
            <span v-else class="time">到期日期&nbsp;{{ item.useExpireTime | dateFormatFun(4) }}</span>
            <span class="rule">使用条件&nbsp;&nbsp;{{ item.ruleRemark }}</span>
          </div>
          <div class="bottom">
            <div v-if="item.useProjectType == 0" class="text all">适用于所有产品</div>
            <div v-else class="text part showed" @click="showTypeName(index + 1)" :id="'rate-part' + (index + 1)">
              <span>点击查看适用产品</span>
              <div class="icon-img">
                <img src="../../assets/images/public/arrow_right.png" :id="'rate-icon-show' + (index + 1)" class="icon-show">
              </div>
            </div>
          </div>
          <div v-if="item.useProjectType != 0" :id="'rate-type-name' + (index + 1)" class="type-name"><span>{{ item.useProjectTypeName }}</span></div>
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
      <p>暂无加息券</p>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';

  export default {
    data() {
      return {
        dataList: [],
        allLoaded: false,
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
      dataLoad(type) {
        this.$http.get(ajaxUrl.userRateCouponList, { params: this.params }).then((res) => {
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
      resetData() {
        this.dataList = [];
        this.params['page.page'] = 1;
      },
      loadTop() {
        setTimeout(() => {
          this.resetData();
          this.$refs.loadMore.onTopLoaded();
          this.dataLoad('reload');
        }, 1000);
      },
      loadBottom() {
        var t = setTimeout(() => {
          this.params['page.page']++;
          this.$refs.loadMore.onBottomLoaded();
          this.dataLoad('loadMore');
        }, 500);
      },
      showTypeName(ref) {
        let target = document.getElementById('rate-part' + ref);
        let height = document.getElementById('rate-type-name' + ref).children[0].getBoundingClientRect().height;

        if (target.className.indexOf('showed') >= 0) {
          target.className = 'text part';
          document.getElementById('rate-icon-show' + ref).className = 'icon-show rotate-up';
          document.getElementById('rate-type-name' + ref).style.height = height + 'px';
        } else {
          target.className = 'text part showed';
          document.getElementById('rate-icon-show' + ref).className = 'icon-show';
          document.getElementById('rate-type-name' + ref).style.height = 0;
        }
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "rateCoupon.sass";
</style>
