package com.rd.ifaes.statics.controller;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.multipart.MultipartFile;

import com.rd.ifaes.statics.util.OrderNoUtil;
import com.rd.ifaes.statics.util.PropertiesUtil;


@RunWith(PowerMockRunner.class)
@PrepareForTest({OrderNoUtil.class, PropertiesUtil.class, ImageIO.class})//声明要Mock的类 
public class HttpClientSaveTest {
	
	@Test
	public void testhttpClientSaveFirst() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("contract");
		EasyMock.expect(request.getParameter("prefix")).andReturn("prefix");
		
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
		controller.httpClientSave(upload, request, response);
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	@Test
	public void testhttpClientSaveSecond() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("contract");
		EasyMock.expect(request.getParameter("prefix")).andReturn("prefix");
		
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test");
		
		File file = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\img\\test\\data\\pdf\\contract\\"+new SimpleDateFormat("yyyyMMdd").format(new Date()));
		if (!file.exists()) {
			file.mkdirs();
		}
		
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("png");
		upload.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall().andThrow(new IOException());
		
		PowerMock.mockStatic(OrderNoUtil.class);//Mock静态方法
		EasyMock.expect(OrderNoUtil.getSerialNumber()).andReturn("1011");
		
		PowerMock.replayAll();
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.httpClientSave(upload, request, response);
		
		//verify
		PowerMock.verifyAll();
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	@Test
	public void testhttpClientSaveThird() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("");
		EasyMock.expect(request.getParameter("prefix")).andReturn("prefix");
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("png");
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.httpClientSave(upload, request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	@Test
	public void testhttpClientSaveFourth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn(null);
		EasyMock.expect(request.getParameter("prefix")).andReturn("prefix");
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("png");
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.httpClientSave(upload, request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	@Test
	public void testhttpClientSaveSixth() throws Exception {
		StaticController controller = new StaticController();
		//request
		HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		//session
		HttpSession session = EasyMock.createMock(HttpSession.class);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("nid");
		EasyMock.expect(request.getParameter("prefix")).andReturn("prefix");
		//servletContext
		ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		//response
		HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
		//MultipartFile upload
		MultipartFile upload = EasyMock.createMock(MultipartFile.class);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("sdds");
		
		EasyMock.replay(request);
		EasyMock.replay(session);
		EasyMock.replay(servletContext);
		EasyMock.replay(response);
		EasyMock.replay(upload);
		//execute method
		controller.httpClientSave(upload, request, response);
		
		//verify
		EasyMock.verify(request);
		EasyMock.verify(session);
		EasyMock.verify(servletContext);
		EasyMock.verify(response);
		EasyMock.verify(upload);
	}
	
	@Test
	public void testhttpClientSaveSeven() throws Exception{
		IMocksControl control = EasyMock.createControl();
		StaticController controller = EasyMock.createMockBuilder(StaticController.class)
				.addMockedMethod("getFolder")
				.createMock();
		EasyMock.expect(controller.getFolder()).andReturn("");
		EasyMock.replay(controller);
		//request
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
		//session
		HttpSession session = control.createMock(HttpSession.class);
		//request expect
		EasyMock.expect(request.getSession()).andReturn(session);
		//request.getParameter("path")
		EasyMock.expect(request.getParameter("nid")).andReturn("borrow");
		EasyMock.expect(request.getParameter("prefix")).andReturn("png");
		
		//servletContext
		ServletContext servletContext = control.createMock(ServletContext.class);
		//session expect
		EasyMock.expect(session.getServletContext()).andReturn(servletContext);
		//servletContext expect
		EasyMock.expect(servletContext.getRealPath("/")).andReturn(System.getProperty("user.dir"));
		
		//response
		HttpServletResponse response = control.createMock(HttpServletResponse.class);
		
		//MultipartFile upload
		MultipartFile upload = control.createMock(MultipartFile.class);
		EasyMock.expect(upload.getOriginalFilename()).andReturn("default_portrait.jpg");
		upload.transferTo(EasyMock.anyObject(File.class));
		EasyMock.expectLastCall().times(2);
		//OrderNoUtil
		PowerMock.mockStatic(OrderNoUtil.class);//Mock静态方法
		EasyMock.expect(OrderNoUtil.getSerialNumber()).andReturn("default_portrait");
		//PropertiesUtil
		PowerMock.mockStatic(PropertiesUtil.class);//Mock静态方法
		EasyMock.expect(PropertiesUtil.getMessage(EasyMock.anyString())).andReturn("/src/main/webapp/data/img/avatar/");
		
		BufferedImage bufferedImg = control.createMock(BufferedImage.class);
		EasyMock.expect(bufferedImg.getWidth()).andReturn(1);
		EasyMock.expect(bufferedImg.getHeight()).andReturn(1);
		
		PowerMock.mockStatic(ImageIO.class);
		EasyMock.expect(ImageIO.read(EasyMock.anyObject(FileInputStream.class))).andReturn(bufferedImg);
		
		
		PowerMock.replayAll();
		control.replay();
		
		//execute method
		controller.httpClientSave(upload, request, response);
		
		//verify
		PowerMock.verifyAll();
		control.verify();
		EasyMock.verify(controller);
	}
	
	/**
	 * 以jpg格式保存文件
	 * @throws Exception 
	 */
	@Test
	public void testsaveImageAsJpgOne() throws Exception{
		ColorModel colorModel = EasyMock.createMock(ColorModel.class);
		EasyMock.expect(colorModel.createCompatibleWritableRaster(EasyMock.anyInt(), EasyMock.anyInt())).andReturn(null);
		EasyMock.expect(colorModel.isCompatibleRaster(EasyMock.anyObject(WritableRaster.class))).andReturn(false);
		EasyMock.replay(colorModel);
		
		BufferedImage image = EasyMock.createMock(BufferedImage.class);
		EasyMock.expect(image.getType()).andReturn(BufferedImage.TYPE_CUSTOM);
		EasyMock.expect(image.getWidth()).andReturn(100).times(2);
		EasyMock.expect(image.getHeight()).andReturn(100);
		EasyMock.expect(image.getColorModel()).andReturn(colorModel);
		EasyMock.replay(image);
		
		PowerMock.mockStatic(ImageIO.class);//Mock静态方法
		EasyMock.expect(ImageIO.read(EasyMock.anyObject(File.class))).andReturn(image);
//		EasyMock.expect(ImageIO.write(EasyMock.anyObject(BufferedImage.class),EasyMock.anyString(),EasyMock.anyObject(File.class))).andReturn(true);
		PowerMock.replay(ImageIO.class);
		//测试
		StaticController controller = new StaticController();
		try{
			controller.saveImageAsJpg("D:\\a.png", "D:\\b", 120, 110, true);
		}catch(IllegalArgumentException e){
		}
		//验证
		EasyMock.verify(colorModel);
		EasyMock.verify(image);
		PowerMock.verify(ImageIO.class);
	}
	
	/**
	 * 以jpg格式保存文件
	 * @throws Exception 
	 */
/*	@Test
	public void testsaveImageAsJpgTwo() throws Exception{
		ColorModel colorModel = EasyMock.createMock(ColorModel.class);
		EasyMock.expect(colorModel.createCompatibleWritableRaster(EasyMock.anyInt(), EasyMock.anyInt())).andReturn(null);
		EasyMock.expect(colorModel.isCompatibleRaster(EasyMock.anyObject(WritableRaster.class))).andReturn(false);
		EasyMock.replay(colorModel);
		
		Graphics2D g = EasyMock.createMock(Graphics2D.class);
		g.setRenderingHint(EasyMock.anyObject(Key.class), EasyMock.anyObject());
		EasyMock.expectLastCall();
		g.drawRenderedImage(EasyMock.anyObject(BufferedImage.class), EasyMock.anyObject(AffineTransform.class));
		EasyMock.expectLastCall();
		g.dispose();
		EasyMock.expectLastCall();
		EasyMock.replay(g);
		
		BufferedImage image = EasyMock.createMock(BufferedImage.class);
		EasyMock.expect(image.getType()).andReturn(BufferedImage.TYPE_INT_ARGB);
		EasyMock.expect(image.getWidth()).andReturn(100).times(10);
		EasyMock.expect(image.getHeight()).andReturn(100).times(10);
		EasyMock.expect(image.getHeight(EasyMock.anyObject(ImageObserver.class))).andReturn(100).times(10);
		EasyMock.expect(image.getColorModel()).andReturn(colorModel);
		EasyMock.expect(image.createGraphics()).andReturn(g);
		EasyMock.replay(image);
		
		PowerMock.mockStatic(ImageIO.class);//Mock静态方法
		EasyMock.expect(ImageIO.read(EasyMock.anyObject(File.class))).andReturn(image);
		EasyMock.expect(ImageIO.write(EasyMock.anyObject(BufferedImage.class),EasyMock.anyString(),EasyMock.anyObject(File.class))).andReturn(true);
		PowerMock.replay(ImageIO.class);
		
		PowerMock.mockStatic(AffineTransform.class);//Mock静态方法
		EasyMock.expect(AffineTransform.getScaleInstance(EasyMock.anyDouble(),EasyMock.anyDouble())).andReturn(null);
		PowerMock.replay(AffineTransform.class);
		
		//测试
		StaticController controller = new StaticController();
		try{
			controller.saveImageAsJpg("D:\\a.png", "D:\\b", 100, 110, false);
		}catch(Exception e){
		}
		//验证
		EasyMock.verify(colorModel);
		EasyMock.verify(image);
		EasyMock.verify(g);
		PowerMock.verify(ImageIO.class);
	}*/
	
	@SuppressWarnings("static-access")
	@Test
	public void testsaveImageAsJpgThree() throws Exception{
		PowerMock.mockStatic(ImageIO.class);//Mock静态方法
		EasyMock.expect(ImageIO.read(EasyMock.anyObject(File.class))).andReturn(new BufferedImage(1, 1, 1));
		EasyMock.expect(ImageIO.write(EasyMock.anyObject(BufferedImage.class),EasyMock.anyString(),EasyMock.anyObject(File.class))).andReturn(true);
		PowerMock.replay(ImageIO.class);

		
		//测试
		StaticController controller = new StaticController();
		try{
			controller.saveImageAsJpg("D:\\a.png", "D:\\b", 0, 0, false);
		}catch(Exception e){
		}
		//验证
		PowerMock.verify(ImageIO.class);
	}
	
	/**
	 * 判断投资合同是否存在
	 */
	@Test
	public void testjudgeProjectProtocolFileIsExists(){
		StaticController controller = EasyMock.createMockBuilder(StaticController.class)
				.addMockedMethod("getFilePdfPath",String.class,String.class,String.class,String.class)
				.createMock();
		EasyMock.expect(controller.getFilePdfPath(EasyMock.anyString(),EasyMock.anyString(),EasyMock.anyString(),EasyMock.anyString())).andReturn("D:/");
		EasyMock.replay(controller);
		
		IMocksControl control = EasyMock.createControl();
		HttpServletRequest request = control.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getParameter(EasyMock.anyString())).andReturn("").times(3);
		
		HttpSession session = control.createMock(HttpSession.class);
		ServletContext servlet = control.createMock(ServletContext.class);
		EasyMock.expect(servlet.getRealPath("/")).andReturn("aaa");
		EasyMock.expect(session.getServletContext()).andReturn(servlet);
		EasyMock.expect(request.getSession()).andReturn(session).times(1);
		
		HttpServletResponse response = control.createMock(HttpServletResponse.class);
		response.setStatus(EasyMock.anyInt());
		EasyMock.expectLastCall();
		
		control.replay();
		controller.judgeProjectProtocolFileIsExists(request, response);
		control.verify();
		EasyMock.verify(controller);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testResize(){
		BufferedImage source = EasyMock.createMock(BufferedImage.class);
		EasyMock.expect(source.getType()).andReturn(BufferedImage.TYPE_INT_RGB).times(1);
		EasyMock.expect(source.getWidth()).andReturn(100).times(1);
		EasyMock.expect(source.getWidth(EasyMock.anyObject(ImageObserver.class))).andReturn(100).times(1);
		EasyMock.expect(source.getHeight()).andReturn(100).times(2);
		EasyMock.expect(source.getHeight(EasyMock.anyObject(ImageObserver.class))).andReturn(100).times(1);
		EasyMock.expect(source.getColorModel()).andThrow(new NullPointerException("msg"));
		EasyMock.replay(source);
		
		StaticController controller = new StaticController();
		try{
			controller.resize(source, 100, 200, true);
		}catch(Exception e){
		}
		
		EasyMock.verify(source);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testResizeTwo() throws IOException{
		File fromFile=new File("E://project//p2p_v3_branch//statics//src//main//webapp//data//img//avatar//default_portrait.png"); 
		BufferedImage srcImage=ImageIO.read(fromFile);
		StaticController controller = new StaticController();
		try{
			controller.resize(srcImage, 100, 200, true);
		}catch(Exception e){
		}
	}
}
