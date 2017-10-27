package com.rd.ifaes.core.score.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.score.domain.ReceivingInfo;
import com.rd.ifaes.core.score.mapper.ReceivingInfoMapper;
import com.rd.ifaes.core.score.service.ReceivingInfoService;

@Service("receivingInfoService")
public class ReceivingInfoServiceImpl extends BaseServiceImpl<ReceivingInfoMapper, ReceivingInfo> implements ReceivingInfoService {

	@Override
	public void setDefaultReceivingInfo(String uuid,String userId) {
		dao.clearDefaultReceivingInfoSetting(userId);
		dao.setDefaultReceivingInfo(uuid);
	}

	@Override
	public void clearDefaultReceivingInfoSetting(String userId) {
		dao.clearDefaultReceivingInfoSetting(userId);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void save(ReceivingInfo receivingInfo){
		//如果新加的收货地址要设置为默认地址，则先清除原来的默认地址配置
		if (Constant.FLAG_YES.equals(receivingInfo.getIsDefult())) {
			dao.clearDefaultReceivingInfoSetting(receivingInfo.getUserID());
		}
		super.save(receivingInfo);
	}
	@Transactional(readOnly = false)
	@Override
	public void update(ReceivingInfo receivingInfo){
		//如果新加的收货地址要设置为默认地址，则先清除原来的默认地址配置
		if (Constant.FLAG_YES.equals(receivingInfo.getIsDefult())) {
			dao.clearDefaultReceivingInfoSetting(receivingInfo.getUserID());
		}
		super.update(receivingInfo);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void deleteReceivingInfo(String id,String userId){
		ReceivingInfo info = get(id);
		super.delete(id);
		if (Constant.FLAG_YES.equals(info.getIsDefult())) {  //如果删除的地址是默认地址，则顺延下一个地址为默认地址
			ReceivingInfo queryModel = new ReceivingInfo();
			queryModel.setUserID(userId);
			List<ReceivingInfo> list = findList(queryModel);
			if (list!=null && !list.isEmpty()) {
				setDefaultReceivingInfo(list.get(0).getUuid(),userId);
			}
		}
		
	}

	@Override
	public int countUserReceivingInfoNum(String userId) {
		return dao.countUserReceivingInfoNum(userId);
	}
}
