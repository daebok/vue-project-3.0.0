/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;

/**
 * 文件工具类   生成txt文件放入本地data下面用于交互
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月7日
 */
public class FileUtil {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 根据文件路径的绝对路径创建文件--单层  
	 * @author QianPengZhan
	 * @date 2017年3月7日
	 * @param dir
	 */
	public static void createSingleFile(String dir) {
		try {
			File dirPath = new File(dir);//
			if (!dirPath.exists()) {
				dirPath.mkdir();
			}
		} catch (Exception e) {
			LOGGER.error("创建目录操作出错:{} " , e.getMessage());
			
		}
	}
	
	/**
	 * 创建文件夹--多层  
	 * @author QianPengZhan
	 * @date 2017年3月7日
	 * @param dir
	 * @return
	 */
	public static boolean createMultilayerFile(String dir) {
		try {
			File dirPath = new File(dir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
		} catch (Exception e) {
			LOGGER.error("创建多层目录操作出错: {}" , e.getMessage());
			return false;
		}
		return true;
	}
	
	
	/**
	 * 创建新的文件   若不存在  则创建一个
	 * @author QianPengZhan
	 * @date 2017年3月7日
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static boolean createFile(String path) throws Exception {
		boolean flag = false;
		try {
			int lastLength = path.lastIndexOf("/");
			// 得到文件夹目录
			String dir = path.substring(0, lastLength);
			// 得到路径之后,先创建文件夹
			if (createMultilayerFile(dir) == true) {
				File filePath = new File(path);
				if (!filePath.exists()) {
					filePath.createNewFile();
				}
			}
		} catch (Exception e) {
			LOGGER.error("写入文件报错" + e.toString());
		}
		return flag;
	}
	
	/**
	 * 写入txt文件
	 * @author QianPengZhan
	 * @date 2017年3月7日
	 * @param content
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static boolean writeTxtFile(String content, String path)
			throws Exception {
		boolean flag = false;
		FileOutputStream o = null;
		File fileName = new File(path);
		try {
			o = new FileOutputStream(fileName);
			o.write(content.getBytes("GBK"));
			o.close();
			flag = true;
		} catch (Exception e) {
			LOGGER.error("写入文件报错"+e.toString());
		}
		return flag;
	} 
	
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public static boolean deleteFile(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}  
	
	
	/**
	 * 应用于渤海银行文件类  本地生成临时文件然后FTP上传到对方服务器，上传成功删除本地临时文件
	 * @author QianPengZhan
	 * @date 2017年3月8日
	 * @param path
	 * @param okPath
	 * @param txtContent
	 * @param fileName
	 * @param okFileName
	 * @return
	 */
	public static boolean createFileFTPToClient(String path,String okPath,String txtContent,String fileName,String okFileName){
		boolean uploadFlag  = false;
		try {
			path = getRootPath()+path;
			okPath = getRootPath()+okPath;
			LOGGER.info("path:{},okPath:{},fileName:{},okFileName:{}",path,okPath,fileName,okFileName);
			//1-获取项目根目录 然后处理path和Okpath
			FileUtil.createFile(path);
			FileUtil.writeTxtFile(txtContent, path);
			FileUtil.createFile(okPath);
			FileUtil.writeTxtFile(StringUtils.EMPTY, okPath);
			FtpUtil ftpUtil = FtpUtil.getInstance();
			uploadFlag = ftpUtil.uploadFile(path, fileName);
			if(uploadFlag){
				boolean uploadOkFlag = ftpUtil.uploadFile(okPath, okFileName);
				if(!uploadOkFlag){
					uploadFlag = false;
				}
			}
		} catch (Exception e) {
			LOGGER.error("创建文件失败,{}",e.toString());
		}
		return uploadFlag;
	}
	
	/**
	 * 下载结果文件 到本地的指定路径
	 * @author QianPengZhan
	 * @date 2017年3月13日
	 * @param filePath  本地存放路径
	 * @param remoteFilePath  远程的文件路径
	 * @param fileName  结果文件名称
	 */
	public static boolean  downloadResultFile(String filePath,String remoteFilePath,String fileName){
		filePath = getRootPath()+filePath;
		FtpUtil ftp = FtpUtil.getInstance();
		return ftp.downLoadFile(filePath,remoteFilePath,fileName);
	}
	
	
	/**
	 * 根据文件路径获取txt文件内容(一行一行读取)   返回每行以","逗号分隔的字符串
	 * @author QianPengZhan
	 * @date 2017年3月13日
	 * @return
	 */
	public static String readTxtFile(String filePath){
		filePath = getRootPath()+filePath;
		LOGGER.info(filePath);
		StringBuilder sb = new StringBuilder();
		String  encoding = "GBK";
		try {
			File file = new File(filePath);
			if(file.isFile() && file.exists()){ //判断文件是否存在
				InputStreamReader read = new InputStreamReader( new FileInputStream(file),encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                	LOGGER.info(lineTxt);
                	sb.append(lineTxt).append(",");
                }
                read.close();
			}else{
				LOGGER.error("系统找不到指定的文件");
			}
		} catch (Exception e) {
			LOGGER.error("读取文件内容错误");
		}
		return sb.toString();
	}
	
	/**
	 * 获取第一行和其他行的信息 放入map
	 * @author QianPengZhan
	 * @date 2017年3月14日
	 * @param filePath
	 * @return
	 */
	public static Map<String,Object> getContent(String filePath){
		Map<String,Object> map  = new HashMap<String,Object>();
		String txtContent = readTxtFile(filePath);
		int length = txtContent.lastIndexOf(",");
		txtContent = txtContent.substring(0,length);
		LOGGER.info("读取出来的内容：{}",txtContent);
		String[] content = txtContent.split(",");//最后一项是空字符串，过滤掉
		LOGGER.info("数组内容：{},{}",content[0],content[content.length-1]);
		String firstContent = StringUtils.EMPTY;
		StringBuilder other = new StringBuilder();
		String[] otherContent = new String[content.length-1];
		for (int i = 0; i < content.length; i++) {
			firstContent = content[0];//第一行
			if(i > 0 ){
				otherContent[i-1] = content[i];
				other.append(content[i]).append(",");
			}
		}
		LOGGER.info("第一行:{},剩下内容：{}",firstContent,other);
		map.put("firstContent", firstContent);
		map.put("otherContent", otherContent);
		return map;
	}
	
	
	/**
	 * 下载result文件到本地 然后读取里面的内容 放入map中
	 * @author QianPengZhan
	 * @date 2017年3月14日
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	public static Map<String,Object> getResultFileContent(String filePath,String remotePath,String fileName){
		//1、FTP下载result文件到本地
		LOGGER.info("fileName:{},filePath:{}",fileName,filePath);
		boolean download = downloadResultFile(filePath,remotePath,fileName);
		LOGGER.info("下载是否成功：{}",download);
		if(!download){
			throw new BussinessException("渤海银行下载文件失败");
		}
		
		//2、读取文件中的内容 放入对象中
		return getContent(filePath);
	}
	
	/**
	 * 获取rootPath
	 * @author QianPengZhan
	 * @date 2017年3月14日
	 * @return
	 */
	public static String getRootPath(){
		String rootPath = FileUtil.class.getClassLoader().getResource("").getPath();
		if(rootPath.contains("WEB-INF")){//获取classPath 根据本地和tomcat下的不同进行处理 获取根目录下的data路径
			rootPath = rootPath.replace("/WEB-INF/classes/",StringUtils.EMPTY);
		}else if(rootPath.contains("target")){
			rootPath = rootPath.replace("/target/classes/",StringUtils.EMPTY)+"/src/main/webapp";
		}
		LOGGER.info("项目根目录：{}",rootPath);
		return rootPath;
	}
}
