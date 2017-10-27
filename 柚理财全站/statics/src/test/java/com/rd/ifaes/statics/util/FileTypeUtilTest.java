package com.rd.ifaes.statics.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageIO.class,ImageUtil.class})
public class FileTypeUtilTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetImageFileType() throws Exception {
		IMocksControl control = EasyMock.createControl();
		
		BufferedImage bufreader = control.createMock(BufferedImage.class);
		EasyMock.expect(bufreader.getWidth()).andReturn(0);
		EasyMock.expect(bufreader.getWidth()).andReturn(10).times(4);
		EasyMock.expect(bufreader.getHeight()).andReturn(0).times(2);
		EasyMock.expect(bufreader.getHeight()).andReturn(10).times(3);
		
		ImageInputStream iis = control.createMock(ImageInputStream.class);
		iis.close();
		EasyMock.expectLastCall();
		
		ImageReader reader = control.createMock(ImageReader.class);
		EasyMock.expect(reader.getFormatName()).andReturn("");
		
		Iterator<ImageReader> iter = control.createMock(Iterator.class);
		EasyMock.expect(iter.hasNext()).andReturn(false);
		EasyMock.expect(iter.hasNext()).andReturn(true);
		EasyMock.expect(iter.next()).andReturn(reader);
		
		control.replay();
		
		PowerMock.mockStatic(ImageIO.class);
		EasyMock.expect(ImageIO.read(EasyMock.anyObject(File.class))).andThrow(new IOException("msg"));
		EasyMock.expect(ImageIO.read(EasyMock.anyObject(File.class))).andReturn(bufreader).times(5);
		EasyMock.expect(ImageIO.createImageInputStream(EasyMock.anyObject(File.class))).andReturn(iis).times(2);
		EasyMock.expect(ImageIO.createImageInputStream(EasyMock.anyObject(File.class))).andThrow(new IOException("MSG"));
		EasyMock.expect(ImageIO.getImageReaders(EasyMock.anyObject(ImageInputStream.class))).andReturn(iter).times(2);
		PowerMock.replay(ImageIO.class);
		
		//单元测试
		try{
			FileTypeUtil.getImageFileType(new File("D:/"));
		}catch(Exception e){
		}
		FileTypeUtil.getImageFileType(new File("D:/"));
		FileTypeUtil.getImageFileType(new File("D:/"));
		FileTypeUtil.getImageFileType(new File("D:/"));
		FileTypeUtil.getImageFileType(new File("D:/"));
		try{
		FileTypeUtil.getImageFileType(new File("D:/"));
		}catch(Exception e){
		}
		//验证
		control.verify();
		PowerMock.verify(ImageIO.class);
	}
	
	/**
	 * 判断上传 类型是否 是jpg/png/gif/pdf 类型
	 * @throws IOException 
	 */
	@Test
	public void testCheckFileType() throws IOException{
		File f1 = new File(System.getProperty("user.dir")+"\\1.jpg");
		System.out.println(f1.createNewFile());
		FileTypeUtil.checkFileType(f1);
		
		
     	FileWriter fr=new FileWriter(System.getProperty("user.dir")+"\\1.jpg",true);
        fr.write("123");   //写入一行字符串
        fr.close();
        FileTypeUtil.checkFileType(f1);
        System.out.println(f1.delete());
        
        PowerMock.mockStatic(ImageUtil.class);
		EasyMock.expect(ImageUtil.fileIsImage(EasyMock.anyObject(File.class))).andReturn(false);
		EasyMock.expect(ImageUtil.fileIsImage(EasyMock.anyObject(File.class))).andReturn(true);
		PowerMock.replay(ImageUtil.class);
        
        File f2 = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\upload\\images\\default_portrait.jpg");
		FileTypeUtil.checkFileType(f2);
		FileTypeUtil.checkFileType(f2);
		PowerMock.verify(ImageUtil.class);
	}
	
    @Test
    public void testGetFileHexString(){
    	byte[] b = new byte[0];
    	FileTypeUtil.getFileHexString(null);
    	FileTypeUtil.getFileHexString(b);
    }
}
