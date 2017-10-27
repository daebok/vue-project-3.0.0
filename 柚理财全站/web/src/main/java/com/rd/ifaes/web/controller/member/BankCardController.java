package com.rd.ifaes.web.controller.member;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.ip.IPUtil;
import com.rd.ifaes.core.account.service.AccountBankCardService;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;
import com.rd.ifaes.web.controller.WebController;

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

	/**
	 * 银行卡列表
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 */
	@RequestMapping("/member/bankCard/getBankCardList")
	@ResponseBody
	public Object getBankCardList(){
		final Map<String, Object> data = Maps.newHashMap();
		User user = SessionUtils.getSessionUser();
		String tppName =ConfigUtils.getTppName();
		if(TppServiceEnum.CBHB.getName().equals(tppName)){
			data.put("result", true);
			data.put("bankList", accountBankCardService.findList());
		} else if (TppServiceEnum.JXBANK.getName().equals(tppName)){
			data.put("result", true);
			data.put("bankList", accountBankCardService.findList(user.getUuid()));
		} else {
			data.put("result", true);
			data.put("bankList", userService.getBankList());
		}
		data.put("userCaCheInfo", userCacheService.findByUserId(user.getUuid()));
		return data;
	}
	/**
	 * 
	 * 担保用户银行卡
	 * @author wyw
	 * @date 2016-8-8
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/member/bankCard/vouchBank")
	public String vouchBank(){
		return "/member/bankCard/vouchList";
	}	
	/**
	 * 绑定银行卡/对应渤海银行的修改绑定银行卡接口
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/member/bankCard/bind")
	public String bind(final Model model) {
		model.addAttribute("reg", userService.addBank());
		return ConfigUtils.getTppName()+"/bindCard";
	}
	
	@RequestMapping("/member/bankCard/bindCard")
	@ResponseBody
	public Object bindCard(String cardId, String smsCode, final HttpServletRequest request){
		final Map<String, Object> map = Maps.newHashMap();
		
		map.put("cardNo", cardId);
		map.put("smsCode", smsCode);
		map.put("userIP", IPUtil.getRemortIP(request));
		userService.addBank(map);
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		//绑定成功删除当前用户银行卡的缓存
		userService.delUserCacheDataByType(user, CacheConstant.KEY_PREFIX_USER_BANK_CARD);
		return renderSuccessResult();
	}
	
	/**
	 * 解绑银行卡
	 * @author xhf
	 * @date 2016年7月27日
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/member/bankCard/unBind")
    public String unBind(final String cardId, final String bankCode,final Model model) {
        if(TppServiceEnum.CBHB.getName().equals(ConfigUtils.getTppName())){
        	return this.bind(model);
        }
		model.addAttribute("reg", userService.deleteBank(cardId, bankCode));
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		//解绑成功删除当前用户银行卡的缓存
		userService.delUserCacheDataByType(user, CacheConstant.KEY_PREFIX_USER_BANK_CARD);
        return ConfigUtils.getTppName()+"/unBindCard";
    }
	
	
	@RequestMapping("/member/bankCard/unbindCard")
	@ResponseBody
	public Object unbindCard(String cardId, final HttpServletRequest request){
		userService.deleteBank(cardId, null);
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		//解绑成功删除当前用户银行卡的缓存
		userService.delUserCacheDataByType(user, CacheConstant.KEY_PREFIX_USER_BANK_CARD);
		return renderSuccessResult();
	}
}
