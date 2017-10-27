package com.rd.ifaes.core.account.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountModel;
import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.Base64Utils;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.account.domain.Recharge;
import com.rd.ifaes.core.account.domain.TppMerchantLog;
import com.rd.ifaes.core.account.mapper.TppMerchantLogMapper;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.statistic.constant.StatisticConstant;
import com.rd.ifaes.core.statistic.model.StatisticModel;
import com.rd.ifaes.core.statistic.model.StatisticResultModel;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.core.sys.domain.DictData;
import com.rd.ifaes.core.sys.domain.LogTemplate;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.sys.service.LogTemplateService;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbMercRechargeModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbMercWithdrawModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxCashModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxMerTransferModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxQueryBalanceModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRechargeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxSubAccountModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 
 * @ClassName: TppMerchantLogServiceImpl 
 * @Description: 
 * @author jxx 
 * @date 2016年7月13日 下午4:55:12 
 *
 */
@Service("tppMerchantLogService") 
public class TppMerchantLogServiceImpl  extends BaseServiceImpl<TppMerchantLogMapper, TppMerchantLog> implements TppMerchantLogService{
	private static final Logger LOGGER = LoggerFactory.getLogger(TppMerchantLogServiceImpl.class);
	
	@Resource
	private UserService userService;
	@Resource
	private RechargeService rechargeService;
	@Resource
	private AccountService accountService;
	@Resource
	private AccountLogService accountLogService;
	@Resource
	private LogTemplateService logTemplateService;
	@Resource 
	private StatisticService statisticService;
	@Resource 
	private DictDataService dictDataService;
	
	@Override
	public Page<TppMerchantLog> findManagePage(TppMerchantLogModel entity) {
		Page<TppMerchantLog> page = entity.getPage();
		List<TppMerchantLog> list = dao.findManageList(entity);
		page.setRows(list);
		return page;
	}
	


	@Override
	public List<TppMerchantLog> findManageList(TppMerchantLogModel entity) {
		return dao.findManageList(entity);
	}
	

	@Override
	public TppMerchantLog findByOrderNo(String ordId) {
		return dao.findByOrderNo(ordId);
	}



	@Override
	public UfxRechargeModel merchantRecharge(TppMerchantLog tppMerchantLog) {
		LOGGER.info("商户充值...");
		TppMerchantLogModel tppMerchantLogModel = TppMerchantLogModel.instance(tppMerchantLog);
		tppMerchantLogModel.validMoney();
		
		Map<String, Object> map = new HashMap<>();
		map.put("amount",tppMerchantLog.getMoney());
		TppService tppService = (TppService)TppUtil.tppService();
		UfxRechargeModel ufxRechargeModel = (UfxRechargeModel)tppService.tppMerchantRecharge(map);

    	tppMerchantLog.setStatus(TppMerchantLogModel.STATUS_UNTREATED);
    	tppMerchantLog.setOperateType(TppMerchantLogModel.TYPE_MERCHANT_RECHARGE);
		tppMerchantLog.setOrderNo(ufxRechargeModel.getOrderNo());
		tppMerchantLog.setRemark("待处理");
		// 保存平台操作记录
		try{
			insert(tppMerchantLog);
		}catch(BussinessException e){
			LOGGER.error("保存商户资金记录错误,原因:{}", e.getMessage(), e);
			throw new BussinessException("保存商户资金记录错误",BussinessException.TYPE_CLOSE);
		}
		return ufxRechargeModel;
	}

	@Override
	public void cbhbMerchantRecharge(TppMerchantLog tppMerchantLog) {
		LOGGER.info("渤海银行----------商户充值...");
		TppMerchantLogModel tppMerchantLogModel = TppMerchantLogModel.instance(tppMerchantLog);
		tppMerchantLogModel.validMoney();
		
		Map<String, Object> map = new HashMap<>();
		map.put("transAmt",tppMerchantLog.getMoney());
		map.put("merAccTyp",tppMerchantLog.getMerAccTyp());
		
		TppService tppservice = (TppService)TppUtil.tppService();
		CbhbMercRechargeModel cbhbMercRechargeModel = (CbhbMercRechargeModel) tppservice.tppMerchantRecharge(map);

		if(!CbhbConstant.CBHB_CODE_SUCCESS.equals(cbhbMercRechargeModel.getRespCode())){
			throw new BussinessException(cbhbMercRechargeModel.getRespDesc(),BussinessException.TYPE_CLOSE);
		}
    	tppMerchantLog.setStatus(TppMerchantLogModel.STATUS_SUCEESS);
    	tppMerchantLog.setOperateType(TppMerchantLogModel.TYPE_MERCHANT_RECHARGE);
		tppMerchantLog.setOrderNo(cbhbMercRechargeModel.getMerBillNo());
		tppMerchantLog.setRemark("充值成功");
		// 保存平台操作记录
		try{
			insert(tppMerchantLog);
		}catch(BussinessException e){
			LOGGER.error("保存商户资金记录错误,原因:{}", e.getMessage(), e);
			throw new BussinessException("保存商户资金记录错误",BussinessException.TYPE_CLOSE);
		}
	}

	@Override
	public UfxCashModel merchantCash(TppMerchantLog tppMerchantLog) {
		LOGGER.info("商户提现...");
		TppMerchantLogModel tppMerchantLogModel = TppMerchantLogModel.instance(tppMerchantLog);
		tppMerchantLogModel.validCashMoney();
		// 封装第三方处理参数
    	Map<String, Object> cashMap = new HashMap<String, Object>();
    	cashMap.put("amount",tppMerchantLog.getMoney());
    	cashMap.put("servFee","0.0");
    	cashMap.put("cardId", "");
    	TppService tppService = (TppService)TppUtil.tppService();
    	UfxCashModel ufxCashModel = (UfxCashModel)tppService.tppMerchantCash(cashMap);

    	tppMerchantLog.setStatus(TppMerchantLogModel.STATUS_UNTREATED);
    	tppMerchantLog.setOperateType(TppMerchantLogModel.TYPE_MERCHANT_CASH);
		tppMerchantLog.setOrderNo(ufxCashModel.getOrderNo());
		tppMerchantLog.setRemark("待处理");
		// 保存平台操作记录
		try{
			insert(tppMerchantLog);
		}catch(BussinessException e){
			LOGGER.error("保存商户资金记录错误,原因:{}", e.getMessage());
			throw new BussinessException("保存商户资金记录错误",BussinessException.TYPE_CLOSE);
		}
		return ufxCashModel;
		
	}

	@Override
	public void merchantCbhbCash(TppMerchantLog tppMerchantLog) {
		LOGGER.info("渤海银行-----商户提现...");
		TppMerchantLogModel tppMerchantLogModel = TppMerchantLogModel.instance(tppMerchantLog);
		tppMerchantLogModel.validCashMoney();
		// 封装第三方处理参数
    	Map<String, Object> cashMap = new HashMap<String, Object>();
    	cashMap.put("transAmt",tppMerchantLog.getMoney());
    	
    	TppService tppservice = (TppService)TppUtil.tppService();
    	CbhbMercWithdrawModel cbhbMercWithdrawModel = (CbhbMercWithdrawModel) tppservice.tppMerchantCash(cashMap);

    	if(!CbhbConstant.CBHB_CODE_SUCCESS.equals(cbhbMercWithdrawModel.getRespCode())){
    		throw new BussinessException(cbhbMercWithdrawModel.getRespDesc(),BussinessException.TYPE_CLOSE);
    	}
    	tppMerchantLog.setStatus(TppMerchantLogModel.STATUS_SUCEESS);
    	tppMerchantLog.setOperateType(TppMerchantLogModel.TYPE_MERCHANT_CASH);
		tppMerchantLog.setOrderNo(cbhbMercWithdrawModel.getMerBillNo());
		tppMerchantLog.setRemark("提现成功");
		// 保存平台操作记录
		try{
			insert(tppMerchantLog);
		}catch(BussinessException e){
			LOGGER.error("保存商户资金记录错误,原因:{}", e.getMessage());
			throw new BussinessException("保存商户资金记录错误",BussinessException.TYPE_CLOSE);
		}
	}

	@Override
	public String merchantTransfer(TppMerchantLog tppMerchantLog,String userName) {
		try {
			TppMerchantLogModel tppMerchantLogModel = TppMerchantLogModel.instance(tppMerchantLog);
			
			if(StringUtils.isBlank(userName)){
				LOGGER.error("操作失败,接收账户不能为空！");
				return "操作失败,接收账户不能为空！";
			}
			User user = userService.getUserByLoginName(userName);
			tppMerchantLogModel.validUser(user);
			tppMerchantLogModel.validTransfer();
			
			//校验出账账户可用
			String flag = checkMoneyByAcc(tppMerchantLog.getMoney(), tppMerchantLog.getAccount());
			if (StringUtils.isNotBlank(flag)) {
				return "操作失败";
			}
			String ordId = OrderNoUtils.getSerialNumber();
			String msg = "操作成功";
			String status = TppMerchantLogModel.STATUS_SUCEESS;
			// 执行转账业务
			UfxModel ufxModel = new UfxModel();
			ufxModel.setMoney(tppMerchantLog.getMoney());
			ufxModel.setToTppUserCustId(user.getTppUserCustId());
			ufxModel.setUserNo(user.getUserNo());
			ufxModel.setRealName(user.getRealName());
			TppService tppService = (TppService)TppUtil.tppService();
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("userNo", ufxModel.getUserNo());
			map.put("realName", ufxModel.getRealName());
			map.put("userCustId",ufxModel.getToTppUserCustId());
			map.put("money", ufxModel.getMoney());
			UfxMerTransferModel retModel = (UfxMerTransferModel)tppService.tppTransfer(map);
			if (retModel == null || !retModel.checkReturn()) {
				status = TppMerchantLogModel.STATUS_FAIL;
				msg = retModel == null ? null : retModel.getRespDesc();
			} else {
				BigDecimal amount =  new BigDecimal(retModel.getAmount());
				// 保存用户充值记录
				Recharge recharge = new Recharge(ordId, user.getUuid(), amount, "3");
				recharge.setFeeType(Constant.FEE_TYPE_MERCHANT);//平台垫付
				recharge.setStatus(Constant.RECHARGE_STATUS_SUCCESS);
				rechargeService.insert(recharge);
				
				//修改资金
				LOGGER.info("用户userId={}充值成功，增加可用金额：{}元。",user.getUuid(),amount);			
				
				//添加资金记录
				Map<String, Object> param = new HashMap<>();
				param.put("money", amount);
				String content = logTemplateService.getTemplateContent(LogTemplate.LOG_TYPE_ACCOUNT, Constant.RECHARGE_SUCCESS_LOG, param);
				
				AccountLog accountLog = new AccountLog(ConfigUtils.getValue(Constant.ACCOUNT_CODE), Constant.RECHARGE_SUCCESS_LOG, user.getUuid(), amount,
						ConfigUtils.getValue(Constant.ADMIN_ID), AccountLog.PAYMENTS_TYPE_INCOME, retModel.getLoanNo(), retModel.getOrderNo(), content);
				AccountModel accountModel = new AccountModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE), amount, BigDecimal.ZERO);
				accountService.saveAccountChange(accountModel, accountLog);
			}

			tppMerchantLog.setToAccount(userName);
	    	tppMerchantLog.setStatus(status);
			tppMerchantLog.setOperateType(TppMerchantLogModel.TYPE_TRANSFER);
			tppMerchantLog.setOrderNo(ordId);
			tppMerchantLog.setRemark(msg);
	    	// 保存平台操作记录
			insert(tppMerchantLog);
		} catch (Exception e) {
			LOGGER.error("操作失败{}",e.getMessage(), e);
			return e.getMessage();
		}
		return null;
	}

	/**
	 * 
	 * 查询商户子账户列表
	 * @author jxx
	 * @date 2016年7月26日
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<UfxSubAccountModel> getSubAccountList(){
		List<UfxSubAccountModel> ufxSubAccounts = new ArrayList<>();
		final String tppName = ConfigUtils.getTppName();
		if(TppServiceEnum.CHINAPNR.getName().equals(tppName)){
			Map<String, Object> ufxMap = new HashMap<>();
			ufxMap.put("userCustId", ConfigUtils.getValue(Constant.UFX_CUSTOMERID));
			ufxMap.put("userType", UfxQueryBalanceModel.USER_TYPE_COMPANY);
			TppService tppService = (TppService)TppUtil.tppService();
			UfxQueryBalanceModel ufxQueryBalanceModel = (UfxQueryBalanceModel)tppService.tppQueryBalance(ufxMap);
	
			// 获取商户子账户信息
			String resp = ufxQueryBalanceModel.getSubAccounts();
	
			// 截取商户子账户信息Json字符串
			String jsonStr = resp.substring(resp.indexOf("["), resp.lastIndexOf("]") + 1);
	
			// 解析Json字符串并封装子账户信息对象
			JSONArray array = JSON.parseArray(jsonStr);
			if (array != null) {
				for (int i = 0; i < array.size(); i++) {
					JSONObject object = array.getJSONObject(i);
					UfxSubAccountModel ufxSubAccount = new UfxSubAccountModel();
					ufxSubAccount.setSubAcctId(object.getString("accountId"));
					ufxSubAccount.setAcctBal(object.getBigDecimal("acctBal"));
					ufxSubAccount.setAvlBal(object.getBigDecimal("avlBal"));
					ufxSubAccount.setFrzBal(object.getBigDecimal("frzBal"));
					ufxSubAccounts.add(ufxSubAccount);
				}
			}
		}else if(TppServiceEnum.CBHB.getName().equals(tppName)){
			//暂无子账户
		}else{
			
		}
		return ufxSubAccounts;
	}
	
	/**
	 * 
	 * 检查金额
	 * @author jxx
	 * @date 2016年7月26日
	 * @param money
	 * @param accId
	 * @return
	 * @throws Exception
	 */
	private String checkMoneyByAcc(BigDecimal money, String accId) {
		if (StringUtils.isBlank(accId)) {
			return "付款账户不能为空！";
		}
		
		List<UfxSubAccountModel> ufxSubAccounts  = getSubAccountList();
		BigDecimal avlBal = BigDecimal.ZERO;
		
		if (ufxSubAccounts != null) {
			for (int i = 0; i < ufxSubAccounts.size(); i++) {
				UfxSubAccountModel ufxSubAccount = ufxSubAccounts.get(i);
				if (accId.equals(ufxSubAccount.getSubAcctId())) {
					avlBal = ufxSubAccount.getAvlBal();
					break;
				}
			}
		}
		
		if (money.compareTo(avlBal) > 0) {
			return "付款账户可用余额不足！";
		}
		
		return null;
	}


	@Override
	public String subMerchantTransfer(TppMerchantLog tppMerchantLog) {
		try {
			TppMerchantLogModel tppMerchantLogModel = TppMerchantLogModel.instance(tppMerchantLog);
			// 校验数据
			tppMerchantLogModel.validTransfer();
			
			String msg = "操作成功";
			String status = TppMerchantLogModel.STATUS_SUCEESS;
			Map<String,String> merPivMap =  new HashMap<>();
			merPivMap.put("outAcctId", tppMerchantLog.getAccount());
			merPivMap.put("inAcctId", tppMerchantLog.getToAccount());
			String merPiv =  JSON.toJSONString(merPivMap);
			
			Map<String,Object> map = new HashMap<>();
			map.put("amount",tppMerchantLog.getMoney());
			map.put("reqExt", Base64Utils.base64Encode(merPiv));
		
			// 执行转账业务
			TppService tppService = (TppService)TppUtil.tppService();
			UfxMerTransferModel  merTransferModel = (UfxMerTransferModel)tppService.tppMerSubAccountTransfer(map);
			if (merTransferModel == null || !merTransferModel.checkReturn()) {
				status = TppMerchantLogModel.STATUS_FAIL;
				msg = merTransferModel==null?null:merTransferModel.getRespDesc();
			}

			tppMerchantLog.setStatus(status);
			tppMerchantLog.setOperateType(TppMerchantLogModel.TYPE_MERCHANT_TRANSFER);
			tppMerchantLog.setOrderNo(merTransferModel==null?null:merTransferModel.getOrderNo());
			tppMerchantLog.setRemark(msg);
	    	// 保存平台操作记录
			insert(tppMerchantLog);
		} catch (Exception e) {
	 		LOGGER.error("操作失败,{}",e.getMessage(), e);
			return e.getMessage();
		}
		return null;
	}

	@Override
	public boolean doMerchantRecharge(UfxRechargeModel model) {
		boolean ret = model.validSign(model);
		LOGGER.info("商户充值验签结果：{}" , ret);
		if (ret) { // 校验参数
			String orderNo = model.getOrderNo();
			if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
				LOGGER.info("商户充值成功，进入本地处理。");
				LOGGER.info("订单号：{} 金额：{}  结果：{}" ,orderNo,model.getAmount(),model.getRespDesc());	
				// 查找记录
				TppMerchantLog merchantLog = findByOrderNo(orderNo);
				// 判断是否已被处理
				if (TppMerchantLogModel.STATUS_SUCEESS.equals(merchantLog.getStatus())) {
					LOGGER.info("平台充值订单已处理");
					return true;
				}
				
				merchantLog.setStatus(TppMerchantLogModel.STATUS_SUCEESS);
				merchantLog.setRemark("操作成功");
				merchantLog.setFee(BigDecimalUtils.round(model.getRechargeFee()));
				update(merchantLog);
				return true;
			} else {
				LOGGER.info("商户充值处理失败,{}" , model.getRespDesc());
				// 查找记录
				TppMerchantLog merchantLog = findByOrderNo(orderNo);
				merchantLog.setStatus(TppMerchantLogModel.STATUS_FAIL);
				merchantLog.setRemark(model.getRespDesc());
				update(merchantLog);
				return false;
			}
		} else {
			LOGGER.info("充值验签失败！");
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL));
		}
	}
	
	@Override
	public boolean doMerchantCash(UfxCashModel model) {
		boolean ret = model.validSign(model);
		String orderNo = model.getOrderNo();
		LOGGER.info("商户取现验签结果：{}" , ret);
		if (ret) { // 校验参数
			if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
				LOGGER.info("商户取现成功，进入本地处理。");
				LOGGER.info("订单号：{} 金额：{} 结果：{}",orderNo ,model.getAmount() , model.getRespDesc() );					
				// 查找记录
				TppMerchantLog merchantLog = findByOrderNo(orderNo);
				// 判断是否已被处理
				if (TppMerchantLogModel.STATUS_SUCEESS.equals(merchantLog.getStatus())) {
					LOGGER.info("平台取现订单已处理");
					return true;
				}
				
				merchantLog.setStatus(TppMerchantLogModel.STATUS_SUCEESS);
				merchantLog.setRemark("操作成功");
				merchantLog.setFee(BigDecimalUtils.round(model.getServFee()));
				update(merchantLog);
				return true;
			} else {
				LOGGER.info("商户取现处理失败,{}" , model.getRespDesc());
				// 查找记录
				TppMerchantLog merchantLog = findByOrderNo(orderNo);
				merchantLog.setStatus(TppMerchantLogModel.STATUS_FAIL);
				merchantLog.setRemark(model.getRespDesc());
				update(merchantLog);
				return false;
			}
		} else {
			LOGGER.info("取现验签失败！");
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL));//没有找到对应的
		}
	}
	
    /**
     * 保存放款、还款2个阶段的平台资金收支日志
     * @author  FangJun
     * @date 2016年11月3日
     * @param operateType  操作类型
     * @param userId 交易用户
     * @param money 交易金额
     * @param orderNo 交易订单号
     */
	public  void  saveMerchantLog(final String operateType,final String userId,final BigDecimal money,final String orderNo){
		User user = userService.get(userId);
		//用户账户
		String userAccount=user.getUserName();
		//平台账户
		String merchantAccount= ConfigUtils.getValue(ConfigConstant.WEB_NAME);
		
		TppMerchantLog merchantLog = null;
		
		 LOGGER.info("平台账户资金记录----  资金类型：{}, 交易用户: {}, 金额：{}元,订单号: {}",operateType,userId,money,orderNo);
		 
		//平台支出：放款 -红包抵扣 , 还款 - 加息利息发放，充值手续费垫付、提现手续费垫付
		if(TppMerchantLogModel.TYPE_REDENVELOPE.equals(operateType)
				||TppMerchantLogModel.TYPE_RATECOUPON.equals(operateType)
				||TppMerchantLogModel.TYPE_CUSTOMER_RECHARGE.equals(operateType)
				||TppMerchantLogModel.TYPE_CUSTOMER_CASH.equals(operateType)){
			merchantLog =new TppMerchantLog(operateType,merchantAccount, userAccount,money,orderNo, null);
		}else{
		//平台收入：放款 -借款管理费,还款 -利息管理费, 还款 -逾期罚息给平台、提现服务费、变现管理费
			merchantLog =new TppMerchantLog(operateType,userAccount, merchantAccount,money,orderNo, null);
		}
		
	    merchantLog.setStatus(TppMerchantLogModel.STATUS_SUCEESS);
		this.insert(merchantLog);
	}
	
	@Override
	public int getManageCount(TppMerchantLogModel model) {
		return dao.getManageCount(model);
	}

	@Override
	public Map<String,Object>findByStatDate(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		//校验时间
		model.checkQueryDate();
		//成功处理订单
		model.setStatus(TppMerchantLogModel.STATUS_SUCEESS);
		//总计
		List<String> totalAmntList = statisticService.statByDate(dao.findByStatDate(model), model); 
		map.put(StatisticConstant.STAT_CHART_AMNT, totalAmntList);
		//环比值
		BigDecimal beforeCount = dao.getCountBeforeStartDate(model);
		map.put(StatisticConstant.STAT_CHART_RATIO, statisticService.statisticRatio(totalAmntList, beforeCount));
		//日期横坐标
		map.put(StatisticConstant.STAT_CHART_DATE, statisticService.getStatChartDate(model));
		return map;
	}
	
	@Override
	public Map<String,Object>findByStatType(StatisticModel model){
		//校验时间
		model.checkQueryDate();
		//成功处理订单
		model.setStatus(TppMerchantLogModel.STATUS_SUCEESS);
		//查询数据库
		Map<String,BigDecimal> statusMap = Maps.newHashMap();
		List<StatisticResultModel> typeList = dao.getMoneyByType(model);
		for (StatisticResultModel resultModel : typeList) {
			statusMap.put(resultModel.getType(),resultModel.getTotalAmnt());
		}
		//轮询状态
		final Map<String, Object> map = Maps.newHashMap();
		String[] typeArr = model.getTypeArr();
		for (String type : typeArr) {
			DictData data = dictDataService.get(TppMerchantLogModel.OPERATE_TYPE, type);
			if(data!=null){
				map.put(data.getItemName(), statusMap.get(type)==null?0:statusMap.get(type));
			}
		}
		return map;
	}
	
	@Override
	public Map<String,Object> statisticPlatProfit(StatisticModel model){
		final Map<String, Object> map = Maps.newHashMap();
		//校验时间
		model.checkQueryDate();
		//日期横坐标
		map.put(StatisticConstant.STAT_CHART_DATE, statisticService.getStatChartDate(model));
		//金额保留两位小数
		model.setStatType(StatisticConstant.STAT_TYPE_AMOUNT);
		//收入
		model.setTypeArr(TppMerchantLogModel.INCOME_ARR.split(","));
		List<String> imcomeList = statisticService.statByDate(dao.findByStatDate(model), model);
		map.put("income", imcomeList);
		//支出
		model.setTypeArr(TppMerchantLogModel.PAY_ARR.split(","));
		List<String> payList = statisticService.statByDate(dao.findByStatDate(model), model);
		map.put("pay", payList);
		//盈亏
		List<String> profitList = new ArrayList<String>();
		for (int i = 0; i < imcomeList.size(); i++) {
			BigDecimal income = BigDecimalUtils.round(imcomeList.get(i));
			BigDecimal pay = BigDecimalUtils.round(payList.get(i));
			BigDecimal profit = BigDecimalUtils.sub(income, pay);
			profitList.add(profit.toString());
		}
		map.put("profit", profitList);
		
		map.put("result", true);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getStatisticMertLogList(StatisticModel model){
		Map<String,Object> dataMap = statisticPlatProfit(model);
		List<String> profitList = (List<String>) dataMap.get("profit");
		List<String> payList = (List<String>) dataMap.get("pay");
		List<String> imcomeList = (List<String>) dataMap.get("income");
		
		List<StatisticResultModel> rowList =  new ArrayList<StatisticResultModel>();
		List<String> dateList = (List<String>) dataMap.get(StatisticConstant.STAT_CHART_DATE);
		for (int i = 0; i < dateList.size(); i++) {
			rowList.add(new StatisticResultModel(dateList.get(i),BigDecimalUtils.valueOf(profitList.get(i)),
					BigDecimalUtils.valueOf(imcomeList.get(i)),BigDecimalUtils.valueOf(payList.get(i))));
		}
		
		Map<String,Object> map = Maps.newHashMap();
		map.put(StatisticConstant.RESULT, true);
		map.put(StatisticConstant.ROWS, rowList);
		map.put(StatisticConstant.TOTAL, dateList==null?BigDecimal.ZERO:dateList.size());
		return map;
	}
	
	@Override
	public void setTppMerchantLogFail() {
		try {
            Date d=DateUtils.getNow();
            long now=d.getTime();
            int dayTime=24*3600*1000;
            List<TppMerchantLog> tppMerchantLogList = dao.findAll();
            for (TppMerchantLog tppMerchantLog : tppMerchantLogList) {
                if((now - tppMerchantLog.getCreateTime().getTime())>= dayTime && TppMerchantLogModel.STATUS_UNTREATED.equals(tppMerchantLog.getStatus())){
                	tppMerchantLog.setStatus(TppMerchantLogModel.STATUS_FAIL);
                	tppMerchantLog.setRemark("失败");
                	dao.update(tppMerchantLog);
                }
            }
        } catch (Exception e) {
        	LOGGER.warn(e.getMessage(), e);
        }
	}
}