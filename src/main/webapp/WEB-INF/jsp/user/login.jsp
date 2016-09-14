<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<base href="<%= basePath%>">
		<title>登陆页面</title>
	</head>
	<body>
		<form action="user/login" method="post">
			<table align="center" border="1">
				<tr><th colspan="2">登陆页面</th></tr>
				<tr><td>邮箱</td><td><input type="text" id="email" name="email" value="${user.email }"/></td></tr>
				<tr><td>密码</td><td><input type="password" id="password" name="password" value="${user.password }"/></td></tr>
				<tr><td>验证码</td><td><input type="text" id="captcha" name="captcha" /><img src="captcha.gif" height="20px" /></td></tr>
				<tr><td colspan="2"><input type="submit" value="登陆"/></td></tr>
				<tr><td colspan="2">${error }&nbsp;</td></tr>
			</table>
		</form>
	</body>
</html>