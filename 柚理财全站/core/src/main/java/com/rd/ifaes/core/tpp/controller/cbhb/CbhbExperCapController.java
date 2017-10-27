package com.rd.ifaes.core.tpp.controller.cbhb;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FileUtil;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbExperCapModel;
import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbChkBaseModel;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;

/**
 * 
 * 渤海银行红包加息券回调
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月28日
 */
@Controller
public class CbhbExperCapController extends BaseController {
	@Resource
	private transient TppTradeService tppTradeService;
	
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbExperCapController.class);
	
	/**
	 * 红包加息券回调
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cbhb/experCap/notify")
	public void tppReturn(final CbhbExperCapModel cbhbModel,final HttpServletResponse response, final HttpServletRequest request) {
		LOGGER.info("红包加息券异步回调业务参数{}",this.getRequestMap(request).toString());
		cbhbModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);
		cbhbModel.response(this.getRequestMap(request));
		final TppTrade trade = tppTradeService.findByOrderNo(cbhbModel.getMerBillNo());
		String fileName = cbhbModel.getResultFileName();
		String localFilePath = cbhbModel.getResultFileNamePath();//相对路径
		String remotePath = new CbhbChkBaseModel().getRemotePath(DateUtils.getNow());
		Map<String,Object> map = FileUtil.getResultFileContent(localFilePath,remotePath,fileName);
		String[] otherContent = (String[])map.get("otherContent");
		LOGGER.info("otherContent:{}",otherContent[0]);
		boolean bl = true;
		for(String contents : otherContent){
			if(contents != null){
				String[] content = contents.split(StringUtils.VERTICAL_LINE);
				if(!CbhbConstant.CBHB_CODE_SUCCESS.equals(content[4])){
					bl = false;
				}
				LOGGER.info("序号:{} 账户存管平台客户号:{} 本金金额:{} 收益金额:{} 应答返回码:{}",content[0],content[1],content[2],content[3],content[4]);
			}
		}
		if(bl){ // 处理成功
			tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), "");
		}else{
			tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_FAIL.getValue(), cbhbModel.getRespDesc());
		}
		cbhbModel.printSuccess500Return(response); 
	}
	
}
