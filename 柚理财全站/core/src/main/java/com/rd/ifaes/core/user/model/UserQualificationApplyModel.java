package com.rd.ifaes.core.user.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.rd.ifaes.core.user.domain.UserQualificationApply;
import com.rd.ifaes.core.user.domain.UserQualificationUpload;

/**
 * entity:UserQualificationApply
 * 
 * @author FangJun
 * @version 3.0
 * @date 2016-7-21
 */
public class UserQualificationApplyModel extends UserQualificationApply {

	private static final long serialVersionUID = 1L;
	/**
	 * 材料名称
	 */
	private String name; 
	/**
	 * 资质文件列表
	 */
	private List<UserQualificationUpload> fileList;  
	
	/**待上传**/
	public static final String STATUS_WAIT_UPLOAD = "99";
	/**待审核**/
	public static final String STATUS_AUDIT = "0";
	/**审核通过**/
	public static final String STATUS_SUCCESS = "1";
	/**审核不通过**/
	public static final String STATUS_FAIL = "2";

	/**
	 * 获取材料名称
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置材料名称
	 * @param  name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * 获取资质文件列表
	 * @return fileList
	 */
	public List<UserQualificationUpload> getFileList() {
		return fileList;
	}

	/**
	 * 设置资质文件列表
	 * @param  fileList
	 */
	public void setFileList(final List<UserQualificationUpload> fileList) {
		this.fileList = fileList;
	}

	/**
	 * 获得状态显示值
	 * @author xhf
	 * @date 2016年8月31日
	 * @return
	 */
	public String getStatusStr(){
		String statusStr="未上传";  //状态
		if(StringUtils.isNotBlank(super.getStatus())){
			if(STATUS_WAIT_UPLOAD.equals(super.getStatus())){
				statusStr = "未上传";
			}else if(STATUS_AUDIT.equals(super.getStatus())){
				statusStr = "待审核";
			}else if(STATUS_SUCCESS.equals(super.getStatus())){
				statusStr = "审核成功";
			}else{
				statusStr = "审核失败";
			}
		}
		return statusStr;
	}

}
