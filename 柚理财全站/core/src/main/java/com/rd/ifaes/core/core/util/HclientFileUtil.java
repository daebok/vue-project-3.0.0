package com.rd.ifaes.core.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.sys.service.ConfigService;

/**
 * 异步上传文件方法
 * @author RDuser
 *
 */
public class HclientFileUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HclientFileUtil.class);
	private static ConfigService configService;
	private HclientFileUtil() {
	}
	/**
	 * 上传文件的方法：通过后台post请求的方式上传
	 * @param url 请求路径
	 * @param nid 文件所属服务器模块
	 * @param file 文件
	 * @return 返回服务器上所存储的文件路径
	 */
	public static String uploadFileMethod(String url,String nid,File file){
		if(!FileTypeUtil.checkFileType(file)){
			throw new BussinessException("您上传的文件无效，请重新上传！",BussinessException.TYPE_JSON);
		}
		if(configService==null){
			configService = SpringContextHolder.getBean("configService");
		}
		final String imageServerUrl =  configService.findByCode("image_server_url").getConfigValue();
		LOGGER.info("imageServerUrl={}",imageServerUrl);
		String fileType = FileTypeUtil.getFileByFile(file);
		String targetURL = imageServerUrl + url+"?"+"nid="+nid+"&prefix="+fileType; // -- 指定URL
		String retStr = "";
		PostMethod filePost = new PostMethod(targetURL);
		filePost.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8"); 
		try {
			Part[] parts = { new FilePart("upload", file)};
			filePost.setRequestEntity(new MultipartRequestEntity(parts,
					filePost.getParams()));
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(5000);
			int status = client.executeMethod(filePost);
			InputStream in = filePost.getResponseBodyAsStream();
			if (status == HttpStatus.SC_OK) {
					LOGGER.debug("上传成功");
				// 上传成功
			} else {
					LOGGER.error("上传失败,status:{}",status);
				// 上传失败
			}
			// 处理内容
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in));
			String tempBf = null;
			StringBuffer html = new StringBuffer();
			try {
				while ((tempBf = reader.readLine()) != null) {
					html.append(tempBf);
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e );
			}
			retStr = html.toString();
			 LOGGER.debug("上传文件模块为：{} ,返回文件路径 : {} ",nid,retStr);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex );
		} finally {
			filePost.releaseConnection();
		}
		return retStr;
	}
	
	/**
	 * 删除图片 （主要用于资料上传失败后的删除）
	 * @param url 请求的方法
	 * @param param 参数字符串
	 * @return
	 */
	public static String deleteImg(final String url, final String param){
		if(configService==null){
			configService = SpringContextHolder.getBean("configService");
		}
		final String imageServerUrl =  configService.findByCode("image_server_url").getConfigValue();
		String tempURL = imageServerUrl+url+"?imgUrl="+param;
		PostMethod method = new PostMethod(tempURL);
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		String msg = "";
		try {
			int result = client.executeMethod(method);
			if(result==HttpStatus.SC_OK){
				msg = "成功";
			}else{
				msg = "失败";
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e );
		}
		return msg;
	}
	
	/**
	 * 判断网络文件是否存在
	 * 
	 * @param fileUrl
	 * @return
	 */
	public static boolean fileExists(String fileUrl) {
		URL url;
		try {
			url = new URL(fileUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(3 * 1000);
			return conn.getResponseCode() == 200;
		} catch(UnknownHostException e1){
			LOGGER.error("访问路径异常：{}",e1.getMessage(), e1);
			return false;
		}catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
	}
}