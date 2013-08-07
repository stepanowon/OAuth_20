<%@ page language="java" contentType="text/plain; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="net.oauth.v2.*" %><% 
	OAuth2Exception ex = (OAuth2Exception)request.getAttribute("oauth2Exception");
	String[] temp = ex.getMessage().split(":");
	int status = Integer.parseInt(temp[0]);
	response.setStatus(status, temp[1]);
%>{
    "error" : {
       "type" : "OAuth2Exception",
       "message" : "<%=temp[1] %>"
    }
}
