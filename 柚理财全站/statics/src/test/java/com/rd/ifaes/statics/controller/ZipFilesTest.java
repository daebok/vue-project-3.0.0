package com.rd.ifaes.statics.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sun.misc.BASE64Decoder;

public class ZipFilesTest {
	
	@Test
	public void testzipFilesFirst() {
		StaticController controller = new StaticController();
		
		
		List<File> srcfileList = new ArrayList<File>();
		//数据模拟S
		File file = new File(System.getProperty("user.dir")+
				"\\src\\main\\webapp\\data\\img\\test\\data\\zip\\2016\\20161031.jpg");
		File zipfile = new File(System.getProperty("user.dir")+
				"\\src\\main\\webapp\\data\\img\\test\\data\\zip\\2016\\20161031.zip");
		
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
			fout.write(new BASE64Decoder().decodeBuffer("jpg"));
			fout = new FileOutputStream(zipfile);
			fout.write(new BASE64Decoder().decodeBuffer("zip"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		srcfileList.add(file);
		//数据模拟E
		controller.zipFiles(srcfileList, zipfile);
	}
	
	@Test
	public void testzipFilesSecond() {
		StaticController controller = new StaticController();
		
		List<File> srcfileList = new ArrayList<File>();
		srcfileList.add(new File("dd"));
		//数据模拟S
		File zipfile = null;
		//数据模拟E
		controller.zipFiles(srcfileList, zipfile);
	}
	
	@Test
	public void testzipFilesThird() {
		StaticController controller = new StaticController();
		
		List<File> srcfileList = new ArrayList<File>();
		//数据模拟S
		File zipfile = null;
		//数据模拟E
		controller.zipFiles(srcfileList, zipfile);
	}
}
