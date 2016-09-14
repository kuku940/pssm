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
		<title>注册页面</title>
	</head>
	<body>
		<form action="user/regist" method="post">
			<table align="center" border="1">
				<tr><th colspan="2">注册页面</th></tr>
				<tr><td>用户名</td><td><input type="text" id="username" name="username" value="${user.username }"/></td></tr>
				<tr><td>邮箱</td><td><input type="text" id="email" name="email" value="${user.email }"/></td></tr>
				<!-- spring:eval 格式化日期的输出 -->
				<tr><td>生日</td><td><input type="text" id="birth" name="birth" value="<spring:eval expression="user.birth" />"/></td></tr>
				<tr><td>密码</td><td><input type="password" id="password" name="password" /></td></tr>
				<tr><td>确认密码</td><td><input type="password" id="confirmpass" name="confirmpass"/></td></tr>
				<tr><td colspan="2"><input type="submit" value="注册"/></td></tr>
				<tr><td colspan="2">${error }&nbsp;</td></tr>
			</table>
		</form>
	</body>
</html>