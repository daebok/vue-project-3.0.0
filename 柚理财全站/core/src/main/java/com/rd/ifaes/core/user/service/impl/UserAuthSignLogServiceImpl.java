package com.rd.ifaes.core.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.model.jx.JxAutoBidAuthPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxAutoCreditInvestAuthPlusModel;
import com.rd.ifaes.core.tpp.model.jx.JxCreditAuthQueryModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxAuthSignModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.util.JxConfig;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserAuthSignLog;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.mapper.UserAuthSignLogMapper;
import com.rd.ifaes.core.user.model.UserIdentifyModel;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:UserAuthSignLogServiceImpl
 *
 * @author zb
 * @version 3.0
 * @date 2016-7-22
 */
@Service("userAuthSignLogService")
public class UserAuthSignLogServiceImpl extends BaseServiceImpl<UserAuthSignLogMapper, UserAuthSignLog> implements UserAuthSignLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthSignLogServiceImpl.class);

    @Resource
    private transient UserIdentifyService userIdentifyService;
    @Resource
    private transient UserAuthSignLogMapper userAuthSignLogMapper;
    @Resource
    private transient UserService userService;

    @Override
    public UfxAuthSignModel auth(final User user, final String addIp, final String authOption) {
        final UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUuid());
        if (!UserIdentifyModel.STATUS_REALNAME_SUCC.equals(userIdentify.getRealNameStatus()) || StringUtils.isBlank(user.getTppUserCustId())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REAL_NAME_STATUS), BussinessException.TYPE_JSON, URLConstant.SECURITY_REALNAMEIDENTIFY, ResourceUtils.get(ResourceConstant.USER_REAL_NAME_OPEN));
        }
        // 封装授权参数
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("tppUserCustId", user.getTppUserCustId());
        map.put("tppUserId", user.getTppUserAccId());
        map.put("serviceType", ConfigUtils.getValue("auth_service_type"));   // 不同第三方对应的业务类型
        map.put("authOption", authOption);
        TppService tppService = (TppService) TppUtil.tppService();
        UfxAuthSignModel authSign = (UfxAuthSignModel) tppService.tppAuthSign(map);
        // 添加申请记录
        final UserAuthSignLog authSignLog = new UserAuthSignLog(authSign);
        authSignLog.setUserId(user.getUuid());
        authSignLog.setAddIp(addIp);
        save(authSignLog);
        return authSign;
    }

    @Override
    public Object auth(Map<String, Object> map) {
        String userId = (String) map.get("userId");
        User user = userService.get(userId);
        final UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUuid());
        if (!UserIdentifyModel.STATUS_REALNAME_SUCC.equals(userIdentify.getRealNameStatus()) || StringUtils.isBlank(user.getTppUserCustId())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REAL_NAME_STATUS), BussinessException.TYPE_JSON, URLConstant.SECURITY_REALNAMEIDENTIFY, ResourceUtils.get(ResourceConstant.USER_REAL_NAME_OPEN));
        }
        String authOption = (String) map.get("authOption");

        Object object = null;

        // 添加申请记录
        final UserAuthSignLog authSignLog = new UserAuthSignLog();
        TppService tppService = (TppService) TppUtil.tppService();
        String orderId = OrderNoUtils.getSerialNumber();
        map.put("orderId", orderId);
        if (JxConfig.JXBANK_AUTH_OPEN.equals(authOption)) {
            map.put("accountId", user.getTppUserCustId());
            map.put("txAmount", ConfigUtils.getBigDecimal("auth_single_amount_limit", 2));
            map.put("totAmount", ConfigUtils.getBigDecimal("auth_max_amount_limit", 2));
            map.put("lastSrvAuthCode", SessionUtils.getSessionAttr("srvAuthCode:" + user.getMobile()));
            object = tppService.tppAuthSign(map);
            SessionUtils.removeAttribute("srvAuthCode:" + user.getMobile());
        } else {
            String orderNo = getOrderNo("1", userId);
            if (StringUtils.isNotBlank(orderNo)) {
                map.put("accountId", user.getTppUserCustId());
                map.put("orgOrderId", orderNo);
                object = tppService.tppAuthSign(map);
                authSignLog.setRespDesc(UserAuthSignLog.RESP_DESC_SUCC);
                userIdentifyService.setAuthSignStatus(user.getUuid(), JxConfig.JXBANK_AUTH_CLOSE);
            } else {
                LOGGER.error("撤销授权失败，没有授权记录:{}", userId);
                throw new BussinessException("撤销授权失败，没有授权记录");
            }
        }
        authSignLog.setAuthOption(authOption);
        authSignLog.setServiceTypes("1");//代表投标
        authSignLog.setUserId(user.getUuid());
        authSignLog.setAddIp((String) map.get("addIp"));
        authSignLog.setOrderNo(orderId);
        authSignLog.setCreateTime(DateUtils.getNow());
        save(authSignLog);
        return object;
    }

    @Override
    public String getOrderNo(String serviceTypes, String userId) {
        String orderNo = StringUtils.EMPTY;
        if (StringUtils.isBlank(serviceTypes)) {
            serviceTypes = UserAuthSignLog.SERVICE_TYPE_AUTO_INVEST;
        }

        UserAuthSignLog userAuthSignLog = new UserAuthSignLog();
        Page<UserAuthSignLog> page = new Page<UserAuthSignLog>();
        page.setOrder("desc");
        page.setSort("create_time");
        userAuthSignLog.setPage(page);
        userAuthSignLog.setServiceTypes(serviceTypes);
        userAuthSignLog.setUserId(userId);
        userAuthSignLog.setAuthOption(JxConfig.JXBANK_AUTH_OPEN);
        userAuthSignLog.setRespDesc(UserAuthSignLog.RESP_DESC_SUCC);
        List<UserAuthSignLog> list = dao.findList(userAuthSignLog);
        if (CollectionUtils.isNotEmpty(list)) {
            userAuthSignLog = list.get(0);
            if (UserAuthSignLog.RESP_DESC_SUCC.equals(userAuthSignLog.getRespDesc())) {
                orderNo = userAuthSignLog.getOrderNo();
            }
        }

        return orderNo;
    }

    @Override
    public void doAuth(final UfxAuthSignModel authModel) {
        String respDesc;
        if (UfxConstant.UFX_CODE_SUCCESS.equals(authModel.getRespCode())) {
            respDesc = UserAuthSignLog.RESP_DESC_SUCC;
            // 设置授权/解除状态
            final User user = userService.findByUserCustId(authModel.getUserCustId());
            userIdentifyService.setAuthSignStatus(user.getUuid(), authModel.getAuthOption());
        } else {
            respDesc = UserAuthSignLog.RESP_DESC_FAIL;
            LOGGER.info("处理失败！原因：{}", authModel.getRespDesc());
        }
        userAuthSignLogMapper.updateRespDescByOrder(new UserAuthSignLog(respDesc, authModel.getOrderNo()));
    }


    @Override
    public void doAuth(JxAutoBidAuthPlusModel authModel) {
        String respDesc;
        respDesc = UserAuthSignLog.RESP_DESC_SUCC;
        
        if (JxConfig.SUCCESS.equals(authModel.getRetCode())) {

            // 设置授权/解除状态
            final User user = userService.findByUserCustId(authModel.getAccountId());
            userIdentifyService.setAuthSignStatus(user.getUuid(), JxConfig.JXBANK_AUTH_OPEN);
        } else {
        	respDesc = UserAuthSignLog.RESP_DESC_FAIL;
            LOGGER.info("处理失败！原因：{}", authModel.getRetMsg());
        }
        userAuthSignLogMapper.updateRespDescByOrder(new UserAuthSignLog(respDesc, authModel.getOrderId()));
    }

    @Override
    public boolean checkAuthSign() {
        boolean result = true;
        String tppName = ConfigUtils.getTppName();
        if (TppServiceEnum.CHINAPNR.getName().equals(tppName) || TppServiceEnum.CBHB.getName().equals(tppName) || TppServiceEnum.JXBANK.getName().equals(tppName)) {
            result = false;
        }
        return result;
    }

    @Override
    public JxAutoCreditInvestAuthPlusModel autoCreditInvestAuthPlus(String smsCode, String userId, String addIp) {
        User user = userService.get(userId);
        final UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUuid());
        if (!UserIdentifyModel.STATUS_REALNAME_SUCC.equals(userIdentify.getRealNameStatus()) || StringUtils.isBlank(user.getTppUserCustId())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REAL_NAME_STATUS), BussinessException.TYPE_JSON, URLConstant.SECURITY_REALNAMEIDENTIFY, ResourceUtils.get(ResourceConstant.USER_REAL_NAME_OPEN));
        }
        if (JxConfig.JXBANK_AUTH_OPEN.equals(userIdentify.getAutoCreditInvestAuthStatus())) {
            throw new BussinessException("您已签约自动债转，请退出后刷新查看!", BussinessException.TYPE_JSON);
        }
        // 添加申请记录
        final UserAuthSignLog authSignLog = new UserAuthSignLog();
        TppService tppService = (TppService) TppUtil.tppService();
        String orderId = OrderNoUtils.getSerialNumber();
        authSignLog.setAuthOption(JxConfig.JXBANK_AUTH_OPEN);//签约
        authSignLog.setServiceTypes(UserAuthSignLog.SERVICE_TYPE_AUTO_BOND);//代表转让
        authSignLog.setUserId(user.getUuid());
        authSignLog.setOrderNo(orderId);
        authSignLog.setCreateTime(DateUtils.getNow());
        authSignLog.setRespDesc(UserAuthSignLog.RESP_DESC);
        authSignLog.setAddIp(addIp);
        save(authSignLog);
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("accountId", user.getTppUserCustId());
        map.put("lastSrvAuthCode", SessionUtils.getSessionAttr("srvAuthCode:" + user.getMobile()));
        map.put("smsCode", smsCode);
        JxAutoCreditInvestAuthPlusModel model = (JxAutoCreditInvestAuthPlusModel) tppService.autoCreditInvestAuthPlus(map);
        SessionUtils.removeAttribute("srvAuthCode:" + user.getMobile());
        return model;
    }

    @Override
    public void doAutoCreditInvestAuth(JxAutoCreditInvestAuthPlusModel model) {
        if (JxConfig.SUCCESS.equals(model.getRetCode())) {
            final User user = userService.findByUserCustId(model.getAccountId());
            userIdentifyService.setAutoCreditInvestAuthStatus(user.getUuid(), JxConfig.JXBANK_AUTH_OPEN);
            userAuthSignLogMapper.updateRespDescByOrder(new UserAuthSignLog(UserAuthSignLog.RESP_DESC_SUCC, model.getOrderId()));
        } else {
            LOGGER.info("处理失败！原因：{}", model.getRetMsg());
            userAuthSignLogMapper.updateRespDescByOrder(new UserAuthSignLog(UserAuthSignLog.RESP_DESC_FAIL, model.getOrderId()));
        }

    }

	@Override
	public void autoCreditInvestAuthCancel(String addIp) {
		final User sessionUser = SessionUtils.getSessionUser(BussinessException.TYPE_CLOSE);
		User user = userService.get(sessionUser.getUuid());
        final UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUuid());
        if (!UserIdentifyModel.STATUS_REALNAME_SUCC.equals(userIdentify.getRealNameStatus()) || StringUtils.isBlank(user.getTppUserCustId())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_REAL_NAME_STATUS), BussinessException.TYPE_JSON, URLConstant.SECURITY_REALNAMEIDENTIFY, ResourceUtils.get(ResourceConstant.USER_REAL_NAME_OPEN));
        }
        if(!JxConfig.JXBANK_AUTH_OPEN.equals(userIdentify.getAutoCreditInvestAuthStatus())){
        	throw new BussinessException("撤销签约失败，您的自动债转未签约！", BussinessException.TYPE_CLOSE);
        }
        final JxCreditAuthQueryModel jxCreditAuthQueryModel = creditAuthQuery(UserAuthSignLog.SERVICE_TYPE_AUTO_BOND, user.getTppUserCustId());
        if(!UserAuthSignLog.RESP_DESC_SUCC.equals(jxCreditAuthQueryModel.getState())){
        	userIdentifyService.setAutoCreditInvestAuthStatus(user.getUuid(), JxConfig.JXBANK_AUTH_CLOSE);//未签约
        	throw new BussinessException("撤销签约失败，您的自动债转未签约！", BussinessException.TYPE_CLOSE);
        }
        
        Map<String, Object> map = new HashMap<>();
        String orderId = OrderNoUtils.getSerialNumber();
        map.put("orderId", orderId);
        map.put("accountId", user.getTppUserCustId());
        map.put("orgOrderId", jxCreditAuthQueryModel.getOrderId());
        TppService tppService = (TppService) TppUtil.tppService();
        tppService.autoCreditInvestAuthCancel(map);
        userIdentifyService.setAutoCreditInvestAuthStatus(user.getUuid(), JxConfig.JXBANK_AUTH_CLOSE);//更新签约状态
        // 添加撤销成功记录
        final UserAuthSignLog authSignLog = new UserAuthSignLog();
        authSignLog.setAuthOption(JxConfig.JXBANK_AUTH_CLOSE);//授权解约
        authSignLog.setServiceTypes(UserAuthSignLog.SERVICE_TYPE_AUTO_BOND);//自动债权转让
        authSignLog.setUserId(user.getUuid());
        authSignLog.setRespDesc(UserAuthSignLog.RESP_DESC_SUCC);//成功
        authSignLog.setOrderNo(orderId);
        authSignLog.setCreateTime(DateUtils.getNow());
        authSignLog.setAddIp(addIp);
        save(authSignLog);
	}

	@Override
	public JxCreditAuthQueryModel creditAuthQuery(String type, String accountId) {
		Map<String, Object> map = new HashMap<>();
		map.put("type", type);
		map.put("accountId", accountId);
		TppService tppservice = (TppService)TppUtil.tppService();
		@SuppressWarnings("unchecked")
		Map<String,Object> resp = (Map<String, Object>) tppservice.creditAuthQuery(map);
		JxCreditAuthQueryModel model = new JxCreditAuthQueryModel();
		model.setState((String) resp.get("state"));//0：未签约 1：已签约
		model.setOrderId((String) resp.get("orderId"));
		model.setType((String) resp.get("type"));
		return model;
	}

}