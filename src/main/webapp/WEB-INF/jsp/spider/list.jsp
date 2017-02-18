<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<base href="<%= basePath%>">
		<title>列表界面</title>
	</head>
	<body>
		<a href="/spider/touch/zhihu">爬取知乎</a><br/>
		<a href="/spider/touch/zreading">爬取左岸读书</a><br/>
		<a href="/spider/touch/music163">爬取网易云音乐</a><br/>
	</body>
</html>