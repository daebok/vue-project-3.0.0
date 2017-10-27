/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb.chk;

import java.io.Serializable;
import java.util.Date;

import com.rd.ifaes.common.util.DateUtils;

/**
 * 对账类的基类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月15日
 */
@SuppressWarnings("serial")
public class CbhbChkBaseModel implements Serializable{
	
	
	/**
	 * 本地存放绝对路径
	 * @author QianPengZhan
	 * @date 2017年3月14日
	 * @param fileName
	 * @return
	 */
	public String getReSultFilePath(Date time,String fileName){
		return "/data/files/chk/"+DateUtils.formatDate(time,DateUtils.DATEFORMAT_STR_012)+"/"+fileName;
	}
	
	/**
	 * FTP远程下载的文件夹路径
	 * @author QianPengZhan
	 * @date 2017年3月14日
	 * @return
	 */
	public String getRemotePath(Date time){
		return "CHK/"+DateUtils.formatDate(time,DateUtils.DATEFORMAT_STR_012)+"/";
	}
}
