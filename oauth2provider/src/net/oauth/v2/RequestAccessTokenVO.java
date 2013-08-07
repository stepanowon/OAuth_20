package net.oauth.v2;

public class RequestAccessTokenVO extends RequestBaseVO {
	private String grant_type;
	private String code;
	private String username;
	private String password;
	private String state;
	private String refresh_token;		//refresh token 시에만 사용
	
	public RequestAccessTokenVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RequestAccessTokenVO(String grant_type, String code,
			String redirect_uri, String client_id, String client_secret, 
			String username, String password, String state) {
		super(client_id, client_secret, redirect_uri);
		this.grant_type = grant_type;
		this.code = code;
		this.username = username;
		this.password = password;
		this.state = state;
		
	}
	public String getGrant_type() {
		return grant_type;
	}
	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	
	@Override
	public String toString() {
		return this.toString(OAuth2Constant.AUTHORIZATION_CODE, false);
	}
	
	public String toString(String response_type, boolean isUseAuthorizationHeader) {
		String val = "grant_type=" + OAuth2Util.encodeURIComponent(grant_type) + 
				"&code="+ OAuth2Util.encodeURIComponent(code) + 
				"&redirect_uri=" + OAuth2Util.encodeURIComponent(this.getRedirect_uri());
		if (!isUseAuthorizationHeader) {
			val += "&client_id=" + OAuth2Util.encodeURIComponent(this.getClient_id());
			if (response_type.equals(OAuth2Constant.AUTHORIZATION_CODE)) {
				val += "&client_secret=" + OAuth2Util.encodeURIComponent(this.getClient_secret());
			}
		}
		
		return val;
	}
	
	//Oauth2 Client에서 사용하는 메서드 
	public String createBasicAuthorizationHeader() {
		return OAuth2Util.generateBasicAuthHeaderString(this);
	}
	
}
