<%@ page language="java" contentType="application/json; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.multi.oauth2client.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.httpclient.*" %>
<%@ page import="org.apache.commons.httpclient.methods.*"%>
<%
	/*
	--토큰을 갱신하는 방법
	1. 리소스 서버로의 요청시에 세션등에 저장하고 있는 token(AccessTokenVO) 정보를 확인한다.
	2. token의 expires_in * 1000 + created_at < curretTimestamp 라면
	3. refresh token 요청 과정을 거쳐야 한다.
	4.   서버쪽의 OAuth2Constant.USE_REFRESH_TOKEN 을 true로 변경하고(기본:false)
	5. 클라이언트에서는 token?grant_type=refresh_token 으로 지정하여 재요청함.
	6. 이때 보유중인 refresh_token, client_id, client_secret, state 값을 함께 전송해야 함.
	7. 너무 오랫동안 갱신되지 않은 토큰은 자동 제거되도록 프로그래밍해야 함.
	8. 갱신하려는 토큰이 이미 제거된 경우에는 invalid token을 리턴하고
	9. 클라이언트는 다시 토큰을 재발급받아야 함.
	*/
	if (session.getAttribute("access_token") == null)
		response.sendRedirect("index.jsp");

	AccessTokenVO tokenVO = (AccessTokenVO)session.getAttribute("access_token");
	String access_token = tokenVO.getAccess_token();
	HashMap<String, String> map = new HashMap<String,String>();
	map.put("output", "json");
	String url = Settings.PERSONAL_INFO_URL + "?" + Settings.getParamString(map, false);
	
	HttpClient client = new HttpClient();
	client.getParams().setContentCharset("utf-8");
	GetMethod method = new GetMethod(url);
	method.addRequestHeader("Authorization", "Bearer " + access_token);
	int status = client.executeMethod(method);
	String result = "";
	
	if (status == 200) {
		result = method.getResponseBodyAsString();	
	} else {
		result = method.getResponseBodyAsString();
	}
%><%=result %>