<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
</head>
<body>
	<shiro:guest> hello guest!</shiro:guest>
	<shiro:hasRole name="admin">hello role admin!</shiro:hasRole>
	<shiro:principal></shiro:principal>
</body>
</html>
