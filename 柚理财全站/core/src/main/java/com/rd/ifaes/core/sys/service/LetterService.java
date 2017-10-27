package com.rd.ifaes.core.sys.service;

import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.sys.domain.Letter;

/**
 * Service Interface:LetterService
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
public interface LetterService extends BaseService<Letter>{
	
	/**
	 * 收件箱未读计数
	 * 
	 * @param userId
	 * @return
	 */
	int unreadCount(String receiveUser);
	
	/**
	 * 批量操作
	 * 
	 * @param ids
	 * @param userId
	 */
	Map<String, Object> set(Letter model);
	
	
	/**
	 * 前台站内信列表
	 * @author fxl
	 * @date 2016年10月14日
	 * @param model
	 * @return
	 */
	Page<Letter> findLetter(Letter model);
	

}