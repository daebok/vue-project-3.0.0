<template>
	<div id="lottery" class="lottery">
    <mt-header class="bar-nav" title="积分抽奖">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <!-- 转盘部分 -->
		<div class="lottery-3" >
      <img src="../../assets/images/lottery/turntable.png" class="turntable-bg" :style="{transform:'rotate('+angles+'deg)'}"/>
      <div class="lottery-btn" @click="startRun">
        <img src="../../assets/images/lottery/turntable_btn_click.png" v-if=" lotteryNum > 0"/>
        <img src="../../assets/images/lottery/turntable_btn_normal.png" v-if=" lotteryNum == 0" />
      </div>
      <!-- 游戏规则 -->
      <img src="../../assets/images/lottery/rule_btn.png" @click="showRule('show')"  class="rule-btn"/>
    </div>

    <!-- 积分信息 -->
		<div class="lottery-4">
      <div v-if=" userLogin ">
        <!-- 可用积分 <strong>{{ useScore }}</strong><br> -->
        可用抽奖次数 <strong>{{ lotteryNum }}</strong>
        <i>每一次抽奖消耗 {{ scoreLotteryNum }}积分</i>
      </div>
      <div v-else>
        <router-link to="/login?redirect=/shop/lottery">
          <span>登录</span>
        </router-link>
        即可抽奖
      </div>
    </div>

    <!-- 中奖记录按钮 -->
    <div class="margin-50">
      <router-link to="/shop/lottery/record">
        <mt-button size="large" type="danger">我的中奖记录</mt-button>
      </router-link>
    </div>

    <!-- 中奖记录滚动 -->    
    <div v-if="recordList.length > 0" class="latest-exchange-scroll-list">
      <div class="scroll-list">
        <ul id="lottery_list">
          <li v-for="(item, index) in recordList">
            恭喜 <span>{{ item.mobile }}</span> <i>抽中</i> {{ item.lotteryValue }}
          </li>
        </ul>
      </div>
    </div>

    <!-- 中奖结果弹窗部分 -->
    <transition name="fade">
      <div class="prompt-box" v-if="showTips">
        <div class="box-frame">
          <div class="box-bg">
            <img src="../../assets/images/lottery/zhuanpan_tan_bg.png" />
          </div>
          <div class="box-look">
            <!-- 未中奖 -->
            <img src="../../assets/images/lottery/zhuanpan_biaoqing01.png" v-if=" lotteryArea == 4 || lotteryArea == 0"/>
            <!-- 中奖 -->
            <img src="../../assets/images/lottery/zhuanpan_biaoqing02.png" v-else />
          </div>
          <div class="box-tips-1">
            <!-- 谢谢参与 -->
            <img src="../../assets/images/lottery/zhuanpan_word2.png" v-if=" lotteryArea == 4 "/>
            <!-- 再来一次 -->
            <img src="../../assets/images/lottery/zhuanpan_word3.png" v-else-if=" lotteryArea == 0 "/>
            <!-- 中奖 -->
            <img src="../../assets/images/lottery/zhuanpan_word1.png" v-else/>
          </div>
          <!-- 中奖内容 -->
          <div class="award-name" v-if="lotteryArea != 4 && lotteryArea != 0">
            <span>获得{{ lotteryResultInfo.lotteryName }}</span>
          </div>
          <!-- 关闭 -->
          <div class="tips-close" @click=" closeTips ">
            <img src="../../assets/images/lottery/zhuanpan_close.png" />
          </div>
        </div>
      </div>
    </transition>

    <!-- 抽奖规则弹窗部分 -->
    <transition name="fade">
      <div class="prompt-box" v-if="showRules">
        <section class="rule-box">
          <h3 class="rule-title">游戏规则</h3>
          <p class="rule-content" v-html="protocolHtml"></p>
          <img src="../../assets/images/lottery/rule_close.png" class="rule_close" @click= "showRule('close')"/>
        </section>
      </div>
    </transition>

	</div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config.js';
  import vsa from '../../mixins/vertical-scrolling-animation.js'; // 垂直滚动动画
  export default {
    data() {
      return {
        sid: this.$store.state.user.__sid || this.$route.query.__sid,
        userId: this.$store.state.user.userId || this.$route.query.userId,
        recordList: '',
        scoreLotteryInfo: '',
        lotteryResultInfo: '', // 抽奖结果集合
        lotteryArea: '',
        offset: '',
        lotteryName: '',
        isRun: false,
        angles: 11,
        lotteryNum: 0, // 抽奖次数
        useScore: 0, //可用积分
        scoreLotteryNum: 0, //消耗积分
        userLogin: localStorage.user || this.$route.query.__sid ? 1 : 0, //用于判断用户是否登录，1.已登录，0.未登录
        showTips: false,
        protocolHtml:'', // 游戏规则
        showRules: false
      };
    },
    mixins: [ vsa ],
    created() {
      this.loadScoreLotteryInfo();
      this.recordLists();
      document.title = this.$route.meta.title
    },
    methods: {
      /* 设置或获取CSS样式
       *设置：css(obj,{display:'block',color:'red'});
       *获取：css(obj,'color');
      */
      rdCss() {
        var obj = arguments[0];
        var options = arguments[1];
        if (!!obj === false || typeof options === 'undefined') {
          return null;
        }
        if (typeof options === 'string') {
          let res = document.defaultView.getComputedStyle(obj, null)[options];
          res = Number(res.replace('px', ''));
          return res;
        }
        if (typeof options === 'object') {
          for (var i in options) {
            obj.style[i] = options[i];
          }
        }
      },
      // 最新兑换上下无缝滚动效果
      startLotteryScroll() {
        let scrollHeight = Number(this.rdCss((document.querySelector('#lottery_list li')), 'height'));
        this.startmarquee('lottery_list', scrollHeight, 40, 3000);
      },
      /**
      * 我的积分及可抽奖次数
      */
      loadScoreLotteryInfo () {
        if(this.userLogin){
          this.$http.get(ajaxUrl.scoreLotteryInfoAjax, { params: { __sid: this.sid, userId: this.userId }}).then((res) => {
            this.scoreLotteryInfo = res.data.resData;
            this.lotteryNum = this.scoreLotteryInfo.lotteryNum;
            this.useScore = this.scoreLotteryInfo.useScore;
            this.scoreLotteryNum = this.scoreLotteryInfo.scoreLotteryNum;
          })
        }
      },
      /**
      * 抽奖
      */
      doLottery () {
        if(this.userLogin){
          this.$http.get(ajaxUrl.doLotteryAjax, { params: { __sid: this.sid, userId: this.userId }}).then((res) => {
            if (res.data.resCode == 39321) {
              this.lotteryResultInfo = res.data.resData;
              this.lotteryArea = this.lotteryResultInfo.lotteryArea;
              this.offset = this.lotteryArea;
              // 抽奖结果后将抽奖次数减减
              if (this.lotteryNum > 0) {
                if (this.lotteryArea != 5) {
                  this.lotteryNum--; //抽奖次数-1
                  this.useScore = this.useScore - this.scoreLotteryNum; //抽奖积分消耗
                }
              }
            }

          })
        }
      },
      /**
      * 执行旋转的方法
      * turntable_bg: dom 节点
      * angle: 角度
      * degs: 偏转变量
      * total: 记录上一次的变化结果,初始值设定偏移角度
      * cnt： 用做ratio的索引(10-29)
      * ratio: 存放角度的变化比例数组，制造快慢的旋转效果
      * offset: 0-7,代表需要停到的奖项，由后端传入
      * amount: 计算每次的偏转角度,每转一次偏转 45/200=0.225度,点击抽奖旋转200次，转完后就多偏转45度
      * result: 奖项名称，用于弹出显示中奖项目，由后端传入
      */
      rotates () {
        var i,
        angle = 0,
        degs = 0,
        cnt = 100,
        total = -22.5,
        ratio = [],
        amount = 18 - ( 0.225 * this.offset );
        ratio[1] = [0.2, 0.4, 0.6, 0.8, 1, 1, 1.2, 1.4, 1.6, 1.8];
        ratio[2] = [1.8, 1.6, 1.4, 1.2, 1, 1, 0.8, 0.6, 0.4, 0.2];

        for (i = 0; i < 200; i++) {
          // 设计为200次40ms的间隔，8s出结果感觉比较好
          setTimeout(() => {
            this.angles = 0;
            // 计算每次偏转增量，对应阶段的增减比例最终造成快慢变化
            degs = amount * (ratio[ String(cnt).substr(0,1) ][ String(cnt).substr(1,1) ]);
            angle = degs + total;
//            console.log(String(cnt).substr(0,1), degs)
            this.angles = angle+11; // 传入偏转角度
            total += degs; // 记录
            cnt++; // 依据次数用作ratio的索引，这里用到了闭包不能使用i
          },i*40);
        }
        setTimeout(() => {
          this.showTips = true; // 完成
        },200*40+500);
      },
      /**
      * 绑定事件，点击指针开始`
      */
      startRun () {
        if(this.userLogin){
          if(this.lotteryNum > 0){
            if(!this.isRun){
              this.doLottery();
              this.isRun = true;
              setTimeout(() => {
                this.rotates();
              },500)
            }
          }
        }else{
          this.$messagebox({
            confirmButtonText: '立即登录',
            message: "您尚未登录，登录后才能抽奖",
            showCancelButton: true,
            closeOnClickModal: false
          }).then(action => {
            if (action === 'confirm') {
              this.$router.push({ name: 'login' });
            }
          });
        }
      },
      closeTips () {
        this.lotteryArea = '';
        this.isRun = false;
        this.showTips = false;
      },
      /**
       * 抽奖记录，默认30条
       */
      recordLists () {
        this.$http.get(ajaxUrl.lotteryListAjax, { params: '' }).then((res) => {
          this.protocolHtml = res.data.resData.lotteryGameRule; //游戏规则
          this.recordList = res.data.resData.recordList;
          this.$nextTick(() => {
            if (this.recordList.length > 0) {
              this.startLotteryScroll();
            }
          });

        });
      },
      showRule(judge){
        if(judge == 'show'){
          this.showRules = true
        }else if(judge == 'close'){
          this.showRules = false
        }
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  .margin-50
    margin: .20rem .50rem
  .mint-button
    height: .50rem
  .lottery
    width: 100%
    //height: 8.55rem
    background: url(../../assets/images/lottery/lottery-bg.png) no-repeat center
    background-color: #14112d
    background-size: 100% auto
    padding-bottom: .20rem
    .lottery-1
      width: 100%
      img
        width: 100%
    .lottery-2
      height: .5rem
      color: #F15949
      img
        width: 1.2rem
        margin-top: .1rem
        margin-left: .2rem
      .lottery-2-tips
        font-weight: 600
        vertical-align: super
        i
          font-size: .18rem
          margin-left: .2rem
    .lottery-3
      position: relative
      padding-top: 1.83rem;
      text-align: center
      width: 100%
      overflow: hidden

      .rule-btn
        width: .71rem;
        position: absolute
        right: 0
        top: 1.75rem
      .turntable-bg
        width: 3.69rem
      .turntable-pointer
        position: absolute
        top: .9rem
        left: 1.02rem
        img
          width: 1.6875rem
      .lottery-btn
        position: absolute
        top: 2.95rem
        left: 1.26rem
        img
          width: 1.3rem
    .lottery-4
      font-size: .16rem
      text-align: center
      color: #fff
      /*-webkit-text-stroke: 1px #FF5C5D*/
      margin-top: .26rem
      i
        color: #bac5e8
        display: block
        margin-top: .05rem
    .lottery-5
      margin-bottom: .6rem
      .lottery-content
        position: relative
        text-align: center
        img
          width: 94%
        i
          position: absolute
          top: .2rem
          left: 1.3rem
          font-size: .22rem
          color: #fff
      .lottery-list
        position: absolute
        width: 80%
        height: 1.8rem
        top: .8rem
        left: 10%
        overflow: hidden
        li
          height: .22rem
          line-height: 1
          margin-bottom: .1rem
          span
            display: inline-block
            float: left
            text-align: center
            width: 33.33%
          .mobile
            color: #E32015
          .prize
            color: #666
          .date
            color: #666
    .lottery-tips
      background: #ffd800
      img
        width: 100%
        display: block
    .prompt-box
      position: fixed
      top: 0
      left: 0
      width: 100%
      height: 100%
      background: rgba(0,0,0,0.8)
      z-index: 222
      .box-frame
        position: relative
        .box-bg
          text-align: center
          margin-top: 50%
          img
            width: 90%
        .box-look
          width: 100%
          text-align: center
          position: absolute
          top: -0.75rem
          left: 0
          img
            width: 1.6rem
        .box-tips-1
          text-align: center
          position: absolute
          top: .9rem
          left: 0
          img
            width: 30%
        .award-name
          position: absolute
          top: 1.5rem
          width: 100%
          span
            display: block
            width: 100%
            text-align: center
            font-size: .2rem
            color: #fff
        .tips-close
          position: absolute
          text-align: center
          top: 0
          right: .2rem
          width: .5rem
          height: .5rem
          img
            width: .2rem
            margin-top: .15rem

  //滚动中奖记录
  .latest-exchange-scroll-list
    width: 3.6rem
    height: 1.2rem;
    background: url(../../assets/images/lottery/record_bg.png) no-repeat center;
    background-size: 100% auto;
    padding-top: .72rem
    .scroll-list
      height: .36rem
      line-height: .36rem
      background-color: #352f99
      overflow: hidden
      color: #fff
      margin: 0 .15rem 0 .30rem
      padding: 0 .24rem
      border-radius: .05rem
      ul
        li
          font-size: .12rem
          white-space: nowrap
          img
            display: inline-block
            width: .2rem
            height: .2rem
            border-radius: 100%
          span,label,i
            display: inline-block
          span
            margin-left: .10rem
          label
            color: #666
          i
            margin: 0 .10rem

  .rule-box
    background-color: #fff 
    width: 2.54rem
    margin: 1.5rem auto 0 auto
    border-radius: .20rem
    position: relative
    .rule-title
      text-align: center
      height: .48rem
      line-height: .48rem
      background-color: #ff9545
      border-top-left-radius: .20rem
      border-top-right-radius: .20rem
      color: #fff
      font-size: .16rem
    .rule-content
      padding: .20rem .25rem
      color: #333
      line-height: .24rem
    .rule_close
      position: absolute
      left: 50%
      width: .25rem
      margin-left: -.125rem
      bottom: -.40rem
</style>