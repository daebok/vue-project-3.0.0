package com.rd.ifaes.statics.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.multipart.MultipartFile;

import com.rd.ifaes.statics.util.OrderNoUtil;


@RunWith(PowerMockRunner.class)
@PrepareForTest({OrderNoUtil.class})//声明要Mock的类 
public class UploadifySaveTest {
	@Test
	public void testuploadifySaveFirst() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("contract");
		
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
		EasyMock.expect(upload.getSize()).andReturn(2L);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("png");
		upload.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall();
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\data\\pdf\\contract\\"+new SimpleDateFormat("yyyyMMdd").format(new Date()));
		if (file.exists()) {
			file.delete();
		}
		
		PowerMock.mockStatic(OrderNoUtil.class);//Mock静态方法
		EasyMock.expect(OrderNoUtil.getSerialNumber()).andReturn("1011");
		
		PowerMock.replayAll();
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.uploadifySave(upload, request, response);
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	@Test
	public void testuploadifySaveSecond() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("contract");
		
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
		EasyMock.expect(upload.getSize()).andReturn(2L);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("png");
		upload.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall().andThrow(new IOException());
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\data\\pdf\\contract\\"+new SimpleDateFormat("yyyyMMdd").format(new Date()));
		if (file.exists()) {
			file.mkdirs();
		}
		
		PowerMock.mockStatic(OrderNoUtil.class);//Mock静态方法
		EasyMock.expect(OrderNoUtil.getSerialNumber()).andReturn("1011");
		
		PowerMock.replayAll();
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.uploadifySave(upload, request, response);
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	@Test
	public void testuploadifySaveThird() throws Exception {
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
		EasyMock.expect(upload.getSize()).andReturn(2L);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("sdsd");
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.uploadifySave(upload, request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	@Test
	public void testuploadifySaveFourth() throws Exception {
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
		long MAX_IMAGE_SIZE = 1024*1025;
		EasyMock.expect(upload.getSize()).andReturn(MAX_IMAGE_SIZE).times(2);
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.uploadifySave(upload, request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
}
