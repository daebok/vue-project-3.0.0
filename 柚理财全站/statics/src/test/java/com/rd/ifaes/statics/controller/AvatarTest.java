package com.rd.ifaes.statics.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

import com.rd.ifaes.statics.util.OrderNoUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OrderNoUtil.class})//声明要Mock的类 
public class AvatarTest {
	
	/**
	 * 
	 * 头像上传success
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testAvatarFirst() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("png1")).andReturn("png1");
		EasyMock.expect(request.getParameter("weburl")).andReturn("weburl");
		EasyMock.expect(request.getHeader("REFERER")).andReturn("http://0dfhjs.com");
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\data\\img\\avatar");
		if (file.exists()) {
			deleteDir(file);
		}
		
		PowerMock.mockStatic(OrderNoUtil.class);//Mock静态方法
		EasyMock.expect(OrderNoUtil.getSerialNumber()).andReturn("1011");
		
		PowerMock.replayAll();
		
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
		controller.avatar(request, response);
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	 /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	
	/**
	 * 
	 * 头像上传异常
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testAvatarSecond() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("png1")).andReturn("png1");
		EasyMock.expect(request.getParameter("weburl")).andReturn("weburl");
		EasyMock.expect(request.getHeader("REFERER")).andReturn("http://localhost:8080");
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\data\\img\\avatar");
		if (!file.exists()) {
			file.mkdirs();
		}
		
		PowerMock.mockStatic(OrderNoUtil.class);//Mock静态方法
		EasyMock.expect(OrderNoUtil.getSerialNumber()).andReturn("1011");
		
		PowerMock.replayAll();
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.sendRedirect(EasyMock.anyString());
		EasyMock.expectLastCall().andThrow(new UnsupportedEncodingException());
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		try {
			controller.avatar(request, response);
		} catch (Exception e) {
		}
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 头像上传异常
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testAvatarthird() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("png1")).andReturn("png1");
		EasyMock.expect(request.getParameter("weburl")).andReturn("weburl");
		EasyMock.expect(request.getHeader("REFERER")).andReturn("5dsds");
		
		PowerMock.mockStatic(OrderNoUtil.class);//Mock静态方法
		EasyMock.expect(OrderNoUtil.getSerialNumber()).andReturn("1011");
		
		PowerMock.replayAll();
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		response.sendRedirect(EasyMock.anyString());
		EasyMock.expectLastCall().andThrow(new IOException());
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		try {
			controller.avatar(request, response);
		} catch (Exception e) {
		}
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 头像上传异常
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testAvatarFourth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("png1")).andReturn("png1");
		EasyMock.expect(request.getParameter("weburl")).andReturn("weburl");
		EasyMock.expect(request.getHeader("REFERER")).andReturn(null);
		
		PowerMock.mockStatic(OrderNoUtil.class);//Mock静态方法
		EasyMock.expect(OrderNoUtil.getSerialNumber()).andReturn("1011");
		
		PowerMock.replayAll();
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		//execute method
		controller.avatar(request, response);
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 头像上传异常
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testAvatarFifth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("png1")).andReturn("");
		EasyMock.expect(request.getParameter("weburl")).andReturn("weburl");
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		EasyMock.replay(request);
		EasyMock.replay(response);
		//execute method
		controller.avatar(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(response);
	}
	
	/**
	 * 
	 * 头像上传异常
	 * @author lt1
	 * @date 2016-10-31
	 * @throws Exception
	 */
	@Test
	public void testAvatarSixth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//request.getParameter("param")
		EasyMock.expect(request.getParameter("png1")).andReturn(null);
		EasyMock.expect(request.getParameter("weburl")).andReturn("weburl");
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		EasyMock.replay(request);
		EasyMock.replay(response);
		//execute method
		controller.avatar(request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(response);
	}
}
