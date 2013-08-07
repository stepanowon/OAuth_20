<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String contenttype = (String)request.getAttribute("contenttype");
	String restdata = (String)request.getAttribute("restdata");
	response.setContentType(contenttype + "; charset=utf-8");
%><%=restdata %>
