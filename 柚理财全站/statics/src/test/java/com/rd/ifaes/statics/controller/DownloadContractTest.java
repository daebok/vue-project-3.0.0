package com.rd.ifaes.statics.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Test;

import sun.misc.BASE64Decoder;

public class DownloadContractTest {
	
	/**
	 * 
	 * 债权协议下载pdfsuccess
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testdownloadContractFirst() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("projectId")).andReturn("2016");
		EasyMock.expect(request.getParameter("investId")).andReturn("20161031");
		EasyMock.expect(request.getParameter("webUrl")).andReturn("http://localhost");
		EasyMock.expect(request.getParameter("token")).andReturn("5D11AE34F99732DB60B4819EEF74A833");
		EasyMock.expect(request.getParameter("fileName")).andReturn("test.pdf");
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\data\\pdf\\projectContract\\2016\\20161031.pdf");
		
		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			fout.write(new BASE64Decoder().decodeBuffer("png1"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.reset();
		EasyMock.expectLastCall();
		response.addHeader(EasyMock.anyString(),EasyMock.anyString());
		EasyMock.expectLastCall().times(2);
		response.setContentType("application/octet-stream");
		EasyMock.expectLastCall();
		ServletOutputStream output = EasyMock.createMock(ServletOutputStream.class);
		EasyMock.expect(response.getOutputStream()).andReturn((ServletOutputStream) output);
		output.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		output.flush();
		output.flush();
		output.close();
		EasyMock.expectLastCall();
		
		EasyMock.replay(output);
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		controller.downloadContract(request, response);
		
		//verify
		EasyMock.verify(output);
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 债权协议下载zipsuccess
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testdownloadContractSecond() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("projectId")).andReturn("2016");
		EasyMock.expect(request.getParameter("investId")).andReturn("20161031");
		EasyMock.expect(request.getParameter("webUrl")).andReturn("http://localhost");
		EasyMock.expect(request.getParameter("token")).andReturn("5D11AE34F99732DB60B4819EEF74A833");
		EasyMock.expect(request.getParameter("fileName")).andReturn("test.zip");
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\data\\pdf\\projectContract\\2016\\20161031.zip");
		
		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
			fout.write(new BASE64Decoder().decodeBuffer("png1"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.reset();
		EasyMock.expectLastCall();
		response.addHeader(EasyMock.anyString(),EasyMock.anyString());
		EasyMock.expectLastCall().times(2);
		response.setContentType("application/x-download");
		EasyMock.expectLastCall();
		ServletOutputStream output = EasyMock.createMock(ServletOutputStream.class);
		EasyMock.expect(response.getOutputStream()).andReturn((ServletOutputStream) output);
		output.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		output.flush();
		output.flush();
		output.close();
		EasyMock.expectLastCall();
		
		EasyMock.replay(output);
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		controller.downloadContract(request, response);
		
		//verify
		EasyMock.verify(output);
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 债权协议下载图片不存在pdf
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testdownloadContractThird() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("projectId")).andReturn("2016");
		EasyMock.expect(request.getParameter("investId")).andReturn("20161031-noexits");
		EasyMock.expect(request.getParameter("webUrl")).andReturn("http://localhost");
		EasyMock.expect(request.getParameter("token")).andReturn("DABFFAAFA56BB4FECCCAEE401F4C2A2D");
		EasyMock.expect(request.getParameter("fileName")).andReturn("test.zip");
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.sendRedirect(EasyMock.anyString());
		EasyMock.expectLastCall();
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		controller.downloadContract(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 债权协议下载图片不存在
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testdownloadContractThird_1() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("projectId")).andReturn("2016");
		EasyMock.expect(request.getParameter("investId")).andReturn("20161031-noexits");
		EasyMock.expect(request.getParameter("webUrl")).andReturn("http://localhost");
		EasyMock.expect(request.getParameter("token")).andReturn("DABFFAAFA56BB4FECCCAEE401F4C2A2D");
		EasyMock.expect(request.getParameter("fileName")).andReturn("test.pdf");
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.sendRedirect(EasyMock.anyString());
		EasyMock.expectLastCall();
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		controller.downloadContract(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * token为空或者不正确
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testdownloadContractFourth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("projectId")).andReturn("2016");
		EasyMock.expect(request.getParameter("investId")).andReturn("20161031-tokenerror");
		EasyMock.expect(request.getParameter("webUrl")).andReturn("http://localhost");
		EasyMock.expect(request.getParameter("token")).andReturn("DABFFAAFA56BB4FECCCAEE401F4C2A2D");
		EasyMock.expect(request.getParameter("fileName")).andReturn("test.zip");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		EasyMock.replay(request);
		EasyMock.replay(response);
		//execute method
		controller.downloadContract(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * token为空或者不正确
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testdownloadContractFifth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("projectId")).andReturn("2016");
		EasyMock.expect(request.getParameter("webUrl")).andReturn("http://localhost");
		EasyMock.expect(request.getParameter("investId")).andReturn("20161031-tokenerror");
		EasyMock.expect(request.getParameter("token")).andReturn("");
		EasyMock.expect(request.getParameter("fileName")).andReturn("test.zip");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		EasyMock.replay(request);
		EasyMock.replay(response);
		//execute method
		controller.downloadContract(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * downloadFileName为空:{}
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testdownloadContractSixth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("projectId")).andReturn("2016");
		EasyMock.expect(request.getParameter("webUrl")).andReturn("http://localhost");
		EasyMock.expect(request.getParameter("investId")).andReturn("20161031-tokenerror");
		EasyMock.expect(request.getParameter("token")).andReturn("sdss");
		EasyMock.expect(request.getParameter("fileName")).andReturn("");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		EasyMock.replay(request);
		EasyMock.replay(response);
		//execute method
		controller.downloadContract(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * investId为空:{}
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testdownloadContractSeventh() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("projectId")).andReturn("2016");
		EasyMock.expect(request.getParameter("investId")).andReturn("");
		EasyMock.expect(request.getParameter("webUrl")).andReturn("http://localhost");
		EasyMock.expect(request.getParameter("token")).andReturn("sdss");
		EasyMock.expect(request.getParameter("fileName")).andReturn("");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		EasyMock.replay(request);
		EasyMock.replay(response);
		//execute method
		controller.downloadContract(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * projectId为空:{}
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testdownloadContractEightth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("projectId")).andReturn("");
		EasyMock.expect(request.getParameter("investId")).andReturn("11");
		EasyMock.expect(request.getParameter("webUrl")).andReturn("http://localhost");
		EasyMock.expect(request.getParameter("token")).andReturn("sdss");
		EasyMock.expect(request.getParameter("fileName")).andReturn("11");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		EasyMock.replay(request);
		EasyMock.replay(response);
		//execute method
		controller.downloadContract(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(response);
	}
}
