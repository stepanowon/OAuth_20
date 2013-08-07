<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.multi.oauth2.provider.vo.*" %>
<%
	ClientVO vo = (ClientVO)request.getAttribute("clientVO");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript">
window.onload = function() {
	document.getElementById("golist").onclick = function() {
		location.href="clientlist.do";
	};
};
</script>
</head>
<body>
	<h1>Client Detail</h1><hr>
	<div>
		Client ID : <%=vo.getClient_id() %><br>
		Client Secret : <%=vo.getClient_secret() %><br>
		Client Name : <%=vo.getClient_name() %><br>
		Description : <%=vo.getDescription() %><br>
		Client Type : <%=vo.getClient_type() %><br>
		Client URL : <%=vo.getClient_url() %><br>
		Redirect URI : <%=vo.getRedirect_uri() %><br>
		Scope : <%=vo.getScope() %><br>
		Registration Date : <%=vo.getRegdate() %><br><br>
	</div>
	<input type="button" id="golist" value="Go Client List">
</body>
</html>