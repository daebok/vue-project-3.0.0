<template>
  <div class="page">
    <div class="content">
    <mt-header class="bar-nav aui-border-b" title="投资详情" >
      <mt-button slot="left" icon="back" @click.native="backUrl('/invest')"></mt-button>
    </mt-header>
      <div class="clearfix">
    <div class="project-detail">
      <div v-if="resdata.projectName" class="project-name">{{ resdata.projectName }}</div>
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
        <span v-if="resdata.addApr > 0" class="project-award main-color font-arial">+{{ resdata.addApr | currency('',2) }}%</span>
      </div>
      <div class="project-detail-1">
        <span v-if="resdata.saleStyle == 2" class="start-money">起投份数 {{ resdata.lowestCopies }} 份</span>
        <span v-else class="start-money">起投金额 {{ resdata.lowestAccount }} 元</span>
        <span v-if="resdata.timeType == 0" class="project-timelimit">产品期限 {{ resdata.timeLimit }} 个月</span>
        <span v-if="resdata.timeType == 1" class="project-timelimit">产品期限 {{ resdata.timeLimit }} 天</span>
      </div>
      <div class="percent-wra">
        <div class="percent-process" :style="'width:'+scales+'%'"></div>
      </div>
      <div class="project-detail-2">
        <span class="info-left">已完成<i class="font-arial">{{ scales }}%</i></span>
        <span v-if="resdata.saleStyle == 2" class="info-right">剩余可投<i class="balance-account main-color">{{ resdata.totalCopies}}</i>份
        </span>
        <span v-else class="info-right">剩余可投<i class="balance-account main-color">{{ resdata.remainAccount | currency('',2)}}</i>元
        </span>
      </div>
    </div>
    </div>
    <div class="clearfix">
    <ul class="project-list margin-t-10">
      <li v-if="resdata.repayStyle">
        <img src="../../../assets/images/finance/details_icon_way_of_repayment.png" />
        <span>{{ resdata.repayStyle }}</span>
      </li>
      <li>
        <img src="../../../assets/images/finance/details_icon_sum.png" />
        <span>产品金额<i class="bond-money">{{ resdata.account | currency('',2)}}</i>元</span>
      </li>
      <li v-if="resdata.saleStyle == 2">
        <img src="../../../assets/images/finance/details_icon_ji.png" />
        <span>每份金额{{ resdata.copyAccount }}元</span>
      </li>
      <li v-if="resdata.interestStyle">
        <img src="../../../assets/images/finance/details_icon_ji.png" />
        <span>{{ resdata.interestStyle }}</span>
      </li>
      <li v-if="resdata.riskLevel">
        <img src="../../../assets/images/finance/details_icon_security_level.png" />
        <span>{{ resdata.riskLevel }}</span>
      </li>
      <li v-if="resdata.tenderCondition">
        <img src="../../../assets/images/finance/details_icon_condition.png" />
        <span>{{ resdata.tenderCondition }}</span>
      </li>
      <li v-if="resdata.ifSaleStatus == 1 && countTime">
        <img src="../../../assets/images/finance/details_icon_time_left.png" />
        <span>剩余时间 {{ countTime }}</span>
      </li>
    </ul>
    </div>
    <div class="clearfix">
    <ul class="project-detail-3 margin-t-10">
      <li class="border-b-e2 aui-border-b">
        <router-link :to="'/investDetail/'+$route.params.projectId+'/investInfo?borrowFlag='+resdata.borrowFlag+'&oldProjectId='+resdata.oldProjectId">产品详情
          <img src="../../../assets/images/public/arrow_right.png" class="arrow-right"/>
        </router-link>
      </li>
      <li class="border-b-d4 aui-border-b">
        <router-link :to="{name: 'investRecord'}">投资记录
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

    <div v-if="resdata.ifSale == 0 && !(clickCode == 0 || clickCode == 1 || clickCode == 5 || clickCode == 6 || clickCode == 8 || clickCode == 9 || clickCode == 11)" class="invest-btn-countDown">
      <div class="count-down-time">{{ countTimeSale }}</div>
      <div class="count-down-remind" :class="{'disabled':bespeak == 1}" id="remindBox" @click="investBespeak">
        <div class="alarm-clock">
          <img src="../../../assets/images/finance/details_icon_reserve.png" />
        </div>
        <div class="remind">
          <p class="remind-title">{{bespeak?"已预约" : "开售提醒"}}</p>
          <p class="remind-number">{{ bespeaNum }}人想买</p>
        </div>
      </div>
    </div>
    <div v-else @click="investBid" class="invest-btn" :class="{disabled: resdata.isClick == 0}">{{ resdata.clickTitle }}
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import countDown from '../../../mixins/countTime'
  import backUrl from '../../../mixins/backUrl'
  import qs from 'qs'
export default {
  name: 'investDetail',
  data(){
    return {
      resdata: '',
      clickCode: 0,
      countTime: '',
      countTimeSale: '', // 小于3小时倒计时
      bespeaNum: '', //多少人想买
      bespeak: 0,
      params: {
        projectId: this.$route.params.projectId,
        userId: this.$store.state.user.userId,
        __sid: this.$store.state.user.__sid
      }
    }
  },
  computed: {
    scales(){
      var scales = parseInt((this.resdata.account - this.resdata.remainAccount) * 100/this.resdata.account)
      if(scales == 100) scales = parseInt(scales)
      return scales
    }
  },
  created(){
    this.alertMsgbox = {
      btnTxt: '', // 确定按钮文字
      msg: '', // 弹框信息
      pathName: '' // 点击确定跳转
    }
    this._init()
  },
  mixins:[countDown,backUrl],
  methods: {
    alertMsgBox(confirmButtonText, message, fn) {
      this.$messagebox({
        title: ' ',
        confirmButtonText: confirmButtonText,
        showCancelButton: true,
        // message: '为了您的资金安全，请您开通第三方资金托管账户！'
         message: message
      }).then(fn);
    },
    _init() {
      this.$http.get(ajaxUrl.projectDetail, {params:this.params}).then((res) => {
        this.resdata = res.data.resData
        this.clickCode = res.data.resData.clickCode
        this.bespeaNum = res.data.resData.bespeaNum
        this.bespeak = res.data.resData.bespeak
        if(this.resdata.saleEndTime){
          this.countDown(this.resdata.saleEndTime,this.resdata.timeNow)
        }
        if(this.resdata.saleTime > this.resdata.timeNow){ // 预售
          this.countDownSale(this.resdata.saleTime,this.resdata.timeNow)
        }
        if(this.clickCode == 1){
          this.alertMsgbox.btnTxt = '去开通'
          this.alertMsgbox.msg = '为了您的资金安全，请您开通第三方资金托管账户！'
          this.alertMsgbox.pathName = 'realname'
        }else if(this.clickCode == 2 && this.resdata.ifSale == 1) {
          this.alertMsgbox.btnTxt = '去评测'
          this.alertMsgbox.msg = '首次投资请您完成风险承受能力评测！'
          this.alertMsgbox.pathName = 'riskTips'
        }else if(this.clickCode == 3 && this.resdata.ifSale == 1) {
          this.alertMsgbox.btnTxt = '去绑定'
          this.alertMsgbox.msg = '该产品为定向邮箱投资，请绑定邮箱查看是否有投资权限！'
          this.alertMsgbox.pathName = 'bindEmail'
        }
        if(!this.alertMsgbox.btnTxt) return
        this.alertMsgBox(this.alertMsgbox.btnTxt, this.alertMsgbox.msg, action => {
          if(action == 'confirm'){
            this.$router.push({name: this.alertMsgbox.pathName,query:{from:'investDetail',id: this.$route.params.projectId}})
          }
        })
      })
    },
    investBid(evt){
      if(this.resdata.clickTitle == '去设置支付密码'){
        window.location.href = 'https://onlineuat.cupdata.com:50005/dbesbsit/app/mobile/settingsN.html';
      }else{
        if(this.clickCode == 1){  // 托管账户
          this.$router.push({name: 'realname', query:{from:'investDetail',id: this.$route.params.projectId}})
        }
        else if(this.clickCode == 2){ // 风险测评
          this.$router.push({name: 'riskTips', query:{from:'investDetail',id: this.$route.params.projectId}})
        }
        else if(this.clickCode == 3){  //绑定邮箱
          this.$router.push({name: 'bindEmail', query:{from:'investDetail',id: this.$route.params.projectId}})
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
              if(this.clickCode == 7) {  //定向密码
                this.$messagebox.prompt(' ', {
                  title: '请输入定向密码',
                  inputType: 'password',
                  inputErrorMessage: '请输入仅为数字的6位密码',
                  inputValidator: function(val){
                    return val && val.search(/^([0-9]\d{0,5})?$/) != -1
                  }
                }).then(({ value, action }) => {
                  if(action == 'confirm'){
                    if(!value){
                      this.$toast('请输入定向密码')
                      return ;
                    }
                    let params = Object.assign(this.params,{pwd: value})
                    this.$http.post(ajaxUrl.checkPwd,qs.stringify(params)).then((res) => {
                      if(res.data.resMsg == '校验成功！'){
                        this.$router.push({name: 'investBid'})
                      }else{
                        this.$toast(res.data.resMsg)
                      }
                    })
                  }
                });
                return;
              }
              this.$router.push({name: 'investBid'})
            }
          });
        }else if(this.clickCode == 7) {  //定向密码
          this.$messagebox.prompt(' ', {
            title: '请输入定向密码',
            inputType: 'password',
            inputErrorMessage: '请输入仅为数字的6位密码',
            inputValidator: function(val){
              if(!val) return true;
              return val && val.search(/^([0-9]\d{0,5})?$/) != -1
            }
          }).then(({ value, action }) => {
            if(action == 'confirm'){
              if(!value){
                this.$toast('请输入定向密码')
                return ;
              }
              let params = Object.assign(this.params,{pwd: value})
              this.$http.post(ajaxUrl.checkPwd,qs.stringify(params)).then((res) => {
                if(res.data.resMsg == '校验成功！'){
                  this.$router.push({name: 'investBid'})
                }else{
                  this.$toast(res.data.resMsg)
                }
              })
            }
          });
        }else{
          if(!evt.target.classList.contains('disabled')){
              this.$router.push({name: 'investBid'})
          }
        }
      }
    },
    investBespeak(){
      var remindBox = document.getElementById('remindBox')
      remindBox.classList.add = 'disabled'
      if(remindBox.classList.contains('disabled')){
        return ;
      }
      this.$http.get(ajaxUrl.investBespeak, {params:this.params}).then((res) => {
        this.$http.get(ajaxUrl.projectDetail, {params:this.params}).then((resDe) => {
          var res_sec = resDe.data.resData
          var fiveMin = res_sec.saleTime - res_sec.timeNow
          this.bespeaNum = res_sec.bespeaNum  // 刷新想购买的人数
          this.bespeak = res_sec.bespeak  // 刷新想购买的人数
          if(this.clickCode == 2) {
            if(fiveMin > 5*60*1000){  //判断是否大于5分钟
              this.$messagebox({
                title: ' ',
                confirmButtonText: '去评测',
                showCancelButton: true,
                message: '已设置开售提醒，产品开售前5分钟提醒您前来投资！为了您能顺利投资，请及时评测风险承受能力！'
              }).then(action => {
                if(action == 'confirm'){
                  this.$router.push({name: 'riskTips',query:{from:'investDetail',id: this.$route.params.projectId}})
                }
              });
            }else{
              this.$messagebox({
                title: ' ',
                confirmButtonText: '去评测',
                showCancelButton: true,
                message: '该产品即将开售，为了您能顺利投资，请及时评测风险承受能力！'
              }).then(action => {
                if(action == 'confirm'){
                  this.$router.push({name: 'riskTips',query:{from:'investDetail',id: this.$route.params.projectId}})
                }
              });
            }
          }else if(this.clickCode == 3){  // 邮箱未绑定
            if(fiveMin > 5*60*1000){  //判断是否大于5分钟
              this.$messagebox({
                title: ' ',
                confirmButtonText: '去绑定',
                showCancelButton: true,
                message: '已设置开售提醒，产品开售前5分钟提醒您前来投资！为了您能顺利投资，请绑定邮箱查看是否有投资权限！'
              }).then(action => {
                if(action == 'confirm'){
                  this.$router.push({name: 'bindEmail',query:{from:'investDetail',id: this.$route.params.projectId}})
                }
              });
            }else{
              this.$messagebox({
                title: ' ',
                confirmButtonText: '去绑定',
                showCancelButton: true,
                message: '该产品即将开售，为了您能顺利投资，请绑定邮箱查看是否有投资权限！'
              }).then(action => {
                if(action == 'confirm'){
                  this.$router.push({name: 'bindEmail',query:{from:'investDetail',id: this.$route.params.projectId}})
                }
              });
            }
          }else{
            this.$toast(res.data.resMsg)
          }
        })
      })
    }
  }
}
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../../assets/scss/detail.scss';

</style>
