package com.rd.ifaes.core.core.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAutherticator extends Authenticator {
	
	private String username;

	private String password;

	public EmailAutherticator(String user, String pwd) {
		super();
		username = user;
		password = pwd;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
