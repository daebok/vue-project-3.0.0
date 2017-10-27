<!-- 变现通详情页 by fenglei -->
<template>
  <div class="page">
    <div class="content">
      <!-- S理财产品简介 -->
      <div class="product-profile">
        <div class="projectName">{{ investData.projectName }}</div>
        <div class="invest-tag" v-if="investData.choice == 1 || investData.novice == 1 || investData.additionalRateUseful == 1 ||
        investData.redEnvelopeUseful == 1 || investData.specificSale != 0 || investData.realizeUseful == 1 || investData.specificSale == 2">
          <label v-if="investData.choice == 1" class="tag-remen"></label>
          <label v-if="investData.novice == 1" class="tag-xinshou"></label>
          <label v-if="investData.additionalRateUseful == 1 || investData.redEnvelopeUseful == 1" class="tag-youhui"></label>
          <label v-if="investData.specificSale != 0" class="tag-dingxiang"></label>
          <label v-if="investData.realizeUseful == 1" class="tag-bainxian"></label>
          <label v-if="investData.specificSale == 2" class="tag-vip" :class="'tag-vip' + investData.vipLevel"></label>
        </div>
        <span class="apr-value">{{ investData.apr | currency('', 2) }}<i v-if="investData.addApr < 0">%+{{ investData.addApr }}%</i><i v-else>%</i></span>
        <span class="apr-text">预期年化收益率</span>
        <ul class="two-block">
          <li>
            <label class="right-border">起投金额(元)</label>
            <label>产品期限<i v-if="investData.timeType == 1">(天)</i><i v-else>(月)</i></label>
          </li>
          <li>
            <span class="right-border">{{ investData.lowestAccount }}</span>
            <span>{{ investData.timeLimit }}</span>
          </li>
        </ul>
        <div class="progress">
          <div class="progress-value" :style="'width:'+scales+'%'"></div>
        </div>
        <span class="progress-text">已完成&nbsp;{{ scales }}%&nbsp;&nbsp;剩余可投<i>{{ investData.remainAccount | currency('', 2) }}</i>元</span>
      </div>
      <!-- E理财产品简介 -->
      <!-- S理财产品详情 -->
      <div class="product-details">
        <div class="title">产品详情</div>
        <ul>
          <li>
            <label>产品名称</label>
            <span>{{ investData.projectName }}</span>
          </li>
          <li>
            <label>产品规模</label>
            <span>{{ investData.account | currency('', 0) }}元</span>
          </li>
          <li>
            <label>产品期限</label>
            <span v-if="investData.timeType == 1">{{ investData.timeLimit }}天</span>
            <span v-else>{{ investData.timeLimit }}月</span>
          </li>
          <li>
            <label>收益起始日</label>
            <span>{{ investData.interestStyle }}</span>
          </li>
          <li>
            <label>提取到账</label>
            <span>产品到期日+{{ investData.repayEndDays }}天内</span>
          </li>
          <li>
            <label>起投金额</label>
            <span>{{ investData.lowestAccount | currency('', 0) }}元</span>
          </li>
          <li>
            <label>最高可投</label>
            <span>{{ investData.mostAccount | currency('', 0) }}元</span>
          </li>
          <li>
            <label>收益方式</label>
            <span>{{ investData.repayStyle }}</span>
          </li>
          <li>
            <label>保障措施</label>
            <span>票据作为还款来源</span>
          </li>
          <li>
            <label>产品信息</label>
            <span>变现通是我买网金融推出的基于投资固收理财、票据理财的个人借款服务。 本产品的借款用户为已投资固收理财的用户, 还款来源为借款人持有的固收理财到期兑付资金。
              我买网金融票据理财由银行承兑汇票或商业承兑汇票等安全等级较高的资产做质押或收益权转让，国内知名股份制银行对融资信息进行审核及见证或保险公司承保，
              并委托第三方支付机构进行资金监管及清算服务，保障用户资金安全，到期本息承兑给用户。</span>
          </li>
          <li>
            <label>购买说明</label>
            <span>产品起购金额1元，支持银行卡快捷支付及粮票宝支付。本产品为固定期限类，产品到期日之前不可赎回。到期后0个工作日内，本金及约定年化收益将自动划转至支付时的银行卡/粮票宝中</span>
          </li>
        </ul>
      </div>
      <!-- E理财产品详情 -->
      <!-- S其他信息 -->
      <div class="other-details">
        <ul>
          <li @click="linkToName('investRecord')">
            <span>投资记录</span>
            <img src="../../../assets/images/index/home_arrow_r.png">
          </li>
        </ul>
      </div>
      <!-- E其他信息 -->
    </div>
    <!-- S底部投资状态按钮 -->
    <div v-if="countTimeSale != ''" class="invest-btn-countDown">
      <div class="count-down-time">{{ countTimeSale }}</div>
      <div class="count-down-remind" :class="{ 'disabledBg': investData.bespeak == 1 }" @click="investBespeak">
        <div class="alarm-clock">
          <img src="../../../assets/images/finance/details_icon_reserve.png" />
        </div>
        <div class="remind">
          <p class="remind-title">{{ investData.bespeak ? "已预约" : "开售提醒" }}</p>
          <p class="remind-number">{{ investData.bespeaNum }}人想买</p>
        </div>
      </div>
    </div>
    <div v-else class="investment-status" @click="linkTo" :class="{ disabledBg: disabledStatus }">
      <label class="status-text">{{ buttonText }}</label>
    </div>
    <!-- E底部投资状态按钮 -->
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../../ajax.config'; // 引入所有接口地址
  import countDown from '../../../mixins/countTime'; // 未开售时间倒计时
  import qs from 'qs';

  export default {
    name: 'investDetail',
    data() {
      return {
        investData: '', // 理财详情数据
        params: {
          projectId: this.$route.params.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        disabledStatus: false, // 部投资状态按钮禁用状态
        routeFromUrl: sessionStorage.fromName, // 上一页面路由地址
        buttonText: '', // 底部投资状态按钮文字内容
        buttonUrlName: '', // 底部投资状态按钮跳转链接
        buttonUrlQuery: { // 底部投资状态按钮跳转链接附带参数
          from: 'investDetail',
          id: this.$route.params.projectId
        },
        messageBoxContent: '', // 提示弹窗提示文字内容
        messageBoxConfirmText: '', // 提示弹窗确认按钮文字
        messageBoxCancelUrl: '', // 提示弹窗取消按钮跳转链接
        countTimeSale: '' // 倒计时
      };
    },
    computed: {
      scales() {
        var scales = parseInt((this.investData.account - this.investData.remainAccount) * 100 / this.investData.account);
        if(scales == 100) scales = parseInt(scales);
        return scales;
      }
    },
    created() {
      // 理财详情数据初始化
      this.$http.get(ajaxUrl.realizeDetailAjax, { params: this.params }).then((res) => {
        if (res.data.resData) {
          this.investData = res.data.resData;
          this.investmentStatus();
        }
      });
    },
    methods: {
      // 项目及按钮状态判断
      investmentStatus() {
        let data = this.investData;
        if (data.clickCode == 1) { // 未开通托管账户
          this.buttonText = '去开通托管账户';
          this.buttonUrlName = 'realname';
          this.messageBoxConfirmText = '去开通';
          this.messageBoxContent = '您尚未开通资金账户，为了顺利投资请先开通资金账户？';
          this.messageBoxShow();
        } else if (data.clickCode == 14) { // 未绑卡
          this.buttonText = '去绑卡';
          this.buttonUrlName = 'addBank';
        } else if (data.clickCode == 13) { // 未设置支付密码
          this.buttonText = '去设置支付密码';
          this.buttonUrlName = '??'; // 暂无设置支付密码页面，name待定
        } else if (data.ifSale == 0) { // 预售中
          if (data.saleEndTime) {
            this.countDown(data.saleEndTime, data.timeNow);
          }
          if (data.saleTime > data.timeNow) {
            this.countDownSale(data.saleTime,data.timeNow);
          }
        } else if(data.clickCode == 12) {  // VIP等级不够
          this.disabledStatus = true;
          this.buttonText = data.clickTitle;
          this.$messagebox({
            title: ' ',
            confirmButtonText: '确认',
            showCancelButton: false,
            message: '抱歉，仅VIP' + data.vipLevel + '及以上用户可投资，你所在的等级无法投资该产品'
          });
        } else if (data.clickCode == 2) { // 未评测风险承受能力
          this.buttonText = '去评测风险承受能力';
          this.messageBoxConfirmText = '去评测';
          this.buttonUrlName = 'riskTips';
          this.messageBoxContent = '首次投资请您完成风险承受能力评测！';
          this.messageBoxShow();
        } else if(data.clickCode == 3) {  // 邮箱未绑定
          this.buttonText = '去绑定邮箱';
          this.messageBoxConfirmText = '去绑定';
          this.buttonUrlName = 'bindEmail';
          this.messageBoxContent = '该产品为定向邮箱投资，请绑定邮箱查看是否有投资权限！';
          this.messageBoxShow();
        } else { // 其他情况按接口所传数据显示
          if (data.isClick == 0) {
            this.disabledStatus = true;
          }
          this.buttonText = data.clickTitle;
        }
      },
      // 提示弹窗
      messageBoxShow() {
        this.$messagebox({
          title: ' ',
          confirmButtonText: this.messageBoxConfirmText,
          showCancelButton: true,
          message: this.messageBoxContent
        }).then(action => {
          if (action == 'confirm') {
            this.linkToName(this.buttonUrlName);
          }
        });
      },
      // 底部投资状态按钮页面跳转
      linkTo() {
        if (this.disabledStatus == false) {
          let data = this.investData;
          if (data.clickCode == 4) { // 投资功能已锁定
            this.$messagebox({
              title: ' ',
              confirmButtonText: '联系客服',
              showCancelButton: true,
              message: '您的投资功能被锁定，请联系客服解锁！'
            }).then(action => {
              if (action == 'confirm') {
                location.href='tel:400-820-8820';
              }
            });
          } else if (data.isTipe == 1) { // 超过用户安全等级
            this.$messagebox({
              title: ' ',
              confirmButtonText: '继续投资',
              showCancelButton: true,
              message: data.userLevelTitle + '本人已充分认识并愿意承担本项目可能存在的风险，同意继续投资。'
            }).then(action => {
              if (action == 'confirm') {
                if (data.clickCode == 7) { // 定向密码
                  this.checkPwd();
                } else {
                  this.buttonUrlName = 'investBid';
                  this.buttonUrlQuery = {prevPage: 'realizeDetail'}
                  this.linkToName(this.buttonUrlName);
                }
              }
            });
          } else if (data.clickCode == 7) { // 定向密码
            this.checkPwd();
          } else if (data.clickCode == 10) { // 立即投资
            this.buttonUrlName = 'investBid';
            this.buttonUrlQuery = {prevPage: 'realizeDetail'}
            this.linkToName(this.buttonUrlName);
          }
        }
      },
      // 开售提醒
      investBespeak() {
        this.$http.get(ajaxUrl.investBespeakAjax, { params: this.params }).then((res) => {
          this.$http.get(ajaxUrl.projectDetailAjax, { params: this.params }).then((resDe) => {
            var res_sec = resDe.data.resData;
            var fiveMin = res_sec.saleTime - res_sec.timeNow;
            this.investData.bespeaNum = res_sec.bespeaNum;  // 刷新想购买的人数
            this.investData.bespeak = res_sec.bespeak;  // 刷新想购买的人数
            if (this.clickCode == 2) { // 未评测风险承受能力
              this.messageBoxConfirmText = '去评测';
              this.buttonUrlName = 'riskTips';
              if (fiveMin > 5 * 60 * 1000) {  // 判断开售时间是否大于5分钟
                this.messageBoxContent = '已设置开售提醒，产品开售前5分钟提醒您前来投资！为了您能顺利投资，请及时评测风险承受能力！';
              }else{
                this.messageBoxContent = '该产品即将开售，为了您能顺利投资，请及时评测风险承受能力！';
              }
              this.messageBoxShow();
            } else if(this.clickCode == 3) {  // 邮箱未绑定
              this.messageBoxConfirmText = '去绑定';
              this.buttonUrlName = 'bindEmail';
              if (fiveMin > 5 * 60 * 1000) {  //判断开售时间是否大于5分钟
                this.messageBoxContent = '已设置开售提醒，产品开售前5分钟提醒您前来投资！为了您能顺利投资，请绑定邮箱查看是否有投资权限！';
              } else {
                this.messageBoxContent = '该产品即将开售，为了您能顺利投资，请绑定邮箱查看是否有投资权限！';
              }
            } else {
              this.$toast(res.data.resMsg);
            }
          })
        })
      },
      // 校验定向密码
      checkPwd() {
        this.$messagebox.prompt(' ', {
          title: '请输入定向密码',
          inputType: 'password',
          inputErrorMessage: '请输入仅为数字的6位密码',
          inputValidator: function(val) {
            if(!val) return true;
            return val && val.search(/^([0-9]\d{0,5})?$/) != -1;
          }
        }).then(({ value, action }) => {
          if (action == 'confirm') {
            if (!value) {
              this.$toast('请输入定向密码');
              return false;
            }
            let params = Object.assign(this.params,{ pwd: value });
            this.$http.post(ajaxUrl.checkPwdAjax,qs.stringify(params)).then((res) => {
              if (res.data.resMsg == '校验成功！') {
                this.buttonUrlName = 'investBid';
                this.buttonUrlQuery = {prevPage: 'realizeDetail'}
                this.linkToName(this.buttonUrlName);
              } else {
                this.$toast(res.data.resMsg);
              }
            })
          }
        });
      },
      // 跳转页面
      linkToName(name) {
        this.$router.push({ name: name, query: this.buttonUrlQuery });
      }
    },
    filters: {
      // 去html标签过滤器
      htmlTagsNo(value) {
        if (!value) return '';
        value = value.replace(/<[^>]+>/g,'');
        return value;
      }
    },
    mixins: [ countDown ]
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "../../../assets/sass/invest_detail.sass";
</style>
