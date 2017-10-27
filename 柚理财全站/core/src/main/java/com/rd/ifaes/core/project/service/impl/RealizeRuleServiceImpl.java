package com.rd.ifaes.core.project.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.project.domain.RealizeRule;
import com.rd.ifaes.core.project.mapper.RealizeRuleMapper;
import com.rd.ifaes.core.project.service.RealizeRuleService;

/**
 * 变现规则实现类
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
@Service("realizeRuleService") 
public class RealizeRuleServiceImpl  extends BaseServiceImpl<RealizeRuleMapper, RealizeRule> implements RealizeRuleService{
	
	
	/* (non-Javadoc)
	 * @see com.rd.ifaes.core.base.service.BaseServiceImpl#insert(com.rd.ifaes.core.base.domain.BaseEntity)
	 */
	@Override
	@Transactional
	public void insert(final RealizeRule entity) {
		BeanValidators.validate(entity);
		entity.validBase();
		TokenUtils.validShiroToken(TokenConstants.TOKEN_REALIZE_RULE_ADD);
		entity.preInsert();
		dao.closeOther(Constant.DISABLED);
		dao.insert(entity);
		CacheUtils.del(CacheConstant.KEY_REALIZE_RULE);
	}

	/* (non-Javadoc)
	 * @see com.rd.ifaes.core.base.service.BaseServiceImpl#findPage(com.rd.ifaes.core.base.domain.BaseEntity)
	 */
	@Override
	public Page<RealizeRule> findPage(final RealizeRule entity) {
		//默认排序按创建时间倒序
	      if(entity!=null && entity.getPage()!=null && StringUtils.isBlank(entity.getPage().getSort())){
	    	  entity.getPage().setSort("create_time");
	    	  entity.getPage().setOrder("desc");
	      }
		return super.findPage(entity);
	}

	/* (non-Javadoc)
	 * @see com.rd.ifaes.core.project.service.RealizeRuleService#getRule()
	 */
	@Override
	@Cacheable(key = CacheConstant.KEY_REALIZE_RULE , expire = ExpireTime.TEN_MIN)
	public RealizeRule getRule() {
		RealizeRule	rule = dao.getRule();
		return rule;
	}
	
	
}