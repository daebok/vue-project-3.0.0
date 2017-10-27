package com.rd.ifaes.statics.controller;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.Test;

public class StaticControllerTest {
	
	/**
	 * 
	 * 读取头像方法
	 * @author Administrator
	 * @date 2016-10-28
	 * @throws Exception
	 */
	@Test
	public void testreadImgFirst() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("path")).andReturn("\\src\\main\\webapp\\data\\img\\avatar\\default_company_portrait.png");
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		EasyMock.expectLastCall();
		
		ServletOutputStream output = EasyMock.createMock(ServletOutputStream.class);
		EasyMock.expect(response.getOutputStream()).andReturn((ServletOutputStream) output);
		
		output.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		output.flush();
		output.flush();
		output.flush();
		output.close();
		output.close();
		EasyMock.expectLastCall();
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(output);
		EasyMock.replay(response);
		//execute method
		controller.readImg(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(output);
		EasyMock.verify(response);
	}
	
	@Test
	public void testreadImgSecond() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("path")).andReturn("\\src\\main\\webapp\\data\\img\\bank\\ABC.gif");
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/gif");
		EasyMock.expectLastCall();
		
		ServletOutputStream output = EasyMock.createMock(ServletOutputStream.class);
		EasyMock.expect(response.getOutputStream()).andReturn((ServletOutputStream) output);
		
		output.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		output.flush();
		output.flush();
		output.flush();
		output.close();
		output.close();
		EasyMock.expectLastCall();
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(output);
		EasyMock.replay(response);
		//execute method
		controller.readImg(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(output);
		EasyMock.verify(response);
	}
	
	@Test
	public void testreadImgThird() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("path")).andReturn("\\src\\main\\webapp\\data\\img\\avatar\\default_portrait.jpg");
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		EasyMock.expectLastCall();
		
		ServletOutputStream output = EasyMock.createMock(ServletOutputStream.class);
		EasyMock.expect(response.getOutputStream()).andReturn((ServletOutputStream) output);
		
		output.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		output.write(EasyMock.anyObject(byte[].class), EasyMock.anyInt(), EasyMock.anyInt());
		output.flush();
		output.flush();
		output.flush();
		output.close();
		output.close();
		EasyMock.expectLastCall();
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(output);
		EasyMock.replay(response);
		//execute method
		controller.readImg(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(output);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 头像不存在-如果图片不存在 返回
	 * @author Administrator
	 * @date 2016-10-28
	 * @throws Exception
	 */
	@Test
	public void testreadImgFourth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("path")).andReturn("notexits");
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		controller.readImg(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 头像不存在-如果图片不存在 返回
	 * @author Administrator
	 * @date 2016-10-28
	 * @throws Exception
	 */
	@Test
	public void testreadImgFifth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("path")).andReturn("");
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		controller.readImg(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 删除图片（主要用户平台资料上传的删除）
	 * @author Administrator
	 * @date 2016-10-28
	 * @throws Exception
	 */
	@Test
	public void testdeleteImgFirst() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("imgUrl")
		EasyMock.expect(request.getParameter("imgUrl")).andReturn("\\src\\main\\webapp\\data\\img\\test\\test.png");
		EasyMock.expect(request.getHeader("Host")).andReturn("hostsdsd");
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir"));
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		//File
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\test.png");
		if (!file.exists()) {
			file.mkdirs();
		}
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		controller.deleteImg(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 删除图片（主要用户平台资料上传的删除）-文件不存在
	 * @author Administrator
	 * @date 2016-10-28
	 * @throws Exception
	 */
	@Test
	public void testdeleteImgSecond() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("imgUrl")
		EasyMock.expect(request.getParameter("imgUrl")).andReturn("ww"); //url
		EasyMock.expect(request.getHeader("Host")).andReturn("ww"); //imgUrl
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn("realPath");
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		controller.deleteImg(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	@Test
	public void testgetFilePdfBondPath() {
		StaticController controller = new StaticController();
		controller.getFilePdfBondPath("avatar", "projectId", "investId", "contractType");
		controller.getFilePdfBondPath("avatar", "projectId", "investId", "");
		controller.getFilePdfBondPath("avatar", "projectId", "", "");
		controller.getFilePdfBondPath("avatar", "", "", "");
		controller.getFilePdfBondPath("", "", "", "");
	}
	
	@Test
	public void testgetFilePdfPath() {
		StaticController controller = new StaticController();
		controller.getFilePdfPath("avatar", "projectId", "investId", "contractType");
		controller.getFilePdfPath("avatar", "projectId", "investId", "");
		controller.getFilePdfPath("avatar", "projectId", "", "");
		controller.getFilePdfPath("avatar", "", "", "");
		controller.getFilePdfPath("", "", "", "");
	}
	
	@Test
	public void testgetFilePath() {
		StaticController controller = new StaticController();
		controller.getFilePath("");
		controller.getFilePath(null);
	}
	
	@Test
	public void testOthers() {
		StaticController controller = new StaticController();
		//getMd5Str
		controller.getMd5Str("sss");
		//getRandomString
		controller.getRandomString(10);
		//getFolder
		controller.getFolder();
		
	}
	
	/**
	 * 模拟
	 * request.getSession().getServletContext().getRealPath("/")
	 * @author Administrator
	 * @date 2016-10-28
	 */
	@Test
	public void testGetSavePath() {
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
		EasyMock.expect(servletContext.getRealPath("/")).andReturn("realPathsss");
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		//execute method
		System.out.println(controller.getSavePath(request, "dsdadsas"));
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
	}
	
	/**
	 * 取默认图片路径
	 * @author lt1
	 * @date 2016-10-28
	 */
	@Test
	public void testGetDefaultPath() {
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
		EasyMock.expect(servletContext.getRealPath("/")).andReturn("realPathsss");
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		//execute method
		System.out.println(controller.getDefaultPath(request));
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
	}
	
	@Test
	public void testMyFilter() {
		MyFilter file = new MyFilter("name");
		file.accept(null, "name");
	}
}
