package com.rd.ifaes.core.bond.service.impl;

import java.math.BigDecimal;
import java.util.List;



import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.BondRuleEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.bond.domain.BondRule;
import com.rd.ifaes.core.bond.mapper.BondRuleMapper;
import com.rd.ifaes.core.bond.model.BondRuleModel;
import com.rd.ifaes.core.bond.service.BondRuleService;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * 债权规则配置  业务类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年7月28日
 */
@Service("bondRuleService") 
public class BondRuleServiceImpl  extends BaseServiceImpl<BondRuleMapper, BondRule> implements BondRuleService{
	
		
	@Override
	@Cacheable(key=CacheConstant.KEY_BOND_RULE_UUID,expire=ExpireTime.FIVE_MIN)
	public BondRule getBondRule(String id) {
		return dao.get(id);
	}

	@Override
	@Transactional(readOnly = false)
	public String insertBondRuleModel(final BondRuleModel model) {
		String message = StringUtils.EMPTY;
		BondRule bondRule = model.instance(model);
		BeanValidators.validate(bondRule);
		bondRule.preInsert();
		bondRule = handleModel(model,bondRule);
		bondRule.setRuleStatus(BondRuleEnum.RULE_STATAS_ON.getValue());
		final String errorMsg = model.validModelAdd(bondRule);
		if(StringUtils.isNotBlank(errorMsg)){
			message = errorMsg;
		}else{
			final BondRule entity = new BondRule();
			entity.setRuleStatus(BondRuleEnum.RULE_STATAS_ON.getValue());
			final List<BondRule> list = dao.findList(entity);
			TokenUtils.validShiroToken(TokenConstants.TOKEN_BONDRULE_ADD);
			if(CollectionUtils.isNotEmpty(list)){
				for(BondRule rule:list){
					rule.setRuleStatus(BondRuleEnum.RULE_STATAS_OFF.getValue());
				}
				dao.updateBatch(list);
			}
			dao.insert(bondRule);
		}
		return message;
	}
	
	/**
	 * 债权规则校验处理方法
	 * @author QianPengZhan
	 * @date 2016年7月28日
	 * @param model
	 * @param bondRule
	 * @return
	 */
	private BondRule  handleModel(final BondRuleModel model,final BondRule bondRule){
		if(BondRuleEnum.RULE_IS_HONDDAYS.eq(StringUtils.isNull(model.getIsHoldDays()))){
			if(model.getHoldDays() == Constant.INT_ZERO){
				throw new BussinessException(ResourceUtils.get(BondResource.BOND_HOlDDAYS_ZERO));
			}
			bondRule.setHoldDays(model.getHoldDays());
		}else{
			bondRule.setHoldDays(Constant.INT_ZERO);
		}
		if(BondRuleEnum.RULE_IS_PERIODREMAINSDAYS.eq(StringUtils.isNull(model.getIsPeriodRemainDays()))){
			if(model.getPeriodRemainDays() == Constant.INT_ZERO){
				throw new BussinessException(ResourceUtils.get(BondResource.BOND_PERIODDAYS_ZERO));
			}
			bondRule.setPeriodRemainDays(model.getPeriodRemainDays());
		}else{
			bondRule.setPeriodRemainDays(Constant.INT_ZERO);
		}
		if(BondRuleEnum.RULE_IS_REMAINSDAYS.eq(StringUtils.isNull(model.getIsRemainDays()))){
			if(model.getRemainDays() == Constant.INT_ZERO){
				throw new BussinessException(ResourceUtils.get(BondResource.BOND_REMAINDAYS_ZERO));
			}
			bondRule.setRemainDays(model.getRemainDays());
		}else{
			bondRule.setRemainDays(Constant.INT_ZERO);
		}
		return bondRule;
	}
	
	/**
	 * 获取最近的债权规则
	 */
	@Override
	public BondRuleModel getRecentBondRuleModel() {
		BondRuleModel brm = null;
		final List<BondRule> list = dao.getRecentBondRuleModel();
		if(CollectionUtils.isNotEmpty(list) && list.size() == Constant.INT_ONE){
			final BondRule bondRule = list.get(Constant.INT_ZERO);
			brm = bondRule.instanceModel(bondRule);
			if(bondRule.getHoldDays() > Integer.parseInt(BondRuleEnum.RULE_DEFAULT_VALUE.getValue())){
				brm.setIsHoldDays(BondRuleEnum.RULE_IS_HONDDAYS.getValue());
			}
			if(bondRule.getRemainDays() > Integer.parseInt(BondRuleEnum.RULE_DEFAULT_VALUE.getValue())){
				brm.setIsRemainDays(BondRuleEnum.RULE_IS_REMAINSDAYS.getValue());
			}
			if(bondRule.getPeriodRemainDays() > Integer.parseInt(BondRuleEnum.RULE_DEFAULT_VALUE.getValue())){
				brm.setIsPeriodRemainDays(BondRuleEnum.RULE_IS_PERIODREMAINSDAYS.getValue());
			}
		}
		return brm;
	}

	@Override
	public Page<BondRule> findPage(BondRule entity) {
		Page<BondRule> page = entity.getPage();
		if(page==null){
			page=new Page<BondRule>();
		}
		final List<BondRule> list  = dao.findList(entity);
		if(CollectionUtils.isNotEmpty(list)){
			for(final BondRule bondRule:list){
				bondRule.setDiscountRateMax(BigDecimalUtils.round(BigDecimal.valueOf(bondRule.getDiscountRateMax()),Constant.INT_ONE).doubleValue());
				bondRule.setDiscountRateMin(BigDecimalUtils.round(BigDecimal.valueOf(bondRule.getDiscountRateMin()),Constant.INT_ONE).doubleValue());
			}
		}
		page.setRows(list);
		return page;
	}
	
	
}