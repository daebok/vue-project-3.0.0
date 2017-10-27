package com.rd.ifaes.core.tpp.service;

import java.util.Map;


/**
 * 第三方支付（银行存管）系统接口
 *
 * @author lh
 */
public interface TppService {

	/*不同的第三方共同的接口   个人开户、个人充值、个人提现、商户充值、商户提现、建标、投标、放款、还款、用户余额查询  start*/

    /**
     * 渤海 APP 用户注册开户
     *
     * @param params
     * @return
     */
    Object tppAppRegister(Map<String, Object> params);

    /**
     * 个人开户
     *
     * @param params
     * @param model
     * @return
     */
    Object tppRegister(Map<String, Object> params);

    /**
     * 个人APP充值
     *
     * @param params
     * @param model
     * @return
     */
    Object tppAppRecharge(Map<String, Object> params);

    /**
     * 个人充值
     *
     * @param params
     * @param model
     * @return
     */
    Object tppRecharge(Map<String, Object> params);

    /**
     * APP个人提现
     *
     * @param params
     * @param model
     * @return
     */
    Object tppAppCash(Map<String, Object> params);

    /**
     * 个人提现
     *
     * @param params
     * @param model
     * @return
     */
    Object tppCash(Map<String, Object> params);

    /**
     * 商户充值
     *
     * @param params
     * @param model
     * @return
     */
    Object tppMerchantRecharge(Map<String, Object> params);

    /**
     * 商户取现
     *
     * @param params
     * @param model
     * @return
     */
    Object tppMerchantCash(Map<String, Object> params);

    /**
     * 建标
     *
     * @param params
     * @param model
     * @return
     */
    Object tppAddProject(Map<String, Object> params);

    /**
     * 投标
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年4月10日
     */
    Object tppAddTender(Map<String, Object> params);

    /**
     * 放款
     *
     * @param model
     * @return
     */
    Object tppLoan(Map<String, Object> params);

    /**
     * 还款
     *
     * @param model
     * @return
     */
    Object tppRepayment(Map<String, Object> params);

    /**
     * 查询用户账户余额
     *
     * @param model
     * @return
     */
    Object tppQueryBalance(Map<String, Object> params);

	/*不同的第三方共同的接口    个人开户、个人充值、个人提现、商户充值、商户提现、建标、投标、放款、还款、用户余额查询   end*/
	
	/* ufx汇付接口                 start */

    /**
     * 投资失败
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年4月11日
     */
    Object tppInvestFail(Map<String, Object> params);

    /**
     * 补单
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年4月11日
     */
    Object tppAdditionOrder(Map<String, Object> params);

    /**
     * 单笔交易查询
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年4月11日
     */
    Object tppQueryTransfer(Map<String, Object> params);

    /**
     * 子賬戶之間的互转
     * TODO 方法说明
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年4月11日
     */
    Object tppMerSubAccountTransfer(Map<String, Object> params);

    /**
     * UFX汇付的补录项目信息
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年4月10日
     */
    Object tppProjectAttachInfo(Map<String, Object> params);

    /**
     * UFX汇付的撤销项目信息:仅用于尚未投标的项目<br>
     * 目前仅徽商、兴业支持此业务
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年4月10日
     */
    Object tppProjectRevoke(Map<String, Object> params);

    /**
     * UFX汇付的更新项目接口
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年4月10日
     */
    Object tppUpdateProject(Map<String, Object> params);

    /**
     * UFX汇付的企业开户
     *
     * @param params
     * @param model
     * @return
     */
    Object tppCropRegister(Map<String, Object> params);

    /**
     * UFX汇付的三方授权接口
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年4月10日
     */
    Object tppAuthSign(Map<String, Object> params);

    /**
     * UFX汇付的取现审核
     *
     * @param model
     * @return
     */
    Object tppVerifyCash(Map<String, Object> params);

    /**
     * 绑卡
     *
     * @param params
     * @param model
     * @return
     */
    Object tppAddBankCard(Map<String, Object> params);

    /**
     * 删除银行卡
     *
     * @param model
     * @return
     */
    Object tppDeleteBankBank(Map<String, Object> params);

    /**
     * 资金冻结
     *
     * @param model
     * @return
     */
    Object tppFreeze(Map<String, Object> params);

    /**
     * 解冻
     *
     * @param model
     * @return
     */
    Object tppUnFreeze(Map<String, Object> params);

    /**
     * 自动投标计划或授权
     *
     * @param model
     * @return
     */
    Object tppAutoTenderPlan(Map<String, Object> params);

    /**
     * 关闭自动投标计划
     *
     * @param model
     * @return
     */
    Object tppCloseAutoTenderPlan(Map<String, Object> params);

    /**
     * 自动投标操作
     *
     * @param model
     * @return
     */
    Object tppAutoTender(Map<String, Object> params);

    /**
     * 查询用户绑定银行卡
     *
     * @param model
     * @return
     */
    Object tppQueryUserBankCard(Map<String, Object> params);

    /**
     * 用户登录
     *
     * @param model
     * @return
     */
    Object tppUserLogin(Map<String, Object> params);

    /**
     * 第三方理财功能接口
     *
     * @param params
     * @param model
     * @return
     */
    Object tppFssTrans(Map<String, Object> params);

    /**
     * 第三方商户给用户转账接口
     *
     * @param params
     * @param model
     * @return
     */
    Object tppTransfer(Map<String, Object> params);

    /**
     * 第三方债权转让
     *
     * @param params
     * @param model
     * @return
     */
    Object tppCreditAssign(Map<String, Object> params);
	
	/* ufx汇付接口                 end */
	
	
	/* 渤海银行存管接口                 start */

    /**
     * 渤海银行--实时红包对账
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年3月15日
     */
    Object tppExpChk(Map<String, Object> params);

    /**
     * 渤海银行--提现对账
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年3月15日
     */
    Object tppWdcChk(Map<String, Object> params);

    /**
     * 渤海银行--充值对账
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年3月15日
     */
    Object tppPpdChk(Map<String, Object> params);

    /**
     * 渤海银行--投标对账
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年3月14日
     */
    Object tppInvestChk(Map<String, Object> params);

    /**
     * 渤海银行--批量投标撤销
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年3月13日
     */
    Object tppBatInvestCancle(Map<String, Object> params);

    /**
     * 渤海银行-- 投标撤销
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年3月4日
     */
    Object tppInvestCancle(Map<String, Object> params);

    /**
     * 渤海银行-- 交易状态查询
     *
     * @param params
     * @return
     * @author QianPengZhan
     * @date 2017年3月2日
     */
    Object tppQueryTransStat(Map<String, Object> params);

    /**
     * 渤海银行 -- 修改三方支付密码
     *
     * @param params
     * @param model
     * @return
     * @author QianPengZhan
     * @date 2017年2月23日
     */
    Object tppUpdatePayPass(Map<String, Object> params);

    /**
     * 渤海银行 -- 修改手机号
     *
     * @param params
     * @param model
     * @return
     * @author QianPengZhan
     * @date 2017年2月23日
     */
    Object tppUpdateMobileNo(Map<String, Object> params);

    /**
     * 渤海银行 -- APP修改银行卡
     *
     * @param params
     * @param model
     * @return
     * @author QianPengZhan
     * @date 2017年2月23日
     */
    Object tppAppUpdateBankCard(Map<String, Object> params);

    /**
     * 渤海银行 -- 修改银行卡
     *
     * @param params
     * @param model
     * @return
     * @author QianPengZhan
     * @date 2017年2月23日
     */
    Object tppUpdateBankCard(Map<String, Object> params);

    /**
     * 渤海银行 -- 动态口令申请接口    获取动态口令（用于存管投标）
     *
     * @param params
     * @param model
     * @return
     * @author QianPengZhan
     * @date 2017年2月21日
     */
    Object tppSendUapMsg(Map<String, Object> params);

    /**
     * 渤海银行 --商户账户查询
     *
     * @return
     * @author ZhangBiao
     * @date 2017年2月24日
     */
    Object queryMerchantAccts(Map<String, Object> params);

    /**
     * 渤海银行 --实时红包
     *
     * @param params
     * @return
     * @author ZhangBiao
     * @date 2017年2月24日
     */
    Object experBonus(Map<String, Object> params);

    /**
     * 渤海银行 --用户信息查询
     *
     * @param params
     * @param object
     * @return
     * @author ZhangBiao
     * @date 2017年2月27日
     */
    Object queryUserInf(Map<String, Object> params);

    /**
     * 渤海银行 --红包体验金
     *
     * @param params
     * @param object
     * @return
     * @author ZhangBiao
     * @date 2017年3月7日
     */
    Object tppExperCap(Map<String, Object> params);

    /**
     * 渤海银行 --批量实名接口
     *
     * @param params
     * @param object
     * @return
     * @author ZhangBiao
     * @date 2017年3月15日
     */
    Object tppExperRealeName(Map<String, Object> params);
	/* 渤海银行存管接口                 end */


    /**
     * 江西银行
     * 按证件号查询电子账号
     *
     * @param params
     * @return
     * @author jxx
     * @date 2017年8月1日
     */
    Object tppAccountIdQuery(Map<String, Object> params);

    /**
     * 江西银行
     * 请求发送短信验证码
     *
     * @param params
     * @return
     * @author jxx
     * @date 2017年8月1日
     */
    Object tppSmsCodeApply(Map<String, Object> params);

    /**
     * 江西银行
     * 红包撤销
     * 包含2个接口
     *
     * @param params
     * @return
     * @author jxx
     * @date 2017年8月18日
     */
    Object voucherPayCancel(Map<String, Object> params);

    /**
     * 批量交易明细查询
     *
     * @param map
     * @return
     */
    Object batchDetailsQuery(Map<String, Object> map);

    /**
     * 企业账户查询
     *
     * @param map
     * @author jxx
     * @date 2017年8月31日
     */
    Object queryCorpration(Map<String, Object> map);

    /**
     * 单笔资金类业务交易查询
     *
     * @param map
     * @return
     * @author jxx
     * @date 2017年9月5日
     */
    Object fundTransQuery(Map<String, Object> map);

    /**
     * 批量红包发放
     *
     * @param map
     * @return
     */
    Object batchVoucherPay(Map<String, Object> map);

    /**
     * 批次发红包
     *
     * @param params
     * @return
     */
    Object batchVoucherDetailsQuery(Map<String, Object> params);

    /**
     * 批次担保账户代偿
     *
     * @param params
     * @return
     */
    Object batchBailRepay(Map<String, Object> params);

    /**
     * 电子账户资金交易明细查询
     *
     * @param params
     * @return
     */
    Object accountDetailsQuery(Map<String, Object> params);

    /**
     * 投资人债权明细查询
     *
     * @param map
     * @return
     * @author jxx
     * @date 2017年9月8日
     */
    Object creditDetailsQuery(Map<String, Object> map);

    /**
     * 投资人投标申请查询
     *
     * @param map
     * @return
     * @author jxx
     * @date 2017年9月8日
     */
    Object bidApplyQuery(Map<String, Object> map);


    /**
     * 投资人购买债权查询
     *
     * @param map
     * @return
     * @author jxx
     * @date 2017年9月8日
     */
    Object creditInvestQuery(Map<String, Object> map);

    /**
     * 投资人签约状态查询
     *
     * @param map
     * @return
     * @author jxx
     * @date 2017年9月8日
     */
    Object creditAuthQuery(Map<String, Object> map);

    /**
     * 账户资金冻结明细查询
     *
     * @param map
     * @return
     * @author jxx
     * @date 2017年9月8日
     */
    Object freezeDetailsQuery(Map<String, Object> map);

    /**
     * 借款人标的信息查询
     *
     * @param map
     * @return
     * @author jxx
     * @date 2017年9月8日
     */
    Object debtDetailsQuery(Map<String, Object> map);


    /**
     * 电子账户密码是否设置查询
     *
     * @param map
     * @return
     * @author jxx
     * @date 2017年9月8日
     */
    Object passwordSetQuery(Map<String, Object> map);

    /**
     * 批次结束债权
     *
     * @param params
     * @return
     */
    Object batchCreditEnd(Map<String, Object> params);


    Object batchQuery(Map<String, Object> params);

    /**
     * 投资人自动债权转让签约增强
     *
     * @param params
     * @return
     */
    Object autoCreditInvestAuthPlus(Map<String, Object> params);

    /**
     * 批次融资人还担保账户垫款
     * @param params
     * @return
     */
    Object batchRepayBail(Map<String, Object> params);
    
    /**
     * 
     * 3.7	交易明细流水文件
     * @author jxx
     * @date 2017年9月18日
     * @param params
     * @return
     */
    Object downTradeDetail(Map<String, Object> params);

    /**
     * 
     * 3.8	交易明细全流水文件
     * @author jxx
     * @date 2017年9月18日
     * @param params
     * @return
     */
    Object downTradeDetailAll(Map<String, Object> params);
    
    /**
     * 撤销自动债权转让签约
     * @author zhangxj
     * @date 2017年10月12日
     * @param params
     * @return
     */
    Object autoCreditInvestAuthCancel(Map<String, Object> params);
}
