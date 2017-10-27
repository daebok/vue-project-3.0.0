package com.rd.ifaes.core.core.util.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.DateUtils;


public class MailWithAttachment {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailWithAttachment.class);
	private static MailWithAttachment instance = null;

	String to;// 收件人
	String from;// 发件人
	String host;// smtp主机
	String username;
	String password;
	String filename;// 附件文件名
	String subject;// 邮件主题
	String content;// 邮件正文
	
	@SuppressWarnings("rawtypes")
	Vector file = new Vector();// 附件文件集合

	/**
	 * 方法说明：默认构造器 输入参数： 返回类型：
	 */
	public MailWithAttachment() {
		super();
	}

	/**
	 *  方法说明：构造器，提供直接的参数传入 
	 * @param to
	 * @param from
	 * @param smtpServer
	 * @param username
	 * @param password
	 * @param subject
	 * @param content
	 */
	public MailWithAttachment(String to, String from, String smtpServer, String username, String password,
			String subject, String content) {
		this.to = to;
		this.from = from;
		this.host = smtpServer;
		this.username = username;
		this.password = password;
		this.subject = subject;
		this.content = content;
	}

	/**
	 * 方法说明：设置邮件服务器地址 输入参数：String host 邮件服务器地址名称 返回类型：
	 */
	public void setHost(String host) {
		this.host = host;
	}

	public String getContent() {
		return content;
	}

	/**
	 * 方法说明：设置登录服务器校验密码 输入参数： 返回类型：
	 */
	public void setPassWord(String pwd) {
		this.password = pwd;
	}

	/**
	 * 方法说明：设置登录服务器校验用户 输入参数： 返回类型：
	 */
	public void setUserName(String usn) {
		this.username = usn;
	}

	/**
	 * 方法说明：设置邮件发送目的邮箱 输入参数： 返回类型：
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * 方法说明：设置邮件发送源邮箱 输入参数： 返回类型：
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * 方法说明：设置邮件主题 输入参数： 返回类型：
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 方法说明：设置邮件内容 输入参数： 返回类型：
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 把主题转换为中文
	 * 
	 * @param strText 主题
	 * @return 转换后文字
	 */
	public String transferChinese(String strText) {
		String sResult = "";
		// 解决邮件标题乱码问题
		try {
			sResult = MimeUtility.encodeText(strText, "utf-8", "B");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return sResult;
	}

	/**
	 * 方法说明：往附件组合中添加附件
	 * @param fname 
	 */
	@SuppressWarnings("unchecked")
	public void attachfile(String fname) {
		file.addElement(fname);
	}

	/**
	 * 方法说明：发送邮件 输入参数： 返回类型：boolean 成功为true，反之为false
	 */
	public static MailWithAttachment getInstance() {
		synchronized(MailWithAttachment.class){
			if (instance == null) {
				instance = new MailWithAttachment();
			}
		}
		return instance;
	}

	/**
	 * 
	 */
	public void readProtocolMsg() {
		this.readMsg("/res/protocol.msg");
	}

	private void readMsg(String filename) {
		StringBuilder sb = new StringBuilder();
		InputStream fis = Mail.class.getResourceAsStream(filename);
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(fis, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LOGGER.error("读取文件遇见不正确的文件编码！", e1);
		}
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		String msg = sb.toString();
		LOGGER.info(msg);
		setContent(msg);
	}

	public void replace(String webName, String host, String username, String email, String url) {
		String msg = this.getContent();
		msg = msg.replace("$web_name$", webName).replace("$email$", email).replace("$host$", host)
				.replace("$username$", username).replace("$url$", host + url);
		this.setContent(msg);
	}


	public void sendMail(String to, String content) {
		this.setTo(to);
		this.setContent(content);
		this.sendMail();
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean sendMail() {

		// 构造mail session
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// 构造MimeMessage 并设定基本的值
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));

			// //这个只能是给一个人发送email
			msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to));
			subject = transferChinese(subject);
			msg.setSubject(subject);

			// 构造Multipart
			Multipart mp = new MimeMultipart();

			// 向Multipart添加正文
			MimeBodyPart mbpContent = new MimeBodyPart();
			mbpContent.setContent(content, "text/html;charset=gb2312");

			// 向MimeMessage添加（Multipart代表正文）
			mp.addBodyPart(mbpContent);

			// 向Multipart添加附件
			Enumeration efile = file.elements();
			while (efile.hasMoreElements()) {

				MimeBodyPart mbpFile = new MimeBodyPart();
				filename = efile.nextElement().toString();
				FileDataSource fds = new FileDataSource(filename);
				mbpFile.setDataHandler(new DataHandler(fds));
				// <span style="color: #ff0000;">//这个方法可以解决附件乱码问题。</span>
				mbpFile.setFileName(new String(fds.getName().getBytes(), "ISO-8859-1"));
				// 向MimeMessage添加（Multipart代表附件）
				mp.addBodyPart(mbpFile);

			}

			file.removeAllElements();
			// 向Multipart添加MimeMessage
			msg.setContent(mp);
			msg.setSentDate(DateUtils.getNow());
			msg.saveChanges();
			// 发送邮件

			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
		} catch (MessagingException | UnsupportedEncodingException e) {
			LOGGER.warn(e.getMessage(), e);
			return false;
		}
		return true;
	}
}
