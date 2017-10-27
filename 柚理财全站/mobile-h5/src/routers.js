//防止刷新时候判断登录是否过期使用组件路由钩子
function requireAuth(to, from, next) {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (localStorage.user) {
      next();
    } else {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      });
    }
  } else {
    next();
  }
}
// require.ensure 是 webpack的特殊语法，用来设置 code-split point
const Home = resolve => {
  require.ensure(['./views/index.vue'], () => {
    resolve(require('./views/index.vue'))
  })
};
// A route component can also contain <router-view> to render
// nested children route components
const Test = { template: '<div>test</div>' };
const Default = { template: '<div>default</div>' };
const Parent = {
  template: `
    <div class="parent">
      <h2>Parent</h2>
      <router-link to="/">返回</router-link>
      <router-view class="child"></router-view>
    </div>
  `
};
const routers = [
  {
    path: '/',
    name: 'home',
    component: Home,
  // components: {
  //   default: Home,
  //   test: Home
  // },
  // meta:{ title: '首页' }
  },
  {
    path: '/index',
    name: 'index',
    component: Home,
  //meta:{ title: '首页' }
  },
  { // 投资列表
    path: '/invest',
    name: 'invest',
    component: resolve => { require(['./views/invest/invest.vue'], resolve) }
  },
  { // 投资详情
    path: '/investDetail/:projectId',
    name: 'investDetail',
    component: resolve => { require(['./views/invest/detail/invest_detail.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 项目了解
    path: '/investDetail/:projectId/investInfo',
    name: 'investInfo',
    component: resolve => { require(['./views/invest/detail/invest_info.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 变现的了解项目
    path: '/realizeDetail/:projectId/realizeInfo',
    name: 'realizeInfo',
    component: resolve => { require(['./views/invest/detail/realize_info.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 投资记录
    path: '/investDetail/:projectId/investRecord',
    name: 'investRecord',
    component: resolve => { require(['./views/invest/detail/invest_record.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 债权详情
    path: '/bondDetail/:projectId',
    name: 'bondDetail',
    component: resolve => { require(['./views/invest/detail/bond_detail.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 变现详情
    path: '/realizeDetail/:projectId',
    name: 'realizeDetail',
    component: resolve => { require(['./views/invest/detail/realize_detail.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 债权投标
    path: '/bondDetail/:projectId/bondBid',
    name: 'bondBid',
    component: resolve => { require(['./views/invest/bid/bond_bid.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 投标
    path: '/investDetail/:projectId/investBid',
    name: 'investBid',
    component: resolve => { require(['./views/invest/bid/invest_bid.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 红包
    path: '/investDetail/:projectId/investBid/redpacket',
    name: 'investRedpacket',
    component: resolve => { require(['./views/invest/bid/redpacket.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 加息券
    path: '/investDetail/:projectId/investBid/coupon',
    name: 'investCoupon',
    component: resolve => { require(['./views/invest/bid/coupon.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 投资结果
    path: '/tender_result',
    name: 'tenderResult',
    component: resolve => { require(['./views/invest/bid/tender_result.vue'], resolve) }
  },
  { // 账户中心
    path: '/account',
    name: 'account',
    component: resolve => { require(['./views/account/account.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 消息
    path: '/account/message',
    name: 'message',
    component: resolve => { require(['./views/account/message.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 消息详情
    path: '/account/message/detail',
    name: 'messageDetail',
    component: resolve => { require(['./views/account/message_detail.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 充值
    path: '/account/recharge',
    name: 'recharge',
    component: resolve => { require(['./views/account/recharge.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 支持银行说明
  path: '/account/recharge/support_bank',
  name: 'supportBank',
  component: resolve => { require(['./views/account/support_bank.vue'], resolve) },
  beforeEnter: requireAuth,
  meta: { requiresAuth: true }
  },
  { // 提现
    path: '/account/withdraw',
    name: 'withdraw',
    component: resolve => { require(['./views/account/withdraw.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 充值、提现记录
    path: '/account/cash_record',
    name: 'cashRecord',
    component: resolve => { require(['./views/account/cash_record.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 资金明细
    path: '/account/logs',
    name: 'logs',
    component: resolve => { require(['./views/account/logs.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 我的投资
    path: '/account/myInvest',
    name: 'myInvest',
    component: resolve => { require(['./views/account/myInvest/my_invest.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 回款计划
    path: '/account/myInvest/repayment_plan',
    name: 'repaymentPlan',
    component: resolve => { require(['./views/account/myInvest/repayment_plan.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 债权转让
    path: '/account/bond/bond',
    name: 'bond',
    component: resolve => { require(['./views/account/bond/bond.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 债权转让交易
    path: '/account/bond/bond_deal',
    name: 'bondDeal',
    component: resolve => { require(['./views/account/bond/bond_deal.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 变现
    path: '/account/money_logs',
    name: 'moneyLogs',
    component: resolve => { require(['./views/account/money_logs.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 变现记录
    path: '/account/money_cash/:id',
    name: 'moneyCash',
    component: resolve => { require(['./views/account/money_cash.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 我的借款
    path: '/account/my_loan',
    name: 'myLoan',
    component: resolve => { require(['./views/account/my_loan.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 立即还款
    path: '/account/refund/:projectId/:period',
    name: 'refund',
    component: resolve => { require(['./views/account/refund.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 还款计划
    path: '/account/refund_plan',
    name: 'refundPlan',
    component: resolve => { require(['./views/account/refund_plan.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 自动投标
    path: '/account/auto',
    name: 'auto',
    component: resolve => { require(['./views/account/auto/auto.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 自动投标参数设置
    path: '/account/auto/setting',
    name: 'autoSetting',
    component: resolve => { require(['./views/account/auto/auto_setting.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 自动投标参数修改
    path: '/account/auto/modify',
    name: 'autoModify',
    component: resolve => { require(['./views/account/auto/auto_modify.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  {  // 预约借款
    path: '/account/appoint_loan',
    name: 'appointLoan',
    component: resolve => { require(['./views/account/appoint_loan.vue'], resolve) }
  },
  {  // 我的优惠
    path: '/account/my_coupon',
    name: 'myCoupon',
    component: resolve => { require(['./views/account/my_coupon.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 我的
    path: '/mine',
    name: 'mine',
    component: resolve => { require(['./views/mine/mine.vue'], resolve) }
  },
  { // 账户与安全
    path: '/mine/safe',
    name: 'safe',
    component: resolve => { require(['./views/mine/safe/settings.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 我的等级
    path: '/mine/safe/vip',
    name: 'vip',
    component: resolve => { require(['./views/mine/safe/vip.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 绑定手机
    path: '/mine/safe/bind_phone',
    name: 'bindPhone',
    component: resolve => { require(['./views/mine/safe/bind_phone.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 修改手机绑定
    path: '/mine/safe/modify_phone',
    name: 'modifyPhone',
    component: resolve => { require(['./views/mine/safe/modify_phone.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 绑定邮箱
    path: '/mine/safe/bind_email',
    name: 'bindEmail',
    component: resolve => { require(['./views/mine/safe/bind_email.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 修改邮箱绑定
    path: '/mine/safe/modify_email',
    name: 'modifyEmail',
    component: resolve => { require(['./views/mine/safe/modify_email.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 托管账户、实名认证
    path: '/mine/safe/realname',
    name: 'realname',
    component: resolve => { require(['./views/mine/safe/realname.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 银行卡
    path: '/mine/safe/bank',
    name: 'bank',
    component: resolve => { require(['./views/mine/safe/bank.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 添加银行卡
    path: '/mine/safe/addBank',
    name: 'addBank',
    component: resolve => { require(['./views/mine/safe/addBank.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 添加银行卡 结果页面
    path: '/mine/safe/addBank_result',
    name: 'addBankResult',
    component: resolve => { require(['./views/mine/safe/addBank_result.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 修改登录密码
    path: '/mine/safe/setPwd',
    name: 'setPwd',
    component: resolve => { require(['./views/mine/safe/setPwd.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 风险测评提示
    path: '/mine/safe/risk_tips',
    name: 'riskTips',
    component: resolve => { require(['./views/mine/safe/risk_tips.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 风险测评
    path: '/mine/safe/risk_rating',
    name: 'riskRating',
    component: resolve => { require(['./views/mine/safe/risk_rating.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 测评完成
    path: '/mine/safe/risk_result',
    name: 'riskResult',
    component: resolve => { require(['./views/mine/safe/risk_result.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 平台公告
    path: '/mine/siteIntro',
    name: 'siteIntro',
    component: resolve => { require(['./views/mine/siteIntro.vue'], resolve) }
  },
  { // 平台公告详情
    path: '/mine/siteIntro/detail/:uuid',
    name: 'siteIntroDetail',
    component: resolve => { require(['./views/mine/siteIntroDetail.vue'], resolve) }
  },
  { // 邀请好友
    path: '/mine/invite',
    name: 'invite',
    component: resolve => { require(['./views/mine/invite.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 奖励规则
    path: '/mine/invite/award_rule',
    name: 'awardRule',
    component: resolve => { require(['./views/mine/award_rule.vue'], resolve) }
  },
  { // 我的邀请记录
    path: '/mine/invite/myInvite_log',
    name: 'myInviteLog',
    component: resolve => { require(['./views/mine/myInvite_log.vue'], resolve) }
  },
  { // 平台数据
    path: '/mine/siteData',
    name: 'siteData',
    component: resolve => { require(['./views/mine/siteData.vue'], resolve) }
  },
  { // 帮助中心
    path: '/mine/help',
    name: 'help',
    component: resolve => { require(['./views/mine/help.vue'], resolve) }
  },
  { // 帮助中心栏目
    path: '/mine/help/help_column',
    name: 'helpColumn',
    component: resolve => { require(['./views/mine/help_column.vue'], resolve) }
  },
  { // 问题反馈
    path: '/mine/question_feedback',
    name: 'questionFeedback',
    component: resolve => { require(['./views/mine/question_feedback.vue'], resolve) },
    beforeEnter: requireAuth,
    meta: { requiresAuth: true }
  },
  { // 关于我们
    path: '/mine/about_us',
    name: 'aboutUs',
    component: resolve => { require(['./views/mine/about_us.vue'], resolve) }
  },
  { // 公司介绍
    path: '/mine/about_us/compan_intro',
    name: 'companIntro',
    component: resolve => { require(['./views/mine/compan_intro.vue'], resolve) }
  },
  { // 安全保障
    path: '/mine/about_us/security',
    name: 'security',
    component: resolve => { require(['./views/mine/security.vue'], resolve) }
  },
  { // 客服服务
    path: '/mine/about_us/custom_service',
    name: 'customService',
    component: resolve => { require(['./views/mine/custom_service.vue'], resolve) }
  },
  { // 登录
    path: '/login',
    name: 'login',
    component: resolve => { require(['./views/user/login.vue'], resolve) }
  },
  { // 注册
    path: '/register',
    name: 'register',
    component: resolve => { require(['./views/user/register.vue'], resolve) }
  },
  { // 注册成功
    path: '/register/regSucc',
    name: 'regSucc',
    component: resolve => { require(['./views/user/regSucc.vue'], resolve) }
  },
  { // 忘记密码第一步
    path: '/login/forgetPwd',
    name: 'forgetPwd',
    component: resolve => { require(['./views/user/forgetPwd.vue'], resolve) }
  },
  { // 忘记密码第二步
    path: '/login/forgetPwd/forgetPwd2',
    name: 'forgetPwd2',
    component: resolve => { require(['./views/user/forgetPwd2.vue'], resolve) }
  },
  { // 投资结果
    path: '/msg_result',
    name: 'msgResult',
    component: resolve => { require(['./views/msg_result.vue'], resolve) }
  },
  { // 测试备用
    path: '/parent',
    component: Parent,
    children:[
      { path: '', component: Default },
      { path: 'test', component: Test }
    ]
  }
]
export default routers;
