<template>
  <div class="page">
    <div class="content">
    <mt-header class="bar-nav aui-border-b" title="债权详情" >
      <mt-button slot="left" icon="back" @click.native="backUrl"></mt-button>
    </mt-header>
      <div class="clearfix">
    <div class="project-detail">
      <div class="project-name">{{ resdata.bondName }}</div>
      <div class="title-icon text-center">
        <span v-if="resdata.novice == 1" class="bid-icon new aui-border">新客</span>
        <span v-if="resdata.bondUseful == 1" class="bid-icon kzr aui-border">可转让</span>
        <span v-if="resdata.realizeUseful == 1" class="bid-icon kbx aui-border">可变现</span>
        <span v-if="resdata.specificSale == 1 || resdata.specificSale == 3" class="bid-icon dx aui-border">定向</span>
        <span v-if="resdata.specificSale == 2" class="bid-icon vip aui-border">VIP{{resdata.vipLevel}}</span>
      </div>
      <div class="project-apr">
        <i class="apr main-auxiliary-color">{{ resdata.apr | currency('',2) }}</i>
        <em class="percent main-color">%</em>
        <span v-if="resdata.bondApr > 0" class="project-award font-arial">+{{ resdata.bondApr | currency('',1) }}%</span>
        <span v-if="resdata.bondApr < 0" class="project-award font-arial">{{ resdata.bondApr | currency('',1) }}%</span>
      </div>
      <div class="project-detail-1">
        <span class="start-money">起投金额 {{ resdata.bondLowestMoney }} 元</span>
        <span class="project-timelimit">产品期限 {{ resdata.remainDay }} 天</span>
      </div>
      <div class="percent-wra">
        <div class="percent-process" :style="'width:'+scales+'%'"></div>
      </div>
      <div class="project-detail-2">
        <span class="info-left">已完成 <i class="font-arial">{{ scales }}%</i></span>
        <span class="info-right">
                剩余可投
          <i class="balance-account main-color">{{ resdata.remainMoney | currency('',2)}}</i>
          元
            </span>
      </div>
    </div>
    </div>
    <div class="clearfix">
    <ul class="project-list margin-t-10">
      <li>
        <img src="../../../assets/images/finance/details_icon_sum.png" />
        <span>债权金额<i class="bond-money">{{ resdata.bondMostMoney | currency('',2)}}</i>元</span>
      </li>
      <li v-if="resdata.interestStyle">
        <img src="../../../assets/images/finance/details_icon_ji.png" />
        <span>{{ resdata.interestStyle }}</span>
      </li>
      <li v-if="resdata.repayStyle">
        <img src="../../../assets/images/finance/details_icon_way_of_repayment.png" />
        <span>{{ resdata.repayStyle }}</span>
      </li>
      <li v-if="resdata.riskLevel">
        <img src="../../../assets/images/finance/details_icon_security_level.png" />
        <span>{{ resdata.riskLevel }}</span>
      </li>
      <li v-if="resdata.tenderCondition">
        <img src="../../../assets/images/finance/details_icon_condition.png" />
        <span>{{ resdata.tenderCondition }}</span>
      </li>
      <li v-if="countTime">
        <img src="../../../assets/images/finance/details_icon_time_left.png" />
        <span>剩余时间 {{ countTime }}</span>
      </li>
    </ul>
    </div>
    <div class="clearfix">
    <ul class="project-detail-3 margin-t-10">
      <li class="border-b-e2 aui-border-b">
        <router-link :to="'/investDetail/'+resdata.oldProjectId+'/investInfo?borrowFlag='+resdata.borrowFlag+'&oldProjectId='+resdata.oldProjectId">产品详情
          <img src="../../../assets/images/public/arrow_right.png" class="arrow-right"/>
        </router-link>
      </li>
      <li class="border-b-d4 aui-border-b">
        <router-link :to="{name: 'investRecord',query:{bondName:$route.query.bondName}}">投资记录
          <img src="../../../assets/images/public/arrow_right.png" class="arrow-right"/>
        </router-link>
      </li>
      <li v-if="resdata.isVouch == 1">
        <span>担保机构</span>
        <span class="project-company color-999">{{resdata.vouchName}}</span>
      </li>
    </ul>
    </div>
    <section class="risk-warning clearfix">
      <span>市场有风险，投资需谨慎</span>
      <img src="../../../assets/images/finance/details_icon_gray.png" class="img-risk" />
    </section>
  </div>
    <div v-if="resdata.ifSale == 0" class="invest-btn-countDown">
      <div class="count-down-time">{{ resdata.saleTime | dateFormatFun(3) }}即将开售</div>
      <div class="count-down-remind">
        <div class="alarm-clock">
          <img src="../../../assets/images/finance/details_icon_reserve.png" />
        </div>
        <div class="remind">
          <p class="remind-title">开售提醒</p>
          <p class="remind-number">{{ resdata.bespeaNum }}人想买</p>
        </div>
      </div>
    </div>
    <a v-else @click="investBid" class="invest-btn" :class="{disabled: resdata.isClick == 0}">{{ resdata.clickTitle }}</a>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import countDown from '../../../mixins/countTime'
export default {
  name: 'bondDetail',
  data(){
    return {
      resdata: '',
      clickCode: 0,
      countTime: ''
    }
  },
  computed: {
    scales(){
      var scales = parseFloat((this.resdata.bondMostMoney - this.resdata.remainMoney) * 100/this.resdata.bondMostMoney)
      if(scales == 100) scales = parseInt(scales)
      return scales
    }
  },
  created(){
    let getParams = {
      projectId: this.$route.params.projectId,
      userId: this.$store.state.user.userId,
      __sid: this.$store.state.user.__sid
    }
    this.$http.get(ajaxUrl.getBondProjectDetail, {params:getParams}).then((res) => {
      this.resdata = res.data.resData
      this.clickCode = res.data.resData.clickCode
      if(this.resdata.saleEndTime){
        this.countDown(this.resdata.saleEndTime,this.resdata.timeNow)
      }
      if(this.clickCode == 1){
        this.$messagebox({
          title: ' ',
          confirmButtonText: '去开通',
          showCancelButton: true,
          message: '为了您的资金安全，请您开通第三方资金托管账户！'
        }).then(action => {
          if(action == 'confirm'){
            this.$router.push({name: 'realname',query:{from:'bondDetail',id: this.$route.params.projectId}})
          }
        });
      }else if(this.clickCode == 2 && this.resdata.ifSale == 1) {
        this.$messagebox({
          title: ' ',
          confirmButtonText: '首次投资请您完成风险承受能力评测！',
          showCancelButton: true,
          message: '请您先完成测评'
        }).then(action => {
          if(action == 'confirm'){
            this.$router.push({name: 'riskTips',query:{from:'bondDetail',id: this.$route.params.projectId}})
          }
        });
      }else if(this.clickCode == 3 && this.resdata.ifSale == 1) {
        this.$messagebox({
          title: ' ',
          confirmButtonText: '去绑定',
          showCancelButton: true,
          message: '该产品为定向邮箱投资，请绑定邮箱查看是否有投资权限！'
        }).then(action => {
          if(action == 'confirm'){
            this.$router.push({name: 'bindEmail',query:{from:'bondDetail',id: this.$route.params.projectId}})
          }
        });
      }
    })
  },
  mixins:[countDown],
  methods: {
    investBid(evt){
      if(this.clickCode == 1){  // 托管账户
        this.$router.push({name: 'realname', query:{from:'bondDetail',id: this.$route.params.projectId}})
      }
      else if(this.clickCode == 2){ // 风险测评
        this.$router.push({name: 'riskTips', query:{from:'bondDetail',id: this.$route.params.projectId}})
      }
      else if(this.clickCode == 3){  //绑定邮箱
        this.$router.push({name: 'bindEmail', query:{from:'bondDetail',id: this.$route.params.projectId}})
      }else if(this.clickCode == 4){  // 用户冻结
        this.$messagebox({
          title: ' ',
          confirmButtonText: '联系客服',
          showCancelButton: true,
          message: '您的投资功能被锁定，请联系客服解锁！'
        }).then(action => {
          if(action == 'confirm'){
            location.href='tel:400-183-3666'
          }
        });
        return ;
      }else if(this.resdata.isTipe == 1) {  // 超出用户风险等级
        let specialStr = '<span style="color:red;">' + this.resdata.tenderConditionstr + '</span>'
        let userStr = '<span class="main-color">' + this.resdata.userLevelName + '</span>'
        let strArr = this.resdata.userLevelTitle.split(this.resdata.tenderConditionstr)
        // 用户的风险类型
        let userArr = strArr[1].split(this.resdata.userLevelName)
        let tipStr = strArr[0] + specialStr + userArr[0] + userStr + userArr[1] + '<br><span style="color:#999">点击继续投资即表示您已充分认识并愿意承受本项目可能存在的风险，同意继续投资。</span>';
        this.$messagebox({
          title: ' ',
          confirmButtonText: '继续投资',
          showCancelButton: true,
          message: '<p style="font-size:.12rem;text-align:left;">'+ tipStr +'</p>'
        }).then(action => {
          if(action == 'confirm'){
            this.$router.push({name: 'bondBid'})
          }
        });
      }
      else{
        if(!evt.target.classList.contains('disabled')){
            this.$router.push({name: 'bondBid'})
        }
      }
    },
    backUrl(){
      if(this.$route.query.from){
        this.$router.push('/invest')
      }else{
        this.$router.go(-1)
      }
    }
  }
}
</script>


<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../../assets/scss/detail.scss';
</style>