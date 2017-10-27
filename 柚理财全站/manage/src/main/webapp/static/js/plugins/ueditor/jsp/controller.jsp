<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.io.File,com.rd.ifaes.manage.web.listener.UploadController,com.rd.ifaes.core.core.util.ConfigUtils,com.rd.ifaes.core.core.constant.ConfigConstant"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%
   	request.setCharacterEncoding("utf-8");
   	response.setCharacterEncoding("utf-8");
   	if(!request.getParameter( "action" ).equals("config")){
	   	UploadController  up = new UploadController();
	    up.setSavePath("/data/upfiles");
	    String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp" , "pdf"};
	    up.setAllowFiles(fileType);
	    up.setMaxSize(2048000); //单位KB
	    up.upload(request);
	    String ret="{'state':'"+up.getState()+"','title':'"+up.getTitle()+"','original':'','type':'."+up.getType()+"','url':'"+up.getUrl()+"','size':''}";
	    System.out.print(ret);
	    out.write( ret );
	}else{
		String imageUrl = ConfigUtils.getValue(ConfigConstant.IMAGE_SERVER_URL);
   		out.write( "{'imageMaxSize':2048000,'imagePathFormat':'/data/upfiles','imageCompressBorder':1600,'imageCompressEnable':true,'imageActionName':'uploadimage','imageInsertAlign':'none','imageUrlPrefix':'"+imageUrl+"','imageAllowFiles':['.png','.jpg','.jpeg','.gif','.bmp','pdf']}");
   	}
	
%>