package com.rd.ifaes.statics.controller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

import com.rd.ifaes.statics.util.MD5;
import com.rd.ifaes.statics.util.OrderNoUtil;
import com.rd.ifaes.statics.util.PrintJsonUtil;
import com.rd.ifaes.statics.util.PropertiesUtil;
 
@SuppressWarnings("restriction")
@Controller
public class StaticController{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(StaticController.class);
	
	private static final int MAX_IMAGE_SIZE = 1*1024*1024;
	
	//路径拼接符 "/"
	private static String separator = File.separator;
	
	private final String tokenCode = PropertiesUtil.getMessage("tokenCode");//下载加密密钥，跟下载链接保持一致
	 
	private String nid;
	 
	/**
	 * 读取头像方法
	 * 
	 * @param url
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload/readImg")
	public void readImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//读取文件相对路径
		String path = request.getParameter("path");
		String contextPath = request.getSession().getServletContext().getRealPath("/");
		if(StringUtils.isBlank(path))
			return ;
		 
		String url = contextPath+path;
		File file = new File(url);
		//如果图片不存在 返回 
		if(!file.exists()){
			return  ;
		}
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		OutputStream output = response.getOutputStream();// 得到输出流
		if (url.toLowerCase().endsWith(".jpg")) {
			// 表明生成的响应是图片
			response.setContentType("image/jpeg");
		} else if (url.toLowerCase().endsWith(".gif")) {
			response.setContentType("image/gif");
		}
		InputStream imageIn = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(imageIn);// 输入缓冲流
		BufferedOutputStream bos = new BufferedOutputStream(output);// 输出缓冲流
		byte data[] = new byte[4096];// 缓冲字节数
		int size = 0;
		size = bis.read(data);
		while (size != -1) {
			bos.write(data, 0, size);
			size = bis.read(data);
		}
		bis.close();
		bos.flush();// 清空输出缓冲流
		bos.close();
		output.flush();
		output.close();
	}
	
	 /**
	  * httpClient请求  保存图片的公用方法
	  * @return
	 * @throws Exception 
	  */
	@RequestMapping(value = "/upload/httpClientSave")
	@ResponseBody
	public String httpClientSave(@RequestParam(value = "upload", required = true) MultipartFile upload, HttpServletRequest request, HttpServletResponse response) throws Exception{

		String nid = request.getParameter("nid");
		String prefix = request.getParameter("prefix");
		
		LOGGER.info("nid:{}",nid);		
		if (!checkFileType(upload.getOriginalFilename())) {
			return "error";
		 }
		
		if(nid==null||nid=="") return "request param nid is requied";
		//定义保存的文件名
		String fileName = OrderNoUtil.getSerialNumber()+"."+prefix;
		//获取save的相对路径
		String filePath = getFilePath(nid)+getFolder();
		//获取save的绝对路径
		String savePath = getSavePath(request, filePath);
		//试图创建这个file 如果没有该目录则创建
		File files = new File(savePath);
		if (!files.exists()) {
			try {
				files.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//创建要生成的文件
		File file=new File(savePath + separator + fileName);
		try {
			//把上传文件copy到要生成的文件里
			upload.transferTo(file);
			if("borrow".equals(nid)||"mortgage".equals(nid)){//移动端修改   先做压缩借款资料
				LOGGER.info("《移动端准备复制图片--start》！"+fileName);
				FileInputStream fis = new FileInputStream(file);
				BufferedImage bufferedImg = ImageIO.read(fis);
				int imgWidth = bufferedImg.getWidth();
				int imgHeight = bufferedImg.getHeight();
				String ps=String.format("%.1f",file.length()/1024.0);
				float picsize = Float.parseFloat(ps);
				LOGGER.info("》》》移动端复制图片 --图片大小为:"+picsize+"名称"+fileName);
				if(picsize>145){//如果文件大小大于145K 就进行压缩处理
					LOGGER.info("》》》移动端复制图片 --图片宽度为:"+imgWidth+"名称"+fileName);
					if(imgWidth>750){//宽度大于750的 以宽度为750等比例压缩
						try {
							LOGGER.info("》》》移动端复制图片 --图片宽度为:"+imgWidth+"名称"+fileName);
							saveImageAsJpg(savePath + separator + fileName,savePath + separator + fileName.replaceAll("."+prefix, "")+"mobile."+prefix,750,imgHeight,true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						}else{//否则 以原尺寸压缩 文件大小
							saveImageAsJpg(savePath + separator + fileName,savePath + separator + fileName.replaceAll("."+prefix, "")+"mobile."+prefix,imgWidth,imgHeight,true);
						}
					LOGGER.info("《移动端准备复制图片--end》！"+"名称"+fileName);
					}else{//否则 直接复制一份
						//创建要生成的文件  移动端用
						File fileMobile=new File(savePath + separator + fileName.replaceAll("."+prefix, "")+"mobile."+prefix);
						//把移动端上传文件copy到要生成的文件里
						upload.transferTo(fileMobile);
					}
			}
		} catch (IOException e) {
			LOGGER.error("上传失败：{}",e.getMessage());
			e.printStackTrace();
		}
		//把保存的相对路径返回到客户端		
		return filePath +  "/"  + fileName;
	 } 
	/**
	  * 等比例压缩图片 
	  * 移动端专用
	  * @return
	  */
	public static void saveImageAsJpg(String fromFileStr, String saveToFileStr, int width, int height,boolean equalProportion) throws Exception {  
        BufferedImage srcImage;  
        String imgType="JPEG";  
        if(fromFileStr.toLowerCase().endsWith(".png")){  
            imgType="PNG";  
        }  
        File fromFile=new File(fromFileStr);  
        File saveFile=new File(saveToFileStr);  
        srcImage=ImageIO.read(fromFile);  
        if(width>0||height>0){  
            srcImage=resize(srcImage,width,height,equalProportion);  
        }  
        ImageIO.write(srcImage,imgType,saveFile); 
    }  
	
	/**
	 * 调整图片大小
	 * @param source
	 * @param targetW
	 * @param targetH
	 * @param equalProportion
	 * @return
	 */
	public static BufferedImage resize(BufferedImage source,int targetW,int targetH,boolean equalProportion){  
	        int type=source.getType();  
	        BufferedImage target=null;  
	        double sx=(double)targetW/source.getWidth();  
	        double sy=(double)targetH/source.getHeight();  
	        if(equalProportion){  
	            if(sx>sy){  
	                sx=sy;  
	                targetW=(int)(sx*source.getWidth());  
	            }else{  
	                sy=sx;  
	                targetH=(int)(sx*source.getHeight());  
	            }  
	        }  
	        if(type==BufferedImage.TYPE_CUSTOM){  
	            ColorModel cm=source.getColorModel();  
	            WritableRaster raster=cm.createCompatibleWritableRaster(targetW,targetH);  
	            boolean alphaPremultiplied=cm.isAlphaPremultiplied();  
	            target=new BufferedImage(cm,raster,alphaPremultiplied,null);  
	        }else{  
	            target=new BufferedImage(targetW,targetH,type);  
	            Graphics2D g=target.createGraphics();  
	            g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);  
	            g.drawRenderedImage(source,AffineTransform.getScaleInstance(sx,sy));  
	            g.dispose();  
	        }  
	        return target;  
	    }  
	 /**
	  * 使用uploadify方式 请求的保存方法
	  * 图片限制
	  * @return
	  */
	@RequestMapping(value = "/upload/uploadifySave")
	@ResponseBody
	//@CrossOrigin(origins = "*",methods={RequestMethod.POST,RequestMethod.OPTIONS})
	public String uploadifySave(@RequestParam(value = "upload", required = true) MultipartFile upload,
			HttpServletRequest request, HttpServletResponse response) {
		
		LOGGER.info("uploadify上传..");

		if(upload.getSize() > MAX_IMAGE_SIZE){
			LOGGER.error("文件超出最大限制，当前文件大小：{}",upload.getSize());
			return "error";
		}
		
		String originalFilename = upload.getOriginalFilename();
		 LOGGER.debug("originalFilename:{}",originalFilename);	
		 
		if (!checkFileType(originalFilename)) {
			return "error";
		}
		String nid = request.getParameter("nid");
		String filePath = getFilePath(nid) + getFolder();
		String savePath = getSavePath(request, filePath);
		// 取文件后缀名
		String prefix = getFileExt(originalFilename);

		String fileName = OrderNoUtil.getSerialNumber() + "." + prefix;
		File files = new File(savePath);
		if (!files.exists()) {
			try {
				files.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		File file = new File(savePath + separator + fileName);
		try {
			upload.transferTo(file);
		} catch (IOException e) {
			LOGGER.error("上传失败：{}",e.getMessage());
			e.printStackTrace();
		}		
		return filePath +  "/"  + fileName;
	}
	
	/**
	 * 上传企业资质文件
	 * 
	 * @return
	 */
	@RequestMapping(value = "/upload/uploadCompanyAudit")
	@ResponseBody
	public Object uploadCompanyAudit(@RequestParam(value = "upload", required = true) MultipartFile upload,
			String userId,String nid,String fileName,HttpServletRequest request) {
		String uploadFileName = upload.getOriginalFilename();
		
		LOGGER.debug("uploadFileName ={} ", uploadFileName);
		
		if (!checkFileType(uploadFileName, "jpg")) {
			return "error";
		}

		String filePath = getFilePath(nid) + userId;
		String savePath = getSavePath(request, filePath);
		File files = new File(savePath);
		if (!files.exists()) {
			try {
				files.mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			fileName = URLDecoder.decode(fileName, "utf-8") + ".jpg";
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		File file = new File(savePath + separator + fileName);
		try {
			upload.transferTo(file);
		} catch (IOException e) {
			LOGGER.error("上传失败：{}",e);
		}
		return filePath +  separator + fileName;
	}
	 
	/**
	 * 头像上传 
	 * @return
	 */
	@RequestMapping(value = "/upload/avatar")
	public String avatar(HttpServletRequest request, HttpServletResponse response) {
		//上传图片、文件上传路径、文件保存路径
		String pic = request.getParameter("png1");
		String weburl = request.getParameter("weburl");

		if(pic==null||pic=="") return null;
		String filePath = getFilePath("avatar");
		String savePath = getSavePath(request,filePath);
		String fileName = OrderNoUtil.getSerialNumber() + ".jpg";
		File file = new File(savePath  + fileName);
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
			fout.write(new BASE64Decoder().decodeBuffer(pic));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//获取请求方的域名
		String requestUrl = request.getHeader("Origin");
		if (requestUrl == null){
			return null;
		}
//		Pattern p = Pattern.compile("(http://|ftp://|https://|www){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)");
//		Matcher m = p.matcher(requestUrl);
//		 if(m.find()){
//				requestUrl = m.group(0);
//			}else {
//				Pattern  p1 = Pattern.compile("((http://|ftp://|https://){0,1}[localhost|\\d|\\.|:]*?)/");
//				Matcher  m1 = p1.matcher(requestUrl);
//				if(m1.find()){
//					requestUrl =m1.group(1);
//				}
//			}
		LOGGER.info("URL来源：{}", requestUrl);
		weburl =  requestUrl + PropertiesUtil.getMessage("avatarUrl");
		//传递到平台的Url进行简单处理

		String url = weburl + "?avaPic=" + filePath+fileName;
		try {
			URLEncoder.encode(url, "UTF-8");
			response.sendRedirect(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * 头像上传
	 * 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/upload/avatarMobile")
	@ResponseBody
	public void appAvatar(HttpServletRequest request,@RequestParam(value = "upload", required = false) MultipartFile file) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		LOGGER.info("app头像上传---------开始------------");
		   if (file == null) {
			   LOGGER.info("app头像上传--------error-------------你上传的图片为空！");
				PrintJsonUtil.printSuccessJson(data, "0000", "你上传的图片为空！");
			}else{
		 String fileNames = file.getOriginalFilename(); 
		//验证上传文件
		  if (file.getSize() > MAX_IMAGE_SIZE) {
			  LOGGER.info("app头像上传--------error-------------您上传的图片大小不能超过1M,请重新上传！");
			PrintJsonUtil.printSuccessJson(data, "0000", "您上传的图片大小不能超过1M,请重新上传！");
		} else if (!checkFileType(fileNames)) {
			LOGGER.info("app头像上传--------error-------------您上传的图片无效，请重新上传！");
			PrintJsonUtil.printSuccessJson(data, "0000", "您上传的图片无效，请重新上传！");
		}else{
		LOGGER.info("app头像上传--------验证通过-------------");
		String filePath = getFilePath("avatar");
		String savePath = getSavePath(request,filePath);
		String fileName = OrderNoUtil.getSerialNumber();
		File descFile = new File(savePath  + "/" + fileName+ ".jpg");
		if (!descFile.getParentFile().exists()) {
			try {
				descFile.getParentFile().mkdirs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("app头像上传--------开始复制-------------");
		try {
			//FileUtils.copyFile(file, descFile);
			file.transferTo(descFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER.info("app头像上传--------结束-------------");
		//头像保存为jpg格式
		String prefix = descFile.getName();
		data.put("imgUrl", pathHandle(getNid(), fileName,prefix));
		PrintJsonUtil.printSuccessJson(data, "39321", null);
		}
			}
	}
	/**
	 * 传递给平台放路径处理
	 * @param nid  类型
	 * @param fileName 图片名字
	 * @return
	 */
	public String pathHandle(String nid,String fileName, String prefix){
		String url = "/data/img/avatar/" + prefix;
		return url;
	}
	/**
	 * 删除图片（主要用户平台资料上传的删除）
	 * @return
	 */
	@RequestMapping(value = "/upload/deleteImg")
	public String deleteImg(HttpServletRequest request, HttpServletResponse response){
		//图片地址 如: http://localhost:8084/data/img/userinfo/20150506/1505060420614194.jpg
		String url = request.getParameter("imgUrl");
		LOGGER.info("删除URL：{}", url);
		//请求的host：指图片服务器的域名
		String imgUrl = request.getHeader("Host");
		//截取图片地址中的 域名部分
		String newUrl[] = url.split(imgUrl);
		String contextPath = request.getSession().getServletContext().getRealPath("/");
		if(newUrl.length>0){
			url = contextPath + newUrl[newUrl.length-1];
		}
		File file = new File(url);
		if(file.exists()){
			file.delete();
		}
		return null;
	}
	
	/**
	 * MD5加密字符串方法
	 * @param fileName
	 * @return
	 */
	public String getMd5Str(String fileName){
		MD5 md5 = new MD5();
		return md5.getMD5ofStr(fileName);
	}
	
	/**
	 * 取默认图片路径
	 * @return
	 */
	@RequestMapping(value = "/upload/getDefaultPath")
	public String getDefaultPath(HttpServletRequest request){
		String contextPath = request.getSession().getServletContext().getRealPath("/");
		String url = contextPath + separator +"data"+ separator + "upload" + separator + "images" + separator + "default_portrait.jpg";
		return url;
	}
	
	/**
	 * 获取完整的保存路径
	 * @param path
	 * @return
	 */
	public String getSavePath(HttpServletRequest request, String path){
		String contextPath = request.getSession().getServletContext().getRealPath("/");
		return contextPath+path;
	}
	
	
	/**
	 * 读取配置文件中的存储路径
	 * @param nid
	 * @return
	 */
	public String getFilePath(String nid){
		if(nid==null||nid=="")return "";
		return PropertiesUtil.getMessage(nid);
	}
	
	/**
	 * 取随机字符串
	 * @param length 字符串长度
	 * @return
	 */
	public String getRandomString(int length) { 
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789#%!~*";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer(); 
	    int number = 0;
	    for (int i = 0; i < length; i++) {   
	         number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }  
	
	/**
	 * 年月日目录
	 * @return
	 */
	public String getFolder(){
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		return formater.format(new Date());
	}
	
	/**
	 * 校验是否是图片
	 * @param fileName
	 * @return
	 */
	private boolean checkFileType(String fileName) {
	     final String[] fileTypeArr = {"gif" , "png" , "jpg" , "jpeg" , "bmp", "pdf"};
		 for(String fileType : fileTypeArr){
			if (fileName.toLowerCase().endsWith(fileType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 校验是否是图片
	 * @param fileName
	 * @return
	 */
	private boolean checkFileType(String fileName,String ftype) {
		final String[] fileTypeArr = ftype.split(",");
		for(String fileType : fileTypeArr){
			if (fileName.toLowerCase().endsWith(fileType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 取文件名后缀
	 * @param fileName
	 * @return
	 */
	private static String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.')+1);
	}

	@RequestMapping(value = "/upload/httpSavePdf")
	@ResponseBody
	public Object httpSavePdf(HttpServletRequest request, HttpServletResponse response, MultipartFile upload){
		PrintWriter out = null;
		String nid = request.getParameter("nid");
		if(StringUtils.isBlank(nid)){
			LOGGER.info("nid为空");
			nid = "contract";
		}
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			LOGGER.error("out创建报错：{}", e1.getMessage());
			return null;
		}
		
		//定义保存的文件名
		String projectId = request.getParameter("projectId");
		if(StringUtils.isBlank(projectId)){
			LOGGER.error("projectId为空");
			out.print("error");
			out.flush();
		    out.close();
			return null;
		}
		String investId = request.getParameter("investId");
		if(StringUtils.isBlank(investId)){
			LOGGER.error("investId为空");
			out.print("error");
			out.flush();
		    out.close();
			return null;
		}
		
		
		String prefix = request.getParameter("prefix");
		if(StringUtils.isBlank(prefix)){
			LOGGER.error("prefix为空");
			out.print("error");
			out.flush();
		    out.close();
			return null;
		}
		
		//取规定的文件格式对应字符码
//		String prefixStr = FileTypeUtil.FILE_TYPE_MAP.get(prefix);
//		if (!FileTypeUtil.checkFileType(upload, "") || StringUtil.isBlank(nid) || StringUtil.isBlank(prefixStr)) {
//			LOGGER.error("prefix即后缀名为空或者查询不对：" + prefix);
//			out.print("error");
//			out.flush();
//		    out.close();
//			return null;
//		 }
		
		//获取save的相对路径
		String filePath = getFilePath(nid) + projectId;
		//获取save的绝对路径
		String savePath = getSavePath(request,filePath);
		//试图创建这个file 如果没有该目录则创建
		File file = new File(savePath);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception e) {
				LOGGER.error("创建目录失败：{}", e.getMessage());
				return null;
			}
		}

		//借款人生成zip压缩包,投资人的协议全部打包，借款人的协议不用打包
		String bulidZip = request.getParameter("bulidZip");
		if("true".equals(bulidZip)){
			File[] files = file.listFiles(new MyFilter("pdf"));
			List<File> fileList = Arrays.asList(files);
			zipFiles(fileList,new File(savePath + separator + projectId + ".zip"));
		}
		
		//创建要生成的文件
		String returnStr = savePath + separator + investId + "."+prefix;
		File localFile = new File(returnStr);
		try {
			upload.transferTo(localFile);
		} catch (IllegalStateException e) {
			LOGGER.error("httpSavePdf处理是失败，{}",e.getMessage());
			return null;
		} catch (IOException e) {
			LOGGER.error("httpSavePdf处理是失败，{}",e.getMessage());
			return null;
		}
		//把保存的相对路径返回到客户端
		out.print(filePath + separator + investId + "."+prefix);//直接用ResponseBody返回报错。。。
		out.flush();
	    out.close();
		return null;
	 } 
	
	
	/**
	 * 债权协议复制到静态服务器
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @param request
	 * @param response
	 * @param upload
	 * @return
	 */
	@RequestMapping(value = "/upload/httpSaveBondPdf")
	@ResponseBody
	public Object httpSaveBondPdf(HttpServletRequest request, HttpServletResponse response, MultipartFile upload){
		LOGGER.info("httpSaveBondPdf开始处理");
		PrintWriter out = null;
		String nid = request.getParameter("nid");
		if(StringUtils.isBlank(nid)){
			LOGGER.info("nid为空");
			nid = "contract";
		}
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			LOGGER.error("out创建报错：{}", e1.getMessage());
			return null;
		}
		
		//定义保存的文件名
		String bondId = request.getParameter("bondId");
		if(StringUtils.isBlank(bondId)){
			LOGGER.error("bondId为空");
			out.print("error");
			out.flush();
		    out.close();
			return null;
		}
		String bondInvestId = request.getParameter("bondInvestId");
		if(StringUtils.isBlank(bondInvestId)){
			LOGGER.error("bondInvestId为空");
			out.print("error");
			out.flush();
		    out.close();
			return null;
		}
		
		
		String prefix = request.getParameter("prefix");
		if(StringUtils.isBlank(prefix)){
			LOGGER.error("prefix为空");
			out.print("error");
			out.flush();
		    out.close();
			return null;
		}
		
		//获取save的相对路径
		String filePath = getFilePath(nid) + bondId;
		//获取save的绝对路径
		String savePath = getSavePath(request,filePath);
		//试图创建这个file 如果没有该目录则创建
		File file = new File(savePath);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception e) {
				LOGGER.error("创建目录失败：{}", e.getMessage());
				return null;
			}
		}

		//借款人生成zip压缩包,投资人的协议全部打包，借款人的协议不用打包
		String bulidZip = request.getParameter("bulidZip");
		if("true".equals(bulidZip)){
			File[] files = file.listFiles(new MyFilter("pdf"));
			List<File> fileList = Arrays.asList(files);
			zipFiles(fileList,new File(savePath + separator + bondId + ".zip"));
		}
		
		//创建要生成的文件
		String returnStr = savePath + separator + bondInvestId + "."+prefix;
		File localFile = new File(returnStr);
		try {
			upload.transferTo(localFile);
		} catch (IllegalStateException e) {
			LOGGER.error("httpSavePdf处理是失败，{}",e.getMessage());
			return null;
		} catch (IOException e) {
			LOGGER.error("httpSavePdf处理是失败，{}",e.getMessage());
			return null;
		}
		LOGGER.info("httpSaveBondPdf处理成功");
		//把保存的相对路径返回到客户端
		out.print(filePath + separator + bondInvestId + "."+prefix);//直接用ResponseBody返回报错。。。
		out.flush();
	    out.close();
		return null;
	 } 
	
	
	/**
	 * 债权协议下载
	 * @author QianPengZhan
	 * @date 2016年9月30日
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload/downloadBondContract")
	public String downloadBondContract(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String bondId = request.getParameter("bondId");
		String bondInvestId = request.getParameter("bondInvestId");
		String token = request.getParameter("token");
		String downloadFileName = request.getParameter("fileName");
		String webUrl = request.getParameter("webUrl");
		LOGGER.info("downloadPdf,downloadFileName:{},bondId:{},invebondInvestIdstId:{}" ,downloadFileName,bondId,bondInvestId);

		if (StringUtils.isBlank(bondId)) {
			LOGGER.error("bondId为空,{}",bondId);
			return null;
		}
		if (StringUtils.isBlank(bondInvestId)) {
			LOGGER.error("bondInvestId为空,{}",bondInvestId);
			return null;
		}
		if (StringUtils.isBlank(downloadFileName)) {
			LOGGER.error("downloadFileName为空:{}", downloadFileName);
			return null;
		}else{
			downloadFileName = URLDecoder.decode(downloadFileName,"UTF-8");
		}
		
		//解析图片真实路径
		String path = getFilePdfBondPath("bondContract",bondId,bondInvestId, getFileExt(downloadFileName));
		LOGGER.info("path:{}",path);
		
		StringBuffer sb = new StringBuffer();
		sb.append(tokenCode).append(bondId).append(bondInvestId).append(tokenCode);
		if (StringUtils.isBlank(token)||!token.equals(getMd5Str(sb.toString()))) {
			LOGGER.error("token为空或者不正确,{}" ,token);
			return null;
		}

		//读取文件相对路径
		String contextPath = request.getSession().getServletContext().getRealPath("/");
		String url = contextPath + path;
		File file = new File(url);
		//如果图片不存在 返回Null
		if(!file.exists()){
			LOGGER.error("file不存在,{}" ,file.getAbsolutePath());
			StringBuilder urlSb = new StringBuilder(webUrl);
			try {
				urlSb.append("/member/myBond/buildBondProtocol.html").
				append("?bondId=").append(bondId).
				append("&bondInvestId=").append(bondInvestId).
				append("&type=").append(url.contains("zip")?"zip":"pdf");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				return null;
			}
			response.sendRedirect(urlSb.toString());
			return null;
		}
		
		if (url.toLowerCase().endsWith(".pdf")) {
			LOGGER.debug("pdf下载,{}",url);
			InputStream ins = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName, "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			ous.write(buffer);
			ous.flush();
			ous.close();
		}
		if (url.toLowerCase().endsWith(".zip")) {
			LOGGER.debug("zip下载,{}",url);
			InputStream ins = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName, "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/x-download");
			ous.write(buffer);
			ous.flush();
			ous.close();
		}
		return null;
	}
	/**
	 * 债权协议下载
	 * @author QianPengZhan
	 * @date 2016年9月30日
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload/downloadBondContractMobile")
	public String downloadBondContractMobile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String bondId = request.getParameter("bondId");
		String bondInvestId = request.getParameter("bondInvestId");
		String token = request.getParameter("token");
		String downloadFileName = request.getParameter("fileName");
		String webUrl = request.getParameter("webUrl");
		LOGGER.info("downloadPdf,downloadFileName:{},bondId:{},invebondInvestIdstId:{}" ,downloadFileName,bondId,bondInvestId);

		if (StringUtils.isBlank(bondId)) {
			LOGGER.error("bondId为空,{}",bondId);
			return null;
		}
		if (StringUtils.isBlank(bondInvestId)) {
			LOGGER.error("bondInvestId为空,{}",bondInvestId);
			return null;
		}
		if (StringUtils.isBlank(downloadFileName)) {
			LOGGER.error("downloadFileName为空:{}", downloadFileName);
			return null;
		}else{
			downloadFileName = URLDecoder.decode(downloadFileName,"UTF-8");
		}
		
		//解析图片真实路径
		String path = getFilePdfBondPath("bondContract",bondId,bondInvestId, getFileExt(downloadFileName));
		LOGGER.info("path:{}",path);
		
		StringBuffer sb = new StringBuffer();
		sb.append(tokenCode).append(bondId).append(bondInvestId).append(tokenCode);
		if (StringUtils.isBlank(token)||!token.equals(getMd5Str(sb.toString()))) {
			LOGGER.error("token为空或者不正确,{}" ,token);
			return null;
		}

		//读取文件相对路径
		String contextPath = request.getSession().getServletContext().getRealPath("/");
		String url = contextPath + path;
		File file = new File(url);
		//如果图片不存在 返回Null
		if(!file.exists()){
			LOGGER.error("file不存在,{}" ,file.getAbsolutePath());
			StringBuilder urlSb = new StringBuilder(webUrl);
			try {
				urlSb.append("/app/open/myBond/buildBondProtocol.html").
				append("?bondId=").append(bondId).
				append("&bondInvestId=").append(bondInvestId).
				append("&User-Agent=micromessenger").
				append("&type=").append(url.contains("zip")?"zip":"pdf");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				return null;
			}
			response.sendRedirect(urlSb.toString());
			return null;
		}
		
		if (url.toLowerCase().endsWith(".pdf")) {
			LOGGER.debug("pdf下载,{}",url);
			InputStream ins = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName, "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			ous.write(buffer);
			ous.flush();
			ous.close();
		}
		if (url.toLowerCase().endsWith(".zip")) {
			LOGGER.debug("zip下载,{}",url);
			InputStream ins = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName, "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/x-download");
			ous.write(buffer);
			ous.flush();
			ous.close();
		}
		return null;
	}
	/**
	 * 判断投资合同是否存在
	 * @author QianPengZhan
	 * @date 2016年11月5日
	 * @param projectId
	 * @param investId
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/protocol/judgeProjectProtocolFileIsExists")
	public String judgeProjectProtocolFileIsExists(HttpServletRequest request, HttpServletResponse response){
		final String projectId = request.getParameter("projectId");
		final String investId = request.getParameter("investId");
		final String type = request.getParameter("type");
		final String path = getFilePdfPath("projectContract",projectId,investId,type);
		LOGGER.info("path:{}" ,path);
		//读取文件相对路径
		final String contextPath = request.getSession().getServletContext().getRealPath("/");
		final String url = contextPath + path;
		final File file = new File(url);
		if(!file.exists()){
			LOGGER.info("file不存在,{}" ,file.getAbsolutePath());
			response.setStatus(HttpServletResponse.SC_OK);
		}else{
			response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: downloadContract 
	 * @Description: 下载文件
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws IOException 
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/upload/downloadContract")
	public String downloadContract(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String projectId = request.getParameter("projectId");
		String investId = request.getParameter("investId");
		String token = request.getParameter("token");
		String downloadFileName = request.getParameter("fileName");
		String webUrl = request.getParameter("webUrl");
		LOGGER.info("downloadPdf,downloadFileName:{},projectId:{},investId:{},webUrl:{}" ,downloadFileName,projectId,investId,webUrl);
		

		if (StringUtils.isBlank(projectId)) {
			LOGGER.error("projectId为空,{}",projectId);
			return null;
		}
		if (StringUtils.isBlank(investId)) {
			LOGGER.error("investId为空,{}",investId);
			return null;
		}
		if (StringUtils.isBlank(downloadFileName)) {
			LOGGER.error("downloadFileName为空:{}", downloadFileName);
			return null;
		}else{
			downloadFileName = URLDecoder.decode(downloadFileName,"UTF-8");
		}
		
		//解析图片真实路径
		String path = getFilePdfPath("projectContract",projectId,investId, getFileExt(downloadFileName));
		
		StringBuffer sb = new StringBuffer();
		sb.append(tokenCode).append(projectId).append(investId).append(tokenCode);
		if (StringUtils.isBlank(token)||!token.equals(getMd5Str(sb.toString()))) {
			LOGGER.error("token为空或者不正确,{}" ,token);
			return null;
		}

		//读取文件相对路径
		String contextPath = request.getSession().getServletContext().getRealPath("/");
		String url = contextPath + path;
		File file = new File(url);
		//如果图片不存在 返回Null
		if(!file.exists()){
			LOGGER.error("file不存在,{}" ,file.getAbsolutePath());
			StringBuilder urlSb = new StringBuilder(webUrl);
			try {
				urlSb.append("/member/myInvest/buildProtocolAndDownload.html").
				append("?projectId=").append(projectId).
				append("&projectInvestId=").append(investId).
				append("&type=").append(url.contains("zip")?"zip":"pdf");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				return null;
			}
			response.sendRedirect(urlSb.toString());
			return null;
		}
		
		if (url.toLowerCase().endsWith(".pdf")) {
			LOGGER.debug("pdf下载");
			InputStream ins = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName, "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			ous.write(buffer);
			ous.flush();
			ous.close();
		}
		if (url.toLowerCase().endsWith(".zip")) {
			LOGGER.debug("zip下载");
			InputStream ins = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName, "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/x-download");
			ous.write(buffer);
			ous.flush();
			ous.close();
		}
		return null;
	}
	/**
	 * 
	 * @Title: downloadContract 
	 * @Description: 下载文件
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @param @throws IOException 
	 * @return String
	 * @throws
	 */
	@RequestMapping(value = "/upload/downloadContractMobile")
	public String downloadContractMobile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String projectId = request.getParameter("projectId");
		String investId = request.getParameter("investId");
		String token = request.getParameter("token");
		String downloadFileName = request.getParameter("fileName");
		String webUrl = request.getParameter("webUrl");
		LOGGER.info("downloadPdf,downloadFileName:{},projectId:{},investId:{},webUrl:{}" ,downloadFileName,projectId,investId,webUrl);
		

		if (StringUtils.isBlank(projectId)) {
			LOGGER.error("projectId为空,{}",projectId);
			return null;
		}
		if (StringUtils.isBlank(investId)) {
			LOGGER.error("investId为空,{}",investId);
			return null;
		}
		if (StringUtils.isBlank(downloadFileName)) {
			LOGGER.error("downloadFileName为空:{}", downloadFileName);
			return null;
		}else{
			downloadFileName = URLDecoder.decode(downloadFileName,"UTF-8");
		}
		
		//解析图片真实路径
		String path = getFilePdfPath("projectContract",projectId,investId, getFileExt(downloadFileName));
		
		StringBuffer sb = new StringBuffer();
		sb.append(tokenCode).append(projectId).append(investId).append(tokenCode);
		if (StringUtils.isBlank(token)||!token.equals(getMd5Str(sb.toString()))) {
			LOGGER.error("token为空或者不正确,{}" ,token);
			return null;
		}

		//读取文件相对路径
		String contextPath = request.getSession().getServletContext().getRealPath("/");
		String url = contextPath + path;
		File file = new File(url);
		//如果图片不存在 返回Null
		if(!file.exists()){
			LOGGER.error("file不存在,{}" ,file.getAbsolutePath());
			StringBuilder urlSb = new StringBuilder(webUrl);
			try {
				urlSb.append("/app/open/myInvest/buildProtocolAndDownload.html").
				append("?projectId=").append(projectId).
				append("&projectInvestId=").append(investId).
				append("&User-Agent=micromessenger").
				append("&type=").append(url.contains("zip")?"zip":"pdf");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				return null;
			}
			response.sendRedirect(urlSb.toString());
			return null;
		}
		
		if (url.toLowerCase().endsWith(".pdf")) {
			LOGGER.debug("pdf下载");
			InputStream ins = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName, "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			ous.write(buffer);
			ous.flush();
			ous.close();
		}
		if (url.toLowerCase().endsWith(".zip")) {
			LOGGER.debug("zip下载");
			InputStream ins = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadFileName, "utf-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/x-download");
			ous.write(buffer);
			ous.flush();
			ous.close();
		}
		return null;
	}
	
	public String getFilePdfBondPath(String nid, String bondId, String bondInvestId,String contractType){
		if (StringUtils.isBlank(nid)) {
			LOGGER.error("nid不存在,{}",nid);
			return StringUtils.EMPTY;
		}
		if(StringUtils.isBlank(bondId)){
			LOGGER.error("bondId不存在,{}",bondId);
			return StringUtils.EMPTY;
		}
		if(StringUtils.isBlank(bondInvestId)){
			LOGGER.error("bondInvestId不存在,{}",bondInvestId);
			return StringUtils.EMPTY;
		}
		if(StringUtils.isBlank(contractType)){
			LOGGER.error("contractType不存在,{}",contractType);
			return StringUtils.EMPTY;
		}
		String result = new StringBuffer(getFilePath(nid))
			.append(bondId).append(separator).append(bondInvestId).append(".").append(contractType).toString();
		return result;
	}
	
	
 	public String getFilePdfPath(String nid, String projectId, String investId,String contractType){
		if (StringUtils.isBlank(nid)) {
			LOGGER.error("nid不存在,{}",nid);
			return StringUtils.EMPTY;
		}
		if(StringUtils.isBlank(projectId)){
			LOGGER.error("projectId不存在,{}",projectId);
			return StringUtils.EMPTY;
		}
		if(StringUtils.isBlank(investId)){
			LOGGER.error("investId不存在,{}",investId);
			return StringUtils.EMPTY;
		}
		if(StringUtils.isBlank(contractType)){
			LOGGER.error("contractType不存在,{}",contractType);
			return StringUtils.EMPTY;
		}
		String result = new StringBuffer(getFilePath(nid))
			.append(projectId).append(separator).append(investId).append(".").append(contractType).toString();
		return result;
	}
 
 	public void zipFiles(List<File> srcfile, File zipfile) {    
		LOGGER.info("zipFiles start");
		if(CollectionUtils.isEmpty(srcfile)){
			LOGGER.error("srcfile为空");
			return;
		}
		if(zipfile == null){
			LOGGER.error("zipfile为空");
			return;
		}
        byte[] buf = new byte[1024];    
        try {    
            // Create the ZIP file    
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));    
            // Compress the files    
            for (int i = 0; i < srcfile.size(); i++) {    
                File file = srcfile.get(i);    
                FileInputStream in = new FileInputStream(file);    
                // Add ZIP entry to output stream.    
                out.putNextEntry(new ZipEntry(file.getName()));    
                // Transfer bytes from the file to the ZIP file    
                int len;    
                while ((len = in.read(buf)) > 0) {    
                    out.write(buf, 0, len);    
                }    
                // Complete the entry    
                out.closeEntry();    
                in.close();    
            }    
            // Complete the ZIP file    
            out.close();   
        } catch (IOException e) {    
    		LOGGER.info("zipFiles error:{}",e.getMessage());
        }

		LOGGER.info("zipFiles end");
    }    
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
}
class MyFilter implements FilenameFilter{
	private String type;
	public MyFilter(String type){
		this.type = type;
	}
	public boolean accept(File dir,String name){
		return name.endsWith(type);
	}
}