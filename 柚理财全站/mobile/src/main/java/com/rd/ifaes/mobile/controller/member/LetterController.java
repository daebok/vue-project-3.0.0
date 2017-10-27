package com.rd.ifaes.mobile.controller.member;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.sys.domain.Letter;
import com.rd.ifaes.core.sys.mapper.LetterMapper;
import com.rd.ifaes.core.sys.service.LetterService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.controller.WebController;
import com.rd.ifaes.mobile.model.PagedData;
import com.rd.ifaes.mobile.model.account.MessageModel;


/**
 * 站内信
 * @version 3.0
 * @author xiaodingjiang
 * @date 2016年10月27日
 */
@Controller
public class LetterController extends WebController {
	
	@Resource
	private LetterService letterService;
	@Resource
	private LetterMapper dao;
	
	/**
	 * 站内信列表(接口)
	 * @author xiaodingjiang
	 * @date 2016年10月27日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/app/member/letter/list")
	@ResponseBody
	public Object list(final Letter model,HttpServletRequest request){
		Object obj=null;
	try{
		User user=getAppSessionUser(request);
		model.setReceiveUser(user.getUuid());
		Page<Letter> letterPage=letterService.findLetter(model);
		List<Letter> letterList=letterPage.getRows();
		PagedData<MessageModel> pirlist=new PagedData<MessageModel>();
		fillPageData(pirlist, letterPage);
		for(Letter letter:letterList){
			MessageModel message=new MessageModel();
			message.setBatchType(letter.getBatchType());//批量处理类型: 1  批量删除 2 批量已读 3 批量未读
			message.setContent(letter.getContent());//消息内容
			message.setCreateTime(letter.getCreateTime());//添加时间
			message.setDeleteFlag(letter.getDeleteFlag());//删除标识：0 未删除，1 已删除 
			message.setReadFlag(letter.getReadFlag());// 阅读标识(1已阅读，0未阅读，默认0)
			message.setReceiveUser(letter.getReceiveUser());//接收用户ID 
			message.setTitle(letter.getTitle());//消息标题
			message.setUuid(letter.getUuid());//消息uuid
			pirlist.getList().add(message);
		}
		obj=createSuccessAppResponse(pirlist);
	   }catch(Exception e){
		  obj= dealException(e);
	   }
	   return obj;
	}

	/**
	 * 站内信详情(接口)
	 * @author xiaodingjiang
	 * @date 2016年11月15日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/app/member/letter/letterInfo")
	@ResponseBody
	public Object letterInfo(final Letter model,String id,HttpServletRequest request){
		MessageModel message=new MessageModel();
		Object obj=null;
	try{
		User user=getAppSessionUser(request);
		model.setReceiveUser(user.getUuid());
		Page<Letter> letterPage=letterService.findLetter(model);
		List<Letter> letterList=letterPage.getRows();
		for(Letter letter:letterList){
			if(letter.getUuid().equals(id)){
			message.setBatchType(letter.getBatchType());//批量处理类型: 1  批量删除 2 批量已读 3 批量未读
			message.setContent(letter.getContent());//消息内容
			message.setCreateTime(letter.getCreateTime());//添加时间
			message.setDeleteFlag(letter.getDeleteFlag());//删除标识：0 未删除，1 已删除 
			message.setReadFlag(letter.getReadFlag());// 阅读标识(1已阅读，0未阅读，默认0)
			message.setReceiveUser(letter.getReceiveUser());//接收用户ID 
			message.setTitle(letter.getTitle());//消息标题
			message.setUuid(letter.getUuid());//消息uuid
			break;
			}else{
				continue;
			}
		}
		//点击详情标记为已读
		String[] idsarr=new String[]{id};
		model.setReadFlag("1");
		model.setIds(idsarr);
		dao.setBatch(model);
		obj=createSuccessAppResponse(message);
	   }catch(Exception e){
		  obj= dealException(e);
	   }
	   return obj;
	}
	
	/**
	 * 批量操作
	 * @author xiaodingjiang
	 * @date 2016年10月27日
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/app/member/letter/batchSet")
	@ResponseBody
	public Object batchSet(int batchType,HttpServletRequest request){
		Map<String, Object> data = new HashMap<String, Object>();
		Object obj=null;
		try{
	    User user=getAppSessionUser(request);
		/*String ids=paramString("id"); int batchType,//uuid 字符串
		int batchType=paramInt("batchType");//批量操作标识：1  批量删除 2 批量已读 3 批量未读
*/		
		if (batchType == 0) {
			throw new AppException(AppResultCode.ERROR, "操作参数出错");
		}
		/*String[] idsarr = null;
		if (StringUtils.isNotBlank(id)) {
			String[] arr = id.split(",");
			idsarr = new String[arr.length];
			for (int i = 0; i < arr.length; i++) {
				idsarr[i] = arr[i];
			}
		}*/
		Letter model=new Letter();
		model.setReceiveUser(user.getUuid());
		Page<Letter> letterPage=letterService.findLetter(model);
		List<Letter> letterList=letterPage.getRows();
		String[] idsarr = new String[letterList.size()];
		for(int i=0;i<letterList.size();i++){
			idsarr[i]=letterList.get(i).getUuid();
		}
		
		System.out.println(Arrays.toString(idsarr));
		model.setBatchType(batchType);
		model.setIds(idsarr);
		data= letterService.set(model);
		obj=createSuccessAppResponse(data);
		}catch(Exception e){
			obj=dealException(e);
		}
		return obj;
	}
}
