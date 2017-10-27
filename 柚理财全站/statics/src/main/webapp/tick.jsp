<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tick OF STATICS</title>
</head>
<body>
<!-- 用于监测web服务器运行情况 -->
<div id="time"></div>
<script type="text/javascript">
document.getElementById('time').innerHTML = new Date().toLocaleString();     
setInterval("document.getElementById('time').innerHTML=new Date().toLocaleString();",1000);  
</script>
</body>
</html>