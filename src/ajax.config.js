export const MobileServer = 'https://mobile.91jinjindai.com'; // 测试服务器的域名
// export const MobileServer = 'http://10.10.1.176:8088'; // 本地调试地址
export const StaticsServer = 'https://static.91jinjindai.com'; // 静态服务器地址
export const avatarMobile = StaticsServer + '/upload/avatarMobile.html'; //头像上传静态服务器
export const uploadAvatar = MobileServer + '/app/member/uploadAvatar.html'; //保存头像到测试服务器
/* S--首页--*/
export const investTotalAjax = MobileServer + '/app/open/index/countInfo.html'; //首页总投资额
export const bannerAjax = MobileServer + '/app/open/index/banner.html'; //首页总投资额
export const recommandProjectAjax = MobileServer + '/app/open/index/getProjectList.html'; //首页推荐标
/* E--首页--*/
/* S--投资--*/
export const projectTypeList = MobileServer + '/app/open/invest/projectTypeList.html'; //投资项目分类列表
export const productListAjax = MobileServer + '/app/open/invest/projectList.html'; //投资项目列表
export const bondListData = MobileServer + '/app/open/bond/bondListData.html'; //转让专区 投资项目列表
export const realizeListData = MobileServer + '/app/open/index/realize/projectList.html'; //变现 投资项目分类列表
export const projectDetail = MobileServer + '/app/member/project/detail.html'; //投资详情
export const investBespeak = MobileServer + '/app/member/index/investBespeak.html'; //预约提醒
export const getBondProjectDetail = MobileServer + '/app/member/bond/getBondProjectDetail.html'; //转让专区 标详情
export const realizeDetail = MobileServer + '/app/member/realize/detailInfo.html'; //变现 标详情
export const checkPwd = MobileServer + '/app/open/project/checkPwd.html'; //校验定向密码
export const recordList = MobileServer + '/app/member/invest/recordList.html'; //投资记录
export const bondRecordList = MobileServer + '/app/member/bond/getBondInvestPage.html'; //债权标 投资记录
export const doBondPay = MobileServer + '/app/member/myBond/doBondPay.html'; //债权去支付
export const borrowerInfo = MobileServer + '/app/member/project/borrower.html'; //借款人信息、借款描述
export const borrowPic = MobileServer + '/app/member/project/borrowPic.html'; //借款资料
export const borrowerDetail = MobileServer + '/app/member/project/content.html'; //借款详情
export const realizeInfo = MobileServer + '/app/member/realize/wxContent.html'; //变现的了解项目
export const initBond = MobileServer + '/app/member/bond/toBondInvest.html'; //转让初始化
export const initInvest = MobileServer + '/app/member/invest/toInvest.html'; //投资初始化
export const doInvest = MobileServer + '/app/member/invest/doInvest.html'; //确认投资
export const doRealizeInvest = MobileServer + '/app/member/realize/doRealizeInvest.html'; //确认投资
export const doBondInvest = MobileServer + '/app/member/bond/doBondInvest.html'; //确认转让
export const interest = MobileServer + '/app/member/invest/interest.html'; //投资预计收益
export const availableRedList = MobileServer + '/app/member/coupon/availableRedList.html'; //投资可用红包
export const availableRateList = MobileServer + '/app/member/coupon/availableRateList.html'; //投资可用加息券
/* E--投资--*/

/* S--资产--*/
export const getAccountInfo = MobileServer + '/app/member/account/getAccountInfo.html'; //账户概览
export const userRedenvelopeList = MobileServer + '/app/member/coupon/userRedenvelopeList.html'; //用户红包列表
export const userRateCouponList = MobileServer + '/app/member/coupon/userRateCouponList.html'; //用户加息劵列表
export const toRecharge = MobileServer + '/app/member/recharge/toRecharge.html'; //充值初始化
export const doRecharge = MobileServer + '/app/member/recharge/doRecharge.html'; //充值提交
export const toCash = MobileServer + '/app/member/cash/toCash.html'; //提现初始化
export const getRechargeList = MobileServer + '/app/member/recharge/getLogList.html'; //充值记录列表
export const getCashList = MobileServer + '/app/member/cash/getLogList.html'; //提现记录列表
export const getBorrowHolding = MobileServer + '/app/member/myInvest/getBorrowHolding.html'; //我的投资持有中列表
export const doPay = MobileServer + '/app/member/myInvest/doPay.html'; //我的投资申请中 去支付
export const getInvestApply = MobileServer + '/app/member/myInvest/getInvestApply.html'; //我的投资申请列表
export const getInvestClosed = MobileServer + '/app/member/myInvest/getInvestClosed.html'; //我的投资持已结束列表
export const getProjectCollectionList = MobileServer + '/app/member/myInvest/getProjectCollectionList.html'; //回款计划
export const ableBondList = MobileServer + '/app/member/myBond/ableBondList.html'; //可转让列表
export const sellingBondList = MobileServer + '/app/member/myBond/sellingBondList.html'; //转让中列表
export const boughtBondList = MobileServer + '/app/member/myBond/boughtBondList.html'; //已受让列表
export const soldBondList = MobileServer + '/app/member/myBond/soldBondList.html'; //已受让列表
export const cancleBond = MobileServer + '/app/member/myBond/cancleBond.html'; //撤回债权标
export const toBondSet = MobileServer + '/app/member/myBond/toBondSet.html'; //转让设置
export const bondSetCommit = MobileServer + '/app/member/myBond/bondSetCommit.html'; //转让设置
export const getRealizeAbleList = MobileServer + '/app/member/myRealize/getRealizeAbleList.html'; //可变现列表
export const getRealizingList = MobileServer + '/app/member/myRealize/getRealizingList.html'; //变现中列表
export const getRealizedList = MobileServer + '/app/member/myRealize/getRealizedList.html'; //已变现列表
export const toRealizeSet = MobileServer + '/app/member/myRealize/toSet.html'; //变现表单
export const doRealizeSet = MobileServer + '/app/member/myRealize/doSet.html'; //变现表单
export const myLoanList = MobileServer + '/app/member/myLoan/getLogList.html'; //借款记录列表
export const myRepaymentList = MobileServer + '/app/member/myRepayment/getLogList.html'; //待还、已还列表
export const myLoanPlanList = MobileServer + '/app/member/myLoan/getProjectRepaymentList.html'; //还款计划列表
export const toRepay = MobileServer + '/app/member/myRepayment/toRepay.html'; //还款初始化
export const doRepay = MobileServer + '/app/member/myRepayment/doRepay.html'; //还款初始化
export const getRepayCode = MobileServer + '/app/member/myRepayment/getRepayCode.html'; //还款--获取手机验证码
export const bespeak = MobileServer + '/app/open/borrow/bespeak.html'; //预约借款
export const bespeakAdd = MobileServer + '/app/open/borrow/bespeakAdd.html'; //预约借款添加
export const autoInit = MobileServer + '/app/member/auto/toSet.html'; //自动投标初始化
export const closeAutoInvest = MobileServer + '/app/member/closeAutoInvest.html'; //关闭自动投标
export const autoInvestDetails = MobileServer + '/app/member/autoInvestDetails.html'; //自动投标参数
export const interestStyle = MobileServer + '/app/open/index/servers.html'; //设置自动投标获取收益方式
export const autoInvestRule = MobileServer + '/app/member/autoInvestRule.html'; //设置自动投标
export const addAutoInvest = MobileServer + '/app/member/addAutoInvest.html'; //设置自动投标
/* E--资产--*/

/* S--我的--*/
export const basicInfo = MobileServer + '/app/member/account/basicInfo.html'; //账户与安全
export const signInfo = MobileServer + '/app/member/security/appSignCard.html'; //签约信息
export const messageList = MobileServer + '/app/member/letter/list.html'; //站内信 我的消息
export const letterInfo = MobileServer + '/app/member/letter/letterInfo.html'; //站内信详情
export const batchSet = MobileServer + '/app/member/letter/batchSet.html'; //站内信-批量操作
export const logsDetail = MobileServer + '/app/member/fund/getLogList.html'; //资金明细
export const vip = MobileServer + '/app/member/account/vip.html'; //vip等级说明
export const tppRegister = MobileServer + '/app/member/security/tppRegister.html'; //托管账户提交
export const apiLogin = MobileServer + '/app/member/security/apiLogin.html'; //进入汇付第三方
export const modifyPhoneCode = MobileServer + '/app/member/security/modifyPhoneCode.html'; //手机绑定 修改 获取验证码
export const doModifyPhone = MobileServer + '/app/member/security/doModifyPhone.html'; //手机绑定 修改 校验
export const bindPhoneCode = MobileServer + '/app/member/security/bindPhoneCode.html'; //手机绑定 获取验证码
export const doBindPhone = MobileServer + '/app/member/security/doBindPhone.html'; //手机绑定 确认提交
export const modifyEmailCode = MobileServer + '/app/member/security/modifyEmailCode.html'; //邮箱绑定 修改 获取验证码
export const doModifyEmail = MobileServer + '/app/member/security/doModifyEmail.html'; //邮箱绑定 修改 校验
export const bindEmailCode = MobileServer + '/app/member/security/bindEmailCode.html'; //邮箱绑定 获取验证码
export const doBindEmail = MobileServer + '/app/member/security/doBindEmail.html'; //邮箱绑定 确认提交
export const getBankCardList = MobileServer + '/app/member/bankCard/getBankCardList.html'; //银行卡列表
export const unBind = MobileServer + '/app/member/bankCard/unBind.html'; //银行卡解绑
export const bindCard = MobileServer + '/app/member/bankCard/bind.html'; //银行卡绑定
export const authSign = MobileServer + '/app/member/security/authSign.html'; //银行卡绑定
export const userRiskPapers = MobileServer + '/app/member/risk/userRiskPapers.html'; //风险评测试卷
export const doUserRiskPapers = MobileServer + '/app/member/risk/doUserRiskPapers.html'; //风险评测试卷提交
export const getArticleList = MobileServer + '/app/open/column/getArticleList.html'; //平台公告
export const articleList = MobileServer + '/app/open/column/articleList.html'; //平台公告详情
export const siteData = MobileServer + '/app/open/column/wxSiteData.html'; //平台数据
export const userInvite = MobileServer + '/app/member/invite/userInvite.html'; //好友邀请
export const inviteLogList = MobileServer + '/app/member/invite/getLogList.html'; //好友邀请记录
export const opinionAddSave = MobileServer + '/app/member/myAssistant/opinionAddSave.html'; //问题反馈
export const customerService = MobileServer + '/app/open/column/customerServiceWx.html'; //客服服务
export const getArticleListcolumn = MobileServer + '/app/open/column/getArticleListcolumnWx.html'; //公司介绍
export const helpCenter = MobileServer + '/app/open/column/wxHelpCenter.html'; //帮助中心
export const helpCenterDetail = MobileServer + '/app/open/column/wxHelpCenterDetail.html'; //帮助中心
/* E--我的--*/

/* S--登录/注册/忘记密码--*/
export const loginAjax = MobileServer + '/app/open/user/doLogin.html'; // 登录接口
export const registerPhoneCode = MobileServer + '/app/open/user/registerPhoneCode.html'; // 注册_获取验证码
export const registerFirst = MobileServer + '/app/open/user/registerFirst.html'; // 注册_基本信息提交
export const doRegister = MobileServer + '/app/open/user/doRegister.html'; // 注册_确认注册
export const sendValidCode = MobileServer + '/app/open/user/security/sendValidCode.html'; // 忘记密码 发送验证码
export const validation = MobileServer + '/app/open/user/retrievepwd/validation.html'; // 忘记密码 点击下一步
export const confirmPwd = MobileServer + '/app/open/user/retrievepwd/confirm.html'; // 忘记密码 确认提交
export const doModifyPwd = MobileServer + '/app/member/security/doModifyPwd.html'; // 修改登录密码
/* --登录/注册/忘记密码--*/

/* S--各种协议--*/
export const registerProtocol = MobileServer + '/app/open/user/registerProtocol.html'; // 注册服务协议
export const registerProtocolDetail = MobileServer + '/app/open/user/wxRegisterProtocolDetail.html'; // 注册服务协议详情
export const getBondProtocolContent = MobileServer + '/app/member/myBond/wxGetBondProtocolContent.html'; // 债权投资协议
export const protocolSearch = MobileServer + '/app/member/myInvest/wxProtocolSearch.html'; // 投资协议
export const realizeProtocol = MobileServer + '/app/member/myRealize/wxRealizeProtocol.html'; // 投资协议
/* E--各种协议--*/
export const wxInvite = MobileServer + '/app/member/invite/wxInvite.html'; // 微信分享接口

/* S--积分商城首页--*/
export const getNewOrders = MobileServer + '/app/open/scoreshop/getNewOrders.html'; // 获取最新兑换的商品
export const getUserScore = MobileServer + '/app/member/scoreshop/getUserScore.html'; // 获取用户积分
export const getVerifyNum = MobileServer + '/app/member/myScore/getVerifyNum.html'; // 获取审核信息
export const getBestseller = MobileServer + '/app/open/scoreshop/getBestseller.html'; // 获取热销商品
export const goodsCategoryList = MobileServer + '/app/open/scoreshop/getScoreGoodsCategoryList.html'; // 获取商品类别
export const getGoodsList = MobileServer + '/app/open/scoreshop/getGoodsList.html'; // 获取商品列表
export const getGoodsInfo = MobileServer + '/app/member/scoreshop/getGoodsInfo.html'; // 获取商品详情信息
export const exchangeGoods = MobileServer + '/app/member/scoreshop/exchangeGoods.html'; // 兑换商品
export const getReceivingInfo = MobileServer + '/app/member/scoreshop/getReceivingInfo.html'; // 获取收货地址
export const configDefaultReceivingInfo = MobileServer + '/app/member/scoreshop/configDefaultReceivingInfo.html'; // 设置默认收货地址
export const deleteReceivingInfo = MobileServer + '/app/member/scoreshop/deleteReceivingInfo.html'; // 删除收货地址
export const addReceivingInfo = MobileServer + '/app/member/scoreshop/addReceivingInfo.html'; // 增加收货地址
export const editReceivingInfo = MobileServer + '/app/member/scoreshop/editReceivingInfo.html'; // 增加收货地址
export const getScoreLogs = MobileServer + '/app/member/myScore/getScoreLogs.html'; // 获取积分记录
export const getScoreOrders = MobileServer + '/app/member/myScore/getScoreOrders.html'; // 获取兑换订单记录

/* E--积分商城首页--*/
