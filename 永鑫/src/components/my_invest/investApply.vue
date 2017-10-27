<!-- 我的理财-处理中 by fenglei -->
<template>
  <div class="invest-apple-list">
    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadMore">
      <ul>
        <li v-for="(item, index) in dataList">
          <div class="top">
            <router-link :to="{ name:'investDetail', params: { projectId: item.projectId }}">
              <div class="left">
                <span class="name">{{ item.projectName }}</span>
              </div>
              <div class="right">
                <span :id="'invest-apple-right' + index" class="status" :class="{ 'success': item.status != 0 }">{{ item.statusStr }}</span>
              </div>
            </router-link>
          </div>
          <div :id="'invest-apple-middle' + index" class="middle" :class="{ 'success': item.status != 0 }">
            <div class="item">
              <span class="text">投资金额(元)</span>
              <span class="value">{{ item.amount | currency('',2) }}</span>
            </div>
            <div class="item">
              <span class="text">投资时间</span>
              <span class="value">{{ item.createTime | dateFormatFun(4) }}</span>
            </div>
          </div>
          <div :id="'invest-apple-bottom' + index" class="bottom" v-if="item.status == 0">
            <div class="left">
              <span class="time">剩余时间&nbsp;
                <count-down class="count-down" @contDownOver="buttomHide" :remainTimes="item.remainTimes" :index="index"></count-down>
              </span>
            </div>
            <div class="right">
              <span class="pay" @click="toPay(item.uuid)">去支付</span>
            </div>
          </div>
        </li>
      </ul>
    </mt-loadmore>
    <div v-show="noData" class="no-data">
      <img src="../../assets/images/public/default/default_icon_no_hb.png">
      <p>暂无记录</p>
    </div>
    <transition name="slide">
      <my-invest-bid v-show="myInvestBidShow" ref="investBid" @paySuccess="reloadData" @rechargeOpen="toRecharge" @fundInOpen="toFundIn"></my-invest-bid>
    </transition>
    <transition name="slide">
      <recharge v-show="rechargeShow" ref="recharge"></recharge>
    </transition>
    <transition name="slide">
      <fund-in v-show="fundInShow" ref="fundIn" @fundInOver="toFundInMsg" @rechargeOpen="toRecharge"></fund-in>
    </transition>
    <transition name="slide">
      <fund-in-msg v-show="fundInMsgShow" ref="fundInMsg"></fund-in-msg>
    </transition>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';
  import CountDown from '../../components/my_invest/myInvest_countTime.vue'; // 剩余时间倒计时组件
  import myInvestBid from '../../components/my_invest/myInvest_bid.vue'; // 去支付组件
  import recharge from '../../components/recharge/recharge.vue'; // 充值组件
  import fundIn from '../../components/fund_in/fund_in.vue'; // 粮票宝转入组件
  import fundInMsg from '../../components/fund_in/fund_in_msg.vue'; // 粮票宝转入结果页组件

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
        },
        myInvestBidShow: false,
        rechargeShow: false,
        fundInShow: false,
        fundInMsgShow: false
      };
    },
    components: { CountDown, myInvestBid, recharge, fundIn, fundInMsg },
    created() {
      this.dataLoad();
    },
    watch: {
      // 监听去支付组件
      // 当组件被隐藏时清除定时器
      myInvestBidShow(newV, oldV) {
        if (newV == false) {
          clearInterval(this.$refs.investBid.interval);
        }
      }
    },
    mounted() {
      // 监听浏览器返回操作，调用componentsClose方法
      window.addEventListener('popstate', this.componentsClose, false);
    },
    destroyed() {
      // 当前页面销毁(即退出当前页面)时移除监听事件，此时也会调用componentsClose方法
      window.removeEventListener('popstate', this.componentsClose, false);
    },
    methods: {
      // 当监听到浏览器返回操作时，关闭某一层组件
      // IOS系统会有兼容性BUG，page-loadmore-wrapper样式的overflow: scroll属性会影响组件的正常显示，某些操作时需要将invest-apple-list节点的overHidden样式去掉
      // 各组件调用及关闭顺序优先级如下：(从左置右依次关闭)
      // 充值/提现记录组件→设置支付密码组件→找回支付密码组件→输入支付密码组件→协议组件→充值组件
      // 粮票宝转入结果页组件
      // 设置支付密码组件→找回支付密码组件→输入支付密码组件→协议组件→粮票宝转入组件
      // 设置支付密码组件→找回支付密码组件→输入支付密码组件→协议组件→去支付组件
      // 组件调用原则：调用一个组件，执行history.pushState(document.title, '#')添加一层当前页的历史记录；关闭一个组件，执行history.back()返回历史记录上一页来触发监听事件，通过监听事件来逐一判断来关闭组件
      componentsClose() {
        if (this.$refs.recharge.recordShow) {
          this.$refs.recharge.recordShow = false;
        } else if (this.$refs.recharge.setPayPwdShow) {
          this.$refs.recharge.setPayPwdShow = false;
        } else if (this.$refs.recharge.getPayPwdShow) {
          this.$refs.recharge.getPayPwdShow = false;
        } else if (this.$refs.recharge.payPwdShow) {
          this.$refs.recharge.payPwdShow = false;
        } else if (this.$refs.recharge.protocolShow) {
          this.$refs.recharge.protocolShow = false;
        } else if (this.rechargeShow) {
          this.rechargeShow = false;
          document.querySelector('.invest-apple-list').className = 'invest-apple-list page-loadmore-wrapper';
        } else if (this.fundInMsgShow) {
          this.fundInMsgShow = false;
          document.querySelector('.invest-apple-list').className = 'invest-apple-list page-loadmore-wrapper';
        } else if (this.$refs.fundIn.setPayPwdShow) {
          this.$refs.fundIn.setPayPwdShow = false;
        } else if (this.$refs.fundIn.getPayPwdShow) {
          this.$refs.fundIn.getPayPwdShow = false;
        } else if (this.$refs.fundIn.payPwdShow) {
          this.$refs.fundIn.payPwdShow = false;
        } else if (this.$refs.fundIn.protocolShow) {
          this.$refs.fundIn.protocolShow = false;
        } else if (this.fundInShow) {
          this.fundInShow = false;
          document.querySelector('.invest-apple-list').className = 'invest-apple-list page-loadmore-wrapper';
        } else if (this.$refs.investBid.setPayPwdShow) {
          this.$refs.investBid.setPayPwdShow = false;
        } else if (this.$refs.investBid.getPayPwdShow) {
          this.$refs.investBid.getPayPwdShow = false;
        } else if (this.$refs.investBid.payPwdShow) {
          this.$refs.investBid.payPwdShow = false;
        } else if (this.$refs.investBid.protocolShow) {
          this.$refs.investBid.protocolShow = false;
        } else if (this.myInvestBidShow) {
          this.myInvestBidShow = false;
          document.querySelector('.invest-apple-list').className = 'invest-apple-list page-loadmore-wrapper';
        }
      },
      dataLoad(type) {
        this.$http.get(ajaxUrl.getInvestApply, { params: this.params }).then((res) => {
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
      // 从剩余时间倒计时组件中监听到contDownOver事件并执行buttomHide方法
      // 当剩余时间为0时，倒计时结束，并将当前列表项中的支付状态显示为支付失败，将invest-apple-middle节点添加样式success，隐藏invest-apple-bottom节点
      buttomHide(index) {
        document.querySelector('#invest-apple-right' + index).innerText = '支付失败';
        document.querySelector('#invest-apple-middle' + index).className = 'middle success';
        document.querySelector('#invest-apple-bottom' + index).style.display = 'none';
      },
      // 去支付按钮，点击调用去支付组件
      // IOS系统会有兼容性BUG，page-loadmore-wrapper样式的overflow: scroll属性会影响组件的正常显示，某些操作时需要将invest-apple-list节点的page-loadmore-wrapper样式去掉，并添加overHidden样式
      toPay(uuid) {
        document.querySelector('.invest-apple-list').className = 'invest-apple-list overHidden';
        history.pushState(document.title, '#');
        this.myInvestBidShow = true;
        this.$refs.investBid.dataLoad(uuid);
      },
      // 从去支付组件中监听到paySuccess事件并执行reloadData方法
      // 当支付成功后重新加载处理中列表数据
      reloadData() {
        this.resetData();
        this.dataLoad('reload');
      },
      // 从去支付组件和粮票宝转入组件中监听到rechargeOpen事件并执行toRecharge方法
      // 当用户需要去充值的时候调用充值组件
      // 调用充值组件时，关闭去支付组件或粮票宝转入组件
      toRecharge() {
        this.myInvestBidShow = false;
        this.fundInShow = false;
        this.rechargeShow = true;
        this.$refs.recharge.dataLoad();
      },
      // 从去支付组件中监听到fundInOpen事件并执行toFundIn方法
      // 当用户需要去转入粮票宝时调用粮票宝转入组件
      // 调用粮票宝转入组件时，关闭去支付组件
      toFundIn() {
        this.myInvestBidShow = false;
        this.fundInShow = true;
        this.$refs.fundIn.dataLoad();
      },
      // 从粮票宝转入组件中监听到fundInOver事件并执行toFundInMsg方法
      // 当用户转入粮票宝成功后调用粮票宝结果页组件
      // 调粮票宝结果页组件时，关闭粮票宝转入组件
      toFundInMsg(data) {
        this.fundInShow = false;
        this.fundInMsgShow = true;
        this.$refs.fundInMsg.dataLoad(data);
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "investApply.sass";
</style>
