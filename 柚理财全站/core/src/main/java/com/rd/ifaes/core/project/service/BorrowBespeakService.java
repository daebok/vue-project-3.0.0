package com.rd.ifaes.core.project.service;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.BorrowBespeak;
import com.rd.ifaes.core.project.model.BorrowBespeakModel;

/**
 * Service Interface:BorrowBespeakService
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
public interface BorrowBespeakService extends BaseService<BorrowBespeak>{

	/**
	 * 添加预约借款
	 * @author zb
	 * @date 2016年7月30日
	 * @param model
	 * @param zone 
	 */
	void add(BorrowBespeakModel model, String[] zone);

	/**
	 * 
	 * 后台分页查询
	 * @author ZhangBiao
	 * @date 2016年8月30日
	 * @return
	 */
	Page<BorrowBespeak> findPage(BorrowBespeak model);
	
	/**
	 * 更新约借款
	 * @author ywt
	 * @date 2016年9月21日
	 * @param model
	 */
	void update(BorrowBespeak entity);
}