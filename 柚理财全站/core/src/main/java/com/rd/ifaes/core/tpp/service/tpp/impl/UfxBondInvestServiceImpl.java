/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.service.tpp.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.BondCollectionEnum;
import com.rd.ifaes.common.dict.BondEnum;
import com.rd.ifaes.common.dict.BondInvestEnum;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.ScoreEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondCollection;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.model.BondInvestOtherModel;
import com.rd.ifaes.core.bond.service.BondCollectionService;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.cache.BondCache;
import com.rd.ifaes.core.cache.InvestCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.service.UserVipService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.protocol.util.bond.BondProtocol;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.ufx.UfxCreditTransferModel;
import com.rd.ifaes.core.tpp.service.tpp.UfxBondInvestService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 债权投资回调业务处理类
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年8月21日
 */
@Service("ufxBondInvestService")
public class UfxBondInvestServiceImpl implements UfxBondInvestService {
	/**
	 * 日志类
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UfxBondInvestServiceImpl.class);
	/**
	 * 用户VIP业务
	 */
	@Resource
	private transient UserVipService userVipService;
	/**
	 * 用户积分业务
	 */
	@Resource
	private transient UserScoreService userScoreService;
	/**
	 * bondInvestService业务
	 */
	@Resource
	private transient BondInvestService bondInvestService;
	/**
	 * projectCollectionService业务
	 */
	@Resource
	private transient ProjectCollectionService projectCollectionService;
	/**
	 * projectInvestService业务
	 */
	@Resource
	private transient ProjectInvestService projectInvestService;
	/**
	 * userService业务
	 */
	@Resource
	private transient UserService userService;
	/**
	 * projectService业务
	 */
	@Resource
	private transient ProjectService projectService;
	/**
	 * bondService业务
	 */
	@Resource
	private transient BondService bondService;
	/**
	 * ProtocolService业务
	 */
	@Resource
	private transient ProtocolService protocolService;
	/**
	 * BondCollectionService业务
	 */
	@Resource
	private transient BondCollectionService bondCollectionService;
	/**
	 * UserCacheService业务
	 */
	@Resource
	private transient UserCacheService userCacheService;
	
	/**
	 * 债权处理校验
	 */
	@Override
	@Transactional(readOnly = false)
	public String doBondInvestValid(final UfxCreditTransferModel model) {
		String bondId = StringUtils.EMPTY;
		LOGGER.info("【订单号：{}】债权投资回调业务逻辑处理...start:{}",StringUtils.isNull(model.getOrderNo()), StringUtils.isNull(model.getOrderNo()));
		//判断订单号存在与否 进入队列的订单号必须存在
		if (StringUtils.isBlank(model.getOrderNo())) {
			LOGGER.info("债权投资回调doBondInvestSuccessHandle-----债权投资订单号不存在-----------");
			throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_ORDER_NO_NOT_EMPTY),BussinessException.TYPE_JSON);
		}
		final boolean ret = model.validSign(model);
		if (ret) { // 验签成功
			//重复处理判断(缓存标记) 投资回调处理 计数 失效时间： 1天
			final String handleKey = String.format(CacheKeys.PREFIX_BOND_INVEST_ORDER_NO_HANDLE_NUM.getKey(),model.getOrderNo());
			if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				LOGGER.info("【订单号：{}】----债权投资订单已处理",StringUtils.isNull(model.getOrderNo()) );
				throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_ORDER_NO_HAS_HANDLE),BussinessException.TYPE_JSON);
			}
			// 更新失效时间
			CacheUtils.expire(handleKey, ExpireTime.ONE_DAY);

			// 3、回调中是成功状态的则进入业务处理 失败进入失败的业务处理
			if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
				LOGGER.info("【订单号：{}】进入成功处理队列：{},{}",StringUtils.isNull(model.getOrderNo()),model.getRespCode(), model.getRespDesc());
				if(ConfigUtils.isOpenMq()){
					RabbitUtils.bondInvest(model);
				}else{
					this.doBondInvestSuccessHandle(model);
				}
			} else {// 失败则按照失败的处理 根据第三方为主
				LOGGER.info("【订单号：{}】进入失败处理队列：{},{}",StringUtils.isNull(model.getOrderNo()),model.getRespCode(), model.getRespDesc());
				CacheUtils.decr(handleKey, Constant.DOUBLE_ONE);// 投资失败 回调数清0
				if(ConfigUtils.isOpenMq()){
					RabbitUtils.bondInvestFail(model);
				}else{
					this.doBondInvestFailHanlde(model);
				}
				throw new UfxException(model.getRespDesc(),BussinessException.TYPE_CLOSE);
			}
		} else {// 验签失败
			LOGGER.info("验签失败：{}" , ret);
			throw new UfxException(ResourceUtils.get(BondResource.TPP_NOTIFY_SIGN_FAIL),BussinessException.TYPE_JSON);
		}

		LOGGER.info("债权投资回调业务逻辑处理...end:{}"	, StringUtils.isNull(model.getOrderNo()));
		final BondInvest bondInvest = bondInvestService.getBondInvestByOrderNo(model.getOrderNo());
		if(bondInvest != null){
			bondId= bondInvest.getBondId();
		}
		return bondId;
	}

	/**
	 * 债权成功处理
	 */
	@Override
	@Transactional(readOnly = false)
	public void doBondInvestSuccessHandle(final UfxCreditTransferModel model) {
		LOGGER.info("【订单号：{}】债权投资回调的业务处理 （成功）-start", StringUtils.isNull(model.getOrderNo()));

		ufxHandleBondInvestSuccess(model);

		LOGGER.info("【订单号：{}】债权投资回调的业务处理 （成功）-end", StringUtils.isNull(model.getOrderNo()));
	}

	/**
	 * 债权失败处理
	 */
	@Override
	@Transactional(readOnly = false)
	public void doBondInvestFailHanlde(final UfxCreditTransferModel model) {
		LOGGER.info("【订单号：{}】债权投资回调的业务处理 （失败）-start", StringUtils.isNull(model.getOrderNo()) );

		// 1、投资回调失败 修改投资状态从待支付改为投资失败
		final BondInvest invest = bondInvestService.getBondInvestByOrderNo(model.getOrderNo());
		invest.setStatus(BondInvestEnum.STATUS_FAIL.getValue());
		bondInvestService.updateBondInvstStatus(invest.getUuid(),BondInvestEnum.STATUS_FAIL.getValue(),BondInvestEnum.STATUS_INIT.getValue());

		//在投记录-1
		userCacheService.subUserInvestNum(invest.getUserId());		
		
		// 2、投资失败，缓存处理 -- 归还剩余可投金额
		final Bond bond = bondService.getBond(invest.getBondId(),StringUtils.EMPTY);
		final double remainMoney = BondCache.incrBondRemainAccount(bond.getBondNo(), invest.getAmount().doubleValue());
		if(remainMoney > 0){//投资中
			bondService.updateStage(Integer.parseInt(BondEnum.STAGE_INVESTING.getValue()), bond.getUuid());
		}
		// 3、用户未支付投资订单数 -1 投资成功未支付订单减1 可投份额已经扣了 此时不做处理
		if (!InvestCache.decrUserInvestUnpay(model.getUserCustId())) {
			LOGGER.error("债权投资成功回调-用户({})未支付投资订单数-处理错误!",invest.getUserId());
		}
		
		LOGGER.info("【订单号：{}】债权投资回调的业务处理 （失败）-end", StringUtils.isNull(model.getOrderNo()) );
	}

	@Transactional(readOnly = false)
	public void ufxHandleBondInvestSuccess(final UfxCreditTransferModel model) {
		LOGGER.info("【订单号：{}】债权投资回调的ufxHandleBondInvestSuccess--start----{}" , StringUtils.isNull(model.getOrderNo()),StringUtils.isNull(model.getInvestNo()));

		// 其他的处理
		final BondInvest invest = bondInvestService.getBondInvestByOrderNo(model.getOrderNo());// 根据订单查询债权投资记录
		final Bond bond = bondService.getBond(invest.getBondId(),StringUtils.EMPTY);// 获取缓存中的债权信息
		final Project project = projectService.get(bond.getProjectId());// 原始标信息
		final ProjectInvest pInvest = projectInvestService.get(bond.getInvestId());// 原始标投资记录信息（可能是bond的investId对应的部分转让）
		final User bondUser = userService.get(bond.getUserId());// 获取转让人用户信息
		final User investUser = userService.get(invest.getUserId());// 获取受让人用户信息
		final BigDecimal money = BigDecimalUtils.round(model.getAssignAmount());// 获取有效的债权投资金额
		final StringBuffer infoBuffer = new StringBuffer();
		infoBuffer.append("<a href=\"")/*.append(StringUtils.stripEnd(ConfigUtils.getValue(ConfigConstant.WEB_URL),"/"))*/
		         .append(URLConstant.BOND_DETAIL_PAGE_PREFIX).append(bond.getUuid()).append("\"  target=\"_blank\">")
				.append(bond.getBondName()).append("</a>");
		final String info = infoBuffer.toString();
		
		// 计算持有天数收益
		final BigDecimal holdInterest = this.caluInterest(pInvest, bond, invest);
		invest.setPayedInterest(BigDecimalUtils.round(holdInterest,Constant.INT_TWO));

		// 1、更新债权投资记录 信息
		LOGGER.info("---------【订单号：{},债权标ID:{}】更新债权投资记录 信息:1、状态为成功=1;2、录入investNo,investDate-------start,"
					+ "传递参数：investNo：{},investDate:{},status:{}",StringUtils.isNull(model.getOrderNo()),bond.getUuid(),model.getCreditTransferInvestNo(),model.getCreditTransferInvestDate(),invest.getStatus());
		this.modifyBondInvest(invest, model);
		LOGGER.info("---------【订单号：{},债权标ID:{}】更新债权投资记录 信息:1、状态为成功=1;2、录入investNo,investDate-------end",StringUtils.isNull(model.getOrderNo()),bond.getUuid());
		
		// 2、修改债权标的已投金额 满标的更改债权标状态 添加手续费
		this.modifyBondInfo(money, bond,BigDecimalUtils.valueOf(model.getCreditFee()), holdInterest,invest.getCreateTime());

		// 3 当投资投满的时候 将转让人原始标投资记录和原始标待收 作废
		final boolean  fullFlag = bond.getSoldCapital().add(money).compareTo(bond.getBondMoney()) >= Constant.INT_ZERO;
		if(fullFlag){
			LOGGER.info("------ 【订单号：{},债权标ID:{}】当投资投满的时候 将转让人原始标投资记录和原始标待收 作废 ------start",StringUtils.isNull(model.getOrderNo()),bond.getUuid());
			bondInvestService.cancleOriProjectInvestAndProjectCollection(pInvest,bondUser,false);
			LOGGER.info("------ 【订单号：{},债权标ID:{}】当投资投满的时候 将转让人原始标投资记录和原始标待收 作废 ------end",StringUtils.isNull(model.getOrderNo()),bond.getUuid());
		}
		
		//4 、判断是否再次转让的情形    查询原始投资订单号 在bondInvest 表中是否存在记录  若存在 则说明是再次转让若是再次转让的需要对上一层的债权待收进行处理
		handleBondCollection(pInvest, fullFlag, invest, bond);
		
		// 5、受让人生成新的原始标投资记录 和 新的原始标待收记录 以及债权待收
		LOGGER.info("------ 【订单号：{},债权标ID:{}】受让人生成新的原始标投资记录 和 新的原始标待收记录 以及债权待收 ------start",StringUtils.isNull(model.getOrderNo()),bond.getUuid());
//		final BigDecimal bondCollectionMoney = bondInvestService.createProjectInvestAndProjectCollection(invest, model,fullFlag);
//		LOGGER.info("------ 【订单号：{},债权标ID:{}】受让人生成新的原始标投资记录 和 新的原始标待收记录 以及债权待收 ------end,债权待收金额-bondCollectionMoney：{}",StringUtils.isNull(model.getOrderNo()),bond.getUuid(),bondCollectionMoney);
		
		//6、部分转让 、 受让满标、    加息利息大于0 的情形下  将加息处理为0 
		this.handleRaiseInterest(fullFlag, bond, pInvest);
		
		// 7、处理本地资金 投资的时候不冻结资金和份额 直接债权划款
//		bondInvestService.handleBondInvestAccount(project, bondUser,investUser, invest, bondCollectionMoney,fullFlag,pInvest,info);

		// 8、用户未支付投资订单数 -1 投资成功未支付订单减1
		if (!InvestCache.decrUserInvestUnpay(model.getUserCustId())) {
			LOGGER.error("债权投资成功回调-用户(" + investUser.getUuid()+ ")未支付投资订单数-处理错误!");
			return;
		}
		
		// 9、缓存债权处理 投资成功
		this.handleBondCache(bond);		
		if(BondCache.getBondRemainAccount(bond.getBondNo()) <= Constant.DOUBLE_ZERO){
			LOGGER.info("updateStage >>:债权投资回调处理");
			bondService.updateStage(Integer.parseInt(BondEnum.STAGE_EMPTING.getValue()), bond.getUuid());		
		}
		//10 、异步处理 协议和消息业务
		notifyHandle(invest, investUser, bondUser, bond,info);
		LOGGER.info("【订单号：{},债权标ID:{}】债权投资回调的ufxHandleBondInvestSuccess--end",StringUtils.isNull(model.getOrderNo()),bond.getUuid());
	}
	
	/**
	 * 异步处理其他业务
	 * @author QianPengZhan
	 * @date 2016年11月30日
	 * @param invest
	 * @param investUser
	 * @param bondUser
	 * @param bond
	 * @param info 
	 */
	private void notifyHandle(final BondInvest invest,final User investUser,final User bondUser,final Bond bond,final String info){
		//10、债权投资成功   生成协议
		LOGGER.info("------ 债权投资成功   生成协议 ------start,进入队列");
		this.buildBondProtocol(invest, investUser, bondUser);
		LOGGER.info("------债权投资成功   生成协议 ------end");
		
		//11、发送消息
		handleMessage(investUser, invest, bond);
		
		//12、发放积分和VIP投资 等其他的业务
		handleOtherService(bond, invest, investUser,info);
	}
	
	/**
	 * 处理其他的业务
	 * @author QianPengZhan
	 * @date 2016年12月7日
	 * @param bond
	 * @param invest
	 * @param investUser
	 */
	private void handleOtherService(final Bond bond,final BondInvest invest,final User investUser,final String info){
		BondInvestOtherModel model = new BondInvestOtherModel();
		model.setInfo(info);
		model.setInvestUser(investUser);
		model.setTenderMoney(invest.getAmount());
		model.setUuid(invest.getUuid());
		LOGGER.info("[BondInvestOtherModel---info:{},investUser:{},TenderMoney:{},Uuid:{}]"
				,model.getInfo(),model.getInvestUser(),model.getTenderMoney(),model.getUuid());
		if(ConfigUtils.isOpenMq()){
			RabbitUtils.bondInvestOtherService(model);
		}else{
			doBondInvestOtherService(model);
		}
	}
	
	/**
	 * 处理其他的业务的具体方法
	 * @author QianPengZhan
	 * @date 2016年12月7日
	 */
	@Override
	public void doBondInvestOtherService(final BondInvestOtherModel model) {
		LOGGER.info("发放积分--start--userId:{},typeCode:{},bondInvestId:{}",model.getInvestUser().getUuid(),
				ScoreEnum.SCORE_BOND_INVEST_SUCCESS.getValue(),model.getUuid());
		userScoreService.grantScore(model.getInvestUser(), ScoreEnum.SCORE_BOND_INVEST_SUCCESS.getValue(),model.getUuid());
		LOGGER.info("发放积分--end--");
		
		final String remark = "【"+model.getInfo() + "】债权投资成功!";
		LOGGER.info("VIP处理--start---userId:{},tenderMoney:{},remark:{}",model.getInvestUser(), model.getTenderMoney(),remark);
		userVipService.addVipGrowthByTender(model.getInvestUser(), model.getTenderMoney(),remark);
		LOGGER.info("VIP处理--end--");
	}
	
	/**
	 * 缓存债权处理 投资成功
	 * @author QianPengZhan
	 * @date 2016年11月30日
	 * @param bond
	 */
	private void handleBondCache(final Bond bond){
		if (BondEnum.STATUS_COMPLETE.eq(bond.getStatus())) {
			// 删除剩余可投债权金额
			BondCache.delBondRemainAccount(bond.getBondNo());
		}
		CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND_LIST);
		CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND.concat(bond.getUuid()));
	}
	
	/**
	 * 债权投资异步生成债权协议
	 * @author QianPengZhan
	 * @date 2016年11月30日
	 * @param invest
	 * @param investUser
	 * @param bondUser
	 */
	private void buildBondProtocol(final BondInvest invest,final User investUser,final User bondUser){
		final BondProtocol bondInvestProcotol = new BondProtocol();
		bondInvestProcotol.setBondInvestId(invest.getUuid());
		bondInvestProcotol.setUserId(investUser.getUuid());
		final BondProtocol bondProcotol = new BondProtocol();
		bondProcotol.setBondInvestId(invest.getUuid());
		bondProcotol.setUserId(bondUser.getUuid());
		if(ConfigUtils.isOpenMq()){
			RabbitUtils.buildBondProtocol(bondInvestProcotol);
			RabbitUtils.buildBondProtocol(bondProcotol);			
		}else{
			protocolService.buildBondProtocol(invest.getUuid(), bondUser.getUuid());
			protocolService.buildBondProtocol(invest.getUuid(), investUser.getUuid());
		}
	}
	
	/**
	 * 债权部分转让   转让部分全部转让出去了 现在剩余拆分部分的加息也需要置为0 
	 * @author QianPengZhan
	 * @param fullFlag
	 * @param bond
	 * @param pInvest
	 */
	private void handleRaiseInterest(final boolean fullFlag,final Bond bond,final ProjectInvest pInvest){
		if(fullFlag  && CommonEnum.NO.eq(bond.getSellStyle()) && pInvest.getRaiseInterest().compareTo(BigDecimal.ZERO) > Constant.INT_ZERO){
			LOGGER.info("债权部分转让   转让部分全部转让出去了 现在剩余拆分部分的加息也需要置为0 ");
			final ProjectInvest otherProjectInvest = projectInvestService.getInvestByOriIdAndUuid(pInvest.getOriginInvestId(), pInvest.getUuid());
			if(otherProjectInvest == null){
				LOGGER.info("otherProjectInvest  不存在!");
			}else{
				otherProjectInvest.setRaiseInterest(BigDecimal.ZERO);//加息利息作废
				projectInvestService.update(otherProjectInvest);
				final ProjectCollection collectionModel = new ProjectCollection();
				collectionModel.setInvestId(otherProjectInvest.getUuid());
				collectionModel.setUserId(otherProjectInvest.getUserId());
				collectionModel.setProjectId(otherProjectInvest.getProjectId());
				final List<ProjectCollection> projectCollectionList =  projectCollectionService.findPcByPIdAndUserId(collectionModel);
				for(ProjectCollection projectCollection:projectCollectionList){
					projectCollection.setRaiseInterest(BigDecimal.ZERO);//加息利息作废
				}
				projectCollectionService.updateBatch(projectCollectionList);
			}
		}
	}
	
	/**
	 * 发送消息
	 * @author QianPengZhan
	 * @param investUser
	 * @param invest
	 * @param bond
	 */
	private void handleMessage(final User investUser,final BondInvest invest,final Bond bond){
		// 发送债权投资债权转让成功
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("user", investUser);
			params.put("investMoney", invest.getAmount().doubleValue());
			params.put("investTime", DateUtils.dateStr4(invest.getCreateTime()));
			params.put("bondName",bond.getBondName().length()>10?bond.getBondName().substring(0, 10)+"...":bond.getBondName());
	        BaseMsg baseMsg = new BaseMsg(MessageConstant.BOND_BUY_SUCC, params);
	        baseMsg.doEvent();
		} catch (Exception e) {
			LOGGER.error("给投资人发送债券投资消息处理失败，investId:{}", invest.getUuid(), e);
		}
		if (BondEnum.STATUS_COMPLETE.eq(bond.getStatus())) {
			try {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("user", userService.get(bond.getUserId()));
				params.put("bondName",bond.getBondName().length()>10?bond.getBondName().substring(0, 10)+"...":bond.getBondName());
		        BaseMsg baseMsg = new BaseMsg(MessageConstant.BOND_SELL_SUCC, params);
		        baseMsg.doEvent();
			} catch (Exception e) {
				LOGGER.error("给转让人发送转让成功消息处理失败，bondId:{}", bond.getUuid(), e);
			}
		}
	}
	
	
	/**
	 * 判断是否再次转让的情形    查询原始投资订单号 在bondInvest 表中是否存在记录  若存在 则说明是再次转让若是再次转让的需要对上一层的债权待收进行处理
	 * @author QianPengZhan
	 * @param pInvest
	 * @param fullFlag
	 * @param invest
	 * @param bond
	 */
	private void handleBondCollection(final ProjectInvest pInvest,final boolean fullFlag,final BondInvest invest,final Bond bond){
		final BondInvest oriBondInvest = bondInvestService.getBondInvestByOrderNo(pInvest.getInvestOrderNo());
		if(oriBondInvest != null){
			final BondCollection bondCollection = new BondCollection();
			bondCollection.setBondInvestId(oriBondInvest.getUuid());
			final List<BondCollection> bondCollectionList = bondCollectionService.findList(bondCollection);
			if(CollectionUtils.isNotEmpty(bondCollectionList)){
				for (final  BondCollection bondColl:bondCollectionList) {
					final BigDecimal rate = BigDecimalUtils.div(invest.getAmount(), bond.getBondMoney(),Constant.INT_TEN);
					bondColl.setBondCapital(bondColl.getCapital().compareTo(BigDecimal.ZERO) > Constant.INT_ZERO?invest.getAmount():BigDecimal.ZERO);
					bondColl.setBondInterest(bondColl.getInterest().multiply(rate));
					if(CommonEnum.YES.eq(bond.getSellStyle()) && fullFlag){//若果是全额转让的
						bondColl.setStatus(BondCollectionEnum.STATUS_PAID.getValue());
						bondColl.setReceivedAmount(BigDecimalUtils.add(bondColl.getBondCapital(),bondColl.getInterest()));
						bondColl.setReceivedTime(DateUtils.getNow());
					}
				}
				bondCollectionService.updateBatch(bondCollectionList);
			}
		}
	}
	

	/**
	 * 获取持有天数收益
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月17日
	 * @param pInvest
	 * @return
	 */
	private BigDecimal caluInterest(final ProjectInvest pInvest,
			final Bond bond, final BondInvest invest) {
		Map<String, Object> map = Maps.newHashMap();
		final ProjectInvestModel pim = pInvest.prototype();
		map = pim.getAbleBondData(pim);
		BigDecimal holdInterest = BigDecimalUtils.valueOf(StringUtils.isNull(map.get("holdDayCollection")));
		holdInterest = BigDecimalUtils.mul(holdInterest,BigDecimalUtils.div(invest.getAmount(), bond.getBondMoney(),Constant.INT_TEN));
		return holdInterest;
	}


	/**
	 * 修改债权标的已投金额
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月9日
	 * @param money
	 * @param bond
	 */
	private void modifyBondInfo(final BigDecimal money, final Bond bond,
			final BigDecimal fee, final BigDecimal holdInterest,final Date successTime) {
		LOGGER.info("---------修改债权标的投资信息-----start,传递的参数:money:{},bond:{},fee:{},holdInterest:{},successTime:{}",money,bond,fee,holdInterest,successTime);
		
		// 1、变化已售金额   递增修改
		LOGGER.info("----变化已售金额   递增修改-----start,传递的参数：money：{}，bondId:{}",money.doubleValue(),bond.getUuid());
		bondService.updateSoldCapitalById(money.doubleValue(),bond.getUuid());
		LOGGER.info("----变化已售金额   递增修改-----end");
		
		//2、若投资满  则还需要改变状态
		LOGGER.info("----若投资满  则还需要改变状态-----start,需要的参数：原来的soldCapital:{},money:{}",bond.getSoldCapital(),money);
		BigDecimal soldCapital = bond.getSoldCapital().add(money);//投资完之后的已售金额
		if (soldCapital.compareTo(bond.getBondMoney()) >= Constant.INT_ZERO) {//投资之后的已售和债权总额比较 若等于则满标
			LOGGER.info("----满标----");
			bondService.updateBondStatusById(BondEnum.STATUS_COMPLETE.getValue(), BondEnum.STATUS_PUBLISH.getValue(), bond.getUuid());
		}
		LOGGER.info("----若投资满  则还需要改变状态-----end");
		
		//3、更新其他的信息   持有利息、债权转让手续费、债权转让成功时间
		LOGGER.info("----更新其他的信息   持有利息、债权转让手续费、债权转让成功时间态-----start,传递的参数：holdInterest:{},fee:{},successTime:{},bondId:{}",holdInterest,fee,successTime,bond.getUuid());
		bondService.updateBondOtherInfo(holdInterest,fee,successTime,bond.getUuid());
		LOGGER.info("----更新其他的信息   持有利息、债权转让手续费、债权转让成功时间-----end");
		
		LOGGER.info("---------修改债权标的投资信息-----end");
	}

	/**
	 * 更新债权投资记录
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月9日
	 * @param invest
	 * @param model
	 */
	private void modifyBondInvest(final BondInvest invest,
			final UfxCreditTransferModel model) {
		invest.setStatus(BondInvestEnum.STATUS_COMPLETE.getValue());
		invest.setInvestNo(model.getCreditTransferInvestNo());
		invest.setInvestDate(model.getCreditTransferInvestDate());
		bondInvestService.update(invest);
	}


}
