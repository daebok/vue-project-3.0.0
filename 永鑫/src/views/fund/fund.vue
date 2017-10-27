<!-- 粮票宝 -->
<template>
  <div class="page" id="account">
    <div class="content">
      <section class="account-info">
        <div class="tips" @click="alertTip">嘉实活期宝货币基金 <img src="../../assets/images/fund/ico_wenhao.png" ></div>
        <router-link to="/fund/list">
          <div class="total-n font-arial">
            <span v-if="resdata.money">{{ resdata.money | currency('', 2) }}</span>
            <span v-else>0.00</span>
            <img src="../../assets/images/fund/btn_arrow_r.png">
          </div>
        </router-link>
        <span class="total-t">
          <span class="rd-text">持有金额(元)</span>
        </span>
        <ul class="clearfix">
          <li>
            <span class="rd-text">昨日收益(元)</span>
            <p class="font-arial">{{ resdata.lastProfit | currency('', 2) }}</p>
            <b></b>
          </li>
          <li>
            <span class="rd-text">累计收益(元)</span>
            <p class="font-arial">{{ resdata.totalProfit | currency('', 2) }}</p>
          </li>
        </ul>
      </section>
      <ul class="rd-tab-title aui-border-b">
        <li><i>7日年化收益率</i> {{sevenProfit}}%</li>
        <li><i>万份收益</i> {{profit}}元</li>
      </ul>
      <div class="container">
        <IEcharts :option="line" :loading="loading" @ready="onReady" @click="onClick"></IEcharts>
      </div>
      <router-link to="/mine/help"><div class="ques">常见问题 <p><img src="../../assets/images/public/arrow_right.png" alt=""></p></div></router-link>
    </div>
    <div class="fund-btn">
      <div class="btn-1" @click="fundOut">转出</div>
      <div class="btn-2" @click="fundIn">转入</div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js'
  import IEcharts from 'vue-echarts-v3/src/lite.vue'
  import 'echarts/lib/chart/line'
  import 'echarts/lib/component/tooltip'
  export default {
    data() {
      return {
        resdata: '',
        userInfo: '',
        profit: '',
        sevenProfit : '',
        active: 'tab-con1',
        loading: false,
        line: {
          legend: {
            show: false
          },
          grid: {
            top: 20,
            bottom: 40,
            borderColor: '#999'
          },
          tooltip: {
            trigger: 'axis',
            showContent: true,
            formatter: '{c0}',
            axisPointer: {
              type: 'line',
              z: 1,
              label: {
                show: false,
                backgroundColor: 'rgba(255,255,255,0)'
              },
              lineStyle: {
                color: '#EF9C00'
              },
              crossStyle: {
                color: '#EF9C00',
                type: 'solid'
              }
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            axisLine: {show: false}, // 不显示坐标轴线
            axisTick: {show: false}, // 不显示坐标轴刻度
            axisLabel: {textStyle: {color: '#999'}},
            data: []
          },
          yAxis: {
            axisLine: {show: false},
            axisTick: {show: false}, // 不显示坐标轴刻度
            splitLine: {
              lineStyle: {
                color : '#EEEEEE'
              }
            },
            axisLabel: {
              textStyle: {color: '#999'},
              formatter: function (value) {
                return value + '%';
              }
            },
            z: 2
          },
          series: [{
            name: '',
            type: 'line',
            data: [],
            areaStyle: {
              normal: {
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: 'rgba(215,50,56,.5)' // 0% 处的颜色
                  }, {
                    offset: 1, color: 'rgba(215,50,56,.1)' // 100% 处的颜色
                  }],
                  globalCoord: false // 缺省为 false
                }
              }
            }
          }]
        }
      };
    },
    created() {
      let getParams = {
        userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
      }
      this.$indicator.open({ spinnerType: 'fading-circle' });
      this.$http.get(ajaxUrl.getUnitStatistics, { params: getParams }).then((res) => {
        this.profit = res.data.resData.profit;
        this.sevenProfit = res.data.resData.sevenProfit;
        let unitTime = res.data.resData.unitTime
        this.line.xAxis.data = unitTime.map(val => { return `${val.slice(4,-2)}-${val.slice(-2)}` })
        this.line.series[0].data = res.data.resData.unitData
        this.$indicator.close();
      })
      this.$http.get(ajaxUrl.fund, { params: getParams }).then((res) => {
        this.resdata = res.data.resData
      })
      this.$http.get(ajaxUrl.basicInfo, { params: getParams }).then((res) => {
        this.userInfo = res.data.resData;
      })
    },
    methods: {
      alertTip () {
        this.$messagebox({
          title: '嘉实活期宝货币基金',
          confirmButtonText: '知道了',
          showCancelButton: false,
          message: '购买粮票宝相当于购买了"嘉实活期宝货币基金”（嘉实基金代码：000464），该产品由嘉实基金公司提供。'
        })
      },
      onReady(instance) {
      },
      onClick(event, instance, echarts) {
        // console.log(arguments);
      },
      // 判断用户实名绑卡信息
      fundIn() {
        if (this.userInfo.realnameStatus == '0') {
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去开通',
            showCancelButton: true,
            message: '您尚未开通资金账户，为了顺利投资请先开通资金账户！'
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push({ name: 'realname', query: { from: 'fund' }});
            }
          });
        } else if (this.userInfo.bankNum < 1) {
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去绑定',
            showCancelButton: true,
            message: '您尚未绑定银行卡，为了顺利投资请先绑定银行卡！'
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push({ name: 'addBank', query: { from: 'fund' }});
            }
          });
        } else if (this.userInfo.hasSetPayPwd == 0) {
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去设置',
            showCancelButton: true,
            message: '您尚未设置支付密码，为了顺利投资请先设置支付密码！'
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push({name: 'setPwd', query: { from: 'fund'}});
            }
          });
        } else {
          this.$router.push({ name: 'fundIn' });
        }
      },
      fundOut() {
        if (this.userInfo.realnameStatus == '0') {
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去开通',
            showCancelButton: true,
            message: '您尚未开通资金账户，为了顺利投资请先开通资金账户！'
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push({ name: 'realname', query: { from: 'fund' }});
            }
          });
        } else if (this.userInfo.bankNum < 1) {
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去绑定',
            showCancelButton: true,
            message: '您尚未绑定银行卡，为了顺利投资请先绑定银行卡！'
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push({ name: 'addBank', query: { from: 'fund' }});
            }
          });
        } else if (this.userInfo.hasSetPayPwd == 0) {
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去设置',
            showCancelButton: true,
            message: '您尚未设置支付密码，为了顺利投资请先设置支付密码！'
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push({ name: 'setPwd', query: { from: 'fund' }});
            }
          });
        } else {
          this.$router.push({ name: 'fundOut' });
        }
      }
    },
    components: {
      IEcharts
    },
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../assets/scss/var.scss';
  .content { margin-bottom: .45rem; }
  .bar-nav{ background-color: $main-color;color:#fff; }
  .account-info{
    background-color: $main-color;
    text-align: center;
    padding-top: .1rem;
    height: 2rem;
    font-size: .12rem;
    background: url(../../assets/images/fund/lpb_bg.png);
    .tips {
      color:#fff;
      opacity: .9;
      text-align: right;
      padding-right: .15rem;
      margin-bottom: .25rem;
      img { width: .11rem; vertical-align: middle;}
    }
    .rd-text { opacity: .8; color:#fff;}
    .total-t {display: inline-block;position: relative;margin-top: 0.1rem;}
    .total-n {
      color: #fff; font-size: .4rem; display: block;
      margin-top: .12rem;line-height:1;
      position: relative;
      img {
        width: .13rem;
        position: absolute;
        right: .15rem;
        top: 60%;
        margin-top: -.07rem;
      }
    }
    ul {
      margin-top: .2rem;
      li {
        float: left;
        width: 50%;
        position: relative;
        p {font-size: .18rem; color:#fff;margin-top: .1rem;}
        b {
          border-right: 1px solid #fff;
          width: 0;
          height: 140%;
          position: absolute;
          right: 0;
          top: -10%;
          transform: scale(.5);
        }
      }
    }
  }
  .rd-tab-title {
    width: 100%;
    height: .4rem;
    background: #fff;
    position: relative;
    z-index: 2;
    li {
      width: 50%;
      line-height: .36rem;
      text-align: center;
      display: inline-block;
      float: left;
      &.current {
        color: $main-color;
        border-bottom: 2px solid $main-color;
      }
      i {
        color: #999999;
      }
    }
  }
  .container { height: 2.4rem; background:#fff;position: relative; overflow: hidden;}
  .ques {
    margin: .1rem auto;
    line-height: .45rem;
    padding: 0 .15rem;
    background: #fff;
    p {
      float: right;
      img { width: .14rem; vertical-align: middle;}
    }
  }
  .fund-btn {
    position: absolute;
    width: 100%;
    bottom: 0;
    z-index: 2;
    background: #fff;
    color: #666;
    text-align: center;
    line-height: .45rem;
    div { float: left;width: 50%; font-size: .18rem; }
    .btn-2 { background: $main-color; color: #fff;}
  }
</style>
