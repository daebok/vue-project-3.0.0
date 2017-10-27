<%@page import="com.rd.ifaes.common.web.ServletUtils"%>
<%@page import="com.rd.ifaes.common.util.ExceptionUtils"%>
<%@page import="com.rd.ifaes.common.util.StringUtils"%>
<%@page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%
response.setStatus(500);

// 获取异常类
Throwable ex = ExceptionUtils.getThrowable(request);
if (ex != null){
	LoggerFactory.getLogger("500.jsp").error(ex.getMessage(), ex);
}

// 编译错误信息
//StringBuilder sb = new StringBuilder("错误信息：\n");
StringBuilder sb = new StringBuilder("\n");
if (ex != null) {
	sb.append(ExceptionUtils.getStackTraceAsString(ex));
} else {
	sb.append("未知错误.\n\n");
}

// 如果是异步请求或是手机端，则直接返回信息
if (ServletUtils.isAjaxRequest(request)) {
	out.print(sb);
}

// 输出异常信息页面
else {
%>
 
<!DOCTYPE html>
<html>
<head>
	<title>500 - 系统内部错误</title>
</head>
<body>
	<div class="container-fluid">
		<div class="page-header"><h1>系统内部错误.</h1></div>
		<div class="errorMessage">
			错误信息：<%=ex==null?"未知错误.":StringUtils.toHtml(ex.getMessage())%> <br/> <br/>
			请点击“查看详细信息”按钮，将详细错误信息发送给系统管理员，谢谢！<br/> <br/>
			<a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a> &nbsp;
			<a href="javascript:" onclick="$('.errorMessage').toggle();" class="btn">查看详细信息</a>
		</div>
		<div class="errorMessage hide">
			<%=StringUtils.toHtml(sb.toString())%> <br/>
			<a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a> &nbsp;
			<a href="javascript:" onclick="$('.errorMessage').toggle();" class="btn">隐藏详细信息</a>
			<br/> <br/>
		</div>
	</div>
</body>
</html>
<%
} out = pageContext.pushBody();
%>