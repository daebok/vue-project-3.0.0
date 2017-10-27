package com.rd.ifaes.core.core.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工具类-图片处理
 * 
 * @author xx
 * @version 2.0
 * @since 2014年1月28日
 */
public class ImageUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtil.class);
	
	private static final String IMAGE_IGNORE_WORDS_EVAL="eval";
	private static final String IMAGE_IGNORE_WORDS_PHP="<?php";
	
	private ImageUtil() {}
	
	/**
	 * 校验文件是否是图片，是:true，否:false
	 * 
	 * @param file 需要验证的File文件
	 * @return true or false
	 */
	@SuppressWarnings("resource")
	public static boolean fileIsImage(File file) {
		InputStream is = null;
		BufferedReader reader = null;
		try{
			// 将文件转换成输入流
			is = new FileInputStream(file);
			// 用image IO读取文件，如果文件file不是图片，则为null
			BufferedImage image = ImageIO.read(is);
			if(image == null){
				return false;
			}
			reader = new BufferedReader(new FileReader(file));
			String exits = null;
			while ((exits = reader.readLine()) != null) {
				exits = shiftD(exits);
				if(exits.indexOf(IMAGE_IGNORE_WORDS_EVAL) >= 0 || exits.indexOf(IMAGE_IGNORE_WORDS_PHP) >= 0) {
					return false;
				}
			}
			return true;
		} catch (IOException e) {
			LOGGER.warn(e.getMessage(), e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String shiftD(String str) {
		int size = str.length();
		char[] chs = str.toCharArray();
		for (int i = 0; i < size; i++) {
			if (chs[i] <= 'Z' && chs[i] >= 'A') {
				chs[i] = (char) (chs[i] + 32);
			}
		}
		return new String(chs);
	}

}
