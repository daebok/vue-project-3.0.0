package com.rd.ifaes.core.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.*;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.BorrowBespeak;
import com.rd.ifaes.core.project.mapper.BorrowBespeakMapper;
import com.rd.ifaes.core.project.model.BorrowBespeakModel;
import com.rd.ifaes.core.project.service.BorrowBespeakService;
import com.rd.ifaes.core.sys.service.AreaService;

/**
 * ServiceImpl:BorrowBespeakServiceImpl
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
@Service("borrowBespeakService") 
public class BorrowBespeakServiceImpl  extends BaseServiceImpl<BorrowBespeakMapper, BorrowBespeak> implements BorrowBespeakService{

	@Resource
	private AreaService areaService;
	
	@Override
	public void add(final BorrowBespeakModel model, final String[] zone) {
		// 校验参数是否正常
		BeanValidators.validate(model.prototype());
		model.checkParams();
		// 添加预约
		final BorrowBespeak borrowBespeak = model.prototype();
		if(areaService.findByAreaCode(zone[0]) == null || areaService.findByAreaCode(zone[1]) == null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_ZONE_ERROR));
		}
		borrowBespeak.setProvince(zone[Constant.INT_ZERO]);
		borrowBespeak.setCity(zone[Constant.INT_ONE]);
		if(zone.length > Constant.INT_TWO){
			borrowBespeak.setArea(zone[Constant.INT_TWO]);
		}
		borrowBespeak.setStatus(Constant.FLAG_NO);
		borrowBespeak.setCreateTime(DateUtils.getNow());
		TokenUtils.validShiroToken(TokenConstants.TOKEN_ADD_BORROW_BESPEAK);//录入信息前进行token的校验
		save(borrowBespeak);
	}

	@Override
	public Page<BorrowBespeak> findPage(final BorrowBespeak model){
		// 默认排序按创建时间倒序
		if (model != null && model.getPage() != null && StringUtils.isBlank(model.getPage().getSort())) {
			model.getPage().setSort("create_time");
			model.getPage().setOrder("desc");
		}
		Page<BorrowBespeak> page=super.findPage(model);
		List<BorrowBespeak> borrowBespeakList=page.getRows();
		for(BorrowBespeak b:borrowBespeakList){
			b.setAddress(IPUtil.getDetailAddressByIP(b.getAddIp()));//把申请的IP查出地址放到address属性中
		}
		page.setRows(borrowBespeakList);
		return page;
	}

	@Override
	@Transactional
	public void update(BorrowBespeak entity) {
		TokenUtils.validShiroToken(TokenConstants.TOKEN_EDIT_BORROW_BESPEAK);
		BorrowBespeak borrowBespeak = dao.get(entity.getUuid());
		if(!Constant.FLAG_NO.equals(borrowBespeak.getStatus())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.BORROW_BESPEAK_STATUS_ERROR));
		}
		dao.update(entity);
	}
	
}