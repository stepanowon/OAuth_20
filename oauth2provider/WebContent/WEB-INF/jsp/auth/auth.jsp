<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="net.oauth.v2.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.multi.oauth2.provider.vo.*" %>
<%
	boolean isloggined = (Boolean)request.getAttribute("isloginned");
	RequestAuthVO rVO = (RequestAuthVO)request.getAttribute("requestAuthVO");
	ClientVO cVO = (ClientVO)request.getAttribute("clientVO");
	//csv를 배열로...
	String[] scopes = rVO.getScope().split(",");
	String currentUrl = "auth?" + request.getQueryString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Authorization</title>
<script type="text/javascript">
function allow() {
	document.getElementById("f1").submit();
}

function deny() {
	document.getElementById("isallow").value = "false";
	document.getElementById("f1").submit();
}
</script>
</head>
<body>
	<h1>Consumer 승인 페이지</h1>
	<hr>
		<b>승인하려는 클라이언트 : <%=cVO.getClient_name() %></b>
	<hr>
	<h3>사용하려는 권한</h3>
	<ul>
<% for (int i=0; i < scopes.length; i++) { %>
		<li><%=OAuth2Scope.getScopeMsg(scopes[i]) %></li>		
<% } %>
	</ul>
	<h1>위 권한을 승인하시겠습니까?</h1>
	<hr>
	<form id="f1" method="post" action="<%=currentUrl %>">
		<input type="hidden" id="isallow" name="isallow" value="true" />
<% if (!isloggined) { 	%>
		계정 : <input type="text" id="userid" name="userid" /><br>
		암호 : <input type="text" id="password" name="password" /><br>
<% } else { %>
		<h3>이미 로그인하셨습니다. 승인하시겠습니까?</h3>
<% } %>
		<input type="button" value="승인" onclick="allow()" />
		<input type="button" value="거부" onclick="deny()" />
	</form>
</body>
</html>