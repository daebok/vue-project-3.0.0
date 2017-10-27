package com.rd.ifaes.mobile.controller.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.account.model.AccountBankModel;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.mobile.controller.WebController;

/**
 * 
 *  账户管理-银行卡
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
@Controller
public class BankCardController extends WebController {

	/**
	 * 用户基础类
	 */
	@Resource
	private transient UserService userService;
	
	@Resource
	private transient UserCacheService userCacheService;
	@Resource
	private transient AccountBankCardService accountBankCardService;
	/**
	 * 用户认证信息处理类
	 */
	@Resource
	private transient UserIdentifyService identifyService;
	static Map<String, Object> datab = Maps.newHashMap();
	/**
	 * 银行卡列表
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping("/member/bankCard/list")
	public String list(final Model model) {
		UserCache userCache = (UserCache)SessionUtils.getSessionAttr(Constant.SESSION_USER_CACHE);
		UserIdentify userIdentify = (UserIdentify) SessionUtils.getSessionAttr(Constant.SESSION_USER_IDENTIFY);
		 model.addAttribute("realNameStatus", userIdentify.getRealNameStatus());
		if(userCache!=null && UserCache.USER_NATURE_VOUCH.equals(userCache.getUserNature())){//担保机构
			return "/member/bankCard/vouchList";
		}	
		return "/member/bankCard/list";
	}
	/**移动端
	 * 银行卡列表
	 * @author lgx
	 * @date 2016年11月15日
	 * @return
	 */
	@RequestMapping("/app/member/bankCard/getBankCardList")
	@ResponseBody
	public Object getBankCardList(HttpServletRequest request){
		Object obj = null;
		final Map<String, Object> data = Maps.newHashMap();
		try {
			User user = getAppSessionUser(request);
			UserIdentify userIdentify = identifyService.findByUserId(user.getUuid());
			data.put("bankNum", "1");
			if (!"1".equals(userIdentify.getRealNameStatus())) {
				data.put("bankList", new ArrayList<AccountBankModel>());
			} else {
				data.put("bankList", accountBankCardService.findList(user.getUuid()));
				data.put("userCaCheInfo", userCacheService.findByUserId(user.getUuid()));
			}
			obj = createSuccessAppResponse(data);
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
 static {
	 datab.put("ABC", "#B2048f7a");
	 datab.put("BCM", "#B20c4f9d");
	 datab.put("BOC", "#B29a1f24");
	 datab.put("CCB", "#B20859a1");
	 datab.put("CEB", "#B2791784");
	 datab.put("CIB", "#B21b69af");
	 datab.put("CMB", "#B2d90003");
	 datab.put("CMBC", "#B2175890");
	 datab.put("CNCB", "#B2d6000f");
	 datab.put("GDB", "#B2e60021");
	 datab.put("HXB", "#B2e12b20");
	 datab.put("ICBC", "#B2cf0500");
	 datab.put("PAB", "#B2dd5411");
	 datab.put("PSBC", "#B2007e3d");
	 datab.put("SPDB", "#B2002f82");
 }
	
	/**移动端
	 * 绑定银行卡
	 * @author lgx
	 * @date 2016年10月15日
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/app/member/bankCard/bind")
	public String bind(final Model model,HttpServletRequest request) {
		try {
		@SuppressWarnings("unused")
		User user = getAppSessionUser(request);
		 model.addAttribute("reg", userService.addBank());
		return ConfigUtils.getTppName()+"/bindCard";
	} catch (Exception e) {
		model.addAttribute("r_msg", e.getMessage());
		return ConfigUtils.getTppName()+"/retresult";
	}
	}
	
	@RequestMapping("/app/member/bankCard/bindCard")
	@ResponseBody
	public Object bindCard(String cardId, String smsCode, final HttpServletRequest request){
		Object obj=null;
		try {
			final Map<String, Object> map = Maps.newHashMap();

			map.put("cardNo", cardId);
			map.put("smsCode", smsCode);
			map.put("userIP", IPUtil.getRemortIP(request));
			userService.addBank(map);
			final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
			//绑定成功删除当前用户银行卡的缓存
			userService.delUserCacheDataByType(user, CacheConstant.KEY_PREFIX_USER_BANK_CARD);
			obj = createSuccessAppResponse("绑定成功");
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
	@RequestMapping("/app/member/bankCard/unbindCard")
	@ResponseBody
	public Object unbindCard(String cardId, final HttpServletRequest request){
		Object obj=null;
		try {
			userService.deleteBank(cardId, null);
			final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
			//解绑成功删除当前用户银行卡的缓存
			userService.delUserCacheDataByType(user, CacheConstant.KEY_PREFIX_USER_BANK_CARD);
			obj = createSuccessAppResponse("解绑成功");
		} catch (Exception e) {
			obj = dealException(e);
		}
		return obj;
	}
	
	/**移动
	 * 解绑银行卡
	 * @author lgx
	 * @date 2016年11月15日
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/app/member/bankCard/unBind")
    public String unBind(final String cardId, final String bankCode,final Model model,HttpServletRequest request) {
		try {
			@SuppressWarnings("unused")
			User user = getAppSessionUser(request);
			model.addAttribute("reg", userService.deleteBank(cardId, bankCode));
			//解绑成功删除当前用户银行卡的缓存
			userService.delUserCacheDataByType(user, CacheConstant.KEY_PREFIX_USER_BANK_CARD);
        return ConfigUtils.getTppName()+"/unBindCard";
	} catch (Exception e) {
		model.addAttribute("r_msg", e.getMessage());
		return ConfigUtils.getTppName()+"/retresult";
	}
    }
}
