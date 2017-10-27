package com.rd.ifaes.statics.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.springframework.web.multipart.MultipartFile;


@PrepareForTest({StaticController.class})//声明要Mock的类 
public class UploadCompanyAuditTest {
	@Rule
    public PowerMockRule rule = new PowerMockRule();
	
	@Test
	public void testuploadCompanyAuditFirst() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("jpg"); //uploadFileName
		upload.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall();
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\data\\pdf\\contract\\userId");
		if (file.exists()) {
			file.delete();
		}
		
		PowerMock.mockStatic(URLDecoder.class);//Mock静态方法
		EasyMock.expect(URLDecoder.decode(EasyMock.anyString(), EasyMock.anyString())).andReturn("1011");
		
		PowerMock.replayAll();
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.uploadCompanyAudit(upload, "userId", "contract", "fileName", request);
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	/**
	 * 
	 * 上传企业资质文件
	 * @author Administrator
	 * @date 2016-10-28
	 * @throws IOException
	 */
	@Test
	public void testuploadCompanyAuditSecond() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("jpg"); //uploadFileName
		upload.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall().andThrow(new IOException());
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\data\\pdf\\contract\\userId");
		if (!file.exists()) {
			file.mkdirs();
		}
		
		PowerMock.mockStatic(URLDecoder.class);//Mock静态方法
		EasyMock.expect(URLDecoder.decode(EasyMock.anyString(), EasyMock.anyString())).andReturn("1011");
		
		PowerMock.replayAll();
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.uploadCompanyAudit(upload, "userId", "contract", "fileName", request);
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	/**
	 * 
	 * 上传企业资质文件
	 * @author Administrator
	 * @date 2016-10-28
	 * @throws IOException
	 */
	@Test
	public void testuploadCompanyAuditThird() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("jpg"); //uploadFileName
		upload.transferTo(EasyMock.anyObject(File.class));
		
		PowerMock.mockStatic(URLDecoder.class);//Mock静态方法
		EasyMock.expect(URLDecoder.decode(EasyMock.anyString(), EasyMock.anyString())).andThrow(new UnsupportedEncodingException());
		
		PowerMock.replayAll();
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.uploadCompanyAudit(upload, "userId", "contract", "fileName", request);
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	/**
	 * 
	 * 上传企业资质文件
	 * @author Administrator
	 * @date 2016-10-28
	 * @throws IOException
	 */
	@Test
	public void testuploadCompanyAuditFourth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("dsds"); //uploadFileName
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.uploadCompanyAudit(upload, "userId", "contract", "fileName", request);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
}
