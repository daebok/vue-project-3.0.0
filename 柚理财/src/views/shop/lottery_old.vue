<template>
	<div id="lottery" class="lottery">
		<div class="lottery-1">
			<img src="../../assets/images/lottery/turntable_bg.png" />
		</div>
		<div class="lottery-3" >
      <img src="../../assets/images/lottery/turntable.png" class="turntable-bg" :style="{transform:'rotate('+angles+'deg)'}"/>
      <div class="turntable-pointer" >
        <img src="../../assets/images/lottery/turntable_pointer.png" />
      </div>
      <div class="lottery-btn" @click="startRun">
        <img src="../../assets/images/lottery/turntable_btn_click.png" v-if=" lotteryNum > 0"/>
        <img src="../../assets/images/lottery/turntable_btn_normal.png" v-if=" lotteryNum == 0" />
      </div>
    </div>
		<div class="lottery-4">
      <div v-if=" userLogin ">
        您还剩余<i>{{ lotteryNum }}</i>次抽奖机会
      </div>
      <div v-else>
        <router-link to="/login?redirect=/shop/lottery">
          <span>登录</span>
        </router-link>
        即可抽奖
      </div>
    </div>
		<div class="lottery-5">
      <div class="lottery-content">
        <img src="../../assets/images/lottery/award_name.png" />
        <ul class="lottery-list" >
          <li v-for="item in recordList">
            <span class="mobile">{{ item.mobile }}</span>
            <span class="prize">{{ item.lotteryValue }}</span>
            <span class="date">{{ item.lotteryTime  | dateFormatFun }}</span>
          </li>
        </ul>
      </div>
    </div>
    <div class="lottery-tips">
      <img src="../../assets/images/lottery/turntable_02.png" />
    </div>
    <transition name="fade">
      <div class="prompt-box" v-if="showTips">
        <div class="box-frame">
          <div class="box-bg">
            <img src="../../assets/images/lottery/zhuanpan_tan_bg.png" />
          </div>
          <div class="box-look">
            <img src="../../assets/images/lottery/zhuanpan_biaoqing01.png" v-if=" lotteryArea == -1 "/>
            <img src="../../assets/images/lottery/zhuanpan_biaoqing02.png" v-if=" lotteryArea != -1 "/>
          </div>
          <div class="box-tips-1">
            <img src="../../assets/images/lottery/zhuanpan_word2.png" v-if=" lotteryArea == -1 "/>
            <img src="../../assets/images/lottery/zhuanpan_word3.png" v-else-if=" lotteryArea == 5 "/>
            <img src="../../assets/images/lottery/zhuanpan_word1.png" v-else/>
          </div>
          <div class="award-name" v-if=" lotteryArea != 5 && lotteryArea != -1 ">
            <span>获得{{ lotteryResultInfo.lotteryName }}</span>
          </div>
          <div class="tips-close" @click=" closeTips ">
            <img src="../../assets/images/lottery/zhuanpan_close.png" />
          </div>
        </div>
      </div>
    </transition>
	</div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config.js';

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
        angles: 0,
        lotteryNum: 0, // 抽奖次数
        userLogin: localStorage.user || this.$route.query.__sid ? 1 : 0, //用于判断用户是否登录，1.已登录，0.未登录
        showTips: false,
      };
    },
    created() {
      this.loadScoreLotteryInfo();
      this.recordLists();
      document.title = this.$route.meta.title
    },
    methods: {
      /**
      * 我的积分及可抽奖次数
      */
      loadScoreLotteryInfo () {
        if(this.userLogin){
          this.$http.get(ajaxUrl.scoreLotteryInfoAjax, { params: { __sid: this.sid, userId: this.userId }}).then((res) => {
            this.scoreLotteryInfo = res.data.resData;
            this.lotteryNum = this.scoreLotteryInfo.lotteryNum;
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
                  this.lotteryNum--;
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
            this.angles = angle; // 传入偏转角度
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
          this.recordList = res.data.resData.recordList;
        });
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  .lottery
    width: 100%
    height: 100%
    background: #ffd800
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
      margin-top: .25rem
      text-align: center
      .turntable-bg
        width: 90%
      .turntable-pointer
        position: absolute
        top: .9rem
        left: 1.02rem
        img
          width: 1.6875rem
      .lottery-btn
        position: absolute
        top: 1.3rem
        left: 1.42rem
        img
          width: .9rem
    .lottery-4
      font-size: .16rem
      text-align: center
      color: #e65d5d
      /*-webkit-text-stroke: 1px #FF5C5D*/
      margin-top: .26rem
      margin-bottom: .3rem
      i
        padding: 0 .1rem
        display: inline-block
        border-bottom: 1px solid #FF5C5D
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
</style>