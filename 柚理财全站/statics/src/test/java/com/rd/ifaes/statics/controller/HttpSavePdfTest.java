package com.rd.ifaes.statics.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

public class HttpSavePdfTest {
	
	@Test
	public void testhttpSavePdfFirst() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("contract");
		EasyMock.expect(request.getParameter("projectId")).andReturn("projectId_"+System.currentTimeMillis());
		EasyMock.expect(request.getParameter("investId")).andReturn("investId");
		EasyMock.expect(request.getParameter("prefix")).andReturn("prefix");
		EasyMock.expect(request.getParameter("bulidZip")).andReturn("true");
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		PrintWriter out = EasyMock.createMock(PrintWriter.class);
		EasyMock.expect(response.getWriter()).andReturn(out);
		
		out.print(EasyMock.anyString());
		out.flush();
		out.close();
		EasyMock.expectLastCall();
		
		
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		upload.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall();
		
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(out);
		EasyMock.replay(upload);
		//execute method
		controller.httpSavePdf(request, response, upload);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(out);
		EasyMock.verify(upload);
	}
	
	/**
	 * 
	 * httpSavePdf处理是失败，{}
	 * @author Administrator
	 * @date 2016-10-28
	 * @throws Exception
	 */
	@Test
	public void testhttpSavePdfSecond() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("contract");
		EasyMock.expect(request.getParameter("projectId")).andReturn("projectId_"+System.currentTimeMillis());
		EasyMock.expect(request.getParameter("investId")).andReturn("investId");
		EasyMock.expect(request.getParameter("prefix")).andReturn("prefix");
		EasyMock.expect(request.getParameter("bulidZip")).andReturn("true");
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		PrintWriter out = EasyMock.createMock(PrintWriter.class);
		EasyMock.expect(response.getWriter()).andReturn(out);
		
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		upload.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall().andThrow(new IllegalStateException());
		
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(out);
		EasyMock.replay(upload);
		//execute method
		controller.httpSavePdf(request, response, upload);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(out);
		EasyMock.verify(upload);
	}
	
	/**
	 * 
	 * httpSavePdf处理是失败，{IOException}
	 * @author Administrator
	 * @date 2016-10-28
	 * @throws Exception
	 */
	@Test
	public void testhttpSavePdfThird() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("contract");
		EasyMock.expect(request.getParameter("projectId")).andReturn("projectId_test");
		EasyMock.expect(request.getParameter("investId")).andReturn("investId");
		EasyMock.expect(request.getParameter("prefix")).andReturn("prefix");
		EasyMock.expect(request.getParameter("bulidZip")).andReturn("false");
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\data\\pdf\\contract\\projectId_test");
		if (!file.exists()) {
			file.mkdirs();
		}
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		PrintWriter out = EasyMock.createMock(PrintWriter.class);
		EasyMock.expect(response.getWriter()).andReturn(out);
		
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		upload.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall().andThrow(new IOException());
		
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(out);
		EasyMock.replay(upload);
		//execute method
		controller.httpSavePdf(request, response, upload);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(out);
		EasyMock.verify(upload);
	}
	
	@Test
	public void testhttpSavePdfFourth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("contract");
		EasyMock.expect(request.getParameter("projectId")).andReturn("projectId_"+System.currentTimeMillis());
		EasyMock.expect(request.getParameter("investId")).andReturn("investId");
		EasyMock.expect(request.getParameter("prefix")).andReturn(null);
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		
		PrintWriter out = EasyMock.createMock(PrintWriter.class);
		EasyMock.expect(response.getWriter()).andReturn(out);
		out.print(EasyMock.anyString());
		out.flush();
		out.close();
		EasyMock.expectLastCall();
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(out);
		EasyMock.replay(upload);
		//execute method
		controller.httpSavePdf(request, response, upload);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
		EasyMock.verify(out);
	}
	
	@Test
	public void testhttpSavePdfFifth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("contract");
		EasyMock.expect(request.getParameter("projectId")).andReturn("projectId_"+System.currentTimeMillis());
		EasyMock.expect(request.getParameter("investId")).andReturn(null);
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		
		PrintWriter out = EasyMock.createMock(PrintWriter.class);
		EasyMock.expect(response.getWriter()).andReturn(out);
		out.print(EasyMock.anyString());
		out.flush();
		out.close();
		EasyMock.expectLastCall();
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(out);
		EasyMock.replay(upload);
		//execute method
		controller.httpSavePdf(request, response, upload);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
		EasyMock.verify(out);
	}
	
	@Test
	public void testhttpSavePdfSixth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("contract");
		EasyMock.expect(request.getParameter("projectId")).andReturn(null);
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		
		PrintWriter out = EasyMock.createMock(PrintWriter.class);
		EasyMock.expect(response.getWriter()).andReturn(out);
		out.print(EasyMock.anyString());
		out.flush();
		out.close();
		EasyMock.expectLastCall();
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(out);
		EasyMock.replay(upload);
		//execute method
		controller.httpSavePdf(request, response, upload);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
		EasyMock.verify(out);
	}
	
	@Test
	public void testhttpSavePdfSeventh() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("");
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		
		PrintWriter out = EasyMock.createMock(PrintWriter.class);
		EasyMock.expect(response.getWriter()).andThrow(new IOException());
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(out);
		EasyMock.replay(upload);
		//execute method
		controller.httpSavePdf(request, response, upload);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
		EasyMock.verify(out);
	}
}
