package com.rd.ifaes.statics.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.rd.ifaes.statics.util.FileTypeUtil;
import com.rd.ifaes.statics.util.OrderNoUtil;
import com.rd.ifaes.statics.util.PrintJsonUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PrintJsonUtil.class, FileUtils.class, FileTypeUtil.class, OrderNoUtil.class})//声明要Mock的类 
public class AppAvatarTest {
	
	@Test
	public void testAppAvatarException() throws Exception{
		IMocksControl control = EasyMock.createControl();
		
		int count=3;
		//ServletContext
		ServletContext servletContext = control.createMock(ServletContext.class);
		EasyMock.expect(servletContext.getRealPath(EasyMock.anyString())).andReturn("D:").times(count);
		//session
		HttpSession session = control.createMock(HttpSession.class);
		EasyMock.expect(session.getServletContext()).andReturn(servletContext).times(count);
		//request
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session).times(count);
		
		//CommonsMultipartFile
		CommonsMultipartFile file = control.createMock(CommonsMultipartFile.class);
		EasyMock.expect(file.getSize()).andReturn(1024*1025l);
		EasyMock.expect(file.getSize()).andReturn(1024l);
		EasyMock.expect(file.getOriginalFilename()).andReturn("123.pp").times(2);
		
		//PrintJsonUtil
		PowerMock.mockStatic(PrintJsonUtil.class);//Mock静态方法
		PrintJsonUtil.printSuccessJson(EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyString());
		EasyMock.expectLastCall().times(count);
		
		//准备
		control.replay();
		PowerMock.replayAll();
		
		//测试
		StaticController controller = EasyMock.createMockBuilder(StaticController.class)
				.addMockedMethod("getFilePath",String.class)
				.addMockedMethod("getSavePath",HttpServletRequest.class,String.class)
				.createMock();
		EasyMock.expect(controller.getFilePath(EasyMock.anyString())).andThrow(new NullPointerException("msg")).times(2);
		EasyMock.replay(controller);
		try{
			controller.appAvatar(request, null);
		}catch(Exception e){
		}
		try{
			controller.appAvatar(request, file);
		}catch(Exception e){
		}
		try{
			controller.appAvatar(request, file);
		}catch(Exception e){
		}
		//验证
		control.verify();
		EasyMock.verify(controller);
		PowerMock.verifyAll();
	}
	
	@Test
	public void testAppAvatarFirst() throws Exception{
		String savePath = "D:\\"+(new Date()).getTime();
		
		IMocksControl control = EasyMock.createControl();
		//ServletContext
		ServletContext servletContext = control.createMock(ServletContext.class);
		EasyMock.expect(servletContext.getRealPath(EasyMock.anyString())).andReturn("D:");
		//session
		HttpSession session = control.createMock(HttpSession.class);
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//request
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		
		//CommonsMultipartFile
		CommonsMultipartFile file = control.createMock(CommonsMultipartFile.class);
		EasyMock.expect(file.getSize()).andReturn(1024l);
		EasyMock.expect(file.getOriginalFilename()).andReturn("123.jpg").times(1);
		file.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall();
		
		//PrintJsonUtil
		PowerMock.mockStatic(PrintJsonUtil.class);//Mock静态方法
		PrintJsonUtil.printSuccessJson(EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyString());
		EasyMock.expectLastCall().times(1);
		PowerMock.replay(PrintJsonUtil.class);
		
		//准备
		control.replay();
		//测试
		StaticController controller = EasyMock.createMockBuilder(StaticController.class)
				.addMockedMethod("getFilePath",String.class)
				.addMockedMethod("getSavePath",HttpServletRequest.class,String.class)
				.createMock();
		EasyMock.expect(controller.getFilePath(EasyMock.anyString())).andReturn("123.jpg");
		EasyMock.expect(controller.getSavePath(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyString())).andReturn(savePath);
		EasyMock.replay(controller);
		
		controller.appAvatar(request, file);
		File descFile = new File(savePath);
		descFile.delete();
		
		//验证
		control.verify();
		PowerMock.verify(PrintJsonUtil.class);
	}
	

	
	@Test
	public void testAppAvatarTwo() throws Exception{
		String savePath = "D:\\"+(new Date()).getTime();
		
		IMocksControl control = EasyMock.createControl();
		//ServletContext
		ServletContext servletContext = control.createMock(ServletContext.class);
		EasyMock.expect(servletContext.getRealPath(EasyMock.anyString())).andReturn("D:");
		//session
		HttpSession session = control.createMock(HttpSession.class);
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//request
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		
		//CommonsMultipartFile
		CommonsMultipartFile file = control.createMock(CommonsMultipartFile.class);
		EasyMock.expect(file.getSize()).andReturn(1024l);
		EasyMock.expect(file.getOriginalFilename()).andReturn("123.jpg").times(1);
		file.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall().andThrow(new IOException("msg"));
		
		//PrintJsonUtil
		PowerMock.mockStatic(PrintJsonUtil.class);//Mock静态方法
		PrintJsonUtil.printSuccessJson(EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyString());
		EasyMock.expectLastCall().times(1);
		PowerMock.replay(PrintJsonUtil.class);
		
		//准备
		control.replay();
		//测试
		StaticController controller = EasyMock.createMockBuilder(StaticController.class)
				.addMockedMethod("getFilePath",String.class)
				.addMockedMethod("getSavePath",HttpServletRequest.class,String.class)
				.createMock();
		EasyMock.expect(controller.getFilePath(EasyMock.anyString())).andReturn("123.jpg");
		EasyMock.expect(controller.getSavePath(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyString())).andReturn(savePath);
		EasyMock.replay(controller);
		
		controller.appAvatar(request, file);
		File descFile = new File(savePath);
		descFile.delete();
		
		//验证
		control.verify();
		PowerMock.verify(PrintJsonUtil.class);
	}
	

   @Test
	public void testAppAvatarThree() throws Exception{
		IMocksControl control = EasyMock.createControl();
		//ServletContext
		ServletContext servletContext = control.createMock(ServletContext.class);
		EasyMock.expect(servletContext.getRealPath(EasyMock.anyString())).andReturn("D:");
		//session
		HttpSession session = control.createMock(HttpSession.class);
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//request
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getSession()).andReturn(session);
		
		//CommonsMultipartFile
		CommonsMultipartFile file = control.createMock(CommonsMultipartFile.class);
		EasyMock.expect(file.getSize()).andReturn(1024l);
		EasyMock.expect(file.getOriginalFilename()).andReturn("123.jpg").times(1);
		file.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall();
		
		//PrintJsonUtil
		PowerMock.mockStatic(PrintJsonUtil.class);//Mock静态方法
		PrintJsonUtil.printSuccessJson(EasyMock.anyObject(), EasyMock.anyString(), EasyMock.anyString());
		EasyMock.expectLastCall().times(1);
		PowerMock.replay(PrintJsonUtil.class);
		
		String fileName = "123";
		PowerMock.mockStatic(OrderNoUtil.class);
		EasyMock.expect(OrderNoUtil.getSerialNumber()).andReturn(fileName);
		PowerMock.replay(OrderNoUtil.class);
		
		String savePath = "D:\\123";
		File descFile = new File(savePath  + "/" + fileName+ ".jpg");
		if (!descFile.getParentFile().exists()) {
			try {
				descFile.getParentFile().mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//准备
		control.replay();
		//测试
		StaticController controller = EasyMock.createMockBuilder(StaticController.class)
				.addMockedMethod("getFilePath",String.class)
				.addMockedMethod("getSavePath",HttpServletRequest.class,String.class)
				.createMock();
		EasyMock.expect(controller.getFilePath(EasyMock.anyString())).andReturn("123.jpg");
		EasyMock.expect(controller.getSavePath(EasyMock.anyObject(HttpServletRequest.class), EasyMock.anyString())).andReturn(savePath);
		EasyMock.replay(controller);
		
		controller.appAvatar(request, file);
		File delFile = new File(savePath);
		delFile.delete();
		
		//验证
		control.verify();
		PowerMock.verifyAll();
	}

}
