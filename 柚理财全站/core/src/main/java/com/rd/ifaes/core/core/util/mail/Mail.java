package com.rd.ifaes.core.core.util.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
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
import com.rd.ifaes.common.util.MD5Utils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.code.BASE64Encoder;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.sys.service.ConfigService;
import com.rd.ifaes.core.user.domain.User;

/**
 * 邮件实体类
 * 
 * @author xx
 * @version 2.0
 * @since 2014年2月27日
 */
public final class Mail {

	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Mail.class);
	
	/** 发送邮件的服务器的IP*/
	private String host;
	/** 邮件发送者的地址*/
	private String from;
	/** 发件人昵称*/
	private String nick;
	/** 邮件接收者的地址*/
	private String to;
	/** 邮件主题*/
	private String subject;
	/** 邮件的文本内容*/
	private String body;
	/** 发送邮件的服务器的端口*/
	private String port;
	private EmailAutherticator auth;
	
	private static Mail instance = null;

	private Mail() {
		init();
	}

	private void init() {
		final ConfigService configService = SpringContextHolder.getBean("configService");
		final String emailAddress = configService.findByCode("email_email").getConfigValue();
		final String emailPwd = configService.findByCode("email_pwd").getConfigValue();
		 
		final EmailAutherticator emailAuth = new EmailAutherticator(emailAddress, emailPwd);
		host = configService.findByCode("email_host").getConfigValue();
		from = configService.findByCode("email_email").getConfigValue();
		nick = configService.findByCode("email_from_name").getConfigValue(); 
		port = configService.findByCode("email_port").getConfigValue(); 
		this.setAuth(emailAuth);
		this.setHost(host);
		this.setNick(nick);
		this.setFrom(from);
		this.setPort(port);
	}
	
	/**
	 * 获得对象
	 * @date 2016年8月31日
	 * @return
	 */
	public static Mail getInstance() {
		synchronized(Mail.class){
			if (instance == null) {
				instance = new Mail();
			}
		}
		return instance;
	}

	public String transferChinese(final String strText) {
		String tempText = "";
		try {
			tempText = MimeUtility.encodeText(new String(strText.getBytes(), Constant.CHARSET), Constant.CHARSET, "B");
		} catch (Exception e) {
			LOGGER.error("编码异常:{}",e.getMessage(), e);
		}
		return tempText;
	}

	/**
	 * 发送邮件
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public void sendMail() throws MessagingException, UnsupportedEncodingException {
		final Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.port", port);
		props.setProperty("mail.smtp.socketFactory.port", port);

		final Session session = Session.getDefaultInstance(props, auth);
		final MimeMessage message = new MimeMessage(session);
		message.setContent("Hello", "text/plain");
		message.setSubject(subject, "utf-8");// 设置邮件主题
		message.setSentDate(DateUtils.getNow());// 设置邮件发送时期
		final Address address = new InternetAddress(from, nick, "utf-8");

		message.setFrom(address);// 设置邮件发送者的地址
		if(StringUtils.isBlank(to)){
			LOGGER.warn("邮件接收者的地址为空");
			return;
		}
		final Address toaddress = new InternetAddress(to);// 设置邮件接收者的地址
		message.addRecipient(Message.RecipientType.TO, toaddress);
		// 创建一个包含HTML内容的MimeBodyPart
		final Multipart mainPart = new MimeMultipart();
		final BodyPart html = new MimeBodyPart();
		html.setContent(body, "text/html; charset=utf-8");
		mainPart.addBodyPart(html);
		// 将MiniMultipart对象设置为邮件内容
		message.setContent(mainPart);
		try {
			Transport.send(message);
		} catch (MessagingException e) {
			LOGGER.error("Send Email founds error!",e);
		}
	}

	@SuppressWarnings("unused")
	private void saveEmailToSentMailFolder(final Folder sentFolder, final Message message) {
		try {
			sentFolder.appendMessages(new Message[] { message });
		} catch (Exception e) {
			LOGGER.error("save Email founds error:{}!", e.getMessage(), e);
		} finally {

			// 判断发件文件夹是否打开如果打开则将其关闭
			if (sentFolder != null && sentFolder.isOpen()) {
				try {
					sentFolder.close(true);
				} catch (MessagingException e) {
					LOGGER.error("Save Email founds error!",e);
				}
			}
			
		}
	}

	/**
	 * 发送邮件
	 * @param to
	 * @param subject
	 * @param content
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void sendMail(final String to, final String subject, final String content) throws UnsupportedEncodingException, MessagingException {
		this.setTo(to);
		this.setSubject(subject);
		this.setBody(content);
		this.sendMail();
	}

	/**
	 * 获取发送邮件的服务器的IP
	 * @return host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 设置发送邮件的服务器的IP
	 * @param  host
	 */
	public void setHost(final String host) {
		this.host = host;
	}

	/**
	 * 获取邮件发送者的地址
	 * @return from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * 设置邮件发送者的地址
	 * @param  from
	 */
	public void setFrom(final String from) {
		this.from = from;
	}

	/**
	 * 获取发件人昵称
	 * @return nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * 设置发件人昵称
	 * @param  nick
	 */
	public void setNick(final String nick) {
		this.nick = nick;
	}

	/**
	 * 获取邮件接收者的地址
	 * @return to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * 设置邮件接收者的地址
	 * @param  to
	 */
	public void setTo(final String to) {
		this.to = to;
	}

	/**
	 * 获取邮件主题
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 设置邮件主题
	 * @param  subject
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * 获取邮件的文本内容
	 * @return body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * 设置邮件的文本内容
	 * @param  body
	 */
	public void setBody(final String body) {
		this.body = body;
	}

	/**
	 * 获取发送邮件的服务器的端口
	 * @return port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * 设置发送邮件的服务器的端口
	 * @param  port
	 */
	public void setPort(final String port) {
		this.port = port;
	}

	/**
	 * 获取auth
	 * @return auth
	 */
	public EmailAutherticator getAuth() {
		return auth;
	}

	/**
	 * 设置auth
	 * @param  auth
	 */
	public void setAuth(EmailAutherticator auth) {
		this.auth = auth;
	}

	public void readActiveMailMsg() {
		this.readMsg("/res/mail.msg");
	}

	/**
	 * 读取消息
	 * @param filename
	 */
	private void readMsg(final String filename) {
		final StringBuilder sb = new StringBuilder();
		final InputStream fis = Mail.class.getResourceAsStream(filename);
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(fis, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			LOGGER.error("读取文件遇见不正确的文件编码，{}！", e1.getMessage(), e1);
		}
		final BufferedReader br = new BufferedReader(isr);
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		final String msg = sb.toString();
		setBody(msg);
	}

	public String getdecodeIdStr(final User user) {
		final String chars = "0123456789qwertyuiopasdfghjklmnbvcxz-=~!@#$%^*+-._/*<>|";
		final int length = chars.length();
		final StringBuilder url = new StringBuilder();
		final StringBuilder rancode = new StringBuilder();
		final String timeStr = System.currentTimeMillis() / 1000 + "";
		String userIDAddtime = user.getUuid() + StringUtils.isNull(user.getCreateTime());
		userIDAddtime = MD5Utils.encode(userIDAddtime);
		url.append(user.getUuid()).append(",").append(userIDAddtime).append(",").append(timeStr).append(",");
		for (int i = 0; i < 10; i++) {
			int num = (int) (Math.random() * (length - 2)) + 1;
			rancode.append(chars.charAt(num));
		}
		url.append(rancode);
		final String idurl = url.toString();
		final BASE64Encoder encoder = new BASE64Encoder();
		final String s = encoder.encode(idurl.getBytes());
		return s;
	}

	public void replace(final String webName, final String host, final String username, final String email, final String url) {
		String msg = this.getBody();
		msg = msg.replace("$web_name$", webName).replace("$email$", email).replace("$host$", host)
				.replace("$username$", username).replace("$url$", host + url);
		this.setBody(msg);
	}
}
