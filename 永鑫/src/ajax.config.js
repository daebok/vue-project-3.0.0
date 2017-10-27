export const domain = 'http://weixin-test.360mili.com/finance-mobile' // 测试服务器的域名
//export const domain = 'http://9mzcu3.natappfree.cc' // 接口本地服务器的域名
//export const domain = 'http://weixin.finance.360mili.com' // 正式服务器的域名
//export const assetsDomain = 'https://res.womai.com' // 静态服务器地址
export const assetsDomain = 'http://Static.finance.360mili.com' // 静态服务器地址
export const avatarMobile = assetsDomain + '/upload/avatarMobile.html' //头像上传静态服务器
export const uploadAvatar = domain + '/app/member/uploadAvatar.html' //保存头像到测试服务器
/* S--首页--*/
export const countInfoAjax = domain + '/app/open/index/countInfo.html' //首页首页统计
export const bannerAjax = domain + '/app/open/index/banner.html' //首页banner图
//export const projectListAjax = domain + '/app/open/invest/projectList.html' //首页推荐标
export const lastFundUnitAjax = domain + '/app/open/fund/lastFundUnit.html' //首页粮票宝
export const articleListAjax = domain + '/app/open/index/articleList.html' //首页获取文章列表
/* E--首页--*/
/* S--粮票宝--*/
export const fund = domain + '/app/member/fund/index.html' //粮票宝首页
export const getUnitStatistics = domain + '/app/member/fund/getUnitStatistics.html' //粮票宝七日收益
export const getFundLogList = domain + '/app/member/fund/getFundLogList.html' //粮票宝转入转出记录
export const getProfitList = domain + '/app/member/fund/getProfitList.html' //粮票宝收益记录
export const perchasePage = domain + '/app/member/fund/perchasePage.html' //粮票宝转入页面
export const perchase = domain + '/app/member/fund/perchase.html' //粮票宝确认转入
export const calculate = domain + '/app/open/fund/calculate.html' //粮票宝预计收益
export const redeemPage = domain + '/app/member/fund/redeemPage.html' //粮票宝转出页面
export const redeem = domain + '/app/member/fund/redeem.html' //粮票宝确认转出赎回
/* E--粮票宝--*/

/* S--永鑫保险--*/
export const projectListAjax = domain + '/app/index/fund/productList.html' //产品列表
export const projectDetailAjax = domain + '/app/fund/cjhx/productDetails.html' //产品详情
export const projectApplyAjax = domain + '/app/fund/cjhx/applyBuy.html' //用户申购
export const projectRedeemAjax = domain + '/app/fund/cjhx/redeem.html' //用户赎回
export const getPositionList = domain + '/app/fund/cjhx/tradingRecords.html' //持仓记录
export const getFundHistory = domain + '/app/fund/cjhx/fundHistory.html' //图表走势
export const currentList = domain + '/app/fund/queryShare.html' //我的活期记录
export const tradingList = domain + '/app/fund/tradingRecords.html' //我的交易记录
export const creatToken = domain + '/app/fund/hx/creatToken.html' //hx生成TOKEN

export const getDiscountList = domain + '/app/member/discountCenter/getDiscountList.html' //领券中心接口
export const doCollect = domain + '/app/member/discountCenter/doCollect.html' //领取优惠券
export const userDiscountCouponList = domain + '/app/member/coupon/userDiscountCouponList.html' //我的优惠券
/* E--永鑫保险--*/

/* S--投资--*/
export const realizeListAjax = domain + '/app/open/index/realize/projectList.html' //变现产品列表
export const projectTypeListAjax = domain + '/app/open/invest/projectTypeList.html' //产品排序分类列表
export const queryConditionAjax = domain + '/app/open/invest/queryCondition.html' //产品排序分类详情列表
//export const projectDetailAjax = domain + '/app/member/project/detail.html' //理财详情
export const investBespeakAjax = domain + '/app/member/index/investBespeak.html' //预约提醒
export const checkPwdAjax = domain + '/app/open/project/checkPwd.html' //校验定向密码
export const realizeDetailAjax = domain + '/app/member/realize/detailInfo.html' //变现标详情
export const recordListAjax = domain + '/app/member/invest/recordList.html' //投资记录
export const bondListData = domain + '/app/open/bond/bondListData.html' //转让专区 投资项目列表
export const getBondProjectDetail = domain + '/app/member/bond/getBondProjectDetail.html' //转让专区 标详情
export const bondRecordList = domain + '/app/member/bond/getBondInvestPage.html' //债权标 投资记录
export const doBondPay = domain + '/app/member/myBond/doBondPay.html' //债权去支付
export const borrowerInfo = domain + '/app/member/project/borrower.html' //借款人信息、借款描述
export const borrowPic = domain + '/app/member/project/borrowPic.html' //借款资料
export const borrowerDetail = domain + '/app/member/project/content.html' //借款详情
export const realizeInfo = domain + '/app/member/realize/wxContent.html' //变现的了解项目
export const initBond = domain + '/app/member/bond/toBondInvest.html' //转让初始化
export const initInvest = domain + '/app/member/invest/toInvest.html' //投资初始化
export const doInvest = domain + '/app/member/invest/doInvestPay.html' //确认投资
export const doRealizeInvest = domain + '/app/member/realize/doRealizeInvest.html' //确认投资
export const doBondInvest = domain + '/app/member/bond/doBondInvest.html' //确认转让
export const interest = domain + '/app/member/invest/interest.html' //投资预计收益
export const availableRedList = domain + '/app/member/coupon/availableRedList.html' //投资可用红包
export const availableRateList = domain + '/app/member/coupon/availableRateList.html' //投资可用加息券
export const queryCoupon = domain + '/app/member/coupon/queryCoupon.html' //查看是否有优惠券可用
export const top5Help = domain + '/app/open/column/top5Help.html' //银行限额

/* E--投资--*/

/* S--资产--*/
export const getAccountInfo = domain + '/app/member/account/getAccountInfo.html' //账户概览
export const userRedenvelopeList = domain + '/app/member/coupon/userRedenvelopeList.html' //用户红包列表
export const userRateCouponList = domain + '/app/member/coupon/userRateCouponList.html' //用户加息劵列表
export const toRecharge = domain + '/app/member/recharge/toRecharge.html' //充值初始化
export const doRecharge = domain + '/app/member/recharge/doRecharge.html' //充值提交
export const toCash = domain + '/app/member/cash/toCash.html' //提现初始化
export const doCash = domain + '/app/member/cash/doCash.html' //提现初始化
export const getRechargeList = domain + '/app/member/recharge/getLogList.html' //充值记录列表
export const getCashList = domain + '/app/member/cash/getLogList.html' //提现记录列表
export const getInvestInfo = domain + '/app/member/myInvest/getInvestInfo.html' //我的投资概览
export const getBorrowHolding = domain + '/app/member/myInvest/getBorrowHolding.html' //我的投资持有中列表
export const toPay = domain + '/app/member/myInvest/toPay.html' //我的投资申请中 去支付
export const doPay = domain + '/app/member/invest/doPay.html' //我的投资申请中 去支付
export const getInvestApply = domain + '/app/member/myInvest/getInvestApply.html' //我的投资申请列表
export const getInvestClosed = domain + '/app/member/myInvest/getInvestClosed.html' //我的投资持已结束列表
export const getInvestFailed = domain + '/app/member/myInvest/getInvestFailed.html' //我的投资持处理失败
export const getProjectCollectionList = domain + '/app/member/myInvest/getProjectCollectionList.html' //回款计划
export const ableBondList = domain + '/app/member/myBond/ableBondList.html' //可转让列表
export const sellingBondList = domain + '/app/member/myBond/sellingBondList.html' //转让中列表
export const boughtBondList = domain + '/app/member/myBond/boughtBondList.html' //已受让列表
export const soldBondList = domain + '/app/member/myBond/soldBondList.html' //已受让列表
export const cancleBond = domain + '/app/member/myBond/cancleBond.html' //撤回债权标
export const toBondSet = domain + '/app/member/myBond/toBondSet.html' //转让设置
export const bondSetCommit = domain + '/app/member/myBond/bondSetCommit.html' //转让设置
export const getRealizeAbleList = domain + '/app/member/myRealize/getRealizeAbleList.html' //可变现列表
export const getRealizingList = domain + '/app/member/myRealize/getRealizingList.html' //变现中列表
export const getRealizedList = domain + '/app/member/myRealize/getRealizedList.html' //已变现列表
export const toRealizeSet = domain + '/app/member/myRealize/toSet.html' //变现表单
export const doRealizeSet = domain + '/app/member/myRealize/doSet.html' //变现表单
export const myLoanList = domain + '/app/member/myLoan/getLogList.html' //借款记录列表
export const myRepaymentList = domain + '/app/member/myRepayment/getLogList.html' //待还、已还列表
export const myLoanPlanList = domain + '/app/member/myLoan/getProjectRepaymentList.html' //还款计划列表
export const toRepay = domain + '/app/member/myRepayment/toRepay.html' //还款初始化
export const doRepay = domain + '/app/member/myRepayment/doRepay.html' //还款初始化
export const getRepayCode = domain + '/app/member/myRepayment/getRepayCode.html' //还款--获取手机验证码
export const bespeak = domain + '/app/open/borrow/bespeak.html' //预约借款
export const bespeakAdd = domain + '/app/open/borrow/bespeakAdd.html' //预约借款添加
export const autoInit = domain + '/app/member/auto/toSet.html' //自动投标初始化
export const closeAutoInvest = domain + '/app/member/closeAutoInvest.html' //关闭自动投标
export const autoInvestDetails = domain + '/app/member/autoInvestDetails.html' //自动投标参数
export const interestStyle = domain + '/app/open/index/servers.html' //设置自动投标获取收益方式
export const autoInvestRule = domain + '/app/member/autoInvestRule.html' //设置自动投标
export const addAutoInvest = domain + '/app/member/addAutoInvest.html' //设置自动投标
/* E--资产--*/

/* S--我的--*/
export const doChangeBank = domain + '/app/member/bankCard/doChangeBank.html' // 银行卡-换卡提交
export const changeBankCode = domain + '/app/member/bankCard/changeBankCode.html' // 银行卡-换卡验证码
export const changeBankCard = domain + '/app/member/bankCard/changeBankCard.html' // 银行卡-换卡页面
export const bindBankCode = domain + '/app/member/bankCard/bindBankCode.html' // 银行卡-绑卡验证码
export const idCardUpload = domain + '/app/member/security/idCardUpload.html' // 身份证认证信息
export const uploadifySave = '/upload/uploadifySave.html' // 身份证认证上传图片
export const saveCardInfo = domain + '/app/member/security/saveCardInfo.html' // 身份证认证提交
export const userGuide = domain + '/app/member/security/userGuide.html' // 实名_获取协议
export const realnamePhoneCode = domain + '/app/member/security/realnamePhoneCode.html' // 实名_获取验证码
export const getAreaJson = domain + '/app/open/area/getAreaJson.html' // 省市区
export const basicInfo = domain + '/app/member/account/basicInfo.html' //账户与安全
export const messageList = domain + '/app/member/letter/list.html' //站内信 我的消息
export const letterInfo = domain + '/app/member/letter/letterInfo.html' //站内信详情
export const batchSet = domain + '/app/member/letter/batchSet.html' //站内信-批量操作
export const logsDetail = domain + '/app/member/fund/getLogList.html' //资金明细
export const vip = domain + '/app/member/account/vip.html' //vip等级说明
export const tppRegister = domain + '/app/member/security/tppRegister.html' //托管账户提交
export const apiLogin = domain + '/app/member/security/apiLogin.html' //进入汇付第三方
export const modifyPhoneCode = domain + '/app/member/security/modifyPhoneCode.html' //手机绑定 修改 获取验证码
export const doModifyPhone = domain + '/app/member/security/doModifyPhone.html' //手机绑定 修改 校验
export const bindPhoneCode = domain + '/app/member/security/bindPhoneCode.html' //手机绑定 获取验证码
export const doBindPhone = domain + '/app/member/security/doBindPhone.html' //手机绑定 确认提交
export const modifyEmailCode = domain + '/app/member/security/modifyEmailCode.html' //邮箱绑定 修改 获取验证码
export const doModifyEmail = domain + '/app/member/security/doModifyEmail.html' //邮箱绑定 修改 校验
export const bindEmailCode = domain + '/app/member/security/bindEmailCode.html' //邮箱绑定 获取验证码
export const doBindEmail = domain + '/app/member/security/doBindEmail.html' //邮箱绑定 确认提交
export const getBankCardList = domain + '/app/member/bankCard/getBankCardList.html' //银行卡列表
export const unBind = domain + '/app/member/bankCard/unBind.html' //银行卡解绑
export const doBindBankCard = domain + '/app/member/bankCard/doBindBankCard.html' //银行卡绑定
export const bindBankCard = domain + '/app/member/bankCard/bindBankCard.html' //添加银行卡页面数据初始化
export const authSign = domain + '/app/member/security/authSign.html' //银行卡绑定
export const userRiskPapers = domain + '/app/member/risk/userRiskPapers.html' //风险评测试卷
export const doUserRiskPapers = domain + '/app/member/risk/doUserRiskPapers.html' //风险评测试卷提交
export const updatePayPwd = domain + '/app/member/security/updatePayPwd.html' //支付密码设置和修改
export const findPayPwdPhoneCode = domain + '/app/member/security/findPayPwdPhoneCode.html' //找回支付密码 获取验证码
export const checkPayPwdPhoneCode = domain + '/app/member/security/checkPayPwdPhoneCode.html' //找回支付密码 获取验证码
export const getArticleList = domain + '/app/open/column/getArticleList.html' //平台公告
export const articleList = domain + '/app/open/column/articleList.html' //平台公告详情
export const siteData = domain + '/app/open/column/wxSiteData.html' //平台数据
export const userInvite = domain + '/app/member/invite/userInvite.html' //好友邀请
export const inviteLogList = domain + '/app/member/invite/getLogList.html' //好友邀请记录
export const opinionAddSave = domain + '/app/member/myAssistant/opinionAddSave.html' //问题反馈
export const customerService = domain + '/app/open/column/customerService.html' //客服服务
export const getArticleListcolumn = domain + '/app/open/column/getArticleListcolumnWx.html' //公司介绍
export const helpCenter = domain + '/app/open/column/helpCenter.html' //帮助中心
export const helpCenterDetail = domain + '/app/open/column/wxHelpCenterDetail.html' //帮助中心
export const helpArticleDetail = domain + '/app/open/column/articleDetail.html' //帮助中心详情
/* E--我的--*/

/* S--登录/注册/忘记密码--*/
export const gayInvite = domain + '/app/open/invite/gayInvite.html' // 好友邀请
export const toLogin = domain + '/app/user/login.html' // 联合登录接口
export const loginAjax = domain + '/app/open/user/doLogin.html' // 登录接口
export const logoutAjax = domain + '/app/open/user/logout.html' // 联合退出接口
export const registerPhoneCode = domain + '/app/open/user/registerPhoneCode.html' // 注册_获取验证码
export const registerFirst = domain + '/app/open/user/registerFirst.html' // 注册_基本信息提交
export const doRegister = domain + '/app/open/user/doRegister.html' // 注册_确认注册
export const sendValidCode = domain + '/app/open/user/security/sendValidCode.html' // 忘记密码 发送验证码
export const validation = domain + '/app/open/user/retrievepwd/validation.html' // 忘记密码 点击下一步
export const confirmPwd = domain + '/app/open/user/retrievepwd/confirm.html' // 忘记密码 确认提交
export const doModifyPwd = domain + '/app/member/security/doModifyPwd.html' // 修改登录密码
/* --登录/注册/忘记密码--*/

/* S--各种协议--*/
export const registerProtocol = domain + '/app/open/user/registerProtocol.html' // 注册服务协议
export const registerProtocolDetail = domain + '/app/open/user/wxRegisterProtocolDetail.html' // 注册服务协议详情
export const getBondProtocolContent = domain + '/app/member/myBond/wxGetBondProtocolContent.html' // 债权投资协议
export const protocolSearch = domain + '/app/member/myInvest/wxProtocolSearch.html' // 投资协议
export const realizeProtocol = domain + '/app/member/myRealize/wxRealizeProtocol.html' // 投资协议
/* E--各种协议--*/

export const wxInvite = domain + '/app/member/invite/wxInvite.html' // 微信分享接口
