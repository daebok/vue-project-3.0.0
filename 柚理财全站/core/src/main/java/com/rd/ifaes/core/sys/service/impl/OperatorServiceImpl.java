package com.rd.ifaes.core.sys.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.classic.Logger;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.PasswordHelper;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.sys.domain.OperatorRole;
import com.rd.ifaes.core.sys.mapper.OperatorMapper;
import com.rd.ifaes.core.sys.mapper.OperatorRoleMapper;
import com.rd.ifaes.core.sys.service.OperatorService;
import com.rd.ifaes.core.user.domain.User;


/**
 * ServiceImpl:operatorServiceImpl
 * @author lh
 * @version 3.0
 * @date 2016-5-30
 */
@Service("operatorService") 
public class OperatorServiceImpl  extends BaseServiceImpl<OperatorMapper, Operator> implements OperatorService{
	
	@Resource
	private OperatorRoleMapper operatorRoleMapper;
	
	@Resource
	private OperatorService operatorService;
	
	@Override
	@Transactional(readOnly = false)
	public void insert(Operator entity) {
		BeanValidators.validate(entity);
		// 判断登录名是否重复
		Operator operator = dao.getByLoginName(entity.getLoginName());
		if(operator != null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NAME_REPEAT));
		}
		entity.setPwd(PasswordHelper.entryptPassword(entity.getPwd()));    
		super.insert(entity);
		saveOperatorRoles(entity, true);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void update(Operator entity) {
		BeanValidators.validate(entity);
		entity.setPwd(PasswordHelper.entryptPassword(entity.getPwd()));    
		super.update(entity);
		saveOperatorRoles(entity, false);
	}
	@Override
	@Transactional(readOnly = false)
	public void updateStatus(Operator operator) {
		super.update(operator);
	}
	@Override
	public Operator getByLoginName(String loginName) {
		return dao.getByLoginName(loginName);
	}

	@Override
	public Set<String> findRoles(String id) {
		return new HashSet<String>(dao.findRoles(id));
	}

	@Override
	public Set<String> findPermissions(String id) {
		return new HashSet<String>(dao.findPermissions(id));
	}

	@Override
	public Page<Operator> findRoleOperators(Operator user) {
		Page<Operator> page = user.getPage();
		page.setRows(dao.findRoleOperators(user));
		return page;
	}
	
	/**
	 * 
	 * @param user
	 * @param newUser
	 */
	@Transactional(readOnly = false)
	private void saveOperatorRoles(Operator user , boolean insert){
		if(user.getRoleIds()!=null && user.getRoleIds().length > 0){	
			if(!insert){
				operatorRoleMapper.deleteOperatorRoles(user.getUuid());				
			}
			List<OperatorRole> urList = new ArrayList<OperatorRole>();
			String[] roleIds = user.getRoleIds();
			for (String roleId : roleIds) {
				urList.add(new OperatorRole(IdGen.uuid(), user.getUuid(), roleId));
			}		
			if(urList.size() > 0){
				operatorRoleMapper.insertBatch(urList);				
			}
		}
	}

	@Override
	public void updatePwd(Operator operator) {
		operator.checkChangePwd();
		// 获取登录帐号
		Operator opera = (Operator) PrincipalUtils.getPrincipal();
		final Operator operatorOld = dao.get(opera.getUuid());
		if(operatorOld == null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_EMPTY));
		}
		// 校验原始密码是否正确
		if(!PasswordHelper.validatePassword(operator.getOldPwd(),operatorOld.getPwd())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_OLD_PWD_ERROR));
		}
		if(PasswordHelper.validatePassword(operator.getPwd(),operatorOld.getPwd())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.OPERATOR_NEWPWD_AGAIN_ERROR));
		}
		operator.setUuid(opera.getUuid());
		operator.setPwd(PasswordHelper.entryptPassword(operator.getPwd()));
		dao.updatePwd(operator);
	}

	@Override
	public List<Operator> findOnlineServer() {
		return dao.findOnlineServer();
	}
	
	@Override
	public int findIsFirstLogin(Operator operator) {
		operator.setIsFirstLogin(dao.findIsFirstLogin(operator));
		return operator.getIsFirstLogin();
	}
	@Override
	public void resetOperatorPwd(Operator operator) {
		operator.checkChangePwd();
		operator.setPwd(PasswordHelper.entryptPassword(operator.getPwd()));
		operator.setPwdFlag(Operator.PWD_FLAG_YES);
		dao.update(operator);
	}

	@Override
	public int getNumByOrgIds(String[] ids) {
		return dao.getNumByOrgIds(ids);
	}
	
	@Override
	public Operator get(String id) {
		return dao.get(id);
	}

	@Override
	public void unFreezeAccountByTimer() {
		List<Operator> operatorList = dao.findByStatus(Operator.USER_STATUS_LOCKED);
		for (Operator operator : operatorList) {
			final long now = DateUtils.getNow().getTime();
			if((now - operator.getLockTime().getTime()) >= 24*60*60*1000)//24小时
			operator.setStatus(Operator.DEFAULT_USER_STATUS);
			operatorService.updateStatus(operator);
		}
	}
}