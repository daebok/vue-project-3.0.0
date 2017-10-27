/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.account.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.JxbankConstant;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.account.domain.AccountBankCard;
import com.rd.ifaes.core.account.mapper.AccountBankCardMapper;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindCardWebModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbQueryUserInfModel;
import com.rd.ifaes.core.tpp.model.cbhb.data.UsrInfExt;
import com.rd.ifaes.core.tpp.model.jx.json.JxCardDetailSubPacksModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 银行卡业务类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月1日
 */
@Service("accountBankCardService") 
public class AccountBankCardServiceImpl extends BaseServiceImpl<AccountBankCardMapper,  AccountBankCard> implements AccountBankCardService {
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountBankCardServiceImpl.class);
	
	@Resource
	private transient UserService userService;
	
	
	
	@Override
	public void insertBankCard(AccountBankCard card) {
		card.preInsert();
		LOGGER.info("insertBankCard--uuid:{}",card.getUuid());
		dao.insert(card);
	}

	@Override
	public List<AccountBankCard> getCardByUserId(String userId) {
		return dao.getCardByUserId(userId);
	}

	@Override
	public void bindCardWebHandle(CbhbBindCardWebModel cardModel) {
		//1、验签
		if(cardModel.validSign(cardModel)){
			String resultFlag = String.format(CacheKeys.PREFIX_TPP_ORDER_HANDLE_KEY.getKey(), cardModel.getMerBillNo());
			if (CbhbConstant.CBHB_CODE_SUCCESS.equals(cardModel.getRespCode())) {
				LOGGER.info("修改银行卡进入本地回调处理，订单号userNo={}，回调参数：{}",cardModel.getMobileNo(), cardModel.toString());
				//重复处理判断(缓存标记) 开户回调处理 计数 失效时间： 1天
				final String handleKey = String.format(CacheKeys.PREFIX_TPP_REGISTER_HANDLE_NUM.getKey(), cardModel.getMerBillNo());
				if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
					LOGGER.info("该修改银行卡订单号已做处理",StringUtils.isNull(cardModel.getMerBillNo()));
					return;
				}
				// 更新失效时间
				CacheUtils.expire(handleKey, ExpireTime.ONE_DAY);
				
				String result = Constant.TPP_ORDER_HANDLE_SUCCESS;
				try{
					//返回的银行卡 入库处理  --- 不作处理
					/*final User user = userService.get(cardModel.getPlaCustId());
					AccountBankCard card = this.getCardByUserIdAndCardId(user.getUuid(),
							cardModel.getOpenAcctId(), CommonEnum.YES.getValue());
					if(null == card){
						card = new AccountBankCard(); 
						card.preInsert();
						card.setUserId(user.getUuid());
						card.setCreateTime(DateUtils.getNow());
						card.setBankCode(cardModel.getOpenBankId());
						card.setBankAccount(cardModel.getOpenAcctId());
						card.setBankName(DictUtils.getItemName(CbhbConstant.CBHB_BANK_CARD, cardModel.getOpenBankId()));
						dao.insert(card);
					}else{
						card.preUpdate();
						card.setBankCode(cardModel.getOpenBankId());
						card.setBankAccount(cardModel.getOpenAcctId());
						card.setBankName(DictUtils.getItemName(CbhbConstant.CBHB_BANK_CARD, cardModel.getOpenBankId()));
						card.setUpdateTime(DateUtils.getNow());
						dao.update(card);
					}*/
				}catch(Exception e){
					if (e instanceof BussinessException) {// 业务异常，保存业务处理信息
                        result = e.getMessage();
                    } else {
                        result = ResourceUtils.get(ResourceConstant.ORDER_HANDLE_SYSTEM_EXCEPTION);
                    }
					throw e;
				}finally{
					CacheUtils.set(resultFlag, result, ExpireTime.ONE_DAY);
				}
			}else{
				throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_BINDCARD,cardModel.getRespCode(),cardModel.getRespDesc()),BussinessException.TYPE_JSON);
			}
		}else{
			throw new CbhbException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL),BussinessException.TYPE_JSON);
		}
	}

	@Override
	public List<AccountBankCard> findList() {
		return findList(SessionUtils.getSessionUser().getUuid());
	}

	
	@Override
    @Cacheable(key = CacheConstant.KEY_PREFIX_USER_BANK_CARD + "{userId}", expire = ExpireTime.HALF_A_MIN)
	public List<AccountBankCard> findList(String userId) {
		List<AccountBankCard> bankList = new ArrayList<AccountBankCard>();
		//校验用户是否登录
		final User user = userService.get(userId);
		if(!"1".equals(user.getTppStatus())){
			LOGGER.info("未实名，没有银行卡信息: user:{}", user.getUuid());
			return bankList;
		}
		TppService tppService = (TppService)TppUtil.tppService();
		Map<String,Object> params = new HashMap<String,Object>();
		String tppName = ConfigUtils.getTppName();
		//查询用户银行卡列表
		if(TppServiceEnum.CBHB.getName().equals(tppName)){
			params.put("plaCustId", user.getTppUserCustId());
			params.put("mobileNo", user.getMobile());
			CbhbQueryUserInfModel infoModel = (CbhbQueryUserInfModel)tppService.queryUserInf(params);
			List<UsrInfExt> infList = infoModel.getList();
			if(CollectionUtils.isNotEmpty(infList)){
				for(UsrInfExt inf :infList){
					AccountBankCard card = new AccountBankCard();
					card.setHideBankNo(inf.getCapCrdNo().substring(inf.getCapCrdNo().length()-4, inf.getCapCrdNo().length()));
					card.setPicPath(Constant.PIC_PATH_BANK + inf.getCapCorg() + ".png");
					card.setBankNo(inf.getCapCrdNo());
					card.setCanDisable(true);
					card.setBankName(DictUtils.getItemName(DictTypeEnum.ACCOUNT_BANK.getValue(), inf.getCapCorg()));
					bankList.add(card);
				}
			}
		} else if (TppServiceEnum.JXBANK.getName().equals(tppName)){
			params.put("accountId", user.getTppUserCustId());
			params.put("state", JxbankConstant.JXBANK_STATE_ONE);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) tppService.tppQueryUserBankCard(params);
			String str = (String) map.get("subPacks");
			List<JxCardDetailSubPacksModel> list = JSONArray.parseArray((String) map.get("subPacks"), JxCardDetailSubPacksModel.class);
			for (JxCardDetailSubPacksModel jxSubPacksModel : list) {
				if ("00000000".equals(jxSubPacksModel.getCanclDate()) && "000000".equals(jxSubPacksModel.getCanclTime())) {
					AccountBankCard card = new AccountBankCard();
					String cardNo = jxSubPacksModel.getCardNo();
					card.setBankNo(cardNo);
					card.setUserName(user.getRealName());//获取真实姓名
					card.setMobile(user.getMobile());//获取电话号码
					card.setBankAccount(user.getTppUserCustId());//获取银行账号
					card.setCanDisable(true);
					card.setHideBankNo(cardNo.substring(cardNo.length()-4, cardNo.length()));
					bankList.add(card);
				} else {
					
				}
				
			}
			
		} 
		
		return bankList;
	}
	
	@Override
	public AccountBankCard getCardByUserIdAndCardId(String userId,
			String cardId, String status) {
		return dao.getCardByUserIdAndCardId(userId, cardId, status);
	}

}

