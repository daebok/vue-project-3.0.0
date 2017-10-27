/**
 * banner模型
 */
package com.rd.ifaes.mobile.model.project;


/**
 * @author yoseflin
 * 标详情model
 */
public class projectUplodeitemModel {
	/**
	 *  上传文件类型:1、借款资料；2、抵押物资料；3、其他 
	 */ 
	private String	fileType;
	/**
	 *  上传文件路径 
	 */
	private String	filePath;		 
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	} 
}
