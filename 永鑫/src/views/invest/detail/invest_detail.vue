<template>
  <div class="page">
    <div class="content">
      <div class="clearfix">
        <!-- s头部产品详情信息 -->
          <ul class="page-infinite-list">
            <!-- 写死dom -->
            <li class="bid-con aui-border-b" @click="toUrl()">
              <h3>
                {{ investData.fundName }}
                <span class="service-charge pull-right">
                  0手续费
                </span>
              </h3>
              <em class="product-num">{{ investData.fundCode }}</em>
              <div class="bid-info clearfix">
                <div class="apr-part pull-left">
                  <h1 class="apr main-color">{{ investData.dayIncomeratio }}<b >%</b></h1>
                  <p>七日年化收益</p>
                </div>
                <div class="time-part pull-left">
                  <p class="mar-dx">
                    <b class="strong">{{ investData.hxHfIncomeratio }}<i>元</i></b>
                  </p>
                  <p>
                    <!-- 万份收益  <b>({{Date.parse(new Date()) | dateFormatFun(6)}})</b> -->
                    万份收益  <b>({{ investData.dayincdate }})</b>
                  </p>
                </div>
              </div>
            </li>
          </ul>
        <!-- e头部产品详情信息 -->
      </div>

      <div class="clearfix">
        <!-- s中部图表 -->
          <div class="chart-wrapper margin-t-10">
            <ul class="chart-list">
              <li :class="{ 'current': active == item.id }" v-for="(item, index) in menuList" @click="menuChange(index)">
                {{ item.title }}
              </li>
            </ul>

            <section class="chart-content">
              <p class="chart-info">
                <span style="width:40%;">七日年化:<i>{{ investData.dayIncomeratio }}%</i></span>
                <span style="width:35%;">万份收益:{{ investData.hxHfIncomeratio }}</span>
                <span style="width:22%; text-align:right;">{{ investData.dayincdate }}</span>
              </p>

              <!-- s图表 -->
                <!-- <img src="../../../assets/images/yxbx/chat.png" class="im-chat"/> -->
                <div class="container">
                  <IEcharts :option="line" :loading="loading" @ready="onReady" @click="onClick"></IEcharts>
                </div>
              <!-- e图表 -->
            </section>
          </div>
        <!-- e中部图表 -->
      </div>

      <!-- s内页列表 -->
      <div class="clearfix">
        <ul class="project-detail-3 margin-t-10">
          <li class="border-b-e2 aui-border-b">
            <router-link :to="{name: 'investInfo'}">
              <img src="../../../assets/images/yxbx/xiangqing_ico_cpxq.png" class="line-icon"/>
              产品详情
              <img src="../../../assets/images/public/arrow_right.png" class="arrow-right"/>
            </router-link>
          </li>
          <li class="border-b-d4 aui-border-b">
            <router-link :to="{name: 'investRule'}">
              <img src="../../../assets/images/yxbx/xiangqing_ico_jygz.png" class="line-icon"/>
              交易规则
              <img src="../../../assets/images/public/arrow_right.png" class="arrow-right"/>
            </router-link>
          </li>
          <li class="border-b-d4">
            <router-link :to="{name: 'fundManager'}">
            <!-- <a href="http://www.baidu.com"> -->
              <img src="../../../assets/images/yxbx/xiangqing_ico_jjjl.png" class="line-icon"/>
              基金经理
              <img src="../../../assets/images/public/arrow_right.png" class="arrow-right"/>
            <!-- </a> -->
            </router-link>
          </li>
        </ul>
      </div>
      <!-- e内页列表 -->
    </div>

    <!-- s购买按钮判断 -->
      <div @click="applyBuy" class="invest-btn">买入</div>
    <!-- e购买按钮判断 -->
  </div>
</template>

<script>
  import * as ajaxUrl from '../../../ajax.config.js'
  // 图表插件
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
        investData: '', // 理财详情数据
        //传给接口数据
        params: {
          fundCode: this.$route.params.projectId,//产品id
          openId: this.$route.params.openId,//微信openId
          //userId: this.$store.state.user.userId,//用户登录信息
          //__sid: this.$store.state.user.__sid
        },
        isreg: true,
        menuList: [
          {
            title: '今年以来',
            id: 'tab-container-0',
            type: '1'
          },
          {
            title: '近3个月',
            id: 'tab-container-1',
            type: '2'
          },
          {
            title: '近1年',
            id: 'tab-container-2',
            type: '3'
          },
          {
            title: '成立以来',
            id: 'tab-container-3',
            type: '4'
          }
        ],
        active: 'tab-container-0',//初始化第一个选中
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
                    offset: 0, color: 'rgba(252,210,186,1)' // 0% 处的颜色
                  }, {
                    offset: 1, color: 'rgba(252,210,186,1)' // 100% 处的颜色
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
      // 理财详情数据初始化
      this.$indicator.open({spinnerType: 'fading-circle'}) //等待接口加载中状态
      this.$http.get(ajaxUrl.projectDetailAjax, { params: this.params }).then((res) => {
        this.$indicator.close();
        if(res.data.resMsg == '请先注册后再进入'){
          //未注册进入注册页面
          //console.log(this.params.openId);
          this.isreg = false;
          //this.$router.push({name:'register', params: {openId: this.params.openId}})
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去注册',
            showCancelButton: false,
            message: '请先注册后再进入'
          }).then(res => {
            this.$router.push({name:'register', params: {openId: this.params.openId}})//进入注册
          });
        }else{
          if (res.data.resData) {
            this.investData = res.data.resData.productDetail;
            //this.investmentStatus();
          }
          //图表初始化
          let getParams_s = {
            fundCode: this.$route.params.projectId,//产品id
            openId: this.$route.params.openId,//微信openId
            timeType: '0',
            //userId: this.$store.state.user.userId,
            //__sid: this.$store.state.user.__sid, 
          }
          this.$indicator.open({ spinnerType: 'fading-circle' });
          this.$http.get(ajaxUrl.getFundHistory, { params: getParams_s }).then((res) => {
            //this.profit = res.data.resData.profit;//万份收益
            //this.sevenProfit = res.data.resData.sevenProfit;//七日年化收益
            let unitTime = res.data.resData.time
            //this.line.xAxis.data = unitTime.map(val => { return `${val.slice(4,-2)}-${val.slice(-2)}` })
            this.line.xAxis.data = unitTime.map(val => { return `${val.slice(0)}` })
            this.line.series[0].data = res.data.resData.profit
            this.$indicator.close();
          })
        }
      })
    },
    methods: {
      //tab切换
      menuChange(index) {
        this.active = 'tab-container-' + index;
        if(this.isreg == true){
          //图表切换
          let getParams_c = {
            fundCode: this.$route.params.projectId,//产品id
            openId: this.$route.params.openId,//微信openId
            timeType: index
            //userId: this.$store.state.user.userId,
            //__sid: this.$store.state.user.__sid
          }
          this.$indicator.open({ spinnerType: 'fading-circle' });
          this.$http.get(ajaxUrl.getFundHistory, { params: getParams_c }).then((res) => {
            let unitTime = res.data.resData.time//时间数据
            //this.line.xAxis.data = unitTime.map(val => { return `${val.slice(4,-2)}-${val.slice(-2)}` })
            this.line.xAxis.data = unitTime.map(val => { return `${val.slice(0)}` })
            this.line.series[0].data = res.data.resData.profit//利率数据
            this.$indicator.close();
          })
        }
      },
      onReady(instance) {
      },
      onClick(event, instance, echarts) {
        // console.log(arguments);
      },
      applyBuy(){
        this.$http.get(ajaxUrl.projectApplyAjax,{ params: this.params }).then((res) => {
          if(res.data.resMsg == '请先注册后再进入'){
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去注册',
              showCancelButton: false,
              message: '请先注册后再进入'
            }).then(res => {
              this.$router.push({name:'register', params: {openId: this.params.openId}})//进入注册
            });
          }else{
            document.write(res.data);//第三方跳转
          }
        })
      }
    },
    components: {
      //注册组件
      IEcharts
    },
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../../assets/scss/detail.scss';
  .container { height: 2.4rem; background:#fff;position: relative; overflow: hidden;}
</style>