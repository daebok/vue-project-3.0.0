package com.rd.ifaes.common.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;


/** 
 * ftp文件的上传与下载
 * @author lh
 * @version 3.0
 * @since 2016-12-7
 */
public class SftpUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SftpUtil.class);
	
	//定义 加密算法,可用 DES,DESede,Blowfish        //keybyte为加密密钥，长度为24字节    //src为被加密的数据缓冲区（源）    
	private static final String ALGORITHM = "DESede";

	/**
	 * sftp服务器ip
	 */
    protected static String host="localhost";
    
    /**
     * 端口号
     */
    protected static int port = 22;//默认的sftp端口号是22
    
    /**
     * 用户名
     */
    protected static String username;
    
    /**
     * 密码
     */
    protected static String password;
    
    /**
     * 密钥文件路径
     */
    protected static String privateKey;
    
    /**
     * 密钥口令
     */
    protected static String passphrase;
	
    /**
     * 初始化参数
     */
    private static void init() {
    	host = "221.239.93.145";//ConfigUtils.getValue("ftp_host"); 
    	port = 21;//ConfigUtils.getInt("ftp_port");
    	username = "rongdu";//ConfigUtils.getValue("ftp_username");
    	password = "rongdu";ConfigUtils.getValue("ftp_password");
		//privateKey = ConfigUtils.getValue("ftp_keyPath");
    }
    
    /**
     * 获取sftp连接
     * @return
     */
	public static ChannelSftp connect(){
		init();
		JSch jsch = new JSch();
        Channel channel = null;
        try {
            if (StringUtils.isNotBlank(privateKey)) {
                //使用密钥验证方式，密钥可以使有口令的密钥，也可以是没有口令的密钥
                if (StringUtils.isNotBlank(passphrase)) {
                    jsch.addIdentity(privateKey, passphrase);
                } else {
                    jsch.addIdentity(privateKey);
                }
            }
            Session session = jsch.getSession(username, host, port);
            if (password != null && !"".equals(password)) {
                session.setPassword(password);
            }
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");// do not verify host key
            session.setConfig(sshConfig);
            // session.setTimeout(timeout);
            session.setServerAliveInterval(92000);
            session.connect();
            LOGGER.debug("创建sftp连接成功");
            //参数sftp指明要打开的连接是sftp连接
            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
        	LOGGER.error(e.getMessage());
        }
        return (ChannelSftp) channel;
	}
	
	public static void testConnect()throws Exception{
		ChannelSftp channel = connect();
		LOGGER.info("channel connect status : {}",channel.isConnected());
		LOGGER.info("channel home is {}.",channel.getHome());
		channel.disconnect();
		LOGGER.info("channel disconnect status: {}.",channel.getExitStatus());
	}
	
	/**
     * 上传文件
     * 
     * @param directory
     *            上传的目录
     * @param uploadFile
     *            要上传的文件
     * @param sftp
     */
    public static void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
        	LOGGER.error(e.getMessage());
        }
    }

    /**
     * 下载文件
     * 
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveDirectory
     *            存在本地的路径
     * @param sftp
     */
    public static boolean download(String directory, String downloadFile,
            String saveDirectory, ChannelSftp sftp) {
    	LOGGER.info("下载sftp对账文件：" + downloadFile);
    	// 判断目录，不存在时 是创建还是 return，商户自行决定,默认创建
    	File file = new File(saveDirectory);
    	if (!file.exists()) {
    		file.mkdirs();
    	}
    	
        try {
            sftp.cd(directory);
            sftp.get(downloadFile, saveDirectory);
            return true;
        } catch (Exception e) {
        	LOGGER.error(e.getMessage());
        	return false;
        }
    }
    
    /**
     * 设置数据流为解密模式
     * @param des3Key
     * @param inputStream
     * @return
     */
    public static CipherInputStream decryptMode(InputStream inputStream) {
    	//MerchantTpp tpp  = Global.getMerchantTpp(ufxCustomerId);
    	String des3Key = ConfigUtils.getValue("tpp_fileDes3Key");//JsonUtil.get(tpp.getReserve(), "fileDes3Key");
        //生成密钥          
        SecretKey deskey = new SecretKeySpec(des3Key.getBytes(), ALGORITHM); //解密         
        return decryptMode(deskey, inputStream);
    }

    /**
     * 设置数据流为解密模式
     * @param secretKey
     * @param inputStream
     * @return
     */
    public static CipherInputStream decryptMode(SecretKey secretKey, InputStream inputStream) {
        try {
            Cipher c1 = Cipher.getInstance(ALGORITHM);
            c1.init(Cipher.DECRYPT_MODE, secretKey);
            return new CipherInputStream(inputStream, c1);
        } catch (InvalidKeyException|NoSuchAlgorithmException|NoSuchPaddingException  e) {
        	LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * 获取文件流
     * 
     * @param directory
     *            sftp目录
     * @param fileName
     *            文件名
     * @param sftp
     */
    public static InputStream getFileStream(String fileName, ChannelSftp sftp) {
    	InputStream in = null;
    	LOGGER.info("读取sftp对账文件输入流：" + fileName);
    	try {
    		sftp.cd("/"+TppUtil.getTppName() +  "/" + TppUtil.getTppCustomerId()+"/");
    		in = sftp.get(fileName);
		} catch (Exception e) {
			LOGGER.error("读取sftp输入流异常：",e);
		}
    	return in;
    }
    
}
