package com.rd.ifaes.core.bond.service;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.bond.domain.BondRule;
import com.rd.ifaes.core.bond.model.BondRuleModel;

/**
 * 债权规则类业务接口
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月30日
 */
public interface BondRuleService extends BaseService<BondRule>{
	
	/**
	 * 根据uuid获取对应的缓存中的债权规则
	 * @author QianPengZhan
	 * @date 2016年8月31日
	 * @param uuid
	 * @return
	 */
	BondRule getBondRule(String uuid);

	/**
	 * insertBondRuleModel:(根据扩展属性去添加债权规则). <br/> 
	 * @author QianPengZhan
	 * @param model
	 * @return
	 */
	String insertBondRuleModel(BondRuleModel model);
	
	/**
	 * bondRuleService:(获取最新的债权规则配置). <br/> 
	 * @author QianPengZhan
	 * @return
	 */
	BondRuleModel getRecentBondRuleModel();
}