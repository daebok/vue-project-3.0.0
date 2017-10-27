package com.rd.ifaes.manage.web.listener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.PropertiesUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.HclientFileUtil;


@Controller
public class UploadController {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	private String url = "";
	private String fileName = "";
	private String fieldName = "";
	private String fileExt = "";
	private String state = "";
	private String type = "";
	private String originalName = "";
	private String size = "";
	private HttpServletRequest request = null;	
	private String title = "";
	private String savePath = "";
	private String fileTypeCode = "";
	private long tmpId;
	private String[] allowFiles = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
	private int maxSize = 10000;
	
	private static HashMap<String, String> ERRORINFO = new HashMap<String, String>();
	static{
		ERRORINFO.put("SUCCESS", "SUCCESS");
		ERRORINFO.put("NOFILE", "未包含文件上传域");
		ERRORINFO.put("TYPE", "不允许的文件格式");
		ERRORINFO.put("SIZE", "文件大小超出限制，请上传小于{0}M的文件");
		ERRORINFO.put("ENTYPE", "请求类型ENTYPE错误");
		ERRORINFO.put("REQUEST", "上传请求异常");
		ERRORINFO.put("IO", "IO异常");
		ERRORINFO.put("DIR", "目录创建失败");
		ERRORINFO.put("UNKNOWN", "未知错误");
		ERRORINFO.put("LIMITCONTENT", "文件内容受限");
	}
	
	
	public void upload( HttpServletRequest request) {
		this.request = request;	
		String editorid = request.getParameter("editorid");	
		
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				this.state = ((String) ERRORINFO.get("NOFILE"));
				return;
			}
			DiskFileItemFactory dff = new DiskFileItemFactory();
			String savePath = getFolder(this.savePath);
			dff.setRepository(new File(savePath));
			ServletFileUpload sfu = new ServletFileUpload(dff);
			sfu.setFileSizeMax(this.maxSize * 1024);
			sfu.setSizeMax(this.maxSize * 1024);
			sfu.setHeaderEncoding("UTF-8");
			FileItemIterator fii = sfu.getItemIterator(this.request);
			while (fii.hasNext()) {
				FileItemStream fis = fii.next();
				if (!fis.isFormField()) {
					this.originalName = fis.getName().substring(
							fis.getName().lastIndexOf(
									System.getProperty("file.separator")) + 1);
					if (!checkFileType(this.originalName)) {
						this.state = ((String) ERRORINFO.get("TYPE"));
						break;
					}
					this.fileName = getName(this.originalName);
					this.fileExt = getFileExt(this.fileName);
					this.type = this.fileExt.substring(this.fileExt.lastIndexOf(".")+1);
					this.url = (savePath + "/" + this.fileName);
					this.title = fileName;
					File tmpFile = new File(getPhysicalPath(this.url));
					BufferedInputStream in = new BufferedInputStream(fis.openStream());
					FileOutputStream out = new FileOutputStream(tmpFile);
					BufferedOutputStream output = new BufferedOutputStream(out);
					Streams.copy(in, output, true);
					if(tmpFile.exists()){
						url = HclientFileUtil.uploadFileMethod(PropertiesUtils.getValue(editorid+"Url"),
								PropertiesUtils.getValue(editorid+"Nid"), tmpFile);
						tmpFile.delete();
					}
					this.state = ((String) ERRORINFO.get("SUCCESS"));
				} else {
					String fname = fis.getFieldName();
					if (!fname.equals("pictitle")) {
						continue;
					}
					BufferedInputStream in = new BufferedInputStream(
							fis.openStream());
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder result = new StringBuilder();
					while (reader.ready()) {
						result.append((char) reader.read());
					}
					this.title = new String(result.toString().getBytes(), "utf-8");
					reader.close();
				}
			}
		} catch (FileUploadBase.SizeLimitExceededException e) {
			this.state = StringUtils.format(((String) ERRORINFO.get("SIZE")), doubleToStr(this.maxSize/1024d));
			logger.error("SizeLimitExceededException, {}", this.state ,e);
		} catch (FileUploadBase.InvalidContentTypeException e) {
			this.state = ((String) ERRORINFO.get("ENTYPE"));
			logger.error("InvalidContentTypeException, {}",this.state,e);
		} catch (FileUploadException e) {
			this.state = ((String) ERRORINFO.get("REQUEST"));
			logger.error("FileUploadException, {}",this.state,e);
		} catch (org.apache.commons.httpclient.ConnectTimeoutException e) {
			this.state = ((String) ERRORINFO.get("REQUEST"));
			logger.error("FileUploadException, {}",this.state,e);
		} catch (Exception e) {
			this.state = ((String) ERRORINFO.get("UNKNOWN"));
			logger.error("Exception, {}", this.state ,e);
		}
	}

	public static String doubleToStr(double d){
		return (d%1>0)?String.valueOf(d):String.valueOf((int)d);
	}
	
	@SuppressWarnings("rawtypes")
	private boolean checkFileType(String fileName) {
		Iterator type = Arrays.asList(this.allowFiles).iterator();
		while (type.hasNext()) {
			String ext = (String) type.next();
			if (fileName.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	private String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	private String getName(String fileName) {
		Random random = new Random();
		return this.fileName = random.nextInt(10000) + System.currentTimeMillis() + getFileExt(fileName);
	}

	private String getFolder(final String path) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		String filePath = path + "/" + formater.format(DateUtils.getNow());
		File dir = new File(getPhysicalPath(filePath));
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				this.state = ((String) ERRORINFO.get("DIR"));
				return "";
			}
		}
		return filePath;
	}
	
//	@Action("/plugins/ueditor/dialogs/image/image")
	public String image(){
		return "image";
	}

	private String getPhysicalPath(String path) {
		String servletPath = this.request.getServletPath();
		String realPath = this.request.getSession().getServletContext()
				.getRealPath(servletPath);
		return new File(realPath).getParent() + "/" + path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getFileTypeCode() {
		return fileTypeCode;
	}

	public void setFileTypeCode(String fileTypeCode) {
		this.fileTypeCode = fileTypeCode;
	}

	public long getTmpId() {
		return tmpId;
	}

	public void setTmpId(long tmpId) {
		this.tmpId = tmpId;
	}

	public String[] getAllowFiles() {
		return allowFiles;
	}

	public void setAllowFiles(String[] allowFiles) {
		this.allowFiles = allowFiles;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	
	
	
}
