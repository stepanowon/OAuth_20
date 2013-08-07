<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="com.multi.oauth2client.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.commons.httpclient.*"%>
<%@ page import="org.apache.commons.httpclient.methods.*"%>
<%
	//http://localhost:8000/tistory/callback.jsp?code=f89e9f48f02ffb80b83f33c5970b9557625ab1c77bf45fbdd3304f04734a4770716df952
 	//code 파라미터는 verification 값임. 사용자가 승인했음을 나타냄.
	String code = request.getParameter("code");
	String state = request.getParameter("state");
	String prevState = (String)session.getAttribute("state");
	
	String result = "";
	System.out.println(state + " , " + prevState);
	if (!state.equals(prevState)) {
		result = "CSRF(Cross Site Request Forgery 공격이 의심됨";
	} else {

		HashMap<String, String> map = new HashMap<String,String>();
		map.put("client_id", Settings.CLIENT_ID);
		map.put("client_secret", Settings.CLIENT_KEY);
		map.put("redirect_uri", Settings.REDIRECT_URI);
		map.put("grant_type", "authorization_code");
		map.put("code", code);
		map.put("state", OAuth2ClientUtil.generateRandomState());
		
		HttpClient client = new HttpClient();
		client.getParams().setContentCharset("utf-8");
		
		//POST 일때는 client와 clientsecret은 파라미터로 전달 가능
		//String url = Settings.ACCES_TOKEN_URL;
		//PostMethod method = new PostMethod(url);
		//method.addParameter("client_id", map.get("client_id"));
		//method.addParameter("client_secret", map.get("client_secret"));
		//method.addParameter("redirect_uri", map.get("redirect_uri"));
		//method.addParameter("grant_type", map.get("grant_type"));
		//method.addParameter("code", map.get("code"));
		
		//GET 일때는 client_id, client_secret 은 Header로 Basic 인증값으로 전달해야 함.
		String url = Settings.ACCES_TOKEN_URL + "?" + Settings.getParamString(map, true);
		GetMethod method = new GetMethod(url);
		String authHeader  = OAuth2ClientUtil.generateBasicAuthHeaderString(
				Settings.CLIENT_ID, Settings.CLIENT_KEY);
		method.addRequestHeader("Authorization", authHeader);
		
		int status = client.executeMethod(method);
		
		if (status == 200) {
			String body = method.getResponseBodyAsString();
			System.out.println(body);
			AccessTokenVO token = OAuth2ClientUtil.getObjectFromJSON(body, AccessTokenVO.class);
			if (token.getState() == null || !token.getState().equals(map.get("state"))) {
				System.out.println("@@@ state 값이 일치하지 않음. CSRF가 의심됨");
			}
			session.setAttribute("access_token", token);
			response.sendRedirect("main.jsp");
		} else {
			result = "인증 실패!!";
		}
	}
%>
<%=result %>
