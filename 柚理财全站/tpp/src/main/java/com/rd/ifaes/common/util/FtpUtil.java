package com.rd.ifaes.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * 用于FTP服务器进行交互的工具类
 * @author lh
 * @version 3.0
 * @since 2017-02-27
 * 
 */
public class FtpUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FtpUtil.class);
	private static String DEAFULT_REMOTE_CHARSET="GBK"; 
	private FTPClient ftpClient = null;
	private static FtpUtil instance = null;
	private String host;
	private int port;
	private String username;
	private String password;

	private FtpUtil() {
		ftpClient = new FTPClient();
		initFTPSetting(
				ConfigUtils.getValue("ftp_host"), 
				ConfigUtils.getInt("ftp_port"), 
				ConfigUtils.getValue("ftp_username"), 
				ConfigUtils.getValue("ftp_password"));
	}

	/*
	 * 得到类对象实例（因为只能有一个这样的类对象，所以用单例模式）
	 */
	public static FtpUtil getInstance() {
		if (instance == null) {
			instance = new FtpUtil();
		}
		return instance;
	}

	/**
	 * 设置FTP服务器
	 * 
	 * @param host
	 *            FTP服务器ip地址
	 * @param FTPPort
	 *            FTP服务器端口号
	 * @param UserName
	 *            登陆FTP服务器的账号
	 * @param UserPassword
	 *            登陆FTP服务器的密码
	 * @return
	 */
	public boolean initFTPSetting(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;

		try {
			ftpClient.setConnectTimeout(10000); // 10秒钟，如果超过就判定超时了
			
			// 1.要连接的FTP服务器Url,Port
			ftpClient.connect(host, port);
			// 设置在与此 Socket 关联的 InputStream 上调用read()阻塞时间超时
			ftpClient.setSoTimeout(10000);
			// 2.看返回的值是不是在200到300之间，如果是，表示登陆成功
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				// 断开
				ftpClient.disconnect();
				return false;
			}
			// 3.登陆FTP服务器
			return ftpClient.login(username, password);

		} catch (SocketException e) {
			LOGGER.error("SocketTimeoutException", e);
			return false;
		} catch (IOException e) {
			LOGGER.error("IOException", e);
			return false;
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param FilePath
	 *            要上传文件所在SDCard的路径
	 * @param FileName
	 *            要上传的文件的文件名(如：Sim唯一标识码)
	 * @return true为成功，false为失败
	 */
	public boolean uploadFile(String filePath, String fileName) {

		if (!ftpClient.isConnected()) {
			if (!initFTPSetting(host, port, username, password)) {
				return false;
			}
		}

		try {
			
			// 设置存储路径			
			String currentDate = getCurrentDate();
			ftpClient.makeDirectory(currentDate);
			ftpClient.changeWorkingDirectory(currentDate);
			
			// 设置PassiveMode传输  
			//ftpClient.enterLocalPassiveMode();
			// 设置上传文件需要的一些基本信息
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding(DEAFULT_REMOTE_CHARSET);
			// 设置以二进制流的方式传输
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE); 
			ftpClient.setDataTimeout(10000);
			
			// 文件上传吧～
			FileInputStream fileInputStream = new FileInputStream(filePath);
			boolean flag = ftpClient.storeFile(fileName, fileInputStream);
			if (flag) { 
				LOGGER.info("上传文件成功！"); 
	        } else { 
	        	LOGGER.info("上传文件失败！"); 
	        } 
			// 关闭文件流
			fileInputStream.close();

			// 退出登陆FTP，关闭ftpCLient的连接
			ftpClient.logout();
			ftpClient.disconnect();

		} catch (IOException e) {
			LOGGER.error("IOException", e);
			return false;
		}
		return true;
	}
	
	private static final String getCurrentDate(){
		return DateUtils.getDate("yyyyMMdd");
	}

	/**
	 * 下载文件
	 * 
	 * @param FilePath
	 *            要存放的文件的路径
	 * @param FileName
	 *            远程FTP服务器上的那个文件的名字
	 * @return true为成功，false为失败
	 */
	public boolean downLoadFile(String localFilePath,String remoteFilePath, String fileName) {
		boolean flag = false;
		if (!ftpClient.isConnected()) {
			if (!initFTPSetting(host, port, username, password)) {
				return false;
			}
		}

		try {
			// 转到指定下载目录
			LOGGER.info("远程文件:{}",remoteFilePath);
			if(StringUtils.isNotBlank(remoteFilePath)){
				ftpClient.changeWorkingDirectory(remoteFilePath);
			}
			ftpClient.setControlEncoding(DEAFULT_REMOTE_CHARSET);
			
			// 列出该目录下所有文件
			FTPFile[] files = ftpClient.listFiles();

			// 遍历所有文件，找到指定的文件
			for (FTPFile file : files) {
				LOGGER.info("ftp文件:{}",file.getName());
				if (file.getName().equals(fileName)) {
					LOGGER.info("找到文件:{}",fileName);
					// 根据绝对路径初始化文件
					File localFile = new File(localFilePath);
					if (!localFile.exists()) {
						try {
							FileUtil.createFile(localFilePath);
						} catch (Exception e) {
							LOGGER.error("创建文件失败");
						}
					}
					LOGGER.info("文件存放路径:{}",localFilePath);
					
					// 输出流
					OutputStream outputStream = new FileOutputStream(localFile);

					// 下载文件
					ftpClient.retrieveFile(file.getName(), outputStream);

					// 关闭流
					outputStream.close();
					
					flag =  true;
				}
			}

			// 退出登陆FTP，关闭ftpCLient的连接
			ftpClient.logout();
			ftpClient.disconnect();

		} catch (IOException e) {
			LOGGER.error("IOException", e);
		}

		return flag;
	}
	
	public boolean deleteFile(String filePath){

		if (!ftpClient.isConnected()) {
			if (!initFTPSetting(host, port, username, password)) {
				return false;
			}
		}
		try {
			ftpClient.dele(filePath);
			// 退出登陆FTP，关闭ftpCLient的连接
			ftpClient.logout();
			ftpClient.disconnect();

		} catch (IOException e) {
			LOGGER.error("IOException", e);
		}

		return true;
	}
	

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
}