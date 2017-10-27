package com.rd.ifaes.core.tpp.service.tpp.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rd.ifaes.common.constant.JxbankConstant;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.rabbit.model.MqAutoInvestModel;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.mapper.BorrowMapper;
import com.rd.ifaes.core.project.mapper.ProjectMapper;
import com.rd.ifaes.core.tpp.model.ufx.UfxProjectApplyModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxProjectAttachInfoModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxProjectUpdateModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.UfxProjectService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.mapper.UserCacheMapper;
import com.rd.ifaes.core.user.mapper.UserMapper;
import com.rd.ifaes.core.user.service.UserCompanyInfoService;
import com.rd.ifaes.core.user.service.UserInvestAutoLogService;

@Service("ufxProjectService") 
public class UfxProjectServiceImpl implements UfxProjectService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UfxProjectServiceImpl.class);
	
	@Resource
	private ProjectMapper projectMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private BorrowMapper borrowMapper;
	@Resource
	private UserCacheMapper userCacheMapper;
	@Resource
	private UserCompanyInfoService userCompanyInfoService;
	@Resource
	private UserInvestAutoLogService userInvestAutoLogService;
	
	@Override
	public UfxProjectApplyModel tppProjectApply(String uuid) {

		if(StringUtils.isBlank(uuid)){
			throw new BussinessException("该项目不存在");
		}
		Project base = projectMapper.get(uuid);
		if(base == null || StringUtils.isBlank(base.getUuid())){
			throw new BussinessException("该项目不存在");
		}
		
		
		User user = userMapper.get(base.getUserId());
				
		if(user == null || StringUtils.isBlank(user.getUuid())){
			throw new BussinessException("该借款人不存在");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productId", base.getUuid());
		map.put("productDesc", base.getProjectName());
		map.put("accountId", user.getTppUserCustId());

		map.put("txAmount", BigDecimalUtils.round(base.getAccount(), 2));
		map.put("rate", base.getApr());
		map.put("repaymentType", base.getRepayStyle());
		
//		ProjectWorker worker = new ProjectWorker(base);
//		//预计利息
//		BigDecimal interest = worker.getRepaymentInterest(DateUtils.getNow());	
//		//还款金额：本金+利息
//		map.put("repaymentAmount", BigDecimalUtils.add(interest,base.getAccount()));
//		map.put("userType", "0"+userCache.getUserNature());
//		map.put("userId", user.getUserNo());
		Date saleTime = base.getSaleTime()==null?DateUtils.getNow():base.getSaleTime();		
		Date endTime = saleTime;
		
		if (LoanEnum.STYLE_ONETIME_REPAYMENT.eq(base.getRepayStyle())) {
			map.put("intType", JxbankConstant.JXBANK_INTTYPE_ONE);
		} else {
			map.put("intType", JxbankConstant.JXBANK_INTTYPE_NOTFIX);
		}
		
		map.put("raiseDate", DateUtils.formatDate(saleTime, "yyyyMMdd")); // 募集开始日
		Date cbhbRaiseEndTime = DateUtils.rollDay(saleTime, base.getRaiseTimeLimit());
		map.put("raiseEndDate", DateUtils.formatDate(cbhbRaiseEndTime, "yyyyMMdd")); // 募集结束日

		if(LoanEnum.TIME_TYPE_MONTH.getValue().equals(base.getTimeType())){
			endTime = DateUtils.rollMon(cbhbRaiseEndTime, base.getTimeLimit());		  
		}else{
			endTime = DateUtils.rollDay(cbhbRaiseEndTime, base.getTimeLimit());      
		}
		map.put("duration", DateUtils.daysBetween(cbhbRaiseEndTime, endTime)); // 借款期限天数
		
		//担保机构
		String guarantorCustId = null;
		if(StringUtils.isNotBlank(base.getVouchId())){
			User guarantor = userMapper.get(base.getVouchId());
			if(guarantor!= null && StringUtils.isNotBlank(guarantor.getTppUserCustId())){
				guarantorCustId = guarantor.getTppUserCustId();
			}
		}
		map.put("bailAccountId", guarantorCustId);
		LOGGER.info("开始进入三方的发布项目接口.....");
		TppService tppservice = (TppService)TppUtil.tppService();
		tppservice.tppAddProject(map);
		String tppName = ConfigUtils.getTppName();
		if(TppServiceEnum.JXBANK.getName().equals(tppName)){
			// 自动投标功能
			if(CacheUtils.getInt(base.getUuid()+CacheConstant.KEY_AUTOINVEST_AUTOSTATUS) == Constant.FLAG_INT_YES){
				CacheUtils.del(base.getUuid()+CacheConstant.KEY_AUTOINVEST_AUTOSTATUS);
				LOGGER.info("产品可执行自动投资，产品uuid："+base.getUuid());
				final MqAutoInvestModel autoInvestModel = new MqAutoInvestModel();
				autoInvestModel.setProject(base);
				if (ConfigUtils.isOpenMq()) {
					RabbitUtils.startAutoInvest(autoInvestModel);
				} else {
					userInvestAutoLogService.startAutoInvest(autoInvestModel);
				}
			}
		}
        return null;
	}
	
	@Override
	public UfxProjectUpdateModel tppProjectUpdate(String uuid, String projectStatus) {
		if (StringUtils.isBlank(uuid)) {
			throw new BussinessException("该项目不存在");
		}
		Project base = projectMapper.get(uuid);
		if (base == null || StringUtils.isBlank(base.getUuid())) {
			throw new BussinessException("该项目不存在");
		}
		
		User user = userMapper.get(base.getUserId());
		if(user == null || StringUtils.isBlank(user.getUuid())){
			throw new BussinessException("该借款人不存在");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectId", base.getProjectNo());
		map.put("projectName", base.getProjectName());
		map.put("amount", base.getAccount());
		map.put("sponsorer", user.getTppUserCustId());
		map.put("projectState", projectStatus);
		
		Integer projectPeriod = base.getTimeLimit();
		if(LoanEnum.TIME_TYPE_MONTH.getValue().equals(base.getTimeType())){
			Date endTime = DateUtils.rollMon(base.getSaleTime(), base.getTimeLimit());
			projectPeriod = ((Double)DateUtils.getDistanceOfTwoDate(base.getSaleTime(), endTime)).intValue();
		}		
		map.put("projectPeriod", projectPeriod);
		TppService tppService = (TppService)TppUtil.tppService();
		return (UfxProjectUpdateModel)tppService.tppUpdateProject(map);
	}
	
	@Override
	@Deprecated
	public void tppProjectRevoke(String uuid) {		
		
		Project base = projectMapper.get(uuid);
		if(base == null || StringUtils.isBlank(base.getUuid())){
			throw new BussinessException("该项目不存在");
		}
			
		//借款人
		User user = userMapper.get(base.getUserId());		
		if(user == null || StringUtils.isBlank(user.getUuid())){
			throw new BussinessException("该借款人不存在");
		}		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectId", base.getProjectNo());
		map.put("sponsorer", user.getTppUserCustId());
		map.put("projectAmount", base.getAccount());
		map.put("userId", user.getUserNo());
		TppService tppService = (TppService)TppUtil.tppService();
		tppService.tppProjectRevoke(map);
	}
	
	@Override
	public UfxProjectAttachInfoModel tppProjectAttachInfo(String uuid) {
		if (StringUtils.isBlank(uuid)) {
			throw new BussinessException("该项目不存在");
		}
		Project base = projectMapper.get(uuid);
		if (base == null || StringUtils.isBlank(base.getUuid())) {
			throw new BussinessException("该项目不存在");
		}
		
		User user = userMapper.get(base.getUserId());
		if(user == null || StringUtils.isBlank(user.getUuid())){
			throw new BussinessException("该借款人不存在");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectId", base.getProjectNo());
		map.put("sponsorer", user.getTppUserCustId());
		map.put("projectName", base.getProjectName());
		map.put("projectAmount", base.getAccount());
		map.put("startTime",DateUtils.formatDate(base.getSaleTime(),"yyyyMMddHHmmss"));
		map.put("projectPeriod", base.getTimeLimit()+""+(LoanEnum.TIME_TYPE_MONTH.getValue().equals(base.getTimeType())?"月":"天"));
		TppService tppService = (TppService)TppUtil.tppService();
		return (UfxProjectAttachInfoModel)tppService.tppProjectAttachInfo(map);
	}
	

}
