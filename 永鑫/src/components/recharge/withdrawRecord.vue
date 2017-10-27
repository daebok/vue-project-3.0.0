<!-- 充值记录 by fenglei -->
<template>
  <div class="withdraw-record-list">
    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadMore">
      <ul>
        <div v-for="(item1, index1) in dataList">
          <div class="timeF">{{ item1.timeF }}</div>
          <li v-for="(item2, index2) in item1.item" class="showed" :id="'withdraw-li' + (index1 + 1) + '-' + (index2 + 1)" @click="showBottom(index1 + 1, index2 + 1)">
            <div class="top">
              <span class="left">{{ item2.statusStr }}</span>
              <span class="right">+{{ item2.amount | currency('',2) }}</span>
            </div>
            <div class="middle">{{ item2.addTime | dateFormatFun(4) }}</div>
            <div class="bottom" :id="'withdraw-bottom' + (index1 + 1) + '-' + (index2 + 1)">
              <div class="more">
                <div class="cashFee item"><i>提现手续费</i><span>{{ item2.cashFee ? item2.cashFee : 0 }}元</span></div>
                <div class="orderNo item"><i>交易流水号</i><span>{{ item2.orderNo }}</span></div>
                <div class="remark item" v-if="item2.remark"><i>备注</i><span>{{ item2.remark }}</span></div>
              </div>
            </div>
          </li>
        </div>
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
        obj: {},
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
    methods: {
      dataLoad(type) {
        this.$http.get(ajaxUrl.getCashList, { params: this.params }).then((res) => {
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
              let rawList = res.data.resData.list;
              let index = 0;
              rawList.forEach(val => {
                let y = new Date(val.addTime).getFullYear();
                let m = new Date(val.addTime).getMonth() + 1;
                let timeF =  y + '年' + m +'月';
                if (this.obj.hasOwnProperty(timeF)) {
                  this.dataList[this.obj[timeF]].item.push(val);
                } else {
                  this.obj[timeF] = index++;
                  this.dataList.push({
                    timeF: timeF,
                    item: [ val ]
                  })
                }
              })
            }
          }
        })
      },
      resetData() {
        this.dataList = [];
        this.obj = {};
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
      showBottom(ref1, ref2) {
        console.log(1);
        let target = document.getElementById('withdraw-li' + ref1 + '-' + ref2);
        let height = document.getElementById('withdraw-bottom' + ref1 + '-' + ref2).children[0].getBoundingClientRect().height;

        if (target.className.indexOf('showed') >= 0) {
          target.className = '';
          document.getElementById('withdraw-bottom' + ref1 + '-' + ref2).style.height = height + 'px';
        } else {
          target.className = 'showed';
          document.getElementById('withdraw-bottom' + ref1 + '-' + ref2).style.height = 0;
        }
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "withdrawRecord.sass";
</style>
