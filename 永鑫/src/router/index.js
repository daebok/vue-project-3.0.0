import Vue from 'vue'
import Router from 'vue-router'
import Index from '../views/index.vue'

Vue.use(Router);

// 防止刷新时候判断登录是否过期使用组件路由钩子---解决接口异常报错
function requireAuth(to, from, next) {
  //document.title = to.meta.title || document.title; // 修改页面head的内容跟随路由变化
  if (localStorage.user) {
    next();
  } else {
    next({
      name: 'login',
      query: { redirect: to.fullPath }
    });
  }
}

//title在main里面设置后才能生效
export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      //component: Index,
      component: resolve => { require(['../views/invest/invest.vue'], resolve) },
      meta: { title: '产品列表' }
    },
    {
      path: '/index',
      name: 'Index2',
      component: Index,
      meta: { title: '永鑫保险' }
    },
    { // 粮票宝
      path: '/fund',
      name: 'fund',
      component: resolve => { require(['../views/fund/fund.vue'], resolve) },

      meta: { requiresAuth: true, title: '粮票宝' }
    },
    { // 粮票宝账单
      path: '/fund/list',
      name: 'fundList',
      component: resolve => { require(['../views/fund/fund_list.vue'], resolve) },

      meta: { requiresAuth: true, title: '账单' }
    },
    { // 转入转出结果
      path: '/fund/msg',
      name: 'fundMsg',
      component: resolve => { require(['../views/fund/fund_msg.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 转入
      path: '/fund/in',
      name: 'fundIn',
      component: resolve => { require(['../views/fund/fund_in.vue'], resolve) },

      meta: { requiresAuth: true, title: '转入' }
    },
    { // 转出
      path: '/fund/out',
      name: 'fundOut',
      component: resolve => { require(['../views/fund/fund_out.vue'], resolve) },

      meta: { requiresAuth: true, title: '转出' }
    },
    { // 转出规则
      path: '/fund/out/rule',
      name: 'fundOutRule',
      component: resolve => { require(['../views/fund/fund_out_rule.vue'], resolve) },
      meta: { title: '转出规则' }
    },
    { // 投资列表
      path: '/invest',
      name: 'invest',
      component: resolve => { require(['../views/invest/invest.vue'], resolve) },
      meta: { title: '产品列表' }
    },
    { // 领券中心
      path: '/couponCenter',
      name: 'couponCenter',
      component: resolve => { require(['../views/invest/coupon_center.vue'], resolve) },
      meta: { title: '领券中心' }
    },
    { // 我的优惠券
      path: '/myCoupon',
      name: 'myCoupon',
      component: resolve => { require(['../views/invest/my_coupon.vue'], resolve) },
      meta: { title: '我的优惠券' }
    },
    { // 投资详情
      path: '/investDetail/:projectId&:openId',
      name: 'investDetail',
      component: resolve => { require(['../views/invest/detail/invest_detail.vue'], resolve) },

      meta: {title: '产品详情' }
    },
    { // 项目了解
      path: '/investDetail/:projectId/investInfo',
      name: 'investInfo',
      component: resolve => { require(['../views/invest/detail/invest_info.vue'], resolve) },
      meta: {title: '产品详情' }
    },
    { // 变现的了解项目
      path: '/realizeDetail/:projectId/realizeInfo',
      name: 'realizeInfo',
      component: resolve => { require(['../views/invest/detail/realize_info.vue'], resolve) },
      meta: { requiresAuth: true }
    },
    { // 交易规则
      path: '/investDetail/:projectId/investRule',
      name: 'investRule',
      component: resolve => { require(['../views/invest/detail/invest_rule.vue'], resolve) },

      meta: {title: '交易规则' }
    },
    { // 基金经理
      path: '/investDetail/:projectId/fundManager',
      name: 'fundManager',
      component: resolve => { require(['../views/invest/detail/fund_manager.vue'], resolve) },
      meta: {title: '基金经理' }
    },
    { // 投资记录
      path: '/investDetail/:projectId/investRecord',
      name: 'investRecord',
      component: resolve => { require(['../views/invest/detail/invest_record.vue'], resolve) },

      meta: { requiresAuth: true, title: '投资记录' }
    },
    { // 债权详情
      path: '/bondDetail/:projectId',
      name: 'bondDetail',
      component: resolve => { require(['../views/invest/detail/bond_detail.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 变现详情
      path: '/realizeDetail/:projectId',
      name: 'realizeDetail',
      component: resolve => { require(['../views/invest/detail/realize_detail.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 债权投标
      path: '/bondDetail/:projectId/bondBid',
      name: 'bondBid',
      component: resolve => { require(['../views/invest/bid/bond_bid.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 投标
      path: '/investDetail/:projectId/investBid',
      name: 'investBid',
      component: resolve => { require(['../views/invest/bid/invest_bid.vue'], resolve) },

      meta: { requiresAuth: true, title: '投资' }
    },
    { // 我的理财进来完成支付
      path: '/myInvest/:projectId/myInvestBid',
      name: 'myInvestBid',
      component: resolve => { require(['../views/invest/bid/myInvest_bid.vue'], resolve) },

      meta: { requiresAuth: true, title: '支付' }
    },
    { // 红包
      path: '/investDetail/:projectId/investBid/redpacket',
      name: 'investRedpacket',
      component: resolve => { require(['../views/invest/bid/redpacket.vue'], resolve) },

      meta: { requiresAuth: true, title: '红包' }
    },
    { // 加息券
      path: '/investDetail/:projectId/investBid/coupon',
      name: 'investCoupon',
      component: resolve => { require(['../views/invest/bid/coupon.vue'], resolve) },

      meta: { requiresAuth: true, title: '加息券' }
    },
    { // 投资结果
      path: '/tender_result',
      name: 'tenderResult',
      component: resolve => { require(['../views/invest/bid/tender_result.vue'], resolve) },
      meta: { title: '购买结果' }
    },
    { // 账户中心
      path: '/account',
      name: 'account',
      component: resolve => { require(['../views/account/account.vue'], resolve) },

      meta: { requiresAuth: true, title: '资产' }
    },
    { // 消息
      path: '/account/message',
      name: 'message',
      component: resolve => { require(['../views/account/message.vue'], resolve) },

      meta: { requiresAuth: true,title: '消息' }
    },
    { // 充值
      path: '/account/recharge',
      name: 'recharge',
      component: resolve => { require(['../views/account/recharge.vue'], resolve) },

      meta: { requiresAuth: true,title: '充值' }
    },
    { // 支持银行说明
      path: '/account/recharge/support_bank',
      name: 'supportBank',
      component: resolve => { require(['../views/account/support_bank.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 提现
      path: '/account/withdraw',
      name: 'withdraw',
      component: resolve => { require(['../views/account/withdraw.vue'], resolve) },

      meta: { requiresAuth: true, title: '提现' }
    },
    { // 充值、提现记录
      path: '/account/cash_record',
      name: 'cashRecord',
      component: resolve => { require(['../views/account/cash_record.vue'], resolve) },

      meta: { requiresAuth: true, title: '记录' }
    },
    { // 永鑫交易记录
      path: '/account/transaction_record/:fundType&:prodId&:openId',
      name: 'transactionRecord',
      component: resolve => { require(['../views/account/transaction_record.vue'], resolve) },
      meta: { title: '交易记录' }
    },
    { // 资金明细
      path: '/account/logs',
      name: 'logs',
      component: resolve => { require(['../views/account/logs.vue'], resolve) },

      meta: { requiresAuth: true, title: '交易记录' }
    },
    { // 我的持仓旧版
      path: '/account/myCurrent',
      name: 'myCurrent',
      component: resolve => { require(['../views/account/myInvest/my_invest.vue'], resolve) },
      meta: {title: '我的持仓' }
    },

    { // 我的持仓新版
      path: '/account/myInvest',
      name: 'myInvest',
      component: resolve => { require(['../views/account/myInvest/my_current.vue'], resolve) },
      meta: {title: '我的持仓' }
    },

    { // 回款计划
      path: '/account/myInvest/repayment_plan',
      name: 'repaymentPlan',
      component: resolve => { require(['../views/account/myInvest/repayment_plan.vue'], resolve) },

      meta: { requiresAuth: true,title: '回款计划' }
    },
    { // 债权转让
      path: '/account/bond/bond',
      name: 'bond',
      component: resolve => { require(['../views/account/bond/bond.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 债权转让交易
      path: '/account/bond/bond_deal',
      name: 'bondDeal',
      component: resolve => { require(['../views/account/bond/bond_deal.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 变现
      path: '/account/money_logs',
      name: 'moneyLogs',
      component: resolve => { require(['../views/account/money_logs.vue'], resolve) },

      meta: { requiresAuth: true, title: '变现' }
    },
    { // 变现记录
      path: '/account/money_cash/:id',
      name: 'moneyCash',
      component: resolve => { require(['../views/account/money_cash.vue'], resolve) },

      meta: { requiresAuth: true, title: '变现设置' }
    },
    { // 我的借款
      path: '/account/my_loan',
      name: 'myLoan',
      component: resolve => { require(['../views/account/my_loan.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 立即还款
      path: '/account/refund/:projectId/:period',
      name: 'refund',
      component: resolve => { require(['../views/account/refund.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 还款计划
      path: '/account/refund_plan',
      name: 'refundPlan',
      component: resolve => { require(['../views/account/refund_plan.vue'], resolve) },

      meta: { requiresAuth: true,title: '还款计划' }
    },
    { // 自动投标
      path: '/account/auto',
      name: 'auto',
      component: resolve => { require(['../views/account/auto/auto.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 自动投标参数设置
      path: '/account/auto/setting',
      name: 'autoSetting',
      component: resolve => { require(['../views/account/auto/auto_setting.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 自动投标参数修改
      path: '/account/auto/modify',
      name: 'autoModify',
      component: resolve => { require(['../views/account/auto/auto_modify.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    {  // 预约借款
      path: '/account/appoint_loan',
      name: 'appointLoan',
      component: resolve => { require(['../views/account/appoint_loan.vue'], resolve) }
    },
    {  // 我的优惠
      path: '/account/my_coupon',
      name: 'myCoupon',
      component: resolve => { require(['../views/account/my_coupon.vue'], resolve) },
      //meta: { requiresAuth: true }
    },
    { // 我的
      path: '/mine',
      name: 'mine',
      component: resolve => { require(['../views/mine/mine.vue'], resolve) },
      meta: { title: '个人中心' }
    },
    { // 账户与安全
      path: '/mine/safe',
      name: 'safe',
      component: resolve => { require(['../views/mine/safe/settings.vue'], resolve) },

      meta: { requiresAuth: true, title: '账户与安全' }
    },
    { // 我的等级
      path: '/mine/safe/vip',
      name: 'vip',
      component: resolve => { require(['../views/mine/safe/vip.vue'], resolve) },

      meta: { requiresAuth: true, title: '我的等级' }
    },
    { // 绑定手机
      path: '/mine/safe/bind_phone',
      name: 'bindPhone',
      component: resolve => { require(['../views/mine/safe/bind_phone.vue'], resolve) },

      meta: { requiresAuth: true, title: '绑定手机' }
    },
    { // 修改手机绑定
      path: '/mine/safe/modify_phone',
      name: 'modifyPhone',
      component: resolve => { require(['../views/mine/safe/modify_phone.vue'], resolve) },

      meta: { requiresAuth: true, title: '绑定手机' }
    },
    { // 绑定邮箱
      path: '/mine/safe/bind_email',
      name: 'bindEmail',
      component: resolve => { require(['../views/mine/safe/bind_email.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 修改邮箱绑定
      path: '/mine/safe/modify_email',
      name: 'modifyEmail',
      component: resolve => { require(['../views/mine/safe/modify_email.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 托管账户、实名认证
      path: '/mine/safe/realname',
      name: 'realname',
      component: resolve => { require(['../views/mine/safe/realname.vue'], resolve) },

      meta: { requiresAuth: true, title: '实名认证' }
    },
    { // 身份证认证
      path: '/mine/safe/idNo',
      name: 'idNo',
      component: resolve => { require(['../views/mine/safe/idNo.vue'], resolve) },

      meta: { requiresAuth: true, title: '身份证认证' }
    },
    { // 身份证认证成功
      path: '/mine/safe/idNo/success',
      name: 'idNoSuccess',
      component: resolve => { require(['../views/mine/safe/idNo_success.vue'], resolve) },

      meta: { requiresAuth: true, title: '身份证认证成功' }
    },
    { // 银行卡
      path: '/mine/safe/bank',
      name: 'bank',
      component: resolve => { require(['../views/mine/safe/bank.vue'], resolve) },

      meta: { requiresAuth: true, title: '银行卡' }
    },
    { // 添加银行卡
      path: '/mine/safe/addBank',
      name: 'addBank',
      component: resolve => { require(['../views/mine/safe/addBank.vue'], resolve) },

      meta: { requiresAuth: true, title: '添加银行卡' }
    },
    { // 更换银行卡
      path: '/mine/safe/modifyBank',
      name: 'modifyBank',
      component: resolve => { require(['../views/mine/safe/modifyBank.vue'], resolve) },

      meta: { requiresAuth: true, title: '更换银行卡' }
    },
    { // 添加银行卡 结果页面
      path: '/mine/safe/addBank_result',
      name: 'addBankResult',
      component: resolve => { require(['../views/mine/safe/addBank_result.vue'], resolve) },

      meta: { requiresAuth: true }
    },
    { // 找回支付密码
      path: '/mine/safe/modifyPwd',
      name: 'modifyPwd',
      component: resolve => { require(['../views/mine/safe/modify_pwd.vue'], resolve) },

      meta: { requiresAuth: true, title: '找回支付密码' }
    },
    { // 修改支付密码
      path: '/mine/safe/setPwd',
      name: 'setPwd',
      component: resolve => { require(['../views/mine/safe/setPwd.vue'], resolve) },

      meta: { requiresAuth: true, title: '修改支付密码' }
    },
    { // 风险测评提示
      path: '/mine/safe/risk_tips',
      name: 'riskTips',
      component: resolve => { require(['../views/mine/safe/risk_tips.vue'], resolve) },

      meta: { requiresAuth: true, title: '风险测评提示' }
    },
    { // 风险测评
      path: '/mine/safe/risk_rating',
      name: 'riskRating',
      component: resolve => { require(['../views/mine/safe/risk_rating.vue'], resolve) },

      meta: { requiresAuth: true, title: '风险测评' }
    },
    { // 测评完成
      path: '/mine/safe/risk_result',
      name: 'riskResult',
      component: resolve => { require(['../views/mine/safe/risk_result.vue'], resolve) },

      meta: { requiresAuth: true, title: '测评结果' }
    },
    { // 平台公告
      path: '/mine/siteIntro',
      name: 'siteIntro',
      component: resolve => { require(['../views/mine/siteIntro.vue'], resolve) },
      meta: { title: '平台公告' }
    },
    { // 平台公告详情
      path: '/mine/siteIntro/detail/:uuid',
      name: 'siteIntroDetail',
      component: resolve => { require(['../views/mine/siteIntroDetail.vue'], resolve) },
      meta: { title: '平台详情' }
    },
    { // 动态资讯
      path: '/mine/column',
      name: 'column',
      component: resolve => { require(['../views/mine/column.vue'], resolve) },

      meta: { title: '动态资讯' }
    },
    { // 动态资讯详情
      path: '/mine/column/detail/:uuid',
      name: 'columnDetail',
      component: resolve => { require(['../views/mine/columnDetail.vue'], resolve) },

      meta: { title: '资讯详情' }
    },
    { // 邀请好友
      path: '/mine/invite',
      name: 'invite',
      component: resolve => { require(['../views/mine/invite.vue'], resolve) },

      meta: { requiresAuth: true, title: '邀请好友' }
    },
    { // 奖励规则
      path: '/mine/invite/award_rule',
      name: 'awardRule',
      component: resolve => { require(['../views/mine/award_rule.vue'], resolve) }
    },
    { // 我的邀请记录
      path: '/mine/invite/myInvite_log',
      name: 'myInviteLog',
      component: resolve => { require(['../views/mine/myInvite_log.vue'], resolve) },
      meta: { requiresAuth: true, title: '我的邀请' }
    },
    { // 平台数据
      path: '/mine/siteData',
      name: 'siteData',
      component: resolve => { require(['../views/mine/siteData.vue'], resolve) }
    },
    { // 帮助中心
      path: '/mine/help',
      name: 'help',
      component: resolve => { require(['../views/mine/help.vue'], resolve) },
      meta: { title: '帮助中心' }
    },
    { // 帮助中心栏目
      path: '/mine/help/help_column',
      name: 'helpColumn',
      component: resolve => { require(['../views/mine/help_column.vue'], resolve) },
      meta: { title: '帮助中心' }
    },
    { // 帮助中心详情页
      path: '/mine/help/help_details',
      name: 'helpDetails',
      component: resolve => { require(['../views/mine/help_details.vue'], resolve) },
      meta: { title: '帮助中心' }
    },
    { // 问题反馈
      path: '/mine/question_feedback',
      name: 'questionFeedback',
      component: resolve => { require(['../views/mine/question_feedback.vue'], resolve) },

      meta: { requiresAuth: true, title: '问题反馈' }
    },
    { // 关于我们
      path: '/mine/about_us',
      name: 'aboutUs',
      component: resolve => { require(['../views/mine/about_us.vue'], resolve) },
      meta: { title: '关于我们' }
    },
    { // 公司介绍
      path: '/mine/about_us/compan_intro',
      name: 'companIntro',
      component: resolve => { require(['../views/mine/compan_intro.vue'], resolve) },
      meta: { title: '永鑫保险介绍' }
    },
    { // 安全保障
      path: '/mine/about_us/security',
      name: 'security',
      component: resolve => { require(['../views/mine/security.vue'], resolve) },
      meta: { title: '安全保障' }
    },
    { // 客服服务
      path: '/mine/about_us/custom_service',
      name: 'customService',
      component: resolve => { require(['../views/mine/custom_service.vue'], resolve) },
      meta: { title: '客服服务' }
    },
    { // 登录
      path: '/login',
      name: 'login',
      component: resolve => { require(['../views/user/login.vue'], resolve) },
      meta: { title: '登录' }
    },
    { // 好友邀请落地页
      path: '/gayInvite',
      name: 'gayInvite',
      component: resolve => { require(['../views/user/gayInvite.vue'], resolve) },
      meta: { title: '好友邀请' }
    },
    { // 注册
      path: '/register/:openId',
      name: 'register',
      component: resolve => { require(['../views/user/register.vue'], resolve) },
      meta: { title: '注册' }
    },
    { // 注册成功
      path: '/register/:openId/regSucc',
      name: 'regSucc',
      component: resolve => { require(['../views/user/regSucc.vue'], resolve) },
      meta: { title: '注册成功' }
    },
    { // 忘记密码第一步
      path: '/login/forgetPwd',
      name: 'forgetPwd',
      component: resolve => { require(['../views/user/forgetPwd.vue'], resolve) }
    },
    { // 忘记密码第二步
      path: '/login/forgetPwd/forgetPwd2',
      name: 'forgetPwd2',
      component: resolve => { require(['../views/user/forgetPwd2.vue'], resolve) }
    },
    { // 投资结果
      path: '/msg_result',
      name: 'msgResult',
      component: resolve => { require(['../views/msg_result.vue'], resolve) }
    }
  ]
})
