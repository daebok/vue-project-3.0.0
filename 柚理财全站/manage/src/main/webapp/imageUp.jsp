    <%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
    <%@ page import="java.io.File,com.rd.ifaes.manage.web.listener.UploadController" %>
    <%
    	request.setCharacterEncoding("utf-8");
    	response.setCharacterEncoding("utf-8");
    	UploadController  up = new UploadController();
        up.setSavePath("/data/upfiles");
        String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp" , "pdf"};
        up.setAllowFiles(fileType);
        up.setMaxSize(1024); //单位KB
        up.upload(request);
        String ret="{'original':'"+up.getOriginalName()+"','url':'"+up.getUrl()+"','title':'"+up.getTitle()+"','tmpId':'"+up.getTmpId()+"','state':'"+up.getState()+"'}";
        response.getWriter().print(ret);
    %>
