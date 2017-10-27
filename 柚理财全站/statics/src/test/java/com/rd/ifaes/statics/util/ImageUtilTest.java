package com.rd.ifaes.statics.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageIO.class})
public class ImageUtilTest {
	
	/**
	 * 校验文件是否是图片
	 * @throws Exception
	 */
	@Test
	public void testFileIsImage() throws Exception {
		BufferedImage image = new BufferedImage(1, 1, 1);
		
		PowerMock.mockStatic(ImageIO.class);
		EasyMock.expect(ImageIO.read(EasyMock.anyObject(InputStream.class))).andThrow(new IOException("msg"));
		EasyMock.expect(ImageIO.read(EasyMock.anyObject(InputStream.class))).andReturn(null).times(1);
		EasyMock.expect(ImageIO.read(EasyMock.anyObject(InputStream.class))).andReturn(image).times(1);
		PowerMock.replay(ImageIO.class);
		
        File f = new File(System.getProperty("user.dir")+"\\src\\main\\webapp\\data\\upload\\images\\default_portrait.jpg");
        try{
        	ImageUtil.fileIsImage(f);
        }catch(Exception e){
        }
        ImageUtil.fileIsImage(f);
        ImageUtil.fileIsImage(f);
        PowerMock.verify(ImageIO.class);
	}
	
	@Test
	public void testFileIsImageTwo() throws Exception {
        ImageUtil.fileIsImage(new File(System.getProperty("user.dir")+"\\2.jpg"));
	}
	
}
