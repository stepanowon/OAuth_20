<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="net.oauth.v2.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<style>
	input[type='text'] {
		width:400px;
	}
</style>
</head>
<body>
	<h1>Register Client Application</h1>
	<hr>
	<form method="post" action="insertclient.do">
		Client Name : <input type="text" name="client_name" value="TestApp1"/><br>
		Description : <input type="text" name="description" value="TestApp1 입니다."/><br>
		Client Type : 
			Web Server<input type="radio" name="client_type" value="W" checked />&nbsp;
			Mobile App<input type="radio" name="client_type" value="M" />&nbsp;
			User Agent(Web App)<input type="radio" name="client_type" value="U" />&nbsp;
			Desktop App<input type="radio" name="client_type" value="D" />&nbsp;<br>
		Client URL : <input type="text" name="client_url" value="http://localhost:8000" /><br>
		Redirect URI : <input type="text" name="redirect_uri" value="http://localhost:8000/oauth2client/callback"/><br>
		Scope : 
		<div style="padding-left: 30px; ">
<%	for (String key : OAuth2Scope.scopeMsgMap.keySet()) {	%>
		- <input type="checkbox" name="scopes" value="<%=key %>"><%=OAuth2Scope.scopeMsgMap.get(key) %><br>
<%	} %>
		</div>
		<br><br>
		<input type="submit" value="Add Client" />
	</form>
</body>
</html>
