package com.rd.ifaes.core.project.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvestBespeak;
import com.rd.ifaes.core.project.mapper.ProjectInvestBespeakMapper;
import com.rd.ifaes.core.project.service.ProjectInvestBespeakService;
import com.rd.ifaes.core.sys.service.ConfigService;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.sys.service.MessageTypeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:ProjectInvestBespeakServiceImpl
 * @author wyw
 * @version 3.0
 * @date 2016-8-20
 */
@Service("projectInvestBespeakService") 
public class ProjectInvestBespeakServiceImpl  extends BaseServiceImpl<ProjectInvestBespeakMapper, ProjectInvestBespeak> implements ProjectInvestBespeakService{
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectInvestBespeakServiceImpl.class);
	@Resource
	private transient MessageTypeService messageTypeService;
	@Resource
	private transient ConfigService configService; 
	@Resource
	private transient MessageService messageService;
	@Resource
	private transient UserService userService;
	
	@Override
	public void investBespeak(String userId, Project project) {
		//查询该用户 该项目是否已经预约
		ProjectInvestBespeak query = new ProjectInvestBespeak();
		query.setUserId(userId);
		query.setProjectId(project.getUuid());
		int count=dao.getCount(query);
		if(count>0){//已经预约过
			throw new BussinessException(ResourceUtils.get("projectInvestBespeak.exist"), BussinessException.TYPE_JSON);
		}
		if(!project.getStatus().equalsIgnoreCase(LoanEnum.STATUS_RAISING.getValue())){//募集中的才能预约
			throw new BussinessException(ResourceUtils.get("projectInvestBespeak.status.error"), BussinessException.TYPE_JSON);
		}
		//已经过了预约时间
		if(project.getSaleTime()!=null&&DateUtils.getNow().after(project.getSaleTime())){
			throw new BussinessException(ResourceUtils.get("projectInvestBespeak.time.error"), BussinessException.TYPE_JSON);
		}
		query.setCreateTime(DateUtils.getNow());
		query.setSaleTime(project.getSaleTime());
		query.setRemindStatus(ProjectInvestBespeak.REMIND_STATUS_NO);
		this.insert(query);
		
		CacheUtils.del(CacheConstant.KEY_PREFIX_USER_BESPEAK_PROJECT_IDS + userId);
	}

	@Override
	public void investRemind() {
		ProjectInvestBespeak queryBespeak = new ProjectInvestBespeak();
		queryBespeak.setRemindStatus(ProjectInvestBespeak.REMIND_STATUS_NO);//未提醒
		queryBespeak.setNowTime(DateUtils.getNow());
		List<ProjectInvestBespeak>  list=this.findList(queryBespeak);
		for(ProjectInvestBespeak pib:list){
			remindUser(pib);
			pib.setRemindStatus(ProjectInvestBespeak.REMIND_STATUS_YES);
			pib.setRemindTime(DateUtils.getNow());
		}
		if(!CollectionUtils.isEmpty(list)){
			this.updateBatch(list);	
		}	
	}
	/**
	 * 
	 * 提醒用户
	 * @author ywt
	 * @date 2016-10-20
	 */
	private void remindUser(ProjectInvestBespeak investBespeak){
		String messageType = MessageConstant.PROJECT_INVEST_BESPEAK_REMIND;//预约投资消息模板
		Map<String,Object> sendData = new HashMap<String,Object>();//模板中的动态数据
		User user=userService.get(investBespeak.getUserId());
		if (user == null) {
			LOGGER.info("预约提醒功能---用户为空");
			return;
		}
		sendData.put("tender", user.getMobile());
		sendData.put("projectName", investBespeak.getProjectName());
		sendData.put("saleTime", DateUtils.dateStr4(investBespeak.getSaleTime()));
		sendData.put("user", user);
		messageService.sendMessage(messageType, sendData);
	}

	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_USER_BESPEAK_PROJECT_IDS+"{userId}", expire = ExpireTime.FIVE_SEC)
	public List<String> getUserBespeakProjectIds(String userId) {
		return dao.getUserBespeakProjectIds(userId);
	}
	
	
}