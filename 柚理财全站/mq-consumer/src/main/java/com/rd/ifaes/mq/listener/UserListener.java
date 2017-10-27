package com.rd.ifaes.mq.listener;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.model.MqUserModel;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.TppWarnMsg;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.model.ufx.UfxCompanyRegisterModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRegisterModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mq.MqListener;

/**
 * 用户队列监听
 * 
 * @version 3.0
 * @author lh
 * @date 2016年8月4日
 */
@Component
public class UserListener implements MqListener<MqUserModel> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserListener.class);

	@Autowired
	private UserCacheService userCacheService;
	@Autowired
	private UserService userService;
	
	@Override
	public void listen(MqUserModel model){	
		LOGGER.info("用户开户队列监听已开启");
		LOGGER.debug(model.toString());
		
		if(MqConstant.OPERATE_REGISGER.equals(model.getOperate())){
			register(model.getRegisterModel());
		}else if(MqConstant.OPERATE_COMPANY_REGISGER.equals(model.getOperate())){
			companyRegister(model.getCompanyRegisterModel());
		}else if(MqConstant.OPERATE_USER_COMPARE_ACCOUNT.equals(model.getOperate())){
			handleLogList(model.getLogList());
		}else if(MqConstant.TPP_WARN.equals(model.getOperate())){
			tppWarnMessage(model.getParams());
		}else if(MqConstant.OPERATE_USER_INVEST_NUM.equals(model.getOperate())){
			addUserInvestNum(model.getParams().get("userId").toString());
		}
		
	}
	
	/**
	 * 第三方调度任务失败   发送预警短信
	 * @author QianPengZhan
	 * @date 2016年10月12日
	 * @param params
	 */
	public void tppWarnMessage(final Map<String,Object> params){
		TppWarnMsg message = new TppWarnMsg(MessageConstant.TPP_WARN, params);
		message.doEvent();
	}
	
	/**
	 * 每天下午17点30扫描昨天17点30~今天17点30  所有有资金操作的用户的本地和第三方资金的对比  找出差异的用户 并发短信通知运维人员
	 * @author QianPengZhan
	 * @date 2016年10月11日
	 * @param userModel
	 */
	public void handleLogList(final List<String> logList){
		userService.handleLogList(logList);
	}
	
	/**
	 * 个人开户
	 * @author lh
	 * @date 2016年8月4日
	 * @param model
	 */
	public void register(UfxRegisterModel model) {
		String result = Constant.TPP_ORDER_HANDLE_SUCCESS;
		try{
			userCacheService.tppUfxUserRegis(model);
		}catch(Exception e){
			if (e instanceof BussinessException) {// 业务异常，保存业务处理信息
                result = e.getMessage();
            } else {
                result = ResourceUtils.get(ResourceConstant.ORDER_HANDLE_SYSTEM_EXCEPTION);
            }
		}
		String resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), model.getOrderNo());
		CacheUtils.set(resultFlag, result, ExpireTime.ONE_DAY);
	}

	/**
	 * 企业开户
	 * @author lh
	 * @date 2016年8月4日
	 * @param model
	 */
	public void companyRegister(UfxCompanyRegisterModel model) {
		userCacheService.tppUfxCompanyRegis(model);
	}
	
	/**
	 * 计算用户投资记录数
	 * @param userId
	 */
	public void addUserInvestNum(String userId){
		userCacheService.addUserInvestNum(userId);
	}

}
