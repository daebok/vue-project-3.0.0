package com.rd.ifaes.core.sys.mapper;

import java.util.List;

import com.rd.ifaes.core.base.mapper.BaseMapper;
import com.rd.ifaes.core.sys.domain.Letter;

/**
 * Dao Interface:LetterMapper
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
public interface LetterMapper extends BaseMapper<Letter> {
	
	/**
	 * 
	 * 批量操作
	 * @author xhf
	 * @date 2016年8月31日
	 * @param params
	 * @return
	 */
	int setBatch(Letter model);
	
	
	/**
	 * 前台站内信列表
	 * @author fxl
	 * @date 2016年10月14日
	 * @param model
	 * @return
	 */
	List<Letter> findLetter(Letter model);

}