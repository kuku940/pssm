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
		<c:forEach items="${page.list }" var="item">
			<a href="${item.url}">${item.title}_${item.vote}</a><br/>
		</c:forEach>
		<a href="zhihu/list?pageIndex=${page.prePage}">上一页</a>

		<a href="zhihu/list?pageIndex=${page.nextPage}">下一页</a>
		<a href="zhihu/list?pageIndex=${page.pages}">${page.pages}</a>

	</body>
</html>