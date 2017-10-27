package com.rd.ifaes.core.sys.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.sys.domain.Letter;
import com.rd.ifaes.core.sys.mapper.LetterMapper;
import com.rd.ifaes.core.sys.service.LetterService;

/**
 * 站内信业务处理
 * @author lh
 * @version 3.0
 * @date 2016-6-21
 */
@Service("letterService") 
public class LetterServiceImpl  extends BaseServiceImpl<LetterMapper, Letter> implements LetterService{
	
	/**
	 * 收件箱未读计数
	 */
	@Override
	public int unreadCount(final String receiveUser) {
		final Letter letter = new Letter();
		letter.setReceiveUser(receiveUser);
		letter.setDeleteFlag("0");
		letter.setReadFlag("0");
		return dao.getCount(letter);
	}
	
	/**
	 * 批量操作
	 */
	@Override
	public Map<String, Object> set(final Letter model) {
		final Map<String, Object> data = Maps.newHashMap();
		// 参数校验
		checkParam(model);
		switch (model.getBatchType()) {
			case 1: // 批量删除
				model.setDeleteFlag(DeleteFlagEnum.YES.getValue());
				break;
			case 2:// 批量已读
				model.setReadFlag(CommonEnum.YES.getValue());
				break;
			case 3:// 批量未读
				model.setReadFlag(CommonEnum.NO.getValue());	
				break;
			default:
				data.put("result", false);
				data.put("msg", ResourceUtils.get(ResourceConstant.GLOBAL_MSG_FAIL));
				return data;
		}
		boolean result =  dao.setBatch(model) > 0;
		data.put("result", result);
		if(result){
			data.put("msg", ResourceUtils.get(ResourceConstant.GLOBAL_MSG_SUCCESS));
			return data;
		}else{
			data.put("msg", ResourceUtils.get(ResourceConstant.GLOBAL_MSG_FAIL));
			return data;
		}
	}

	/**
	 * 参数校验
	 * @author fxl
	 * @date 2016年10月14日
	 * @param model
	 */
	private void checkParam(Letter model){
		if(StringUtils.isBlank(model.getIds())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.NEED_SELECT_ONE_RECORD), BussinessException.TYPE_JSON);
		}
		if(model.getBatchType()>3 || model.getBatchType()<0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_MSG_ILLEGAL), BussinessException.TYPE_JSON);
		}
	}
	
	
	@Override
	public Page<Letter> findLetter(Letter model) {
		Page<Letter> page = model.getPage();
		if(page==null){
			page=new Page<Letter>();
		}
		page.setRows(dao.findLetter(model));
		return page;
	}
	
	

}